package br.com.ghfsoftware.faster.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to indicate the Join between two
 * tables
 * 
 * @author gustavo
 * @version 1.0
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Documented
public @interface Join {

    /**
     * Annotation to indicate the relation between the fields
     */
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.METHOD)
    @Documented
    @interface Relation {

        /**
         * Column name of the local table
         * @return name of the column on data base
         */
        String name();

        /**
         * Column name of table related
         * @return name of the column on data base
         */
        String columnRelated();

        /**
         * Inform if the local field accept null or not
         * @return not null or null
         */
        boolean isNotNull() default true;
    }

    /**
     * Mapped the fields related
     * @return relations list
     */
    Relation[] value() default {};
	
}
