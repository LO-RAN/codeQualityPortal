package com.compuware.caqs.service;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.compuware.caqs.business.consult.BottomUp;
import com.compuware.caqs.dao.factory.DaoFactory;
import com.compuware.caqs.dao.interfaces.ElementDao;
import com.compuware.caqs.dao.interfaces.MetriqueDao;
import com.compuware.caqs.domain.dataschemas.BottomUpSummaryBean;
import com.compuware.caqs.domain.dataschemas.ElementBean;
import com.compuware.caqs.domain.dataschemas.FactorRepartitionBean;
import com.compuware.caqs.domain.dataschemas.FilterBean;
import com.compuware.caqs.domain.dataschemas.MetriqueBean;
import com.compuware.caqs.domain.dataschemas.SyntheseCorrectionBean;

public class BottomUpSyntheseSvc {

    private static final BottomUpSyntheseSvc instance = new BottomUpSyntheseSvc();

    private BottomUpSyntheseSvc() {
    }

    public static BottomUpSyntheseSvc getInstance() {
        return instance;
    }
    private BottomUp bottomUp = new BottomUp();

    public void retrieveCorrectionSynthese(ElementBean eltBean, FilterBean filter, SyntheseCorrectionBean syntheseCorr) {
        bottomUp.retrieveCorrectionSynthese(eltBean, filter, syntheseCorr);
    }

    public List<BottomUpSummaryBean> retrieveCriterionBottomUpSummary(ElementBean eltBean, FilterBean filter) {
        return bottomUp.retrieveCriterionBottomUpSummary(eltBean, filter);
    }

    public List<ElementBean> retrieveRecursiveSubElements(ElementBean eltBean) {
        DaoFactory daoFactory = DaoFactory.getInstance();
        ElementDao elementFacade = daoFactory.getElementDao();
        List<ElementBean> result = elementFacade.retrieveRecursiveSubElements(eltBean.getId(), eltBean.getBaseline());
        return result;
    }

    public Collection retrieveRepartitionByCriterion(ElementBean eltBean, String filter, String typeElt) {
        return bottomUp.retrieveRepartitionByCriterion(eltBean, filter, typeElt);
    }

    public Collection<FactorRepartitionBean> retrieveRepartitionByFactor(ElementBean eltBean, String filter, String typeElt) {
        return bottomUp.retrieveRepartitionByFactor(eltBean, filter, typeElt);
    }

    public Map<String, Map<String, MetriqueBean>> retrieveAdditionnalMetrics(ElementBean eltBean, String metList) {
        DaoFactory daoFactory = DaoFactory.getInstance();
        MetriqueDao metricDao = daoFactory.getMetriqueDao();
        return metricDao.retrieveQametriqueFromMetListBline(eltBean.getBaseline().getId(), metList);
    }
}
