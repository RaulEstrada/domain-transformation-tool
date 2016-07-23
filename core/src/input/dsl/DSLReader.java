package input.dsl;

import java.io.IOException;
import java.net.MalformedURLException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import com.google.inject.Injector;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.xtext.resource.XtextResourceSet;
import org.xtext.example.tfg.MyDslStandaloneSetup;
import org.xtext.example.tfg.myDsl.MODEL;

import input.Reader;
import model.structure.Embeddable;
import model.structure.Entity;
import model.structure.Enumeration;
import model.structure.Package;

public class DSLReader implements Reader {
	private Map<String, Entity> entityMap = new HashMap<>();
	private Map<String, Embeddable> embeddableMap = new HashMap<>();
	private Map<String, Enumeration> enumMap = new HashMap<>();

	/**
	 * It loads the .tfg DSL file, compiles it, obtains the semantic model, 
	 * builds the intermediate domain model and returns its root element.
	 */
	@Override
	public Package loadModel(String directory) throws MalformedURLException, 
		ClassNotFoundException, IOException {
		MODEL model = loadDSLModel(directory);
		Package packg = new Package("root");
		new EntityCreator().createBasicElements(model, packg, entityMap, 
				embeddableMap, enumMap);
		new EntityElementsCreator(entityMap, enumMap, embeddableMap)
			.processAttributesAndInheritance(model, packg);
		new IdClassProcessor().process(packg, entityMap);
		return packg;
	}

	/**
	 * It throws an exception/error since this loading operation is not 
	 * supported by the DSL loading module.
	 */
	@Override
	public Package loadModel(String host, String port, String databaseName, 
			String username, String password)
			throws SQLException {
		throw new UnsupportedOperationException("DSL Reader does not support "
				+ "loading models from a URL, username and password");
	}
	
	/**
	 * It loads the DSL file, obtains the semantic model elements and returns 
	 * its root DSL element.
	 * @param path - The path of the DSL file
	 * @return - The root of the DSL semantic model.
	 */
	private MODEL loadDSLModel(String path) {
		Injector injector = 
				new MyDslStandaloneSetup().createInjectorAndDoEMFRegistration();
		XtextResourceSet xtextRS = injector.getInstance(XtextResourceSet.class);
		Resource resource = xtextRS.getResource(URI.createURI("file:/" + path), true);
		resource.getContents();
		return (MODEL)(resource.getContents().get(0));
	}
	
}
