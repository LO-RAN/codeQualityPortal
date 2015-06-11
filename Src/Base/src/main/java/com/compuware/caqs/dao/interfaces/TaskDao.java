package com.compuware.caqs.dao.interfaces;

public interface TaskDao {

    public boolean reportGeneratingForElement(String idElt, String idBline);

    /**
     * renvoie <code>true</code> si un calcul est en cours pour l'element ou
     * pour un des elements de son arborescence
     * @param idElt identifiant de l'element
     * @param idBline identifiant de la baseline
     * @return <code>true</code> si un calcul est en cours pour l'element ou
     * pour un des elements de son arborescence
     */
    public boolean isComputingElement(String idElt, String idBline);

    public void updateElementAsRecomputed(String idElt, String idBline);

    public boolean elementNeedsRecompute(String idElt, String idBline);

    /**
     * place tous les messages "en cours" en "en echec". utilis� au lancement
     * du serveur ==> les taches ne s'ayant pas terminees a la fermeture du
     * serveur sont forcement en echec.
     */
    public void setInProgressTaskAsFailed();
}
