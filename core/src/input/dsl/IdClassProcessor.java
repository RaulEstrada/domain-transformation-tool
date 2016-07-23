package input.dsl;

import java.util.Map;

import model.inheritance.IdClass;
import model.structure.Entity;
import model.structure.Id;
import model.structure.Package;

public class IdClassProcessor {
	/**
	 * Iterates over the entities in a given intermediate model package to 
	 * create the ClassId element.
	 * @param packg - The intermediate model element.
	 * @param entityMap - The map of entities.
	 */
	public void process(Package packg, Map<String, Entity> entityMap) {
		for (Entity entity : packg.getAllEntitiesInPackageTree()) {
			if (entity.getIdClass() != null) {
				for (Id id : entity.getAttributes().getId()) {
					processId(entity, id, entityMap);
				}
			}
		}
	}
	
	/**
	 * Creates the new Id
	 * @param entity - The intermediate model entity.
	 * @param id - The intermediate model Id
	 * @param entityMap - The map of entities.
	 */
	private void processId(Entity entity, Id id, Map<String, Entity> entityMap) {
		Id idClassId = new Id();
		idClassId.setName(id.getName());
		if (entityMap.containsKey(id.getTypeName())) {
			Entity refEntity = entityMap.get(id.getTypeName());
			if (refEntity.getIdClass() != null) {
				idClassId.setTypeName(refEntity.getIdClass().getClazz());
			} else {
				idClassId.setTypeName(refEntity.getIDsWithParent().get(0)
						.getTypeName());
			}
		} else {
			idClassId.setTypeName(id.getTypeName());
		}
		entity.getIdClass().getAttributes().add(idClassId);
	}
	
	/**
	 * Creates an intermediate model IdClass
	 * @param modelEntity - The intermediate model entity.
	 */
	public void createIdClass(Entity modelEntity) {
		IdClass idClass = new IdClass();
		idClass.setClazz(modelEntity.getClazz() + "_Key");
		modelEntity.setIdClass(idClass);
	}
}
