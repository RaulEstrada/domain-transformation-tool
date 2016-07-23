package input.classes.analyzer;

import java.lang.reflect.AnnotatedElement;

import model.customtypes.EnumType;
import model.customtypes.FetchType;
import model.structure.Basic;
import model.structure.Column;
import model.structure.Lob;
import model.structure.TemporalType;

public class BasicAnalyzer {
	/**
	 * Analyzes an element that hasn't produced an ORM.xml element
	 * yet but contains annotations that may be children of the implicit 
	 * @Basic annotation.
	 * @param annotation - The @Basic annotation
	 * @param element - The element analyzed for its annotations
	 * @return - An JAXB annotated object representing the annotation
	 */
	public Basic createBasic(AnnotatedElement element, String elementName) {
		javax.persistence.Basic basicAnnotation = element.getAnnotation(
				javax.persistence.Basic.class);
		Basic basic = new Basic();
		basic.setName(elementName);
		if (basicAnnotation != null) {
			basic.setOptional(basicAnnotation.optional());
			basic.setFetch(FetchType.fromValue(basicAnnotation.fetch().toString()));
		}
		javax.persistence.Column columnAnnotation = element.getAnnotation(
				javax.persistence.Column.class);
		if (columnAnnotation != null) {
			Column column = new ColumnAnalyzer().createColumn(columnAnnotation);
			basic.setColumn(column);
		}
		javax.persistence.Lob lobAnnotation = element.getAnnotation(
				javax.persistence.Lob.class);
		if (lobAnnotation != null) {
			Lob lob = new Lob();
			basic.setLob(lob);
		}
		javax.persistence.Temporal temporalAnnotation = element.getAnnotation(
				javax.persistence.Temporal.class);
		if (temporalAnnotation != null) {
			basic.setTemporal(TemporalType.fromValue(temporalAnnotation.value()
					.toString()));
		}
		javax.persistence.Enumerated enumeratedAnnotation = 
				element.getAnnotation(javax.persistence.Enumerated.class);
		if (enumeratedAnnotation != null) {
			basic.setEnumerated(EnumType.fromValue(enumeratedAnnotation.value()
					.toString()));
		}
		return (columnAnnotation != null || lobAnnotation != null || 
				temporalAnnotation != null || enumeratedAnnotation != null
				|| basicAnnotation != null) ? basic : null;
	}
}
