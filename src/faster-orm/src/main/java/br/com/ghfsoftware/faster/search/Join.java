package br.com.ghfsoftware.faster.search;

import br.com.ghfsoftware.faster.enumerator.JoinType;

/**
 * Class to join tables
 * @author gustavo
 * @version 1.0
 *
 * @param <T>
 */
public class Join<T> {
	
	private JoinType joinType;
	private Class<T> table;
	
	/**
	 * Constructor
	 * 
	 * @param joinType
	 * @param table
	 */
	public Join(JoinType joinType, Class<T> table){
		this.joinType = joinType;
		this.table = table;
	}
	
	/**
	 * Get join table
	 * @return table name
	 */
	public JoinType getJoinType() {
		return joinType;
	}

	/**
	 * Get table name
	 * @return table name
	 */
	public Class<T> getTable() {
		return table;
	}
	
	

}
