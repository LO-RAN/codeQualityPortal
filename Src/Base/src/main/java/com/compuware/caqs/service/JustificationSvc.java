/**
 * 
 */
package com.compuware.caqs.service;

import java.util.Collection;

import com.compuware.caqs.dao.factory.DaoFactory;
import com.compuware.caqs.dao.interfaces.JustificatifDao;
import com.compuware.caqs.domain.dataschemas.JustificatifResume;
import com.compuware.caqs.domain.dataschemas.JustificationBean;
import com.compuware.caqs.exception.CaqsException;
import com.compuware.caqs.exception.DataAccessException;

/**
 * @author cwfr-fdubois
 *
 */
public class JustificationSvc {

    private static final JustificationSvc instance = new JustificationSvc();

    private JustificatifDao justificatifDao = DaoFactory.getInstance().getJustificatifDao();

    private JustificationSvc() {
    }

    public static JustificationSvc getInstance() {
        return instance;
    }

    public Collection<JustificatifResume> getAllJustifications(String req, String userId) throws CaqsException {
        return justificatifDao.getAllJustifications(req, userId);
    }

    public Collection<JustificatifResume> getAllCriterionJustificationsForElement(String req, String idElt, String idBline) throws CaqsException {
        return justificatifDao.getAllCriterionJustificationsForElement(req, idElt, idBline);
    }

    public int getNbCriterionJustificationsForElement(String req, String idElt, String idBline, String idEltEA) throws CaqsException {
        return justificatifDao.getNbCriterionJustificationsForElement(req, idElt, idBline, idEltEA);
    }

    public double[] getJustifiedNote(String idElt, String idCritfac, String idBline, String dbtable) throws CaqsException {
        double[] result = justificatifDao.getJustifiedNote(idElt, idCritfac, idBline, dbtable);
        if (result[1] == 0.0) {
            if (idCritfac.startsWith("ANTI_") || idCritfac.startsWith("NETSTD_")) {
                result[1] = 4.0;
            } else {
                result[1] = 2.0;
            }
        }
        return result;
    }

    public JustificationBean getJustificatif(String idJust, double note) throws DataAccessException {
        return justificatifDao.getJustificatif(idJust, note);
    }

    public void setJustification(String idElt, String idBline, String idCritfacqa, JustificationBean just, boolean update) throws CaqsException {
        justificatifDao.setJustification(idElt, idBline, idCritfacqa, just, update);
    }

    public void linkJustificatifs(String idJustOld, String idJustNew) throws CaqsException {
        justificatifDao.linkJustificatifs(idJustOld, idJustNew);
    }

    public void removeJustificatif(String idElt, String idBline, String idCritfacqa, String idJust) throws CaqsException {
        //nous recuperons la justification a annuler
        JustificationBean just = justificatifDao.getJustificatif(idJust, 0.0);
        if (just != null && just.getLinkedJustificatif() != null) {
            this.setJustification(idElt, idBline, idCritfacqa, just.getLinkedJustificatif(), true);
            //nous supprimons la justification a annuler
            justificatifDao.removeJustificatif(idJust);
        }
    }
}
