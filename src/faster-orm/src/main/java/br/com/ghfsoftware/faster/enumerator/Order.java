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
	 * @param value
	 */
	Order(String value){
		this.value=value;
	}
	
	/**
	 * Get value
	 * @return value
	 */
	public String getValue(){
		return this.value;
	}
}
