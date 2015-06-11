package com.compuware.caqs.presentation.consult.actions;

import com.compuware.caqs.business.util.StringFormatUtil;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONObject;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

import com.compuware.caqs.constants.Constants;
import com.compuware.caqs.domain.dataschemas.BottomUpSummaryBean;
import com.compuware.caqs.domain.dataschemas.ElementBean;
import com.compuware.caqs.domain.dataschemas.ElementType;
import com.compuware.caqs.domain.dataschemas.FilterBean;
import com.compuware.caqs.domain.dataschemas.MetriqueBean;
import com.compuware.caqs.domain.dataschemas.comparators.BottomUpSummaryBeanComparator;
import com.compuware.caqs.presentation.common.actions.ExtJSGridAjaxAction;
import com.compuware.caqs.presentation.util.RequestUtil;
import com.compuware.caqs.service.BottomUpSyntheseSvc;

public class BottomUpSyntheseListAjaxAction extends ExtJSGridAjaxAction {

    public JSONObject retrieveDatas(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {
        JSONObject root = new JSONObject();
        HttpSession session = request.getSession();
        ElementBean eltBean = (ElementBean) session.getAttribute(Constants.CAQS_CURRENT_ELEMENT_SESSION_KEY);

        int startIndex = RequestUtil.getIntParam(request, "start", 0);
        int limitIndex = RequestUtil.getIntParam(request, "limit", 100);

        String sOnlyProblematics = request.getParameter("onlyProb");
        boolean onlyProblematics = "ok".equals(sOnlyProblematics);

        if (eltBean != null) {
            String filterDesc = request.getParameter("filter");
            String filterTypeElt = request.getParameter("typeElt");
            if (filterTypeElt == null) {
                filterTypeElt = ElementType.ALL;
            }
            FilterBean filter = new FilterBean(filterDesc, filterTypeElt);
            request.getSession().setAttribute("filter", filterDesc);
            request.getSession().setAttribute("typeElt", filterTypeElt);

            List<BottomUpSummaryBean> result = null;

            BottomUpSyntheseSvc bottomUpSyntheseSvc = BottomUpSyntheseSvc.getInstance();
            if (eltBean.getTypeElt().equals(ElementType.EA)) {
                result = retrieveSummaryForEa(eltBean, filter, bottomUpSyntheseSvc);
            } else {
                result = retrieveRecursiveSummary(eltBean, filter, bottomUpSyntheseSvc);
            }

            if (result != null) {
                Collections.sort(result);
                session.setAttribute("syntheseBottomUp", result);

                if (onlyProblematics) {
                    //nous voulons afficher les 100 elements les plus probl�matiques
                    List<BottomUpSummaryBean> l = new ArrayList<BottomUpSummaryBean>();
                    int tailleMax = result.size();
                    for (int i = 0; i < 100 && i < tailleMax; i++) {
                        l.add(result.get(i));
                    }
                    result = l;
                }

                if (limitIndex == -1) {
                    limitIndex = result.size();
                }

                Locale loc = RequestUtil.getLocale(request);

                if (request.getParameter("sort") != null) {
                    String sort = request.getParameter("sort");
                    String sens = request.getParameter("dir");
                    boolean desc = "desc".equals(sens.toLowerCase());
                    Collections.sort(result, new BottomUpSummaryBeanComparator(desc, sort, loc));
                }

                root.put("totalCount", result.size());
                List<JSONObject> l = new ArrayList<JSONObject>();

                NumberFormat nf = StringFormatUtil.getIntegerFormatter(loc);

                for (int i = startIndex; i < (startIndex + limitIndex) && i <
                        result.size(); i++) {
                    BottomUpSummaryBean elt = result.get(i);
                    JSONObject o = this.elementBottomUpSummaryBeanToJSONObject(elt, eltBean.getBaseline().getId(), nf, session, loc);
                    l.add(o);
                }

                root.put("elements", l.toArray(new JSONObject[0]));
            }

        }
        return root;
    }

    private List<BottomUpSummaryBean> retrieveRecursiveSummary(ElementBean eltBean, FilterBean filter, BottomUpSyntheseSvc bottomUpSyntheseSvc) {
        List<BottomUpSummaryBean> result = new ArrayList<BottomUpSummaryBean>();
        List<ElementBean> allEa = bottomUpSyntheseSvc.retrieveRecursiveSubElements(eltBean);
        for (ElementBean bean : allEa) {
            bean.setBaseline(eltBean.getBaseline());
            bean.setProject(eltBean.getProject());
            result.addAll(retrieveSummaryForEa(bean, filter, bottomUpSyntheseSvc));
        }
        return result;
    }

    private List<BottomUpSummaryBean> retrieveSummaryForEa(ElementBean eltBean, FilterBean filter, BottomUpSyntheseSvc bottomUpSyntheseSvc) {
        List<BottomUpSummaryBean> result = null;
        result = bottomUpSyntheseSvc.retrieveCriterionBottomUpSummary(eltBean, filter);
        return result;
    }

    private JSONObject elementBottomUpSummaryBeanToJSONObject(BottomUpSummaryBean elt, String idBline,
            NumberFormat nf, HttpSession session, Locale loc) {
        JSONObject retour = new JSONObject();
        retour.put("eltDesc", elt.getElement().getDesc());
        retour.put("eltId", elt.getElement().getId());
        retour.put("eltLib", elt.getElement().getLib());
        retour.put("eltHasSource", elt.getElement().hasSource());
        retour.put("eltIdBline", idBline);
        retour.put("note1", nf.format(elt.getNote(0)));
        retour.put("note2", nf.format(elt.getNote(1)));
        retour.put("note3", nf.format(elt.getNote(2)));
        retour.put("note4", nf.format(elt.getNote(3)));

        MetriqueBean[] additionnalMetrics = (MetriqueBean[]) session.getAttribute("additionnalMetrics");
        if (additionnalMetrics != null && additionnalMetrics.length > 0) {
            for (int addIdx = 0; addIdx < additionnalMetrics.length; addIdx++) {
                String addMetId = additionnalMetrics[addIdx].getId();
                retour.put("idGridCol" + additionnalMetrics[addIdx].getLib(loc), nf.format(elt.getAdditionnalMetric(addMetId)));
            }
        }
        return retour;
    }
}
