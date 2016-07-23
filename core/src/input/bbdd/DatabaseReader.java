package input.bbdd;

import java.net.MalformedURLException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import input.Reader;
import input.bbdd.utils.DBReaderUtils;
import model.structure.Entity;
import model.structure.Package;

public class DatabaseReader implements Reader {
	private DatabaseType databaseType;
	private DBReaderUtils utils = new DBReaderUtils();
	private Map<String, Entity> entityMap = new HashMap<>();
	private Map<Entity, List<String>> keysInManyToMany;
	
	/**
	 * Constructor that initializes the reader and where the client specifies 
	 * the type of database to connect to.
	 * @param databaseType - The type of the database
	 */
	public DatabaseReader(DatabaseType databaseType) {
		if (databaseType == null) {
			throw new IllegalArgumentException("Cannot create a database reader"
					+ " if the database type is missing");
		}
		this.databaseType = databaseType;
	}

	/**
	 * It throws an exception since this loading method is not supported by the
	 *  database loading module.
	 */
	@Override
	public Package loadModel(String directory) throws MalformedURLException, 
	ClassNotFoundException {
		throw new UnsupportedOperationException("Database Reader does not "
				+ "support loading models from a directory path");
	}

	/**
	 * It creates a connection to the database, reads the database metadata, 
	 * builds the intermediate model and returns its root element.
	 */
	@Override
	public Package loadModel(String host, String port, String databaseName, 
			String username, String password) throws SQLException, ClassNotFoundException {
		if (databaseType == null || host == null || host.isEmpty() || 
				username == null || password == null || port == null || 
				databaseName == null || databaseName.isEmpty()) {
			throw new IllegalArgumentException("Cannot load a model if the "
					+ "database host, port, database name, username or password"
					+ " are missing");
		}
		Connection connection = null;
		try {
			Class.forName(databaseType.getDriverName());
			String url = utils.getUpdatedURL(databaseType, host, port, databaseName);
			connection = DriverManager.getConnection(url, username, password);
			return doRead(connection);
		} catch (SQLException ex) {
			throw ex;
		} finally {
			if (connection != null && !connection.isClosed()) {connection.close();}
		}
	}
	
	/**
	 * It reads the database metadata, builds the intermediate model and 
	 * returns its root element.
	 * @param connection - The connection to the database
	 * @return - The root element of the intermediate loaded model.
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	private Package doRead(Connection connection) throws ClassNotFoundException, 
		SQLException {
		DatabaseMetaData dbMetadata = connection.getMetaData();
		TableReader tableReader = new TableReader();
		this.entityMap = tableReader.readTables(dbMetadata);
		List<String> tableKeys = this.readForeignKeys(dbMetadata);
		this.keysInManyToMany = 
				new ManyToManyCreator(tableReader.getManyToManyInfoWrappers()
						, entityMap).createManyToManyAssociations();
		this.readTableElements(dbMetadata, tableKeys);
		Package pckg = new Package("root");
		pckg.addAllEntities(entityMap.values());
		return pckg;
	}

	/**
	 * It obtains and returns all the foreign keys of all the entities, that is,
	 *  all the columns in the database used to link or connect tables.
	 * @param dbMetadata - The database metadata
	 * @return - The list of foreign keys
	 * @throws SQLException
	 */
	private List<String> readForeignKeys(DatabaseMetaData dbMetadata) 
			throws SQLException{
		ForeignKeyReader fKeyReader = new ForeignKeyReader();
		List<String> fKeys = new ArrayList<>();
		for (Entity entity : entityMap.values()) {
			fKeys.addAll(fKeyReader.readForeignKeys(dbMetadata, entity, entityMap));
		}
		return fKeys;
	}

	/**
	 * It iterates over all the entities created, and accesses the database 
	 * tables of those entities to process its columns to create the entity 
	 * inner elements in the intermediate model.
	 * @param dbMetadata - The database metadata
	 * @param keyNames - The list of database keys.
	 * @throws SQLException
	 */
	private void readTableElements(DatabaseMetaData dbMetadata, 
			List<String> keyNames) throws SQLException  {
		ColumnReader columnReader = new ColumnReader();
		for (Entity entity : entityMap.values()) {
			keyNames.addAll(entity.getIDNames());
			if (this.keysInManyToMany.containsKey(entity)) {
				keyNames.addAll(this.keysInManyToMany.get(entity));
			}
			columnReader.readColumns(dbMetadata, entity, keyNames);
		}
	}

}
