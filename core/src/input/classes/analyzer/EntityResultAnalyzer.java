package input.classes.analyzer;

import javax.persistence.FieldResult;

import model.queries.EntityResult;

public class EntityResultAnalyzer {
	/**
	 * Analyzes the JPA annotation and creates the corresponding intermediate
	 * model element.
	 * @param annotation - JPA annotation
	 * @return - The corresponding intermediate model element.
	 */
	public EntityResult createEntityResult(javax.persistence.EntityResult annotation) {
		EntityResult entityResult = new EntityResult();
		entityResult.setEntityClass(annotation.entityClass().getCanonicalName());
		entityResult.setDiscriminatorColumn(annotation.discriminatorColumn());
		FieldResult[] fieldResultAnnotations = annotation.fields();
		for (FieldResult fieldResultAnnotation : fieldResultAnnotations) {
			entityResult.getFieldResult().add(new FieldResultAnalyzer()
					.createFieldResult(fieldResultAnnotation));
		}
		return entityResult;
	}
}
