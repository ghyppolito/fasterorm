package br.com.ghfsoftware.faster.json;

import java.util.List;

/**
 * Class to mapping database scripts
 * @author Gustavo Ferreira
 *
 */
public class Script {

	private Creation creation;
	private List<Upgrade> upgrade;
	
	/**
	 * Get creation scripts
	 * @return creation scripts
	 */
	public Creation getCreation() {
		return creation;
	}
	
	/**
	 * Get upgrade scripts
	 * @return upgrade scripts
	 */
	public List<Upgrade> getUpgrade() {
		return upgrade;
	}
	
	
}
