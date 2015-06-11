/**
 * 
 */
package com.compuware.caqs.presentation.common.tag;

import java.io.IOException;

import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.BodyContent;
import javax.servlet.jsp.tagext.BodyTagSupport;

import com.compuware.caqs.presentation.util.RequestUtil;
import com.compuware.caqs.security.auth.Users;

/**
 * @author cwfr-fdubois
 *
 */
public abstract class AbstractAccessTag extends BodyTagSupport {

	private String function;
	
	/**
	 * @param function The function to set.
	 */
	public void setFunction(String function) {
		this.function = function;
	}

	public int doStartTag() throws JspTagException {
		int result = SKIP_BODY;
    	HttpSession session = pageContext.getSession();
        if (session != null) {
            Users user = (Users)session.getAttribute(RequestUtil.USER_SESSION_ATTRIBUTE_NAME);
            if (user != null && this.function != null) {
            	if (evalBody(this.function, user)) {
            		result = EVAL_BODY_BUFFERED;
            	}
            }
    	}
		return result;
	}
	
	public int doAfterBody() throws JspTagException {
		return SKIP_BODY;
	}
	
	public int doEndTag() throws JspTagException {
    	HttpSession session = pageContext.getSession();
        if (session != null) {
            Users user = (Users)session.getAttribute(RequestUtil.USER_SESSION_ATTRIBUTE_NAME);
            if (user != null && this.function != null) {
            	if (evalBody(this.function, user)) {
            		BodyContent bc = getBodyContent();
            		// get the bc as string
            		String content = bc.getString();
            		JspWriter writer = pageContext.getOut();
        			try {
						writer.append(content);
					}
        			catch (IOException e) {
						throw new JspTagException(e);
					}
            	}
            }
        }
		return EVAL_PAGE;
	}

	protected abstract boolean evalBody(String methodName, Users user);
	
}
