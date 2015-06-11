package com.compuware.caqs.presentation.consult.actions;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.compuware.caqs.constants.Constants;
import com.compuware.caqs.domain.dataschemas.callsto.CallGraphNode;
import com.compuware.caqs.exception.CaqsException;
import com.compuware.caqs.service.ArchitectureSvc;
import com.compuware.caqs.util.RequestHelper;
import com.compuware.toolbox.util.logging.LoggerManager;

public class CallRetrieveAction extends Action {

    private static org.apache.log4j.Logger mLog = LoggerManager.getLogger(Constants.LOG4J_IHM_LOGGER_KEY);

    public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {

	    String idElt = RequestHelper.getParameterAndForward(request, "id_elt");
	    String idBline = RequestHelper.getParameterAndForward(request, "id_bline");
	    String sNbIn = request.getParameter("nbIn");
	    String sNbOut = request.getParameter("nbOut");
	    
	    ArchitectureSvc architectureSvc = ArchitectureSvc.getInstance();
	    String graphStr = "";
	    try {
		    CallGraphNode graph = architectureSvc.retrieveCalls(idElt, idBline, Integer.parseInt(sNbIn), Integer.parseInt(sNbOut));
		    request.setAttribute("graph", graph);
		    if (graph != null) {
		    	graphStr = graph.toString();
		    }
	    }
	    catch (CaqsException e) {
	    	mLog.error("Error retrieving Calls", e);
	    }

	    PrintWriter out = response.getWriter();
        // Set your response headers here
        response.setContentType("text/xml");
        out.write("<?xml version=\"1.0\" encoding=\"" + Constants.GLOBAL_CHARSET + "\" ?>\n");
        out.write("<?meta name=\"GENERATOR\" content=\"XML::Smart 1.3.1\" ?>\n");
        out.write("<graph edgedefault=\"directed\">\n");
        out.write(graphStr);
        mLog.debug(graphStr);
        out.write("</graph>\n");
        out.flush();
	    
	    
		return null;

	}

	
}
