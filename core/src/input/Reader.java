package input;

import java.io.IOException;
import java.net.MalformedURLException;
import java.sql.SQLException;

import input.uml.exceptions.NoUMLFileException;
import model.structure.Package;

public interface Reader {
	/**
	 * Loads the model from a local location, that is, from a directory or from
	 *  a file in the end user’s computer, and returns the root element from the
	 *   loaded intermediate model.
	 */
	Package loadModel(String directory) throws MalformedURLException, 
	ClassNotFoundException, IOException, NoUMLFileException;
	
	/**
	 * Loads the model from a database and returns the root element from the 
	 * loaded intermediate model.
	 */
	Package loadModel(String host, String port, String databaseName, 
			String username, String password) 
					throws SQLException, ClassNotFoundException;
}
