package cmdtool.presentation;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.xml.bind.JAXBException;

import com.sun.codemodel.JClassAlreadyExistsException;

import cmdtool.presentation.processors.HelpCommandProcessor;
import cmdtool.presentation.processors.InputCommandProcessor;
import cmdtool.presentation.processors.OutputCommandProcessor;
import cmdtool.presentation.utils.PropertyReader;
import model.structure.Package;

public class Main {
	private static PropertyReader properties;

	public static void main(String[] args) throws IOException, ClassNotFoundException, 
		JAXBException, JClassAlreadyExistsException {
		properties = new PropertyReader();
		System.out.println(properties.getProperty("initialMessage"));
		start(new BufferedReader(new InputStreamReader(System.in)));
	}
	
	private static void start(BufferedReader reader) throws IOException, 
		ClassNotFoundException, JAXBException, JClassAlreadyExistsException {
		List<String> fullInput = getUserInput(reader);
		String command = (fullInput.isEmpty())? "" : fullInput.get(0); 
		Package intermediateRootPackage = null;
		while (!command.equals(UserInputValues.EXIT)) {
			switch(command) {
			case UserInputValues.INPUT: 
				intermediateRootPackage = InputCommandProcessor.getInstance()
					.process(fullInput);
				break;
			case UserInputValues.OUTPUT: 
				OutputCommandProcessor.getInstance()
					.process(intermediateRootPackage, fullInput);
				break;
			case UserInputValues.HELP:
				HelpCommandProcessor.getInstance().process();
				break;
			default: System.out.println(properties
					.getProperty("commandNotSupported", "'" + command + "'"));
			}
			fullInput = getUserInput(reader);
			command = (fullInput.isEmpty())? "" : fullInput.get(0); 
		}
	}

	private static List<String> getUserInput(BufferedReader reader) 
			throws IOException {
		System.out.print("> ");
		String[] input = reader.readLine().trim().split("\"");
		List<String> fullInput = new ArrayList<>();
		for (int i = 0; i < input.length; i++) {
			if (i%2!=0) {
				fullInput.add(input[i]);
			} else {
				fullInput.addAll(Arrays.asList(input[i].split(" ")));
			}
		}
		return fullInput;
	}
}
