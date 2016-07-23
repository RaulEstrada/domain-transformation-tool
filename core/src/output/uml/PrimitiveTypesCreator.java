package output.uml;

import java.util.Date;
import java.util.Map;

import org.eclipse.uml2.uml.PrimitiveType;
import org.eclipse.uml2.uml.Type;

class PrimitiveTypesCreator {
	/**
	 * Builder that handles the creation of UML elements (classes, associations,
	 *  attributes, etc)
	 */
	private UML2Builder umlBuilder = new UML2Builder();
	/**
	 * Maps the canonical name of simple types (primitive data types and 
	 * enumerations) to the UML type elements.
	 * Used mostly to create attributes.
	 */
	private Map<String, Type> types;
	
	/**
	 * Creates the default UML types representing the primitive java types and 
	 * Date type.
	 * @param umlRootPackage - The UML root package that will own these types.
	 */
	void create(org.eclipse.uml2.uml.Package umlRootPackage) {
		if (umlRootPackage == null) {
			throw new IllegalArgumentException("Cannot create primitive types "
					+ "if the owning UML package is missing");
		}
		PrimitiveType integ = umlBuilder.createPrimitiveType(umlRootPackage, 
				Integer.class.getCanonicalName());
		types.put(Integer.class.getCanonicalName().toLowerCase(), integ);
		PrimitiveType str = umlBuilder.createPrimitiveType(umlRootPackage, 
				String.class.getCanonicalName());
		types.put(String.class.getCanonicalName().toLowerCase(), str);
		PrimitiveType bool = umlBuilder.createPrimitiveType(umlRootPackage, 
				Boolean.class.getCanonicalName());
		types.put(Boolean.class.getCanonicalName().toLowerCase(), bool);
		PrimitiveType lng = umlBuilder.createPrimitiveType(umlRootPackage, 
				Long.class.getCanonicalName());
		types.put(Long.class.getCanonicalName().toLowerCase(), lng);
		PrimitiveType dte = umlBuilder.createPrimitiveType(umlRootPackage, 
				Date.class.getCanonicalName());
		types.put(Date.class.getCanonicalName().toLowerCase(), dte);
		PrimitiveType bte = umlBuilder.createPrimitiveType(umlRootPackage, 
				Byte.class.getCanonicalName());
		types.put(Byte.class.getCanonicalName().toLowerCase(), bte);
		PrimitiveType dble = umlBuilder.createPrimitiveType(umlRootPackage, 
				Double.class.getCanonicalName());
		types.put(Double.class.getCanonicalName().toLowerCase(), dble);
		PrimitiveType flat = umlBuilder.createPrimitiveType(umlRootPackage, 
				Float.class.getCanonicalName());
		types.put(Float.class.getCanonicalName().toLowerCase(), flat);
	}
	
	/**
	 * Assigns the primitive/enumeration types map to the current primitive types creator.
	 * @param types - The types map.
	 * @return - The current primitive types creator.
	 */
	PrimitiveTypesCreator withTypes(Map<String, Type> types) {
		if (types == null) {
			throw new IllegalArgumentException("The types map cannot be missing");
		}
		this.types = types;
		return this;
	}
}
