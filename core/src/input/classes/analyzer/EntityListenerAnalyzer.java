package input.classes.analyzer;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import model.lifecycle.EntityListener;

public class EntityListenerAnalyzer {
	/**
	 * Creates the entity listener of a given class.
	 * @param clazz - The loaded class.
	 * @param classLoader - The ClassLoader to load the class.
	 * @return - The EntityListener intermediate model.
	 * @throws ClassNotFoundException
	 */
	@SuppressWarnings("rawtypes")
	public EntityListener createEntityListener(Class clazz, ClassLoader classLoader)
			throws ClassNotFoundException {
		EntityListener entityListener = new EntityListener();
		entityListener.setClazz(clazz.getCanonicalName());
		Class loadedClass = classLoader.loadClass(clazz.getCanonicalName());
		LifeCycleAnalyzer lifeCycleAnalyzer = new LifeCycleAnalyzer();
		Field[] fields = loadedClass.getFields();
		for (Field field : fields) {
			lifeCycleAnalyzer.analyzeLifeCycle(field, field.getName(), entityListener);
		}
		Method[] methods = loadedClass.getMethods();
		for (Method method : methods) {
			lifeCycleAnalyzer.analyzeLifeCycle(method, method.getName(), entityListener);
		}
		return entityListener;
	}
}
