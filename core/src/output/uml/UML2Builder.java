package output.uml;

import org.eclipse.uml2.uml.Enumeration;
import org.eclipse.uml2.uml.EnumerationLiteral;
import org.eclipse.uml2.uml.Generalization;
import org.eclipse.uml2.uml.Operation;
import org.eclipse.uml2.uml.UMLFactory;
import org.eclipse.uml2.uml.UMLPackage;
import org.eclipse.uml2.uml.resources.util.UMLResourcesUtil;
import org.eclipse.uml2.uml.Package;
import org.eclipse.uml2.uml.PrimitiveType;
import org.eclipse.uml2.uml.Profile;
import org.eclipse.uml2.uml.Property;
import org.eclipse.uml2.uml.Type;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.uml2.uml.AggregationKind;
import org.eclipse.uml2.uml.Association;
import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.Classifier;

/**
 * Class that creates different UML elements
 * @author Raul Estrada
 *
 */
public class UML2Builder {

	/**
	 * Creates a UML root package with a specified name.
	 * @param name - The name of the UML root package
	 * @return - The newly created UML root package.
	 */
	Package createPackage(String name) {
		if (name == null || name.isEmpty()) {
			throw new IllegalArgumentException("Cannot create a UML root package"
					+ " with no name, or an empty name");
		}
		Package pck = UMLFactory.eINSTANCE.createPackage();
		pck.setName(name);
		return pck;
	}

	/**
	 * Creates a package with a specified name nested inside another package
	 * @param nestingPackage - The parent package. That is, the package that 
	 * will contain the new package.
	 * @param name - The name of the new package.
	 * @return - The newly created package
	 */
	Package createPackage(Package nestingPackage, String name) {
		if (name == null || name.isEmpty()) {
			throw new IllegalArgumentException("Cannot create a UML package "
					+ "with no name, or an empty name");
		}
		if (nestingPackage == null) {
			throw new IllegalArgumentException("Cannot create a UML nested "
					+ "package if the parent package is missing");
		}
		return nestingPackage.createNestedPackage(name);
	}

	/**
	 * Creates a UML primitive type inside a specified package with a given name.
	 * @param pckg - The package containing the primitive type.
	 * @param name - The name of the new primitive type
	 * @return - The newly created primitive type.
	 */
	PrimitiveType createPrimitiveType(Package pckg, String name) {
		if (name == null || name.isEmpty()) {
			throw new IllegalArgumentException("Cannot create a UML primitive "
					+ "type with no name, or an empty name");
		}
		if (pckg == null) {
			throw new IllegalArgumentException("Cannot create a UML primitive "
					+ "type if the parent package is missing");
		}
		return pckg.createOwnedPrimitiveType(name);
	}

	/**
	 * Creates a UML enumeration inside a specified package with a given name.
	 * @param pckg - The package containing the enumeration.
	 * @param name - The name of the new enumeration.
	 * @return - The newly created UML enumeration.
	 */
	Enumeration createEnumeration(Package pckg, String name) {
		if (name == null || name.isEmpty()) {
			throw new IllegalArgumentException("Cannot create a UML enumeration"
					+ " with no name, or an empty name");
		}
		if (pckg == null) {
			throw new IllegalArgumentException("Cannot create a UML enumeration"
					+ " if the parent package is missing");
		}
		return pckg.createOwnedEnumeration(name);
	}

	/**
	 * Creates a UML enumeration literal belonging to a specified enumeration.
	 * @param enumeration - The enumeration containing the new enumeration literal.
	 * @param name - The name (value) of the new enumeration literal.
	 * @return - The newly created enumeration literal.
	 */
	EnumerationLiteral createEnumerationLiteral(Enumeration enumeration, String name) {
		if (name == null || name.isEmpty()) {
			throw new IllegalArgumentException("Cannot create a UML enumeration"
					+ " literal with no name, or an empty name");
		}
		if (enumeration == null) {
			throw new IllegalArgumentException("Cannot create a UML enumeration"
					+ " literal if the enumeration is missing");
		}
		return enumeration.createOwnedLiteral(name);
	}

	/**
	 * Creates a UML class inside a specified package.
	 * @param pckg - The package containing the new UML class.
	 * @param name - The name of the new UML class.
	 * @param isAbstract - If the class is abstract or not.
	 * @return - The newly created UML class.
	 */
	Class createClass(Package pckg, String name, boolean isAbstract) {
		if (name == null || name.isEmpty()) {
			throw new IllegalArgumentException("Cannot create a UML class with"
					+ " no name, or an empty name");
		}
		if (pckg == null) {
			throw new IllegalArgumentException("Cannot create a UML class if "
					+ "the parent package is missing");
		}
		return pckg.createOwnedClass(name, isAbstract);
	}

	/**
	 * Creates an inheritance relation between two classes.
	 * @param child - The child class
	 * @param parent - The parent or base class.
	 * @return - The newly created inheritance relation between the two UML classes.
	 */
	Generalization createGeneralization(Classifier child, Classifier parent) {
		if (child == null) {
			throw new IllegalArgumentException("Cannot create an inheritance "
					+ "relationship if the child class is missing");
		}
		if (parent == null) {
			throw new IllegalArgumentException("Cannot create an inheritance "
					+ "relationship if the parent/base class is missing");
		}
		return child.createGeneralization(parent);
	}

	/**
	 * Creates a UML attribute belonging to a UML type (class in this case)
	 * @param umlClass - The class that the attribute belongs to.
	 * @param name - The name of the new attribute
	 * @param type - The type of the new attribute
	 * @param lowerBound - The lower bound (0 if the attribute can have no value, 
	 * 1 if the attribute must always have a value)
	 * @param upperBound - The upper bound (1 if the attribute has only one value,
	 *  unlimited if the attribute has multiple values)
	 * @return - The newly created UML attribute
	 */
	Property createAttribute(Class umlClass, String name, Type type, int lowerBound,
			int upperBound) {
		if (umlClass == null) {
			throw new IllegalArgumentException("Cannot create an attribute if "
					+ "the UML class is missing");
		}
		if (name == null || name.isEmpty()) {
			throw new IllegalArgumentException("Cannot create an attribute with "
					+ "a missing or empty name");
		}
		if (type == null) {
			throw new IllegalArgumentException("Cannot create an attribute if "
					+ "its type is missing");
		}
		return umlClass.createOwnedAttribute(name, type, lowerBound, upperBound);
	}

	/**
	 * Creates an association or relationship between two UML classes.
	 * @param endAType - The UML class owning the association (the association 
	 * starts in this class)
	 * @param navigableEndA - True if we can go from the owner class (type1) 
	 * to the other class (type2)
	 * @param aggrEndA - The aggregation type in the other part of the 
	 * relationship.
	 * @param nameEndA - The name the association takes when going from the 
	 * owner part to the other part (type2)
	 * @param lowerA - The lower bound of the attribute in the owner 
	 * class (type1)
	 * @param upperA - The upper bound of the attribute in the owner 
	 * class (type2)
	 * @param endBType - The UML class in the other end of the relationship.
	 * @param navigableEndB - True if we can go from type2 to the owner of
	 *  the association (type1)
	 * @param aggrEndB - The aggregation type when going from type2 to 
	 * type1.
	 * @param nameB - The name the association takes when going from type2 
	 * to the owner of the relationship (type1)
	 * @param lowerB - The lower bound of the attribute in the type2 class.
	 * @param upperB - The upper bound of the attribute in the type2 class.
	 * @return - The newly created UML association between two UML classes.
	 */
	Association createAssociation(Type endAType, boolean navigableEndA, AggregationKind 
			aggrEndA, String nameEndA, int lowerA, int upperA,
			Type endBType, boolean navigableEndB, AggregationKind aggrEndB,
			String nameB, int lowerB, int upperB) {
		if (endAType == null || aggrEndA == null) {
			throw new IllegalArgumentException("The UML owner class (type1) and"
					+ " its end aggregation cannot be missing");
		}
		if (endBType == null || aggrEndB == null) {
			throw new IllegalArgumentException("The not-owner UML class (type2) "
					+ "and its end aggregation cannot be missing");
		}
		return endAType.createAssociation(navigableEndA, aggrEndA, nameEndA,
				lowerA, upperA, endBType, navigableEndB, 
				aggrEndB, nameB, lowerB, upperB);
	}

	/**
	 * Creates a new operation in a UML class.
	 * @param clazz - The UML class containing the operation
	 * @param name - The name of the operation
	 * @param parameterNames - The names of the parameters
	 * @param parameterTypes - The UML types of the parameters
	 * @param returnType - The return UML type of the operation
	 * @return - The newly created operation.
	 */
	Operation createOperation(Class clazz, String name, EList<String> parameterNames, 
			EList<Type> parameterTypes, Type returnType) {
		if (clazz == null) {
			throw new IllegalArgumentException("Cannot create a new operation "
					+ "if the UML class is missing");
		}
		if (name == null || name.isEmpty()) {
			throw new IllegalArgumentException("Cannot create a new operation "
					+ "if its name is missing or empty");
		}
		if (parameterNames == null || parameterTypes == null || returnType == null) {
			throw new IllegalArgumentException("Cannot create a new operation "
					+ "if the names or types of its arguments are missing,"
					+ " or if its return type is missing");
		}
		return clazz.createOwnedOperation(name, parameterNames, parameterTypes, returnType);
	}

	/**
	 * Loads the UML profile created previously and stored in the current 
	 * project folder.
	 * @param uri - The URI of the .profile.uml file containing the UML profile 
	 * and stereotypes
	 * @return - The loaded UML profile with the stereotypes
	 */
	public Profile load(URI uri) {
		if (uri == null) {
			
		}
		ResourceSet resourceSet = new ResourceSetImpl();
		UMLResourcesUtil.init(resourceSet);
		Resource resource = resourceSet.getResource(uri, true);
		return (Profile) EcoreUtil.getObjectByType(resource.getContents(), 
				UMLPackage.Literals.PROFILE);
	}
}
