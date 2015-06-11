/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.compuware.caqs.presentation.consult.actions;

import com.compuware.caqs.business.consult.Synthese;
import com.compuware.caqs.business.util.StringFormatUtil;
import com.compuware.caqs.domain.dataschemas.ElementBean;
import com.compuware.caqs.domain.dataschemas.FactorBean;
import com.compuware.caqs.domain.dataschemas.FactorNoteRepartition;
import com.compuware.caqs.presentation.common.actions.ExtJSGridAjaxAction;
import com.compuware.caqs.presentation.util.RequestUtil;
import java.text.MessageFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
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
 *
 * @author cwfr-dzysman
 */
public class DomainSynthesisRepartitionGridAction extends ExtJSGridAjaxAction {

    public JSONObject retrieveDatas(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {
        JSONObject object = null;
        String domainId = request.getParameter("domainId");
        List<ElementBean> projects = (List<ElementBean>) request.getSession().getAttribute("domainSynthesisProjectsList");
        if (domainId != null && projects != null) {
            object = this.retrieveFactorRepartition(projects, request);
        }

        return object;
    }

    /**
     * creates a factor dispatching for projects included in the list provided as parameter
     * @param projects
     */
    private JSONObject retrieveFactorRepartition(List<ElementBean> projects,
            HttpServletRequest request) {
        Locale loc = RequestUtil.getLocale(request);
        MessageResources resources = RequestUtil.getResources(request);
        Synthese synthese = new Synthese();

        List<FactorBean> factors = new ArrayList<FactorBean>();
        for (ElementBean project : projects) {
            factors.addAll(synthese.retrieveKiviat(project, project.getBaseline().getId()));
        }

        List<FactorNoteRepartition> repartition = new ArrayList<FactorNoteRepartition>();
        for (FactorBean factor : factors) {
            FactorNoteRepartition rep = this.getRepartitionForFactor(factor.getId(),
                    repartition);
            double note = factor.getNote();
            if (note < 2.0) {
                rep.add(0, 1);
            } else if (note < 3.0) {
                rep.add(1, 1);
            } else if (note < 4.0) {
                rep.add(2, 1);
            } else {
                rep.add(3, 1);
            }
        }
        List<JSONObject> objects = new ArrayList<JSONObject>();
        String tooltipMessage = resources.getMessage(loc, "caqs.facteursynthese.popup");

        NumberFormat markFormatter = StringFormatUtil.getMarkFormatter(loc);

        for (FactorNoteRepartition rep : repartition) {
            JSONObject object = new JSONObject();
            object.put("lib", rep.getLib(loc));
            object.put("desc", rep.getDesc(loc));
            object.put("compl", rep.getComplement(loc));
            object.put("pct1", rep.getPct(0));
            object.put("pct2", rep.getPct(1));
            object.put("pct3", rep.getPct(2));
            object.put("pct4", rep.getPct(3));
            object.put("id", rep.getId());
            String format = MessageFormat.format(tooltipMessage,
                    new Object[]{rep.getValue(0), rep.getPct(0)});
            object.put("tooltip1", format);
            format = MessageFormat.format(tooltipMessage,
                    new Object[]{rep.getValue(1), rep.getPct(1)});
            object.put("tooltip2", format);
            format = MessageFormat.format(tooltipMessage,
                    new Object[]{rep.getValue(2), rep.getPct(2)});
            object.put("tooltip3", format);
            format = MessageFormat.format(tooltipMessage,
                    new Object[]{rep.getValue(3), rep.getPct(3)});
            object.put("tooltip4", format);
            int total = rep.getValue(0) + rep.getValue(1) + rep.getValue(2) + rep.getValue(3);
            int ok = rep.getValue(2) + rep.getValue(3);
            double percentageOk = (((double) ok) * 100.0) / ((double) total);
            object.put("percentageOk", Double.toString(percentageOk));
            object.put("formattedPercentageOk", markFormatter.format(percentageOk));
            objects.add(object);
        }
        JSONObject retour = new JSONObject();
        retour.put("totalCount", objects.size());
        retour.put("factors", JSONArray.fromObject(objects.toArray(new JSONObject[0])));
        return retour;
    }

    private FactorNoteRepartition getRepartitionForFactor(String idFact, List<FactorNoteRepartition> repartition) {
        FactorNoteRepartition retour = null;
        for (FactorNoteRepartition rep : repartition) {
            if (rep.getId().equals(idFact)) {
                retour = rep;
                break;
            }
        }
        if (retour == null) {
            retour = new FactorNoteRepartition();
            retour.setId(idFact);
            repartition.add(retour);
        }
        return retour;
    }
}
