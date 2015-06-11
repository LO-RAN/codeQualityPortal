/**
 * 
 */
package com.compuware.caqs.flow.bean;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author cwfr-fdubois
 *
 */
public class ProcessInstance {

	private String id = null;
	private ProcessInstance parent = null;
	private Map<String,ProcessInstance> children = new HashMap<String,ProcessInstance>();
	private Date creationTime = null;
	
	public ProcessInstance(String id) {
		this.id = id;
	}

	/**
	 * @return the parent
	 */
	public ProcessInstance getParent() {
		return parent;
	}

	/**
	 * @param parent the parent to set
	 */
	public void setParent(ProcessInstance parent) {
		this.parent = parent;
	}

	/**
	 * @return the children
	 */
	public Map<String, ProcessInstance> getChildren() {
		return children;
	}

	/**
	 * @param children the children to set
	 */
	public void setChildren(Map<String, ProcessInstance> children) {
		this.children = children;
	}

	/**
	 * @param child the child to add
	 */
	public void addChild(ProcessInstance child) {
		this.children.put(child.getId(), child);
	}

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}
	
	/**
	 * Is the current process part of the given one?
	 * @param processId the given process id.
	 * @return true if the current process part of the given one, else false.
	 */
	public boolean isSameProcess(String processId) {
		boolean result = false;
		if (this.id.equals(processId)) {
			result = true;
		}
		else if (this.parent != null && this.parent.isSameProcess(processId)) {
			result = true;
		}
		return result;
	}

	public void setCreationTime(Date creationTime) {
		this.creationTime = creationTime;
	}
	
	/**
	 * @return the creationTime
	 */
	public Date getCreationTime() {
		return creationTime;
	}

}
