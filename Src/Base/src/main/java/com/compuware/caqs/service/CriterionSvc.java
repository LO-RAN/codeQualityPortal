package com.compuware.caqs.service;

import com.compuware.caqs.constants.Constants;
import com.compuware.caqs.constants.MessagesCodes;
import java.util.List;

import com.compuware.caqs.dao.factory.DaoFactory;
import com.compuware.caqs.dao.interfaces.CriterionDao;
import com.compuware.caqs.domain.dataschemas.BottomUpDetailBean;
import com.compuware.caqs.domain.dataschemas.CriterionDefinition;
import com.compuware.caqs.domain.dataschemas.FactorDefinitionBean;
import com.compuware.caqs.domain.dataschemas.modeleditor.ModelEditorCriterionBean;
import com.compuware.caqs.exception.DataAccessException;
import com.compuware.caqs.service.modelmanager.CaqsQualimetricModelManager;
import com.compuware.toolbox.util.logging.LoggerManager;
import java.util.Locale;
import java.util.Map;

public class CriterionSvc {

    protected static org.apache.log4j.Logger logger = LoggerManager.getLogger(Constants.LOG4J_IHM_LOGGER_KEY);
    private static final CriterionSvc instance = new CriterionSvc();

    private CriterionSvc() {
    }

    public static CriterionSvc getInstance() {
        return instance;
    }

    public List<BottomUpDetailBean> retrieveCriterionBottomUpDetail(
            String idBline, String idElt, String idPro) {
        CriterionDao dao = DaoFactory.getInstance().getCriterionDao();
        List<BottomUpDetailBean> liste = dao.retrieveCriterionBottomUpDetail(idBline, idElt, idPro);
        return liste;
    }

    public double getMarkForElementToCriterion(String idElt, String idBline, String idCrit) {
        CriterionDao dao = DaoFactory.getInstance().getCriterionDao();
        return dao.getMarkForEAToCriterion(idElt, idCrit, idBline);
    }

    public double getAverageMetricValueForEA(String idEa, String idBline, String idMet) {
        CriterionDao dao = DaoFactory.getInstance().getCriterionDao();
        return dao.getAverageMetricValueForEA(idEa, idBline, idMet);
    }

    /**
     * retrieves criterions associated to a goal and a model. retrieves criterions
     * definition, weight, element type but not metrics.
     * @param idFact goal id
     * @param idUsa model id
     * @return criterions
     */
    public List<CriterionDefinition> retrieveCriterionDefinitionByGoalAndModel(String idFact, String idUsa) {
        CriterionDao dao = DaoFactory.getInstance().getCriterionDao();
        return dao.retrieveCriterionDefinitionByGoalAndModel(idFact, idUsa);
    }

    /**
     * Retrieve models by id
     * @param id the pattern to search for the id
     * @param lib the pattern to search for the lib
     * @param idLoc le language id
     * @return the retrieved models, if any, null otherwise
     */
    public List<CriterionDefinition> retrieveCriterionsByIdLibGoalModel(String id, String lib, String goal, String model, Locale loc) {
        CriterionDao dao = DaoFactory.getInstance().getCriterionDao();
        return dao.retrieveCriterionsByIdLibGoalModel(id, lib, goal, model, loc.getLanguage());
    }

    /**
     * retrieves all criterions not associated to a specific goal and a specific model
     * @param idUsa model's id
     * @param idFact goal's id
     * @param filterId filter on goal's id
     * @param filterLib filter on goal's lib
     * @param idLoc locale id used for lib filter
     * @return criterions
     */
    public List<CriterionDefinition> retrieveCriterionsNotAssociatedToGoalAndModel(String idUsa, String idFact, String filterId, String filterLib, String idLoc) {
        CriterionDao dao = DaoFactory.getInstance().getCriterionDao();
        return dao.retrieveCriterionsNotAssociatedToGoalAndModel(idUsa, idFact, filterId, filterLib, idLoc);
    }

    /**
     * deletes a criterion
     * @param id the id of the metric to delete
     * @throws com.compuware.caqs.exception.DataAccessException
     */
    public MessagesCodes deleteCriterionBean(String id) {
        MessagesCodes retour = MessagesCodes.NO_ERROR;
        try {
            DaoFactory.getInstance().getCriterionDao().deleteCriterionBean(id);
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
    public MessagesCodes saveCriterionBean(CriterionDefinition criterion) {
        MessagesCodes retour = MessagesCodes.NO_ERROR;
        try {
            DaoFactory.getInstance().getCriterionDao().saveCriterionBean(criterion);
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
    public ModelEditorCriterionBean retrieveCriterionByIdWithAssociatedModelsCount(String id) {
        CriterionDao dao = DaoFactory.getInstance().getCriterionDao();
        return dao.retrieveCriterionByIdWithAssociatedModelsCount(id);
    }

    /**
     * updates criterion's weight in a goal computation for a quality model
     * @param idCrit criterion's id
     * @param idFact goal's id
     * @param idUsa model's id
     * @param weight new weight to apply
     * @throws DataAccessException
     */
    public MessagesCodes updateCriterionWeightForGoal(String idCrit, String idFact,
            String idUsa, int weight) {
        MessagesCodes retour = MessagesCodes.NO_ERROR;
        try {
            DaoFactory.getInstance().getCriterionDao().updateCriterionWeightForGoal(idCrit,
                    idFact, idUsa, weight);
        } catch (DataAccessException dae) {
            logger.error(dae);
            retour = MessagesCodes.DATABASE_ERROR;
        }
        return retour;
    }

    /**
     *  updates criterion's element type for a quality model
     * @param idCrit criterion's id
     * @param idUsa model's id
     * @param idTelt new element type's id
     * @throws DataAccessException
     */
    public MessagesCodes updateCriterionTEltForModel(String idCrit,
            String idUsa, String idTelt) {
        MessagesCodes retour = MessagesCodes.NO_ERROR;
        try {
            DaoFactory.getInstance().getCriterionDao().updateCriterionTEltForModel(idCrit,
                    idUsa, idTelt);
        } catch (DataAccessException dae) {
            logger.error(dae);
            retour = MessagesCodes.DATABASE_ERROR;
        }
        return retour;
    }

    /**
     * associate a criterion to a goal and a model
     * @param idCrit criterion's id
     * @param idFact goal's id
     * @param idUsa model's id
     * @return code
     */
    public MessagesCodes associateCriterionToGoalAndModel(String idCrit, String idFact, String idUsa) {
        MessagesCodes retour = MessagesCodes.NO_ERROR;
        try {
            DaoFactory.getInstance().getCriterionDao().associateCriterionToGoalAndModel(idCrit, idFact, idUsa);
        } catch (DataAccessException dae) {
            logger.error(dae);
            retour = MessagesCodes.DATABASE_ERROR;
        }
        return retour;
    }

    /**
     * deletes a criterion to a goal and a model
     * @param idCrit criterion's id
     * @param idFact goal's id
     * @param idUsa model's id
     * @return code
     */
    public MessagesCodes deleteCriterionFromGoalAndModel(String idCrit, String idFact, String idUsa) {
        MessagesCodes retour = MessagesCodes.NO_ERROR;
        try {
            if(DaoFactory.getInstance().getCriterionDao().deleteCriterionFromGoalAndModel(idCrit, idFact, idUsa)) {
                CaqsQualimetricModelManager man = CaqsQualimetricModelManager.getQualimetricModelManager(idUsa);
                if(man!=null) {
                    man.removeCritere(idCrit);
                    man.saveToDisk();
                }
            }
        } catch (DataAccessException dae) {
            logger.error(dae);
            retour = MessagesCodes.DATABASE_ERROR;
        }
        return retour;
    }

    /**
     * retrieves all associated models/goals for a criterion
     * @param idCrit criterion id
     * @return all associated models/goals
     */
    public Map<String, List<FactorDefinitionBean>> retrieveAssociatedModelsAndGoalsForCriterion(String idCrit) {
        return DaoFactory.getInstance().getCriterionDao().retrieveAssociatedModelsAndGoalsForCriterion(idCrit);
    }
}
