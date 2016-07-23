package input.classes.analyzer;

import java.lang.reflect.AnnotatedElement;

import model.structure.EmbeddedId;

public class EmbeddedIdAnalyzer {
	
	/**
	 * Processes an annotated element to create its corresponding EmbeddedId
	 * intermediate model element.
	 * @param element - The annotated element to be processed.
	 * @param elementName - The name of the embeddedId element.
	 * @return
	 */
	public EmbeddedId createEmbeddedId(AnnotatedElement element, String elementName) {
		EmbeddedId embeddedId = new EmbeddedId();
		javax.persistence.AttributeOverrides attributeOverridesAnnotation = 
				element.getAnnotation(javax.persistence.AttributeOverrides.class);
		if (attributeOverridesAnnotation != null) {	
			embeddedId.getAttributeOverride().addAll(new AttributeOverridesAnalyzer()
					.createAttributeOverrides(attributeOverridesAnnotation));

		}
		javax.persistence.AttributeOverride attributeOverrideAnnotation = 
				element.getAnnotation(javax.persistence.AttributeOverride.class);
		if (attributeOverrideAnnotation != null) {
			embeddedId.getAttributeOverride().add(new AttributeOverrideAnalyzer()
					.createAttributeOverride(attributeOverrideAnnotation));
		}
		embeddedId.setName(elementName);
		return embeddedId;
	}
}
