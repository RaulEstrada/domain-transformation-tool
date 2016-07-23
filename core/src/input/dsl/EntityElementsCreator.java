package input.dsl;

import java.util.Map;

import org.eclipse.xtext.common.types.JvmFormalParameter;
import org.eclipse.xtext.common.types.JvmTypeReference;
import org.xtext.example.tfg.myDsl.ANNOTATED_ATTRIBUTE;
import org.xtext.example.tfg.myDsl.ATTRIBUTE;
import org.xtext.example.tfg.myDsl.ELEMENT;
import org.xtext.example.tfg.myDsl.ENTITY;
import org.xtext.example.tfg.myDsl.ENTITYELEMENT;
import org.xtext.example.tfg.myDsl.LINK;
import org.xtext.example.tfg.myDsl.MODEL;
import org.xtext.example.tfg.myDsl.OPERATION;
import org.xtext.example.tfg.myDsl.RELATIONSHIP;
import org.xtext.example.tfg.myDsl.VALUEOBJECT;
import org.xtext.example.tfg.myDsl.impl.MyDslFactoryImpl;

import model.customtypes.InheritanceType;
import model.inheritance.Inheritance;
import model.structure.Attributes;
import model.structure.Embeddable;
import model.structure.Entity;
import model.structure.Enumeration;
import model.structure.Id;
import model.structure.Operation;
import model.structure.OperationParameter;
import model.structure.Package;
import model.structure.Version;

public class EntityElementsCreator {
	private Map<String, Entity> entityMap;
	private Map<String, Enumeration> enumMap;
	private Map<String, Embeddable> embeddableMap;
	private AssociationCreator associationCreator = new AssociationCreator();
	private BasicAttributeCreator basicAttributeCreator = new BasicAttributeCreator();
	private IdClassProcessor idClassProcessor = new IdClassProcessor();
	private Utils utils = new Utils();
	
	public EntityElementsCreator(Map<String, Entity> entityMap, Map<String, 
			Enumeration> enumMap, Map<String, Embeddable> embeddableMap) {
		this.entityMap = entityMap;
		this.enumMap = enumMap;
		this.embeddableMap = embeddableMap;
	}
	
	/**
	 * Processes the DSL elements, creating the inner elements of entities,
	 * value objects and links.
	 * @param model - The DSL root element.
	 * @param packg - The intermediate model package.
	 */
	public void processAttributesAndInheritance(MODEL model, Package packg) {
		for (ELEMENT element : model.getElements()) {
			if (element instanceof ENTITY) {
				processEntity(element);
			} else if (element instanceof VALUEOBJECT) {
				VALUEOBJECT dslValueObject = (VALUEOBJECT)element;
				processValueObjectAttributes(dslValueObject);
			} else if (element instanceof LINK) {
				processLink(element, packg);
			}
		}
	}
	
	/**
	 * Processes the DSL element that maps to an intermediate model entity.
	 * @param element - The DSL element to be processed.
	 */
	private void processEntity(ELEMENT element) {
		ENTITY dslEntity = (ENTITY)element;
		if (dslEntity.getParent() != null) {
			Entity parentEntity = entityMap.get(dslEntity.getParent().getName());
			Entity childEntity = entityMap.get(dslEntity.getName());
			childEntity.setParentClassName(parentEntity.getClazz());
			childEntity.setParentEntity(parentEntity);
			Inheritance defaultInheritance = new Inheritance();
			defaultInheritance.setStrategy(InheritanceType.SINGLE_TABLE);
			parentEntity.setInheritance(defaultInheritance);
		}
		processEntityAttributes(dslEntity);
	}
	
	/**
	 * Processes a DSL element that maps to a relationship in the intermediate
	 * model.
	 * @param element - The DSL element to be processed
	 * @param packg - The intermediate model package.
	 */
	private void processLink(ELEMENT element, Package packg) {
		LINK dslLink = (LINK)element;
		RELATIONSHIP end1 = dslLink.getRelations().get(0);
		RELATIONSHIP end2 = dslLink.getRelations().get(1);
		Entity entityEnd1 = entityMap.get(end1.getType().getName());
		Entity entityEnd2 = entityMap.get(end2.getType().getName());
		if (dslLink.getAttributes().isEmpty()) {
			associationCreator.registerAssociation(end1, end2, entityEnd1, 
					entityEnd2, true, false);
			associationCreator.registerAssociation(end2, end1, entityEnd2, 
					entityEnd1, false, false);
		} else { //It's an entity as well
			Entity associativeEntity = new Entity();
			associativeEntity.setClazz(dslLink.getName());
			associativeEntity.setAttributes(new Attributes());
			associativeEntity.setName(dslLink.getName());
			packg.addEntity(associativeEntity);
			createAssociativeIds(end1, end2, entityEnd1, entityEnd2, 
					associativeEntity);
			createAssociativeRelationships(end1, end2, entityEnd1, entityEnd2, 
					associativeEntity, dslLink);
			
		}
	}
	
	/**
	 * Creates the IDs of an associative entity.
	 * @param end1 - The association in the first end.
	 * @param end2 - The association in the second end.
	 * @param entityEnd1 - The entity in the first end.
	 * @param entityEnd2 - The entity in the second end.
	 * @param associativeEntity - The associative entity.
	 */
	private void createAssociativeIds(RELATIONSHIP end1, RELATIONSHIP end2, 
			Entity entityEnd1, Entity entityEnd2, Entity associativeEntity) {
		Id id1 = new Id();
		id1.setName(end1.getName());
		id1.setTypeName(entityEnd1.getClazz());
		Id id2 = new Id();
		id2.setName(end2.getName());
		id2.setTypeName(entityEnd2.getClazz());
		associativeEntity.getAttributes().getId().add(id1);
		associativeEntity.getAttributes().getId().add(id2);
		idClassProcessor.createIdClass(associativeEntity);
	}
	
	/**
	 * Creates an associative entity for the association.
	 * @param end1 - The association in the first end.
	 * @param end2 - The association in the second end.
	 * @param entityEnd1 - The entity in the first end.
	 * @param entityEnd2 - The entity in the second end.
	 * @param associativeEntity - The associative entity.
	 * @param dslLink - The DSL element that generated the association.
	 */
	private void createAssociativeRelationships(RELATIONSHIP end1, 
			RELATIONSHIP end2, Entity entityEnd1, Entity entityEnd2, 
			Entity associativeEntity, LINK dslLink) {
		RELATIONSHIP one = new MyDslFactoryImpl().createRELATIONSHIP();
		one.setCardinal("One");
		one.setName(end1.getName());
		associationCreator.registerAssociation(one, end1, entityEnd1, 
				associativeEntity, true, false);
		associationCreator.registerAssociation(end1, one, associativeEntity, 
				entityEnd1, false, true);
		one.setName(end2.getName());
		associationCreator.registerAssociation(one, end2, entityEnd2, 
				associativeEntity, true, false);
		associationCreator.registerAssociation(end2, one, associativeEntity, 
				entityEnd2, false, true);
		for (ATTRIBUTE attribute : dslLink.getAttributes()) {
			basicAttributeCreator.createBasicAttribute(attribute.getType(), 
					attribute.getName(), associativeEntity.getAttributes(),
					embeddableMap, enumMap);
		}
	}
	
	/**
	 * Processes a DSL element that maps to a value object.
	 * @param valueObject - The DSL element to be processed.
	 */
	private void processValueObjectAttributes(VALUEOBJECT valueObject) {
		Embeddable embeddable = embeddableMap.get(valueObject.getName());
		for (ATTRIBUTE attribute : valueObject.getValueattributes()) {
			basicAttributeCreator.createBasicAttribute(attribute.getType(), 
					attribute.getName(), embeddable.getAttributes(), embeddableMap,
					enumMap);
		}
	}
	
	/**
	 * Processes a DSL element that maps to an entity.
	 * @param dslEntity - The DSL element to be processed.
	 */
	private void processEntityAttributes(ENTITY dslEntity) {
		Entity modelEntity = entityMap.get(dslEntity.getName());
		for (ENTITYELEMENT entityElement : dslEntity.getEntityElements()) {
			if (entityElement instanceof ANNOTATED_ATTRIBUTE) {
				ANNOTATED_ATTRIBUTE attribute = (ANNOTATED_ATTRIBUTE)entityElement;
				if (attribute.getAnnotation() != null && 
						attribute.getAnnotation().equals("@Id")) {
					Id id = new Id();
					id.setName(attribute.getName());
					id.setTemporal(utils.getTemporalType(attribute.getType()));
					id.setTypeName(attribute.getType().getQualifiedName());
					modelEntity.getAttributes().getId().add(id);
				} else if (attribute.getAnnotation() != null && 
						attribute.getAnnotation().equals("@Version")) {
					Version version = new Version();
					version.setName(attribute.getName());
					version.setTemporal(utils.getTemporalType(attribute.getType()));
					version.setTypeName(attribute.getType().getQualifiedName());
					modelEntity.getAttributes().getVersion().add(version);
				} else {
					basicAttributeCreator.createBasicAttribute(attribute.getType(),
							attribute.getName(), modelEntity.getAttributes(),
							embeddableMap, enumMap);
				}
			} else {
				OPERATION operation = (OPERATION)entityElement;
				JvmTypeReference returnType = operation.getReturn();
				String returnTypeClass = (returnType != null) ? 
						returnType.getQualifiedName() : null;
				Operation modelOperation = 
						new Operation(operation.getName(), returnTypeClass);
				for (JvmFormalParameter parameter : operation.getParams()) {
					OperationParameter modelParamter = new OperationParameter(
							parameter.getIdentifier(), parameter.getQualifiedName());
					modelOperation.addOperationParameter(modelParamter);
				}
				modelEntity.addOperation(modelOperation);
			}
		}
		if (modelEntity.getAttributes().getId().size() > 1) {
			idClassProcessor.createIdClass(modelEntity);
		}
	}
}
