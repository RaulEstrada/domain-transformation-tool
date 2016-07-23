package input.classes.analyzer;

import model.associations.AttributeOverride;
import model.structure.Column;

public class AttributeOverrideAnalyzer {
	/**
	 * Analyzes the @AttributeOverride annotation and creates a JAXB annotated object representing
	 * the annotation.
	 * @param annotation - The @AttributeOverride annotation
	 * @param element - The element containing the @AttributeOverride annotation
	 * @return - The JAXB annotated object
	 */
	public AttributeOverride createAttributeOverride(
			javax.persistence.AttributeOverride annotation) {
		AttributeOverride attributeOverride = new AttributeOverride();
		attributeOverride.setName(annotation.name());
		javax.persistence.Column columnAnnotation = annotation.column();
		if (columnAnnotation != null) {
			Column column = new ColumnAnalyzer().createColumn(columnAnnotation);
			attributeOverride.setColumn(column);
		}
		return attributeOverride;
	}
}
