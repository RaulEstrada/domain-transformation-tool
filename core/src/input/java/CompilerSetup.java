package input.java;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.tools.DiagnosticCollector;
import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.StandardLocation;
import javax.tools.ToolProvider;

public class CompilerSetup {
	private StandardJavaFileManager fileManager;
	private JavaCompiler.CompilationTask task;
	private List<File> filesToBeCompiled = new ArrayList<>();
	private DiagnosticCollector<JavaFileObject> diagnostics;

	/**
	 * Sets up the compiler so that a later call to the compile() method can compile the 
	 * JPA source code files and process the annotation with a custom Annotation Processor
	 * @throws IOException 
	 */
	public void setUp(String sourcePath) 
			throws IOException {
		//Obtain a reference to the Java Compiler in the system.
		JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
		//The DiagnosisCollection collects diagnosis from the compilation process.
		//It can be helpful to see compilation errors.
		diagnostics = new DiagnosticCollector<JavaFileObject>();
		//In the StandardJavaFileManager the diagnosis collector and the classpath variable
		//are specified. This file manager will be later passed to the compilation task.
		fileManager = compiler.getStandardFileManager(diagnostics, null, null);
		assignClassAndOutputPath(fileManager, sourcePath);

		task = compiler.getTask(
				null, fileManager, diagnostics, createCompilerOptions(),
				null, setFilesToBeCompiled(fileManager, sourcePath));
	}
	
	private List<String> createCompilerOptions() {
		List<String> options = new ArrayList<String>();
		// We pass the -proc:only option to the compiler so that only annotation processing
		// is performed, and no subsequent compilation is done.
		//options.add("-g");
		options.add("-classpath");
		String classpath = System.getProperty("java.class.path");
		options.add(classpath);
		return options;
	}
	
	/**
	 * Compiles all the Java Source Code Files specified by the user, and the compiling
	 * lifecycle will call the custom Annotation Processor to process the different JPA
	 * annotations.
	 * @return - The result of the compiler
	 * @throws IOException
	 */
	public boolean compile() throws IOException {
		boolean success = task.call();
		fileManager.close();
		return success;
	}

	/**
	 * Adds the Hibernate JPA 2.1 jar necessary to compile the JPA annotations.
	 * Indicates the directory where the compiled .class files will be created.
	 * @param fileManager
	 * @throws IOException
	 */
	private void assignClassAndOutputPath(StandardJavaFileManager fileManager, 
			String directory) throws IOException {
		fileManager.setLocation(
				StandardLocation.CLASS_OUTPUT,
				Arrays.asList(new File(directory)));
	}

	/**
	 * Creates a compilation units. A compilation unit is a single logical unit that will be
	 * compiled. The method creates compilation units from all the .java source code files 
	 * found starting from the root directory provided, and navigating through its subdirectories
	 * @param fileManager 
	 * @param rootDirectory
	 * @return
	 */
	private Iterable<? extends JavaFileObject> setFilesToBeCompiled(
			StandardJavaFileManager fileManager, String rootDirectory) {
		List<File> filesToBeCompiled = new ArrayList<File>();
		JavaFilesCollector.gatherSourceFilesToBeCompiled(rootDirectory, 
				filesToBeCompiled);
		this.filesToBeCompiled.addAll(filesToBeCompiled);
		Iterable<? extends JavaFileObject> compilationUnits = fileManager
				.getJavaFileObjects(filesToBeCompiled.toArray(new File[0]));
		return compilationUnits;
	}
	
	public List<File> getFilesToBeCompiled() {
		return this.filesToBeCompiled;
	}
}
