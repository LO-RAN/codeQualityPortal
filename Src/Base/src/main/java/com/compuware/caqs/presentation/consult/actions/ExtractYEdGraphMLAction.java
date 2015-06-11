package com.compuware.caqs.presentation.consult.actions;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.compuware.caqs.constants.Constants;
import com.compuware.caqs.domain.dataschemas.ElementBean;
import com.compuware.caqs.domain.dataschemas.ElementType;
import com.compuware.caqs.domain.dataschemas.callsto.CallGraphNode;
import com.compuware.caqs.exception.CaqsException;
import com.compuware.caqs.service.ArchitectureSvc;
import com.compuware.caqs.util.RequestHelper;
import com.compuware.toolbox.util.logging.LoggerManager;

public class ExtractYEdGraphMLAction extends Action {

    private static org.apache.log4j.Logger mLog = LoggerManager.getLogger(Constants.LOG4J_IHM_LOGGER_KEY);

    public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {

	    String idElt = RequestHelper.getParameterAndForward(request, "idElt");
	    String idBline = RequestHelper.getParameterAndForward(request, "idBline");
	    String sNbIn = request.getParameter("nbIn");
	    String sNbOut = request.getParameter("nbOut");
	    String all = request.getParameter("all");
	    String maxIn = request.getParameter("maxIn");
	    
	    ArchitectureSvc architectureSvc = ArchitectureSvc.getInstance();
	    String graphStr = "";
	    try {
	    	if (all != null) {
	    		Collection<CallGraphNode> graph = architectureSvc.retrieveAllCalls(idBline);
			    request.setAttribute("graph", graph);
			    if (graph != null) {
			    	graphStr = toString(graph, Integer.parseInt(maxIn));
			    }
	    	}
	    	else {
			    CallGraphNode graph = architectureSvc.retrieveCalls(idElt, idBline, Integer.parseInt(sNbIn), Integer.parseInt(sNbOut));
			    request.setAttribute("graph", graph);
			    if (graph != null) {
			    	graphStr = toString(graph);
			    }
	    	}
	    }
	    catch (CaqsException e) {
	    	mLog.error("Error retrieving Calls", e);
	    }

	    PrintWriter out = response.getWriter();
        // Set your response headers here
		response.setContentType("application/x-download");
		response.setHeader("Content-Disposition", "attachment; filename=\"graph.graphml\"");
        out.write("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>\n");
		out.write("<graphml xmlns=\"http://graphml.graphdrawing.org/xmlns/graphml\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:y=\"http://www.yworks.com/xml/graphml\" xsi:schemaLocation=\"http://graphml.graphdrawing.org/xmlns/graphml http://www.yworks.com/xml/schema/graphml/1.0/ygraphml.xsd\">\n");
        out.write("<key for=\"node\" id=\"name\" yfiles.type=\"nodegraphics\"/>\n");
        out.write("<key for=\"edge\" id=\"estyle\" yfiles.type=\"edgegraphics\"/>\n");
        out.write("<graph edgedefault=\"directed\">\n");
        out.write(graphStr);
        mLog.debug(graphStr);
        out.write("</graph>\n");
        out.write("</graphml>\n");
        out.flush();
	    
	    
		return null;

	}
    
    public String toString(CallGraphNode graph) {
		Map existingNodes = new HashMap();
		existingNodes.put(graph.getElement().getId(), graph.getElement().getId());
		StringBuffer result = new StringBuffer();
		ElementBean element = graph.getElement();
		result.append("<node id='").append(element.getId()).append("'><data key='name'><y:ShapeNode><y:NodeLabel>").append(getLib(element)).append("</y:NodeLabel></y:ShapeNode></data></node>\n");
		CallGraphNode current = null;
		if (!graph.getCallIn().isEmpty()) {
			Iterator i = graph.getCallIn().iterator();
			while (i.hasNext()) {
				current = (CallGraphNode)i.next();
				result.append(toStringIn(current, existingNodes));
				result.append("<edge id='").append(current.getElement().getId()).append('-').append(element.getId()).append("' source='").append(current.getElement().getId()).append("' target='").append(element.getId()).append("'><data key=\"estyle\"><y:PolyLineEdge><y:Arrows source=\"none\" target=\"standard\"/></y:PolyLineEdge></data></edge>\n");
			}
		}
		if (!graph.getCallOut().isEmpty()) {
			Iterator i = graph.getCallOut().iterator();
			while (i.hasNext()) {
				current = (CallGraphNode)i.next();
				result.append(toStringOut(current, existingNodes));
				result.append("<edge id='").append(graph.getElement().getId()).append('-').append(current.getElement().getId()).append("' source='").append(element.getId()).append("' target='").append(current.getElement().getId()).append("'><data key=\"estyle\"><y:PolyLineEdge><y:Arrows source=\"none\" target=\"standard\"/></y:PolyLineEdge></data></edge>\n");
			}
		}
		return result.toString();
	}

	public String toStringIn(CallGraphNode graph, Map existingNodes) {
		StringBuffer result = new StringBuffer();
		ElementBean element = graph.getElement();
		if (existingNodes.get(element.getId()) == null) {
			existingNodes.put(element.getId(), element.getId());
			result.append("<node id='").append(element.getId()).append("'><data key='name'><y:ShapeNode><y:NodeLabel>").append(getLib(element)).append("</y:NodeLabel></y:ShapeNode></data></node>\n");
			if (!graph.getCallIn().isEmpty()) {
				CallGraphNode current = null;
				Iterator i = graph.getCallIn().iterator();
				while (i.hasNext()) {
					current = (CallGraphNode)i.next();
					result.append(toStringIn(current, existingNodes));
					result.append("<edge id='").append(current.getElement().getId()).append('-').append(element.getId()).append("' source='").append(current.getElement().getId()).append("' target='").append(element.getId()).append("'><data key=\"estyle\"><y:PolyLineEdge><y:Arrows source=\"none\" target=\"standard\"/></y:PolyLineEdge></data></edge>\n");
				}
			}
		}
		return result.toString();
	}

	public String toStringOut(CallGraphNode graph, Map existingNodes) {
		StringBuffer result = new StringBuffer();
		ElementBean element = graph.getElement();
		if (existingNodes.get(element.getId()) == null) {
			existingNodes.put(element.getId(), element.getId());
			result.append("<node id='").append(element.getId()).append("'><data key='name'><y:ShapeNode><y:NodeLabel>").append(getLib(element)).append("</y:NodeLabel></y:ShapeNode></data></node>\n");
			if (!graph.getCallOut().isEmpty()) {
				CallGraphNode current = null;
				Iterator i = graph.getCallOut().iterator();
				while (i.hasNext()) {
					current = (CallGraphNode)i.next();
					result.append(toStringOut(current, existingNodes));
					result.append("<edge id='").append(element.getId()).append('-').append(current.getElement().getId()).append("' source='").append(element.getId()).append("' target='").append(current.getElement().getId()).append("'><data key=\"estyle\"><y:PolyLineEdge><y:Arrows source=\"none\" target=\"standard\"/></y:PolyLineEdge></data></edge>\n");
				}
			}
		}
		return result.toString();
	}
	
	private String getLib(ElementBean elt) {
		String result = null;
		if (ElementType.MET.equals(elt.getTypeElt())) {
			int idx = elt.getDesc().lastIndexOf('.');
			String tmp = elt.getDesc().substring(0, idx);
			idx = tmp.lastIndexOf('.');
			result = elt.getDesc().substring(idx + 1);
		}
		else {
			result = elt.getLib();
		}
		return result;
	}	
	
	public String toString(Collection<CallGraphNode> graph, int maxIn) {
		StringBuffer result = new StringBuffer();
		Iterator<CallGraphNode> nodeIter = graph.iterator();
		CallGraphNode current = null;
		Map<String, CallGraphNode> excluded = new HashMap<String, CallGraphNode>();
		while (nodeIter.hasNext()) {
			current = nodeIter.next();
			if (current.getCallIn().isEmpty() || current.getCallIn().size() < maxIn) {
				ElementBean element = current.getElement();
				result.append("<node id='").append(element.getId()).append("'><data key='name'><y:ShapeNode><y:NodeLabel>").append(getLib(element)).append("</y:NodeLabel></y:ShapeNode></data></node>\n");
			}
			else {
				excluded.put(current.getElement().getId(), current);
			}
		}
		nodeIter = graph.iterator();
		CallGraphNode main = null;
		while (nodeIter.hasNext()) {
			main = nodeIter.next();
			ElementBean element = main.getElement();
			if (!main.getCallIn().isEmpty() && main.getCallIn().size() < maxIn) {
				Iterator i = main.getCallIn().iterator();
				while (i.hasNext()) {
					current = (CallGraphNode)i.next();
					if (!excluded.containsKey(current.getElement().getId()) && !excluded.containsKey(element.getId())) {
						result.append("<edge id='").append(current.getElement().getId()).append('-').append(element.getId()).append("' source='").append(current.getElement().getId()).append("' target='").append(element.getId()).append("'><data key=\"estyle\"><y:PolyLineEdge><y:Arrows source=\"none\" target=\"standard\"/></y:PolyLineEdge></data></edge>\n");
					}
				}
			}
		}

		return result.toString();
	}
	
}
