package com.compuware.caqs.dao.interfaces;

import java.util.Collection;
import java.util.List;

import com.compuware.caqs.domain.dataschemas.DialecteBean;
import com.compuware.caqs.domain.dataschemas.LanguageBean;
import com.compuware.caqs.domain.dataschemas.modeleditor.ModelEditorDialecteBean;
import com.compuware.caqs.domain.dataschemas.modeleditor.ModelEditorLanguageBean;
import com.compuware.caqs.exception.DataAccessException;

/**
 * The dialect and laguage DAO.
 * @author cwfr-fdubois
 */
public interface DialecteDao {

    /**
     * renvoie tous les dialectes
     * @return une collection contenant tous les dialectes
     */
    public Collection<DialecteBean> retrieveAllDialectes();

    /**
     * le dialecte d'un element
     * @param elementId identifiant de l'element pour lequel il faut rechercher
     * le dialecte
     * @return un DialecteBean dument rempli ou <code>null</code> s'il n'y a pas
     * de dialecte associe
     */
    public DialecteBean retrieveDialecteByElementId(java.lang.String elementId);

    /**
     * Retrieve the list of distinct languages used by the given module elements.
     * @param eltList the given modules elements as an array of ID.
     * @return the list of distinct languages used by the given modules.
     */
    public List<DialecteBean> retrieveDialects(String[] eltList);

    public List<LanguageBean> retrieveLanguages();

    /**
     * retrieves dialects by id and/or lib
     * @param id id
     * @param lib lib
     * @return dialects
     */
    public List<DialecteBean> retrieveDialectesByIdLib(String id, String lib);

    /**
     * retrieve one dialect, by id, with the number of associated eas
     * @param id dialect's id
     * @return one dialect, by id, with the number of associated eas
     */
    public ModelEditorDialecteBean retrieveDialectWithAssociationCountById(String id);

    public void deleteDialecteBean(String id) throws DataAccessException;

    public void saveDialecteBean(DialecteBean dialecte) throws DataAccessException;

    /**
     * retrieves languages by id and/or lib
     * @param id id
     * @param lib lib
     * @return languages
     */
    public List<LanguageBean> retrieveLanguagesByIdLib(String id, String lib);

    /**
     * retrieve one dialect, by id, with the number of associated eas
     * @param id dialect's id
     * @return one dialect, by id, with the number of associated eas
     */
    public ModelEditorLanguageBean retrieveLanguageWithAssociationCountById(String id);

    public void deleteLanguageBean(String id) throws DataAccessException;

    public void saveLanguageBean(LanguageBean dialecte) throws DataAccessException;

    public List<DialecteBean> retrieveDialectsByLanguages(String idLangage);
}
