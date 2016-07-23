package output.uml;

import java.util.Map;

import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.Type;

import model.structure.ElementInRelationship;
import model.structure.Embeddable;
import model.structure.Entity;
import model.structure.Enumeration;
import model.structure.Package;

class PackageStructureCreator {
	/**
	 * Builder that handles the creation of UML elements (classes, 
	 * associations, attributes, etc)
	 */
	private UML2Builder umlBuilder = new UML2Builder();
	/**
	 * Maps the canonical name of simple types (primitive data types and 
	 * enumerations) to the UML type elements.
	 * Used mostly to create attributes.
	 */
	private Map<String, Type> types;
	/**
	 * Maps the canonical name of custom types (classes, embeddable) to the 
	 * UML type elements.
	 * Used mostly to create associations between classes.
	 */
	private Map<String, Type> customTypes;
	/**
	 * Maps the UML type to the intermediate domain model element that can take
	 *  part in an association.
	 * Used mostly to get the target element in a relationship between two 
	 * UML elements.
	 */
	private Map<Type, ElementInRelationship> typeToMappedElement;
	
	/**
	 * Creates the enumeration, class and package structure from the root element.
	 *  It is required to create this structure
	 * without internal elements (attributes) and associations because these may
	 *  have a custom type that has not been 
	 * created yet. As the enumerations and classes are created, they are mapped
	 *  so that later on they can be used when 
	 * needed. This method is called recursively to create subtrees in nested 
	 * UML packages.
	 * @param packg - The root package from the intermediate model that contains
	 *  all the intermediate elements.
	 * @param umlPackage - The UML root element that will contain all the UML 
	 * elements.
	 */
	void create(Package packg, org.eclipse.uml2.uml.Package umlPackage) {
		if (packg == null || umlPackage == null) {
			throw new IllegalArgumentException("Cannot create the class, "
					+ "enumeration and package structure if the UML package or "
					+ "the package from the intermediate model are missing");
		}
		for (Enumeration enumeration : packg.getEnumerations()) {
			org.eclipse.uml2.uml.Enumeration umlEnumeration = 
					umlBuilder.createEnumeration(umlPackage, enumeration.getName());
			for (Object constant : enumeration.getConstants()) {
				umlBuilder.createEnumerationLiteral(umlEnumeration, constant.toString());
			}
			types.put(enumeration.getName().toLowerCase(), umlEnumeration);
		}
		for (Entity entity : packg.getEntities()) {
			Class umlClass = umlBuilder.createClass(umlPackage, entity.getName(),
					entity.isAbstract());
			customTypes.put(entity.getClazz().toLowerCase(), umlClass);
			typeToMappedElement.put(umlClass, entity);
		}
		for (Embeddable embeddable : packg.getValueObjects()) {
			Class umlClass = umlBuilder.createClass(umlPackage, embeddable.getName(),
					embeddable.isAbstract());
			umlClass.applyStereotype(UMLWriter.EMBEDDABLE_STEREOTYPE);
			customTypes.put(embeddable.getClazz().toLowerCase(), umlClass);
			typeToMappedElement.put(umlClass, embeddable);
		}
		for (Package subPackage : packg.getChildrenPackages()) {
			org.eclipse.uml2.uml.Package umlSubPackage = umlBuilder
					.createPackage(umlPackage, subPackage.getName());
			create(subPackage, umlSubPackage);
		}
	}
	
	/**
	 * Assigns the primitive/enumeration types map to the current package 
	 * structure creator.
	 * @param types - The types map.
	 * @return - The current package structure creator.
	 */
	PackageStructureCreator withTypes(Map<String, Type> types) {
		if (types == null) {
			throw new IllegalArgumentException("The types map cannot "
					+ "be missing");
		}
		this.types = types;
		return this;
	}
	
	/**
	 * Assigns the custom types map to the current package structure creator.
	 * @param customTypes - The custom types map.
	 * @return - The current package structure creator.
	 */
	PackageStructureCreator withCustomTypes(Map<String, Type> customTypes) {
		if (customTypes == null) {
			throw new IllegalArgumentException("The custom types map "
					+ "cannot be missing");
		}
		this.customTypes = customTypes;
		return this;
	}
	
	/**
	 * Assigns the UML to intermediate model map to the current package 
	 * structure creator.
	 * @param typeToMappedElement - The UML to intermediate model map.
	 * @return - The current package structure creator.
	 */
	PackageStructureCreator withTypeToMappedElement(
			Map<Type, ElementInRelationship> typeToMappedElement) {
		if (typeToMappedElement == null) {
			throw new IllegalArgumentException("The UML to intermediate model "
					+ "map cannot be missing");
		}
		this.typeToMappedElement = typeToMappedElement;
		return this;
	}
}
