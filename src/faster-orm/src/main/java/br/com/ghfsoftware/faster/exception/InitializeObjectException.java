package br.com.ghfsoftware.faster.exception;

/**
 * Initialize object exception
 * 
 * @author gustavo
 * @version 1.0
 *
 */
public class InitializeObjectException extends FasterException {

	/**
	 * Serial version
	 */
	private static final long serialVersionUID = -6089770968761865405L;
	
	/**
	 * Constructor
	 * 
	 * @param cause
	 */
	public InitializeObjectException(Throwable cause){
		super("Initialization object error", cause);
	}

}
