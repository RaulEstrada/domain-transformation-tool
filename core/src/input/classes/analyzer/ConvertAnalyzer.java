package input.classes.analyzer;

import model.queries.Convert;

public class ConvertAnalyzer {
	/**
	 * Analyzes the JPA annotation and creates the corresponding intermediate
	 * model element.
	 * @param annotation - JPA annotation
	 * @return - The corresponding intermediate model element.
	 */
	public Convert createConvert(javax.persistence.Convert annotation) {
		Convert convert = new Convert();
		convert.setAttributeName(annotation.attributeName());
		convert.setConverter(annotation.converter().getCanonicalName());
		convert.setDisableConversion(annotation.disableConversion());
		return convert;
	}
}
