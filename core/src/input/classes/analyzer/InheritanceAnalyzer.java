package input.classes.analyzer;

import model.customtypes.InheritanceType;
import model.inheritance.Inheritance;

public class InheritanceAnalyzer {
	/**
	 * Analyzes the JPA annotation and creates the corresponding intermediate
	 * model element.
	 * @param annotation - JPA annotation
	 * @return - The corresponding intermediate model element.
	 */
	public Inheritance createInhheritance(javax.persistence.Inheritance annotation) {
		Inheritance inheritance = new Inheritance();
		inheritance.setStrategy(InheritanceType.fromValue(annotation
				.strategy().toString()));
		return inheritance;
	}
}
