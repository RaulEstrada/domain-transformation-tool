package output.bbdd;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.StringJoiner;

import input.bbdd.DatabaseType;
import model.associations.ManyToOne;
import model.customtypes.InheritanceType;
import model.inheritance.Inheritance;
import model.structure.Attributes;
import model.structure.Entity;
import model.structure.Id;
import model.structure.Index;
import output.bbdd.scripters.EntitiesForeignKeyScripter;
import output.bbdd.scripters.ForeignKeyScripter;
import output.bbdd.scripters.TableContentsScripter;
import output.bbdd.utils.PropertyReader;
import output.bbdd.utils.Utils;

/**
 * Class that generates the SQL script to create the database, table, relations 
 * and other elements from the intermediate model structure.
 * @author Raul Estrada
 *
 */
public class Scripter {
	/**
	 * Reader to load the SQL statements depending on the key and the database type.
	 */
	private PropertyReader properties;

	/**
	 * Initializes the scripter with a target database type
	 * @param databaseType - The target database type
	 * @throws IOException - Could not load the properties file
	 */
	public Scripter(DatabaseType databaseType) throws IOException {
		if (databaseType == null) {
			throw new IllegalArgumentException("Cannot create a SQL script if "
					+ "the database type is missing");
		}
		this.properties = new PropertyReader(databaseType);
	}

	/**
	 * Generates the script content of the output.sql file by iterating over the
	 *  entities and their relations.
	 * @param writer - Element to write in the output.sql file
	 * @param rootPackage - The root package from the intermediate model 
	 * containing all the entities and subpackages to script.
	 * @throws IOException - Error while writing the file or accessing the 
	 * properties file.
	 */
	public void script(BufferedWriter writer, model.structure.Package rootPackage)
			throws IOException {
		Set<Entity> entities = rootPackage.getAllEntitiesInPackageTree();
		Set<Entity> processedEntities = processInheritance(entities);
		Map<String, Entity> map = new Utils().getEntitiesMap(processedEntities);
		ForeignKeyScripter foreigKeyScripter = 
				new ForeignKeyScripter(this.properties, writer, map);
		TableContentsScripter tableScripter = 
				new TableContentsScripter(properties, map);
		for (Entity entity : processedEntities) {
			scriptEntityContent(writer, entity, tableScripter);
		}
		new EntitiesForeignKeyScripter(foreigKeyScripter, writer, properties, map)
			.scriptEntitiesForeignKeys(rootPackage);
	}
	
	/**
	 * Creates the database script to generate the inner elements of the entity
	 * @param writer - The BufferedWriter to write in the .sql file
	 * @param entity - The entity whose inner elements are to be scripted.
	 * @param tableScripter - The element that scripts table elements
	 * @throws IOException
	 */
	private void scriptEntityContent(BufferedWriter writer, Entity entity, 
			TableContentsScripter tableScripter) throws IOException {
		StringJoiner stringJoiner = tableScripter.scriptTableContents(
				entity.getAttributes(), new StringJoiner(",\n"), new ArrayList<>(), entity);
		String tableName = 
				entity.getTable() != null ? entity.getTable().getName() : null;
		tableName = (tableName != null && !tableName.isEmpty()) ? 
				tableName : entity.getName();
		writer.write(properties.getProperty("create_table", tableName, 
				stringJoiner.toString()) + "\n");
		if (entity.getTable() != null) {
			for (Index index : entity.getTable().getIndex()) {
				String indexName =  
						(index.getName() != null && !index.getName().isEmpty()) 
						? index.getName() : "idx_" + index.getColumnList();
				writer.write(properties.getProperty("create_index", indexName, 
						tableName, index.getColumnList()));
			}
		}
	}
	
	/**
	 * Processes inheritance in a set of intermediate model entities.
	 * @param entities - The collection of intermediate model entities.
	 * @return - The new updated entities.
	 */
	private Set<Entity> processInheritance(Set<Entity> entities) {
		Set<Entity> updatedEntities = new HashSet<>();
		Set<Entity> entitiesToFurtherProcess = new HashSet<>();
		for (Entity entity : entities) {
			processEntity(entity, updatedEntities, entitiesToFurtherProcess);
		}
		if (!entitiesToFurtherProcess.isEmpty()) {
			updatedEntities.addAll(processInheritance(entitiesToFurtherProcess));
		}
		return updatedEntities;
	}
	
	/**
	 * Processes inheritance of a single given entity.
	 * @param entity - The entity to be processed
	 * @param updatedEntities - The collection of new updated entities
	 * @param entitiesToFurtherProcess - Collection of entities to be processed
	 */
	private void processEntity(Entity entity, Set<Entity> updatedEntities,
			Set<Entity> entitiesToFurtherProcess) {
		Inheritance inheritance = entity.getInheritance();
		if (inheritance != null) {
			InheritanceType inheritanceType = inheritance.getStrategy();
			inheritanceType = inheritanceType != null ? inheritanceType : InheritanceType.SINGLE_TABLE;
			switch (inheritanceType) {
			case SINGLE_TABLE: 
				scriptSingleTable(updatedEntities, entity, entitiesToFurtherProcess);
				break;
			case JOINED: 
				scriptJoined(updatedEntities, entity, entitiesToFurtherProcess);
				break;
			case TABLE_PER_CLASS: 
				scriptTablePerClass(updatedEntities, entity, entitiesToFurtherProcess);
				break;
			default: throw new RuntimeException("Inheritance type not recognized: " + inheritanceType);
			}
		} else {
			Attributes attr = entity.getAttributes();
			if (!attr.getId().isEmpty() || !attr.getBasic().isEmpty() || !attr.getVersion().isEmpty() || !attr.getEmbedded().isEmpty()
					|| !attr.getOneToMany().isEmpty() || !attr.getOneToOne().isEmpty() || !attr.getManyToMany().isEmpty() || 
					!attr.getManyToOne().isEmpty()) {
				updatedEntities.add(entity);
			}
		}
	}
	
	/**
	 * Scripts single table inheritance strategy.
	 * @param updatedEntities - Collection of new updated entities
	 * @param entity - Current entity being processed
	 * @param entitiesToFurtherProcess - Collection of entities to be processed
	 */
	private void scriptSingleTable(Set<Entity> updatedEntities, Entity entity, 
			Set<Entity> entitiesToFurtherProcess) {
		for (Entity childEntity : entity.getChildrenEntity()) {
			entity.getAttributes().getId().addAll(childEntity.getAttributes().getId());
			childEntity.getAttributes().getId().clear();
			entity.getAttributes().getVersion().addAll(childEntity.getAttributes().getVersion());
			childEntity.getAttributes().getVersion().clear();
			entity.getAttributes().getBasic().addAll(childEntity.getAttributes().getBasic());
			childEntity.getAttributes().getBasic().clear();
			entity.getAttributes().getEmbedded().addAll(childEntity.getAttributes().getEmbedded());
			childEntity.getAttributes().getEmbedded().clear();
			entity.getAttributes().getOneToOne().addAll(childEntity.getAttributes().getOneToOne());
			childEntity.getAttributes().getOneToOne().clear();
			entity.getAttributes().getOneToMany().addAll(childEntity.getAttributes().getOneToMany());
			childEntity.getAttributes().getOneToMany().clear();
			entity.getAttributes().getManyToOne().addAll(childEntity.getAttributes().getManyToOne());
			childEntity.getAttributes().getManyToOne().clear();
			entity.getAttributes().getManyToMany().addAll(childEntity.getAttributes().getManyToMany());
			childEntity.getAttributes().getManyToMany().clear();
			updatedEntities.remove(childEntity);
		}
		if (entity.getParentEntity() != null && entity.getParentEntity().getInheritance() != null && 
				(entity.getParentEntity().getInheritance().getStrategy() == null || 
						entity.getParentEntity().getInheritance().getStrategy() == InheritanceType.SINGLE_TABLE)) {
			entitiesToFurtherProcess.add(entity.getParentEntity());
		}
		updatedEntities.add(entity);
	}
	
	/**
	 * Scripts joined inheritance strategy.
	 * @param updatedEntities - Collection of new updated entities
	 * @param entity - Current entity being processed
	 * @param entitiesToFurtherProcess - Collection of entities to be processed
	 */
	private void scriptJoined(Set<Entity> updatedEntities, Entity entity, 
			Set<Entity> entitiesToFurtherProcess) {
		for (Entity childEntity : entity.getChildrenEntity()) {
			for (Id id : entity.getAttributes().getId()) {
				Id newId = new Id();
				newId.setAccess(id.getAccess());
				newId.setColumn(id.getColumn());
				newId.setGeneratedValue(id.getGeneratedValue());
				newId.setName(id.getName());
				ManyToOne m2o = new ManyToOne();
				m2o.setEntity(entity);
				m2o.setTargetEntity(entity.getClazz());
				newId.setPrimaryKeyJoinColumnParentEntity(m2o);
				newId.setSequenceGenerator(id.getSequenceGenerator());
				newId.setTableGenerator(id.getTableGenerator());
				newId.setTemporal(id.getTemporal());
				newId.setTypeName(id.getTypeName());
				childEntity.getAttributes().getId().add(newId);
				entitiesToFurtherProcess.add(childEntity);
			}
		}
		updatedEntities.add(entity);
	}
	
	/**
	 * Scripts table per class inheritance strategy.
	 * @param updatedEntities - Collection of new updated entities
	 * @param entity - Current entity being processed
	 * @param entitiesToFurtherProcess - Collection of entities to be processed
	 */
	private void scriptTablePerClass(Set<Entity> updatedEntities, Entity entity, 
			Set<Entity> entitiesToFurtherProcess) {
		for (Entity childEntity : entity.getChildrenEntity()) {
			childEntity.getAttributes().getId().addAll(entity.getAttributes().getId());
			childEntity.getAttributes().getVersion().addAll(entity.getAttributes().getVersion());
			childEntity.getAttributes().getBasic().addAll(entity.getAttributes().getBasic());
			childEntity.getAttributes().getEmbedded().addAll(entity.getAttributes().getEmbedded());
			childEntity.getAttributes().getManyToMany().addAll(entity.getAttributes().getManyToMany());
			childEntity.getAttributes().getManyToOne().addAll(entity.getAttributes().getManyToOne());
			childEntity.getAttributes().getOneToMany().addAll(entity.getAttributes().getOneToMany());
			childEntity.getAttributes().getOneToOne().addAll(entity.getAttributes().getOneToOne());
			entitiesToFurtherProcess.add(childEntity);
		}
		entity.getAttributes().getId().clear();
		entity.getAttributes().getVersion().clear();
		entity.getAttributes().getBasic().clear();
		entity.getAttributes().getEmbedded().clear();
		entity.getAttributes().getOneToOne().clear();
		entity.getAttributes().getOneToMany().clear();
		entity.getAttributes().getManyToOne().clear();
		entity.getAttributes().getManyToMany().clear();
		updatedEntities.remove(entity);
	}

}
