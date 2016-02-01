package br.com.ghfsoftware.faster.enumerator;

/**
 * Order types enum
 * @author gustavo
 *
 */
public enum Order {

	ASC("ASC"), 
	DESC("DESC");
	
	private String value;
	
	/**
	 * Constructor
	 * @param value type ordering
	 */
	Order(String value){
		this.value=value;
	}
	
	/**
	 * Get value
	 * @return type ordering
	 */
	public String getValue(){
		return this.value;
	}
}
