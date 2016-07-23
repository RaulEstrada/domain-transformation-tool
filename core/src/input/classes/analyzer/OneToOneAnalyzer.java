package input.classes.analyzer;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Type;

import model.associations.OneToOne;
import model.customtypes.FetchType;

public class OneToOneAnalyzer {
	/**
	 * Processes the JPA annotation and creates the One to One association
	 * @param annotation - The JPA annotation
	 * @param elementName - The name of the annotated element
	 * @param element - The annotated element
	 * @param type - The target type
	 * @return - The new One to One association
	 */
	public OneToOne createOneToOne(javax.persistence.OneToOne annotation, 
			String elementName, AnnotatedElement element, Type type) {
		OneToOne oneToOne = new OneToOne();
		String target = type.getTypeName();
		if (!annotation.targetEntity().getName().equals("void")) {
			target = annotation.targetEntity().getName();
		}
		oneToOne.setTargetEntity(target);
		oneToOne.setCascade(new CascadeAnalyzer().createCascade(
				annotation.cascade()));
		oneToOne.setFetch(FetchType.fromValue(annotation.fetch().toString()));
		oneToOne.setMappedBy(annotation.mappedBy());
		oneToOne.setOptional(annotation.optional());
		oneToOne.setOrphanRemoval(annotation.orphanRemoval());
		oneToOne.setName(elementName);
		processAnnotations(element, oneToOne);
		return oneToOne;
	}
	
	private void processAnnotations(AnnotatedElement element, OneToOne oneToOne) {
		javax.persistence.PrimaryKeyJoinColumn pkJoinColumnAnnotation = 
				element.getAnnotation(javax.persistence.PrimaryKeyJoinColumn.class);
		if (pkJoinColumnAnnotation != null) {
			oneToOne.getPrimaryKeyJoinColumn().add(new PrimaryKeyJoinColumnAnalyzer()
					.createPrimaryKeyJoinColumn(pkJoinColumnAnnotation));
		}
		javax.persistence.PrimaryKeyJoinColumns pkJoinColumnAnnotations = 
				element.getAnnotation(javax.persistence.PrimaryKeyJoinColumns.class);
		if (pkJoinColumnAnnotations != null) {
			oneToOne.getPrimaryKeyJoinColumn().addAll(new PrimaryKeyJoinColumnsAnalyzer()
					.createPrimaryKeyJoinColumns(pkJoinColumnAnnotations));
		}
		javax.persistence.JoinColumn joinColumnAnnotation = 
				element.getAnnotation(javax.persistence.JoinColumn.class);
		if (joinColumnAnnotation != null) {
			oneToOne.getJoinColumn().add(new JoinColumnAnalyzer()
					.createJoinColumn(joinColumnAnnotation));
		}
		javax.persistence.JoinColumns joinColumnsAnnotation = 
				element.getAnnotation(javax.persistence.JoinColumns.class);
		if (joinColumnsAnnotation != null) {
			oneToOne.getJoinColumn().addAll(new JoinColumnsAnalyzer()
					.createJoinColumns(joinColumnsAnnotation));
		}
		javax.persistence.JoinTable joinTableAnnotation = 
				element.getAnnotation(javax.persistence.JoinTable.class);
		if (joinTableAnnotation != null) {
			oneToOne.setJoinTable(new JoinTableAnalyzer()
					.createJoinTable(joinTableAnnotation));
		}
	}
}
