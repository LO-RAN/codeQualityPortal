package com.compuware.caqs.dao.dbms;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import com.compuware.caqs.constants.Constants;
import com.compuware.caqs.dao.interfaces.CaqsMessageDao;
import com.compuware.caqs.domain.dataschemas.ElementBean;
import com.compuware.caqs.domain.dataschemas.ProjectBean;
import com.compuware.caqs.domain.dataschemas.tasks.CaqsMessageBean;
import com.compuware.caqs.domain.dataschemas.tasks.MessageStatus;
import com.compuware.caqs.domain.dataschemas.tasks.ShowUserTask;
import com.compuware.caqs.domain.dataschemas.tasks.TaskBean;
import com.compuware.caqs.domain.dataschemas.tasks.TaskId;
import com.compuware.caqs.domain.dataschemas.tasks.TaskType;
import com.compuware.caqs.util.IDCreator;
import com.compuware.toolbox.dbms.JdbcDAOUtils;
import com.compuware.toolbox.util.logging.LoggerManager;

public class CaqsMessageDbmsDao implements CaqsMessageDao {

    private static org.apache.log4j.Logger logger = LoggerManager.getLogger(Constants.LOG4J_DBMS_LOGGER_KEY);
    protected static DataAccessCache dataCache = DataAccessCache.getInstance();
    private static CaqsMessageDbmsDao singleton = new CaqsMessageDbmsDao();

    public static CaqsMessageDbmsDao getInstance() {
        return CaqsMessageDbmsDao.singleton;
    }

    /** Creates a new instance of Class */
    private CaqsMessageDbmsDao() {
    }
    /**
     * requete declarant comme lus tous les messages commences ou finis
     */
    private static final String SET_ALL_MESSAGES_AS_SEEN_QUERY =
            "UPDATE CAQS_MESSAGES SET seen = 1 WHERE seen = 0 AND id_user = ?"
            + " AND status != 'NOT_STARTED'";

    /**
     * @{@inheritDoc }
     */
    public void setAllMessageAsSeen(String idUser) {
        Connection connection = JdbcDAOUtils.getConnection(this, Constants.CAQS_DATASOURCE_KEY);
        PreparedStatement blstmt = null;
        try {
            blstmt = connection.prepareStatement(SET_ALL_MESSAGES_AS_SEEN_QUERY);
            blstmt.setString(1, idUser);
            blstmt.executeUpdate();
            JdbcDAOUtils.commit(connection);
        } catch (SQLException e) {
            logger.error("Aucun message cree :" + e.toString());
            JdbcDAOUtils.rollbackConnection(connection);
        } finally {
            JdbcDAOUtils.closePrepareStatement(blstmt);
            JdbcDAOUtils.closeConnection(connection);
        }
    }
    /**
     * affiche tous les messages pour un utilisateur :
     * - non lus
     * -    definis devant �tre toujours affiches et commences ou finis
     * -    ou commences et finis et definis comme devant etre affiches quand termines
     * - places sur un element et/ou baseline en particulier
     */
    private static final String RETRIEVE_MESSAGES_FOR_USER_TO_DISPLAY_QUERY =
            "SELECT c.id_message, c.id_elt, c.id_bline, c.id_user, c.id_task, elt.lib_elt, "
            + "    t.type_task, c.status, c.end_date, c.begin_date, c.info1, c.otherId, c.completion_pct, p.id_pro, p.lib_pro"
            + " FROM CAQS_MESSAGES c, Element elt, CAQS_TASKS t, PROJET p"
            + " WHERE c.id_user = ?" + " AND c.seen = 0"
            + " AND c.id_elt=elt.id_elt" + " AND c.id_task=t.id_task" + " AND ("
            + "  (t.show_user='" + ShowUserTask.ALWAYS + "' AND c.STATUS != '"
            + MessageStatus.NOT_STARTED + "')" +//les messages qui doivent toujours etre montres
            "  OR (" + "				t.show_user='" + ShowUserTask.WHEN_FINISHED
            + "' AND (c.STATUS='" + MessageStatus.COMPLETED + "'"
            + "                          OR c.STATUS='" + MessageStatus.FAILED
            + "')" + "  )" +//les messages qui ne doivent etre montres que quand ils sont finis
            ")" + " AND elt.id_pro=p.id_pro" + " ORDER BY END_DATE";
    /**
     * affiche tous les messages pour un utilisateur :
     * - non lus
     * -    definis devant �tre toujours affiches et commences ou finis
     * -    ou commences et finis et definis comme devant etre affiches quand termines
     * - places aucun element en particulier
     */
    private static final String RETRIEVE_MESSAGES_ALL_ELTS_FOR_USER_TO_DISPLAY_QUERY =
            "SELECT c.id_message, c.id_elt, c.id_bline, c.id_user, c.id_task, "
            + "    t.type_task, c.status, c.end_date, c.begin_date, c.info1, c.otherId, c.completion_pct"
            + " FROM CAQS_MESSAGES c, CAQS_TASKS t" + " WHERE c.id_user = ?"
            + " AND c.seen = 0" + " AND c.id_elt = '"
            + Constants.TASK_ON_ALL_ELEMENTS + "'" + " AND c.id_task=t.id_task"
            + " AND (" + "  (t.show_user='" + ShowUserTask.ALWAYS
            + "' AND c.STATUS != '" + MessageStatus.NOT_STARTED + "')" +//les messages qui doivent toujours etre montres
            "  OR (" + "				t.show_user='" + ShowUserTask.WHEN_FINISHED
            + "' AND (c.STATUS='" + MessageStatus.COMPLETED + "'"
            + "                          OR c.STATUS='" + MessageStatus.FAILED
            + "')" + "	)" +//les messages qui ne doivent etre montres que quand ils sont finis
            " )" + " ORDER BY END_DATE";

    public List<CaqsMessageBean> retrieveMessagesForUserToDisplay(java.lang.String userId) {
        List<CaqsMessageBean> result = new ArrayList<CaqsMessageBean>();
        Connection connection = JdbcDAOUtils.getConnection(this, Constants.CAQS_DATASOURCE_KEY);
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        logger.trace("Retrieve message for user : " + userId);
        try {
            pstmt = connection.prepareStatement(RETRIEVE_MESSAGES_FOR_USER_TO_DISPLAY_QUERY);
            pstmt.setString(1, userId);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                CaqsMessageBean line = new CaqsMessageBean();
                line.setIdMessage(rs.getString("id_message"));
                line.setIdBline(rs.getString("id_bline"));
                line.setIdUser(rs.getString("id_user"));

                ElementBean elt = new ElementBean();
                elt.setId(rs.getString("id_elt"));
                elt.setLib(rs.getString("lib_elt"));
                line.setEltBean(elt);

                TaskBean task = new TaskBean();
                task.setId(TaskId.valueOf(TaskId.class, rs.getString("id_task")));
                task.setType(TaskType.valueOf(TaskType.class, rs.getString("type_task")));
                line.setTask(task);

                line.setStatus(MessageStatus.valueOf(rs.getString("status")));
                line.setEndDate(rs.getTimestamp("end_date"));
                line.setBeginDate(rs.getTimestamp("begin_date"));

                line.setInfo1(rs.getString("info1"));
                line.setOtherId(rs.getString("otherId"));
                line.setPercent(rs.getFloat("completion_pct"));

                ProjectBean pb = new ProjectBean();
                pb.setId(rs.getString("id_pro"));
                pb.setLib(rs.getString("lib_pro"));
                elt.setProject(pb);

                result.add(line);
            }

            JdbcDAOUtils.closeResultSet(rs);
            JdbcDAOUtils.closePrepareStatement(pstmt);

            pstmt = connection.prepareStatement(RETRIEVE_MESSAGES_ALL_ELTS_FOR_USER_TO_DISPLAY_QUERY);
            pstmt.setString(1, userId);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                CaqsMessageBean line = new CaqsMessageBean();
                line.setIdMessage(rs.getString("id_message"));
                line.setIdBline(rs.getString("id_bline"));
                line.setIdUser(rs.getString("id_user"));

                TaskBean task = new TaskBean();
                task.setId(TaskId.valueOf(TaskId.class, rs.getString("id_task")));
                task.setType(TaskType.valueOf(TaskType.class, rs.getString("type_task")));
                line.setTask(task);

                line.setStatus(MessageStatus.valueOf(rs.getString("status")));
                line.setEndDate(rs.getTimestamp("end_date"));
                line.setBeginDate(rs.getTimestamp("begin_date"));

                line.setInfo1(rs.getString("info1"));
                line.setOtherId(rs.getString("otherId"));
                line.setPercent(rs.getFloat("completion_pct"));

                result.add(line);
            }
        } catch (SQLException e) {
            logger.error("Error during Messages retrieve", e);
        } finally {
            JdbcDAOUtils.closeResultSet(rs);
            JdbcDAOUtils.closePrepareStatement(pstmt);
            JdbcDAOUtils.closeConnection(connection);
        }
        return result;
    }
    private static final String CREATE_MESSAGE_QUERY =
            "INSERT INTO CAQS_MESSAGES (ID_MESSAGE,ID_ELT,ID_BLINE,ID_USER,ID_TASK,STATUS,BEGIN_DATE,END_DATE,COMPLETION_PCT,SEEN,INFO1,OTHERID)"
            + "VALUES (?,?,?,?,?,?,?,null,0,0,?,?)";

    /**
     * @{@inheritDoc }
     */
    public String createMessage(TaskId task, String idElt, String idBline,
            String idUser, List<String> infos, String otherId) {
        String idMsg = IDCreator.getID();
        Connection connection = JdbcDAOUtils.getConnection(this, Constants.CAQS_DATASOURCE_KEY);
        PreparedStatement blstmt = null;
        try {
            blstmt = connection.prepareStatement(CREATE_MESSAGE_QUERY);
            blstmt.setString(1, idMsg);
            if (idElt != null) {
                blstmt.setString(2, idElt);
            } else {
                blstmt.setNull(2, java.sql.Types.VARCHAR);
            }
            blstmt.setString(3, idBline);
            blstmt.setString(4, idUser);
            blstmt.setString(5, task.toString());
            blstmt.setString(6, MessageStatus.NOT_STARTED.toString());
            blstmt.setTimestamp(7, new Timestamp(Calendar.getInstance().getTimeInMillis()));

            String s = "";
            if (infos != null) {
                for (Iterator<String> it = infos.iterator(); it.hasNext();) {
                    s += it.next();
                    if (it.hasNext()) {
                        s += CaqsMessageBean.INFO1_SEPARATOR;
                    }
                }
            }

            blstmt.setString(8, s);
            blstmt.setString(9, otherId);
            blstmt.executeUpdate();
            JdbcDAOUtils.commit(connection);
        } catch (SQLException e) {
            logger.error("Aucun message cree :" + e.toString());
            JdbcDAOUtils.rollbackConnection(connection);
            idMsg = null;
        } finally {
            JdbcDAOUtils.closePrepareStatement(blstmt);
            JdbcDAOUtils.closeConnection(connection);
        }
        return idMsg;
    }
    /**
     * met a jour le champ info1 pour un message donne
     */
    private static final String SET_MESSAGE_INFOS_QUERY =
            "UPDATE CAQS_MESSAGES" + " SET INFO1=?" + " WHERE ID_MESSAGE=?";
    private static final String GET_MESSAGE_INFOS_QUERY =
            "SELECT INFO1 FROM CAQS_MESSAGES" + " WHERE ID_MESSAGE=?";

    /**
     * @{@inheritDoc }
     */
    public void setMessageSpecificInfo(String idMess, String info) {
        Connection connection = JdbcDAOUtils.getConnection(this, Constants.CAQS_DATASOURCE_KEY);
        PreparedStatement blstmt = null;
        ResultSet rs = null;
        try {
            String[] oldInfos = null;
            String[] newInfos = null;
            blstmt = connection.prepareStatement(GET_MESSAGE_INFOS_QUERY);
            blstmt.setString(1, idMess);
            rs = blstmt.executeQuery();

            boolean found = false;
            if (rs != null && rs.next()) {
                String temp = rs.getString("info1");
                if (temp != null) {
                    oldInfos = temp.split(CaqsMessageBean.INFO1_SEPARATOR);
                    if (oldInfos != null) {
                        String prefix = "";
                        String[] tabPrefix = info.split("=");
                        if (tabPrefix != null && tabPrefix.length > 0) {
                            prefix = tabPrefix[0];
                        }
                        for (int i = 0; i < oldInfos.length; i++) {
                            String thisPrefix = "";
                            String[] thisTabPrefix = oldInfos[i].split("=");
                            if (thisTabPrefix != null && thisTabPrefix.length
                                    > 0) {
                                thisPrefix = thisTabPrefix[0];
                            }
                            if (prefix.equals(thisPrefix)) {
                                //meme prefixe, on met a jour ce champ
                                oldInfos[i] = info;
                                found = true;
                                newInfos = oldInfos;
                                break;
                            }
                        }
                    }

                }
            }
            JdbcDAOUtils.closeResultSet(rs);
            JdbcDAOUtils.closePrepareStatement(blstmt);
            if (!found) {
                int newlength = (oldInfos == null) ? 1 : oldInfos.length + 1;
                newInfos = new String[newlength];
                if (newlength > 1) {
                    System.arraycopy(oldInfos, 0, newInfos, 0, oldInfos.length);
                }
                newInfos[newlength - 1] = info;
            }

            blstmt = connection.prepareStatement(SET_MESSAGE_INFOS_QUERY);

            String s = "";
            if (newInfos != null) {
                for (int i = 0; i < newInfos.length; i++) {
                    if (CaqsMessageBean.INFO1_SEPARATOR.equals(newInfos[i])) {
                        continue;
                    }
                    s += newInfos[i];
                    if (i < (newInfos.length - 1)) {
                        s += CaqsMessageBean.INFO1_SEPARATOR;
                    }
                }
            }
            blstmt.setString(1, s);
            blstmt.setString(2, idMess);
            blstmt.executeUpdate();
            JdbcDAOUtils.commit(connection);
        } catch (SQLException e) {
            logger.error("Aucun message mis a jour :" + e.toString());
            JdbcDAOUtils.rollbackConnection(connection);
        } finally {
            JdbcDAOUtils.closePrepareStatement(blstmt);
            JdbcDAOUtils.closeConnection(connection);
        }
    }

    /**
     * @{@inheritDoc }
     */
    public String createMessage(TaskId task, String idElt, String idBline,
            String idUser, List<String> infos, String otherId, Timestamp startingDate) {
        String idMsg = IDCreator.getID();
        Connection connection = JdbcDAOUtils.getConnection(this, Constants.CAQS_DATASOURCE_KEY);
        PreparedStatement blstmt = null;
        try {
            blstmt = connection.prepareStatement(CREATE_MESSAGE_QUERY);
            blstmt.setString(1, idMsg);
            if (idElt != null) {
                blstmt.setString(2, idElt);
            } else {
                blstmt.setNull(2, java.sql.Types.VARCHAR);
            }
            blstmt.setString(3, idBline);
            blstmt.setString(4, idUser);
            blstmt.setString(5, task.toString());
            blstmt.setString(6, MessageStatus.NOT_STARTED.toString());
            if (startingDate != null) {
                blstmt.setTimestamp(7, startingDate);
            } else {
                blstmt.setNull(7, java.sql.Types.TIMESTAMP);
            }

            String s = "";
            if (infos != null) {
                for (Iterator<String> it = infos.iterator(); it.hasNext();) {
                    s += it.next();
                    if (it.hasNext()) {
                        s += ",";
                    }
                }
            }

            blstmt.setString(8, s);
            blstmt.setString(9, otherId);
            blstmt.executeUpdate();
            JdbcDAOUtils.commit(connection);
        } catch (SQLException e) {
            logger.error("Aucun message cree :" + e.toString());
            JdbcDAOUtils.rollbackConnection(connection);
            idMsg = null;
        } finally {
            JdbcDAOUtils.closePrepareStatement(blstmt);
            JdbcDAOUtils.closeConnection(connection);
        }
        return idMsg;
    }

    public String createMessageWithStatus(TaskId task, String idElt, String idBline, String idUser,
            List<String> infos, String otherId, MessageStatus taskStatus) {
        String idMsg = IDCreator.getID();
        Connection connection = JdbcDAOUtils.getConnection(this, Constants.CAQS_DATASOURCE_KEY);
        PreparedStatement blstmt = null;
        try {
            blstmt = connection.prepareStatement(CREATE_MESSAGE_QUERY);
            blstmt.setString(1, idMsg);
            blstmt.setString(2, idElt);
            blstmt.setString(3, idBline);
            blstmt.setString(4, idUser);
            blstmt.setString(5, task.toString());
            blstmt.setString(6, taskStatus.toString());
            blstmt.setTimestamp(7, new Timestamp(Calendar.getInstance().getTimeInMillis()));

            String s = "";
            if (infos != null) {
                for (Iterator<String> it = infos.iterator(); it.hasNext();) {
                    s += it.next();
                    if (it.hasNext()) {
                        s += ",";
                    }
                }
            }

            blstmt.setString(8, s);
            blstmt.setString(9, otherId);
            blstmt.executeUpdate();
            JdbcDAOUtils.commit(connection);
        } catch (SQLException e) {
            logger.error("Aucun message cree :" + e.toString());
            JdbcDAOUtils.rollbackConnection(connection);
            idMsg = null;
        } finally {
            JdbcDAOUtils.closePrepareStatement(blstmt);
            JdbcDAOUtils.closeConnection(connection);
        }
        return idMsg;
    }
    private static final String SET_MESSAGE_AS_SEEN_QUERY =
            "UPDATE CAQS_MESSAGES" + " SET SEEN=1" + " WHERE ID_MESSAGE=?";

    /**
     * @{@inheritDoc }
     */
    public void setMessageAsSeen(String idMess) {
        Connection connection = JdbcDAOUtils.getConnection(this, Constants.CAQS_DATASOURCE_KEY);
        PreparedStatement blstmt = null;
        try {
            blstmt = connection.prepareStatement(SET_MESSAGE_AS_SEEN_QUERY);
            blstmt.setString(1, idMess);
            blstmt.executeUpdate();
            JdbcDAOUtils.commit(connection);
        } catch (SQLException e) {
            logger.error("Probleme lors de setMessageAsSeen :" + e.toString());
            JdbcDAOUtils.rollbackConnection(connection);
        } finally {
            JdbcDAOUtils.closePrepareStatement(blstmt);
            JdbcDAOUtils.closeConnection(connection);
        }
    }
    private static final String SET_TASK_COMPLETED_QUERY =
            "UPDATE CAQS_MESSAGES" + " SET STATUS=?, END_DATE=?, SEEN=0"
            + " WHERE ID_MESSAGE=?";

    public void setMessageTaskStatus(String idMess, MessageStatus taskStatus) {
        Connection connection = JdbcDAOUtils.getConnection(this, Constants.CAQS_DATASOURCE_KEY);
        PreparedStatement blstmt = null;
        try {
            blstmt = connection.prepareStatement(SET_TASK_COMPLETED_QUERY);
            blstmt.setString(1, taskStatus.toString());
            if (MessageStatus.COMPLETED.equals(taskStatus)
                    || MessageStatus.FAILED.equals(taskStatus)) {
                blstmt.setTimestamp(2, new Timestamp(Calendar.getInstance().getTimeInMillis()));
            } else {
                blstmt.setNull(2, java.sql.Types.TIMESTAMP);
            }
            blstmt.setString(3, idMess);
            blstmt.executeUpdate();
            JdbcDAOUtils.commit(connection);
        } catch (SQLException e) {
            logger.error("Probleme lors de la mise a jour de la tache :"
                    + e.toString());
            JdbcDAOUtils.rollbackConnection(connection);
        } finally {
            JdbcDAOUtils.closePrepareStatement(blstmt);
            JdbcDAOUtils.closeConnection(connection);
        }
    }
    private static final String SPECIFIC_TASKS_NOT_SEEN_BY_USER_FOR_ELEMENT_QUERY =
            "SELECT c.id_message" + " FROM CAQS_MESSAGES c"
            + " WHERE c.id_user = ?" + " AND c.seen = 0" + " AND c.id_elt=?"
            + " AND c.id_task=?" + " AND c.id_bline=?";

    public List<CaqsMessageBean> specificTasksNotSeenByUserForElement(TaskId taskId, String eltId, String idBline, String userId, String otherId) {
        List<CaqsMessageBean> result = new ArrayList<CaqsMessageBean>();
        Connection connection = JdbcDAOUtils.getConnection(this, Constants.CAQS_DATASOURCE_KEY);
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        logger.trace("Retrieve message for user : " + userId);
        try {
            String query = SPECIFIC_TASKS_NOT_SEEN_BY_USER_FOR_ELEMENT_QUERY;
            if (otherId != null) {
                query += " AND c.otherId = '" + otherId + "'";
            }

            pstmt = connection.prepareStatement(query);
            pstmt.setString(1, userId);
            pstmt.setString(2, eltId);
            pstmt.setString(3, taskId.toString());
            pstmt.setString(4, idBline);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                CaqsMessageBean line = new CaqsMessageBean();
                line.setIdMessage(rs.getString("id_message"));
                result.add(line);
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
    /**
     * met a jour le pourcentage pour un message donne
     */
    private static final String SET_MESSAGE_PERCENTAGE_QUERY =
            "UPDATE CAQS_MESSAGES" + " SET COMPLETION_PCT=?, SEEN=0"
            + " WHERE ID_MESSAGE=?";

    /**
     * @{@inheritDoc}
     */
    public void setMessagePercentage(String idMess, int percent) {
        Connection connection = JdbcDAOUtils.getConnection(this, Constants.CAQS_DATASOURCE_KEY);
        PreparedStatement blstmt = null;
        try {
            blstmt = connection.prepareStatement(SET_MESSAGE_PERCENTAGE_QUERY);
            blstmt.setInt(1, percent);
            blstmt.setString(2, idMess);
            blstmt.executeUpdate();
            JdbcDAOUtils.commit(connection);
        } catch (SQLException e) {
            logger.error("Probleme lors de la mise a jour du pourcentage :"
                    + e.toString());
            JdbcDAOUtils.rollbackConnection(connection);
        } finally {
            JdbcDAOUtils.closePrepareStatement(blstmt);
            JdbcDAOUtils.closeConnection(connection);
        }
    }
    private static final String RETRIEVE_ALL_ACTIONS_TO_START_FOR_TASK =
            "SELECT id_message, id_elt, id_bline " + " FROM CAQS_MESSAGES "
            + " WHERE status = ?" + " AND id_task = ?";

    /**
     * @{@inheritDoc }
     */
    public List<CaqsMessageBean> retrieveAllActionsToDoFor(TaskId taskId) {
        List<CaqsMessageBean> result = new ArrayList<CaqsMessageBean>();
        Connection connection = JdbcDAOUtils.getConnection(this, Constants.CAQS_DATASOURCE_KEY);
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = connection.prepareStatement(RETRIEVE_ALL_ACTIONS_TO_START_FOR_TASK);
            pstmt.setString(1, MessageStatus.NOT_STARTED.toString());
            pstmt.setString(2, taskId.toString());
            rs = pstmt.executeQuery();
            while (rs.next()) {
                CaqsMessageBean line = new CaqsMessageBean();
                line.setIdMessage(rs.getString("id_message"));
                line.setIdBline(rs.getString("id_bline"));
                ElementBean eltBean = ElementDbmsDao.getInstance().retrieveElementById(rs.getString("id_elt"));
                line.setEltBean(eltBean);
                result.add(line);
            }
        } catch (SQLException e) {
            logger.error("Error during message retrieve", e);
        } finally {
            JdbcDAOUtils.closeResultSet(rs);
            JdbcDAOUtils.closePrepareStatement(pstmt);
            JdbcDAOUtils.closeConnection(connection);
        }
        return result;
    }
    private static final String RETRIEVE_ALL_ACTIONS_TO_START =
            "SELECT id_message, id_elt, id_bline " + " FROM CAQS_MESSAGES "
            + " WHERE status = ?" + " AND begin_date < ?";

    /**
     * @{@inheritDoc }
     */
    public List<CaqsMessageBean> retrieveAllActionsToDo() {
        List<CaqsMessageBean> result = new ArrayList<CaqsMessageBean>();
        Connection connection = JdbcDAOUtils.getConnection(this, Constants.CAQS_DATASOURCE_KEY);
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = connection.prepareStatement(RETRIEVE_ALL_ACTIONS_TO_START);
            pstmt.setString(1, MessageStatus.NOT_STARTED.toString());
            Timestamp now = new Timestamp(Calendar.getInstance().getTimeInMillis());
            pstmt.setTimestamp(2, now);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                CaqsMessageBean line = new CaqsMessageBean();
                line.setIdMessage(rs.getString("id_message"));
                line.setIdBline(rs.getString("id_bline"));
                ElementBean eltBean = ElementDbmsDao.getInstance().retrieveElementById(rs.getString("id_elt"));
                line.setEltBean(eltBean);
                result.add(line);
            }
        } catch (SQLException e) {
            logger.error("Error during message retrieve", e);
        } finally {
            JdbcDAOUtils.closeResultSet(rs);
            JdbcDAOUtils.closePrepareStatement(pstmt);
            JdbcDAOUtils.closeConnection(connection);
        }
        return result;
    }
    
    private static final String RETRIEVE_NOT_FINISHED_MSG_ID_BY_BASELINE_AND_TASK =
            "SELECT id_message " + " FROM CAQS_MESSAGES "
            + " WHERE status NOT IN ('COMPLETED', 'FAILED') "
            + " AND id_task = ? AND id_bline = ?";

    /**
     * retrieves a message id, if there is a task not finished (nor completed
     * neither failed) for this taskid and this baseline id
     * @param idBline baseline id
     * @param task task id
     * @return the message id if there is one, null otherwise
     */
    private String retrieveNotFinishedMessageIdByBaselineAndTask(String idBline, TaskId task) {
        String result = null;
        Connection connection = JdbcDAOUtils.getConnection(this, Constants.CAQS_DATASOURCE_KEY);
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = connection.prepareStatement(RETRIEVE_NOT_FINISHED_MSG_ID_BY_BASELINE_AND_TASK);
            pstmt.setString(1, task.toString());
            pstmt.setString(2, idBline);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                result = rs.getString("id_message");
            }
        } catch (SQLException e) {
            logger.error("Error during message retrieve", e);
        } finally {
            JdbcDAOUtils.closeResultSet(rs);
            JdbcDAOUtils.closePrepareStatement(pstmt);
            JdbcDAOUtils.closeConnection(connection);
        }
        return result;
    }

    private static final String RETRIEVE_NOT_FINISHED_MSG_ID =
            "SELECT id_message " + " FROM CAQS_MESSAGES "
            + " WHERE status NOT IN ('COMPLETED', 'FAILED') "
            + " AND id_task = ? AND id_bline = ? AND id_elt = ?";

    /**
     * @{@inheritDoc }
     */
    public String retrieveNotFinishedMessageId(String idEa, String idBline, TaskId task) {
        String result = null;
        if (idEa == null) {
            result = this.retrieveNotFinishedMessageIdByBaselineAndTask(idBline, task);
        } else {
            Connection connection = JdbcDAOUtils.getConnection(this, Constants.CAQS_DATASOURCE_KEY);
            PreparedStatement pstmt = null;
            ResultSet rs = null;
            try {
                pstmt = connection.prepareStatement(RETRIEVE_NOT_FINISHED_MSG_ID);
                pstmt.setString(1, task.toString());
                pstmt.setString(2, idBline);
                pstmt.setString(3, idEa);
                rs = pstmt.executeQuery();
                if (rs.next()) {
                    result = rs.getString("id_message");
                }
            } catch (SQLException e) {
                logger.error("Error during message retrieve", e);
            } finally {
                JdbcDAOUtils.closeResultSet(rs);
                JdbcDAOUtils.closePrepareStatement(pstmt);
                JdbcDAOUtils.closeConnection(connection);
            }
        }
        return result;
    }
}
