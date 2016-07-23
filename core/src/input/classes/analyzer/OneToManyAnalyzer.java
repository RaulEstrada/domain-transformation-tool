package input.classes.analyzer;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import model.associations.OneToMany;
import model.customtypes.EnumType;
import model.customtypes.FetchType;
import model.structure.TemporalType;

public class OneToManyAnalyzer {
	/**
	 * Analizes the @OneToMany annotation and creates a JAXB annotated object 
	 * representing it.
	 * @param annotation - The @OneToMany annotation
	 * @return - The JAXB annotated object
	 */
	public OneToMany createOneToMany(javax.persistence.OneToMany annotation, 
			String elementName, AnnotatedElement element, Type type) {
		OneToMany relation = new OneToMany();
		relation.setName(elementName);
		String target = ((ParameterizedType)type).getActualTypeArguments()[0]
				.getTypeName();
		if (!annotation.targetEntity().getName().equals("void")) {
			target = annotation.targetEntity().getName();
		}
		relation.setTargetEntity(target);
		relation.setFetch(FetchType.fromValue(annotation.fetch().toString()));
		relation.setMappedBy(annotation.mappedBy());
		relation.setOrphanRemoval(annotation.orphanRemoval());
		relation.setCascade(new CascadeAnalyzer().createCascade(annotation.cascade()));
		processOrder(element, relation);
		processMapKeys(element, relation);
		processOverride(element, relation);
		processConvert(element, relation);
		processJoin(element, relation);
		return relation;
	}
	
	private void processOrder(AnnotatedElement element, OneToMany relation) {
		javax.persistence.OrderBy orderByAnnotation = 
				element.getAnnotation(javax.persistence.OrderBy.class);
		if (orderByAnnotation != null) {
			relation.setOrderBy(orderByAnnotation.value());
		}
		javax.persistence.OrderColumn orderColumnAnnotation = 
				element.getAnnotation(javax.persistence.OrderColumn.class);
		if (orderColumnAnnotation != null) {
			relation.setOrderColumn(new OrderColumnAnalyzer()
					.createOrderColumn(orderColumnAnnotation));
		}
	}
	
	private void processMapKeys(AnnotatedElement element, OneToMany relation) {
		javax.persistence.MapKey mapKeyAnnotation = 
				element.getAnnotation(javax.persistence.MapKey.class);
		if (mapKeyAnnotation != null) {
			relation.setMapKey(new MapKeyAnalyzer().createMapKey(mapKeyAnnotation));
		}
		javax.persistence.MapKeyClass mapKeyClassAnnotation = 
				element.getAnnotation(javax.persistence.MapKeyClass.class);
		if (mapKeyClassAnnotation != null) {
			relation.setMapKeyClass(new MapKeyClassAnalyzer()
					.createMapKeyClass(mapKeyClassAnnotation));
		}
		javax.persistence.MapKeyTemporal mapKeyTemporalAnnotation = 
				element.getAnnotation(javax.persistence.MapKeyTemporal.class);
		if (mapKeyTemporalAnnotation != null) {
			relation.setMapKeyTemporal(TemporalType.fromValue(
					mapKeyTemporalAnnotation.value().toString()));
		}
		javax.persistence.MapKeyEnumerated mapKeyEnumeratedAnnotation = 
				element.getAnnotation(javax.persistence.MapKeyEnumerated.class);
		if (mapKeyEnumeratedAnnotation != null) {
			relation.setMapKeyEnumerated(EnumType.fromValue(
					mapKeyEnumeratedAnnotation.value().toString()));
		}
		javax.persistence.MapKeyColumn mapKeyColumnAnnotation = 
				element.getAnnotation(javax.persistence.MapKeyColumn.class);
		if (mapKeyColumnAnnotation != null) {
			relation.setMapKeyColumn(new MapKeyColumnAnalyzer()
					.createMapKeyColumn(mapKeyColumnAnnotation));
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
	}
	
	private void processOverride(AnnotatedElement element, OneToMany relation) {
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
	
	private void processConvert(AnnotatedElement element, OneToMany relation) {
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
	
	private void processJoin(AnnotatedElement element, OneToMany relation) {
		javax.persistence.JoinColumn joinColumnAnnotation = 
				element.getAnnotation(javax.persistence.JoinColumn.class);
		if (joinColumnAnnotation != null) {
			relation.getJoinColumn().add(new JoinColumnAnalyzer()
					.createJoinColumn(joinColumnAnnotation));
		}
		javax.persistence.JoinColumns joinColumnsAnnotation = 
				element.getAnnotation(javax.persistence.JoinColumns.class);
		if (joinColumnsAnnotation != null) {
			relation.getJoinColumn().addAll(new JoinColumnsAnalyzer()
					.createJoinColumns(joinColumnsAnnotation));
		}
		javax.persistence.JoinTable joinTableAnnotation = 
				element.getAnnotation(javax.persistence.JoinTable.class);
		if (joinTableAnnotation != null) {
			relation.setJoinTable(new JoinTableAnalyzer()
					.createJoinTable(joinTableAnnotation));
		}
	}
}
