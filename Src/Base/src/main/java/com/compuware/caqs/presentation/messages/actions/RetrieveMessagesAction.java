package com.compuware.caqs.presentation.messages.actions;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

import com.compuware.caqs.presentation.util.RequestUtil;
import com.compuware.caqs.security.auth.Users;
import net.sf.json.JSONObject;

public class RetrieveMessagesAction extends MessagesAction {

    public JSONObject retrieveDatas(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) {

        Users user = RequestUtil.getConnectedUser(request);

        JSONObject object = new JSONObject();
        this.retrieveMessagesForUserIntoResponse(user.getId(), request, response, object, true);
        return object;
    }
}
