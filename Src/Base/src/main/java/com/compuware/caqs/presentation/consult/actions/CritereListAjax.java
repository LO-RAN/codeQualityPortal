package com.compuware.caqs.presentation.consult.actions;

import com.compuware.caqs.constants.Constants;
import com.compuware.caqs.dao.factory.DaoFactory;
import com.compuware.caqs.dao.interfaces.CriterionDao;
import com.compuware.caqs.domain.dataschemas.CriterionBean;
import com.compuware.caqs.domain.dataschemas.CriterionDefinition;
import com.compuware.caqs.domain.dataschemas.ElementBean;
import com.compuware.caqs.domain.dataschemas.ElementType;
import com.compuware.caqs.domain.dataschemas.JustificationBean;
import com.compuware.caqs.domain.dataschemas.MetriqueDefinitionBean;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

import com.compuware.caqs.presentation.common.actions.ExtJSGridAjaxAction;
import com.compuware.caqs.presentation.consult.forms.ElementCriterionForm;
import com.compuware.caqs.presentation.consult.forms.ElementForm;
import com.compuware.caqs.presentation.consult.forms.JustificationForm;
import com.compuware.caqs.presentation.consult.forms.comparators.ElementCriterionMetricComparator;
import com.compuware.caqs.presentation.util.RequestUtil;
import com.compuware.caqs.presentation.util.StringFormatUtil;
import com.compuware.caqs.service.CriterionDetailSvc;
import com.compuware.caqs.util.RequestHelper;
import java.util.Collections;
import java.util.Iterator;
import java.util.Locale;
import javax.servlet.http.HttpSession;

public class CritereListAjax extends ExtJSGridAjaxAction {

    public JSONObject retrieveDatas(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {

        //ElementCriterionForm
        int startIndex = RequestUtil.getIntParam(request, "start", 0);
        int limitIndex = RequestUtil.getIntParam(request, "limit", 10);

        JSONObject root = new JSONObject();
        List<ElementCriterionForm> elts = null;
        boolean fromCache = RequestUtil.getBooleanParam(request, "fromCache", true);
        if(fromCache) {
            elts = (List<ElementCriterionForm>) request.getSession().getAttribute("critereList");
        } else {
            elts = this.doRetrieve(request, request.getSession());
            request.getSession().setAttribute("critereList", elts);
        }
        int taille = 0;
        int nbElts = 0;
        List<JSONObject> l = new ArrayList<JSONObject>();
        if (elts != null) {
            if (request.getParameter("sort") != null) {
                String sort = request.getParameter("sort");
                String sens = request.getParameter("dir");
                boolean asc = "asc".equals(sens.toLowerCase());
                int metricIndex = 0;
                if (sort.startsWith("met")) {
                    sort = sort.substring(3);
                    metricIndex = Integer.parseInt(sort);
                    sort = "met";
                }
                Collections.sort(elts, new ElementCriterionMetricComparator(sort, metricIndex, asc, RequestUtil.getLocale(request)));
            }
            taille = elts.size();
            
            for (int i = startIndex; i < (startIndex + limitIndex) && i <
                    taille; i++) {
                ElementCriterionForm elt = elts.get(i);
                JSONObject o = this.elementCriterionFormToJSONObject(elt);
                l.add(o);
                nbElts++;
            }
        }
        root.put("criteres", l.toArray(new JSONObject[nbElts]));
        root.put("totalCount", taille);

        return root;
    }

    private JSONObject elementCriterionFormToJSONObject(ElementCriterionForm elt) {
        JSONObject retour = new JSONObject();
        retour.put("critTendance", elt.getTendance());
        retour.put("critTendanceLib", elt.getTendanceLib());
        retour.put("critBNeedJust", elt.getNeedJustification());
        retour.put("hasJust", (elt.getJustificatif() != null));
        retour.put("eltBDesc", elt.getElement().getEscapedDesc(true, false, false));
        retour.put("eltBId", elt.getElement().getId());
        retour.put("eltBLib", elt.getElement().getEscapedLib(true, false, false));
        retour.put("eltBHasSource", elt.getElement().hasSource());
        retour.put("eltBType", elt.getElement().getEscapedType(true, false, false));
        retour.put("eltBStereotype", elt.getElement().getStereotype());
        retour.put("note", elt.getNote());
        if (elt.getJustificatif() != null) {
            retour.put("critBOldNote", elt.getNote());
            retour.put("justDesc", elt.getJustificatif().getEscapedDesc(true, false, false));
            retour.put("justStatus", elt.getJustificatif().getStatus());
            retour.put("justId", elt.getJustificatif().getId());
        }
        if (elt.getMetrics() != null) {
            for (int i = 0; i < elt.getMetrics().length; i++) {
                retour.put("met" + i, elt.getMetrics()[i]);
            }
        }
        return retour;
    }


    private List<ElementCriterionForm> doRetrieve(HttpServletRequest request,
            HttpSession session) {
        List<ElementCriterionForm> retour = null;
        ElementBean eltBean = (ElementBean) session.getAttribute(Constants.CAQS_CURRENT_ELEMENT_SESSION_KEY);
        String idCrit = request.getParameter("idCrit");
        List<CriterionBean> result = null;
        if (idCrit != null) {
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
                retour = createFormList(result, filterTypeElt, request);
            }
        }
        String all = request.getParameter("all");
        if (all == null) {
            all = (String) request.getAttribute("all");
        }
        request.setAttribute("all", all);
        return retour;
    }

    private double getSeuil(String all) {
        double result = 3;
        if (all != null && all.equals("true")) {
            result = 4.01;
        }
        return result;
    }

    private List<CriterionBean> retrieveCritAndMet(HttpServletRequest request,
            HttpSession session,
            CriterionDefinition critDef,
            ElementBean eltBean) {
        List<CriterionBean> result = null;
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

    private List<CriterionBean> retrieveCrit(HttpServletRequest request,
            HttpSession session,
            CriterionDefinition critDef,
            ElementBean eltBean) {
        List<CriterionBean> result = null;
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

    private List<ElementCriterionForm> createFormList(List criterionList, String filterTypeElt, HttpServletRequest request) {
        List<ElementCriterionForm> result = new ArrayList<ElementCriterionForm>();
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
