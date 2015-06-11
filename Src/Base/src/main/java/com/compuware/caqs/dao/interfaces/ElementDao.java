package com.compuware.caqs.dao.interfaces;

import com.compuware.caqs.constants.MessagesCodes;
import com.compuware.caqs.domain.dataschemas.treemap.TreeMapElementBean;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.compuware.caqs.business.load.db.DataFileType;
import com.compuware.caqs.domain.dataschemas.BaselineBean;
import com.compuware.caqs.domain.dataschemas.ElementBean;
import com.compuware.caqs.domain.dataschemas.ElementLinked;
import com.compuware.caqs.domain.dataschemas.ElementRetrieveFilterBean;
import com.compuware.caqs.domain.dataschemas.ElementType;
import com.compuware.caqs.domain.dataschemas.ProjectBean;
import com.compuware.caqs.domain.dataschemas.analysis.EA;
import com.compuware.caqs.domain.dataschemas.calcul.Volumetry;
import com.compuware.caqs.domain.dataschemas.copypaste.CopyPasteBean;
import com.compuware.caqs.exception.DataAccessException;

public interface ElementDao {

    public abstract void createBaseLine(String idPro, java.sql.Date dateInst);

    public abstract void generateProject(String idPro, String libPro,
            String descPro, String usagePro, int isObject,
            java.sql.Date dateInst, java.sql.Date dateMaj);

    public abstract ElementBean retrieveElementById(java.lang.String id);

    /**
     * returns the application entity of the element'id
     * @param id the element'id to search for
     * @return elementbean
     */
    public abstract ElementBean retrieveApplicationEntityByElementId(String id);

    /**
     * 
     * @param desc
     * @param idEA
     * @return
     */
    public abstract ElementBean retrieveElementByDesc(String desc, String idEA);

    /**
     * renvoie un elementbean selon un libelle et un identifiant de projet
     * @param lib libelle selon lequel rechercher
     * @param idEa identifiant de l'entite applicative
     * @return l'element
     */
    public abstract ElementBean retrieveElementByLib(String lib, String idEa);

    public abstract ElementBean retrieveFatherElement(java.lang.String id);

    public abstract ElementBean retrieveMainElement(java.lang.String id);

    public abstract ElementBean retrieveAllElementDataById(java.lang.String id);

    public abstract Collection<ElementBean> retrieveProjects();

    /**
     * retrieve all elements for which the userid has access
     * @param idUser user id
     * @param idTelt elements type to retrieve
     * @return elements list
     */
    public abstract Collection<ElementBean> retrieveElements(String idUser, String idTelt);

    public abstract Collection<ElementBean> retrieveElementByType(String idPro,
            String idTelt);

    /**
     * Retrieve subelement of an EA for a given type.
     * @param idElt the EA id.
     * @param idTelt the given element type.
     * @return subelement of the EA for the given type.
     */
    public abstract Collection<ElementBean> retrieveSubElementByType(String idElt,
            String idTelt);

    public abstract Collection<ElementBean> retrieveElementByType(String idPro,
            String idTelt, Timestamp dmajBline);

    public abstract Collection<ElementBean> retrieveSubElement(String idElt);

    /**
     * returns the father element
     * @param idElt element'id for which to search the father
     * @return father element
     */
    public abstract ElementBean retrieveSuperElement(String idElt);

    public abstract List<ElementBean> retrieveSubElements(String idElt,
            Timestamp dmajBline);

    public abstract List<ElementBean> retrieveRecursiveSubElements(String idElt,
            BaselineBean baseline);

    /**
     * Retrieve the number of subelements by type for a given baseline.
     * @param dinstBline the baseline instanciation date.
     * @param dmajBline the baseline update date.
     * @param idElt the main element ID.
     * @return the number of subelements by type for a given baseline.
     */
    public abstract Map<String, Integer> retrieveTotalElements(
            Timestamp dinstBline,
            Timestamp dmajBline, String idElt);

    /**
     * Retrieve the number of created subelements by type for a given baseline.
     * @param dinstBline the baseline instanciation date.
     * @param dmajBline the baseline update date.
     * @param idElt the main element ID.
     * @return the number of created subelements by type for a given baseline.
     */
    public abstract Map<String, Integer> retrieveCreatedElements(
            Timestamp dinstBline,
            Timestamp dmajBline, String idElt);

    /**
     * Retrieve the number of deleted subelements by type for a given baseline.
     * @param dinstBline the baseline instanciation date.
     * @param dmajBline the baseline update date.
     * @param idElt the main element ID.
     * @return the number of deleted subelements by type for a given baseline.
     */
    public abstract Map<String, Integer> retrieveDeletedElements(
            Timestamp dinstBline,
            Timestamp dmajBline, String idElt);

    /**
     * Store the element volumetry collection.
     * @param volumetryCollection the volumetry collection.
     * @param idBline the current baseline.
     */
    public abstract void setElementVolumetry(Collection<Volumetry> volumetryCollection, String idBline) throws DataAccessException;

    /**
     * Retrieve the volumetry for a given EA by type (total, created, deleted).
     * @param idElt the EA ID.
     * @param idBline the baseline ID.
     * @return a volumetry for each type in a list.
     */
    public abstract List<Volumetry> retrieveVolumetry(String idElt, String idBline);

    /**
     * Retrieve all elements contained in the given application.
     * @param idMainElt the application ID.
     * @return The collection of all elements contained in the given application.s
     */
    public abstract Collection<ElementBean> retrieveExistingElement(String idMainElt);

    /** Effacer logiquement un element
     * @param idElt Id de l'element � effacer
     * @param telt Identifiant du type de l'element a supprimer
     * @return true si la suppression a reussi, false sinon
     */
    public abstract boolean delete(String idElt, String telt);

    public abstract void setElement(ElementBean eltBean);

    /** Enleve les droits de tous les utilisateurs associ� � l'element
     * @param idElt Id de l'element
     */
    public abstract void deleteUsersRights(String idElt);

    /** Positionne les droits des utilisateurs sp�cifi� � l'element choisi
     * @param idElt Id de l'element
     * @param userList Liste d'utilisateurs
     */
    public abstract void updateUsersRights(String idElt, String userList);

    /** Mise � jour des droits pour les fils d'un element
     * @param idFather Id de l'element p�re
     * @param userList Liste d'utilisateurs ayant les droits pour les elements fils
     * @param userId Identifiant de l'utilisateur demandant la mise � jour
     */
    public abstract void updateChildrenRights(String idFather, String userList, String userId, boolean canModifyDomains, boolean canModifyAllProjets);

    public abstract ElementBean retrieveUnknownElement(String eltDesc,
            String eaId, boolean createIfNotFound);

    public abstract ElementBean retrieveUnknownElement(ElementBean eltBean,
            String eaId, boolean createIfNotFound);
    public static final String TREE_LINK = "T";
    public static final String LEAF_LINK = "L";

    public abstract void setTreeElementLink(ElementBean eltBean, String fatherId);

    public abstract void setLeafElementLink(ElementBean eltBean, String fatherId);

    /**
     * Set expiration date for elements.
     * @param eltBeanColl the element collection.
     * @param typeList the supported type list. Only element of type contained into the list could expire.
     */
    public abstract void setOutOfDateElements(Collection<ElementBean> eltBeanColl, List<String> typeList);

    /**
     * Retrieve all project tree structures.
     * @return the list of project tree linked elements.
     */
    public abstract List<ElementLinked> retrieveProjectArboElements();

    /**
     * Retrieve project tree structures for a given user.
     * @param idUser a user ID.
     * @return the list of project tree linked elements.
     */
    public abstract List<ElementLinked> retrieveProjectArboElements(String idUser);

    /**
     * Retrieve project tree structures for a given project, baseline and user.
     * @param idEA a project ID.
     * @param idBline a baseline ID.
     * @param idUser a user ID.
     * @return the list of project tree linked elements.
     */
    public abstract List<ElementLinked> retrieveProjectArboElements(String idPro, String idBline, String idUser);

    /**
     * Retrieve project tree structures for a given project and user.
     * @param idEA a project ID.
     * @param idUser a user ID.
     * @return the project tree linked elements.
     */
    public abstract ElementLinked retrieveProjectArboElements(String idPro, String idUser)
            throws DataAccessException;

    /**
     * Recherche les sous elements d'une entite applicative pour la gestion des stereotypes.
     * @param idEa L'identifiant de l'entite applicative mere.
     * @param filter Le filtre sur les elements recherches.
     * @param connection La connexion utilisee.
     * @return la collection des sous-elements correspondant aux criteres de recherche.
     */
    public Collection<ElementBean> retrieveSubElementForStereotypeMgmt(String idEa, ElementRetrieveFilterBean filter);

    /**
     * Set a stereotype for an element collection.
     * @param elementCollection the given element collection.
     * @param idStereotype the given stereotype.
     * @param connection the DBMS connection used.
     */
    public void setElementStereotype(List<String> elementCollection, String idStereotype);

    /**
     * Retrieve all element types for a given EA. 
     * @param idMainElt the EA ID.
     * @return the list of found types.
     */
    public List<ElementType> retrieveSubElementTypes(String idMainElt);

    public List<ElementLinked> retrieveSubElementLinks(String idMainElt, Timestamp dmajBline);

    /**
     * retrieves all children elements for the element given as parameter, for which
     * the user given as parameter has the rights to access.
     * @param idEltPere element's id
     * @param idUser user id
     * @return children elements
     */
    public List<ElementLinked> retrieveAllChildrenElements(String idEltPere,
            String idUser);

    public List<ElementLinked> retrieveAllChildrenElements(String idEltPere);

    public boolean elementHasBaselines(String idElt);

    public void moveElement(String son, String father);

    public List<ElementLinked> retrieveProjectArboElementsOneLevel(String idPro,
            String idBline, String idElt, String idUser);

    public void updateElementDate(String idElt) throws DataAccessException;

    public void updateProject(String idPro, String libPro,
            String descPro, java.sql.Date dateMaj);

    public void removeRightsToChildren(String id, String[] rights,
            String userId, boolean canModifyDomains,
            boolean canModifyAllProjets);

    public abstract void insertCopyPaste(List<CopyPasteBean> copyPasteList,
            String idBline) throws DataAccessException;

    public abstract List<CopyPasteBean> retrieveCopyPaste(String idElt,
            String idBline) throws DataAccessException;

    public abstract CopyPasteBean retrieveCopyPaste(String idCopy, String idElt,
            String idBline, int line) throws DataAccessException;

    /**

     * @return all element types available
     * @throws DataAccessException
     */
    public List<ElementType> retrieveAllElementTypes();

    /**
     * @param idEa Application Entity's id
     * @return
     */
    public List<ElementType> retrieveAllElementTypesForApplicationEntity(
            String idEa);

    public abstract List<EA> retrieveProjectSubElements(
            String projectId, String[] eaArray,
            DataFileType wantedType) throws DataAccessException;

    public String retrieveParentPathByLib(String idElt);

    /**
     * returns a string containing all parents id, separated by a '/', without the entrypoint,
     * or "ENTRYPOINT" if the element is under hte entrypoint
     * @param idElt starting point
     * @return string containing all parents id, separated by a '/', without the entrypoint,
     * or "ENTRYPOINT" if the element is under hte entrypoint
     */
    public String retrieveParentPathById(String idElt);

    /**
     * @param idParent l'identifiant du pere
     * @param idUser l'identifiant de l'utilisateur
     * @param idTelt le type d'element a rechercher
     * @return la liste des elements fils de idParent, pour lequels idUser est habilite, de type element idTelt
     */
    public List<ElementBean> retrieveAllElementsForTypeBelongingToParentByUser(String idParent, String idUser, String idTelt);

    /**
     * retrieves all application entities for provided project
     * @param project project
     * @return all application entities
     */
    public List<ElementBean> retrieveAllApplicationEntitiesForProject(ElementBean project);

    /**
     * retrieves all application entities for provided project and baseline
     * @param project project
     * @param baseline baseline
     * @return all application entities
     */
    public List<ElementBean> retrieveAllApplicationEntitiesForProjectAndBaseline(ProjectBean project, BaselineBean baseline);

    /**
     *
     * @param root l'identifiant de l'element racine
     * @param leafType le type d'element qui sera une feuille
     * @param idFac l'identifiant de l'objectif pour lequel recuperer la note ou
     * Constants.ALL_FACTORS pour la note moyenne de tous les objectifs
     * @param idUser identifiant de l'utilisateur accredite
     * @return l'element racine avec sa note et sa hierarchie
     */
    public TreeMapElementBean retrieveMarkedTree(
            String root, String leafType, String idFac, String idUser);

    /**
     *
     * @param root l'identifiant de l'element racine
     * @param baseline baseline
     * @param idFac l'identifiant de l'objectif pour lequel recuperer la note ou
     * Constants.ALL_FACTORS pour la note moyenne de tous les objectifs
     * @param idUser identifiant de l'utilisateur accredite
     * @return l'element racine avec sa note et sa hierarchie
     */
    public TreeMapElementBean retrieveProjectMarkedTree(
            String root, BaselineBean baseline, String idFac, String idUser);

    /**
     * renvoie la volumetrie des elements de type "fichier"
     * @param eas Les entites applicatives pour lesquelles rechercher
     * @param onlyFileElements true si on ne recherche que pour les types d'element "fichier"
     * @return volumetrie des elements de type "fichier"
     */
    public List<Volumetry> retrieveFileElementsVolumetry(
            List<ElementBean> eas,
            boolean onlyFileElements);

    /**
     * renvoie le nombre d'elements de type "fichier" pour un element donne
     * @param eas Les entites applicatives pour lesquelles rechercher
     * @param onlyFileElements true si on ne recherche que pour les types d'element "fichier"
     * @return nombre d'elements de type "fichier" pour un element donne
     */
    public int retrieveGlobalNumberOfElements(List<ElementBean> eas,
            boolean onlyFileElements);

    /**
     * renvoie la somme des ifpug des eas envoyees en parametre.
     * @param eas Les entites applicatives pour lesquelles rechercher
     * @return somme des ifpug des eas
     */
    public int retrieveGlobalIFPUG(List<ElementBean> eas);

    /**
     * insere un nouveau lien symbolique
     * @param fatherId l'element pere du lien
     * @param childId l'element fils du lien
     * @return <code>MessagesCode.NO_ERROR</code> s'il n'y a pas d'erreur, un
     * code d'erreur sinon.
     */
    public MessagesCodes createSymbolicLink(String fatherId, String childId);

    /**
     * supprime un lien symbolique
     * @param fatherId l'element pere du lien
     * @param childId l'element fils du lien
     */
    public void deleteSymbolicLink(String fatherId, String childId) throws DataAccessException;

    /**
     * Renvoie tous les elements perimes dont l'element pere ne l'est pas.
     * @return La liste des elements concernes.
     */
    public List<ElementLinked> retrieveAllPeremptedRootsElements();

    /**
     * Renvoie tous les elements perimes dont l'element pere ne l'est pas.
     * @param idUser identifiant de l'utilisateur
     * @return La liste des elements concernes.
     */
    public List<ElementLinked> retrieveAllPeremptedRootsElements(String idUser);

    /**
     * Retire la date de peremption sur l'element envoye en parametre et sur sa
     * sous-arborescence
     * @param idElt identifiant de l'element a "de-perimer"
     */
    public void restoreAllPeremptedElementsTree(String idElt) throws DataAccessException;

    /**
     * Supprime physiquement un element de la base de donnees.  <code>MessagesCode.NO_ERROR</code> s'il n'y a pas d'erreur, un
     * code d'erreur sinon.
     * @param idElt l'element a supprimer
     */
    public void deletePeremptedElement(String idElt) throws DataAccessException;

    /**
     * Renvoie tous les sous-elements perimes
     * @param idElt l'element pere
     * @return la liste des sous-element de l'element pere qui sont perimes
     */
    public List<ElementBean> retrieveAllPeremptedSubElements(String idElt);

    /**
     * Supprime les donnees relatives aux analyses pour le projet envoye en parametre
     * @param idElt l'identifiant de l'element representant le projet
     */
    public void deleteProjectAnalysises(String idElt) throws DataAccessException;

    /**
     *
     */
    public ElementBean retrieveNotPeremptedElementById(java.lang.String id, String userId);

    /**
     * retrieves all eas using a model, by project id
     * @param idUsa model to search for
     * @return all eas using the model
     */
    public Map<String, List<ElementBean>> retrieveAllApplicationEntitiesForModel(String idUsa);

    /**
     * retrieves all eas using a dialecte, by project id
     * @param idDialecte dialecte to search for
     * @return all eas using the dialecte
     */
    public Map<String, List<ElementBean>> retrieveAllApplicationEntitiesForDialecte(String idDialecte);
}
