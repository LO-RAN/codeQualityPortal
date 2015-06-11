/**
 * 
 */
package com.compuware.caqs.service.copypaste;

import java.util.List;

import com.compuware.caqs.business.copypaste.CopyPaste;
import com.compuware.caqs.domain.dataschemas.copypaste.CopyPasteBean;
import com.compuware.caqs.exception.CaqsException;

/**
 * @author cwfr-fdubois
 *
 */
public class CopyPasteSvc {
	private static final CopyPasteSvc instance = new CopyPasteSvc();
	
	private CopyPasteSvc() {
	}
	
	public static CopyPasteSvc getInstance() {
		return instance;
	}
	
	public List<CopyPasteBean> retrieveCopyPaste(String idElt, String idBline) throws CaqsException {
		CopyPaste component = new CopyPaste();
		return component.retrieveCopyPaste(idElt, idBline);
	}
	
	public CopyPasteBean retrieveCopyPaste(String idCopy, String idElt, String idBline, int line) throws CaqsException {
		CopyPaste component = new CopyPaste();
		return component.retrieveCopyPaste(idCopy, idElt, idBline, line);
	}
	
}
