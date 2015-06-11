package com.compuware.caqs.presentation.admin.actions;

import com.compuware.caqs.constants.Constants;
import com.compuware.caqs.domain.dataschemas.ElementBean;
import com.compuware.caqs.domain.dataschemas.ElementLinked;
import com.compuware.caqs.domain.dataschemas.ElementType;
import com.compuware.caqs.domain.dataschemas.UserBean;
import com.compuware.caqs.exception.CaqsException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

import com.compuware.caqs.presentation.common.ExtJSAjaxAction;
import com.compuware.caqs.presentation.common.ExtJSUtils;
import com.compuware.caqs.presentation.util.RequestUtil;
import com.compuware.caqs.security.auth.Users;
import com.compuware.caqs.service.AdminSvc;
import com.compuware.caqs.service.PortalUserSvc;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import javax.servlet.http.HttpSession;
import org.apache.struts.util.MessageResources;

public class RetrieveElementDataAction extends ExtJSAjaxAction {

    private static final String PORTAL_USER_LIST_ATTR = "portalUserList";

    /** Recupere tous les utilisateur du portail.
     * @return Vecteur contenant les Id et les noms des utilisateurs
     */
    private List<UserBean> getPortalUsers(HttpSession session) throws CaqsException {
        List<UserBean> result = (List<UserBean>) session.getAttribute(PORTAL_USER_LIST_ATTR);
        if (result == null) {
            PortalUserSvc uviewSvc = PortalUserSvc.getInstance();
            result = uviewSvc.getAllUsers();
        }
        return result;
    }

    protected List<UserBean> getPortalInfos(List<UserBean> eltUserList, List<UserBean> unifaceviewUserList) {
        List<UserBean> result = new ArrayList<UserBean>(unifaceviewUserList);
        result.retainAll(eltUserList);
        return result;
    }

    private JSONArray getJSONValue(ElementLinked elt) {
        JSONArray retour = null;
        JSONObject obj = this.getJSONObject(elt, "");
        if (obj != null) {
            JSONObject[] array = new JSONObject[1];
            array[0] = obj;
            retour = JSONArray.fromObject(array);
        }
        return retour;
    }

    private JSONObject getJSONObject(ElementLinked elt, String parentId) {
        JSONObject retour = new JSONObject();
        String id = elt.getId();
        if (elt.isSymbolicLink()) {
            id += "-link";
        }
        retour.put("id", id);
        retour.put("text", elt.getLib());
        boolean isLeaf = (ElementType.EA.equals(elt.getTypeElt())) || elt.getChildren()
                == null || elt.getChildren().isEmpty();
        retour.put("leaf", new Boolean(isLeaf));
        //tout est coche par defaut
        retour.put("checked", "true");
        String cls = ExtJSUtils.getIconClassForTelt(elt.getTypeElt(),
                elt.isSymbolicLink());
        retour.put("iconCls", cls);
        retour.put("uiProvider", "col");
        //retour.put("expanded", "true");
        retour.put("parentId", parentId);
        retour.put("attributes", elt.getTypeElt());

        //gestion des enfants
        if (elt.getChildren() != null && !elt.getChildren().isEmpty()) {
            List<JSONObject> liste = new ArrayList<JSONObject>();
            for (ElementBean child : elt.getChildren()) {
                if (child instanceof ElementLinked) {
                    ElementLinked link = (ElementLinked) child;
                    JSONObject obj = this.getJSONObject(link, elt.getId());
                    liste.add(obj);
                }
            }
            retour.put("children", JSONArray.fromObject(liste.toArray(new JSONObject[0])));
        }

        return retour;
    }

    private void setBlankJSONObject(JSONObject obj, SimpleDateFormat sdf) {
        obj.put("id", "");
        obj.put("lib", "");
        obj.put("desc", "");
        obj.put("weight", "1");
        obj.put("dmaj", "");
        obj.put("dinst", sdf.format(new Date()));
        obj.put("dapplication", "");
        obj.put("dperemption", "");
        obj.put("info1", "");
        obj.put("info2", "");
        obj.put("idPro", "");
        obj.put("idDialecte", "");
        obj.put("idModel", "");
        obj.put("scmRepository", "");
        obj.put("scmModule", "");
        obj.put("libraries", "");
        obj.put("sourceDir", "");
        obj.put("projectFilePath", "");
        obj.put("scmModule", "");
    }

    private void updateJSONFromElement(ElementBean elt, JSONObject obj, SimpleDateFormat sdf) {
        obj.put("id", elt.getId());
        obj.put("lib", elt.getLib());
        obj.put("desc", elt.getDesc());
        obj.put("weight", elt.getPoids());
        if (elt.getDmaj() != null) {
            obj.put("dmaj", sdf.format(elt.getDmaj()));
        }
        if (elt.getDinst() != null) {
            obj.put("dinst", sdf.format(elt.getDinst()));
        }
        if (elt.getDapplication() != null) {
            obj.put("dapplication", sdf.format(elt.getDapplication()));
        }
        if (elt.getDperemption() != null) {
            obj.put("dperemption", sdf.format(elt.getDperemption()));
        }
        obj.put("info1", elt.getInfo1());
        obj.put("info2", elt.getInfo2());
        if (elt.getProject() != null) {
            obj.put("idPro", elt.getProject().getId());
        }
        if (elt.getDialecte() != null) {
            obj.put("idDialecte", elt.getDialecte().getId());
        }
        if (elt.getUsage() != null) {
            obj.put("idModel", elt.getUsage().getId());
        }
        obj.put("scmRepository", elt.getScmRepository());
        obj.put("scmModule", elt.getScmModule());
        obj.put("libraries", elt.getLibraries());
        obj.put("sourceDir", elt.getSourceDir());
        obj.put("projectFilePath", elt.getProjectFilePath());
        obj.put("scmModule", elt.getScmModule());
    }

    private void addProjectTreeAnalysis(ElementBean elt, JSONObject obj, HttpServletRequest request) {
        //concerne la fenetre de lancement d'analyse
        Users user = RequestUtil.getConnectedUser(request);
        String idPro = elt.getProject().getId();
        String idUser = user.getId();
        ElementLinked eltLink = AdminSvc.getInstance().retrieveProjectArboElements(idPro, idUser);
        if (eltLink != null) {
            JSONArray analysisTree = this.getJSONValue(eltLink);
            obj.put("projectTreeJSON", analysisTree);
        }
    }

    private void addBlankUserAccreditation(JSONObject obj, HttpServletRequest request, String fatherId, String idTelt) throws CaqsException {
        List<UserBean> portalUsers = getPortalUsers(request.getSession());
        List<UserBean> fatherUserCollection = null;
        List<UserBean> userCollection = null;
        if (fatherId == null || Constants.ENTRYPOINT_ELEMENT_VALUE.equals(fatherId)) {
            fatherUserCollection = portalUsers;
            userCollection = new ArrayList();
            request.setAttribute("fatherUserCollection", fatherUserCollection);
            request.setAttribute("userCollection", new ArrayList());
        } else {
            //dans le cas ou le pere n'est pas le point d'entree
            //on rempli par defaut les utilisateurs associes uniquement pour les EA et les SSP.
            fatherUserCollection = AdminSvc.getInstance().retrieveAllUsersByElementId(fatherId);
            fatherUserCollection = getPortalInfos(fatherUserCollection, portalUsers);
            if (idTelt.equals(ElementType.SSP) || idTelt.equals(ElementType.EA)) {
                userCollection = fatherUserCollection;
                fatherUserCollection = new ArrayList();
                request.setAttribute("fatherUserCollection", new ArrayList());
                request.setAttribute("userCollection", fatherUserCollection);
            } else {
                userCollection = new ArrayList();
                request.setAttribute("fatherUserCollection", fatherUserCollection);
                request.setAttribute("userCollection", new ArrayList());
            }
        }

        JSONArray jsonUserCollection = new JSONArray();
        for (UserBean ub : userCollection) {
            JSONObject user = new JSONObject();
            user.put("id", ub.getId());
            user.put("lib", ub.getLib());
            jsonUserCollection.add(user);
        }
        JSONObject userCollObj = new JSONObject();
        userCollObj.put("rows", jsonUserCollection);
        userCollObj.put("totalCount", jsonUserCollection.size());
        obj.put("userCollection", userCollObj);

        jsonUserCollection = new JSONArray();
        for (UserBean ub : fatherUserCollection) {
            JSONObject user = new JSONObject();
            user.put("id", ub.getId());
            user.put("lib", ub.getLib());
            jsonUserCollection.add(user);
        }
        userCollObj = new JSONObject();
        userCollObj.put("rows", jsonUserCollection);
        userCollObj.put("totalCount", jsonUserCollection.size());
        obj.put("fatherUserCollection", userCollObj);
    }

    private void addUsersAccreditationInfos(ElementBean elt, JSONObject obj, HttpServletRequest request) throws CaqsException {
        List<UserBean> coll = getPortalUsers(request.getSession());
        List<UserBean> eltUserCollection = AdminSvc.getInstance().retrieveAllUsersByElementId(elt.getId());
        eltUserCollection = getPortalInfos(eltUserCollection, coll);
        JSONArray userCollection = new JSONArray();
        for (UserBean ub : eltUserCollection) {
            JSONObject user = new JSONObject();
            user.put("id", ub.getId());
            user.put("lib", ub.getLib());
            userCollection.add(user);
        }
        JSONObject userCollObj = new JSONObject();
        userCollObj.put("rows", userCollection);
        userCollObj.put("totalCount", userCollection.size());
        obj.put("userCollection", userCollObj);

        List<UserBean> fatherUserCollection = null;
        ElementBean father = AdminSvc.getInstance().retrieveFatherElement(elt.getId());
        if (father == null
                || Constants.ENTRYPOINT_ELEMENT_VALUE.equals(father.getId())) {
            fatherUserCollection = new ArrayList<UserBean>();
            fatherUserCollection.addAll(coll);
        } else {
            fatherUserCollection = AdminSvc.getInstance().retrieveAllUsersByFatherElementId(elt.getId());
            fatherUserCollection = getPortalInfos(fatherUserCollection, coll);
        }
        fatherUserCollection.removeAll(eltUserCollection);

        JSONArray jsonFatherUserCollection = new JSONArray();
        for (UserBean ub : fatherUserCollection) {
            JSONObject user = new JSONObject();
            user.put("id", ub.getId());
            user.put("lib", ub.getLib());
            jsonFatherUserCollection.add(user);
        }
        JSONObject fatherCollObj = new JSONObject();
        fatherCollObj.put("rows", jsonFatherUserCollection);
        fatherCollObj.put("totalCount", jsonFatherUserCollection.size());
        obj.put("fatherUserCollection", fatherCollObj);
    }

    public JSONObject retrieveDatas(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) {
        JSONObject obj = new JSONObject();
        String idElt = request.getParameter("id_elt");
        if (idElt == null) {
            idElt = (String) request.getAttribute("id_elt");
        }

        try {
            MessageResources ressources = RequestUtil.getResources(request);
            Locale loc = RequestUtil.getLocale(request);
            SimpleDateFormat sdf = new SimpleDateFormat(ressources.getMessage(loc, "caqs.dateFormat"));
            if (idElt != null) {
                ElementBean eltBean = AdminSvc.getInstance().retrieveElement(idElt);
                this.updateJSONFromElement(eltBean, obj, sdf);
                if (ElementType.PRJ.equals(eltBean.getTypeElt())) {
                    this.addProjectTreeAnalysis(eltBean, obj, request);
                }
                this.addUsersAccreditationInfos(eltBean, obj, request);
            } else {
                String fatherId = request.getParameter("fatherId");
                String idTelt = request.getParameter("idTelt");
                this.setBlankJSONObject(obj, sdf);
                this.addBlankUserAccreditation(obj, request, fatherId, idTelt);
            }
        } catch (CaqsException e) {
            mLog.error("RetrieveElementDataAction", e);
        }

        return obj;
    }
}
