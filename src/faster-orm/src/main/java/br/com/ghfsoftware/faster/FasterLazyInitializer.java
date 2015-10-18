package br.com.ghfsoftware.faster;

import java.lang.reflect.Method;
import java.util.List;

import br.com.ghfsoftware.faster.annotation.Join;
import br.com.ghfsoftware.faster.annotation.JoinList;
import br.com.ghfsoftware.faster.annotation.Table.Id;
import br.com.ghfsoftware.faster.exception.FasterException;
import br.com.ghfsoftware.faster.exception.InitializeObjectException;
import br.com.ghfsoftware.faster.exception.InvokeException;

/**
 * Class to make lazy objects initialization
 * @author gustavo
 * @version 1.0
 *
 */
public class FasterLazyInitializer {
	
	public static final String GET = "get";
	public static final String SET = "set";
	
	/**
	 * Initialize the lazy objects
	 * 
	 * @param fasterManager
	 * @param table
	 * @return table object
	 * @throws InvokeException 
	 */
	@SuppressWarnings("unchecked")
	public static <T> T initialize(FasterManager fasterManager, T table) throws FasterException{
		
		Class<T> clazz = (Class<T>) table.getClass();
		
		for (Method method : clazz.getMethods()){
			
			if (method.isAnnotationPresent(Join.class)){
				
				
				Object lazyObj;
				try {
					lazyObj = method.invoke(table);
				} catch (Exception e) {
					throw new InvokeException(e);
				} 
				lazyObj = fasterManager.get(lazyObj).unique();
				
				if (lazyObj!=null){
					String methodName = method.getName();
					String setMethodName = methodName.replaceFirst(GET, SET);
					try{

						Method setMethod = clazz.getMethod(setMethodName, method.getReturnType());
						setMethod.invoke(table, lazyObj);
					} catch (Exception e) {
						throw new InvokeException(e);
					}
				}
			}
			
			if (method.isAnnotationPresent(JoinList.class)){
				
				Class<List<?>> listLazyClass = (Class<List<?>>) method.getReturnType();
				Class<?> lazyClass = listLazyClass.getSuperclass();
				Object lazyList;
				try {
					lazyList = lazyClass.newInstance();
				} catch (Exception e) {
					throw new InitializeObjectException(e);
				}				
				
				try {
					
					List<?> listResults = null;
					for (Method lazyMethod :  lazyClass.getMethods()){
						
						if (lazyMethod.isAnnotationPresent(Join.class)){
							if (clazz.getName().equals(lazyMethod.getReturnType().getName())){
								
								Object lazyReference = clazz.newInstance();
								
								for (Method methodId : clazz.getMethods()){
									
									if (methodId.isAnnotationPresent(Id.class)){
										String methodName = method.getName();
										String setMethodName = methodName.replaceFirst(GET, SET);
										try{

											Method setMethod = clazz.getMethod(setMethodName, methodId.getReturnType());
											setMethod.invoke(lazyReference, methodId.invoke(table));
										} catch (Exception e) {
											throw new InvokeException(e);
										}

									}
								}
							}
							
							listResults = fasterManager.get(lazyList).list();
						}
						
					}
					
					if  (listResults!=null){
						String methodName = method.getName();
						String setMethodName = methodName.replaceFirst(GET, SET);
						Method setMethod = clazz.getMethod(setMethodName, method.getReturnType());
						
						setMethod.invoke(table, listResults);
					}
				} catch (Exception e) {
					throw new InvokeException(e);
				} 
				
				
			}
		}
		
		return null;
	}

}
