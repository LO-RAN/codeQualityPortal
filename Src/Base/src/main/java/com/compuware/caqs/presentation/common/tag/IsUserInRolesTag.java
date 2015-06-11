/**
 * 
 */
package com.compuware.caqs.presentation.common.tag;

import com.compuware.caqs.security.auth.Users;

/**
 * @author cwfr-fdubois
 *
 */
public class IsUserInRolesTag extends AbstractUserRoleTag {

	private static final long serialVersionUID = 6236509798745258815L;

	@Override
	protected boolean evalBody(Users user) {
		return isUserInRole(user);
	}

}
