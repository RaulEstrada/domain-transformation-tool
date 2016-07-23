package input.classes.analyzer;

import model.associations.CascadeType;
import model.structure.EmptyType;

public class CascadeAnalyzer {
	/**
	 * Analyzes the cascade type returns the JAXB annotated
	 * object representing it.
	 * @param cascadeElements - The different cascade items
	 * @return - The JAXB annotated object
	 */
	public CascadeType createCascade(javax.persistence.CascadeType[] cascadeElements) {
		if (cascadeElements == null || cascadeElements.length == 0) {
			return null;
		}
		CascadeType cascade = new CascadeType();
		for (javax.persistence.CascadeType item : cascadeElements) {
			switch (item) {
			case ALL: cascade.setCascadeAll(new EmptyType()); break;
			case DETACH: cascade.setCascadeDetach(new EmptyType()); break;
			case MERGE: cascade.setCascadeMerge(new EmptyType()); break;
			case PERSIST: cascade.setCascadePersist(new EmptyType()); break;
			case REFRESH: cascade.setCascadeRefresh(new EmptyType()); break;
			case REMOVE: cascade.setCascadeRemove(new EmptyType()); break;
			}
		}
		return cascade;
	}
}
