package input.classes.analyzer;

import model.customtypes.LockModeType;
import model.queries.NamedQuery;

public class NamedQueryAnalyzer {
	/**
	 * Analyzes the @NamedQuery annotation and creates a named query object representing
	 * it.
	 * @param annotation - The @NamedQuery annotation
	 * @return - The named query object
	 */
	public NamedQuery createNamedQuery(javax.persistence.NamedQuery annotation) {
		NamedQuery namedQuery = new NamedQuery();
		namedQuery.setName(annotation.name());
		namedQuery.setQuery(annotation.query());
		namedQuery.setLockMode(LockModeType.fromValue(annotation.lockMode()
				.toString()));
		javax.persistence.QueryHint[] queryHints = annotation.hints();
		if (queryHints != null && queryHints.length > 0) {
			for (javax.persistence.QueryHint annot : queryHints) {
				namedQuery.getHint().add(new QueryHintAnalyzer()
						.createQueryHint(annot));
			}
		}
		return namedQuery;
	}
}
