package input.classes.analyzer;

import javax.persistence.ColumnResult;

import model.queries.ConstructorResult;

public class ConstructorResultAnalyzer {
	/**
	 * Analyzes the JPA annotation and creates the corresponding intermediate
	 * model element.
	 * @param annotation - JPA annotation
	 * @return - The corresponding intermediate model element.
	 */
	public ConstructorResult createConstructorResult(
			javax.persistence.ConstructorResult annotation) {
		ConstructorResult constructorResult = new ConstructorResult();
		constructorResult.setTargetClass(annotation.targetClass().getCanonicalName());
		javax.persistence.ColumnResult[] columnResultAnnotations = annotation.columns();
		for (ColumnResult columnResultAnnotation : columnResultAnnotations) {
			constructorResult.getColumn().add(new ColumnResultAnalyzer()
					.createColumnResult(columnResultAnnotation));
		}
		return constructorResult;
	}
}
