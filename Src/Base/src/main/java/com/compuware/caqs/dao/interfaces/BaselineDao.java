package com.compuware.caqs.dao.interfaces;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.List;

import com.compuware.caqs.domain.dataschemas.BaselineBean;
import com.compuware.caqs.domain.dataschemas.ProjectBean;
import com.compuware.caqs.domain.dataschemas.evolutions.EvolutionBaselineBean;
import com.compuware.caqs.exception.DataAccessException;

public interface BaselineDao {

    /**
     * recupere une baseline par son id
     * @param id
     * @return
     */
    public abstract BaselineBean retrieveBaselineById(java.lang.String id);

    public abstract BaselineBean retrieveLastBaseline(java.lang.String projectId);

    public abstract BaselineBean retrieveBaselineAndProjectById(String id);

    public abstract String retrieveLinkedBaseline(String idElt, String idBline)
            throws DataAccessException;

    public boolean update(String baselineName, String baselineId, boolean changeDmaj) throws DataAccessException;

    public BaselineBean create(String projectId, String forcedId) throws DataAccessException;

    /**
     * @param projectId identifiant du projet
     * @return la liste des baselines valides plus la baseline d'instanciation pour
     * un projet
     */
    public Collection<BaselineBean> retrieveBaselinesAndInstanciationByProjectId(String projectId);

    /**
     * @param projectId identifiant de projet
     * @return la liste des baselines diff�rentes de la baseline d'instanciation
     * et �tant valides.
     */
    public abstract Collection<BaselineBean> retrieveValidBaselinesByProjectId(String projectId);

    public abstract BaselineBean getPreviousBaseline(BaselineBean currentBline)
            throws DataAccessException;

    public abstract BaselineBean getPreviousBaseline(BaselineBean currentBline, String idElt)
            throws DataAccessException;

    public abstract Timestamp getBaselineDmaj(String idBline);

    public abstract void delete(String idBline, String idPro, boolean split);

    public abstract List<BaselineBean> retrieveAllValidBaseline();

    /**
     * @param projectId identifiant de projet
     * @return la liste des baselines differentes de la baseline d'instanciation et qui ne sont pas en cours de calcul
     * et �tant valides ou non.
     */
    public Collection<BaselineBean> retrieveAllTerminatedBaselinesByProjectId(String projectId);

    /**
     * @param projectId identifiant de projet
     * @return la liste des baselines differentes de la baseline d'instanciation
     * et �tant valides ou non.
     */
    public Collection<BaselineBean> retrieveAllBaselinesByProjectId(String projectId);

    public List<BaselineBean> isBaselineAttachedToOtherBaseline(String idBline);

    /**
     * @param baselineId identifiant de la baseline
     * @param idElt identifiant de l'element
     * @return true si la baseline est bien la derniere pour l'element, false sinon
     */
    public boolean isLastBaseline(String baselineId, String idElt);

    /**
     * @param idElt identifiant de l'element
     * @return la derniere baseline pour l'element, null s'il n'y en a pas
     */
    public BaselineBean getLastBaseline(String idElt);

    /**
     *
     * @param baselineId
     * @param idElt
     * @return true si l'identifiant de baseline envoye correspond a la premiere
     * baseline de l'element, false sinon
     */
    public boolean isFirstBaseline(String baselineId, String idElt);

    /**
     * recupere toutes les baselines valides anterieures a celle en parametre
     * @param idEa id elt
     * @param currentBaseline baseline
     * @return baselines anterieures
     */
    public List<EvolutionBaselineBean> retrieveAllPreviousValidBaseline(String idEa, BaselineBean currentBaseline);

    /**
     * recupere la bline suivante de currentBline, null s'il n'y en a pas
     * @param currentBline
     * @return la bline suivante, null s'il n'y en a pas
     * @throws DataAccessException
     */
    public BaselineBean getNextBaseline(BaselineBean currentBline, ProjectBean pb) throws DataAccessException;

    /**
     * perime une bline
     * @param bline la bline a perimer
     * @throws DataAccessException
     */
    public void peremptBaseline(String bline) throws DataAccessException;

    /**
     * returns last baseline. returns last baseline or last linked baseline if there is one
     * @param elt the element for which the last baseline has to be returned
     * @return the last baseline for the element
     */
    public BaselineBean getLastRealBaseline(String idElt) throws DataAccessException;
}
