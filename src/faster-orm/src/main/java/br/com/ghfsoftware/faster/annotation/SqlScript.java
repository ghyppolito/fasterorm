package br.com.ghfsoftware.faster.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to create scripts SQL
 * 
 * @author gustavo
 * @version 1.0
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
public @interface SqlScript {
	
	/**
	 * It's the SQL script will be executed
	 * 
	 * @return SQL script
	 */
	String sql();
	
	/**
	 * Indicate the object that will be returned
	 * 
	 * @return class will be returned
	 */
	Class<?> result() default Object.class;
	
	/**
	 * Annotation to set parameter in script
	 * 
	 * @author gustavo
	 *
	 */
	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.METHOD)
	@Documented
	public @interface Parameter{
		
		/**
		 * Parameter name
		 * 
		 * @return parameter name
		 */
		String name();
	}
}
