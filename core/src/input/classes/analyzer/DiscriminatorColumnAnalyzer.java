package input.classes.analyzer;

import model.customtypes.DiscriminatorType;
import model.inheritance.DiscriminatorColumn;

public class DiscriminatorColumnAnalyzer {
	/**
	 * Analyzes the JPA annotation and creates the corresponding intermediate
	 * model element.
	 * @param annotation - JPA annotation
	 * @return - The corresponding intermediate model element.
	 */
	public DiscriminatorColumn createDiscriminatorColumn(
			javax.persistence.DiscriminatorColumn annotation) {
		DiscriminatorColumn discriminatorColumn = new DiscriminatorColumn();
		discriminatorColumn.setName(annotation.name());
		discriminatorColumn.setDiscriminatorType(DiscriminatorType
				.fromValue(annotation.discriminatorType().toString()));
		discriminatorColumn.setColumnDefinition(annotation.columnDefinition());
		discriminatorColumn.setLength(annotation.length());
		return discriminatorColumn;
	}
}
