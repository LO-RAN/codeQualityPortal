package com.compuware.caqs.dao.interfaces;

import com.compuware.caqs.domain.dataschemas.actionplan.ActionPlanUnit;
import com.compuware.caqs.exception.DataAccessException;
import java.util.List;

public interface ActionPlanUnitDao {

    /**
     * retrieves action plan units by id and/or lib
     * @param id
     * @param lib
     * @param idLang
     * @return list of action plan units
     */
    public List<ActionPlanUnit> retrieveActionPlanUnitByIdLib(String id,
            String lib, String idLang);

    /**
     * deletes an action plan unit
     * @param id
     * @throws com.compuware.caqs.exception.DataAccessException
     */
    public void deleteActionPlanUnit(String id) throws DataAccessException;

    /**
     * saves an action plan unit, creating it if it does not exist
     * @param apu the action plan unit to save
     * @throws com.compuware.caqs.exception.DataAccessException
     */
    public void saveActionPlanUnit(ActionPlanUnit apu) throws DataAccessException;

    /**
     * retrieves an action plan unit by id
     * @param id the id
     * @return the action plan unit or null if it does not exist
     */
    public ActionPlanUnit retrieveActionPlanUnitById(String id);

    /**
     * retrieves all action plan's units
     * @return all action plan's units
     */
    public List<ActionPlanUnit> retrieveAllActionPlanUnits();

}
