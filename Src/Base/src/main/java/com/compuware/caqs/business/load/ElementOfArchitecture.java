/*
 * ElementOfArchitecture.java
 *
 * Created on 10 mars 2003, 11:47
 * @author  cwfr-fxalbouy
 */

package com.compuware.caqs.business.load;

public class ElementOfArchitecture {
    protected String mId;
    protected String mDesc;
    protected String mIdArchiMod;
    
    /** Creates a new instance of ElementOfArchitecture */
    public ElementOfArchitecture(String id,String desc,String idArchiMod) {
        this.mId = id;
        this.mDesc = desc;
        this.mIdArchiMod = idArchiMod;
    }
    
    public String getId(){
        return this.mId;
    }
    
    public String getDesc(){
        return this.mDesc;
    }
    
    public String getIdArchiMod(){
        return this.mIdArchiMod;
    }
    
}
