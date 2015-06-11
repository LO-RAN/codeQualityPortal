/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.compuware.caqs.security.ldap.impl;

import java.util.HashSet;
import java.util.Set;

import org.springframework.ldap.core.ContextSource;
import org.springframework.ldap.core.DirContextOperations;
import org.springframework.security.GrantedAuthority;
import org.springframework.security.ldap.LdapAuthoritiesPopulator;
import org.springframework.security.ldap.populator.DefaultLdapAuthoritiesPopulator;
import org.springframework.security.userdetails.UserDetails;
import org.springframework.security.userdetails.UserDetailsService;

/**
 * Class used to populate LDAP authorities from database.
 * The default behaviour is full LDAP.
 * This allows mixed authentication :
 *     - LDAP for user/password authentication
 *     - JDBC for role extract
 *
 * @author cwfr-fdubois
 */
public class JdbcAuthoritiesPopulator extends DefaultLdapAuthoritiesPopulator implements LdapAuthoritiesPopulator {

    /**
     * The user service used to extract user/role data.
     */
	private UserDetailsService userService;

	/**
     * Constructor
	 * @param contextSource the LDAP context source.
	 * @param groupSearchBase the group search base.
	 */
	public JdbcAuthoritiesPopulator(ContextSource contextSource, String groupSearchBase) {
		super(contextSource, groupSearchBase);
	}

    /**
     * Get additional roles from user service.
     * @param user the user context.
     * @param username the username.
     * @return a set of found roles.
     */
    @SuppressWarnings("unchecked")
	@Override
	protected Set getAdditionalRoles(DirContextOperations user, String username) {
		Set<GrantedAuthority> userPerms = new HashSet<GrantedAuthority>();

		UserDetails userDetails = userService.loadUserByUsername(username);

		for (GrantedAuthority role : userDetails.getAuthorities()) {
				userPerms.add(role);
		}

		return userPerms;
	}

    /**
     * Setter for the user service.
     * @param userService the user service used to extract user/role data.
     */
    public void setUserService(UserDetailsService userService) {
        this.userService = userService;
    }

}
