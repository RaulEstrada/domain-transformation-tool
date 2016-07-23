package input.classes.analyzer;

import java.lang.reflect.Type;

import model.structure.Transient;

public class TransientAnalyzer {
	/**
	 * Creates an intermediate transient element
	 * @param elementName - The name of the transient element
	 * @param type - The type of the transient element
	 * @return - The new intermediate transient element
	 */
	public Transient createTransient(String elementName, Type type) {
		Transient transientElement = new Transient();
		transientElement.setTypeName(type.getTypeName());
		transientElement.setName(elementName);
		return transientElement;
	}
}
