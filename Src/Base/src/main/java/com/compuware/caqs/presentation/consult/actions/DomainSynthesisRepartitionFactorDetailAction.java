package com.compuware.caqs.presentation.consult.actions;

import com.compuware.caqs.business.util.StringFormatUtil;
import com.compuware.caqs.domain.dataschemas.ElementBean;
import com.compuware.caqs.domain.dataschemas.FactorBean;
import com.compuware.caqs.presentation.common.actions.ExtJSGridAjaxAction;
import com.compuware.caqs.presentation.util.RequestUtil;
import com.compuware.caqs.service.FactorSvc;
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
public class DomainSynthesisRepartitionFactorDetailAction extends ExtJSGridAjaxAction {

    @Override
    public JSONObject retrieveDatas(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {
        JSONObject object = null;
        String factorId = request.getParameter("factorId");
        List<ElementBean> projects = (List<ElementBean>) request.getSession().getAttribute("domainSynthesisProjectsList");
        if (factorId != null && projects != null) {
            NumberFormat markFormatter = StringFormatUtil.getMarkFormatter(RequestUtil.getLocale(request));

            object = this.retrieveRepartitionForFactor(projects,
                    factorId, markFormatter, request);
        }

        return object;
    }

    /**
     * creates a factor dispatching for projects included in the list provided as parameter
     * @param projects
     */
    private JSONObject retrieveRepartitionForFactor(List<ElementBean> projects,
            String idFact, NumberFormat markFormatter, HttpServletRequest request) {
        MessageResources resources = RequestUtil.getResources(request);

        List<JSONObject> objects = new ArrayList<JSONObject>();

        Locale locale = RequestUtil.getLocale(request);

        for (ElementBean project : projects) {
            FactorBean fb = FactorSvc.getInstance().retrieveFactorAndJustByIdElementBaseline(
                    project.getBaseline().getId(),
                    project.getProject().getId(),
                    project.getId(), idFact);
            if (fb != null) {
                double mark = fb.getNote();
                String groupLib = "";
                if (mark < 2.0) {
                    groupLib = "reject";
                } else if (mark < 3.0) {
                    groupLib = "reserve";
                } else if (mark < 4.0) {
                    groupLib = "accepte";
                } else {
                    groupLib = "quatre";
                }
                this.addJSONObjectsToList(project,
                        "caqs.domainsynthese.repartition.factorsdetails."+groupLib,
                        resources, mark, objects, markFormatter, locale);
            }
        }

        JSONObject retour = new JSONObject();
        retour.put("totalCount", objects.size());
        retour.put("projects", JSONArray.fromObject(objects.toArray(new JSONObject[0])));
        return retour;
    }

    private void addJSONObjectsToList(ElementBean project,
            String groupLib, MessageResources resources, double mark,
            List<JSONObject> objects, NumberFormat markFormatter, Locale locale) {
        JSONObject object = new JSONObject();
        object.put("lib", project.getLib());
        object.put("desc", project.getDesc());
        object.put("id", project.getId());
        object.put("groupLib", resources.getMessage(locale, groupLib));
        object.put("mark", markFormatter.format(mark - 0.005));
        objects.add(object);
    }
}
