package com.compuware.caqs.dao.dbms;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

import com.compuware.caqs.constants.Constants;
import com.compuware.caqs.dao.interfaces.StereotypeDao;
import com.compuware.caqs.domain.dataschemas.StereotypeBean;
import com.compuware.toolbox.dbms.JdbcDAOUtils;
import com.compuware.toolbox.util.logging.LoggerManager;

/**
 * Created by IntelliJ IDEA.
 * User: cwfr-fdubois
 * Date: 7 févr. 2006
 * Time: 11:35:42
 * To change this template use File | Settings | File Templates.
 */
public class StereotypeDbmsDao implements StereotypeDao {

    private static org.apache.log4j.Logger logger = LoggerManager.getLogger(Constants.LOG4J_DBMS_LOGGER_KEY);

    /** L'instance unique de la classe. */
    private static StereotypeDao singleton = new StereotypeDbmsDao();

    /** Constructeur privé. */
    private StereotypeDbmsDao() {
    }

    /** Méthode d'accès à l'instance de la classe.
     * @return l'instance unique de la classe.
     */
    public static StereotypeDao getInstance() {
        return singleton;
    }

    /** La requête SQL de sélection d'un stéréotype à partir de son identifiant. */
    private static final String STEREOTYPE_BY_ID_REQUEST = "SELECT id_stereotype, lib_stereotype, desc_stereotype FROM Stereotype WHERE id_stereotype = ?";

    /** {@inheritDoc}
	 */
    public StereotypeBean retrieveStereotypeById(java.lang.String id) {
        // Récupération d'une connexion JDBC.
        Connection connection = JdbcDAOUtils.getConnection(this, Constants.CAQS_DATASOURCE_KEY);
        StereotypeBean result = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        logger.debug("Retrieve stereotype by id: "+id);
        try {
            pstmt = connection.prepareStatement(STEREOTYPE_BY_ID_REQUEST);
            pstmt.setString(1, id);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                result = new StereotypeBean();
                result.setId(rs.getString("id_stereotype"));
                result.setLib(rs.getString("lib_stereotype"));
                result.setDesc(rs.getString("desc_stereotype"));
            }
        }
        catch (SQLException e) {
            logger.error("Error during Stereotype retrieve", e);
        }
        finally {
            JdbcDAOUtils.closeResultSet(rs);
            JdbcDAOUtils.closePrepareStatement(pstmt);
            JdbcDAOUtils.closeConnection(connection);
        }
        return result;
    }

    /** La requête SQL de sélection d'un stéréotype à partir de son identifiant. */
    private static final String ALL_STEREOTYPE_REQUEST = "SELECT id_stereotype, lib_stereotype, desc_stereotype FROM Stereotype ORDER BY lib_stereotype";

    /** {@inheritDoc}
	 */
    public Collection retrieveAllStereotype() {
        // Récupération d'une connexion JDBC.
        Connection connection = JdbcDAOUtils.getConnection(this, Constants.CAQS_DATASOURCE_KEY);
        Collection result = new ArrayList();
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        logger.debug("Retrieve all stereotypes");
        try {
            pstmt = connection.prepareStatement(ALL_STEREOTYPE_REQUEST);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                StereotypeBean stereotypeBean = new StereotypeBean();
                stereotypeBean.setId(rs.getString("id_stereotype"));
                stereotypeBean.setLib(rs.getString("lib_stereotype"));
                stereotypeBean.setDesc(rs.getString("desc_stereotype"));
                result.add(stereotypeBean);
            }
        }
        catch (SQLException e) {
            logger.error("Error during Stereotype retrieve", e);
        }
        finally {
            JdbcDAOUtils.closeResultSet(rs);
            JdbcDAOUtils.closePrepareStatement(pstmt);
            JdbcDAOUtils.closeConnection(connection);
        }
        return result;
    }

    private static final String SELECT_STEREOTYPE_FOR_UPDATE_QUERY = "SELECT id_stereotype FROM Stereotype WHERE id_stereotype = ?";
    private static final String INSERT_STEREOTYPE_QUERY = "INSERT INTO Stereotype (id_stereotype, lib_stereotype, desc_stereotype) VALUES (?, ?, ?)";
    private static final String UPDATE_STEREOTYPE_QUERY = "UPDATE Stereotype SET lib_stereotype=?, desc_stereotype=? WHERE id_stereotype = ?";

    /** {@inheritDoc}
	 */
    public void storeStereotype(StereotypeBean stereotypeBean) {
        if (stereotypeBean != null && stereotypeBean.getId() != null) {
            // Récupération d'une connexion JDBC.
            Connection connection = JdbcDAOUtils.getConnection(this, Constants.CAQS_DATASOURCE_KEY);
            PreparedStatement pstmt = null;
            ResultSet rs = null;
            logger.debug("Store stereotype");
            try {
                pstmt = connection.prepareStatement(SELECT_STEREOTYPE_FOR_UPDATE_QUERY);
                pstmt.setString(1, stereotypeBean.getId());
                rs = pstmt.executeQuery();
                if (rs.next()) {
                    JdbcDAOUtils.closePrepareStatement(pstmt);
                    pstmt = connection.prepareStatement(UPDATE_STEREOTYPE_QUERY);
                    pstmt.setString(1, stereotypeBean.getLib());
                    pstmt.setString(2, stereotypeBean.getDesc());
                    pstmt.setString(3, stereotypeBean.getId());
                }
                else {
                    JdbcDAOUtils.closePrepareStatement(pstmt);
                    pstmt = connection.prepareStatement(INSERT_STEREOTYPE_QUERY);
                    pstmt.setString(1, stereotypeBean.getId());
                    pstmt.setString(2, stereotypeBean.getLib());
                    pstmt.setString(3, stereotypeBean.getDesc());
                }
                pstmt.executeUpdate();
                JdbcDAOUtils.commit(connection);
            }
            catch (SQLException e) {
                logger.error("Error during Stereotype store", e);
                JdbcDAOUtils.rollbackConnection(connection);
            }
            finally {
                JdbcDAOUtils.closeResultSet(rs);
                JdbcDAOUtils.closePrepareStatement(pstmt);
                JdbcDAOUtils.closeConnection(connection);
            }
        }
    }

}
