package com.compuware.caqs.dao.interfaces;

import com.compuware.caqs.domain.dataschemas.BaselineBean;
import com.compuware.caqs.domain.dataschemas.BottomUpDetailBean;
import com.compuware.caqs.domain.dataschemas.CriterionRepartitionBean;
import com.compuware.caqs.domain.dataschemas.ElementEvolutionSummaryBean;
import com.compuware.caqs.domain.dataschemas.FilterBean;
import com.compuware.caqs.domain.dataschemas.calcul.Volumetry;
import java.util.Collection;
import java.util.List;

/**
 *
 * @author cwfr-dzysman
 */
public interface EvolutionDao {

    /**
     * supprime toutes les donnees relatives aux evolutions entre deux baselines et 
     * utilisant cette baseline
     * @param idBline baseline
     */
    public void clearBaselineEvolutionDatas(String idBline);

    /**
     *
     * @param idElt
     * @param idBline
     * @param idPrevBline
     * @param filter
     * @return
     */
    public abstract List<ElementEvolutionSummaryBean> retrieveNewAndBadElements(String idElt,
            String idBline, BaselineBean previousBaseline, FilterBean filter);

    /**
     *
     * @param idElt
     * @param idBline
     * @param idPrevBline
     * @param filter
     * @return
     */
    public abstract List<ElementEvolutionSummaryBean> retrieveOldAndWorstElements(String idElt,
            String idBline, BaselineBean previousBaseline, FilterBean filter);

    /**
     *
     * @param idElt
     * @param idBline
     * @param idPrevBline
     * @param filter
     * @return
     */
    public abstract List<ElementEvolutionSummaryBean> retrieveOldAndBetterElements(String idElt,
            String idBline, BaselineBean previousBaseline, FilterBean filter);

    /**
     *
     * @param idElt
     * @param idBline
     * @param idPrevBline
     * @param filter
     * @return
     */
    public abstract List<ElementEvolutionSummaryBean> retrieveOldBetterAndWorstElements(String idElt,
            String idBline, BaselineBean previousBaseline, FilterBean filter);

    /**
     *
     * @param idElt
     * @param idBline
     * @param idPrevBline
     * @param filter
     * @return
     */
    public abstract List<ElementEvolutionSummaryBean> retrieveStableElements(String idElt,
            String idBline, BaselineBean previousBaseline, FilterBean filter);

    /**
     * 
     * @param idElt
     * @param idBline
     * @param idPrevBline
     * @param filter
     * @return
     */
    public abstract List<ElementEvolutionSummaryBean> retrieveBadAndStableElements(String idElt,
            String idBline, BaselineBean previousBaseline, FilterBean filter);

    /**
     * 
     * @param idElt
     * @param bline
     * @param prevBline
     * @return
     */
    public List<Volumetry> retrieveVolumetryBetweenBaselines(String idElt, BaselineBean bline, BaselineBean prevBline);

    /**
     * 
     * @param idElt
     * @param idBline
     * @param previousIdBline
     * @param filter
     * @return
     */
    public Collection<CriterionRepartitionBean> retrieveRepartitionBadStableElements(String idElt, String idBline, String previousIdBline, FilterBean filter);

    /**
     * 
     * @param idElt
     * @param idBline
     * @param previousIdBline
     * @param filter
     * @return
     */
    public Collection<CriterionRepartitionBean> retrieveRepartitionOldBetterWorstElements(String idElt, String idBline, String previousIdBline, FilterBean filter);

    /**
     *
     * @param idElt
     * @param idBline
     * @param previousIdBline
     * @param filter
     * @return
     */
    public Collection retrieveRepartitionNewAndBadElements(String idElt, String idBline, String previousIdBline, FilterBean filter);

    /**
     *
     * @param idElt
     * @param idBline
     * @param previousIdBline
     * @param filter
     * @return
     */
    public Collection<CriterionRepartitionBean> retrieveRepartitionOldAndWorstElements(String idElt, String idBline, String previousIdBline, FilterBean filter);

    /**
     * 
     * @param idElt
     * @param idBline
     * @param previousIdBline
     * @param filter
     * @return
     */
    public Collection<CriterionRepartitionBean> retrieveRepartitionOldAndBetterElements(String idElt, String idBline, String previousIdBline, FilterBean filter);

    /**
     * 
     * @param idBline
     * @param idPreviousBline
     * @param idElt
     * @param idPro
     * @return
     */
    public List<BottomUpDetailBean> retrieveCriterionBottomUpDetail(
            String idBline, String idPreviousBline, String idElt, String idPro);
    
}
