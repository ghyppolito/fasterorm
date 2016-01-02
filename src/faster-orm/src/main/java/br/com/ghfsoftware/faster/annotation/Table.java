package br.com.ghfsoftware.faster.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to mapping table
 * objects
 * 
 * @author gustavo
 * @version 1.0
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
public @interface Table {
	
	/**
	 * Table name
	 * @return table name
	 */
	String name();
	
	/**
	 * Indicate if use or not lazy strategy
	 * 
	 * @return the lazy strategy
	 */
	boolean lazy() default true;

	/**
	 * Annotation to apply sequence in
	 * identification field
	 *
	 * @author Gustavo Hyppolito
	 */
	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.METHOD)
	@Documented
	@interface Sequence{

		/**
		 * Inform the name of the sequence
		 * @return sequence name
		 */
		String value();
	}
	
	/**
	 * Annotation to mapping id fields
	 * in tables
	 * 
	 * @author gustavo
	 *
	 */
	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.METHOD)
	@Documented
	@interface Id{
		
	}
	
	/**
	 * Annotation to mapping colunms in
	 * tables
	 * 
	 * @author gustavo
	 *
	 */
	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.METHOD)
	@Documented
	@interface Column{
		
		/**
		 * SQLite types enumerator
		 * @author gustavo
		 *
		 */
		public enum SQLiteType{
			
			NULL("NULL"), INTEGER("INT"), REAL("REAL"), TEXT("TEXT"), BLOB("BLOB");
			
			private String value;
			
			/**
			 * Constructor
			 * @param value
			 */
			SQLiteType(String value){
				this.value = value;
			}
			
			/**
			 * Get value
			 * @return value
			 */
			public String getValue(){
				return value;
			}
		}
		
		/**
		 * Indicate the column name
		 * 
		 * @return column name
		 */
		String name();
		
		/**
		 * Indicate if the field is not null or not
		 * 
		 * @return is null or not null 
		 */
		boolean isNotNull() default false;
		
		/**
		 * Indicate the ignore case strategy
		 * in search
		 * 
		 * @return ignore case strategy
		 */
		boolean isIgnoreCase() default false;
		
		/**
		 * Indicate the column type
		 * 
		 * @return column type
		 */
		SQLiteType type();
		
		
	}
	
}
