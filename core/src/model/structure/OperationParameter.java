package model.structure;

public class OperationParameter {
	private String name;
	private String type;
	
	public OperationParameter(String name, String type) {
		if (name == null || name.isEmpty() || type == null || type.isEmpty()) {
			throw new IllegalArgumentException("The name and type of the method's parameter cannot be null or empty");
		}
		this.name = name;
		this.type = type;
	}
	
	public String getName() {
		return this.name;
	}
	
	public String getType() {
		return this.type;
	}
}
