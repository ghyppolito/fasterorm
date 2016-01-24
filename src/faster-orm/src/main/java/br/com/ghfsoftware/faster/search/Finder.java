package br.com.ghfsoftware.faster.search;

import java.util.ArrayList;
import java.util.List;

import br.com.ghfsoftware.faster.annotation.Table;

/**
 * Class to create SQL criteria
 * @author gustavo
 * @version 1.0
 *
 */
public class Finder {
	
	private String table;
	private Selection<?> selection;
	private List<Join<?>> join;
	private List<Condition> where;
	private OrderBy order;
	private int limit = -1;
	
	/**
	 * Constructor
	 * @param clazz: table class
	 */
	public <T> Finder(Class<T> clazz){
		
		if (clazz.isAnnotationPresent(Table.class)){
			this.table = clazz.getAnnotation(Table.class).name();
		}
	}
	
	/**
	 * Add the selection fields
	 * @param selection: selection
	 * @return finder
	 */
	public <T> Finder setSelection(Selection<T> selection){
		this.selection = selection;
		return this;
	}
	
	/**
	 * Add join tables
	 * @param join: join tables
	 * @return join tables
	 */
	public Finder addJoin(Join<?> join){
		if (this.join==null){
			this.join = new ArrayList<Join<?>>();
		}
		this.join.add(join);
		return this;
	}
	
	/**
	 * Add conditions
	 * @param condition: condition
	 * @return conditions
	 */
	public Finder addCondition(Condition condition){
		if (this.where==null){
			this.where = new ArrayList<Condition>();
		}
		this.where.add(condition);
		return this;
	}
	
	/**
	 * Add order by
	 * @param order: order by
	 * @return order by
	 */
    public Finder addOrder(OrderBy order){
		this.order = order;
		return this;
	}
    
    /**
     * Selected limit
     * @param limit: limit
     * @return limit
     */
    public Finder setLimit(int limit){
    	this.limit = limit;
    	return this;
    }

    /**
     * Get table name
     * @return table name
     */
	public String getTable() {
		return table;
	}

	/**
	 * Get instance selection
	 * @return selection
	 */
	@SuppressWarnings("unchecked")
	public <T> T getSelection() {
		return (T) selection;
	}

	/**
	 * Get joins table
	 * @return joins table
	 */
	public List<Join<?>> getJoin() {
		return join;
	}

	/**
	 * Get where conditions
	 * @return conditions
	 */
	public List<Condition> getWhere() {
		return where;
	}

	/**
	 * Get order
	 * @return order
	 */
	public OrderBy getOrder() {
		return order;
	}

	/**
	 * Get limit
	 * @return limit
	 */
	public int getLimit() {
		return limit;
	}
    
    
    

}
