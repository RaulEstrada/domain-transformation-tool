package input.classes.analyzer;

import model.customtypes.GenerationType;
import model.generators.GeneratedValue;

public class GeneratedValueAnalyzer {
	/**
	 * Analyzes the @GeneratedValue annotation and creates a JAXB annotated 
	 * object representing
	 * it
	 * @param annotation - The @GeneratedValue annotation
	 * @return - The JAXB annotated object
	 */
	public GeneratedValue createGeneratedValue(javax.persistence.GeneratedValue annotation) {
		GeneratedValue generatedValue = new GeneratedValue();
		generatedValue.setGenerator(annotation.generator());
		generatedValue.setStrategy(GenerationType.fromValue(annotation.strategy()
				.toString()));
		return generatedValue;
	}
}
