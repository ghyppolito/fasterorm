package br.com.ghfsoftware.faster.search;

import java.util.ArrayList;
import java.util.List;

/**
 * Class to create the selection return of query
 * @author Gustavo Ferreira
 * @version 1.0
 *
 * @param <T>
 */
public class Selection<T> {
	
	private List<Column> columns;
	private Class<T> classMapping;

	/**
	 * Constructor
	 * @param clazz: class
	 */
	public Selection(Class<T> clazz){
		this.classMapping = clazz;
	}
	
	/**
	 * Factory for selection
	 * @param clazz: class
	 * @return selection
	 */
	public static <T> Selection<T> create(Class<T> clazz){
		return new Selection<T>(clazz);
	}
	
	/**
	 * Add new column in selection
	 * @param columnName: column name
	 */
	public Selection<T> addColumn(String columnName){
		if (columns==null){
			columns = new ArrayList<Column>();
		}
		columns.add(Column.create(columnName));
		return this;
	}
	
	/**
	 * Add new column in selection
	 * @param columnName: column name
	 */
	public Selection<T> addColumn(String columnName, String alias){
		if (columns==null){
			columns = new ArrayList<Column>();
		}
		columns.add(Column.create(columnName, alias));
		return this;
	}
	
	/**
	 * Add new column in selection
	 * @param columnName: column name
	 */
	public Selection<T> addColumn(String columnName, String alias, String table){
		if (columns==null){
			columns = new ArrayList<Column>();
		}
		columns.add(Column.create(columnName, alias, table));
		return this;
	}
	
	/**
	 * Get columns of selection
	 * @return columns
	 */
	public List<Column> getColumns() {
		return columns;
	}

	/**
	 * Get the return class
	 * @return return class
	 */
	public Class<T> getClassMapping() {
		return classMapping;
	}
	
	
	
}
