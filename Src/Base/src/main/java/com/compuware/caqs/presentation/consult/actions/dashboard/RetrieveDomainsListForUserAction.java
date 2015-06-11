package com.compuware.caqs.presentation.consult.actions.dashboard;

import com.compuware.caqs.constants.Constants;
import com.compuware.caqs.domain.dataschemas.ElementBean;
import com.compuware.caqs.domain.dataschemas.ElementType;
import com.compuware.caqs.presentation.common.ExtJSAjaxAction;
import com.compuware.caqs.presentation.util.RequestUtil;
import com.compuware.caqs.service.ElementSvc;
import java.util.Collection;
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
public class RetrieveDomainsListForUserAction extends ExtJSAjaxAction {

    @Override
    public JSONObject retrieveDatas(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {
        JSONObject retour = new JSONObject();
        String userId = RequestUtil.getConnectedUserId(request);

        if (userId != null && !"".equals(userId)) {
            MessageResources resources = RequestUtil.getResources(request);
            Locale loc = RequestUtil.getLocale(request);
            Collection<ElementBean> domains = ElementSvc.getInstance().retrieveElements(userId, ElementType.DOMAIN);
            JSONArray array = new JSONArray();
            JSONObject obj = new JSONObject();
            obj.put("id", Constants.DASHBOARD_ALL_DOMAINS);
            obj.put("lib", resources.getMessage(loc, "caqs.dashboard.alldomains"));
            array.add(obj);
            if (domains != null) {
                for (ElementBean elt : domains) {
                    String parentPath = ElementSvc.getInstance().retrieveParentPathByLib(elt.getId());
                    obj = new JSONObject();
                    obj.put("id", elt.getId());
                    String fullPath = parentPath;
                    if(!Constants.ELEMENT_PATH_SEPARATOR.equals(fullPath)) {
                        fullPath += Constants.ELEMENT_PATH_SEPARATOR;
                    }
                    fullPath += elt.getLib();
                    obj.put("lib", fullPath);
                    array.add(obj);
                }
            }
            this.putArrayIntoObject(array, retour);
        }
        return retour;
    }
}
