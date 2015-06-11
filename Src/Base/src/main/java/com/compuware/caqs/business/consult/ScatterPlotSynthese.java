package com.compuware.caqs.business.consult;

import java.util.Iterator;
import java.util.List;

import com.compuware.caqs.dao.factory.DaoFactory;
import com.compuware.caqs.dao.interfaces.MetriqueDao;
import com.compuware.caqs.domain.dataschemas.ElementBean;
import com.compuware.caqs.domain.dataschemas.ScatterDataBean;

public class ScatterPlotSynthese {
	
    public List<ScatterDataBean> retrieveScatterPlot(ElementBean eltBean, String metH, String metV, String idTelt ) {
        DaoFactory daoFactory = DaoFactory.getInstance();
		MetriqueDao metriquesFacade = daoFactory.getMetriqueDao();
		List<ScatterDataBean> result = metriquesFacade.retrieveScatterPlot(eltBean, metH, metV, idTelt);
		return result;
	}

	public String getScatterBytes(List<ScatterDataBean> coll) {
		StringBuffer buffer = new StringBuffer();
		Iterator<ScatterDataBean> i = coll.iterator();
		while (i.hasNext()) {
			ScatterDataBean scatterBean = i.next();
			if (scatterBean.isValid()) {
				buffer.append("'" + scatterBean.getLib()).append(";");
				buffer.append(scatterBean.getMetH()).append(";");
				buffer.append(scatterBean.getMetV()).append(";");
				buffer.append(scatterBean.getId()).append("\r\n");
			}
		}
		return buffer.toString();
	}

}
