package input.classes;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import model.associations.ManyToMany;
import model.associations.ManyToOne;
import model.associations.OneToMany;
import model.associations.OneToOne;
import model.structure.Embeddable;
import model.structure.Embedded;
import model.structure.Entity;
import model.structure.Package;

/**
 * This class is in charge of loading the .class files inside a given directory,
 *  process them and transform them into the intermediate model.
 * @author Raul Estrada
 *
 */
public class ClassReader implements input.Reader {
	/**
	 * The file extension targeted by this reader.
	 */
	private final static String CLASS_SUFFIX = ".class";
	/**
	 * Class loader. Element used to load the class from the .class file.
	 */
	private ClassLoader classLoader;
	/**
	 * Element used to analyze the different elements in the class loaded, and 
	 * create the corresponding intermediate element.
	 */
	private ClassProcessor classReader = new ClassProcessor();
	private Map<String, Entity> entityMap = new HashMap<>();
	private Map<String, Embeddable> embeddableMap = new HashMap<>();
	private Map<String, String> sourceContent = new HashMap<>();
	
	public Package loadModel(String host, String port, String databaseName, 
			String username, String password){
		throw new UnsupportedOperationException("Class Reader does not support "
				+ "loading models from a URL, username and password");
	}
		
	/**
	 * Loads the intermediary model by loading the .class files found in the 
	 * given directory and its subdirectories.
	 * @param directory - The directory to find the .class files to load.
	 * @return - An intermediary model from which different outputs can be created
	 * @throws ClassNotFoundException
	 * @throws IOException 
	 */
	public Package loadModel(String directory) 
			throws ClassNotFoundException, IOException {
		if (directory == null || directory.isEmpty()) {
			throw new IllegalArgumentException("Cannot load any model from a null"
					+ " or empty directory");
		}
		return loadFiles(directory);
	}
	
	/**
	 * Private method that actually loads the .class files and creates the package
	 *  structure by calling a recursive method.
	 * @param directory - The directory to find the .class files to load.
	 * @return - An intermediary model from which different outputs can be created
	 * @throws ClassNotFoundException
	 * @throws IOException 
	 */
	private Package loadFiles(String directory) 
			throws ClassNotFoundException, IOException {
		File file = new File(directory);
		URL url = file.toURI().toURL();
		this.classLoader = new URLClassLoader(new URL[]{url}, 
				getClass().getClassLoader());
		Package rootPackage = new Package(file.getName());
		List<Package> subpackages = loadFiles(directory, rootPackage);
		for (Package pckg : subpackages) {
			if (!pckg.isEmpty()){
				rootPackage.addChildrenPackage(pckg);
			}
		}
		this.updateAssociationEntities(rootPackage);
		return rootPackage;
	}
	
	private void updateAssociationEntities(Package pckg) {
		for (Entity entity : pckg.getEntities()) {
			for (OneToOne oneToOne : entity.getAttributes().getOneToOne()) {
				oneToOne.setEntity(this.entityMap.get(oneToOne.getTargetEntity()));
			}
			for (ManyToOne manyToOne : entity.getAttributes().getManyToOne()) {
				manyToOne.setEntity(this.entityMap.get(manyToOne.getTargetEntity()));
			}
			for (ManyToMany manyToMany : entity.getAttributes().getManyToMany()){
				manyToMany.setEntity(this.entityMap.get(manyToMany.getTargetEntity()));
			}
			for (OneToMany oneToMany : entity.getAttributes().getOneToMany()) {
				oneToMany.setEntity(this.entityMap.get(oneToMany.getTargetEntity()));
			}
			for (Embedded embedded : entity.getAttributes().getEmbedded()) {
				embedded.setEmbeddable(this.embeddableMap.get(embedded.getTypeName()));
			}
			entity.setParentEntity(this.entityMap.get(entity.getParentClassName()));
		}
		for (Package subPackage : pckg.getChildrenPackages()){
			updateAssociationEntities(subPackage);
		}
	}
	
	/**
	 * Loads the .class files in the current directory and adds them to the 
	 * current package. Then, creates subpackages from the subfolders with 
	 * .class files in them.
	 * @param currentLocation - The current folder location to scan for 
	 * .class files to load.
	 * @param currentPackage - The current package
	 * @return - The list of subpackages with .class files.
	 * @throws ClassNotFoundException
	 * @throws IOException 
	 */
	private List<Package> loadFiles(String currentLocation, Package currentPackage) {
		List<Package> packages = new ArrayList<>();
		File rootDirectory = new File(currentLocation);
		File[] filesInDirectory = rootDirectory.listFiles();
		if (filesInDirectory == null || filesInDirectory.length == 0) {
			return packages;
		}
		
		for (File file : filesInDirectory) {
			if (file.isFile() && file.getAbsolutePath().endsWith(CLASS_SUFFIX)) {
				try {
					classReader.loadElement(file, this.classLoader, 
							currentPackage, this.entityMap, 
						this.embeddableMap, 
						this.sourceContent.get(file.getCanonicalPath()
								.replace(".class", "")));
				} catch (NoClassDefFoundError ex) {
					System.out.println("Could not load class from file " + 
				file.getAbsolutePath());
				} catch (Exception ex) {
					System.out.println("Could not load file " + file.getAbsolutePath());
				}
			} else if (file.isDirectory()) {
				List<Package> subpackages = loadFiles(file.getAbsolutePath(), 
						new Package(file.getName(), currentPackage));
				for (Package pckg : subpackages) {
					if (pckg.isEmpty()) {
						currentPackage.removeChildrenPackage(pckg);
					}
				}
			}
		}
		
		return packages;
	}

	public void setSourceContent(Map<String, String> sourceContent) {
		this.sourceContent = sourceContent;
	}
}
