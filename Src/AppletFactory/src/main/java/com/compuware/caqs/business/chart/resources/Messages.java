package com.compuware.caqs.business.chart.resources;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class Messages {
	private static final String BUNDLE_NAME = "com.compuware.caqs.business.chart.resources.messages";

	private Messages() {
	}

	public static String getString(String key, Locale loc) {
		String retour = "";
		try {
			ResourceBundle currentBundle = ResourceBundle.getBundle(BUNDLE_NAME, loc);
			if(currentBundle!=null) {
				retour = currentBundle.getString(key);
			}
		} catch (MissingResourceException e) {
			retour = '!' + key + '!';
		}
		return retour;
	}
}
