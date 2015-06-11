
package com.compuware.caqs.security.auth.tag;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTagSupport;

import org.apache.log4j.Logger;

import com.compuware.caqs.constants.Constants;
import com.compuware.caqs.dao.factory.DaoFactory;
import com.compuware.caqs.dao.interfaces.UsersDao;
import com.compuware.caqs.presentation.util.RequestUtil;
import com.compuware.caqs.security.auth.Users;

public final class AuthenticationTag extends BodyTagSupport {
    
	private static final long serialVersionUID = -1294644956674181691L;

	//logger
    static protected Logger logger = com.compuware.toolbox.util.logging.LoggerManager.getLogger(Constants.LOG4J_IHM_LOGGER_KEY);

    private String idelt;
    private String idbline;
    
	/**
	 * @return Returns the idbline.
	 */
	public String getIdbline() {
		return idbline;
	}


	/**
	 * @param idbline The idbline to set.
	 */
	public void setIdbline(String idbline) {
		this.idbline = idbline;
	}


	/**
	 * @return Returns the idelt.
	 */
	public String getIdelt() {
		return idelt;
	}


	/**
	 * @param idelt The idelt to set.
	 */
	public void setIdelt(String idelt) {
		this.idelt = idelt;
	}

	/*
	public int doStartTag() throws JspException {
		return EVAL_BODY_BUFFERED;
	}
	*/

    public int doEndTag() throws JspException {
    	int result = EVAL_PAGE;
    	boolean isEltVisible = false;
    	
    	HttpSession session = pageContext.getSession();
        if (session != null) {
            Users user = (Users)session.getAttribute(RequestUtil.USER_SESSION_ATTRIBUTE_NAME);
            if (user != null) {
             	DaoFactory daoFactory = DaoFactory.getInstance();
            	UsersDao usersDao = daoFactory.getUsersDao();
            	isEltVisible = usersDao.isEltVisible(getIdelt(), user.getId(), getIdbline());
            }
        }
        
        if (!isEltVisible) {
        	ServletRequest pReq = pageContext.getRequest();
        	ServletResponse pRes = pageContext.getResponse();
         	try {
				pageContext.getServletContext().getRequestDispatcher("/noaccess.jsp" ).forward(pReq,pRes);
			} catch (ServletException e) {
				logger.error("Error during noaccess forward", e);
			} catch (IOException e) {
				logger.error("Error during noaccess forward", e);
			}
        	result = SKIP_PAGE; 
        }

        return result;
    }
    
    public void release() {
        super.release();
    }
    
}
