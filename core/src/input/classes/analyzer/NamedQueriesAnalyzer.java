package input.classes.analyzer;

import java.util.HashSet;
import java.util.Set;

import model.queries.NamedQuery;

public class NamedQueriesAnalyzer {
	/**
	 * Analyzes the @NamedQueries annotation and creates the different named query
	 * objects representing the @NamedQuery annotations.
	 * @param annotation - The @NamedQueries annotation
	 */
	public Set<NamedQuery> createNamedQueries(javax.persistence.NamedQueries annotation) {
		javax.persistence.NamedQuery[] namedQueriesAnnotations = annotation.value();
		Set<NamedQuery> queries = new HashSet<>();
		if (namedQueriesAnnotations != null && namedQueriesAnnotations.length > 0) {
			for (javax.persistence.NamedQuery query : namedQueriesAnnotations) {
				queries.add(new NamedQueryAnalyzer().createNamedQuery(query));
			}
		}
		return queries;
	}
}
