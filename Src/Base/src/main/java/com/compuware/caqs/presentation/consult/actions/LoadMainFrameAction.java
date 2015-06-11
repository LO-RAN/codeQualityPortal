package com.compuware.caqs.presentation.consult.actions;

import com.compuware.caqs.business.report.Reporter;
import com.compuware.caqs.dao.factory.DaoFactory;
import com.compuware.caqs.exception.CaqsException;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import javax.servlet.http.HttpSession;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

import com.compuware.caqs.constants.Constants;
import com.compuware.caqs.constants.MessagesCodes;
import com.compuware.caqs.dao.interfaces.LabelDao;
import com.compuware.caqs.domain.dataschemas.BaselineBean;
import com.compuware.caqs.domain.dataschemas.ElementBean;
import com.compuware.caqs.domain.dataschemas.ElementType;
import com.compuware.caqs.domain.dataschemas.FactorBean;
import com.compuware.caqs.domain.dataschemas.LabelBean;
import com.compuware.caqs.domain.dataschemas.actionplan.ActionPlanBean;
import com.compuware.caqs.domain.dataschemas.actionplan.ActionPlanCriterionBean;
import com.compuware.caqs.domain.dataschemas.actionplan.list.ActionPlanElementBeanCollection;
import com.compuware.caqs.domain.dataschemas.comparators.FactorBeanComparator;
import com.compuware.caqs.presentation.common.ExtJSAjaxAction;
import com.compuware.caqs.presentation.util.RequestUtil;
import com.compuware.caqs.service.ActionPlanSvc;
import com.compuware.caqs.service.BaselineSvc;
import com.compuware.caqs.service.ElementSvc;
import com.compuware.caqs.service.FactorSvc;
import com.compuware.caqs.service.TaskSvc;
import java.util.Collections;

public class LoadMainFrameAction extends ExtJSAjaxAction {

    @Override
    public JSONObject retrieveDatas(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {
        JSONObject obj = new JSONObject();
        MessagesCodes code = MessagesCodes.NO_ERROR;
        try {
            this.doRetrieve(request, request.getSession(), obj);
        } catch (CaqsException exc) {
            mLog.error("erreur durant recuperation element", exc);
            code = MessagesCodes.CAQS_GENERIC_ERROR;
        }
        this.fillJSONObjectWithReturnCode(obj, code);
        return obj;
    }

    private void retrieveGoals(JSONObject obj, ElementBean elt, Locale loc) {
        FactorSvc factorSvc = FactorSvc.getInstance();
        JSONArray goals = new JSONArray();
        try {
            // get all the relevant goal Ids
            List<FactorBean> result = factorSvc.retrieveFactorList(elt);           
            if (result != null) {

                // populate localized labels
               for (FactorBean fb : result) {
                    fb.setLib(fb.getLib(loc));
                    fb.setDesc(fb.getDesc(loc));
                }

               // then sort by Lib rather than by Id
               Collections.sort(result, new FactorBeanComparator(false, "lib", loc));
               
               // finally output to Json Array
                for (FactorBean fb : result) {
                    JSONObject o = new JSONObject();
                    o.put("id", fb.getId());
                    o.put("lib", fb.getLib());
                    o.put("desc", fb.getDesc());
                    goals.add(o);
                }
            }
        } catch (CaqsException exc) {
            mLog.error("erreur durant recuperation objectifs", exc);
        }
        obj.put("goals", goals);
    }

    private void setLastBaselineFlag(ElementBean eltBean, String idBline, JSONObject obj) {
        boolean notDisplayedBaseline = false;
        if (!idBline.equals(eltBean.getBaseline().getId())) {
            obj.put("currentBaselineLib", eltBean.getBaseline().getLib());
            notDisplayedBaseline = true;
        }
        obj.put("notDisplayedBaseline", notDisplayedBaseline);
    }

    private void updateTabsDatas(ElementBean eltBean, String idBline, JSONObject obj, HttpServletRequest request) {
        //doit-on afficher l'onglet plan d'action
        ActionPlanSvc actionPlanSvc = ActionPlanSvc.getInstance();
        ActionPlanBean ap = actionPlanSvc.getSavedActionPlan(eltBean, eltBean.getBaseline().getId());
        ActionPlanElementBeanCollection<ActionPlanCriterionBean> savedActionPlan = ap.getCorrectedElements();
        BaselineSvc baselineSvc = BaselineSvc.getInstance();
        boolean lastBaseline = baselineSvc.isLastBaseline(idBline, eltBean);
        obj.put("lastBaseline", lastBaseline);
        boolean actionPlanTab = true;
        if (savedActionPlan.isEmpty() && !lastBaseline) {
            actionPlanTab = false;
        }
        obj.put("actionsPlanTab", actionPlanTab);
        boolean firstBaseline = baselineSvc.isFirstBaseline(eltBean.getBaseline().getId(), eltBean.getId());
        obj.put("evolutionTab", (!firstBaseline));
        boolean hasPreviousActionsPlan = false;
        if (!firstBaseline) {
            BaselineBean previousBaseline = baselineSvc.getPreviousBaseline(eltBean.getBaseline(), eltBean.getId());
            if (previousBaseline != null) {
                ActionPlanBean previousActionsPlan = actionPlanSvc.getCompleteActionPlan(eltBean, previousBaseline.getId(), false, request);
                ActionPlanElementBeanCollection<ActionPlanCriterionBean> actionPlanPreviousBaseline = previousActionsPlan.getCorrectedElements();
                hasPreviousActionsPlan = !actionPlanPreviousBaseline.isEmpty();
                //this.setActionPlanApplication(eltBean, actionPlanPreviousBaseline, evolutionSvc, request);
                obj.put("previousBaselineId", previousBaseline.getId());
                obj.put("previousBaselineLib", previousBaseline.getLib());
            }
        }
        obj.put("previousHasActionPlan", hasPreviousActionsPlan);
        obj.put("currentBaselineId", idBline);
        obj.put("currentBaselineLib", eltBean.getBaseline().getLib());
    }

    private void doRetrieve(HttpServletRequest request,
            HttpSession session, JSONObject obj) throws CaqsException {
        String idElt = request.getParameter("idElt");
        String idBline = request.getParameter("idBline");

        ElementSvc elementSvc = ElementSvc.getInstance();
        ElementBean eltBean = elementSvc.retrieveElement(idElt, idBline);
        session.setAttribute(Constants.CAQS_CURRENT_ELEMENT_SESSION_KEY, eltBean);

        if (eltBean != null) {
            obj.put("realBline", eltBean.getBaseline().getId());
            Locale loc = RequestUtil.getLocale(request);
            this.retrieveGoals(obj, eltBean, loc);
            this.setLastBaselineFlag(eltBean, idBline, obj);
            this.updateTabsDatas(eltBean, idBline, obj, request);

            //recuperation de la presence ou non du rapport pour cet element
            //le test n'est pas fait sur le type EA pour le cas ou un rapport pourra etre genere sur tous les types
            Reporter reporter = new Reporter();
            boolean reportAvailable = reporter.reportAlreadyExists(eltBean, RequestUtil.getLocale(request));
            obj.put("reportAvailable", reportAvailable);

            boolean generatingReport = TaskSvc.getInstance().reportGeneratingForElement(eltBean.getId(), idBline);
            obj.put("generatingReport", generatingReport);
            boolean needsRecompute = TaskSvc.getInstance().elementNeedsRecompute(eltBean.getId(), idBline);
            obj.put("needsRecompute", needsRecompute);

            this.retrieveLabel(eltBean, obj, request);

            List<ElementType> elementTypes = elementSvc.retrieveSubElementTypes(idElt);
            session.setAttribute(Constants.CAQS_SUB_ELEMENT_TYPES_SESSION_KEY, elementTypes);
        }
    }

    public void retrieveLabel(ElementBean eltBean, JSONObject retour, HttpServletRequest request) {
        DaoFactory daoFactory = DaoFactory.getInstance();
        LabelDao labelFacade = daoFactory.getLabelDao();
        LabelBean result = labelFacade.retrieveLabelByElement(eltBean);
        if (result != null) {
            retour.put("syntheseLabelStatus", result.getStatut());
            retour.put("syntheseLabelDesc", result.getDesc());
            retour.put("syntheseLabelId", result.getId());
        }
        retour.put("labelled", "" + (result != null));
    }
}
