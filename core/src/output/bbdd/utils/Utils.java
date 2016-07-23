package output.bbdd.utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringJoiner;

import model.associations.AttributeOverride;
import model.structure.Column;
import model.structure.Entity;
import model.structure.Id;

public class Utils {
	/**
	 * Converts Java data types to SQL data types
	 */
	private Java2SQLDataConverter typeConverter = new Java2SQLDataConverter();
	
	/**
	 * Gets and returns the name of the id column
	 * @param attributesOverride - The list of attributes override to be applied
	 * @param id - The id element whose column name is to be returned
	 * @return - The name of the id column
	 */
	public String getIDColumnName (List<AttributeOverride> attributesOverride, 
			Id id, Map<String, Entity> entitiesMap, Entity entity) {
		if (entitiesMap.containsKey(id.getTypeName())) {
			Entity refEntity = entitiesMap.get(id.getTypeName());
			StringJoiner sj = new StringJoiner(",");
			for (Id refId : refEntity.getAttributes().getId()) {
				String pref = "";
				pref = entitiesMap.containsKey(refId.getTypeName()) ? 
						pref : refEntity.getName().toLowerCase() + "_";
				sj.add( pref + 
						getIDColumnName(attributesOverride, refId, entitiesMap,refEntity));
			}
			return sj.toString();
		} else {
			Column column = getColumnOverriding(attributesOverride, id.getName());
			column = column != null ? column : id.getColumn();
			String columnName = (column != null) ? column.getName() : null;
			columnName = (columnName != null && !columnName.isEmpty()) ? 
					columnName : id.getName();
			return columnName;
		}
	}
	
	/**
	 * Gets and return the column column overriding a given attribute of a 
	 * given entity.
	 * @param attributesOverride - The list of attribute override applied to 
	 * the entity
	 * @param attributeName - The name of the attribute whose column override 
	 * is to be returned
	 * @return - The column of the attribute override applied to the attribute.
	 */
	public Column getColumnOverriding(List<AttributeOverride> attributesOverride,
			String attributeName) {
		if (attributesOverride == null) {
			return null;
		}
		for (AttributeOverride ao : attributesOverride) {
			if (ao.getName().equals(attributeName)) {
				return ao.getColumn();
			}
		}
		return null;
	}
	
	/**
	 * Creates the SQL script to create the id element. It can be created as a 
	 * normal column later used for a foreign key, or as a 
	 * primary key depending on the property name passed as a parameter.
	 * @param attributesOverride - The list of attribute override applied.
	 * @param id - The id element to be scripted.
	 * @param sj - The string joiner to join all the elements of a given entity 
	 * or table.
	 * @param propertyName - The key of the SQL statement to be used in this
	 *  instance.
	 * @param prefix - The prefix to be applied to the column name of the id
	 *  element.
	 * @param properties - The properties reader to load the SQL statement.
	 */
	public void createIdColumn(List<AttributeOverride> attributesOverride, 
			Id id, StringJoiner sj, String propertyName, String prefix, 
			PropertyReader properties, Map<String, Entity> entitiesMap, Entity entity) {
		if (entitiesMap.containsKey(id.getTypeName())) {
			Entity refEntity = entitiesMap.get(id.getTypeName());
			for (Id refId : refEntity.getAttributes().getId()) {
				createIdColumn(attributesOverride, refId, sj, "create_column", 
						refEntity.getName().toLowerCase() + "_", properties, entitiesMap, refEntity);
			}
		} else {
			sj.add("\t" + properties.getProperty(propertyName, prefix + 
					this.getIDColumnName(attributesOverride, id, entitiesMap, entity),
					typeConverter.convert(id.getTypeName(), 
					properties.getDatabaseType())));
		}
	}
	
	public Map<String, Entity> getEntitiesMap(Set<Entity> entities) {
		Map<String, Entity> entitiesMap = new HashMap<>();
		for (Entity entity : entities) {
			entitiesMap.put(entity.getClazz(), entity);
		}
		return entitiesMap;
	}
}
