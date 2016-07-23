package output.uml;

import java.io.IOException;
import java.util.Iterator;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.uml2.uml.resources.util.UMLResourcesUtil;
import org.eclipse.uml2.uml.util.UMLUtil;
import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.Package;
import org.eclipse.uml2.uml.resource.UMLResource;

/**
 * Class that creates the .uml file with the UML model transformed from the 
 * intermediate domain model.
 * @author Raul Estrada
 *
 */
public class UML2FileCreator {
	/**
	 * The name of the output .uml file
	 */
	private final static String OUTPUT_FILE_NAME = "UMLDiagram";

	/**
	 * Creates a .uml file in the given output directory with the UML model 
	 * contained in the specified UML package.
	 * @param pckg - The root UML package containing the UML model to be 
	 * written in the .uml file.
	 * @param outputDirectory - The directory where the .uml file must be created.
	 */
	@SuppressWarnings("rawtypes")
	protected void save(Package pckg, String outputDirectory) {
		ResourceSet resourceSet = new ResourceSetImpl();
		UMLResourcesUtil.init(resourceSet);

		URI uri = URI.createFileURI(outputDirectory).appendSegment(OUTPUT_FILE_NAME)
				.appendFileExtension(UMLResource.FILE_EXTENSION);
		Resource resource = resourceSet.createResource(uri);
		resource.getContents().add(pckg);
		
		// We add the stereotypes applications so that they are saved as well 
		// in the .uml file.
		Iterator contents = UMLUtil.getAllContents(pckg, true, false);
		while (contents.hasNext()) {
			Object next = contents.next();
			if (next instanceof Element) {
				resource.getContents().addAll(((Element)next).getStereotypeApplications());
			}
		}
		try {
			resource.save(null);
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
}
