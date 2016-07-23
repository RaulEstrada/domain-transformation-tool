package output.bbdd;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import javax.xml.bind.JAXBException;

import input.bbdd.DatabaseType;
import model.structure.Package;
import output.Writer;

/**
 * This class takes the root package in the intermediate model and creates a 
 * .sql output file
 * from all the elements contained in the package tree structure.
 * @author Raul Estrada
 *
 */
public class SQLWriter implements Writer {
	/**
	 * Name of the output file. It will be followed by the database type (e.g. 
	 * output_oracle.sql, output_mysql.sql)
	 */
	private final static String OUTPUT_FILE_NAME = "/output_";
	/**
	 * The target database where the script will be executed.
	 */
	private DatabaseType databaseType;
	
	/**
	 * Instantiates the SQL writer, setting the database type to the one passed 
	 * as a parameter.
	 * @param databaseType - The target database type
	 */
	public SQLWriter(DatabaseType databaseType) {
		if (databaseType == null) {
			throw new IllegalArgumentException("Cannot create a SQL writer if "
					+ "the database type is not specified");
		}
		this.databaseType = databaseType;
	}

	/**
	 * Method that takes the root element in the intermediate domain model of 
	 * this tool and creates .sql file in the specified output directory.
	 * @param rootPackage - The root package in the intermediate model.
	 * @param outputDirectory - The directory where the output.sql file must be
		created.
	 * @throws IOException 
	 */
	@Override
	public void write(Package rootPackage, String outputDirectory) 
			throws JAXBException, IOException {
		if (rootPackage == null || outputDirectory == null || 
				outputDirectory.isEmpty()) {
			throw new IllegalArgumentException("Cannot write a SQL file if the "
					+ "root package or the output directory are missing");
		}
		File outputSQLDirectory = new File(outputDirectory);
		outputSQLDirectory.mkdirs();
		BufferedWriter writer = new BufferedWriter(new FileWriter(new File
				(outputDirectory + OUTPUT_FILE_NAME 
				+ this.databaseType.getPropertyExtension() + ".sql")));
		try{
			new Scripter(this.databaseType).script(writer, rootPackage);
		} catch (Exception ex) {
			throw ex;
		} finally {
			writer.close();
		}
	}

}
