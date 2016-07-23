package output.orm;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.IOUtils;

import model.structure.Embeddable;
import model.structure.Entity;
import model.structure.MappedSuperclass;
import model.structure.Package;

public class AnnotationCleaner {
	private final static String REGEX_PATTERN_TWO_NESTED_LEVELS = 
			"(\\(([^\\(\\)]|\\s|\\)\"|\".*\"|\\(([^\\(\\)]|\\s|\\)\"|\".*\")*\\))*\\))?(\\s)*";
	private final static String REGEX_PATTERN_ONE_NESTED_LEVEL = 
			"(\\(([^)]|\\s|\\)\")*\\))?(\\s)*";
	private final static String REGEX_PATTERN_IMPORT = 
			"import javax\\.persistence\\..*;(\\s)*";
	private final static String REGEX_IMPORT_JPA = 
			"import javax.persistence.(.*?);";
	private final static String REGEX_PATTERN_PACKAGE = "^package .*(?=;)";
	private Set<String> firstOrderAnnotations = new HashSet<>();
	private Set<String> secondOrderAnnotations = new HashSet<>();
	
	/**
	 * Creates the new directory and the new java files, removing the persistence annotations.
	 * @param rootPackage - The intermediate model root package
	 * @param outputPath - The path of the output directory
	 * @return True
	 * @throws IOException
	 */
	public boolean clean(Package rootPackage, String outputPath) throws IOException {
		if (rootPackage == null || rootPackage.isEmpty()) {
			throw new IllegalArgumentException("The package cannot be null or empty");
		}
		if (outputPath == null || outputPath.isEmpty()) {
			throw new IllegalArgumentException("Output path cannot be null or empty");
		}
		File directory = createOutputDirectory(outputPath);
		cleanFiles(rootPackage, directory);
		return true;
	}
	
	/**
	 * Creates the output directory if such directory did not exist before.
	 * @param outputPath - The path of the output directory
	 * @return - The new directory
	 */
	private File createOutputDirectory(String outputPath){
		File directory = new File(outputPath + "/clean/");
		directory.mkdirs();
		return directory;
	}
	
	/**
	 * Traverses recursively the package structure in the intermediate model 
	 * loaded, creating new files for the entities, embeddable, and mapped 
	 * superclasses, and cleaning any persistence annotation from them.
	 * @param packg - Intermediate model package
	 * @param directory - The output directory
	 * @throws IOException
	 */
	private void cleanFiles(Package packg, File directory) throws IOException{
		for (Entity entity : packg.getEntities()) {
			createCleanFile(entity.getEntityContent(), entity.getClazz(), 
					directory);
		}
		for (Embeddable embeddable : packg.getValueObjects()) {
			createCleanFile(embeddable.getEmbeddableContent(), 
					embeddable.getClazz(), directory);
		}
		for (MappedSuperclass mappedSuperclass : packg.getMappedSuperclasses()) {
			createCleanFile(mappedSuperclass.getMappedSuperclassContent(), 
					mappedSuperclass.getClazz(), directory);
		}
		for (Package subPackage : packg.getChildrenPackages()) {
			cleanFiles(subPackage, directory);
		}
	}
	
	/**
	 * Takes the textual content and removes the annotation, which is not 
	 * contained in another annotation.
	 * @param annotation - The annotation to be removed
	 * @param content - The original source code content
	 * @return - The new source code content
	 */
	private String removeFirstOrderAnnotation(String annotation, String content) {
		String[] annotationsParts = annotation.split("\\.");
		String annotationName = annotationsParts[annotationsParts.length - 1];
		String regex = "@" + annotationName + REGEX_PATTERN_TWO_NESTED_LEVELS;
		Matcher m = Pattern.compile(regex).matcher(content);
		content = m.replaceAll("");
		return content;
	}
	
	/**
	 * Takes the textual content and removes the annotation, which is nested 
	 * inside another annotation.
	 * @param annotation - The annotation to be removed
	 * @param content - The original source code content
	 * @return - The new source code content
	 */
	private String removeSecondOrderAnnotation(String annotation, String content) {
		String[] annotationsParts = annotation.split("\\.");
		String annotationName = annotationsParts[annotationsParts.length - 1];
		String regex = "@" + annotationName + REGEX_PATTERN_ONE_NESTED_LEVEL;
		return content.replaceAll(regex, "");
	}
	
	/**
	 * Takes the content, cleans the import statements of the persistence 
	 * annotations, creates the directory and returns its path.
	 * @param content - The source code content
	 * @param directory - The output directory
	 * @return - The new source code content.
	 */
	private String getClassPackagePath(String content, File directory) {
		Matcher m = Pattern.compile(REGEX_PATTERN_PACKAGE).matcher(content);
		String filePath = directory.getAbsolutePath() + "/";
		if (m.find()) {
			String[] packageParts = m.group().split(" ");
			filePath += packageParts[1].replaceAll("\\.", "/");
		}
		new File(filePath).mkdirs();
		return filePath;
	}
	
	/**
	 * adds the persistence annotation to the list of annotations to be removed 
	 * from the source code content.
	 * @param annotation - The name of the annotation
	 */
	public void addAnnotation(String annotation){
		firstOrderAnnotations.add(annotation);
	}
	
	public AnnotationCleaner(){
		secondOrderAnnotations.add("javax.persistence.Column");
		secondOrderAnnotations.add("javax.persistence.QueryHint");
		secondOrderAnnotations.add("javax.persistence.StoredProcedureParameter");
		secondOrderAnnotations.add("javax.persistence.NamedQuery");
		secondOrderAnnotations.add("javax.persistence.NamedNativeQuery");
	}
	
	/**
	 * Takes the content, removes the persistence configurations, and creates a 
	 * new java source code file with such content in the specified directory.
	 * @param content - The source code content
	 * @param clazz - The name of the class.
	 * @param directory - The output directory
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	private void createCleanFile(String content, String clazz, File directory) 
			throws FileNotFoundException, IOException {
		if (content != null && !content.isEmpty()) {
			Matcher m = Pattern.compile(REGEX_IMPORT_JPA).matcher(content);
			while (m.find()) {
				addAnnotation(m.group(1));
			}
			for (String annotation : secondOrderAnnotations) {
				content = removeSecondOrderAnnotation(annotation, content);
			}
			for (String annotation : firstOrderAnnotations) {
				content = removeFirstOrderAnnotation(annotation, content);
			}
			content = content.replaceAll(REGEX_PATTERN_IMPORT, "");
			String filePath = getClassPackagePath(content, directory);
			String[] filePathParts = clazz.split("\\.");
			File newFile = new File(filePath + "/" + 
					filePathParts[filePathParts.length-1] + ".java");
			IOUtils.write(content, new FileOutputStream(newFile), 
					StandardCharsets.UTF_8);
		}
	}
}
