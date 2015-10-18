package br.com.ghfsoftware.faster.exception;

/**
 * Runtime exception to Faster ORM API
 * @author gustavo
 * @version 1.0
 *
 */
public class FasterRuntimeException extends RuntimeException {

	/**
	 * Serial version
	 */
	private static final long serialVersionUID = -7255200162678731550L;

	/**
	 * Constructor
	 */
	public FasterRuntimeException(){
		super();
	}
	
	/**
	 * Constructor
	 * @param message
	 */
	public FasterRuntimeException(String message){
		super(message);
	}
	
	/**
	 * Constructor
	 * @param cause
	 */
	public FasterRuntimeException(Throwable cause){
		super(cause);
	}
	
	/**
	 * Constructor
	 * @param message
	 * @param cause
	 */
	public FasterRuntimeException(String message, Throwable cause){
		super(message, cause);
	}

}
