/*
 * JustificatifResume.java
 *
 * Created on 30 octobre 2002, 15:51
 */

package com.compuware.caqs.domain.dataschemas;

import java.util.Date;
import java.util.Locale;

import com.compuware.caqs.domain.dataschemas.i18n.I18nUtil;
import com.compuware.caqs.presentation.util.StringFormatUtil;
import com.compuware.toolbox.util.resources.DbmsResourceBundle;
import com.compuware.toolbox.util.resources.Internationalizable;

/**
 *
 * @author  cwfr-fdubois
 */
public class JustificatifResume implements Internationalizable {

    private String 	libPro;
    private String 	idJust;
    private String 	libJust;
    private Date 	dinstJust;
    private Date	dmajJust;
    private String 	justCuser;
    private String 	idBline;
    private String 	libBline;
    private String 	idCritfac;
    //private String 	libCritfac;
    private String  desc;
    private String 	idElt;
    private String 	libElt;
    private String 	idTelt;
    private int    	justMark;
    private int		oldMark;
    private boolean isFact;
    
    /** Creates a new instance of JustificatifResume */
    public JustificatifResume(String libpro, String idjust, String libjust, Date dinstjust, 
    		String justcuser, String idbline, String libbline, String idcritfac, 
    		String idelt, String libelt, String idtelt, Date dmajJust, boolean isfact) {
        libPro = libpro;
        idJust = idjust;
        libJust = libjust;
        dinstJust = dinstjust;
        justCuser = justcuser;
        idBline = idbline;
        libBline = libbline;
        idCritfac = idcritfac;
        idElt = idelt;
        libElt = libelt;
        idTelt = idtelt;
        this.isFact = isfact;
        this.dmajJust = dmajJust;
    }
    
    public String getEscapedProjetLabel(boolean quote, boolean bracket, boolean doubleQuote) {
    	return StringFormatUtil.escapeString(this.getProjetLabel(), quote, bracket, doubleQuote);
    }

    public String getProjetLabel() {
        return libPro;
    }
    
    public String getJustificatifId() {
        return idJust;
    }
    
    public String getEscapedJustificatifLibelle(boolean quote, boolean bracket, boolean doubleQuote) {
    	return StringFormatUtil.escapeString(this.getJustificatifLibelle(), quote, bracket, doubleQuote);
    }

    public String getJustificatifLibelle() {
        return libJust;
    }
    
    public Date getJustificatifDinst() {
        return dinstJust;
    }
    
    public String getEscapedJustificatifAuteur(boolean quote, boolean bracket, boolean doubleQuote) {
    	return StringFormatUtil.escapeString(this.getJustificatifAuteur(), quote, bracket, doubleQuote);
    }

    public String getJustificatifAuteur() {
        return justCuser;
    }
    
    public String getBaselineId() {
        return idBline;
    }
    
    public String getEscapedBaselineLabel(boolean quote, boolean bracket, boolean doubleQuote) {
    	return StringFormatUtil.escapeString(this.getBaselineLabel(), quote, bracket, doubleQuote);
    }

    public String getBaselineLabel() {
    	String result = libBline;
    	if (result == null) {
    		result = idBline;
    	}
        return result;
    }
    
    public String getCritfacId() {
        return idCritfac;
    }
    
    public String getCritfacLabel(Locale loc) {
    	String retour = "";
    	if(this.isFact) {
    		retour = DbmsResourceBundle.getString(this, I18nUtil.FACTEUR_BUNDLE_NAME, 
    				Internationalizable.LIB_PROPERTY_KEY, loc);
    	} else {
    		retour = DbmsResourceBundle.getString(this, I18nUtil.CRITERE_BUNDLE_NAME, 
    				Internationalizable.LIB_PROPERTY_KEY, loc);
    	}
        return retour;
    }
    
    public String getEscapedCritfacLabel(boolean quote, boolean bracket, boolean doubleQuote, Locale loc) {
    	return StringFormatUtil.escapeString(this.getCritfacLabel(loc), quote, bracket, doubleQuote);
    }

    public String getElementId() {
        return idElt;
    }
    
    public String getEscapedElementLabel(boolean quote, boolean bracket, boolean doubleQuote) {
    	return StringFormatUtil.escapeString(this.getElementLabel(), quote, bracket, doubleQuote);
    }

    public String getElementLabel() {
        return libElt;
    }
    
    public String getEscapedElementType(boolean quote, boolean bracket, boolean doubleQuote, Locale l) {
    	return StringFormatUtil.escapeString(this.getElementType(l), quote, bracket, doubleQuote);
    }

    public String getElementType(Locale l) {
    	ElementType et = new ElementType();
    	et.setId(this.idTelt);
    	return et.getLib(l);
    }

	public int getNoteJustificatif() {
		return justMark;
	}

	public void setNoteJustificatif(int noteJustificatif) {
		this.justMark = noteJustificatif;
	}

	public int getOldMark() {
		return oldMark;
	}

	public void setOldMark(int oldMark) {
		this.oldMark = oldMark;
	}
    
	public String getTextKey(String columnName) {
		String retour = "";
		if(this.isFact) {
			retour = I18nUtil.FACTEUR_BUNDLE_NAME + Internationalizable.KEY_SEPARATOR + 
			columnName + Internationalizable.KEY_SEPARATOR + this.idCritfac;
		} else {
			retour = I18nUtil.CRITERE_BUNDLE_NAME + Internationalizable.KEY_SEPARATOR + 
			columnName + Internationalizable.KEY_SEPARATOR + this.idCritfac;
		}
		return retour;
	}

	public String getComplement(Locale arg0) {
		return null;
	}

	public String getDesc(Locale arg0) {
		return null;
	}

	public String getId() {
		return null;
	}

	public String getLib(Locale arg0) {
		return null;
	}

	public Date getJustificatifDMaj() {
		return dmajJust;
	}

	public void setJustificatifDMaj(Date pDmajJust) {
		this.dmajJust = pDmajJust;
	}

	public String getJustificatifDesc() {
		return desc;
	}

	public void setJustificatifDesc(String pDesc) {
		this.desc = pDesc;
	}
	
	public Date getLastModifDate() {
		Date retour = this.getJustificatifDMaj();
		if(retour==null) {
			retour = this.getJustificatifDinst();
		}
		return retour;
	}

    public String getBundleName() {
        return null;
    }
}
