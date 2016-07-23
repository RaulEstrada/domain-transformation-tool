package output.java;

import java.util.Map;

import com.google.common.base.CaseFormat;
import com.sun.codemodel.JBlock;
import com.sun.codemodel.JClassAlreadyExistsException;
import com.sun.codemodel.JDefinedClass;
import com.sun.codemodel.JExpr;
import com.sun.codemodel.JInvocation;
import com.sun.codemodel.JMethod;
import com.sun.codemodel.JMod;
import com.sun.codemodel.JVar;

import model.associations.OneToOne;
import model.structure.Entity;

public class OneToOneAssociationClass {
	private JDefinedClass associationsClass;
	private Creator creator;
	private Map<String, JDefinedClass> classesMap;
	private Entity entity;
	private OneToOne association;
	
	/**
	 * Initializes the one to one association class.
	 * @param associationsClass - The new java class that contains the association
	 * @param creator - The creator of java elements.
	 * @param classesMap - The map of classes
	 * @param entity - The entity that participates in the association
	 * @param association - The association
	 */
	public OneToOneAssociationClass(JDefinedClass associationsClass, 
			Creator creator, Map<String, JDefinedClass> classesMap, 
			Entity entity, OneToOne association){
		this.associationsClass = associationsClass;
		this.creator = creator;
		this.classesMap = classesMap;
		this.entity = entity;
		this.association = association;
	}
	
	/**
	 * Creates the new java class for the association
	 * @throws JClassAlreadyExistsException
	 */
	public void create() throws JClassAlreadyExistsException {
		String name = entity.getName() + "_" + association.getEntity().getName();
		JDefinedClass class_ = creator.createStaticInnerClassDefinition(name, 
				name, associationsClass);
		if (class_ == null) {
			return;
		}
		JDefinedClass thisClass = classesMap.get(entity.getClazz());
		JDefinedClass otherClass = classesMap.get(association.getTargetEntity());
		createLink(class_, thisClass, otherClass);
		createUnlink(class_, thisClass, otherClass);
	}
	
	/**
	 * Creates a method to link the two entity objects in the association
	 * @param class_ - The association class
	 * @param thisClass - The class of the first entity
	 * @param otherClass - The class of the second entity
	 */
	private void createLink(JDefinedClass class_, JDefinedClass thisClass, 
			JDefinedClass otherClass) {
		JMethod methodLink = class_.method(JMod.PUBLIC | JMod.STATIC, Void.TYPE, 
				"link");
		JBlock bodyLink = methodLink.body();
		JVar arg0 = methodLink.param(thisClass, entity.getName());
		JVar arg1 = methodLink.param(otherClass, association.getEntity().getName());
		String setNameThis = CaseFormat.LOWER_UNDERSCORE
				.to(CaseFormat.LOWER_CAMEL, "set_" + association.getName());
		JInvocation assign1 = arg0.invoke(setNameThis).arg(arg1);
		bodyLink.add(assign1);
		String mappedBy = association.getMappedBy();
		if (mappedBy != null && !mappedBy.isEmpty()) {
			String setNameOther = mappedBy.substring(0, 1).toUpperCase() + 
					mappedBy.substring(1);
			setNameOther = "set" + setNameOther;
			JInvocation assign2 = arg1.invoke(setNameOther).arg(arg0);
			bodyLink.add(assign2);
		}
	}
	
	/**
	 * Creates a method to unlink the two entity objects in the association
	 * @param class_ - The association class
	 * @param thisClass - The class of the first entity
	 * @param otherClass - The class of the second entity
	 */
	private void createUnlink(JDefinedClass class_, JDefinedClass thisClass, 
			JDefinedClass otherClass) {
		JMethod methodUnlink = class_.method(JMod.PUBLIC | JMod.STATIC, 
				Void.TYPE, "unlink");
		JBlock bodyUnlink = methodUnlink.body();
		JVar arg0 = methodUnlink.param(thisClass, entity.getName());
		JVar arg1 = methodUnlink.param(otherClass, association.getEntity().getName());
		String setNameThis = CaseFormat.LOWER_UNDERSCORE
				.to(CaseFormat.LOWER_CAMEL, "set_" + association.getName());
		JInvocation assign1 = arg0.invoke(setNameThis).arg(JExpr._null());
		bodyUnlink.add(assign1);
		String mappedBy = association.getMappedBy();
		if (mappedBy != null && !mappedBy.isEmpty()) {
			String setNameOther = mappedBy.substring(0, 1).toUpperCase() + 
					mappedBy.substring(1);
			setNameOther = "set" + setNameOther;
			JInvocation assign2 = arg1.invoke(setNameOther).arg(JExpr._null());
			bodyUnlink.add(assign2);
		}
	}
}
