package input.classes.analyzer;

import model.associations.MapKey;

public class MapKeyAnalyzer {
	/**
	 * Analyzes the JPA annotation and creates the corresponding intermediate
	 * model element.
	 * @param annotation - JPA annotation
	 * @return - The corresponding intermediate model element.
	 */
	public MapKey createMapKey(javax.persistence.MapKey annotation) {
		MapKey mapKey = new MapKey();
		mapKey.setName(annotation.name());
		return mapKey;
	}
}
