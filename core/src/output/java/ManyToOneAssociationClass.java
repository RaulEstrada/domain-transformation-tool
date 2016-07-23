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

import model.associations.ManyToOne;
import model.associations.OneToMany;
import model.structure.Entity;

public class ManyToOneAssociationClass {
	private JDefinedClass associationsClass;
	private Creator creator;
	private Map<String, JDefinedClass> classesMap;
	private Entity entity;
	private ManyToOne association;
	
	/**
	 * Initializes the many to one association class.
	 * @param associationsClass - The new java class that contains the association
	 * @param creator - The creator of java elements.
	 * @param classesMap - The map of classes
	 * @param entity - The entity that participates in the association
	 * @param association - The association
	 */
	public ManyToOneAssociationClass(JDefinedClass associationsClass, 
			Creator creator, Map<String, JDefinedClass> classesMap, 
			Entity entity, ManyToOne association){
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
		link(class_, thisClass, otherClass);
		unlink(class_, thisClass, otherClass);
	}
	
	/**
	 * Creates a method to link the two entity objects in the association
	 * @param class_ - The association class
	 * @param thisClass - The class of the first entity
	 * @param otherClass - The class of the second entity
	 */
	private void link(JDefinedClass class_, JDefinedClass thisClass, 
			JDefinedClass otherClass) {
		JMethod methodLink = class_.method(JMod.PUBLIC | JMod.STATIC, Void.TYPE,
				"link");
		JBlock bodyLink = methodLink.body();
		JVar arg0 = methodLink.param(thisClass, entity.getName());
		JVar arg1 = methodLink.param(otherClass, association.getEntity().getName());
		String setName = CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, 
				"set_" + association.getName());
		JInvocation assign1 = arg0.invoke(setName).arg(arg1);
		bodyLink.add(assign1);
		OneToMany oneToMany = association.getEntity().getCorrespondingOneToMany(
				entity.getClazz(), association.getName());
		if (oneToMany != null) {
			String setNameThis = CaseFormat.LOWER_UNDERSCORE
					.to(CaseFormat.LOWER_CAMEL, "get_" + oneToMany.getName());
			setNameThis = "_" + setNameThis;
			JInvocation assign2 = arg1.invoke(setNameThis).invoke("add").arg(arg0);
			bodyLink.add(assign2);
		}
	}
	
	/**
	 * Creates a method to unlink the two entity objects in the association
	 * @param class_ - The association class
	 * @param thisClass - The class of the first entity
	 * @param otherClass - The class of the second entity
	 */
	private void unlink(JDefinedClass class_, JDefinedClass thisClass, 
			JDefinedClass otherClass) {
		JMethod methodUnlink = class_.method(JMod.PUBLIC | JMod.STATIC, 
				Void.TYPE, "unlink");
		JBlock bodyUnlink = methodUnlink.body();
		JVar arg0 = methodUnlink.param(thisClass, entity.getName());
		JVar arg1 = methodUnlink.param(otherClass, association.getEntity().getName());
		OneToMany oneToMany = association.getEntity().getCorrespondingOneToMany(
				entity.getClazz(), association.getName());
		if (oneToMany != null) {
			String setNameThis = CaseFormat.LOWER_UNDERSCORE
					.to(CaseFormat.LOWER_CAMEL, "get_" + oneToMany.getName());
			setNameThis = "_" + setNameThis;
			JInvocation assign2 = arg1.invoke(setNameThis).invoke("remove")
					.arg(arg0);
			bodyUnlink.add(assign2);
		}
		String setName = CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, 
				"set_" + association.getName());
		JInvocation assign1 = arg0.invoke(setName).arg(JExpr._null());
		bodyUnlink.add(assign1);
	}
}
