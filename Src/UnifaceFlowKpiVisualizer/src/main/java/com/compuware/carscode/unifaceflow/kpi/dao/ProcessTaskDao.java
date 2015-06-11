package com.compuware.carscode.unifaceflow.kpi.dao;

import com.compuware.carscode.unifaceflow.kpi.Constants;
import com.compuware.dbms.JdbcDAOUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;

/**
 * Created by IntelliJ IDEA.
 * User: cwfr-fdubois
 * Date: 13 mars 2006
 * Time: 15:10:32
 * To change this template use File | Settings | File Templates.
 */
public class ProcessTaskDao {

    private static ProcessTaskDao instance = new ProcessTaskDao();

    private ProcessTaskDao() {
    }

    public static ProcessTaskDao getInstance() {
        return ProcessTaskDao.instance;
    }

    private static final String AGGREGATED_PROCESS_DATA_QUERY =
            "SELECT UPROCID, UOBJNAME, COUNT(UPROCID)," +
            " trunc(AVG(uended-ustarted))," +
            " trunc( mod( AVG(uended-ustarted)*24, 24 ) )," +
            " trunc( mod( AVG(uended-ustarted)*24*60, 60 ) )," +
            " trunc( mod( AVG(uended-ustarted)*24*60*60, 60 ) )," +
            " trunc(AVG(ustarted-ucreated))," +
            " trunc( mod( AVG(ustarted-ucreated)*24, 24 ) )," +
            " trunc( mod( AVG(ustarted-ucreated)*24*60, 60 ) )," +
            " trunc( mod( AVG(ustarted-ucreated)*24*60*60, 60 ) )," +
            " trunc(AVG(uended-ucreated))," +
            " trunc( mod( AVG(uended-ucreated)*24, 24 ) )," +
            " trunc( mod( AVG(uended-ucreated)*24*60, 60 ) )," +
            " trunc( mod( AVG(uended-ucreated)*24*60*60, 60 ) )" +
            " FROM UWPRCI" +
            " WHERE UPRCITYPE = 'Process'" +
            " AND USTATE = 'Processed'" +
            " GROUP BY UPROCID, UOBJNAME";

    public Collection retrieveAggregatedProcessData() {
        Collection result = new ArrayList();
        Connection conn = JdbcDAOUtils.getConnection(this, Constants.UNIFACE_FLOW_DS_NAME);
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = conn.prepareStatement(AGGREGATED_PROCESS_DATA_QUERY);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                ProcessSummaryBean bean = new ProcessSummaryBean();
                bean.setId(rs.getString(1));
                bean.setName(rs.getString(2));
                bean.setNbExecutions(rs.getInt(3));
                Calendar avgExec = Calendar.getInstance();
                avgExec.set(0, 0, rs.getInt(4), rs.getInt(5), rs.getInt(6), rs.getInt(7));
                bean.setAvgExecutionTime(avgExec);
                Calendar avgWait = Calendar.getInstance();
                avgWait.set(0, 0, rs.getInt(8), rs.getInt(9), rs.getInt(10), rs.getInt(11));
                bean.setAvgWaitingTime(avgWait);
                Calendar avgGlobal = Calendar.getInstance();
                avgGlobal.set(0, 0, rs.getInt(12), rs.getInt(13), rs.getInt(14), rs.getInt(15));
                bean.setAvgGlobalTime(avgGlobal);
                result.add(bean);
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            JdbcDAOUtils.closeResultSet(rs);
            JdbcDAOUtils.closePrepareStatement(pstmt);
            JdbcDAOUtils.closeConnection(conn);
        }
        return result;
    }

    public Collection retrieveProcessInstanceData(String processId) {
        Collection result = new ArrayList();
        Connection conn = JdbcDAOUtils.getConnection(this, Constants.UNIFACE_FLOW_DS_NAME);
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            String query =
                    "SELECT UOBJID, UOBJNAME, UCREATED, USTARTED, UENDED," +
                    " trunc((uended-ustarted))," +
                    " trunc( mod( (uended-ustarted)*24, 24 ) )," +
                    " trunc( mod( (uended-ustarted)*24*60, 60 ) )," +
                    " trunc( mod( (uended-ustarted)*24*60*60, 60 ) )," +
                    " trunc((ustarted-ucreated))," +
                    " trunc( mod( (ustarted-ucreated)*24, 24 ) )," +
                    " trunc( mod( (ustarted-ucreated)*24*60, 60 ) )," +
                    " trunc( mod( (ustarted-ucreated)*24*60*60, 60 ) )," +
                    " trunc((uended-ucreated))," +
                    " trunc( mod( (uended-ucreated)*24, 24 ) )," +
                    " trunc( mod( (uended-ucreated)*24*60, 60 ) )," +
                    " trunc( mod( (uended-ucreated)*24*60*60, 60 ) )" +
                    " FROM UWPRCI" +
                    " WHERE UPRCITYPE = 'Process'" +
                    " AND USTATE = 'Processed'" +
                    " AND UPROCID = '"+processId+"'";
            pstmt = conn.prepareStatement(query);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                ProcessTaskBean bean = new ProcessTaskBean();
                bean.setId(rs.getString(1));
                bean.setName(rs.getString(2));
                bean.setCreateTime(rs.getTimestamp(3));
                bean.setStartTime(rs.getTimestamp(4));
                bean.setEndTime(rs.getTimestamp(5));
                Calendar exec = Calendar.getInstance();
                exec.set(0, 0, rs.getInt(6), rs.getInt(7), rs.getInt(8), rs.getInt(9));
                bean.setExecutionTime(exec);
                Calendar wait = Calendar.getInstance();
                wait.set(0, 0, rs.getInt(10), rs.getInt(11), rs.getInt(12), rs.getInt(13));
                bean.setWaitingTime(wait);
                Calendar global = Calendar.getInstance();
                global.set(0, 0, rs.getInt(14), rs.getInt(15), rs.getInt(16), rs.getInt(17));
                bean.setGlobalTime(global);
                result.add(bean);
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            JdbcDAOUtils.closeResultSet(rs);
            JdbcDAOUtils.closePrepareStatement(pstmt);
            JdbcDAOUtils.closeConnection(conn);
        }
        return result;
    }

    public Collection retrieveProcessTaskData(String processId) {
        Collection result = new ArrayList();
        Connection conn = JdbcDAOUtils.getConnection(this, Constants.UNIFACE_FLOW_DS_NAME);
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            String query =
                    "SELECT UOBJID, UOBJNAME, UCREATED, USTARTED, UENDED," +
                    " trunc((uended-ustarted))," +
                    " trunc( mod( (uended-ustarted)*24, 24 ) )," +
                    " trunc( mod( (uended-ustarted)*24*60, 60 ) )," +
                    " trunc( mod( (uended-ustarted)*24*60*60, 60 ) )," +
                    " trunc((ustarted-ucreated))," +
                    " trunc( mod( (ustarted-ucreated)*24, 24 ) )," +
                    " trunc( mod( (ustarted-ucreated)*24*60, 60 ) )," +
                    " trunc( mod( (ustarted-ucreated)*24*60*60, 60 ) )," +
                    " trunc((uended-ucreated))," +
                    " trunc( mod( (uended-ucreated)*24, 24 ) )," +
                    " trunc( mod( (uended-ucreated)*24*60, 60 ) )," +
                    " trunc( mod( (uended-ucreated)*24*60*60, 60 ) )" +
                    " FROM UWPRCI" +
                    " WHERE UPRCITYPE = 'Task'" +
                    " AND USTATE = 'Processed'" +
                    " AND UPROCINSTID = '"+processId+"'";
            pstmt = conn.prepareStatement(query);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                ProcessTaskBean bean = new ProcessTaskBean();
                bean.setId(rs.getString(1));
                bean.setName(rs.getString(2));
                bean.setCreateTime(rs.getTimestamp(3));
                bean.setStartTime(rs.getTimestamp(4));
                bean.setEndTime(rs.getTimestamp(5));
                Calendar exec = Calendar.getInstance();
                exec.set(0, 0, rs.getInt(6), rs.getInt(7), rs.getInt(8), rs.getInt(9));
                bean.setExecutionTime(exec);
                Calendar wait = Calendar.getInstance();
                wait.set(0, 0, rs.getInt(10), rs.getInt(11), rs.getInt(12), rs.getInt(13));
                bean.setWaitingTime(wait);
                Calendar global = Calendar.getInstance();
                global.set(0, 0, rs.getInt(14), rs.getInt(15), rs.getInt(16), rs.getInt(17));
                bean.setGlobalTime(global);
                result.add(bean);
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            JdbcDAOUtils.closeResultSet(rs);
            JdbcDAOUtils.closePrepareStatement(pstmt);
            JdbcDAOUtils.closeConnection(conn);
        }
        return result;
    }

}
