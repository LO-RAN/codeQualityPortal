package com.compuware.caqs.service;

import com.compuware.caqs.constants.Constants;
import com.compuware.caqs.constants.MessagesCodes;
import java.util.List;

import com.compuware.caqs.dao.factory.DaoFactory;
import com.compuware.caqs.dao.interfaces.DialecteDao;
import com.compuware.caqs.domain.dataschemas.DialecteBean;
import com.compuware.caqs.domain.dataschemas.LanguageBean;
import com.compuware.caqs.domain.dataschemas.modeleditor.ModelEditorDialecteBean;
import com.compuware.caqs.domain.dataschemas.modeleditor.ModelEditorLanguageBean;
import com.compuware.caqs.exception.DataAccessException;
import com.compuware.toolbox.util.logging.LoggerManager;
import java.util.Collection;

/**
 * The dialect and language service.
 * @author cwfr-fdubois
 */
public final class DialecteSvc {
    protected static org.apache.log4j.Logger logger = LoggerManager.getLogger(Constants.LOG4J_IHM_LOGGER_KEY);

    /** The service unique instance. */
    private static DialecteSvc instance = new DialecteSvc();

    /** The private constructor for the unique instance. */
    private DialecteSvc() {
    }

    /**
     * The the unique service instance.
     * @return the unique service instance.
     */
    public static DialecteSvc getInstance() {
        return instance;
    }

    /**
     * Retrieve the dialect of the given element.
     * @param idElt the element ID.
     * @return the dialect of the given element.
     */
    public DialecteBean retrieveDialecteByElementId(String idElt) {
        DialecteDao dao = DaoFactory.getInstance().getDialecteDao();
        return dao.retrieveDialecteByElementId(idElt);
    }

    /**
     * Retrieve the list of distinct dialects used by the given module list.
     * @param eltList the module list.
     * @return the list of distinct dialects used by the given module list.
     */
    public List<DialecteBean> retrieveDialects(String[] eltList) {
        DialecteDao dao = DaoFactory.getInstance().getDialecteDao();
        return dao.retrieveDialects(eltList);
    }

    /**
     * renvoie tous les dialectes
     * @return une collection contenant tous les dialectes
     */
    public Collection<DialecteBean> retrieveAllDialectes() {
        return DaoFactory.getInstance().getDialecteDao().retrieveAllDialectes();
    }

    /**
     * retrieves languages by id and/or lib
     * @param id language's id pattern
     * @param lib language's lib pattern
     * @return list of languages
     */
    public List<DialecteBean> retrieveDialectesByIdLib(String id, String lib) {
        return DaoFactory.getInstance().getDialecteDao().retrieveDialectesByIdLib(id,
                lib);
    }

    /**
     * retrieve one dialect, by id, with the number of associated eas
     * @param id dialect's id
     * @return one dialect, by id, with the number of associated eas
     */
    public ModelEditorDialecteBean retrieveDialectWithAssociationCountById(String id) {
        return DaoFactory.getInstance().getDialecteDao().retrieveDialectWithAssociationCountById(id);
    }

    /**
     * retrieve languages
     * @return languages
     */
    public List<LanguageBean> retrieveLanguages() {
        return DaoFactory.getInstance().getDialecteDao().retrieveLanguages();
    }


    /**
     * deletes a metric
     * @param id the id of the metric to delete
     * @throws com.compuware.caqs.exception.DataAccessException
     */
    public MessagesCodes deleteDialecteBean(String id) {
        MessagesCodes retour = MessagesCodes.NO_ERROR;
        try {
            DaoFactory.getInstance().getDialecteDao().deleteDialecteBean(id);
        } catch(DataAccessException exc) {
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
    public MessagesCodes saveDialecteBean(DialecteBean dialecte) {
        MessagesCodes retour = MessagesCodes.NO_ERROR;
        try {
            DaoFactory.getInstance().getDialecteDao().saveDialecteBean(dialecte);
        } catch(DataAccessException dae) {
            logger.error(dae);
            retour = MessagesCodes.DATABASE_ERROR;
        }
        return retour;
    }


    /**
     * retrieve one language, by id, with the number of associated dialects
     * @param id language's id
     * @return one language, by id, with the number of associated dialects
     */
    public ModelEditorLanguageBean retrieveLanguageWithAssociationCountById(String id) {
        return DaoFactory.getInstance().getDialecteDao().retrieveLanguageWithAssociationCountById(id);
    }

    /**
     * deletes a metric
     * @param id the id of the metric to delete
     * @throws com.compuware.caqs.exception.DataAccessException
     */
    public MessagesCodes deleteLanguageBean(String id) {
        MessagesCodes retour = MessagesCodes.NO_ERROR;
        try {
            DaoFactory.getInstance().getDialecteDao().deleteLanguageBean(id);
        } catch(DataAccessException exc) {
            logger.error(exc);
            retour = MessagesCodes.DATABASE_ERROR;
        }
        return retour;
    }

    /**
     * saves a language, updating it if it already exists, creating it otherwise
     * @param dialecte the language to save
     * @throws com.compuware.caqs.exception.DataAccessException
     */
    public MessagesCodes saveLanguageBean(LanguageBean dialecte) {
        MessagesCodes retour = MessagesCodes.NO_ERROR;
        try {
            DaoFactory.getInstance().getDialecteDao().saveLanguageBean(dialecte);
        } catch(DataAccessException dae) {
            logger.error(dae);
            retour = MessagesCodes.DATABASE_ERROR;
        }
        return retour;
    }

    /**
     *
     * @param idLangage
     * @return
     */
    public List<DialecteBean> retrieveDialectsByLanguages(String idLangage) {
       return DaoFactory.getInstance().getDialecteDao().retrieveDialectsByLanguages(idLangage);
    }

    public List<LanguageBean> retrieveLanguagesByIdLib(String id, String lib) {
        return DaoFactory.getInstance().getDialecteDao().retrieveLanguagesByIdLib(id, lib);
    }
}
