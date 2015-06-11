/**
 * 
 */
package com.compuware.carscode.parser.util.links;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.compuware.carscode.parser.util.FileUtil;

/**
 * @author cwfr-fdubois
 *
 */
public class SunWafLinkExtractor extends LinkExtractor {

	private static final Pattern MAPPINGS_EXTRACT_PATTERN = Pattern.compile(
			"<url-mapping[\\s]+url=\"([^\"]+)\"[\\s]+screen=\"([^\"]+)\"[\\s]*>"
			+ ".*?<web-action-class>([^<]+)</web-action-class>", Pattern.DOTALL | Pattern.CASE_INSENSITIVE);

	private static final Pattern SCREEN_DEFINITION_PATTERN = 
		Pattern.compile("<screen[\\s]+name=\"([^\"]+)\">(.*?)</screen>", Pattern.DOTALL | Pattern.CASE_INSENSITIVE);

	private static final Pattern SCREEN_DEFINITION_PARAMETER_PATTERN = 
		Pattern.compile("<parameter[\\s]+key=\"([^\"]+)\"[\\s]+value=\"([/a-zA-Z0-9-_\\.]+)\"[\\s]*/>", Pattern.DOTALL | Pattern.CASE_INSENSITIVE);
	
	public SunWafLinkExtractor(File configFile) {
		super(configFile);
	}

	/* (non-Javadoc)
	 * @see com.compuware.carscode.parser.util.links.LinkExtractor#extractMapping()
	 */
	public Map extractMapping() throws IOException {
		Map result = null;
		if (this.configFile != null) {
			if (this.configFile.getName().equalsIgnoreCase("mappings.xml")) {
				result = extractFromMappingsFile();
			}
			else {
				result = extractFromScreenDefinitionFile();
			}
		}
		return result;
	}
	
	private Map extractFromMappingsFile() throws IOException {
		Map result = new HashMap();
		String content = FileUtil.extractContent(this.configFile);
		Matcher m = MAPPINGS_EXTRACT_PATTERN.matcher(content);
		while(m.find()) {
			result.put(m.group(1), m.group(3));
		}
		return result;
	}

	private Map extractFromScreenDefinitionFile() throws IOException {
		Map result = new HashMap();
		String content = FileUtil.extractContent(this.configFile);
		Matcher m = SCREEN_DEFINITION_PATTERN.matcher(content);
		while(m.find()) {
			String screenName = m.group(1) + ".screen";
			result.put(screenName, extractFromScreenParameters(m.group(2)));
		}
		return result;
	}
	
	private List extractFromScreenParameters(String screenContent) {
		List result = new ArrayList();
		if (screenContent != null) {
			Matcher m = SCREEN_DEFINITION_PARAMETER_PATTERN.matcher(screenContent);
			while(m.find()) {
				result.add(getName(m.group(2)));
			}
		}
		return result;
	}
	
    protected String getName(String source) {
    	String result = null;
    	if (source != null) {
    		int startIdx = 0;
    		if (source.startsWith("/")) {
    			startIdx = 1;
    		}
    		result = source.substring(startIdx, source.lastIndexOf('.'));
            result = result.replaceAll("\\\\", "/");
            result = result.replaceAll("/", ".");
    		result += '_' + source.substring(source.lastIndexOf('.') + 1);
    	}
    	return result;
    }
    
	/* (non-Javadoc)
	 * @see com.compuware.carscode.parser.util.links.LinkExtractor#extractLinks()
	 */
	public Map extractLinks() throws IOException {
		Map result = new HashMap();
		if (this.configFile != null) {
			if (this.configFile.getName().equalsIgnoreCase("mappings.xml")) {
				String content = FileUtil.extractContent(this.configFile);
				Matcher m = MAPPINGS_EXTRACT_PATTERN.matcher(content);
				while(m.find()) {
					result.put(m.group(3), m.group(2));
				}
			}
		}
		return result;
	}
	
}
