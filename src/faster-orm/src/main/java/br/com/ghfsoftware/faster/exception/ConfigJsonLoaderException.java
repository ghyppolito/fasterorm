package br.com.ghfsoftware.faster.exception;

/**
 * Exception when it is impossible to
 * load the xml configuration file
 * 
 * @author gustavo
 * @version 1.0
 *
 */
public class ConfigJsonLoaderException extends FasterRuntimeException {

	/**
	 * Serial version
	 */
	private static final long serialVersionUID = -1599295152916429110L;
	
	/**
	 * Constructor
	 * @param e
	 */
	public ConfigJsonLoaderException(Exception e){
		super("It is impossible to load the json configuration file", e);
	}

}
