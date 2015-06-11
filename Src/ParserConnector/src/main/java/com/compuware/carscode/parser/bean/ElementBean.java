/**
 * 
 */
package com.compuware.carscode.parser.bean;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author cwfr-fdubois
 *
 */
public class ElementBean implements IElementBean {

	String descElt = null;
	String descParent = null;
	String typeElt = null;
	String filePath = null;
	Map metricMap = null;
	Set links = new HashSet();
	
	/* (non-Javadoc)
	 * @see com.compuware.carscode.parser.bean.IElementBean#getDescElt()
	 */
	public String getDescElt() {
		return descElt;
	}
	
	/* (non-Javadoc)
	 * @see com.compuware.carscode.parser.bean.IElementBean#setDescElt(java.lang.String)
	 */
	public void setDescElt(String descElt) {
		this.descElt = descElt;
	}
	
	/* (non-Javadoc)
	 * @see com.compuware.carscode.parser.bean.IElementBean#getDescParent()
	 */
	public String getDescParent() {
		return descParent;
	}
	
	/* (non-Javadoc)
	 * @see com.compuware.carscode.parser.bean.IElementBean#setDescParent(java.lang.String)
	 */
	public void setDescParent(String descParent) {
		this.descParent = descParent;
	}
	
	/* (non-Javadoc)
	 * @see com.compuware.carscode.parser.bean.IElementBean#getMetricMap()
	 */
	public Map getMetricMap() {
		return metricMap;
	}
	
	/* (non-Javadoc)
	 * @see com.compuware.carscode.parser.bean.IElementBean#setMetricMap(java.util.Map)
	 */
	public void setMetricMap(Map metricMap) {
		this.metricMap = metricMap;
	}
	
	/* (non-Javadoc)
	 * @see com.compuware.carscode.parser.bean.IElementBean#getTypeElt()
	 */
	public String getTypeElt() {
		return typeElt;
	}
	
	/* (non-Javadoc)
	 * @see com.compuware.carscode.parser.bean.IElementBean#setTypeElt(java.lang.String)
	 */
	public void setTypeElt(String typeElt) {
		this.typeElt = typeElt;
	}
	
	public void setMetric(String idMet, String value, boolean createIfZero) throws ParseException {
		NumberFormat nf = NumberFormat.getInstance();
		Number nval = nf.parse(value);
		if (createIfZero || (nval != null && nval.doubleValue() > 0)) {
			IMetricBean metricBean = (IMetricBean)this.metricMap.get(idMet);
			if (metricBean == null) {
				metricBean = new MetricBean();
				metricBean.setId(idMet);
				this.metricMap.put(idMet, metricBean);
			}
			metricBean.setValue(nval.doubleValue());
		}
	}
	
	public void setMetric(IMetricBean newMetric) {
		if (newMetric != null && newMetric.getId() != null) {
			IMetricBean metricBean = (IMetricBean)this.metricMap.get(newMetric.getId());
			if (metricBean == null) {
				this.metricMap.put(newMetric.getId(), newMetric);
			}
			else {
				metricBean.incValue(newMetric.getValue());
				metricBean.addLines(newMetric.getLines());
			}
		}
	}

	/**
	 * @return Returns the filePath.
	 */
	public String getFilePath() {
		return filePath;
	}

	/**
	 * @param filePath The filePath to set.
	 */
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	
	/**
	 * @return the links
	 */
	public Set getLinks() {
		return links;
	}

	/**
	 * @param links the links to set
	 */
	public void setLinks(Set links) {
		this.links = links;
	}

	public void addLink(String eltLinkDesc) {
		if (eltLinkDesc != null && eltLinkDesc.length() > 0) {
			this.links.add(eltLinkDesc);
		}
	}

	public void addLink(Collection eltLinkDescList) {
		if (eltLinkDescList != null && eltLinkDescList.size() > 0) {
			this.links.addAll(eltLinkDescList);
		}
	}
}
