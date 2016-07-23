package cmdtool.presentation.utils;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cmdtool.presentation.UserInputValues;
import input.bbdd.DatabaseType;

public class Utils {
	private PropertyReader properties;
	
	public Utils() throws IOException{
		this.properties = new PropertyReader();
	}
	
	public Map<String, String> getDatabaseInputOptions(List<String> input) {
		Map<String, String> options = new HashMap<>();
		for (int i = 2; i < input.size(); i++) {
			String[] parts = input.get(i).split("=");
			if (parts.length != 2) {
				return null;
			}
			switch(parts[0]) {
			case UserInputValues.DB_TYPE: 
				options.put(UserInputValues.DB_TYPE, parts[1]); break;
			case UserInputValues.DB_HOST: 
				options.put(UserInputValues.DB_HOST, parts[1]); break;
			case UserInputValues.DB_PORT: 
				options.put(UserInputValues.DB_PORT, parts[1]); break;
			case UserInputValues.DB_NAME: 
				options.put(UserInputValues.DB_NAME, parts[1]); break;
			case UserInputValues.DB_USERNAME: 
				options.put(UserInputValues.DB_USERNAME, parts[1]); break;
			case UserInputValues.DB_PASSWORD: 
				options.put(UserInputValues.DB_PASSWORD, parts[1]); break;
			default: return null;
			}
		}
		return options;
	}
	
	public boolean checkNumberArgumentsReaderDirectory(List<String> options) {
		if (options.size() != 3) {
			System.out.println(properties.getProperty(
					"readCommandDirectoryFormatException", options.get(1)));
			return false;
		}
		return true;
	}
	
	public boolean checkNumberArgumentsOutputDirectory(List<String> input) {
		if (input.size() != 3) {
			System.out.println(properties.getProperty("writeCommandInvalidFormat"));
			System.out.println(properties.getProperty("writeCommandStructure"));
			return false;
		}
		return true;
	}
	
	public boolean checkNumberArgumentsSQLOutput(List<String> input) {
		if (input.size() != 4) {
			System.out.println(properties.getProperty("writeSQLInvalidFormat"));
			System.out.println(properties.getProperty("writeSQLCommandStructure"));
			return false;
		}
		return true;
	}
	
	public DatabaseType getDatabaseType(Map<String, String> options) {
		String dbTypeStr = options.get(UserInputValues.DB_TYPE);
		try {
			return DatabaseType.valueOf(dbTypeStr);
		} catch (IllegalArgumentException ex) {
			System.out.println(properties.getProperty("readDBCommandStructure"));
			return null;
		}
	}
}
