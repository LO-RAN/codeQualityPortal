/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.compuware.caqs.presentation.consult.actions.dashboard;

import com.compuware.caqs.constants.Constants;
import com.compuware.caqs.domain.dataschemas.ElementType;
import com.compuware.caqs.domain.dataschemas.FactorBean;
import com.compuware.caqs.domain.dataschemas.comparators.DashboardElementBeanComparator;
import com.compuware.caqs.domain.dataschemas.dashboard.DashboardElementBean;
import com.compuware.caqs.domain.dataschemas.settings.Settings;
import com.compuware.caqs.presentation.common.ExtJSAjaxAction;
import com.compuware.caqs.presentation.util.RequestUtil;
import com.compuware.caqs.presentation.util.StringFormatUtil;
import com.compuware.caqs.security.auth.Users;
import com.compuware.caqs.service.DashboardSvc;
import com.compuware.caqs.service.SettingsSvc;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.MessageResources;

/**
 * renvoie les projets pour le dashboard.
 * ne prend pas en compte une limite ==> pas de pagination.
 * @author cwfr-dzysman
 */
public class RetrieveProjectsAction extends ExtJSAjaxAction {

    @Override
    public JSONObject retrieveDatas(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) {
        Locale loc = RequestUtil.getLocale(request);
        MessageResources ms = RequestUtil.getResources(request);
        JSONObject root = new JSONObject();
        //ElementCriterionForm
        boolean fromCache = true;
        String sFromCache = request.getParameter("fromCache");
        if (sFromCache != null) {
            fromCache = Boolean.parseBoolean(sFromCache);
        }

        NumberFormat nf = StringFormatUtil.getIntegerFormatter(loc);

        String sNbDaysLastAnalysisBeforeWarning = SettingsSvc.getInstance().getPreferenceForUser(
                Settings.LAST_ANALYSIS_DATE_WARNING, RequestUtil.getConnectedUserId(request));
        int nbDaysLastAnalysisBeforeWarning = 0;

        try {
            nbDaysLastAnalysisBeforeWarning = Integer.parseInt(sNbDaysLastAnalysisBeforeWarning);
        } catch (NumberFormatException e) {
            mLog.error("Impossible to format the NbDaysLastAnalysisBeforeWarning preference : "
                    + sNbDaysLastAnalysisBeforeWarning + " : " + e.getMessage());
        }


        List<DashboardElementBean> coll = null;

        if (fromCache) {
            coll = (List<DashboardElementBean>) request.getSession().getAttribute("dashboardElementsBean");
        }

        if (coll == null) {
            String domainId = request.getParameter("domainId");
            String rootId = "ENTRYPOINT";
            if (domainId != null && !Constants.DASHBOARD_ALL_DOMAINS.equals(domainId)) {
                rootId = domainId;
            }
            Users user = RequestUtil.getConnectedUser(request);
            String userId = user.getId();
            if (user.isInRole(new String[]{"ADMINISTRATOR"})) {
                userId = null;
            }

            coll = DashboardSvc.getInstance().retrieveAllElementsForDashboard(ElementType.PRJ,
                        ms, loc, userId, rootId, request);
            request.getSession().setAttribute("dashboardElementsBean", coll);
        }

        if (coll != null) {
            NumberFormat markFormatter = StringFormatUtil.getMarkFormatter(loc);
            if (request.getParameter("sort") != null) {
                String sort = request.getParameter("sort");
                String sens = request.getParameter("dir");
                boolean desc = "desc".equals(sens.toLowerCase());
                Collections.sort(coll, new DashboardElementBeanComparator(sort, desc));
            }
            String datePattern = ms.getMessage(loc, "caqs.dateFormat.withHour");
            SimpleDateFormat sdf = new SimpleDateFormat(datePattern);

            List<JSONObject> l = new ArrayList<JSONObject>();
            root.put("totalCount", coll.size());
            for (int i = 0; i < coll.size(); i++) {
                DashboardElementBean elt = coll.get(i);
                JSONObject o = this.dashboardBeanToJSONObject(elt, sdf, nf, ms, loc, markFormatter);
                o.put("dmajJSParser", ms.getMessage(loc, "caqs.dateFormat.withHour.js"));
                o.put("nbLastDaysWarning", nbDaysLastAnalysisBeforeWarning);
                l.add(o);
            }
            root.put("elements", l.toArray(new JSONObject[0]));
        }
        return root;
    }

    /**
     * converti un DashboardElementBean en JSONObject
     * @param elt l'element a convertir
     * @param sdf le formatage des dates
     * @param nf le formatage des nombres entiers
     * @param resources resources
     * @param loc locale
     * @param scoreFormatter formatage des notes
     * @return le jsonobject representation le dashboardelementbean
     */
    private JSONObject dashboardBeanToJSONObject(DashboardElementBean elt,
            SimpleDateFormat sdf, NumberFormat nf, MessageResources resources, Locale loc,
            NumberFormat scoreFormatter) {
        JSONObject retour = new JSONObject();
        retour.put("meteoImg", elt.getMeteo());
        retour.put("meteoTooltip", elt.getMeteoTooltip());
        retour.put("eltDesc", elt.getDesc());
        retour.put("lib", elt.getLib());
        retour.put("eltId", elt.getId());
        retour.put("prjId", elt.getProject().getId());
        retour.put("prjLib", elt.getProject().getLib());
        retour.put("blineLib", elt.getBaseline().getLib());
        retour.put("goalsAvg", elt.getGoalsAverage());
        retour.put("formattedGoalsAvg", StringFormatUtil.getMarkFormatter(loc).format(elt.getGoalsAverage()));
        retour.put("dmaj", sdf.format(elt.getBaseline().getDmaj()));
        SimpleDateFormat sortable = new SimpleDateFormat("yyyy/MM/DD HH:mm");
        retour.put("sortableDmaj", sortable.format(elt.getBaseline().getDmaj()));
        String tendanceLabel = StringFormatUtil.getTendanceLabel(elt.getPreviousGoalsAverage(), elt.getGoalsAverage());
        retour.put("tendanceLabel", tendanceLabel);
        retour.put("tendancePopup", resources.getMessage(loc, "caqs.trend."
                + tendanceLabel));
        retour.put("nbLOC", nf.format(elt.getNbLOC()));
        retour.put("nbLOCWithoutFormat", elt.getNbLOC());
        retour.put("nbFileElements", nf.format(elt.getNbFileElements()));
        retour.put("nbFileElementsWithoutFormat", elt.getNbFileElements());
        long nbDays = ((new Date().getTime()
                - elt.getBaseline().getDmaj().getTime()) / 86400000);
        String nbDaysLastAnalysisBeforeWarningPopup = resources.getMessage(loc,
                "caqs.dashboard.nbLastDaysWarning.popup", nbDays);
        retour.put("nbLastDaysWarningPopup", nbDaysLastAnalysisBeforeWarningPopup);

        JSONArray goals = new JSONArray();
        List<String> currentGoalsIds = elt.getCurrentGoalsIds();
        for (String s : currentGoalsIds) {
            FactorBean fb = new FactorBean();
            fb.setId(s);
            JSONObject obj = new JSONObject();
            obj.put("goalLib", fb.getLib(loc));
            obj.put("goalDesc", fb.getDesc(loc));
            obj.put("goalTrend", elt.getTrendForGoal(s));
            obj.put("goalScore", scoreFormatter.format(elt.getCurrentScoreForGoal(s)));
            obj.put("goalTrendPopup", resources.getMessage(loc, "caqs.trend."
                    + elt.getTrendForGoal(s)));
            goals.add(obj);
        }
        retour.put("goals", goals);
        return retour;
    }
}
