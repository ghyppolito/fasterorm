package br.com.ghfsoftware.faster.search;

import br.com.ghfsoftware.faster.enumerator.JoinType;

/**
 * Class to join tables
 * @author gustavo
 * @version 1.0
 *
 * @param <T>
 * @param <U>
 */
public class Join<T, U> {
	
	private JoinType joinType;
	private Class<T> table;
	private Class<U> target;
	
	/**
	 * Constructor
	 * 
	 * @param joinType join type
	 * @param table table class
	 * @param target class
	 */
	public Join(JoinType joinType, Class<T> table, Class<U> target){
		this.joinType = joinType;
		this.table = table;
		this.target = target;
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

	/**
	 * Get target class
	 * @return target class
	 */
	public Class<U> getTarget(){
		return this.target;
	}

}
