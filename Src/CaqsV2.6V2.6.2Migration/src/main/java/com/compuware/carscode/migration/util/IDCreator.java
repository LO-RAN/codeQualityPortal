/**
 * Titre : <p>
 * Description : <p>
 * Copyright : Copyright (c) François-Xavier ALBOUY<p>
 * Société : Software & Process<p>
 * @author François-Xavier ALBOUY
 * @version 1.0
 */
package com.compuware.carscode.migration.util;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class IDCreator {
    
    public IDCreator() {        
    }
    
    /**
     * Generate a 23 character length uniqueId.
     * @return a uniqueId.
     */
    public static String getID(){
        long newid = new Double(1000000*Math.random()).longValue();
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        DecimalFormat formatterNum = new DecimalFormat("######");
        String randomNumberStr = formatterNum.format(newid);
        if (randomNumberStr.length() < 6){
            for (int i = randomNumberStr.length() ; i < 6; i++){
                randomNumberStr+="0";
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