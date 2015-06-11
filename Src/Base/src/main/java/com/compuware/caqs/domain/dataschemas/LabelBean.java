/*
 * Justificatif.java
 *
 * Created on 25 octobre 2002, 10:18
 */

package com.compuware.caqs.domain.dataschemas;

import java.io.Serializable;


/**
 *
 * @author  cwfr-fdubois
 */
public class LabelBean extends DefinitionBean implements Serializable {

    private static final long serialVersionUID = 3312915695547649126L;
	
	private BaselineBean mBline = null;
    private String mStatutLabel = null;
    private String mCuserLabel = null;
    private LabelBean mLabelLink = null;
    
    public LabelBean() {
    }
    
    public ProjectBean getProjet() {
        ProjectBean result = null;
        if (this.mBline != null) {
            result = this.mBline.getProject();
        }
        return result;
    }
    
    public BaselineBean getBaseline() {
        return mBline;
    }
    
    public String getStatut() {
        return mStatutLabel;
    }
    
    public String getUser() {
        return mCuserLabel;
    }
    
    public LabelBean getLabelLink() {
        return mLabelLink;
    }
        
    public void setBaseline(BaselineBean bline) {
        mBline = bline;
    }
    
    public void setStatut(String statutLabel) {
        mStatutLabel = statutLabel;
    }
    
    public void setUser(String user) {
        mCuserLabel = user;
    }
    
    public void setLabelLink(LabelBean labelLink) {
        mLabelLink = labelLink;
    }
        
}
