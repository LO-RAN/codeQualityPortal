/**
 * 
 */
package com.compuware.caqs.business.consult;

import com.compuware.caqs.dao.factory.DaoFactory;
import com.compuware.caqs.dao.interfaces.ElementDao;
import com.compuware.caqs.domain.dataschemas.ElementBean;

/**
 * @author cwfr-fdubois
 *
 */
public class Element {

    public ElementBean retrieveElement(String idElt) {
        DaoFactory daoFactory = DaoFactory.getInstance();
        ElementDao elementFacade = daoFactory.getElementDao();
        return elementFacade.retrieveAllElementDataById(idElt);
    }

    public ElementBean retrieveFatherElement(String idElt) {
        DaoFactory daoFactory = DaoFactory.getInstance();
        ElementDao elementFacade = daoFactory.getElementDao();
        return elementFacade.retrieveFatherElement(idElt);
    }
}
