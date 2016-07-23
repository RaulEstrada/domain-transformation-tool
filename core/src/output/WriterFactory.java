package output;

public interface WriterFactory {
	/**
	 * obtains and returns a writer that generates the ORM persistence 
	 * configuration file from the loaded model.
	 */
	public Writer getORMWriter();
	
	/**
	 * obtains and returns a writer that generates a UML model file from the 
	 * loaded model.
	 */
	public Writer getUMLWriter();
	
	/**
	 * obtains and returns a writer that generates a SQL script for Oracle 
	 * databases from the loaded model
	 */
	public Writer getOracleSQLWriter();
	
	/**
	 * obtains and returns a writer that generates a SQL script for MySQL 
	 * databases from the loaded model.
	 */
	public Writer getMySQLSQLWriter();
	
	/**
	 * obtains and returns a writer that generates a SQL script for PostgreSQL 
	 * databases from the loaded model.
	 */
	public Writer getPostgreSQLWriter();
	
	/**
	 * obtains and returns a writer that generates a SQL script for HSQL 
	 * databases from the loaded model.
	 */
	public Writer getHSQLDBSQLWriter();
	
	/**
	 * obtains and returns a writer that generates java source code files from 
	 * the loaded model.
	 */
	public Writer getJavaWriter();
}
