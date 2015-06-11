package com.compuware.caqs.dao.interfaces;

import java.sql.Connection;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.compuware.caqs.domain.dataschemas.BaselineBean;
import com.compuware.caqs.domain.dataschemas.BottomUpDetailBean;
import com.compuware.caqs.domain.dataschemas.BottomUpSummaryBean;
import com.compuware.caqs.domain.dataschemas.CriterionBean;
import com.compuware.caqs.domain.dataschemas.CriterionDefinition;
import com.compuware.caqs.domain.dataschemas.CriterionRepartitionBean;
import com.compuware.caqs.domain.dataschemas.ElementBean;
import com.compuware.caqs.domain.dataschemas.ElementEvolutionSummaryBean;
import com.compuware.caqs.domain.dataschemas.FactorDefinitionBean;
import com.compuware.caqs.domain.dataschemas.FactorRepartitionBean;
import com.compuware.caqs.domain.dataschemas.FilterBean;
import com.compuware.caqs.domain.dataschemas.ProjectBean;
import com.compuware.caqs.domain.dataschemas.calcul.Critere;
import com.compuware.caqs.domain.dataschemas.modeleditor.ModelEditorCriterionBean;
import com.compuware.caqs.exception.DataAccessException;

public interface CriterionDao {

    public abstract CriterionDefinition retrieveCriterionDefinitionByKey(
            String idCrit);

    public abstract List<CriterionDefinition> retrieveCriterionDefinitionByUsage(String idUsa);

    public abstract CriterionDefinition retrieveCriterionDefinitionByKey(
            String idCrit, String idUsa);

    public abstract Collection retrieveCriterionDefinitionByFactor(
            String idFact, String idUsa);

    public abstract Collection retrieveAllCriterions();

    public abstract List<CriterionBean> retrieveCriterionDetailsForElts(ElementBean eltBean,
            CriterionDefinition criterionDef, double noteSeuil, String filter,
            String typeElt);

    public abstract List<CriterionBean> retrieveCriterionDetailsForSubElts(
            ElementBean eltBean, CriterionDefinition criterionDef,
            double noteSeuil, String filter, String typeElt);

    public abstract List<CriterionBean> retrieveCriterionDetailsNoMetForElts(
            ElementBean eltBean, CriterionDefinition criterionDef,
            double noteSeuil, String filter, String typeElt);

    public abstract List<CriterionBean> retrieveCriterionDetailsNoMetForSubElts(
            ElementBean eltBean, CriterionDefinition criterionDef,
            double noteSeuil, String filter, String typeElt);

    /**
     * retrieves criterions associated to a goal and a model. retrieves criterions
     * definition, weight, element type but not metrics.
     * @param idFact goal id
     * @param idUsa model id
     * @return criterions
     */
    public List<CriterionDefinition> retrieveCriterionDefinitionByGoalAndModel(String idFact, String idUsa);

    /**
     * ********************************************************************************************************************************
     */
    public abstract Collection retrieveCriterionSummary(String idBline,
            String idFact, String idElt);

    public abstract List<BottomUpSummaryBean> retrieveCriterionBottomUpSummary(String idBline,
            String idElt, FilterBean filter);

    public abstract List<BottomUpDetailBean> retrieveCriterionBottomUpDetail(String idBline,
            String idElt, String idPro);

    public abstract int retrieveCriterionTotalCorrections(String idBline,
            String idElt, int baseNote, FilterBean filter);

    public abstract int retrieveCriterionTotalElements(String idBline,
            String idElt, int baseNote, FilterBean filter);

    public abstract Collection<FactorRepartitionBean> retrieveRepartitionByFactor(String idBline,
            String idElt, String idPro, String idUsa, String filter,
            String lastTypeElt);

    public abstract Collection<CriterionRepartitionBean> retrieveRepartitionByCriterion(String idBline,
            String idElt, String idPro, String filter, String lastTypeElt);

    public abstract List<CriterionBean> retrieveFacteurSynthese(String idElt, String idPro,
            String idBline, String idFac);

    /**
     * Renvoie la repartition des notes des criteres pour un objectif
     * @param idElt identifiant de l'entite applicative
     * @param idPro identifiant du projet
     * @param idBline identifiant de la baseline
     * @param idFac identifiant de l'objectif
     * @return la repartition
     */
    public abstract Map retrieveCriterionNoteRepartition(String idElt,
            String idPro, String idBline, String idFac);

    public abstract void calculateCriterionNoteRepartition(String idElt,
            String idPro, String idBline);

    public abstract void calculateCriterionNoteRepartition(String idElt,
            String idPro, String idBline, Connection connection)
            throws DataAccessException;

    public abstract List<ElementEvolutionSummaryBean> retrieveNewAndBadElements(String idElt,
            String idBline, FilterBean filter);

    public abstract List<ElementEvolutionSummaryBean> retrieveOldAndWorstElements(String idElt,
            String idBline, FilterBean filter);

    public abstract List<ElementEvolutionSummaryBean> retrieveOldAndBetterElements(String idElt,
            String idBline, FilterBean filter);

    public abstract List<ElementEvolutionSummaryBean> retrieveOldBetterAndWorstElements(String idElt,
            String idBline, FilterBean filter);

    public abstract List<ElementEvolutionSummaryBean> retrieveStableElements(String idElt,
            String idBline, FilterBean filter);

    public abstract List<ElementEvolutionSummaryBean> retrieveBadAndStableElements(String idElt,
            String idBline, FilterBean filter);

    public abstract Collection retrieveRepartitionNewAndBadElements(
            String idElt, String idBline, FilterBean filter);

    public abstract Collection retrieveRepartitionOldAndWorstElements(
            String idElt, String idBline, FilterBean filter);

    public abstract Collection retrieveRepartitionOldAndBetterElements(
            String idElt, String idBline, FilterBean filter);

    public abstract Collection retrieveRepartitionOldBetterWorstElements(
            String idElt, String idBline, FilterBean filter);

    public abstract Collection retrieveRepartitionBadStableElements(
            String idElt, String idBline, FilterBean filter);

    public abstract Collection retrieveEvolutionRepartitionByCriterion(
            String idElt, String idBline, String query);

    public abstract void updateElementBaselineInformation(String idElt,
            String idBline, String idMainElt, int[] notes, Connection connection);

    public abstract void initElementBaselineInformation(String idElt,
            String idBline, String idMainElt, int startLine)
            throws DataAccessException;

    public abstract void updateElementBaselineInformationEvolution(
            String idElt, String idBline, Connection connection);

    public abstract void updateElementBaselineInformationEvolution(
            String idElt, String idBline, String query, Connection connection);

    public abstract void updateCriterion(Collection criterionColl, BaselineBean baseline, ProjectBean projet, String idElt, Connection connection) throws DataAccessException;

    public abstract void updateWeight(Critere crit, BaselineBean baseline, ProjectBean projet, String idElt, String idFac, double poids, Connection connection) throws DataAccessException;

    public List<BottomUpDetailBean> retrieveNewAndBadElementsWithCriterions(String idEa, String idBline, String idPro);

    public List<BottomUpDetailBean> retrieveOldBadStableElementsWithCriterions(String idEa, String idBline, String idPro);

    public List<BottomUpDetailBean> retrieveOldBetterWorstElementsWithCriterions(String idEa, String idBline, String idPro);

    public List<BottomUpDetailBean> retrieveOldBetterElementsWithCriterions(String idEa, String idBline, String idPro);

    public List<BottomUpDetailBean> retrieveOldWorstElementsWithCriterions(String idEa, String idBline, String idPro);

    public double getMarkForEAToCriterion(String idElt, String idCrit, String idBline);

    /**
     * @param idEa entite applicative concernee
     * @param idBline baseline concernee
     * @param idMet identifiant de metrique pour laquelle la moyenne est demandee
     * @return la note moyenne obtenue par l'ensemble des elements d'une EA pour une baseline
     * et une metrique donnees
     */
    public double getAverageMetricValueForEA(String idEa, String idBline, String idMet);

    /**
     * met a jour le commentaire place sur un critere. l'element envoye en parametre
     * doit avoir une baseline.
     * @param elt l'element pour lequel commenter le critere
     * @param idCrit l'identifiant du critere a commenter
     * @param comment le commentaire
     * @return <code>MessagesCodes.NO_ERROR</code> s'il n'y a pas d'erreur, un
     * message d'erreur sinon.
     */
    public void updateCommentForCriterion(ElementBean elt,
            String idCrit, String comment) throws DataAccessException;

    /**
     * retrieve criterions by id/lib/goal associated and/or model using it
     * @param id
     * @param lib
     * @param goal
     * @param model
     * @param idLang
     * @return
     */
    public List<CriterionDefinition> retrieveCriterionsByIdLibGoalModel(String id, String lib, String goal, String model, String idLang);

    /**
     * saves a criterion, updating it if it already exists, creating it otherwise
     * @param criterion the criterion to save
     * @throws com.compuware.caqs.exception.DataAccessException
     */
    public void saveCriterionBean(CriterionDefinition criterion) throws DataAccessException;

    /**
     * deletes a criterion
     * @param id the id of the criterion to delete
     * @throws com.compuware.caqs.exception.DataAccessException
     */
    public void deleteCriterionBean(String id) throws DataAccessException;

    /**
     * retrieves a criterion with the number of models using it
     * @param id the criterion id
     * @return the criterion
     */
    public ModelEditorCriterionBean retrieveCriterionByIdWithAssociatedModelsCount(String id);

    /**
     * updates criterion's weight in a goal computation for a quality model
     * @param idCrit criterion's id
     * @param idFact goal's id
     * @param idUsa model's id
     * @param weight new weight to apply
     * @throws DataAccessException
     */
    public void updateCriterionWeightForGoal(String idCrit, String idFact,
            String idUsa, int weight) throws DataAccessException;

    /**
     *  updates criterion's element type for a quality model
     * @param idCrit criterion's id
     * @param idUsa model's id
     * @param idTelt new element type's id
     * @throws DataAccessException
     */
    public void updateCriterionTEltForModel(String idCrit,
            String idUsa, String idTelt) throws DataAccessException;

    /**
     * retrieves all criterions not associated to a specific goal and a specific model
     * @param idUsa model's id
     * @param idFact goal's id
     * @param filterId filter on goal's id
     * @param filterLib filter on goal's lib
     * @param idLoc locale id used for lib filter
     * @return criterions
     */
    public List<CriterionDefinition> retrieveCriterionsNotAssociatedToGoalAndModel(String idUsa, String idFact, String filterId, String filterLib, String idLoc);

    /**
     * associate a criterion to a goal and a model
     * @param idCrit criterion's id
     * @param idFact goal's id
     * @param idUsa model's id
     * @throws DataAccessException
     */
    public void associateCriterionToGoalAndModel(String idCrit, String idFact, String idUsa)
        throws DataAccessException;

    /**
     * deletes a criterion from a goal and a model
     * @param idCrit criterion's id
     * @param idFact goal's id
     * @param idUsa model's id
     * @return true if the criterion has been removed from the model, false otherwise
     * @throws DataAccessException
     */
    public boolean deleteCriterionFromGoalAndModel(String idCrit, String idFact, String idUsa)
        throws DataAccessException;

    /**
     * retrieves all associated models/goals for a criterion
     * @param idCrit criterion id
     * @return all associated models/goals
     */
    public Map<String, List<FactorDefinitionBean>> retrieveAssociatedModelsAndGoalsForCriterion(String idCrit);

}
