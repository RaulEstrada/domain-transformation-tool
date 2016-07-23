package input.bbdd;

import java.util.List;

public class ManyToManyInfoWrapper {
	private String table1;
	private List<String> columns1;
	private String table2;
	private List<String> columns2;
	
	/**
	 * Initializes the element that will contain information about a many to 
	 * many association between two tables.
	 * @param table1 - The first table in the association
	 * @param columns1 - The foreign keys column in the first table.
	 * @param table2 - The second table in the association.
	 * @param columns2 - The foreign keys column in the second table.
	 */
	public ManyToManyInfoWrapper(String table1, List<String> columns1, 
			String table2, List<String> columns2) {
		this.table1 = table1;
		this.columns1 = columns1;
		this.table2 = table2;
		this.columns2 = columns2;
	}

	public String getTable1() {
		return table1;
	}

	public List<String>  getColumns1() {
		return columns1;
	}

	public String getTable2() {
		return table2;
	}

	public List<String>  getColumns2() {
		return columns2;
	}
	
	
}
