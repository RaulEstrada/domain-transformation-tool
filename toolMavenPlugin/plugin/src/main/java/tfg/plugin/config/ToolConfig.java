package tfg.plugin.config;

import java.io.File;
import java.util.Set;

import org.apache.maven.plugins.annotations.Parameter;

public class ToolConfig {
	@Parameter(required = false)
	private DirectoryInput directoryInput;
	@Parameter(required = false)
	private DatabaseInput databaseInput;
	@Parameter(required = true)
	private File toDirectory;
	@Parameter(required = true)
	private Set<TransformationOutputOption> conversions;
	
	public DirectoryInput getDirectoryInput() {
		return directoryInput;
	}

	public DatabaseInput getDatabaseInput() {
		return databaseInput;
	}

	public File getToDirectory() {
		return toDirectory;
	}
	
	public Set<TransformationOutputOption> getConversions() {
		return conversions;
	}
	
}
