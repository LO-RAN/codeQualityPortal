package com.compuware.caqs.presentation.consult.actions.evolution;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.MessageResources;

import com.compuware.caqs.domain.dataschemas.FactorBean;
import com.compuware.caqs.presentation.common.actions.ExtJSGridAjaxAction;
import com.compuware.caqs.presentation.util.RequestUtil;
import com.compuware.caqs.presentation.util.StringFormatUtil;
import com.compuware.caqs.service.FactorSvc;
import java.text.ParseException;

/**
 * Liste des evolutions des objectifs.
 * Pour ExtJS.
 * @author cwfr-dzysman
 */
public class FacteurEvolutionListAjaxAction extends ExtJSGridAjaxAction {

    @Override
    public JSONObject retrieveDatas(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {
        String idEa = request.getParameter("id_elt");
        String idBline = request.getParameter("id_bline");
        String idPreviousBline = request.getParameter("id_previous_bline");

        List<FactorBean> goals = FactorSvc.getInstance().retrieveGoalsEvolutions(idEa, idBline, idPreviousBline);

        JSONObject root = new JSONObject();

        int taille = 0;
        List<JSONObject> l = new ArrayList<JSONObject>();

        if (goals != null) {
            taille = goals.size();
            Locale loc = RequestUtil.getLocale(request);

            NumberFormat markFormatter = StringFormatUtil.getMarkFormatter(loc);
            MessageResources messageResources = RequestUtil.getResources(request);

            for (int i = 0; i < taille; i++) {
                FactorBean elt = goals.get(i);
                JSONObject o = this.elementFactorBeanToJSONObject(elt, loc,
                        markFormatter, messageResources);
                l.add(o);
            }

        }
        root.put("totalCount", taille);
        root.put("goals", l.toArray(new JSONObject[l.size()]));

        return root;
    }

    private JSONObject elementFactorBeanToJSONObject(FactorBean fb, Locale loc,
            NumberFormat markFormatter, MessageResources messageResources) {
        JSONObject retour = new JSONObject();
        retour.put("id", fb.getId());
        retour.put("lib", fb.getLib(loc));
        retour.put("desc", fb.getDesc(loc));
        retour.put("compl", fb.getComplement(loc));
        String score = markFormatter.format(fb.getNote() - 0.005);
        String previousScore = markFormatter.format(fb.getTendance() - 0.005);
        retour.put("score", score);
        retour.put("previousScore", previousScore);
        retour.put("doubleScore", fb.getNote() - 0.005);
        retour.put("doublePreviousScore", fb.getTendance() - 0.005);
        //nous faisons ca pour eviter les baisses de notes affichees quand nous avons
        //une baisse due a un arrondi apres la deuxieme decimale
        double parsedScore = 0.0;
        double parsedPreviousScore = 0.0;
        try {
            parsedScore = markFormatter.parse(score).doubleValue();
            parsedPreviousScore = markFormatter.parse(previousScore).doubleValue();
        } catch (ParseException exc) {
            mLog.debug("error while parsing double", exc);
        }
        String tendanceLabel = StringFormatUtil.getTendanceLabel(parsedPreviousScore,
                parsedScore);
        retour.put("trend", tendanceLabel);
        retour.put("trendPopup", messageResources.getMessage(loc, "caqs.critere."
                + tendanceLabel));
        return retour;
    }
}
