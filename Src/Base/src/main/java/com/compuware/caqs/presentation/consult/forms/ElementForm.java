package com.compuware.caqs.presentation.consult.forms;

/**
 *
 *
 */

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.struts.Globals;

import com.compuware.caqs.constants.Constants;
import com.compuware.caqs.presentation.util.StringFormatUtil;
import com.compuware.toolbox.util.logging.LoggerManager;

public class ElementForm extends DefinitionForm  {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7616358841928749401L;

	private static org.apache.log4j.Logger mLog = LoggerManager.getLogger(Constants.LOG4J_IHM_LOGGER_KEY);
    
	private String 	type;
	private String 	stereotype;
	private boolean	hasSource;
	
    /**
	 * @return Returns the stereotype.
	 */
	public String getStereotype() {
		return stereotype;
	}

	/**
	 * @param stereotype The stereotype to set.
	 */
	public void setStereotype(String stereotype) {
		this.stereotype = stereotype;
	}

	public String getEscapedType(boolean quote, boolean bracket, boolean doubleQuote) {
    	return StringFormatUtil.escapeString(this.getType(), quote, bracket, doubleQuote);
    }

	/**
	 * @return Returns the type.
	 */
	public String getType() {
		return type;
	}



	/**
	 * @param type The type to set.
	 */
	public void setType(String type) {
		this.type = type;
	}



	public Locale getLocale(HttpServletRequest request) {

	    HttpSession session = request.getSession();
	    Locale locale = (Locale) session.getAttribute(Globals.LOCALE_KEY);
	    if (locale == null) {
	        locale = Locale.getDefault();
	    }
	    return locale;
    }

	public boolean hasSource() {
		return hasSource;
	}

	public void setHasSource(boolean hasSource) {
		this.hasSource = hasSource;
	}

}
