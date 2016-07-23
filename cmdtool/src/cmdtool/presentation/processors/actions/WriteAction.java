package cmdtool.presentation.processors.actions;

import java.io.IOException;
import java.util.List;

import cmdtool.presentation.UserInputValues;
import cmdtool.presentation.utils.PropertyReader;
import cmdtool.presentation.utils.Utils;
import input.bbdd.DatabaseType;
import output.Writer;
import output.WriterFactoryImpl;

public class WriteAction {
	private PropertyReader properties;
	private Utils utils;
	
	public WriteAction() throws IOException {
		this.properties = new PropertyReader();
		this.utils = new Utils();
	}
	
	public Writer obtainWriter(List<String> input) {
		Writer writer = null;
		switch(input.get(1)) {
		case UserInputValues.DATABASE: 
			writer = obtainDatabaseWriter(input);
			break;
		case UserInputValues.UML: 
			if (utils.checkNumberArgumentsOutputDirectory(input)) {
				writer = WriterFactoryImpl.getInstance().getUMLWriter();
			}
			break;
		case UserInputValues.JAVA: 
			if (utils.checkNumberArgumentsOutputDirectory(input)){
				writer = WriterFactoryImpl.getInstance().getJavaWriter();
			}
			break;
		case UserInputValues.ORM:
			if (utils.checkNumberArgumentsOutputDirectory(input)) {
				writer = WriterFactoryImpl.getInstance().getORMWriter();
			}
			break;
		default: 
			System.out.println(properties.getProperty("writeTypeNotRecognized", 
					input.get(1)));
			System.out.println(properties.getProperty("writeCommandStructure"));
		}
		return writer;
	}
	
	private Writer obtainDatabaseWriter(List<String> input) {
		if (utils.checkNumberArgumentsSQLOutput(input)) {
			DatabaseType type = null;
			try{
				type = DatabaseType.valueOf(input.get(2));
			} catch (IllegalArgumentException ex) {
				System.out.println(properties.getProperty("writeSQLCommandStructure"));
				return null;
			}
			switch (type) {
			case HSQLDB: return WriterFactoryImpl.getInstance().getHSQLDBSQLWriter();
			case MYSQL: return WriterFactoryImpl.getInstance().getMySQLSQLWriter();
			case ORACLE: return WriterFactoryImpl.getInstance().getOracleSQLWriter();
			case POSTGRESQL: return WriterFactoryImpl.getInstance().getPostgreSQLWriter();
			default: throw new RuntimeException("Database type not supported: " + type);
			}
		}
		return null;
	}
}
