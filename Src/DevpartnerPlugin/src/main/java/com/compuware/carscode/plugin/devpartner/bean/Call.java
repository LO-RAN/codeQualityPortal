package com.compuware.carscode.plugin.devpartner.bean;

public class Call implements Comparable {

	private String from = null;
	private String to = null;
	
	/**
	 * @return Returns the from.
	 */
	public String getFrom() {
		return from;
	}
	
	/**
	 * @param from The from to set.
	 */
	public void setFrom(String from) {
		this.from = from;
	}
	
	/**
	 * @return Returns the to.
	 */
	public String getTo() {
		return to;
	}
	
	/**
	 * @param to The to to set.
	 */
	public void setTo(String to) {
		this.to = to;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((from == null) ? 0 : from.hashCode());
		result = prime * result + ((to == null) ? 0 : to.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object obj) {
		return this.compareTo(obj) == 0;
	}

	public int compareTo(Object obj) {
		int result = 0;
		if (this != obj && obj != null && (getClass() == obj.getClass())) {
			final Call other = (Call) obj;
			if (from != null || other.from != null || to != null || other.to != null) {
				if (from != null && to != null) {
					if (other.from == null || other.to == null) {
						result = -1;
					}
					else {
						result = from.compareTo(other.from) + to.compareTo(other.to);
					}
				}
				else {
					if ((from == null && to == null) && (other.from != null || other.to != null)) {
						// from and to are null but both target from and to are not
						result = -1;
					}
					else {
						if (from == null) {
							// From is null, to is not
							if (other.from != null || other.to == null) {
								result = -1;
							}
							else {
								result = to.compareTo(other.to);
							}
						}
						else {
							// from != null && to == null
							if (other.from == null || other.to != null) {
								result = -1;
							}
							else {
								result = from.compareTo(other.from);
							}
						}
					}
				}
			}
		}
		return result;
	}
		
}
