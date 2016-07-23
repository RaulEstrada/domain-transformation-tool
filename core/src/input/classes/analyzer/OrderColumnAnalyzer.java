package input.classes.analyzer;

import model.queries.OrderColumn;

public class OrderColumnAnalyzer {
	/**
	 * Analyzes the JPA annotation and creates the corresponding intermediate
	 * model element.
	 * @param annotation - JPA annotation
	 * @return - The corresponding intermediate model element.
	 */
	public OrderColumn createOrderColumn(javax.persistence.OrderColumn annotation) {
		OrderColumn orderColumn = new OrderColumn();
		orderColumn.setName(annotation.name());
		orderColumn.setNullable(annotation.nullable());
		orderColumn.setInsertable(annotation.insertable());
		orderColumn.setUpdatable(annotation.updatable());
		orderColumn.setColumnDefinition(annotation.columnDefinition());
		return orderColumn;
	}
}
