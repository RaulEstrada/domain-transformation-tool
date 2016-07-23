package input.classes.analyzer;

import model.lifecycle.EntityListeners;

public class EntityListenersAnalyzer {
	/**
	 * Creates the entity listeners of a given entity.
	 * @param annotation - The JPA annotation
	 * @param classLoader - The ClassLoader
	 * @return - The intermediate model EntityListeners element
	 * @throws ClassNotFoundException
	 */
	@SuppressWarnings("rawtypes")
	public EntityListeners createEntityListeners(javax.persistence.EntityListeners 
			annotation, ClassLoader classLoader) throws ClassNotFoundException {
		EntityListeners entityListeners = new EntityListeners();
		for (Class clazz : annotation.value()) {
			entityListeners.getEntityListener().add(new EntityListenerAnalyzer()
					.createEntityListener(clazz, classLoader));
		}
		return entityListeners;
	}
}
