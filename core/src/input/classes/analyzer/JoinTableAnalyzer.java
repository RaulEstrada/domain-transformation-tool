package input.classes.analyzer;

import model.associations.JoinTable;

public class JoinTableAnalyzer {
	/**
	 * Analyzes the @JoinTable annotation and creates a JAXB annotated object 
	 * that represents the annotation and its values.
	 * @param annotation - The @JoinTable annotation
	 * @return - The JAXB annotated object
	 */
	public JoinTable createJoinTable(javax.persistence.JoinTable annotation) {
		JoinTable joinTable = new JoinTable();
		joinTable.setCatalog(annotation.catalog());
		joinTable.setName(annotation.name());
		joinTable.setSchema(annotation.schema());
		javax.persistence.JoinColumn[] joinColumnAnnotations = annotation.joinColumns();
		for (javax.persistence.JoinColumn jca : joinColumnAnnotations) {
			joinTable.getJoinColumn().add(new JoinColumnAnalyzer()
					.createJoinColumn(jca));
		}
		javax.persistence.JoinColumn[] inverseJoinColumnAnnotations = 
				annotation.inverseJoinColumns();
		for (javax.persistence.JoinColumn jca : inverseJoinColumnAnnotations) {
			joinTable.getInverseJoinColumn().add(new JoinColumnAnalyzer()
					.createJoinColumn(jca));
		}
		javax.persistence.UniqueConstraint[] uniqueConstraintAnnotations = 
				annotation.uniqueConstraints();
		for (javax.persistence.UniqueConstraint uniqueConstraintAnnotation : 
			uniqueConstraintAnnotations) {
			joinTable.getUniqueConstraint().add(new UniqueConstraintAnalyzer()
					.createUniqueConstraint(uniqueConstraintAnnotation));
		}
		javax.persistence.Index[] indexAnnotations = annotation.indexes();
		for (javax.persistence.Index indexAnnotation : indexAnnotations) {
			joinTable.getIndex().add(new IndexAnalyzer().createIndex(indexAnnotation));
		}
		javax.persistence.ForeignKey fkAnnotation = annotation.foreignKey();
		if (fkAnnotation != null) {
			joinTable.setForeignKey(new ForeignKeyAnalyzer()
					.createForeignKey(fkAnnotation));
		}
		javax.persistence.ForeignKey inverseFKAnnotation = 
				annotation.inverseForeignKey();
		if (inverseFKAnnotation != null) {
			joinTable.setInverseForeignKey(new ForeignKeyAnalyzer()
					.createForeignKey(inverseFKAnnotation));
		}
		return joinTable;
	}
}
