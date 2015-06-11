package com.compuware.caqs.presentation.consult.forms;

/**
 *
 *
 */

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.struts.Globals;
import org.apache.struts.action.ActionForm;

import com.compuware.caqs.constants.Constants;
import com.compuware.caqs.presentation.util.StringFormatUtil;
import com.compuware.toolbox.util.logging.LoggerManager;

public class DefinitionForm extends ActionForm  {

	private static final long serialVersionUID = -4424932626290425592L;

	private static org.apache.log4j.Logger mLog = LoggerManager.getLogger(Constants.LOG4J_IHM_LOGGER_KEY);
    
	private String id;
	private String lib;
	private String desc;
	
	/**
	 * @return Returns the id.
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id The id to set.
	 */
	public void setId(String id) {
		this.id = id;
	}

	public String getEscapedLib(boolean quote, boolean bracket, boolean doubleQuote) {
    	return StringFormatUtil.escapeString(this.getLib(), quote, bracket, doubleQuote);
    }

	/**
	 * @return Returns the lib.
	 */
	public String getLib() {
		return lib;
	}

	/**
	 * @param lib The lib to set.
	 */
	public void setLib(String lib) {
		this.lib = lib;
	}

	public String getEscapedDesc(boolean quote, boolean bracket, boolean doubleQuote) {
    	return StringFormatUtil.escapeString(this.getDesc(), quote, bracket, doubleQuote);
    }

    /**
	 * @return Returns the desc.
	 */
	public String getDesc() {
		return desc;
	}

	/**
	 * @param desc The desc to set.
	 */
	public void setDesc(String desc) {
		this.desc = desc;
	}

	public Locale getLocale(HttpServletRequest request) {

	    HttpSession session = request.getSession();
	    Locale locale = (Locale) session.getAttribute(Globals.LOCALE_KEY);
	    if (locale == null) {
	        locale = Locale.getDefault();
	    }
	    return locale;
    }

}
