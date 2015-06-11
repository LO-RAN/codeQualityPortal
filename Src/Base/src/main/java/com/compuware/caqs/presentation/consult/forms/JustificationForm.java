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

public class JustificationForm extends DefinitionForm  {

	private static org.apache.log4j.Logger mLog = LoggerManager.getLogger(Constants.LOG4J_IHM_LOGGER_KEY);
    
	private String status;
	private String note;
	


	/**
	 * @return Returns the note.
	 */
	public String getNote() {
		return note;
	}



	/**
	 * @param noteJust The note to set.
	 */
	public void setNote(String note) {
		if (note != null) {
			this.note = note;
		}
		else {
			this.note = "0.0";
		}
	}

	/**
	 * @param noteJust The note to set.
	 */
	public void setNote(double note) {
		this.note = StringFormatUtil.decimalFormat(note);
	}

	/**
	 * @return Returns the status.
	 */
	public String getStatus() {
		return status;
	}



	/**
	 * @param status The status to set.
	 */
	public void setStatus(String status) {
		this.status = status;
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
