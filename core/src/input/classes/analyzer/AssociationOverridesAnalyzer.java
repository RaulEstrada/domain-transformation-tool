package input.classes.analyzer;

import java.util.ArrayList;
import java.util.List;

import model.associations.AssociationOverride;

public class AssociationOverridesAnalyzer {
	/**
	 * Analyzes the JPA annotation and creates the corresponding intermediate
	 * model element.
	 * @param annotation - JPA annotation
	 * @return - The corresponding intermediate model element.
	 */
	public List<AssociationOverride> createAssociationOverrides(
			javax.persistence.AssociationOverrides annotation) {
		javax.persistence.AssociationOverride[] associationOverrideAnnotations =
				annotation.value();
		List<AssociationOverride> associationOverrides = new ArrayList<>();
		for (javax.persistence.AssociationOverride associationOverrideAnnotation
				: associationOverrideAnnotations) {
			associationOverrides.add(new AssociationOverrideAnalyzer()
					.createAssociationOverride(associationOverrideAnnotation));
		}
		return associationOverrides;
	}
}
