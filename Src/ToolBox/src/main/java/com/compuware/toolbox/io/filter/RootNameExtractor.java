/*
 * RootNameExtractor.java
 *
 * Created on 11 mars 2004, 18:18
 */

package com.compuware.toolbox.io.filter;

import java.util.Collection;
import java.util.Iterator;

/**
 * Library for string part extraction.
 * @author  cwfr-fdubois
 */
public class RootNameExtractor {
    
    /** Creates a new instance of RootNameExtractor */
    public RootNameExtractor() {
    }

    /** Retrieve the substring of the given name with the given size.
     * @param name the given name.
     * @param size the size of the substring.
     * @return the substring of the given name and the given size.
     */    
    public static String getRootName(String name, int size) {
        String result = null;
        if (name != null && name.length() >= size) {
            result = name.substring(0, size);
        }
        return result;
    }
    
    /** Retrieve the substring of the given name with the given size from the given starting character index.
     * @param name the given name.
     * @param size the size of the substring.
     * @param start the starting character index.
     * @return the substring of the given name with the given size from the given starting character index.
     */    
    public static String getRootName(String name, int size, int start) {
        String result = null;
        if (name != null && name.length() <= (start+size)) {
            result = name.substring(start, size-1);
        }
        return result;
    }
    
    /**
     * Return the root name of a given string according to a given end the string should have.
     * @param name the string to analyse
     * @param end the end the string to analyse should have
     * @return the root name corresponding to the name minus the end if possible, null
     * otherwise.
     */    
    public static String getRootName(String name, String end) {
        String result = null;
        if (name != null && end != null && name.endsWith(end)) {
            result = name.substring(0, name.lastIndexOf(end));
        }
        return result;
    }
    
    /**
     * Return the root name of a given string according to a given end the string should have.
     * @param name the string to analyse
     * @param ends the end collection the string to analyse could have
     * @return the root name corresponding to the name minus the end found, null
     * otherwise.
     */    
    public static String getRootNameFromEndCollection(String name, Collection ends) {
        String result = null;
        if (ends != null) {
            Iterator i = ends.iterator();
            while (i.hasNext() && result == null) {
                String end = (String)i.next();
                result = RootNameExtractor.getRootName(name, end);
            }
        }
        return result;
    }
    
}
