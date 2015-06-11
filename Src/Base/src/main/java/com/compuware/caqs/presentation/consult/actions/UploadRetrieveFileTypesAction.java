/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.compuware.caqs.presentation.consult.actions;

import com.compuware.caqs.domain.dataschemas.upload.FileType;
import com.compuware.caqs.domain.dataschemas.upload.Separator;
import com.compuware.caqs.presentation.common.ExtJSAjaxAction;
import com.compuware.caqs.presentation.util.RequestUtil;
import java.util.Locale;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.MessageResources;

/**
 *
 * @author cwfr-dzysman
 */
public class UploadRetrieveFileTypesAction extends ExtJSAjaxAction {

    @Override
    public JSONObject retrieveDatas(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {
        JSONArray globalArray = new JSONArray();

        MessageResources resources = RequestUtil.getResources(request);
        Locale loc = RequestUtil.getLocale(request);

        JSONArray datas = new JSONArray();
        for(FileType fileType : FileType.values()) {
            JSONObject o = new JSONObject();
            o.put("idTypeFichier",fileType.getId());
            o.put("libTypeFichier",resources.getMessage(loc, "caqs.upload.fileType."+fileType.getId()));
            o.put("needsSeparator", fileType.needsSeparator());
            datas.add(o);
        }
        globalArray.add(datas);

        datas = new JSONArray();
        for(Separator separator : Separator.values()) {
            JSONObject o = new JSONObject();
            o.put("idSeparator",separator.getId());
            o.put("libSeparator",resources.getMessage(loc, "caqs.upload.separator."+separator.getId()));
            datas.add(o);
        }
        globalArray.add(datas);
        JSONObject retour = new JSONObject();
        this.putArrayIntoObject(globalArray, retour);
        return retour;
    }
}
