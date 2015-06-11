package com.compuware.caqs.dao.interfaces;

import java.sql.Connection;
import java.util.Collection;
import java.util.List;

import com.compuware.caqs.domain.dataschemas.BaselineBean;
import com.compuware.caqs.domain.dataschemas.CriterionPerFactorBean;
import com.compuware.caqs.domain.dataschemas.ElementBean;
import com.compuware.caqs.domain.dataschemas.ElementFactorBaseline;
import com.compuware.caqs.domain.dataschemas.FactorBean;
import com.compuware.caqs.domain.dataschemas.FactorElementBean;
import com.compuware.caqs.domain.dataschemas.ProjectBean;
import com.compuware.caqs.domain.dataschemas.UsageBean;
import com.compuware.caqs.domain.dataschemas.calcul.Facteur;
import com.compuware.caqs.domain.dataschemas.list.FactorBeanCollection;
import com.compuware.caqs.domain.dataschemas.modeleditor.ModelEditorFactorBean;
import com.compuware.caqs.exception.DataAccessException;

public interface FactorDao {

    Collection<FactorBean> retrieveAllFactorDefinitions();

    /**
     * retrieves a list of factors by theirs model's id
     * @param idUsage model's id
     * @return list of factors
     */
    List<FactorBean> retrieveFactorDefinitionByUsage(String idUsage);

    /**
     * retrieves a list of factors by theirs model's id
     * @param idModel model's id
     * @return list of factors
     */
    List<FactorBean> retrieveGoalsListByModel(String idModel);

    /**
     * retrieves a list of factors which are not associated to a model
     * @param idModel model's id
     * @param filterId filter on goal's id
     * @param filterLib filter on goal's lib
     * @param idLoc locale id
     * @return list of factors
     */
    List<FactorBean> retrieveGoalsNotAssociatedToModel(String idModel, String filterId, String filterLib, String idLoc);

    List<FactorBean> retrieveFactorsByElementBaseline(String idBline,
            String idPro, String idElt, String idUsa);

    FactorBeanCollection retrieveFactorsByElementBaseline(
            String idBline, String idPro, String idElt);

    /**
     * Renvoie un FactorBean complete avec les informations sur l'objectif,
     * la note et la labellisation, si elle existe
     * @param idBline identifiant de baseline
     * @param idPro identifiant de projet
     * @param idElt identifiant de l'element pour lequel on veut la note
     * @param idFac identifiant de l'objectif
     * @return Renvoie un FactorBean complete avec les informations sur l'objectif,
     * la note et la labellisation, si elle existe
     */
    FactorBean retrieveFactorAndJustByIdElementBaseline(
            String idBline, String idPro, String idElt, String idFac);

    /**
     * Retrieve factors and justifications for subelements (SSP or EA).
     * @param idBline the baseline ID.
     * @param idPro the project ID.
     * @param idElt the current element ID.
     * @param idFac the factor ID.
     * @return factors and justifications for subelements.
     */
    Collection<FactorElementBean> retrieveFactorAndJustForSubElts(String idBline,
            String idPro, String idElt, String idFac);

    List<CriterionPerFactorBean> retrieveFactorCritereDef(String idBline, String idElt);

    List<ElementFactorBaseline> retrieveAllElementFactor();

    void updateFacteurDataBase(Collection<Facteur> factorColl, BaselineBean baseline, ProjectBean projet, String idElt, Connection connection) throws DataAccessException;

    /**
     * Retrieve factors for a given element.
     * @param eltBean the tree structure element (project, sub-project or entity)
     * @return the element factor collection.
     * @throws DataAccessException
     */
    public List<FactorBean> retrieveFactorList(ElementBean eltBean) throws DataAccessException;

    public double retrieveAverageFactorMarkForElement(String idElt, String idBline);

    /**
     * renvoie l'union de l'ensemble des objectifs pour les elements envoyes en parametre
     * @param elements elements
     * @return renvoie l'union de l'ensemble des objectifs pour les elements envoyes en parametre
     */
    public List<FactorBean> retrieveAllFactorsForElements(List<ElementBean> elements);

    /**
     * met a jour le commentaire place sur un objectif. l'element envoye en parametre
     * doit avoir une baseline.
     * @param elt l'element pour lequel commenter le critere
     * @param idFac l'identifiant de l'objectif a commenter
     * @param comment le commentaire
     */
    public void updateCommentForFactor(ElementBean elt, String idFac, String comment) throws DataAccessException;

    /**
     * Retrieve goals by id
     * @param id the pattern to search for the id
     * @param lib the pattern to search for the lib
     * @param idLoc le language id
     * @return the retrieved goals, if any, null otherwise
     */
    public List<FactorBean> retrieveFactorsByIdAndLib(String id, String lib, String idLoc);

    /**
     * saves a goal, updating it if it already exists, creating it otherwise
     * @param goal the goal to save
     * @throws com.compuware.caqs.exception.DataAccessException
     */
    public void saveFactorBean(FactorBean goal) throws DataAccessException;

    /**
     * deletes a goal
     * @param id the id of the goal to delete
     * @throws com.compuware.caqs.exception.DataAccessException
     */
    public void deleteFactorBean(String id) throws DataAccessException;

    /**
     * retrieves a goal with the number of models using it
     * @param id the goal id
     * @return the goal
     */
    public ModelEditorFactorBean retrieveFactorByIdWithAssociatedModelsCount(String id);

    /**
     * renvoie tous les objectifs associes a un element, pour une baseline
     * @param idElt element
     * @param idPro identifiant du projet de l'element
     * @param idBline baseline
     * @return tous les objectifs associes a un element, pour une baseline
     */
    public List<FactorBean> retrieveAllFactorsForElement(String idElt, String idPro, String idBline);

    /**
     * retrieves all models using goal
     * @param idGoal goal id
     * @return model's list
     */
    public List<UsageBean> retrieveAllModelsAssociatedToGoal(String idGoal);

    /**
     * retrieves goals' evolutions between two baselines
     * @param idEa ea
     * @param idBline first bline
     * @param idPrevBline previous bline
     * @return evolutions
     */
    public List<FactorBean> retrieveGoalsEvolutions(String idEa, String idBline, String idPrevBline);
}
