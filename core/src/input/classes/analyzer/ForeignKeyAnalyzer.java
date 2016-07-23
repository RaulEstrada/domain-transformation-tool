package input.classes.analyzer;

import model.customtypes.ConstraintMode;
import model.structure.ForeignKey;

public class ForeignKeyAnalyzer {
	/**
	 * Analyzes the JPA annotation and creates the corresponding intermediate
	 * model element.
	 * @param annotation - JPA annotation
	 * @return - The corresponding intermediate model element.
	 */
	public ForeignKey createForeignKey(javax.persistence.ForeignKey annotation) {
		ForeignKey foreignKey = new ForeignKey();
		foreignKey.setName(annotation.name());
		foreignKey.setConstraintMode(ConstraintMode.fromValue(
				annotation.value().toString()));
		foreignKey.setForeignKeyDefinition(annotation.foreignKeyDefinition());
		return foreignKey;
	}
}
