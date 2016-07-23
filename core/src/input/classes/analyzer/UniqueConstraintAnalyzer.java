package input.classes.analyzer;

import model.structure.UniqueConstraint;

public class UniqueConstraintAnalyzer {
	/**
	 * Analyzes the @UniqueConstraint annotation and creates a JAXB annotated object
	 * representing it.
	 * @param annotation - The @UniqueConstraint annotation
	 * @return - The annotated JAXB object
	 */
	public UniqueConstraint createUniqueConstraint(
			javax.persistence.UniqueConstraint annotation) {
		UniqueConstraint unique = new UniqueConstraint();
		unique.setName(annotation.name());
		String[] colNames = annotation.columnNames();
		if (colNames != null && colNames.length > 0) {
			for (String colName : colNames) {
				unique.getColumnName().add(colName);
			}
		}
		return unique;
	}
}
