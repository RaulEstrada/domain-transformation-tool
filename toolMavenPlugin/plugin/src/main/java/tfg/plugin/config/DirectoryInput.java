package tfg.plugin.config;

import java.io.File;

import org.apache.maven.plugins.annotations.Parameter;

public class DirectoryInput {
	@Parameter(required = true)
	private File from;
	@Parameter(required = true)
	private DirectoryInputType type;
	
	public File getFrom() {
		return from;
	}
	
	public DirectoryInputType getType() {
		return type;
	}
}
