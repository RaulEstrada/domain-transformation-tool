package cmdtool.presentation.processors;

import java.io.IOException;

import cmdtool.presentation.utils.PropertyReader;

public class HelpCommandProcessor {
	private static HelpCommandProcessor INSTANCE = null;
	private PropertyReader properties;
	
	private HelpCommandProcessor() throws IOException {
		this.properties = new PropertyReader();
	}
	
	public static HelpCommandProcessor getInstance() throws IOException {
		if (INSTANCE == null) {
			INSTANCE = new HelpCommandProcessor();
		}
		return INSTANCE;
	}
	
	public void process() {
		System.out.println(properties.getProperty("helpMessage"));
		System.out.println("\n\n");
		System.out.println(properties.getProperty("readCommandStructure"));
		System.out.println("\n\n");
		System.out.println(properties.getProperty("writeCommandStructure"));
	}
}
