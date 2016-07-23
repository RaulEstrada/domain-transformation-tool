package input.bbdd.utils;

import input.bbdd.DatabaseType;

public class DBReaderUtils {
	/**
	 * Gets the corresponding URL to connect to the database.
	 * @param dbType - The type of the database.
	 * @param host - The host to connect to the database.
	 * @param port - The port to connect to the database.
	 * @param databaseName - The name of the database
	 * @return - The URL to connect to the database.
	 */
	public String getUpdatedURL(DatabaseType dbType, String host, String port, 
			String databaseName) {
		String updated = dbType.getLabel() + host + ":" + port;
		switch(dbType) {
		case ORACLE: updated = updated + ":" + databaseName; break;
		case HSQLDB:
		case POSTGRESQL:
		case MYSQL: updated = updated + "/" + databaseName; break;
		default: throw new RuntimeException("Unsupported database type: " + dbType);
		}
		return updated;
	}
}
