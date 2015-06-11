/**
 * 
 */
package com.compuware.caqs.domain.dataschemas.callsto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.compuware.caqs.domain.dataschemas.ElementBean;
import com.compuware.caqs.domain.dataschemas.ElementType;

/**
 * @author cwfr-fdubois
 *
 */
public class CallGraphNode {

	private ElementBean element = null;
	private List callIn = new ArrayList();
	private List callOut = new ArrayList();
	
	/**
	 * @return Returns the callIn.
	 */
	public List getCallIn() {
		return callIn;
	}
	
	/**
	 * @param callIn The callIn to set.
	 */
	public void setCallIn(List callIn) {
		this.callIn = callIn;
	}
	
	/**
	 * @param graphIn The graphIn to add.
	 */
	public void addCallIn(CallGraphNode graphIn) {
		this.callIn.add(graphIn);
	}
	
	/**
	 * @return Returns the callOut.
	 */
	public List getCallOut() {
		return callOut;
	}
	
	/**
	 * @param callOut The callOut to set.
	 */
	public void setCallOut(List callOut) {
		this.callOut = callOut;
	}
	
	/**
	 * @param graphOut The graphOut to add.
	 */
	public void addCallOut(CallGraphNode graphOut) {
		this.callOut.add(graphOut);
	}
	
	/**
	 * @return Returns the element.
	 */
	public ElementBean getElement() {
		return element;
	}
	
	/**
	 * @param element The element to set.
	 */
	public void setElement(ElementBean element) {
		this.element = element;
	}
	
	public String toString() {
		Map existingNodes = new HashMap();
		existingNodes.put(this.element.getId(), this.element.getId());
		StringBuffer result = new StringBuffer();
		result.append("<node id='").append(this.element.getId()).append("' idelt='").append(this.element.getId()).append("' name='").append(getLib(this.element)).append("' desc='").append(this.element.getDesc()).append("' />\n");
		CallGraphNode current = null;
		if (!this.callIn.isEmpty()) {
			Iterator i = this.callIn.iterator();
			while (i.hasNext()) {
				current = (CallGraphNode)i.next();
				result.append(current.toStringIn(existingNodes));
				result.append("<edge id='").append(current.getElement().getId()).append('-').append(this.element.getId()).append("' source='").append(current.getElement().getId()).append("' target='").append(this.element.getId()).append("' />\n");
			}
		}
		if (!this.callOut.isEmpty()) {
			Iterator i = this.callOut.iterator();
			while (i.hasNext()) {
				current = (CallGraphNode)i.next();
				result.append(current.toStringOut(existingNodes));
				result.append("<edge id='").append(this.element.getId()).append('-').append(current.getElement().getId()).append("' source='").append(this.element.getId()).append("' target='").append(current.getElement().getId()).append("' />\n");
			}
		}
		return result.toString();
	}

	public String toStringIn(Map existingNodes) {
		StringBuffer result = new StringBuffer();
		if (existingNodes.get(this.element.getId()) == null) {
			existingNodes.put(this.element.getId(), this.element.getId());
			result.append("<node id='").append(this.element.getId()).append("' idelt='").append(this.element.getId()).append("' name='").append(getLib(this.element)).append("' desc='").append(this.element.getDesc()).append("' />\n");
			if (!this.callIn.isEmpty()) {
				CallGraphNode current = null;
				Iterator i = this.callIn.iterator();
				while (i.hasNext()) {
					current = (CallGraphNode)i.next();
					result.append(current.toStringIn(existingNodes));
					result.append("<edge id='").append(current.getElement().getId()).append('-').append(this.element.getId()).append("' source='").append(current.getElement().getId()).append("' target='").append(this.element.getId()).append("' />\n");
				}
			}
		}
		return result.toString();
	}

	public String toStringOut(Map existingNodes) {
		StringBuffer result = new StringBuffer();
		if (existingNodes.get(this.element.getId()) == null) {
			existingNodes.put(this.element.getId(), this.element.getId());
			result.append("<node id='").append(this.element.getId()).append("' idelt='").append(this.element.getId()).append("' name='").append(getLib(this.element)).append("' desc='").append(this.element.getDesc()).append("' />\n");
			if (!this.callOut.isEmpty()) {
				CallGraphNode current = null;
				Iterator i = this.callOut.iterator();
				while (i.hasNext()) {
					current = (CallGraphNode)i.next();
					result.append(current.toStringOut(existingNodes));
					result.append("<edge id='").append(this.element.getId()).append('-').append(current.getElement().getId()).append("' source='").append(this.element.getId()).append("' target='").append(current.getElement().getId()).append("' />\n");
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

}
