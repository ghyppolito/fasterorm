package br.com.ghfsoftware.faster;

import java.util.List;
import java.util.Map;
import java.util.Set;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import br.com.ghfsoftware.faster.annotation.FasterConfig;
import br.com.ghfsoftware.faster.exception.AnnotationRuntimeException;
import br.com.ghfsoftware.faster.exception.ConfigJsonLoaderException;
import br.com.ghfsoftware.faster.json.Script;
import br.com.ghfsoftware.faster.util.ClassMapper;
import br.com.ghfsoftware.faster.util.JsonConfigUtil;

/**
 * This class is responsible for create and update
 * the database application 
 * 
 * @author Gustavo Ferreira
 * @version 1.0
 *
 */
public abstract class FasterSQLiteHelper extends SQLiteOpenHelper {

	private Context context;

	/**
	 * Constructor 
	 * 
	 * @param context: context
	 * @param name: name
	 * @param factory: cursor factory
	 * @param version: version
	 */
	public FasterSQLiteHelper(Context context, String name,
			CursorFactory factory, int version) {
		super(context, name, factory, version);

		this.context = context;

		this.initialize();
	}
	
	/**
	 * Constructor 
	 * 
	 * @param context: context
	 * @param name: name
	 * @param factory: cursor factory
	 * @param version: version
	 * @param errorHandler: error handler
	 */
	public FasterSQLiteHelper(Context context, String name,
			CursorFactory factory, int version,
			DatabaseErrorHandler errorHandler) {
		super(context, name, factory, version, errorHandler);

		this.context = context;

		this.initialize();
	}
	
	/**
	 * Initialize class
	 */
	private void initialize(){
		
		if (getClass().isAnnotationPresent(FasterConfig.class)){
			FasterConfig fasterAnnotation = getClass().getAnnotation(FasterConfig.class);
			FasterConfigInfo.initialize(fasterAnnotation.showSql());
		}else{
			throw new AnnotationRuntimeException();
		}
	}

	@Override
	public void onCreate(SQLiteDatabase arg0) {
		
		Class<?> clazz = getClass();
		if (clazz.isAnnotationPresent(FasterConfig.class)){
			FasterConfig config = clazz.getAnnotation(FasterConfig.class);
			
			FasterManager fasterManager = new FasterManager(this, arg0);
			String[] classes = ClassMapper.list(config.packageName(), context);
			
			fasterManager.createDatabase(classes);
			
			Script script = verifyConfiguration();
			
			if (script!=null && script.getCreation()!=null){
				List<String> commands = JsonConfigUtil.getLoadCommands(script.getCreation());
				fasterManager.executeCommands(commands);
			}
			
		}else{
			throw new AnnotationRuntimeException();
		}

	}

	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
		
		Script script = verifyConfiguration();
	
		if (script!=null && script.getUpgrade()!=null){
			
			FasterManager fasterManager = new FasterManager(this, arg0);

			for (int version = arg1;version<arg2;version++){
				Map<Integer, List<String>> commands = JsonConfigUtil.getLoadCommands(script.getUpgrade());
				fasterManager.executeCommands(commands.get(version));
			}
		}
	
	}
	
	/**
	 * Verify if there's configuration and
	 * load and return the object with
	 * the configuration 
	 * 
	 * @return script loaded
	 */
	private Script verifyConfiguration(){
		
		Class<?> ownClass = this.getClass(); 
		Script script = null;
		
		if (ownClass.isAnnotationPresent(FasterConfig.class)){
		
			FasterConfig configAnnotation = ownClass.getAnnotation(FasterConfig.class);
			String scriptFile = configAnnotation.scriptFile();
			
			if (!"".equals(scriptFile)){
				try {
					script = JsonConfigUtil.getConfigJsonObject(context.getAssets().open(scriptFile));
				}catch(Exception e){
					throw new ConfigJsonLoaderException(e);
				}
			}
			
		}
		
		return script;
	}
	

}
