package com.compuware.caqs.service;

import java.util.Collections;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import com.compuware.caqs.constants.Constants;
import com.compuware.caqs.constants.MessagesCodes;
import com.compuware.caqs.dao.factory.DaoFactory;
import com.compuware.caqs.dao.interfaces.ActionPlanDao;
import com.compuware.caqs.dao.interfaces.ActionPlanUnitDao;
import com.compuware.caqs.dao.interfaces.FactorDao;
import com.compuware.caqs.domain.dataschemas.BaselineBean;
import com.compuware.caqs.domain.dataschemas.ElementBean;
import com.compuware.caqs.domain.dataschemas.ElementLinked;
import com.compuware.caqs.domain.dataschemas.ElementType;
import com.compuware.caqs.domain.dataschemas.UsageBean;
import com.compuware.caqs.domain.dataschemas.actionplan.ActionPlanBean;
import com.compuware.caqs.domain.dataschemas.actionplan.ActionPlanCriterionBean;
import com.compuware.caqs.domain.dataschemas.actionplan.ActionPlanElementBean;
import com.compuware.caqs.domain.dataschemas.actionplan.ActionPlanFactorBean;
import com.compuware.caqs.domain.dataschemas.actionplan.ActionPlanPriority;
import com.compuware.caqs.domain.dataschemas.actionplan.ActionPlanUnit;
import com.compuware.caqs.domain.dataschemas.actionplan.ApplicationEntityActionPlanBean;
import com.compuware.caqs.domain.dataschemas.actionplan.ProjectActionPlanBean;
import com.compuware.caqs.domain.dataschemas.actionplan.comparator.ActionPlanElementBeanComparator;
import com.compuware.caqs.domain.dataschemas.actionplan.list.ActionPlanElementBeanCollection;
import com.compuware.caqs.domain.dataschemas.actionplan.list.ActionPlanImpactedElementBeanCollection;
import com.compuware.caqs.domain.dataschemas.actionplan.list.ActionPlanElementBeanMap;
import com.compuware.caqs.domain.dataschemas.list.FactorBeanCollection;
import com.compuware.caqs.exception.DataAccessException;
import com.compuware.caqs.presentation.consult.actions.actionplan.util.ActionPlanSimulateGoalsMark;
import com.compuware.caqs.presentation.util.RequestUtil;
import com.compuware.toolbox.util.logging.LoggerManager;
import java.util.List;
import java.util.Map;

public class ActionPlanSvc {

    private static final String ACTION_PLAN_REAL_KIVIAT_DATAS = "ActionPlanRealKiviatDatas";
    protected static org.apache.log4j.Logger logger = LoggerManager.getLogger(Constants.LOG4J_IHM_LOGGER_KEY);
    private static final ActionPlanSvc instance = new ActionPlanSvc();

    /**
     * private constructor for singleton
     */
    private ActionPlanSvc() {
    }

    /**
     * @return singleton's instance
     */
    public static ActionPlanSvc getInstance() {
        return ActionPlanSvc.instance;
    }

    /**
     * retrieves action plan units by id and/or lib
     * @param id
     * @param lib
     * @param idLang
     * @return list of action plan units
     */
    public List<ActionPlanUnit> retrieveActionPlanUnitByIdLib(String id,
            String lib, Locale loc) {
        ActionPlanUnitDao unitDao = DaoFactory.getInstance().getActionPlanUnitDao();
        return unitDao.retrieveActionPlanUnitByIdLib(id, lib, loc.getLanguage());
    }

    /**
     * retrieves a element type by id
     * @param id element type's id pattern
     * @return a element type or null
     */
    public ActionPlanUnit retrieveActionPlanUnitById(String id) {
        return DaoFactory.getInstance().getActionPlanUnitDao().retrieveActionPlanUnitById(id);
    }

    /**
     * save an action plan unit
     * @param apu the action plan unit to save
     * @return return code
     */
    public MessagesCodes saveActionPlanUnit(ActionPlanUnit apu) {
        MessagesCodes retour = MessagesCodes.NO_ERROR;
        try {
            DaoFactory.getInstance().getActionPlanUnitDao().saveActionPlanUnit(apu);
        } catch (DataAccessException dae) {
            logger.error(dae);
            retour = MessagesCodes.DATABASE_ERROR;
        }
        return retour;
    }

    /**
     * deletes an action plan unit
     * @param id the id of the element type to delete
     * @throws com.compuware.caqs.exception.DataAccessException
     */
    public MessagesCodes deleteActionPlanUnitBean(String id) {
        MessagesCodes retour = MessagesCodes.NO_ERROR;
        try {
            DaoFactory.getInstance().getActionPlanUnitDao().deleteActionPlanUnit(id);
        } catch (DataAccessException exc) {
            logger.error(exc);
            retour = MessagesCodes.DATABASE_ERROR;
        }
        return retour;
    }

    public FactorBeanCollection getRealKiviatDatas(ElementBean eltBean, String idBline,
            HttpServletRequest request) {
        FactorBeanCollection retour = null;
        if (request != null) {
            retour = (FactorBeanCollection) request.getSession().getAttribute(ACTION_PLAN_REAL_KIVIAT_DATAS);
            if (retour != null) {
                //ainsi, nous sommes surs que nous recuperons les donnees pour les memes baseline/ea
                if (!retour.getIdBline().equals(idBline)
                        || !retour.getIdElt().equals(eltBean.getId())) {
                    retour = null;
                }
            }
        }
        if (retour == null) {
            retour = this.getRealKiviatDatas(eltBean, idBline);
            if (request != null) {
                request.getSession().setAttribute(ACTION_PLAN_REAL_KIVIAT_DATAS, retour);
            }
        }
        return retour;
    }

    private FactorBeanCollection getRealKiviatDatas(ElementBean eltBean, String idBline) {
        FactorDao factorFacade = DaoFactory.getInstance().getFactorDao();
        return factorFacade.retrieveFactorsByElementBaseline(idBline, eltBean.getProject().getId(), eltBean.getId());
    }

    /**
     * @param ea L'element
     * @param idBline L'identifiant de la baseline
     * @param sort tri les criteres ou non par gravite
     * @param request la requete
     * @return le plan d'action
     */
    public ActionPlanBean getCompleteActionPlan(
            ElementBean ea,
            String idBline,
            boolean sort,
            HttpServletRequest request) {
        ActionPlanDao actionPlanDao = DaoFactory.getInstance().getActionPlanDao();
        ActionPlanBean retour = null;
        if (ElementType.EA.equals(ea.getTypeElt())) {
            retour = actionPlanDao.getCompleteApplicationEntityActionPlan(ea, idBline, ea.getProject().getId());
        } else {
            retour = actionPlanDao.getCompleteProjectActionPlan(ea, idBline, ea.getProject().getId());
        }
        if (sort) {
            this.sortElementListBySeverity(retour, RequestUtil.getLocale(request));
        }
        return retour;
    }

    /**
     * sort elements by severity
     * @param ap action plan for which elements have to be sorted
     * @param loc locale
     */
    private void sortElementListBySeverity(
            ActionPlanBean ap,
            Locale loc) {
        Collections.sort(ap.getElements(), new ActionPlanElementBeanComparator(loc, "severitySort", true));
        int taille = ap.getElements().size();
        for (int i = 0; i < taille; i++) {
            ActionPlanElementBean node = (ActionPlanElementBean) ap.getElements().get(i);
            node.setIndiceGravite(i + 1);
        }
    }

    /**
     * renvoie le plan d'action sauvegarde, pris du cache
     * @param idEa l'identifiant de l'ea
     * @param idBline l'identifiant de la baseline
     * @param elementType element's type
     * @param useCache true pour recuperer dans le cache (si disponible) le plan d'action sauvegarde, false sinon
     * @return	Retourne le plan d'action (la liste des criteres et leur cout associe) associe
     * a l'entite applicative et a la baseline envoyees en parametres.
     */
    public ActionPlanBean getSavedActionPlan(ElementBean ea, String idBline) {
        ActionPlanBean retour = null;
        ActionPlanDao actionPlanDao = DaoFactory.getInstance().getActionPlanDao();
        retour = actionPlanDao.getSavedActionPlanForElement(ea, idBline, ea.getTypeElt(), true, true);
        return retour;
    }

    /**
     * renvoie le plan d'action sauvegarde, pris du cache
     * @param idEa l'identifiant de l'ea
     * @param idBline l'identifiant de la baseline
     * @param elementType element's type
     * @param fromCache true pour recuperer dans le cache (si disponible) le plan d'action sauvegarde, false sinon
     * @param create creer un nouveau plan d'actions en bdd s'il n'existe pas
     * @return	Retourne le plan d'action (la liste des criteres et leur cout associe) associe
     * a l'entite applicative et a la baseline envoyees en parametres.
     */
    public ActionPlanBean getSavedActionPlan(ElementBean ea, String idBline, boolean fromCache, boolean create) {
        ActionPlanBean retour = null;
        ActionPlanDao actionPlanDao = DaoFactory.getInstance().getActionPlanDao();
        retour = actionPlanDao.getSavedActionPlanForElement(ea, idBline, ea.getTypeElt(), fromCache, create);
        return retour;
    }

    /**
     * renvoie le plan d'action sauvegarde, directement de la bdd
     * @param idEa l'identifiant de l'ea
     * @param idBline l'identifiant de la baseline
     * @param elementType element's type
     * @param useCache true pour recuperer dans le cache (si disponible) le plan d'action sauvegarde, false sinon
     * @param create creer un nouveau plan d'actions en bdd s'il n'existe pas
     * @return	Retourne le plan d'action (la liste des criteres et leur cout associe) associe
     * a l'entite applicative et a la baseline envoyees en parametres.
     */
    public ActionPlanBean getSavedActionPlanFromDB(ElementBean ea, String idBline, boolean create) {
        ActionPlanBean retour = null;
        ActionPlanDao actionPlanDao = DaoFactory.getInstance().getActionPlanDao();
        retour = actionPlanDao.getSavedActionPlanForElement(ea, idBline, ea.getTypeElt(), false, create);
        return retour;
    }

    public void updateElementForActionPlan(ActionPlanBean actionPlan, ActionPlanElementBean criterion) {
        ActionPlanDao actionPlanDao = DaoFactory.getInstance().getActionPlanDao();
        try {
            actionPlanDao.updateElementForActionPlan(actionPlan, criterion);
        } catch (DataAccessException e) {
            logger.error("Error saving inside action plan for ea:"
                    + actionPlan.getIdElt() + ", bline:"
                    + actionPlan.getIdBline() + " criterion "
                    + criterion.getId());
            logger.error("Exception : " + e.getMessage());
        }
    }

    /**
     * @param ap le plan d'action pour lequel sauvegarder les informations
     * Sauvegarde les informations sur le plan d'action (commentaires).
     */
    public void saveActionPlanInfos(ActionPlanBean ap) {
        ActionPlanDao actionPlanDao = DaoFactory.getInstance().getActionPlanDao();
        try {
            actionPlanDao.saveActionPlanInfos(ap);
        } catch (DataAccessException e) {
            logger.error("Error saving action plan for ea:" + ap.getIdElt()
                    + ", bline:" + ap.getIdBline());
            logger.error("Exception : " + e.getMessage());
        }
    }

    /**
     * @param actionPlan le plan d'action
     * @param idUsa l'identifiant du modele qualimetrique
     * @return	les elements impactes par ce plan d'action
     */
    public ActionPlanElementBeanMap getElementsImpactedByActionPlan(ActionPlanElementBeanCollection<ActionPlanCriterionBean> criterions, String idUsa) {
        ActionPlanElementBeanMap retour = null;
        ActionPlanDao actionPlanDao = DaoFactory.getInstance().getActionPlanDao();
        retour = actionPlanDao.getElementsImpactedByActionPlan(criterions, idUsa);
        return retour;
    }

    /**
     * Invalidate the cache used to retrieve search results
     * @param idEa Applicative entity id
     * @param idBline Baseline id
     */
    public void invalidateSearchResults(String idBline) {
        ActionPlanDao actionPlanDao = DaoFactory.getInstance().getActionPlanDao();
        actionPlanDao.invalidateSearchResults(idBline);
    }

    public ActionPlanImpactedElementBeanCollection getDeterioratedElementsForCriterion(
            String idEa,
            String idCrit,
            String idBline,
            String idPrevBline,
            String idUsa) {
        ActionPlanDao actionPlanDao = DaoFactory.getInstance().getActionPlanDao();
        return actionPlanDao.getDeterioratedElementsForCriterion(idEa, idCrit, idBline, idPrevBline, idUsa);
    }

    public ActionPlanImpactedElementBeanCollection getStablesElementsForCriterion(
            String idEa,
            String idCrit,
            String idBline,
            String idPrevBline,
            String idUsa) {
        ActionPlanDao actionPlanDao = DaoFactory.getInstance().getActionPlanDao();
        return actionPlanDao.getStablesElementsForCriterion(idEa, idCrit, idBline, idPrevBline, idUsa);
    }

    public ActionPlanImpactedElementBeanCollection getCorrectedElementsForCriterion(
            String idEa,
            String idCrit,
            String idBline,
            String idPrevBline,
            String idUsa) {
        ActionPlanDao actionPlanDao = DaoFactory.getInstance().getActionPlanDao();
        return actionPlanDao.getCorrectedElementsForCriterion(idEa, idCrit, idBline, idPrevBline, idUsa);
    }

    public ActionPlanImpactedElementBeanCollection getPartiallyCorrectedElementsForCriterion(
            String idEa,
            String idCrit,
            String idBline,
            String idPrevBline,
            String idUsa) {
        ActionPlanDao actionPlanDao = DaoFactory.getInstance().getActionPlanDao();
        return actionPlanDao.getPartiallyCorrectedElementsForCriterion(idEa, idCrit, idBline, idPrevBline, idUsa);
    }

    /**
     * autofills the action plan selecting the criterions which permits to have all marks above 3, selecting the less criterions necessary.
     * @param ea applicative entity
     * @param idBline baseline id
     * @param request request
     */
    public void autoFillApplicationEntityActionPlan(ElementBean ea, String idBline, HttpServletRequest request) {
        if (ElementType.EA.equals(ea.getTypeElt())) {
            //nous recuperons le plan d'actions avec tri par severite effectue
            ApplicationEntityActionPlanBean ap = (ApplicationEntityActionPlanBean) this.getCompleteActionPlan(ea, idBline, true, request);
            if (ap != null) {
                //recuperons tous les criteres
                ActionPlanElementBeanCollection<ActionPlanCriterionBean> criterions = ap.getElements();
                if (criterions != null) {
                    //on calcule le kiviat sur le long terme afin de savoir quels objectifs ne sont pas
                    //corriges en simulation avec les criteres deja selectionnes
                    ActionPlanSimulateGoalsMark.getInstance().computeKiviatScores(ea, idBline, ActionPlanPriority.LONG_TERM, true, request);
                    int criterionIndex = 0;
                    int nbGoalsToCorrect = this.hasGoalsToCorrect(ap.getFactors());
                    boolean actionPlanDirty = false;
                    UsageBean modele = ea.getUsage();
                    //tant qu'il reste des criteres correctibles et des objectifs a corriger
                    while (nbGoalsToCorrect > 0 && criterionIndex
                            < criterions.size()) {
                        ActionPlanCriterionBean criterion = criterions.get(criterionIndex);
                        //si le critere n'est pas corrige
                        if (!criterion.isCorrected()) {
                            //on recupere la somme des notes des objectifs
                            double marksSum = this.getSumOfKiviatGoalsToCorrectMark(ap.getFactors());
                            boolean save = true;
                            //on place le critere comme corrige
                            ap.setElementCorrected(true, criterion);
                            //on calcule les notes sur le long terme
                            ActionPlanSimulateGoalsMark.getInstance().computeKiviatScores(ea, idBline, ActionPlanPriority.LONG_TERM, true, request);
                            //le nombre d'objectifs a corriger restant apres nouvelle simulation
                            int newNbGoalsToCorrect = this.hasGoalsToCorrect(ap.getFactors());
                            //s'il y a moins ou le meme nombre d'objectifs a corriger apres inclusion du critere
                            if (nbGoalsToCorrect <= newNbGoalsToCorrect) {
                                double newMarksSum = this.getSumOfKiviatGoalsToCorrectMark(ap.getFactors());
                                //si la nouvelle somme des notes des objectifs a corriger est inférieure (le critere n'a pas corrige un peu un objectif)
                                if (newMarksSum < marksSum) {
                                    ap.setElementCorrected(false, criterion);
                                    save = false;
                                } else if (newMarksSum == marksSum) {
                                    //si les notes des objectifs n'ont pas bouge
                                    boolean isBlocked = false;
                                    for (ActionPlanFactorBean factor : ap.getFactors()) {
                                        if (factor.getCorrectedScore() == 2.99
                                                && factor.getAssociatedCriterions().contains(criterion)) {
                                            //si un des criteres est a 2.99, alors il est bloque par un autre critere moins severe mais de note
                                            //inferieure a 2
                                            isBlocked = true;
                                            break;
                                        }
                                    }
                                    if (!isBlocked) {
                                        //si les notes ne bougent pas et qu'aucun objectif n'est a 2.99
                                        //le critere ne sert a rien
                                        ap.setElementCorrected(false, criterion);
                                        save = false;
                                    }
                                }
                            }
                            if (save) {
                                actionPlanDirty = true;
                                criterion.setElementMaster(ea.getId());
                                //on place sur le court (defaut), moyen ou long terme selon le modele.
                                if (modele.getLowerLimitLongRun()
                                        <= criterion.getCost()) {
                                    criterion.setPriority(ActionPlanPriority.LONG_TERM);
                                } else if (modele.getLowerLimitMediumRun()
                                        <= criterion.getCost()) {
                                    criterion.setPriority(ActionPlanPriority.MEDIUM_TERM);
                                }
                                this.updateElementForActionPlan(ap, criterion);
                                nbGoalsToCorrect = newNbGoalsToCorrect;
                            }
                        }
                        criterionIndex++;
                    }
                    ap.setNeedsImpactedElementsRecompute(actionPlanDirty);
                    ap.setSimulatedKiviatRecomputedPriority(ActionPlanPriority.LONG_TERM);
                }
            }
        }
    }

    /**
     *
     * @param kiviat a kiviat
     * @return the sum of the goal's marks which needs to be corrected
     */
    private double getSumOfKiviatGoalsToCorrectMark(ActionPlanElementBeanCollection<ActionPlanFactorBean> kiviat) {
        double retour = 0.0;
        for (ActionPlanFactorBean factor : kiviat) {
            if (factor.getCorrectedScore() < 3.0) {
                retour += factor.getCorrectedScore();
            }
        }
        return retour;
    }

    /**
     * @param kiviat le kiviat
     * @return true if there is at least one goal which needs to be corrected
     */
    private int hasGoalsToCorrect(ActionPlanElementBeanCollection<ActionPlanFactorBean> kiviat) {
        int retour = 0;
        for (ActionPlanFactorBean factor : kiviat) {
            if (factor.getCorrectedScore() < 3.0) {
                retour++;
            }
        }
        return retour;
    }

    public List<ActionPlanUnit> retrieveAllActionPlanUnits() {
        return DaoFactory.getInstance().getActionPlanUnitDao().retrieveAllActionPlanUnits();
    }

    /**
     * returns true if elementId is in the supertree of leafId, otherwise false
     * @param leafId
     * @param elementId
     * @return true if elementId is in the supertree of leafId, otherwise false
     */
    private boolean isElementInSuperTree(String leafId, String elementId) {
        boolean retour = false;
        String parentPath = ElementSvc.getInstance().retrieveParentPathById(leafId);
        if (!ElementType.ENTRYPOINT.equals(parentPath)) {
            String[] ids = parentPath.split("/");
            if (ids != null && ids.length > 0) {
                for (int i = 0; i < ids.length && !retour; i++) {
                    if (ids[i] != null && ids[i].equals(elementId)) {
                        retour = true;
                    }
                }
            }
        }
        return retour;
    }

    /**
     * autofill all sub action plans to correct the goal given as parameters. the root element is
     * the one given as parameter
     * @param goalId goal's id
     * @param root root element
     * @param idUser the user's id, for rights managment
     * @param parentAP parent's action plan
     * @param request request
     */
    public void autoFillSubActionPlansToCorrectGoal(ActionPlanElementBean goal, ElementBean root, String idUser, ProjectActionPlanBean parentAP, HttpServletRequest request) {
        //we start by taking all sub elements
        List<ElementLinked> children = ElementSvc.getInstance().retrieveAllChildrenElements(root.getId(), idUser);
        if (children != null) {
            for (ElementLinked child : children) {
                //on recupere sa derniere baseline
                BaselineBean bb = BaselineSvc.getInstance().getLastBaseline(child);
                if (bb != null) {
                    child.setBaseline(bb);
                    if (ElementType.EA.equals(child.getTypeElt())) {
                        //il n'y a pas de niveau inférieur
                        //on sélectionne tous les critères nécessaires à la correction de l'objectif
                        this.autoFillApplicationEntityActionPlanForGoal(child, bb.getId(), goal, parentAP, request);
                    } else {
                        //c'est encore un niveau au dessus des eas
                        ProjectActionPlanBean ap = (ProjectActionPlanBean) this.getCompleteActionPlan(child, bb.getId(), false, request);
                        ActionPlanElementBeanCollection<ActionPlanFactorBean> factors = ap.getCorrectedElements();
                        ActionPlanFactorBean factor = factors.get(goal.getId());
                        if ((!goal.isCorrected() && (factor != null
                                && factor.isCorrected())) || (goal.isCorrected() && (factor
                                == null || !factor.isCorrected()))) {
                            //si l'on a deselectionne l'objectif au niveau principal et qu'il etait selectionne a ce niveau
                            //ou que (l'objectif a ete selectionne et il n'etait pas selectionne a ce niveau) :
                            //la priorite est-elle moindre ?
                            boolean lessPriority = true;
                            if (factor != null && factor.getElementMaster()
                                    != null) {
                                //l'objectif a deja ete selectionne et il y a un element maitre.
                                //1. cas le plus simple, l'element maitre est le fils en cours d'inspection.
                                String elementMaster = factor.getElementMaster();
                                if (elementMaster.equals(child.getId())) {
                                    //oui : priorite plus forte
                                    lessPriority = false;
                                } else {
                                    //non : 2. l'element maitre est un element parent mais est-il plus prioritaire que
                                    //le nouvel element maitre ?
                                    if (this.isElementInSuperTree(elementMaster, child.getId())) {
                                        //elementMaster est dans la sous arborescence de child.
                                        //il est plus prioritaire
                                        lessPriority = false;
                                    }
                                }
                            }
                            if (lessPriority) {
                                //la priorite est plus faible, on peut mettre a jour
                                //on commence par s'occuper des niveaux inférieurs
                                this.autoFillSubActionPlansToCorrectGoal(goal, child, idUser, ap, request);
                                //puis on place met à jour l'objectif
                                if (factor == null) {
                                    factor = (ActionPlanFactorBean) ap.getElements().get(goal.getId());
                                }
                                if (factor != null) {
                                    factor.setElementMaster(goal.getElementMaster());
                                    ap.setElementCorrected(goal.isCorrected(), factor);
                                    this.updateElementForActionPlan(ap, factor);
                                    ap.setSimulatedKiviatRecomputedPriority(ActionPlanPriority.UNDEFINED);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private void addMandatoryCriterionsToCorrectGoal(ApplicationEntityActionPlanBean ap, ActionPlanElementBean goal, ElementBean ea, Locale loc) {
        if (ap != null) {
            ActionPlanElementBeanCollection<ActionPlanCriterionBean> criterions = ap.getElements();
            //on trie par severite
            ActionPlanElementBeanCollection<ActionPlanCriterionBean> duplicatedCriterions = new ActionPlanElementBeanCollection<ActionPlanCriterionBean>(criterions);
            Collections.sort(duplicatedCriterions, new ActionPlanElementBeanComparator(loc, "severitySort", true));
            if (duplicatedCriterions != null) {
                double goalScore = ActionPlanSimulateGoalsMark.getInstance().getGoalScoreForActionPlan(ap, goal, ActionPlanPriority.LONG_TERM);
                int criterionIndex = 0;
                while ((goalScore < 3.0) && (criterionIndex
                        < duplicatedCriterions.size())) {
                    ActionPlanElementBean criterion = duplicatedCriterions.get(criterionIndex);
                    if (!criterion.isCorrected()) {
                        boolean save = true;
                        ap.setElementCorrected(true, criterion);
                        double newGoalScore = ActionPlanSimulateGoalsMark.getInstance().getGoalScoreForActionPlan(ap, goal, ActionPlanPriority.LONG_TERM);
                        //si la note de l'objectif n'a pas augmentee
                        if (newGoalScore <= goalScore) {
                            save = false;
                        } else {
                            //si ici, nous pouvons mettre a jour l'element, alors
                            //c'est que, au plus, la priorite est celle de l'ea, sinon, c'est ok
                            if (criterion.getElementMaster() != null
                                    && criterion.getElementMaster().equals(ea.getId())) {
                                //le critere a deja comme elementmaster l'ea, c'est a dire le niveau
                                //le plus prioritaire
                                save = false;
                            }
                        }
                        if (save) {
                            criterion.setElementMaster(goal.getElementMaster());
                            this.updateElementForActionPlan(ap, criterion);
                            ap.setSimulatedKiviatRecomputedPriority(ActionPlanPriority.UNDEFINED);
                            ap.setNeedsImpactedElementsRecompute(true);
                            goalScore = newGoalScore;
                        } else {
                            ap.setElementCorrected(false, criterion);
                        }
                    }
                    criterionIndex++;
                }
            }
        }
    }

    private void removeCriterionsUsedToCorrectGoal(ApplicationEntityActionPlanBean ap, ActionPlanElementBean goal, ElementBean ea, ProjectActionPlanBean parentAP) {
        if (ap != null) {
            //nous devons retirer tous les criteres du plan d'action qui ne corrigent que cet objectif
            ActionPlanElementBeanCollection<ActionPlanFactorBean> factors = ap.getFactors();
            if (factors != null) {
                ActionPlanFactorBean factor = factors.get(goal.getId());
                if (factor != null) {
                    for (ActionPlanCriterionBean criterion : factor.getAssociatedCriterions()) {
                        if (criterion.isCorrected()) {
                            if (criterion.getElementMaster() != null
                                    && criterion.getElementMaster().equals(ea.getId())) {
                                continue;
                            }
                            if (this.isElementInSuperTree(criterion.getElementMaster(), goal.getElementMaster())) {
                                continue;
                            }
                            //le critere est declare comme corrige. doit-on le retirer ?
                            //on le retire si :
                            //- le critere ne corrige que cet objectif
                            //- ou le critere en corrige plusieurs mais n'intervient pas dans la correction des autres objectifs selectionnes
                            //au niveau au dessus
                            if ((criterion.getFactors().size() == 1)
                                    || !this.correctAnotherSelectedGoal(ap, criterion, goal, parentAP)) {
                                ap.setElementCorrected(false, criterion);
                                this.updateElementForActionPlan(ap, criterion);
                                ap.setSimulatedKiviatRecomputedPriority(ActionPlanPriority.UNDEFINED);
                                ap.setNeedsImpactedElementsRecompute(true);
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     *
     * @param parentAP parent action plan
     * @param criterion the criterion to test
     * @param mainGoal the main goal
     * @param parentAP parent's action plan
     * @return true if the criterion corrects another goal, selected in parentAP, thant mainGoal
     */
    private boolean correctAnotherSelectedGoal(ApplicationEntityActionPlanBean ap,
            ActionPlanCriterionBean criterion, ActionPlanElementBean mainGoal, ProjectActionPlanBean parentAP) {
        boolean retour = false;
        ActionPlanElementBeanCollection<ActionPlanFactorBean> correctedGoals = parentAP.getCorrectedElements();
        for (ActionPlanFactorBean correctedGoal : correctedGoals) {
            if (correctedGoal.getId().equals(mainGoal.getId())) {
                continue;
            }
            ActionPlanFactorBean eaGoal = ap.getFactors().get(mainGoal.getId());
            if (eaGoal != null) {
                ActionPlanElementBeanCollection<ActionPlanCriterionBean> associatedCriterions = eaGoal.getAssociatedCriterions();
                if (associatedCriterions != null) {
                    ActionPlanCriterionBean usedCriterion = associatedCriterions.get(criterion.getId());
                    if (usedCriterion != null) {
                        retour = true;
                        break;
                    }
                }
            }
        }
        return retour;
    }

    /**
     * autofills the action plan selecting the criterions which permits to have all marks above 3, selecting the less criterions necessary.
     * @param ea applicative entity
     * @param idBline baseline id
     * @param goal goal
     * @param parentAP parent's action plan
     * @param request request
     */
    private void autoFillApplicationEntityActionPlanForGoal(ElementBean ea, String idBline, ActionPlanElementBean goal, ProjectActionPlanBean parentAP,
            HttpServletRequest request) {
        if (ElementType.EA.equals(ea.getTypeElt())) {
            ApplicationEntityActionPlanBean ap = (ApplicationEntityActionPlanBean) this.getCompleteActionPlan(ea, idBline, true, request);
            if (goal.isCorrected()) {
                //nous devons ajouter des criteres
                this.addMandatoryCriterionsToCorrectGoal(ap, goal, ea, RequestUtil.getLocale(request));
            } else {
                //nous devons en retirer
                this.removeCriterionsUsedToCorrectGoal(ap, goal, ea, parentAP);
            }
        }
    }

    /**
     * retourne le nombre d'elements impactes par critere
     * @param eltBean
     * @param idBline
     * @return
     */
    public Map<String, Integer> getNumberOfElementsInActionsPlanForCriterion(ElementBean eltBean, String idBline) {
        return DaoFactory.getInstance().getActionPlanDao().getNumberOfElementsInActionsPlanForCriterion(eltBean, idBline);
    }

    /**
     *
     * @param idEa
     * @param idBline
     * @param idPrevBline
     * @param idUsa
     * @return
     */
    public Map<String, ActionPlanImpactedElementBeanCollection> getCorrectedElementsByCriterions(
            ElementBean eltBean,
            String idPrevBline) {
        return DaoFactory.getInstance().getActionPlanDao().getCorrectedElementsByCriterions(
                eltBean.getId(), eltBean.getBaseline().getId(),
                idPrevBline, eltBean.getUsage().getId());
    }

    /**
     *
     * @param idEa
     * @param idBline
     * @param idPrevBline
     * @param idUsa
     * @return
     */
    public Map<String, ActionPlanImpactedElementBeanCollection> getPartiallyCorrectedElementsByCriterions(
            ElementBean eltBean,
            String idPrevBline) {
        return DaoFactory.getInstance().getActionPlanDao().getPartiallyCorrectedElementsByCriterions(
                eltBean.getId(), eltBean.getBaseline().getId(),
                idPrevBline, eltBean.getUsage().getId());
    }

    /**
     *
     * @param idEa
     * @param idBline
     * @param idPrevBline
     * @param idUsa
     * @return
     */
    public Map<String, ActionPlanImpactedElementBeanCollection> getStableElementsByCriterions(
            ElementBean eltBean,
            String idPrevBline) {
        return DaoFactory.getInstance().getActionPlanDao().getStableElementsByCriterions(
                eltBean.getId(), eltBean.getBaseline().getId(),
                idPrevBline, eltBean.getUsage().getId());
    }

    /**
     *
     * @param idEa
     * @param idBline
     * @param idPrevBline
     * @param idUsa
     * @return
     */
    public Map<String, ActionPlanImpactedElementBeanCollection> getCorrectedBecauseSuppressedElementsByCriterions(
            ElementBean eltBean,
            String idPrevBline) {
        return DaoFactory.getInstance().getActionPlanDao().getCorrectedBecauseSuppressedElementsByCriterions(
                eltBean.getId(), eltBean.getBaseline().getId(),
                idPrevBline, eltBean.getUsage().getId());
    }

    /**
     *
     * @param idEa
     * @param idBline
     * @param idPrevBline
     * @param idUsa
     * @return
     */
    public Map<String, ActionPlanImpactedElementBeanCollection> getDegradedElementsByCriterions(
            ElementBean eltBean,
            String idPrevBline) {
        return DaoFactory.getInstance().getActionPlanDao().getDegradedElementsByCriterions(
                eltBean.getId(), eltBean.getBaseline().getId(),
                idPrevBline, eltBean.getUsage().getId());
    }
}
