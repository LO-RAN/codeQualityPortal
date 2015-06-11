package com.compuware.caqs.dao.interfaces;

import com.compuware.caqs.domain.dataschemas.CriterionDefinition;
import java.sql.Connection;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.compuware.caqs.domain.dataschemas.ElementBean;
import com.compuware.caqs.domain.dataschemas.ElementMetricsBean;
import com.compuware.caqs.domain.dataschemas.MetriqueBean;
import com.compuware.caqs.domain.dataschemas.MetriqueDefinitionBean;
import com.compuware.caqs.domain.dataschemas.ScatterDataBean;
import com.compuware.caqs.domain.dataschemas.evolutions.ElementsCategory;
import com.compuware.caqs.domain.dataschemas.upload.UpdatePolicy;
import com.compuware.caqs.exception.DataAccessException;

public interface MetriqueDao {

    /**
     * retrieves the list of metric associated to a criterion for a quality model
     * @param idCrit criterion's id
     * @param idUsa the model's id
     * @return the list of associated metrics
     */
    public abstract List<MetriqueDefinitionBean> retrieveMetriqueDefinitionByIdCrit(
            String idCrit, String idUsa);

    public abstract List<MetriqueDefinitionBean> retrieveMetriqueDefinitionByUsage(String idUsa);

    public abstract Collection retrieveAllMetriques();

    public abstract HashMap retrieveAllMetriquesMap();

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
            String idCrit, String idUsa, String toolId, String filterId, String filterLib, String idLoc);

    /**
     * Retrieve the map of metrics calculated for the given baseline for methods.
     * @param idBline the baseline id.
     * @param idTelt the element type id.
     * @return the map of metrics calculated for the given baseline for methods.
     */
    public abstract Map<String, MetriqueDefinitionBean> retrieveMetriqueDefinition(String idBline, String idTelt);

    /**
     * Retrieve the collection of metrics calculated for the given baseline for methods.
     * @param idBline the baseline id.
     * @return the collection of metrics calculated for the given baseline for methods.
     */
    public abstract Collection retrieveMetriqueDefinitionCollection(String idEa, String idBline);

    public abstract MetriqueBean retrieveQametriqueFromMetEltBline(
            String idBline, String idMet, String idElt);

    public abstract MetriqueBean retrieveAverageQametriqueFromMetEltBline(
            String idBline, String idMet, String idPro);

    public abstract Map<String, Map<String, MetriqueBean>> retrieveQametriqueFromMetListBline(
            String idBline, String idMetList);

    public abstract Map<String, Map<String, MetriqueBean>> retrieveQametriqueMapFromMetListBline(String idBline,
            String metList);

    /**
     * Retrieve the scatterplot data for a given element and metrics.
     * @param ea the current element.
     * @param metH the X-coordinate metric ID.
     * @param metV the Y-coordinate metric ID.
     * @param idTelt the element type ID.
     * @return the list of data found.
     */
    public abstract List<ScatterDataBean> retrieveScatterPlot(ElementBean ea, String metH,
            String metV, String idTelt);

    public abstract void setMetrique(String idElt, String idBline,
            String idMet, double valbrute) throws DataAccessException;

    /**
     * Set the given element metrics into the database.
     * @param eltBean the element metrics to set.
     * @param idBline the baseline ID.
     * @param existingMetrics the existing metrics in the repository.
     * @param updatePolicy the update policy if metrics already exists.
     * @throws com.compuware.caqs.exception.DataAccessException if an exception occured during database access.
     */
    public abstract void setMetrique(ElementMetricsBean eltBean,
            String idBline, HashMap existingMetrics, UpdatePolicy updatePolicy) throws DataAccessException;

    /**
     * Set the given element metrics into the database.
     * @param eltBean the element to set metrics for.
     * @param metList the metric list to set.
     * @param idBline the baseline ID.
     * @param existingMetrics the existing metrics in the repository.
     * @param updatePolicy the update policy if metrics already exists.
     * @throws com.compuware.caqs.exception.DataAccessException if an exception occured during database access.
     */
    public abstract void setMetrique(ElementBean eltBean, Collection metList,
            String idBline, Map existingMetrics, UpdatePolicy updatePolicy) throws DataAccessException;

    /**
     * retrieves loc, comment loc, ifpug for an ea and a bline
     * @param eltBean ea
     * @param idBline bline
     * @return volumetry metrics
     */
    public abstract Map<String, Double> retrieveVolumetryMetrics(ElementBean eltBean, String idBline);

    /**
     * 
     * @param idElt
     * @param idBline
     * @param idCrit
     * @param idUsa
     * @param tendance
     * @return
     */
    public abstract Collection<MetriqueBean> retrieveQametriqueLines(String idElt,
            String idBline, String idCrit, String idUsa, ElementsCategory tendance);

    public abstract void saveQametriqueLines(String idElt, String idBline,
            Collection metrics);

    public void setMetrique(String idElt, String idBline, String idMet, double valbrute, Connection connection, boolean doCommit) throws DataAccessException;

    public abstract Collection retrieveAllQametriqueForEa(String idEa, String idBline);

    public abstract void updateVolumetrie(String idElt, String idBline, Connection connection) throws DataAccessException;

    public abstract void insertMetrics(Map<String, MetriqueBean> metricMap, String idBline) throws DataAccessException;

    /**
     * retrieves metrics using id, lib and tool's lib
     * @param id
     * @param lib
     * @param toolLib
     * @param idLang
     * @return
     */
    public List<MetriqueDefinitionBean> retrieveMetricsByIdLibTool(String id, String lib, String toolId, String idLang);

    /**
     * retrieves a metric by its id
     * @param id the id
     * @return the metric, null if it does not exists
     */
    public MetriqueDefinitionBean retrieveMetricById(String id);

    /**
     * saves a metric, updating it if it already exists, creating it otherwise
     * @param metric the metric to save
     * @throws com.compuware.caqs.exception.DataAccessException
     */
    public void saveMetricBean(MetriqueDefinitionBean metric) throws DataAccessException;

    /**
     * deletes a metric
     * @param id the id of the metric to delete
     * @throws com.compuware.caqs.exception.DataAccessException
     */
    public void deleteMetricBean(String id) throws DataAccessException;

    /**
     * deletes a rule
     * @param idMet metric's id
     * @param idUsa model's id
     * @param idCrit criterion's id
     * @throws DataAccessException
     */
    public void deleteRegle(String idMet, String idUsa, String idCrit) throws DataAccessException;

    /**
     * creates a rule
     * @param idMet metric's id
     * @param idUsa model's id
     * @param idCrit criterion's id
     * @throws DataAccessException
     */
    public void createRegle(String idMet, String idUsa, String idCrit) throws DataAccessException;

    /**
     * renvoie la liste des metriques, extraite de la liste envoyee
     * @param metrics
     * @param idUsa
     * @return
     */
    public List<String> retrieveCorrectMetricsFromProvidedForModel(List<String> metrics, String idUsa);

    /**
     * deletes all qametrique for an application entity's sub-elements, a baseline and a given metric
     * @param idElt ea id
     * @param idMet metric id
     * @param idBline baseline id
     * @throws DataAccessException
     */
    public void deleteMetricForBaselineAndElement(String idElt, String idMet, String idBline) throws DataAccessException;

    /**
     * retrieves a metric with the number of time it has been selected for use in a criterion's computation
     * @param id metric id
     * @return metric with the number of time it has been selected for use in a criterion's computation
     */
    public MetriqueDefinitionBean retrieveMetricWithAssociationCountById(String id);

    /**
     * retrieves all models and criterions associated to the metric, by models
     * @param idMet metric's id
     * @return all models and criterions associated to the metric, by models
     */
    public Map<String, List<CriterionDefinition>> retrieveAssociatedModelsAndCriterionsForMetric(String idMet);

    /**
     * retrieves metrics synthesis volumetry evolution
     * @param eltBean
     * @return
     */
    public Map<String, Map<String, Double>> retrieveVolumetryMetricsEvolution(ElementBean eltBean);
}
