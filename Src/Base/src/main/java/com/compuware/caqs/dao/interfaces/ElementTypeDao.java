/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.compuware.caqs.dao.interfaces;

import com.compuware.caqs.domain.dataschemas.CriterionDefinition;
import com.compuware.caqs.domain.dataschemas.ElementType;
import com.compuware.caqs.domain.dataschemas.modeleditor.ElementTypeBean;
import com.compuware.caqs.exception.DataAccessException;
import java.util.List;
import java.util.Map;

/**
 *
 * @author cwfr-dzysman
 */
public interface ElementTypeDao {

    /**
     * retrieves element types by id and/or lib
     * @param id tool's id pattern
     * @param lib tool's lib pattern
     * @param idLang language id
     * @return list of element types
     */
    public abstract List<ElementType> retrieveElementTypesByIdLib(String id, String lib, String idLang);


    /**
     * retrieves a element type by id
     * @param id element type's id pattern
     * @return a element type or null
     */
    public ElementTypeBean retrieveElementTypeById(String id);

    /**
     * saves an element type, updating it if it already exists, creating it otherwise
     * @param et the element type to save
     * @throws com.compuware.caqs.exception.DataAccessException
     */
    public void saveElementTypeBean(ElementTypeBean et) throws DataAccessException;

    /**
     * deletes an element type
     * @param id the id of the element type to delete
     * @throws com.compuware.caqs.exception.DataAccessException
     */
    public void deleteElementTypeBean(String id) throws DataAccessException;

    /**
     * retrieves all element types
     * @return all element types
     */
    public List<ElementType> retrieveAllElementTypes();

    /**
     * retrieves all criterions by models associated to an element type.
     * @param idTelt the element type
     * @return all criterions by models associated to an element type
     */
    public Map<String, List<CriterionDefinition>> retrieveAssociatedModelsAndCriterionsForElementType(String idTelt);
}
