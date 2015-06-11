/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.compuware.caqs.dao.interfaces;

import com.compuware.caqs.domain.dataschemas.modeleditor.LangueBean;
import com.compuware.caqs.exception.DataAccessException;
import java.util.List;

/**
 *
 * @author cwfr-dzysman
 */
public interface LangueDao {

    /**
     * retrieves languages by id and/or lib
     * @param id languages's id pattern
     * @param lib languages's lib pattern
     * @param idLang language id
     * @return list of languages
     */
    public abstract List<LangueBean> retrieveLanguesByIdLib(String id, String lib);

    /**
     * retrieves a language by id
     * @param id language's id pattern
     * @return a language or null
     */
    public LangueBean retrieveLangueById(String id);

    /**
     * saves a languebean, updating it if it already exists, creating it otherwise
     * @param lb the languebean to save
     * @throws com.compuware.caqs.exception.DataAccessException
     */
    public void saveLangueBean(LangueBean lb) throws DataAccessException;

    /**
     * deletes a languebean
     * @param id the id of the languebean to delete
     * @throws com.compuware.caqs.exception.DataAccessException
     */
    public void deleteLangueBean(String id) throws DataAccessException;

    /**
     * retrieves all languages
     * @return all languages or an empty list if none exists
     * @throws com.compuware.caqs.exception.DataAccessException
     */
    public List<LangueBean> retrieveAllLanguages();
}
