/**
 * 
 */
package com.compuware.caqs.presentation.common.tag;

import com.compuware.caqs.security.auth.Users;

/**
 * @author cwfr-fdubois
 *
 */
public class AccessTag extends AbstractAccessTag {

	private static final long serialVersionUID = -7389841640936510057L;

	@Override
	protected boolean evalBody(String methodName, Users user) {
		boolean result = false;
		if (user != null) {
			result = user.access(methodName);
		}
		return result;
	}
	
}
