package com.compuware.caqs.service;

import java.util.List;

import com.compuware.caqs.business.consult.CriterionDetail;
import com.compuware.caqs.domain.dataschemas.CriterionBean;
import com.compuware.caqs.domain.dataschemas.CriterionDefinition;
import com.compuware.caqs.domain.dataschemas.ElementBean;
import com.compuware.caqs.domain.dataschemas.UsageBean;

public class CriterionDetailSvc {

    private static final CriterionDetailSvc instance = new CriterionDetailSvc();

    private CriterionDetailSvc() {
    }

    public static CriterionDetailSvc getInstance() {
        return instance;
    }
    private CriterionDetail detail = new CriterionDetail();

    public List<CriterionDefinition> retrieveCriterionDefinitions(String idUsa) {
        return detail.retrieveCriterionDefinitions(idUsa);
    }

    public List<CriterionDefinition> retrieveCriterionDefinitions(UsageBean usageBean) {
        return retrieveCriterionDefinitions(usageBean.getId());
    }

    public List<CriterionDefinition> retrieveCriterionDefinitions(ElementBean eltBean) {
        return retrieveCriterionDefinitions(eltBean.getUsage().getId());
    }

    public List<CriterionBean> retrieveCriterionDetailsForSubElts(ElementBean eltBean, CriterionDefinition criterionDef, double seuil, String filter, String typeElt) {
        return detail.retrieveCriterionDetailsForSubElts(eltBean, criterionDef, seuil, filter, typeElt);
    }

    public List<CriterionBean> retrieveCriterionDetailsForElts(ElementBean eltBean, CriterionDefinition criterionDef, double seuil, String filter, String typeElt) {
        return detail.retrieveCriterionDetailsForElts(eltBean, criterionDef, seuil, filter, typeElt);
    }

    public List<CriterionBean> retrieveCriterionDetailsNoMetForSubElts(ElementBean eltBean, CriterionDefinition criterionDef, double seuil, String filter, String typeElt) {
        return detail.retrieveCriterionDetailsNoMetForSubElts(eltBean, criterionDef, seuil, filter, typeElt);
    }

    public List<CriterionBean> retrieveCriterionDetailsNoMetForElts(ElementBean eltBean, CriterionDefinition criterionDef, double seuil, String filter, String typeElt) {
        return detail.retrieveCriterionDetailsNoMetForElts(eltBean, criterionDef, seuil, filter, typeElt);
    }
}
