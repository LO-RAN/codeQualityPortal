package com.compuware.caqs.business.consult;

import com.compuware.caqs.business.chart.config.ChartConfigGenerator;
import com.compuware.caqs.business.chart.factory.EvolutionChartFactory;
import com.compuware.caqs.business.chart.util.ImageUtil;
import com.compuware.caqs.business.chart.xml.XmlDatasetGenerator;
import com.compuware.caqs.constants.Constants;
import com.compuware.caqs.domain.dataschemas.graph.GraphImageConfig;
import java.io.File;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import com.compuware.caqs.dao.factory.DaoFactory;
import com.compuware.caqs.dao.interfaces.CriterionDao;
import com.compuware.caqs.dao.interfaces.ElementDao;
import com.compuware.caqs.dao.interfaces.FactorEvolutionDao;
import com.compuware.caqs.domain.chart.config.EvolutionConfig;
import com.compuware.caqs.domain.dataschemas.BaselineBean;
import com.compuware.caqs.domain.dataschemas.BottomUpDetailBean;
import com.compuware.caqs.domain.dataschemas.ElementBean;
import com.compuware.caqs.domain.dataschemas.ElementEvolutionSummaryBean;
import com.compuware.caqs.domain.dataschemas.EvolutionBean;
import com.compuware.caqs.domain.dataschemas.FactorBean;
import com.compuware.caqs.domain.dataschemas.FactorEvolutionBean;
import com.compuware.caqs.domain.dataschemas.FilterBean;
import com.compuware.caqs.domain.dataschemas.actionplan.ActionPlanCriterionBean;
import com.compuware.caqs.domain.dataschemas.actionplan.list.ActionPlanElementBeanCollection;
import com.compuware.caqs.domain.dataschemas.calcul.Volumetry;
import com.compuware.toolbox.util.logging.LoggerManager;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import org.apache.log4j.Logger;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;

public class Evolution {

    protected static final Logger logger = LoggerManager.getLogger(Constants.LOG4J_IHM_LOGGER_KEY);
    DaoFactory daoFactory = DaoFactory.getInstance();

    public File retrieveDashboardEvolutionImage(ElementBean elt, GraphImageConfig imgConfig) {
        ImageUtil imgUtil = ImageUtil.getInstance();
        File result = imgUtil.retrieveExistingDashboardEvolutionImage(elt.getId(), elt.getBaseline().getId(), imgConfig);
        EvolutionConfig cfg = ChartConfigGenerator.getEvolutionConfig();
        cfg.setTitle(imgConfig.getTitle());
        try {
            List<FactorEvolutionBean> evolutionCollection = this.retrieveEvolutionMinAvg(elt);
            List<BaselineBean> baselineCollection = this.retrieveBaselines(elt);
            StringBuffer xmlout = new StringBuffer();
            XmlDatasetGenerator.writeFactorEvolutionData(xmlout, baselineCollection, evolutionCollection, imgConfig.getLocale(), imgConfig.getResources());
            InputStream str = new ByteArrayInputStream(xmlout.toString().getBytes(Constants.GLOBAL_CHARSET));
            String baselineLabel = "Baseline";
            String scoreLabel = "Score";
            if (imgConfig.getResources() != null) {
                baselineLabel = imgConfig.getResources().getMessage(imgConfig.getLocale(), "caqs.baselines");
                scoreLabel = imgConfig.getResources().getMessage(imgConfig.getLocale(), "caqs.evolution.note");
            }
            JFreeChart evolutionChart = EvolutionChartFactory.createFromXml(str, cfg, baselineLabel, scoreLabel);
            ChartUtilities.saveChartAsPNG(result, evolutionChart, imgConfig.getWidth(), imgConfig.getHeight());
        } catch (IOException e) {
            logger.error("Error creating evolution image for dashboard", e);
        }
        return result;
    }

    public List<BottomUpDetailBean> retrieveNewAndBadElementsWithCriterions(String idEa, String idBline, String idPro) {
        CriterionDao criterionFacade = daoFactory.getCriterionDao();
        return criterionFacade.retrieveNewAndBadElementsWithCriterions(idEa, idBline, idPro);
    }

    public List<BottomUpDetailBean> retrieveOldWorstElementsWithCriterions(String idEa, String idBline, String idPro) {
        CriterionDao criterionFacade = daoFactory.getCriterionDao();
        return criterionFacade.retrieveOldWorstElementsWithCriterions(idEa, idBline, idPro);
    }

    public List<BottomUpDetailBean> retrieveOldBetterElementsWithCriterions(String idEa, String idBline, String idPro) {
        CriterionDao criterionFacade = daoFactory.getCriterionDao();
        return criterionFacade.retrieveOldBetterElementsWithCriterions(idEa, idBline, idPro);
    }

    public List<BottomUpDetailBean> retrieveOldBetterWorstElementsWithCriterions(String idEa, String idBline, String idPro) {
        CriterionDao criterionFacade = daoFactory.getCriterionDao();
        return criterionFacade.retrieveOldBetterWorstElementsWithCriterions(idEa, idBline, idPro);
    }

    public List<BottomUpDetailBean> retrieveOldBadStableElementsWithCriterions(String idEa, String idBline, String idPro) {
        CriterionDao criterionFacade = daoFactory.getCriterionDao();
        return criterionFacade.retrieveOldBadStableElementsWithCriterions(idEa, idBline, idPro);
    }

    public List<ElementEvolutionSummaryBean> retrieveBadAndStableElements(String idElt, String idBline, FilterBean filter) {
        CriterionDao criterionFacade = daoFactory.getCriterionDao();
        return criterionFacade.retrieveBadAndStableElements(idElt, idBline, filter);
    }


    public List<ElementEvolutionSummaryBean> retrieveNewAndBadElements(String idElt, String idBline, FilterBean filter) {
        CriterionDao criterionFacade = daoFactory.getCriterionDao();
        return criterionFacade.retrieveNewAndBadElements(idElt, idBline, filter);
    }

    public List<ElementEvolutionSummaryBean> retrieveOldAndWorstElements(String idElt, String idBline, FilterBean filter) {
        CriterionDao criterionFacade = daoFactory.getCriterionDao();
        return criterionFacade.retrieveOldAndWorstElements(idElt, idBline, filter);
    }

    public List<ElementEvolutionSummaryBean> retrieveOldAndBetterElements(String idElt, String idBline, FilterBean filter) {
        CriterionDao criterionFacade = daoFactory.getCriterionDao();
        return criterionFacade.retrieveOldAndBetterElements(idElt, idBline, filter);
    }

    public List<ElementEvolutionSummaryBean> retrieveOldBetterAndWorstElements(String idElt, String idBline, FilterBean filter) {
        CriterionDao criterionFacade = daoFactory.getCriterionDao();
        return criterionFacade.retrieveOldBetterAndWorstElements(idElt, idBline, filter);
    }

    public List<ElementEvolutionSummaryBean> retrieveStableElements(String idElt, String idBline, FilterBean filter) {
        CriterionDao criterionFacade = daoFactory.getCriterionDao();
        return criterionFacade.retrieveStableElements(idElt, idBline, filter);
    }

    public Collection retrieveRepartitionNewAndBadElements(String idElt, String idBline, FilterBean filter) {
        CriterionDao criterionFacade = daoFactory.getCriterionDao();
        return criterionFacade.retrieveRepartitionNewAndBadElements(idElt, idBline, filter);
    }

    public Collection retrieveRepartitionOldAndWorstElements(String idElt, String idBline, FilterBean filter) {
        CriterionDao criterionFacade = daoFactory.getCriterionDao();
        return criterionFacade.retrieveRepartitionOldAndWorstElements(idElt, idBline, filter);
    }

    public Collection retrieveRepartitionOldAndBetterElements(String idElt, String idBline, FilterBean filter) {
        CriterionDao criterionFacade = daoFactory.getCriterionDao();
        return criterionFacade.retrieveRepartitionOldAndBetterElements(idElt, idBline, filter);
    }

    public Collection retrieveRepartitionOldBetterWorstElements(String idElt, String idBline, FilterBean filter) {
        CriterionDao criterionFacade = daoFactory.getCriterionDao();
        return criterionFacade.retrieveRepartitionOldBetterWorstElements(idElt, idBline, filter);
    }

    public Collection retrieveRepartitionBadStableElements(String idElt, String idBline, FilterBean filter) {
        CriterionDao criterionFacade = daoFactory.getCriterionDao();
        return criterionFacade.retrieveRepartitionBadStableElements(idElt, idBline, filter);
    }

    public Collection<FactorBean> retrieveFactorEvolution(ElementBean eltBean, BaselineBean bline) {
        FactorEvolutionDao fEvolutionDao = daoFactory.getFactorEvolutionDao();
        return fEvolutionDao.retrieveFactorEvolution(eltBean, bline);
    }

    public List<BaselineBean> retrieveBaselines(ElementBean eltBean) {
        FactorEvolutionDao fEvolutionBf = daoFactory.getFactorEvolutionDao();
        return fEvolutionBf.retrieveBaselines(eltBean);
    }

    public Collection<EvolutionBean> retrieveEvolution(ElementBean eltBean, String target) {
        Collection<EvolutionBean> result = null;
        FactorEvolutionDao fEvolutionBf = daoFactory.getFactorEvolutionDao();
        if (target != null && target.equals("CRIT")) {
            result = fEvolutionBf.retrieveCriterionEvolution(eltBean);
        } else {
            result = fEvolutionBf.retrieveFactorEvolution(eltBean);
        }
        return result;
    }

    private List<FactorEvolutionBean> retrieveEvolutionMinAvg(ElementBean elt) {
        FactorEvolutionDao fEvolutionBf = daoFactory.getFactorEvolutionDao();
        return fEvolutionBf.retrieveEvolutionMinAvg(elt);
    }

    public Volumetry retrieveVolumetry(ElementBean eltBean) {
        BaselineBean blineBean = eltBean.getBaseline();
        ElementDao elementFacade = daoFactory.getElementDao();
        List volumetryList = elementFacade.retrieveVolumetry(eltBean.getId(), blineBean.getId());
        Volumetry volumetrySum = summarize(volumetryList);
        return volumetrySum;
    }

    private Volumetry summarize(List volumetryList) {
        Volumetry result = new Volumetry();
        Iterator i = volumetryList.iterator();
        Volumetry current = null;
        while (i.hasNext()) {
            current = (Volumetry) i.next();
            result.setCreated(result.getCreated() + current.getCreated());
            result.setDeleted(result.getDeleted() + current.getDeleted());
        }
        return result;
    }

    /**
     * @param previousActionPlan the previous action plan
     * @param badCriterions criterions which have a mark lower than 3 for the current baseline
     * @return Returns a criterion list containing criterions included in the action plan defined for the previous baseline,
     * and which have been corrected
     */
    public ActionPlanElementBeanCollection<ActionPlanCriterionBean> getCorrectedCriterionsFromPreviousActionPlan(
            ActionPlanElementBeanCollection<ActionPlanCriterionBean> previousActionPlan,
            ActionPlanElementBeanCollection<ActionPlanCriterionBean> badCriterions) {
        ActionPlanElementBeanCollection<ActionPlanCriterionBean> corrected = new ActionPlanElementBeanCollection<ActionPlanCriterionBean>(
                badCriterions.getIdEa(),
                badCriterions.getIdBline());
        for (Iterator<ActionPlanCriterionBean> it = previousActionPlan.iterator(); it.hasNext();) {
            ActionPlanCriterionBean criterion = it.next();
            if (badCriterions.get(criterion) == null) {
                //it's corrected
                corrected.add(criterion);
                it.remove();
            }
        }
        return corrected;
    }

    /**
     * @param previousActionPlan the previous action plan
     * @param badCriterions criterions which have a mark lower than 3 for the current baseline
     * @return Returns a criterion list containing criterions included in the action plan defined for the previous baseline,
     * and which have been partially corrected
     */
    public ActionPlanElementBeanCollection<ActionPlanCriterionBean> getPartiallyCorrectedCriterionsFromPreviousActionPlan(
            ActionPlanElementBeanCollection<ActionPlanCriterionBean> previousActionPlan,
            ActionPlanElementBeanCollection<ActionPlanCriterionBean> badCriterions) {
        ActionPlanElementBeanCollection<ActionPlanCriterionBean> retour = new ActionPlanElementBeanCollection<ActionPlanCriterionBean>(
                badCriterions.getIdEa(),
                badCriterions.getIdBline());
        for (Iterator<ActionPlanCriterionBean> it = previousActionPlan.iterator(); it.hasNext();) {
            ActionPlanCriterionBean criterion = it.next();
            ActionPlanCriterionBean newCriterion = badCriterions.get(criterion);
            if (newCriterion != null) {
                //not yet corrected
                int newNbElts = newCriterion.getNumberElt();
                int oldNbElts = criterion.getNumberElt();
                if (newNbElts < oldNbElts) {
                    retour.add(criterion);
                    it.remove();
                }
            }
        }
        return retour;
    }

    /**
     * @param previousActionPlan the previous action plan
     * @param badCriterions criterions which have a mark lower than 3 for the current baseline
     * @return Returns a criterion list containing criterions included in the action plan defined for the previous baseline,
     * and which have been deteriorated
     */
    public ActionPlanElementBeanCollection<ActionPlanCriterionBean> getDeterioratedCriterionsFromPreviousActionPlan(
            ActionPlanElementBeanCollection<ActionPlanCriterionBean> previousActionPlan,
            ActionPlanElementBeanCollection<ActionPlanCriterionBean> badCriterions) {
        ActionPlanElementBeanCollection<ActionPlanCriterionBean> retour = new ActionPlanElementBeanCollection<ActionPlanCriterionBean>(
                badCriterions.getIdEa(),
                badCriterions.getIdBline());
        for (Iterator<ActionPlanCriterionBean> it = previousActionPlan.iterator(); it.hasNext();) {
            ActionPlanCriterionBean criterion = it.next();
            ActionPlanCriterionBean newCriterion = badCriterions.get(criterion);
            if (newCriterion != null) {
                //not yet corrected
                int newNbElts = newCriterion.getNumberElt();
                int oldNbElts = criterion.getNumberElt();
                if (newNbElts > oldNbElts) {
                    retour.add(criterion);
                    it.remove();
                }
            }
        }
        return retour;
    }

    /**
     * @param previousActionPlan the previous action plan
     * @param badCriterions criterions which have a mark lower than 3 for the current baseline
     * @return Returns a criterion list containing criterions included in the action plan defined for the previous baseline,
     * and which have the same number of element to correct
     */
    public ActionPlanElementBeanCollection<ActionPlanCriterionBean> getStableCriterionsFromPreviousActionPlan(
            ActionPlanElementBeanCollection<ActionPlanCriterionBean> previousActionPlan,
            ActionPlanElementBeanCollection<ActionPlanCriterionBean> badCriterions) {
        ActionPlanElementBeanCollection<ActionPlanCriterionBean> retour = new ActionPlanElementBeanCollection<ActionPlanCriterionBean>(
                badCriterions.getIdEa(),
                badCriterions.getIdBline());
        for (Iterator<ActionPlanCriterionBean> it = previousActionPlan.iterator(); it.hasNext();) {
            ActionPlanCriterionBean criterion = it.next();
            ActionPlanCriterionBean newCriterion = badCriterions.get(criterion);
            if (newCriterion != null) {
                //not yet corrected
                int newNbElts = newCriterion.getNumberElt();
                int oldNbElts = criterion.getNumberElt();
                if (newNbElts == oldNbElts) {
                    retour.add(criterion);
                    it.remove();
                }
            }
        }
        return retour;
    }

    public Map<String, Map<String, Double>> retrieveVolumetryMetricsEvolution(ElementBean eltBean) {
        return DaoFactory.getInstance().getMetriqueDao().retrieveVolumetryMetricsEvolution(eltBean);
    }
}
