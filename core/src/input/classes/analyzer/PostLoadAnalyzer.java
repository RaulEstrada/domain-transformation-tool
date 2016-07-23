package input.classes.analyzer;

import model.lifecycle.PostLoad;

public class PostLoadAnalyzer {
	/**
	 * Creates the lifecycle intermediate element
	 * @param methodName - The name of the method
	 * @return - The lifecycle intermediate element.
	 */
	public PostLoad createPostLoad(String methodName) {
		PostLoad postLoad = new PostLoad();
		postLoad.setMethodName(methodName);
		return postLoad;
	}
}
