package input.classes.analyzer;

import model.queries.ColumnResult;

public class ColumnResultAnalyzer {
	/**
	 * Analyzes the JPA annotation and creates the corresponding intermediate
	 * model element.
	 * @param annotation - JPA annotation
	 * @return - The corresponding intermediate model element.
	 */
	public ColumnResult createColumnResult(javax.persistence.ColumnResult annotation) {
		ColumnResult columnResult = new ColumnResult();
		columnResult.setName(annotation.name());
		columnResult.setClazz(annotation.type().getCanonicalName());
		return columnResult;
	}
}
