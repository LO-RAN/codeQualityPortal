package com.compuware.caqs.service;

import com.compuware.caqs.constants.Constants;
import com.compuware.caqs.constants.MessagesCodes;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.List;

import com.compuware.caqs.dao.factory.DaoFactory;
import com.compuware.caqs.dao.interfaces.BaselineDao;
import com.compuware.caqs.dao.interfaces.FactorDao;
import com.compuware.caqs.domain.dataschemas.BaselineBean;
import com.compuware.caqs.domain.dataschemas.ElementBean;
import com.compuware.caqs.domain.dataschemas.ElementFactorBaseline;
import com.compuware.caqs.domain.dataschemas.FactorBean;
import com.compuware.caqs.domain.dataschemas.UsageBean;
import com.compuware.caqs.domain.dataschemas.formatter.ElementFactorBaselineCsvFormatter;
import com.compuware.caqs.domain.dataschemas.formatter.Formatter;
import com.compuware.caqs.domain.dataschemas.modeleditor.ModelEditorFactorBean;
import com.compuware.caqs.exception.CaqsException;
import com.compuware.caqs.exception.DataAccessException;
import com.compuware.toolbox.util.logging.LoggerManager;
import java.util.Locale;

public class FactorSvc {

    private static final FactorSvc instance = new FactorSvc();
    protected static org.apache.log4j.Logger logger = LoggerManager.getLogger(Constants.LOG4J_IHM_LOGGER_KEY);
    private static final FactorDao dao = DaoFactory.getInstance().getFactorDao();

    private FactorSvc() {
    }

    public static FactorSvc getInstance() {
        return instance;
    }

    public void writeAllElementFactors(PrintWriter writer) {
        DaoFactory daoFactory = DaoFactory.getInstance();
        BaselineDao baselineDao = daoFactory.getBaselineDao();
        List<BaselineBean> baselineList = baselineDao.retrieveAllValidBaseline();
        List<ElementFactorBaseline> allEltFactBaseline = dao.retrieveAllElementFactor();
        Formatter formatter = new ElementFactorBaselineCsvFormatter(baselineList);
        formatter.format(allEltFactBaseline, writer, null, null);
    }

    public List<FactorBean> retrieveFactorList(ElementBean eltBean) throws CaqsException {
        return dao.retrieveFactorList(eltBean);
    }

    public double getAverageFactorMarkForElement(String idElt, String idBline) {
        return dao.retrieveAverageFactorMarkForElement(idElt, idBline);
    }

    /**
     * renvoie l'union de l'ensemble des objectifs pour les elements envoyes en parametre
     * @param elements elements
     * @return renvoie l'union de l'ensemble des objectifs pour les elements envoyes en parametre
     */
    public List<FactorBean> retrieveAllFactorsForElements(List<ElementBean> elements) {
        return dao.retrieveAllFactorsForElements(elements);
    }

    /**
     * renvoie tous les objectifs associes a un element, pour une baseline
     * @param idElt element
     * @param idPro identifiant du projet de l'element
     * @param idBline baseline
     * @return tous les objectifs associes a un element, pour une baseline
     */
    public List<FactorBean> retrieveAllFactorsForElement(String idElt, String idPro, String idBline) {
        return dao.retrieveAllFactorsForElement(idElt, idPro, idBline);
    }

    /**
     * Renvoie un FactorBean complete avec les informations sur l'objectif,
     * la note et la labellisation, si elle existe
     * @param idBline identifiant de baseline
     * @param idPro identifiant de projet
     * @param idElt identifiant de l'element pour lequel on veut la note
     * @param idFac identifiant de l'objectif
     * @return Renvoie un FactorBean complete avec les informations sur l'objectif,
     * la note et la labellisation, si elle existe
     */
    public FactorBean retrieveFactorAndJustByIdElementBaseline(
            String idBline, String idPro, String idElt, String idFac) {
        return dao.retrieveFactorAndJustByIdElementBaseline(idBline, idPro,
                idElt, idFac);
    }

    /**
     * Retrieve goals by id
     * @param id the pattern to search for the id
     * @param lib the pattern to search for the lib
     * @param idLoc le language id
     * @return the retrieved goals, if any, null otherwise
     */
    public List<FactorBean> retrieveFactorsByIdAndLib(String id, String lib, Locale loc) {
        return dao.retrieveFactorsByIdAndLib(id, lib, loc.getLanguage());
    }

    /**
     * deletes a factor
     * @param id the id of the factor to delete
     * @throws com.compuware.caqs.exception.DataAccessException
     */
    public MessagesCodes deleteFactorBean(String id) {
        MessagesCodes retour = MessagesCodes.NO_ERROR;
        try {
            dao.deleteFactorBean(id);
        } catch (DataAccessException exc) {
            logger.error(exc);
            retour = MessagesCodes.DATABASE_ERROR;
        }
        return retour;
    }

    /**
     * saves a factor, updating it if it already exists, creating it otherwise
     * @param factor the factor to save
     * @throws com.compuware.caqs.exception.DataAccessException
     */
    public MessagesCodes saveFactorBean(FactorBean factor) {
        MessagesCodes retour = MessagesCodes.NO_ERROR;
        try {
            dao.saveFactorBean(factor);
        } catch (DataAccessException dae) {
            logger.error(dae);
            retour = MessagesCodes.DATABASE_ERROR;
        }
        return retour;
    }

    /**
     * retrieves a criterion with the number of models using it
     * @param id the criterion id
     * @return the criterion
     */
    public ModelEditorFactorBean retrieveFactorByIdWithAssociatedModelsCount(String id) {
        return dao.retrieveFactorByIdWithAssociatedModelsCount(id);
    }

    /**
     * retrieves a list of factors by theirs model's id
     * @param idUsage model's id
     * @return list of factors
     */
    public List<FactorBean> retrieveGoalsListByModel(String idModel) {
        return dao.retrieveGoalsListByModel(idModel);
    }

    /**
     * retrieves a list of factors not associated to a model
     * @param idUsage model's id
     * @param filterId filter on goal's id
     * @param filterLib filter on goal's lib
     * @param idLoc locale id used for lib filter
     * @return list of factors
     */
    public List<FactorBean> retrieveGoalsNotAssociatedToModel(String idModel, String filterId, String filterLib, String idLoc) {
        return dao.retrieveGoalsNotAssociatedToModel(idModel, filterId, filterLib, idLoc);
    }

    /**
     * retrieves all models using goal
     * @param idGoal goal id
     * @return model's list
     */
    public List<UsageBean> retrieveAllModelsAssociatedToGoal(String idGoal) {
        return dao.retrieveAllModelsAssociatedToGoal(idGoal);
    }

    /**
     * retrieves goals' evolutions between two baselines
     * @param idEa ea
     * @param idBline first bline
     * @param idPrevBline previous bline
     * @return evolutions
     */
    public List<FactorBean> retrieveGoalsEvolutions(String idEa, String idBline, String idPrevBline) {
        return dao.retrieveGoalsEvolutions(idEa, idBline, idPrevBline);
    }
}
