package com.compuware.caqs.presentation.consult.actions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.compuware.caqs.constants.Constants;
import com.compuware.caqs.dao.factory.DaoFactory;
import com.compuware.caqs.dao.interfaces.CriterionDao;
import com.compuware.caqs.domain.dataschemas.CriterionBean;
import com.compuware.caqs.domain.dataschemas.CriterionDefinition;
import com.compuware.caqs.domain.dataschemas.ElementBean;
import com.compuware.caqs.domain.dataschemas.ElementType;
import com.compuware.caqs.domain.dataschemas.JustificationBean;
import com.compuware.caqs.domain.dataschemas.MetriqueDefinitionBean;
import com.compuware.caqs.presentation.consult.forms.ElementCriterionForm;
import com.compuware.caqs.presentation.consult.forms.ElementForm;
import com.compuware.caqs.presentation.consult.forms.JustificationForm;
import com.compuware.caqs.presentation.util.RequestUtil;
import com.compuware.caqs.presentation.util.StringFormatUtil;
import com.compuware.caqs.service.CriterionDetailSvc;
import com.compuware.caqs.util.RequestHelper;
import com.compuware.toolbox.util.logging.LoggerManager;

public final class CritereSelectAction extends ElementSelectedActionAbstract {

    // --------------------------------------------------------- Public Methods
    protected void sortAsc(List coll, String property) {
        BeanComparator naturalOrderBeanComparator = new BeanComparator(property);
        Collections.sort(coll, naturalOrderBeanComparator);
    }

    public ActionForward doExecute(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response)
            throws IOException, ServletException {

        HttpSession session = request.getSession();

        LoggerManager.pushContexte("CritereSelect");

        ActionForward forward = null;

        String full = request.getParameter("full");

        if ("true".equals(full)) {
            List formList = (List) session.getAttribute("critereList");
            if (formList != null) {
                this.sortAsc(formList, "element.lib");
                request.setAttribute("critereList", formList);
            }
        } else {
            forward = doRetrieve(mapping, request, session);
        }

        LoggerManager.popContexte();

        if (full != null && full.equals("true")) {
            request.setAttribute("full", full);
        }

        if (request.getParameter("vi") != null) {
            forward = mapping.findForward("successVersionImprimable");
        }

        if (forward == null) {
            forward = mapping.findForward("success");
        }

        return forward;

    }

    private ActionForward doRetrieve(ActionMapping mapping,
            HttpServletRequest request,
            HttpSession session) {
        ElementBean eltBean = (ElementBean) session.getAttribute(Constants.CAQS_CURRENT_ELEMENT_SESSION_KEY);
        ActionForward forward = null;
        String idCrit = request.getParameter("id_crit");
        if (idCrit == null) {
            idCrit = (String) request.getAttribute("id_crit");
        }

        List result = null;
        if (idCrit != null) {
            request.setAttribute("idCrit", idCrit);
            DaoFactory daoFactory = DaoFactory.getInstance();
            CriterionDao criterionFacade = daoFactory.getCriterionDao();
            CriterionDefinition critDef = criterionFacade.retrieveCriterionDefinitionByKey(idCrit, eltBean.getUsage().getId());
            String filterTypeElt = adaptTypeElt(critDef, RequestHelper.retrieve(request, session, "typeElt", "ALL"));

            if (critDef != null) {
                if (critDef.getMetriquesDefinitions() != null &&
                        (critDef.getMetriquesDefinitions().size() > 0) &&
                        (critDef.getIdTElt().equals(filterTypeElt) ||
                        filterTypeElt.equals("ALL"))) {
                    result = retrieveCritAndMet(request, session, critDef, eltBean);
                } else {
                    result = retrieveCrit(request, session, critDef, eltBean);
                }
                session.setAttribute("criterionDefinition", critDef);
                List formList = createFormList(result, filterTypeElt, request);
                request.setAttribute("critereList", formList);
                session.setAttribute("critereList", formList);
            }
        }
        String all = request.getParameter("all");
        if (all == null) {
            all = (String) request.getAttribute("all");
        }
        request.setAttribute("all", all);

        forward = mapping.findForward("success");

        return forward;
    }

    private double getSeuil(String all) {
        double result = 3;
        if (all != null && all.equals("true")) {
            result = 4.01;
        }
        return result;
    }

    private List retrieveCritAndMet(HttpServletRequest request,
            HttpSession session,
            CriterionDefinition critDef,
            ElementBean eltBean) {
        List result = null;
        String filterDesc = RequestHelper.retrieve(request, session, "filter", "%");
        session.setAttribute("filter", filterDesc);
        String filterTypeElt = adaptTypeElt(critDef, RequestHelper.retrieve(request, session, "typeElt", "ALL"));
        session.setAttribute("typeElt", filterTypeElt);
        String all = RequestHelper.retrieve(request, session, "all", "false");
        double seuil = getSeuil(all);
        CriterionDetailSvc criterionDetailSvc = CriterionDetailSvc.getInstance();
        if (!critDef.getIdTElt().equals(ElementType.EA)) {
            result = criterionDetailSvc.retrieveCriterionDetailsForSubElts(eltBean, critDef, seuil, filterDesc, filterTypeElt);
        } else {
            result = criterionDetailSvc.retrieveCriterionDetailsForElts(eltBean, critDef, seuil, filterDesc, "ALL");
        }

        return result;
    }

    private List retrieveCrit(HttpServletRequest request,
            HttpSession session,
            CriterionDefinition critDef,
            ElementBean eltBean) {
        List result = null;
        String filterDesc = RequestHelper.retrieve(request, session, "filter", "%");
        session.setAttribute("filter", filterDesc);
        String filterTypeElt = adaptTypeElt(critDef, RequestHelper.retrieve(request, session, "typeElt", "ALL"));
        session.setAttribute("typeElt", filterTypeElt);
        String all = RequestHelper.retrieve(request, session, "all", "false");
        double seuil = getSeuil(all);
        CriterionDetailSvc criterionDetailSvc = CriterionDetailSvc.getInstance();
        if (!critDef.getIdTElt().equals(ElementType.EA)) {
            result = criterionDetailSvc.retrieveCriterionDetailsNoMetForSubElts(eltBean, critDef, seuil, filterDesc, filterTypeElt);
        } else {
            result = criterionDetailSvc.retrieveCriterionDetailsNoMetForElts(eltBean, critDef, seuil, filterDesc, "ALL");
        }

        return result;
    }

    private String adaptTypeElt(CriterionDefinition critDef, String filterTypeElt) {
        String result = filterTypeElt;
        if (filterTypeElt != null && filterTypeElt.equals("AUTO")) {
            if (critDef != null && critDef.getIdTElt() != null) {
                result = critDef.getIdTElt();
            }
        }
        return result;
    }

    private List createFormList(List criterionList, String filterTypeElt, HttpServletRequest request) {
        List result = new ArrayList();
        Locale locale = RequestUtil.getLocale(request);
        Iterator i = criterionList.iterator();
        while (i.hasNext()) {
            CriterionBean crit = (CriterionBean) i.next();
            result.add(beanToForm(crit, filterTypeElt, locale, request));
        }
        return result;
    }

    private ElementCriterionForm beanToForm(CriterionBean bean, String filterTypeElt, Locale locale, HttpServletRequest request) {
        ElementCriterionForm result = null;
        if (bean != null) {
            result = new ElementCriterionForm();
            result.setElement(beanToForm(bean.getElement()));
            result.setMetrics(beanToForm(bean, filterTypeElt));
            result.setNote(StringFormatUtil.decimalFormat(bean.getNoteOrJustNote()));
            result.setNeedJustification((bean.getNote() < 3) &&
                    (bean.getNote() > 0));
            result.setJustificatif(beanToForm(bean.getJustificatif()));
            String tendanceStr = StringFormatUtil.getTendanceLabel(bean.getTendance(), bean.getNoteOrJustNote());
            result.setTendance(tendanceStr);
            String tendanceLib = this.getResources(request).getMessage(locale, "caqs.critere." +
                    tendanceStr);
            result.setTendanceLib(tendanceLib);
        }
        return result;
    }

    private ElementForm beanToForm(ElementBean bean) {
        ElementForm result = null;
        if (bean != null) {
            result = new ElementForm();
            result.setId(bean.getId());
            result.setLib(bean.getLib());
            result.setDesc(bean.getDesc());
            result.setType(bean.getTypeElt());
            result.setStereotype(bean.getStereotype());
            result.setHasSource(bean.hasSource());
        }
        return result;
    }

    private JustificationForm beanToForm(JustificationBean bean) {
        JustificationForm result = null;
        if (bean != null) {
            result = new JustificationForm();
            result.setId(bean.getId());
            result.setLib(bean.getLib());
            result.setDesc(bean.getDesc());
            result.setStatus(bean.getStatut());
            result.setNote(bean.getNote());
        }
        return result;
    }

    private String[] beanToForm(CriterionBean crit, String currentTypeElt) {
        String[] result = null;
        CriterionDefinition critDef = crit.getCriterionDefinition();
        if (critDef != null) {
            List<MetriqueDefinitionBean> metricDef = critDef.getMetriquesDefinitions();
            if (metricDef != null &&
                    (critDef.getIdTElt().equals(currentTypeElt) ||
                    currentTypeElt.equals("ALL"))) {
                result = new String[metricDef.size()];
                int idx = 0;
                Iterator<MetriqueDefinitionBean> metIter = metricDef.iterator();
                while (metIter.hasNext()) {
                    MetriqueDefinitionBean def = metIter.next();
                    Double valbrute = crit.getValbrute(def.getId());
                    if (valbrute != null) {
                        result[idx] = StringFormatUtil.decimalFormat(valbrute.doubleValue());
                    } else {
                        result[idx] = null;
                    }
                    idx++;
                }
            }
        }
        return result;
    }

}
