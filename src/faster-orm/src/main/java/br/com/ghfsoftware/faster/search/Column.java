package br.com.ghfsoftware.faster.search;

/**
 * Class to mapping the columns that it will be return
 * when the query is executed
 * 
 * @author Gustavo Ferreira
 * @version 1.0
 *
 */
public class Column {
	
	private String name;
	private String alias;
	private String tableName;
	
	/**
	 * Constructor
	 * @param name
	 * @param alias
	 * @param tableName
	 */
	private Column(String name, String alias, String tableName){
		this.name = name;
		this.alias = alias;
		this.tableName = tableName;
	}
	
	/**
	 * Constructor
	 * @param name
	 * @param alias
	 */
	private Column (String name, String alias){
		this(name, alias, null);
	}
	
	/**
	 * Constructor
	 * @param name
	 */
	private Column (String name){
		this(name, null, null);
	}
	
	/**
	 * Factory of column
	 * @param name
	 * @param alias
	 * @param tableName
	 * @return column
	 */
	public static Column create(String name, String alias, String tableName){
		return new Column(name, alias, tableName);
	}
	
	/**
	 * Factory of column
	 * @param name
	 * @param alias
	 * @return column
	 */
	public static Column create(String name, String alias){
		return new Column(name, alias);
	}

	/**
	 * Factory of column
	 * @param name
	 * @return column
	 */
	public static Column create(String name){
		return new Column(name);
	}
	
	/**
	 * Get column name
	 * @return column name
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Get alias
	 * @return alias
	 */
	public String getAlias() {
		return alias;
	}
	
	/**
	 * Get table name
	 * @return table name
	 */
	public String getTableName() {
		return tableName;
	}
	

}
