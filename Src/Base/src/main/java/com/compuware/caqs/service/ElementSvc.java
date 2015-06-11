/**
 * 
 */
package com.compuware.caqs.service;

import com.compuware.caqs.constants.Constants;
import com.compuware.caqs.constants.MessagesCodes;
import com.compuware.caqs.domain.dataschemas.calcul.Volumetry;
import java.util.Collection;
import java.util.List;

import com.compuware.caqs.dao.factory.DaoFactory;
import com.compuware.caqs.dao.interfaces.BaselineDao;
import com.compuware.caqs.dao.interfaces.ElementDao;
import com.compuware.caqs.domain.dataschemas.BaselineBean;
import com.compuware.caqs.domain.dataschemas.ElementBean;
import com.compuware.caqs.domain.dataschemas.ElementLinked;
import com.compuware.caqs.domain.dataschemas.ElementType;
import com.compuware.caqs.domain.dataschemas.ProjectBean;
import com.compuware.caqs.exception.CaqsException;
import com.compuware.caqs.exception.DataAccessException;
import com.compuware.toolbox.util.logging.LoggerManager;
import java.util.Iterator;
import java.util.Map;

/**
 * @author cwfr-fdubois
 *
 */
public class ElementSvc {

    protected static org.apache.log4j.Logger mLog = LoggerManager.getLogger(Constants.LOG4J_ANALYSIS_LOGGER_KEY);
    private static ElementSvc instance = new ElementSvc();

    private ElementSvc() {
    }

    public static ElementSvc getInstance() {
        return instance;
    }
    private DaoFactory daoFactory = DaoFactory.getInstance();

    /**
     * Retrieve element informations icluding project, baseline, model
     * @param idElt the element ID.
     * @param idBline the baseline ID.
     * @return the element found.
     */
    public ElementBean retrieveElement(String idElt, String idBline) throws CaqsException {
        BaselineDao baselineFacade = daoFactory.getBaselineDao();
        String realIdBline = baselineFacade.retrieveLinkedBaseline(idElt, idBline);
        if (realIdBline == null) {
            realIdBline = idBline;
        }
        BaselineBean blineBean = baselineFacade.retrieveBaselineAndProjectById(realIdBline);
        ElementDao elementFacade = daoFactory.getElementDao();
        ElementBean eltBean = elementFacade.retrieveElementById(idElt);
        eltBean.setBaseline(blineBean);
        return eltBean;
    }

    public ElementBean retrieveApplicationEntityByElementId(String id) {
        return daoFactory.getElementDao().retrieveApplicationEntityByElementId(id);
    }

    /**
     * Retrieve sub element distinct types.
     * @param idElt the current element ID.
     * @return a list of types.
     */
    public List<ElementType> retrieveSubElementTypes(String idElt) {
        ElementDao elementFacade = daoFactory.getElementDao();
        return elementFacade.retrieveSubElementTypes(idElt);
    }

    public ElementBean retrieveElementById(String idElt) {
        ElementDao elementFacade = daoFactory.getElementDao();
        ElementBean eltBean = elementFacade.retrieveElementById(idElt);
        return eltBean;
    }

    public ElementBean retrieveNotPeremptedElementById(String idElt, String userId) {
        ElementDao elementFacade = daoFactory.getElementDao();
        ElementBean eltBean = elementFacade.retrieveNotPeremptedElementById(idElt, userId);
        return eltBean;
    }

    public ElementBean retrieveAllElementDataById(String idElt) {
        ElementDao elementFacade = daoFactory.getElementDao();
        ElementBean eltBean = elementFacade.retrieveAllElementDataById(idElt);
        return eltBean;
    }

    public List<ElementLinked> retrieveProjectArboElementsOneLevel(String idPro, String idBline, String idElt, String idUser) {
        ElementDao elementFacade = daoFactory.getElementDao();
        return elementFacade.retrieveProjectArboElementsOneLevel(idPro, idBline, idElt, idUser);
    }

    /**
     * retrieves all children elements for the element given as parameter, for which
     * the user given as parameter has the rights to access.
     * @param idEltPere element's id
     * @param idUser user id
     * @return children elements
     */
    public List<ElementLinked> retrieveAllChildrenElements(String idEltPere, String idUser) {
        ElementDao elementFacade = daoFactory.getElementDao();
        return elementFacade.retrieveAllChildrenElements(idEltPere, idUser);
    }

    public List<ElementLinked> retrieveAllChildrenElements(String idEltPere) {
        ElementDao elementFacade = daoFactory.getElementDao();
        return elementFacade.retrieveAllChildrenElements(idEltPere);
    }

    /**
     * retrieve all elements for which the userid has access
     * @param idUser user id
     * @param idTelt elements type to retrieve
     * @return elements list
     */
    public Collection<ElementBean> retrieveElements(String idUser, String idTelt) {
        ElementDao elementFacade = daoFactory.getElementDao();
        return elementFacade.retrieveElements(idUser, idTelt);
    }

    public List<ElementLinked> retrieveProjectArboElements() {
        ElementDao elementFacade = daoFactory.getElementDao();
        return elementFacade.retrieveProjectArboElements();
    }

    public List<ElementLinked> retrieveProjectArboElements(String userId) {
        ElementDao elementFacade = daoFactory.getElementDao();
        return elementFacade.retrieveProjectArboElements(userId);
    }

    public List<ElementLinked> retrieveProjectArboElements(String idPro, String idBline, String idUser) {
        ElementDao elementFacade = daoFactory.getElementDao();
        return elementFacade.retrieveProjectArboElements(idPro, idBline, idUser);
    }

    public boolean elementHasBaselines(String idElt) {
        ElementDao elementFacade = daoFactory.getElementDao();
        return elementFacade.elementHasBaselines(idElt);
    }

    /**
     * @return all element types available
     */
    public List<ElementType> retrieveAllElementTypes() {
        ElementDao elementFacade = daoFactory.getElementDao();
        return elementFacade.retrieveAllElementTypes();
    }

    public List<ElementType> retrieveAllElementTypesForApplicationEntity(String idEa) {
        ElementDao elementFacade = daoFactory.getElementDao();
        return elementFacade.retrieveAllElementTypesForApplicationEntity(idEa);
    }

    public String retrieveParentPathByLib(String idElt) {
        ElementDao elementFacade = daoFactory.getElementDao();
        return elementFacade.retrieveParentPathByLib(idElt);
    }

    /**
     * returns a string containing all parents id, separated by a '/', without the entrypoint,
     * or "ENTRYPOINT" if the element is under hte entrypoint
     * @param idElt starting point
     * @return string containing all parents id, separated by a '/', without the entrypoint,
     * or "ENTRYPOINT" if the element is under hte entrypoint
     */
    public String retrieveParentPathById(String idElt) {
        ElementDao elementFacade = daoFactory.getElementDao();
        return elementFacade.retrieveParentPathById(idElt);
    }

    /**
     * @param idParent l'identifiant du pere
     * @param idUser l'identifiant de l'utilisateur
     * @param idTelt le type d'element a rechercher
     * @return la liste des elements fils de idParent, pour lequels idUser est habilite, de type element idTelt
     */
    public List<ElementBean> retrieveAllElementsForTypeBelongingToParentByUser(String idDomain, String idUser, String idTelt) {
        ElementDao elementFacade = daoFactory.getElementDao();
        return elementFacade.retrieveAllElementsForTypeBelongingToParentByUser(idDomain, idUser, idTelt);
    }

    /**
     * @param idParent l'identifiant du pere
     * @param idUser l'identifiant de l'utilisateur
     * @param idTelt le type d'element a rechercher
     * @param retrieveLastBaseline true pour recuperer la derniere baseline de l'element, false sinon
     * @return la liste des elements fils de idParent, pour lequels idUser est habilite, de type element idTelt.
     * chaque element est initialise avec sa derniere baseline, s'il en a une
     */
    public List<ElementBean> retrieveAllElementsForTypeBelongingToParentByUser(
            String idDomain, String idUser, String idTelt,
            boolean retrieveLastBaseline) {
        BaselineSvc baselineSvc = BaselineSvc.getInstance();
        ElementDao elementFacade = daoFactory.getElementDao();
        List<ElementBean> l = elementFacade.retrieveAllElementsForTypeBelongingToParentByUser(idDomain, idUser, idTelt);
        if (retrieveLastBaseline) {
            for (Iterator<ElementBean> it = l.iterator(); it.hasNext();) {
                ElementBean project = it.next();
                BaselineBean bb = baselineSvc.getLastBaseline(project);
                if (bb != null) {
                    bb.setProject(project.getProject());
                    project.setBaseline(bb);
                } else {
                    it.remove();
                }
            }
        }
        return l;
    }

    /**
     * retrieves all application entities for provided project
     * @param project project
     * @return all application entities
     */
    public List<ElementBean> retrieveAllApplicationEntitiesForProject(ElementBean project) {
        ElementDao elementFacade = daoFactory.getElementDao();
        List<ElementBean> retour = elementFacade.retrieveAllApplicationEntitiesForProject(project);
        for (Iterator<ElementBean> it = retour.iterator(); it.hasNext();) {
            ElementBean ea = it.next();
            BaselineBean bb = BaselineSvc.getInstance().getLastBaseline(ea);
            if (bb != null) {
                bb.setProject(ea.getProject());
                ea.setBaseline(bb);
            } else {
                it.remove();
            }
        }
        return retour;
    }

    /**
     * renvoie la volumetrie des elements de type "fichier"
     * @param eas Les entites applicatives pour lesquelles rechercher
     * @param onlyFileElements true si on ne recherche que pour les types d'element "fichier"
     * @return volumetrie des elements de type "fichier"
     */
    public List<Volumetry> retrieveFileElementsVolumetry(
            List<ElementBean> eas,
            boolean onlyFileElements) {
        ElementDao elementFacade = daoFactory.getElementDao();
        return elementFacade.retrieveFileElementsVolumetry(eas, onlyFileElements);
    }

    /**
     * renvoie le nombre d'elements de type "fichier" pour un element donne
     * @param eas Les entites applicatives pour lesquelles rechercher
     * @param onlyFileElements true si on ne recherche que pour les types d'element "fichier"
     * @return nombre d'elements de type "fichier" pour un element donne
     */
    public int retrieveGlobalNumberOfElements(
            List<ElementBean> eas,
            boolean onlyFileElements) {
        ElementDao elementFacade = daoFactory.getElementDao();
        return elementFacade.retrieveGlobalNumberOfElements(eas, onlyFileElements);
    }

    /**
     * renvoie la somme des ifpug des eas envoyees en parametre.
     * @param eas Les entites applicatives pour lesquelles rechercher
     * @return somme des ifpug des eas
     */
    public int retrieveGlobalIFPUG(List<ElementBean> eas) {
        ElementDao elementFacade = daoFactory.getElementDao();
        return elementFacade.retrieveGlobalIFPUG(eas);
    }

    /**
     * Renvoie tous les elements perimes dont l'element pere ne l'est pas.
     * @param idUser identifiant de l'utilisateur
     * @return La liste des elements concernes.
     */
    public List<ElementLinked> retrieveAllPeremptedRootsElements(String idUser) {
        ElementDao elementFacade = daoFactory.getElementDao();
        return elementFacade.retrieveAllPeremptedRootsElements(idUser);
    }

    /**
     * Renvoie tous les elements perimes dont l'element pere ne l'est pas.
     * @return La liste des elements concernes.
     */
    public List<ElementLinked> retrieveAllPeremptedRootsElements() {
        ElementDao elementFacade = daoFactory.getElementDao();
        return elementFacade.retrieveAllPeremptedRootsElements();
    }

    public MessagesCodes deletePeremptedElement(String idElt) {
        MessagesCodes retour = MessagesCodes.NO_ERROR;
        ElementDao elementFacade = daoFactory.getElementDao();
        ElementBean eltBean = this.retrieveElementById(idElt);
        if (eltBean != null) {
            try {
                if (ElementType.EA.equals(eltBean.getTypeElt())) {
                    //si c'est une ea, on ne peut supprimer que l'element
                    elementFacade.deletePeremptedElement(idElt);
                } else if (ElementType.SSP.equals(eltBean.getTypeElt())) {
                    //si c'est un ssp, on ne peut supprimer que l'element et ses fils
                    List<ElementBean> fils = elementFacade.retrieveAllPeremptedSubElements(idElt);
                    for (ElementBean elt : fils) {
                        retour = this.deletePeremptedElement(elt.getId());
                        if (retour != MessagesCodes.NO_ERROR) {
                            break;
                        }
                    }
                    elementFacade.deletePeremptedElement(idElt);
                } else if (ElementType.DOMAIN.equals(eltBean.getTypeElt())) {
                    //pareil que pour un ssp pour l'instant.
                    //je laisse le code distingue du ssp pour quand cela changera
                    //si c'est un ssp, on ne peut supprimer que l'element et ses fils
                    List<ElementBean> fils = elementFacade.retrieveAllPeremptedSubElements(idElt);
                    for (ElementBean elt : fils) {
                        this.deletePeremptedElement(elt.getId());
                        if (retour != MessagesCodes.NO_ERROR) {
                            break;
                        }
                    }
                    elementFacade.deletePeremptedElement(idElt);
                } else if (ElementType.PRJ.equals(eltBean.getTypeElt())) {
                    Collection<BaselineBean> baselines = BaselineSvc.getInstance().retrieveAllBaselineByProjectId(eltBean.getProject().getId());
                    try {
                        for (BaselineBean bb : baselines) {
                            BaselineSvc.getInstance().purge(eltBean.getProject().getId(),
                                    bb.getId());
                        }
                        //les baselines sont supprimees
                        List<ElementBean> fils = elementFacade.retrieveAllPeremptedSubElements(idElt);
                        for (ElementBean elt : fils) {
                            this.deletePeremptedElement(elt.getId());
                            if (retour != MessagesCodes.NO_ERROR) {
                                break;
                            }
                        }
                        elementFacade.deletePeremptedElement(idElt);
                    } catch (CaqsException exc) {
                        mLog.error("error while deleting project's baseline : id_pro="
                                + eltBean.getProject().getId(), exc);
                        retour = MessagesCodes.DATABASE_ERROR;
                    }
                }
            } catch (DataAccessException exc) {
                retour = MessagesCodes.DATABASE_ERROR;
            }
        }
        return retour;
    }

    /**
     * Retire la date de peremption sur l'element envoye en parametre et sur sa
     * sous-arborescence
     * @param idElt identifiant de l'element a "de-perimer"
     * @return <code>MessagesCode.NO_ERROR</code> s'il n'y a pas d'erreur, un
     * code d'erreur sinon.
     */
    public MessagesCodes restoreAllPeremptedElementsTree(String idElt) {
        MessagesCodes retour = MessagesCodes.NO_ERROR;
        ElementDao elementFacade = daoFactory.getElementDao();
        try {
            elementFacade.restoreAllPeremptedElementsTree(idElt);
        } catch (DataAccessException exc) {
            retour = MessagesCodes.DATABASE_ERROR;
        }
        return retour;
    }

    /**
     * retrieves all application entities for provided project and baseline
     * @param project project
     * @param baseline baseline
     * @return all application entities
     */
    public List<ElementBean> retrieveAllApplicationEntitiesForProjectAndBaseline(ProjectBean project, BaselineBean baseline) {
        List<ElementBean> retour = daoFactory.getElementDao().retrieveAllApplicationEntitiesForProjectAndBaseline(project, baseline);
        for (ElementBean elt : retour) {
            BaselineBean bb = BaselineSvc.getInstance().getRealBaseline(elt, baseline.getId());
            elt.setBaseline(bb);
        }
        return retour;
    }

    /**
     * retrieves all eas using a model, by project id
     * @param idDialecte model to search for
     * @return all eas using the model
     */
    public Map<String, List<ElementBean>> retrieveAllApplicationEntitiesForModel(String idUsa) {
        return DaoFactory.getInstance().getElementDao().retrieveAllApplicationEntitiesForModel(idUsa);
    }

    /**
     * retrieves all eas using a dialect, by project id
     * @param idDialecte dialect to search for
     * @return all eas using the dialect
     */
    public Map<String, List<ElementBean>> retrieveAllApplicationEntitiesForDialecte(String idDialecte) {
        return DaoFactory.getInstance().getElementDao().retrieveAllApplicationEntitiesForDialecte(idDialecte);
    }

    /**
     * returns the father element
     * @param idElt element'id for which to search the father
     * @return father element
     */
    public ElementBean retrieveSuperElement(String idElt) {
        return DaoFactory.getInstance().getElementDao().retrieveSuperElement(idElt);
    }

    /**
     *
     * @param desc
     * @param idEA
     * @return
     */
    public ElementBean retrieveElementByDesc(String desc, String idEA) {
        return DaoFactory.getInstance().getElementDao().retrieveElementByDesc(desc, idEA);
    }
}
