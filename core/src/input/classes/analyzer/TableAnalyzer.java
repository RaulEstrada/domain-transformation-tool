package input.classes.analyzer;

import model.structure.Table;

public class TableAnalyzer {
	/**
	 * Analyzes the @Table annotation and creates a JAXB annotated object 
	 * representing it.
	 * @param annotation - The @Table annotation
	 * @return - The SQL Table configuration of an entity
	 */
	public Table createTable(javax.persistence.Table annotation) {
		Table table = new Table();
		table.setCatalog(annotation.catalog());
		table.setSchema(annotation.schema());
		table.setName(annotation.name());
		javax.persistence.Index[] indexes = annotation.indexes();
		if (indexes != null && indexes.length > 0) {
			for (javax.persistence.Index index : indexes) {
				table.getIndex().add(new IndexAnalyzer().createIndex(index));
			}
		}
		javax.persistence.UniqueConstraint[] constraints = 
				annotation.uniqueConstraints();
		if (constraints != null && constraints.length > 0) {
			for (javax.persistence.UniqueConstraint constraint : constraints) {
				table.getUniqueConstraint().add(new UniqueConstraintAnalyzer()
						.createUniqueConstraint(constraint));
			}
		}
		return table;
	}
}
