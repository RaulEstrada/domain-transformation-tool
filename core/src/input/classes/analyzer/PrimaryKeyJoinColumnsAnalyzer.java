package input.classes.analyzer;

import java.util.ArrayList;
import java.util.List;

import model.inheritance.PrimaryKeyJoinColumn;

public class PrimaryKeyJoinColumnsAnalyzer {
	/**
	 * Analyzes the JPA annotation and creates the corresponding intermediate
	 * model element.
	 * @param annotation - JPA annotation
	 * @return - The corresponding intermediate model element.
	 */
	public List<PrimaryKeyJoinColumn> createPrimaryKeyJoinColumns(
			javax.persistence.PrimaryKeyJoinColumns annotation) {
		javax.persistence.PrimaryKeyJoinColumn[] pkJoinColumnsAnnotation = annotation.value();
		List<PrimaryKeyJoinColumn> pkJoinColumns = new ArrayList<>();
		for (javax.persistence.PrimaryKeyJoinColumn pkJoinColumnAnnotation : 
			pkJoinColumnsAnnotation) {
			pkJoinColumns.add(new PrimaryKeyJoinColumnAnalyzer()
					.createPrimaryKeyJoinColumn(pkJoinColumnAnnotation));
		}
		return pkJoinColumns;
	}
}
