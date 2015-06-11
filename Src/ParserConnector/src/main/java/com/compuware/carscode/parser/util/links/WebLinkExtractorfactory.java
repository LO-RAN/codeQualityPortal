/**
 * 
 */
package com.compuware.carscode.parser.util.links;

import java.io.File;

/**
 * @author cwfr-fdubois
 *
 */
public class WebLinkExtractorfactory {

	private static WebLinkExtractorfactory singleton = new WebLinkExtractorfactory();
	
	private WebLinkExtractorfactory() {
	}

	public static WebLinkExtractorfactory getInstance() {
		return singleton;
	}
	
	public LinkExtractor getLinkExtractor(File f) {
		LinkExtractor result = null;
		if (f != null) {
			if (f.getName().equalsIgnoreCase("mappings.xml")) {
				result = new SunWafLinkExtractor(f);
			}
			else if (f.getName().startsWith("screendefinitions")) {
				result = new SunWafLinkExtractor(f);
			}
		}
		return result;
	}
	
}
