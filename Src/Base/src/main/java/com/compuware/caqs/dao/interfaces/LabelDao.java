package com.compuware.caqs.dao.interfaces;

import java.sql.Connection;
import java.util.List;

import com.compuware.caqs.domain.dataschemas.BaselineBean;
import com.compuware.caqs.domain.dataschemas.ElementBean;
import com.compuware.caqs.domain.dataschemas.Label;
import com.compuware.caqs.domain.dataschemas.LabelBean;
import com.compuware.caqs.domain.dataschemas.LabelResume;
import com.compuware.caqs.exception.DataAccessException;

public interface LabelDao {

	public abstract LabelBean retrieveLabelById(String id);

	public abstract LabelBean retrieveLabelById(BaselineBean baseline,
			java.lang.String id);

	public abstract LabelBean retrieveLabelByElement(ElementBean eltBean);

	public abstract void setLabel(LabelBean label, ElementBean eltBean) throws DataAccessException;
	
	public abstract void performInsert(LabelBean label) throws DataAccessException;

	public abstract void performUpdate(LabelBean label) throws DataAccessException;
	
	public abstract void updateElement(ElementBean eltBean,
            LabelBean labelBean, boolean onlyEmpty,
            Connection existingConnection) throws DataAccessException;

    public abstract Label retrieveLinkedLabel(String idLabel, String idPro, String idBline);

    public abstract void linkLabels(String idLabelOld, String idLabelNew) throws DataAccessException;
    
    public abstract List<LabelResume> retrieveAllLabels(String req, String userId) throws DataAccessException;
    
}