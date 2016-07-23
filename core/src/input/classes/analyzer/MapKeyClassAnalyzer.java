package input.classes.analyzer;

import model.associations.MapKeyClass;

public class MapKeyClassAnalyzer {
	/**
	 * Analyzes the JPA annotation and creates the corresponding intermediate
	 * model element.
	 * @param annotation - JPA annotation
	 * @return - The corresponding intermediate model element.
	 */
	public MapKeyClass createMapKeyClass(javax.persistence.MapKeyClass annotation) {
		MapKeyClass mapKeyClass = new MapKeyClass();
		mapKeyClass.setClazz(annotation.value().getCanonicalName());
		return mapKeyClass;
	}
}
