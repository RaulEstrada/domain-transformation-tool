package output.java;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.google.common.base.CaseFormat;
import com.sun.codemodel.ClassType;
import com.sun.codemodel.JBlock;
import com.sun.codemodel.JClass;
import com.sun.codemodel.JClassAlreadyExistsException;
import com.sun.codemodel.JCodeModel;
import com.sun.codemodel.JDefinedClass;
import com.sun.codemodel.JExpr;
import com.sun.codemodel.JFieldVar;
import com.sun.codemodel.JInvocation;
import com.sun.codemodel.JMethod;
import com.sun.codemodel.JMod;
import com.sun.codemodel.JPackage;
import com.sun.codemodel.JVar;

import model.structure.Enumeration;
import model.structure.Id;
import model.structure.Operation;
import model.structure.OperationParameter;

public class Creator {
	private JCodeModel model;
	private JClass setClass;
	private Map<String, JDefinedClass> classesMap;
	
	/**
	 * Initializes the creator element, that creates java elements.
	 * @param model - The CodeModel model.
	 * @param classesMap - The map of java classes.
	 */
	public Creator(JCodeModel model, Map<String, JDefinedClass> classesMap) {
		if (model == null || classesMap == null){
			throw new IllegalArgumentException("JCode model or classes map cannot"
					+ " be null");
		}
		this.classesMap = classesMap;
		this.model = model;
		this.setClass = model.ref(Set.class);
	}
	
	/**
	 * Creates the property for the id of an entity
	 * @param elementClass - The class of the entity
	 * @param id - The id intermediate model element.
	 * @throws ClassNotFoundException
	 */
	public void createId(String elementClass, Id id) throws ClassNotFoundException {
		JDefinedClass class_ = classesMap.get(elementClass);
		if (!class_.fields().containsKey(id.getName())) {
			if (classesMap.containsKey(id.getTypeName())) {
				createReference(elementClass, id.getTypeName(), id.getName(), 
						false, true);
			} else {
				createField(elementClass, id.getName(), id.getTypeName(), true);
			}
		}
	}
	
	/**
	 * Creates a java enumeration.
	 * @param name - The name of the enumeration
	 * @param clazz - The class of the enumeration
	 * @param packg - The java package where it will be located
	 * @param enumeration - The intermediate model enumeration
	 * @return - The new java enumeration
	 * @throws JClassAlreadyExistsException
	 */
	public JDefinedClass createEnumeration(String name, String clazz, JPackage packg,
			Enumeration enumeration) throws JClassAlreadyExistsException {
		JDefinedClass class_ = packg._class(JMod.PUBLIC, 
				CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, name),
				ClassType.ENUM);
		classesMap.put(clazz, class_);
		for (Object constant : enumeration.getConstants()) {
			class_.enumConstant(constant.toString());
		}
		return class_;
	}
	
	/**
	 * Creates a normal java class
	 * @param name - The name of the class.
	 * @param clazz - The class.
	 * @param packg - The java package where it will be located
	 * @return - The new java class
	 * @throws JClassAlreadyExistsException
	 */
	public JDefinedClass createClassDefinition(String name, String clazz, 
			JPackage packg) throws JClassAlreadyExistsException {
		JDefinedClass class_ = packg._class(CaseFormat.UPPER_UNDERSCORE.
				to(CaseFormat.UPPER_CAMEL, name));
		makeClassSerializable(class_);
		classesMap.put(clazz, class_);
		return class_;
	}
	
	/**
	 * Creates a new static java class.
	 * @param name - The name of the class.
	 * @param clazz - The class
	 * @param packg - The java package where it will be located
	 * @return - The new static java class.
	 * @throws JClassAlreadyExistsException
	 */
	public JDefinedClass createStaticClassDefinition(String name, String clazz, 
			JPackage packg) throws JClassAlreadyExistsException {
		JDefinedClass class_ = packg._class(JMod.PUBLIC | JMod.STATIC, 
				CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, name), 
				ClassType.CLASS);
		makeClassSerializable(class_);
		classesMap.put(clazz, class_);
		return class_;
	}
	
	/**
	 * Creates a new static inner java class.
	 * @param name - The name of the class.
	 * @param clazz - The class.
	 * @param outerClass - The class containing the new class.
	 * @return - The new static inner class.
	 * @throws JClassAlreadyExistsException
	 */
	public JDefinedClass createStaticInnerClassDefinition(String name, String clazz,
			JDefinedClass outerClass) throws JClassAlreadyExistsException {
		if (classesMap.containsKey(clazz)) {
			return null;
		}
		JDefinedClass class_ = outerClass._class(JMod.PUBLIC | JMod.STATIC, 
				CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, name), 
				ClassType.CLASS);
		makeClassSerializable(class_);
		classesMap.put(clazz, class_);
		return class_;
	}
	
	/**
	 * Makes a given class implement the serializable contract.
	 * @param class_ - The class to make serializable.
	 */
	private void makeClassSerializable(JDefinedClass class_) {
		class_._implements(Serializable.class);
		class_.field(JMod.PRIVATE | JMod.STATIC | JMod.FINAL, Long.class, 
				"serialVersionUID", JExpr.lit(1l));
	}
	
	/**
	 * Creates a java attribute in a given class.
	 * @param elementClass - The class to add the attribute to.
	 * @param fieldName - The name of the attribute
	 * @param fieldType - The type of the attribute
	 * @param immutable - If the attribute is immutable or can change.
	 * @throws ClassNotFoundException
	 */
	public void createField(String elementClass, String fieldName, 
			String fieldType, boolean immutable) throws ClassNotFoundException {
		JDefinedClass class_ = classesMap.get(elementClass);
		JFieldVar field = class_.field(JMod.PRIVATE, Class.forName(fieldType), 
				CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, fieldName));
		createGetter(class_, field, false);
		if (!immutable) {
			createSetter(class_, field);
		}
	}
	
	/**
	 * Creates a java attribute referencing another class.
	 * @param elementClass - The class to add the attribute to.
	 * @param referencedElementClass - The class the attribute is referencing.
	 * @param name - The name of the attribute
	 * @param collection - if it's a collection
	 * @param immutable - whether the attribute can change or not.
	 */
	public void createReference(String elementClass, String referencedElementClass,
			String name, boolean collection, boolean immutable) {
		JDefinedClass class_ = classesMap.get(elementClass);
		if (class_.fields().containsKey(name)) {
			return;
		}
		JClass referencedClass = classesMap.get(referencedElementClass);
		JFieldVar field = null;
		if (collection) {
			referencedClass = setClass.narrow(referencedClass);
			field = class_.field(JMod.PRIVATE, referencedClass, name, 
					JExpr._new(model.ref(HashSet.class)));
			createUnmodifiableGetter(class_, field);
		} else {
			field = class_.field(JMod.PRIVATE, referencedClass, name);
		}
		createGetter(class_, field, collection);
		if (!immutable) {
			createSetter(class_, field);
		}
	}
	
	/**
	 * Creates the getter method of a given field.
	 * @param class_ - The class owning the method.
	 * @param field - The field whose value is returned.
	 * @param startWithUnderscore - If the name starts with underscore or not.
	 */
	private void createGetter(JDefinedClass class_, JFieldVar field, 
			boolean startWithUnderscore) {
		String name = CaseFormat.LOWER_UNDERSCORE
				.to(CaseFormat.LOWER_CAMEL, "get_" + field.name());
		if (startWithUnderscore) {
			name = "_" + name;
		}
		JMethod getter = class_.method(JMod.PUBLIC, field.type(), 
				name);
		getter.body()._return(field);
	}
	
	/**
	 * Creates a getter method that does not allow the user to change the value 
	 * of the field.
	 * @param class_ - The class owning the method.
	 * @param field - The field whose value is returned.
	 */
	private void createUnmodifiableGetter(JDefinedClass class_, JFieldVar field) {
		JMethod getter = class_.method(JMod.PUBLIC, field.type(), 
				CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, 
						"get_" + field.name()));
		JBlock body = getter.body();
		JInvocation collectionsInvocation = model.ref(Collections.class)
				.staticInvoke("unmodifiableSet").arg(field);
		body._return(collectionsInvocation);
	}
	
	/**
	 * Creates the java setter method to assign a value to a field
	 * @param class_ - The class owning the method.
	 * @param field - The field whose value is assigned.
	 */
	private void createSetter(JDefinedClass class_, JFieldVar field) {
		JMethod setter = class_.method(JMod.PUBLIC, Void.TYPE, 
				getGetterName(field.name()));
		JVar param = setter.param(field.type(), "value");
		JBlock setterBody = setter.body();
		setterBody.assign(JExpr._this().ref(field), param);
	}
	
	/**
	 * Computes the correct name of a getter method
	 * @param fieldName - The name of the field
	 * @return - The getter method formatted correctly.
	 */
	private String getGetterName(String fieldName) {
		return CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, 
				"set_" + fieldName);
	}
	
	/**
	 * Creates a java method.
	 * @param class_ - The class owning the method
	 * @param operation - The intermediate model operation representing the 
	 * method
	 * @throws ClassNotFoundException
	 */
	public void createMethod(JDefinedClass class_, Operation operation) 
			throws ClassNotFoundException {
		JMethod method = null;
		if (classesMap.containsKey(operation.getReturnType())) {
			method = class_.method(JMod.PUBLIC, classesMap.get(operation
					.getReturnType()), operation.getName());
		} else if (operation.getReturnType() == null || operation.getReturnType()
				.isEmpty() || operation.getReturnType().equals("void")) {
			method = class_.method(JMod.PUBLIC, Void.TYPE, operation.getName());
		} else {
			method = class_.method(JMod.PUBLIC, Class.forName(operation
					.getReturnType()), operation.getName());
		}
		for (OperationParameter parameter : operation.getParameters()) {
			if (classesMap.containsKey(parameter.getType())) {
				method.param(classesMap.get(parameter.getType()), 
						parameter.getName());
			} else {
				method.param(Class.forName(parameter.getType()), 
						parameter.getName());
			}
		}
		JBlock body = method.body();
		body._throw(JExpr._new(this.model
				._ref(UnsupportedOperationException.class)));
	}
}
