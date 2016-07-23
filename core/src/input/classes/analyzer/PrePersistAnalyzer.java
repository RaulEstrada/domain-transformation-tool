package input.classes.analyzer;

import model.lifecycle.PrePersist;

public class PrePersistAnalyzer {
	/**
	 * Creates the lifecycle intermediate element
	 * @param methodName - The name of the method
	 * @return - The lifecycle intermediate element.
	 */
	public PrePersist createPrePersist(String methodName) {
		PrePersist prepersist = new PrePersist();
		prepersist.setMethodName(methodName);
		return prepersist;
	}
}
