package com.compuware.caqs.presentation.consult.actions;

import com.compuware.caqs.service.TaskSvc;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.compuware.caqs.constants.Constants;
import com.compuware.caqs.domain.dataschemas.BaselineBean;
import com.compuware.caqs.domain.dataschemas.ElementBean;
import com.compuware.caqs.domain.dataschemas.ElementType;
import com.compuware.caqs.domain.dataschemas.actionplan.ActionPlanBean;
import com.compuware.caqs.domain.dataschemas.actionplan.ActionPlanCriterionBean;
import com.compuware.caqs.domain.dataschemas.actionplan.list.ActionPlanElementBeanCollection;
import com.compuware.caqs.exception.CaqsException;
import com.compuware.caqs.service.ActionPlanSvc;
import com.compuware.caqs.service.BaselineSvc;
import com.compuware.caqs.service.ElementSvc;
import com.compuware.toolbox.util.logging.LoggerManager;

public final class ElementSelectAction extends Action {

    // --------------------------------------------------------- Public Methods
    @Override
    public ActionForward execute(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) throws CaqsException {
        ActionForward forward = null;

        HttpSession session = request.getSession();

        String idElt = request.getParameter("id_elt");
        String idBline = request.getParameter("id_bline");

        LoggerManager.pushContexte("ElementSelect");

        ElementSvc elementSvc = ElementSvc.getInstance();
        ElementBean eltBean = elementSvc.retrieveElement(idElt, idBline);
        session.setAttribute(Constants.CAQS_CURRENT_ELEMENT_SESSION_KEY, eltBean);

        if (!idBline.equals(eltBean.getBaseline().getId())) {
            session.setAttribute("currentBaselineLib", eltBean.getBaseline().getLib());
        } else {
            session.removeAttribute("currentBaselineLib");
        }

        boolean computing = TaskSvc.getInstance().isComputingElement(eltBean.getId(), idBline);
        if (computing) {
            forward = mapping.findForward("computing");
        } else {
            List<ElementType> elementTypes = elementSvc.retrieveSubElementTypes(idElt);
            session.setAttribute(Constants.CAQS_SUB_ELEMENT_TYPES_SESSION_KEY, elementTypes);

            LoggerManager.popContexte();

            if (request.getParameter("from") != null) {
                forward = mapping.findForward("success"
                        + request.getParameter("from"));
            } else if (!ElementType.EA.equals(eltBean.getTypeElt())) {
                forward = mapping.findForward("successPRJ");
            } else {
                forward = mapping.findForward("success");
            }

            //doit-on afficher l'onglet plan d'action
            ActionPlanSvc actionPlanSvc = ActionPlanSvc.getInstance();
            ActionPlanBean ap = actionPlanSvc.getSavedActionPlan(eltBean, idBline);
            ActionPlanElementBeanCollection<ActionPlanCriterionBean> savedActionPlan = ap.getCorrectedElements();
            BaselineSvc baselineSvc = BaselineSvc.getInstance();
            boolean lastBaseline = baselineSvc.isLastBaseline(idBline, eltBean);
            boolean actionPlanTab = true;
            if (savedActionPlan.isEmpty() && !lastBaseline) {
                actionPlanTab = false;
            }
            request.setAttribute("actionPlanTab", "" + actionPlanTab);
            boolean firstBaseline = baselineSvc.isFirstBaseline(idBline, eltBean.getId());
            request.setAttribute("evolutionTab", "" + (!firstBaseline));
            boolean hasPreviousActionsPlan = false;
            if (!firstBaseline) {
                BaselineBean previousBaseline = baselineSvc.getPreviousBaseline(eltBean.getBaseline(), eltBean.getId());
                if (previousBaseline != null) {
                    ActionPlanBean previousActionsPlan = actionPlanSvc.getCompleteActionPlan(eltBean, previousBaseline.getId(), false, request);
                    ActionPlanElementBeanCollection<ActionPlanCriterionBean> actionPlanPreviousBaseline = previousActionsPlan.getCorrectedElements();
                    hasPreviousActionsPlan = !actionPlanPreviousBaseline.isEmpty();
                    //this.setActionPlanApplication(eltBean, actionPlanPreviousBaseline, evolutionSvc, request);
                    request.setAttribute("previousBaselineId", previousBaseline.getId());
                    request.setAttribute("previousBaselineLib", previousBaseline.getLib());
                }
            }
            request.setAttribute("previousHasActionPlan", hasPreviousActionsPlan);
            request.setAttribute("currentBaselineId", idBline);
            request.setAttribute("currentBaselineLib", eltBean.getBaseline().getLib());
        }
        return forward;
    }
}
