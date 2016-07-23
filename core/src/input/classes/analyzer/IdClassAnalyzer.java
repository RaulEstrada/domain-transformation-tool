package input.classes.analyzer;

import model.inheritance.IdClass;

public class IdClassAnalyzer {
	/**
	 * Analyzes the JPA annotation and creates the corresponding intermediate
	 * model element.
	 * @param annotation - JPA annotation
	 * @return - The corresponding intermediate model element.
	 */
	public IdClass createIdClass(javax.persistence.IdClass annotation) {
		IdClass idClass = new IdClass();
		idClass.setClazz(annotation.value().getCanonicalName());
		return idClass;
	}
}
