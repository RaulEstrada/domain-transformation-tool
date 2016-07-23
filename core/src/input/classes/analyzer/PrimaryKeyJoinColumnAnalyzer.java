package input.classes.analyzer;

import model.inheritance.PrimaryKeyJoinColumn;

public class PrimaryKeyJoinColumnAnalyzer {
	/**
	 * Analyzes the JPA annotation and creates the corresponding intermediate
	 * model element.
	 * @param annotation - JPA annotation
	 * @return - The corresponding intermediate model element.
	 */
	public PrimaryKeyJoinColumn createPrimaryKeyJoinColumn(
			javax.persistence.PrimaryKeyJoinColumn annotation) {
		PrimaryKeyJoinColumn pkJoinColumn = new PrimaryKeyJoinColumn();
		pkJoinColumn.setName(annotation.name());
		pkJoinColumn.setColumnDefinition(annotation.columnDefinition());
		pkJoinColumn.setReferencedColumnName(annotation.referencedColumnName());
		return pkJoinColumn;
	}
}
