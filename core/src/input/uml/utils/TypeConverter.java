package input.uml.utils;

import java.util.Date;

import org.eclipse.uml2.uml.Type;

/**
 * Takes the name of primitive data types and returns their classes.
 * @author Raul Estrada
 *
 */
public class TypeConverter {
	/**
	 * Takes the name of primitive data types and returns their classes.
	 * @param umlType - The UML data type
	 * @return - The java class of that primitive data type.
	 */
	@SuppressWarnings("rawtypes")
	public Class convertUmlType(Type umlType) {
		if (umlType == null) {
			throw new IllegalArgumentException("The UML type cannot be missing");
		}
		if (umlType.getName().equals(Boolean.class.getCanonicalName())) {
			return Boolean.class;
		} else if (umlType.getName().equals(Byte.class.getCanonicalName())) {
			return Byte.class;
		} else if (umlType.getName().equals(Date.class.getCanonicalName())) {
			return java.util.Date.class;
		} else if (umlType.getName().equals(Integer.class.getCanonicalName())) {
			return Integer.class;
		} else if (umlType.getName().equals(Long.class.getCanonicalName())) {
			return Long.class;
		} else if (umlType.getName().equals(String.class.getCanonicalName())) {
			return String.class;
		} else {
			throw new RuntimeException("UML type " + umlType.getName() + " not recognized");
		}

	}
}
