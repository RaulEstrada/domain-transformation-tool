package input.classes.analyzer;

import javax.lang.model.type.MirroredTypeException;

import model.queries.NamedNativeQuery;

public class NamedNativeQueryAnalyzer {
	/**
	 * Analyzes the @NamedNativeQuery annotation and creates a named native query object 
	 * representing it
	 * @param annotation - The @NamedNativeQuery annotation
	 * @return - The named native query
	 */
	public NamedNativeQuery createNamedNativeQuery(
			javax.persistence.NamedNativeQuery annotation) {
		NamedNativeQuery namedNativeQuery = new NamedNativeQuery();
		namedNativeQuery.setName(annotation.name());
		namedNativeQuery.setQuery(annotation.query());
		namedNativeQuery.setResultSetMapping(annotation.resultSetMapping());
		String resultClass = null;
		try {
			resultClass = annotation.resultClass().getName();
		} catch (MirroredTypeException ex) {
			resultClass = ex.getTypeMirror().toString();
		}
		if (!resultClass.equals("void")) {
			namedNativeQuery.setResultClass(resultClass);
		}
		javax.persistence.QueryHint[] queryHints = annotation.hints();
		if (queryHints != null && queryHints.length > 0) {
			for (javax.persistence.QueryHint annot : queryHints) {
				namedNativeQuery.getHint().add(new QueryHintAnalyzer()
						.createQueryHint(annot));
			}
		}
		return namedNativeQuery;
	}
}
