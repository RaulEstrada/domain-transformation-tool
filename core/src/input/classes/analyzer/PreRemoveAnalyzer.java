package input.classes.analyzer;

import model.lifecycle.PreRemove;

public class PreRemoveAnalyzer {
	/**
	 * Creates the lifecycle intermediate element
	 * @param methodName - The name of the method
	 * @return - The lifecycle intermediate element.
	 */
	public PreRemove createPreRemove(String methodName) {
		PreRemove preremove = new PreRemove();
		preremove.setMethodName(methodName);
		return preremove;
	}
}
