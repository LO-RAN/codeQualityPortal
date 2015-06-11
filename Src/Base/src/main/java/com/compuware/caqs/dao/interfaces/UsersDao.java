package com.compuware.caqs.dao.interfaces;

import com.compuware.caqs.domain.dataschemas.DefinitionBean;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.compuware.caqs.domain.dataschemas.UserBean;
import com.compuware.caqs.domain.dataschemas.rights.RightBean;
import com.compuware.caqs.domain.dataschemas.rights.RoleBean;
import com.compuware.caqs.exception.DataAccessException;

public interface UsersDao {

	public abstract List<UserBean> retrieveAllUsersByElementId(String idElt);

	public abstract List<UserBean> retrieveAllUsersByFatherElementId(String idElt);

	public abstract void deleteRights(String idElt);

	/** Positionne les droits des utilisateurs sp�cifi� � l'element choisi
	 * @param idElt Id de l'element
	 * @param userList Liste d'utilisateurs
	 */
	public abstract void addRights(String idElt, String[] userCollection);

    /** Determine si l'element est visible ou non selon les droits de l'utilisateur
     * @param id_elt Id de l'element � tester
     * @return Vrai si l'utilisateur � le droit de voir l'element, Faux sinon
     */    
     public boolean isEltVisible(String idElt, String idUser, String idBaseline);
	
     /**
      * Determine si l'element est visible ou non selon les droits de l'utilisateur
      *
      * @param id_elt Id de l'element � tester
      * @param idUser Id de l'utilisateur � verifier
      * @return Vrai si l'utilisateur � le droit de voir l'element, Faux sinon
      */
     public boolean isEltVisible(String idElt, String idUser);

     /**
      * Retrieve all access rights for given groups.
      * @param groups a collection of group id.
      * @return the access right map.
      * @throws DataAccessException
      */
     public Map<String, String> retrieveUserAccessRights(Collection<String> groups) throws DataAccessException;
     
     /**
      * Retrieve the caqs role from given unifaceview groups.
      * @param groups a collection of group id.
      * @return the caqs role from given unifaceview groups.
      * @throws DataAccessException
      */
     public Map<String, String> retrieveRoleFromGroup(Collection<String> groups) throws DataAccessException;
 
     /**
      * Retrieve all access rights
     * @return a list containing all access rights.
     */
    public List<RightBean> getAllAccessRights();
    
    /**
     * Retrieve all roles defined in caqs database
     * @return a list containing all caqs roles
     */
    public List<RoleBean> getAllCaqsRoles();
    
    /**
     * Retrieve the accredidated roles for a right (functionnality)
     * @param rb the functionnality for which we want the accredidated roles
     * @return a list of roles which can access a functionnality
     */
    public List<RoleBean> getAllCaqsRolesWhichCanAccess(RightBean rb);
    
    /**
     * Tests if a role is associated to a right
     * @param role the role to test
     * @param right the right to test
     * @return true if a role has accreditation to the right, false otherwise
     */
    public boolean isAssociated(RoleBean role, RightBean right);

    /**
     * save right change for a role
     * @param rightId right id
     * @param roleId role id
     * @param newValue new right for the role
     */
    public void saveRightsChange(String rightId, String roleId, boolean newValue);

    public List<UserBean> retrieveUsersByIdLastname(String id, String lastname);

    public UserBean retrieveUserById(String id);

    public void saveUserBean(UserBean ub) throws DataAccessException;

    public void deleteUserBean(String id) throws DataAccessException;

    public List<UserBean> retrieveAllUsers();

    public void udpateLastLoginTime(String userId) throws DataAccessException;

       /**
     * Retrieve all roles assigned to user
     * @return a list containing roles
     */
 public List<RoleBean> getUserRoles(String userId);

 public void saveRoles(String userId, List<RoleBean> roles);

 public void deleteUsersRoles(String userId);

}