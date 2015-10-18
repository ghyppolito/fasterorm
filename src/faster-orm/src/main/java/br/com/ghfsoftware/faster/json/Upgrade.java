package br.com.ghfsoftware.faster.json;

import java.util.List;

/**
 * Class to mapping upgrade script
 * @author Gustavo Ferreira
 * @version 1.0
 *
 */
public class Upgrade {
	
	private Integer version;
	private List<String> commands;
	
	/**
	 * Get version
	 * @return version
	 */
	public Integer getVersion() {
		return version;
	}
	
	/**
	 * Get commands
	 * @return commands
	 */
	public List<String> getCommands() {
		return commands;
	}
	
	

}
