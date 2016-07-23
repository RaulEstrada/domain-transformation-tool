package input.uml.creators;

import java.util.Map;

import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.Type;

import input.uml.utils.UMLReaderUtils;
import model.structure.Embeddable;
import model.structure.Entity;

public class EmbeddableProcessor {
	/**
	 * Map that relates UML types to Embeddable (or value objects) from the 
	 * intermediate model
	 */
	private Map<Type, Embeddable> typeEmbeddable;
	/**
	 * Utils object that provides some utility functions such as creating a 
	 * column for a property and getting the name of the type of the property
	 */
	private UMLReaderUtils utils;
	/**
	 * Creates associations
	 */
	private RelationCreator relationCreator;
	
	public EmbeddableProcessor(Map<Type, Embeddable> typeEmbeddable, 
			Map<Type, Entity> typeEntityMap, UMLReaderUtils utils) {
		if (typeEmbeddable == null || typeEntityMap == null) {
			throw new IllegalArgumentException("Maps relating UML types to "
					+ "intermediate types cannot be missing "
					+ "in the embeddable processor");
		}
		this.typeEmbeddable = typeEmbeddable;
		this.relationCreator = new RelationCreator(typeEmbeddable, typeEntityMap);
		this.utils = utils;
	}
	
	/**
	 * Creates the inner elements (attributes and relations to other 
	 * embeddables and entities) of a given embeddable.
	 * @param umlClass - The UML class representing the Embeddable instance 
	 * in the intermediate model.
	 * @return - The Embeddable instance updated with its inner elements created.
	 */
	Embeddable processEmbeddable(Class umlClass) {
		Embeddable embeddable = typeEmbeddable.get(umlClass);
		utils.processEntityOrValueObject(umlClass, relationCreator, 
				embeddable.getAttributes(), null);
		return embeddable;
	}
	
	
}
