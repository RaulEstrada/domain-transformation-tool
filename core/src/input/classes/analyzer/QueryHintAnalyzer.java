package input.classes.analyzer;

import model.queries.QueryHint;

public class QueryHintAnalyzer {
	/**
	 * Analyzes the @QueryHint annotation and creates a JAXB annotated object
	 * representing it.
	 * @param annotation - The @QueryHint annotation
	 * @return - The JAXB annotated object
	 */
	public QueryHint createQueryHint(javax.persistence.QueryHint annotation) {
		QueryHint hint = new QueryHint();
		hint.setName(annotation.name());
		hint.setValue(annotation.value());
		return hint;
	}
}
