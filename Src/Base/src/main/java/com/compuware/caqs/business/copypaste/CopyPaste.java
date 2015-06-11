/**
 * 
 */
package com.compuware.caqs.business.copypaste;

import java.util.List;

import com.compuware.caqs.dao.factory.DaoFactory;
import com.compuware.caqs.dao.interfaces.ElementDao;
import com.compuware.caqs.domain.dataschemas.copypaste.CopyPasteBean;
import com.compuware.caqs.exception.CaqsException;

/**
 * @author cwfr-fdubois
 *
 */
public class CopyPaste {

	public List<CopyPasteBean> retrieveCopyPaste(String idElt, String idBline) throws CaqsException {
		DaoFactory daoFactory = DaoFactory.getInstance();
		ElementDao elementDao = daoFactory.getElementDao();
		return elementDao.retrieveCopyPaste(idElt, idBline);
	}

	public CopyPasteBean retrieveCopyPaste(String idCopy, String idElt, String idBline, int line) throws CaqsException {
		DaoFactory daoFactory = DaoFactory.getInstance();
		ElementDao elementDao = daoFactory.getElementDao();
		return elementDao.retrieveCopyPaste(idCopy, idElt, idBline, line);
	}
}
