package input.classes.analyzer;

import java.util.ArrayList;
import java.util.List;

import model.structure.SecondaryTable;

public class SecondaryTablesAnalyzer {
	/**
	 * Analyzes the JPA annotation and creates the corresponding intermediate
	 * model element.
	 * @param annotation - JPA annotation
	 * @return - The corresponding intermediate model element.
	 */
	public List<SecondaryTable> createSecondaryTables(
			javax.persistence.SecondaryTables annotation) {
		javax.persistence.SecondaryTable[] secondaryTables = annotation.value();
		List<SecondaryTable> tables = new ArrayList<>();
		for (javax.persistence.SecondaryTable table : secondaryTables) {
			tables.add(new SecondaryTableAnalyzer().createSecondaryTable(table));
		}
		return tables;
	}
}
