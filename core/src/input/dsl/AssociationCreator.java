package input.dsl;

import org.xtext.example.tfg.myDsl.RELATIONSHIP;

import model.associations.ManyToMany;
import model.associations.ManyToOne;
import model.associations.OneToMany;
import model.associations.OneToOne;
import model.structure.Entity;

public class AssociationCreator {
	/**
	 * Crestes an association between two entities.
	 * @param end1 - The association type in the first end
	 * @param end2 - The association type in the second end
	 * @param entityEnd1 - The entity in the first end
	 * @param entityEnd2 - The entity in the second end
	 * @param mappedBy - if the association contains a mappedBy element
	 * @param id - If it's an ID.
	 */
	public void registerAssociation(RELATIONSHIP end1, RELATIONSHIP end2, 
			Entity entityEnd1, Entity entityEnd2, boolean mappedBy, boolean id) {
		if (end1.getCardinal().endsWith("One") && end2.getCardinal()
				.endsWith("One")) { //OneToOne
			registerOneToOne(end1, end2, entityEnd1, entityEnd2, mappedBy, id);
		} else if (end1.getCardinal().endsWith("One") && end2.getCardinal()
				.endsWith("Many")) { //OneToMany
			registerOneToMany(end1, end2, entityEnd1, entityEnd2);
		} else if (end1.getCardinal().endsWith("Many") && end2.getCardinal()
				.endsWith("One")) { //ManyToOne
			registerManyToOne(end2, entityEnd1, entityEnd2, id);
		} else if (end1.getCardinal().endsWith("Many") && end2.getCardinal()
				.endsWith("Many")) { //ManyToMany
			registerManyToMany(end1, end2, entityEnd1, entityEnd2, mappedBy);
		} else {
			throw new RuntimeException("Link combination not supported: " + 
		end1.getCardinal() + "<->" + end2.getCardinal());
		}
	}
	
	/**
	 * Registers a OneToOne association
	 * @param end1 - The association type in the first end
	 * @param end2 - The association type in the second end
	 * @param entityEnd1 - The entity in the first end
	 * @param entityEnd2 - The entity in the second end
	 * @param mappedBy - if the association contains a mappedBy element
	 * @param id - If it's an ID.
	 */
	private void registerOneToOne(RELATIONSHIP end1, RELATIONSHIP end2, 
			Entity entityEnd1, Entity entityEnd2, boolean mappedBy, boolean id) {
		OneToOne oneToOne = new OneToOne();
		oneToOne.setEntity(entityEnd2);
		oneToOne.setName(end2.getName());
		oneToOne.setMappedBy(end1.getName());
		oneToOne.setTargetEntity(entityEnd2.getClazz());
		oneToOne.setOptional(end2.getCardinal().startsWith("Zero"));
		oneToOne.setId(id);
		entityEnd1.getAttributes().getOneToOne().add(oneToOne);
	}
	
	/**
	 * Registers a OneToMany association between two entities.
	 * @param end1 - The association type in the first end
	 * @param end2 - The association type in the second end
	 * @param entityEnd1 - The entity in the first end
	 * @param entityEnd2 - The entity in the second end
	 */
	private void registerOneToMany(RELATIONSHIP end1, RELATIONSHIP end2, 
			Entity entityEnd1, Entity entityEnd2) {
		OneToMany oneToMany = new OneToMany();
		oneToMany.setMappedBy(end1.getName());
		oneToMany.setName(end2.getName());
		oneToMany.setTargetEntity(entityEnd2.getClazz());
		oneToMany.setEntity(entityEnd2);
		entityEnd1.getAttributes().getOneToMany().add(oneToMany);
	}
	
	/**
	 * Registers a ManyToOne association between two entities.
	 * @param end2 - The association type in the second end
	 * @param entityEnd1 - The entity in the first end
	 * @param entityEnd2 - The entity in the second end
	 * @param id - If it's an ID.
	 */
	private void registerManyToOne(RELATIONSHIP end2, Entity entityEnd1, 
			Entity entityEnd2, boolean id) {
		ManyToOne manyToOne = new ManyToOne();
		manyToOne.setEntity(entityEnd2);
		manyToOne.setId(id);
		manyToOne.setName(end2.getName());
		manyToOne.setTargetEntity(entityEnd2.getClazz());
		manyToOne.setOptional(end2.getCardinal().startsWith("Zero"));
		entityEnd1.getAttributes().getManyToOne().add(manyToOne);
	}
	
	/**
	 * Registers a ManyToMany association between two entities.
	 * @param end1 - The association type in the first end
	 * @param end2 - The association type in the second end
	 * @param entityEnd1 - The entity in the first end
	 * @param entityEnd2 - The entity in the second end
	 * @param mappedBy - if the association contains a mappedBy element
	 */
	private void registerManyToMany(RELATIONSHIP end1, RELATIONSHIP end2, 
			Entity entityEnd1, Entity entityEnd2, boolean mappedBy) {
		ManyToMany manyToMany = new ManyToMany();
		manyToMany.setEntity(entityEnd2);
		if (mappedBy) {
			manyToMany.setMappedBy(end1.getName());
		}
		manyToMany.setName(end2.getName());
		manyToMany.setTargetEntity(entityEnd2.getClazz());
		entityEnd1.getAttributes().getManyToMany().add(manyToMany);
	}
}
