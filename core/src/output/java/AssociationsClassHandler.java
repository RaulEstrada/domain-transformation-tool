package output.java;

import java.util.Map;

import com.sun.codemodel.JClassAlreadyExistsException;
import com.sun.codemodel.JDefinedClass;

import model.associations.ManyToMany;
import model.associations.ManyToOne;
import model.associations.OneToMany;
import model.associations.OneToOne;
import model.structure.Entity;

public class AssociationsClassHandler {
	private JDefinedClass associationsClass;
	private Creator creator;
	private Map<String, JDefinedClass> classesMap;
	
	/**
	 * Initializes the associations class handler element.
	 * @param associationsClass - The java static class that will contain inner
	 * classes
	 * @param creator - The CodeModel creator
	 * @param classesMap - The map of classes.
	 */
	public AssociationsClassHandler(JDefinedClass associationsClass, 
			Creator creator, Map<String, JDefinedClass> classesMap) {
		if (associationsClass == null) {
			throw new IllegalArgumentException("Associations class cannot be null");
		}
		this.associationsClass = associationsClass;
		this.creator = creator;
		this.classesMap = classesMap;
	}
	
	/**
	 * Creates an inner class to handle one to one associations.
	 * @param association - The one to one association
	 * @param entity - The entity that participates in the association
	 * @throws JClassAlreadyExistsException
	 */
	public void associateOneToOne(OneToOne association, Entity entity) 
			throws JClassAlreadyExistsException {
		OneToOne other = association.getEntity().getCorrespondingOneToOne(
				entity.getClazz(), association.getName());
		if (other == null) {
			new OneToOneAssociationClass(associationsClass, creator, classesMap,
					entity, association).create();
		}
	}
	
	/**
	 * Creates an inner class to handle one to many associations.
	 * @param association - The one to many association
	 * @param entity - The entity that participates in the association
	 * @throws JClassAlreadyExistsException
	 */
	public void associateOneToMany(OneToMany association, Entity entity) 
			throws JClassAlreadyExistsException {
		if (association.getMappedBy() == null || 
				association.getMappedBy().isEmpty()) {
			new OneToManyAssociationClass(associationsClass, creator, classesMap,
					entity, association).create();
		}
	}
	
	/**
	 * Creates an inner class to handle many to one associations.
	 * @param association - The many to one association
	 * @param entity - The entity that participates in the association
	 * @throws JClassAlreadyExistsException
	 */
	public void associateManyToOne(ManyToOne association, Entity entity) 
			throws JClassAlreadyExistsException {
		OneToMany other = entity.getCorrespondingOneToMany(
				association.getTargetEntity(), association.getName());
		if (other == null) {
			new ManyToOneAssociationClass(associationsClass, creator, classesMap,
					entity, association).create();
		}
	}
	
	/**
	 * Creates an inner class to handle many to many associations.
	 * @param association - The many to many association
	 * @param entity - The entity that participates in the association
	 * @throws JClassAlreadyExistsException
	 */
	public void associateManyToMany(ManyToMany association, Entity entity) 
			throws JClassAlreadyExistsException {
		ManyToMany other = association.getEntity().getCorrespondingManyToMany(
				entity.getClazz(), association.getName());
		if (other == null) {
			new ManyToManyAssociationClass(associationsClass, creator, 
					classesMap, entity, association).create();
		}
	}
}
