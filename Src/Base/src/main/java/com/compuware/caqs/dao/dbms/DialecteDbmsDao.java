/*
 * Class.java
 *
 * Created on 23 janvier 2004, 12:22
 */
package com.compuware.caqs.dao.dbms;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.compuware.caqs.constants.Constants;
import com.compuware.caqs.dao.interfaces.DialecteDao;
import com.compuware.caqs.dao.util.QueryUtil;
import com.compuware.caqs.domain.dataschemas.DialecteBean;
import com.compuware.caqs.domain.dataschemas.LanguageBean;
import com.compuware.caqs.domain.dataschemas.modeleditor.ModelEditorDialecteBean;
import com.compuware.caqs.domain.dataschemas.modeleditor.ModelEditorLanguageBean;
import com.compuware.caqs.exception.DataAccessException;
import com.compuware.toolbox.dbms.JdbcDAOUtils;
import com.compuware.toolbox.util.logging.LoggerManager;

import org.apache.log4j.Logger;

/**
 * Data access class for dialect and language.
 * @author  cwfr-fdubois
 */
public final class DialecteDbmsDao implements DialecteDao {

    /** The DAO logger. */
    private static Logger logger = LoggerManager.getLogger(Constants.LOG4J_DBMS_LOGGER_KEY);
    /** Query used to retrieve the dialect and language for the given dialect ID. */
    private static final String DIALECTE_BY_LANGUAGE_REQUEST = "SELECT d.id_dialecte, d.lib_dialecte, d.desc_dialecte "
            + " FROM Dialecte d " + " WHERE d.id_langage = ? ";
    private static final String LANGUAGE_BY_ID_REQUEST = "SELECT id_langage, lib_lang, desc_lang "
            + " FROM Langage WHERE id_langage = ? ";
    private static final String DIALECTE_BY_ID_REQUEST = "SELECT d.id_dialecte, l.id_langage, d.lib_dialecte, d.desc_dialecte, l.lib_lang "
            + " FROM Dialecte d, Langage l " + " WHERE d.id_dialecte = ? "
            + " AND d.id_langage = l.id_langage";
    /** Query used to retrieve all dialects with associated languages. */
    private static final String ALL_DIALECTE_REQUEST = "SELECT d.id_dialecte, l.id_langage, d.lib_dialecte, d.desc_dialecte, l.lib_lang "
            + " FROM Dialecte d, Langage l "
            + " WHERE d.id_langage = l.id_langage" + " ORDER BY lib_dialecte ";
    /** Query used to retrieve the dialect and language for a given element ID. */
    private static final String DIALECTE_BY_ELEMENTID_REQUEST =
            "SELECT d.id_dialecte, d.id_langage, d.lib_dialecte, d.desc_dialecte, l.lib_lang "
            + " FROM Dialecte d, Element e, Langage l"
            + " WHERE d.id_dialecte=e.id_dialecte"
            + " AND l.id_langage = d.id_langage" + " AND e.id_elt=?";
    private static final String RETRIEVE_DIALECTE_WITH_ASSOCIATION_COUNT_BY_ID =
            "SELECT d.id_dialecte, d.lib_dialecte, d.desc_dialecte, d.id_langage, "
            + " count(elt.id_elt) as nbEAs " + " FROM dialecte d, element elt"
            + " WHERE d.id_dialecte = ? "
            + " AND d.id_dialecte = elt.id_dialecte and elt.id_telt = 'EA' "
            + " GROUP BY d.id_dialecte, d.lib_dialecte, d.desc_dialecte, d.id_langage ";
    private static final String RETRIEVE_DIALECTE_WITHOUT_ASSOCIATION_COUNT_BY_ID =
            "SELECT d.id_dialecte, d.lib_dialecte, d.desc_dialecte, d.id_langage "
            + " FROM dialecte d" + " WHERE d.id_dialecte = ? ";
    private static final String DELETE_DIALECTE_QUERY = "DELETE FROM DIALECTE WHERE "
            + " id_dialecte = ?";
    private static final String CREATE_DIALECTE_QUERY = "INSERT INTO " +
            "DIALECTE(ID_DIALECTE, LIB_DIALECTE, DESC_DIALECTE, ID_LANGAGE)" +
            " VALUES(?, ?, ?, ?)";
    private static final String UPDATE_DIALECTE_QUERY = "UPDATE " +
            " DIALECTE SET LIB_DIALECTE = ?, DESC_DIALECTE=?, ID_LANGAGE=? " +
            " WHERE ID_DIALECTE = ?";
    private static final String RETRIEVE_LANGUAGE_WITH_ASSOCIATION_COUNT_BY_ID =
            "SELECT l.id_langage, l.lib_lang, l.desc_lang, "
            + " count(d.id_dialecte) as nbDialects " + " FROM dialecte d, langage l"
            + " WHERE l.id_langage = ? "
            + " AND d.id_langage = l.id_langage "
            + " GROUP BY l.id_langage, l.lib_lang, l.desc_lang ";
    private static final String RETRIEVE_LANGUAGE_WITHOUT_ASSOCIATION_COUNT_BY_ID =
            "SELECT l.id_langage, l.lib_lang, l.desc_lang "
            + " FROM langage l" + " WHERE l.id_langage = ? ";
    private static final String DELETE_LANGUAGE_QUERY = "DELETE FROM LANGAGE WHERE "
            + " id_langage = ?";
    private static final String CREATE_LANGUAGE_QUERY = "INSERT INTO " +
            "LANGAGE(ID_LANGAGE, LIB_LANG, DESC_LANG)" +
            " VALUES(?, ?, ?)";
    private static final String UPDATE_LANGUAGE_QUERY = "UPDATE " +
            " LANGAGE SET LIB_LANG = ?, DESC_LANG = ?" +
            " WHERE ID_LANGAGE = ?";
    /** The unique instance of the DAO. */
    private static DialecteDao singleton = new DialecteDbmsDao();

    /** Get the unique instance of the DAO.
     * @return the unique instance of the DAO.
     */
    public static DialecteDao getInstance() {
        return DialecteDbmsDao.singleton;
    }

    /** Creates the unique instance of Class */
    private DialecteDbmsDao() {
    }

    /**
     * @{@inheritDoc }
     */
    public LanguageBean retrieveLanguageById(String id, Connection connection) {
        LanguageBean result = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        logger.debug("Retrieve language by id: " + id);
        try {
            pstmt = connection.prepareStatement(LANGUAGE_BY_ID_REQUEST);
            pstmt.setString(1, id);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                result = new LanguageBean(rs.getString("id_langage"));
                result.setLib(rs.getString("lib_lang"));
                result.setDesc(rs.getString("desc_lang"));
            }
        } catch (SQLException e) {
            logger.error("Error during Language retrieve", e);
        } finally {
            JdbcDAOUtils.closeResultSet(rs);
            JdbcDAOUtils.closePrepareStatement(pstmt);
        }
        logger.debug("language found: " + result);
        return result;
    }

    /**
     * @{@inheritDoc }
     */
    public static DialecteBean retrieveDialecteById(String id, Connection connection) {
        DialecteBean result = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        logger.debug("Retrieve dialecte by id: " + id);
        try {
            pstmt = connection.prepareStatement(DIALECTE_BY_ID_REQUEST);
            pstmt.setString(1, id);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                result = new DialecteBean();
                result.setId(rs.getString("id_dialecte"));
                result.setLib(rs.getString("lib_dialecte"));
                result.setDesc(rs.getString("desc_dialecte"));
                LanguageBean lb = new LanguageBean(rs.getString("id_langage"));
                lb.setLib(rs.getString("lib_lang"));
                result.setLangage(lb);
            }
        } catch (SQLException e) {
            logger.error("Error during Dialecte retrieve", e);
        } finally {
            JdbcDAOUtils.closeResultSet(rs);
            JdbcDAOUtils.closePrepareStatement(pstmt);
        }
        logger.debug("Dialecte found: " + result);
        return result;
    }

    /**
     * {@inheritDoc}
     */
    public Collection<DialecteBean> retrieveAllDialectes() {
        Collection<DialecteBean> result = new ArrayList<DialecteBean>();
        Connection connection = JdbcDAOUtils.getConnection(this, Constants.CAQS_DATASOURCE_KEY);
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = connection.prepareStatement(ALL_DIALECTE_REQUEST);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                DialecteBean newDialecte = new DialecteBean();
                newDialecte.setId(rs.getString("id_dialecte"));
                newDialecte.setLib(rs.getString("lib_dialecte"));
                newDialecte.setDesc(rs.getString("desc_dialecte"));
                LanguageBean lb = new LanguageBean(rs.getString("id_langage"));
                lb.setLib(rs.getString("lib_lang"));
                newDialecte.setLangage(lb);
                result.add(newDialecte);
            }
        } catch (SQLException e) {
            logger.error("Error during Dialecte retrieve", e);
        } finally {
            JdbcDAOUtils.closeResultSet(rs);
            JdbcDAOUtils.closePrepareStatement(pstmt);
            JdbcDAOUtils.closeConnection(connection);
        }
        return result;
    }

    /**
     * {@inheritDoc}
     */
    public DialecteBean retrieveDialecteByElementId(String elementId) {
        DialecteBean result = null;
        Connection connection = JdbcDAOUtils.getConnection(this, Constants.CAQS_DATASOURCE_KEY);
        try {
            result = retrieveDialecteByElementId(elementId, connection);
        } finally {
            JdbcDAOUtils.closeConnection(connection);
        }
        return result;
    }

    /**
     * @{@inheritDoc }
     */
    public static DialecteBean retrieveDialecteByElementId(String elementId, Connection connection) {
        DialecteBean result = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        logger.debug("Retrieve dialecte by element id: " + elementId);
        try {
            pstmt = connection.prepareStatement(DIALECTE_BY_ELEMENTID_REQUEST);
            pstmt.setString(1, elementId);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                result = new DialecteBean();
                result.setId(rs.getString("id_dialecte"));
                result.setLib(rs.getString("lib_dialecte"));
                result.setDesc(rs.getString("desc_dialecte"));
                LanguageBean lb = new LanguageBean(rs.getString("id_langage"));
                lb.setLib(rs.getString("lib_lang"));
                result.setLangage(lb);
            }
        } catch (SQLException e) {
            logger.error("Error during Dialecte retrieve", e);
        } finally {
            JdbcDAOUtils.closeResultSet(rs);
            JdbcDAOUtils.closePrepareStatement(pstmt);
        }
        logger.debug("Dialecte found: " + result);
        return result;
    }

    /**
     * {@inheritDoc}
     */
    public List<DialecteBean> retrieveDialects(String[] eltList) {
        List<DialecteBean> result = new ArrayList<DialecteBean>();

        String languageQuery =
                "SELECT distinct d.id_dialecte, d.lib_dialecte "
                + " FROM Dialecte d, Element e"
                + " WHERE d.id_dialecte = e.id_dialecte"
                + " AND e.id_elt IN "
                + QueryUtil.getInstance().getInClause(eltList);

        Connection connection = JdbcDAOUtils.getConnection(this, Constants.CAQS_DATASOURCE_KEY);
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = connection.prepareStatement(languageQuery);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                DialecteBean newDialect = new DialecteBean(rs.getString("id_dialecte"));
                newDialect.setLib(rs.getString("lib_dialecte"));
                result.add(newDialect);
            }
        } catch (SQLException e) {
            logger.error("Error during dialect retrieve", e);
        } finally {
            JdbcDAOUtils.closeResultSet(rs);
            JdbcDAOUtils.closePrepareStatement(pstmt);
            JdbcDAOUtils.closeConnection(connection);
        }
        return result;
    }


    /**
     * {@inheritDoc}
     */
    public List<DialecteBean> retrieveDialectsByLanguages(String idLangage) {
        List<DialecteBean> result = new ArrayList<DialecteBean>();

        Connection connection = JdbcDAOUtils.getConnection(this, Constants.CAQS_DATASOURCE_KEY);
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = connection.prepareStatement(DIALECTE_BY_LANGUAGE_REQUEST);
            pstmt.setString(1, idLangage);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                DialecteBean newLanguage = new DialecteBean(rs.getString("id_dialecte"));
                newLanguage.setLib(rs.getString("lib_dialecte"));
                newLanguage.setDesc(rs.getString("desc_dialecte"));
                result.add(newLanguage);
            }
        } catch (SQLException e) {
            logger.error("Error during dialecte retrieve", e);
        } finally {
            JdbcDAOUtils.closeResultSet(rs);
            JdbcDAOUtils.closePrepareStatement(pstmt);
            JdbcDAOUtils.closeConnection(connection);
        }
        return result;
    }

    /**
     * {@inheritDoc}
     */
    public List<LanguageBean> retrieveLanguages() {
        List<LanguageBean> result = new ArrayList<LanguageBean>();

        String languageQuery =
                "SELECT distinct id_langage, lib_lang, desc_lang "
                + " FROM Langage order by lib_lang";

        Connection connection = JdbcDAOUtils.getConnection(this, Constants.CAQS_DATASOURCE_KEY);
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = connection.prepareStatement(languageQuery);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                LanguageBean newLanguage = new LanguageBean(rs.getString("id_langage"));
                newLanguage.setLib(rs.getString("lib_lang"));
                newLanguage.setDesc(rs.getString("desc_lang"));
                result.add(newLanguage);
            }
        } catch (SQLException e) {
            logger.error("Error during language retrieve", e);
        } finally {
            JdbcDAOUtils.closeResultSet(rs);
            JdbcDAOUtils.closePrepareStatement(pstmt);
            JdbcDAOUtils.closeConnection(connection);
        }
        return result;
    }

    /**
     * @{@inheritDoc }
     */
    public List<DialecteBean> retrieveDialectesByIdLib(String id, String lib) {
        StringBuffer query = new StringBuffer("SELECT d.id_dialecte, d.id_langage, d.lib_dialecte, d.desc_dialecte ");
        StringBuffer fromClause = new StringBuffer(" FROM Dialecte d");
        StringBuffer whereClause = new StringBuffer(" WHERE ");
        boolean alreadyOneClause = false;

        if (!"%".equals(id)) {
            //il y a une recherche sur l'identifiant du critere
            whereClause = whereClause.append(" lower(d.id_dialecte) like '").append(id.toLowerCase()).append("' ");
            alreadyOneClause = true;
        }

        if (!"%".equals(lib)) {
            //il y a une recherche sur le lib
            if (alreadyOneClause) {
                whereClause = whereClause.append(" AND ");
            }
            whereClause = whereClause.append(" lower(d.lib_dialecte) like '").append(lib.toLowerCase()).append("' ");
            alreadyOneClause = true;
        }

        query.append(fromClause);
        if (alreadyOneClause) {
            query.append(whereClause);
        }
        List<DialecteBean> result = new ArrayList<DialecteBean>();
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
                DialecteBean dialecte = new DialecteBean();
                dialecte.setId(rs.getString("id_dialecte"));
                dialecte.setLib(rs.getString("lib_dialecte"));
                dialecte.setDesc(rs.getString("desc_dialecte"));
                LanguageBean l = new LanguageBean(rs.getString("id_langage"));
                dialecte.setLangage(l);
                result.add(dialecte);
            }
        } catch (SQLException e) {
            logger.error("Error retrieving dialectes", e);
        } finally {
            // Fermeture du resultat et de la requete.
            JdbcDAOUtils.closeResultSet(rs);
            JdbcDAOUtils.closePrepareStatement(stmt);
            JdbcDAOUtils.closeConnection(connection);
        }
        return result;
    }

    /**
     * @{@inheritDoc }
     */
    public ModelEditorDialecteBean retrieveDialectWithAssociationCountById(String id) {
        ModelEditorDialecteBean result = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Connection connection = JdbcDAOUtils.getConnection(this, Constants.CAQS_DATASOURCE_KEY);
        try {
            // Preparation de la requete.
            stmt = connection.prepareStatement(RETRIEVE_DIALECTE_WITH_ASSOCIATION_COUNT_BY_ID);
            stmt.setString(1, id);
            // Execution de la requete.
            rs = stmt.executeQuery();
            if (rs.next()) {
                // Parcours du resultat.
                result = new ModelEditorDialecteBean();
                result.setId(rs.getString("id_dialecte"));
                result.setLib(rs.getString("lib_dialecte"));
                result.setDesc(rs.getString("desc_dialecte"));
                result.setNbEAsAssociated(rs.getInt("nbEAs"));
                String langage = rs.getString("id_langage");
                if (langage != null) {
                    LanguageBean langageBean = new LanguageBean(langage);
                    langageBean.setId(langage);
                    result.setLangage(langageBean);
                }
            } else {
                JdbcDAOUtils.closeResultSet(rs);
                JdbcDAOUtils.closePrepareStatement(stmt);
                stmt = connection.prepareStatement(RETRIEVE_DIALECTE_WITHOUT_ASSOCIATION_COUNT_BY_ID);
                stmt.setString(1, id);
                // Execution de la requete.
                rs = stmt.executeQuery();
                if (rs.next()) {
                    // Parcours du resultat.
                    result = new ModelEditorDialecteBean();
                    result.setId(rs.getString("id_dialecte"));
                    result.setLib(rs.getString("lib_dialecte"));
                    result.setDesc(rs.getString("desc_dialecte"));
                    result.setNbEAsAssociated(0);
                    String langage = rs.getString("id_langage");
                    if (langage != null) {
                        LanguageBean langageBean = new LanguageBean(langage);
                        langageBean.setId(langage);
                        result.setLangage(langageBean);
                    }
                }
            }
        } catch (SQLException e) {
            logger.error("Error retrieving dialecte", e);
        } finally {
            // Fermeture du resultat et de la requete.
            JdbcDAOUtils.closeResultSet(rs);
            JdbcDAOUtils.closePrepareStatement(stmt);
            JdbcDAOUtils.closeConnection(connection);
        }
        return result;
    }

    /**
     * @{@inheritDoc }
     */
    public void deleteDialecteBean(String id) throws DataAccessException {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Connection connection = JdbcDAOUtils.getConnection(this, Constants.CAQS_DATASOURCE_KEY);
        try {
            // Preparation de la requete.
            stmt = connection.prepareStatement(DELETE_DIALECTE_QUERY);
            stmt.setString(1, id);
            // Execution de la requete.
            int nb = stmt.executeUpdate();
            if (nb == 0) {
                throw new DataAccessException("Nothing deleted");
            }
        } catch (SQLException e) {
            logger.error("Error deleting dialect", e);
            throw new DataAccessException(e);
        } finally {
            // Fermeture du resultat et de la requete.
            JdbcDAOUtils.closeResultSet(rs);
            JdbcDAOUtils.closePrepareStatement(stmt);
            JdbcDAOUtils.closeConnection(connection);
        }
    }

    /**
     * @{@inheritDoc }
     */
    public void saveDialecteBean(DialecteBean dialecte) throws DataAccessException {
        PreparedStatement stmt = null;
        Connection connection = JdbcDAOUtils.getConnection(this, Constants.CAQS_DATASOURCE_KEY);
        try {
            if (retrieveDialecteById(dialecte.getId(), connection)
                    == null) {
                // Preparation de la requete.
                stmt = connection.prepareStatement(CREATE_DIALECTE_QUERY);
                stmt.setString(1, dialecte.getId());
                stmt.setString(2, dialecte.getLib());
                stmt.setString(3, dialecte.getDesc());
                stmt.setString(4, dialecte.getLangage().getId());
                // Execution de la requete.
                int nb = stmt.executeUpdate();
                if (nb == 0) {
                    throw new DataAccessException("Nothing created");
                }
            } else {
                stmt = connection.prepareStatement(UPDATE_DIALECTE_QUERY);
                stmt.setString(1, dialecte.getLib());
                stmt.setString(2, dialecte.getDesc());
                stmt.setString(3, dialecte.getLangage().getId());
                stmt.setString(4, dialecte.getId());
                // Execution de la requete.
                int nb = stmt.executeUpdate();
                if (nb == 0) {
                    throw new DataAccessException("Nothing updated");
                }
            }
        } catch (SQLException e) {
            logger.error("Error creating tool", e);
            throw new DataAccessException(e);
        } finally {
            // Fermeture du resultat et de la requete.
            JdbcDAOUtils.closePrepareStatement(stmt);
            JdbcDAOUtils.closeConnection(connection);
        }
    }

    /**
     * @{@inheritDoc }
     */
    public List<LanguageBean> retrieveLanguagesByIdLib(String id, String lib) {
        StringBuffer query = new StringBuffer("SELECT id_langage, lib_lang, desc_lang ");
        StringBuffer fromClause = new StringBuffer(" FROM Langage");
        StringBuffer whereClause = new StringBuffer(" WHERE ");
        boolean alreadyOneClause = false;

        if (!"%".equals(id)) {
            //il y a une recherche sur l'identifiant du critere
            whereClause = whereClause.append(" lower(id_langage) like '").append(id.toLowerCase()).append("' ");
            alreadyOneClause = true;
        }

        if (!"%".equals(lib)) {
            //il y a une recherche sur le lib
            if (alreadyOneClause) {
                whereClause = whereClause.append(" AND ");
            }
            whereClause = whereClause.append(" lower(lib_lang) like '").append(lib.toLowerCase()).append("' ");
            alreadyOneClause = true;
        }

        query.append(fromClause);
        if (alreadyOneClause) {
            query.append(whereClause);
        }
        List<LanguageBean> result = new ArrayList<LanguageBean>();
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
                LanguageBean language = new LanguageBean(rs.getString("id_langage"));
                language.setLib(rs.getString("lib_lang"));
                language.setDesc(rs.getString("desc_lang"));
                result.add(language);
            }
        } catch (SQLException e) {
            logger.error("Error retrieving languages", e);
        } finally {
            // Fermeture du resultat et de la requete.
            JdbcDAOUtils.closeResultSet(rs);
            JdbcDAOUtils.closePrepareStatement(stmt);
            JdbcDAOUtils.closeConnection(connection);
        }
        return result;
    }

    /**
     * @{@inheritDoc }
     */
    public ModelEditorLanguageBean retrieveLanguageWithAssociationCountById(String id) {
        ModelEditorLanguageBean result = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Connection connection = JdbcDAOUtils.getConnection(this, Constants.CAQS_DATASOURCE_KEY);
        try {
            // Preparation de la requete.
            stmt = connection.prepareStatement(RETRIEVE_LANGUAGE_WITH_ASSOCIATION_COUNT_BY_ID);
            stmt.setString(1, id);
            // Execution de la requete.
            rs = stmt.executeQuery();
            if (rs.next()) {
                // Parcours du resultat.
                result = new ModelEditorLanguageBean(rs.getString("id_langage"));
                result.setLib(rs.getString("lib_lang"));
                result.setDesc(rs.getString("desc_lang"));
                result.setNbDialectsAssociated(rs.getInt("nbDialects"));
            } else {
                JdbcDAOUtils.closeResultSet(rs);
                JdbcDAOUtils.closePrepareStatement(stmt);
                stmt = connection.prepareStatement(RETRIEVE_LANGUAGE_WITHOUT_ASSOCIATION_COUNT_BY_ID);
                stmt.setString(1, id);
                // Execution de la requete.
                rs = stmt.executeQuery();
                if (rs.next()) {
                    // Parcours du resultat.
                    result = new ModelEditorLanguageBean(rs.getString("id_langage"));
                    result.setLib(rs.getString("lib_lang"));
                    result.setDesc(rs.getString("desc_lang"));
                    result.setNbDialectsAssociated(0);
                }
            }
        } catch (SQLException e) {
            logger.error("Error retrieving dialecte", e);
        } finally {
            // Fermeture du resultat et de la requete.
            JdbcDAOUtils.closeResultSet(rs);
            JdbcDAOUtils.closePrepareStatement(stmt);
            JdbcDAOUtils.closeConnection(connection);
        }
        return result;
    }

    /**
     * @{@inheritDoc }
     */
    public void deleteLanguageBean(String id) throws DataAccessException {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Connection connection = JdbcDAOUtils.getConnection(this, Constants.CAQS_DATASOURCE_KEY);
        try {
            // Preparation de la requete.
            stmt = connection.prepareStatement(DELETE_LANGUAGE_QUERY);
            stmt.setString(1, id);
            // Execution de la requete.
            int nb = stmt.executeUpdate();
            if (nb == 0) {
                throw new DataAccessException("Nothing deleted");
            }
        } catch (SQLException e) {
            logger.error("Error deleting language", e);
            throw new DataAccessException(e);
        } finally {
            // Fermeture du resultat et de la requete.
            JdbcDAOUtils.closeResultSet(rs);
            JdbcDAOUtils.closePrepareStatement(stmt);
            JdbcDAOUtils.closeConnection(connection);
        }
    }

    /**
     * @{@inheritDoc }
     */
    public void saveLanguageBean(LanguageBean language) throws DataAccessException {
        PreparedStatement stmt = null;
        Connection connection = JdbcDAOUtils.getConnection(this, Constants.CAQS_DATASOURCE_KEY);
        try {
            if (retrieveLanguageById(language.getId(), connection)
                    == null) {
                // Preparation de la requete.
                stmt = connection.prepareStatement(CREATE_LANGUAGE_QUERY);
                stmt.setString(1, language.getId());
                stmt.setString(2, language.getLib());
                stmt.setString(3, language.getDesc());
                // Execution de la requete.
                int nb = stmt.executeUpdate();
                if (nb == 0) {
                    throw new DataAccessException("Nothing created");
                }
            } else {
                stmt = connection.prepareStatement(UPDATE_LANGUAGE_QUERY);
                stmt.setString(1, language.getLib());
                stmt.setString(2, language.getDesc());
                stmt.setString(3, language.getId());
                // Execution de la requete.
                int nb = stmt.executeUpdate();
                if (nb == 0) {
                    throw new DataAccessException("Nothing updated");
                }
            }
        } catch (SQLException e) {
            logger.error("Error creating language", e);
            throw new DataAccessException(e);
        } finally {
            // Fermeture du resultat et de la requete.
            JdbcDAOUtils.closePrepareStatement(stmt);
            JdbcDAOUtils.closeConnection(connection);
        }
    }

}
