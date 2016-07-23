package input.bbdd;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import input.bbdd.utils.SQL2JavaDataConverter;
import model.inheritance.IdClass;
import model.structure.Attributes;
import model.structure.Entity;
import model.structure.Id;
import model.structure.Table;

public class TableReader {
	private static final String TABLE_TYPE_KEY = "TABLE_TYPE";
	private static final String TABLE_NAME_KEY = "TABLE_NAME";
	private static final String DATA_TYPE_KEY = "DATA_TYPE";
	private static final String COLUMN_NAME_KEY = "COLUMN_NAME";
	private static final String FK_COLUMN_NAME = "FKCOLUMN_NAME";
	private static final String FK_REF_TABLE_NAME = "PKTABLE_NAME";
	private SQL2JavaDataConverter typeConverter = new SQL2JavaDataConverter();
	private List<ManyToManyInfoWrapper> manyToManyInfoWrappers = new ArrayList<>();
	
	/**
	 * It reads the tables from the metadata and the database and checks if they
	 *  are part of a manyToMany association. If they are not, it creates the 
	 *  entities for those tables (but not their inner elements) and returns a 
	 *  map of the name of the table and the entity created from that table.
	 * @param dbMetadata - The database metadata.
	 * @return - Map of the names of the tables and the entities
	 * @throws SQLException
	 */
	public Map<String, Entity> readTables(DatabaseMetaData dbMetadata) 
			throws SQLException{
		Map<String, Entity> entityMap = new HashMap<>();
		ResultSet tableSet = null;
		try{
			tableSet = dbMetadata.getTables(null, null, "%", new String[]{"TABLE"});
			processDBResult(tableSet, entityMap, dbMetadata);
		} catch (SQLException ex) {
			throw ex;
		} finally {
			if (tableSet != null && !tableSet.isClosed()) {tableSet.close();}
		}
		return entityMap;
	}

	/**
	 * Processes the database result and creates the entities.
	 * @param tableSet - The database query result.
	 * @param entityMap - The map of the tables and entities.
	 * @param dbMetadata - The database metadata
	 * @throws SQLException
	 */
	private void processDBResult(ResultSet tableSet, Map<String, 
			Entity> entityMap, DatabaseMetaData dbMetadata) throws SQLException {
		while (tableSet.next()) {
			if (tableSet.getString(TABLE_TYPE_KEY).equals("TABLE")) {
				String tableName = tableSet.getString(TABLE_NAME_KEY);
				ManyToManyInfoWrapper manyToManyInfo = 
						checkIfPartOfManyToMany(tableName, dbMetadata);
				if (manyToManyInfo == null) {
					Table table = new Table();
					table.setName(tableName);
					Entity entity = new Entity();
					entity.setAttributes(new Attributes());
					entity.setClazz(tableName);
					entity.setName(tableName);
					entity.setTable(table);
					readPrimaryKeys(dbMetadata, entity);
					entityMap.put(tableName, entity);
				} else {
					this.manyToManyInfoWrappers.add(manyToManyInfo);
				}
			}
		}
	}
	
	/**
	 * it accesses the database and reads the columns that form the identity of
	 *  the entity. Then, it creates the appropriate intermediate model element
	 *   to express them.
	 * @param dbMetadata - The database metadata
	 * @param entity - The entity to be processed.
	 * @throws SQLException
	 */
	private void readPrimaryKeys(DatabaseMetaData dbMetadata, Entity entity) 
			throws SQLException {
		ResultSet primaryKeySet = null;
		ResultSet columnSet = null;
		try {
			primaryKeySet = dbMetadata.getPrimaryKeys(null, null,
					entity.getTable().getName());
			while (primaryKeySet.next()) {
				Id id = new Id();
				String pKeyName = primaryKeySet.getString(COLUMN_NAME_KEY);
				id.setName(pKeyName);
				columnSet = dbMetadata.getColumns(null, null, 
						entity.getTable().getName(), pKeyName);
				while (columnSet.next()) {
					int type = columnSet.getInt(DATA_TYPE_KEY);
					id.setTypeName(typeConverter.getJavaType(type)
							.getCanonicalName());
				}
				entity.getAttributes().getId().add(id);
			}
			if (entity.getAttributes().getId().size()>1) {
				IdClass idClass = new IdClass();
				idClass.setClazz(entity.getClazz() + "_Key");
				entity.setIdClass(idClass);
			}
		} catch (Exception ex) {
			throw ex;
		} finally {
			if (primaryKeySet != null && !primaryKeySet.isClosed()) {
				primaryKeySet.close();
			}
			if (columnSet != null && !columnSet.isClosed()) {
				columnSet.close();
			}
		}
	}
	
	/**
	 * It accesses the database and checks if the table is just an associative 
	 * table to link two entities in a ManyToMany association, and returns an 
	 * object with the results.
	 * @param tableName - The name of the table.
	 * @param dbMetadata - The database metadata
	 * @return - An element with the information about the possible relationship.
	 * @throws SQLException
	 */
	private ManyToManyInfoWrapper checkIfPartOfManyToMany(String tableName, 
			DatabaseMetaData dbMetadata) throws SQLException {
		ResultSet keySet = null;
		try {
			keySet = dbMetadata.getPrimaryKeys(null, null, tableName);
			List<String> pKeys = new ArrayList<>();
			while (keySet.next()) {
				pKeys.add(keySet.getString(COLUMN_NAME_KEY));
			}
			keySet.close();
			keySet = dbMetadata.getImportedKeys(null, null, tableName);
			Map<String, List<String>> fKeysToTables = new HashMap<>();
			while (keySet.next()) {
				String fKeyName = keySet.getString(FK_COLUMN_NAME);
				pKeys.remove(fKeyName);
				String refTableName = keySet.getString(FK_REF_TABLE_NAME);
				List<String> fKeysToEntity = fKeysToTables.remove(refTableName);
				fKeysToEntity = fKeysToEntity != null ? fKeysToEntity : new ArrayList<>();
				fKeysToEntity.add(fKeyName);
				fKeysToTables.put(refTableName, fKeysToEntity);
			}
			/*if (fKeysToTables.keySet().size() == 2 && pKeys.isEmpty()) {
				String[] refTableNames = fKeysToTables.keySet().toArray(new String[2]);
				return new ManyToManyInfoWrapper(refTableNames[0], 
						fKeysToTables.get(refTableNames[0]), 
						refTableNames[1], fKeysToTables.get(refTableNames[1]));
			}*/
			return null;
		} catch (SQLException ex) {
			throw ex;
		} finally {
			if (keySet != null && !keySet.isClosed()) {keySet.close();}
		}
	}

	/**
	 * It returns all the information about tables that are associative ones 
	 * used to link two entities in a ManyToMany association.
	 * @return - All the information about tables that are associative ones
	 */
	public List<ManyToManyInfoWrapper> getManyToManyInfoWrappers() {
		return manyToManyInfoWrappers;
	}
}
