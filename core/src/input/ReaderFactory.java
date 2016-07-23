package input;

public interface ReaderFactory {
	/**
	 * Obtains and returns a reader capable of loading a model from compiled 
	 * java classes.
	 */
	Reader getClassReader();
	
	/**
	 * Obtains and returns a reader capable of loading a model from source 
	 * code java files.
	 */
	Reader getJavaReader();
	
	/**
	 * Obtains and returns a reader capable of loading the model from a 
	 * UML model file.
	 */
	Reader getUMLReader();
	
	/**
	 * Obtains and returns a reader capable of loading the model from an 
	 * Oracle database.
	 */
	Reader getOracleDatabaseReader();
	
	/**
	 * Obtains and returns a reader capable of loading the model from a 
	 * MySQL database.
	 */
	Reader getMySQLDatabaseReader();
	
	/**
	 * Obtains and returns a reader capable of loading the model from a 
	 * PostgreSQL database.
	 */
	Reader getPostGreSQLDatabaseReader();
	
	/**
	 * Obtains and returns a reader capable of loading the model from a 
	 * HSQL database.
	 */
	Reader getHSQLDatabaseReader();
	
	/**
	 * Obtains and returns a reader capable of loading the model from a DSL file.
	 */
	Reader getDSLReader();
}
