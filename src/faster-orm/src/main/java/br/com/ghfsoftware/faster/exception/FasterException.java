package br.com.ghfsoftware.faster.exception;

/**
 * Exception base of FasterORM
 * 
 * @author gustavo
 * @version 1.0
 *
 */
public class FasterException extends Exception{

	/**
	 * serial version
	 */
	private static final long serialVersionUID = -3457277390089891257L;
	
	/**
	 * Constructor
	 */
	public FasterException(){
		super();
	}
	
	/**
	 * Constructor
	 * @param message
	 */
	public FasterException(String message){
		super(message);
	}
	
	/**
	 * Constructor
	 * @param cause
	 */
	public FasterException(Throwable cause){
		super(cause);
	}
	
	/**
	 * Constructor
	 * @param message
	 * @param cause
	 */
	public FasterException(String message, Throwable cause){
		super(message, cause);
	}


}
