package input.classes.analyzer;

import javax.lang.model.type.MirroredTypeException;

import model.customtypes.ParameterMode;
import model.queries.StoredProcedureParameter;

public class StoredProcedureParamAnalyzer {
	/**
	 * Analyzes the @StoredProcedureParameter annotation and creates a JAXB 
	 * annotated object representing it.
	 * @param annotation - The @StoredProcedureParameter annotation
	 * @return - The annotated JAXB object
	 */
	public StoredProcedureParameter createParameter(
			javax.persistence.StoredProcedureParameter annotation) {
		StoredProcedureParameter procedureParameter = new StoredProcedureParameter();
		String type = null;
		try {
			type = annotation.type().getName();
		} catch (MirroredTypeException ex) {
			type = ex.getTypeMirror().toString();
		}
		if (!type.equals("void")) {
			procedureParameter.setClazz(type);
		}
		procedureParameter.setName(annotation.name());
		procedureParameter.setMode(ParameterMode.fromValue(annotation.mode()
				.toString()));
		return procedureParameter;
	}
}
