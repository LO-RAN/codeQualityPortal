/**
 * 
 */
package com.compuware.caqs.business.consult;

import java.util.ArrayList;
import java.util.List;

import com.compuware.caqs.dao.factory.DaoFactory;
import com.compuware.caqs.dao.interfaces.LabelDao;
import com.compuware.caqs.domain.dataschemas.ElementBean;
import com.compuware.caqs.domain.dataschemas.LabelBean;
import com.compuware.caqs.domain.dataschemas.LabelResume;
import com.compuware.caqs.exception.CaqsException;

/**
 * @author cwfr-fdubois
 *
 */
public class Label {

    public void linkLabels(String idLabelOld, String idLabelNew) throws CaqsException {
        if (idLabelOld != null && idLabelNew != null) {
        	DaoFactory daoFactory = DaoFactory.getInstance();
        	LabelDao labelDao = daoFactory.getLabelDao();
        	labelDao.linkLabels(idLabelOld, idLabelNew);
        }
    }
	
    public LabelBean retrieveLabel(String idLabel) throws CaqsException {
    	LabelBean result = null;
    	if (idLabel != null) {
	    	DaoFactory daoFactory = DaoFactory.getInstance();
	    	LabelDao labelDao = daoFactory.getLabelDao();
	    	result = labelDao.retrieveLabelById(idLabel);
    	}
    	return result;
    }
    
    public void store(LabelBean labelBean, ElementBean eltBean) throws CaqsException {
    	if (labelBean != null && eltBean != null) {
	    	DaoFactory daoFactory = DaoFactory.getInstance();
	    	LabelDao labelDao = daoFactory.getLabelDao();
	    	labelDao.setLabel(labelBean, eltBean);
    	}
    }
    
    public List<LabelResume> retrieveAllLabels(String req, String userId) throws CaqsException {
    	List<LabelResume> result = null;
    	if (req != null && userId != null) {
	    	DaoFactory daoFactory = DaoFactory.getInstance();
	    	LabelDao labelDao = daoFactory.getLabelDao();
	    	result = labelDao.retrieveAllLabels(req, userId);
    	}
    	else {
    		result = new ArrayList<LabelResume>();
    	}
    	return result;
    }    
    
}
