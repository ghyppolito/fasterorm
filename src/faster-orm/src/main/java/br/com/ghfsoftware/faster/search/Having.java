package br.com.ghfsoftware.faster.search;

import br.com.ghfsoftware.faster.enumerator.Operator;

/**
 * Class to create having conditions in search
 * @author gustavo
 * @version 1.0
 *
 */
public class Having {

	/**
	 * Enum to obtains the having function selected
	 */
	public enum HavingEnum {
		MAX("MAX"),
		MIN("MIN"),
		COUNT("COUNT"),
		AVG("AVG"),
		SUM("SUM"),
		TOTAL("TOTAL");

		private String function;

		/**
		 * Enum constructor
		 * @param function function selected
		 */
		HavingEnum(String function){
			this.function = function;
		}

		/**
		 * Get function selected
		 * @return function selected
		 */
		public String getFunction(){
			return this.function;
		}

	}

	private String column;
	private Operator operator;
	private HavingEnum having;

	/**
	 * Constructor
	 *
	 * @param column column that it's working
	 * @param operator having operator
	 * @param having enum with the function selected
	 */
	public Having(String column, Operator operator, HavingEnum having){
		this.column = column;
		this.operator = operator;
		this.having = having;
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
	 * Get having function selected enum
	 * @return having function enum
	 */
	public HavingEnum getHaving() {
		return having;
	}

}
