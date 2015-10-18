package br.com.ghfsoftware.faster.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to indicate the package where
 * the models are
 * 
 * @author gustavo
 * @version 1.0
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
public @interface FasterConfig {

	/**
	 * Indicate where the models are
	 * @return package
	 */
	String packageName();
	
	/**
	 * Indicate where are the upgrade database file
	 * @return upgrade database file
	 */
	String scriptFile() default "";
	
	/**
	 * Setting debug mode
	 * @return debug mode
	 */
	boolean showSql() default false;
}
