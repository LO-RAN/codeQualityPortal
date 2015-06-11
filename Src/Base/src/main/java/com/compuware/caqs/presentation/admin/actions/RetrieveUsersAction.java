package com.compuware.caqs.presentation.admin.actions;

import com.compuware.caqs.constants.MessagesCodes;
import com.compuware.caqs.domain.dataschemas.UserBean;
import com.compuware.caqs.domain.dataschemas.comparators.UserBeanComparator;
import com.compuware.caqs.presentation.modeleditor.common.RetrieveSearchScreenResultsAction;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

import com.compuware.caqs.presentation.util.RequestUtil;
import com.compuware.caqs.service.UserSvc;
import net.sf.json.JSONObject;

public class RetrieveUsersAction extends RetrieveSearchScreenResultsAction {

    public JSONObject retrieveDatas(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) {
        JSONObject retour = new JSONObject();
        String id = this.retrieveSearchParameter("id", true, request);
        String lastname = this.retrieveSearchParameter("lastname", true, request);

        List<UserBean> collection = UserSvc.getInstance().retrieveUsersByIdLastname(id, lastname);
        if (request.getParameter("sort") != null) {
            String sort = request.getParameter("sort");
            String sens = request.getParameter("dir");
            boolean desc = "desc".equals(sens.toLowerCase());
            UserBeanComparator comparator = new UserBeanComparator(sort, desc);
            this.sort(collection, comparator);
        }
        MessagesCodes codeRetour = MessagesCodes.NO_ERROR;
        if (collection == null) {
            codeRetour = MessagesCodes.DATABASE_ERROR;
        } else {
            retour.put("totalCount", collection.size());
            int startIndex = RequestUtil.getIntParam(request, "start", 0);
            int limitIndex = RequestUtil.getIntParam(request, "limit", 10);
            List<JSONObject> l = new ArrayList<JSONObject>();
            for (int i = startIndex; i < (startIndex + limitIndex) && i <
                    collection.size(); i++) {
                UserBean langue = collection.get(i);
                JSONObject obj = this.convertToJSONObject(langue);
                l.add(obj);
            }
            retour.put("datas", l.toArray(new JSONObject[0]));
        }
        this.fillJSONObjectWithReturnCode(retour, codeRetour);
        return retour;
    }

    private JSONObject convertToJSONObject(UserBean user) {
        JSONObject retour = new JSONObject();
        retour.put("id", user.getId());
        retour.put("lastname", user.getLastName());
        retour.put("firstname", user.getFirstName());
        retour.put("email", user.getEmail());
        retour.put("roles", user.getRoles());
        return retour;
    }
}
