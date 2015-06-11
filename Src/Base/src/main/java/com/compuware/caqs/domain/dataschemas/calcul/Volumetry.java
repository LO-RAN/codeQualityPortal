package com.compuware.caqs.domain.dataschemas.calcul;

import com.compuware.caqs.domain.dataschemas.ElementType;

public class Volumetry {

	private String idElt = null;
	private ElementType idTElt = null;
	private int total = 0;
	private int created = 0;
	private int deleted = 0;
	
	/**
	 * Add those volumetry metrics to the current one
	 * @param vol the volymetry to add
	 */
	public void add(Volumetry vol) {
		this.total += vol.getTotal();
		this.created += vol.getCreated();
		this.deleted += vol.getDeleted();
	}
	
	/**
	 * @return Returns the created.
	 */
	public int getCreated() {
		return created;
	}
	
	/**
	 * @param created The created to set.
	 */
	public void setCreated(int created) {
		this.created = created;
	}
	
	/**
	 * @param created The created to set.
	 */
	public void setCreated(Integer created) {
		if (created != null) {
			this.created = created.intValue();
		}
		else {
			this.created = 0;
		}
	}
	
	/**
	 * @return Returns the deleted.
	 */
	public int getDeleted() {
		return deleted;
	}
	
	/**
	 * @param deleted The deleted to set.
	 */
	public void setDeleted(int deleted) {
		this.deleted = deleted;
	}
	
	/**
	 * @param deleted The deleted to set.
	 */
	public void setDeleted(Integer deleted) {
		if (deleted != null) {
			this.deleted = deleted.intValue();
		}
		else {
			this.deleted = 0;
		}
	}
	
	/**
	 * @return Returns the idElt.
	 */
	public String getIdElt() {
		return idElt;
	}
	
	/**
	 * @param idTElt The idTElt to set.
	 */
	public void setIdTElt(String idTElt) {
		this.idTElt = new ElementType();
		this.idTElt.setId(idTElt);
	}
	
	/**
	 * @return Returns the element type id.
	 */
	public String getIdTElt() {
		return idTElt.getId();
	}
	
	public ElementType getElementType() {
		return this.idTElt;
	}
	
	/**
	 * @param idElt The idElt to set.
	 */
	public void setIdElt(String idElt) {
		this.idElt = idElt;
	}
	
	/**
	 * @return Returns the total.
	 */
	public int getTotal() {
		return total;
	}
	
	/**
	 * @param total The total to set.
	 */
	public void setTotal(int total) {
		this.total = total;
	}
	
	/**
	 * @param total The total to set.
	 */
	public void setTotal(Integer total) {
		this.total = total.intValue();
	}
	
}
