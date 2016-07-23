package output.bbdd.utils;

import input.bbdd.DatabaseType;

/**
 * Class that takes a Java data type and returns the SQL data type as a string,
 * depending on the database type.
 * @author Raul Estrada
 *
 */
public class Java2SQLDataConverter {
	/**
	 * Converts a Java data type to its corresponding SQL data type, taking
	 * into consideration the target database type
	 * @param dataClassName - The java data type name
	 * @param databaseType - The target database type.
	 * @return - The SQL data type name
	 */
	public String convert(String dataClassName, DatabaseType databaseType) {
		if (dataClassName == null || databaseType == null) {
			throw new IllegalArgumentException("Cannot convert a null data "
					+ "class name or to a null database type");
		}
		switch (databaseType) {
		case MYSQL:
		case POSTGRESQL:
		case HSQLDB:return convertOthers(dataClassName);
		case ORACLE: return convertOracle(dataClassName);
		default:
			throw new RuntimeException("Database type not supported: " 
					+ databaseType);
		}
	}
	
	/**
	 * Converts a Java data type to its corresponding SQL data type
	 * @param dataClassName - The java data type name
	 * @return - The SQL data type name
	 */
	private String convertOthers(String dataClassName) {
		switch(dataClassName) {
		case "java.lang.Byte": return "tinyint";
		case "java.lang.Long": return "bigint";
		case "java.lang.Integer": return "integer";
		case "java.lang.String": return "varchar(255)";
		case "java.util.Date": return "date";
		case "java.lang.Double":
		case "java.lang.Float": return "decimal";
		default: throw new RuntimeException("Data class name not recognized: " 
				+ dataClassName);
		}
	}
	
	/**
	 * Converts a Java data type to its corresponding SQL data type
	 * @param dataClassName - The java data type name
	 * @return - The SQL data type name
	 */
	private String convertOracle(String dataClassName) {
		switch(dataClassName) {
		case "java.lang.Byte":
		case "java.lang.Long":
		case "java.lang.Integer": return "number";
		case "java.lang.String": return "varchar2(4000)";
		case "java.util.Date": return "date";
		case "java.lang.Double":
		case "java.lang.Float": return "decimal";
		default: throw new RuntimeException("Data class name not recognized: " 
				+ dataClassName);
		}
	}
}
