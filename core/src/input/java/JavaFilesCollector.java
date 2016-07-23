package input.java;

import java.io.File;
import java.util.List;

public class JavaFilesCollector {
	/**
	 * The file extension targeted by this reader.
	 */
	private final static String FILE_SUFFIX = ".java";
	
	
	/**
	 * Given a directory absolute path, it goes through all the files in that directory
	 * and, if they are .java files, they will be collected to be compiled. If the directory
	 * contains subdirectories, this method will be called recursively to check all the 
	 * subdirectories.
	 * @param directoryRoot - The current directory absolute path to scan for .java files
	 * @param files - The list of files where the .java files will be added.
	 */
	public static void gatherSourceFilesToBeCompiled(String directoryRoot, 
			List<File> files) {
		if (files == null) {
			throw new IllegalArgumentException("The list of files cannot be empty");
		}
		if (directoryRoot == null) {
			return;
		}
		File file = new File(directoryRoot);
		File[] filesInDirectory = file.listFiles();
		if (filesInDirectory == null) {
			return;
		}
		for (File fileInDirectory : filesInDirectory){
			if (fileInDirectory.isDirectory()) {
				gatherSourceFilesToBeCompiled(fileInDirectory.getAbsolutePath(),
						files);
			} else if (fileInDirectory.isFile() && 
					fileInDirectory.getName().endsWith(FILE_SUFFIX)) {
				files.add(fileInDirectory);
			}
		}
	}
}
