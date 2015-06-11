/**
 * 
 */
package com.compuware.carscode.parser.util.links;

import java.io.File;
import java.io.IOException;
import java.util.Map;

/**
 * @author cwfr-fdubois
 *
 */
public abstract class LinkExtractor {
	
	File configFile = null;
	
	public LinkExtractor(File configFile) {
		this.configFile = configFile; 
	}

	public abstract Map extractMapping() throws IOException;
	
	public abstract Map extractLinks() throws IOException;
	
}
