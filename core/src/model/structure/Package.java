package model.structure;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * The package class represents any element containing other packages or classes.
 * @author Raul Estrada
 *
 */
@SuppressWarnings("rawtypes")
public class Package {
	/**
	 * The name of the package
	 */
	private String name;
	/**
	 * The parent package of the current package. If this element is null, it means the current 
	 * package is the root package.
	 */
	private Package parentPackage;
	/**
	 * The set of subpackages, or packages nested inside the current package.
	 */
	private Set<Package> childrenPackages = new HashSet<>();
	/**
	 * The set of entities contained in this package
	 */
	private Set<Entity> entities = new HashSet<>();
	/**
	 * The set of value objects contained in this package
	 */
	private Set<Embeddable> valueObjects = new HashSet<>();
	
	private Set<MappedSuperclass> mappedSuperclasses = new HashSet<>();
	
	private Set<Class> classesWithLifeCycle = new HashSet<>();
	
	private Set<Enumeration> enumerations = new HashSet<>();
	
	/**
	 * Creates a new package with the given name
	 * @param name - The non-empty name of the new package
	 */
	public Package(String name) {
		if (name == null || name.isEmpty()) {
			throw new IllegalArgumentException("The name of the package cannot be null or empty");
		}
		this.name = name;
	}
	
	/**
	 * Creates a new package with the given name and the given package parent.
	 * @param name - The non-empty name of the new package
	 * @param parentPackage - The parent package of the current one.
	 */
	public Package(String name, Package parentPackage) {
		this(name);
		if (parentPackage == null) {
			throw new IllegalArgumentException("The parent of the current package cannot be null");
		}
		parentPackage.addChildrenPackage(this);
		this.parentPackage = parentPackage;
	}
	
	/**
	 * Adds a new child or nested package to the current one.
	 * @param childPackage - The new package nested inside the current one.
	 */
	public void addChildrenPackage(Package childPackage) {
		if (childPackage == null) {
			throw new IllegalArgumentException("Cannot add a null child package to the current package");
		}
		this.childrenPackages.add(childPackage);
	}
	
	/**
	 * Removes a child from the set of nested packages in this one.
	 * @param childPackage - The child to remove from the nested packages.
	 */
	public void removeChildrenPackage(Package childPackage) {
		if (childPackage == null) {
			throw new IllegalArgumentException("Cannot remove a null child package from the current package");
		}
		this.childrenPackages.remove(childPackage);
	}
	
	/**
	 * Adds a new entity to the current package.
	 * @param entity - The new entity to be placed in this package
	 */
	public void addEntity(Entity entity) {
		if (entity == null) {
			throw new IllegalArgumentException("Cannot add a null entity to the current package");
		}
		this.entities.add(entity);
	}
	
	public void addAllEntities(Collection<Entity> entities) {
		if (entities == null) {
			throw new IllegalArgumentException("Cannot add a null collection of entities to the current package");
		}
		this.entities.addAll(entities);
	}
	
	/**
	 * Adds a new value object to the current package
	 * @param valueObject - The new value object to be placed in this package.
	 */
	public void addValueObject(Embeddable valueObject) {
		if (valueObject == null) {
			throw new IllegalArgumentException("Cannot add a null value object to the current package");
		}
		this.valueObjects.add(valueObject);
	}
	
	public void addMappedSuperclass(MappedSuperclass mappedSuperclass) {
		if (mappedSuperclass == null) {
			throw new IllegalArgumentException("Cannot add a null mapped superclass to the current package");
		}
		this.mappedSuperclasses.add(mappedSuperclass);
	}
	
	public void addClassWithLifeCycle(Class classWithLifeCycle) {
		if (classWithLifeCycle == null) {
			throw new IllegalArgumentException("Cannot add a null class with life cycle to the current package");
		}
		this.classesWithLifeCycle.add(classWithLifeCycle);
	}
	
	public void addEnumeration(Enumeration enumeration) {
		if (enumeration == null) {
			throw new IllegalArgumentException("Cannot add a null enumeration to the current package");
		}
		this.enumerations.add(enumeration);
	}
	
	/**
	 * Gets and returns an unmodifiable set of packages nested inside the current package.
	 * @return - An unmodifiable set of packages that are nested inside the current one.
	 */
	public Set<Package> getChildrenPackages() {
		return Collections.unmodifiableSet(this.childrenPackages);
	}
	
	/**
	 * Gets and returns an unmodifiable set of entities contained in this package.
	 * @return - An unmodifiable set of entities contained in this package.
	 */
	public Set<Entity> getEntities() {
		return Collections.unmodifiableSet(this.entities);
	}
	
	/**
	 * Gets and returns an unmodifiable set of value objects contained in this package.
	 * @return - An unmodifiable set of entities contained in this package.
	 */
	public Set<Embeddable> getValueObjects() {
		return Collections.unmodifiableSet(this.valueObjects);
	}
	
	public Set<MappedSuperclass> getMappedSuperclasses() {
		return Collections.unmodifiableSet(this.mappedSuperclasses);
	}
	
	public Set<Class> getClassesWithLifeCycle() {
		return Collections.unmodifiableSet(this.classesWithLifeCycle);
	}
	
	public Set<Enumeration> getEnumerations() {
		return Collections.unmodifiableSet(this.enumerations);
	}
	
	/**
	 * Gets and returns the parent package of the current one
	 * @return - The parent package of the current one.
	 */
	public Package getParentPackage() {
		return this.parentPackage;
	}
	
	/**
	 * Gets and returns the name of the package
	 * @return - The name of the package
	 */
	public String getName() {
		return this.name;
	}
	
	/**
	 * Checks if the package is empty. A package is empty if it does not contain any entity, and no sub-package contains any entity as well.
	 * @return - True if the package is empty. False otherwise.
	 */
	public boolean isEmpty() {
		for (Package pckg : this.childrenPackages) {
			if (!pckg.isEmpty()) {
				return false;
			}
		}
		return this.entities.isEmpty() && this.valueObjects.isEmpty() && this.mappedSuperclasses.isEmpty()
				&& this.classesWithLifeCycle.isEmpty() && this.enumerations.isEmpty();
	}
	
	/**
	 * Gets and returns the fully qualified name of the current package
	 * @return - The fully qualified name of the current package
	 */
	public String getFullName() {
		if (this.parentPackage != null) {
			String fullName = this.parentPackage.getFullName();
			if (!fullName.isEmpty()) {
				fullName += ".";
			}
			fullName += this.name;
			return fullName;
		} else {
			return "";
		}
	}
	
	/**
	 * Gets and returns all the entities placed somewhere in the subtree that has this package as the root package.
	 * @return - All entities in the package or any package nested inside this package
	 */
	public Set<Entity> getAllEntitiesInPackageTree() {
		Set<Entity> entities = new HashSet<>();
		for (Package pckg : this.childrenPackages) {
			entities.addAll(pckg.getAllEntitiesInPackageTree());
		}
		entities.addAll(this.getEntities());
		return entities;
	}
	
	/**
	 * Gets and returns all the value objects placed somewhere in the subtree that has this package as the root package.
	 * @return - All value objects in the package or any package nested inside this package
	 */
	public Set<Embeddable> getAllValueObjectsInPackageTree() {
		Set<Embeddable> valueObjects = new HashSet<>();
		for (Package pckg : this.childrenPackages) {
			valueObjects.addAll(pckg.getAllValueObjectsInPackageTree());
		}
		valueObjects.addAll(this.getValueObjects());
		return valueObjects;
	}
	
	public Set<Enumeration> getAllEnumsInPackageTree() {
		Set<Enumeration> enums = new HashSet<>();
		for (Package pckg : this.childrenPackages) {
			enums.addAll(pckg.getAllEnumsInPackageTree());
		}
		enums.addAll(this.getEnumerations());
		return enums;
	}
	
	public Set<MappedSuperclass> getAllMappedSuperclassesInPackageTree() {
		Set<MappedSuperclass> mappedSuperclasses = new HashSet<>();
		for (Package pckg : this.childrenPackages) {
			mappedSuperclasses.addAll(pckg.getAllMappedSuperclassesInPackageTree());
		}
		mappedSuperclasses.addAll(this.getMappedSuperclasses());
		return mappedSuperclasses;
	}

	@Override
	public String toString() {
		return "Package [name=" + name + ", childrenPackages=\n\t" + childrenPackages
				+ ", entities=\n\t" + entities + ",valueObjects=\n\t" + valueObjects + 
				", mappedSupperclasses=\n\t" + mappedSuperclasses + ", classesWithLifeCycle=\n\t"
				+ classesWithLifeCycle + ", enumerations=\n\t" + enumerations + "]\n";
	}
	
	
}
