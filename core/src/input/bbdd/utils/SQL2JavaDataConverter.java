package input.bbdd.utils;

import java.sql.Types;
import java.util.Calendar;
import java.util.Date;

import model.structure.TemporalType;

public class SQL2JavaDataConverter {
	/**
	 * Gets the Java type a given SQL type maps to.
	 * @param sqlType - The SQL type whose Java type mapping is to be returned
	 * @return - The Java type mapping of the SQL type.
	 */
	@SuppressWarnings("rawtypes")
	public Class getJavaType(int sqlType) {
		switch(sqlType) {
		case Types.DECIMAL: return Double.class;
		case Types.BIGINT: return Long.class;
		case Types.VARCHAR:
		case Types.LONGNVARCHAR:
		case Types.CHAR: return String.class;
		case Types.INTEGER: return Integer.class;
		case Types.TIME:
		case Types.TIME_WITH_TIMEZONE:
		case Types.TIMESTAMP:
		case Types.TIMESTAMP_WITH_TIMEZONE: return Calendar.class;
		case Types.DATE: return Date.class;
		default: throw new RuntimeException("SQL type code " + sqlType + 
				" not recognized");
		}
	}

	/**
	 * Gets the intermediate model temporal type a given SQL type maps to.
	 * @param sqlType - The SQL type whose intermediate model temporal type
	 * is to be returned
	 * @return - The intermediate model temporal type of the SQL type.
	 */
	public TemporalType getTemporalType(int sqlType) {
		switch(sqlType) {
		case Types.TIME:
		case Types.TIME_WITH_TIMEZONE: return TemporalType.TIME;
		case Types.TIMESTAMP:
		case Types.TIMESTAMP_WITH_TIMEZONE: return TemporalType.TIMESTAMP;
		case Types.DATE: return TemporalType.DATE;
		default: return null;
		}
	}
}
