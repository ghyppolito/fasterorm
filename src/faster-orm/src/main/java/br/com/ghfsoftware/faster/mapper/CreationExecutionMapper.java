package br.com.ghfsoftware.faster.mapper;

import java.util.ArrayList;
import java.util.List;

/**
 * Class to mapping the sql exection in creation
 * 
 * @author Gustavo Ferreira
 * @version 1.0
 *
 */
public class CreationExecutionMapper {
	
	private String tableName;
	private String sql;
	private boolean statusExecution;
	private List<CreationExecutionMapper> dependecies;
	
	/**
	 * Constructor
	 * @param tableName
	 * @param sql
	 */
	private CreationExecutionMapper (String tableName, String sql){
		this.tableName = tableName;
		this.sql = sql;
		this.statusExecution = false;
	}
	
	/**
	 * Factory to create
	 * @param tableName
	 * @param sql
	 * @return CreationExecutionMapper
	 */
	public static CreationExecutionMapper create(String tableName, String sql){
		return new CreationExecutionMapper(tableName, sql);
	}
	
	/**
	 * Get table name
	 * @return table name
	 */
	public String getTableName() {
		return tableName;
	}

	/**
	 * Get sql script
	 * @return sql script
	 */
	public String getSql() {
		return sql;
	}
	
	/**
	 * Get execution status
	 * @return execution status
	 */
	public boolean isStatusExecution() {
		return statusExecution;
	}
	
	/**
	 * Get dependencies list
	 * @return dependencies list
	 */
	public List<CreationExecutionMapper> getDependecies() {
		return dependecies;
	}
	
	/**
	 * Set as executed
	 */
	public void setExecuted(){
		this.statusExecution = true;
	}
	
	/**
	 * Add dependency
	 * @param dependency
	 */
	public void addDependency(CreationExecutionMapper dependency){
		
		if (this.dependecies == null){
			this.dependecies = new ArrayList<CreationExecutionMapper>();
		}
		
		if (dependency!= this){
			this.dependecies.add(dependency);
		}
	}
	
	

}
