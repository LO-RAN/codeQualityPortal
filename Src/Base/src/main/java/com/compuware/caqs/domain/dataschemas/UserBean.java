/*
 * ElementBean.java
 *
 * Created on 1 d�cembre 2003, 10:40
 */
package com.compuware.caqs.domain.dataschemas;

//import com.compuware.util.logging.LoggerManager;
import com.compuware.caqs.domain.dataschemas.rights.RoleBean;
import java.io.Serializable;
import java.util.List;

/**
 * Contains User informations.
 * @author  cwfr-fdubois
 */
public class UserBean extends DefinitionBean implements Serializable, Comparable {

    /**
     *
     */
    private static final long serialVersionUID = 2768444158104342760L;
    private String firstName = null;
    private String lastName = null;
    private String email = null;
    private List<RoleBean> roles = null;
    private String password = null;

    /** Constructor. */
    public UserBean() {
    }

    /** Constructor.
     * @param id the user ID.
     */
    public UserBean(String id) {
        this.id = id;
    }

    /** Indicates whether some other user bean is "equal to" this one.
     * Overwrite the Object.equals(Object obj) method.
     * @param usr another user bean.
     * @return true if this object is the same as the usr argument; false otherwise.
     */
    @Override
    public boolean equals(Object usr) {
        boolean result = false;
        if (usr != null && usr instanceof UserBean && this.id != null) {
            result = id.equals(((UserBean) usr).getId());
        }
        return result;
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    public int compareTo(Object usr) {
        int result = 0;
        if (usr != null) {
            if (this.lib != null) {
                result = this.lib.compareTo(((UserBean) usr).getLib());
            } else if (this.id != null) {
                result = this.id.compareTo(((UserBean) usr).getId());
            }
        }
        return result;
    }

    /**
     * @return Returns the firstName.
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * @param firstName The firstName to set.
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * @return Returns the lastName.
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * @param lastName The lastName to set.
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }


    public void setEmail(String email) {
        this.email = email;
    }

    public void setRoles(List<RoleBean> roles) {
        this.roles = roles;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public List<RoleBean> getRoles() {
        return roles;
    }

    public String getPassword() {
        return password;
    }
}
