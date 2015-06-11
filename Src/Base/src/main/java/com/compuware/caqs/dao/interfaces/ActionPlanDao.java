package com.compuware.caqs.dao.interfaces;

import com.compuware.caqs.domain.dataschemas.ElementBean;
import com.compuware.caqs.domain.dataschemas.actionplan.ActionPlanBean;
import com.compuware.caqs.domain.dataschemas.actionplan.ActionPlanCriterionBean;
import com.compuware.caqs.domain.dataschemas.actionplan.ActionPlanElementBean;
import com.compuware.caqs.domain.dataschemas.actionplan.ApplicationEntityActionPlanBean;
import com.compuware.caqs.domain.dataschemas.actionplan.ProjectActionPlanBean;
import com.compuware.caqs.domain.dataschemas.actionplan.list.ActionPlanElementBeanCollection;
import com.compuware.caqs.domain.dataschemas.actionplan.list.ActionPlanImpactedElementBeanCollection;
import com.compuware.caqs.domain.dataschemas.actionplan.list.ActionPlanElementBeanMap;
import com.compuware.caqs.exception.DataAccessException;
import java.util.Map;

public interface ActionPlanDao {

    /**
     * @param idEa		identifiant de l'ea
     * @param idBline	identifiant de la baseline
     * @param idPro		identifiant du projet
     * @return			la liste des crit�res ayant une note inf�rieure � 3, justification comprise, pour l'ea, la baseline et le projet donn�s
     */
    //public ActionPlanCriterionBeanMap retrieveCriterionForActionPlan(String idEa, String idBline, String idPro, String idUsa);
    /**
     * @param ap le plan d'action pour lequel sauvegarder les informations
     * @throws DataAccessException
     * Sauvegarde les informations sur le plan d'action (commentaires).
     */
    public void saveActionPlanInfos(ActionPlanBean ap) throws DataAccessException;

    /**
     * @param idEa l'identifiant de l'ea
     * @param idBline l'identifiant de la baseline
     * @param elementType element's type
     * @param useCache true pour recuperer dans le cache (si disponible) le plan d'action sauvegarde, false sinon
     * @param create creer un nouveau plan d'actions en base de donnees s'il n'existe pas
     * @return	Retourne le plan d'action (la liste des criteres et leur cout associe) associe
     * a l'entite applicative et a la baseline envoyees en parametres.
     */
    public ActionPlanBean getSavedActionPlanForElement(ElementBean ea, String idBline, String elementType, boolean useCache, boolean create);

    public ApplicationEntityActionPlanBean getCompleteApplicationEntityActionPlan(ElementBean ea, String idBline, String idPro);

    /**
     * @param criterions les criteres selectionnes du plan d'action
     * @param idUsa l'identifiant du modele qualimetrique
     * @return	les elements impactes par ce plan d'action
     */
    public ActionPlanElementBeanMap getElementsImpactedByActionPlan(ActionPlanElementBeanCollection<ActionPlanCriterionBean> criterions, String idUsa);

    /**
     * Used to invalidate action plan cached search results
     * @param idBline
     */
    public void invalidateSearchResults(String idBline);

    public ActionPlanImpactedElementBeanCollection getDeterioratedElementsForCriterion(
            String idEa,
            String idCrit,
            String idBline,
            String idPrevBline,
            String idUsa);

    public ActionPlanImpactedElementBeanCollection getStablesElementsForCriterion(
            String idEa,
            String idCrit,
            String idBline,
            String idPrevBline,
            String idUsa);

    public ActionPlanImpactedElementBeanCollection getCorrectedElementsForCriterion(
            String idEa,
            String idCrit,
            String idBline,
            String idPrevBline,
            String idUsa);

    public ActionPlanImpactedElementBeanCollection getPartiallyCorrectedElementsForCriterion(
            String idEa,
            String idCrit,
            String idBline,
            String idPrevBline,
            String idUsa);

    /**
     * saves in database the criterion state for the action plan
     * @param actionPlan action plan
     * @param criterion criterion to save
     * @throws DataAccessException
     */
    public void updateElementForActionPlan(ActionPlanBean actionPlan, ActionPlanElementBean criterion) throws DataAccessException;

    /**
     * returns the complete action plan (all possible goals included) for a project or a sub-project
     * @param ea element bean
     * @param idBline baseline
     * @param idPro project id
     * @return action plan
     */
    public ProjectActionPlanBean getCompleteProjectActionPlan(ElementBean ea, String idBline, String idPro);

    /**
     * retourne le nombre d'elements impactes par critere
     * @param eltBean
     * @param idBline
     * @return
     */
    public Map<String, Integer> getNumberOfElementsInActionsPlanForCriterion(ElementBean eltBean, String idBline);

    /**
     *
     * @param idEa
     * @param idBline
     * @param idPrevBline
     * @param idUsa
     * @return
     */
    public Map<String, ActionPlanImpactedElementBeanCollection> getCorrectedElementsByCriterions(
            String idEa,
            String idBline,
            String idPrevBline,
            String idUsa);

    /**
     *
     * @param idEa
     * @param idBline
     * @param idPrevBline
     * @param idUsa
     * @return
     */
    public Map<String, ActionPlanImpactedElementBeanCollection> getDegradedElementsByCriterions(
            String idEa,
            String idBline,
            String idPrevBline,
            String idUsa);

    /**
     *
     * @param idEa
     * @param idBline
     * @param idPrevBline
     * @param idUsa
     * @return
     */
    public Map<String, ActionPlanImpactedElementBeanCollection> getPartiallyCorrectedElementsByCriterions(
            String idEa,
            String idBline,
            String idPrevBline,
            String idUsa);

    /**
     *
     * @param idEa
     * @param idBline
     * @param idPrevBline
     * @param idUsa
     * @return
     */
    public Map<String, ActionPlanImpactedElementBeanCollection> getStableElementsByCriterions(
            String idEa,
            String idBline,
            String idPrevBline,
            String idUsa);

    /**
     *
     * @param idEa
     * @param idBline
     * @param idPrevBline
     * @param idUsa
     * @return
     */
    public Map<String, ActionPlanImpactedElementBeanCollection> getCorrectedBecauseSuppressedElementsByCriterions(
            String idEa,
            String idBline,
            String idPrevBline,
            String idUsa);
}
