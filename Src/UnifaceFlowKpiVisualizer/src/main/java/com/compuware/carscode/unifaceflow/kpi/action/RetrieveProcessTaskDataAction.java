package com.compuware.carscode.unifaceflow.kpi.action;

import com.compuware.carscode.unifaceflow.kpi.dao.ProcessTaskDao;
import org.apache.struts.action.*;
import org.apache.struts.util.MessageResources;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Collection;
import java.util.Locale;

/**
 * Created by IntelliJ IDEA.
 * User: cwfr-fdubois
 * Date: 13 mars 2006
 * Time: 15:06:00
 * To change this template use File | Settings | File Templates.
 */
public class RetrieveProcessTaskDataAction extends Action {

    public static final String PROCESS_BEAN_COLLECTION_KEY = "processBeanCollection";
    public static final String PROCESS_ID_KEY = "processId";

    public ActionForward perform(ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) {

        // Extract attributes we will need
        Locale locale = getLocale(request);
        MessageResources messages = getResources();

        // ActionErrors needed for error passing
        ActionErrors errors = new ActionErrors();

        HttpSession session = request.getSession();

        String processId = request.getParameter(PROCESS_ID_KEY);

        ProcessTaskDao dao = ProcessTaskDao.getInstance();
        Collection instances = dao.retrieveProcessTaskData(processId);

        request.setAttribute(RetrieveProcessTaskDataAction.PROCESS_BEAN_COLLECTION_KEY, instances);
        
        return (mapping.findForward("success"));

    }
}
