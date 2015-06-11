package com.compuware.caqs.service;

import java.util.List;

import com.compuware.caqs.business.consult.ScatterPlotSynthese;
import com.compuware.caqs.domain.dataschemas.ElementBean;
import com.compuware.caqs.domain.dataschemas.ScatterDataBean;

public class ScatterPlotSyntheseSvc {
	private static final ScatterPlotSyntheseSvc instance = new ScatterPlotSyntheseSvc();
	
	private ScatterPlotSyntheseSvc() {
	}
	
	public static ScatterPlotSyntheseSvc getInstance() {
		return instance;
	}
	
	private ScatterPlotSynthese scatterplot = new ScatterPlotSynthese();
	
    public List<ScatterDataBean> retrieveScatterPlot(ElementBean eltBean, String metH, String metV, String idTelt) {
        return scatterplot.retrieveScatterPlot(eltBean, metH, metV, idTelt);
	}

	public String getScatterBytes(List<ScatterDataBean> coll) {
		return scatterplot.getScatterBytes(coll);
	}

}
