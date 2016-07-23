package input.classes.analyzer;

import model.lifecycle.PostPersist;

public class PostPersistAnalyzer {
	/**
	 * Creates the lifecycle intermediate element
	 * @param methodName - The name of the method
	 * @return - The lifecycle intermediate element.
	 */
	public PostPersist createPostPersist(String methodName) {
		PostPersist postpersist = new PostPersist();
		postpersist.setMethodName(methodName);
		return postpersist;
	}
}
