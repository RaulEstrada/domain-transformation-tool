package input.classes.analyzer;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Type;

import model.structure.Column;
import model.structure.Id;
import model.structure.TemporalType;

public class IdAnalyzer {
	/**
	 * Creates the intermediate model Id of a given annotated element.
	 * @param element - The annotated element whose Id is to be created.
	 * @param elementName - The name of the element
	 * @param type - The type of the Id
	 * @return - The intermediate model Id.
	 */
	public Id createId(AnnotatedElement element, String elementName, Type type) {
		Id id = new Id();
		id.setName(elementName);
		id.setTypeName(type.getTypeName());
		javax.persistence.Column columnAnnotation = element.getAnnotation(
				javax.persistence.Column.class);
		if (columnAnnotation != null) {
			Column column = new ColumnAnalyzer().createColumn(columnAnnotation);
			id.setColumn(column);
		}
		javax.persistence.GeneratedValue generatedValueAnnotation = 
				element.getAnnotation(javax.persistence.GeneratedValue.class);
		if (generatedValueAnnotation != null) {
			id.setGeneratedValue(new GeneratedValueAnalyzer()
					.createGeneratedValue(generatedValueAnnotation));
		}
		javax.persistence.TableGenerator tableGeneratorAnnotation = 
				element.getAnnotation(javax.persistence.TableGenerator.class);
		if (tableGeneratorAnnotation != null) {
			id.setTableGenerator(new TableGeneratorAnalyzer()
					.createTableGenerator(tableGeneratorAnnotation));
		}
		javax.persistence.Temporal temporalAnnotation = 
				element.getAnnotation(javax.persistence.Temporal.class);
		if (temporalAnnotation != null) {
			id.setTemporal(TemporalType.fromValue(temporalAnnotation.toString()));
		}
		javax.persistence.SequenceGenerator seqGeneratorAnnotation = 
				element.getAnnotation(javax.persistence.SequenceGenerator.class);
		if (seqGeneratorAnnotation != null) {
			id.setSequenceGenerator(new SequenceGeneratorAnalyzer()
					.createSeqGenerator(seqGeneratorAnnotation));
		}
		return id;
	}
}
