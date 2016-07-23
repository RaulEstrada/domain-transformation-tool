package input.classes;

import java.io.File;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.Map;
import java.util.Set;

import javax.persistence.AssociationOverride;
import javax.persistence.AssociationOverrides;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Embeddable;
import javax.persistence.EntityListeners;
import javax.persistence.ExcludeDefaultListeners;
import javax.persistence.ExcludeSuperclassListeners;
import javax.persistence.IdClass;
import javax.persistence.Inheritance;
import javax.persistence.MappedSuperclass;
import javax.persistence.NamedNativeQueries;
import javax.persistence.NamedQueries;
import javax.persistence.NamedStoredProcedureQueries;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.SecondaryTable;
import javax.persistence.SecondaryTables;
import javax.persistence.SqlResultSetMapping;
import javax.persistence.SqlResultSetMappings;
import javax.persistence.Table;

import input.classes.analyzer.AssociationOverrideAnalyzer;
import input.classes.analyzer.AssociationOverridesAnalyzer;
import input.classes.analyzer.BasicAnalyzer;
import input.classes.analyzer.DiscriminatorColumnAnalyzer;
import input.classes.analyzer.EmbeddedAnalyzer;
import input.classes.analyzer.EmbeddedIdAnalyzer;
import input.classes.analyzer.EntityListenersAnalyzer;
import input.classes.analyzer.IdAnalyzer;
import input.classes.analyzer.IdClassAnalyzer;
import input.classes.analyzer.InheritanceAnalyzer;
import input.classes.analyzer.LifeCycleAnalyzer;
import input.classes.analyzer.ManyToManyAnalyzer;
import input.classes.analyzer.ManyToOneAnalyzer;
import input.classes.analyzer.NamedNativeQueriesAnalyzer;
import input.classes.analyzer.NamedQueriesAnalyzer;
import input.classes.analyzer.NamedStoredProcedureQueriesAnalyzer;
import input.classes.analyzer.OneToManyAnalyzer;
import input.classes.analyzer.OneToOneAnalyzer;
import input.classes.analyzer.PrimaryKeyJoinColumnAnalyzer;
import input.classes.analyzer.SecondaryTableAnalyzer;
import input.classes.analyzer.SecondaryTablesAnalyzer;
import input.classes.analyzer.SqlResultSetMappingAnalyzer;
import input.classes.analyzer.SqlResultSetMappingsAnalyzer;
import input.classes.analyzer.TableAnalyzer;
import input.classes.analyzer.TransientAnalyzer;
import input.classes.analyzer.VersionAnalyzer;
import model.queries.NamedNativeQuery;
import model.queries.NamedQuery;
import model.queries.NamedStoredProcedureQuery;
import model.structure.AnnotatedAttributes;
import model.structure.Attributes;
import model.structure.Basic;
import model.structure.EmbeddableAttributes;
import model.structure.EmptyType;
import model.structure.Entity;
import model.structure.Enumeration;
import model.structure.Package;

@SuppressWarnings("rawtypes")
public class ClassProcessor {
	/**
	 * Loads a .class file and creates an entity based on the content of that .class file.
	 * @param file - The .class file being loaded
	 * @param classLoader
	 * @param currentPackage - The package that contains the new entity
	 * @return - The new entity instance
	 * @throws ClassNotFoundException
	 */
	public void loadElement(File file, ClassLoader classLoader, 
			Package currentPackage, Map<String, Entity> entityMap, 
			Map<String, model.structure.Embeddable> embeddableMap, 
			String fileSourceContent) 
			throws ClassNotFoundException {
		if (file == null || classLoader == null || currentPackage == null) {
			throw new IllegalArgumentException("The file, class loader or current"
					+ " package cannot be null");
		}
		String className = file.getName().replace(".class", "");
		Class loadedClass = classLoader.loadClass(currentPackage.getFullName() 
				+ "." + className);
		LifeCycleAnalyzer lifeCycleAnalyzer = new LifeCycleAnalyzer();
		processEntity(loadedClass, currentPackage, lifeCycleAnalyzer, classLoader,
				entityMap, fileSourceContent);
		processEmbeddable(loadedClass, currentPackage, embeddableMap, fileSourceContent);
		processMappedSuperclass(loadedClass, currentPackage, lifeCycleAnalyzer,
				fileSourceContent);
		if (loadedClass.isEnum()) {
			Enumeration enumeration = new Enumeration(loadedClass.getCanonicalName(),
					loadedClass.getEnumConstants());
			currentPackage.addEnumeration(enumeration);
		}
	}
	
	/**
	 * Processes the loaded class to see if it is an entity. If it is, it processes its elements and creates
	 * the corresponding intermediate model elements.
	 * @param loadedClass - The loaded class being processed
	 * @param currentPackage - The intermediate model package
	 * @param lifeCycleAnalyzer - Element that processes life cycle events
	 */
	@SuppressWarnings("unchecked")
	private void processEntity(Class loadedClass, Package currentPackage, 
			LifeCycleAnalyzer lifeCycleAnalyzer, ClassLoader classLoader,
			Map<String, Entity> entityMap, String fileSourceContent) 
					throws ClassNotFoundException {
		javax.persistence.Entity entityAnnotation = (javax.persistence.Entity) 
				loadedClass.getAnnotation(javax.persistence.Entity.class);
		if (entityAnnotation != null) {
			String name = entityAnnotation.name();
			if (name == null || name.isEmpty()) {
				name = loadedClass.getSimpleName();
			}
			Entity entity = new Entity();
			entity.setEntityContent(fileSourceContent);
			entity.setName(name);
			entity.setClazz(loadedClass.getCanonicalName());
			entityMap.put(entity.getClazz(), entity);
			Class superClass = loadedClass.getSuperclass();
			if (superClass != null) {
				entity.setParentClassName(superClass.getCanonicalName());
			}
			entity.setAbstract(Modifier.isAbstract(loadedClass.getModifiers()));
			entity.setAttributes(new Attributes());
			currentPackage.addEntity(entity);
			javax.persistence.Table tableAnnotation = (Table) loadedClass.getAnnotation(javax.persistence.Table.class);
			if (tableAnnotation != null) {
				entity.setTable(new TableAnalyzer().createTable(tableAnnotation));
			}
			javax.persistence.SecondaryTable secondaryTableAnnotation = (SecondaryTable) loadedClass.getAnnotation(javax.persistence.SecondaryTable.class);
			if (secondaryTableAnnotation != null) {
				entity.getSecondaryTable().add(new SecondaryTableAnalyzer().createSecondaryTable(secondaryTableAnnotation));
			}
			javax.persistence.SecondaryTables secondaryTablesAnnotation = (SecondaryTables) loadedClass.getAnnotation(javax.persistence.SecondaryTables.class);
			if (secondaryTablesAnnotation != null) {
				entity.getSecondaryTable().addAll(new SecondaryTablesAnalyzer().createSecondaryTables(secondaryTablesAnnotation));
			}
			javax.persistence.PrimaryKeyJoinColumn pkJoinColumnAnnotation = (PrimaryKeyJoinColumn) loadedClass.getAnnotation(javax.persistence.PrimaryKeyJoinColumn.class);
			if (pkJoinColumnAnnotation != null) {
				entity.getPrimaryKeyJoinColumn().add(new PrimaryKeyJoinColumnAnalyzer().createPrimaryKeyJoinColumn(pkJoinColumnAnnotation));
			}
			javax.persistence.NamedNativeQueries namedNativeQueriesAnnotation = (NamedNativeQueries) loadedClass.getAnnotation(javax.persistence.NamedNativeQueries.class);
			if (namedNativeQueriesAnnotation != null) {
				Set<NamedNativeQuery> queries = new NamedNativeQueriesAnalyzer().createNamedNativeQueries(namedNativeQueriesAnnotation);
				entity.getNamedNativeQuery().addAll(queries);
			}
			javax.persistence.NamedQueries namedQueriesAnnotation = (NamedQueries) loadedClass.getAnnotation(javax.persistence.NamedQueries.class);
			if (namedQueriesAnnotation != null) {
				Set<NamedQuery> queries = new NamedQueriesAnalyzer().createNamedQueries(namedQueriesAnnotation);
				entity.getNamedQuery().addAll(queries);
			}
			NamedStoredProcedureQueries namedStProcQueries = (NamedStoredProcedureQueries) loadedClass.getAnnotation(javax.persistence.NamedStoredProcedureQueries.class);
			if (namedStProcQueries != null) {
				Set<NamedStoredProcedureQuery> queries = new NamedStoredProcedureQueriesAnalyzer().createNamedStoredProcedureQueries(namedStProcQueries);
				entity.getNamedStoredProcedureQuery().addAll(queries);
			}
			javax.persistence.IdClass idClassAnnotation = (IdClass) loadedClass.getAnnotation(javax.persistence.IdClass.class);
			if (idClassAnnotation != null) {
				entity.setIdClass(new IdClassAnalyzer().createIdClass(idClassAnnotation));
			}
			javax.persistence.AssociationOverride associationOverrideAnnotation = (AssociationOverride) loadedClass.getAnnotation(javax.persistence.AssociationOverride.class);
			if (associationOverrideAnnotation != null) {
				entity.getAssociationOverride().add(new AssociationOverrideAnalyzer().createAssociationOverride(associationOverrideAnnotation));
			}
			javax.persistence.AssociationOverrides associationOverridesAnnotation = (AssociationOverrides) loadedClass.getAnnotation(javax.persistence.AssociationOverrides.class);
			if (associationOverridesAnnotation != null) {
				entity.getAssociationOverride().addAll(new AssociationOverridesAnalyzer().createAssociationOverrides(associationOverridesAnnotation));
			}
			javax.persistence.Inheritance inheritanceAnnotation = (Inheritance) loadedClass.getAnnotation(javax.persistence.Inheritance.class);
			if (inheritanceAnnotation != null) {
				entity.setInheritance(new InheritanceAnalyzer().createInhheritance(inheritanceAnnotation));
			}
			javax.persistence.DiscriminatorColumn discriminatorColumnAnnotation = (DiscriminatorColumn) loadedClass.getAnnotation(javax.persistence.DiscriminatorColumn.class);
			if (discriminatorColumnAnnotation != null) {
				entity.setDiscriminatorColumn(new DiscriminatorColumnAnalyzer().createDiscriminatorColumn(discriminatorColumnAnnotation));
			}
			javax.persistence.DiscriminatorValue discriminatorValueAnnotation = (DiscriminatorValue) loadedClass.getAnnotation(javax.persistence.DiscriminatorValue.class);
			if (discriminatorValueAnnotation != null) {
				entity.setDiscriminatorValue(discriminatorValueAnnotation.value());
			}
			javax.persistence.EntityListeners entityListenersAnnotation = (EntityListeners) loadedClass.getAnnotation(javax.persistence.EntityListeners.class);
			if (entityListenersAnnotation != null) {
				entity.setEntityListeners(new EntityListenersAnalyzer().createEntityListeners(entityListenersAnnotation, classLoader));
			}
			javax.persistence.ExcludeDefaultListeners excludeDefaultListenersAnnotation = (ExcludeDefaultListeners) loadedClass.getAnnotation(javax.persistence.ExcludeDefaultListeners.class);
			if (excludeDefaultListenersAnnotation != null) {
				entity.setExcludeDefaultListeners(new EmptyType());
			}
			javax.persistence.ExcludeSuperclassListeners excludeSuperclassListenersAnnotation = (ExcludeSuperclassListeners) loadedClass.getAnnotation(javax.persistence.ExcludeSuperclassListeners.class);
			if (excludeSuperclassListenersAnnotation != null) {
				entity.setExcludeSuperclassListeners(new EmptyType());
			}
			javax.persistence.SqlResultSetMappings sqlResultSetMappingsAnnotation = (SqlResultSetMappings) loadedClass.getAnnotation(javax.persistence.SqlResultSetMappings.class);
			if (sqlResultSetMappingsAnnotation != null) {
				entity.getSqlResultSetMapping().addAll(new SqlResultSetMappingsAnalyzer().createSQLResultSetMappings(sqlResultSetMappingsAnnotation));
			}
			javax.persistence.SqlResultSetMapping sqlResultSetMappingAnnotation = (SqlResultSetMapping) loadedClass.getAnnotation(javax.persistence.SqlResultSetMapping.class);
			if (sqlResultSetMappingAnnotation != null) {
				entity.getSqlResultSetMapping().add(new SqlResultSetMappingAnalyzer().createSQLResultSetMapping(sqlResultSetMappingAnnotation));
			}
			Field[] fields = loadedClass.getDeclaredFields();
			for (Field field : fields) {
				processMember(field, field.getName(), entity.getAttributes(), field.getGenericType());
				lifeCycleAnalyzer.analyzeLifeCycle(field, field.getName(), entity);
			}
			Method[] methods = loadedClass.getDeclaredMethods();
			for (Method method : methods) {
				processMember(method, method.getName(), entity.getAttributes(), method.getGenericReturnType());
				lifeCycleAnalyzer.analyzeLifeCycle(method, method.getName(), entity);
			}
		} 
	}
	
	/**
	 * Processes the loaded class to see if it is an embeddable element. If it is, it processes its elements and creates the 
	 * corresponding intermediate elements.
	 * @param loadedClass - The loaded class being processed
	 * @param currentPackage - The current intermediate model package
	 */
	@SuppressWarnings("unchecked")
	private void processEmbeddable(Class loadedClass, Package currentPackage, Map<String, model.structure.Embeddable> embeddableMap,
			 String fileSourceContent) {
		javax.persistence.Embeddable embeddableAnnotation = (Embeddable) loadedClass.getAnnotation(javax.persistence.Embeddable.class);
		if (embeddableAnnotation != null) {
			model.structure.Embeddable embeddable = new model.structure.Embeddable();
			embeddable.setEmbeddableContent(fileSourceContent);
			embeddable.setClazz(loadedClass.getCanonicalName());
			embeddable.setName(loadedClass.getSimpleName());
			embeddable.setAbstract(Modifier.isAbstract(loadedClass.getModifiers()));
			embeddable.setAttributes(new EmbeddableAttributes());
			embeddableMap.put(embeddable.getClazz(), embeddable);
			Field[] fields = loadedClass.getDeclaredFields();
			for (Field field : fields) {
				processMember(field, field.getName(), embeddable.getAttributes(), field.getGenericType());
			}
			Method[] methods = loadedClass.getDeclaredMethods();
			for (Method method : methods) {
				processMember(method, method.getName(), embeddable.getAttributes(), method.getGenericReturnType());
			}
			currentPackage.addValueObject(embeddable);
		}
	}
	
	/**
	 * Processes the loaded class to see if it is a mapped super class. If it is, it processes its elements and creates
	 * the corresponding intermediate model elements.
	 * @param loadedClass - The loaded class being processed
	 * @param currentPackage - The intermediate model package
	 * @param lifeCycleAnalyzer - Element that processes life cycle events
	 */
	@SuppressWarnings("unchecked")
	private void processMappedSuperclass(Class loadedClass, Package currentPackage, LifeCycleAnalyzer lifeCycleAnalyzer, String fileSourceContent) {
		javax.persistence.MappedSuperclass mappedSuperclassAnnotation = (MappedSuperclass) loadedClass.getAnnotation(javax.persistence.MappedSuperclass.class);
		if (mappedSuperclassAnnotation != null) {
			model.structure.MappedSuperclass mappedSuperclass = new model.structure.MappedSuperclass();
			mappedSuperclass.setMappedSuperclassContent(fileSourceContent);
			mappedSuperclass.setAttributes(new Attributes());
			javax.persistence.IdClass idClassAnnotation = (IdClass) loadedClass.getAnnotation(javax.persistence.IdClass.class);
			if (idClassAnnotation != null) {
				mappedSuperclass.setIdClass(new IdClassAnalyzer().createIdClass(idClassAnnotation));
			}
			mappedSuperclass.setClazz(loadedClass.getCanonicalName());
			Field[] fields = loadedClass.getDeclaredFields();
			for (Field field : fields) {
				processMember(field, field.getName(), mappedSuperclass.getAttributes(), field.getGenericType());
				lifeCycleAnalyzer.analyzeLifeCycle(field, field.getName(), mappedSuperclass);
			}
			Method[] methods = loadedClass.getDeclaredMethods();
			for (Method method : methods) {
				processMember(method, method.getName(), mappedSuperclass.getAttributes(), method.getGenericReturnType());
				lifeCycleAnalyzer.analyzeLifeCycle(method, method.getName(), mappedSuperclass);
			}
			currentPackage.addMappedSuperclass(mappedSuperclass);
		}
	}
	
	/**
	 * Processes members of entities, embeddable, mapped superclasses. These members are attributes or methods.
	 * @param member - The attribute or method being processed.
	 * @param memberName - The name of the attribute or method
	 * @param attributes - The attributes of the entity/embeddable/mapped superclass already created.
	 * @param type - The type of the attribute or method being processed.
	 */
	private void processMember(AnnotatedElement member, String memberName, AnnotatedAttributes attributes, Type type) {
		boolean hasProducedElement = false;
		javax.persistence.Id idAnnotation = member.getAnnotation(javax.persistence.Id.class);
		if (idAnnotation != null) {
			attributes.getId().add(new IdAnalyzer().createId(member, memberName, type));
			hasProducedElement = true;
		}
		javax.persistence.Version versionAnnotation = member.getAnnotation(javax.persistence.Version.class);
		if (versionAnnotation != null) {
			attributes.getVersion().add(new VersionAnalyzer().createVersion(member, memberName, type));
			hasProducedElement = true;
		}
		javax.persistence.Embedded embeddedAnnotation = member.getAnnotation(javax.persistence.Embedded.class);
		if (embeddedAnnotation != null) {
			attributes.getEmbedded().add(new EmbeddedAnalyzer().createEmbedded(member, memberName, type));
			hasProducedElement = true;
		}
		javax.persistence.OneToMany oneToManyAnnotation = member.getAnnotation(javax.persistence.OneToMany.class);
		if (oneToManyAnnotation != null) {
			attributes.getOneToMany().add(new OneToManyAnalyzer().createOneToMany(oneToManyAnnotation, memberName, member, type));
			hasProducedElement = true;
		}
		javax.persistence.OneToOne oneToOneAnnotation = member.getAnnotation(javax.persistence.OneToOne.class);
		if (oneToOneAnnotation != null) {
			attributes.getOneToOne().add(new OneToOneAnalyzer().createOneToOne(oneToOneAnnotation, memberName, member, type));
			hasProducedElement = true;
		}
		javax.persistence.ManyToOne manyToOneAnnotation = member.getAnnotation(javax.persistence.ManyToOne.class);
		if (manyToOneAnnotation != null) {
			attributes.getManyToOne().add(new ManyToOneAnalyzer().createManyToOne(member, memberName, manyToOneAnnotation, type));
			hasProducedElement = true;
		}
		javax.persistence.ManyToMany manyToManyAnnotation = member.getAnnotation(javax.persistence.ManyToMany.class);
		if (manyToManyAnnotation != null) {
			attributes.getManyToMany().add(new ManyToManyAnalyzer().createManyToMany(member, memberName, manyToManyAnnotation, type));
			hasProducedElement = true;
		}
		javax.persistence.EmbeddedId embeddedIdAnnotation = member.getAnnotation(javax.persistence.EmbeddedId.class);
		if (embeddedIdAnnotation != null) {
			attributes.setEmbeddedId(new EmbeddedIdAnalyzer().createEmbeddedId(member, memberName));
			hasProducedElement = true;
		}
		javax.persistence.Transient transientAnnotation = member.getAnnotation(javax.persistence.Transient.class);
		if (transientAnnotation != null) {
			attributes.getTransient().add(new TransientAnalyzer().createTransient(memberName, type));
			hasProducedElement = true;
		}
		Basic basic = new BasicAnalyzer().createBasic(member, memberName);
		if (basic != null && !hasProducedElement) {
			basic.setTypeName(type.getTypeName());
			attributes.getBasic().add(basic);
		}
	}

}
