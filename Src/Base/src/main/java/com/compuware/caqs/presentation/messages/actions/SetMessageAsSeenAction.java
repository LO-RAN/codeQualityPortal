package com.compuware.caqs.presentation.messages.actions;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

import com.compuware.caqs.presentation.util.RequestUtil;
import com.compuware.caqs.service.messages.MessagesSvc;
import net.sf.json.JSONObject;

public class SetMessageAsSeenAction extends MessagesAction {
	
	public JSONObject retrieveDatas(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) {
		boolean setAllMessagesAsSeen = false;
		String allMessages = request.getParameter("allMessages");
		if("true".equals(allMessages)) {
			setAllMessagesAsSeen = true;
		}
		
		String idMess = request.getParameter("id_mess");
		MessagesSvc mlm = MessagesSvc.getInstance();
		if(setAllMessagesAsSeen) {
			mlm.setAllMessageAsSeen(RequestUtil.getConnectedUserId(request));
		} else {
			mlm.setMessageAsSeen(idMess);
		}
		
		JSONObject object = new JSONObject();
		this.retrieveMessagesForUserIntoResponse(RequestUtil.getConnectedUserId(request), request, response, object, false);
		return object;
	}
}
