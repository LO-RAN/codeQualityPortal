package com.compuware.caqs.dao.interfaces;

import java.util.Collection;

import com.compuware.caqs.domain.dataschemas.ElementBean;
import com.compuware.caqs.domain.dataschemas.callsto.CallGraphNode;
import com.compuware.caqs.exception.DataAccessException;

public interface CallDao {

	public abstract Collection<CallGraphNode> retrieveAllCalls(String idBline) throws DataAccessException;

	public abstract CallGraphNode retrieveCalls(String idElt, String idBline, int nbIn, int nbOut) throws DataAccessException;

	public abstract CallGraphNode retrieveCalls(ElementBean elt, String idBline, int nbIn, int nbOut) throws DataAccessException;

	public abstract void createParentLinks(String idElt, String idBline) throws DataAccessException;
	
}