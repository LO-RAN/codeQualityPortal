package com.compuware.caqs.presentation.consult.actions.dashboard;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

import com.compuware.caqs.domain.dataschemas.ElementType;
import com.compuware.caqs.domain.dataschemas.dashboard.DashboardElementBean;
import com.compuware.caqs.presentation.common.ExtJSAjaxAction;
import com.compuware.caqs.presentation.util.RequestUtil;

public class RetrieveElementInfosAction extends ExtJSAjaxAction {

    @Override
    public JSONObject retrieveDatas(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {
        JSONObject retour = new JSONObject();
        String idElt = request.getParameter("idElt");

        if (idElt != null && !"".equals(idElt)) {
            DashboardElementBean elt = null;
            List<DashboardElementBean> elts = (List<DashboardElementBean>) request.getSession().getAttribute("dashboardElementsBean");
            if (elts != null) {
                for (DashboardElementBean e : elts) {
                    if (e.getId().equals(idElt)) {
                        elt = e;
                        break;
                    }
                }
            }
            if (elt != null) {
                ResourceBundle resources = RequestUtil.getCaqsResourceBundle(request);
                if (elt != null) {
                    String datePattern = resources.getString("caqs.dateFormat.withHour");
                    SimpleDateFormat sdf = new SimpleDateFormat(datePattern);
                    retour.put("meteoImg", elt.getMeteo());
                    retour.put("meteoTooltip", elt.getMeteoTooltip());
                    retour.put("eltDesc", elt.getDesc());
                    retour.put("eltLib", elt.getLib());
                    retour.put("eltId", elt.getId());
                    retour.put("prjId", elt.getProject().getId());
                    retour.put("prjLib", elt.getProject().getLib());
                    retour.put("telt", elt.getTypeElt());
                    retour.put("libTelt", new ElementType(elt.getTypeElt()).getLib(RequestUtil.getLocale(request)));
                    retour.put("blineLib", elt.getBaseline().getLib());
                    retour.put("dmaj", sdf.format(elt.getBaseline().getDmaj()));
                }
            }
        }
        return retour;
    }
}
