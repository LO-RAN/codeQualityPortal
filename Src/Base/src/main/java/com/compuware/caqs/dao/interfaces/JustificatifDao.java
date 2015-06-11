package com.compuware.caqs.dao.interfaces;

import java.sql.Connection;
import java.util.Collection;

import com.compuware.caqs.domain.dataschemas.JustificatifResume;
import com.compuware.caqs.domain.dataschemas.JustificationBean;
import com.compuware.caqs.exception.DataAccessException;

public interface JustificatifDao {

    JustificationBean retrieveJustificatifById(java.lang.String id, Connection connection);

    Collection<JustificatifResume> getAllJustifications(String req, String userId) throws DataAccessException;

    JustificationBean getJustificatif(String idJust, double note) throws DataAccessException;

    double[] getJustifiedNote(String idElt, String idCritfac, String idBline, String dbtable) throws DataAccessException;

    void setJustification(String idElt, String idBline, String idCritfacqa, JustificationBean just, boolean update) throws DataAccessException;

    void linkJustificatifs(String idJustOld, String idJustNew) throws DataAccessException;

    Collection<JustificatifResume> getAllCriterionJustificationsForElement(String req, String idPro, String idBline) throws DataAccessException;

    public int getNbCriterionJustificationsForElement(String req, String idPro, String idBline, String idEltEA) throws DataAccessException;

    public void removeJustificatif(String idJustNew) throws DataAccessException;
}
