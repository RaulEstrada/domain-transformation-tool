package output.bbdd.utils;

import java.io.IOException;
import java.io.InputStream;
import java.text.MessageFormat;
import java.util.Properties;

import input.bbdd.DatabaseType;

/**
 * This class loads a specific property file depending on the target database 
 * type used
 * and is in charge of getting and returning the different SQL statements 
 * (properties)
 * @author Raul Estrada
 *
 */
public class PropertyReader {
	/**
	 * The persistent set of properties (key-value pair of SQL statements)
	 */
	private Properties properties = new Properties();
	/**
	 * Base name of the property files containing the SQL statements
	 */
	private static final String BASE_NAME = "configuration.sql_";
	/**
	 * Extension of the property files.
	 */
	private static final String FILE_EXTENSION = ".properties";
	/**
	 * The target database type used in this reader
	 */
	private final DatabaseType databaseType;

	/**
	 * Instantiates the property reader object and loads one file or another, 
	 * depending on the target database type passed as a parameter
	 * @param db - The target database type
	 * @throws IOException - Could not load the properties file
	 */
	public PropertyReader(DatabaseType db) throws IOException {
		this.databaseType = db;
		String fileName = BASE_NAME + db.getPropertyExtension() + FILE_EXTENSION;
		InputStream is = getClass().getClassLoader().getResourceAsStream(fileName);
		if (is != null) {
			this.properties.load(is);
		} else {
			throw new RuntimeException("Could not load properties file");
		}
	}
	
	/**
	 * Gets and returns a property (SQL statement) from its key
	 * @param key - The key of the SQL statement
	 * @return - The SQL statement
	 */
	public String getProperty(String key) {
		return properties.getProperty(key);
	}
	
	/**
	 * Gets and returns a property (SQL statement) from its key, setting the 
	 * different parameters in the SQL statement
	 * @param key - The key of the SQL statement
	 * * @param params - The parameters for the SQL statement
	 * @return - The SQL statement
	 */
	public String getProperty(String key, Object... params) {
		return MessageFormat.format(getProperty(key), params);
	}
	
	/**
	 * Gets and returns the target database type
	 * @return - The target database type
	 */
	public DatabaseType getDatabaseType() {
		return this.databaseType;
	}
}
