package input.classes.analyzer;

import javax.persistence.JoinColumn;

import model.associations.AssociationOverride;

public class AssociationOverrideAnalyzer {
	/**
	 * Analyzes the JPA annotation and creates the corresponding intermediate
	 * model element.
	 * @param annotation - JPA annotation
	 * @return - The corresponding intermediate model element.
	 */
	public AssociationOverride createAssociationOverride(
			javax.persistence.AssociationOverride annotation) {
		AssociationOverride associationOverride = new AssociationOverride();
		associationOverride.setName(annotation.name());
		javax.persistence.JoinTable joinTableAnnotation = annotation.joinTable();
		if (joinTableAnnotation != null) {
			associationOverride.setJoinTable(new JoinTableAnalyzer()
					.createJoinTable(joinTableAnnotation));
		}
		javax.persistence.JoinColumn[] joinColumnsAnnotation = annotation.joinColumns();
		for (JoinColumn joinColumnAnnotation : joinColumnsAnnotation) {
			associationOverride.getJoinColumn().add(new JoinColumnAnalyzer()
					.createJoinColumn(joinColumnAnnotation));
		}
		javax.persistence.ForeignKey fKeyAnnotation = annotation.foreignKey();
		if (fKeyAnnotation != null) {
			associationOverride.setForeignKey(new ForeignKeyAnalyzer()
					.createForeignKey(fKeyAnnotation));
		}
		return associationOverride;
	}
}
