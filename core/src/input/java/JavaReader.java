package input.java;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import input.Reader;
import input.ReaderFactoryImpl;
import input.classes.ClassReader;
import model.structure.Package;

public class JavaReader implements Reader {
	
	/**
	 * It accesses the directory, gathers all the java source code files, loads
	 *  them, processes them to build the intermediate model from them and 
	 *  returns its root element.
	 */
	@Override
	public Package loadModel(String directory) 
			throws ClassNotFoundException, IOException {
		CompilerSetup compiler = new CompilerSetup();
		compiler.setUp(directory);
		boolean result = compiler.compile();
		if (!result) {
			throw new RuntimeException("Compilation processed failed");
		}
		Map<String, String> fileContents = new HashMap<>();
		for (File file : compiler.getFilesToBeCompiled()){
			Path path = Paths.get(file.getAbsolutePath());
			String content = new String(Files.readAllBytes(path));
			fileContents.put(file.getCanonicalPath().replace(".java", ""), content);
		}
		ClassReader classReader = (ClassReader)ReaderFactoryImpl.getInstance()
				.getClassReader();
		classReader.setSourceContent(fileContents);
		return classReader.loadModel(directory);
	}

	/**
	 * It throws an exception/error since this loading operation is not supported
	 *  by the java loading module.
	 */
	@Override
	public Package loadModel(String host, String port, String databaseName, 
			String username, String password)
			throws SQLException {
		throw new UnsupportedOperationException("Class Reader does not support "
				+ "loading models from a URL, username and password");
	}

}
