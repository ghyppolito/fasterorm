package br.com.ghfsoftware.faster.mapper;

import br.com.ghfsoftware.faster.annotation.Table.Column;

public class ColumnMapper {

	private Column columnAnnotation;
	private String value;
	
	/**
	 * Get column annotation
	 * @return column annotation
	 */
	public Column getColumnAnnotation() {
		return columnAnnotation;
	}
	
	/**
	 * Set column annotation
	 * @param columnAnnotation
	 */
	public void setColumnAnnotation(Column columnAnnotation) {
		this.columnAnnotation = columnAnnotation;
	}
		
	/**
	 * Get value
	 * @return value
	 */
	public String getValue() {
		return value;
	}
	
	/**
	 * Set value
	 * 
	 * @param value
	 */
	public void setValue(String value) {
		this.value = value;
	}
	

}
