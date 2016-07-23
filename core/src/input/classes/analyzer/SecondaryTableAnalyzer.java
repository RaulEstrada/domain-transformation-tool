package input.classes.analyzer;

import javax.persistence.Index;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.UniqueConstraint;

import model.structure.SecondaryTable;

public class SecondaryTableAnalyzer {
	/**
	 * Analyzes the JPA annotation and creates the corresponding intermediate
	 * model element.
	 * @param annotation - JPA annotation
	 * @return - The corresponding intermediate model element.
	 */
	public SecondaryTable createSecondaryTable(javax.persistence.SecondaryTable annotation) {
		SecondaryTable secondaryTable = new SecondaryTable();
		secondaryTable.setName(annotation.name());
		secondaryTable.setCatalog(annotation.catalog());
		secondaryTable.setSchema(annotation.schema());
		PrimaryKeyJoinColumn[] pkJoinColumnAnnotations = 
				annotation.pkJoinColumns();
		for (PrimaryKeyJoinColumn pkJoinColumnAnnotation : pkJoinColumnAnnotations) {
			secondaryTable.getPrimaryKeyJoinColumn().add(
					new PrimaryKeyJoinColumnAnalyzer()
					.createPrimaryKeyJoinColumn(pkJoinColumnAnnotation));
		}
		UniqueConstraint[] uniqueConstraints = annotation.uniqueConstraints();
		for (UniqueConstraint uniqueConstraint : uniqueConstraints) {
			secondaryTable.getUniqueConstraint().add(
					new UniqueConstraintAnalyzer()
					.createUniqueConstraint(uniqueConstraint));
		}
		Index[] indexes = annotation.indexes();
		for (Index index : indexes) {
			secondaryTable.getIndex().add(new IndexAnalyzer().createIndex(index));
		}
		return secondaryTable;
	}
}
