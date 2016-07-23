package output.bbdd.scripters;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;

import model.associations.AttributeOverride;
import model.associations.ManyToOne;
import model.associations.OneToOne;
import model.customtypes.EnumType;
import model.structure.AnnotatedAttributes;
import model.structure.Basic;
import model.structure.Column;
import model.structure.Embeddable;
import model.structure.Embedded;
import model.structure.Entity;
import model.structure.Id;
import model.structure.Version;
import output.bbdd.utils.Java2SQLDataConverter;
import output.bbdd.utils.PropertyReader;
import output.bbdd.utils.Utils;

public class TableContentsScripter {
	private Utils utils = new Utils();
	/**
	 * Element that creates the primary keys of a ManyToMany relationship from 
	 * the JoinTable object.
	 */
	private Java2SQLDataConverter typeConverter = new Java2SQLDataConverter();
	/**
	 * Element that loads the SQL statements.
	 */
	private PropertyReader properties;
	/**
	 * Element that creates the keys of a relationship from the JoinTable object.
	 */
	private RelationshipScripter relationshipScripter;
	private Map<String, Entity> entitiesMap;
	
	/**
	 * Initializes the scripter with the properties reader
	 * @param properties - Element that loads the SQL statements.
	 */
	public TableContentsScripter(PropertyReader properties, Map<String, Entity> entitiesMap) {
		if (properties == null) {
			throw new IllegalArgumentException("Cannot script the table content "
					+ "with a null properties reader");
		}
		this.properties = properties;
		this.entitiesMap = entitiesMap;
		this.relationshipScripter = new RelationshipScripter(properties, entitiesMap);
	}
	
	/**
	 * Generates the SQL code to fill the table with its columns and primary 
	 * keys. Foreign key columns are created, but the 
	 * foreign key restriction is added later.
	 * @param attributes - The attributes of the entity to be scripted.
	 * @param sj - The string joiner to join all the elements
	 * @param attributesOverride - The list of attributes override to be applied
	 * @return - The string joiner with the sql code of all the elements
	 * @throws IOException
	 */
	public StringJoiner scriptTableContents(AnnotatedAttributes attributes, 
			StringJoiner sj, List<AttributeOverride> attributesOverride, 
			Entity entity) 
					throws IOException {
		StringJoiner pKeysJoiner = new StringJoiner(",");
		for (Id id : attributes.getId()){
			processId(attributesOverride, id, sj, pKeysJoiner, entity);
		}
		for (Version version : attributes.getVersion()) {
			processVersion(attributesOverride, version, sj);
		}
		for (Basic basic : attributes.getBasic()) {
			processBasic(attributesOverride, basic, sj);
		}
		for (Embedded embedded : attributes.getEmbedded()) {
			processEmbedded(embedded, sj);
		}
		processAssociations(attributesOverride, attributes, sj);
		if (!attributes.getId().isEmpty()) {
			sj.add("\t" + properties.getProperty("create_primary_key", 
					pKeysJoiner.toString()));
		}
		return sj;
	}
	
	/**
	 * Scripts version attributes
	 * @param attributesOverride - The list of attribute overrides.
	 * @param version - The version element to be scripted
	 * @param sj - The StringJoiner with the SQL script.
	 */
	private void processVersion(List<AttributeOverride> attributesOverride, 
			Version version, StringJoiner sj) {
		Column column = utils.getColumnOverriding(attributesOverride, 
				version.getName());
		column = column != null ? column : version.getColumn();
		String columnName = (column != null) ? column.getName() : null;
		columnName = (columnName != null && !columnName.isEmpty()) ? 
				columnName : version.getName();
		sj.add("\t" + properties.getProperty("create_column", columnName, 
				typeConverter.convert(version.getTypeName(), 
						properties.getDatabaseType())));
	}
	
	/**
	 * Scripts Id attributes
	 * @param attributesOverride - The list of attribute overrides.
	 * @param id - The id element to be scripted
	 * @param sj - The StringJoiner with the SQL script.
	 * @param pKeysJoiner - The StringJoiner with primary keys of the entity
	 */
	private void processId(List<AttributeOverride> attributesOverride, Id id, 
			StringJoiner sj, StringJoiner pKeysJoiner, Entity entity) {
		pKeysJoiner.add(utils.getIDColumnName(attributesOverride, id, 
				entitiesMap, entity));
		utils.createIdColumn(attributesOverride, id, sj, "create_column", "",
				this.properties, entitiesMap, entity);
	}
	
	/**
	 * Scripts basic attributes
	 * @param attributesOverride - The list of attribute overrides.
	 * @param basic - The basic element to be scripted
	 * @param sj - The StringJoiner with the SQL script.
	 */
	private void processBasic(List<AttributeOverride> attributesOverride, 
			Basic basic, StringJoiner sj) {
		Column column = utils.getColumnOverriding(attributesOverride, 
				basic.getName());
		column = column != null ? column : basic.getColumn();
		String columnName = (column != null) ? column.getName() : null;
		columnName = (columnName != null && !columnName.isEmpty()) ? 
				columnName : basic.getName();
		if (basic.getEnumerated() != null) {
			if (basic.getEnumerated() == EnumType.ORDINAL) {
				sj.add("\t" + properties.getProperty("create_column", 
						columnName, typeConverter.convert("java.lang.Integer",
								properties.getDatabaseType())));
			} else {
				sj.add("\t" + properties.getProperty("create_column", 
						columnName, typeConverter.convert("java.lang.String", 
								properties.getDatabaseType())));
			}
		} else {
			sj.add("\t" + properties.getProperty("create_column", columnName,
					typeConverter.convert(basic.getTypeName(), 
							properties.getDatabaseType())));
		}
	}
	
	/**
	 * Scripts embedded attributes
	 * @param embedded - The embedded element to be scripted
	 * @param sj - The StringJoiner with the SQL script.
	 */
	private void processEmbedded(Embedded embedded, StringJoiner sj) 
			throws IOException {
		Embeddable embeddable = embedded.getEmbeddable();
		scriptTableContents(embeddable.getAttributes(), sj, 
				embedded.getAttributeOverride(), null);
	}
	
	/**
	 * Scripts association attributes
	 * @param attributesOverride - The list of attribute overrides.
	 * @param attributes - The annotated attributes of the element scripted
	 * @param sj - The StringJoiner with the SQL script.
	 */
	private void processAssociations(List<AttributeOverride> attributesOverride, 
			AnnotatedAttributes attributes, StringJoiner sj) {
		for (OneToOne oneToOne : attributes.getOneToOne()) {
			if (!associationIsID(oneToOne.getName(), attributes.getId())) {
				for (Entity referencedEntities : oneToOne.getEntity()
						.getReferencedEntityInRelationship()) {
					relationshipScripter.scriptRelationships(oneToOne.getJoinColumn(),
							referencedEntities, sj, attributesOverride, 
							"create_column", null);
				}
			}
		}
		for (ManyToOne manyToOne : attributes.getManyToOne()) {
			if (!associationIsID(manyToOne.getName(), attributes.getId())) {
				for (Entity referencedEntities : manyToOne.getEntity()
						.getReferencedEntityInRelationship()) {
					relationshipScripter.scriptRelationships(manyToOne.getJoinColumn(),
							referencedEntities, sj, attributesOverride, "create_column",
							null);
				}
			}
		}
	}
	
	private boolean associationIsID(String name, List<Id> ids) {
		for (Id id : ids) {
			if (id.getName().equals(name)) {
				return true;
			}
		}
		return false;
	}

}
