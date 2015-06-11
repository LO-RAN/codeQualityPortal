package com.compuware.caqs.dao.interfaces;

import java.util.Collection;
import java.util.Map;

import com.compuware.caqs.business.load.ElementOfArchitecture;
import com.compuware.caqs.business.load.db.DataFileType;
import com.compuware.caqs.domain.dataschemas.callsto.CallBean;
import com.compuware.caqs.exception.DataAccessException;

public interface ArchitectureDao {

	public abstract Collection retrieveLinks(String idElt, String idBline, int state);
	
	/**
	 * Insert a call collection into the database for the given baseline.
	 * @param callColl the call collection.
	 * @param idBline the baseline Id.
	 */
	public abstract void insertCalls(Collection<CallBean> callColl, String idBline) throws DataAccessException;
	
	public abstract Map<String, ElementOfArchitecture> retrieveArchitectureElement(String idModule, DataFileType typeElement) throws DataAccessException;
	
}
