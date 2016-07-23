package input.classes.analyzer;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import model.associations.ManyToMany;
import model.customtypes.EnumType;
import model.customtypes.FetchType;
import model.structure.TemporalType;

public class ManyToManyAnalyzer {
	/**
	 * Analyzes the @ManyToMany annotation and the element containing it, and creates a JAXB 
	 * annotated object representing it.
	 * @param element - The element containing the @ManyToMany annotation
	 * @param annotation - The @ManyToMany annotation
	 * @return - The JAXB annotated object
	 */
	public ManyToMany createManyToMany(AnnotatedElement element, 
			String elementName, javax.persistence.ManyToMany annotation, Type type) {
		ManyToMany relation = new ManyToMany();
		relation.setName(elementName);
		String target = ((ParameterizedType)type).getActualTypeArguments()[0]
				.getTypeName();
		if (!annotation.targetEntity().getName().equals("void")) {
			target = annotation.targetEntity().getName();
		}
		relation.setTargetEntity(target);
		relation.setCascade(new CascadeAnalyzer().createCascade(annotation.cascade()));
		relation.setFetch(FetchType.fromValue(annotation.fetch().toString()));
		relation.setMappedBy(annotation.mappedBy());
		processJoinMapColumns(element, relation);
		processConvert(element, relation);
		processOverride(element, relation);
		processMapKey(element, relation);
		processOrder(element, relation);
		return relation;
	}
	
	private void processJoinMapColumns(AnnotatedElement element, ManyToMany relation) {
		javax.persistence.JoinTable joinTableAnnotation = 
				element.getAnnotation(javax.persistence.JoinTable.class);
		if (joinTableAnnotation != null) {
			relation.setJoinTable(new JoinTableAnalyzer().createJoinTable(joinTableAnnotation));
		}
		javax.persistence.MapKeyJoinColumn mapKeyJoinColumnAnnotation = 
				element.getAnnotation(javax.persistence.MapKeyJoinColumn.class);
		if (mapKeyJoinColumnAnnotation != null) {
			relation.getMapKeyJoinColumn().add(new MapKeyJoinColumnAnalyzer()
					.createMapKeyJoinColumn(mapKeyJoinColumnAnnotation));
		}
		javax.persistence.MapKeyJoinColumns mapKeyJoinColumnsAnnotation = 
				element.getAnnotation(javax.persistence.MapKeyJoinColumns.class);
		if (mapKeyJoinColumnsAnnotation != null) {
			relation.getMapKeyJoinColumn().addAll(new MapKeyJoinColumnsAnalyzer()
					.createMapKeyJoinColumns(mapKeyJoinColumnsAnnotation));
		}
		javax.persistence.MapKeyColumn mapKeyColumnAnnotation = 
				element.getAnnotation(javax.persistence.MapKeyColumn.class);
		if (mapKeyColumnAnnotation != null) {
			relation.setMapKeyColumn(new MapKeyColumnAnalyzer()
					.createMapKeyColumn(mapKeyColumnAnnotation));
		}
	}
	
	private void processConvert(AnnotatedElement element, ManyToMany relation) {
		javax.persistence.Convert convertAnnotation = 
				element.getAnnotation(javax.persistence.Convert.class);
		if (convertAnnotation != null) {
			relation.getMapKeyConvert().add(new ConvertAnalyzer()
					.createConvert(convertAnnotation));
		}
		javax.persistence.Converts convertsAnnotation = 
				element.getAnnotation(javax.persistence.Converts.class);
		if (convertsAnnotation != null) {
			relation.getMapKeyConvert().addAll(new ConvertsAnalyzer()
					.createConverts(convertsAnnotation));
		}
	}
	
	private void processOverride(AnnotatedElement element, ManyToMany relation) {
		javax.persistence.AttributeOverrides attributeOverridesAnnotation = 
				element.getAnnotation(javax.persistence.AttributeOverrides.class);
		if (attributeOverridesAnnotation != null) {
			relation.getMapKeyAttributeOverride().addAll(new AttributeOverridesAnalyzer()
					.createAttributeOverrides(attributeOverridesAnnotation));
		}
		javax.persistence.AttributeOverride attributeOverrideAnnotation = 
				element.getAnnotation(javax.persistence.AttributeOverride.class);
		if (attributeOverrideAnnotation != null) {
			relation.getMapKeyAttributeOverride().add(new AttributeOverrideAnalyzer()
					.createAttributeOverride(attributeOverrideAnnotation));
		}
	}
	
	private void processMapKey(AnnotatedElement element, ManyToMany relation) {
		javax.persistence.MapKeyEnumerated mapKeyEnumeratedAnnotation = 
				element.getAnnotation(javax.persistence.MapKeyEnumerated.class);
		if (mapKeyEnumeratedAnnotation != null) {
			relation.setMapKeyEnumerated(EnumType.fromValue(mapKeyEnumeratedAnnotation
					.value().toString()));
		}
		javax.persistence.MapKeyTemporal mapKeyTemporalAnnotation = 
				element.getAnnotation(javax.persistence.MapKeyTemporal.class);
		if (mapKeyTemporalAnnotation != null) {
			relation.setMapKeyTemporal(TemporalType.fromValue(
					mapKeyTemporalAnnotation.value().toString()));
		}
		javax.persistence.MapKeyClass mapKeyClassAnnotation = 
				element.getAnnotation(javax.persistence.MapKeyClass.class);
		if (mapKeyClassAnnotation != null) {
			relation.setMapKeyClass(new MapKeyClassAnalyzer()
					.createMapKeyClass(mapKeyClassAnnotation));
		}
		javax.persistence.MapKey mapKeyAnnotation = 
				element.getAnnotation(javax.persistence.MapKey.class);
		if (mapKeyAnnotation != null) {
			relation.setMapKey(new MapKeyAnalyzer().createMapKey(mapKeyAnnotation));
		}
	}
	
	private void processOrder(AnnotatedElement element, ManyToMany relation) {
		javax.persistence.OrderColumn orderColumnAnnotation = 
				element.getAnnotation(javax.persistence.OrderColumn.class);
		if (orderColumnAnnotation != null) {
			relation.setOrderColumn(new OrderColumnAnalyzer()
					.createOrderColumn(orderColumnAnnotation));
		}
		javax.persistence.OrderBy orderByAnnotation = 
				element.getAnnotation(javax.persistence.OrderBy.class);
		if (orderByAnnotation != null) {
			relation.setOrderBy(orderByAnnotation.value());
		}
	}
}
