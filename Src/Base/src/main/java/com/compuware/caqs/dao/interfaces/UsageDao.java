package com.compuware.caqs.dao.interfaces;

import java.util.Collection;
import java.util.Map;

import com.compuware.caqs.domain.dataschemas.UsageBean;
import com.compuware.caqs.domain.dataschemas.modeleditor.ModelEditorModelBean;
import com.compuware.caqs.exception.DataAccessException;
import java.util.List;

public interface UsageDao {

    /**
     * Retrieve all models from database.
     * @return a model collection.
     */
    public Collection<UsageBean> retrieveAllUsages();

    /**
     * Retrieve a model according to an element unique ID.
     * @param idElt the element unique ID.
     * @return the model found.
     */
    public UsageBean retrieveUsageByElementId(String idElt);

    /**
     * Retrieve a model factor definition.
     * @param idUsa the model unique ID.
     */
    public Map<String, Map<String, Double>> retrieveFactorDefinition(String idUsa);

    /**
     * Retrieve criterion ids for a given model and element type.
     * @param idUsa the given model.
     * @param idTelt the given type of element.
     * @return the criterion colleciton.
     */
    public Collection<String> retrieveCriterionsForElement(String idUsa, String idTelt);

    /**
     * Retrieve criterion ids for a given model and element type.
     * @param idUsa the given model.
     * @param idTelt the given type of element.
     * @return the criterion colleciton.
     */
    public Collection<String> retrieveCriterionsForElement(String idUsa, Collection<String> idTelt);

    /**
     * Retrieve a criterion map based on element types for a given model.
     * @param idUsa the given model unique ID.
     * @param typeEltColl the given collection of element types.
     * @return the criterion map associated to the model.
     */
    public Map<String, Collection<String>> retrieveCriterionMapForElement(String idUsa, Collection<String> typeEltColl);

    /**
     * Retrieve a criterion element type map.
     * @param idUsa the model ID.
     * @return the criterion element type map.
     * @throws DataAccessException
     */
    public Map<String, String> retrieveCriterionElementTypeMap(String idUsa) throws DataAccessException;

    /**
     * Retrieve a model by id
     * @param id the model's to retrieve id
     * @return the retrieved model, if any, null otherwise
     */
    public UsageBean retrieveUsageById(java.lang.String id);

    /**
     * Retrieve models by id and/or lib
     * @param id the pattern to search for the id
     * @param lib the pattern to search for the lib
     * @param idLoc le language id
     * @return the retrieved models, if any, an empty list
     */
    public List<UsageBean> retrieveModelsByIdAndLib(String id, String lib, String idLoc);

    /**
     * saves a model, updating it if it already exists, creating it otherwise
     * @param model the model to save
     * @throws com.compuware.caqs.exception.DataAccessException
     */
    public void saveModelBean(UsageBean model) throws DataAccessException;

    /**
     * deletes a model
     * @param id the id of the model to delete
     * @throws com.compuware.caqs.exception.DataAccessException
     */
    public void deleteQualityModel(String id) throws DataAccessException;

    /**
     * Duplicate a model in the database
     * @param modelId model to duplicate id
     * @param newModelId new model id
     * @throws DataAccessException
     */
    public void duplicateModel(String modelId, String newModelId) throws DataAccessException;

    /**
     * delete a goal association from this model
     * @param modelId model id
     * @param goalId goal to delete id
     * @return a list of criterions id which are not used anymore
     * @throws DataAccessException
     */
    public List<String> removeGoalFromModel(String modelId, String goalId) throws DataAccessException;

    /**
     * retrieves a model with the number of eas using it
     * @param id model'id to search for
     * @return model with the number of eas using it, if there is one
     */
    public ModelEditorModelBean retrieveModelWithAssociationCountById(String id);

    /**
     * assigne archivedModelId a toutes les eas assignnees a modelId
     * @param modelId
     * @param archivedModelId
     */
    public void archiveModel(String modelId, String archivedModelId);
}
