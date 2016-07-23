package input.classes.analyzer;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Type;

import model.structure.Column;
import model.structure.TemporalType;
import model.structure.Version;

public class VersionAnalyzer {
	/**
	 * Analyzes the element containing the @Version annotation and creates a 
	 * JAXB annotated object representing it
	 * @param element - The element with the @Version annotation
	 * @return - The JAXB annotated object
	 */
	public Version createVersion(AnnotatedElement element, String elementName, 
			Type type) {
		Version version = new Version();
		version.setName(elementName);
		version.setTypeName(type.getTypeName());
		javax.persistence.Column columnAnnotation = 
				element.getAnnotation(javax.persistence.Column.class);
		if (columnAnnotation != null) {
			Column column = new ColumnAnalyzer().createColumn(columnAnnotation);
			version.setColumn(column);
		}
		javax.persistence.Temporal temporalAnnotation = 
				element.getAnnotation(javax.persistence.Temporal.class);
		if (temporalAnnotation != null) {
			version.setTemporal(TemporalType.fromValue(temporalAnnotation
					.value().toString()));
		}
		return version;
	}
}
