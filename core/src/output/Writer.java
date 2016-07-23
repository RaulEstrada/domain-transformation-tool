package output;

import java.io.IOException;

import javax.xml.bind.JAXBException;

import com.sun.codemodel.JClassAlreadyExistsException;

import model.structure.Package;

public interface Writer {
	/**
	 * Converts the intermediate model and generates a new form of the model in 
	 * the specified directory.
	 */
	public void write(Package rootPackage, String outputDirectory) throws 
	JAXBException, IOException, JClassAlreadyExistsException, ClassNotFoundException;
}
