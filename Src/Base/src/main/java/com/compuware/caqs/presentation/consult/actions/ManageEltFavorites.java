package com.compuware.caqs.presentation.consult.actions;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

import com.compuware.caqs.constants.Constants;
import com.compuware.caqs.domain.dataschemas.ElementBean;
import com.compuware.caqs.presentation.common.ExtJSAjaxAction;
import com.compuware.caqs.service.ElementSvc;
import com.compuware.caqs.service.ProjectSvc;
import com.compuware.toolbox.util.logging.LoggerManager;
import net.sf.json.JSONObject;

/*
 * Extends Action because this class can also be used while not on the synthesis page
 */
public class ManageEltFavorites extends ExtJSAjaxAction {

    private static org.apache.log4j.Logger logger = LoggerManager.getLogger(Constants.LOG4J_IHM_LOGGER_KEY);

    // --------------------------------------------------------- Public Methods
    private static final int GO_TO_FAVORITE_ACTION = 0;
    private static final int REMOVE_FAVORITE_ACTION = 1;
    private static final int ADD_FAVORITE_ACTION = 2;
    private static final int UPDATE_COORDONATES_ACTION = 3;

    public JSONObject retrieveDatas(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) {
        JSONObject retour = new JSONObject();
        int action = -1;
        String sAction = request.getParameter("action");
        if("GO_TO_FAVORITE_ACTION".equals(sAction)) {
            action = GO_TO_FAVORITE_ACTION;
        } else if("REMOVE_FAVORITE_ACTION".equals(sAction)) {
            action = REMOVE_FAVORITE_ACTION;
        } else if("ADD_FAVORITE_ACTION".equals(sAction)) {
            action = ADD_FAVORITE_ACTION;
        } else if("UPDATE_COORDONATES_ACTION".equals(sAction)) {
            action = UPDATE_COORDONATES_ACTION;
        }


        String responseToWrite = null;
        if (action != GO_TO_FAVORITE_ACTION) {
            String idElt = request.getParameter("idElt");
            if (idElt == null) {
                ElementBean elt = (ElementBean) request.getSession().getAttribute(Constants.CAQS_CURRENT_ELEMENT_SESSION_KEY);
                if (elt != null) {
                    idElt = elt.getId();
                }
            }
            responseToWrite = idElt;
        } else {
            //si nous n'avons pas d'idElt, c'est que nous sommes sur le dahsboard
            //et que nous avons clique sur un nom de projet pour y aller directement
            String idPro = request.getParameter("idPro");
            String idEltToSelect = request.getParameter("idEltToSelect");
            if (idPro != null) {
                //on commence par ecrire l'identifiant du projet, son libelle et sa description
                ElementBean eltPro = ProjectSvc.getInstance().retrieveProjectElementBean(idPro);
                responseToWrite = eltPro.getId();
                responseToWrite += "|";
                responseToWrite += eltPro.getLib();
                responseToWrite += "|";
                responseToWrite += eltPro.getDesc();
                responseToWrite += "|";
                //on continue en ecrivant le path
                //on prefixe le path par ce qui serait prefixe par extjs
                responseToWrite += ElementSvc.getInstance().retrieveParentPathByLib(eltPro.getId());
                responseToWrite += "|";
                //maintenant, on recupere le chemin d'identifiants depuis l'element jusqu'au projet
                //on doit couper au projet car ce qui nous est renvoye ici va jusqu'au entrypoint exclus
                String parentPathById = ElementSvc.getInstance().retrieveParentPathById(idEltToSelect);
                String[] cuts = parentPathById.split("/");
                int i = 0;
                while (i < cuts.length && !cuts[i].equals(eltPro.getId())) {
                    i++;
                }
                //on ignore l'id du projet
                i++;
                //ici cuts[i] correspond au projet
                String eltPath = "";
                boolean first = true;
                for (; i < cuts.length; i++) {
                    if (!first) {
                        eltPath += "/";
                    }
                    eltPath += cuts[i];
                    first = false;
                }
                if (!eltPath.equals("")) {
                    eltPath += "/";
                }
                eltPath += idEltToSelect;
                responseToWrite += eltPath;
            }
        }
        if (responseToWrite == null) {
            responseToWrite = "";
        }
        retour.put("datas", responseToWrite);
        return retour;
    }
}
