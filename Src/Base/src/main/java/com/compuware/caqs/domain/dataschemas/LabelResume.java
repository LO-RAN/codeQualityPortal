/*
 * LabelResume.java
 *
 * Created on 30 octobre 2002, 15:51
 */

package com.compuware.caqs.domain.dataschemas;

import java.io.Serializable;
import java.util.Date;
import java.util.Locale;

import com.compuware.caqs.presentation.util.StringFormatUtil;


/**
 *
 * @author  cwfr-fdubois
 */
public class LabelResume implements Serializable {

    private static final long serialVersionUID = 4980012567649821601L;
	
	private String mIdPro;
    private String mLibPro;
    private String mIdLabel;
    private String mLibLabel;
    private Date mDinstLabel;
    private String mLabelCuser;
    private String mIdBline;
    private String mLibBline;
    private String mIdElt;
    private String mLibElt;
    private String mIdTelt;
    
    /** Creates a new instance of LabelResume */
    public LabelResume(String idLabel, String libLabel,
            Date dinstLabel, String idPro, String libPro,
            String labelCuser, String idBline, String libBline,
            String idElt, String libElt, String idTelt) {
        mIdPro = idPro;
        mLibPro = libPro;
        mIdLabel = idLabel;
        mLibLabel = libLabel;
        mDinstLabel = dinstLabel;
        mLabelCuser = labelCuser;
        mIdBline = idBline;
        mLibBline = libBline;
        mIdElt = idElt;
        mLibElt = libElt;
        mIdTelt = idTelt;
    }
    
    public String getProjet() {
        return mIdPro;
    }
    
    public String getEscapedProjetLabel(boolean quote, boolean bracket, boolean doubleQuote) {
    	return StringFormatUtil.escapeString(this.getProjetLabel(), quote, bracket, doubleQuote);
    }

    public String getProjetLabel() {
        return mLibPro;
    }
    
    public String getLabelId() {
        return mIdLabel;
    }
    
    public String getEscapedLabelLibelle(boolean quote, boolean bracket, boolean doubleQuote) {
    	return StringFormatUtil.escapeString(this.getLabelLibelle(), quote, bracket, doubleQuote);
    }

    public String getLabelLibelle() {
    	String result = mLibLabel;
    	if (result == null) {
    		result = mIdLabel;
    	}
    	return result;
    }
    
    public Date getLabelDinst() {
        return mDinstLabel;
    }
    
    public String getEscapedLabelAuteur(boolean quote, boolean bracket, boolean doubleQuote) {
    	return StringFormatUtil.escapeString(this.getLabelAuteur(), quote, bracket, doubleQuote);
    }

    public String getLabelAuteur() {
        return mLabelCuser;
    }
    
    public String getBaselineId() {
        return mIdBline;
    }
    
    public String getEscapedBaselineLabel(boolean quote, boolean bracket, boolean doubleQuote) {
    	return StringFormatUtil.escapeString(this.getBaselineLabel(), quote, bracket, doubleQuote);
    }

    public String getBaselineLabel() {
        return mLibBline;
    }
    
    public String getElementId() {
        return mIdElt;
    }
    
    public String getEscapedElementLabel(boolean quote, boolean bracket, boolean doubleQuote) {
    	return StringFormatUtil.escapeString(this.getElementLabel(), quote, bracket, doubleQuote);
    }

    public String getElementLabel() {
        return mLibElt;
    }
    
    public String getEscapedElementType(boolean quote, boolean bracket, boolean doubleQuote, Locale l) {
    	return StringFormatUtil.escapeString(this.getElementType(l), quote, bracket, doubleQuote);
    }

    public String getElementType(Locale l) {
    	ElementType et = new ElementType();
    	et.setId(mIdTelt);
    	return et.getLib(l);
    }
    
}
