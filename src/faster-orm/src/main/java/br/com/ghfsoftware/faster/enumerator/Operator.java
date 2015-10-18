package br.com.ghfsoftware.faster.enumerator;

/**
 * Operators enum
 * @author gustavo
 * @version 1.0
 *
 */
public enum Operator {
	
	EQUAL("="), 
	NOT_EQUAL("<>"), 
	GREATER(">"), 
	GREATER_OR_EQUAL(">="), 
	LESS("<"), 
	LESS_OR_EQUAL("<="); 

	private String value;
	
	/**
	 * Constructor
	 * @param value
	 */
	Operator(String value){
		this.value = value;
	}
	
	/**
	 * Get value
	 * @return value
	 */
	public String getValue(){
		return this.value;
	}
}
