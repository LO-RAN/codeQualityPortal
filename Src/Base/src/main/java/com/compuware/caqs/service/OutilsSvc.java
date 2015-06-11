package com.compuware.caqs.service;

import com.compuware.caqs.constants.Constants;
import com.compuware.caqs.constants.MessagesCodes;
import com.compuware.caqs.dao.factory.DaoFactory;
import com.compuware.caqs.domain.dataschemas.MetriqueDefinitionBean;
import com.compuware.caqs.domain.dataschemas.OutilBean;
import com.compuware.caqs.exception.DataAccessException;
import com.compuware.toolbox.util.logging.LoggerManager;
import java.util.List;
import java.util.Locale;

/**
 *
 * @author cwfr-dzysman
 */
public class OutilsSvc {

    private static final OutilsSvc instance = new OutilsSvc();

    private OutilsSvc() {
    }

    public static OutilsSvc getInstance() {
        return instance;
    }
    protected static org.apache.log4j.Logger logger = LoggerManager.getLogger(Constants.LOG4J_IHM_LOGGER_KEY);

    /**
     * retrieve tools
     * @return tool's list
     */
    public List<OutilBean> retrieveTools() {
        return DaoFactory.getInstance().getOutilDao().retrieveTools();
    }

    /**
     * retrieves tools by id and/or lib
     * @param id tool's id pattern
     * @param lib tool's lib pattern
     * @param idLang language id
     * @return list of tools
     */
    public List<OutilBean> retrieveOutilByIdLib(String id, String lib, Locale loc) {
        return DaoFactory.getInstance().getOutilDao().retrieveOutilByIdLib(id, lib, loc.getLanguage());
    }

    /**
     * retrieves a tool by id
     * @param id tool's id pattern
     * @return a tool or null
     */
    public OutilBean retrieveToolById(String id) {
        return DaoFactory.getInstance().getOutilDao().retrieveOutilByIdWithMetricAndModelCount(id);
    }

    /**
     * saves a tool, updating it if it already exists, creating it otherwise
     * @param et the tool to save
     * @throws com.compuware.caqs.exception.DataAccessException
     */
    public MessagesCodes saveOutilsBean(OutilBean tool) {
        MessagesCodes retour = MessagesCodes.NO_ERROR;
        try {
            DaoFactory.getInstance().getOutilDao().saveOutilsBean(tool);
        } catch (DataAccessException dae) {
            logger.error(dae);
            retour = MessagesCodes.DATABASE_ERROR;
        }
        return retour;
    }

    /**
     * deletes a tool
     * @param id the id of the tool to delete
     * @throws com.compuware.caqs.exception.DataAccessException
     */
    public MessagesCodes deleteOutilsBean(String id) {
        MessagesCodes retour = MessagesCodes.NO_ERROR;
        try {
            DaoFactory.getInstance().getOutilDao().deleteOutilsBean(id);
        } catch (DataAccessException exc) {
            logger.error(exc);
            retour = MessagesCodes.DATABASE_ERROR;
        }
        return retour;
    }

    /**
     * list of tool's associated metrics
     * @param toolId tool's id
     * @return list of tool's associated metrics
     */
    public List<MetriqueDefinitionBean> retrieveAssociatedMetrics(String toolId) {
        return DaoFactory.getInstance().getOutilDao().retrieveAssociatedMetrics(toolId);
    }

    /**
     * retrieves tools associated to a model
     * @param model the model id
     * @return the tools associated
     */
    public List<OutilBean> retrieveOutilsByModelWithMetricCount(String model) {
        return DaoFactory.getInstance().getOutilDao().retrieveOutilsByModelWithMetricCount(model);
    }

    /**
     * retrieves all tools with associated metrics count
     * @return all tools
     */
    public List<OutilBean> retrieveToolsWithMetricCount() {
        return DaoFactory.getInstance().getOutilDao().retrieveToolsWithMetricCount();
    }

    /**
     * add a tool association for a model
     * @param modelId the model id
     * @param toolId the tool's id to add
     * @throws DataAccessException
     */
    public MessagesCodes addToolAssociationToModel(String modelId, String toolId) {
        MessagesCodes retour = MessagesCodes.NO_ERROR;
        try {
            DaoFactory.getInstance().getOutilDao().addToolAssociationToModel(modelId, toolId);
        } catch (DataAccessException exc) {
            logger.error(exc);
            retour = MessagesCodes.DATABASE_ERROR;
        }
        return retour;
    }

    /**
     * remove a tool association for a model
     * @param modelId the model id
     * @param toolId the tool's id to remove
     * @throws DataAccessException
     */
    public MessagesCodes removeToolAssociationForModel(String modelId, String toolId) {
        MessagesCodes retour = MessagesCodes.NO_ERROR;
        try {
            DaoFactory.getInstance().getOutilDao().removeToolAssociationForModel(modelId, toolId);
        } catch (DataAccessException exc) {
            logger.error(exc);
            retour = MessagesCodes.DATABASE_ERROR;
        }
        return retour;
    }
}
