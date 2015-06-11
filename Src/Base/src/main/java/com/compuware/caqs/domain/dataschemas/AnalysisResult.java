/**
 * 
 */
package com.compuware.caqs.domain.dataschemas;

import java.util.HashMap;
import java.util.Map;

/**
 * @author cwfr-fdubois
 *
 */
public class AnalysisResult {

	private boolean success = false;
	
	private Map<String, String> paramMap = null;
	
	private String message = null;

	/**
	 * @return Returns the message.
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * @param message The message to set.
	 */
	public void setMessage(String message) {
		this.message = message;
	}

	/**
	 * @return Returns the paramMap.
	 */
	public Map<String, String> getParamMap() {
		return paramMap;
	}

	/**
	 * @param paramMap The paramMap to set.
	 */
	public void setParamMap(Map<String, String> paramMap) {
		this.paramMap = paramMap;
	}
	
	/**
	 * Add a new parameter to the map.
	 * @param key the parameter map.
	 * @param value the parameter value.
	 */
	public void addParam(String key, String value) {
		if (this.paramMap == null) {
			this.paramMap = new HashMap<String, String>();
		}
		this.paramMap.put(key, value);
	}

	/**
	 * @return Returns the success.
	 */
	public boolean isSuccess() {
		return success;
	}

	/**
	 * @param success The success to set.
	 */
	public void setSuccess(boolean success) {
		this.success = success;
	}
	
	
}
