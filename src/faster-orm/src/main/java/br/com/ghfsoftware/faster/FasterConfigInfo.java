package br.com.ghfsoftware.faster;

/**
 * Class to give informations about the configurations
 * @author Gustavo Ferreira
 * @version 1.0
 *
 */
public class FasterConfigInfo {
	
	private boolean showSql;
	private static FasterConfigInfo INSTANCE;
	
	/**
	 * Constructor
	 * @param debug
	 */
	private FasterConfigInfo(final boolean showSql){
		this.showSql=showSql;
	}
	
	/**
	 * Get instance
	 * @return instance
	 */
	public static FasterConfigInfo getInstance(){
		return INSTANCE;
	}
	
	/**
	 * Get status of debug mode
	 * @return debug mode
	 */
	public boolean isShowSql(){
		return this.showSql;
	}
	
	/**
	 * Initialize configure info
	 * @param debug
	 */
	public static void initialize(final boolean debug){
		if (INSTANCE==null){
			INSTANCE = new FasterConfigInfo(debug);
		}
	}

}
