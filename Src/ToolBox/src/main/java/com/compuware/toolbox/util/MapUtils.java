package com.compuware.toolbox.util;

import java.util.Map;

public class MapUtils {

    /**
     * Get a value from a hashmap, testing a list of keys.
     * @param var the hashmap input.
     * @param keyList the list of keys.
     * @return the value found or null.
     */
	public static Object get(Map var, String[] keyList) {
    	Object result = null;
    	if (var != null && keyList != null) {
    		int le = keyList.length;
    		int i = 0;
    		while (result == null && i < le) {
    			result = var.get(keyList[i]);
    			i++;
    		}
    	}
    	return result;
    }
        
	
}
