/*
 * Class.java
 *
 * Created on 23 janvier 2004, 12:22
 */
package com.compuware.caqs.dao.dbms;

// Imports for DBMS transactions.
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.compuware.caqs.constants.Constants;
import com.compuware.caqs.dao.interfaces.UsersDao;
import com.compuware.caqs.dao.util.QueryUtil;
import com.compuware.caqs.domain.dataschemas.UserBean;
import com.compuware.caqs.domain.dataschemas.rights.RightBean;
import com.compuware.caqs.domain.dataschemas.rights.RoleBean;
import com.compuware.caqs.exception.DataAccessException;
import com.compuware.toolbox.dbms.JdbcDAOUtils;
import com.compuware.toolbox.util.logging.LoggerManager;

/**
 *
 * @author  cwfr-fdubois
 */
public class UsersDbmsDao implements UsersDao {

    private static org.apache.log4j.Logger logger = LoggerManager.getLogger(Constants.LOG4J_DBMS_LOGGER_KEY);
    private static final String USERS_BY_ELEMENT_REQUEST = "SELECT ID_PROFIL_USER FROM DROITS WHERE ID_ELT=? ORDER BY ID_PROFIL_USER";
    private static final String USERS_BY_FATHER_ELEMENT_REQUEST = "SELECT ID_PROFIL_USER FROM DROITS, ELT_LINKS WHERE ID_ELT=ELT_PERE AND ELT_FILS=? ORDER BY ID_PROFIL_USER";
    private static UsersDao singleton = new UsersDbmsDao();

    public static UsersDao getInstance() {
        return UsersDbmsDao.singleton;
    }

    /** Creates a new instance of Class */
    private UsersDbmsDao() {
    }

    /** {@inheritDoc}
     */
    public List<UserBean> retrieveAllUsersByElementId(String idElt) {
        List<UserBean> result = new ArrayList<UserBean>();
        Connection connection = JdbcDAOUtils.getConnection(this, Constants.CAQS_DATASOURCE_KEY);
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = connection.prepareStatement(USERS_BY_ELEMENT_REQUEST);
            pstmt.setString(1, idElt);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                UserBean newUser = new UserBean();
                newUser.setId(rs.getString("ID_PROFIL_USER"));
                result.add(newUser);
            }
        } catch (SQLException e) {
            logger.error(e);
        } finally {
            JdbcDAOUtils.closeResultSet(rs);
            JdbcDAOUtils.closePrepareStatement(pstmt);
            JdbcDAOUtils.closeConnection(connection);
        }
        return result;
    }

    /** {@inheritDoc}
     */
    public List<UserBean> retrieveAllUsersByFatherElementId(String idElt) {
        List<UserBean> result = new ArrayList<UserBean>();
        Connection connection = JdbcDAOUtils.getConnection(this, Constants.CAQS_DATASOURCE_KEY);
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = connection.prepareStatement(USERS_BY_FATHER_ELEMENT_REQUEST);
            pstmt.setString(1, idElt);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                UserBean newUser = new UserBean();
                newUser.setId(rs.getString("ID_PROFIL_USER"));
                result.add(newUser);
            }
        } catch (SQLException e) {
            logger.error(e);
        } finally {
            JdbcDAOUtils.closeResultSet(rs);
            JdbcDAOUtils.closePrepareStatement(pstmt);
            JdbcDAOUtils.closeConnection(connection);
        }
        return result;
    }

    /** {@inheritDoc}
     */
    public void deleteRights(String idElt) {
        String deleteRights = "DELETE FROM DROITS WHERE ID_ELT=?";
        Connection connection = JdbcDAOUtils.getConnection(this, Constants.CAQS_DATASOURCE_KEY);
        PreparedStatement pstmt = null;
        try {
            pstmt = connection.prepareStatement(deleteRights);
            pstmt.setString(1, idElt);
            pstmt.executeUpdate();
            JdbcDAOUtils.commit(connection);
        } catch (SQLException e) {
            logger.error("Error deleting user rights", e);
            logger.error("Aucun Droit supprim�");
        } finally {
            JdbcDAOUtils.closePrepareStatement(pstmt);
            JdbcDAOUtils.closeConnection(connection);
        }

    }

    /** {@inheritDoc}
     */
    public void addRights(String idElt, String[] userCollection) {
        String insertRights = "INSERT INTO DROITS (ID_ELT,ID_PROFIL_USER,TYPE_ACCES) VALUES (?,?,'U')";
        Connection connection = JdbcDAOUtils.getConnection(this, Constants.CAQS_DATASOURCE_KEY);
        PreparedStatement pstmt = null;
        try {
            pstmt = connection.prepareStatement(insertRights);
            for (int i = 0; i < userCollection.length; i++) {
                String idUser = userCollection[i];
                logger.debug("USER:" + idUser);
                if (idUser != null && idUser.length() > 0) {
                    pstmt.setString(1, idElt);
                    pstmt.setString(2, idUser.trim());
                    pstmt.executeUpdate();
                }
            }
            JdbcDAOUtils.commit(connection);
        } catch (SQLException e) {
            logger.error("Error inserting user rights", e);
            logger.error("Aucun Droit ajout�");
        } finally {
            JdbcDAOUtils.closePrepareStatement(pstmt);
            JdbcDAOUtils.closeConnection(connection);
        }

    }

    /** {@inheritDoc}
     */
    public boolean isEltVisible(String idElt, String idUser, String idBaseline) {
        boolean result = false;

        if (idBaseline == null) {
            result = isEltVisible(idElt, idUser);
        } else {
            String userRights = "SELECT ID_PROFIL_USER FROM DROITS WHERE ID_ELT=?";
            String dateBaselineRequest = "SELECT DINST_ELT, DINST_BLINE FROM ELEMENT, BASELINE WHERE BASELINE.PRO_BLINRE=ELEMENT.ID_PRO AND ID_ELT=? AND  id_bline=?";

            Connection connection = JdbcDAOUtils.getConnection(this, Constants.CAQS_DATASOURCE_KEY);
            PreparedStatement pstmt = null;
            ResultSet rs = null;

            boolean bVisible = false;
            boolean bDateOk = false;
            try {
                pstmt = connection.prepareStatement(userRights);
                pstmt.setString(1, idElt);
                rs = pstmt.executeQuery();
                while (rs.next()) {
                    String testString = rs.getString(1);
                    if (testString != null) {
                        testString = testString.trim();
                        if (testString.compareToIgnoreCase(idUser) == 0) {
                            bVisible = true;
                        }
                    }
                }
                JdbcDAOUtils.closeResultSet(rs);
                JdbcDAOUtils.closePrepareStatement(pstmt);

                pstmt = connection.prepareStatement(dateBaselineRequest);
                pstmt.setString(1, idElt);
                pstmt.setString(2, idBaseline);
                rs = pstmt.executeQuery();
                while (rs.next()) {
                    java.util.Date dateElt = rs.getDate(1);
                    java.util.Date dateBl = rs.getDate(2);
                    if (dateElt.compareTo(dateBl) <= 0) {
                        bDateOk = true;
                    }
                }

                result = bVisible && bDateOk;
            } catch (SQLException e) {
                logger.error(e);
                result = false;
            } finally {
                JdbcDAOUtils.closeResultSet(rs);
                JdbcDAOUtils.closePrepareStatement(pstmt);
                JdbcDAOUtils.closeConnection(connection);
            }
        }
        return result;
    }

    /** {@inheritDoc}
     */
    public boolean isEltVisible(String idElt, String idUser) {
        String userRights = "SELECT ID_PROFIL_USER FROM DROITS WHERE ID_ELT=?";
        boolean bVisible = false;
        PreparedStatement eltstmt = null;
        ResultSet rs = null;
        Connection conn = JdbcDAOUtils.getConnection(this, Constants.CAQS_DATASOURCE_KEY);
        try {
            eltstmt = conn.prepareStatement(userRights);
            eltstmt.setString(1, idElt);
            rs = eltstmt.executeQuery();
            while (rs.next() && !bVisible) {
                String testString = rs.getString(1);
                if (testString != null) {
                    testString = testString.trim();
                    //  System.out.println("NOT null ! on compare "+testString+" VS "+idUser+"/");
                    if (testString.compareToIgnoreCase(idUser) == 0) {
                        bVisible = true;
                    }
                }
            }
        } catch (SQLException e) {
            logger.error(e.toString());
            bVisible = false;
        } finally {
            JdbcDAOUtils.closeResultSet(rs);
            JdbcDAOUtils.closePrepareStatement(eltstmt);
            JdbcDAOUtils.closeConnection(conn);
        }
        return bVisible;
    }
    private static final String USER_ACCESS_RIGHTS_QUERY =
            "Select distinct ID_ACCESS"
            + " From CAQS_ACCESS_RIGHTS"
            + " Where ID_ROLE IN ";

    /** {@inheritDoc}
     */
    public Map<String, String> retrieveUserAccessRights(
            Collection<String> groups) throws DataAccessException {
        Map<String, String> result = new HashMap<String, String>();
        if (groups != null && groups.size() > 0) {
            PreparedStatement stmt = null;
            ResultSet rs = null;
            Connection connection = JdbcDAOUtils.getConnection(this, Constants.CAQS_DATASOURCE_KEY);
            try {
                // Pr�paration de la requ�te.
                QueryUtil queryUtil = QueryUtil.getInstance();
                stmt = connection.prepareStatement(USER_ACCESS_RIGHTS_QUERY
                        + queryUtil.getInClause(groups));
                // Ex�cution de la requ�te.
                rs = stmt.executeQuery();
                while (rs.next()) {
                    // Parcours du r�sultat.
                    result.put(rs.getString(1), rs.getString(1));
                }
            } catch (SQLException e) {
                logger.error(e.toString());
            } finally {
                // Fermeture du r�sultat et de la requ�te.
                JdbcDAOUtils.closeResultSet(rs);
                JdbcDAOUtils.closePrepareStatement(stmt);
                JdbcDAOUtils.closeConnection(connection);
            }
        }
        return result;
    }
    private static final String ROLE_GROUP_MAPPING_QUERY =
            "Select ID_ROLE" + " From CAQS_ROLE" + " Where ID_ROLE IN ";

    /** {@inheritDoc}
     */
    public Map<String, String> retrieveRoleFromGroup(
            Collection<String> groups) throws DataAccessException {
        Map<String, String> result = new HashMap<String, String>();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Connection connection = JdbcDAOUtils.getConnection(this, Constants.CAQS_DATASOURCE_KEY);
        try {
            // Pr�paration de la requ�te.
            QueryUtil queryUtil = QueryUtil.getInstance();
            stmt = connection.prepareStatement(ROLE_GROUP_MAPPING_QUERY
                    + queryUtil.getInClause(groups));
            // Ex�cution de la requ�te.
            rs = stmt.executeQuery();
            while (rs.next()) {
                // Parcours du r�sultat.
                result.put(rs.getString("ID_ROLE"), rs.getString("ID_ROLE"));
            }
        } catch (SQLException e) {
            logger.error(e.toString());
        } finally {
            // Fermeture du r�sultat et de la requ�te.
            JdbcDAOUtils.closeResultSet(rs);
            JdbcDAOUtils.closePrepareStatement(stmt);
            JdbcDAOUtils.closeConnection(connection);
        }
        return result;
    }
    private static final String GET_ALL_ACCESS_RIGHTS_QUERY =
            "SELECT id_access, lib_access "
            + " FROM CAQS_ACCESS_DEFINITION ";

    /** {@inheritDoc}
     */
    public List<RightBean> getAllAccessRights() {
        List<RightBean> retour = new ArrayList<RightBean>();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Connection connection = JdbcDAOUtils.getConnection(this, Constants.CAQS_DATASOURCE_KEY);
        try {
            stmt = connection.prepareStatement(GET_ALL_ACCESS_RIGHTS_QUERY);
            // Execution de la requete.
            rs = stmt.executeQuery();
            while (rs.next()) {
                RightBean rb = new RightBean(rs.getString("id_access"));
                rb.setLib(rs.getString("lib_access"));
                retour.add(rb);
            }
        } catch (SQLException e) {
            logger.error(e.toString());
        } finally {
            // Fermeture du r�sultat et de la requ�te.
            JdbcDAOUtils.closeResultSet(rs);
            JdbcDAOUtils.closePrepareStatement(stmt);
            JdbcDAOUtils.closeConnection(connection);
        }
        return retour;
    }
    private static final String GET_ALL_CAQS_ROLES_QUERY =
            "SELECT id_role, portal_role "
            + " FROM CAQS_ROLE ";

    /** {@inheritDoc}
     */
    public List<RoleBean> getAllCaqsRoles() {
        List<RoleBean> retour = new ArrayList<RoleBean>();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Connection connection = JdbcDAOUtils.getConnection(this, Constants.CAQS_DATASOURCE_KEY);
        try {
            stmt = connection.prepareStatement(GET_ALL_CAQS_ROLES_QUERY);
            // Ex�cution de la requ�te.
            rs = stmt.executeQuery();
            while (rs.next()) {
                RoleBean rb = new RoleBean();
                rb.setId(rs.getString("id_role"));
                rb.setLib(rs.getString("portal_role"));
                retour.add(rb);
            }
        } catch (SQLException e) {
            logger.error(e.toString());
        } finally {
            // Fermeture du r�sultat et de la requ�te.
            JdbcDAOUtils.closeResultSet(rs);
            JdbcDAOUtils.closePrepareStatement(stmt);
            JdbcDAOUtils.closeConnection(connection);
        }
        return retour;
    }
    private static final String GET_CAQS_ROLES_FOR_RIGHT_QUERY =
            "SELECT id_role, portal_role "
            + " FROM CAQS_ROLE "
            + " WHERE id_role IN ("
            + "   SELECT id_role "
            + "   FROM CAQS_ACCESS_RIGHTS"
            + "   WHERE id_access = ?)";

    /** {@inheritDoc}
     */
    public List<RoleBean> getAllCaqsRolesWhichCanAccess(RightBean rb) {
        List<RoleBean> retour = new ArrayList<RoleBean>();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Connection connection = JdbcDAOUtils.getConnection(this, Constants.CAQS_DATASOURCE_KEY);
        try {
            stmt = connection.prepareStatement(GET_CAQS_ROLES_FOR_RIGHT_QUERY);
            stmt.setString(1, rb.getId());
            // Ex�cution de la requ�te.
            rs = stmt.executeQuery();
            while (rs.next()) {
                RoleBean role = new RoleBean();
                role.setId(rs.getString("id_role"));
                role.setLib(rs.getString("portal_role"));
                retour.add(role);
            }
        } catch (SQLException e) {
            logger.error(e.toString());
        } finally {
            // Fermeture du r�sultat et de la requ�te.
            JdbcDAOUtils.closeResultSet(rs);
            JdbcDAOUtils.closePrepareStatement(stmt);
            JdbcDAOUtils.closeConnection(connection);
        }
        return retour;
    }

    private boolean isAssociated(String role, String right, Connection conn) {
        boolean retour = false;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            stmt = conn.prepareStatement(IS_ROLE_ASSOCIATED_TO_RIGHT_QUERY);
            stmt.setString(1, role);
            stmt.setString(2, right);
            // Ex�cution de la requ�te.
            rs = stmt.executeQuery();
            if (rs.next()) {
                int nb = rs.getInt("theCount");
                retour = (nb > 0);
            }
        } catch (SQLException e) {
            logger.error(e.toString());
        } finally {
            // Fermeture du r�sultat et de la requ�te.
            JdbcDAOUtils.closeResultSet(rs);
            JdbcDAOUtils.closePrepareStatement(stmt);
        }
        return retour;
    }
    // LI - 09/03/2009 16:07:11 - HSQLDB does not accept column alias being a keyword;
    //                            hence "count" has been renamed "theCount"
    private static final String IS_ROLE_ASSOCIATED_TO_RIGHT_QUERY =
            "SELECT count(id_role) as theCount"
            + " FROM CAQS_ACCESS_RIGHTS "
            + " WHERE id_role = ? AND id_access = ?";

    /** {@inheritDoc}
     */
    public boolean isAssociated(RoleBean role, RightBean right) {
        boolean retour = false;
        Connection connection = JdbcDAOUtils.getConnection(this, Constants.CAQS_DATASOURCE_KEY);
        retour = this.isAssociated(role.getId(), right.getId(), connection);
        JdbcDAOUtils.closeConnection(connection);
        return retour;
    }

    /** {@inheritDoc}
     */
    public void saveRightsChange(String rightId, String roleId, boolean newValue) {
        Connection connection = null;
        PreparedStatement pstmt = null;
        try {
            connection = JdbcDAOUtils.getConnection(this, Constants.CAQS_DATASOURCE_KEY);
            if (!newValue) {
                pstmt = connection.prepareStatement(DELETE_RIGHT_QUERY);
            } else {
                pstmt = connection.prepareStatement(ADD_RIGHT_QUERY);
            }
            pstmt.setString(1, rightId);
            pstmt.setString(2, roleId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            logger.error("Error during role change saving", e);
        } finally {
            JdbcDAOUtils.closeStatement(pstmt);
            JdbcDAOUtils.closeConnection(connection);
        }
    }
    private static final String ADD_RIGHT_QUERY = "INSERT INTO CAQS_ACCESS_RIGHTS(ID_ACCESS, ID_ROLE) VALUES (?, ?)";
    private static final String DELETE_RIGHT_QUERY = "DELETE FROM CAQS_ACCESS_RIGHTS WHERE ID_ACCESS=? AND ID_ROLE=?";

    public List<UserBean> retrieveUsersByIdLastname(String id, String lastname) {
        StringBuilder query = new StringBuilder("SELECT id_user, lastname, firstname, email, password ");
        StringBuffer fromClause = new StringBuffer(" FROM CAQS_USER");
        StringBuffer whereClause = new StringBuffer(" where ");
        boolean alreadyOneClause = false;

        if (!"%".equals(id)) {
            //il y a une recherche sur l'identifiant
            whereClause = whereClause.append(" lower(id_user) like '").append(id.toLowerCase()).append("' ");
            alreadyOneClause = true;
        }

        if (!"%".equals(lastname)) {
            //il y a une recherche sur le lib
            if (alreadyOneClause) {
                whereClause = whereClause.append(" AND ");
            }
            whereClause = whereClause.append(" lower(lastname) like '").append(lastname.toLowerCase()).append("' ");
            alreadyOneClause = true;
        }

        query.append(fromClause);
        if (alreadyOneClause) {
            query.append(whereClause);
        }
        List<UserBean> result = new ArrayList<UserBean>();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Connection connection = JdbcDAOUtils.getConnection(this, Constants.CAQS_DATASOURCE_KEY);
        try {
            // Preparation de la requete.
            stmt = connection.prepareStatement(query.toString());
            // Execution de la requete.
            rs = stmt.executeQuery();
            while (rs.next()) {
                // Parcours du resultat.
                UserBean user = new UserBean();
                user.setId(rs.getString("id_user"));
                user.setLastName(rs.getString("lastname"));
                user.setFirstName(rs.getString("firstname"));
                user.setEmail(rs.getString("email"));
                user.setPassword(rs.getString("password"));
                user.setRoles(getUserRoles(user.getId()));
                result.add(user);
            }
        } catch (SQLException e) {
            logger.error("Error retrieving users", e);
        } finally {
            // Fermeture du resultat et de la requete.
            JdbcDAOUtils.closeResultSet(rs);
            JdbcDAOUtils.closePrepareStatement(stmt);
            JdbcDAOUtils.closeConnection(connection);
        }
        return result;
    }
    private static final String RETRIEVE_USER_BY_ID = "SELECT id_user, lastname, firstname, email, password "
            + " FROM CAQS_USER "
            + " WHERE id_user = ? ";

    public UserBean retrieveUserById(String id) {
        UserBean result = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Connection connection = JdbcDAOUtils.getConnection(this, Constants.CAQS_DATASOURCE_KEY);
        try {
            // Preparation de la requete.
            stmt = connection.prepareStatement(RETRIEVE_USER_BY_ID);
            stmt.setString(1, id);
            // Execution de la requete.
            rs = stmt.executeQuery();
            if (rs.next()) {
                // Parcours du resultat.
                result = new UserBean();
                result.setId(rs.getString("id_user"));
                result.setLastName(rs.getString("lastname"));
                result.setFirstName(rs.getString("firstname"));
                result.setEmail(rs.getString("email"));
                result.setPassword(rs.getString("password"));
                result.setRoles(getUserRoles(result.getId()));
            }
        } catch (SQLException e) {
            logger.error("Error retrieving user", e);
        } finally {
            // Fermeture du resultat et de la requete.
            JdbcDAOUtils.closeResultSet(rs);
            JdbcDAOUtils.closePrepareStatement(stmt);
            JdbcDAOUtils.closeConnection(connection);
        }
        return result;
    }
    private static final String CREATE_USER_QUERY = "INSERT INTO CAQS_USER (ID_USER,lastname,firstname,email, password, createddate) VALUES (?, ?, ?, ?, ?, {fn now()})";
    private static final String UPDATE_USER_QUERY = "UPDATE CAQS_USER SET Lastname = ?, firstname = ?, email=?, password=? WHERE ID_USER = ?";

    public void saveUserBean(UserBean ub) throws DataAccessException {
        PreparedStatement update = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Connection connection = JdbcDAOUtils.getConnection(this, Constants.CAQS_DATASOURCE_KEY);
        try {
            // Preparation de la requete.
            stmt = connection.prepareStatement(RETRIEVE_USER_BY_ID);
            stmt.setString(1, ub.getId());
            // Execution de la requete.
            rs = stmt.executeQuery();
            if (rs.next()) {
                update = connection.prepareStatement(UPDATE_USER_QUERY);
                update.setString(1, ub.getLastName());
                update.setString(2, ub.getFirstName());
                update.setString(3, ub.getEmail());
                update.setString(4, ub.getPassword());
                update.setString(5, ub.getId());
            } else {
                update = connection.prepareStatement(CREATE_USER_QUERY);
                update.setString(1, ub.getId());
                update.setString(2, ub.getLastName());
                update.setString(3, ub.getFirstName());
                update.setString(4, ub.getEmail());
                update.setString(5, ub.getPassword());
            }
            int nb = update.executeUpdate();
            if (nb == 0) {
                throw new DataAccessException("Nothing updated");
            }

            saveRoles(ub.getId(), ub.getRoles());

        } catch (SQLException e) {
            logger.error("Error updating user", e);
            throw new DataAccessException(e);
        } finally {
            // Fermeture du resultat et de la requete.
            JdbcDAOUtils.closeResultSet(rs);
            JdbcDAOUtils.closePrepareStatement(update);
            JdbcDAOUtils.closePrepareStatement(stmt);
            JdbcDAOUtils.closeConnection(connection);
        }
    }
    private static final String DELETE_USER_QUERY = "DELETE FROM CAQS_USER WHERE "
            + " id_user = ?";

    public void deleteUserBean(String userId) throws DataAccessException {

                // on efface les roles associes a l'elt pour les remettre; moins couteux que erreur + update !!!
        this.deleteUsersRoles(userId);

        PreparedStatement stmt = null;
        ResultSet rs = null;
        Connection connection = JdbcDAOUtils.getConnection(this, Constants.CAQS_DATASOURCE_KEY);
        try {
            // Preparation de la requete.
            stmt = connection.prepareStatement(DELETE_USER_QUERY);
            stmt.setString(1, userId);
            // Execution de la requete.
            int nb = stmt.executeUpdate();
            if (nb == 0) {
                throw new DataAccessException("Nothing deleted");
            }
        } catch (SQLException e) {
            logger.error("Error deleting user", e);
            throw new DataAccessException(e);
        } finally {
            // Fermeture du resultat et de la requete.
            JdbcDAOUtils.closeResultSet(rs);
            JdbcDAOUtils.closePrepareStatement(stmt);
            JdbcDAOUtils.closeConnection(connection);
        }
    }
    private static final String RETRIEVE_ALL_USERS = "SELECT id, lastname, firstname, email, password "
            + " FROM CAQS_USER ORDER BY lastname";

    public List<UserBean> retrieveAllUsers() {
        List<UserBean> result = new ArrayList<UserBean>();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Connection connection = JdbcDAOUtils.getConnection(this, Constants.CAQS_DATASOURCE_KEY);
        try {
            // Preparation de la requete.
            stmt = connection.prepareStatement(RETRIEVE_ALL_USERS);
            // Execution de la requete.
            rs = stmt.executeQuery();
            while (rs.next()) {
                // Parcours du resultat.
                UserBean user = new UserBean();
                user.setId(rs.getString("id"));
                user.setLastName(rs.getString("lastname"));
                user.setFirstName(rs.getString("firstname"));
                user.setEmail(rs.getString("email"));
                user.setPassword(rs.getString("password"));
                user.setRoles(getUserRoles(user.getId()));
                result.add(user);
            }
        } catch (SQLException e) {
            logger.error("Error retrieving users", e);
        } finally {
            // Fermeture du resultat et de la requete.
            JdbcDAOUtils.closeResultSet(rs);
            JdbcDAOUtils.closePrepareStatement(stmt);
            JdbcDAOUtils.closeConnection(connection);
        }
        return result;
    }
    private static final String UPDATE_LAST_LOGIN_QUERY = "UPDATE CAQS_USER SET lastlogintime = {fn now()} WHERE ID_USER = ?";

    public void udpateLastLoginTime(String userId) throws DataAccessException {
        PreparedStatement update = null;
        ResultSet rs = null;
        Connection connection = JdbcDAOUtils.getConnection(this, Constants.CAQS_DATASOURCE_KEY);
        try {
            update = connection.prepareStatement(UPDATE_LAST_LOGIN_QUERY);
            update.setString(1, userId);
            int nb = update.executeUpdate();
            if (nb == 0) {
                throw new DataAccessException("Nothing updated");
            }
        } catch (SQLException e) {
            logger.error("Error updating user", e);
            throw new DataAccessException(e);
        } finally {
            // Fermeture du resultat et de la requete.
            JdbcDAOUtils.closeResultSet(rs);
            JdbcDAOUtils.closePrepareStatement(update);
            JdbcDAOUtils.closeConnection(connection);
        }

    }

    private static final String GET_USER_ROLES_QUERY =
            "SELECT r.id_role, r.portal_role "
            + " FROM CAQS_ROLE r, USER_ROLE u where u.ID_USER=? and u.ID_ROLE=r.ID_ROLE";

    public List<RoleBean> getUserRoles(String userId) {
        List<RoleBean> retour = new ArrayList<RoleBean>();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Connection connection = JdbcDAOUtils.getConnection(this, Constants.CAQS_DATASOURCE_KEY);
        try {
            stmt = connection.prepareStatement(GET_USER_ROLES_QUERY);
            stmt.setString(1, userId);
            // Execute request.
            rs = stmt.executeQuery();
            while (rs.next()) {
                RoleBean rb = new RoleBean();
                rb.setId(rs.getString("id_role"));
                rb.setLib(rs.getString("portal_role"));
                retour.add(rb);
            }
        } catch (SQLException e) {
            logger.error(e.toString());
        } finally {
            // Fermeture du r�sultat et de la requ�te.
            JdbcDAOUtils.closeResultSet(rs);
            JdbcDAOUtils.closePrepareStatement(stmt);
            JdbcDAOUtils.closeConnection(connection);
        }
        return retour;
    }

    public void saveRoles(String userId, List<RoleBean> roles) {

        // on efface les roles associes a l'elt pour les remettre; moins couteux que erreur + update !!!
        this.deleteUsersRoles(userId);

        String updateRoles = "INSERT INTO USER_ROLE (ID_USER,ID_ROLE) VALUES (?,?)";

        Connection connection = JdbcDAOUtils.getConnection(this, Constants.CAQS_DATASOURCE_KEY);
        PreparedStatement pstmt = null;
        try {
            pstmt = connection.prepareStatement(updateRoles);
            for (int i=0;i<roles.size();i++) {
                pstmt.setString(1, userId);
                pstmt.setString(2, roles.get(i).getId());
                pstmt.addBatch();
            }
            pstmt.executeBatch();
            JdbcDAOUtils.commit(connection);
        } catch (SQLException e) {
            logger.error("Error during user roles update", e);
            JdbcDAOUtils.rollbackConnection(connection);
        } finally {
            JdbcDAOUtils.closePrepareStatement(pstmt);
            JdbcDAOUtils.closeConnection(connection);
        }

    }

    public void deleteUsersRoles(String userId) {
        String deleteRights = "DELETE FROM USER_ROLE WHERE ID_USER=?";
        Connection connection = JdbcDAOUtils.getConnection(this, Constants.CAQS_DATASOURCE_KEY);
        PreparedStatement pstmt = null;
        try {
            pstmt = connection.prepareStatement(deleteRights);
            pstmt.setString(1, userId);
            pstmt.executeUpdate();
            JdbcDAOUtils.commit(connection);
        } catch (SQLException e) {
            logger.error("Error during user roles delete", e);
            JdbcDAOUtils.rollbackConnection(connection);
        } finally {
            JdbcDAOUtils.closePrepareStatement(pstmt);
            JdbcDAOUtils.closeConnection(connection);
        }

    }

}
