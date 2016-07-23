package input.classes.analyzer;

import javax.persistence.ColumnResult;
import javax.persistence.ConstructorResult;
import javax.persistence.EntityResult;

import model.queries.SqlResultSetMapping;

public class SqlResultSetMappingAnalyzer {
	/**
	 * Analyzes the JPA annotation and creates the corresponding intermediate
	 * model element.
	 * @param annotation - JPA annotation
	 * @return - The corresponding intermediate model element.
	 */
	public SqlResultSetMapping createSQLResultSetMapping(
			javax.persistence.SqlResultSetMapping annotation) {
		SqlResultSetMapping sqlResultSetMapping = new SqlResultSetMapping();
		sqlResultSetMapping.setName(annotation.name());
		ConstructorResult[] constructorResultAnnotations = annotation.classes();
		for (ConstructorResult constructorResultAnnotation : constructorResultAnnotations) {
			sqlResultSetMapping.getConstructorResult().add(
					new ConstructorResultAnalyzer()
					.createConstructorResult(constructorResultAnnotation));
		}
		ColumnResult[] columnResultAnnotations = annotation.columns();
		for (ColumnResult columnResultAnnotation : columnResultAnnotations) {
			sqlResultSetMapping.getColumnResult().add(
					new ColumnResultAnalyzer()
					.createColumnResult(columnResultAnnotation));
		}
		EntityResult[] entityResultAnnotations = annotation.entities();
		for (EntityResult entityResultAnnotation : entityResultAnnotations) {
			sqlResultSetMapping.getEntityResult().add(
					new EntityResultAnalyzer()
					.createEntityResult(entityResultAnnotation));
		}
		return sqlResultSetMapping;
	}
}
