package br.com.ghfsoftware.faster.util;

import android.content.Context;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Set;

import org.reflections.Reflections;

import br.com.ghfsoftware.faster.exception.ConfigJsonLoaderException;
import dalvik.system.DexFile;

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
	 * @param packageName: package name
	 * @return classes
	 */
	public static String[] list(String packageName, Context context){

		List<String> classes = new ArrayList<String>();
		try {
			String packageCodePath = context.getPackageCodePath();
			DexFile df = new DexFile(packageCodePath);
			for (Enumeration<String> iter = df.entries(); iter.hasMoreElements(); ) {
				String className = iter.nextElement();
				if (className.contains(packageName)) {
					classes.add(className);
				}
			}
		} catch (IOException e) {
			throw new ConfigJsonLoaderException(e);
		}

		return classes.toArray(new String[classes.size()]);
	}

}
