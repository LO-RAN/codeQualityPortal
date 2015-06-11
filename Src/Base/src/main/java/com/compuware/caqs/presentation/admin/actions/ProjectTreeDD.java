package com.compuware.caqs.presentation.admin.actions;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.compuware.caqs.constants.Constants;
import com.compuware.caqs.domain.dataschemas.UserBean;
import com.compuware.caqs.service.AdminSvc;

public class ProjectTreeDD extends Action {

    public ActionForward execute(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) {
        String son = request.getParameter("source");
        String father = request.getParameter("target");
        this.saveNodeDragDrop(son, father);
        return null;
    }

    private void saveNodeDragDrop(String sonId, String fatherId) {
        AdminSvc adminSvc = AdminSvc.getInstance();

        adminSvc.moveElement(sonId, fatherId);

        //update rights of new ancestors.         
        // go up the tree until we reach the root
        while (!Constants.ENTRYPOINT_ELEMENT_VALUE.equals(fatherId)) {
            // get rights for father
            List<UserBean> fatherRights = adminSvc.retrieveAllUsersByElementId(fatherId);
            // get rights for son
            List<UserBean> sonRights = adminSvc.retrieveAllUsersByElementId(sonId);

            // create a brand new list
            List<String> newRights = new ArrayList<String>();

            // populate it with existing father's rights
            for (Iterator<UserBean> itF = fatherRights.iterator(); itF.hasNext();) {
                UserBean b = itF.next();
                newRights.add(b.getId());
            }

            // and add son's rights if they are not already part of father's rights
            for (Iterator<UserBean> itS = sonRights.iterator(); itS.hasNext();) {
                UserBean b = itS.next();
                if (!this.isContained(fatherRights, b)) {
                    newRights.add(b.getId());
                }
            }

            // update father's rights
            adminSvc.updateRights(fatherId, newRights.toArray(new String[0]));

            // position one level up in the tree for next iteration
            sonId = fatherId;
            fatherId = adminSvc.retrieveFatherElement(fatherId).getId();
        }

    }

    private boolean isContained(List<UserBean> liste, UserBean bean) {
        boolean retour = false;
        if (bean != null) {
            for (Iterator<UserBean> it = liste.iterator(); it.hasNext();) {
                UserBean b = it.next();
                if (b != null && b.equals(bean)) {
                    retour = true;
                    break;
                }
            }
        }

        return retour;
    }
}
