
package com.compuware.caqs.presentation.common.tag;

import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.log4j.Logger;

import com.compuware.caqs.constants.Constants;

public final class CheckSelectedElementTag extends TagSupport {
    
    /**
	 * 
	 */
	private static final long serialVersionUID = -4929102048562146257L;
	

	//logger
    static protected Logger logger = com.compuware.toolbox.util.logging.LoggerManager.getLogger(Constants.LOG4J_IHM_LOGGER_KEY);

    private static final String DEFAULT_TIMEOUT_PAGE_URL = "/sessiontimedout.jsp"; 
    
	/**
	* The page to which we should forward for the user to log on.
	*/
	private String page = DEFAULT_TIMEOUT_PAGE_URL;	
	
	/**
     * Defer the check for a logged-in user until the end of this tag is encountered.
     *
     * @exception JspException if a JSP exception has occurred
     */
    public int doStartTag() throws JspException {
        return SKIP_BODY;
    }
    
    
    public int doEndTag() throws JspException {
        int retour = SKIP_PAGE;
        // Is there a valid user logged on?
        boolean valid = false;
        HttpSession session = pageContext.getSession();
        if (session != null) {
            valid = checkElement(session);
        }
        // Forward control based on the results
        if (valid) {
            retour = EVAL_PAGE;
        }
        else {
        	try {
        		pageContext.forward(page);
    		}
        	catch (Exception e) {
    			throw new JspException(e.toString());
    		}
        }
        return retour;
    }
    
    public void release() {
        super.release();
        this.page = DEFAULT_TIMEOUT_PAGE_URL;
    }
    
    /**
	 * @return the page
	 */
	public String getPage() {
		return page;
	}

	/**
	 * @param page the page to set
	 */
	public void setPage(String page) {
		this.page = page;
	}

	private boolean checkElement(HttpSession session) {
        return session.getAttribute(Constants.CAQS_CURRENT_ELEMENT_SESSION_KEY) != null;
    }
    
}
