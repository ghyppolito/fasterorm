package br.com.ghfsoftware.faster.exception;

/**
 * Type not supported
 * @author gustavo
 * @version 1.0
 *
 */
public class TypeNotSupportedException extends FasterException {
	
	/**
	 * Serial version
	 */
	private static final long serialVersionUID = -6173999911333994475L;

	/**
	 * Constructor
	 */
	public TypeNotSupportedException(){
		super("Class Type not supported");
	}

}
