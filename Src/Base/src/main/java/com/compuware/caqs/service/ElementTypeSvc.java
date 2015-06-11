package com.compuware.caqs.service;

import com.compuware.caqs.constants.Constants;
import com.compuware.caqs.constants.MessagesCodes;
import com.compuware.caqs.dao.factory.DaoFactory;
import com.compuware.caqs.domain.dataschemas.CriterionDefinition;
import com.compuware.caqs.domain.dataschemas.ElementType;
import com.compuware.caqs.domain.dataschemas.modeleditor.ElementTypeBean;
import com.compuware.caqs.exception.DataAccessException;
import com.compuware.toolbox.util.logging.LoggerManager;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 *
 * @author cwfr-dzysman
 */
public class ElementTypeSvc {

    private static final ElementTypeSvc instance = new ElementTypeSvc();

    private ElementTypeSvc() {
    }

    public static ElementTypeSvc getInstance() {
        return instance;
    }
    protected static org.apache.log4j.Logger logger = LoggerManager.getLogger(Constants.LOG4J_IHM_LOGGER_KEY);

    /**
     * retrieves element types by id and/or lib
     * @param id tool's id pattern
     * @param lib tool's lib pattern
     * @param idLang language id
     * @return list of element types
     */
    public List<ElementType> retrieveElementTypeByIdLib(String id, String lib, Locale loc) {
        return DaoFactory.getInstance().getElementTypeDao().retrieveElementTypesByIdLib(id, lib, loc.getLanguage());
    }


    /**
     * retrieves a element type by id
     * @param id element type's id pattern
     * @return a element type or null
     */
    public ElementTypeBean retrieveElementTypeById(String id) {
        return DaoFactory.getInstance().getElementTypeDao().retrieveElementTypeById(id);
    }

    /**
     * saves an element type, updating it if it already exists, creating it otherwise
     * @param et the element type to save
     * @throws com.compuware.caqs.exception.DataAccessException
     */
    public MessagesCodes saveElementTypeBean(ElementTypeBean et) {
        MessagesCodes retour = MessagesCodes.NO_ERROR;
        try {
            DaoFactory.getInstance().getElementTypeDao().saveElementTypeBean(et);
        } catch(DataAccessException dae) {
            logger.error(dae);
            retour = MessagesCodes.DATABASE_ERROR;
        }
        return retour;
    }

    /**
     * deletes an element type
     * @param id the id of the element type to delete
     * @throws com.compuware.caqs.exception.DataAccessException
     */
    public MessagesCodes deleteElementTypeBean(String id) {
        MessagesCodes retour = MessagesCodes.NO_ERROR;
        try {
            DaoFactory.getInstance().getElementTypeDao().deleteElementTypeBean(id);
        } catch(DataAccessException exc) {
            logger.error(exc);
            retour = MessagesCodes.DATABASE_ERROR;
        }
        return retour;
    }

    /**
     * retrieves all element types
     * @return all element types
     */
    public List<ElementType> retrieveAllElementTypes() {
        return DaoFactory.getInstance().getElementTypeDao().retrieveAllElementTypes();
    }

    /**
     * retrieves all criterions by models associated to an element type.
     * @param idTelt the element type
     * @return all criterions by models associated to an element type
     */
    public Map<String, List<CriterionDefinition>> retrieveAssociatedModelsAndCriterionsForElementType(String idTelt) {
        Map<String, List<CriterionDefinition>> retour = DaoFactory.getInstance().getElementTypeDao().retrieveAssociatedModelsAndCriterionsForElementType(idTelt);
        return retour;
    }

}
