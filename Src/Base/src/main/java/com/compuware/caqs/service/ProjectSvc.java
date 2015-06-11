package com.compuware.caqs.service;

import com.compuware.caqs.constants.Constants;
import com.compuware.caqs.dao.factory.DaoFactory;
import com.compuware.caqs.dao.interfaces.ProjectDao;
import com.compuware.caqs.domain.dataschemas.ElementBean;
import com.compuware.caqs.domain.dataschemas.ProjectBean;
import com.compuware.caqs.exception.DataAccessException;
import com.compuware.toolbox.util.logging.LoggerManager;

public class ProjectSvc {
	
	protected static org.apache.log4j.Logger logger = LoggerManager.getLogger(Constants.LOG4J_IHM_LOGGER_KEY);

	private static final ProjectSvc instance = new ProjectSvc();
	
	private ProjectSvc() {
	}
	
	public static ProjectSvc getInstance() {
		return instance;
	}
	
	public ProjectBean retrieveProjectById(String id) {
		ProjectBean retour = null;
		ProjectDao projectDao = DaoFactory.getInstance().getProjectDao();
		retour = projectDao.retrieveProjectById(id);
		return retour;
	}
	
	public String retrieveProjectElementId(String idPro) {
		ProjectDao projectDao = DaoFactory.getInstance().getProjectDao();
		String retour = null;
		try {
			retour = projectDao.retrieveProjectElementId(idPro);
		} catch(DataAccessException exc) {
			logger.error("Error while retrieving project element id for idPro="+idPro, exc);
		}
		return retour;
	}
	
	public ElementBean retrieveProjectElementBean(String idPro) {
		ProjectDao projectDao = DaoFactory.getInstance().getProjectDao();
		ElementBean retour = null;
		try {
			retour = projectDao.retrieveProjectElementBean(idPro);
		} catch(DataAccessException exc) {
			logger.error("Error while retrieving project element id for idPro="+idPro, exc);
		}
		return retour;
	}
}
