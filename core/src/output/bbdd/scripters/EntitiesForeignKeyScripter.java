package output.bbdd.scripters;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;

import model.associations.JoinTable;
import model.associations.ManyToMany;
import model.associations.ManyToOne;
import model.associations.OneToMany;
import model.associations.OneToOne;
import model.structure.Entity;
import model.structure.Id;
import model.structure.Package;
import output.bbdd.utils.PropertyReader;
import output.bbdd.utils.Utils;

/**
 * This class creates the necessary SQL code to add the relationships between 
 * the different entities or 
 * tables. In the case of OneToOne or ManyToOne, such goal is achieved by adding
 *  foreign keys. In the case 
 * of ManyToMany relationships, an intermediate table is created.
 * @author Raul Estrada
 *
 */
public class EntitiesForeignKeyScripter {
	/**
	 * Element that creates the foreign keys between tables
	 */
	private ForeignKeyScripter foreignKeyScripter;
	/**
	 * Element to write in the output.sql file
	 */
	private BufferedWriter writer;
	/**
	 * Element that loads the SQL statements.
	 */
	private PropertyReader properties;
	/**
	 * Element that creates the primary keys of a ManyToMany relationship from 
	 * the JoinTable object.
	 */
	private RelationshipScripter relationshipScripter;
	private Utils utils = new Utils();
	private Map<String, Entity> entitiesMap;

	/**
	 * Initializes the scripter with a foreign key scripter, a buffered writer 
	 * and a properties reader.
	 * @param foreignKeyScripter - Element that creates the foreign keys 
	 * between tables
	 * @param writer - Element to write in the output.sql file
	 * @param properties - Element that loads the SQL statements.
	 */
	public EntitiesForeignKeyScripter(ForeignKeyScripter foreignKeyScripter, 
			BufferedWriter writer, PropertyReader properties, Map<String, Entity> entitiesMap) {
		if (foreignKeyScripter == null || writer == null || properties == null) {
			throw new IllegalArgumentException("Cannot create the foreign keys "
					+ "of entities with a null foreign key scripter or buffered "
					+ "writer");
		}
		this.foreignKeyScripter = foreignKeyScripter;
		this.writer = writer;
		this.relationshipScripter = new RelationshipScripter(properties, entitiesMap);
		this.properties = properties;
		this.entitiesMap = entitiesMap;
	}

	/**
	 * Generates the SQL code to create the different associations between 
	 * tables in the database.
	 * @param rootPackage - The root package in the intermediate model
	 * @throws IOException
	 */
	public void scriptEntitiesForeignKeys(Package rootPackage) throws IOException {
		for (Entity entity : rootPackage.getAllEntitiesInPackageTree()) {
			String tableName = entity.getTable() != null ? 
					tableName = entity.getTable().getName() : null;
			tableName = (tableName != null && !tableName.isEmpty()) ? 
					tableName : entity.getName();
			for (OneToOne oneToOne : entity.getAttributes().getOneToOne()) {
				scriptOneToOne(tableName, entity, oneToOne);
			}
			for (ManyToOne manyToOne : entity.getAttributes().getManyToOne()) {
				scriptManyToOne(tableName, entity, manyToOne);
			}
			for (OneToMany oneToMany : entity.getAttributes().getOneToMany()) {
				if (oneToMany.getJoinTable() != null) {
					createJoinTableScript(oneToMany.getEntity()
							.getReferencedEntityInRelationship(),
							oneToMany.getJoinTable(), entity, tableName);
				}
			}
			for (ManyToMany manyToMany : entity.getAttributes().getManyToMany()) {
				scriptManyToMany(manyToMany, writer, entity, tableName);
			}
		}
	}

	/**
	 * Creates the SQL code necessary to generate the ManyToMany relationship 
	 * in the database.
	 * @param manyToMany - The ManyToMany association
	 * @param writer - Element that writes in the output.sql file
	 * @param entity - Entity owning the ManyToMany object
	 * @param tableName - The table name of the entity owning the ManyToMany 
	 * association.
	 * @throws IOException
	 */
	private void scriptManyToMany(ManyToMany manyToMany, BufferedWriter writer, 
			Entity entity, String tableName) throws IOException {
		if (manyToMany.getMappedBy() == null || manyToMany.getMappedBy().isEmpty()) { 
			/*
			 * The join table for the relationship is specified in the owning side of the association
			 * The non-owning side of the relationship must use the mapped by attribute to link to the owning part of the 
			 * association
			 */
			createJoinTableScript(manyToMany.getEntity()
					.getReferencedEntityInRelationship(), 
					manyToMany.getJoinTable(), entity, tableName);
		}
	}
	
	/**
	 * Scripts a one to one association
	 * @param tableName - Name of the associative table
	 * @param entity - Current entity
	 * @param oneToOne - One To One association
	 * @throws IOException
	 */
	private void scriptOneToOne(String tableName, Entity entity, 
			OneToOne oneToOne) throws IOException {
		if (oneToOne.getJoinTable() != null) {
			createJoinTableScript(oneToOne.getEntity()
					.getReferencedEntityInRelationship(),
					oneToOne.getJoinTable(), entity, tableName);
		} else {
			for (Entity referencedEntity : oneToOne.getEntity()
					.getReferencedEntityInRelationship()) {
				foreignKeyScripter.scriptForeignKeys(referencedEntity, 
						oneToOne.getJoinColumn(), tableName);
			}
		}
	}
	
	/**
	 * Scripts a many to one association
	 * @param tableName - Name of the associative table
	 * @param entity - Current entity
	 * @param manyToOne - Many To One association
	 * @throws IOException
	 */
	private void scriptManyToOne(String tableName, Entity entity, 
			ManyToOne manyToOne) throws IOException {
		if (manyToOne.getJoinTable() != null) {
			createJoinTableScript(manyToOne.getEntity()
					.getReferencedEntityInRelationship(), 
					manyToOne.getJoinTable(), entity, tableName);
		} else {
			for (Entity referencedEntity : manyToOne.getEntity()
					.getReferencedEntityInRelationship()) {
				foreignKeyScripter.scriptForeignKeys(referencedEntity, 
						manyToOne.getJoinColumn(), tableName);
			}
		}
	}
	
	/**
	 * Scripts the join associative table
	 * @throws IOException
	 */
	private void createJoinTableScript(List<Entity> refEntities, 
			JoinTable joinTable, Entity entity, String tableName) 
					throws IOException {
		for (Entity refEntity : refEntities) {
			String refTbleName = refEntity.getTable() != null ? 
					refEntity.getTable().getName() : null;
			refTbleName = (refTbleName != null && !refTbleName.isEmpty()) ? 
					refTbleName : refEntity.getName();
			StringJoiner stringJoiner = new StringJoiner(",\n");
			String intermediateTableName = null;
			StringJoiner keysJoiner = new StringJoiner(",");
			if (joinTable != null) {
				intermediateTableName = joinTable.getName();
			}
			processJoinTable(stringJoiner, joinTable, keysJoiner, entity, refEntity);
			stringJoiner.add("\t" + properties.getProperty("create_primary_key",
					keysJoiner.toString()));
			intermediateTableName = intermediateTableName != null ? 
					intermediateTableName : tableName + "_" + refTbleName;
			writer.write(properties.getProperty("create_table", 
					intermediateTableName, stringJoiner.toString()) + "\n");
			foreignKeyScripter.scriptForeignKeys(entity, null, intermediateTableName);
			foreignKeyScripter.scriptForeignKeys(refEntity, null, intermediateTableName);
		}
	}
	
	private void processJoinTable(StringJoiner stringJoiner, JoinTable joinTable,
			StringJoiner keysJoiner, Entity entity, Entity refEntity) {
		if (joinTable != null && joinTable.getJoinColumn() != null && 
				!joinTable.getJoinColumn().isEmpty()) {
			relationshipScripter.scriptRelationships(joinTable.getJoinColumn(),
					entity, stringJoiner, null, "create_column", keysJoiner);
		} else {
			for (Id id : entity.getIDsWithParent()) {
				keysJoiner.add(entity.getName().toLowerCase() + "_" + 
						utils.getIDColumnName(null, id, entitiesMap, entity));
				utils.createIdColumn(null, id, stringJoiner, "create_column",
						entity.getName().toLowerCase() + "_", properties, entitiesMap,
						entity);
			}
		}
		if (joinTable != null && joinTable.getInverseJoinColumn() != null 
				&& !joinTable.getInverseJoinColumn().isEmpty()) {
			relationshipScripter.scriptRelationships(
					joinTable.getInverseJoinColumn(), refEntity, stringJoiner,
					null, "create_column", keysJoiner);
		} else {
			for (Id id : refEntity.getIDsWithParent()) {
				keysJoiner.add(refEntity.getName().toLowerCase() + "_" + 
						utils.getIDColumnName(null, id, entitiesMap, refEntity));
				utils.createIdColumn(null, id, stringJoiner, "create_column",
						refEntity.getName().toLowerCase() + "_", properties, 
						entitiesMap, refEntity);
			}
		}
	}
}
