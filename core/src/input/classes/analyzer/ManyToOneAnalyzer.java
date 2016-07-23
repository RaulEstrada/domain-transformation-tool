package input.classes.analyzer;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Type;

import model.associations.ManyToOne;
import model.customtypes.FetchType;

public class ManyToOneAnalyzer {
	/**
	 * Analyzes the @ManyToOne annotation and the element that contains it, and creates a
	 * JAXB annotated object representing it.
	 * @param element - The element containing the @ManyToOne annotation
	 * @param annotation - The @ManyToOne annotation
	 * @return - The JAXB annotated object
	 */
	public ManyToOne createManyToOne(AnnotatedElement element, String elementName,
			javax.persistence.ManyToOne annotation, Type type) {
		ManyToOne relation = new ManyToOne();
		relation.setName(elementName);
		String target = type.getTypeName();
		if (!annotation.targetEntity().getName().equals("void")) {
			target = annotation.targetEntity().getName();
		}
		relation.setTargetEntity(target);
		relation.setCascade(new CascadeAnalyzer().createCascade(annotation.cascade()));
		relation.setOptional(annotation.optional());
		relation.setFetch(FetchType.fromValue(annotation.fetch().toString()));
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
		return relation;
	}
}
