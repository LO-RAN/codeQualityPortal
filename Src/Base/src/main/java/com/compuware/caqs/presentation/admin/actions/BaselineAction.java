package com.compuware.caqs.presentation.admin.actions;

import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

import com.compuware.caqs.constants.MessagesCodes;
import com.compuware.caqs.domain.dataschemas.BaselineBean;
import com.compuware.caqs.exception.CaqsException;
import com.compuware.caqs.presentation.common.ExtJSAjaxAction;
import com.compuware.caqs.service.BaselineSvc;
import net.sf.json.JSONObject;

public class BaselineAction extends ExtJSAjaxAction {

    private static final long serialVersionUID = -1928302713876938118L;
    public static final int ACTION_RENAME = 1;
    public static final int ACTION_PURGE = 3;  //physical deletion

    public JSONObject retrieveDatas(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) {
        MessagesCodes retour = MessagesCodes.NO_ERROR;

        String action = request.getParameter("action");
        String projectId = request.getParameter("projectId");
        String baselineName = request.getParameter("baselineName");
        String baselineId = request.getParameter("baselineId");

        BaselineSvc baselineSvc = BaselineSvc.getInstance();

        int iAction = -1;

        try {
            iAction = Integer.parseInt(action);
        } catch (NumberFormatException exc) {
            mLog.error("BaselineAction : Action non reconnue : " + action);
        }

        JSONObject object = new JSONObject();

        try {
            switch (iAction) {
                case BaselineAction.ACTION_PURGE:
                    List<BaselineBean> attached = baselineSvc.isBaselineAttachedToOtherBaseline(baselineId);
                    if (attached.isEmpty()) {
                        baselineSvc.purge(projectId, baselineId);
                    } else {
                        //construction des libelles de baselines
                        String blibs = "";
                        for (Iterator<BaselineBean> it = attached.iterator(); it.hasNext();) {
                            BaselineBean bb = it.next();
                            blibs += bb.getLib();
                            if (it.hasNext()) {
                                blibs += ", ";
                            }
                        }
                        retour = MessagesCodes.ATTACHED_BASELINE;
                        object.put("msgReturnCodeArg0", blibs);
                    }
                    break;
                case BaselineAction.ACTION_RENAME:
                    baselineSvc.update(baselineName, baselineId, false);
                    break;
            }
        } catch (CaqsException e) {
            mLog.error("BaselineAction : CaqsException : " + e.getMessage());
            retour = MessagesCodes.CAQS_GENERIC_ERROR;
        }

        this.fillJSONObjectWithReturnCode(object, retour);

        return object;
    }
}
