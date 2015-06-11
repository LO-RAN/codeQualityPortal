/*
 * ElementBean.java
 *
 * Created on 1 d�cembre 2003, 10:40
 */

package com.compuware.caqs.domain.dataschemas;

import com.compuware.caqs.business.util.StringFormatUtil;
import java.io.Serializable;
import java.text.NumberFormat;
import java.util.Locale;

/**
 *
 * @author  cwfr-fdubois
 */
public class SyntheseCorrectionBean implements Serializable {
    
    /**
	 * 
	 */
	private static final long serialVersionUID = -2917408037534618937L;

	private int mNbEltRej = 0;
    
    private int mNbEltRes = 0;

    private int mNbEltAcc = 0;
    
    private int mNbCorrRej = 0;
    
    private int mNbCorrRes = 0;

    private int mNbCorrTotal = 0;
    
    private NumberFormat nf = null;
    
    /** Creates a new instance of SyntheseCorrectionBean */
    public SyntheseCorrectionBean() {
        nf = StringFormatUtil.getIntegerFormatter(Locale.getDefault());
    }

    /** Creates a new instance of SyntheseCorrectionBean */
    public SyntheseCorrectionBean(Locale locale) {
        nf = StringFormatUtil.getIntegerFormatter(locale);
    }

    public int getNbEltsRejets() {
        return mNbEltRej;
    }
    
    public int getNbEltsReserve() {
        return mNbEltRes;
    }
    
    public int getNbEltsAccepte() {
        return mNbEltAcc;
    }
    
    public String getPctEltsCorr() {
        double result = 0.0;
        if ((mNbEltRej + mNbEltRes + mNbEltAcc) != 0) {
            result = (mNbEltRej + mNbEltRes)*100/(mNbEltRej + mNbEltRes + mNbEltAcc);
        }
        return nf.format(result);
    }
    
    public int getNbCorrRejets() {
        return mNbCorrRej;
    }
    
    public String getPctCorrRejets() {
        double result = 0.0;
        if (mNbCorrTotal != 0) {
            result = mNbCorrRej*100/mNbCorrTotal;
        }
        return nf.format(result);
    }
    
    public String getPctCorrReserve() {
        double result = 0.0;
        if (mNbCorrTotal != 0) {
            result = mNbCorrRes*100/mNbCorrTotal;
        }
        return nf.format(result);
    }
    
    public int getNbCorrReserve() {
        return mNbCorrRes;
    }
    
    public int getNbCorrTotal() {
        return mNbCorrTotal;
    }
    
    public void setNbEltsRejets(int nbEltRej) {
        mNbEltRej = nbEltRej;
    }
    
    public void setNbEltsReserve(int nbEltRes) {
        mNbEltRes = nbEltRes;
    }
    
    public void setNbEltsAccepte(int nbEltAcc) {
        mNbEltAcc = nbEltAcc;
    }
    
    public void setNbCorrRejets(int nbCorrRej) {
        mNbCorrRej = nbCorrRej;
    }
    
    public void setNbCorrReserve(int nbCorrRes) {
        mNbCorrRes = nbCorrRes;
    }
    
    public void setNbCorrTotal(int nbCorrTotal) {
        mNbCorrTotal += nbCorrTotal;
    }
    
    public void addNbEltsRejets(int nbEltRej) {
        mNbEltRej += nbEltRej;
    }
    
    public void addNbEltsReserve(int nbEltRes) {
        mNbEltRes += nbEltRes;
    }
    
    public void addNbEltsAccepte(int nbEltAcc) {
        mNbEltAcc += nbEltAcc;
    }
    
    public void addNbCorrRejets(int nbCorrRej) {
        mNbCorrRej += nbCorrRej;
    }
    
    public void addNbCorrReserve(int nbCorrRes) {
        mNbCorrRes += nbCorrRes;
    }
    
    public void addNbCorrTotal(int nbCorrTotal) {
        mNbCorrTotal += nbCorrTotal;
    }
    
}
