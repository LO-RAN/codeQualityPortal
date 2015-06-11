package com.compuware.dbms.connection;

//import org.apache.commons.dbcp.DriverManagerConnectionFactory;
//import org.apache.commons.dbcp.PoolableConnectionFactory;
import org.apache.commons.dbcp.PoolingDataSource;
import org.apache.commons.pool.impl.GenericObjectPool;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.*;
import java.util.Properties;

//GEN-GUARD:JDBCDAOUTILS$pidf7d7c0aa9fdce87d


//GEN-FREE:daojdbcdaoutils$$JDBCDAOUTILS$pidf7d7c0aa9fdce87d

public class JdbcDAOUtils {
    /**
     * Gets the database connection. It first tries to use the DataSource named in
     * the parameter lookupName. If nothing is found, it will use a DriverManager.
     *
     * @param obj        The object that needs the connection.
     * @param lookupName the name of the DataSource to use.
     * @return Connection The database connection, null on error
     */
    public static Connection getConnection(Object obj, String lookupName) {
        Connection connection = null;
        InitialContext initialContext = null;
        DataSource dataSource = null;

        if (lookupName == null) {
            return getConnectionFromDriverManager(obj);
        }

        try {
            initialContext = new InitialContext();
            dataSource = (DataSource) javax.rmi.PortableRemoteObject.narrow(initialContext.lookup("java:comp/env/jdbc/" + lookupName), DataSource.class);
        } catch (NamingException ne) {
//            return getConnectionFromDriverManager(obj);
            return getConnection(obj);
        } catch (ClassCastException cce) {
//            return getConnectionFromDriverManager(obj);
            return getConnection(obj);
        }

        if (dataSource != null) {
            try {
                connection = dataSource.getConnection();
            } catch (SQLException ex) {
                System.out.println("### got sql exception on datasource.getconnection: " + ex);
            }
        }

        return connection;
    }


    /**
     * Gets the database connection. It will use the DriverManager
     * for retrieving a connection.
     *
     * @param obj The object that needs the connection.
     * @return Connection The database connection, null on error
     */
    public static Connection getConnection(Object obj) {
// getConnectionFromDriverManager(obj) is being moved out in favour of getConnectionFromDataSource
//        return getConnectionFromDriverManager(obj);
        return getConnectionFromDataSource(obj);
    }


    private static Connection getConnectionFromDriverManager(Object obj) {
        Properties databaseProps = new Properties();
        Connection connection = null;

        // try to find the properties file
        try {
            // dbms.properties file located in classpath (of object?)
            databaseProps.load(obj.getClass().getResourceAsStream("/datasource.properties"));
        } catch (java.io.IOException io) {
            System.out.println("### JdbcDAOUtils, IO Exception, could not find the file dbms.properties ###");
            System.out.println("### JdbcDAOUtils, IO Exception: " + io.getMessage());
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
            System.out.println("### JdbcDAOUtils, Exception.... Database driver problem ###");
            System.out.println("### Is the Database driver located in the classpath ?! ###");
            e.printStackTrace();
        }

        return connection;
    }

    private static PoolingDataSource dataSource = null;

    private static synchronized Connection getConnectionFromDataSource(Object obj) {
        Properties databaseProps = new Properties();
        Connection connection = null;
        if (dataSource == null) {
// try to find the properties file
            try {
// dbms.properties file located in classpath (of object?)
                databaseProps.load(obj.getClass().getResourceAsStream("/datasource.properties"));
            } catch (java.io.IOException io) {
                System.out.println("### JdbcDAOUtils, IO Exception, could not find the file dbms.properties ###");
                System.out.println("### JdbcDAOUtils, IO Exception: " + io.getMessage());
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
                System.out.println("### JdbcDAOUtils, Exception.... Database driver problem ###");
                System.out.println("### Is the Database driver located in the classpath ?! ###");
                e.printStackTrace();
                return null;
            }
        }

        try {
            connection = dataSource.getConnection();
        } catch (java.sql.SQLException sql) {
            sql.printStackTrace();
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
            System.out.println("### JdbcDAOUtils, SQLException, closing result set: " + se.getMessage());
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
            System.out.println("### JdbcDAOUtils, SQLException, closing prepare statement: " + se.getMessage());
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
            System.out.println("### JdbcDAOUtils, SQLException, closing prepare statement: " + se.getMessage());
        }
    }

    /**
     * Closes the database connection
     *
     * @param connection The Connection that needs to close
     */
    public static void closeConnection(Connection connection) {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException se) {
            System.out.println("### JdbcDAOUtils, SQLException, closing DB connection: " + se.getMessage());
        }
    }

    /**
     * Rollback the database connection
     *
     * @param connection The Connection that needs to rollback
     */
    public static void rollbackConnection(Connection connection) {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.rollback();
            }
        } catch (SQLException se) {
            System.out.println("### JdbcDAOUtils, SQLException, rollback DB connection: " + se.getMessage());
        }
    }

}

//GEN-GUARD:1$$JDBCDAOUTILS$pidf7d7c0aa9fdce87d


