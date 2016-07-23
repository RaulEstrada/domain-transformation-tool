package input.bbdd;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import input.bbdd.utils.SQL2JavaDataConverter;
import model.structure.Basic;
import model.structure.Column;
import model.structure.Entity;

public class ColumnReader {
	private static final String COLUMN_NAME_KEY = "COLUMN_NAME";
	private static final String DATA_TYPE_KEY = "DATA_TYPE";
	private SQL2JavaDataConverter typeConverter = new SQL2JavaDataConverter();
	
	/**
	 * Queries the database for the columns of the table of a given entity and
	 * creates the intermediate model columns.
	 * @param dbMetadata - The database metadata.
	 * @param entity - The entity whose columns are to be created.
	 * @param tableKeys - Column names or keys not to be processed.
	 * @throws SQLException
	 */
	public void readColumns(DatabaseMetaData dbMetadata, Entity entity, 
			List<String> tableKeys) throws SQLException{
		ResultSet columnSet = null;
		try {
			columnSet = dbMetadata.getColumns(null, null, 
					entity.getTable().getName(), "%");
			while (columnSet.next()) {
				String columnName = columnSet.getString(COLUMN_NAME_KEY);
				if (!tableKeys.contains(columnName)) {
					Basic basic = new Basic();
					basic.setName(columnName);
					int sqlType = columnSet.getInt(DATA_TYPE_KEY);
					basic.setTypeName(typeConverter.getJavaType(sqlType)
							.getCanonicalName());
					basic.setTemporal(typeConverter.getTemporalType(sqlType));
					Column column = new Column();
					column.setName(columnName);
					basic.setColumn(column);
					entity.getAttributes().getBasic().add(basic);
				}
			}
		} catch (SQLException ex) {
			throw ex;
		} finally {
			if (columnSet != null && !columnSet.isClosed()) {columnSet.close();}
		}
	}
}
