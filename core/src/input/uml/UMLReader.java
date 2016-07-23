package input.uml;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.common.util.EList;
import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.Enumeration;
import org.eclipse.uml2.uml.NamedElement;
import org.eclipse.uml2.uml.Type;

import input.uml.creators.ClassLikeElementsCreator;
import input.uml.exceptions.NoUMLFileException;
import input.uml.utils.FileLoader;
import input.uml.utils.UMLReaderUtils;
import model.structure.Attributes;
import model.structure.Embeddable;
import model.structure.EmbeddableAttributes;
import model.structure.Entity;
import model.structure.Package;

/**
 * This class receives the path where the UML model file is located (the.uml file), and
 * loads the model, transforming it and creating the intermediate model used by this
 * tool to produce several outputs.
 * @author Raul Estrada
 *
 */
public class UMLReader implements input.Reader {
	/**
	 * Map that relates UML types to Entities from the interemdiate model
	 */
	private Map<Type, Entity> typeEntityMap = new HashMap<>();
	/**
	 * Map that relates UML types to Embeddable (or value objects) from the 
	 * intermediate model
	 */
	private Map<Type, Embeddable> typeEmbeddable = new HashMap<>();
	/**
	 * Map that relates UML types to Enumerations from the intermediate model
	 */
	private Map<Type, model.structure.Enumeration> typeEnum = new HashMap<>();
	/**
	 * Utils object that provides some utility functions such as creating a 
	 * column for a property and getting the name of the type of the property
	 */
	private UMLReaderUtils utils;
	/**
	 * Creates entities and embeddable elements.
	 */
	private ClassLikeElementsCreator classLikeCreator;
	
	public Package loadModel(String host, String port, String databaseName, 
			String username, String password){
		throw new UnsupportedOperationException("UML Reader does not support "
				+ "loading models from a URL, username and password");
	}
	
	/**
	 * Given a path with a .uml file inside its tree folder structure, it loads the UML model
	 * from that file, creates the intermediate model from the UML one, and returns the root package.
	 * @param path - The directory path containing the .uml file with the UML model.
	 * @return - The root package in the intermediate model.
	 * @throws NoUMLFileException 
	 */
	public Package loadModel(String path) throws NoUMLFileException {
		if (path == null || path.isEmpty()) {
			throw new IllegalArgumentException("Cannot load any model from a "
					+ "null or empty path");
		}
		List<NamedElement> rootPackages = new FileLoader().loadRootPackages(path);
		if (!rootPackages.isEmpty()) {
			createEntities(rootPackages.get(0));
			this.classLikeCreator = new ClassLikeElementsCreator(typeEmbeddable, 
					typeEntityMap, getUtils(), typeEnum);
			Package rootPackage = 
					createIntermediateModel(rootPackages.get(0), null);
			return rootPackage;
		} else {
			return null;
		}
	}

	/**
	 * Creates the outer structure of the intermediate elements, that is, Embeddable, Entities and Enumeration
	 * instances but without processing their inner elements yet. Once created, they are added to their corresponding
	 * map so that later on the tool can create the inner elements and assocations without any problem.
	 * @param namedElementPackage - The root named element from the UML model loaded, and in the uml2 model.
	 */
	private void createEntities(NamedElement namedElementPackage) {
		if (namedElementPackage == null) {
			throw new IllegalArgumentException("Cannot create a package structure"
					+ " if the root UML named element is missing");
		}
		for (Element element : namedElementPackage.getOwnedElements()) {
			if (element instanceof org.eclipse.uml2.uml.Package) {
				createEntities((NamedElement)element);
			} else if (element instanceof Class) {
				Class umlClass = (Class) element;
				if (getUtils().containsStereotype(umlClass, "valueObject")) {
					processEmbeddable(umlClass);
				} else {
					processEntity(umlClass);
				}
			} else if (element instanceof Enumeration) {
				processEnum(element);
			}
		}
		for (Type umlClass : this.typeEntityMap.keySet()) {
			Entity entity = this.typeEntityMap.get(umlClass);
			EList<Class> superClasses = ((Class)umlClass).getSuperClasses();
			if (!superClasses.isEmpty() && typeEntityMap.containsKey(superClasses.get(0))) {
				entity.setParentEntity(this.typeEntityMap.get(superClasses.get(0)));
			}
		}
	}
	
	private void processEnum(Element element) {
		Enumeration umlEnum = (Enumeration)element;
		Object[] literals = new Object[umlEnum.getOwnedLiterals().size()];
		for (int i = 0; i < literals.length; i++) {
			literals[i] = umlEnum.getOwnedLiterals().get(i).getName();
		}
		model.structure.Enumeration modelEnumeration = 
				new model.structure.Enumeration(umlEnum.getName(), literals);
		this.typeEnum.put(umlEnum, modelEnumeration);
	}
	
	private void processEntity(Class umlClass) {
		Entity entity = new Entity();
		entity.setName(umlClass.getName());
		entity.setClazz(umlClass.getQualifiedName()
				.replace(NamedElement.SEPARATOR, "."));
		entity.setAttributes(new Attributes());
		if (!umlClass.getSuperClasses().isEmpty()) {
			entity.setParentClassName(umlClass.getSuperClasses()
					.get(0).getQualifiedName()
					.replace(NamedElement.SEPARATOR, "."));
		}
		this.typeEntityMap.put(umlClass, entity);
	}
	
	private void processEmbeddable(Class umlClass) {
		Embeddable embeddable = new Embeddable();
		embeddable.setName(umlClass.getName());
		embeddable.setClazz(umlClass.getQualifiedName()
				.replace(NamedElement.SEPARATOR, "."));
		embeddable.setAttributes(new EmbeddableAttributes());
		this.typeEmbeddable.put(umlClass, embeddable);
	}

	/**
	 * Recursively traverses the package structure in the UML model, creating the inner elements (attributes and associations)
	 * of the entities, value objects and enumerations.
	 * @param namedElementPackage - The UML named element package whose entities, enumerations and embeddable objects will be analyzed
	 * and further completed.
	 * @param parentPackage - The package in the intermediate model being created and where the entities, enumerations and value objects
	 * will be placed.
	 * @return - The newly created package in the intermediate model.
	 */
	private Package createIntermediateModel(NamedElement namedElementPackage, 
			Package parentPackage) {
		Package newPackage = null;
		if (parentPackage != null) {
			newPackage = new Package(namedElementPackage.getName(), parentPackage);
		} else {
			newPackage = new Package(namedElementPackage.getName());
		}

		for (Element element : namedElementPackage.getOwnedElements()) {
			if (element instanceof org.eclipse.uml2.uml.Package) {
				createIntermediateModel((NamedElement)element, newPackage);
			} else if (element instanceof Enumeration) {
				model.structure.Enumeration enumeration = 
						this.typeEnum.get((Enumeration)element);
				newPackage.addEnumeration(enumeration);
			} else if (element instanceof Class) {
				classLikeCreator.create(element, newPackage);
			}
		}
		return newPackage;
	}
	
	/**
	 * Gets and returns the UML reader utils instance, creating a new one if 
	 * it has not been set yet.
	 * @return - The UML reader utils instance.
	 */
	private UMLReaderUtils getUtils() {
		if (this.utils == null) {
			this.utils = new UMLReaderUtils(typeEnum, typeEntityMap);
		}
		return utils;
	}

}
