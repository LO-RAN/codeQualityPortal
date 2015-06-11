package com.compuware.carscode.migration.bean;

import com.compuware.carscode.migration.util.IDCreator;

public class ElementBean {

	private String id = null;
	private String desc = null;
	private ElementBean parent = null;

	/**
	 * @return Returns the id.
	 */
	public String getId() {
		if (this.id == null) {
			this.id = IDCreator.getID();
		}
		return id;
	}

	/**
	 * @param id The id to set.
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return Returns the desc.
	 */
	public String getDesc() {
		return desc;
	}

	/**
	 * @param desc The desc to set.
	 */
	public void setDesc(String desc) {
		this.desc = desc;
	}

	/**
	 * @return Returns the parent.
	 */
	public ElementBean getParent() {
		return parent;
	}

	/**
	 * @param parent The parent to set.
	 */
	public void setParent(ElementBean parent) {
		this.parent = parent;
	}
	
}
