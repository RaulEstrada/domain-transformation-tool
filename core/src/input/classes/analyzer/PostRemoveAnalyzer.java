package input.classes.analyzer;

import model.lifecycle.PostRemove;

public class PostRemoveAnalyzer {
	/**
	 * Creates the lifecycle intermediate element
	 * @param methodName - The name of the method
	 * @return - The lifecycle intermediate element.
	 */
	public PostRemove createPostRemove(String methodName) {
		PostRemove postRemove = new PostRemove();
		postRemove.setMethodName(methodName);
		return postRemove;
	}
}
