/**
 * 
 */
package com.compuware.caqs.domain.dataschemas.formatter;

import java.io.PrintWriter;
import java.util.Collection;
import java.util.Locale;

import org.apache.struts.util.MessageResources;

/**
 * @author cwfr-fdubois
 *
 */
public interface Formatter {

	/**
	 * Format the given collection.
	 * @param c a collection of objects to format.
	 * @param writer the output writer.
	 */
	public abstract void format(Collection c, PrintWriter writer, MessageResources resources, Locale loc);
	
	/**
	 * Format the given object.
	 * @param o the object to format.
	 * @param writer the output writer.
	 */
	public abstract void format(Object o, PrintWriter writer, MessageResources resources, Locale loc);
	
}
