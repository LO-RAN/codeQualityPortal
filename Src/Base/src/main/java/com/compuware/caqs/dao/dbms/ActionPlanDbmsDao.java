package com.compuware.caqs.dao.dbms;

import com.compuware.caqs.domain.dataschemas.ElementType;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.compuware.caqs.constants.Constants;
import com.compuware.caqs.dao.interfaces.ActionPlanDao;
import com.compuware.caqs.domain.dataschemas.CriterionNoteRepartition;
import com.compuware.caqs.domain.dataschemas.ElementBean;
import com.compuware.caqs.domain.dataschemas.actionplan.ActionPlanBean;
import com.compuware.caqs.domain.dataschemas.actionplan.ActionPlanCriterionBean;
import com.compuware.caqs.domain.dataschemas.actionplan.ActionPlanElementBean;
import com.compuware.caqs.domain.dataschemas.actionplan.ActionPlanImpactedElementBean;
import com.compuware.caqs.domain.dataschemas.actionplan.ActionPlanFactorBean;
import com.compuware.caqs.domain.dataschemas.actionplan.ActionPlanPriority;
import com.compuware.caqs.domain.dataschemas.actionplan.ApplicationEntityActionPlanBean;
import com.compuware.caqs.domain.dataschemas.actionplan.ProjectActionPlanBean;
import com.compuware.caqs.domain.dataschemas.actionplan.list.ActionPlanCriterionBeanMap;
import com.compuware.caqs.domain.dataschemas.actionplan.list.ActionPlanElementBeanCollection;
import com.compuware.caqs.domain.dataschemas.actionplan.list.ActionPlanImpactedElementBeanCollection;
import com.compuware.caqs.domain.dataschemas.actionplan.list.ActionPlanElementBeanMap;
import com.compuware.caqs.exception.DataAccessException;
import com.compuware.caqs.util.IDCreator;
import com.compuware.toolbox.dbms.JdbcDAOUtils;
import com.compuware.toolbox.util.logging.LoggerManager;
import java.util.HashMap;
import java.util.Map;

public class ActionPlanDbmsDao implements ActionPlanDao {

    /**
     * Logger
     */
    private static org.apache.log4j.Logger logger = LoggerManager.getLogger(Constants.LOG4J_DBMS_LOGGER_KEY);
    /**
     * Data cache object
     */
    protected static DataAccessCache dataCache = DataAccessCache.getInstance();
    /**
     * Singleton
     */
    private static ActionPlanDao singleton = new ActionPlanDbmsDao();

    /**
     * @return the instance of the singleton
     */
    public static ActionPlanDao getInstance() {
        return ActionPlanDbmsDao.singleton;
    }

    /**
     * Creates a new instance of Class
     */
    private ActionPlanDbmsDao() {
    }

    private void addCriterionToActionPlan(String idUsa,
            ActionPlanCriterionBeanMap result, boolean hasRepartition, ResultSet rs)
            throws SQLException {
        String lastCritId = "";
        ActionPlanCriterionBean lastCriterion = null;
        while (rs.next()) {
            String actualCritId = rs.getString("idcrit");
            if (!actualCritId.equals(lastCritId)) {
                /* The actual result concern a different criterion than the last one.
                 * Creation of a new storage element associated to the new criterion.
                 * Criterion ID initialization.
                 * Add the new criterion storage element to the hashtable result.
                 */
                lastCriterion = new ActionPlanCriterionBean(actualCritId);
                lastCriterion.setIdUsa(idUsa);
                lastCriterion.setTypeElt(rs.getString("id_telt"));
                double note = rs.getDouble("note_cribl");
                double just = rs.getDouble("just_note_cribl");
                if (just > 0) {
                    lastCriterion.setScore(just);
                } else {
                    lastCriterion.setScore(note);
                }
                lastCriterion.setCost(rs.getDouble("cost"));
                result.put(actualCritId, lastCriterion);
            }
//					Add part and value to the criterion storage element.
            if (lastCriterion != null) {
                lastCriterion.addFactor(rs.getString("id_fact"), rs.getDouble("poids"));
                if (hasRepartition) {
                    lastCriterion.getRepartition().addWithoutSum(rs.getInt("seuil"), rs.getInt("total"));
                } else {
                    //il n'y a pas de repartition. un seul element donc, l'ea, une seule note.
                    double note = lastCriterion.getScore();
                    int seuil = 3;
                    if (note < 4.0) {
                        seuil = 2;
                    }
                    if (note < 3.0) {
                        seuil = 1;
                    }
                    if (note < 2.0) {
                        seuil = 0;
                    }
                    lastCriterion.getRepartition().addWithoutSum(seuil, 1);
                }
            }
//					Update the last criterion ID.
            lastCritId = actualCritId;
        }
    }

    private void retrieveCriterionsForElements(String query, ElementBean eltBean,
            String idBline, boolean hasRepartition, Connection connection,
            ActionPlanCriterionBeanMap result) {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = connection.prepareStatement(query);
            pstmt.setString(1, eltBean.getId());
            pstmt.setString(2, idBline);
            pstmt.setString(3, eltBean.getUsage().getId());
//				Executing the request.
            rs = pstmt.executeQuery();
            this.addCriterionToActionPlan(eltBean.getUsage().getId(), result, hasRepartition, rs);
        } catch (SQLException exc) {
            logger.error("Erreur lors de la recuperation de la repartition des notes par critere pour le plan d'action");
            logger.error("id_bline=" + idBline + ", id_elt=" + eltBean.getId(), exc);
        } finally {
            JdbcDAOUtils.closeResultSet(rs);
            JdbcDAOUtils.closePrepareStatement(pstmt);
        }
    }
    private static final String RETRIEVE_GOALS_FOR_ACTION_PLAN_QUERY =
            "SELECT id_fac, note_facbl FROM facteur_bline WHERE "
            + " id_bline = ? " + " AND id_elt = ? " + " AND id_pro = ? ";

    /**
     * retrieves all goals with scores below 3 for an elementbean and a baseline
     * @param ea element bean
     * @param idBline baseline
     * @param idPro project id
     * @return goals with scores below 3
     */
    private ActionPlanElementBeanCollection<ActionPlanFactorBean> retrieveGoalsForActionPlan(ElementBean ea, String idBline, String idPro) {
        ActionPlanElementBeanCollection<ActionPlanFactorBean> result = (ActionPlanElementBeanCollection<ActionPlanFactorBean>) dataCache.loadFromCache("retrieveGoalsForActionPlan"
                + idBline + ea.getId() + idPro);
        if (result == null) {
            result = new ActionPlanElementBeanCollection<ActionPlanFactorBean>(ea.getId(), idBline);
            Connection connection = JdbcDAOUtils.getConnection(this, Constants.CAQS_DATASOURCE_KEY);
            PreparedStatement stmt = null;
            ResultSet rs = null;
            try {
                stmt = connection.prepareStatement(RETRIEVE_GOALS_FOR_ACTION_PLAN_QUERY);
                stmt.setString(1, idBline);
                stmt.setString(2, ea.getId());
                stmt.setString(3, idPro);
                rs = stmt.executeQuery();
                if (rs != null) {
                    while (rs.next()) {
                        ActionPlanFactorBean fb = new ActionPlanFactorBean(rs.getString("id_fac"));
                        fb.setScore(rs.getDouble("note_facbl"));
                        result.add(fb);
                    }
                }
                dataCache.storeToCache(idBline, "retrieveGoalsForActionPlan"
                        + idBline + ea.getId() + idPro, result);
            } catch (SQLException exc) {
                logger.error("retrieveGoalsForActionPlan : id_bline=" + idBline
                        + ", id_elt=" + ea.getId(), exc);
            } finally {
                JdbcDAOUtils.closeResultSet(rs);
                JdbcDAOUtils.closePrepareStatement(stmt);
                JdbcDAOUtils.closeConnection(connection);
            }
        }
        return result;
    }

    /**
     * @{@inheritDoc }
     */
    public ProjectActionPlanBean getCompleteProjectActionPlan(ElementBean ea, String idBline, String idPro) {
        ProjectActionPlanBean retour = (ProjectActionPlanBean) dataCache.loadFromCache("retrieveProjectActionPlan"
                + idBline + ea.getId());
        if (retour == null || !retour.hasBeenCompleted()) {
            if (retour == null) {
                retour = (ProjectActionPlanBean) this.getSavedActionPlanForElement(ea, idBline, ea.getTypeElt(), true, true);
            }
            ActionPlanElementBeanCollection<ActionPlanFactorBean> allGoalsPossibleForAP = this.retrieveGoalsForActionPlan(ea, idBline, idPro);
            ActionPlanElementBeanCollection<ActionPlanFactorBean> savedColl = retour.getElements();
            //la map contient tous les criteres ainsi : (id_crit, crit)
            for (ActionPlanFactorBean factor : allGoalsPossibleForAP) {
                ActionPlanFactorBean savedGoal = savedColl.get(factor.getId());
                if (savedGoal == null) {
                    savedGoal = factor;
                    retour.addElementToActionPlan(factor);
                } else {
                    savedGoal.setScore(factor.getScore());
                    savedGoal.setCorrectedScore(factor.getScore());
                }
                if (savedGoal.getScore() < 3.0) {
                    retour.getFactorsWithProblematicElements().add(savedGoal);
                }
            }
            retour.setHasBeenCompleted(true);
            dataCache.storeToCache(idBline, "retrieveProjectActionPlan"
                    + idBline + ea.getId(), retour);
        }
        return retour;
    }

    /** {@inheritDoc}
     */
    private ActionPlanCriterionBeanMap retrieveCriterionsForActionPlan(ElementBean ea, String idBline) {
        ActionPlanCriterionBeanMap result = new ActionPlanCriterionBeanMap(ea.getId(), idBline);
        Connection connection = JdbcDAOUtils.getConnection(this, Constants.CAQS_DATASOURCE_KEY);
        String request = "SELECT nr.id_crit as idcrit, nr.seuil, nr.total, cb.note_cribl, cb.just_note_cribl, "
                + "cu.id_telt, fc.id_fact, fc.poids, cb.cost, cb.id_crit as cbidcrit"
                + " FROM CritereNoteRepartition nr, Critere_bline cb, Critere_usage cu, Facteur_critere fc"
                + " WHERE nr.id_elt = ?" + " AND nr.id_bline = ?"
                + " AND cb.id_elt   = nr.id_elt"
                + " AND cb.id_crit  = nr.id_crit"
                + " AND cb.id_bline = nr.id_bline" + " AND cb.note_cribl > 0"
                + " AND cu.id_usa   = ?" + " AND fc.id_usa   = cu.id_usa"
                + " AND fc.id_crit  = cb.id_crit"
                + " AND cu.id_crit  = cb.id_crit"
                + " ORDER BY nr.id_crit, nr.total ASC";
        this.retrieveCriterionsForElements(request, ea, idBline, true, connection, result);
        request = "SELECT cb.note_cribl, cb.just_note_cribl, "
                + "cu.id_telt, fc.id_fact, fc.poids, cb.cost, cb.id_crit as idcrit"
                + " FROM Critere_bline cb, Critere_usage cu, Facteur_critere fc, Element elt"
                + " WHERE elt.id_telt = '" + ElementType.EA + "' "
                + " AND elt.id_elt = ? " + " AND cb.id_elt   = elt.id_elt"
                + " AND cb.id_bline = ?" + " AND cu.id_usa   = ?"
                + " AND cb.note_cribl > 0" + " AND fc.id_usa   = cu.id_usa"
                + " AND fc.id_crit  = cb.id_crit"
                + " AND cu.id_crit  = cb.id_crit"
                + " AND elt.id_telt = cu.id_telt" + " ORDER BY cb.id_crit";
        this.retrieveCriterionsForElements(request, ea, idBline, false, connection, result);
        JdbcDAOUtils.closeConnection(connection);
        return result;
    }

    private void addOrCompleteCriterionsDatas(ApplicationEntityActionPlanBean ap, ActionPlanCriterionBean criterionToComplete, ActionPlanCriterionBean criterion) {
        criterionToComplete.setFactors(criterion.getFactors());
        criterionToComplete.setScore(criterion.getScore());
        criterionToComplete.setTypeElt(criterion.getTypeElt());
        criterionToComplete.setRepartition(criterion.getRepartition());
        int nbElts = criterionToComplete.getRepartition().getValue(0)
                + criterionToComplete.getRepartition().getValue(1);
        //criteres ayant un element minimum avec une note inferieure a 3
        if (nbElts > 0) {
            ap.getElementsWithProblematicElement().add(criterionToComplete);
        }
        criterionToComplete.updateActionPlanAssociatedFactorsList(ap);
    }

    /**
     * @{@inheritDoc }
     */
    public ApplicationEntityActionPlanBean getCompleteApplicationEntityActionPlan(ElementBean ea, String idBline, String idPro) {
        ApplicationEntityActionPlanBean retour = (ApplicationEntityActionPlanBean) dataCache.loadFromCache("retrieveApplicationEntityActionPlan"
                + idBline + ea.getId());
        if (retour == null || !retour.hasBeenCompleted()) {
            if (retour == null) {
                retour = (ApplicationEntityActionPlanBean) this.getSavedActionPlanForElement(ea, idBline, ElementType.EA, true, true);
            }
            //on recupere tous les criteres ayant au moins un element problematique avec toutes les infos
            ActionPlanCriterionBeanMap allCriterionsPossibleForAP = this.retrieveCriterionsForActionPlan(ea, idBline);
            //on recupere tous les criteres inclus dans le plan d'actions
            ActionPlanElementBeanCollection<ActionPlanCriterionBean> savedColl = retour.getElements();
            //la map contient tous les criteres ainsi : (id_crit, crit)
            //pour chaque critere selectionne
            for (ActionPlanCriterionBean criterion : savedColl) {
                //l'avons nous bien avec plus d'infos
                ActionPlanCriterionBean completedCriterion = allCriterionsPossibleForAP.get(criterion.getId());
                if (completedCriterion != null) {
                    //oui, nous copions les infos
                    this.addOrCompleteCriterionsDatas(retour, criterion, completedCriterion);
                    //nous le retirons car il est deja inclus dans le plan d'actions
                    allCriterionsPossibleForAP.remove(criterion.getId());
                }
            }
            //pour chaque critere dont nous avons toutes les infos et non inclus dans le plan d'actions
            for (ActionPlanCriterionBean entry : allCriterionsPossibleForAP.values()) {
                //il va dans les criteres selectionnables du plan d'actions
                retour.addElementToActionPlan(entry);
                int nbElts = entry.getRepartition().getValue(0)
                        + entry.getRepartition().getValue(1);
                //criteres ayant un element minimum avec une note inferieure a 3
                if (nbElts > 0) {
                    retour.getElementsWithProblematicElement().add(entry);
                }
                entry.updateActionPlanAssociatedFactorsList(retour);
            }
            retour.setHasBeenCompleted(true);
            dataCache.storeToCache(idBline, "retrieveApplicationEntityActionPlan"
                    + idBline + ea.getId(), retour);
        }
        return retour;
    }

    /**
     * returns an action plan element depending on the element's type.
     * @param rs the resultset
     * @param elementType the action plan element's type
     * @param elt element bean
     * @return actionplanelementbean
     * @throws SQLException
     */
    private ActionPlanElementBean getElementFromResultSetAndElementType(ResultSet rs, String elementType, ElementBean elt) throws SQLException {
        ActionPlanElementBean retour = null;
        if (rs != null) {
            if (ElementType.EA.equals(elementType)) {
                retour = new ActionPlanCriterionBean(rs.getString("id_crit"));
                ((ActionPlanCriterionBean) retour).setCost(rs.getDouble("cost"));
                if (elt.getUsage() != null) {
                    ((ActionPlanCriterionBean) retour).setIdUsa(elt.getUsage().getId());
                }
            } else {
                retour = new ActionPlanFactorBean(rs.getString("id_crit"));
            }

            if (retour != null) {
                retour.setComment(rs.getString("CRITERION_COMMENT"));
                retour.setPriority(ActionPlanPriority.valueOf(rs.getString("priority")));
                retour.setCommentUser(rs.getString("comment_user"));
                retour.setElementMaster(rs.getString("MASTER_ELEMENT"));
            }
        }
        return retour;
    }

    /**
     * @{@inheritDoc }
     */
    public ActionPlanBean getSavedActionPlanForElement(ElementBean ea, String idBline, String elementType, boolean useCache, boolean create) {
        ActionPlanBean retour = (ActionPlanBean) dataCache.loadFromCache("retrieveActionPlan"
                + idBline + ea.getId());
        if (retour == null || !useCache) {
            Connection connection = JdbcDAOUtils.getConnection(this, Constants.CAQS_DATASOURCE_KEY);
            PreparedStatement pstmt = null;
            ResultSet rs = null;
            try {
                retour = this.getActionPlanBean(ea, idBline, create, connection);
                if (retour != null) {
                    String request = null;
                    if (ElementType.EA.equals(ea.getTypeElt())) {
                        request = "SELECT apc.id_crit, apc.priority, apc.CRITERION_COMMENT, cb.cost, apc.selected, apc.comment_user, apc.MASTER_ELEMENT"
                                + " FROM ACTION_PLAN ap, ACTION_PLAN_CRITERION apc, Critere_bline cb"
                                + " WHERE ap.id_action_plan = ?"
                                + " AND apc.id_action_plan = ap.id_action_plan"
                                + " AND cb.id_crit = apc.id_crit"
                                + " AND cb.id_bline = ap.id_bline"
                                + " AND cb.id_elt = ap.id_elt"
                                + " ORDER BY apc.id_crit";
                    } else {
                        request = "SELECT apc.id_crit, apc.priority, apc.CRITERION_COMMENT, apc.selected, apc.comment_user, apc.MASTER_ELEMENT"
                                + " FROM ACTION_PLAN ap, ACTION_PLAN_CRITERION apc"
                                + " WHERE ap.id_action_plan = ?"
                                + " AND apc.id_action_plan = ap.id_action_plan"
                                + " ORDER BY apc.id_crit";
                    }
//				Preparing the request.
                    pstmt = connection.prepareStatement(request);
                    pstmt.setString(1, retour.getId());
//				Executing the request.
                    rs = pstmt.executeQuery();
//				Loop on result set.

                    ActionPlanElementBean element = null;
                    while (rs.next()) {
                        element = this.getElementFromResultSetAndElementType(rs, elementType, ea);
                        if (element != null) {
                            retour.setElementCorrected(rs.getBoolean("selected"), element);
                            retour.addElementToActionPlan(element);
                        }
                    }
                    if (useCache) {
                        dataCache.storeToCache(idBline, "retrieveActionPlan"
                                + idBline + ea.getId(), retour);
                    }
                }
            } catch (SQLException e) {
                logger.error("Erreur lors de la recuperation du plan d'action");
                logger.error("id_bline=" + idBline + ", id_elt=" + ea.getId(), e);
            } finally {
                JdbcDAOUtils.closeResultSet(rs);
                JdbcDAOUtils.closePrepareStatement(pstmt);
                JdbcDAOUtils.closeConnection(connection);
            }
        }

        return retour;
    }

    /**
     * Creates a new action plan
     * @param idEa the element id for the action plan
     * @param idBline the baseline id for the action plan
     * @param conn the connection to the database, not closed at the end
     * @return the new action plan's id
     */
    private String createNewActionPlan(String idElt, String idBline, Connection conn) {
        String retour = "";

        PreparedStatement pstmtInsert = null;
        String createNewActionPlanQuery = "INSERT INTO ACTION_PLAN(ID_ACTION_PLAN, ID_ELT, ID_BLINE) VALUES (?, ?, ?)";
        try {
            retour = IDCreator.getID();
            pstmtInsert = conn.prepareStatement(createNewActionPlanQuery);
            pstmtInsert.setString(1, retour);
            pstmtInsert.setString(2, idElt);
            pstmtInsert.setString(3, idBline);
            pstmtInsert.executeUpdate();
        } catch (SQLException e) {
            logger.error("Error action plan creation", e);
        } finally {
            // Fermeture des requ�tes.
            JdbcDAOUtils.closePrepareStatement(pstmtInsert);
        }

        return retour;
    }

    /**
     * returns action plan information (id, comments) for an element and a baseline
     * @param elt element
     * @param idBline baseline
     * @param conn connexion to the database
     * @param create create one if it does not exists
     * @return action plan information
     */
    private ActionPlanBean getActionPlanBean(ElementBean elt, String idBline, boolean create, Connection conn) {
        ActionPlanBean retour = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String createNewActionPlanQuery = "SELECT ID_ACTION_PLAN, ACTION_PLAN_COMMENT, COMMENT_USER FROM ACTION_PLAN WHERE ID_ELT = ? AND ID_BLINE = ?";
        try {
            pstmt = conn.prepareStatement(createNewActionPlanQuery);
            pstmt.setString(1, elt.getId());
            pstmt.setString(2, idBline);
            rs = pstmt.executeQuery();
            if (ElementType.EA.equals(elt.getTypeElt())) {
                retour = new ApplicationEntityActionPlanBean(elt.getId(), idBline);
            } else {
                retour = new ProjectActionPlanBean(elt.getId(), idBline);
            }
            if (rs.next()) {
                retour.setId(rs.getString("id_action_plan"));
                retour.setCommentUser(rs.getString("COMMENT_USER"));
                retour.setActionPlanComment(rs.getString("ACTION_PLAN_COMMENT"));
            } else if (create) {
                String id = this.createNewActionPlan(elt.getId(), idBline, conn);
                retour.setId(id);
            } else {
                retour = null;
            }
        } catch (SQLException e) {
            logger.error("Error action plan retrieving", e);
        } finally {
            // Fermeture des requetes.
            JdbcDAOUtils.closeResultSet(rs);
            JdbcDAOUtils.closePrepareStatement(pstmt);
        }
        return retour;
    }

    /**
     * @{@inheritDoc }
     */
    public void updateElementForActionPlan(ActionPlanBean actionPlan, ActionPlanElementBean element) throws DataAccessException {
        if (element.isCorrected() || (element.getComment() != null
                && !"".equals(element.getComment()))) {
            this.insertOrUpdateElementForActionPlan(actionPlan, element);
        } else {
            this.deleteElementFromActionPlan(actionPlan, element);
        }
    }

    private void insertOrUpdateElementForActionPlan(ActionPlanBean actionPlan, ActionPlanElementBean element) throws DataAccessException {
        Connection connection = JdbcDAOUtils.getConnection(this, Constants.CAQS_DATASOURCE_KEY);

        String selectRequest = "SELECT COUNT(ID_CRIT) as nbCrit FROM ACTION_PLAN_CRITERION WHERE ID_ACTION_PLAN = ? AND ID_CRIT = ?";
        String insertRequest = "INSERT INTO ACTION_PLAN_CRITERION(ID_ACTION_PLAN, ID_CRIT, PRIORITY, CRITERION_COMMENT, SELECTED, COMMENT_USER, MASTER_ELEMENT)"
                + " VALUES(?, ?, ?, ?, ?, ?, ?)";
        String updateRequest = "UPDATE ACTION_PLAN_CRITERION SET PRIORITY = ?, CRITERION_COMMENT = ?, SELECTED = ?, COMMENT_USER = ?, MASTER_ELEMENT = ? "
                + " WHERE ID_ACTION_PLAN = ? AND ID_CRIT = ?";


        PreparedStatement pstmtSelect = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmtSelect = connection.prepareStatement(selectRequest);
            pstmtSelect.setString(1, actionPlan.getId());
            pstmtSelect.setString(2, element.getId());

            int nb = -1;

            rs = pstmtSelect.executeQuery();
            if (rs.next()) {
                nb = rs.getInt("nbCrit");
            }
            if (nb == 0) {
                pstmt = connection.prepareStatement(insertRequest);
                pstmt.setString(1, actionPlan.getId());
                pstmt.setString(2, element.getId());
                pstmt.setString(3, element.getPriority().toString());
                pstmt.setString(4, element.getComment());
                pstmt.setInt(5, element.isCorrected()?1:0);
                pstmt.setString(6, element.getCommentUser());
                pstmt.setString(7, element.getElementMaster());

            } else {
                pstmt = connection.prepareStatement(updateRequest);
                pstmt.setString(1, element.getPriority().toString());
                pstmt.setString(2, element.getComment());
                pstmt.setInt(3, element.isCorrected()?1:0);
                pstmt.setString(4, element.getCommentUser());
                pstmt.setString(5, element.getElementMaster());
                pstmt.setString(6, actionPlan.getId());
                pstmt.setString(7, element.getId());
            }
            pstmt.executeUpdate();

            JdbcDAOUtils.commit(connection);
            //dataCache.clearCache("actionPlan" + idBline);
        } catch (SQLException e) {
            logger.error("Error updating criterion data", e);
            throw new DataAccessException(e);
        } finally {
            JdbcDAOUtils.closeResultSet(rs);
            JdbcDAOUtils.closePrepareStatement(pstmt);
            JdbcDAOUtils.closeConnection(connection);
        }
    }

    private void deleteElementFromActionPlan(ActionPlanBean actionPlan, ActionPlanElementBean criterion) throws DataAccessException {
        Connection connection = JdbcDAOUtils.getConnection(this, Constants.CAQS_DATASOURCE_KEY);

        String selectRequest = "SELECT COUNT(ID_CRIT) as nbCrit FROM ACTION_PLAN_CRITERION WHERE ID_ACTION_PLAN = ? AND ID_CRIT = ?";
        String deleteRequest = "DELETE FROM ACTION_PLAN_CRITERION WHERE ID_ACTION_PLAN = ? AND ID_CRIT = ?";


        PreparedStatement pstmtSelect = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmtSelect = connection.prepareStatement(selectRequest);
            pstmtSelect.setString(1, actionPlan.getId());
            pstmtSelect.setString(2, criterion.getId());

            int nb = -1;

            rs = pstmtSelect.executeQuery();
            if (rs.next()) {
                nb = rs.getInt("nbCrit");
            }
            if (nb > 0) {
                pstmt = connection.prepareStatement(deleteRequest);
                pstmt.setString(1, actionPlan.getId());
                pstmt.setString(2, criterion.getId());
                pstmt.executeUpdate();
            }
        } catch (SQLException e) {
            logger.error("Error updating criterion data", e);
            throw new DataAccessException(e);
        } finally {
            // Fermeture des requetes.
            JdbcDAOUtils.closeResultSet(rs);
            JdbcDAOUtils.closePrepareStatement(pstmtSelect);
            JdbcDAOUtils.closePrepareStatement(pstmt);
            JdbcDAOUtils.closeConnection(connection);
        }
    }

    /** {@inheritDoc}
     */
    public void saveActionPlanInfos(ActionPlanBean ap) throws DataAccessException {
        Connection connection = JdbcDAOUtils.getConnection(this, Constants.CAQS_DATASOURCE_KEY);
        String updateAPRequest = "UPDATE ACTION_PLAN SET ACTION_PLAN_COMMENT = ?, COMMENT_USER = ? "
                + " WHERE ID_ACTION_PLAN = ?";

        PreparedStatement pstmtAPUpdate = null;
        try {
            pstmtAPUpdate = connection.prepareStatement(updateAPRequest);
            pstmtAPUpdate.setString(1, ap.getActionPlanComment());
            pstmtAPUpdate.setString(2, ap.getCommentUser());
            pstmtAPUpdate.setString(3, ap.getId());
            pstmtAPUpdate.executeUpdate();
            JdbcDAOUtils.commit(connection);
        } catch (SQLException e) {
            logger.error("Error updating criterion data", e);
            throw new DataAccessException(e);
        } finally {
            // Fermeture des requetes.
            JdbcDAOUtils.closePrepareStatement(pstmtAPUpdate);
            JdbcDAOUtils.closeConnection(connection);
        }
    }
    private static final String NUMBER_ELEMENTS_IN_ACTIONS_PLAN_BY_CRITERIONS_REQUEST =
            "Select count(crit.id_elt) as nb, crit.id_crit "
            + " From Critere_bline crit, Element elt, Critere_usage cu "
            + " Where crit.id_bline = ? And ( "
            + " (crit.just_note_cribl IS NULL AND crit.note_cribl < 3) "
            + " OR (crit.just_note_cribl IS NOT NULL AND crit.just_note_cribl < 3) ) AND crit.note_cribl > 0 "
            + " AND crit.id_crit in "
            + "  (select id_crit from action_plan_criterion where id_action_plan in (select id_action_plan from action_plan where id_bline = ? "
            + " AND id_elt = ?) AND selected = 1)"
            + " And crit.id_elt = elt.id_elt And elt.id_telt = cu.id_telt And cu.id_usa = ? and cu.id_crit = crit.id_crit group by crit.id_crit";

    /**
     * @{@inheritDoc }
     */
    public Map<String, Integer> getNumberOfElementsInActionsPlanForCriterion(ElementBean eltBean, String idBline) {
        Map<String, Integer> retour =
                (Map<String, Integer>) dataCache.loadFromCache(
                "getNumberOfElementsInActionsPlanForCriterion" + idBline
                + eltBean.getId());
        if (retour == null) {
            retour = new HashMap<String, Integer>();

            Connection connection = JdbcDAOUtils.getConnection(this, Constants.CAQS_DATASOURCE_KEY);
            PreparedStatement pstmt = null;
            ResultSet rs = null;
            try {
                pstmt = connection.prepareStatement(NUMBER_ELEMENTS_IN_ACTIONS_PLAN_BY_CRITERIONS_REQUEST);
                pstmt.setString(1, idBline);
                pstmt.setString(2, idBline);
                pstmt.setString(3, eltBean.getId());
                pstmt.setString(4, eltBean.getUsage().getId());
                // Executing the request.
                rs = pstmt.executeQuery();
                while (rs.next()) {
                    String idCrit = rs.getString("id_crit");
                    int nb = rs.getInt("nb");
                    retour.put(idCrit, nb);
                }
                dataCache.storeToCache(idBline, "getNumberOfElementsInActionsPlanForCriterion"
                        + idBline + eltBean.getId(), retour);
            } catch (SQLException e) {
                logger.error("getNumberOfElementsInActionsPlanForCriterion");
                logger.error("id_bline=" + idBline + ", id_ea="
                        + eltBean.getId(), e);
            } finally {
                JdbcDAOUtils.closeResultSet(rs);
                JdbcDAOUtils.closePrepareStatement(pstmt);
                JdbcDAOUtils.closeConnection(connection);
            }
        }
        return retour;
    }
    private static final String ACTION_PLAN_SUB_CRIT_REQUEST =
            "Select elt.id_elt, elt.id_telt, elt.lib_elt, elt.desc_elt, crit.note_cribl, crit.tendance, crit.just_note_cribl, crit.id_just"
            + " From Element elt, Critere_bline crit, Critere_usage cu"
            + " Where crit.id_bline = ?" + " And crit.id_elt = elt.id_elt"
            + " And crit.id_crit = ?" + " And ("
            + " (crit.just_note_cribl IS NULL AND crit.note_cribl < 3)"
            + " OR (crit.just_note_cribl IS NOT NULL AND crit.just_note_cribl < 3)"
            + ")" + "AND crit.note_cribl > 0" + " And cu.id_usa = ?"
            + " And cu.id_crit = crit.id_crit" + " And elt.id_telt = cu.id_telt"
            + " And elt.id_main_elt = ?" + " And elt.id_pro = crit.id_pro";

    /** {@inheritDoc}
     */
    public ActionPlanElementBeanMap getElementsImpactedByActionPlan(ActionPlanElementBeanCollection<ActionPlanCriterionBean> criterions, String idUsa) {
        String idEa = criterions.getIdEa();
        String idBline = criterions.getIdBline();
        ActionPlanElementBeanMap retour = new ActionPlanElementBeanMap(idEa, idBline);

        Connection connection = JdbcDAOUtils.getConnection(this, Constants.CAQS_DATASOURCE_KEY);
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = connection.prepareStatement(ACTION_PLAN_SUB_CRIT_REQUEST);
            for (ActionPlanCriterionBean criterion : criterions) {
                if (criterion.isCorrected()) {
                    try {
                        pstmt.setString(1, idBline);
                        pstmt.setString(2, criterion.getId());
                        pstmt.setString(3, idUsa);
                        pstmt.setString(4, idEa);
                        // Executing the request.
                        rs = pstmt.executeQuery();
                        while (rs.next()) {
                            String idElt = rs.getString("id_elt");
                            ActionPlanImpactedElementBean elt = retour.get(idElt);
                            if (elt == null) {
                                elt = new ActionPlanImpactedElementBean(idElt);
                                elt.setIdTelt(rs.getString("id_telt"));
                                elt.setLibElt(rs.getString("lib_elt"));
                                elt.setDescElt(rs.getString("desc_elt"));
                                int note = rs.getInt("note_cribl");
                                int justNote = rs.getInt("just_note_cribl");
                                elt.setMark((justNote > 0) ? justNote : note);
                                retour.put(idElt, elt);
                            }
                            elt.getCriterions().add(criterion);
                        }

                    } catch (SQLException e) {
                        logger.error("Erreur lors de la recuperation du plan d'action");
                        logger.error("id_bline=" + criterions.getIdBline()
                                + ", id_elt=" + criterions.getIdEa()
                                + ", id_crit=" + criterion.getId(), e);
                    } finally {
                        JdbcDAOUtils.closeResultSet(rs);
                    }
                }
            }
        } catch (SQLException e) {
            logger.error("Erreur lors de la recuperation du plan d'action");
            logger.error("id_bline=" + criterions.getIdBline() + ", id_elt="
                    + criterions.getIdEa(), e);
        } finally {
            JdbcDAOUtils.closePrepareStatement(pstmt);
            JdbcDAOUtils.closeConnection(connection);
        }
        return retour;
    }

    /** {@inheritDoc}
     */
    public void invalidateSearchResults(String idBline) {
        dataCache.clearCache(idBline);
    }
    private static final String ACTION_PLAN_DETERIORATED_ELTS_FOR_CRITERION_QUERY =
            "SELECT e.id_elt, e.lib_elt, e.desc_elt, e.id_telt, cb.note_cribl, cb.just_note_cribl"
            + " FROM Element e, Critere_bline cb, Critere_bline prevCb, Critere_usage cu"
            + " WHERE e.id_main_elt = ?" + " AND cb.id_elt = e.id_elt"
            + " AND prevCb.id_elt = e.id_elt" + " AND cb.id_crit = ?"
            + " AND cb.id_bline = ?" + " AND cb.id_crit = prevCb.id_crit"
            + " AND prevCb.id_bline = ?" + " AND cu.id_usa = ?"
            + " AND cu.id_crit = cb.id_crit" + " AND e.id_telt = cu.id_telt"
            + " AND " + "	("
            + "		(cb.just_note_cribl IS NULL AND prevCb.just_note_cribl IS NULL AND cb.note_cribl < prevCb.note_cribl)"
            + "	OR"
            + "		(cb.just_note_cribl IS NOT NULL AND prevCb.just_note_cribl IS NULL AND cb.just_note_cribl < prevCb.note_cribl)"
            + "	OR"
            + "		(cb.just_note_cribl IS NOT NULL AND prevCb.just_note_cribl IS NOT NULL AND cb.just_note_cribl < prevCb.just_note_cribl)"
            + "	OR"
            + "		(cb.just_note_cribl IS NULL AND prevCb.just_note_cribl IS NOT NULL AND cb.note_cribl < prevCb.just_note_cribl)"
            + "	)";
    private static final String ACTION_PLAN_CORRECTED_ELTS_FOR_CRITERION_QUERY =
            "SELECT e.id_elt, e.lib_elt, e.desc_elt, e.id_telt, cb.note_cribl, cb.just_note_cribl"
            + " FROM Element e, Critere_bline cb, Critere_bline prevCb, Critere_usage cu"
            + " WHERE e.id_main_elt = ?" + " AND cb.id_elt = e.id_elt"
            + " AND prevCb.id_elt = e.id_elt" + " AND cb.id_crit = ?"
            + " AND cb.id_bline = ?" + " AND cb.id_crit = prevCb.id_crit"
            + " AND prevCb.id_bline = ?" + " AND cu.id_usa = ?"
            + " AND cu.id_crit = cb.id_crit" + " AND e.id_telt = cu.id_telt"
            + " AND " + "	("
            + "		(cb.just_note_cribl IS NULL AND prevCb.just_note_cribl IS NULL AND cb.note_cribl >= 3 AND prevCb.note_cribl < cb.note_cribl)"
            + "	OR"
            + "		(cb.just_note_cribl IS NOT NULL AND prevCb.just_note_cribl IS NULL AND cb.just_note_cribl >= 3 AND prevCb.note_cribl < cb.just_note_cribl)"
            + "	OR"
            + "		(cb.just_note_cribl IS NOT NULL AND prevCb.just_note_cribl IS NOT NULL AND cb.just_note_cribl >= 3 AND prevCb.just_note_cribl < cb.just_note_cribl)"
            + "	OR"
            + "		(cb.just_note_cribl IS NULL AND prevCb.just_note_cribl IS NOT NULL AND cb.note_cribl >= 3 AND prevCb.just_note_cribl < cb.note_cribl)"
            + "	)";
    private static final String ACTION_PLAN_PARTIALLY_CORRECTED_ELTS_FOR_CRITERION_QUERY =
            "SELECT e.id_elt, e.lib_elt, e.desc_elt, e.id_telt, cb.note_cribl, cb.just_note_cribl"
            + " FROM Element e, Critere_bline cb, Critere_bline prevCb, Critere_usage cu"
            + " WHERE e.id_main_elt = ?" + " AND cb.id_elt = e.id_elt"
            + " AND prevCb.id_elt = e.id_elt" + " AND cb.id_crit = ?"
            + " AND cb.id_bline = ?" + " AND cb.id_crit = prevCb.id_crit"
            + " AND prevCb.id_bline = ?" + " AND cu.id_usa = ?"
            + " AND cu.id_crit = cb.id_crit" + " AND e.id_telt = cu.id_telt"
            + " AND " + "	("
            + "		(cb.just_note_cribl IS NULL AND prevCb.just_note_cribl IS NULL AND cb.note_cribl > prevCb.note_cribl AND cb.note_cribl < 3)"
            + "	OR"
            + "		(cb.just_note_cribl IS NOT NULL AND prevCb.just_note_cribl IS NULL AND cb.just_note_cribl > prevCb.note_cribl AND cb.just_note_cribl < 3)"
            + "	OR"
            + "		(cb.just_note_cribl IS NOT NULL AND prevCb.just_note_cribl IS NOT NULL AND cb.just_note_cribl > prevCb.just_note_cribl AND cb.just_note_cribl < 3)"
            + "	OR"
            + "		(cb.just_note_cribl IS NULL AND prevCb.just_note_cribl IS NOT NULL AND cb.note_cribl > prevCb.just_note_cribl AND cb.note_cribl < 3)"
            + "	)";
    private static final String ACTION_PLAN_STABLE_ELTS_FOR_CRITERION_QUERY =
            "SELECT e.id_elt, e.lib_elt, e.desc_elt, e.id_telt, cb.note_cribl, cb.just_note_cribl"
            + " FROM Element e, Critere_bline cb, Critere_bline prevCb, Critere_usage cu"
            + " WHERE e.id_main_elt = ?" + " AND cb.id_elt = e.id_elt"
            + " AND prevCb.id_elt = e.id_elt" + " AND cb.id_crit = ?"
            + " AND cb.id_bline = ?" + " AND cb.id_crit = prevCb.id_crit"
            + " AND prevCb.id_bline = ?" + " AND cu.id_usa = ?"
            + " AND cu.id_crit = cb.id_crit" + " AND e.id_telt = cu.id_telt"
            + " AND " + "	("
            + "		(cb.just_note_cribl IS NULL AND prevCb.just_note_cribl IS NULL AND cb.note_cribl = prevCb.note_cribl and cb.note_cribl < 3)"
            + "	OR"
            + "		(cb.just_note_cribl IS NOT NULL AND prevCb.just_note_cribl IS NULL AND cb.just_note_cribl = prevCb.note_cribl AND cb.just_note_cribl < 3)"
            + "	OR"
            + "		(cb.just_note_cribl IS NOT NULL AND prevCb.just_note_cribl IS NOT NULL AND cb.just_note_cribl = prevCb.just_note_cribl AND cb.just_note_cribl < 3)"
            + "	OR"
            + "		(cb.just_note_cribl IS NULL AND prevCb.just_note_cribl IS NOT NULL AND cb.note_cribl = prevCb.just_note_cribl AND cb.note_cribl < 3)"
            + "	)";

    public ActionPlanImpactedElementBeanCollection getDeterioratedElementsForCriterion(
            String idEa,
            String idCrit,
            String idBline,
            String idPrevBline,
            String idUsa) {
        return this.getElementsForCriterion(idEa, idCrit, idBline, idPrevBline, ACTION_PLAN_DETERIORATED_ELTS_FOR_CRITERION_QUERY, "deteriorated", idUsa);
    }

    public ActionPlanImpactedElementBeanCollection getStablesElementsForCriterion(
            String idEa,
            String idCrit,
            String idBline,
            String idPrevBline,
            String idUsa) {
        return this.getElementsForCriterion(idEa, idCrit, idBline, idPrevBline, ACTION_PLAN_STABLE_ELTS_FOR_CRITERION_QUERY, "stable", idUsa);
    }

    public ActionPlanImpactedElementBeanCollection getCorrectedElementsForCriterion(
            String idEa,
            String idCrit,
            String idBline,
            String idPrevBline,
            String idUsa) {
        return this.getElementsForCriterion(idEa, idCrit, idBline, idPrevBline, ACTION_PLAN_CORRECTED_ELTS_FOR_CRITERION_QUERY, "corrected", idUsa);
    }

    public ActionPlanImpactedElementBeanCollection getPartiallyCorrectedElementsForCriterion(
            String idEa,
            String idCrit,
            String idBline,
            String idPrevBline,
            String idUsa) {
        return this.getElementsForCriterion(idEa, idCrit, idBline, idPrevBline, ACTION_PLAN_PARTIALLY_CORRECTED_ELTS_FOR_CRITERION_QUERY, "partiallycorrected", idUsa);
    }
    private static final String ACTION_PLAN_DEGRADED_ELTS_BY_CRITERION_QUERY =
            "SELECT e.id_elt, e.lib_elt, e.desc_elt, e.id_telt, cb.note_cribl, cb.just_note_cribl, cb.id_crit"
            + " FROM Element e, Critere_bline cb, Critere_bline prevCb, Critere_usage cu, Action_plan ap, Action_plan_criterion apc"
            + " WHERE e.id_main_elt = ?" + " AND cb.id_elt = e.id_elt"
            + " AND prevCb.id_elt = e.id_elt" + " AND cb.id_crit = apc.id_crit"
            + " AND cb.id_bline = ?" + " AND cb.id_crit = prevCb.id_crit"
            + " AND prevCb.id_bline = ?" + " AND cu.id_usa = ?"
            + " AND cu.id_crit = cb.id_crit" + " AND e.id_telt = cu.id_telt"
            + " AND " + "	("
            + "		(cb.just_note_cribl IS NULL AND prevCb.just_note_cribl IS NULL AND cb.note_cribl < prevCb.note_cribl)"
            + "	OR"
            + "		(cb.just_note_cribl IS NOT NULL AND prevCb.just_note_cribl IS NULL AND cb.just_note_cribl < prevCb.note_cribl)"
            + "	OR"
            + "		(cb.just_note_cribl IS NOT NULL AND prevCb.just_note_cribl IS NOT NULL AND cb.just_note_cribl < prevCb.just_note_cribl)"
            + "	OR"
            + "		(cb.just_note_cribl IS NULL AND prevCb.just_note_cribl IS NOT NULL AND cb.note_cribl < prevCb.just_note_cribl)"
            + "	)"
            + " AND ap.id_elt = ? AND ap.id_bline = ? AND ap.id_action_plan = apc.id_action_plan AND apc.selected = 1";

    /**
     * @{@inheritDoc }
     */
    public Map<String, ActionPlanImpactedElementBeanCollection> getDegradedElementsByCriterions(
            String idEa,
            String idBline,
            String idPrevBline,
            String idUsa) {
        return this.getElementsByCriterion(idEa, idBline, idPrevBline, ACTION_PLAN_DEGRADED_ELTS_BY_CRITERION_QUERY, "degraded", idUsa);
    }
    private static final String ACTION_PLAN_CORRECTED_ELTS_BY_CRITERION_QUERY =
            "SELECT e.id_elt, e.lib_elt, e.desc_elt, e.id_telt, cb.note_cribl, cb.just_note_cribl, cb.id_crit"
            + " FROM Element e, Critere_bline cb, Critere_bline prevCb, Critere_usage cu, Action_plan ap, Action_plan_criterion apc"
            + " WHERE e.id_main_elt = ?" + " AND cb.id_elt = e.id_elt"
            + " AND prevCb.id_elt = e.id_elt" + " AND cb.id_crit = apc.id_crit"
            + " AND cb.id_bline = ?" + " AND cb.id_crit = prevCb.id_crit"
            + " AND prevCb.id_bline = ?" + " AND cu.id_usa = ?"
            + " AND cu.id_crit = cb.id_crit" + " AND e.id_telt = cu.id_telt"
            + " AND " + "	("
            + "		(cb.just_note_cribl IS NULL AND prevCb.just_note_cribl IS NULL AND cb.note_cribl >= 3 AND prevCb.note_cribl < 3)"
            + "	OR"
            + "		(cb.just_note_cribl IS NOT NULL AND prevCb.just_note_cribl IS NULL AND cb.just_note_cribl >= 3 AND prevCb.note_cribl < 3)"
            + "	OR"
            + "		(cb.just_note_cribl IS NOT NULL AND prevCb.just_note_cribl IS NOT NULL AND cb.just_note_cribl >= 3 AND prevCb.just_note_cribl < 3)"
            + "	OR"
            + "		(cb.just_note_cribl IS NULL AND prevCb.just_note_cribl IS NOT NULL AND cb.note_cribl >= 3 AND prevCb.just_note_cribl < 3)"
            + "	) "
            + " AND ap.id_elt = ? AND ap.id_bline = ? AND ap.id_action_plan = apc.id_action_plan AND apc.selected = 1";

    /**
     * @{@inheritDoc }
     */
    public Map<String, ActionPlanImpactedElementBeanCollection> getCorrectedElementsByCriterions(
            String idEa,
            String idBline,
            String idPrevBline,
            String idUsa) {
        return this.getElementsByCriterion(idEa, idBline, idPrevBline, ACTION_PLAN_CORRECTED_ELTS_BY_CRITERION_QUERY, "corrected", idUsa);
    }

    private Map<String, ActionPlanImpactedElementBeanCollection> getElementsByCriterion(
            String idEa,
            String idBline,
            String idPrevBline,
            String query,
            String type,
            String idUsa) {
        Map<String, ActionPlanImpactedElementBeanCollection> retour =
                (Map<String, ActionPlanImpactedElementBeanCollection>) dataCache.loadFromCache(
                "getElementsByCriterion" + idBline + idEa + idPrevBline + type
                + idUsa);
        if (retour == null) {
            Connection connection = JdbcDAOUtils.getConnection(this, Constants.CAQS_DATASOURCE_KEY);
            PreparedStatement pstmt = null;
            ResultSet rs = null;
            try {
                pstmt = connection.prepareStatement(query);
                pstmt.setString(1, idEa);
                pstmt.setString(2, idBline);
                pstmt.setString(3, idPrevBline);
                pstmt.setString(4, idUsa);
                pstmt.setString(5, idEa);
                pstmt.setString(6, idPrevBline);

                retour = new HashMap<String, ActionPlanImpactedElementBeanCollection>();
                // Executing the request.
                rs = pstmt.executeQuery();

                while (rs.next()) {
                    String idElt = rs.getString("id_elt");
                    ActionPlanImpactedElementBean elt = new ActionPlanImpactedElementBean(idElt);
                    elt.setIdTelt(rs.getString("id_telt"));
                    elt.setLibElt(rs.getString("lib_elt"));
                    elt.setDescElt(rs.getString("desc_elt"));
                    int note = rs.getInt("note_cribl");
                    int justNote = rs.getInt("just_note_cribl");
                    elt.setMark((justNote > 0) ? justNote : note);
                    String idCrit = rs.getString("id_crit");
                    ActionPlanImpactedElementBeanCollection coll = retour.get(idCrit);
                    if (coll == null) {
                        coll = new ActionPlanImpactedElementBeanCollection(idEa, idBline);
                        retour.put(idCrit, coll);
                    }
                    coll.add(elt);
                }
                dataCache.storeToCache(idBline, "getElementsByCriterion"
                        + idBline
                        + idEa + idPrevBline + type + idUsa, retour);
            } catch (SQLException e) {
                logger.error("Erreur lors de la recuperation du plan d'action");
                logger.error("id_bline=" + idBline + ", id_elt=" + idEa
                        + ", idPrevBline=" + idPrevBline + ", idUsa = " + idUsa, e);
            } finally {
                JdbcDAOUtils.closeResultSet(rs);
                JdbcDAOUtils.closePrepareStatement(pstmt);
                JdbcDAOUtils.closeConnection(connection);
            }
        }
        return retour;
    }

    private ActionPlanImpactedElementBeanCollection getElementsForCriterion(
            String idEa,
            String idCrit,
            String idBline,
            String idPrevBline,
            String query,
            String type,
            String idUsa) {
        ActionPlanImpactedElementBeanCollection retour =
                (ActionPlanImpactedElementBeanCollection) dataCache.loadFromCache(
                "getElementsForCriterion" + idBline + idEa + idPrevBline + type
                + idCrit + idUsa);
        if (retour == null) {
            Connection connection = JdbcDAOUtils.getConnection(this, Constants.CAQS_DATASOURCE_KEY);
            PreparedStatement pstmt = null;
            ResultSet rs = null;
            try {
                pstmt = connection.prepareStatement(query);
                pstmt.setString(1, idEa);
                pstmt.setString(2, idCrit);
                pstmt.setString(3, idBline);
                pstmt.setString(4, idPrevBline);
                pstmt.setString(5, idUsa);

                retour = new ActionPlanImpactedElementBeanCollection(idEa, idBline);
                // Executing the request.
                rs = pstmt.executeQuery();

                while (rs.next()) {
                    String idElt = rs.getString("id_elt");
                    ActionPlanImpactedElementBean elt = new ActionPlanImpactedElementBean(idElt);
                    elt.setIdTelt(rs.getString("id_telt"));
                    elt.setLibElt(rs.getString("lib_elt"));
                    elt.setDescElt(rs.getString("desc_elt"));
                    int note = rs.getInt("note_cribl");
                    int justNote = rs.getInt("just_note_cribl");
                    elt.setMark((justNote > 0) ? justNote : note);
                    retour.add(elt);
                }
                dataCache.storeToCache(idBline, "getElementsForCriterion"
                        + idBline
                        + idEa + idPrevBline + type + idCrit + idUsa, retour);
            } catch (SQLException e) {
                logger.error("Erreur lors de la recuperation du plan d'action");
                logger.error("id_bline=" + idBline + ", id_elt=" + idEa
                        + ", idPrevBline=" + idPrevBline + ", id_crit=" + idCrit
                        + ", idUsa = " + idUsa, e);
            } finally {
                JdbcDAOUtils.closeResultSet(rs);
                JdbcDAOUtils.closePrepareStatement(pstmt);
                JdbcDAOUtils.closeConnection(connection);
            }
        }
        return retour;
    }

    private static final String ACTION_PLAN_PARTIALLY_CORRECTED_ELTS_BY_CRITERION_QUERY =
            "SELECT e.id_elt, e.lib_elt, e.desc_elt, e.id_telt, cb.note_cribl, cb.just_note_cribl, cb.id_crit"
            + " FROM Element e, Critere_bline cb, Critere_bline prevCb, Critere_usage cu, Action_plan ap, Action_plan_criterion apc"
            + " WHERE e.id_main_elt = ?" + " AND cb.id_elt = e.id_elt"
            + " AND prevCb.id_elt = e.id_elt" + " AND cb.id_crit = apc.id_crit"
            + " AND cb.id_bline = ?" + " AND cb.id_crit = prevCb.id_crit"
            + " AND prevCb.id_bline = ?" + " AND cu.id_usa = ?"
            + " AND cu.id_crit = cb.id_crit" + " AND e.id_telt = cu.id_telt"
            + " AND " + "	("
            + "		(cb.just_note_cribl IS NULL AND prevCb.just_note_cribl IS NULL AND cb.note_cribl > prevCb.note_cribl AND cb.note_cribl < 3)"
            + "	OR"
            + "		(cb.just_note_cribl IS NOT NULL AND prevCb.just_note_cribl IS NULL AND cb.just_note_cribl > prevCb.note_cribl AND cb.just_note_cribl < 3)"
            + "	OR"
            + "		(cb.just_note_cribl IS NOT NULL AND prevCb.just_note_cribl IS NOT NULL AND cb.just_note_cribl > prevCb.just_note_cribl AND cb.just_note_cribl < 3)"
            + "	OR"
            + "		(cb.just_note_cribl IS NULL AND prevCb.just_note_cribl IS NOT NULL AND cb.note_cribl > prevCb.just_note_cribl AND cb.note_cribl < 3)"
            + "	)"
            + " AND ap.id_elt = ? AND ap.id_bline = ? AND ap.id_action_plan = apc.id_action_plan AND apc.selected = 1";

    /**
     * @{@inheritDoc }
     */
    public Map<String, ActionPlanImpactedElementBeanCollection> getPartiallyCorrectedElementsByCriterions(
            String idEa,
            String idBline,
            String idPrevBline,
            String idUsa) {
        return this.getElementsByCriterion(idEa, idBline, idPrevBline, ACTION_PLAN_PARTIALLY_CORRECTED_ELTS_BY_CRITERION_QUERY, "partiallyCorrected", idUsa);
    }

    private static final String ACTION_PLAN_STABLE_ELTS_BY_CRITERION_QUERY =
            "SELECT e.id_elt, e.lib_elt, e.desc_elt, e.id_telt, cb.note_cribl, cb.just_note_cribl, cb.id_crit"
            + " FROM Element e, Critere_bline cb, Critere_bline prevCb, Critere_usage cu, Action_plan ap, Action_plan_criterion apc"
            + " WHERE e.id_main_elt = ?" + " AND cb.id_elt = e.id_elt"
            + " AND prevCb.id_elt = e.id_elt" + " AND cb.id_crit = apc.id_crit"
            + " AND cb.id_bline = ?" + " AND cb.id_crit = prevCb.id_crit"
            + " AND prevCb.id_bline = ?" + " AND cu.id_usa = ?"
            + " AND cu.id_crit = cb.id_crit" + " AND e.id_telt = cu.id_telt"
            + " AND " + "	("
            + "		(cb.just_note_cribl IS NULL AND prevCb.just_note_cribl IS NULL AND cb.note_cribl = prevCb.note_cribl and cb.note_cribl < 3)"
            + "	OR"
            + "		(cb.just_note_cribl IS NOT NULL AND prevCb.just_note_cribl IS NULL AND cb.just_note_cribl = prevCb.note_cribl AND cb.just_note_cribl < 3)"
            + "	OR"
            + "		(cb.just_note_cribl IS NOT NULL AND prevCb.just_note_cribl IS NOT NULL AND cb.just_note_cribl = prevCb.just_note_cribl AND cb.just_note_cribl < 3)"
            + "	OR"
            + "		(cb.just_note_cribl IS NULL AND prevCb.just_note_cribl IS NOT NULL AND cb.note_cribl = prevCb.just_note_cribl AND cb.note_cribl < 3)"
            + "	)"
            + " AND ap.id_elt = ? AND ap.id_bline = ? AND ap.id_action_plan = apc.id_action_plan AND apc.selected = 1";

    /**
     * @{@inheritDoc }
     */
    public Map<String, ActionPlanImpactedElementBeanCollection> getStableElementsByCriterions(
            String idEa,
            String idBline,
            String idPrevBline,
            String idUsa) {
        return this.getElementsByCriterion(idEa, idBline, idPrevBline, ACTION_PLAN_STABLE_ELTS_BY_CRITERION_QUERY, "stable", idUsa);
    }

    private static final String ACTION_PLAN_CORRECTED_BECAUSE_SUPPRESSED_ELTS_BY_CRITERION_QUERY =
            "SELECT e.id_elt, e.lib_elt, e.desc_elt, e.id_telt, prevCb.id_crit" +
            " FROM Element e, Critere_bline prevCb, Critere_usage cu, Action_plan ap, Action_plan_criterion apc "+
            " WHERE e.id_main_elt = ? "+
            " AND prevCb.id_elt = e.id_elt AND prevCb.id_bline = ? AND cu.id_crit = prevCb.id_crit  "+
            " AND e.id_telt = cu.id_telt AND cu.id_usa = ? "+
            " AND ( "+
            " (prevCb.just_note_cribl IS NULL AND prevCb.NOTE_CRIBL < 3) "+
            " OR (prevCb.just_note_cribl IS NOT NULL AND prevCb.just_note_cribl < 3) "+
            " ) "+
            " AND ap.id_elt = ? AND ap.id_bline = ? AND ap.id_action_plan = apc.id_action_plan AND apc.selected = 1 AND prevCb.id_crit = apc.id_crit "+
            " AND e.id_elt NOT IN " +
            " (SELECT distinct cb.id_elt FROM Critere_bline cb, Element elt WHERE cb.id_elt = elt.id_elt AND elt.id_main_elt = ? AND cb.id_crit = prevCb.id_crit AND cb.id_bline = ?)";

    /**
     * @{@inheritDoc }
     */
    public Map<String, ActionPlanImpactedElementBeanCollection> getCorrectedBecauseSuppressedElementsByCriterions(
            String idEa,
            String idBline,
            String idPrevBline,
            String idUsa) {
        Map<String, ActionPlanImpactedElementBeanCollection> retour =
                (Map<String, ActionPlanImpactedElementBeanCollection>) dataCache.loadFromCache(
                "getElementsByCriterion" + idBline + idEa + idPrevBline + "correctedBecauseSuppressed"
                + idUsa);
        if (retour == null) {
            Connection connection = JdbcDAOUtils.getConnection(this, Constants.CAQS_DATASOURCE_KEY);
            PreparedStatement pstmt = null;
            ResultSet rs = null;
            try {
                pstmt = connection.prepareStatement(ACTION_PLAN_CORRECTED_BECAUSE_SUPPRESSED_ELTS_BY_CRITERION_QUERY);
                pstmt.setString(1, idEa);
                pstmt.setString(2, idPrevBline);
                pstmt.setString(3, idUsa);
                pstmt.setString(4, idEa);
                pstmt.setString(5, idPrevBline);
                pstmt.setString(6, idEa);
                pstmt.setString(7, idBline);

                retour = new HashMap<String, ActionPlanImpactedElementBeanCollection>();
                // Executing the request.
                rs = pstmt.executeQuery();

                while (rs.next()) {
                    String idElt = rs.getString("id_elt");
                    ActionPlanImpactedElementBean elt = new ActionPlanImpactedElementBean(idElt);
                    elt.setIdTelt(rs.getString("id_telt"));
                    elt.setLibElt(rs.getString("lib_elt"));
                    elt.setDescElt(rs.getString("desc_elt"));
                    elt.setMark(0);
                    String idCrit = rs.getString("id_crit");
                    ActionPlanImpactedElementBeanCollection coll = retour.get(idCrit);
                    if (coll == null) {
                        coll = new ActionPlanImpactedElementBeanCollection(idEa, idBline);
                        retour.put(idCrit, coll);
                    }
                    coll.add(elt);
                }
                dataCache.storeToCache(idBline, "getElementsByCriterion"
                        + idBline
                        + idEa + idPrevBline + "correctedBecauseSuppressed" + idUsa, retour);
            } catch (SQLException e) {
                logger.error("Erreur lors de la recuperation du plan d'action");
                logger.error("id_bline=" + idBline + ", id_elt=" + idEa
                        + ", idPrevBline=" + idPrevBline + ", idUsa = " + idUsa, e);
            } finally {
                JdbcDAOUtils.closeResultSet(rs);
                JdbcDAOUtils.closePrepareStatement(pstmt);
                JdbcDAOUtils.closeConnection(connection);
            }
        }
        return retour;
    }

}
