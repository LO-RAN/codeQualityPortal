package com.compuware.caqs.business.messages;

import com.compuware.caqs.constants.Constants;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.compuware.caqs.dao.factory.DaoFactory;
import com.compuware.caqs.dao.interfaces.CaqsMessageDao;
import com.compuware.caqs.domain.dataschemas.ElementBean;
import com.compuware.caqs.domain.dataschemas.tasks.CaqsMessageBean;
import com.compuware.caqs.domain.dataschemas.tasks.MessageStatus;
import com.compuware.caqs.domain.dataschemas.tasks.TaskId;

public class MessagesListMgmt {

    private static MessagesListMgmt instance = new MessagesListMgmt();

    private MessagesListMgmt() {
    }

    public static MessagesListMgmt getInstance() {
        return instance;
    }

    public String retrieveNotFinishedMessageId(String idEa, String idBline, TaskId task) {
        CaqsMessageDao messageDao = DaoFactory.getInstance().getCaqsMessagesDao();
        return messageDao.retrieveNotFinishedMessageId(idEa, idBline, task);
    }

    public void setInProgressMessageToStep(String messId, String stepMessRessource) {
        CaqsMessageDao messageDao = DaoFactory.getInstance().getCaqsMessagesDao();
        messageDao.setMessageSpecificInfo(messId, Constants.MESSAGES_STEP_INFO1+stepMessRessource);
    }
    
    public List<CaqsMessageBean> getMessageList(String idUser) {
        List<CaqsMessageBean> retour = new ArrayList<CaqsMessageBean>();
        CaqsMessageDao messageDao = DaoFactory.getInstance().getCaqsMessagesDao();
        if (idUser != null) {
            retour = messageDao.retrieveMessagesForUserToDisplay(idUser);
        }
        return retour;
    }

    public List<CaqsMessageBean> getSpecificTasksForUserAndEltId(TaskId taskId, String idElt, String idBline, String otherId, String userId) {
        List<CaqsMessageBean> retour = new ArrayList<CaqsMessageBean>();

        CaqsMessageDao messageDao = DaoFactory.getInstance().getCaqsMessagesDao();
        if (userId != null) {
            retour = messageDao.specificTasksNotSeenByUserForElement(taskId, idElt, idBline, userId, otherId);
        }
        return retour;
    }

    public void setMessageAsSeen(String idMess) {
        CaqsMessageDao messageDao = DaoFactory.getInstance().getCaqsMessagesDao();
        messageDao.setMessageAsSeen(idMess);
    }

    public void setAllMessageAsSeen(String userId) {
        CaqsMessageDao messageDao = DaoFactory.getInstance().getCaqsMessagesDao();
        messageDao.setAllMessageAsSeen(userId);
    }

    public void setMessageTaskStatus(String idMess, MessageStatus taskStatus) {
        CaqsMessageDao messageDao = DaoFactory.getInstance().getCaqsMessagesDao();
        messageDao.setMessageTaskStatus(idMess, taskStatus);
    }

    public String addMessage(TaskId id, String idBline, List<String> info1, String otherId,
            ElementBean eltBean, String userId) {
        CaqsMessageDao messageDao = DaoFactory.getInstance().getCaqsMessagesDao();
        return messageDao.createMessage(id, eltBean.getId(), idBline, userId, info1, otherId);
    }

    public String addMessage(TaskId id, String idElt, String idBline, String idUser, List<String> info1,
            String otherId) {
        if (idElt == null) {
            idElt = Constants.TASK_ON_ALL_ELEMENTS;
        }
        if (idBline == null) {
            idBline = Constants.TASK_ON_ALL_BASELINES;
        }
        CaqsMessageDao messageDao = DaoFactory.getInstance().getCaqsMessagesDao();
        return messageDao.createMessage(id, idElt, idBline, idUser, info1, otherId);
    }

    public String addMessage(TaskId id, String idElt, String idBline,
            String idUser, List<String> info1, String otherId,
            Timestamp startingDate) {
        if (idElt == null) {
            idElt = Constants.TASK_ON_ALL_ELEMENTS;
        }
        if (idBline == null) {
            idBline = Constants.TASK_ON_ALL_BASELINES;
        }
        CaqsMessageDao messageDao = DaoFactory.getInstance().getCaqsMessagesDao();
        return messageDao.createMessage(id, idElt, idBline, idUser, info1, otherId, startingDate);
    }

    public String addMessageWithStatus(TaskId id, String idElt, String idBline, String idUser, List<String> info1,
            String otherId, MessageStatus taskStatus) {
        if (idElt == null) {
            idElt = Constants.TASK_ON_ALL_ELEMENTS;
        }
        if (idBline == null) {
            idBline = Constants.TASK_ON_ALL_BASELINES;
        }
        CaqsMessageDao messageDao = DaoFactory.getInstance().getCaqsMessagesDao();
        return messageDao.createMessageWithStatus(id, idElt, idBline, idUser, info1, otherId, taskStatus);
    }

    public void addMessageAndSetCompleted(TaskId id, String idElt, String idBline, List<String> info1, String otherId, String userId) {
        if (idElt == null) {
            idElt = Constants.TASK_ON_ALL_ELEMENTS;
        }
        CaqsMessageDao messageDao = DaoFactory.getInstance().getCaqsMessagesDao();
        messageDao.createMessageWithStatus(id, idElt, idBline, userId, info1, otherId,
                MessageStatus.COMPLETED);
    }

    public void setMessagePercentage(String idMessage, int percent) {
        CaqsMessageDao messageDao = DaoFactory.getInstance().getCaqsMessagesDao();
        messageDao.setMessagePercentage(idMessage, percent);
    }

    public List<CaqsMessageBean> retrieveAllActionsToDoFor(TaskId taskId) {
        CaqsMessageDao messageDao = DaoFactory.getInstance().getCaqsMessagesDao();
        return messageDao.retrieveAllActionsToDoFor(taskId);
    }
}
