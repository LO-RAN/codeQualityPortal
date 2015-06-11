package com.compuware.caqs.dao.dbms;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.compuware.caqs.constants.Constants;
import com.compuware.caqs.dao.interfaces.PortalUserDao;
import com.compuware.caqs.domain.dataschemas.UserBean;
import com.compuware.caqs.exception.DataAccessException;
import com.compuware.caqs.security.auth.Users;
import com.compuware.toolbox.dbms.JdbcDAOUtils;
import com.compuware.toolbox.util.logging.LoggerManager;

/**
 * The Portal database accessor.
 * @author cwfr-fdubois
 */
public final class PortalUserDbmsDao implements PortalUserDao {

    /**
     * The dao logger.
     */
    private static org.apache.log4j.Logger logger = LoggerManager.getLogger(Constants.LOG4J_DBMS_LOGGER_KEY);

    /** L'instance unique de la classe. */
    private static PortalUserDao singleton = new PortalUserDbmsDao();

    /** Private constructor. */
    private PortalUserDbmsDao() {
    }

    /** Get the class instance.
     * @return the unique class instance.
     */
    public static PortalUserDao getInstance() {
        return singleton;
    }

    /**
     * Query to retrieve all portal users with firstname and lastname.
     */
    private static final String ALL_USERS_QUERY =
            "Select ID_USER, FIRSTNAME, LASTNAME From CAQS_USER ORDER BY lower(LASTNAME), lower(FIRSTNAME)";

    /**
    * {@inheritDoc}
    */
    public List<UserBean> getAllUsers() throws DataAccessException {
        List<UserBean> result = new ArrayList<UserBean>();
        Connection connection = JdbcDAOUtils.getConnection(this, Constants.CAQS_DATASOURCE_KEY);
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = connection.prepareStatement(ALL_USERS_QUERY);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                UserBean newUser = new UserBean();
                newUser.setId(rs.getString("ID_USER"));
                newUser.setFirstName(rs.getString("FIRSTNAME"));
                newUser.setLastName(rs.getString("LASTNAME"));
                newUser.setLib(newUser.getLastName() + ' ' + newUser.getFirstName() + " - " + newUser.getId());
                result.add(newUser);
            }
        }
        catch (SQLException e) {
            logger.error("Error retrieving users", e);
            throw new DataAccessException("Error retrieving users", e);
        }
        finally {
            JdbcDAOUtils.closeResultSet(rs);
            JdbcDAOUtils.closePrepareStatement(pstmt);
            JdbcDAOUtils.closeConnection(connection);
        }
        return result;
    }

    /**
     * Query to retrieve user informations: firstname, lastname, email.
     */
    private static final String USER_INFOS_QUERY =
            "Select FIRSTNAME, LASTNAME, EMAIL From CAQS_USER Where ID_USER=?";

    /**
     * {@inheritDoc}
     */
    public Users getUserInfos(String id) throws DataAccessException {
        Users result = new Users();
        result.setId(id);
        Connection connection = JdbcDAOUtils.getConnection(this, Constants.CAQS_DATASOURCE_KEY);
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = connection.prepareStatement(USER_INFOS_QUERY);
            pstmt.setString(1, id);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                result.setFirstName(rs.getString("FIRSTNAME"));
                result.setLastName(rs.getString("LASTNAME"));
                result.setEmail(rs.getString("EMAIL"));
            }
        }
        catch (SQLException e) {
            logger.error("Error retrieving user information", e);
            throw new DataAccessException("Error retrieving user information", e);
        }
        finally {
            JdbcDAOUtils.closeResultSet(rs);
            JdbcDAOUtils.closePrepareStatement(pstmt);
            JdbcDAOUtils.closeConnection(connection);
        }
        return result;
    }

}
