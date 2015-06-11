package com.compuware.caqs.business.consult;

import com.compuware.caqs.dao.factory.DaoFactory;
import com.compuware.caqs.dao.interfaces.BaselineDao;
import com.compuware.caqs.domain.dataschemas.BaselineBean;
import com.compuware.caqs.exception.DataAccessException;

public class Baseline {
	
	public BaselineBean getPreviousBaseline(BaselineBean currentBline, String idElt) {
    	BaselineBean result = null;
    	BaselineDao baselineDao = DaoFactory.getInstance().getBaselineDao();
    	try {
    		result = baselineDao.getPreviousBaseline(currentBline, idElt);
    	}
    	catch (DataAccessException e) {
    		result = null;
    	}
    	return result;
    }
}
