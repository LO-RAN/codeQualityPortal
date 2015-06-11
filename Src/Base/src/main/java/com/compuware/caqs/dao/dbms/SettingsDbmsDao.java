package com.compuware.caqs.dao.dbms;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.compuware.caqs.constants.Constants;
import com.compuware.caqs.dao.interfaces.SettingsDao;
import com.compuware.caqs.domain.dataschemas.settings.SettingValue;
import com.compuware.caqs.domain.dataschemas.settings.SettingValuesBean;
import com.compuware.caqs.domain.dataschemas.settings.Settings;
import com.compuware.caqs.domain.dataschemas.settings.UserSettingBean;
import com.compuware.caqs.exception.DataAccessException;
import com.compuware.toolbox.dbms.JdbcDAOUtils;
import com.compuware.toolbox.util.logging.LoggerManager;
import java.util.ArrayList;
import java.util.List;

public class SettingsDbmsDao implements SettingsDao {

    private static org.apache.log4j.Logger logger = LoggerManager.getLogger(Constants.LOG4J_DBMS_LOGGER_KEY);
    protected static DataAccessCache dataCache = DataAccessCache.getInstance();
    private static SettingsDbmsDao singleton = new SettingsDbmsDao();

    public static SettingsDbmsDao getInstance() {
        return SettingsDbmsDao.singleton;
    }

    /** Creates a new instance of Class */
    private SettingsDbmsDao() {
    }
    private static final String RETRIEVE_SETTINGS_VALUES_QUERY =
            "SELECT setting_value" +
            " FROM SETTINGS_VALUES" +
            " WHERE setting_id = ?";

    /** {@inheritDoc}
     */
    public SettingValuesBean getSettingValues(Settings setting) {
        SettingValuesBean result = new SettingValuesBean();
        Connection connection = JdbcDAOUtils.getConnection(this, Constants.CAQS_DATASOURCE_KEY);
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        logger.trace("Retrieve settings for setting : " + setting);
        try {
            pstmt = connection.prepareStatement(RETRIEVE_SETTINGS_VALUES_QUERY);
            pstmt.setString(1, setting.toString());
            rs = pstmt.executeQuery();
            while (rs.next()) {
                String val = rs.getString("setting_value");
                SettingValue settingValue = new SettingValue(val);
                result.addSettingValue(settingValue);
            }
        } catch (SQLException e) {
            logger.error("Error during Element retrieve", e);
        } finally {
            JdbcDAOUtils.closeResultSet(rs);
            JdbcDAOUtils.closePrepareStatement(pstmt);
            JdbcDAOUtils.closeConnection(connection);
        }
        return result;
    }
    private static final String RETRIEVE_SETTINGS_VALUE_FOR_USER_QUERY =
            "SELECT setting_value" +
            " FROM USER_SETTINGS" +
            " WHERE setting_id = ?" +
            " AND user_id = ?";
    private static final String RETRIEVE_DEFAULT_VALUE_FOR_SETTING_QUERY =
            "SELECT setting_default" +
            " FROM SETTINGS" +
            " WHERE setting_id = ?";

    /** {@inheritDoc}
     */
    public String getSettingValueForUser(Settings setting, String userId) {
        String result = (String) dataCache.loadFromCache(setting.toString() +
                userId);
        if (result == null || "".equals(result)) {
            Connection connection = JdbcDAOUtils.getConnection(this, Constants.CAQS_DATASOURCE_KEY);
            PreparedStatement pstmt = null;
            ResultSet rs = null;
            logger.trace("Retrieve settings for setting " + setting +
                    " and user " + userId);
            try {
                pstmt = connection.prepareStatement(RETRIEVE_SETTINGS_VALUE_FOR_USER_QUERY);
                pstmt.setString(1, setting.toString());
                pstmt.setString(2, userId);
                rs = pstmt.executeQuery();
                if (rs.next()) {
                    result = rs.getString("setting_value");
                } else {
                    //pas encore de valeur pour l'utilisateur donne. on prend celle par defaut
                    JdbcDAOUtils.closeResultSet(rs);
                    JdbcDAOUtils.closePrepareStatement(pstmt);
                    pstmt = connection.prepareStatement(RETRIEVE_DEFAULT_VALUE_FOR_SETTING_QUERY);
                    pstmt.setString(1, setting.toString());
                    rs = pstmt.executeQuery();
                    if (rs.next()) {
                        result = rs.getString("setting_default");
                    }
                }
                if (result != null) {
                    dataCache.storeToCache("settings" + userId, setting.toString() +
                            userId, result);
                }
            } catch (SQLException e) {
                logger.error("Error during Element retrieve", e);
            } finally {
                JdbcDAOUtils.closeResultSet(rs);
                JdbcDAOUtils.closePrepareStatement(pstmt);
                JdbcDAOUtils.closeConnection(connection);
            }
        }
        return result;
    }
    private static final String UPDATE_VALUE_FOR_USER_SETTING_QUERY =
            "UPDATE USER_SETTINGS SET SETTING_VALUE=?" +
            " WHERE USER_ID=? AND SETTING_ID=?";
    private static final String INSERT_VALUE_FOR_USER_SETTING_QUERY =
            "INSERT INTO USER_SETTINGS(USER_ID, SETTING_ID, SETTING_VALUE)" +
            " VALUES(?, ?, ?)";
    private static final String DELETE_VALUE_FOR_USER_SETTING_QUERY =
            "DELETE FROM USER_SETTINGS WHERE USER_ID = ? AND SETTING_ID = ?";

    /** {@inheritDoc}
     */
    public void updateSettingValueForUser(Settings setting, String userId,
            String newValue) throws DataAccessException {
        Connection connection = JdbcDAOUtils.getConnection(this, Constants.CAQS_DATASOURCE_KEY);
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        logger.trace("Retrieve settings for setting " + setting + " and user " +
                userId);
        try {
            pstmt = connection.prepareStatement(RETRIEVE_SETTINGS_VALUE_FOR_USER_QUERY);
            pstmt.setString(1, setting.toString());
            pstmt.setString(2, userId);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                JdbcDAOUtils.closeResultSet(rs);
                JdbcDAOUtils.closePrepareStatement(pstmt);
                if (!"".equals(newValue)) {
                    pstmt = connection.prepareStatement(UPDATE_VALUE_FOR_USER_SETTING_QUERY);
                    pstmt.setString(1, newValue);
                    pstmt.setString(2, userId);
                    pstmt.setString(3, setting.toString());
                } else {
                    pstmt = connection.prepareStatement(DELETE_VALUE_FOR_USER_SETTING_QUERY);
                    pstmt.setString(1, userId);
                    pstmt.setString(2, setting.toString());
                }
            } else {
                JdbcDAOUtils.closeResultSet(rs);
                JdbcDAOUtils.closePrepareStatement(pstmt);
                pstmt = connection.prepareStatement(INSERT_VALUE_FOR_USER_SETTING_QUERY);
                pstmt.setString(1, userId);
                pstmt.setString(2, setting.toString());
                pstmt.setString(3, newValue);
            }
            pstmt.executeUpdate();
            dataCache.clearCache("settings" + userId);
        } catch (SQLException e) {
            logger.error("Error during Element retrieve", e);
            throw new DataAccessException(e);
        } finally {
            JdbcDAOUtils.closeResultSet(rs);
            JdbcDAOUtils.closePrepareStatement(pstmt);
            JdbcDAOUtils.closeConnection(connection);
        }
    }

    private static final String RETRIEVE_SETTINGS_VALUES =
            "SELECT user_id, setting_value" +
            " FROM USER_SETTINGS" +
            " WHERE setting_id = ?";

    /**
     * @{@inheritDoc }
     */
    public List<UserSettingBean> getSettingValuesByUsers(Settings setting) {
        List<UserSettingBean> retour = null;
        Connection connection = JdbcDAOUtils.getConnection(this, Constants.CAQS_DATASOURCE_KEY);
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        logger.trace("Retrieve settings for setting " + setting);
        try {
            retour = new ArrayList<UserSettingBean>();
            pstmt = connection.prepareStatement(RETRIEVE_SETTINGS_VALUES);
            pstmt.setString(1, setting.toString());
            rs = pstmt.executeQuery();
            while (rs.next()) {
                UserSettingBean usb = new UserSettingBean();
                usb.setSettingId(setting);
                usb.setUserId(rs.getString("user_id"));
                usb.setSettingValue(rs.getString("setting_value"));
                retour.add(usb);
            }
        } catch (SQLException e) {
            logger.error("Error during Element retrieve", e);
        } finally {
            JdbcDAOUtils.closeResultSet(rs);
            JdbcDAOUtils.closePrepareStatement(pstmt);
            JdbcDAOUtils.closeConnection(connection);
        }
        return retour;
    }
}
