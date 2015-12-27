package br.com.ghfsoftware.faster.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;

import br.com.ghfsoftware.faster.exception.ConfigJsonLoaderException;
import br.com.ghfsoftware.faster.json.Creation;
import br.com.ghfsoftware.faster.json.Script;
import br.com.ghfsoftware.faster.json.Upgrade;

/**
 * Class that make operations with
 * the xml configuration file
 * 
 * @author Gustavo Ferreira
 * @version 1.0
 *
 */
public class JsonConfigUtil {

	/**
	 * Load the xml configuration file in an object
	 * @param file: InputStream from file
	 * @return xml configuration object
	 */
	public static Script getConfigJsonObject(final InputStream file){
		
		try{

			final BufferedReader reader = new BufferedReader(new InputStreamReader(file));

			Gson gson = new Gson();
			Script script;
			script = gson.fromJson(reader, Script.class);
			
			return script;
			
		} catch (Exception e) {
			throw new ConfigJsonLoaderException(e);
		} 
				
	}
	
	/**
	 * Load the creation commands
	 * @param creation: creation object
	 * @return creation commands list
	 */
	public static List<String> getLoadCommands (final Creation creation){
		
		if (creation!=null){
			
			if (creation.getCommands()!=null && !creation.getCommands().isEmpty()){
				
				return creation.getCommands();
			}
			
		}
		
		return null;
	}
	
	/**
	 * Load the upgrade SQL commands
	 * 
	 * @param upgrade: upgrade object
	 * @return map with the upgrade commands
	 */
	public static Map<Integer,List<String>> getLoadCommands (final List<Upgrade> upgrade){
		
		Map<Integer, List<String>> commands = null;
		
		if (upgrade!=null && !upgrade.isEmpty()){
			
			for (Upgrade patch : upgrade){
				
				if (patch.getVersion()!=null && patch.getCommands()!=null && !patch.getCommands().isEmpty()){
					
					if (commands==null){
						commands = new HashMap<Integer,List<String>>();
					}
					
					commands.put(patch.getVersion().intValue(), new ArrayList<String>(patch.getCommands()));
					
				}
			}
						
		}
		
		return commands;
	}
	
}
