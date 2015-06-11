/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.compuware.caqs.security.ldap.impl;

import com.compuware.caqs.security.userdetails.CommonLdapUserDetails;
import com.compuware.caqs.security.userdetails.CommonUserDetails;
import org.springframework.ldap.core.DirContextAdapter;
import org.springframework.ldap.core.DirContextOperations;
import org.springframework.security.GrantedAuthority;
import org.springframework.security.userdetails.UserDetails;
import org.springframework.security.userdetails.ldap.UserDetailsContextMapper;

/**
 * User details context manager to extract additional user information.
 * @author cwfr-fdubois
 */
public class ExtensibleUserDetailsContextMapper implements UserDetailsContextMapper {

    /** Creates a fully populated UserDetails object for use by the security framework.
     * @param ctx the context object which contains the user information.
     * @param username the user's supplied login name.
     * @param authority the list of authorities which the user should be given.
     * @return the user object.
     */
    public UserDetails mapUserFromContext(DirContextOperations ctx, String username, GrantedAuthority[] authority) {
        CommonLdapUserDetails result = new CommonLdapUserDetails();
        result.setLastname(ctx.getStringAttribute("sn"));
        result.setFirstname(ctx.getStringAttribute("givenname"));
        result.setEmail(ctx.getStringAttribute("mail"));
        return result;
    }

    /** Reverse of the above operation. Populates a context object from the supplied user object. Called when saving a user, for example.
     * @param user the user object.
     * @param ctx the context object which contains the user information.
     */
    public void mapUserToContext(UserDetails user, DirContextAdapter ctx) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
