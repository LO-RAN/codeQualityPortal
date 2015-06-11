package com.compuware.caqs.presentation.consult.actions.actionplan;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

import com.compuware.caqs.constants.Constants;
import com.compuware.caqs.domain.dataschemas.ElementBean;
import com.compuware.caqs.domain.dataschemas.ElementType;
import com.compuware.caqs.domain.dataschemas.actionplan.ActionPlanBean;
import com.compuware.caqs.domain.dataschemas.actionplan.ActionPlanElementBean;
import com.compuware.caqs.domain.dataschemas.actionplan.ActionPlanPriority;
import com.compuware.caqs.domain.dataschemas.actionplan.ProjectActionPlanBean;
import com.compuware.caqs.presentation.common.ExtJSAjaxAction;
import com.compuware.caqs.presentation.consult.actions.actionplan.util.ActionPlanSimulateGoalsMark;
import com.compuware.caqs.presentation.util.RequestUtil;
import com.compuware.caqs.presentation.util.StringFormatUtil;
import com.compuware.caqs.service.ActionPlanSvc;
import com.compuware.caqs.service.ElementSvc;
import net.sf.json.JSONObject;

public class ManageActionPlanSavesAction extends ExtJSAjaxAction {

    private ActionPlanElementBean getElementFromRequest(ActionPlanBean ap, HttpServletRequest request) {
        String idCrit = request.getParameter("id_elt");
        ActionPlanElementBean critere = ap.getElements().get(idCrit);
        return critere;
    }

    private void resetActionPlanComputation(ActionPlanBean ap, ElementBean elementBean, String idBline, HttpServletRequest request) {
        ap.setNeedsImpactedElementsRecompute(true);
        ap.setSimulatedKiviatRecomputedPriority(ActionPlanPriority.UNDEFINED);
        //ActionPlanSimulateGoalsMark.getInstance().calculatePieChartMarks(elementBean, idBline, request);
        //on indique aux plan d'actions superieurs qu'ils doivent se mettre à jour
        if(!ElementType.PRJ.equals(elementBean.getTypeElt())) {
            ElementBean father = ElementSvc.getInstance().retrieveSuperElement(elementBean.getId());
            ActionPlanBean fatherAP = ActionPlanSvc.getInstance().getSavedActionPlan(father, idBline, true, false);
            if(fatherAP != null) {
                this.resetActionPlanComputation(fatherAP, father, idBline, request);
            }
        }
    }

    private void updateCriterionForAP(ActionPlanElementBean elt, boolean selected,
            ActionPlanBean ap, ElementBean elementBean, String idBline, HttpServletRequest request) {
        ap.setElementCorrected(selected, elt);
        elt.setElementMaster(elementBean.getId());
        this.resetActionPlanComputation(ap, elementBean, idBline, request);
    }

    public JSONObject retrieveDatas(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {
        JSONObject retour = new JSONObject();
        ElementBean eltBean = (ElementBean) request.getSession().getAttribute(Constants.CAQS_CURRENT_ELEMENT_SESSION_KEY);
        String idBline = eltBean.getBaseline().getId();

        ActionPlanSvc actionPlanSvc = ActionPlanSvc.getInstance();
        ActionPlanBean ap = actionPlanSvc.getCompleteActionPlan(eltBean, idBline, false, request);

        String selectedParam = request.getParameter("selected");
        if (selectedParam != null) {
            ActionPlanElementBean element = this.getElementFromRequest(ap, request);
            //on a selectionne ou deselectionne un critere
            boolean selected = Boolean.parseBoolean(selectedParam);
            this.updateCriterionForAP(element, selected, ap, eltBean, idBline, request);
            if (!ElementType.EA.equals(eltBean.getTypeElt())) {
                actionPlanSvc.autoFillSubActionPlansToCorrectGoal(element, eltBean, RequestUtil.getConnectedUserId(request), (ProjectActionPlanBean) ap, request);
            }
            actionPlanSvc.updateElementForActionPlan(ap, element);
        } else {
            //devons nous sauvegarder un nouveau commentaire de plan d'action ?
            String apComment = request.getParameter("apComment");
            if (apComment != null) {
                apComment = StringFormatUtil.replaceCarriageReturnByHTML(apComment);
                ap.setActionPlanComment(apComment);
                ap.setCommentUser(RequestUtil.getConnectedUserId(request));
                actionPlanSvc.saveActionPlanInfos(ap);
            } else {
                //ou devons nous sauvegarder un nouveau commentaire de critere ?
                String comment = request.getParameter("criterionComment");
                if (comment != null) {
                    ActionPlanElementBean element = this.getElementFromRequest(ap, request);
                    String commentForDeselectMaster = request.getParameter("commentForDeselectMaster");
                    if (commentForDeselectMaster != null) {
                        //c'est une deselection d'un element selectionne par un plan d'actions maitre
                        this.updateCriterionForAP(element, false, ap, eltBean, idBline, request);
                    }
                    comment = StringFormatUtil.replaceCarriageReturnByHTML(comment);
                    element.setComment(comment);
                    element.setCommentUser(RequestUtil.getConnectedUserId(request));
                    if (comment.length() == 0 && !element.isCorrected()) {
                        //il sera supprime de la base de donnees.
                        element.setElementMaster(null);
                    }
                    actionPlanSvc.updateElementForActionPlan(ap, element);
                } else {
                    //serait-ce la priorite ?
                    String priority = request.getParameter("priority");
                    if (priority != null) {
                        ActionPlanElementBean element = this.getElementFromRequest(ap, request);
                        element.setPriority(ActionPlanPriority.valueOf(priority));
                        actionPlanSvc.updateElementForActionPlan(ap, element);
                    }
                }
            }
        }

        return retour;
    }
}
