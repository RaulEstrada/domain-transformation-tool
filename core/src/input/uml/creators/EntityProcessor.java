package input.uml.creators;

import java.util.Map;

import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.Type;

import input.uml.utils.UMLReaderUtils;
import model.customtypes.InheritanceType;
import model.inheritance.Inheritance;
import model.structure.Embeddable;
import model.structure.Entity;

public class EntityProcessor {
	/**
	 * Map that relates UML types to Entities from the interemdiate model
	 */
	private Map<Type, Entity> typeEntityMap;
	/**
	 * Utils object that provides some utility functions such as creating a 
	 * column for a property and
	 * getting the name of the type of the property
	 */
	private UMLReaderUtils utils;
	/**
	 * Creates associations
	 */
	private RelationCreator relationCreator;
	
	
	public EntityProcessor(Map<Type, Embeddable> typeEmbeddable,
			Map<Type, Entity> typeEntityMap, UMLReaderUtils utils,
			Map<Type, model.structure.Enumeration> typeEnum) {
		if (typeEmbeddable == null || typeEntityMap == null || typeEnum == null) {
			throw new IllegalArgumentException("Maps relating UML types to "
					+ "intermediate types cannot be missing "
					+ "in the entity processor");
		}
		if (utils == null) {
			throw new IllegalArgumentException("The UML reader utils cannot be"
					+ " missing in order to create entities objects");
		}
		this.typeEntityMap = typeEntityMap;
		this.utils = utils;
		this.relationCreator = new RelationCreator(typeEmbeddable, typeEntityMap);
	}
	
	/**
	 * Creates the inner elements (attributes and relations to other entities 
	 * and embeddable) of a given entity.
	 * @param umlClass - The UML class representing the Entity instance in the 
	 * intermediate model.
	 * @return - The Entity instance updated with its inner elements created.
	 */
	Entity processEntity(Class umlClass) {
		Entity entity = typeEntityMap.get(umlClass);
		if (!umlClass.getSuperClasses().isEmpty()) {
			Entity superClass = typeEntityMap
					.get(umlClass.getSuperClasses().get(0));
			Inheritance inheritance = new Inheritance();
			inheritance.setStrategy(InheritanceType.SINGLE_TABLE);
			superClass.setInheritance(inheritance);
			entity.setParentEntity(superClass);
		}
		utils.processEntityOrValueObject(umlClass, relationCreator, 
				entity.getAttributes(), entity);
		return entity;
	}
	
	
}
