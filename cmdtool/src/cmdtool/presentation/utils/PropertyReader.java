package cmdtool.presentation.utils;

import java.io.IOException;
import java.io.InputStream;
import java.text.MessageFormat;
import java.util.Properties;

/**
 * This class loads a specific property file 
 * and is in charge of getting and returning the different statements (properties)
 * @author Raul Estrada
 *
 */
public class PropertyReader {
	/**
	 * The persistent set of properties (key-value pair of statements)
	 */
	private Properties properties = new Properties();
	/**
	 * Name of the property files containing the statements
	 */
	private static final String NAME = "MessageProperties.properties";

	/**
	 * Instantiates the property reader object and loads one file or another, depending on the 
	 * target database type passed as a parameter
	 * @param db - The target database type
	 * @throws IOException - Could not load the properties file
	 */
	public PropertyReader() throws IOException {
		InputStream is = getClass().getClassLoader().getResourceAsStream(NAME);
		if (is != null) {
			this.properties.load(is);
		} else {
			throw new RuntimeException("Could not load properties file");
		}
	}

	/**
	 * Gets and returns a property from its key
	 * @param key - The key of the statement
	 * @return - The statement
	 */
	public String getProperty(String key) {
		return properties.getProperty(key);
	}

	/**
	 * Gets and returns a property from its key, setting the different
	 *  parameters in the statement
	 * @param key - The key of the statement
	 * * @param params - The parameters for the statement
	 * @return - The statement
	 */
	public String getProperty(String key, Object... params) {
		return MessageFormat.format(getProperty(key), params);
	}
}
