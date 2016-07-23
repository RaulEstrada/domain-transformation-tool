package input.classes.analyzer;

import model.generators.SequenceGenerator;

public class SequenceGeneratorAnalyzer {
	/**
	 * Analyzes the @SequenceGenerator annotation and creates a JAXB annotated object representing
	 * it
	 * @param annotation - The @SequenceGenerator annotation
	 * @return - The JAXB annotated object
	 */
	public SequenceGenerator createSeqGenerator(
			javax.persistence.SequenceGenerator annotation) {
		SequenceGenerator generator = new SequenceGenerator();
		generator.setName(annotation.name());
		generator.setSequenceName(annotation.sequenceName());
		generator.setAllocationSize(annotation.allocationSize());
		generator.setCatalog(annotation.catalog());
		generator.setInitialValue(annotation.initialValue());
		generator.setSchema(annotation.schema());
		return generator;
	}
}
