package com.compuware.caqs.service;

import java.util.Collection;

import com.compuware.caqs.dao.factory.DaoFactory;
import com.compuware.caqs.dao.interfaces.ArchitectureDao;
import com.compuware.caqs.dao.interfaces.CallDao;
import com.compuware.caqs.domain.dataschemas.callsto.CallGraphNode;
import com.compuware.caqs.exception.CaqsException;

public class ArchitectureSvc {
	
	private static final ArchitectureSvc instance = new ArchitectureSvc();
	
	private ArchitectureSvc() {
	}
	
	public static ArchitectureSvc getInstance() {
		return instance;
	}

	public Collection retrieveLinks(String idElt, String idBline, int state) {
	    DaoFactory daoFactory = DaoFactory.getInstance();
	    ArchitectureDao architectureDao = daoFactory.getArchitectureDao();
	    return architectureDao.retrieveLinks(idElt, idBline, state);
	}
	
	public CallGraphNode retrieveCalls(String idElt, String idBline, int nbIn, int nbOut) throws CaqsException {
		DaoFactory daoFactory = DaoFactory.getInstance();
		CallDao callDao = daoFactory.getCallDao();
		return callDao.retrieveCalls(idElt, idBline, nbIn, nbOut);
	}

	public Collection<CallGraphNode> retrieveAllCalls(String idBline) throws CaqsException {
		DaoFactory daoFactory = DaoFactory.getInstance();
		CallDao callDao = daoFactory.getCallDao();
		return callDao.retrieveAllCalls(idBline);
	}
}
