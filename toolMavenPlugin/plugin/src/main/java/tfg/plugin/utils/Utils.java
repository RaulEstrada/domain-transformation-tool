package tfg.plugin.utils;

import java.io.IOException;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.logging.Log;

import input.Reader;
import input.ReaderFactoryImpl;
import input.uml.exceptions.NoUMLFileException;
import model.structure.Package;
import output.Writer;
import output.WriterFactoryImpl;
import tfg.plugin.config.DatabaseInput;
import tfg.plugin.config.DirectoryInput;
import tfg.plugin.config.ToolConfig;
import tfg.plugin.config.TransformationOutputOption;

public class Utils {
	public Reader getDirectoryReader(DirectoryInput input) 
			throws MojoExecutionException {
		switch(input.getType()) {
		case CLASS: return ReaderFactoryImpl.getInstance().getClassReader();
		case DSL: return ReaderFactoryImpl.getInstance().getDSLReader();
		case JAVA: return ReaderFactoryImpl.getInstance().getJavaReader();
		case UML: return ReaderFactoryImpl.getInstance().getUMLReader();
		default: throw new MojoExecutionException("Maven TFG plugin: Directory "
				+ "input type not recognized: " + input.getType());
		}
	}
	
	public Reader getDatabaseReader(DatabaseInput input) 
			throws MojoExecutionException {
		switch(input.getType()) {
		case HSQLDB: return ReaderFactoryImpl.getInstance().getHSQLDatabaseReader();
		case MYSQL: return ReaderFactoryImpl.getInstance().getMySQLDatabaseReader();
		case ORACLE: return ReaderFactoryImpl.getInstance().getOracleDatabaseReader();
		case POSTGRESQL: return ReaderFactoryImpl.getInstance().getPostGreSQLDatabaseReader();
		default: throw new MojoExecutionException("Maven TFG plugin: Database "
				+ "input type not recognized: " + input.getType());
		}
	}
	
	public Writer getWriter(TransformationOutputOption output) 
			throws MojoExecutionException {
		switch(output) {
		case TO_JAVA: 
			return WriterFactoryImpl.getInstance().getJavaWriter();
		case TO_ORM_XML: 
			return WriterFactoryImpl.getInstance().getORMWriter();
		case TO_UML_MODEL: 
			return WriterFactoryImpl.getInstance().getUMLWriter();
		case TO_DATABASE_HSQLDB_SCRIPT: 
			return WriterFactoryImpl.getInstance().getHSQLDBSQLWriter();
		case TO_DATABASE_MYSQL_SCRIPT: 
			return WriterFactoryImpl.getInstance().getMySQLSQLWriter();
		case TO_DATABASE_ORACLE_SCRIPT: 
			return WriterFactoryImpl.getInstance().getOracleSQLWriter();
		case TO_DATABASE_POSTGRESQL_SCRIPT: 
			return WriterFactoryImpl.getInstance().getPostgreSQLWriter();
		default: throw new MojoExecutionException("Maven TFG plugin: "
				+ "Transformation option type not recognized: " + output);
		}
	}
	
	public Package getIntermediateModelFromDirectoryInput(ToolConfig toolConfig,
			Log log) throws MojoExecutionException {
		Reader reader = getDirectoryReader(toolConfig.getDirectoryInput());
		log.info("Maven TFG plugin: Directory input configuration detected");
		log.info("Maven TFG plugin: Input directory " + 
				toolConfig.getDirectoryInput().getFrom().getAbsolutePath());
		try {
			return reader.loadModel(toolConfig.getDirectoryInput()
					.getFrom().getAbsolutePath());
		} catch (ClassNotFoundException e) {
			throw new MojoExecutionException("Maven TFG plugin: An error has occurred: "
					+ e.getMessage());
		} catch (IOException e){
			throw new MojoExecutionException("Maven TFG plugin: An error has occurred: "
					+ e.getMessage());
		} catch (NoUMLFileException e) {
			log.error("Maven TFG plugin: No UML was read from the input "
					+ "configuration, but a UML file was expected");
			throw new MojoExecutionException("Maven TFG plugin: No UML was read"
					+ " from the input configuration, but a UML file was expected");
		}
	}
	
	public Package getIntermediateModelFromDatabaseInput(ToolConfig toolConfig, 
			Log log) throws MojoExecutionException {
		DatabaseInput input = toolConfig.getDatabaseInput();
		Reader reader = getDatabaseReader(input);
		log.info("Maven TFG plugin: Database input configuration detected");
		log.info("Maven TFG plugin: " + input.getConfigurationMessage());
		try {
			return reader.loadModel(input.getHost(), input.getPort(), 
					input.getName(), input.getUsername(), input.getPassword());
		} catch (Exception e) {
			throw new MojoExecutionException("Maven TFG plugin: An error occurred: "
					+ e.getMessage());
		}
	}
}
