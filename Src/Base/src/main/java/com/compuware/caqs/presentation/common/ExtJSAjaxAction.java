/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.compuware.caqs.presentation.common;

import com.compuware.caqs.constants.Constants;
import com.compuware.caqs.constants.MessagesCodes;
import com.compuware.toolbox.util.logging.LoggerManager;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 *
 * @author cwfr-dzysman
 */
public abstract class ExtJSAjaxAction extends Action {

    protected static org.apache.log4j.Logger mLog = LoggerManager.getLogger(Constants.LOG4J_IHM_LOGGER_KEY);

    /**
     * 
     * @param array
     * @param obj
     * @return
     */
    protected JSONObject putArrayIntoObject(JSONArray array, JSONObject obj) {
        JSONObject retour = obj;
        if (retour == null) {
            retour = new JSONObject();
        }
        retour.put("dataArray", array);
        return retour;
    }

    protected void fillJSONObjectWithReturnCode(JSONObject object, MessagesCodes code) {
        if (!MessagesCodes.NO_ERROR.equals(code)) {
            object.put("msgReturnCode", code.getCode());
            object.put("typeMsg", code.getType().toString());
        }
        object.put("success", (MessagesCodes.NO_ERROR.equals(code)));
    }

    protected void fillJSONObjectWithReturnCode(JSONObject object, MessagesCodes code,
            boolean success) {
        if (!MessagesCodes.NO_ERROR.equals(code)) {
            object.put("msgReturnCode", code.getCode());
            object.put("typeMsg", code.getType().toString());
        }
        object.put("success", success);
    }

    @Override
    public ActionForward execute(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response)
            throws IOException {
        JSONObject root = new JSONObject();
        boolean success = true;
        try {
            root = this.retrieveDatas(mapping, form, request, response);
        } catch (Throwable exc) {
            mLog.error("an error has occured.", exc);
            success = false;
            this.fillJSONObjectWithReturnCode(root, MessagesCodes.CAQS_GENERIC_ERROR);
        } finally {
            if (root == null) {
                root = new JSONObject();
            }
            if (root.get("success") == null) {
                root.put("success", success);
            }
        }

        if (root != null) {
            try {
                PrintWriter out = response.getWriter();
                out.write(root.toString());
                out.flush();
            } catch (IOException e) {
                mLog.error("ExtJSAjaxAction : " + e.getMessage());
            }
        }

        return null;
    }

    /**
     * retrieve datas to be written into the response as a jsonobject
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     */
    protected abstract JSONObject retrieveDatas(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response);
}
