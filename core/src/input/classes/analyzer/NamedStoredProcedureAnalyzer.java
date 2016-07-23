package input.classes.analyzer;

import model.queries.NamedStoredProcedureQuery;

public class NamedStoredProcedureAnalyzer {
	/**
	 * Analyzes the @NamedStoredProcedureQuery and creates a named stored 
	 * procedure object representing it.
	 * @param annotation - The @NamedStoredProcedureQuery annotation
	 * @return - The named stored procedure
	 */
	@SuppressWarnings("rawtypes")
	public NamedStoredProcedureQuery createNamedStoredProcedure(
			javax.persistence.NamedStoredProcedureQuery annotation) {
		NamedStoredProcedureQuery query = new NamedStoredProcedureQuery();
		query.setName(annotation.name());
		query.setProcedureName(annotation.procedureName());
		javax.persistence.StoredProcedureParameter[] params = 
				annotation.parameters();
		if (params != null && params.length > 0) {
			for (javax.persistence.StoredProcedureParameter param : params) {
				query.getParameter().add(new StoredProcedureParamAnalyzer()
						.createParameter(param));
			}
		}
		Class[] resultClasses = annotation.resultClasses();
		if (resultClasses != null && resultClasses.length > 0) {
			for (Class resultClass : resultClasses) {
				query.getResultClass().add(resultClass.getName());
			}
		}
		String[] resultSetMappings = annotation.resultSetMappings();
		if (resultSetMappings != null && resultSetMappings.length > 0) {
			for (String resultSetMapping : resultSetMappings) {
				query.getResultSetMapping().add(resultSetMapping);
			}
		}
		return query;
	}
}
