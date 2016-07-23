package cmdtool.presentation.processors;

import java.io.IOException;
import java.util.List;

import javax.xml.bind.JAXBException;

import com.sun.codemodel.JClassAlreadyExistsException;

import cmdtool.presentation.UserInputValues;
import cmdtool.presentation.processors.actions.WriteAction;
import cmdtool.presentation.utils.PropertyReader;
import model.structure.Package;
import output.Writer;

public class OutputCommandProcessor {
	private static OutputCommandProcessor INSTANCE = null;
	private PropertyReader properties;
	private WriteAction writeAction;
	
	private OutputCommandProcessor() throws IOException {
		this.properties = new PropertyReader();
		this.writeAction = new WriteAction();
	}
	
	public static OutputCommandProcessor getInstance() throws IOException {
		if (INSTANCE == null) {
			INSTANCE = new OutputCommandProcessor();
		}
		return INSTANCE;
	}
	
	public void process(Package intermediateRootPackage, List<String> fullInput) 
			throws ClassNotFoundException, JAXBException, IOException, 
			JClassAlreadyExistsException {
		if (intermediateRootPackage == null) {
			System.out.println(properties.getProperty("writeBeforeRead"));
			return;
		}
		Writer writer = writeAction.obtainWriter(fullInput);
		if (writer != null) {
			if (fullInput.get(1).equals(UserInputValues.DATABASE)) {
				writer.write(intermediateRootPackage, fullInput.get(3));
			} else {
				writer.write(intermediateRootPackage, fullInput.get(2));
			}
			System.out.println(properties.getProperty("finished"));
		}
	}
}
