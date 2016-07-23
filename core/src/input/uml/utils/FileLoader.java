package input.uml.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.uml2.uml.NamedElement;
import org.eclipse.uml2.uml.UMLPackage;
import org.eclipse.uml2.uml.internal.resource.XMI2UMLResourceFactoryImpl;
import org.eclipse.uml2.uml.resource.UMLResource;

import input.uml.exceptions.NoUMLFileException;
import output.uml.UMLWriter;

/**
 * This class loads the first file inside a directory that contains a UML model,
 * and returns a list of root packages elements from that UML model.
 * @author Raul Estrada
 *
 */
public class FileLoader {
	/**
	 * Method that takes the path of a given directory, finds a .uml file, 
	 * loads it and returns the root elements
	 * of the UML model contained in such file.
	 * @param directory - The path of the directory to look for the .uml file
	 * @return - The list of root named elements in the UML model loaded from the
	 *  .uml file in the directory
	 * @throws NoUMLFileException 
	 */
	public List<NamedElement> loadRootPackages(String directory) 
			throws NoUMLFileException {
		if (directory == null || directory.isEmpty()) {
			throw new IllegalArgumentException("Cannot load any model from a "
					+ "null or empty directory");
		}
		Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap()
			.put("uml", new XMI2UMLResourceFactoryImpl());
		File inputFile = new File(directory);
		if (inputFile.isDirectory() || !inputFile.getAbsolutePath().endsWith(".uml")) {
			throw new NoUMLFileException();
		}
		URI modelURI = URI.createFileURI(inputFile.getAbsolutePath());
		return parseRootElements(modelURI);
	}

	/**
	 * Given the URI of the .uml file, it loads the elements and returns the
	 *  list of root packages in the model.
	 * @param uri - The URI of the file containing the UML model.
	 * @return - The list of root elements (NamedElements, in this case packages)
	 */
	private List<NamedElement> parseRootElements(URI uri) {
		ResourceSet resourceSet = new ResourceSetImpl();
		loadUMLProfile(resourceSet);
		Resource resource = resourceSet.getResource(uri, true);
		EList<EObject> contents = resource.getContents();
		List<NamedElement> rootElements = new ArrayList<>();
		for (EObject item : contents) {
			if (item instanceof NamedElement) {
				rootElements.add((NamedElement)item);
			}
		}
		return rootElements;
	}
	
	/**
	 * Loads the URL profile and the different stereotypes (version, transient,
	 *  embeddable, mappedSuperclass)
	 * @param rootPackage - The UML root package where the UML profile will be applied.
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void loadUMLProfile(ResourceSet resourceSet) {
		resourceSet.getPackageRegistry().put(UMLPackage.eNS_URI, UMLPackage.eINSTANCE);
		resourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap()
			.put(UMLResource.FILE_EXTENSION, UMLResource.Factory.INSTANCE);
		resourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap()
			.put(UMLResource.PROFILE_FILE_EXTENSION, UMLResource.Factory.INSTANCE );
		Map uriMap = resourceSet.getURIConverter().getURIMap();
		URI baseUri = URI.createURI("jar:file:/" + new File(UMLWriter.LIB_PATH)
				.getAbsolutePath() + UMLWriter.UML_RESOURCES_JAR);
		uriMap.put(URI.createURI("pathmap://UML_LIBRARIES/"), 
				baseUri.appendSegment("libraries").appendSegment(""));
		uriMap.put(URI.createURI("pathmap://UML_PROFILES/"), 
				baseUri.appendSegment("profiles").appendSegment(""));
		uriMap.put(URI.createURI("pathmap://UML_METAMODELS/"), 
				baseUri.appendSegment("metamodels").appendSegment(""));
		uriMap.put(URI.createURI("pathmap://resources/"), 
				baseUri.appendSegment("resources").appendSegment(""));
	}
}
