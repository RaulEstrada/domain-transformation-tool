package output.uml;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.URIConverter;
import org.eclipse.uml2.uml.Profile;
import org.eclipse.uml2.uml.Stereotype;
import org.eclipse.uml2.uml.Type;
import org.eclipse.uml2.uml.resource.UMLResource;

import model.customtypes.RelationshipType;
import model.structure.ElementInRelationship;
import model.structure.Package;
import output.Writer;

/**
 * This class takes the root package from the intermediate model and transforms
 *  such structure into a 
 * UML structure in order to end up saving it in a .uml file.
 * @author Raul Estrada
 *
 */
public class UMLWriter implements Writer {
	/**
	 * Builder that handles the creation of UML elements (classes, associations,
	 *  attributes, etc)
	 */
	private UML2Builder umlBuilder = new UML2Builder();
	/**
	 * Maps the canonical name of simple types (primitive data types and
enumerations) to the UML type elements.
	 * Used mostly to create attributes.
	 */
	private Map<String, Type> types = new HashMap<>();
	/**
	 * Maps the canonical name of custom types (classes, embeddable) to the 
	 * UML type elements.
	 * Used mostly to create associations between classes.
	 */
	private Map<String, Type> customTypes = new HashMap<>();
	/**
	 * Maps the UML type to the intermediate domain model element that can take
	 *  part in an association.
	 * Used mostly to get the target element in a relationship between two UML
	 *  elements.
	 */
	private Map<Type, ElementInRelationship> typeToMappedElement = new HashMap<>();
	/**
	 * Maps which element that takes part in an association has already have 
	 * such association created
	 * by the other end of the relationship so that the association is not 
	 * duplicated.
	 */
	private Map<ElementInRelationship, Map<RelationshipType, 
		List<ElementInRelationship>>> relationsCreated = new HashMap<>();
	/**
	 * The stereotype for the version attributes
	 */
	public static Stereotype VERSION_STEREOTYPE;
	/**
	 * The stereotype for the transient attributes
	 */
	public static Stereotype TRANSIENT_STEREOTYPE;
	/**
	 * The stereotype for the embeddable classes
	 */
	public static Stereotype EMBEDDABLE_STEREOTYPE;
	/**
	 * The stereotype for the mapped Superclass classes
	 */
	public static Stereotype MAPPED_SUPERCLASS_STEREOTYPE;
	/**
	 * The path to the file containing the UML profile with the stereotypes.
	 */
	public static final String PROFILE_PATH = "Ecore.profile.uml";
	/**
	 * The path to the lib folder of the project
	 */
	public static final String LIB_PATH = "/core/lib";
	public static final String UML_RESOURCES_JAR = "\\org.eclipse.uml2.uml.resources_5.0.2.v20150202-0947.jar!/";

	/**
	 * Method that takes the root element in the intermediate domain model of this tool and
	 * creates .uml file with the UML model in the specified output directory.
	 * @param rootPackage - The root package in the intermediate model.
	 * @param outputDirectory - The directory where the UMLDiagram.uml file must be created.
	 * @throws IOException 
	 */
	public void write(Package rootPackage, String outputDirectory) 
			throws IOException {
		if (rootPackage == null || rootPackage.isEmpty()) {
			throw new IllegalArgumentException("Cannot write a UML model file "
					+ "from a null or empty package");
		}
		if (outputDirectory == null || outputDirectory.isEmpty()) {
			throw new IllegalArgumentException("Cannot write the UML model file "
					+ "in a null or empty output directory path");
		}
		new File(outputDirectory).mkdirs();
		org.eclipse.uml2.uml.Package umlRootPackage = 
				umlBuilder.createPackage(rootPackage.getName());
		loadUMLProfile(umlRootPackage, outputDirectory);
		new PrimitiveTypesCreator().withTypes(types).create(umlRootPackage);
		new PackageStructureCreator().withTypes(types)
			.withCustomTypes(customTypes)
			.withTypeToMappedElement(typeToMappedElement)
			.create(rootPackage, umlRootPackage);
		new InternalStructureCreator().withTypes(types).withCustomTypes(customTypes)
			.withTypeToMappedElement(typeToMappedElement)
			.withRelationsCreated(relationsCreated).create(rootPackage);
		new UML2FileCreator().save(umlRootPackage, outputDirectory);
	}
	
	/**
	 * Loads the URL profile and the different stereotypes (version, transient, 
	 * embeddable, mappedSuperclass)
	 * @param rootPackage - The UML root package where the UML profile will 
	 * be applied.
	 * @throws IOException 
	 */
	private void loadUMLProfile(org.eclipse.uml2.uml.Package rootPackage, 
			String outputDirectory) throws IOException {
		URI baseUri = URI.createURI("jar:file:/" + new File(LIB_PATH)
				.getAbsolutePath() + UML_RESOURCES_JAR);
		URIConverter.URI_MAP.put(URI.createURI(UMLResource.METAMODELS_PATHMAP), 
				baseUri.appendSegment("metamodels"));
		URIConverter.URI_MAP.put(URI.createURI(UMLResource.PROFILES_PATHMAP), 
				baseUri.appendSegment("profiles"));
		URIConverter.URI_MAP.put(URI.createURI(UMLResource.LIBRARIES_PATHMAP), 
				baseUri.appendSegment("libraries"));
 
		String profileFilePath = copyProfileFileToOutputDirectory(outputDirectory);
		Profile umlProfile = umlBuilder.load(URI.createFileURI(
				new File(profileFilePath).getAbsolutePath()));
		rootPackage.applyProfile(umlProfile);
		VERSION_STEREOTYPE = umlProfile.getOwnedStereotype("version");
		TRANSIENT_STEREOTYPE = umlProfile.getOwnedStereotype("transient");
		EMBEDDABLE_STEREOTYPE = umlProfile.getOwnedStereotype("valueObject");
		MAPPED_SUPERCLASS_STEREOTYPE = umlProfile.getOwnedStereotype("mappedSuperclass");
	}
	
	/**
	 * Duplicates the profile file and moves the new copy to the output directory
	 * @param outputDirectory - The output directory
	 * @return - The path to the new profile file.
	 * @throws IOException 
	 */
	private String copyProfileFileToOutputDirectory(String outputDirectory) 
			throws IOException {
		InputStream is = getClass().getClassLoader().getResourceAsStream(PROFILE_PATH);
		OutputStream out = 
				new FileOutputStream(new File(outputDirectory + "/" + PROFILE_PATH));
		IOUtils.copy(is, out);
		return outputDirectory + "/" + PROFILE_PATH;
	}

}
