package tooleclipseplugin;

import model.structure.Package;

public class Core {
	private static Package intermediateModel;
	
	public void setIntermediateModel(Package model) {
		intermediateModel = model;
	}
	
	public Package getIntermediateModel() {
		return intermediateModel;
	}
	
	public boolean getIntermediateModelLoaded() {
		return intermediateModel != null;
	}
}
