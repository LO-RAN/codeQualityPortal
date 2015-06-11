/*
 * BaselineBean.java
 *
 * Created on 23 janvier 2004, 11:27
 */

package com.compuware.caqs.domain.dataschemas;

import java.io.Serializable;

/**
 *
 * @author  cwfr-fdubois
 */
public class BaselineBean extends DefinitionBean implements Serializable {
    
    private static final long serialVersionUID = 8693603433437373506L;
	
    private boolean mIsLastBline = false;
    private ProjectBean mProject = null;
    private boolean labelised = false;
    
    /** Creates a new instance of BaselineBean */
    public BaselineBean() {
    }
    
    public void setProject(ProjectBean project) {
        mProject = project;
    }
    
    public ProjectBean getProject() {
        return this.mProject;
    }
    
    public void setIsLastBaseline(boolean isLastBaseline) {
        this.mIsLastBline = isLastBaseline;
    }
    
    public boolean getIsLastBaseline() {
        return this.mIsLastBline;
    }
    
    public boolean isLabelised() {
        return labelised;
    }

    public void setLabelised(boolean labelised) {
        this.labelised = labelised;
    }
    
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("[");
        buffer.append(super.toString());
        buffer.append("]");
        return buffer.toString();
    }
    
}
