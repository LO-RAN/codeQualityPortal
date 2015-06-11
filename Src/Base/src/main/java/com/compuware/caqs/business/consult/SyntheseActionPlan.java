package com.compuware.caqs.business.consult;

import com.compuware.caqs.dao.factory.DaoFactory;
import com.compuware.caqs.dao.interfaces.ActionPlanDao;
import com.compuware.caqs.domain.dataschemas.ElementBean;
import com.compuware.caqs.domain.dataschemas.actionplan.ActionPlanBean;

public class SyntheseActionPlan {
	private DaoFactory daoFactory = DaoFactory.getInstance();

	public ActionPlanBean retrieveSavedActionPlanForEaAndBaseline(ElementBean ea, String idBline) {
		ActionPlanDao dao = daoFactory.getActionPlanDao();
		return dao.getCompleteApplicationEntityActionPlan(ea, idBline, ea.getProject().getId());
	}
}
