package com.compuware.caqs.presentation.consult.actions;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONObject;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

import com.compuware.caqs.constants.Constants;
import com.compuware.caqs.domain.dataschemas.BottomUpDetailBean;
import com.compuware.caqs.domain.dataschemas.CriterionFactorWeightBean;
import com.compuware.caqs.domain.dataschemas.CriterionPerFactorBean;
import com.compuware.caqs.domain.dataschemas.ElementBean;
import com.compuware.caqs.domain.dataschemas.comparators.CriterionPerFactorBeanComparator;
import com.compuware.caqs.presentation.common.actions.ExtJSGridAjaxAction;
import com.compuware.caqs.presentation.util.RequestUtil;
import com.compuware.caqs.presentation.util.StringFormatUtil;
import com.compuware.caqs.service.CriterionSvc;
import com.compuware.caqs.service.EvolutionSvc;
import com.compuware.toolbox.util.logging.LoggerManager;

public class BottomUpDetailListAction extends ExtJSGridAjaxAction {

    private static org.apache.log4j.Logger logger = LoggerManager.getLogger(Constants.LOG4J_IHM_LOGGER_KEY);

    public JSONObject retrieveDatas(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {
        ActionErrors errors = new ActionErrors();

        JSONObject root = new JSONObject();
        BottomUpDetailBean elt = doRetrieve(mapping, request, request.getSession(), errors, response);

        List<JSONObject> l = new ArrayList<JSONObject>();
        int taille = 0;
        if (elt != null) {
            java.util.List critList = elt.getCriterions();

            if (request.getParameter("sort") != null) {
                String sort = request.getParameter("sort");
                String sens = request.getParameter("dir");
                boolean desc = "desc".equals(sens.toLowerCase());
                Collections.sort(critList, new CriterionPerFactorBeanComparator(desc, sort, RequestUtil.getLocale(request)));
            }

            taille = critList.size();
            Locale loc = RequestUtil.getLocale(request);
            ResourceBundle resource = RequestUtil.getCaqsResourceBundle(request);
            for (int i = 0; i < taille; i++) {
                CriterionPerFactorBean crit = (CriterionPerFactorBean) critList.get(i);
                JSONObject o = this.elementCriterionFormToJSONObject(crit, loc, resource);
                l.add(o);
            }
        }
        root.put("totalCount", taille);
        root.put("criteres", l.toArray(new JSONObject[0]));

        return root;
    }

    private BottomUpDetailBean doRetrieve(ActionMapping mapping,
            HttpServletRequest request,
            HttpSession session,
            ActionErrors errors,
            HttpServletResponse response) {
        ElementBean eltBean = (ElementBean) session.getAttribute(Constants.CAQS_CURRENT_ELEMENT_SESSION_KEY);
        String idElt = request.getParameter("id_elt");
        return retrieveDetail(mapping, request, session, errors, response, eltBean, idElt);
    }

    public BottomUpDetailBean retrieveDetail(ActionMapping mapping,
            HttpServletRequest request,
            HttpSession session,
            ActionErrors errors,
            HttpServletResponse response,
            ElementBean eltBean,
            String idElt) {
        BottomUpDetailBean retour = null;
        List<BottomUpDetailBean> liste = null;
        String idPrevBline = request.getParameter("idPreviousBline");
        if(idPrevBline == null || "".equals(idPrevBline)) {
            liste = CriterionSvc.getInstance().retrieveCriterionBottomUpDetail(eltBean.getBaseline().getId(), idElt, eltBean.getBaseline().getProject().getId());
        } else {
            liste = EvolutionSvc.getInstance().retrieveCriterionBottomUpDetail(eltBean.getBaseline().getId(), idPrevBline, idElt, eltBean.getBaseline().getProject().getId());
        }
        if (liste != null && !liste.isEmpty()) {
            retour = liste.get(0);
            java.util.List critList = retour.getCriterions();
            for (Iterator it = critList.iterator(); it.hasNext();) {
                CriterionPerFactorBean b = (CriterionPerFactorBean) it.next();
                if (b.getNote() == null) {
                    it.remove();
                }
            }
        }
        return retour;
    }

    private JSONObject elementCriterionFormToJSONObject(CriterionPerFactorBean elt, Locale loc, ResourceBundle resource) {
        NumberFormat nf = StringFormatUtil.getMarkFormatter(loc);

        JSONObject retour = new JSONObject();
        retour.put("id", elt.getId());
        retour.put("lib", elt.getEscapedLib(false, false, false, loc));
        retour.put("desc", elt.getEscapedDesc(false, false, false, loc));
        retour.put("compl", elt.getEscapedComplement(false, false, false, loc));
        retour.put("note", elt.getNote());
        retour.put("formattedNote", (elt.getNote() != null) ? nf.format(elt.getNote()) : "");
        retour.put("formattedJustNote", nf.format(elt.getJustNote()));
        retour.put("justNote", elt.getJustNote());
        retour.put("tendance", elt.getTendance());
        retour.put("hasJust", (elt.getJustificatif() != null));

        double note = (elt.getNote() != null) ? elt.getNote().doubleValue() : 0.0;
        double tendance = elt.getTendance();

        String tendanceLabel = StringFormatUtil.getTendanceLabel(tendance, note);

        String popup = "caqs.critere." + tendanceLabel;

        retour.put("tendanceMsg", resource.getString(popup));
        retour.put("tendanceLabel", tendanceLabel);
        if (elt.getJustificatif() != null) {
            retour.put("critBOldNote", elt.getNote());
            retour.put("justDesc", elt.getJustificatif().getEscapedDesc(true, false, false));
            retour.put("justStatus", elt.getJustificatif().getStatut());
            retour.put("justId", elt.getJustificatif().getId());
        }

        retour.put("objectifs", this.elementGoalFormToJSONString(elt, loc, resource));

        return retour;
    }

    private String elementGoalFormToJSONString(CriterionPerFactorBean crit, Locale loc, ResourceBundle resource) {
        NumberFormat nfpct = StringFormatUtil.getIntegerFormatter(loc);

        NumberFormat nfweight = StringFormatUtil.getFormatter(loc, 1, 1);
        String objectif = "";
        List factList = crit.getFactors();
        java.util.Iterator itFact = factList.iterator();
        boolean firstFact = true;
        while (itFact.hasNext()) {
            CriterionFactorWeightBean fact = (CriterionFactorWeightBean) itFact.next();
            if (!firstFact) {
                objectif += "<BR />";
            }
            objectif += fact.getLib(loc) + " - "
                    + resource.getString("caqs.bottomupdetail.poids") + " : "
                    + nfweight.format(fact.getWeight()) + " ("
                    + nfpct.format(fact.getPctWeight()) + ")";
            firstFact = false;
        }
        return objectif;
    }
}
