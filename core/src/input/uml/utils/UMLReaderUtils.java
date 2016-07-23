package input.uml.utils;

import java.util.Map;

import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.Property;
import org.eclipse.uml2.uml.Stereotype;
import org.eclipse.uml2.uml.Type;

import input.uml.creators.RelationCreator;
import model.customtypes.EnumType;
import model.inheritance.IdClass;
import model.structure.AnnotatedAttributes;
import model.structure.Basic;
import model.structure.Column;
import model.structure.Entity;
import model.structure.Id;
import model.structure.Transient;
import model.structure.Version;

public class UMLReaderUtils {
	/**
	 * Map that relates the UML type to the enumeration in the intermediate model.
	 */
	private Map<Type, model.structure.Enumeration> typeEnum;
	private Map<Type, model.structure.Entity> entityMap;
	/**
	 * Element that converts types used in the UML model to Java classes.
	 */
	private TypeConverter typeConverter = new TypeConverter();
	
	public UMLReaderUtils(Map<Type, model.structure.Enumeration> typeEnum,
			Map<Type, model.structure.Entity> entityMap) {
		if (typeEnum == null) {
			throw new IllegalArgumentException("Cannot create an instance of the"
					+ " UML reader utils if the type-enum map is missing");
		}
		this.typeEnum = typeEnum;
		this.entityMap = entityMap;
	}
	
	
	/**
	 * Gets and returns the type name of a given UML property by checking if it's
	 *  an enumeration or if it's a primitive type.
	 * @param property - The property whose type name is to be returned
	 * @return - The type name of the property passed as a parameter.
	 */
	public String getTypeName(Property property) {
		if (this.typeEnum.containsKey(property.getType())) {
			model.structure.Enumeration enumeration = 
					this.typeEnum.get(property.getType());
			return enumeration.getName();
		} else if (this.entityMap.containsKey(property.getType())){
			Entity entity = this.entityMap.get(property.getType());
			return entity.getClazz();
		} else {
			return typeConverter.convertUmlType(property.getType()).getCanonicalName();
		}
	}
	
	/**
	 * Creates a column for a property that can be nullable. Used to mark the property as nullable.
	 * @param property - The property whose column is to be created
	 * @return - The new column if the property can be nullable. Null otherwise.
	 */
	public Column getColumn(Property property) {
		if (property.getLower() != 0) {
			model.structure.Column column = new model.structure.Column();
			column.setNullable(false);
			return column;
		}
		return null;
	}
	
	/**
	 * Checks if a stereotype with a given name has been applied to an element.
	 * @param element - The element the stereotype has been applied to.
	 * @param name - The name of the stereotype
	 * @return - True if the element contains a stereotype with the specified name.
	 */
	public boolean containsStereotype(Element element, String name) {
		for (Stereotype stereotype : element.getAppliedStereotypes()) {
			if (stereotype.getName().equals(name)) {
				return true;
			}
		}
		return false;
	}
	
	public void processEntityOrValueObject(Class umlClass, 
			RelationCreator relationCreator,  AnnotatedAttributes attributes, 
			Entity entity) {
		for (Property property : umlClass.getAllAttributes()) {
			if (property.isID()) {
				handleID(property, attributes);
			} else {
				if (property.getAssociation() != null) {
					relationCreator.createAssociation(property, entity);
				} else {
					if (this.containsStereotype(property, "version")) {
						handleVersion(property, attributes);
					} else if (this.containsStereotype(property, "transient")) {
						handleTransient(property, attributes);
					} else {
						handleBasic(property, attributes);
					}
				}
			}
		}
		if (attributes.getId().size() > 1 && entity != null) {
			IdClass idClass = new IdClass();
			idClass.setClazz(entity.getClazz() + "_Key");
			entity.setIdClass(idClass);
		}
	}
	
	/**
	 * Creates the ID property and assigns it to the corresponding entity
	 * @param property - The ID property in the UML model
	 * @param entity - The entity in the intermediate model
	 */
	private void handleID(Property property, AnnotatedAttributes attributes) {
		Id id = new Id();
		id.setName(property.getName());
		id.setTypeName(getTypeName(property));
		attributes.getId().add(id);
	}
	
	/**
	 * Creates the version property and assigns it to the corresponding entity
	 * @param property - The version property in the UML model
	 * @param entity - The entity in the intermediate model
	 */
	private void handleVersion(Property property, AnnotatedAttributes attributes) {
		Version version = new Version();
		version.setName(property.getName());
		version.setTypeName(this.getTypeName(property));
		version.setColumn(this.getColumn(property));
		attributes.getVersion().add(version);
	}
	
	/**
	 * Creates the transient property and assigns it to the corresponding entity
	 * @param property - The transient property in the UML model
	 * @param entity - The entity in the intermediate model
	 */
	private void handleTransient(Property property, AnnotatedAttributes attributes) {
		Transient transient_ = new Transient();
		transient_.setName(property.getName());
		transient_.setTypeName(this.getTypeName(property));
		attributes.getTransient().add(transient_);
	}
	
	/**
	 * Creates the basic property and assigns it to the corresponding entity
	 * @param property - The basic property in the UML model
	 * @param entity - The entity in the intermediate model
	 */
	private void handleBasic(Property property, AnnotatedAttributes attributes) {
		Basic basic = new Basic();
		basic.setName(property.getName());
		basic.setTypeName(this.getTypeName(property));
		basic.setColumn(this.getColumn(property));
		attributes.getBasic().add(basic);
		if (this.typeEnum.containsKey(property.getType())) {
			basic.setEnumerated(EnumType.STRING);
		}
	}
}
