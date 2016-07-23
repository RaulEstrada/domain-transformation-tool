package model.structure;

import java.util.Arrays;

public class Enumeration {
	private String name;
	private Object[] constants;
	
	public Enumeration(String name, Object[] constants) {
		this.name = name;
		this.constants = constants;
	}

	public String getName() {
		return name;
	}

	public Object[] getConstants() {
		return constants;
	}

	@Override
	public String toString() {
		return "Enumeration [name=" + name + ", constants=" + Arrays.toString(constants) + "]";
	}
	
	
}
