package com.compuware.caqs.domain.dataschemas.callsto;

public class CallBean implements Comparable {

	private String idFrom;
	private String idTo;
	/**
	 * @return Returns the idFrom.
	 */
	public String getIdFrom() {
		return idFrom;
	}
	/**
	 * @param idFrom The idFrom to set.
	 */
	public void setIdFrom(String idFrom) {
		this.idFrom = idFrom;
	}
	/**
	 * @return Returns the idTo.
	 */
	public String getIdTo() {
		return idTo;
	}
	/**
	 * @param idTo The idTo to set.
	 */
	public void setIdTo(String idTo) {
		this.idTo = idTo;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((idFrom == null) ? 0 : idFrom.hashCode());
		result = prime * result + ((idTo == null) ? 0 : idTo.hashCode());
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
		if (obj != null && (getClass() == obj.getClass())) {
			final CallBean other = (CallBean) obj;
			if (idFrom != null || other.idFrom != null || idTo != null || other.idTo != null) {
				if (idFrom != null && idTo != null) {
					if (other.idFrom == null || other.idTo == null) {
						result = -1;
					}
					else {
						result = idFrom.compareTo(other.idFrom);
						if (result == 0) {
							result = idTo.compareTo(other.idTo);
						}
					}
				}
				else {
					if ((idFrom == null && idTo == null) && (other.idFrom != null || other.idTo != null)) {
						// from and to are null but both target from and to are not
						result = -1;
					}
					else {
						if (idFrom == null) {
							// From is null, to is not
							if (other.idFrom != null || other.idTo == null) {
								result = -1;
							}
							else {
								result = idTo.compareTo(other.idTo);
							}
						}
						else {
							// from != null && to == null
							if (other.idFrom == null || other.idTo != null) {
								result = -1;
							}
							else {
								result = idFrom.compareTo(other.idFrom);
							}
						}
					}
				}
			}
		}
		return result;
	}	
}
