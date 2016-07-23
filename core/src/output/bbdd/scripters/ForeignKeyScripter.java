package output.bbdd.scripters;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;

import model.associations.JoinColumn;
import model.structure.Entity;
import model.structure.Id;
import output.bbdd.utils.PropertyReader;
import output.bbdd.utils.Utils;

/**
 * This class creates the SQL code to add foreign keys to tables
 * @author Raul Estrada
 *
 */
public class ForeignKeyScripter {
	/**
	 * Element that loads the SQL statements.
	 */
	private PropertyReader properties;
	/**
	 * Element to write in the output.sql file
	 */
	private BufferedWriter writer;
	private Utils utils = new Utils();
	private Map<String, Entity> entitiesMap;
	
	/**
	 * Initialize the scripter with the properties reader and buffered writer.
	 * @param properties - Element that loads the SQL statements.
	 * @param writer - Element to write in the output.sql file
	 */
	public ForeignKeyScripter(PropertyReader properties, BufferedWriter writer,
			Map<String, Entity> entitiesMap) {
		if (properties == null || writer == null) {
			throw new IllegalArgumentException("Cannot create a foreign key "
					+ "scripter with a null properties reader or buffered writer");
		}
		this.properties = properties;
		this.writer = writer;
		this.entitiesMap = entitiesMap;
	}

	/**
	 * Creates the SQL code to generate the foreign keys in a table referencing 
	 * the primary keys of another table.
	 * @param refEntity - The referenced entity
	 * @param joinColumns - The JoinColumns to be applied in the association
	 * @param tableNameEntity - The table name of the entity where the foreign 
	 * key will be added.
	 * @throws IOException
	 */
	public void scriptForeignKeys(Entity refEntity, List<JoinColumn> joinColumns,
			String tableNameEntity) throws IOException {
		StringJoiner foreignKeys = new StringJoiner(",");
		StringJoiner primaryKeys = new StringJoiner(",");
		List<Id> idsOfRefEntity = refEntity.getIDsWithParent();
		if (joinColumns != null && !joinColumns.isEmpty()) {
			int index = 0;
			for (JoinColumn jc : joinColumns) {
				foreignKeys.add(jc.getName());
				String pKeyName = jc.getReferencedColumnName();
				pKeyName = (pKeyName != null && !pKeyName.isEmpty()) ? pKeyName :
					utils.getIDColumnName(null, idsOfRefEntity.get(index++), entitiesMap, refEntity);
				primaryKeys.add(pKeyName);
			}
		} else {
			for (Id id : idsOfRefEntity) {
				String keyName = utils.getIDColumnName(null, id, entitiesMap, refEntity);
				String fkPre = "";
				fkPre = entitiesMap.containsKey(id.getTypeName()) ? "" : 
					refEntity.getName().toLowerCase() + "_";
				foreignKeys.add(fkPre + keyName);
				primaryKeys.add(keyName);
			}
		}
		String refTableName = refEntity.getTable() != null ? 
				refEntity.getTable().getName() : null;
		refTableName = (refTableName != null && !refTableName.isEmpty()) ? 
				refTableName : refEntity.getName();
		String constraintName = "fk_" + tableNameEntity + "_" + 
				refEntity.getName().toLowerCase();
		writer.write(properties.getProperty("create_foreign_key", tableNameEntity,
				constraintName, foreignKeys.toString(), refTableName, primaryKeys.toString()) + "\n");
	}
}
