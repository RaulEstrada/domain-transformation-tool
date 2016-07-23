package input.bbdd;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import model.associations.ManyToMany;
import model.structure.Entity;

public class ManyToManyCreator {
	private List<ManyToManyInfoWrapper> manyToManyInfoWrappers;
	private Map<String, Entity> entityMap;
	
	/**
	 * Initializes the ManyToMany creator with the information needed to create
	 * the associations.
	 * @param manyToManyInfoWrappers - Info about the manyToMany relationships.
	 * @param entityMap - The map connecting tables and entities.
	 */
	public ManyToManyCreator(List<ManyToManyInfoWrapper> manyToManyInfoWrappers,
			Map<String, Entity> entityMap) {
		this.manyToManyInfoWrappers = manyToManyInfoWrappers;
		this.entityMap = entityMap;
	}

	/**
	 * Creates the ManyToMany associations
	 * @return - A map of entities and their foreign keys.
	 */
	public Map<Entity, List<String>> createManyToManyAssociations() {
		Map<Entity, List<String>> keysInManyToMany = new HashMap<>();
		for (ManyToManyInfoWrapper wrapper : this.manyToManyInfoWrappers) {
			Entity entity1 = this.entityMap.get(wrapper.getTable1());
			Entity entity2 = this.entityMap.get(wrapper.getTable2());
			ManyToMany manyToMany = new ManyToMany();
			manyToMany.setName(entity2.getName());
			manyToMany.setTargetEntity(entity2.getClazz());
			manyToMany.setEntity(entity2);
			entity1.getAttributes().getManyToMany().add(manyToMany);
			ManyToMany correspManyToMany = new ManyToMany();
			correspManyToMany.setMappedBy(manyToMany.getName());
			correspManyToMany.setName(entity1.getName());
			correspManyToMany.setTargetEntity(entity1.getClazz());
			correspManyToMany.setEntity(entity1);
			entity2.getAttributes().getManyToMany().add(correspManyToMany);
			List<String> keysInEntity1 = keysInManyToMany.get(entity1);
			keysInEntity1 = keysInEntity1 != null ? keysInEntity1 : new ArrayList<>();
			keysInEntity1.addAll(wrapper.getColumns1());
			keysInManyToMany.put(entity1, keysInEntity1);
			List<String> keysInEntity2 = keysInManyToMany.get(entity2);
			keysInEntity2 = keysInEntity2 != null ? keysInEntity2 : new ArrayList<>();
			keysInEntity2.addAll(wrapper.getColumns2());
			keysInManyToMany.put(entity2, keysInEntity2);
		}
		return keysInManyToMany;
	}
}
