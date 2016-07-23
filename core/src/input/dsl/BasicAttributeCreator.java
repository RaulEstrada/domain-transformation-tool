package input.dsl;

import java.util.Map;

import org.eclipse.xtext.common.types.JvmTypeReference;

import model.structure.AnnotatedAttributes;
import model.structure.Basic;
import model.structure.Embeddable;
import model.structure.Embedded;
import model.structure.Enumeration;

public class BasicAttributeCreator {
	private Utils utils = new Utils();
	
	/**
	 * Creates a basic element in the intermediate model.
	 * @param typeRef - The DSL JvmType reference
	 * @param attributeName - The name of the element
	 * @param modelAttributes - The attributes that will contain the basic element
	 * @param embeddableMap - The map of embeddables
	 * @param enumMap - The map of entities.
	 */
	public void createBasicAttribute(JvmTypeReference typeRef, 
			String attributeName, AnnotatedAttributes modelAttributes, 
			Map<String, Embeddable> embeddableMap, Map<String, Enumeration> enumMap) {
		if (embeddableMap.containsKey(typeRef.getQualifiedName())) {
			Embeddable referencedEmbeddable = embeddableMap.get(
					typeRef.getQualifiedName());
			Embedded embedded = new Embedded();
			embedded.setEmbeddable(referencedEmbeddable);
			embedded.setName(attributeName);
			embedded.setTypeName(typeRef.getQualifiedName());
			modelAttributes.getEmbedded().add(embedded);
		} else {
			Basic basic = new Basic();
			basic.setName(attributeName);
			basic.setTemporal(utils.getTemporalType(typeRef));
			basic.setTypeName(typeRef.getQualifiedName());
			basic.setEnumerated(utils.getEnumType(typeRef, enumMap));
			modelAttributes.getBasic().add(basic);
		}
	}
}
