package output.orm;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import model.ObjectFactory;
import model.structure.EntityMappings;
import model.structure.Package;

/**
 * This class is in charge of taking the root element from the intermediate model
 * and do the corresponding transformations to end up creating the orm.xml
 * file in the specified output folder.
 * @author Raul Estrada
 *
 */
public class ORMWriter implements output.Writer {
	/**
	 * orm.xml version.
	 */
	private static final String version = "2.1";
	/**
	 * The name with file extension of the xml configuration file that is the 
	 * output of this class.
	 */
	private static final String OUTPUT_FILE_NAME = "/orm.xml";
	
	/**
	 * Method that takes the root element in the intermediate domain model of 
	 * this tool and
	 * creates the xml JPA configuration file in the specified output directory.
	 * @param rootPackage - The root package in the intermediate model.
	 * @param outputDirectory - The directory where the orm.xml file must be created.
	 * @throws JAXBException - An error ocurred while creating the JAXB context,
	 *  necessary to create the marshaller
	 * and write the orm.xml file.
	 * @throws IOException 
	 */
	public void write(Package rootPackage, String outputDirectory) 
			throws JAXBException, IOException {
		if (rootPackage == null || rootPackage.isEmpty()) {
			throw new IllegalArgumentException("Cannot write an orm.xml file "
					+ "from a null or empty package");
		}
		if (outputDirectory == null || outputDirectory.isEmpty()) {
			throw new IllegalArgumentException("Cannot write the orm.xml file "
					+ "in a null or empty output directory path");
		}
		ClassLoader loader = ObjectFactory.class.getClassLoader();
		JAXBContext context = JAXBContext.newInstance(
				ObjectFactory.class.getPackage().getName(), loader);
		Marshaller marshaller = context.createMarshaller();
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, 
				Boolean.TRUE);
		File outputORMDirectory = new File(outputDirectory);
		outputORMDirectory.mkdirs();
		File ormFile = new File(outputDirectory + OUTPUT_FILE_NAME);
		FileOutputStream fos = new FileOutputStream(ormFile);
		marshaller.marshal(createEntityMapping(rootPackage), fos);
		new AnnotationCleaner().clean(rootPackage, outputDirectory);
	}
	
	/**
	 * Private method that takes the root package in the intermediate domain model and
	 * creates an Entity Mappings instance (the root element in the orm.xml domain model),
	 * adding all the entities, domain elements and mapped superclasses.
	 * @param rootPackage - The root element in the intermediate domain model.
	 * @return - The Entity Mapping instance containing all the entities, 
	 * embeddable and mapped superclasses.
	 */
	private EntityMappings createEntityMapping(Package rootPackage) {
		ObjectFactory of = new ObjectFactory();
		EntityMappings em = of.createEntityMappings();
		em.setVersion(version);
		em.getEntity().addAll(rootPackage.getAllEntitiesInPackageTree());
		em.getEmbeddable().addAll(rootPackage.getAllValueObjectsInPackageTree());
		em.getMappedSuperclass().addAll(rootPackage.getAllMappedSuperclassesInPackageTree());
		return em;
	}
}
