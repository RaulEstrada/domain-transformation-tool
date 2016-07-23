package input.classes.analyzer;

import javax.persistence.Index;
import javax.persistence.UniqueConstraint;

import model.generators.TableGenerator;

public class TableGeneratorAnalyzer {
	/**
	 * Analyzes the JPA annotation and creates the corresponding intermediate
	 * model element.
	 * @param annotation - JPA annotation
	 * @return - The corresponding intermediate model element.
	 */
	public TableGenerator createTableGenerator(
			javax.persistence.TableGenerator annotation) {
		TableGenerator tableGenerator = new TableGenerator();
		tableGenerator.setName(annotation.name());
		tableGenerator.setTable(annotation.table());
		tableGenerator.setCatalog(annotation.catalog());
		tableGenerator.setSchema(annotation.schema());
		tableGenerator.setPkColumnName(annotation.pkColumnName());
		tableGenerator.setPkColumnValue(annotation.pkColumnValue());
		tableGenerator.setValueColumnName(annotation.valueColumnName());
		tableGenerator.setInitialValue(annotation.initialValue());
		tableGenerator.setAllocationSize(annotation.allocationSize());
		UniqueConstraint[] uniqueConstraints = annotation.uniqueConstraints();
		for (UniqueConstraint uniqueConstraint : uniqueConstraints) {
			tableGenerator.getUniqueConstraint().add(new UniqueConstraintAnalyzer()
					.createUniqueConstraint(uniqueConstraint));
		}
		Index[] indexes = annotation.indexes();
		for (Index index : indexes) {
			tableGenerator.getIndex().add(new IndexAnalyzer().createIndex(index));
		}
		return tableGenerator;
	}
}
