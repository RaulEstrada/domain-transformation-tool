package cmdtool.presentation.processors;

import java.io.IOException;
import java.util.List;

import cmdtool.presentation.processors.actions.ReadAction;
import cmdtool.presentation.utils.PropertyReader;
import input.uml.exceptions.NoUMLFileException;
import model.structure.Package;

public class InputCommandProcessor {
	private static InputCommandProcessor INSTANCE = null;
	private PropertyReader properties;
	private ReadAction readAction;
	
	private InputCommandProcessor() throws IOException{
		this.properties = new PropertyReader();
		this.readAction = new ReadAction();
	}
	
	public static InputCommandProcessor getInstance() throws IOException {
		if (INSTANCE == null) {
			INSTANCE = new InputCommandProcessor();
		}
		return INSTANCE;
	}
	
	public Package process(List<String> fullInput) {
		try{
			Package intermediateRootPackage = readAction.read(fullInput);
			if (intermediateRootPackage != null) {
				System.out.println(properties.getProperty("modelLoaded", 
						intermediateRootPackage.getAllEntitiesInPackageTree().size(),
						intermediateRootPackage.getAllValueObjectsInPackageTree().size(),
						intermediateRootPackage.getAllEnumsInPackageTree().size()));
			}
			return intermediateRootPackage;
		} catch (NoUMLFileException ex) {
			System.out.println(properties.getProperty("noUMLFileMessage"));
		} catch (Exception ex) {
			System.out.println(properties.getProperty("error", ex.getMessage()));
		}
		return null;
	}
}
