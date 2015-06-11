package com.compuware.caqs.service;

import com.compuware.caqs.dao.factory.DaoFactory;
import com.compuware.caqs.dao.interfaces.TaskDao;

/**
 * service singleton
 * @author cwfr-dzysman
 */
public class TaskSvc {
	/**
     * instance
     */
    private static final TaskSvc instance = new TaskSvc();

	/**
     * singleton
     */
    private TaskSvc() {
	}

	/**
     * renvoie l'instance
     * @return l'instance
     */
    public static TaskSvc getInstance() {
		return instance;
	}

    /**
     * dao task
     */
    private TaskDao dao = DaoFactory.getInstance().getTaskDao();

    /**
     * renvoie <code>true</code> si un calcul est en cours pour l'element ou
     * pour un des elements de son arborescence
     * @param idElt identifiant de l'element
     * @param idBline identifiant de la baseline
     * @return <code>true</code> si un calcul est en cours pour l'element ou
     * pour un des elements de son arborescence
     */
    public boolean isComputingElement(String idElt, String idBline) {
        boolean calculEnCours = dao.isComputingElement(idElt, idBline);
        if(!calculEnCours) {
            //aucun calcul est en cours sur cet element precis
        }
        return calculEnCours;
    }

    public boolean reportGeneratingForElement(String idElt, String idBline) {
        return dao.reportGeneratingForElement(idElt, idBline);
    }

    public boolean elementNeedsRecompute(String idElt, String idBline) {
        return dao.elementNeedsRecompute(idElt, idBline);
    }

    public void updateElementAsRecomputed(String idElt, String idBline) {
        dao.updateElementAsRecomputed(idElt, idBline);
    }

    /**
     * place tous les messages "en cours" en "en echec". utilisé au lancement
     * du serveur ==> les taches ne s'ayant pas terminees a la fermeture du
     * serveur sont forcement en echec.
     */
    public void setInProgressTaskAsFailed() {
        dao.setInProgressTaskAsFailed();
    }

}
