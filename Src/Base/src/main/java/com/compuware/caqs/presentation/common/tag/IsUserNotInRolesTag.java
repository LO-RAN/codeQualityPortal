/**
 * 
 */
package com.compuware.caqs.presentation.common.tag;

import com.compuware.caqs.security.auth.Users;

/**
 * @author cwfr-fdubois
 *
 */
public class IsUserNotInRolesTag extends AbstractUserRoleTag {

	private static final long serialVersionUID = -1287363789646942323L;

	@Override
	protected boolean evalBody(Users user) {
		return !isUserInRole(user);
	}

}
