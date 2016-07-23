package input.classes.analyzer;

import model.structure.Column;

public class ColumnAnalyzer {
	/**
	 * Analyzes the @Column annotation, creating and returning the JAXB object
	 * representing the annotation and its values.
	 * @param annotation - the @Column annotation
	 * @return - The JAXB annotated object representing the annotation.
	 */
	public Column createColumn(javax.persistence.Column annotation) {
		Column column = new Column();
		column.setColumnDefinition(annotation.columnDefinition());
		column.setInsertable(annotation.insertable());
		column.setLength(annotation.length());
		column.setName(annotation.name());
		column.setNullable(annotation.nullable());
		column.setPrecision(annotation.precision());
		column.setScale(annotation.scale());
		column.setTable(annotation.table());
		column.setUnique(annotation.unique());
		column.setUpdatable(annotation.updatable());
		return column;
	}
}
