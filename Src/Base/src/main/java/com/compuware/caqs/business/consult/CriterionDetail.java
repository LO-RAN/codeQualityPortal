package com.compuware.caqs.business.consult;

import java.util.List;

import com.compuware.caqs.dao.factory.DaoFactory;
import com.compuware.caqs.dao.interfaces.CriterionDao;
import com.compuware.caqs.domain.dataschemas.CriterionBean;
import com.compuware.caqs.domain.dataschemas.CriterionDefinition;
import com.compuware.caqs.domain.dataschemas.ElementBean;
import com.compuware.caqs.domain.dataschemas.UsageBean;

public class CriterionDetail {

    private DaoFactory daoFactory = DaoFactory.getInstance();

    public List<CriterionDefinition> retrieveCriterionDefinitions(String idUsa) {
        CriterionDao criterionFacade = daoFactory.getCriterionDao();
        return criterionFacade.retrieveCriterionDefinitionByUsage(idUsa);
    }

    public List<CriterionDefinition> retrieveCriterionDefinitions(UsageBean usageBean) {
        return retrieveCriterionDefinitions(usageBean.getId());
    }

    public List<CriterionDefinition> retrieveCriterionDefinitions(ElementBean eltBean) {
        return retrieveCriterionDefinitions(eltBean.getUsage().getId());
    }

    public List<CriterionBean> retrieveCriterionDetailsForSubElts(ElementBean eltBean, CriterionDefinition criterionDef, double seuil, String filter, String typeElt) {
        CriterionDao criterionFacade = daoFactory.getCriterionDao();
        List<CriterionBean> result = criterionFacade.retrieveCriterionDetailsForSubElts(eltBean, criterionDef, seuil, filter, typeElt);
        return result;
    }

    public List<CriterionBean> retrieveCriterionDetailsForElts(ElementBean eltBean, CriterionDefinition criterionDef, double seuil, String filter, String typeElt) {
        CriterionDao criterionFacade = daoFactory.getCriterionDao();
        List<CriterionBean> result = criterionFacade.retrieveCriterionDetailsForElts(eltBean, criterionDef, seuil, filter, typeElt);
        return result;
    }

    public List<CriterionBean> retrieveCriterionDetailsNoMetForSubElts(ElementBean eltBean, CriterionDefinition criterionDef, double seuil, String filter, String typeElt) {
        CriterionDao criterionFacade = daoFactory.getCriterionDao();
        List<CriterionBean> result = criterionFacade.retrieveCriterionDetailsNoMetForSubElts(eltBean, criterionDef, seuil, filter, typeElt);
        return result;
    }

    public List<CriterionBean> retrieveCriterionDetailsNoMetForElts(ElementBean eltBean, CriterionDefinition criterionDef, double seuil, String filter, String typeElt) {
        CriterionDao criterionFacade = daoFactory.getCriterionDao();
        List<CriterionBean> result = criterionFacade.retrieveCriterionDetailsNoMetForElts(eltBean, criterionDef, seuil, filter, typeElt);
        return result;
    }
}
