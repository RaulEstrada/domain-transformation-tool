package input.uml.creators;

import java.util.Map;

import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.Type;

import input.uml.utils.UMLReaderUtils;
import model.structure.Embeddable;
import model.structure.Entity;
import model.structure.Package;

public class ClassLikeElementsCreator {
	/**
	 * Map that relates UML types to Embeddable (or value objects) from the
	 *  intermediate model
	 */
	private Map<Type, Embeddable> typeEmbeddable;
	/**
	 * Map that relates UML types to Entities from the interemdiate model
	 */
	private Map<Type, Entity> typeEntityMap;
	/**
	 * Processes Embeddable objects, creating their inner elements
	 */
	private EmbeddableProcessor embeddableProcessor;
	/**
	 * Processes Entities, creating their inner elements
	 */
	private EntityProcessor entityProcessor;
	
	public ClassLikeElementsCreator(Map<Type, Embeddable> typeEmbeddable, 
			Map<Type, Entity> typeEntityMap, UMLReaderUtils utils,
			Map<Type, model.structure.Enumeration> typeEnum) {
		if (typeEmbeddable == null || typeEntityMap == null || typeEnum == null) {
			throw new IllegalArgumentException("Cannot instantiate a class like"
					+ " elements creator if the maps are missing");
		}
		if (utils == null) {
			throw new IllegalArgumentException("The UML reader utils cannot be"
					+ " missing in order to create entities and embeddable objects");
		}
		this.typeEmbeddable = typeEmbeddable;
		this.typeEntityMap = typeEntityMap;
		this.embeddableProcessor = 
				new EmbeddableProcessor(typeEmbeddable, typeEntityMap, utils);
		this.entityProcessor = 
				new EntityProcessor(typeEmbeddable, typeEntityMap, utils, typeEnum);
	}
	
	public void create(Element element, Package newPackage) {
		Class umlClass = (Class) element;
		if (typeEntityMap.containsKey(umlClass)) {
			newPackage.addEntity(entityProcessor.processEntity(umlClass));
		} else if (typeEmbeddable.containsKey(umlClass)) {
			newPackage.addValueObject(embeddableProcessor
					.processEmbeddable(umlClass));
		}
	}
}
