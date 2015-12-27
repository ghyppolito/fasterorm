package br.com.ghfsoftware.faster.exception;

/**
 * Exception to indicate SQLite object not available to use
 * Created by Gustavo Ferreira on 18/10/15.
 */
public class SQLiteObjectRuntimeException extends FasterRuntimeException {

    /**
     * Constructor
     */
    public SQLiteObjectRuntimeException(){
        super("SQLite object not available to use");
    }
}
