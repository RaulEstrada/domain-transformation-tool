package output.java;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.JAXBException;

import com.sun.codemodel.JBlock;
import com.sun.codemodel.JClassAlreadyExistsException;
import com.sun.codemodel.JCodeModel;
import com.sun.codemodel.JDefinedClass;
import com.sun.codemodel.JExpr;
import com.sun.codemodel.JFieldVar;
import com.sun.codemodel.JMethod;
import com.sun.codemodel.JMod;
import com.sun.codemodel.JPackage;
import com.sun.codemodel.JVar;

import model.associations.ManyToMany;
import model.associations.ManyToOne;
import model.associations.OneToMany;
import model.associations.OneToOne;
import model.structure.AnnotatedAttributes;
import model.structure.Basic;
import model.structure.Embeddable;
import model.structure.Embedded;
import model.structure.Entity;
import model.structure.Enumeration;
import model.structure.Id;
import model.structure.MappedSuperclass;
import model.structure.Operation;
import model.structure.Package;
import model.structure.Version;
import output.Writer;

public class JavaWriter implements Writer {
	private static final String OUTPUT_DIRECTORY_NAME = "/output_src";
	private final JCodeModel model = new JCodeModel();
	private Creator creator;
	private Map<String, JDefinedClass> classesMap = new HashMap<>();
	private Map<Package, JPackage> packagesMap = new HashMap<>();
	private AssociationsClassHandler associationsClassHandler;

	/**
	 * Traverses the loaded intermediate model, generating the corresponding 
	 * java class representing the entity, value object or custom type.
	 */
	@Override
	public void write(Package rootPackage, String outputDirectory) 
			throws JAXBException, IOException, JClassAlreadyExistsException, 
			ClassNotFoundException {
		this.creator = new Creator(model, classesMap);
		File file = new File(outputDirectory + OUTPUT_DIRECTORY_NAME);
		file.mkdirs();
		createClassesDefinition(rootPackage, null);
		createClassElements(rootPackage);
		model.build(file);
	}
	
	/**
	 * Recursively traverses the packages in the intermediate model, creating 
	 * the corresponding java packages and classes for the entities, value objects
	 *  and enumerations found. It only creates the skeleton of the class, its 
	 *  definition, but not its attributes and operations.
	 * @param packg - The intermediate model package
	 * @param codePackage - The java parent package
	 * @throws JClassAlreadyExistsException
	 */
	private void createClassesDefinition(Package packg, JPackage codePackage) 
			throws JClassAlreadyExistsException {
		JPackage codeSubPackage = getPackage(packg, codePackage);
		for (Entity entity : packg.getEntities()) {
			creator.createClassDefinition(entity.getName(), entity.getClazz(), 
					codeSubPackage);
			if (entity.getIdClass() != null) {
				String name = entity.getIdClass().getClazz();
				String[] nameParts = name.split("\\.");
				creator.createClassDefinition(nameParts[nameParts.length-1], 
						entity.getIdClass().getClazz(), packagesMap.get(packg));
			}
		}
		for (Embeddable embeddable : packg.getValueObjects()) {
			creator.createClassDefinition(embeddable.getName(), 
					embeddable.getClazz(), codeSubPackage);
		}
		for (MappedSuperclass mappedSuperclass : packg.getMappedSuperclasses()) {
			String clazz = mappedSuperclass.getClazz();
			String[] clazzParts = clazz.split("\\.");
			creator.createClassDefinition(clazzParts[clazzParts.length-1], 
					clazz, codeSubPackage);
		}
		for (Enumeration enumeration : packg.getEnumerations()) {
			String name = enumeration.getName();
			String[] nameParts = name.split("\\.");
			creator.createEnumeration(nameParts[nameParts.length-1], 
					enumeration.getName(), codeSubPackage, enumeration);
		}
		for (Package subPackg : packg.getChildrenPackages()) {
			createClassesDefinition(subPackg, codeSubPackage);
		}
	}
	
	/**
	 * Creates and returns the corresponding package to process
	 * @param packg - The intermediate model package.
	 * @param codePackage - The java parent package
	 * @return - The new java package.
	 * @throws JClassAlreadyExistsException
	 */
	private JPackage getPackage(Package packg, JPackage codePackage) 
			throws JClassAlreadyExistsException {
		if (packg == null) {
			throw new IllegalArgumentException("Cannot create the java classes "
					+ "of an empty package");
		}
		JPackage codeSubPackage = null;
		if (codePackage == null) {
			codePackage = model._package(packg.getName());
			codeSubPackage = codePackage;
			this.associationsClassHandler = new AssociationsClassHandler(
					creator.createStaticClassDefinition("Associations", 
							"Associations", codePackage), creator, classesMap);
		} else {
			codeSubPackage = codePackage.subPackage(packg.getName());
		}
		packagesMap.put(packg, codePackage);
		return codeSubPackage;
	}
	
	/**
	 * Recursively traverses the packages in the intermediate model, and for 
	 * each entity, value object or mapped superclass, it processes them and 
	 * creates the inner java class elements, adding them to their java class.
	 * @param packg - The intermediate model package
	 * @throws ClassNotFoundException
	 * @throws JClassAlreadyExistsException
	 */
	private void createClassElements(Package packg) 
			throws ClassNotFoundException, JClassAlreadyExistsException {
		for (Entity entity : packg.getEntities()) {
			createEntityClassElements(entity);
		}
		for (Embeddable embeddable : packg.getValueObjects()) {
			createAttributeElements(embeddable.getClazz(), 
					embeddable.getAttributes(), null);
		}
		for (MappedSuperclass mappedSuperclass : packg.getMappedSuperclasses()) {
			createAttributeElements(mappedSuperclass.getClazz(), 
					mappedSuperclass.getAttributes(), null);
		}
		for (Package subPackg : packg.getChildrenPackages()) {
			createClassElements(subPackg);
		}
	}
	
	/**
	 * Creates the java inner elements of the entity class.
	 * @param entity - The entity whose class is to be filled.
	 * @throws ClassNotFoundException
	 * @throws JClassAlreadyExistsException
	 */
	private void createEntityClassElements(Entity entity) 
			throws ClassNotFoundException, JClassAlreadyExistsException {
		if (entity.getParentEntity() != null) {
			JDefinedClass subClass = this.classesMap
					.get(entity.getClazz());
			JDefinedClass parentClass = this.classesMap
					.get(entity.getParentEntity().getClazz());
			subClass._extends(parentClass);
		}
		if (entity.getIdClass() != null) {
			for (Id id : entity.getIdClass().getAttributes()) {
				creator.createId(entity.getIdClass().getClazz(), id);
			}
		}
		createAttributeElements(entity.getClazz(), entity.getAttributes(), 
				entity);
		createEntityOperations(entity);
	}
	
	/**
	 * It creates the java methods in the java class of the entity specified. 
	 * The methods are created from the operations loaded in the entity 
	 * intermediate model.
	 * @param entity - The entity whose methods are to be created
	 * @throws ClassNotFoundException
	 */
	private void createEntityOperations(Entity entity) 
			throws ClassNotFoundException {
		JDefinedClass class_ = classesMap.get(entity.getClazz());
		for (Operation operation : entity.getOperations()) {
			creator.createMethod(class_, operation);
		}
	}
	
	/**
	 * It creates the different java attributes of an entity, mapped superclass 
	 * or value object, and adds them to their corresponding java class.
	 * @param elementClass - The class whose attributes will be created
	 * @param attributes - The intermediate model attributes
	 * @param entity - The intermediate model entity
	 * @throws ClassNotFoundException
	 * @throws JClassAlreadyExistsException
	 */
	private void createAttributeElements(String elementClass, 
			AnnotatedAttributes attributes, Entity entity) 
				throws ClassNotFoundException, JClassAlreadyExistsException {
		for (Version version : attributes.getVersion()) {
			creator.createField(elementClass, version.getName(), 
					version.getTypeName(), false);
		}
		for (Basic basic : attributes.getBasic()) {
			if (basic.getEnumerated() != null) {
				creator.createReference(elementClass, basic.getTypeName(), 
						basic.getName(), false, false);
			} else {
				creator.createField(elementClass, basic.getName(), 
						basic.getTypeName(), false);
			}
		}
		for (Embedded embedded : attributes.getEmbedded()) {
			creator.createReference(elementClass, embedded.getEmbeddable()
					.getClazz(), embedded.getName(), false, false);
		}
		createAssociationElements(elementClass, attributes, entity);
		for (Id id : attributes.getId()) {
			creator.createId(elementClass, id);
		}
		createConstructor(attributes, elementClass);
	}
	
	/**
	 * It creates the different java references of an entity, mapped superclass 
	 * or value object, and adds them to their corresponding java class.
	 * @param elementClass - The class whose references will be created
	 * @param attributes - The intermediate model attributes
	 * @param entity - The intermediate model entity
	 * @throws ClassNotFoundException
	 * @throws JClassAlreadyExistsException
	 */
	private void createAssociationElements(String elementClass, 
			AnnotatedAttributes attributes, Entity entity) 
					throws JClassAlreadyExistsException {
		for (OneToOne oneToOne : attributes.getOneToOne()) {
			creator.createReference(elementClass, oneToOne.getEntity().getClazz(), 
					oneToOne.getName(), false, false);
			if (entity != null) { 
				associationsClassHandler.associateOneToOne(oneToOne, entity); 
			}
		}
		for (OneToMany oneToMany : attributes.getOneToMany()) {
			creator.createReference(elementClass, oneToMany.getTargetEntity(), 
					oneToMany.getName(), true, false);
			if (entity != null) { 
				associationsClassHandler.associateOneToMany(oneToMany, entity); 
			}
		}
		for (ManyToOne manyToOne : attributes.getManyToOne()) {
			creator.createReference(elementClass, manyToOne.getEntity().getClazz(),
					manyToOne.getName(), false, false);
			if (entity != null) { 
				associationsClassHandler.associateManyToOne(manyToOne, entity); 
			}
		}
		for (ManyToMany manyToMany : attributes.getManyToMany()) {
			creator.createReference(elementClass, manyToMany.getEntity().getClazz(),
					manyToMany.getName(), true, false);
			if (entity != null) { 
				associationsClassHandler.associateManyToMany(manyToMany, entity); 
			}
		}
	}
	
	/**
	 * It generates the corresponding java constructor for the specified class, 
	 * with the attributes that form the identity of the element that spawned the
	 *  creation of the class.
	 * @param attributes - The intermediate model attributes
	 * @param elementClass - The name of the java class.
	 * @throws ClassNotFoundException
	 */
	private void createConstructor(AnnotatedAttributes attributes, 
			String elementClass) throws ClassNotFoundException {
		if (!attributes.getId().isEmpty()) {
			JDefinedClass class_ = classesMap.get(elementClass);
			class_.constructor(JMod.PUBLIC);
			JMethod constructorWithParams = class_.constructor(JMod.PUBLIC);
			JBlock body = constructorWithParams.body();
			for (Id id : attributes.getId()) {
				JVar var = null;
				if (classesMap.containsKey(id.getTypeName())) {
					var = constructorWithParams.param(classesMap.get(
							id.getTypeName()), id.getName());
				} else {
					var = constructorWithParams.param(Class.forName(
							id.getTypeName()), id.getName());
				}
				JFieldVar varRetrieved = class_.fields().get(var.name());
				if (varRetrieved != null) {
					body.assign(JExpr._this().ref(varRetrieved), 
							var);
				}
			}
		}
	}

}
