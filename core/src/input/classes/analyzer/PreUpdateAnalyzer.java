package input.classes.analyzer;

import model.lifecycle.PreUpdate;

public class PreUpdateAnalyzer {
	/**
	 * Creates the lifecycle intermediate element
	 * @param methodName - The name of the method
	 * @return - The lifecycle intermediate element.
	 */
	public PreUpdate createPreUpdate(String methodName) {
		PreUpdate preUpdate = new PreUpdate();
		preUpdate.setMethodName(methodName);
		return preUpdate;
	}
}
