/*
 * Justificatif.java
 *
 * Created on 25 octobre 2002, 10:18
 */

package com.compuware.caqs.domain.dataschemas;

import java.io.Serializable;
import java.util.Date;

import com.compuware.caqs.util.IDCreator;

/**
 *
 * @author  cwfr-fdubois
 */
public class Label implements Serializable {
    
    private static final long serialVersionUID = -7388162436538693704L;
	
	private String mIdLabel;
    private String mIdPro;
    private String mIdBline;
    private String mLibLabel;
    private String mDescLabel;
    private String mStatutLabel;
    private String mCuserLabel;
    private Date mDinstLabel;
    private Date mDmajLabel;
    private String mLabelLink;
    private Label mLabelOld;
    
    /** Creates a new instance of Justificatif */
    public Label(String idPro, String idBline) {
        mIdLabel = IDCreator.getID();
        mIdPro = idPro;
        mIdBline = idBline;
        mStatutLabel = "DEMAND";
        mLibLabel = "";
        mDescLabel = "";
    }
    
    public Label(String libLabel, String idPro, String idBline,
            String descLabel, String statutLabel,
            String cuserLabel, String labelLink) {
        mIdLabel = IDCreator.getID();
        mIdPro = idPro;
        mIdBline = idBline;
        mLibLabel = libLabel;
        mDescLabel = descLabel;
        mStatutLabel = statutLabel;
        mCuserLabel = cuserLabel;
        mLabelLink = labelLink;
    }
    
    /** Creates a new instance of Justificatif */
    public Label(String idLabel, String libLabel, String idPro, String idBline,
            String descLabel, String statutLabel, String cuserLabel,
            Date dinstLabel, Date dmajLabel, String labelLink) {
        mIdLabel = idLabel;
        mLibLabel = libLabel;
        mIdPro = idPro;
        mIdBline = idBline;
        mDescLabel = descLabel;
        mStatutLabel = statutLabel;
        mCuserLabel = cuserLabel;
        mDinstLabel = dinstLabel;
        mDmajLabel = dmajLabel;
        mLabelLink = labelLink;
    }

    public String getId() {
        return mIdLabel;
    }
    
    public String getLabel() {
        return mLibLabel;
    }

    public String getProjet() {
        return mIdPro;
    }
    
    public String getBaseline() {
        return mIdBline;
    }
    
    public String getDescription() {
        return mDescLabel;
    }
    
    public String getStatut() {
        return mStatutLabel;
    }
    
    public String getUser() {
        return mCuserLabel;
    }
    
    public Date getDinst() {
        return mDinstLabel;
    }
    
    public Date getDmaj() {
        return mDmajLabel;
    }
    
    public String getLabelLink() {
    	return this.mLabelLink;
    }
    
    public Label getLabelOld() {
        return mLabelOld;
    }
    
    public void setLabelOld(Label old) {
        mLabelOld = old;
    }
    
}
