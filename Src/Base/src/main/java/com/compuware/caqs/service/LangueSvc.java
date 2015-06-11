package com.compuware.caqs.service;

import com.compuware.caqs.constants.Constants;
import com.compuware.caqs.constants.MessagesCodes;
import com.compuware.caqs.dao.factory.DaoFactory;
import com.compuware.caqs.domain.dataschemas.modeleditor.LangueBean;
import com.compuware.caqs.exception.DataAccessException;
import com.compuware.toolbox.util.logging.LoggerManager;
import java.util.List;

/**
 *
 * @author cwfr-dzysman
 */
public class LangueSvc {

    private static final LangueSvc instance = new LangueSvc();

    private LangueSvc() {
    }

    public static LangueSvc getInstance() {
        return instance;
    }
    protected static org.apache.log4j.Logger logger = LoggerManager.getLogger(Constants.LOG4J_IHM_LOGGER_KEY);

    /**
     * retrieves languages by id and/or lib
     * @param id language's id pattern
     * @param lib language's lib pattern
     * @return list of languages
     */
    public List<LangueBean> retrieveLanguesByIdLib(String id, String lib) {
        return DaoFactory.getInstance().getLangueDao().retrieveLanguesByIdLib(id,
                lib);
    }

    /**
     * retrieves a language by id
     * @param id language's id pattern
     * @return a language or null
     */
    public LangueBean retrieveLangueById(String id) {
        return DaoFactory.getInstance().getLangueDao().retrieveLangueById(id);
    }

    /**
     * saves a languebean, updating it if it already exists, creating it otherwise
     * @param lb the languebean to save
     * @throws com.compuware.caqs.exception.DataAccessException
     */
    public MessagesCodes saveLangueBean(LangueBean lb) {
        MessagesCodes retour = MessagesCodes.NO_ERROR;
        try {
            DaoFactory.getInstance().getLangueDao().saveLangueBean(lb);
        } catch (DataAccessException exc) {
            logger.error(exc.getMessage());
            retour = MessagesCodes.DATABASE_ERROR;
        }
        return retour;
    }

    /**
     * deletes a languebean
     * @param id the id of the languebean to delete
     * @throws com.compuware.caqs.exception.DataAccessException
     */
    public MessagesCodes deleteLangueBean(String id) {
        MessagesCodes retour = MessagesCodes.NO_ERROR;
        try {
            DaoFactory.getInstance().getLangueDao().deleteLangueBean(id);
        } catch (DataAccessException exc) {
            logger.error(exc.getMessage());
            retour = MessagesCodes.DATABASE_ERROR;
        }
        return retour;
    }
    /**
     * retrieves all languages
     * @return all languages or an empty list if none exists
     * @throws com.compuware.caqs.exception.DataAccessException
     */
    public List<LangueBean> retrieveAllLanguages() {
        return DaoFactory.getInstance().getLangueDao().retrieveAllLanguages();
    }

}
