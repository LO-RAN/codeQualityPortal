/**
 * 
 */
package com.compuware.caqs.service;

import com.compuware.caqs.constants.MessagesCodes;
import java.util.Collection;
import java.util.List;

import com.compuware.caqs.business.admin.ProjectAdmin;
import com.compuware.caqs.business.consult.Element;
import com.compuware.caqs.business.consult.ModelDefinition;
import com.compuware.caqs.business.security.User;
import com.compuware.caqs.constants.Constants;
import com.compuware.caqs.dao.factory.DaoFactory;
import com.compuware.caqs.dao.interfaces.ElementDao;
import com.compuware.caqs.domain.dataschemas.DialecteBean;
import com.compuware.caqs.domain.dataschemas.ElementBean;
import com.compuware.caqs.domain.dataschemas.ElementLinked;
import com.compuware.caqs.domain.dataschemas.StereotypeBean;
import com.compuware.caqs.domain.dataschemas.UsageBean;
import com.compuware.caqs.domain.dataschemas.UserBean;
import com.compuware.caqs.exception.DataAccessException;
import com.compuware.toolbox.util.logging.LoggerManager;

/**
 * @author cwfr-fdubois
 *
 */
public class AdminSvc {

    protected static org.apache.log4j.Logger logger = LoggerManager.getLogger(Constants.LOG4J_IHM_LOGGER_KEY);
    private static final AdminSvc instance = new AdminSvc();

    private AdminSvc() {
    }

    public static AdminSvc getInstance() {
        return instance;
    }

    public void moveElement(String son, String father) {
        ElementDao dao = DaoFactory.getInstance().getElementDao();
        dao.moveElement(son, father);
    }

    public ElementBean retrieveElement(String idElt) {
        Element element = new Element();
        return element.retrieveElement(idElt);
    }

    public Collection<UsageBean> retrieveAllModels() {
        ModelDefinition modelDefinition = new ModelDefinition();
        return modelDefinition.retrieveAllModels();
    }

    public Collection<DialecteBean> retrieveAllDialects() {
        ModelDefinition modelDefinition = new ModelDefinition();
        return modelDefinition.retrieveAllDialects();
    }

    public List<UserBean> retrieveAllUsersByElementId(String idElt) {
        User user = new User();
        return user.retrieveAllUsersByElementId(idElt);
    }

    public List<UserBean> retrieveAllUsersByFatherElementId(String idElt) {
        User user = new User();
        return user.retrieveAllUsersByFatherElementId(idElt);
    }

    public ElementBean retrieveFatherElement(String id) {
        Element element = new Element();
        return element.retrieveFatherElement(id);
    }

    /** Effacer logiquement un element
     * @param idElt Id de l'element � effacer
     * @param telt Identifiant du type de l'element a supprimer
     * @return true si la suppression a reussi, false sinon
     */
    public boolean delete(String id, String telt) {
        ProjectAdmin projectAdmin = new ProjectAdmin();
        return projectAdmin.delete(id, telt);
    }

    public void generateProject(String id, String lib, String desc) {
        ProjectAdmin projectAdmin = new ProjectAdmin();
        projectAdmin.generateProject(id, lib, desc);
    }

    public void updateProject(String id, String lib, String desc) {
        ProjectAdmin projectAdmin = new ProjectAdmin();
        projectAdmin.updateProject(id, lib, desc);
    }

    /**
     * cree la baseline d'instanciation pour un projet
     * @param idPro identifiant du projet
     */
    public void createBaseLineInstanciation(String idPro) {
        ProjectAdmin projectAdmin = new ProjectAdmin();
        projectAdmin.createBaseLine(idPro);
    }

    public ElementBean retrieveElementById(String id) {
        ProjectAdmin projectAdmin = new ProjectAdmin();
        return projectAdmin.retrieveElementById(id);
    }

    public void setElement(ElementBean eltBean) {
        ProjectAdmin projectAdmin = new ProjectAdmin();
        projectAdmin.setElement(eltBean);
    }

    public void setTreeElementLink(ElementBean eltBean, String fatherId) {
        ProjectAdmin projectAdmin = new ProjectAdmin();
        projectAdmin.setTreeElementLink(eltBean, fatherId);
    }

    public void updateChildrenRights(String id, String userList, String userId, boolean canModifyDomains, boolean canModifyAllProjets) {
        ProjectAdmin projectAdmin = new ProjectAdmin();
        projectAdmin.updateChildrenRights(id, userList, userId, canModifyDomains, canModifyAllProjets);
    }

    public void removeRightsToChildren(String id, String[] rights, String userId, boolean canModifyDomains, boolean canModifyAllProjets) {
        ProjectAdmin projectAdmin = new ProjectAdmin();
        projectAdmin.removeRightsToChildren(id, rights, userId, canModifyDomains, canModifyAllProjets);
    }

    public void updateRights(String id, String[] userArray) {
        User user = new User();
        user.updateRights(id, userArray);
    }

    public void clearCache(String idPro) {
        ProjectAdmin projectAdmin = new ProjectAdmin();
        projectAdmin.clearCache(idPro);
    }

    public StereotypeBean retrieveStereotype(String id) {
        ProjectAdmin projectAdmin = new ProjectAdmin();
        return projectAdmin.retrieveStereotype(id);
    }

    public void storeStereotype(StereotypeBean stereotypeBean) {
        ProjectAdmin projectAdmin = new ProjectAdmin();
        projectAdmin.storeStereotype(stereotypeBean);
    }

    /**
     * Retrieves project tree structures for a given project and user.
     * @param idPro a project ID.
     * @param idUser a user ID.
     * @return the project tree linked elements.
     */
    public ElementLinked retrieveProjectArboElements(String idPro, String idUser) {
        ElementLinked retour = null;
        ElementDao dao = DaoFactory.getInstance().getElementDao();
        try {
            retour = dao.retrieveProjectArboElements(idPro, idUser);
        } catch (DataAccessException e) {
            logger.error("retrieveProjectArboElements:" + e.getMessage());
        }
        return retour;
    }

    /**
     * insere un nouveau lien symbolique
     * @param fatherId l'element pere du lien
     * @param childId l'element fils du lien
     * @return <code>MessagesCode.NO_ERROR</code> s'il n'y a pas d'erreur, un
     * code d'erreur sinon.
     */
    public MessagesCodes createSymbolicLink(String fatherId, String childId) {
        ElementDao dao = DaoFactory.getInstance().getElementDao();
        return dao.createSymbolicLink(fatherId, childId);
    }

    /**
     * supprime un lien symbolique
     * @param fatherId l'element pere du lien
     * @param childId l'element fils du lien
     * @return <code>MessagesCode.NO_ERROR</code> s'il n'y a pas d'erreur, un
     * code d'erreur sinon.
     */
    public MessagesCodes deleteSymbolicLink(String fatherId, String childId) {
        MessagesCodes retour = MessagesCodes.NO_ERROR;
        ElementDao dao = DaoFactory.getInstance().getElementDao();
        try {
            dao.deleteSymbolicLink(fatherId, childId);
        } catch (DataAccessException exc) {
            retour = MessagesCodes.DATABASE_ERROR;
        }
        return retour;
    }
}
