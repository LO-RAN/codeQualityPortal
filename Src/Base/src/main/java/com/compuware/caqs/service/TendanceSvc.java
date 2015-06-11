package com.compuware.caqs.service;

import com.compuware.caqs.dao.factory.DaoFactory;
import com.compuware.caqs.dao.interfaces.TendanceDao;

/**
 * service singleton
 * @author cwfr-dzysman
 */
public class TendanceSvc {

    /**
     * instance
     */
    private static final TendanceSvc instance = new TendanceSvc();

    /**
     * singleton
     */
    private TendanceSvc() {
    }

    /**
     * renvoie l'instance
     * @return l'instance
     */
    public static TendanceSvc getInstance() {
        return instance;
    }
    /**
     * dao task
     */
    private TendanceDao dao = DaoFactory.getInstance().getTendanceDao();

    /**
     * met a jour toutes les tendances : objectifs, critere, metrique
     * pour un element donne entre deux blines
     * @param idEa entite applicative
     * @param idBline baseline courante
     * @param idPreviousBline id de la precedente baseline
     */
    public void updateAllTrends(String idEa, String idBline, String idPreviousBline) {
        if(idPreviousBline!=null) {
            this.dao.updateAllTrends(idEa, idBline, idPreviousBline);
        } else {
            this.dao.deleteAllTrends(idEa, idBline);
        }
    }
}
