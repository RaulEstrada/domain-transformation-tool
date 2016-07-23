package output.uml;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.uml2.uml.AggregationKind;
import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.LiteralUnlimitedNatural;
import org.eclipse.uml2.uml.Type;

import model.associations.JoinTable;
import model.associations.ManyToMany;
import model.associations.ManyToOne;
import model.associations.OneToMany;
import model.associations.OneToOne;
import model.customtypes.RelationshipType;
import model.structure.AnnotatedAttributes;
import model.structure.ElementInRelationship;
import model.structure.Embedded;

public class AssociationsCreator {
	/**
	 * Builder that handles the creation of UML elements (classes, associations,
	 *  attributes, etc)
	 */
	private UML2Builder umlBuilder = new UML2Builder();
	/**
	 * Maps the canonical name of custom types (classes, embeddable) to the UML
	 *  type elements.
	 * Used mostly to create associations between classes.
	 */
	private Map<String, Type> customTypes;
	/**
	 * Maps the UML type to the intermediate domain model element that can take
	 *  part in an association.
	 * Used mostly to get the target element in a relationship between two UML 
	 * elements.
	 */
	private Map<Type, ElementInRelationship> typeToMappedElement;
	/**
	 * Maps which element that takes part in an association has already have 
	 * such association created
	 * by the other end of the relationship so that the association is not duplicated.
	 */
	private Map<ElementInRelationship, 
	Map<RelationshipType, List<ElementInRelationship>>> relationsCreated;

	/**
	 * Traverses the list of embedded, one to one, one to many, many to one and 
	 * many to many elements, 
	 * creating the corresponding UML associations in each case.
	 * @param umlClass - The UML whose elements are being created
	 * @param attributes - The attributes of the entity being analyzed.
	 * @param className - The class name of the entity being analyzed.
	 */
	void create(Class umlClass, AnnotatedAttributes attributes, String className) {
		if (umlClass == null || attributes == null || className == null || 
				className.isEmpty()) {
			throw new IllegalArgumentException("Cannot create associations if "
					+ "the UML class, the attributes or class name are missing");
		}
		for (Embedded embedded : attributes.getEmbedded()) {
			createEmbedded(embedded, umlClass);
		}
		for (OneToOne oneToOne : attributes.getOneToOne()) {
			createOneToOne(oneToOne, umlClass, className);
		}
		for (OneToMany oneToMany : attributes.getOneToMany()) {
			createOneToMany(oneToMany, umlClass, className);
		}
		for (ManyToOne manyToOne : attributes.getManyToOne()) {
			createManyToOne(manyToOne, umlClass, className);
		}
		for (ManyToMany manyToMany : attributes.getManyToMany()) {
			createManyToMany(manyToMany, umlClass, className);
		}
	}

	/**
	 * Creates the association from the @Embedded annotation.
	 * @param embedded - The embedded intermediate element
	 * @param umlClass - The UML class whose elements are being created
	 */
	private void createEmbedded(Embedded embedded, Class umlClass) {
		Type embeddedType = this.customTypes.get(embedded.getTypeName().toLowerCase());
		umlBuilder.createAssociation(umlClass, true, AggregationKind.NONE_LITERAL,
				embedded.getName(), 0, 1, embeddedType, false, 
				AggregationKind.NONE_LITERAL, "", 0, 
				LiteralUnlimitedNatural.UNLIMITED);
	}

	/**
	 * Creates the association from the @OneToOne annotation, with an intermediate
	 *  table if it has been specified.
	 * @param oneToOne - The OneToOne intermediate model element.
	 * @param umlClass - The UML class whose elements are being created.
	 * @param className - The classname of the entity
	 */
	private void createOneToOne(OneToOne oneToOne, Class umlClass, String className) {
		ElementInRelationship currentElement = typeToMappedElement.get(umlClass);
		Type targetType = this.customTypes.get(oneToOne.getTargetEntity().toLowerCase());
		ElementInRelationship targetElement = this.typeToMappedElement.get(targetType);
		if (relationsCreated.containsKey(currentElement) && relationsCreated
				.get(currentElement).containsKey(RelationshipType.ONE_TO_ONE)
				&& relationsCreated.get(currentElement)
				.get(RelationshipType.ONE_TO_ONE).contains(targetElement)) {
			return;
		}
		JoinTable joinTable = oneToOne.getJoinTable();
		OneToOne correspondingOneToOne = targetElement.getCorrespondingOneToOne(
				className, oneToOne.getName());
		processCreateJoinTable(oneToOne, umlClass, targetType, joinTable, correspondingOneToOne);
		if (correspondingOneToOne != null) {
			addReverseRelationship(targetElement, RelationshipType.ONE_TO_MANY, umlClass);
		}
	}

	private void processCreateJoinTable(OneToOne oneToOne, Class umlClass, 
			Type targetType, JoinTable joinTable, OneToOne correspondingOneToOne) {
		if (joinTable != null) {
			Class intermediateClass = umlBuilder.createClass(umlClass.getPackage(),
					joinTable.getName(), false);
			umlBuilder.createAssociation(umlClass, true, AggregationKind.NONE_LITERAL,
					oneToOne.getName(), (oneToOne.isOptional()) ? 0 : 1, 1,
					intermediateClass, true, AggregationKind.NONE_LITERAL, "", 1, 1);
			umlBuilder.createAssociation(targetType, correspondingOneToOne != null, 
					AggregationKind.NONE_LITERAL, correspondingOneToOne.getName(), 
					(correspondingOneToOne != null && !correspondingOneToOne
					.isOptional()) ? 1 : 0, 1, intermediateClass, true, 
					AggregationKind.NONE_LITERAL, (correspondingOneToOne != null)
					? correspondingOneToOne.getName() : "", 1, 1);
		} else {
			umlBuilder.createAssociation(umlClass, true, AggregationKind.NONE_LITERAL,
					oneToOne.getName(), (oneToOne.isOptional()) ? 0 : 1, 1,
					targetType, correspondingOneToOne != null, 
					AggregationKind.NONE_LITERAL, (correspondingOneToOne != null) 
					? correspondingOneToOne.getName() : "", (correspondingOneToOne != null
					&& !correspondingOneToOne.isOptional()) ? 1 : 0, 1);
		}
	}

	/**
	 * Creates a one to many association, with an intermediate table if it has been specified.
	 * @param oneToMany - The OneToMany intermediate model element.
	 * @param umlClass - The uML class whose elements are being created.
	 * @param className - The class name of the entity
	 */
	private void createOneToMany(OneToMany oneToMany, Class umlClass, String className) {
		ElementInRelationship currentElement = typeToMappedElement.get(umlClass);
		Type targetType = this.customTypes.get(oneToMany.getTargetEntity().toLowerCase());
		ElementInRelationship targetElement = this.typeToMappedElement.get(targetType);
		if (relationsCreated.containsKey(currentElement) && relationsCreated
				.get(currentElement).containsKey(RelationshipType.ONE_TO_MANY)
				&& relationsCreated.get(currentElement)
				.get(RelationshipType.ONE_TO_MANY).contains(targetElement)) {
			return;
		}
		JoinTable joinTable = oneToMany.getJoinTable();
		ManyToOne correspondingManyToOne = targetElement
				.getCorrespondingManyToOne(className, oneToMany.getName());
		if (joinTable != null) {
			Class intermediateClass = umlBuilder.createClass(
					umlClass.getPackage(), joinTable.getName(), false);
			umlBuilder.createAssociation(umlClass, true, AggregationKind.NONE_LITERAL,
					oneToMany.getName(), 0, LiteralUnlimitedNatural.UNLIMITED,
					intermediateClass, true, AggregationKind.NONE_LITERAL, "", 1, 1);
			umlBuilder.createAssociation(targetType, (correspondingManyToOne != null),
					AggregationKind.NONE_LITERAL, (correspondingManyToOne != null) ? 
					correspondingManyToOne.getName() : "", (correspondingManyToOne != null
					&& !correspondingManyToOne.isOptional()) ? 1 : 0, 1, 
					intermediateClass, true, AggregationKind.NONE_LITERAL, "", 1, 1);
		} else {
			umlBuilder.createAssociation(umlClass, true, AggregationKind.NONE_LITERAL,
					oneToMany.getName(), 0, LiteralUnlimitedNatural.UNLIMITED,
					targetType, correspondingManyToOne != null, 
					AggregationKind.NONE_LITERAL, correspondingManyToOne != null ? 
					correspondingManyToOne.getName() : "", (correspondingManyToOne != null 
					&& !correspondingManyToOne.isOptional()) ? 1 : 0, 1);
		}
		if (correspondingManyToOne != null) {
			addReverseRelationship(targetElement, RelationshipType.MANY_TO_ONE, umlClass);
		}
	}

	/**
	 * Creates a many to one association, with an intermediate table if it has been specified.
	 * @param manyToOne - The ManyToOne intermediate model element.
	 * @param umlClass - The uML class whose elements are being created.
	 * @param className - The class name of the entity.
	 */
	private void createManyToOne(ManyToOne manyToOne, Class umlClass, 
			String className) {
		ElementInRelationship currentElement = typeToMappedElement.get(umlClass);
		Type targetType = this.customTypes.get(manyToOne.getTargetEntity().toLowerCase());
		ElementInRelationship targetElement = this.typeToMappedElement.get(targetType);
		if (relationsCreated.containsKey(currentElement) && relationsCreated
				.get(currentElement).containsKey(RelationshipType.MANY_TO_ONE)
				&& relationsCreated.get(currentElement)
				.get(RelationshipType.MANY_TO_ONE).contains(targetElement)) {
			return;
		}
		JoinTable joinTable = manyToOne.getJoinTable();
		OneToMany correspondingOneToMany = targetElement
				.getCorrespondingOneToMany(className, manyToOne.getName());
		processManyToOneJoinTable(manyToOne, umlClass, targetType, joinTable, 
				correspondingOneToMany);
		if (correspondingOneToMany != null) {
			addReverseRelationship(targetElement, RelationshipType.ONE_TO_MANY, umlClass);
		}
	}
	
	private void processManyToOneJoinTable(ManyToOne manyToOne, Class umlClass, 
			Type targetType, JoinTable joinTable, OneToMany correspondingOneToMany) {
		if (joinTable != null) {
			Class intermediateClass = umlBuilder.createClass(umlClass.getPackage(),
					joinTable.getName(), false);
			umlBuilder.createAssociation(umlClass, true, AggregationKind.NONE_LITERAL,
					manyToOne.getName(), manyToOne.isOptional() ? 0 : 1, 1,
					intermediateClass, true, AggregationKind.NONE_LITERAL, "", 1, 1);
			umlBuilder.createAssociation(targetType, (correspondingOneToMany != null),
					AggregationKind.NONE_LITERAL, (correspondingOneToMany != null) ?
					correspondingOneToMany.getName() : "", 0, 
					LiteralUnlimitedNatural.UNLIMITED, intermediateClass,
					true, AggregationKind.NONE_LITERAL, "", 1, 1);
		} else {
			umlBuilder.createAssociation(umlClass, true, AggregationKind.NONE_LITERAL, 
					manyToOne.getName(), (manyToOne.isOptional() != null && manyToOne.isOptional()) ? 0 : 1, 1,
							targetType, (correspondingOneToMany != null),
							AggregationKind.NONE_LITERAL, (correspondingOneToMany != null)
							? correspondingOneToMany.getName() : "", 0, 
							LiteralUnlimitedNatural.UNLIMITED);
		}
	}

	/**
	 * Creates a many to many association, with an intermediate table if it has been specified.
	 * @param manyToMany - The ManyToMany intermediate model element.
	 * @param umlClass - The UML class whose elements are being created
	 * @param className - The class name of the entity
	 */
	private void createManyToMany(ManyToMany manyToMany, Class umlClass, 
			String className) {
		ElementInRelationship currentElement = typeToMappedElement.get(umlClass);
		Type targetType = this.customTypes.get(manyToMany.getTargetEntity().toLowerCase());
		ElementInRelationship targetElement = this.typeToMappedElement.get(targetType);
		if (relationsCreated.containsKey(currentElement) && relationsCreated
				.get(currentElement).containsKey(RelationshipType.MANY_TO_MANY) 
				&& relationsCreated.get(currentElement)
				.get(RelationshipType.MANY_TO_MANY).contains(targetElement)) {
			return;
		}
		JoinTable joinTable = manyToMany.getJoinTable();
		ManyToMany correspondingManyToMany = targetElement
				.getCorrespondingManyToMany(className, manyToMany.getName());
		createManyToManyJoinTable(manyToMany, umlClass, targetType, joinTable, 
				correspondingManyToMany);
		if (correspondingManyToMany != null) {
			addReverseRelationship(targetElement, RelationshipType.MANY_TO_MANY, 
					umlClass);
		}
	}
	
	private void createManyToManyJoinTable(ManyToMany manyToMany, Class umlClass, 
			Type targetType, JoinTable joinTable, ManyToMany correspondingManyToMany) {
		if (joinTable != null) {
			Class intermediateClass = umlBuilder.createClass(umlClass.getPackage(),
					joinTable.getName(), false);
			umlBuilder.createAssociation(umlClass, true, AggregationKind.NONE_LITERAL,
					manyToMany.getName(), 0, LiteralUnlimitedNatural.UNLIMITED,
					intermediateClass, true, AggregationKind.NONE_LITERAL, "", 1, 1);
			umlBuilder.createAssociation(targetType, (correspondingManyToMany != null),
					AggregationKind.NONE_LITERAL, (correspondingManyToMany != null) ?
							correspondingManyToMany.getName() : "", 0, 
							LiteralUnlimitedNatural.UNLIMITED, intermediateClass, 
							true, AggregationKind.NONE_LITERAL, "", 1, 1);
		} else {
			umlBuilder.createAssociation(umlClass, true, AggregationKind.NONE_LITERAL, 
					manyToMany.getName(), 0, LiteralUnlimitedNatural.UNLIMITED,
					targetType, correspondingManyToMany != null, 
					AggregationKind.NONE_LITERAL, (correspondingManyToMany != null) ? 
					correspondingManyToMany.getName() : "", 0, LiteralUnlimitedNatural.UNLIMITED);
		}
	}

	/**
	 * Marks a relationship as created when the reverse complementary association 
	 * has been created so as not to duplicate it.
	 * @param targetElement - The UML class that's at the end of the association
	 *  that has been created.
	 * @param relationshipType - The type of relationship that needs to be 
	 * marked because its complementary has been created.
	 * @param umlClass - The UML class that is the owner of the relationship 
	 * just created.
	 */
	private void addReverseRelationship(ElementInRelationship targetElement, 
			RelationshipType relationshipType, Class umlClass) {
		if (relationsCreated.containsKey(targetElement)) {
			if (relationsCreated.get(targetElement).containsKey(relationshipType)) {
				relationsCreated.get(targetElement).get(relationshipType)
				.add(this.typeToMappedElement.get(umlClass));
			} else {
				List<ElementInRelationship> elementsInRelationship = 
						new ArrayList<>();
				elementsInRelationship.add(this.typeToMappedElement.get(umlClass));
				relationsCreated.get(targetElement)
					.put(relationshipType, elementsInRelationship);
			}
		} else {
			List<ElementInRelationship> elementsInRelationship = 
					new ArrayList<>();
			elementsInRelationship.add(this.typeToMappedElement.get(umlClass));
			Map<RelationshipType, List<ElementInRelationship>> relations = 
					new HashMap<>();
			relations.put(relationshipType, elementsInRelationship);
			this.relationsCreated.put(targetElement, relations);
		}
	}

	/**
	 * Assigns the custom types map to the current associations creator.
	 * @param customTypes - The custom types map.
	 * @return - The current associations creator.
	 */
	AssociationsCreator withCustomTypes(Map<String, Type> customTypes) {
		if (customTypes == null) {
			throw new IllegalArgumentException("The custom types map cannot be missing");
		}
		this.customTypes = customTypes;
		return this;
	}

	/**
	 * Assigns the UML to intermediate model map to the current associations creator.
	 * @param typeToMappedElement - The UML to intermediate model map.
	 * @return - The current associations creator.
	 */
	AssociationsCreator withTypeToMappedElement(Map<Type, ElementInRelationship> 
		typeToMappedElement) {
		if (typeToMappedElement == null) {
			throw new IllegalArgumentException("The UML to intermediate model "
					+ "map cannot be missing");
		}
		this.typeToMappedElement = typeToMappedElement;
		return this;
	}

	/**
	 * Assigns the map with the associations previously created.
	 * @param typeToMappedElement - The UML to intermediate model map.
	 * @return - The current associations creator.
	 */
	AssociationsCreator withRelationsCreated(
			Map<ElementInRelationship, Map<RelationshipType, 
			List<ElementInRelationship>>> relationsCreated) {
		if (relationsCreated == null) {
			throw new IllegalArgumentException("The map with the associations "
					+ "already created cannot be missing");
		}
		this.relationsCreated = relationsCreated;
		return this;
	}
}
