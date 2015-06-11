package com.compuware.caqs.presentation.architecture.actions.projecttree;

import com.compuware.caqs.presentation.util.RequestUtil;

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
            /*if(projectsTree) {
            objects[i].put("href", "javascript:clickOnTree('"+bean.getId()+
            "', '"+bean.getTypeElt()+"', '"+id_pro+"');");
            } else {
            if(ElementType.EA.equals(bean.getTypeElt())) {
            objects[i].put("href", "javascript:selectElementOnProjectTree('"+bean.getId()+"', true);");
            }
            }*/
            objects[i].put("idPro", idPro);
            String teltLeaf = (projectsTree) ? ElementType.PRJ : ElementType.EA;
            objects[i].put("isLink", new Boolean(teltLeaf.equals(bean.getTypeElt())));
            objects[i].put("leaf", new Boolean(teltLeaf.equals(bean.getTypeElt())));
            objects[i].put("telt", bean.getTypeElt());
            objects[i].put("linkType", bean.getLinkType());
            String cls = ExtJSUtils.getIconClassForTelt(bean.getTypeElt(),
                    bean.isSymbolicLink());
            objects[i].put("iconCls", cls);
            if (bean.getDesc() != null && !"".equals(bean.getDesc())) {
                objects[i].put("qtip", bean.getDesc());
            }
            i++;
        }
        return JSONArray.fromObject(objects);
    }

    @Override
    public JSONObject retrieveDatas(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) {
        boolean projectsTree = (request.getParameter("projectsTree") != null);
        String idElt = request.getParameter("id_elt");
        String completeId = request.getParameter("completeId");

        Users user = RequestUtil.getConnectedUser(request);

        List<ElementLinked> eltColl = null;
        if (projectsTree) {
            eltColl = ElementSvc.getInstance().retrieveAllChildrenElements(idElt, user.getId());
        } else {
            String idPro = request.getParameter("id_pro");
            String idBline = request.getParameter("id_bline");
            eltColl = ElementSvc.getInstance().retrieveProjectArboElementsOneLevel(idPro, idBline, idElt, user.getId());
        }

        JSONObject obj = new JSONObject();

        if (eltColl != null) {
            JSONArray array = this.getElementChildrenListJSON(eltColl, completeId, projectsTree);
            this.putArrayIntoObject(array, obj);
        }

        return obj;
    }
}
