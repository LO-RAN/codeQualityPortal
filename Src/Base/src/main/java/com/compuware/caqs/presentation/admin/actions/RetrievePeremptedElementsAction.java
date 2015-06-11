/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.compuware.caqs.presentation.admin.actions;

import com.compuware.caqs.domain.dataschemas.ElementLinked;
import com.compuware.caqs.domain.dataschemas.ElementType;
import com.compuware.caqs.domain.dataschemas.comparators.ElementLinkedComparator;
import com.compuware.caqs.presentation.common.ExtJSUtils;
import com.compuware.caqs.presentation.common.actions.ExtJSGridAjaxAction;
import com.compuware.caqs.presentation.util.CaqsUtils;
import com.compuware.caqs.presentation.util.RequestUtil;
import com.compuware.caqs.security.auth.Users;
import com.compuware.caqs.service.ElementSvc;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
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
public class RetrievePeremptedElementsAction extends ExtJSGridAjaxAction {

    @Override
    public JSONObject retrieveDatas(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) {
        Users user = RequestUtil.getConnectedUser(request);
        List<ElementLinked> elts = null;
        if (user != null && (user.access("ALL_PROJECT_ADMIN"))) {
            elts = ElementSvc.getInstance().retrieveAllPeremptedRootsElements();
        } else {
            elts = ElementSvc.getInstance().retrieveAllPeremptedRootsElements(user.getId());
        }

        return this.getJSONObjectFromColl(elts, request);
    }

    private JSONObject getJSONObjectFromColl(List<ElementLinked> coll,
            HttpServletRequest request) {
        JSONObject root = new JSONObject();
        //ElementCriterionForm
        Locale loc = RequestUtil.getLocale(request);
        int startIndex = RequestUtil.getIntParam(request, "start", 0);
        int limitIndex = RequestUtil.getIntParam(request, "limit", 10);

        if (limitIndex == -1) {
            limitIndex = coll.size();
        }
        if (request.getParameter("sort") != null) {
            String sort = request.getParameter("sort");
            String sens = request.getParameter("dir");
            boolean desc = "desc".equals(sens.toLowerCase());
            Collections.sort(coll, new ElementLinkedComparator(sort, desc, loc));
        }

        root.put("totalCount", coll.size());
        List<JSONObject> l = new ArrayList<JSONObject>();

        MessageResources resources = RequestUtil.getResources(request);
        SimpleDateFormat sdf = new SimpleDateFormat(resources.getMessage(loc, "caqs.dateFormat.withHour"));

        for (int i = startIndex; i < (startIndex + limitIndex) && i <
                coll.size(); i++) {
            ElementLinked elt = coll.get(i);
            JSONObject o = this.elementBeanToJSONObject(elt, sdf,
                    loc, resources);
            l.add(o);
        }

        root.put("elements", l.toArray(new JSONObject[0]));
        return root;
    }

    private JSONObject elementBeanToJSONObject(ElementLinked elt, SimpleDateFormat sdf,
            Locale loc, MessageResources resources) {
        JSONObject retour = new JSONObject();
        retour.put("id", elt.getId());
        retour.put("lib", elt.getLib());
        retour.put("desc", elt.getDesc());
        ElementType telt = new ElementType();
        telt.setId(elt.getTypeElt());
        retour.put("telt", telt.getLib(loc));
        retour.put("id_telt", elt.getTypeElt());
        retour.put("dperemption", sdf.format(elt.getDperemption()));
        String parentPath = ElementSvc.getInstance().retrieveParentPathByLib(elt.getId());
        retour.put("fatherLib", parentPath);
        String cls = ExtJSUtils.getIconClassForTelt(elt.getTypeElt(), false);
        retour.put("iconCls", cls);
        this.manageElementConstraints(elt, retour, resources, loc);
        return retour;
    }

    /**
     * applique des contraintes aux elements, entre autres la possibilite de
     * supprimer ou non l'element. ces contraintes doivent s'appliquer aussi
     * au vidage de la corbeille.
     * @param elt
     * @param obj
     * @param resources
     * @param loc
     */
    private void manageElementConstraints(ElementLinked elt, JSONObject obj,
            MessageResources resources, Locale loc) {
        boolean deletable = CaqsUtils.recycleBinElementIsDeletable(elt);
        if (!deletable) {
            obj.put("msgIcon", "warning");
            obj.put("msgTxt", resources.getMessage(loc, "caqs.admin.trashGrid.warning.notdeletable"));
        }
        obj.put("deletable", deletable);
    }
}
