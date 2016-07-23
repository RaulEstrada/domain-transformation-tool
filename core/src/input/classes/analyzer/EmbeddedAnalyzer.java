package input.classes.analyzer;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Type;

import model.structure.Embedded;

public class EmbeddedAnalyzer {
	/**
	 * Analyzes the element containing the @Embedded annotation to create the JAXB annotated 
	 * object representing the annotation and its structure.
	 * @param element - The element containing the @Embedded annotation
	 * @return - The JAXB annotated object
	 */
	public Embedded createEmbedded(AnnotatedElement element, String elementName, Type type) {
		Embedded embedded = new Embedded();
		embedded.setName(elementName);
		embedded.setTypeName(type.getTypeName());
		javax.persistence.AttributeOverrides attributeOverridesAnnotation =
				element.getAnnotation(javax.persistence.AttributeOverrides.class);
		if (attributeOverridesAnnotation != null) {
			embedded.getAttributeOverride().addAll(new AttributeOverridesAnalyzer().
					createAttributeOverrides(attributeOverridesAnnotation));
		}
		javax.persistence.AttributeOverride attributeOverrideAnnotation = 
				element.getAnnotation(javax.persistence.AttributeOverride.class);
		if (attributeOverrideAnnotation != null) {
			embedded.getAttributeOverride().add(new AttributeOverrideAnalyzer()
					.createAttributeOverride(attributeOverrideAnnotation));
		}
		javax.persistence.Convert convertAnnotation = 
				element.getAnnotation(javax.persistence.Convert.class);
		if (convertAnnotation != null) {
			embedded.getConvert().add(new ConvertAnalyzer().createConvert(convertAnnotation));
		}
		javax.persistence.Converts convertsAnnotation =
				element.getAnnotation(javax.persistence.Converts.class);
		if (convertsAnnotation != null) {
			embedded.getConvert().addAll(new ConvertsAnalyzer().createConverts(convertsAnnotation));
		}
		javax.persistence.AssociationOverride associationOverrideAnnotation =
				element.getAnnotation(javax.persistence.AssociationOverride.class);
		if (associationOverrideAnnotation != null) {
			embedded.getAssociationOverride().add(new AssociationOverrideAnalyzer().
					createAssociationOverride(associationOverrideAnnotation));
		}
		javax.persistence.AssociationOverrides associationOverridesAnnotation = 
				element.getAnnotation(javax.persistence.AssociationOverrides.class);
		if (associationOverridesAnnotation != null) {
			embedded.getAssociationOverride().addAll(new AssociationOverridesAnalyzer()
					.createAssociationOverrides(associationOverridesAnnotation));
		}
		return embedded;
	}
}
