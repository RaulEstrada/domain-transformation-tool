package input.bbdd;

public enum DatabaseType {
	MYSQL("jdbc:mysql://", "mysql", "com.mysql.jdbc.Driver"), 
	ORACLE("jdbc:oracle:thin:@", "oracle", "oracle.jdbc.driver.OracleDriver"), 
	POSTGRESQL("jdbc:postgresql://", "postgre", "org.postgresql.Driver"), 
	HSQLDB("jdbc:hsqldb:hsql://", "hsql", "org.hsqldb.jdbc.JDBCDriver");
	
	private String label;
	private String propertyExtension;
	private String driverName;
	
	DatabaseType(String label, String propertyExtension, String driverName) {
		this.label = label;
		this.propertyExtension = propertyExtension;
		this.driverName = driverName;
	}
	
	public String getLabel() {
		return this.label;
	}
	
	public String getPropertyExtension() {
		return this.propertyExtension;
	}
	
	public String getDriverName() {
		return this.driverName;
	}
}
