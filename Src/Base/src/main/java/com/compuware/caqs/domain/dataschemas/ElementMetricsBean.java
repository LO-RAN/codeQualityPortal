package com.compuware.caqs.domain.dataschemas;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.compuware.caqs.constants.Constants;

public class ElementMetricsBean extends ElementBean implements Serializable {

	private static final long serialVersionUID = 6460958240670833896L;
	
	private ElementBean superElement;
	private Collection<MetriqueBean> metricCollection = new ArrayList<MetriqueBean>();
	private Map<String, MetriqueBean> metricMap = new HashMap<String, MetriqueBean>();
    private String parentName;
	
	public ElementMetricsBean() {
		super();
	}

	public ElementMetricsBean(ElementBean elt) {
		this.setId(elt.getId());
		this.setLib(elt.getLib());
		this.setDesc(elt.getDesc());
		this.setTypeElt(elt.getTypeElt());
	}

	public ElementMetricsBean(BaselineBean baseline, String id,
            String idTelt, String lib) {
		super(baseline, id, idTelt, lib);
	}

	public Collection<MetriqueBean> getMetricCollection() {
		return metricCollection;
	}

	public void setMetricCollection(Collection<MetriqueBean> metricCollection) {
		this.metricCollection = metricCollection;
		this.metricMap = createMetriqueMap(this.metricCollection);
	}

	public ElementBean getSuperElement() {
		return superElement;
	}

	public void setSuperElement(ElementBean superElement) {
		this.superElement = superElement;
	}

    public String getParentName() {
        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

	public void addMetrique(MetriqueBean met) {
		metricCollection.add(met);
		metricMap.put(met.getId(), met);
	}
	
	public void addAndIncMetrique(MetriqueBean met) {
		MetriqueBean existingMet = (MetriqueBean)metricMap.get(met.getId());
		if (existingMet != null) {
			existingMet.addLine(met.getLinesAsString(',', Constants.MAX_LINES_SIZE));
			existingMet.setValbrute(existingMet.getValbrute() + 1);
		}
		else {
			addMetrique(met);
		}
	}
	
	public MetriqueBean getMetrique(String id) {
		return (MetriqueBean)this.metricMap.get(id);
	}

    private Map<String, MetriqueBean> createMetriqueMap(Collection<MetriqueBean> coll) {
    	Map<String, MetriqueBean> result = new HashMap<String, MetriqueBean>();
    	if (coll != null) {
    		MetriqueBean met = null;
    		Iterator<MetriqueBean> i = coll.iterator();
    		while (i.hasNext()) {
    			met = i.next();
    			result.put(met.getId(), met);
    		}
    	}
    	return result;
    }
        	
}
