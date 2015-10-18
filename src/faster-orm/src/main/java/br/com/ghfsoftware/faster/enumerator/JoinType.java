package br.com.ghfsoftware.faster.enumerator;

/**
 * Types of join tables enum
 * @author gustavo
 * @version 1.0
 *
 */
public enum JoinType {

	INNER("INNER"),
	LEFT("LEFT"),
	RIGHT("RIGHT");
	
	private String value;
	
	/**
	 * Constructor
	 * @param value
	 */
	JoinType(String value){
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
