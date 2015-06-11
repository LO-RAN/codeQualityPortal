
package com.compuware.caqs.presentation.common.tag;

import java.io.InputStream;
import java.util.Properties;

import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.log4j.Logger;

import com.compuware.caqs.constants.Constants;

public final class CheckResources extends TagSupport {
    
    //logger
    static protected Logger logger = com.compuware.toolbox.util.logging.LoggerManager.getLogger(Constants.LOG4J_IHM_LOGGER_KEY);

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

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
            valid = getWebResources(session);
        }
        // Forward control based on the results
        if (valid) {
            retour = EVAL_PAGE;
        }
        return retour;
    }
    
    public void release() {
        super.release();
    }
    
    private boolean getWebResources(HttpSession session) {
        boolean result = false;
        Properties dbProps = (Properties)session.getAttribute("webResources");
        if (dbProps == null) {
            InputStream is = getClass().getResourceAsStream("/WebResources.properties");
            dbProps = new Properties();
            try {
                dbProps.load(is);
                session.setAttribute("webResources", dbProps);
                result = true;
            }
            catch (Exception e) {
                logger.error("CheckResources.getWebResources", e);
            }
            finally{
                try{
                    if (is != null) {
                        is.close();
                    }
                }
                catch(Exception e){
                    logger.error("CheckResources.getWebResources", e);
                }
            }
        }
        else {
            result = true;
        }
        return result;
    }
    
}
