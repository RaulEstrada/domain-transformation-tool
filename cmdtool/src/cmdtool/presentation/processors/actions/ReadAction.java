package cmdtool.presentation.processors.actions;

import java.io.IOException;
import java.net.MalformedURLException;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import cmdtool.presentation.UserInputValues;
import cmdtool.presentation.utils.PropertyReader;
import cmdtool.presentation.utils.Utils;
import input.ReaderFactoryImpl;
import input.bbdd.DatabaseType;
import input.uml.exceptions.NoUMLFileException;
import model.structure.Package;

public class ReadAction {
	private PropertyReader properties;
	private Utils utils;
	
	public ReadAction() throws IOException {
		this.properties = new PropertyReader();
		this.utils = new Utils();
	}
	
	public Package read(List<String> input) throws SQLException, 
		MalformedURLException, ClassNotFoundException, IOException, NoUMLFileException {
		if (input.size() < 2) {
			System.out.println(properties.getProperty("inputCommandInvalidFormat"));
			System.out.println(properties.getProperty("readCommandStructure"));
			return null;
		} else {
			Package intermediateRootPackage = null;
			String type = input.get(1);
			switch (type) {
			case UserInputValues.DATABASE: 
				intermediateRootPackage = loadModelFromDatabase(input);
				break;
			case UserInputValues.UML: 
				if (utils.checkNumberArgumentsReaderDirectory(input)) {
					intermediateRootPackage = ReaderFactoryImpl.getInstance()
							.getUMLReader().loadModel(input.get(2));
				}
				break;
			case UserInputValues.CLASS: 
				if (utils.checkNumberArgumentsReaderDirectory(input)) {
					intermediateRootPackage = ReaderFactoryImpl.getInstance()
							.getClassReader().loadModel(input.get(2));
				}
				break;
			case UserInputValues.JAVA: 
				if (utils.checkNumberArgumentsReaderDirectory(input)) {
					intermediateRootPackage = ReaderFactoryImpl.getInstance()
							.getJavaReader().loadModel(input.get(2)); 
				}
				break;
			case UserInputValues.DSL: 
				if (utils.checkNumberArgumentsReaderDirectory(input)) {
					intermediateRootPackage = ReaderFactoryImpl.getInstance()
							.getDSLReader().loadModel(input.get(2)); 
				}
				break;
			default: 
				System.out.println(properties.getProperty("readTypeNotRecognized", type));
				System.out.println(properties.getProperty("readCommandStructure"));
			}
			return intermediateRootPackage;
		}
	}
	
	private Package loadModelFromDatabase(List<String> input)
			throws SQLException, ClassNotFoundException {
		Map<String, String> options = utils.getDatabaseInputOptions(input);
		if (options == null || options.size() != 6) {
			System.out.println(properties.getProperty("readDBCommandStructure"));
		} else {
			DatabaseType dbType = utils.getDatabaseType(options);
			String host = options.get(UserInputValues.DB_HOST);
			String port = options.get(UserInputValues.DB_PORT);
			String name = options.get(UserInputValues.DB_NAME);
			String username = options.get(UserInputValues.DB_USERNAME);
			String password = options.get(UserInputValues.DB_PASSWORD);
			switch(dbType) {
			case HSQLDB: return ReaderFactoryImpl.getInstance().getHSQLDatabaseReader()
					.loadModel(host, port, name, username, password);
			case MYSQL: return ReaderFactoryImpl.getInstance().getMySQLDatabaseReader()
					.loadModel(host, port, name, username, password);
			case ORACLE: return ReaderFactoryImpl.getInstance().getOracleDatabaseReader()
					.loadModel(host, port, name, username, password);
			case POSTGRESQL: return ReaderFactoryImpl.getInstance().getPostGreSQLDatabaseReader()
					.loadModel(host, port, name, username, password);
			}
		}
		return null;
	}
}
