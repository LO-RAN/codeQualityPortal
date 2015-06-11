package com.compuware.caqs.dao.interfaces;

import java.util.Collection;

import com.compuware.caqs.domain.dataschemas.ElementBean;
import com.compuware.caqs.domain.dataschemas.ProjectBean;
import com.compuware.caqs.exception.DataAccessException;

public interface ProjectDao {

	public abstract ProjectBean retrieveProjectById(java.lang.String id);

	public abstract Collection<ProjectBean> retrieveAllProject();

    public abstract ProjectBean retrieveModuleProject(java.lang.String idElt) throws DataAccessException;

    public abstract String retrieveProjectElementId(java.lang.String projectId) throws DataAccessException;

    public abstract ElementBean retrieveProjectElementBean(java.lang.String projectId) throws DataAccessException;

}