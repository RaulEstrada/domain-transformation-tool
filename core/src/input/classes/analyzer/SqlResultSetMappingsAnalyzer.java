package input.classes.analyzer;

import java.util.ArrayList;
import java.util.List;

import model.queries.SqlResultSetMapping;

public class SqlResultSetMappingsAnalyzer {
	/**
	 * Analyzes the JPA annotation and creates the corresponding intermediate
	 * model element.
	 * @param annotation - JPA annotation
	 * @return - The corresponding intermediate model element.
	 */
	public List<SqlResultSetMapping> createSQLResultSetMappings(
			javax.persistence.SqlResultSetMappings annotation) {
		javax.persistence.SqlResultSetMapping[] sqlResultSetMappingsAnnotations =
				annotation.value();
		List<SqlResultSetMapping> sqlResultSetMappings = new ArrayList<>();
		for (javax.persistence.SqlResultSetMapping sqlResultSetMappingsAnnotation 
				: sqlResultSetMappingsAnnotations) {
			sqlResultSetMappings.add(new SqlResultSetMappingAnalyzer()
					.createSQLResultSetMapping(sqlResultSetMappingsAnnotation));
		}
		return sqlResultSetMappings;
	}
}
