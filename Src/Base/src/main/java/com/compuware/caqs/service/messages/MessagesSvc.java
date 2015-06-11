package com.compuware.caqs.service.messages;

import java.sql.Timestamp;
import java.util.List;
import java.util.ResourceBundle;

import com.compuware.caqs.business.messages.MessagesListMgmt;
import com.compuware.caqs.domain.dataschemas.ElementBean;
import com.compuware.caqs.domain.dataschemas.tasks.CaqsMessageBean;
import com.compuware.caqs.domain.dataschemas.tasks.MessageStatus;
import com.compuware.caqs.domain.dataschemas.tasks.TaskId;

public class MessagesSvc {

    private static MessagesSvc instance = new MessagesSvc();

    private MessagesSvc() {
    }

    public static MessagesSvc getInstance() {
        return instance;
    }

    public String retrieveNotFinishedMessageId(String idElt, String idBline, TaskId task) {
        return MessagesListMgmt.getInstance().retrieveNotFinishedMessageId(idElt, idBline, task);
    }

    public void setInProgressMessageToStep(String messId, String stepMessRessource) {
        MessagesListMgmt.getInstance().setInProgressMessageToStep(messId, stepMessRessource);
    }

    public String getStepFromInProgressMessage(String key, ResourceBundle resources) {
        String retour = "";
        try {
            if (key != null) {
                retour = resources.getString(key);
            }
        } catch (java.util.MissingResourceException exc) {
            retour = key;
        }
        return retour;
    }

    public List<CaqsMessageBean> getMessageList(String idUser) {
        return MessagesListMgmt.getInstance().getMessageList(idUser);
    }

    public List<CaqsMessageBean> getSpecificTasksForUserAndEltId(TaskId taskId, String idElt, String idBline, String otherId, String userId) {
        return MessagesListMgmt.getInstance().getSpecificTasksForUserAndEltId(taskId, idElt, idBline, otherId, userId);
    }

    public void setMessageAsSeen(String idMess) {
        MessagesListMgmt.getInstance().setMessageAsSeen(idMess);
    }

    public void setAllMessageAsSeen(String userId) {
        MessagesListMgmt.getInstance().setAllMessageAsSeen(userId);
    }

    public void setMessageTaskStatus(String idMess, MessageStatus taskStatus) {
        MessagesListMgmt.getInstance().setMessageTaskStatus(idMess, taskStatus);
    }

    public String addMessage(TaskId id, String idBline, List<String> info1, String otherId, ElementBean eltBean, String userId) {
        return MessagesListMgmt.getInstance().addMessage(id, idBline, info1, otherId, eltBean, userId);
    }

    public String addMessage(TaskId id, String idElt, String idBline, String idUser, List<String> info1, String otherId) {
        return MessagesListMgmt.getInstance().addMessage(id, idElt, idBline, idUser, info1, otherId);
    }

    public String addMessage(TaskId id, String idElt, String idBline, String idUser, List<String> info1, String otherId, Timestamp startingDate) {
        return MessagesListMgmt.getInstance().addMessage(id, idElt, idBline, idUser, info1, otherId, startingDate);
    }

    public String addMessageWithStatus(TaskId id, String idElt, String idBline, String idUser, List<String> info1,
            String otherId, MessageStatus taskStatus) {
        return MessagesListMgmt.getInstance().addMessageWithStatus(id, idElt, idBline, idUser, info1, otherId, taskStatus);
    }

    public void addMessageAndSetCompleted(TaskId id, String idElt, String idBline, List<String> info1,
            String otherId, String userId) {
        MessagesListMgmt.getInstance().addMessageAndSetCompleted(id, idElt, idBline, info1, otherId, userId);
    }

    public void setMessagePercentage(String idMessage, int percent) {
        MessagesListMgmt.getInstance().setMessagePercentage(idMessage, percent);
    }

    public List<CaqsMessageBean> retrieveAllActionsToDoFor(TaskId taskId) {
        return MessagesListMgmt.getInstance().retrieveAllActionsToDoFor(taskId);
    }
}
