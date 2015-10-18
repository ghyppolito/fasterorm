package br.com.ghfsoftware.faster;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import br.com.ghfsoftware.faster.annotation.Join;
import br.com.ghfsoftware.faster.annotation.SqlScript;
import br.com.ghfsoftware.faster.annotation.SqlScript.Parameter;
import br.com.ghfsoftware.faster.annotation.Table;
import br.com.ghfsoftware.faster.annotation.Table.Column;
import br.com.ghfsoftware.faster.annotation.Table.Id;
import br.com.ghfsoftware.faster.exception.AnnotationRuntimeException;
import br.com.ghfsoftware.faster.exception.FasterException;
import br.com.ghfsoftware.faster.exception.InvokeException;
import br.com.ghfsoftware.faster.exception.SQLiteObjectRuntimeException;
import br.com.ghfsoftware.faster.mapper.CreationExecutionMapper;
import br.com.ghfsoftware.faster.mapper.ColumnMapper;
import br.com.ghfsoftware.faster.mapper.ParameterMapper;
import br.com.ghfsoftware.faster.search.Condition;
import br.com.ghfsoftware.faster.search.Finder;
import br.com.ghfsoftware.faster.search.OrderBy;
import br.com.ghfsoftware.faster.search.Selection;

/**
 * Class responsible to manager the
 * database operations
 * @author gustavo
 * @version 1.0
 *
 */
public class FasterManager {
	
	private static final String EQUAL = "=";
	private static final String PARAM = "?";
	private static final String PREFIX = "#{";
	private static final String SUFFIX = "}";
	private static final String AND = "AND";
	private static final String POINT = ".";
	private static final String COMMA = ",";
		
	private FasterSQLiteHelper fasterHelper;
	private SQLiteDatabase sqlite;
	
	/**
	 * Constructor
	 * 
	 * @param fasterHelper: fasterHelper object
	 */
	public FasterManager(FasterSQLiteHelper fasterHelper){
		this.fasterHelper = fasterHelper;
	}
	
	/**
	 * Constructor
	 * 
	 * @param fasterHelper: fasterHelper object
	 * @param sqlite: sqlite object
	 */
	public FasterManager(FasterSQLiteHelper fasterHelper, SQLiteDatabase sqlite){
		this.fasterHelper = fasterHelper;
		this.sqlite = sqlite;
	}
	
	/**
	 * Open writable SQLiteDatabase
	 * @return SQLiteDatabase
	 */
	public SQLiteDatabase openWritable(){
		
		if (this.sqlite!=null){
			sqlite.close();
		}
		
		this.sqlite = fasterHelper.getWritableDatabase();
		return this.sqlite;
	}
	
	/**
	 * Open readable SQLiteDatabase
	 * @return SQLiteDatabase object
	 */
	public SQLiteDatabase openReadable(){
		
		if (this.sqlite!=null){
			sqlite.close();
		}
		
		this.sqlite = fasterHelper.getReadableDatabase();
		return this.sqlite;
	}
	
	/**
	 * Close SQLiteDatabase object
	 */
	public void close(){
		
		if (sqlite!=null){
			this.sqlite.close();
			this.sqlite = null;
		}
	}
	
	/**
	 * Start transaction
	 */
	public void beginTransaction(){
		if (sqlite==null){
			throw new SQLiteObjectRuntimeException();
		}else{
			if (!sqlite.inTransaction()){
				sqlite.beginTransaction();
			}
		}
	}
	
	/**
	 * End transaction
	 */
	public void endTransaction(){
		if (sqlite==null){
			throw new SQLiteObjectRuntimeException();
		}else{
			if (sqlite.inTransaction()){
				sqlite.endTransaction();
			}
		}
	}
	
	/**
	 * Set successful in operation
	 */
	public void commit(){
        if (sqlite==null){
			throw new SQLiteObjectRuntimeException();
		}else{
			if (sqlite.inTransaction()){
				sqlite.setTransactionSuccessful();
			}
		}
	}
	
	/**
	 * Indicate if the base is open
	 * @return base state
	 */
	public boolean isOpen(){
		if (this.sqlite==null){
			return false;
		}
		
		return this.sqlite.isOpen();
	}
	
	/**
	 * Insert table data
	 * 
	 * @param table: table object
	 * @return long
	 */
	@SuppressWarnings("unchecked")
	public <T> Long insert(T table) throws FasterException{
		
		long valueReturn;
		Class<T> clazz = (Class<T>)table.getClass();
		if (clazz.isAnnotationPresent(Table.class)){
			
			if (sqlite==null){
				this.openWritable();
			}
			Table tableAnnotation = clazz.getAnnotation(Table.class);
			String tableName = tableAnnotation.name();
			
			FasterObjectMapper fMapper = FasterObjectMapper.getInstance();
			ContentValues content = fMapper.toContentValues(table);
			
			valueReturn = sqlite.insert(tableName, null, content);
			
		}else{
			throw new AnnotationRuntimeException();
		}
		
		
		return valueReturn;
	}
	
	/**
	 * Update table data
	 * 
	 * @param table: table object
	 * @return int
	 */
	@SuppressWarnings("unchecked")
	public <T> Integer update(T table) throws FasterException{
		
		int valueReturn;
		Class<T> clazz = (Class<T>)table.getClass();
		if (clazz.isAnnotationPresent(Table.class)){
			if (sqlite==null){
				this.openWritable();
			}
			
			Table tableAnnotation = clazz.getAnnotation(Table.class);
			String tableName = tableAnnotation.name();
			
			FasterObjectMapper fMapper = FasterObjectMapper.getInstance();
			ContentValues content = fMapper.toContentValues(table);

			List<ColumnMapper> search = createListJoinMapper(table, true, null);

			StringBuilder sb = new StringBuilder();
			
			boolean isFirstLoop = true;
			List<String> values = new ArrayList<String>();
			for (ColumnMapper mapper : search){
				
				if (!isFirstLoop){
					sb.append(" ");
					sb.append(AND);
					sb.append(" ");
				}

				if (mapper.getRelation()==null) {
					sb.append(mapper.getColumn().name());
				}else{
					sb.append(mapper.getRelation().name());
				}
				sb.append(EQUAL);
				sb.append(PARAM);
				
				values.add(mapper.getValue()==null?null:mapper.getValue().toString());
				content.remove(mapper.getColumn().name());

				isFirstLoop = false;
			}
			
			String[] args = values.toArray(new String[]{});
			
			valueReturn = sqlite.update(tableName, content, sb.toString(), args);
			
		}else{
			throw new AnnotationRuntimeException();
		}
		
		return valueReturn;
	}
	
	/**
	 * Delete row by id
	 * 
	 * @param table: table class
	 * @return rows removed
	 * @throws FasterException 
	 */
	@SuppressWarnings("unchecked")
	public <T> Integer delete(T table) throws FasterException{
		
		int valueReturn;
		
		Class<T> clazz = (Class<T>)table.getClass();
		
		Table tableAnnotation = clazz.getAnnotation(Table.class);
		String tableName = tableAnnotation.name();
			
		if (sqlite==null){
			this.openWritable();
		}
		
		List<ColumnMapper> search = createListJoinMapper(table, false, null);
		
		StringBuilder sb = new StringBuilder();
		boolean isFirstLoop = true;
		List<String> values = new ArrayList<String>();
		for (ColumnMapper mapper : search){
			
			if (!isFirstLoop){
				sb.append(" ");
				sb.append(AND);
				sb.append(" ");
			}

			if (mapper.getRelation()==null) {
				sb.append(mapper.getColumn().name());
			}else{
				sb.append(mapper.getRelation().name());
			}
			sb.append(EQUAL);
			sb.append(PARAM);
			
			values.add(mapper.getValue()==null?null:mapper.getValue().toString());

			isFirstLoop = false;
		}
		//Create args selection
		String[] args = values.toArray(new String[]{});
		
		valueReturn = sqlite.delete(tableName, sb.toString(), args);;
		
		return valueReturn;
	}
	
	/**
	 * Load object by id
	 * @param table: table class
	 * @return result
	 */
	@SuppressWarnings("unchecked")
	public <T> Result<T> get(T table) throws FasterException{
		
		Class<T> clazz = (Class<T>) table.getClass();
		
		if (sqlite==null){
			this.openReadable();
		}
		
		Table tableAnnotation = clazz.getAnnotation(Table.class);
		String tableName = tableAnnotation.name();
		
		boolean isLazy = tableAnnotation.lazy();
		
		List<ColumnMapper> search = createListJoinMapper(table, false, null);
		
		//Create selection
		StringBuilder sb = new StringBuilder();
		
		boolean isFirstLoop = true;
		List<String> values = new ArrayList<String>();
		for (ColumnMapper mapper : search){
			
			if (!isFirstLoop){
				sb.append(" ");
				sb.append(AND);
				sb.append(" ");
			}
			
			String valueCase;
			if (mapper.getColumn().isIgnoreCase()){
				sb.append(" UPPER(");

				if (mapper.getRelation()==null) {
					sb.append(mapper.getColumn().name());
				}else{
					sb.append(mapper.getRelation().name());
				}
				sb.append(")");
				sb.append(EQUAL);
				sb.append(PARAM);
				valueCase = (mapper.getValue()==null?null:mapper.getValue().toString().toUpperCase());
			}else{
				sb.append(mapper.getColumn().name());
				sb.append(EQUAL);
				sb.append(PARAM);
				valueCase = mapper.getValue()==null?null:mapper.getValue().toString();
			}
			
			values.add(valueCase);
			isFirstLoop = false;
		}
		//Create args selection
		String[] args = values.toArray(new String[]{});
		
		List<String> columns = getAllColumns(clazz, false);
		
		Cursor cursor = sqlite.query(tableName, columns.toArray(new String[]{}), sb.toString(), args, null, null, null);
		
		FasterObjectMapper fMapper = FasterObjectMapper.getInstance();
		List<T> result = fMapper.toObject(cursor, clazz);
		
		if (!isLazy){
			
			//Initialize lazy
			for (T lazyTable : result){
				FasterLazyInitializer.initialize(this, lazyTable);
			}
		}
		
		Result<T> results = new Result<T>();
		results.results = result;
		
		return results;
	}
	
	/**
	 * Get all columns name
	 * @param clazz: class
	 * @param onlyId: info if return only ids
	 * @return all columns name
	 */
	private <T> List<String> getAllColumns(Class<T> clazz, boolean onlyId){
		
		List<String> columns = new ArrayList<String>();
		for (Method method : clazz.getMethods()){
			
			if (method.isAnnotationPresent(Column.class)){
				
				Column columnAnnotation  = method.getAnnotation(Column.class);
				
				if (onlyId){
					if (method.isAnnotationPresent(Id.class)){
						columns.add(columnAnnotation.name());
					}
				}else{
					columns.add(columnAnnotation.name());
				}
				
			}else if (method.isAnnotationPresent(Join.class)){
				
				columns.addAll(getAllColumns(method.getReturnType(), true));
			}
		}
		
		return columns;
	}
	
	/**
	 * Create list with the columns mapper
	 * 
	 * @param table: table class
	 * @return list column mapper
	 * @throws FasterException
	 */
	@SuppressWarnings("unchecked")
	private <T> List<ColumnMapper> createListJoinMapper(T table, boolean onlyId, Join.Relation[] relations) throws FasterException{
		
		Class<T> clazz = (Class<T>)table.getClass();
		List<ColumnMapper> columnMappers = null;
		
		for (Method method : clazz.getMethods()){
		
			boolean isContinue = true;
			if (onlyId){
				if (!method.isAnnotationPresent(Id.class)){
					isContinue = false;
				}
			}
			
			if (isContinue && method.isAnnotationPresent(Column.class)){
				
				ColumnMapper columnMapper = createJoinMapper(method, table, relations);
				
				if (columnMapper !=null){
					
					if (columnMappers ==null){
						columnMappers = new ArrayList<ColumnMapper>();
					}
					
					columnMappers.add(columnMapper);
				}
				
			}else if (isContinue && method.isAnnotationPresent(Join.class)){

				Join joinAnnotation = method.getAnnotation(Join.class);
				Join.Relation[] relationsChild = joinAnnotation.value();

				Object tableJoin;
				try{
					tableJoin = method.invoke(table);
				}catch(Exception e){
					throw new InvokeException(e);
				}
				
				if (tableJoin!=null){
					
					if (columnMappers ==null){
						columnMappers = new ArrayList<ColumnMapper>();
					}

					List<ColumnMapper> columns = createListJoinMapper(tableJoin, onlyId, relationsChild);

					if (columns!=null && !columns.isEmpty()){

						for (ColumnMapper columnMapper : columns){

							if (relations!=null && relations.length>0){
								for (Join.Relation relation : relations){
									if (columnMapper.getRelation()!=null && columnMapper.getRelation() != relation
											&& relation.columnRelated().equals(columnMapper.getRelation().name())){
										columnMapper.setRelation(relation);
									}
								}
							}

						}
					}

					columnMappers.addAll(columns);
				}

			}
				
		}	
		
		return columnMappers;
	}
	
	/**
	 * Create object column mapper with the values 
	 * found in table object
	 * 
	 * @param method: method class
	 * @param table: table class
	 * @return column mapper
	 * @throws FasterException
	 */
	private <T> ColumnMapper createJoinMapper (Method method, T table, Join.Relation[] relations) throws FasterException{
		
		ColumnMapper columnMapper =null;
		Column columnAnnotation = method.getAnnotation(Column.class);
		
		Object value;
		try{
			value = method.invoke(table);
		}catch(Exception e){
			throw new InvokeException(e);
		}
		
		if (value!=null){

			if (relations!=null && relations.length>0){
				for (Join.Relation relation : relations){
					if (relation.columnRelated().equals(columnAnnotation.name())){
						columnMapper = new ColumnMapper(null, columnAnnotation, value);
					}
				}
			}else {
				columnMapper = new ColumnMapper(null, columnAnnotation, value);
			}
		}
		
		return columnMapper;
	
	}
	
	/**
	 * Execute script and return the result
	 * 
	 * @param query: query object
	 * @return result
	 */
	@SuppressWarnings("unchecked")
	public <T> Result<T> find(Object query) throws FasterException{
		
		Class<?> clazz = query.getClass();
		
		if (clazz.isAnnotationPresent(SqlScript.class)){
			SqlScript sqlAnnotation = clazz.getAnnotation(SqlScript.class);
			
			Class<T> clazzReturn = (Class<T>) sqlAnnotation.result();
			String sql = sqlAnnotation.sql();
			String sqlChanded = sql;
			
			List<ParameterMapper> params = null;
			
			for (Method method : clazz.getMethods()){
				if (method.isAnnotationPresent(Parameter.class)){
					
					if (params == null){
						params = new ArrayList<ParameterMapper>();
					}
					
					Parameter paramAnnotation = method.getAnnotation(Parameter.class);
					String paramName = paramAnnotation.name();
					
					int position = 0;
					while (position >= 0){
						position = sql.indexOf(paramName);
						if (position>=0){
							
							Object value;
							try {
								value = method.invoke(query);
							} catch (Exception e) {
								throw new InvokeException(e);
							} 
							
							ParameterMapper pMapper = new ParameterMapper(position, paramName, value);
							params.add(pMapper);
							
							StringBuilder sbParam = new StringBuilder();
							sbParam.append(PREFIX);
							sbParam.append(paramName);
							sbParam.append(SUFFIX);
						
							sqlChanded = sqlChanded.replaceFirst(sbParam.toString(), PARAM);
						}
					}
				}
			}
			
			if (sqlite==null){
				this.openReadable();
			}
			
			String[] args = ParameterMapper.toValueArray(params);
			
			if (FasterConfigInfo.getInstance().isShowSql()){
				Log.d("Faster - Find SQL", sqlChanded);
				Log.d("Faster - Find Args", args.toString());
			}
			
			Cursor cursor = this.sqlite.rawQuery(sqlChanded, args);
			
			FasterObjectMapper fMapper = FasterObjectMapper.getInstance();
			List<T> result = fMapper.toObject(cursor, clazzReturn);
		
			Result<T> results = new Result<T>();
			results.results = result;
			
			return results;
		}else{
			throw new AnnotationRuntimeException();
		}
	}
	
	/**
	 * Execute SQL search with the finder object
	 * @param finder: finder object
	 * @return selection object
	 * @throws FasterException
	 */
	public <T> Result<T> find(Finder finder) throws FasterException{
		
		String tableName = finder.getTable();
		List<br.com.ghfsoftware.faster.search.Join<?>> joins = finder.getJoin();
		List<Condition> conditions = finder.getWhere();
		OrderBy orderBy = finder.getOrder();
		Selection<T> selection = finder.getSelection();
		int limit = finder.getLimit();
				
		StringBuilder sbSql = new StringBuilder();
		sbSql.append("SELECT ");
		boolean isFistLoop = true;
		for (br.com.ghfsoftware.faster.search.Column column : selection.getColumns()){
			
			if (!isFistLoop){
				sbSql.append(COMMA);
			}
			
			if (column.getTableName()!=null){
				sbSql.append(column.getTableName());
				sbSql.append(POINT);
			}
			sbSql.append(column.getName());
			if (column.getAlias()!=null){
				sbSql.append(" AS ");
				sbSql.append(column.getAlias());
			}
			isFistLoop = false;
		}
		sbSql.append(" FROM ");
		sbSql.append(tableName);
		if (joins!=null && !joins.isEmpty()){
			for (br.com.ghfsoftware.faster.search.Join<?> join : joins){
				
				sbSql.append(" ");
				sbSql.append(join.getJoinType().getValue());
				sbSql.append(" ");
				
				Class<?> joinClazz = join.getTable();
				if (joinClazz.isAnnotationPresent(Table.class)){
					
					Table tableAnnotation = joinClazz.getAnnotation(Table.class);
					String joinTableName = tableAnnotation.name();
					
					sbSql.append("JOIN ");
					sbSql.append(joinTableName);
					sbSql.append(" ON ");
					
					List<String> idColumns = this.getAllColumns(joinClazz, true);
					
					boolean isNeedAnd = false;
					for (String idColumn : idColumns){
						
						if (isNeedAnd){
							sbSql.append(" ");
							sbSql.append(AND);
							sbSql.append(" ");
						}
						
						sbSql.append(tableName);
						sbSql.append(POINT);
						sbSql.append(idColumn);
						sbSql.append(EQUAL);
						sbSql.append(joinTableName);
						sbSql.append(POINT);
						sbSql.append(idColumn);
						
						isNeedAnd=true;
					}
					
				}else{
					throw new AnnotationRuntimeException();
				}
				
			}
		}
		List<String> parameters = null;
		if (conditions!=null && !conditions.isEmpty()){
			
			parameters = new ArrayList<String>();
			
			sbSql.append(" WHERE ");
			
			boolean isNeedAnd=false;
			for (Condition condition : conditions){
				
				if (isNeedAnd){
					sbSql.append(" ");
					sbSql.append(AND);
					sbSql.append(" ");
				}
				
				sbSql.append(condition.getColumn());
				sbSql.append(condition.getOperator().getValue());
				sbSql.append(PARAM);
				
				parameters.add(condition.getValue().toString());
				isNeedAnd=true;
			}
			
		}
		if (orderBy!=null){
			sbSql.append(" ORDER BY ");
			
			boolean isNeedComma=false;
			for (String column : orderBy.getColumns()){
				
				if (isNeedComma){
					sbSql.append(COMMA);
				}
				
				sbSql.append(column);
				isNeedComma=true;
			}
			sbSql.append(" ");
			sbSql.append(orderBy.getOrder().getValue());
		}
		if (limit>=0){
			sbSql.append(" LIMIT ");
			sbSql.append(limit);
		}
		
		if (sqlite==null){
			this.openReadable();
		}

		String[] args = null;
		if (parameters!=null) {
			args = parameters.toArray(new String[]{});
		}

		if (FasterConfigInfo.getInstance().isShowSql()){
			Log.d("Faster - Find SQL", sbSql.toString());
			if (args!=null) {
				Log.d("Faster - Find Args", args.toString());
			}
		}
		
		Cursor cursor = this.sqlite.rawQuery(sbSql.toString(), args);
		
		FasterObjectMapper fMapper = FasterObjectMapper.getInstance();
		List<T> result = fMapper.toObject(cursor, selection.getClassMapping());
	
		Result<T> results = new Result<T>();
		results.results = result;
		
		return results;
	}
	
	/**
	 * Execute SQL script
	 * @param script: Script object
	 * @throws FasterException
	 */
	public void execute(Object script) throws FasterException{
		
		Class<?> clazz = script.getClass();
		
		if (clazz.isAnnotationPresent(SqlScript.class)){
			SqlScript sqlAnnotation = clazz.getAnnotation(SqlScript.class);
			
			String sql = sqlAnnotation.sql();
			String sqlChanded = sql;
			
			List<ParameterMapper> params = null;
			
			for (Method method : clazz.getMethods()){
				if (method.isAnnotationPresent(Parameter.class)){
					
					if (params == null){
						params = new ArrayList<ParameterMapper>();
					}
					
					Parameter paramAnnotation = method.getAnnotation(Parameter.class);
					String paramName = paramAnnotation.name();
					
					int position = 0;
					while (position >= 0){
						position = sql.indexOf(paramName);
						if (position>=0){
							
							Object value;
							try {
								value = method.invoke(script);
							} catch (Exception e) {
								throw new InvokeException(e);
							} 
							
							ParameterMapper pMapper = new ParameterMapper(position, paramName, value);
							params.add(pMapper);
							
							StringBuilder sbParam = new StringBuilder();
							sbParam.append(PREFIX);
							sbParam.append(paramName);
							sbParam.append(SUFFIX);
						
							sqlChanded = sqlChanded.replaceFirst(sbParam.toString(), PARAM);
						}
					}
				}
			}
			
			if (sqlite==null){
				this.openWritable();
			}
			
			String[] args = ParameterMapper.toValueArray(params);
			
			if (FasterConfigInfo.getInstance().isShowSql()){
				Log.d("Faster - Exec SQL", sqlChanded);
				Log.d("Faster - Exec Args", args.toString());
			}
			
			this.sqlite.execSQL(sqlChanded, args);
		}
			
	}
	
	/**
	 * Create all tables
	 * @param classes: class found
	 */
	public void createDatabase(Set<Class<?>> classes){
		
		if (classes!=null && !classes.isEmpty()){
			
			Map<String, CreationExecutionMapper> mapperExecution = new HashMap<String, CreationExecutionMapper>();
			Map<String, List<String>> mapperTableDependencies = new HashMap<String, List<String>>();
					
			for (Class<?> clazz : classes){
				
				if (clazz.isAnnotationPresent(Table.class)){
					
					Table tableAnnotation = clazz.getAnnotation(Table.class);
					String tableName = tableAnnotation.name();
					
					StringBuilder sbSql = new StringBuilder();
					sbSql.append("CREATE TABLE ");
					sbSql.append(tableName);
					sbSql.append(" (");
					
					List<String> ids = new ArrayList<String>();
					List<String> fks = new ArrayList<String>();
					
					boolean isFirstLoop = true;
					for (Method method : clazz.getMethods()){
						
						if (!isFirstLoop){
							sbSql.append(COMMA);
						}

						if (method.isAnnotationPresent(Column.class)){
							
							Column columnAnnotation = method.getAnnotation(Column.class);
							String columnName = columnAnnotation.name();
							Column.SQLiteType type = columnAnnotation.type();
							boolean isNotNull = columnAnnotation.isNotNull();
							
							if (method.isAnnotationPresent(Id.class)){
								ids.add(columnName);
							}
							
							sbSql.append(columnName);
							sbSql.append(" ");
							sbSql.append(type.getValue());
							sbSql.append(" ");
						
							if (isNotNull){
								sbSql.append("NOT NULL");
							}
							isFirstLoop = false;

						}else if(method.isAnnotationPresent(Join.class)){

							Join joinAnnotation = method.getAnnotation(Join.class);

							Class<?> tableDependencyClass = method.getReturnType();
							if (tableDependencyClass.isAnnotationPresent(Table.class)){
								Table tableDependencyAnnot = tableDependencyClass.getAnnotation(Table.class);
								
								if (mapperTableDependencies.containsKey(tableName)){

									List<String> listDependency = mapperTableDependencies.get(tableName);
									listDependency.add(tableDependencyAnnot.name());

								}else{
									List<String> listDependency = new ArrayList<String>();
									listDependency.add(tableDependencyAnnot.name());

									mapperTableDependencies.put(tableName, listDependency);
								}						
		
							}else{
								throw new AnnotationRuntimeException();
							}
							  
							
							List<ColumnMapper> identificators = this.getIdJoinFields(method.getReturnType().getClass(), joinAnnotation.value());
							
							boolean isPK=false;
							if (method.isAnnotationPresent(Id.class)){
								isPK = true;
							}
							
							if (identificators!=null && !identificators.isEmpty()){
								
								for (ColumnMapper columnMapper : identificators){

									Join.Relation relationAnnotation = columnMapper.getRelation();
									Column columnAnnotation = columnMapper.getColumn();

									String columnName;
									if (relationAnnotation==null){
										columnName = columnAnnotation.name();
									}else{
										columnName = relationAnnotation.name();
									}

									Column.SQLiteType type = columnAnnotation.type();
									boolean isNotNull = columnAnnotation.isNotNull();
									
									if (isPK){
										ids.add(columnName);
									}
									
									sbSql.append(columnName);
									sbSql.append(" ");
									sbSql.append(type.getValue());
									sbSql.append(" ");
								
									if (isNotNull){
										sbSql.append("NOT NULL");
									}
									
									fks.add(columnName);
									
									isFirstLoop = false;
								}
							}
							
						}
						
					}
					
					if (!ids.isEmpty()){
						sbSql.append(", PRIMARY KEY (");
						isFirstLoop=true;
						for (String columnName : ids){
							if (!isFirstLoop){
								sbSql.append(COMMA);
							}
							sbSql.append(columnName);
							isFirstLoop=false;
						}
						sbSql.append(")");
					}
					
					if (!ids.isEmpty()){
						sbSql.append(", FOREIGN KEY (");
						isFirstLoop=true;
						for (String columnName : fks){
							if (!isFirstLoop){
								sbSql.append(COMMA);
							}
							sbSql.append(columnName);
						}
						sbSql.append(")");
					}
					
					sbSql.append(" )");
					
					if (FasterConfigInfo.getInstance().isShowSql()){
						Log.d("Faster - Database SQL", sbSql.toString());
					}
					
					mapperExecution.put(tableName, CreationExecutionMapper.create(tableName, sbSql.toString()));
					
				}
				
			}
			
			this.associateDependencies(mapperExecution, mapperTableDependencies);
		
			if (!mapperExecution.isEmpty()){
				
				for (CreationExecutionMapper execution : mapperExecution.values()){
					processCreationScripts(execution);
					execution.setExecuted();
				}
			}
			
		}
		
	}
	
	/**
	 * Make association of dependencies
	 * 
	 * @param mapperExecution: mapperExecutor object
	 * @param mapperTableDependencies: mapperTableDependencies object
	 */
	private void associateDependencies (Map<String, CreationExecutionMapper> mapperExecution, Map<String, List<String>> mapperTableDependencies){
		
		if (mapperExecution!=null && !mapperExecution.isEmpty()){
			
			for (CreationExecutionMapper execution : mapperExecution.values()){


				List<String> dependencies = mapperTableDependencies.get(execution.getTableName());
				
				if (dependencies!=null && !dependencies.isEmpty()){
					
					for (String dependency : dependencies){
						execution.addDependency(mapperExecution.get(dependency));
					}
				}
				
			}
		}
		
	}
	
	/**
	 * Create the tables using the mapping execution object
	 * @param execution: execution class
	 */
	private void processCreationScripts (CreationExecutionMapper execution){
		
		if (!execution.isStatusExecution()){
		
			if (execution.getDependecies()!=null && !execution.getDependecies().isEmpty()){
				
				for (CreationExecutionMapper dependency : execution.getDependecies()){
					processCreationScripts(dependency);
					dependency.setExecuted();
				}
				
			}
			
			sqlite.execSQL(execution.getSql());
			
		}
					
	}
	
	/**
	 * Return list of identifications column about the join fieds
	 * @param clazz: class
	 * @return join identifications fields
	 */
	private List<ColumnMapper> getIdJoinFields(Class<?> clazz, Join.Relation[] relations){
		
		if (clazz.isAnnotationPresent(Table.class)){
			
			List<ColumnMapper> identificators = new ArrayList<ColumnMapper>();
			
			for (Method method : clazz.getMethods()){

				if (method.isAnnotationPresent(Id.class)){

					if (method.isAnnotationPresent(Column.class) && method.isAnnotationPresent(Id.class)){
						Column columnAnnotation = method.getAnnotation(Column.class);

						if (relations!=null) {
							for (Join.Relation relation : relations) {

								Join.Relation relationAnnotation = relation;

								if (relationAnnotation.columnRelated().equals(columnAnnotation.name())){
									identificators.add(new ColumnMapper(relation, columnAnnotation));
								}
							}
						}else {
							identificators.add(new ColumnMapper(null, columnAnnotation));
						}
					}else if (method.isAnnotationPresent(Join.class)){

						Join joinAnnotation = method.getAnnotation(Join.class);
						Join.Relation[] relationsChild = joinAnnotation.value();

						List<ColumnMapper> relIdentificators = this.getIdJoinFields(method.getReturnType().getClass(), relationsChild);

						if (relationsChild!=null) {
							for (Join.Relation relation : relations) {
								if (relIdentificators != null && !relIdentificators.isEmpty()) {
									for (ColumnMapper joinIdentificator : relIdentificators) {

										if (joinIdentificator.getRelation()!=null && joinIdentificator.getRelation() != relation && joinIdentificator.getRelation().name().equals(relation.columnRelated())){
											joinIdentificator.setRelation(relation);
										}
									}
								}
							}
						}

						identificators.addAll(relIdentificators);
					}
				}
				
			}
			
			return identificators;
			
		}else{
			throw new AnnotationRuntimeException();
		}
	}
	
	/**
	 * Execute a SQL command list
	 * @param commands: sql commands
	 */
	public void executeCommands(List<String> commands){
		
		for (String sql : commands){
			
			if (FasterConfigInfo.getInstance().isShowSql()){
				Log.d("Faster - SQL", sql);
			}
			
			sqlite.execSQL(sql);
		}
	}
	
	/**
	 * Class responsible to offer the results
	 * @author gustavo
	 * @version 1.0
	 *
	 * @param <T>
	 */
	public class Result <T> {
		
		protected List<T> results;
		
		/**
		 * Get list results
		 * @return list results
		 */
		public List<T> list(){
			return results;
		}
		
		/**
		 * Get unique result
		 * @return unique result
		 * @throws FasterException
		 */
		public T unique() throws FasterException{
			if (this.results==null || this.results.isEmpty()){
				throw new FasterException();
			}else{
				return results.get(0);
			}
		}

	}
	
}

