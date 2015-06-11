/**
 * 
 */
package com.compuware.caqs.service;

import com.compuware.caqs.constants.Constants;
import com.compuware.caqs.constants.MessagesCodes;
import java.util.Collection;
import java.util.Iterator;

import com.compuware.caqs.dao.factory.DaoFactory;
import com.compuware.caqs.dao.interfaces.MetriqueDao;
import com.compuware.caqs.domain.dataschemas.BaselineBean;
import com.compuware.caqs.domain.dataschemas.CriterionDefinition;
import com.compuware.caqs.domain.dataschemas.DonneesBrutes;
import com.compuware.caqs.domain.dataschemas.ElementBean;
import com.compuware.caqs.domain.dataschemas.MetriqueBean;
import com.compuware.caqs.domain.dataschemas.MetriqueDefinitionBean;
import com.compuware.caqs.domain.dataschemas.UsageBean;
import com.compuware.caqs.domain.dataschemas.evolutions.ElementsCategory;
import com.compuware.caqs.domain.dataschemas.modelmanager.FormulaPartOperand;
import com.compuware.caqs.domain.dataschemas.modelmanager.IFPUGFormulaForm;
import com.compuware.caqs.exception.DataAccessException;
import com.compuware.caqs.service.modelmanager.CaqsFormulaManager;
import com.compuware.caqs.service.modelmanager.CaqsQualimetricModelManager;
import com.compuware.toolbox.util.logging.LoggerManager;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * @author cwfr-fdubois
 *
 */
public class MetricSvc {

    protected static org.apache.log4j.Logger logger = LoggerManager.getLogger(Constants.LOG4J_IHM_LOGGER_KEY);
    private static final MetricSvc instance = new MetricSvc();

    private MetricSvc() {
    }

    public static MetricSvc getInstance() {
        return instance;
    }

    /**
     * retrieves all metrics not associated to the criterion for the model
     * @param idCrit criterion's id
     * @param idUsa model's id
     * @param toolId tool's id
     * @param filterId filter on metric's id
     * @param filterLib filter on metric's lib
     * @param idLoc locale id used for lib filter
     * @return metrics
     */
    public List<MetriqueDefinitionBean> retrieveMetriqueDefinitionNotAssociatedToCriterionAndModelByTool(
            String idCrit, String idUsa, String toolId, String filterId, String filterLib, String idLoc) {
        DaoFactory daoFactory = DaoFactory.getInstance();
        MetriqueDao metriqueDao = daoFactory.getMetriqueDao();
        return metriqueDao.retrieveMetriqueDefinitionNotAssociatedToCriterionAndModelByTool(idCrit, idUsa, toolId,
                filterId, filterLib, idLoc);
    }

    /**
     * retrieves the list of metric associated to a criterion for a quality model
     * @param idCrit criterion's id
     * @param idUsa the model's id
     * @return the list of associated metrics
     */
    public List<MetriqueDefinitionBean> retrieveMetriqueDefinitionByIdCrit(String idCrit, String idUsa) {
        DaoFactory daoFactory = DaoFactory.getInstance();
        MetriqueDao metriqueDao = daoFactory.getMetriqueDao();
        return metriqueDao.retrieveMetriqueDefinitionByIdCrit(idCrit, idUsa);
    }

    public String retrieveAllMetricsAsCsv(ElementBean ea, BaselineBean bline) {
        DaoFactory daoFactory = DaoFactory.getInstance();
        MetriqueDao metriqueDao = daoFactory.getMetriqueDao();
        Collection metDefColl = metriqueDao.retrieveMetriqueDefinitionCollection(ea.getId(), bline.getId());
        StringBuffer result = new StringBuffer("Nom;Type;");
        Iterator i = metDefColl.iterator();
        MetriqueBean currentMetric = null;
        while (i.hasNext()) {
            currentMetric = (MetriqueBean) i.next();
            result.append(currentMetric.getId()).append(';');
        }
        result.append('\n');
        Collection eltMetColl = metriqueDao.retrieveAllQametriqueForEa(ea.getId(), bline.getId());
        i = eltMetColl.iterator();
        DonneesBrutes currentElement = null;
        while (i.hasNext()) {
            currentElement = (DonneesBrutes) i.next();
            print(result, currentElement, metDefColl);
        }
        return result.toString();
    }

    public void print(StringBuffer out, DonneesBrutes data, Collection metCollection) {
        Iterator i = metCollection.iterator();
        MetriqueBean actualMetric = null;
        out.append(data.getDesc()).append(';').append(data.getTypeElt()).append(';');
        while (i.hasNext()) {
            actualMetric = (MetriqueBean) i.next();
            Double val = data.getValue(actualMetric.getId());
            if (val != null) {
                out.append(val.toString());
            }
            out.append(';');
        }
        out.append('\n');
    }

    public double getAllCodeValue(ElementBean elt) {
        //c'est une feuille, on recupere la metrique
        MetriqueDao metriqueDao = DaoFactory.getInstance().getMetriqueDao();
        MetriqueBean mb = metriqueDao.retrieveQametriqueFromMetEltBline(
                elt.getBaseline().getId(), "ALL_CODE", elt.getId());
        return mb.getValbrute();
    }

    public double getIFPUGValue(ElementBean elt) {
        //c'est une feuille, on recupere la metrique
        MetriqueDao metriqueDao = DaoFactory.getInstance().getMetriqueDao();
        MetriqueBean mb = metriqueDao.retrieveQametriqueFromMetEltBline(
                elt.getBaseline().getId(), "IFPUG", elt.getId());
        return mb.getValbrute();
    }

    public List<MetriqueDefinitionBean> retrieveMetricsByIdLibTool(String id, String lib, String toolId, Locale loc) {
        MetriqueDao metriqueDao = DaoFactory.getInstance().getMetriqueDao();
        return metriqueDao.retrieveMetricsByIdLibTool(id, lib, toolId, loc.getLanguage());
    }

    public MetriqueDefinitionBean retrieveMetricById(String id) {
        MetriqueDao metriqueDao = DaoFactory.getInstance().getMetriqueDao();
        return metriqueDao.retrieveMetricById(id);
    }

    /**
     * retrieves a metric with the number of time it has been selected for use in a criterion's computation
     * @param id metric id
     * @return metric with the number of time it has been selected for use in a criterion's computation
     */
    public MetriqueDefinitionBean retrieveMetricWithAssociationCountById(String id) {
        MetriqueDao metriqueDao = DaoFactory.getInstance().getMetriqueDao();
        return metriqueDao.retrieveMetricWithAssociationCountById(id);
    }

    /**
     * deletes a metric
     * @param id the id of the metric to delete
     * @throws com.compuware.caqs.exception.DataAccessException
     */
    public MessagesCodes deleteMetricBean(String id) {
        MessagesCodes retour = MessagesCodes.NO_ERROR;
        try {
            DaoFactory.getInstance().getMetriqueDao().deleteMetricBean(id);
        } catch (DataAccessException exc) {
            logger.error(exc);
            retour = MessagesCodes.DATABASE_ERROR;
        }
        return retour;
    }

    /**
     * saves a metric, updating it if it already exists, creating it otherwise
     * @param et the metric to save
     * @throws com.compuware.caqs.exception.DataAccessException
     */
    public MessagesCodes saveMetricBean(MetriqueDefinitionBean tool) {
        MessagesCodes retour = MessagesCodes.NO_ERROR;
        try {
            DaoFactory.getInstance().getMetriqueDao().saveMetricBean(tool);
        } catch (DataAccessException dae) {
            logger.error(dae);
            retour = MessagesCodes.DATABASE_ERROR;
        }
        return retour;
    }

    /**
     * deletes a rule
     * @param idMet metric's id
     * @param idUsa model's id
     * @param idCrit criterion's id
     * @throws DataAccessException
     */
    public MessagesCodes deleteRegle(String idMet, String idUsa, String idCrit) {
        MessagesCodes retour = MessagesCodes.NO_ERROR;
        try {
            DaoFactory.getInstance().getMetriqueDao().deleteRegle(idMet, idUsa, idCrit);
        } catch (DataAccessException dae) {
            logger.error(dae);
            retour = MessagesCodes.DATABASE_ERROR;
        }
        return retour;
    }

    /**
     * creates a rule
     * @param idMet metric's id
     * @param idUsa model's id
     * @param idCrit criterion's id
     * @throws DataAccessException
     */
    public MessagesCodes createRegle(String idMet, String idUsa, String idCrit) {
        MessagesCodes retour = MessagesCodes.NO_ERROR;
        try {
            DaoFactory.getInstance().getMetriqueDao().createRegle(idMet, idUsa, idCrit);
        } catch (DataAccessException dae) {
            logger.error(dae);
            retour = MessagesCodes.DATABASE_ERROR;
        }
        return retour;
    }

    /**
     * renvoie la liste des metriques, extraite de la liste envoyee
     * @param metrics
     * @param idUsa
     * @return
     */
    public List<String> retrieveCorrectMetricsFromProvidedForModel(List<String> metrics, String idUsa) {
        return DaoFactory.getInstance().getMetriqueDao().retrieveCorrectMetricsFromProvidedForModel(metrics, idUsa);
    }

    /**
     * retrieves all models and criterions associated to the metric, by models
     * @param idMet metric's id
     * @return all models and criterions associated to the metric, by models
     */
    public Map<String, List<CriterionDefinition>> retrieveAssociatedModelsAndCriterionsForMetric(String idMet) {
        return DaoFactory.getInstance().getMetriqueDao().retrieveAssociatedModelsAndCriterionsForMetric(idMet);
    }

    /**
     * 
     * @param idElt
     * @param idBline
     * @param idCrit
     * @param idUsa
     * @param tendance
     * @return
     */
    public Collection<MetriqueBean> retrieveQametriqueLines(String idElt,
            String idBline, String idCrit, String idUsa, ElementsCategory tendance) {
        return DaoFactory.getInstance().getMetriqueDao().retrieveQametriqueLines(idElt, idBline, idCrit, idUsa, tendance);
    }

    /**
     * recherche dans toutes les formules definies pour un calcul d'ifpug
     * si la metrique est utilisee
     * @param metricId identifiant de metrique
     * @return true si la metrique est utilisee, false sinon
     */
    public boolean isMetricUsedInIFPUGComputation(String metricId) {
        boolean retour = false;
        CaqsQualimetricModelManager modelManager = null;
        Collection<UsageBean> models = ModelSvc.getInstance().retrieveAllModels();
        for (UsageBean model : models) {
            modelManager = CaqsQualimetricModelManager.getQualimetricModelManager(model.getId());
            if (modelManager != null) {
                IFPUGFormulaForm ifpug = modelManager.getIFPUGFormula();
                if (ifpug != null) {
                    List<FormulaPartOperand> metrics = CaqsFormulaManager.getAllMetrics(ifpug.getFormula());
                    for (FormulaPartOperand metric : metrics) {
                        if (metric.getValue().equals(metricId)) {
                            retour = true;
                            break;
                        }
                    }
                    if (retour) {
                        break;
                    }
                }
            }
        }
        return retour;
    }
}
