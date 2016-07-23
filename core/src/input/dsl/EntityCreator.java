package input.dsl;

import java.util.Map;

import org.xtext.example.tfg.myDsl.ELEMENT;
import org.xtext.example.tfg.myDsl.ENTITY;
import org.xtext.example.tfg.myDsl.ENUMERATION;
import org.xtext.example.tfg.myDsl.MODEL;
import org.xtext.example.tfg.myDsl.VALUEOBJECT;

import model.structure.Attributes;
import model.structure.Embeddable;
import model.structure.EmbeddableAttributes;
import model.structure.Entity;
import model.structure.Enumeration;
import model.structure.Package;

public class EntityCreator {
	/**
	 * Iterates over the DSL elements in the MODEL DSL element, creating the 
	 * corresponding entities, value objects and custom types.
	 * @param model - The DSL root element.
	 * @param rootPackg - The intermediate model package.
	 * @param entityMap - The map of entities
	 * @param embeddableMap - The map of embeddables
	 * @param enumMap - The map of enumerations.
	 */
	public void createBasicElements(MODEL model, Package rootPackg, 
			Map<String, Entity> entityMap, Map<String, Embeddable> embeddableMap,
			Map<String, Enumeration> enumMap) {
		for (ELEMENT element : model.getElements()) {
			if (element instanceof ENTITY) {
				processEntity(entityMap, element, rootPackg);
			} else if (element instanceof ENUMERATION) {
				processEnumerated(enumMap, element, rootPackg);
			} else if (element instanceof VALUEOBJECT) {
				processValueObject(embeddableMap, element, rootPackg);
			}
		}
	}
	
	/**
	 * It processes the DSL ELEMENT element, creating the intermediate model 
	 * entity and adding it to the intermediate model package.
	 * @param entityMap - The map of entities
	 * @param element - The DSL element to be processed
	 * @param rootPackg - The intermediate model package.
	 */
	private void processEntity(Map<String, Entity> entityMap, ELEMENT element, 
			Package rootPackg) {
		ENTITY dslEntity = (ENTITY)element;
		Entity modelEntity = new Entity();
		modelEntity.setAttributes(new Attributes());
		modelEntity.setClazz(dslEntity.getName());
		modelEntity.setName(dslEntity.getName());
		modelEntity.setAbstract(dslEntity.isAbstractEntity());
		entityMap.put(modelEntity.getClazz(), modelEntity);
		rootPackg.addEntity(modelEntity);
	}
	
	/**
	 * It processes the DSL ELEMENT element, creating the intermediate model 
	 * enumeration and adding it to the intermediate model package.
	 * @param enumMap - The map of enumerations
	 * @param element - The DSL element to be processed
	 * @param rootPackg - The intermediate model package
	 */
	private void processEnumerated(Map<String, Enumeration> enumMap, 
			ELEMENT element, Package rootPackg) {
		ENUMERATION dslEnumeration = (ENUMERATION)element;
		Object[] enumLiterals = new Object[dslEnumeration.getEnumelements().size()];
		for (int i = 0; i < enumLiterals.length; i++) {
			enumLiterals[i] = dslEnumeration.getEnumelements().get(i);
		}
		Enumeration modelEnumeration = new Enumeration(dslEnumeration.getName(),
				enumLiterals);
		rootPackg.addEnumeration(modelEnumeration);
		enumMap.put(modelEnumeration.getName(), modelEnumeration);
	}
	
	/**
	 * It processes the DSL ELEMENT element, creating the intermediate model 
	 * embeddable and adding it to the intermediate model package.
	 * @param embeddableMap - The map of embeddables
	 * @param element - The DSL element to be processed
	 * @param rootPackg - The intermediate model package.
	 */
	private void processValueObject(Map<String, Embeddable> embeddableMap, 
			ELEMENT element, Package rootPackg) {
		VALUEOBJECT dslValueObject = (VALUEOBJECT)element;
		Embeddable embeddable = new Embeddable();
		embeddable.setAttributes(new EmbeddableAttributes());
		embeddable.setClazz(dslValueObject.getName());
		embeddable.setName(dslValueObject.getName());
		rootPackg.addValueObject(embeddable);
		embeddableMap.put(embeddable.getClazz(), embeddable);
	}
}
