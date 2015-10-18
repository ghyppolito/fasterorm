package br.com.ghfsoftware.faster;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;
import br.com.ghfsoftware.faster.annotation.Join;
import br.com.ghfsoftware.faster.annotation.Table;
import br.com.ghfsoftware.faster.annotation.Table.Column;
import br.com.ghfsoftware.faster.annotation.Table.Id;
import br.com.ghfsoftware.faster.exception.AnnotationRuntimeException;
import br.com.ghfsoftware.faster.exception.FasterException;
import br.com.ghfsoftware.faster.exception.InitializeObjectException;
import br.com.ghfsoftware.faster.exception.InvokeException;
import br.com.ghfsoftware.faster.exception.TypeNotSupportedException;
import br.com.ghfsoftware.faster.mapper.ColumnMapper;

/**
 * Class responsible to transform the Object
 * in a SQL text 
 * @author gustavo
 *
 */
public class FasterObjectMapper {

	private static FasterObjectMapper instance;
	
	/**
	 * Constructor
	 */
	private FasterObjectMapper(){
		
	}
	
	/**
	 * Get instance of singleton
	 * @return instance
	 */
	public static FasterObjectMapper getInstance(){
		
		if (instance == null){
			instance = new FasterObjectMapper();
		}
		return instance;
	}
	
	/**
	 * Convert the table fields in ContentValues
	 * @param table
	 * @return ContentValues
	 */
	public <T> ContentValues toContentValues(T table) throws FasterException{
		return toContentValues(null, table);
	}
	
	/**
	 * 
	 * @param content
	 * @param table
	 * @return
	 * @throws FasterException
	 */
	@SuppressWarnings("unchecked")
	private <T> ContentValues toContentValues(ContentValues content, T table) throws FasterException{
		
		Class<T> clazz = (Class<T>)table.getClass();
		
		for (Method method : clazz.getMethods()){
			this.setColumnValue(content, method, table);
			this.setJoinValue(content, method, table);
		}
		
		return content;
	}
	
	/**
	 * Set the value of methods that it have Column annotation
	 * in content
	 * @param content
	 * @param method
	 * @param table
	 * @throws FasterException
	 */
	private <T> void setColumnValue(ContentValues content, Method method, T table) throws FasterException{
		
		if (method.isAnnotationPresent(Column.class)){
			if (content==null){
				content = new ContentValues();
			}
			Column columnAnnotation = method.getAnnotation(Column.class);
			String name = columnAnnotation.name();
			Object value = null;
			try {
				value = method.invoke(table);
			} catch (Exception e) {
				throw new InvokeException(e);
			}
			//Set value to content object making casting
			this.putValue(content, name, value);
		}
	}
	
	/**
	 * Set the value of methods that it have Column annotation
	 * in content
	 * 
	 * @param content
	 * @param method
	 * @param table
	 * @throws FasterException
	 */
	private <T> void setJoinValue(ContentValues content, Method method, T table) throws FasterException{
		
		if (method.isAnnotationPresent(Join.class)){
			Join joinAnnotation = method.getAnnotation(Join.class);

			if (content==null){
				content = new ContentValues();
			}

			Object relTable = null;
			try {
				relTable = method.invoke(table);
			} catch (Exception e) {
				throw new InvokeException(e);
			}			
			
			List<ColumnMapper> identificators = this.getIdentificators(relTable, joinAnnotation.value());
			
			if (identificators!=null && !identificators.isEmpty()){
				for (ColumnMapper columnMapper : identificators){

					String name;
					if (columnMapper.getRelation()==null){
						name = columnMapper.getColumn().name();
					}else{
						name = columnMapper.getRelation().name();
					}
					this.putValue(content, name, columnMapper.getValue());
				}
			}
		}
	} 
	
	/**
	 * Class to get identificators about a table
	 * @param table
	 * @return map with identificators
	 * @throws FasterException
	 */
	@SuppressWarnings("unchecked")
	private <T> List<ColumnMapper> getIdentificators (T table, Join.Relation[] relations) throws FasterException{
		
		Class<T> clazz = (Class<T>) table.getClass();
		
		if (clazz.isAnnotationPresent(Table.class)){
			
			List<ColumnMapper> identificators = new ArrayList<ColumnMapper>();
			
			for (Method method : clazz.getMethods()){
				
				if (method.isAnnotationPresent(Id.class)){
					
					if (method.isAnnotationPresent(Column.class)){
						Column columnAnnotation = method.getAnnotation(Column.class);
						
						Object value;
						try{
							value = method.invoke(table);
						}catch(Exception e){
							throw new InvokeException(e);
						}

						if (relations!=null){
							for (Join.Relation relation : relations){
								if (relation.columnRelated().equals(columnAnnotation.name())){
									identificators.add(new ColumnMapper(relation,columnAnnotation,value));
								}
							}
						}else{
							identificators.add(new ColumnMapper(null,columnAnnotation,value));
						}

					}else if (method.isAnnotationPresent(Join.class)){

						Join joinAnnotation = method.getAnnotation(Join.class);
						Join.Relation[] relationsChild = joinAnnotation.value();
						
						Object value;
						try{
							value = method.invoke(table);
						}catch(Exception e){
							throw new InvokeException(e);
						}
						
						List<ColumnMapper> relIdentificators = this.getIdentificators(value, relationsChild);

						if (relationsChild!=null && relationsChild.length>0){
							for (Join.Relation relation : relations){
								if (relIdentificators!=null && !relIdentificators.isEmpty()){

									for (ColumnMapper columnMapper : relIdentificators){

										if (columnMapper.getRelation()!=null && relation != columnMapper.getRelation() && relation.columnRelated().equals(columnMapper.getRelation().name())){
											columnMapper.setRelation(relation);
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
	 * Convert Cursor in Object
	 * @param cursor
	 * @param clazz
	 * @return list objects
	 * @throws InitializeObjectException 
	 * @throws InvokeException 
	 */
	public <T> List<T> toObject (Cursor cursor, Class<T> clazz) throws FasterException{
		
		List<T> list = null;
			
			if (cursor != null && cursor.moveToFirst()) {
				list = new ArrayList<T>();
				
				do {
					T object;
					try {
						object = clazz.newInstance();
					} catch (Exception e) {
						throw new InitializeObjectException(e);
					}
					
					populateObject(object, cursor);
					list.add(object);
	            	
	            }while (cursor.moveToNext());
			}
			 
		
		return list;
	}
	
	/**
	 * Method to populate the object table
	 * 
	 * @param table
	 * @param cursor
	 * @return if is populated
	 * @throws FasterException
	 */
	@SuppressWarnings("unchecked")
	private <T> boolean populateObject(T table, Cursor cursor) throws FasterException{
		
		Class<T> clazz = (Class<T>) table.getClass();
		boolean isSet = false;
		
		for (Field field : clazz.getFields()){
			
			try {
				
				Class<?> fieldClass = field.getClass();
					
				if (fieldClass.isAnnotationPresent(Table.class)){

					Object tableJoin = fieldClass.newInstance();
					isSet = populateObject(tableJoin, cursor);
				}else{
					String name = field.getName();
					Object value = this.getCursorValue(cursor, name, fieldClass);
					if (value!=null){
						field.set(table, value);
						isSet = true;
					}
				}
				
			} catch (Exception e) {
				throw new InvokeException(e);
			} 
		}
		
		return isSet;
		
	}
	
	/**
	 * Get cursor field value
	 * 
	 * @param cursor
	 * @param fieldName
	 * @param fieldClass
	 * @return cursor field value
	 * @throws TypeNotSupportedException 
	 */
	@SuppressWarnings("unchecked")
	private <T> T getCursorValue(Cursor cursor, String fieldName, Class<?> fieldClass) throws TypeNotSupportedException{
		
		String canonicalName = fieldClass.getCanonicalName();
		T value = null;
		int index = cursor.getColumnIndex(fieldName);
		
		if (index>=0){
			if(canonicalName.equals(String.class.getCanonicalName())){
				value = (T) cursor.getString(index);
			}else if (canonicalName.equals(Long.class.getCanonicalName())){
				value = (T) Long.valueOf(cursor.getLong(index));
			}else if (canonicalName.equals(Integer.class.getCanonicalName())){
				value = (T) Integer.valueOf(cursor.getInt(index));
			}else if (canonicalName.equals(Double.class.getCanonicalName())){
				value = (T) Double.valueOf(cursor.getDouble(index));
			}else if (canonicalName.equals(Float.class.getCanonicalName())){
				value = (T) Float.valueOf(cursor.getFloat(index));
			}else if (canonicalName.equals(Short.class.getCanonicalName())){
				value = (T) Short.valueOf(cursor.getShort(index));
			}else if (canonicalName.equals(Date.class.getCanonicalName())){
				value = (T) new Date(cursor.getLong(index));
			}else{
				throw new TypeNotSupportedException();
			}
		}
		
		return value;
	}
	
	/**
	 * Put values into content
	 * @param content
	 * @param name
	 * @param value
	 */
	private void putValue(ContentValues content, String name, Object value) throws FasterException{
		
		if(value instanceof String){
			content.put(name, (String)value);
		}else if (value instanceof Long){
			content.put(name, (Long)value);
		}else if (value instanceof Integer){
			content.put(name, (Integer)value);
		}else if (value instanceof Double){
			content.put(name, (Double)value);
		}else if (value instanceof Float){
			content.put(name, (Float)value);
		}else if (value instanceof Short){
			content.put(name, (Short)value);
		}else if (value instanceof Date){
			content.put(name, ((Date)value).getTime());
		}else{
			throw new TypeNotSupportedException();
		}
	}
}
