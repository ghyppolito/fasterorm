package br.com.ghfsoftware.faster.search;

import br.com.ghfsoftware.faster.enumerator.Operator;

/**
 * Class to create conditions in search
 * @author gustavo
 * @version
 *
 */
public class Condition {
	
	private String column;
	private Operator operator;
	private Object value;
	
	/**
	 * Constructor
	 * 
	 * @param column
	 * @param operator
	 * @param value
	 */
	public Condition(String column, Operator operator, Object value){
		this.column = column;
		this.operator = operator;
		this.value = value;
	}
	
	/**
	 * Get column name
	 * @return column name
	 */
	public String getColumn() {
		return column;
	}
	
	/**
	 * Get operator
	 * @return operator
	 */
	public Operator getOperator() {
		return operator;
	}
	
	/**
	 * Get value
	 * @return value
	 */
	public Object getValue() {
		return value;
	}
	
	

}
