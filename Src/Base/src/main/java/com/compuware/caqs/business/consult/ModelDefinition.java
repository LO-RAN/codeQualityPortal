/**
 * 
 */
package com.compuware.caqs.business.consult;

import java.util.Collection;
import java.util.List;
import java.util.Locale;

import com.compuware.caqs.business.calculation.xmlimpl.UsageCalculator;
import com.compuware.caqs.dao.factory.DaoFactory;
import com.compuware.caqs.dao.interfaces.CriterionDao;
import com.compuware.caqs.dao.interfaces.DialecteDao;
import com.compuware.caqs.dao.interfaces.FactorDao;
import com.compuware.caqs.dao.interfaces.MetriqueDao;
import com.compuware.caqs.dao.interfaces.UsageDao;
import com.compuware.caqs.domain.dataschemas.CriterionDefinition;
import com.compuware.caqs.domain.dataschemas.DialecteBean;
import com.compuware.caqs.domain.dataschemas.ElementBean;
import com.compuware.caqs.domain.dataschemas.FactorBean;
import com.compuware.caqs.domain.dataschemas.MetriqueDefinitionBean;
import com.compuware.caqs.domain.dataschemas.UsageBean;

/**
 * @author cwfr-fdubois
 *
 */
public class ModelDefinition {

    private DaoFactory daoFactory = DaoFactory.getInstance();

    public List<FactorBean> retrieveFactorDefinition(ElementBean eltBean) {
        UsageBean usageBean = eltBean.getUsage();
        FactorDao factorFacade = daoFactory.getFactorDao();
        return factorFacade.retrieveFactorDefinitionByUsage(usageBean.getId());
    }

    public List<CriterionDefinition> retrieveCriterionDefinition(ElementBean eltBean) {
        UsageBean usageBean = eltBean.getUsage();
        CriterionDao criterionFacade = daoFactory.getCriterionDao();
        return criterionFacade.retrieveCriterionDefinitionByUsage(usageBean.getId());
    }

    public String retrieveCriterionRules(ElementBean eltBean, String idCrit) {
        String result = null;
        if (eltBean != null && eltBean.getUsage() != null && idCrit != null) {
            UsageBean usageBean = eltBean.getUsage();
            UsageCalculator calculator = new UsageCalculator();
            calculator.init("method-" + usageBean.getId().toLowerCase() + ".xml");
            result = calculator.getCriterionDefintionAsString(usageBean.getId(), idCrit);
        }
        return result;
    }

    public List<MetriqueDefinitionBean> retrieveMetriquesDefinition(ElementBean eltBean, Locale loc) {
        UsageBean usageBean = eltBean.getUsage();
        MetriqueDao metriqueFacade = daoFactory.getMetriqueDao();
        return metriqueFacade.retrieveMetriqueDefinitionByUsage(usageBean.getId());
    }

    /**
     * retrieves a metric
     * @param idMet metric's id
     * @return metric
     */
    public MetriqueDefinitionBean retrieveMetriqueDefinition(String idMet) {
        MetriqueDao metriqueFacade = daoFactory.getMetriqueDao();
        return metriqueFacade.retrieveMetricById(idMet);
    }

    public List<MetriqueDefinitionBean> retrieveMetriquesDefinition(ElementBean eltBean) {
        return retrieveMetriquesDefinition(eltBean, Locale.getDefault());
    }

    public Collection<UsageBean> retrieveAllModels() {
        UsageDao usageFacade = daoFactory.getUsageDao();
        return usageFacade.retrieveAllUsages();
    }

    public Collection<DialecteBean> retrieveAllDialects() {
        DialecteDao dialecteDao = daoFactory.getDialecteDao();
        return dialecteDao.retrieveAllDialectes();
    }
}
