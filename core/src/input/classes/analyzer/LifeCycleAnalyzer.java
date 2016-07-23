package input.classes.analyzer;

import java.lang.reflect.AnnotatedElement;

import model.lifecycle.EntityWithLifeCycle;

public class LifeCycleAnalyzer {
	/**
	 * Analyzes the lifecycle methods of an annotated element to create the 
	 * corresponding intermediate model elements.
	 * @param member - The annotated element to be processed.
	 * @param memberName - The name of the attribute or method
	 * @param entityWithLifeCycle - The element that will contain the lifecycle
	 * methods
	 */
	public void analyzeLifeCycle(AnnotatedElement member, String memberName, 
			EntityWithLifeCycle entityWithLifeCycle) {
		javax.persistence.PrePersist prepersistAnnotation = 
				member.getAnnotation(javax.persistence.PrePersist.class);
		if (prepersistAnnotation != null) {
			entityWithLifeCycle.setPrePersist(new PrePersistAnalyzer().createPrePersist(memberName));
		}
		javax.persistence.PostPersist postpersistAnnotation = 
				member.getAnnotation(javax.persistence.PostPersist.class);
		if (postpersistAnnotation != null) {
			entityWithLifeCycle.setPostPersist(new PostPersistAnalyzer().createPostPersist(memberName));
		}
		javax.persistence.PreRemove preremoveAnnotation = 
				member.getAnnotation(javax.persistence.PreRemove.class);
		if (preremoveAnnotation != null) {
			entityWithLifeCycle.setPreRemove(new PreRemoveAnalyzer().createPreRemove(memberName));
		}
		javax.persistence.PostRemove postremoveAnnotation = 
				member.getAnnotation(javax.persistence.PostRemove.class);
		if (postremoveAnnotation != null) {
			entityWithLifeCycle.setPostRemove(new PostRemoveAnalyzer().createPostRemove(memberName));
		}
		javax.persistence.PreUpdate preUpdateAnnotation = 
				member.getAnnotation(javax.persistence.PreUpdate.class);
		if (preUpdateAnnotation != null) {
			entityWithLifeCycle.setPreUpdate(new PreUpdateAnalyzer().createPreUpdate(memberName));
		}
		javax.persistence.PostUpdate postUpdateAnnotation = 
				member.getAnnotation(javax.persistence.PostUpdate.class);
		if (postUpdateAnnotation != null) {
			entityWithLifeCycle.setPostUpdate(new PostUpdateAnalyzer().createPostUpdate(memberName));
		}
		javax.persistence.PostLoad postLoadAnnotation = 
				member.getAnnotation(javax.persistence.PostLoad.class);
		if (postLoadAnnotation != null) {
			entityWithLifeCycle.setPostLoad(new PostLoadAnalyzer().createPostLoad(memberName));
		}
	}
}
