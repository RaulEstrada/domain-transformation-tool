package input.classes.analyzer;

import java.util.HashSet;
import java.util.Set;

import model.queries.NamedStoredProcedureQuery;

public class NamedStoredProcedureQueriesAnalyzer {
	/**
	 * Analyzes the @NamedStoredProcedureQueries and creates the different 
	 *  named stored procedure query objects representing the multiple 
	 *  @NamedStoredProcedureQuery
	 * @param annotation - The @NamedStoredProcedureQueries annotation
	 */
	public Set<NamedStoredProcedureQuery> createNamedStoredProcedureQueries(
			javax.persistence.NamedStoredProcedureQueries annotation) {
		javax.persistence.NamedStoredProcedureQuery[] queries = annotation.value();
		Set<NamedStoredProcedureQuery> procedures = new HashSet<>();
		if (queries != null && queries.length > 0) {
			for (javax.persistence.NamedStoredProcedureQuery query : queries) {
				procedures.add(new NamedStoredProcedureAnalyzer()
						.createNamedStoredProcedure(query));
			}
		}
		return procedures;
	}
}
