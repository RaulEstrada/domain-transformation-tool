package output;

import input.bbdd.DatabaseType;
import output.bbdd.SQLWriter;
import output.java.JavaWriter;
import output.orm.ORMWriter;
import output.uml.UMLWriter;

public class WriterFactoryImpl implements WriterFactory {
	
	private static WriterFactoryImpl INSTANCE = null;
	
	private WriterFactoryImpl(){}
	
	public static WriterFactoryImpl getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new WriterFactoryImpl();
		}
		return INSTANCE;
	}
	
	@Override
	public Writer getORMWriter() {
		return new ORMWriter();
	}
	
	@Override
	public Writer getUMLWriter() {
		return new UMLWriter();
	}
	
	@Override
	public Writer getOracleSQLWriter() {
		return new SQLWriter(DatabaseType.ORACLE);
	}
	
	@Override
	public Writer getMySQLSQLWriter() {
		return new SQLWriter(DatabaseType.MYSQL);
	}
	
	@Override
	public Writer getPostgreSQLWriter() {
		return new SQLWriter(DatabaseType.POSTGRESQL);
	}
	
	@Override
	public Writer getHSQLDBSQLWriter() {
		return new SQLWriter(DatabaseType.HSQLDB);
	}
	
	@Override
	public Writer getJavaWriter() {
		return new JavaWriter();
	}
}
