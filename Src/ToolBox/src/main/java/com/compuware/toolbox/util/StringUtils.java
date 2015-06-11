package com.compuware.toolbox.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.StringTokenizer;

/**
 * Created by IntelliJ IDEA.
 * User: cwfr-fdubois
 * Date: 3 févr. 2006
 * Time: 16:44:43
 * To change this template use File | Settings | File Templates.
 */
public class StringUtils {

    /**
     * Transforme une chaine de caractères en collection en fonction d'un séparateur.
     * @param from la chaine de caractères source.
     * @param separator le séparateur.
     * @return la collection représentant la chaine de caractère.
     */
    public static Collection getCollectionFromString(String from, String separator) {
        Collection result = new ArrayList();
        if (from != null && from.length() > 0) {
            StringTokenizer tokenizer = new StringTokenizer(from, separator);
            while (tokenizer.hasMoreTokens()) {
                result.add(tokenizer.nextToken());
            }
        }
        return result;
    }

    /**
     * Retourne la sous chaine de caracteres comprise entre 2 caracteres.
     * @param value la chaine a etudier.
     * @param beginDelimiterChar le dernier caractere de depart de la sous chaine.
     * @param endDelimiterChar le premier caractere de fin de la sous chaine.
     * @return la sous chaine de caracteres comprise entre le premier caractere de fin et le dernier caractere de depart de la sous chaine.
     */
    public static String getStringBetween(String value, char beginDelimiterChar, char endDelimiterChar) {
    	String result = value;
    	int idx = result.indexOf(endDelimiterChar);
    	if (idx > 0) {
    		result = result.substring(0, idx);
    	}
    	result = result.substring(result.lastIndexOf(beginDelimiterChar) + 1);
    	return result;
    }

    /**
     * Retourne la sous chaine maximale de caracteres comprise entre 2 caracteres.
     * @param value la chaine a etudier.
     * @param beginDelimiterChar le dernier caractere de depart de la sous chaine.
     * @param endDelimiterChar le premier caractere de fin de la sous chaine.
     * @param maxlength la taille maximale de la chaine reoutrnee.
     * @return la sous chaine de caracteres comprise entre le premier caractere de fin et le dernier caractere de depart de la sous chaine.
     */
    public static String getStringBetween(String value, char beginDelimiterChar, char endDelimiterChar, int maxlength) {
    	String result = getStringBetween(value, beginDelimiterChar, endDelimiterChar);
    	return getMaxString(result, maxlength);
    }
    
    /**
     * Retourne la valeur maximale de la chaine de caracteres.
     * @param value la chaine de caracteres a etudier.
     * @param maxlength la taille maximale.
     * @return la valeur maximale de la chaine de caracteres.
     */
    public static String getMaxString(String value, int maxlength) {
    	String result = value;
    	if (value.length() > maxlength) {
            result = value.substring(0, maxlength);
        }
    	return result;
    }
    
    /**
     * Verify if a string starts with another one ignoring case.
     * @param s the input string.
     * @param ref the beginning.
     * @return true if the string starts with the reference string, false elsewhere.
     */
    public static boolean startsWithIgnoreCase(String s, String ref) {
    	// Default value: startsWith = false
    	boolean result = false;
    	if ((s == null) && (ref == null)) {
    		// Everything is null: OK
    		result = true;
    	}
    	else if ((s != null) && (ref != null) && (s.length() >= ref.length())) {
    		// Everything is not null and the beginning length is lower than the string 
    		// Get two strings with the same length
        	String tmp = s.substring(0, ref.length());
        	// Compare the two strings ignoring case.
        	result = (tmp.compareToIgnoreCase(ref) == 0);
    	}
    	return result;
    }

}
