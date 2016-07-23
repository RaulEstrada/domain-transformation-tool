package input.classes.analyzer;

import java.util.HashSet;
import java.util.Set;

import model.queries.NamedNativeQuery;

public class NamedNativeQueriesAnalyzer {
	/**
	 * Analyzes the @NamedNativeQueries annotation and creates the named native query
	 * representing the different @NamedNativeQuery inside.
	 * @param annotation - The @NamedNativeQueries annotation
	 */
	public Set<NamedNativeQuery> createNamedNativeQueries(
			javax.persistence.NamedNativeQueries annotation) {
		javax.persistence.NamedNativeQuery[] namedNativeQueries = 
				annotation.value();
		Set<NamedNativeQuery> queries = new HashSet<>();
		if (namedNativeQueries != null && namedNativeQueries.length > 0) {
			for (javax.persistence.NamedNativeQuery queryAnnotation : 
				namedNativeQueries) {
				queries.add(new NamedNativeQueryAnalyzer()
						.createNamedNativeQuery(queryAnnotation));
			}
		}
		return queries;
	}
}
