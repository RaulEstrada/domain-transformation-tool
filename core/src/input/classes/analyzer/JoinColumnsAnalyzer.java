package input.classes.analyzer;

import java.util.ArrayList;
import java.util.List;

import model.associations.JoinColumn;

public class JoinColumnsAnalyzer {
	/**
	 * Analyzes the JPA annotation and creates the corresponding intermediate
	 * model element.
	 * @param annotation - JPA annotation
	 * @return - The corresponding intermediate model element.
	 */
	public List<JoinColumn> createJoinColumns(javax.persistence.JoinColumns annotation) {
		javax.persistence.JoinColumn[] joinColumnAnnotations = annotation.value();
		List<JoinColumn> joinColumns = new ArrayList<>();
		for (javax.persistence.JoinColumn joinColumnAnnotation : joinColumnAnnotations) {
			joinColumns.add(new JoinColumnAnalyzer().createJoinColumn(joinColumnAnnotation));
		}
		return joinColumns;
	}
}
