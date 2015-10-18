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
	 * @param clazz
	 */
	private Selection(Class<T> clazz){
		this.classMapping = clazz;
	}
	
	/**
	 * Factory for selection
	 * @param clazz
	 * @return selection
	 */
	public static <T> Selection<T> create(Class<T> clazz){
		return new Selection<T>(clazz);
	}
	
	/**
	 * Add new column in selection
	 * @param column
	 */
	public void addColumn(String columnName){
		if (columns==null){
			columns = new ArrayList<Column>();
		}
		columns.add(Column.create(columnName));
	}
	
	/**
	 * Add new column in selection
	 * @param column
	 */
	public void addColumn(String columnName, String alias){
		if (columns==null){
			columns = new ArrayList<Column>();
		}
		columns.add(Column.create(columnName, alias));
	}
	
	/**
	 * Add new column in selection
	 * @param column
	 */
	public void addColumn(String columnName, String alias, String table){
		if (columns==null){
			columns = new ArrayList<Column>();
		}
		columns.add(Column.create(columnName, alias, table));
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
