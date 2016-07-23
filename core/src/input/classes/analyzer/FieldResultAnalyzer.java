package input.classes.analyzer;

import model.queries.FieldResult;

public class FieldResultAnalyzer {
	/**
	 * Analyzes the JPA annotation and creates the corresponding intermediate
	 * model element.
	 * @param annotation - JPA annotation
	 * @return - The corresponding intermediate model element.
	 */
	public FieldResult createFieldResult(javax.persistence.FieldResult annotation) {
		FieldResult fieldResult = new FieldResult();
		fieldResult.setColumn(annotation.column());
		fieldResult.setName(annotation.name());
		return fieldResult;
	}
}
