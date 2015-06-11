/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.compuware.caqs.presentation.messages.actions;

import com.compuware.caqs.presentation.common.ExtJSAjaxAction;
import com.compuware.caqs.util.ErrorFileUtil;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.sf.json.JSONObject;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

/**
 *
 * @author cwfr-dzysman
 */
public class RetrieveTextForAnalysisFailedAction extends ExtJSAjaxAction {

    @Override
    public JSONObject retrieveDatas(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) {
        JSONObject retour = new JSONObject();
        byte[] result = null;
        String idBline = request.getParameter("idBline");
        if (idBline != null) {
            File errorFile = ErrorFileUtil.getErrorFile(idBline, "analysis");
            if (errorFile != null && errorFile.exists() && errorFile.isFile()) {
                try {
                    result = new byte[(int) errorFile.length()];
                    BufferedInputStream input = new BufferedInputStream(new FileInputStream(errorFile));
                    input.read(result);
                    input.close();
                    retour.put("datas", new String(result));
                } catch (IOException e) {
                    mLog.error("Error reading file: " + errorFile.getName(), e);
                }
            }
        } else {
            mLog.error("RetrievetextForAnalysisFailedAction : missing idbline");
        }

        return retour;
    }
}
