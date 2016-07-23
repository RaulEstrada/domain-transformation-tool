package input.classes.analyzer;

import model.structure.Index;

public class IndexAnalyzer {
	/**
	 * Analyzes the @Index annotation and creates a JAXB annotated object representing it.
	 * @param annotation - The @Index annotation
	 * @return - The JAXB annotated object
	 */
	public Index createIndex(javax.persistence.Index annotation) {
		Index index = new Index();
		index.setColumnList(annotation.columnList());
		index.setName(annotation.name());
		index.setUnique(annotation.unique());
		return index;
	}
}
