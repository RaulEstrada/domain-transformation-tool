package input.classes.analyzer;

import java.util.ArrayList;
import java.util.List;

import model.queries.Convert;

public class ConvertsAnalyzer {
	/**
	 * Analyzes the JPA annotation and creates the corresponding intermediate
	 * model element.
	 * @param annotation - JPA annotation
	 * @return - The corresponding intermediate model element.
	 */
	public List<Convert> createConverts(javax.persistence.Converts annotation) {
		javax.persistence.Convert[] convertAnnotations = annotation.value();
		List<Convert> converts = new ArrayList<>();
		for (javax.persistence.Convert convertAnnotation : convertAnnotations) {
			converts.add(new ConvertAnalyzer().createConvert(convertAnnotation));
		}
		return converts;
	}
}
