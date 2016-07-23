package output.uml;

import java.util.List;
import java.util.Map;

import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.Type;

import model.customtypes.RelationshipType;
import model.structure.AnnotatedAttributes;
import model.structure.Basic;
import model.structure.Column;
import model.structure.ElementInRelationship;
import model.structure.Embeddable;
import model.structure.Entity;
import model.structure.Id;
import model.structure.Package;
import model.structure.Transient;
import model.structure.Version;

/**
 * This class creates the different attributes and associations/relationships 
 * between UML types or elements.
 * @author Raul Estrada
 *
 */
class InternalStructureCreator {
	/**
	 * Builder that handles the creation of UML elements (classes, associations, 
	 * attributes, etc)
	 */
	private UML2Builder umlBuilder = new UML2Builder();
	/**
	 * Maps the canonical name of simple types (primitive data types and 
	 * enumerations) to the UML type elements.
	 * Used mostly to create attributes.
	 */
	private Map<String, Type> types;
	/**
	 * Maps the canonical name of custom types (classes, embeddable) to the UML
	 *  type elements.
	 * Used mostly to create associations between classes.
	 */
	private Map<String, Type> customTypes;
	/**
	 * Element used to create associations like Embedded, OneToMany, OneToOne, 
	 * ManyToOne, ManyToMany.
	 */
	private AssociationsCreator associationsCreator = new AssociationsCreator();
	
	/**
	 * Method that traverses recursively the package and classes structure, and 
	 * creates their internal structure and associations.
	 * @param packg - The UML package to be traverse at the moment
	 */
	void create(Package packg) {
		if (packg == null) {
			throw new IllegalArgumentException("Cannot traverse a null UML package");
		}
		for (Entity entity : packg.getEntities()) {
			if (entity.getParentClassName() != null && !entity.getParentClassName()
					.isEmpty() && this.customTypes.containsKey(
					entity.getParentClassName().toLowerCase())) {
				Type parentClass = this.customTypes.get(entity
						.getParentClassName().toLowerCase());
				umlBuilder.createGeneralization((Class)this.customTypes.get(
						entity.getClazz().toLowerCase()), (Class)parentClass);
			}
			createSimpleAttributes(entity.getAttributes(), entity.getClazz());
		}
		for (Embeddable embeddable : packg.getValueObjects()) {
			createSimpleAttributes(embeddable.getAttributes(), embeddable.getClazz());
		}
		for (Package subPackage : packg.getChildrenPackages()) {
			create(subPackage);
		}
	}
	
	/**
	 * 	Creates UML elements for each of the simple attributes in the 
	 * intermediate model.
	 * @param attributes - The intermediate model attributes
	 * @param className - The name of the class containing the attributes
	 */
	private void createSimpleAttributes(AnnotatedAttributes attributes, 
			String className) {
		Class umlClass = (Class)this.customTypes.get(className.toLowerCase());
		for (Id id : attributes.getId()) {
			Type idType = this.types.get(id.getTypeName().toLowerCase());
			if (idType == null) {
				idType = this.customTypes.get(id.getTypeName().toLowerCase());
			}
			umlBuilder.createAttribute(umlClass, id.getName(), idType, 1, 1).setIsID(true);
		}
		for (Version version : attributes.getVersion()) {
			Type versionType = this.types.get(version.getTypeName().toLowerCase());
			Column column = version.getColumn();
			umlBuilder.createAttribute(umlClass, version.getName(), versionType, 
					(column != null && !column.isNullable()) ? 1 : 0, 1)
			.applyStereotype(UMLWriter.VERSION_STEREOTYPE);
		}
		for (Transient _transient : attributes.getTransient()) {
			Type transientType = this.types.get(_transient.getTypeName().toLowerCase());
			if (transientType != null) {
				umlBuilder.createAttribute(umlClass, _transient.getName(), 
						transientType, 0, 1).applyStereotype(UMLWriter.TRANSIENT_STEREOTYPE);
			}
		}
		for (Basic basic : attributes.getBasic()) {
			Type basicType = this.types.get(basic.getTypeName().toLowerCase());
			if (basicType == null) {
				basicType = this.customTypes.get(basic.getTypeName().toLowerCase());
			}
			Column column = basic.getColumn();
			umlBuilder.createAttribute(umlClass, basic.getName(), basicType, 
					(column != null && column.isNullable() != null && column.isNullable()) ? 1 : 0, 1);
		}
		associationsCreator.create(umlClass, attributes, className);
	}
	
	/**
	 * Assigns the primitive/enumeration types map to the current internal 
	 * structure creator.
	 * @param types - The types map.
	 * @return - The current internal structure creator.
	 */
	InternalStructureCreator withTypes(Map<String, Type> types) {
		if (types == null) {
			throw new IllegalArgumentException("The types map cannot be missing");
		}
		this.types = types;
		return this;
	}
	
	/**
	 * Assigns the custom types map to the current internal structure creator.
	 * @param customTypes - The custom types map.
	 * @return - The current internal structure creator.
	 */
	InternalStructureCreator withCustomTypes(Map<String, Type> customTypes) {
		if (customTypes == null) {
			throw new IllegalArgumentException("The custom types map cannot "
					+ "be missing");
		}
		this.customTypes = customTypes;
		this.associationsCreator.withCustomTypes(customTypes);
		return this;
	}
	
	/**
	 * Assigns the UML to intermediate model map to the current internal 
	 * structure creator.
	 * @param typeToMappedElement - The UML to intermediate model map.
	 * @return - The current internal structure creator.
	 */
	InternalStructureCreator withTypeToMappedElement(
			Map<Type, ElementInRelationship> typeToMappedElement) {
		if (typeToMappedElement == null) {
			throw new IllegalArgumentException("The UML to intermediate model "
					+ "map cannot be missing");
		}
		this.associationsCreator.withTypeToMappedElement(typeToMappedElement);
		return this;
	}
	
	/**
	 * Assigns the map with the associations previously created.
	 * @param typeToMappedElement - The UML to intermediate model map.
	 * @return - The current internal structure creator.
	 */
	InternalStructureCreator withRelationsCreated(
			Map<ElementInRelationship, Map<RelationshipType, 
			List<ElementInRelationship>>> relationsCreated) {
		if (relationsCreated == null) {
			throw new IllegalArgumentException("The map with the associations "
					+ "already created cannot be missing");
		}
		this.associationsCreator.withRelationsCreated(relationsCreated);
		return this;
	}
	
}
