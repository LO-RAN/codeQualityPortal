package com.compuware.caqs.service;

import com.compuware.caqs.constants.MessagesCodes;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.compuware.caqs.business.consult.SyntheseObjectifs;
import com.compuware.caqs.domain.dataschemas.CriterionBean;
import com.compuware.caqs.domain.dataschemas.ElementBean;
import com.compuware.caqs.domain.dataschemas.FactorBean;
import com.compuware.caqs.domain.dataschemas.FactorElementBean;

public class SyntheseObjectifsSvc {

    private static final SyntheseObjectifsSvc instance = new SyntheseObjectifsSvc();

    private SyntheseObjectifsSvc() {
    }

    public static SyntheseObjectifsSvc getInstance() {
        return instance;
    }
    private SyntheseObjectifs syntheseObj = new SyntheseObjectifs();

    public Collection retrieveFactorsByElementAndBaseline(ElementBean eltBean) {
        return syntheseObj.retrieveFactorsByElementAndBaseline(eltBean);
    }

    public List retrieveCriterionSummary(ElementBean eltBean, FactorBean factorBean) {
        return syntheseObj.retrieveCriterionSummary(eltBean, factorBean);
    }

    public List<CriterionBean> retrieveCriterionSummary(ElementBean eltBean, String idFact) {
        return syntheseObj.retrieveCriterionSummary(eltBean, idFact);
    }

    public Map retrieveCriterionNoteRepartition(ElementBean eltBean, FactorBean factorBean) {
        return syntheseObj.retrieveCriterionNoteRepartition(eltBean, factorBean);
    }

    public Map retrieveCriterionNoteRepartition(ElementBean eltBean, String idFact) {
        return syntheseObj.retrieveCriterionNoteRepartition(eltBean, idFact);
    }

    public Collection<FactorElementBean> retrieveArboSynthese(ElementBean eltBean, String idFact) {
        return syntheseObj.retrieveArboSynthese(eltBean, idFact);
    }

    public FactorBean retrieveFactorInfos(ElementBean eltBean, String idFact) {
        return syntheseObj.retrieveFactorInfos(eltBean, idFact);
    }

    /**
     * met a jour le commentaire place sur un critere. l'element envoye en parametre
     * doit avoir une baseline.
     * @param elt l'element pour lequel commenter le critere
     * @param idCrit l'identifiant du critere a commenter
     * @param comment le commentaire
     * @return <code>MessagesCodes.NO_ERROR</code> s'il n'y a pas d'erreur, un
     * message d'erreur sinon.
     */
    public MessagesCodes updateCommentForCriterion(ElementBean elt,
            String idCrit, String comment) {
        return syntheseObj.updateCommentForCriterion(elt, idCrit, comment);
    }

    /**
     * met a jour le commentaire place sur un objectif. l'element envoye en parametre
     * doit avoir une baseline.
     * @param elt l'element pour lequel commenter le critere
     * @param idFact l'identifiant de l'objectif a commenter
     * @param comment le commentaire
     * @return <code>MessagesCodes.NO_ERROR</code> s'il n'y a pas d'erreur, un
     * message d'erreur sinon.
     */
    public MessagesCodes updateCommentForFactor(ElementBean elt,
            String idFac, String comment) {
        return syntheseObj.updateCommentForFactor(elt, idFac, comment);
    }
}
