package com.compuware.caqs.presentation.admin.actions;

import com.compuware.caqs.domain.dataschemas.ElementLinked;
import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

import com.compuware.caqs.domain.dataschemas.ElementType;
import com.compuware.caqs.presentation.common.ExtJSAjaxAction;
import com.compuware.caqs.presentation.common.ExtJSUtils;
import com.compuware.caqs.presentation.util.RequestUtil;
import com.compuware.caqs.security.auth.Users;
import com.compuware.caqs.service.ElementSvc;

public class ProjectTree extends ExtJSAjaxAction {

    private JSONArray getElementChildrenListJSON(Collection<ElementLinked> elts) {
        JSONObject[] objects = new JSONObject[elts.size()];

        int i = 0;
        for (ElementLinked bean : elts) {
            objects[i] = new JSONObject();
            String id = bean.getId();
            if (bean.isSymbolicLink()) {
                id += "-link";
            }
            objects[i].put("id", id);
            objects[i].put("realId", bean.getId());
            objects[i].put("text", bean.getLib());
            boolean isLeaf = new Boolean(ElementType.EA.equals(bean.getTypeElt())) ||
                    bean.isSymbolicLink();
            objects[i].put("leaf", isLeaf);
            objects[i].put("linkType", bean.getLinkType());
            String cls = ExtJSUtils.getIconClassForTelt(bean.getTypeElt(),
                    bean.isSymbolicLink());
            objects[i].put("iconCls", cls);
            if (ElementType.EA.equals(bean.getTypeElt())) {
                objects[i].put("allowDrop", "false");
            }
            if (bean.getDesc() != null && !"".equals(bean.getDesc())) {
                objects[i].put("qtip", bean.getDesc());
            }

            boolean draggable = true;
            if (ElementType.EA.equals(bean.getTypeElt()) ||
                    ElementType.SSP.equals(bean.getTypeElt())) {
                draggable = !ElementSvc.getInstance().elementHasBaselines(bean.getId());
            }
            objects[i].put("draggable", draggable);
            objects[i].put("events", "{move:nodeMoved}");
            objects[i].put("telt", bean.getTypeElt());
            String idPro = (bean.getProject()!=null) ? bean.getProject().getId() : "";
            objects[i].put("idpro", idPro);
            i++;
        }
        return JSONArray.fromObject(objects);
    }

    public JSONObject retrieveDatas(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) {
        String idElt = request.getParameter("id_elt");

        Users user = RequestUtil.getConnectedUser(request);

        Collection<ElementLinked> projectColl = null;

        if (user != null && (user.access("ALL_PROJECT_ADMIN"))) {
            projectColl = ElementSvc.getInstance().retrieveAllChildrenElements(idElt);
        } else if (user != null && user.access("PROJECT_ADMIN")) {
            projectColl = ElementSvc.getInstance().retrieveAllChildrenElements(idElt, user.getId());
        }

        JSONObject obj = null;

        if (projectColl != null) {
            JSONArray array = this.getElementChildrenListJSON(projectColl);
            obj = this.putArrayIntoObject(array, obj);
        }

        return obj;
    }
}
