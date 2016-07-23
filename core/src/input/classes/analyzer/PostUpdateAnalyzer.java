package input.classes.analyzer;

import model.lifecycle.PostUpdate;

public class PostUpdateAnalyzer {
	/**
	 * Creates the lifecycle intermediate element
	 * @param methodName - The name of the method
	 * @return - The lifecycle intermediate element.
	 */
	public PostUpdate createPostUpdate(String methodName) {
		PostUpdate postUpdate = new PostUpdate();
		postUpdate.setMethodName(methodName);
		return postUpdate;
	}
}
