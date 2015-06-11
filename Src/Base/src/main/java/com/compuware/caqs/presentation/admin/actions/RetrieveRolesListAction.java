package com.compuware.caqs.presentation.admin.actions;

import com.compuware.caqs.domain.dataschemas.rights.RoleBean;
import com.compuware.caqs.presentation.common.ExtJSAjaxAction;
import com.compuware.caqs.presentation.util.RequestUtil;
import com.compuware.caqs.service.UserSvc;
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
public class RetrieveRolesListAction extends ExtJSAjaxAction {

    @Override
    protected JSONObject retrieveDatas(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {

        JSONObject retour = new JSONObject();
        Locale loc = RequestUtil.getLocale(request);
        MessageResources resources = RequestUtil.getResources(request);
        List<RoleBean> collection = null;
        collection = UserSvc.getInstance().retrieveAllRoles();


        List<JSONObject> liste = new ArrayList<JSONObject>();
        JSONObject obj = null;
        //on ajoute la possibilite de filtrer sur tous les outils
        for (RoleBean role : collection) {
            obj = new JSONObject();
            obj.put("id", role.getId());
            obj.put("lib", role.getLib());
            liste.add(obj);
        }

        this.putArrayIntoObject(JSONArray.fromObject(liste.toArray(new JSONObject[0])), retour);

        return retour;
    }
}
