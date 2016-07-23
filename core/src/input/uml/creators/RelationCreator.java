package input.uml.creators;

import java.util.Map;

import org.eclipse.uml2.uml.Association;
import org.eclipse.uml2.uml.LiteralUnlimitedNatural;
import org.eclipse.uml2.uml.Property;
import org.eclipse.uml2.uml.Type;

import model.associations.ManyToMany;
import model.associations.ManyToOne;
import model.associations.OneToMany;
import model.associations.OneToOne;
import model.structure.Embeddable;
import model.structure.Embedded;
import model.structure.Entity;

public class RelationCreator {
	/**
	 * Map that relates UML types to Embeddable (or value objects) from the intermediate model
	 */
	private Map<Type, Embeddable> typeEmbeddable;
	/**
	 * Map that relates UML types to Entities from the interemdiate model
	 */
	private Map<Type, Entity> typeEntityMap;

	public RelationCreator(Map<Type, Embeddable> typeEmbeddable, 
			Map<Type, Entity> typeEntityMap){
		if (typeEmbeddable == null || typeEntityMap == null) {
			throw new IllegalArgumentException("Maps relating UML types to "
					+ "intermediate types cannot be missing "
					+ "in the embeddable processor");
		}
		this.typeEmbeddable = typeEmbeddable;
		this.typeEntityMap = typeEntityMap;
	}
	
	/**
	 * Creates associations between entities or embeddable objects.
	 * @param property - The UML property
	 * @param entity - The intermediate model element.
	 */
	public void createAssociation(Property property, Entity entity) {
		Association association = property.getAssociation();
		Property reverseProperty = null;
		for (Property endPropertyTmp : association.getMemberEnds()) {
			if (!endPropertyTmp.equals(property)) {
				reverseProperty = endPropertyTmp;
			}
		}
		if (typeEntityMap.containsKey(property.getType())) {
			if (property.isNavigable()) {
				if (property.getUpper() == LiteralUnlimitedNatural.UNLIMITED) { //OneToMany or ManyToMany
					if (reverseProperty.getUpper() == LiteralUnlimitedNatural.UNLIMITED) { //ManyToMany
						handleManyToMany(property, entity, reverseProperty);
					} else { //OneToMany
						handleOneToMany(property, entity, reverseProperty);
					}
				} else { //ManyToOne or OneToOne
					if (reverseProperty.getUpper() == LiteralUnlimitedNatural.UNLIMITED) { //ManyToOne
						handleManyToOne(property, entity, reverseProperty);
					} else { //OneToOne
						handleOneToOne(property, entity, reverseProperty);
					}
				}
			}
		} else if (typeEmbeddable.containsKey(property.getType())) {
			Embedded embedded = new Embedded();
			embedded.setName(property.getName());
			embedded.setEmbeddable(this.typeEmbeddable.get(property.getType()));
			entity.getAttributes().getEmbedded().add(embedded);
		}
	}
	
	/**
	 * Creates a many to many association from a UML element.
	 * @param property - The UML property
	 * @param entity - The intermediate model element.
	 * @param endProperty - The property in the referenced element.
	 */
	private void handleManyToMany(Property property, Entity entity, Property endProperty) {
		ManyToMany manyToMany = new ManyToMany();
		manyToMany.setName(property.getName());
		manyToMany.setTargetEntity(typeEntityMap.get(property.getType()).getClazz());
		manyToMany.setEntity(typeEntityMap.get(property.getType()));
		ManyToMany correspondingManyToMany = typeEntityMap.get(property.getType())
				.getCorrespondingManyToMany(entity.getClazz(), property.getName());
		if (correspondingManyToMany == null) {
			manyToMany.setMappedBy(endProperty.getName());
		}
		entity.getAttributes().getManyToMany().add(manyToMany);
	}
	
	/**
	 * Creates a one to many association from a UML element.
	 * @param property - The UML property
	 * @param entity - The intermediate model element.
	 * @param endProperty - The property in the referenced element.
	 */
	private void handleOneToMany(Property property, Entity entity, Property endProperty) {
		OneToMany oneToMany = new OneToMany();
		oneToMany.setName(property.getName());
		oneToMany.setTargetEntity(typeEntityMap.get(property.getType()).getClazz());
		oneToMany.setEntity(typeEntityMap.get(property.getType()));
		oneToMany.setMappedBy(endProperty.getName());
		entity.getAttributes().getOneToMany().add(oneToMany);
	}
	
	/**
	 * Creates a many to one association from a UML element.
	 * @param property - The UML property
	 * @param entity - The intermediate model element.
	 * @param endProperty - The property in the referenced element.
	 */
	private void handleManyToOne(Property property, Entity entity, Property endProperty) {
		ManyToOne manyToOne = new ManyToOne();
		manyToOne.setName(property.getName());
		manyToOne.setTargetEntity(typeEntityMap.get(property.getType()).getClazz());
		manyToOne.setEntity(typeEntityMap.get(property.getType()));
		manyToOne.setOptional(property.getLower() == 0);
		entity.getAttributes().getManyToOne().add(manyToOne);
	}
	
	/**
	 * Creates a one to one association from a UML element.
	 * @param property - The UML property
	 * @param entity - The intermediate model element.
	 * @param endProperty - The property in the referenced element.
	 */
	private void handleOneToOne(Property property, Entity entity, Property endProperty) {
		OneToOne oneToOne = new OneToOne();
		oneToOne.setName(property.getName());
		oneToOne.setTargetEntity(typeEntityMap.get(property.getType()).getClazz());
		oneToOne.setEntity(typeEntityMap.get(property.getType()));
		oneToOne.setOptional(property.getLower() == 0);
		OneToOne correspondingOneToOne = typeEntityMap.get(property.getType())
				.getCorrespondingOneToOne(entity.getClazz(), property.getName());
		if (correspondingOneToOne == null) {
			oneToOne.setMappedBy(endProperty.getName());
		}
		entity.getAttributes().getOneToOne().add(oneToOne);
	}
}
