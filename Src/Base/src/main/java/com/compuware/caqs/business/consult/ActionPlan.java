package com.compuware.caqs.business.consult;

import com.compuware.caqs.dao.factory.DaoFactory;
import com.compuware.caqs.dao.interfaces.ActionPlanDao;
import com.compuware.caqs.domain.dataschemas.ElementBean;
import com.compuware.caqs.domain.dataschemas.actionplan.ActionPlanBean;
import com.compuware.caqs.domain.dataschemas.actionplan.ApplicationEntityActionPlanBean;
import com.compuware.caqs.domain.dataschemas.actionplan.list.ActionPlanImpactedElementBeanCollection;

public class ActionPlan {
	
	/**
	 * @param ea L'entite applicative
	 * @param idBline L'identifiant de la baseline
	 * @param request la requete
	 * @return le plan d'action
	 */
	public ApplicationEntityActionPlanBean getCompleteActionPlan(
			ElementBean ea, 
			String idBline) {
		ActionPlanDao actionPlanDao = DaoFactory.getInstance().getActionPlanDao();
		return actionPlanDao.getCompleteApplicationEntityActionPlan(ea, idBline, ea.getProject().getId());
	}
	
	public ActionPlanImpactedElementBeanCollection getDeterioratedElementsForCriterion(
			String idEa, 
			String idCrit, 
			String idBline, 
			String idPrevBline,
			String idUsa
	) {
		ActionPlanDao actionPlanDao = DaoFactory.getInstance().getActionPlanDao();
		return actionPlanDao.getDeterioratedElementsForCriterion(idEa, idCrit, idBline, idPrevBline, idUsa);
	}

	public ActionPlanImpactedElementBeanCollection getStablesElementsForCriterion(
			String idEa, 
			String idCrit, 
			String idBline, 
			String idPrevBline,
			String idUsa
	) {
		ActionPlanDao actionPlanDao = DaoFactory.getInstance().getActionPlanDao();
		return actionPlanDao.getStablesElementsForCriterion(idEa, idCrit, idBline, idPrevBline, idUsa);
	}

	public ActionPlanImpactedElementBeanCollection getCorrectedElementsForCriterion(
			String idEa, 
			String idCrit, 
			String idBline, 
			String idPrevBline,
			String idUsa
	){
		ActionPlanDao actionPlanDao = DaoFactory.getInstance().getActionPlanDao();
		return actionPlanDao.getCorrectedElementsForCriterion(idEa, idCrit, idBline, idPrevBline, idUsa);
	}

	public ActionPlanImpactedElementBeanCollection getPartiallyCorrectedElementsForCriterion(
			String idEa, 
			String idCrit, 
			String idBline, 
			String idPrevBline,
			String idUsa
	){
		ActionPlanDao actionPlanDao = DaoFactory.getInstance().getActionPlanDao();
		return actionPlanDao.getPartiallyCorrectedElementsForCriterion(idEa, idCrit, idBline, idPrevBline, idUsa);
	}
}
