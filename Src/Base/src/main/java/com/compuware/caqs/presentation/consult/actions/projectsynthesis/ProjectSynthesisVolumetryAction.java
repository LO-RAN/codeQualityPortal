package com.compuware.caqs.presentation.consult.actions.projectsynthesis;

import com.compuware.caqs.business.util.StringFormatUtil;
import com.compuware.caqs.constants.Constants;
import com.compuware.caqs.domain.dataschemas.BaselineBean;
import com.compuware.caqs.domain.dataschemas.ElementBean;
import com.compuware.caqs.domain.dataschemas.calcul.Volumetry;
import com.compuware.caqs.domain.dataschemas.comparators.VolumetryComparator;
import com.compuware.caqs.presentation.common.actions.ExtJSGridAjaxAction;
import com.compuware.caqs.presentation.util.RequestUtil;
import com.compuware.caqs.service.BaselineSvc;
import com.compuware.caqs.service.ElementSvc;
import com.compuware.caqs.service.SyntheseSvc;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

/**
 *
 * @author cwfr-dzysman
 */
public class ProjectSynthesisVolumetryAction extends ExtJSGridAjaxAction {

    @Override
    public JSONObject retrieveDatas(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {
        JSONObject object = new JSONObject();
        
        Locale locale = RequestUtil.getLocale(request);
        NumberFormat nf = StringFormatUtil.getIntegerFormatter(locale);

        ElementBean eltBean = (ElementBean) request.getSession().getAttribute(Constants.CAQS_CURRENT_ELEMENT_SESSION_KEY);
        List<ElementBean> elts = ElementSvc.getInstance().retrieveAllApplicationEntitiesForProjectAndBaseline(eltBean.getProject(), eltBean.getBaseline());

        if (elts != null) {
            List<Volumetry> globalVolumetry = new ArrayList<Volumetry>();
            for (ElementBean ea : elts) {
                List<Volumetry> l = SyntheseSvc.getInstance().retrieveVolumetry(ea);
                this.addAllVolumetryInformation(globalVolumetry, l);
            }

            if (request.getParameter("sort") != null) {
                String sort = request.getParameter("sort");
                String sens = request.getParameter("dir");
                boolean desc = "desc".equals(sens.toLowerCase());
                Collections.sort(globalVolumetry, new VolumetryComparator(sort, desc, locale));
            }

            List<JSONObject> liste = new ArrayList<JSONObject>();

            for (Volumetry vol : globalVolumetry) {
                JSONObject obj = new JSONObject();
                obj.put("idTelt", vol.getIdTElt());
                obj.put("libTelt", vol.getElementType().getLib(locale));
                obj.put("nbElts", nf.format(vol.getTotal()));
                obj.put("nbCrees", nf.format(vol.getCreated()));
                obj.put("nbSupp", nf.format(vol.getDeleted()));
                liste.add(obj);
            }

            JSONArray array = JSONArray.fromObject(liste.toArray(new JSONObject[0]));
            object.put("volumetries", array);
            object.put("totalCount", array.size());
            object.put("root", "volumetries");
            object.put("id", "idTelt");
        }

        return object;
    }

    private void addVolumetryInformation(List<Volumetry> globalVolumetry, Volumetry volumetry) {
        boolean added = false;
        for (Volumetry vol : globalVolumetry) {
            if (vol.getIdTElt().equals(volumetry.getIdTElt())) {
                added = true;
                vol.add(volumetry);
                break;
            }
        }
        if (!added) {
            globalVolumetry.add(volumetry);
        }
    }

    private void addAllVolumetryInformation(List<Volumetry> globalVolumetry, List<Volumetry> volumetry) {
        for (Volumetry vol : volumetry) {
            this.addVolumetryInformation(globalVolumetry, vol);
        }
    }
}
