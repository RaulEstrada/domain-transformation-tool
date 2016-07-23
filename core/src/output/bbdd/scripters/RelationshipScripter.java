package output.bbdd.scripters;

import java.util.List;
import java.util.Map;
import java.util.StringJoiner;

import model.associations.AttributeOverride;
import model.associations.JoinColumn;
import model.structure.Entity;
import model.structure.Id;
import output.bbdd.utils.Java2SQLDataConverter;
import output.bbdd.utils.PropertyReader;
import output.bbdd.utils.Utils;

/**
 * This class adds the columns to the table association 
 * where the JoinColumn elements are to be applied.
 * @author Raul Estrada
 *
 */
public class RelationshipScripter {
	private Utils utils = new Utils();
	/**
	 * Element that loads the SQL statements.
	 */
	private PropertyReader properties;
	/**
	 * Element that creates the primary keys of a ManyToMany relationship from 
	 * the JoinTable object.
	 */
	private Java2SQLDataConverter typeConverter = new Java2SQLDataConverter();
	private Map<String, Entity> entitiesMap;
	
	/**
	 * Initializes the scripter with a given properties reader.
	 * @param properties - Element that loads the SQL statements.
	 */
	public RelationshipScripter(PropertyReader properties, Map<String, Entity> entitiesMap) {
		if (properties == null) {
			throw new IllegalArgumentException("Cannot script relationships with"
					+ " a null properties reader");
		}
		this.properties = properties;
		this.entitiesMap = entitiesMap;
	}
	
	/**
	 * Creates the SQL code to generate the column, applying the joinColumns 
	 * when necessary.
	 * @param joinColumns - The JoinColumns specified for the association
	 * @param entity - The referenced entity in the association
	 * @param sj - The string joiner to add all the elements of a table
	 * @param attributesOverride - The list of attributes override to be applied
	 * @param propertyName - The key of the SQL statement
	 */
	public void scriptRelationships(List<JoinColumn> joinColumns, Entity entity,
			StringJoiner sj, List<AttributeOverride> attributesOverride,
			String propertyName, StringJoiner pKeyJoiner) {
		if (!joinColumns.isEmpty()) {
			int index = 0;
			List<Id> refIds = entity.getIDsWithParent();
			for (JoinColumn jc : joinColumns) {
				String idName = null;
				if(jc.getReferencedColumnName() != null && 
						!jc.getReferencedColumnName().isEmpty()) {
					idName = jc.getReferencedColumnName();
				} else {
					idName = utils.getIDColumnName(null, refIds.get(index++), 
							entitiesMap, entity).replace(entity.getName().toLowerCase() + "_", "");
				}
				if (pKeyJoiner != null) {
					pKeyJoiner.add(idName);
				}
				sj.add("\t" + properties.getProperty(propertyName, jc.getName(), 
						typeConverter.convert(entity.getIdByName(idName).getTypeName(),
								this.properties.getDatabaseType())));
			}
		} else {
			for (Id id : entity.getIDsWithParent()) {
				if (pKeyJoiner != null) {
					pKeyJoiner.add(entity.getName().toLowerCase() + "_" + 
							utils.getIDColumnName(attributesOverride, id, 
									entitiesMap, entity));
				}
				utils.createIdColumn(attributesOverride, id, sj, propertyName, 
						entity.getName().toLowerCase() + "_", this.properties,
						entitiesMap, entity);
			}
		}
	}
}
