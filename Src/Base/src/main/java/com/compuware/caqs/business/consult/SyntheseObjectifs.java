package com.compuware.caqs.business.consult;

import com.compuware.caqs.constants.Constants;
import com.compuware.caqs.constants.MessagesCodes;
import com.compuware.caqs.exception.DataAccessException;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.compuware.caqs.dao.factory.DaoFactory;
import com.compuware.caqs.dao.interfaces.CriterionDao;
import com.compuware.caqs.dao.interfaces.FactorDao;
import com.compuware.caqs.domain.dataschemas.CriterionBean;
import com.compuware.caqs.domain.dataschemas.ElementBean;
import com.compuware.caqs.domain.dataschemas.FactorBean;
import com.compuware.caqs.domain.dataschemas.FactorElementBean;
import com.compuware.toolbox.util.logging.LoggerManager;

public class SyntheseObjectifs {

    protected static org.apache.log4j.Logger logger = LoggerManager.getLogger(Constants.LOG4J_IHM_LOGGER_KEY);
    private DaoFactory daoFactory = DaoFactory.getInstance();

    public List<FactorBean> retrieveFactorsByElementAndBaseline(ElementBean eltBean) {
        FactorDao factorsFacade = daoFactory.getFactorDao();
        List<FactorBean> result = factorsFacade.retrieveFactorsByElementBaseline(eltBean.getBaseline().getId(), eltBean.getProject().getId(), eltBean.getId(), eltBean.getUsage().getId());
        return result;
    }

    public List<CriterionBean> retrieveCriterionSummary(ElementBean eltBean, FactorBean factorBean) {
        List<CriterionBean> result = retrieveCriterionSummary(eltBean, factorBean.getId());
        return result;
    }

    public List<CriterionBean> retrieveCriterionSummary(ElementBean eltBean, String idFact) {
        CriterionDao criterionFacade = daoFactory.getCriterionDao();
        List<CriterionBean> result = criterionFacade.retrieveFacteurSynthese(eltBean.getId(), eltBean.getProject().getId(), eltBean.getBaseline().getId(), idFact);
        return result;
    }

    public Map retrieveCriterionNoteRepartition(ElementBean eltBean, FactorBean factorBean) {
        Map result = retrieveCriterionNoteRepartition(eltBean, factorBean.getId());
        return result;
    }

    public Map retrieveCriterionNoteRepartition(ElementBean eltBean, String idFact) {
        CriterionDao criterionFacade = daoFactory.getCriterionDao();
        Map result = criterionFacade.retrieveCriterionNoteRepartition(eltBean.getId(), eltBean.getProject().getId(), eltBean.getBaseline().getId(), idFact);
        return result;
    }

    public Collection<FactorElementBean> retrieveArboSynthese(ElementBean eltBean, String idFact) {
        FactorDao factorFacade = daoFactory.getFactorDao();
        Collection<FactorElementBean> result = factorFacade.retrieveFactorAndJustForSubElts(eltBean.getBaseline().getId(), eltBean.getProject().getId(), eltBean.getId(), idFact);
        return result;
    }

    public FactorBean retrieveFactorInfos(ElementBean eltBean, String idFact) {
        FactorDao factorFacade = daoFactory.getFactorDao();
        FactorBean factorBean = factorFacade.retrieveFactorAndJustByIdElementBaseline(eltBean.getBaseline().getId(), eltBean.getProject().getId(), eltBean.getId(), idFact);
        return factorBean;
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
        CriterionDao criterionFacade = daoFactory.getCriterionDao();
        MessagesCodes retour = MessagesCodes.NO_ERROR;
        try {
            criterionFacade.updateCommentForCriterion(elt, idCrit, comment);
        } catch (DataAccessException exc) {
            retour = MessagesCodes.DATABASE_ERROR;
        }
        return retour;
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
            String idFact, String comment) {
        FactorDao factorFacade = daoFactory.getFactorDao();
        MessagesCodes retour = MessagesCodes.NO_ERROR;
        try {
            factorFacade.updateCommentForFactor(elt, idFact, comment);
        } catch (DataAccessException exc) {
            logger.error(exc);
            retour = MessagesCodes.DATABASE_ERROR;
        }
        return retour;
    }
}
