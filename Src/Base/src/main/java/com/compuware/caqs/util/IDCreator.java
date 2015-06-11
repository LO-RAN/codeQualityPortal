/**
 * Titre : <p>
 * Description : <p>
 * Copyright : Copyright (c) François-Xavier ALBOUY<p>
 * Société : Software & Process<p>
 * @author François-Xavier ALBOUY
 * @version 1.0
 */
package com.compuware.caqs.util;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class IDCreator {
    
    public IDCreator() {        
    }
    
	private static Random randomizer = new Random();

	private static int suffix = randomizer.nextInt(1000000);
	private static Object lock = new Object();
	
	private static int getSuffix() {
		int result;
		synchronized (lock) {
			result = IDCreator.suffix;
			IDCreator.suffix = (IDCreator.suffix + 1) % 1000000;
		}
		return result;
	}
	
	/**
     * Generate a 23 character length uniqueId.
     * @return a uniqueId.
	 * @throws Exception 
     */
    public static String getID() {
    	int newid = getSuffix(); 
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        DecimalFormat formatterNum = new DecimalFormat("######");
        String randomNumberStr = formatterNum.format(newid);
        if (randomNumberStr.length() < 6){
            for (int i = randomNumberStr.length() ; i < 6; i++){
                randomNumberStr = "0" + randomNumberStr;
            }
        }
        String newidStr = formatter.format(date) + randomNumberStr;
        return newidStr;
    }
    
    /**
     * Format a given ID according to the folowing rules:
     * An id should be in uppercase.
     * An id should not have any space. Spaces are transformed into underscores.
     * @param id
     * @return
     */
    public static String formatId(String id) {
    	String result = null;
    	if (id != null) {
    		result = id.toUpperCase();
    		result = result.replaceAll("\\s", "_");
    	}
    	return result;
    }
    
}