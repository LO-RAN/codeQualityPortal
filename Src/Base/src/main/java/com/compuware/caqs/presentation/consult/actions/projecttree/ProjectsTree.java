package com.compuware.caqs.presentation.consult.actions.projecttree;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

import com.compuware.caqs.domain.dataschemas.ElementLinked;
import com.compuware.caqs.domain.dataschemas.ElementType;
import com.compuware.caqs.presentation.common.ExtJSAjaxAction;
import com.compuware.caqs.presentation.common.ExtJSUtils;
import com.compuware.caqs.presentation.util.RequestUtil;
import com.compuware.caqs.security.auth.Users;
import com.compuware.caqs.service.ElementSvc;
import java.util.List;

public class ProjectsTree extends ExtJSAjaxAction {

    private JSONArray getElementChildrenListJSON(List<ElementLinked> elts, String parentId, boolean projectsTree) {
        JSONObject[] objects = new JSONObject[elts.size()];
        int i = 0;
        for (ElementLinked bean : elts) {
            objects[i] = new JSONObject();
            String id = bean.getId();
            if (bean.isSymbolicLink()) {
                id += "-link";
            }
            objects[i].put("id", parentId + id);
            objects[i].put("realId", bean.getId());
            objects[i].put("text", bean.getLib());
            String idPro = (bean.getProject() != null) ? bean.getProject().getId() : "";
            String teltLeaf = (projectsTree) ? ElementType.PRJ : ElementType.EA;
            boolean leaf = teltLeaf.equals(bean.getTypeElt());
            objects[i].put("leaf", leaf);
            if (bean.getDesc() != null && !"".equals(bean.getDesc())) {
                objects[i].put("qtip", bean.getDesc());
            }
            objects[i].put("telt", bean.getTypeElt());
            objects[i].put("idPro", idPro);
            //isLink permettra de gérer plus tard si l'element est cliquable dans l'arborescence ou non
            objects[i].put("isLink", new Boolean(true));
            objects[i].put("linkType", bean.getLinkType());
            String cls = ExtJSUtils.getIconClassForTelt(bean.getTypeElt(),
                    bean.isSymbolicLink());
            objects[i].put("iconCls", cls);
            i++;
        }
        return JSONArray.fromObject(objects);
    }

    public JSONObject retrieveDatas(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) {
        JSONObject retour = new JSONObject();
        ElementSvc elementSvc = ElementSvc.getInstance();

        boolean projectsTree = (request.getParameter("projectsTree") != null);
        String idElt = request.getParameter("id_elt");
        String completeId = request.getParameter("completeId");

        Users user = RequestUtil.getConnectedUser(request);
        List<ElementLinked> eltColl = null;
        if (user != null) {
            if (projectsTree) {
                eltColl = elementSvc.retrieveAllChildrenElements(idElt, user.getId());
            } else {
                String idPro = request.getParameter("id_pro");
                String idBline = request.getParameter("id_bline");
                eltColl = elementSvc.retrieveProjectArboElementsOneLevel(idPro, idBline, idElt, user.getId());
            }
        }

        if (eltColl != null) {
            JSONArray array = this.getElementChildrenListJSON(eltColl, completeId, projectsTree);
            this.putArrayIntoObject(array, retour);
        }

        return retour;
    }
}
