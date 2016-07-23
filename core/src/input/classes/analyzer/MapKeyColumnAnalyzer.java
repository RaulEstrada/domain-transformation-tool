package input.classes.analyzer;

import model.associations.MapKeyColumn;

public class MapKeyColumnAnalyzer {
	/**
	 * Analyzes the JPA annotation and creates the corresponding intermediate
	 * model element.
	 * @param annotation - JPA annotation
	 * @return - The corresponding intermediate model element.
	 */
	public MapKeyColumn createMapKeyColumn(javax.persistence.MapKeyColumn annotation) {
		MapKeyColumn mapKeyColumn = new MapKeyColumn();
		mapKeyColumn.setName(annotation.name());
		mapKeyColumn.setNullable(annotation.nullable());
		mapKeyColumn.setUnique(annotation.unique());
		mapKeyColumn.setInsertable(annotation.insertable());
		mapKeyColumn.setUpdatable(annotation.updatable());
		mapKeyColumn.setColumnDefinition(annotation.columnDefinition());
		mapKeyColumn.setTable(annotation.table());
		mapKeyColumn.setLength(annotation.length());
		mapKeyColumn.setPrecision(annotation.precision());
		mapKeyColumn.setScale(annotation.scale());
		return mapKeyColumn;
	}
}
