package com.compuware.caqs.dao.interfaces;

import java.sql.Timestamp;
import java.util.List;

import com.compuware.caqs.domain.dataschemas.tasks.CaqsMessageBean;
import com.compuware.caqs.domain.dataschemas.tasks.MessageStatus;
import com.compuware.caqs.domain.dataschemas.tasks.TaskId;

public interface CaqsMessageDao {

    public List<CaqsMessageBean> retrieveMessagesForUserToDisplay(java.lang.String userId);

    /**
     * Cree une tache de type <code>task</code>. La declare non commencee. Place la date de debut a maintenant.
     * @param task type de la tache
     * @param idElt identifiant de l'element sur lequel elle porte
     * @param idBline identifiant de la baseline sur laquelle elle porte
     * @param idUser identifiant de l'utilisateur concerne
     * @param infos autres informations
     * @param otherId autre identifiant utile
     * @return identifiant du message cree
     */
    public String createMessage(TaskId task, String idElt, String idBline, String idUser, List<String> infos, String otherId);

    /**
     * Cree une tache de type <code>task</code>. La declare non commencee.
     * @param task type de la tache
     * @param idElt identifiant de l'element sur lequel elle porte
     * @param idBline identifiant de la baseline sur laquelle elle porte
     * @param idUser identifiant de l'utilisateur concerne
     * @param infos autres informations
     * @param otherId autre identifiant utile
     * @param startingDate la date de debut. peut etre null.
     * @return identifiant du message cree
     */
    public String createMessage(TaskId task, String idElt, String idBline, String idUser, List<String> infos, String otherId, Timestamp startingDate);

    /**
     * defini un message comme lu
     * @param idMess identifiant du message
     */
    public void setMessageAsSeen(String idMess);

    /**
     * defini tous les messages commences ou finis comme lus pour un utilisateur
     * donne
     * @param idUser identifiant de l'utilisateur
     */
    public void setAllMessageAsSeen(String idUser);

    /**
     * Met a jour le pourcentage d'avancement pour un message
     * @param idMess identifiant du message a mettre a jour
     * @param percent nouveau pourcentage d'avancement
     */
    public void setMessageTaskStatus(String idMess, MessageStatus taskStatus);

    public List<CaqsMessageBean> specificTasksNotSeenByUserForElement(TaskId taskId, String eltId, String idBline, java.lang.String userId, String otherId);

    public String createMessageWithStatus(TaskId task, String idElt, String idBline, String idUser, List<String> infos, String otherId, MessageStatus taskStatus);

    public void setMessagePercentage(String idMess, int percent);

    /**
     * renvoie toutes les actions a effectuer pour une tache particuliere. par exemple,
     * tous les elements a supprimer.
     * @param taskId le type de tache a effectuer
     * @return les actions a faire.
     */
    public List<CaqsMessageBean> retrieveAllActionsToDoFor(TaskId taskId);

    /**
     * renvoie toutes les actions a effectuer, c'est-a-dire toutes les actions
     * dont la date de depart est dans le passe et qui ne sont pas commencees.
     * @return les actions a faire.
     */
    public List<CaqsMessageBean> retrieveAllActionsToDo();

    /**
     * updates the field info1 adding thisn new info, or updating the same info
     * with the new content
     * @param idMess message id
     * @param info info
     */
    public void setMessageSpecificInfo(String idMess, String info);

    /**
     * retrieves a message id, if there is a task not finished (nor completed
     * neither failed) for this taskid and this baseline id
     * @param idEa may be null
     * @param idBline baseline id
     * @param task task id
     * @return the message id if there is one, null otherwise
     */
    public String retrieveNotFinishedMessageId(String idEa, String idBline, TaskId task);
}
