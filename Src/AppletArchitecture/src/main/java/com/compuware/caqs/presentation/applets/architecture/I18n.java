/**
 * 
 */
package com.compuware.caqs.presentation.applets.architecture;

import java.util.ResourceBundle;

/**
 * @author cwfr-fdubois
 *
 */
public class I18n {
    private static ResourceBundle resources = ResourceBundle.getBundle("architecture_en");

    protected static void init(String language) {
    	resources = ResourceBundle.getBundle("architecture_" + language);
    }
    
    public static String getString(String key) {
    	return resources.getString(key);
    }
    
}
