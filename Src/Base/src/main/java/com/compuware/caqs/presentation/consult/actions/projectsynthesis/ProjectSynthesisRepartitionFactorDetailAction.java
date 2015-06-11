package com.compuware.caqs.presentation.consult.actions.projectsynthesis;

import com.compuware.caqs.business.util.StringFormatUtil;
import com.compuware.caqs.constants.Constants;
import com.compuware.caqs.domain.dataschemas.ElementBean;
import com.compuware.caqs.domain.dataschemas.FactorBean;
import com.compuware.caqs.presentation.common.actions.ExtJSGridAjaxAction;
import com.compuware.caqs.presentation.util.RequestUtil;
import com.compuware.caqs.service.ElementSvc;
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
public class ProjectSynthesisRepartitionFactorDetailAction extends ExtJSGridAjaxAction {

    @Override
    public JSONObject retrieveDatas(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {
        JSONObject object = null;
        ElementBean eltBean = (ElementBean) request.getSession().getAttribute(Constants.CAQS_CURRENT_ELEMENT_SESSION_KEY);
        String factorId = request.getParameter("factorId");
        List<ElementBean> elts = ElementSvc.getInstance().retrieveAllApplicationEntitiesForProjectAndBaseline(eltBean.getProject(), eltBean.getBaseline());
        if (factorId != null && elts != null) {
            NumberFormat markFormatter = StringFormatUtil.getMarkFormatter(RequestUtil.getLocale(request));

            object = this.retrieveRepartitionForFactor(elts,
                    factorId, markFormatter, request);
        }

        return object;
    }

    /**
     * creates a factor dispatching for projects included in the list provided as parameter
     * @param projects
     */
    private JSONObject retrieveRepartitionForFactor(List<ElementBean> eas,
            String idFact, NumberFormat markFormatter, HttpServletRequest request) {
        MessageResources resources = RequestUtil.getResources(request);

        List<JSONObject> objects = new ArrayList<JSONObject>();

        Locale locale = RequestUtil.getLocale(request);

        for (ElementBean ea : eas) {
            FactorBean fb = FactorSvc.getInstance().retrieveFactorAndJustByIdElementBaseline(
                    ea.getBaseline().getId(),
                    ea.getProject().getId(),
                    ea.getId(), idFact);
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
                this.addJSONObjectsToList(ea,
                        "caqs.domainsynthese.repartition.factorsdetails."+groupLib,
                        resources, mark, objects, markFormatter, locale);
            }
        }

        JSONObject retour = new JSONObject();
        retour.put("totalCount", objects.size());
        retour.put("elements", JSONArray.fromObject(objects.toArray(new JSONObject[0])));
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
        object.put("score", markFormatter.format(mark - 0.005));
        objects.add(object);
    }
}
