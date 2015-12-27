package br.com.ghfsoftware.faster.search;

import java.util.ArrayList;
import java.util.List;

import br.com.ghfsoftware.faster.enumerator.Order;

/**
 * Class to set order in search
 * @author gustavo
 * @version 1.0
 *
 */
public class OrderBy {

	private Order order;
	private List<String> columns;
	
	/**
	 * Constructor
	 * @param order: order enum
	 */
	public OrderBy(Order order){
		this.order = order;
	}
	
	/**
	 * Add column
	 * @param column
	 * @return OrderBy object
	 */
	public OrderBy addColumn(String column){
		if (this.columns==null){
			this.columns=new ArrayList<String>();
		}
		this.columns.add(column);
		return this;
	}
	
	/**
	 * Get order setting
	 * @return order
	 */
	public Order getOrder() {
		return order;
	}
	
	/**
	 * Get columns setting
	 * @return columns
	 */
	public List<String> getColumns() {
		return columns;
	}
	
	
}
