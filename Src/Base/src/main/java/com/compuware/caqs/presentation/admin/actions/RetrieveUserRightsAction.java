package com.compuware.caqs.presentation.admin.actions;

import com.compuware.caqs.domain.dataschemas.comparators.AccreditationBeanComparator;
import com.compuware.caqs.domain.dataschemas.rights.AccreditationBean;
import com.compuware.caqs.presentation.common.actions.ExtJSGridAjaxAction;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

import com.compuware.caqs.domain.dataschemas.rights.RightBean;
import com.compuware.caqs.domain.dataschemas.rights.RoleBean;
import com.compuware.caqs.presentation.util.RequestUtil;
import com.compuware.caqs.service.UserRightsSvc;
import java.util.Collections;
import java.util.Map;
import net.sf.json.JSONObject;

public class RetrieveUserRightsAction extends ExtJSGridAjaxAction {

    @Override
    public JSONObject retrieveDatas(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) {
        JSONObject root = new JSONObject();
        //ElementCriterionForm
        int startIndex = RequestUtil.getIntParam(request, "start", 0);
        int limitIndex = RequestUtil.getIntParam(request, "limit", 10);

        List<AccreditationBean> coll = this.retrieveAccreditations();
        if (request.getParameter("sort") != null) {
            String sort = request.getParameter("sort");
            String sens = request.getParameter("dir");
            boolean desc = "desc".equals(sens.toLowerCase());
            Collections.sort(coll, new AccreditationBeanComparator(sort, desc));
        }

        List<JSONObject> l = new ArrayList<JSONObject>();
        root.put("totalCount", coll.size());
        for (int i = startIndex; i < (startIndex + limitIndex) && i <
                coll.size(); i++) {
            AccreditationBean elt = coll.get(i);
            JSONObject o = this.accreditationBeanToJSONObject(elt);
            l.add(o);
        }
        root.put("elements", l.toArray(new JSONObject[0]));
        return root;
    }

    private AccreditationBean getAccreditationBean(List<AccreditationBean> l, String id) {
        AccreditationBean retour = null;
        if (l != null && id != null) {
            for (AccreditationBean b : l) {
                if(id.equals(b.getRightId())) {
                    retour = b;
                    break;
                }
            }
        }
        return retour;
    }

    private List<AccreditationBean> retrieveAccreditations() {
        List<AccreditationBean> retour = new ArrayList<AccreditationBean>();
        List<RightBean> rightsList = UserRightsSvc.getInstance().getAllAccessRights();
        List<RoleBean> roleList = UserRightsSvc.getInstance().getAllCaqsRoles();

        for (RightBean rb : rightsList) {
            for (RoleBean role : roleList) {
                AccreditationBean ab = this.getAccreditationBean(retour, rb.getId());
                if(ab == null) {
                    ab = new AccreditationBean();
                    ab.setRightId(rb.getId());
                    ab.setRightLib(rb.getLib());
                    retour.add(ab);
                }
                ab.getRights().put(role.getId(), new Boolean(UserRightsSvc.getInstance().isAssociated(role, rb)));
            }
        }
        return retour;
    }

    private JSONObject accreditationBeanToJSONObject(AccreditationBean elt) {
        JSONObject retour = new JSONObject();
        retour.put("id", elt.getRightId());
        retour.put("rightName", elt.getRightLib());
        Map<String, Boolean> map = elt.getRights();
        for(Map.Entry<String, Boolean> entry : map.entrySet()) {
            retour.put(entry.getKey(), entry.getValue().toString());
        }
        return retour;
    }
}
