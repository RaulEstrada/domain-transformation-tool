package model.structure;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class Operation {
	private String name;
	private String returnType;
	private Set<OperationParameter> attributes = new HashSet<>();
	
	public Operation(String name, String returnType) {
		if (name == null || name.isEmpty()) {
			throw new IllegalArgumentException("Operation name cannot be missing");
		}
		this.name = name;
		this.returnType = returnType;
	}
	
	public void addOperationParameter(OperationParameter parameter){
		this.attributes.add(parameter);
	}
	
	public String getName() {
		return this.name;
	}
	
	public String getReturnType() {
		return this.returnType;
	}
	
	public Set<OperationParameter> getParameters() {
		return Collections.unmodifiableSet(this.attributes);
	}
}
