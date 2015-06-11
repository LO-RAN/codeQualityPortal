/**
 * 
 */
package com.compuware.caqs.presentation.common.tag;

import com.compuware.caqs.security.auth.Users;

/**
 * @author cwfr-fdubois
 *
 */
public class NotAccessTag extends AbstractAccessTag {

	private static final long serialVersionUID = 1597297304646552587L;

	@Override
	protected boolean evalBody(String methodName, Users user) {
		boolean result = false;
		if (user != null) {
			result = !user.access(methodName);
		}
		return result;
	}
	
}
