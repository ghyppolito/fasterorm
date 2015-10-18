package br.com.ghfsoftware.faster.util;

import java.util.Set;

import org.reflections.Reflections;

/**
 * Class to get the class list of the package
 * @author Gustavo Ferreira
 * @version 1.0
 *
 */
public class ClassMapper {
	
	/**
	 * Method to search classes in package
	 * 
	 * @param packageName
	 * @return classes
	 */
	public static Set<Class<? extends Object>> list(String packageName){
		
		Reflections reflections = new Reflections(packageName);

		 Set<Class<? extends Object>> allClasses = 
		     reflections.getSubTypesOf(Object.class);
		 
		 return allClasses;
		
	}

}
