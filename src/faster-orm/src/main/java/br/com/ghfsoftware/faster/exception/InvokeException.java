package br.com.ghfsoftware.faster.exception;

/**
 * Method invoke exception
 *  
 * @author gustavo
 * @version 1.0
 *
 */
public class InvokeException extends FasterRuntimeException {

	/**
	 * Serial version
	 */
	private static final long serialVersionUID = -8984176222842582095L;
	
	/**
	 * Constructor
	 */
	public InvokeException(Throwable cause){
		super("Exception when it made invocation", cause);
	}

}
