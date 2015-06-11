/**
 * 
 */
package com.compuware.caqs.business.admin;

import com.compuware.caqs.dao.factory.DaoFactory;
import com.compuware.caqs.dao.interfaces.ElementDao;
import com.compuware.caqs.dao.interfaces.StereotypeDao;
import com.compuware.caqs.domain.dataschemas.ElementBean;
import com.compuware.caqs.domain.dataschemas.StereotypeBean;

/**
 * @author cwfr-fdubois
 *
 */
public class ProjectAdmin {
	
    protected static com.compuware.caqs.dao.dbms.DataAccessCache dataCache = com.compuware.caqs.dao.dbms.DataAccessCache.getInstance();

	/** Effacer logiquement un element
	 * @param idElt Id de l'element � effacer
     * @param telt Identifiant du type de l'element a supprimer
     * @return true si la suppression a reussi, false sinon
	 */
	public boolean delete(String idElt, String telt) {
		DaoFactory daoFactory = DaoFactory.getInstance();
		ElementDao elementDao = daoFactory.getElementDao();
		return elementDao.delete(idElt, telt);
	}

	public void generateProject(String id, String lib, String desc) {
		DaoFactory daoFactory = DaoFactory.getInstance();
		ElementDao elementDao = daoFactory.getElementDao();
		java.util.Date now = new java.util.Date();
		elementDao.generateProject(id, lib, desc, null, 0, new java.sql.Date(now.getTime()), null);
	}
	
	public void updateProject(String id, String lib, String desc) {
		DaoFactory daoFactory = DaoFactory.getInstance();
		ElementDao elementDao = daoFactory.getElementDao();
		java.util.Date now = new java.util.Date();
		elementDao.updateProject(id, lib, desc, new java.sql.Date(now.getTime()));
	}

	public void createBaseLine(String idPro) {
		DaoFactory daoFactory = DaoFactory.getInstance();
		ElementDao elementDao = daoFactory.getElementDao();
		java.util.Date now = new java.util.Date();
		elementDao.createBaseLine(idPro, new java.sql.Date(now.getTime()));
	}

	public ElementBean retrieveElementById(String id) {
		DaoFactory daoFactory = DaoFactory.getInstance();
		ElementDao elementDao = daoFactory.getElementDao();
		return elementDao.retrieveElementById(id);
	}

	public void setElement(ElementBean eltBean) {
		DaoFactory daoFactory = DaoFactory.getInstance();
		ElementDao elementDao = daoFactory.getElementDao();
		elementDao.setElement(eltBean);
	}

	public void setTreeElementLink(ElementBean eltBean, String fatherId) {
		DaoFactory daoFactory = DaoFactory.getInstance();
		ElementDao elementDao = daoFactory.getElementDao();
		elementDao.setTreeElementLink(eltBean, fatherId);
	}

	public void updateChildrenRights(String id, String userList, String userId, boolean canModifyDomains, boolean canModifyAllProjets) {
		DaoFactory daoFactory = DaoFactory.getInstance();
		ElementDao elementDao = daoFactory.getElementDao();
		elementDao.updateChildrenRights(id, userList, userId, canModifyDomains, canModifyAllProjets);
	}

	public void clearCache(String idPro) {
		dataCache.clearCache(idPro);
	}

	public StereotypeBean retrieveStereotype(String id) {
        DaoFactory daoFactory = DaoFactory.getInstance();
        StereotypeDao stereotypeDao = daoFactory.getStereotypeDao();
		return stereotypeDao.retrieveStereotypeById(id);
	}

	public void storeStereotype(StereotypeBean stereotypeBean) {
        DaoFactory daoFactory = DaoFactory.getInstance();
        StereotypeDao stereotypeDao = daoFactory.getStereotypeDao();
        stereotypeDao.storeStereotype(stereotypeBean);
	}
	
	public void removeRightsToChildren(String id, String[] rights, String userId, boolean canModifyDomains, boolean canModifyAllProjets) {
		DaoFactory daoFactory = DaoFactory.getInstance();
		ElementDao elementDao = daoFactory.getElementDao();
		elementDao.removeRightsToChildren(id, rights, userId, canModifyDomains, canModifyAllProjets);
	}
}
