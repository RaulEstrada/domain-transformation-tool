package tfg.plugin.config;

import org.apache.maven.plugins.annotations.Parameter;

public class DatabaseInput {
	@Parameter(required = true)
	private String host;
	@Parameter(required = true)
	private String port;
	@Parameter(required = true)
	private String name;
	@Parameter(required = true)
	private String username;
	@Parameter(required = true)
	private String password;
	@Parameter(required = true)
	private DatabaseInputType type;
	
	public String getHost() {
		return host;
	}
	
	public String getPort() {
		return port;
	}
	
	public String getName() {
		return name;
	}
	
	public String getUsername() {
		return username;
	}
	
	public String getPassword() {
		return password;
	}
	
	public DatabaseInputType getType() {
		return type;
	}
	
	public String getConfigurationMessage() {
		return "Database input configuration (host: " + getHost() + ". Port: " +
				getPort() + ". Database name: " + getName() + ". Type: " +
				getType() + ". Username: " + getUsername() + ". Password: " +
				getPassword() + ")";
	}
}
