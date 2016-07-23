package input.classes.analyzer;

import model.associations.MapKeyJoinColumn;

public class MapKeyJoinColumnAnalyzer {
	/**
	 * Analyzes the JPA annotation and creates the corresponding intermediate
	 * model element.
	 * @param annotation - JPA annotation
	 * @return - The corresponding intermediate model element.
	 */
	public MapKeyJoinColumn createMapKeyJoinColumn(
			javax.persistence.MapKeyJoinColumn annotation) {
		MapKeyJoinColumn mapKeyJoinColumn = new MapKeyJoinColumn();
		mapKeyJoinColumn.setName(annotation.name());
		mapKeyJoinColumn.setReferencedColumnName(annotation.referencedColumnName());
		mapKeyJoinColumn.setUnique(annotation.unique());
		mapKeyJoinColumn.setNullable(annotation.nullable());
		mapKeyJoinColumn.setInsertable(annotation.insertable());
		mapKeyJoinColumn.setUpdatable(annotation.updatable());
		mapKeyJoinColumn.setColumnDefinition(annotation.columnDefinition());
		mapKeyJoinColumn.setTable(annotation.table());
		return mapKeyJoinColumn;
	}
}
