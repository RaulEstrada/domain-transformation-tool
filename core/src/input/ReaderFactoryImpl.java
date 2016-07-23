package input;

import input.bbdd.DatabaseReader;
import input.bbdd.DatabaseType;
import input.classes.ClassReader;
import input.dsl.DSLReader;
import input.java.JavaReader;
import input.uml.UMLReader;

public class ReaderFactoryImpl implements ReaderFactory {
	private static ReaderFactoryImpl INSTANCE = null;
	
	private ReaderFactoryImpl(){}
	
	public static ReaderFactoryImpl getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new ReaderFactoryImpl();
		}
		return INSTANCE;
	}
	
	@Override
	public Reader getClassReader() {
		return new ClassReader();
	}
	
	@Override
	public Reader getJavaReader() {
		return new JavaReader();
	}
	
	@Override
	public Reader getUMLReader() {
		return new UMLReader();
	}
	
	@Override
	public Reader getOracleDatabaseReader() {
		return new DatabaseReader(DatabaseType.ORACLE);
	}
	
	@Override
	public Reader getMySQLDatabaseReader() {
		return new DatabaseReader(DatabaseType.MYSQL);
	}
	
	@Override
	public Reader getPostGreSQLDatabaseReader() {
		return new DatabaseReader(DatabaseType.POSTGRESQL);
	}
	
	@Override
	public Reader getHSQLDatabaseReader() {
		return new DatabaseReader(DatabaseType.HSQLDB);
	}
	
	@Override
	public Reader getDSLReader() {
		return new DSLReader();
	}
}
