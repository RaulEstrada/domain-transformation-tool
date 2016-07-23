package input.dsl;

import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import org.eclipse.xtext.common.types.JvmTypeReference;

import model.customtypes.EnumType;
import model.structure.Enumeration;
import model.structure.TemporalType;

public class Utils {
	/**
	 * Returns the intermediate model temporal type from a DSL reference type.
	 * @param typeRef - The DSL reference type.
	 * @return - The intermediate model temporal type.
	 */
	public TemporalType getTemporalType(JvmTypeReference typeRef) {
		String qualifiedName = typeRef.getType().getQualifiedName();
		return (qualifiedName.equals(Date.class.getCanonicalName()) || 
				qualifiedName.equals(Calendar.class.getCanonicalName()))
				? TemporalType.DATE : null;
	}
	
	/**
	 * Returns the intermediate model enum type from a DSL reference type.
	 * @param typeRef - The DSL reference type.
	 * @param enumMap - The map of enumerations
	 * @return - The intermediate model enum type.
	 */
	public EnumType getEnumType(JvmTypeReference typeRef, Map<String, 
			Enumeration> enumMap) {
		return (enumMap.containsKey(typeRef.getQualifiedName())) 
				? EnumType.STRING : null;
	}
}
