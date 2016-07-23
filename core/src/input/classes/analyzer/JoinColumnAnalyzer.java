package input.classes.analyzer;

import model.associations.JoinColumn;

public class JoinColumnAnalyzer {
	/**
	 * Analyzed the @JoinColumn annotation and creates a JAXB annotated object representing
	 * the annotation.
	 * @param annotation - The @JoinColumn annotation
	 * @return - The JAXB annotated object
	 */
	public JoinColumn createJoinColumn(javax.persistence.JoinColumn annotation) {
		JoinColumn joinColumn = new JoinColumn();
		joinColumn.setColumnDefinition(annotation.columnDefinition());
		joinColumn.setInsertable(annotation.insertable());
		joinColumn.setName(annotation.name());
		joinColumn.setNullable(annotation.nullable());
		joinColumn.setReferencedColumnName(annotation.referencedColumnName());
		joinColumn.setTable(annotation.table());
		joinColumn.setUnique(annotation.unique());
		joinColumn.setUpdatable(annotation.updatable());
		// TODO Missing ForeignKey
		return joinColumn;
	}
}
