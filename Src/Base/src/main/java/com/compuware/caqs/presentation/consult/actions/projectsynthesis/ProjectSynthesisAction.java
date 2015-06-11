package com.compuware.caqs.presentation.consult.actions.projectsynthesis;

import com.compuware.caqs.business.locking.LockManager;
import com.compuware.caqs.business.locking.Locks;
import com.compuware.caqs.business.report.Reporter;
import com.compuware.caqs.constants.Constants;
import com.compuware.caqs.constants.MessagesCodes;
import com.compuware.caqs.dao.factory.DaoFactory;
import com.compuware.caqs.dao.interfaces.LabelDao;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.sf.json.JSONObject;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

import com.compuware.caqs.domain.dataschemas.ElementBean;
import com.compuware.caqs.domain.dataschemas.LabelBean;
import com.compuware.caqs.domain.dataschemas.actionplan.ActionPlanBean;
import com.compuware.caqs.domain.dataschemas.actionplan.ActionPlanCriterionBean;
import com.compuware.caqs.domain.dataschemas.actionplan.ActionPlanPriority;
import com.compuware.caqs.domain.dataschemas.actionplan.list.ActionPlanElementBeanCollection;
import com.compuware.caqs.domain.dataschemas.list.FactorBeanCollection;
import com.compuware.caqs.exception.CaqsException;
import com.compuware.caqs.presentation.common.ExtJSAjaxAction;
import com.compuware.caqs.presentation.util.RequestUtil;
import com.compuware.caqs.security.auth.Users;
import com.compuware.caqs.service.ActionPlanSvc;
import com.compuware.caqs.service.BaselineSvc;
import com.compuware.caqs.service.ElementSvc;
import com.compuware.caqs.service.TaskSvc;
import javax.servlet.http.HttpSession;
import net.sf.json.JSONArray;

/**
 * Accede a la synthese d'un projet
 * @author cwfr-dzysman
 */
public class ProjectSynthesisAction extends ExtJSAjaxAction {


    private void retrieveProjectGlobalSynthesis(HttpServletRequest request,
            ElementBean eltBean, JSONObject retour) {
        String idBline = eltBean.getBaseline().getId();
        if (eltBean != null) {
            retour.put("telt", eltBean.getTypeElt());
            retour.put("isEA", false);
            //recuperation de la presence ou non du rapport pour cet element
            //le test n'est pas fait sur le type EA pour le cas ou un rapport pourra etre genere sur tous les types
            Reporter reporter = new Reporter();
            boolean reportAvailable = reporter.reportAlreadyExists(eltBean, RequestUtil.getLocale(request));
            retour.put("reportAvailable", reportAvailable);

            //recuperation du label
            retrieveLabel(eltBean, retour, request);

            boolean generatingReport = TaskSvc.getInstance().reportGeneratingForElement(eltBean.getId(), idBline);
            retour.put("generatingReport", generatingReport);
            boolean needsRecompute = TaskSvc.getInstance().elementNeedsRecompute(eltBean.getId(), idBline);
            retour.put("needsRecompute", needsRecompute);

            BaselineSvc baselineSvc = BaselineSvc.getInstance();
            boolean lastBaseline = baselineSvc.isLastBaseline(idBline, eltBean);
            retour.put("lastBaseline", lastBaseline);

            //doit-on afficher l'onglet plan d'action
            ActionPlanBean ap = ActionPlanSvc.getInstance().getSavedActionPlan(eltBean, eltBean.getBaseline().getId());
            ActionPlanElementBeanCollection<ActionPlanCriterionBean> savedActionPlan = ap.getCorrectedElements();
            boolean actionPlanTab = true;
            if (savedActionPlan.isEmpty() && !lastBaseline) {
                actionPlanTab = false;
            }
            retour.put("actionPlanTab", actionPlanTab);

            this.retrieveActionPlanPriorities(retour, request);
        }
    }

    private void retrieveActionPlanPriorities(JSONObject retour, HttpServletRequest request) {
        JSONArray array = new JSONArray();
        ActionPlanPriority[] priorites = ActionPlanPriority.getValidValues();
        for (int i = 0; i < priorites.length; i++) {
            JSONObject obj = new JSONObject();
            obj.put("idPriority", priorites[i].toString());
            obj.put("lib", priorites[i].toString(request));
            array.add(obj);
        }
        retour.put("priorities", array);
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

    private void retrieveActionPlanDatas(ElementBean eltBean, String idBline, HttpServletRequest request, JSONObject obj) {
        HttpSession session = request.getSession();
        session.removeAttribute("ActionPlanAllCriterions");

        //on commence par recuperer les criteres
        ActionPlanSvc actionPlanSvc = ActionPlanSvc.getInstance();

        BaselineSvc baselineSvc = BaselineSvc.getInstance();
        boolean readOnly = !baselineSvc.isLastBaseline(idBline, eltBean);
        Users user = RequestUtil.getConnectedUser(request);
        String idUser = user.getId();

        if (!readOnly) {
            /*if (!LockManager.getInstance().hasAccess(Locks.ACTION_PLAN_MODIFICATION, idUser, eltBean.getId(), user.getCookie())) {
                readOnly = true;
                obj.put("concurrentModification", "true");
            }*/
        }

        boolean hasAccess = user.access("ACTION_PLAN_EDITION");
        if (!hasAccess) {
            readOnly = true;
        } else if (!readOnly && hasAccess) {
            LockManager.getInstance().setLock(Locks.ACTION_PLAN_MODIFICATION, idUser, eltBean.getId(), user.getCookie());
        }
        obj.put("actionPlanreadOnly", readOnly);
        session.setAttribute(Constants.ACTION_PLAN_READ_ONLY, Boolean.valueOf(readOnly));

        //recuperation des donnees calculees pour le kiviat

        FactorBeanCollection real = actionPlanSvc.getRealKiviatDatas(eltBean, idBline, request);
        if (!real.isEmpty()) {
            FactorBeanCollection factors = new FactorBeanCollection(real);
            session.setAttribute("factors", factors);

            //doit-on afficher l'onglet plan d'action
            ActionPlanBean ap = actionPlanSvc.getSavedActionPlan(eltBean, eltBean.getBaseline().getId());
            ActionPlanElementBeanCollection<ActionPlanCriterionBean> savedActionPlan = ap.getCorrectedElements();
            boolean lastBaseline = baselineSvc.isLastBaseline(idBline, eltBean);
            boolean actionPlanTab = true;
            if (savedActionPlan.isEmpty() && !lastBaseline) {
                actionPlanTab = false;
            }
            obj.put("actionPlanTab", "" + actionPlanTab);
            obj.put("noProjectSynthesis", false);
        } else {
            obj.put("noProjectSynthesis", true);
        }
    }

    @Override
    protected JSONObject retrieveDatas(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        MessagesCodes retour = MessagesCodes.CAQS_GENERIC_ERROR;
        JSONObject obj = new JSONObject();
        String idElt = request.getParameter("projectId");
        String idBline = request.getParameter("idBline");
        if (idElt != null && idBline != null) {
            try {
                ElementSvc elementSvc = ElementSvc.getInstance();
                ElementBean eltBean = elementSvc.retrieveElement(idElt, idBline);
                request.getSession().setAttribute(Constants.CAQS_CURRENT_ELEMENT_SESSION_KEY, eltBean);
                this.retrieveActionPlanDatas(eltBean, idBline, request, obj);
                this.retrieveProjectGlobalSynthesis(request, eltBean, obj);
                retour = MessagesCodes.NO_ERROR;
            } catch (CaqsException exc) {
                mLog.error("erreur durant recuperation project "+idElt+" pour bline "+idBline, exc);
            }
        }
        this.fillJSONObjectWithReturnCode(obj, retour);
        return obj;
    }
}
