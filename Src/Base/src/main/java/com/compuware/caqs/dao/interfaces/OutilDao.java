package com.compuware.caqs.dao.interfaces;

import com.compuware.caqs.domain.dataschemas.MetriqueDefinitionBean;
import java.util.Collection;

import com.compuware.caqs.domain.dataschemas.OutilBean;
import com.compuware.caqs.exception.DataAccessException;
import java.util.List;

public interface OutilDao {

    /**
     * Recherche d'un outil par son identifiant.
     * @param id l'identifiant de l'outil.
     * @return l'outil trouve ou null;
     */
    public abstract OutilBean retrieveOutilById(java.lang.String id);

    /**
     * Recherche de tous les outils utilises sur une baseline.
     * @return la colleciotn de tous les outils trouves;
     */
    public abstract Collection<OutilBean> retrieveOutilByBaseline(String idBline);

    /**
     * retrieve tools
     * @return tool's list
     */
    public abstract List<OutilBean> retrieveTools();

    /**
     * retrieves tools by id and/or lib
     * @param id tool's id pattern
     * @param lib tool's lib pattern
     * @param idLang language id
     * @return list of tools
     */
    public abstract List<OutilBean> retrieveOutilByIdLib(String id, String lib, String idLang);

    /**
     * retrieves a tool by id
     * @param id tool's id pattern
     * @return a tool or null
     */
    public OutilBean retrieveOutilByIdWithMetricAndModelCount(String id);

    /**
     * saves a tool, updating it if it already exists, creating it otherwise
     * @param tool the tool to save
     * @throws com.compuware.caqs.exception.DataAccessException
     */
    public void saveOutilsBean(OutilBean tool) throws DataAccessException;

    /**
     * deletes a tool
     * @param id the id of the tool to delete
     * @throws com.compuware.caqs.exception.DataAccessException
     */
    public void deleteOutilsBean(String id) throws DataAccessException;

    /**
     * list of tool's associated metrics
     * @param toolId tool's id
     * @return list of tool's associated metrics
     */
    public List<MetriqueDefinitionBean> retrieveAssociatedMetrics(String toolId);

    /**
     * retrieves tools associated to a model
     * @param model the model id
     * @return the tools associated
     */
    public List<OutilBean> retrieveOutilsByModelWithMetricCount(String model);

    /**
     * retrieves all tools with associated metrics count
     * @return all tools
     */
    public List<OutilBean> retrieveToolsWithMetricCount();

    /**
     * remove a tool association for a model
     * @param modelId the model id
     * @param toolId the tool's id to remove
     * @throws DataAccessException
     */
    public void removeToolAssociationForModel(String modelId, String toolId) throws DataAccessException;

    /**
     * add a tool association for a model
     * @param modelId the model id
     * @param toolId the tool's id to add
     * @throws DataAccessException
     */
    public void addToolAssociationToModel(String modelId, String toolId) throws DataAccessException;
}
