/**
 * 
 */
package com.compuware.caqs.business.consult;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.compuware.caqs.dao.factory.DaoFactory;
import com.compuware.caqs.dao.interfaces.CriterionDao;
import com.compuware.caqs.dao.interfaces.ElementDao;
import com.compuware.caqs.dao.interfaces.MetriqueDao;
import com.compuware.caqs.domain.dataschemas.BottomUpSummaryBean;
import com.compuware.caqs.domain.dataschemas.CriterionRepartitionBean;
import com.compuware.caqs.domain.dataschemas.ElementBean;
import com.compuware.caqs.domain.dataschemas.FactorRepartitionBean;
import com.compuware.caqs.domain.dataschemas.FilterBean;
import com.compuware.caqs.domain.dataschemas.MetriqueBean;
import com.compuware.caqs.domain.dataschemas.RepartitionBean;
import com.compuware.caqs.domain.dataschemas.SyntheseCorrectionBean;

/**
 * @author cwfr-fdubois
 *
 */
public class BottomUp {

	private DaoFactory daoFactory = DaoFactory.getInstance(); 
	
	public void retrieveCorrectionSynthese(ElementBean eltBean, FilterBean filter, SyntheseCorrectionBean syntheseCorr) {
    	CriterionDao criterionFacade = daoFactory.getCriterionDao();
        syntheseCorr.addNbEltsRejets(criterionFacade.retrieveCriterionTotalElements(eltBean.getBaseline().getId(), eltBean.getId(), 1, filter));
        syntheseCorr.addNbEltsReserve(criterionFacade.retrieveCriterionTotalElements(eltBean.getBaseline().getId(), eltBean.getId(), 2, filter));
        syntheseCorr.addNbEltsAccepte(criterionFacade.retrieveCriterionTotalElements(eltBean.getBaseline().getId(), eltBean.getId(), 3, filter));
        int nbCorrRejet = criterionFacade.retrieveCriterionTotalCorrections(eltBean.getBaseline().getId(), eltBean.getId(), 1, filter);
        int nbCorrReserve = criterionFacade.retrieveCriterionTotalCorrections(eltBean.getBaseline().getId(), eltBean.getId(), 2, filter);
        syntheseCorr.addNbCorrRejets(nbCorrRejet);
        syntheseCorr.addNbCorrReserve(nbCorrReserve);
        syntheseCorr.addNbCorrTotal(nbCorrRejet + nbCorrReserve);
    }
	
    public List<BottomUpSummaryBean> retrieveCriterionBottomUpSummary(ElementBean eltBean, FilterBean filter) {
        List<BottomUpSummaryBean> result = null;
    	CriterionDao criterionFacade = daoFactory.getCriterionDao();
        result = criterionFacade.retrieveCriterionBottomUpSummary(eltBean.getBaseline().getId(), eltBean.getId(), filter);
        return result;
    }
    
    public List retrieveRecursiveSubElements(ElementBean eltBean) {
    	ElementDao elementFacade = daoFactory.getElementDao();
        List result = elementFacade.retrieveRecursiveSubElements(eltBean.getId(), eltBean.getBaseline());
        return result;
    }
    
    public Collection retrieveRepartitionByCriterion(ElementBean eltBean, String filter, String typeElt) {
    	CriterionDao criterionFacade = daoFactory.getCriterionDao();
        Collection repartition = criterionFacade.retrieveRepartitionByCriterion(eltBean.getBaseline().getId(), eltBean.getId(), eltBean.getProject().getId(), filter, typeElt);
        orderAndPurge(repartition);
        return repartition;
    }

    private void orderAndPurge(Collection repartition) {
        int sum = getSum(repartition);
        Iterator i = repartition.iterator();
        RepartitionBean other = new CriterionRepartitionBean("OTHER", 0);
        while (i.hasNext()) {
            RepartitionBean bean = (RepartitionBean) i.next();
            if (bean.getNb() <= 0.02 * sum) {
                other.setNb(other.getNb() + bean.getNb());
                i.remove();
            }
        }
        if (other.getNb() > 0) {
            repartition.add(other);
        }
    }

    private int getSum(Collection repartition) {
        int result = 0;
        Iterator i = repartition.iterator();
        while (i.hasNext()) {
            RepartitionBean bean = (RepartitionBean) i.next();
            result += bean.getNb();
        }
        return result;
    }

    public Collection<FactorRepartitionBean> retrieveRepartitionByFactor(ElementBean eltBean, String filter, String typeElt) {
    	CriterionDao criterionFacade = daoFactory.getCriterionDao();
        Collection<FactorRepartitionBean> repartition = criterionFacade.retrieveRepartitionByFactor(eltBean.getBaseline().getId(), eltBean.getId(), eltBean.getProject().getId(), eltBean.getUsage().getId(), filter, typeElt);
        return repartition;
    }

    public Map<String, Map<String, MetriqueBean>> retrieveAdditionnalMetrics(ElementBean eltBean, String metList) {
    	MetriqueDao metricDao = daoFactory.getMetriqueDao();
        return metricDao.retrieveQametriqueFromMetListBline(eltBean.getBaseline().getId(), metList);
    }
}
