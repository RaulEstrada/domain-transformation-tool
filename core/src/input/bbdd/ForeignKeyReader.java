package input.bbdd;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import model.associations.ManyToOne;
import model.associations.OneToMany;
import model.associations.OneToOne;
import model.structure.Entity;

public class ForeignKeyReader {
	private static final String FK_COLUMN_NAME = "FKCOLUMN_NAME";
	private static final String FK_REF_TABLE_NAME = "PKTABLE_NAME";
	private static final String FKTABLE_NAME_KEY = "FKTABLE_NAME";

	/**
	 * Reads the foreign keys of the table of a given entity.
	 * @param dbMetadata - The database metadata
	 * @param entity - The entity whose foreign keys are to be processed.
	 * @param entityMap - The map connecting table names to entities.
	 * @return - The list of foreign keys.
	 * @throws SQLException
	 */
	public List<String> readForeignKeys(DatabaseMetaData dbMetadata, 
			Entity entity, Map<String, Entity> entityMap) throws SQLException{
		ResultSet foreignKeySet = null;
		List<String> foreignKeyNames = new ArrayList<>();
		try {
			foreignKeySet = dbMetadata.getImportedKeys(null, null, 
					entity.getTable().getName());
			Map<Entity, List<String>> foreignKeys = 
					readForeignKeys(foreignKeySet, entityMap);
			createRelationShips(foreignKeys, dbMetadata, entity);
			for (List<String> l : foreignKeys.values()) {
				foreignKeyNames.addAll(l);
			}
		} catch (SQLException ex) {
			throw ex;
		} finally {
			if (foreignKeySet != null && !foreignKeySet.isClosed()) {
				foreignKeySet.close();
			}
		}
		return foreignKeyNames;
	}

	/**
	 * Reads the foreign keys of the table of a given entity.
	 * @param foreignKeySet - Database result of the foreign keys query
	 * @param entityMap - The map connecting table names to entities.
	 * @return - A map with the entities and foreign keys linked to the entity
	 * @throws SQLException
	 */
	private Map<Entity, List<String>> readForeignKeys(ResultSet foreignKeySet, 
			Map<String, Entity> entityMap) throws SQLException {
		Map<Entity, List<String>> foreignKeys = new HashMap<>();
		while (foreignKeySet.next()) {
			String fKeyName = foreignKeySet.getString(FK_COLUMN_NAME);
			String refTableName = foreignKeySet.getString(FK_REF_TABLE_NAME);
			Entity targetEntity = entityMap.get(refTableName);
			List<String> fKeysToEntity = foreignKeys.remove(targetEntity);
			fKeysToEntity = fKeysToEntity != null ? fKeysToEntity : new ArrayList<>();
			fKeysToEntity.add(fKeyName);
			foreignKeys.put(targetEntity, fKeysToEntity);
		}
		return foreignKeys;
	}

	/**
	 * Creates the corresponding association between entities.
	 * @param foreignKeys - The map of entities and foreign keys.
	 * @param dbMetadata - The database metadata.
	 * @param entity - The entity to be processed.
	 * @throws SQLException
	 */
	private void createRelationShips(Map<Entity, List<String>> foreignKeys, 
			DatabaseMetaData dbMetadata, Entity entity) throws SQLException {
		for (Entity referencedEntity : foreignKeys.keySet()) {
			//List<String> referencedEntityPKeys = referencedEntity.getIDNames();
			//List<String> fKeysToReferenceEntity = foreignKeys.get(referencedEntity);
			ResultSet exportedKeys = null;
			List<String> referenceEntityfKeysToCurrentEntity = new ArrayList<>();
			try {
				exportedKeys = dbMetadata.getExportedKeys(null, null, 
						entity.getTable().getName());
				while (exportedKeys.next()) {
					if (exportedKeys.getString(FKTABLE_NAME_KEY)
							.equals(referencedEntity.getTable().getName())){
						referenceEntityfKeysToCurrentEntity
							.add(exportedKeys.getString(FK_COLUMN_NAME));
					}
				}
			} catch (SQLException ex) {
				throw ex;
			} finally {
				if (exportedKeys != null && !exportedKeys.isClosed()) {exportedKeys.close();}
			}
			if (referenceEntityfKeysToCurrentEntity.containsAll(entity.getIDNames())) {
				processOneToOne(entity, referencedEntity);
			} else {
				processManyToOne(entity, referencedEntity);
			}
		}
	}
	
	/**
	 * Creates the two OneToOne associations
	 * @param entity - The entity that participates in the association.
	 * @param referencedEntity - The referenced entity in the association.
	 */
	private void processOneToOne(Entity entity, Entity referencedEntity) {
		OneToOne oneToOne = new OneToOne();
		oneToOne.setTargetEntity(referencedEntity.getClazz());
		oneToOne.setName(referencedEntity.getName());
		oneToOne.setEntity(referencedEntity);
		entity.getAttributes().getOneToOne().add(oneToOne);
		OneToOne correspondingOneToOne = new OneToOne();
		correspondingOneToOne.setTargetEntity(entity.getClazz());
		correspondingOneToOne.setName(entity.getName());
		correspondingOneToOne.setMappedBy(oneToOne.getName());
		correspondingOneToOne.setEntity(entity);
		referencedEntity.getAttributes().getOneToOne().add(correspondingOneToOne);
	}
	
	/**
	 * Creates the ManyToOne and OneToMany associations.
	 * @param entity - The entity that participates in the association.
	 * @param referencedEntity - The referenced entity in the association.
	 */
	private void processManyToOne(Entity entity, Entity referencedEntity) {
		ManyToOne manyToOne = new ManyToOne();
		manyToOne.setName(referencedEntity.getName());
		manyToOne.setTargetEntity(referencedEntity.getClazz());
		manyToOne.setEntity(referencedEntity);
		entity.getAttributes().getManyToOne().add(manyToOne);
		OneToMany oneToMany = new OneToMany();
		oneToMany.setName(entity.getName());
		oneToMany.setTargetEntity(entity.getClazz());
		oneToMany.setEntity(entity);
		oneToMany.setMappedBy(manyToOne.getName());
		referencedEntity.getAttributes().getOneToMany().add(oneToMany);
	}
}
