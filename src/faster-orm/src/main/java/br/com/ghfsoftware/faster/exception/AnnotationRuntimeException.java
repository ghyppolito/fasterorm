package br.com.ghfsoftware.faster.exception;

/**
 * Runtime exception throwing when FasterConfig annotation
 * don't exist
 * 
 * @author gustavo
 * @version 1.0
 *
 */
public class AnnotationRuntimeException extends
		FasterRuntimeException {

	/**
	 * Serial version
	 */
	private static final long serialVersionUID = 8113294826307611120L;

	/**
	 * Constructor
	 */
	public AnnotationRuntimeException(){
		super("FasterConfig annotation dont exist in Helper");
	}
}
