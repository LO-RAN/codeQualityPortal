package com.compuware.caqs.dao.dbms;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import com.compuware.caqs.constants.Constants;
import com.compuware.caqs.dao.interfaces.InternationalizationDao;
import com.compuware.caqs.domain.dataschemas.InternationalizationBean;
import com.compuware.caqs.domain.dataschemas.i18n.I18nUtil;
import com.compuware.toolbox.dbms.JdbcDAOUtils;
import com.compuware.toolbox.util.logging.LoggerManager;
import com.compuware.toolbox.util.resources.DbmsResourceBundle;

public class InternationalizationDbmsDao implements InternationalizationDao {

    private static org.apache.log4j.Logger logger = LoggerManager.getLogger(Constants.LOG4J_DBMS_LOGGER_KEY);
    private static final String VALUE_COLUMN_NAME = "TEXT";
    private static final List<String> KEY_COLUMN_LIST = getKeyColumnList();

    private static final List<String> getKeyColumnList() {
        List<String> result = new ArrayList<String>();
        result.add("TABLE_NAME");
        result.add("COLUMN_NAME");
        result.add("ID_TABLE");
        return result;
    }
    private static final String RETRIEVE_RESOURCES_QUERY =
            "SELECT TABLE_NAME, COLUMN_NAME, ID_TABLE, TEXT" + " FROM I18N" +
            " WHERE ID_LANGUE = ?" + " AND TABLE_NAME = ?";

    /**
     * @{@inheritDoc }
     */
    public void initResources(Locale loc) {
        initResources(I18nUtil.METRIQUE_BUNDLE_NAME, loc);
        initResources(I18nUtil.CRITERE_BUNDLE_NAME, loc);
        initResources(I18nUtil.FACTEUR_BUNDLE_NAME, loc);
        initResources(I18nUtil.OUTIL_BUNDLE_NAME, loc);
        initResources(I18nUtil.ELEMENT_TYPE_BUNDLE_NAME, loc);
        initResources(I18nUtil.UNIFACEVIEW_BUNDLE_NAME, loc);
        initResources(I18nUtil.MODELE_BUNDLE_NAME, loc);
        initResources(I18nUtil.TASK_BUNDLE_NAME, loc);
        initResources(I18nUtil.MESSAGE_STATUS_BUNDLE_NAME, loc);
        initResources(I18nUtil.SETTING_VALUE_BUNDLE_NAME, loc);
        initResources(I18nUtil.ACTION_PLAN_UNIT_BUNDLE_NAME, loc);
    }

    /**
     * @{@inheritDoc }
     */
    public void initResources(String tableName, Locale loc) {
        Connection connection = JdbcDAOUtils.getConnection(this, Constants.CAQS_DATASOURCE_KEY);
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = connection.prepareStatement(InternationalizationDbmsDao.RETRIEVE_RESOURCES_QUERY);
            pstmt.setString(1, loc.getLanguage());
            pstmt.setString(2, tableName);
            rs = pstmt.executeQuery();
            DbmsResourceBundle.init(tableName, loc, rs, KEY_COLUMN_LIST, VALUE_COLUMN_NAME);
        } catch (SQLException e) {
            logger.error("Error retrieving resources from database.", e);
        } finally {
            JdbcDAOUtils.closeResultSet(rs);
            JdbcDAOUtils.closePrepareStatement(pstmt);
            JdbcDAOUtils.closeConnection(connection);
        }
    }
    private static final String RETRIEVE_LOCALES_QUERY = "SELECT ID" +
            " FROM LANGUE";

    /**
     * @{@inheritDoc }
     */
    public Collection<Locale> retrieveLocales() {
        Collection<Locale> result = new ArrayList<Locale>();
        Connection connection = JdbcDAOUtils.getConnection(this, Constants.CAQS_DATASOURCE_KEY);
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = connection.prepareStatement(InternationalizationDbmsDao.RETRIEVE_LOCALES_QUERY);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                String language = rs.getString(1);
                result.add(new Locale(language));
            }
        } catch (SQLException e) {
            logger.error("Error retrieving locales from database.", e);
        } finally {
            JdbcDAOUtils.closeResultSet(rs);
            JdbcDAOUtils.closePrepareStatement(pstmt);
            JdbcDAOUtils.closeConnection(connection);
        }
        return result;
    }
    private static final String RETRIEVE_LOCALE_QUERY = "SELECT ID FROM LANGUE WHERE ID = ?";

    /**
     * @{@inheritDoc }
     */
    public Locale retrieveLocale(String language) {
        Locale result = null;
        Connection connection = JdbcDAOUtils.getConnection(this, Constants.CAQS_DATASOURCE_KEY);
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = connection.prepareStatement(InternationalizationDbmsDao.RETRIEVE_LOCALE_QUERY);
            pstmt.setString(1, language);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                String l = rs.getString(1);
                result = new Locale(l);
            } else {
                result = Locale.getDefault();
            }
        } catch (SQLException e) {
            logger.error("Error retrieving locales from database.", e);
        } finally {
            JdbcDAOUtils.closeResultSet(rs);
            JdbcDAOUtils.closePrepareStatement(pstmt);
            JdbcDAOUtils.closeConnection(connection);
        }
        return result;
    }
    private static final String SELECT_DATA_FOR_UPDATE_QUERY = "SELECT TABLE_NAME, COLUMN_NAME, ID_TABLE, ID_LANGUE" +
            " FROM I18N" + " WHERE TABLE_NAME = ?" + " AND COLUMN_NAME = ?" +
            " AND ID_TABLE = ?" + " AND ID_LANGUE = ?";
    private static final String INSERT_DATA_QUERY = "INSERT INTO I18N (TABLE_NAME, COLUMN_NAME, ID_TABLE, ID_LANGUE, TEXT) VALUES (?, ?, ?, ?, ?)";
    private static final String UPDATE_DATA_QUERY = "UPDATE I18N" +
            " SET TEXT = ?" + " WHERE TABLE_NAME = ?" + " AND COLUMN_NAME = ?" +
            " AND ID_TABLE = ?" + " AND ID_LANGUE = ?";
    private static final String DELETE_DATA_QUERY = "DELETE FROM I18N" +
            " WHERE TABLE_NAME = ?" + " AND COLUMN_NAME = ?" +
            " AND ID_TABLE = ?" + " AND ID_LANGUE = ?";

    /**
     * @{@inheritDoc }
     */
    public void updateData(Collection<InternationalizationBean> data) {
        Connection connection = JdbcDAOUtils.getConnection(this, Constants.CAQS_DATASOURCE_KEY);
        logger.debug("Update internationalization data...");
        try {
            InternationalizationBean bean;
            Iterator<InternationalizationBean> i = data.iterator();
            connection.setAutoCommit(false);
            while (i.hasNext()) {
                bean = (InternationalizationBean) i.next();
                updateData(bean, connection);
            }
            JdbcDAOUtils.commit(connection);
            connection.setAutoCommit(true);
        } catch (SQLException e) {
            logger.error("Error during internationalization store", e);
            JdbcDAOUtils.rollbackConnection(connection);
        } finally {
            JdbcDAOUtils.closeConnection(connection);
        }
    }

    /**
     * {@inheritDoc }
     */
    public void updateData(InternationalizationBean data) throws SQLException {
        Connection connection = JdbcDAOUtils.getConnection(this, Constants.CAQS_DATASOURCE_KEY);
        logger.debug("Update internationalization data...");
        try {
            connection.setAutoCommit(false);
            updateData(data, connection);
            JdbcDAOUtils.commit(connection);
            connection.setAutoCommit(true);
        } finally {
            JdbcDAOUtils.closeConnection(connection);
        }
    }

    /**
     * @{@inheritDoc }
     */
    public void updateData(InternationalizationBean bean, Connection connection) throws SQLException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        logger.debug("Updating: " + bean.getTableId());
        try {
            if (bean.getText() == null || bean.getText().length() < 1) {
                pstmt = connection.prepareStatement(DELETE_DATA_QUERY);
                pstmt.setString(1, bean.getTableName());
                pstmt.setString(2, bean.getColumnName());
                pstmt.setString(3, bean.getTableId());
                pstmt.setString(4, bean.getLanguageId());
            } else {
                pstmt = connection.prepareStatement(SELECT_DATA_FOR_UPDATE_QUERY);
                pstmt.setString(1, bean.getTableName());
                pstmt.setString(2, bean.getColumnName());
                pstmt.setString(3, bean.getTableId());
                pstmt.setString(4, bean.getLanguageId());
                rs = pstmt.executeQuery();
                if (rs.next()) {
                    JdbcDAOUtils.closePrepareStatement(pstmt);
                    pstmt = connection.prepareStatement(UPDATE_DATA_QUERY);
                    pstmt.setString(1, bean.getText());
                    pstmt.setString(2, bean.getTableName());
                    pstmt.setString(3, bean.getColumnName());
                    pstmt.setString(4, bean.getTableId());
                    pstmt.setString(5, bean.getLanguageId());
                } else {
                    JdbcDAOUtils.closePrepareStatement(pstmt);
                    pstmt = connection.prepareStatement(INSERT_DATA_QUERY);
                    pstmt.setString(1, bean.getTableName());
                    pstmt.setString(2, bean.getColumnName());
                    pstmt.setString(3, bean.getTableId());
                    pstmt.setString(4, bean.getLanguageId());
                    pstmt.setString(5, bean.getText());
                }
            }
            pstmt.executeUpdate();
        } finally {
            JdbcDAOUtils.closeResultSet(rs);
            JdbcDAOUtils.closePrepareStatement(pstmt);
        }
    }
}
