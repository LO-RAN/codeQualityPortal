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
public abstract class AbstractUserRoleTag extends BodyTagSupport {

	private String[] roles;
	
	/**
	 * @param roles The roles to set.
	 */
	public void setRoles(String roles) {
		if (roles != null && roles.length() > 0) {
			this.roles = roles.split(",");
		}
	}

	public int doStartTag() throws JspTagException {
		return EVAL_BODY_BUFFERED;
	}
	
	public int doAfterBody() throws JspTagException {
		return SKIP_BODY;
	}
	
	public int doEndTag() throws JspTagException {
    	HttpSession session = pageContext.getSession();
        if (session != null) {
            Users user = (Users)session.getAttribute(RequestUtil.USER_SESSION_ATTRIBUTE_NAME);
            if (user != null && this.roles != null) {
            	if (evalBody(user)) {
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
	
	protected boolean isUserInRole(Users user) {
		boolean result = false;
		if (user != null) {
			result = user.isInRole(roles);
		}
		return result;
	}
	
	protected abstract boolean evalBody(Users user);
	
}
