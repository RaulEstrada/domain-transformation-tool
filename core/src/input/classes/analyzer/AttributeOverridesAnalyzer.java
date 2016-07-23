package input.classes.analyzer;

import java.util.ArrayList;
import java.util.List;

import model.associations.AttributeOverride;

public class AttributeOverridesAnalyzer {
	/**
	 * Analyzes the JPA annotation and creates the corresponding intermediate
	 * model element.
	 * @param annotation - JPA annotation
	 * @return - The corresponding intermediate model element.
	 */
	public List<AttributeOverride> createAttributeOverrides(
			javax.persistence.AttributeOverrides annotation) {
		javax.persistence.AttributeOverride[] attributeOverridesAnnotation = 
				annotation.value();
		List<AttributeOverride> attributeOverrides = new ArrayList<>();
		for (javax.persistence.AttributeOverride attributeOverrideAnnotation 
				: attributeOverridesAnnotation) {
			attributeOverrides.add(new AttributeOverrideAnalyzer()
					.createAttributeOverride(attributeOverrideAnnotation));
		}
		return attributeOverrides;
	}
}
