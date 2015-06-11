package com.compuware.toolbox.dbms;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import javax.sql.DataSource;

import org.apache.commons.dbcp.PoolingDataSource;
import org.apache.commons.pool.impl.GenericObjectPool;

import com.compuware.toolbox.io.JndiReader;
import com.compuware.toolbox.constants.Constants;
import com.compuware.toolbox.util.logging.LoggerManager;

public class JdbcDAOUtils {
	
    private static org.apache.log4j.Logger mLogger = LoggerManager.getLogger(Constants.LOG4J_DBMS_LOGGER_KEY);

    public static final String DATABASE_STRING_NOFILTER = "%";

    /**
     * Gets the database connection. It first tries to use the DataSource named in
     * the parameter lookupName. If nothing is found, it will use a DriverManager.
     *
     * @param obj        The object that needs the connection.
     * @param lookupName the name of the DataSource to use.
     * @return Connection The database connection, null on error
     */
    public static synchronized Connection getConnection(Object obj, String lookupName) {
        Connection connection = null;

        if (lookupName == null) {
        	connection = getConnectionFromDriverManager(obj);
        }

        if (connection == null) {
	        try {
	            Object objref = JndiReader.getValue("jdbc/" + lookupName);
	            if (objref == null) {
	                objref = JndiReader.getValue(lookupName);
	            }
	            if (objref != null) {
	                dataSource = (DataSource) javax.rmi.PortableRemoteObject.narrow(objref, DataSource.class);
	            }
	            else {
	            	connection = getConnection(obj);
	            }
	        }
	        catch (ClassCastException cce) {
	        	connection = getConnection(obj);
	        }
        }
        
        if (connection == null) {
	        if (dataSource != null) {
	            try {
	                connection = dataSource.getConnection();
	            } catch (SQLException ex) {
	            	mLogger.error("### got sql exception on datasource.getconnection", ex);
	            }
	        }
        }
        
        mLogger.trace("Get Connection from object " + obj + ", lookupName=" + lookupName + ", connection=" + connection);
        
        return connection;
    }


    /**
     * Gets the database connection. It will use the DataSource.
     * for retrieving a connection.
     *
     * @param obj The object that needs the connection.
     * @return Connection The database connection, null on error
     */
    public static Connection getConnection(Object obj) {
        return getConnectionFromDataSource(obj);
    }


    /**
     * Gets the database connection. It will use the DriverManager
     * for retrieving a connection.
     *
     * @param obj The object that needs the connection.
     * @return Connection The database connection, null on error
     */
    private static Connection getConnectionFromDriverManager(Object obj) {
        Properties databaseProps = new Properties();
        Connection connection = null;

        // try to find the properties file
        try {
            // dbms.properties file located in classpath (of object?)
            databaseProps.load(obj.getClass().getResourceAsStream("/datasource.properties"));
        } catch (java.io.IOException io) {
        	mLogger.error("### JdbcDAOUtils, IO Exception, could not find the file dbms.properties ###");
        	mLogger.error("### JdbcDAOUtils, IO Exception", io);
        }

        String dbUrl = databaseProps.getProperty("datasource.url");
        String dbClassName = databaseProps.getProperty("datasource.classname");
        String dbUserName = databaseProps.getProperty("datasource.username");
        String dbPassWord = databaseProps.getProperty("datasource.password");

        try {
            Class.forName(dbClassName).newInstance();
            connection = DriverManager.getConnection(dbUrl, dbUserName, dbPassWord);
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
        	mLogger.error("### JdbcDAOUtils, Exception.... Database driver problem ###");
        	mLogger.error("### Is the Database driver located in the classpath ?! ###", e);
        }

        return connection;
    }

    /** An internal pooling datasource if application managed. */
    private static DataSource dataSource = null;

    /**
     * Gets the database connection. It will use the DataSource.
     * for retrieving a connection.
     *
     * @param obj The object that needs the connection.
     * @return Connection The database connection, null on error
     */
    private static synchronized Connection getConnectionFromDataSource(Object obj) {
        Properties databaseProps = new Properties();
        Connection connection = null;
        if (dataSource == null) {
// try to find the properties file
            try {
// dbms.properties file located in classpath (of object?)
                databaseProps.load(obj.getClass().getResourceAsStream("/datasource.properties"));
            } catch (java.io.IOException io) {
            	mLogger.error("### JdbcDAOUtils, IO Exception, could not find the file dbms.properties ###");
            	mLogger.error("### JdbcDAOUtils, IO Exception", io);
                return null;
            }

            //String dbUrl = databaseProps.getProperty("datasource.url");
            String dbClassName = databaseProps.getProperty("datasource.classname");
            //String dbUserName = databaseProps.getProperty("datasource.username");
            //String dbPassWord = databaseProps.getProperty("datasource.password");

            try {
                Class.forName(dbClassName).newInstance();

                GenericObjectPool connectionPool = new GenericObjectPool(null); // will link this later
                //DriverManagerConnectionFactory connectionFactory = new DriverManagerConnectionFactory(dbUrl, dbUserName, dbPassWord);
                //PoolableConnectionFactory poolableConnectionFactory = new PoolableConnectionFactory(connectionFactory, connectionPool, null, null, false, true);
// the above constructor arguments: (connectionFactory, connectionPool, null                  , null                  , false                  , true);
// mean                             (ConnectionFactory, ObjectPool    , KeyedObjectPoolFactory, String validationQuery, boolean defaultReadOnly, boolean defaultAutoCommit)

                dataSource = new PoolingDataSource(connectionPool);
            } catch (Exception e) {
            	mLogger.error("### JdbcDAOUtils, Exception.... Database driver problem ###");
            	mLogger.error("### Is the Database driver located in the classpath ?! ###", e);
                return null;
            }
        }

        try {
            connection = dataSource.getConnection();
        } catch (java.sql.SQLException sql) {
        	mLogger.error("Error getting connection", sql);
        }
        return connection;
    }

    /**
     * Closes the result set
     *
     * @param result The ResultSet that needs to close
     */
    public static void closeResultSet(ResultSet result) {
        try {
            if (result != null) {
                result.close();
            }
        } catch (SQLException se) {
        	mLogger.error("### JdbcDAOUtils, SQLException, closing result set", se);
        }
    }

    /**
     * Closes the prepare statement
     *
     * @param prepare The PreparedStatement that needs to close
     */
    public static void closePrepareStatement(PreparedStatement prepare) {
        try {
            if (prepare != null) {
                prepare.close();
            }
        } catch (SQLException se) {
        	mLogger.error("### JdbcDAOUtils, SQLException, closing prepare statement", se);
        }
    }

    /**
     * Closes the statement
     *
     * @param stmt The Statement that needs to close
     */
    public static void closeStatement(Statement stmt) {
        try {
            if (stmt != null) {
                stmt.close();
            }
        } catch (SQLException se) {
        	mLogger.error("### JdbcDAOUtils, SQLException, closing prepare statement", se);
        }
    }

    /**
     * Closes the database connection
     *
     * @param connection The Connection that needs to close
     */
    public static void closeConnection(Connection connection) {
        mLogger.trace("Closing connection " + connection);
        try {
            if (connection != null && !connection.isClosed()) {
            	// Reinitialisation des parametres par defaut...
            	connection.setAutoCommit(true);
                connection.close();
            }
        } catch (SQLException se) {
        	mLogger.error("### JdbcDAOUtils, SQLException, closing DB connection", se);
        }
    }

    /**
     * Rollback the database connection
     *
     * @param connection The Connection that needs to rollback
     */
    public static void rollbackConnection(Connection connection) {
        try {
            if (connection != null && !connection.isClosed() && !connection.getAutoCommit()) {
                connection.rollback();
            }
        } catch (SQLException se) {
        	mLogger.error("### JdbcDAOUtils, SQLException, rollback DB connection", se);
        }
    }

    /**
     * Commit the database connection
     *
     * @param connection The Connection that needs to commit
     */
    public static void commit(Connection connection) throws SQLException {
        if (connection != null && !connection.isClosed() && !connection.getAutoCommit()) {
            connection.commit();
        }
    }

}
