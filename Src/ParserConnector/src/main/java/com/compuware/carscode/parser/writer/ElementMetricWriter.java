/**
 * 
 */
package com.compuware.carscode.parser.writer;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Map;

/**
 * @author cwfr-fdubois
 *
 */
public interface ElementMetricWriter {

	public void print(Map data, File out) throws IOException;
	
	public void print(Collection data, File out) throws IOException;
}
