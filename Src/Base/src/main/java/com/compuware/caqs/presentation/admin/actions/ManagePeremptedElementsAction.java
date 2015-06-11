/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.compuware.caqs.presentation.admin.actions;

import com.compuware.caqs.constants.Constants;
import com.compuware.caqs.constants.MessagesCodes;
import com.compuware.caqs.domain.dataschemas.ElementLinked;
import com.compuware.caqs.domain.dataschemas.tasks.TaskId;
import com.compuware.caqs.presentation.common.actions.ExtJSGridAjaxAction;
import com.compuware.caqs.presentation.util.CaqsUtils;
import com.compuware.caqs.presentation.util.RequestUtil;
import com.compuware.caqs.security.auth.Users;
import com.compuware.caqs.service.ElementSvc;
import com.compuware.caqs.service.messages.MessagesSvc;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.sf.json.JSONObject;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.MessageResources;

/**
 *
 * @author cwfr-dzysman
 */
public class ManagePeremptedElementsAction extends ExtJSGridAjaxAction {

    @Override
    public JSONObject retrieveDatas(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) {
        Users user = RequestUtil.getConnectedUser(request);
        JSONObject object = new JSONObject();
        MessageResources resources = RequestUtil.getResources(request);
        Locale loc = RequestUtil.getLocale(request);
        MessagesCodes retour = MessagesCodes.NO_ERROR;
        String userId = RequestUtil.getConnectedUserId(request);

        String action = request.getParameter("action");
        if (action != null) {
            if ("delete".equals(action)) {
                String[] ids = null;
                String emptyTrash = request.getParameter("emptyTrash");
                boolean bEmptyTrash = false;
                if (emptyTrash != null) {
                    bEmptyTrash = Boolean.parseBoolean(emptyTrash);
                }
                if (bEmptyTrash) {
                    List<ElementLinked> elts = null;
                    if (user != null && (user.access("ALL_PROJECT_ADMIN"))) {
                        elts = ElementSvc.getInstance().retrieveAllPeremptedRootsElements();
                    } else {
                        elts = ElementSvc.getInstance().retrieveAllPeremptedRootsElements(user.getId());
                    }
                    List<String> lIds = new ArrayList<String>();
                    if (elts != null) {
                        for (ElementLinked el : elts) {
                            if (CaqsUtils.recycleBinElementIsDeletable(el)) {
                                lIds.add(el.getId());
                            }
                        }
                    }
                    ids = lIds.toArray(new String[0]);
                } else {
                    String allIds = request.getParameter("allIds");
                    ids = allIds.split(",");
                }
                for (int i = 0; (i < ids.length) && (retour ==
                        MessagesCodes.NO_ERROR); i++) {
                    //retour = ElementSvc.getInstance().deletePeremptedElement(ids[i]);
                    //on place dans la file l'element
                    MessagesSvc.getInstance().addMessage(TaskId.DELETE_ELEMENTS,
                            ids[i], Constants.TASK_ON_ALL_BASELINES, userId, null,
                            null, null);
                }
            } else if ("restore".equals(action)) {
                String allIds = request.getParameter("allIds");
                String[] ids = allIds.split(",");
                for (int i = 0; (i < ids.length) && (retour ==
                        MessagesCodes.NO_ERROR); i++) {
                    retour = ElementSvc.getInstance().restoreAllPeremptedElementsTree(ids[i]);
                }
            }
        }

        this.fillJSONObjectWithReturnCode(object, retour);
       return object;
    }
}
