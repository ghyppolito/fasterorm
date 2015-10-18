package br.com.ghfsoftware.faster.mapper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Class to mapper SQL parameter structure
 * 
 * @author gustavo
 * @version 1.0
 *
 */
public class ParameterMapper {

	private int position;
	private String name;
	private Object value;
	
	/**
	 * Constructor
	 * 
	 * @param position
	 * @param name
	 * @param value
	 */
	public ParameterMapper(int position, String name, Object value){
		this.position = position;
		this.name = name;
		this.value = value;
	}
	
	/**
	 * Get parameter position
	 * @return position
	 */
	public int getPosition() {
		return position;
	}
	
	/**
	 * Set parameter position
	 * @param position
	 */
	public void setPosition(int position) {
		this.position = position;
	}
	
	/**
	 * Get parameter name
	 * @return parameter name
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Set parameter name
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * Get parameter value
	 * @return parameter value
	 */
	public Object getValue() {
		return value;
	}
	
	/**
	 * Set parameter value
	 * @param value
	 */
	public void setValue(Object value) {
		this.value = value;
	}
		
	/**
	 * Order and return list of values
	 * 
	 * @param params
	 * @return values
	 */
    public static String[] toValueArray(List<ParameterMapper> params){
		
        Collections.sort(params, new Comparator<ParameterMapper>() {
    		
    		public int compare(ParameterMapper o1, ParameterMapper o2) {
    			if (o1.position <= o2.position){
    				return 1;
    			}
    			return 0;
    		}
    	});
		
		List<String> values = null;
		
		if (params!=null && !params.isEmpty()){
			values = new ArrayList<String>();
			for (ParameterMapper param : params){
				values.add(param.value.toString());
			}
		}
		
		return values.toArray(new String[values.size()]);
	}
	
	
}
