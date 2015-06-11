/*
 * File.java
 *
 * Created on 1 octobre 2002, 17:45
 */
package com.compuware.caqs.business.load.db;
/**
 *
 * @author  fxa
 */
public class DataFile {
	
	private DataFileType mType=null;
    private String mPath=null;
    private String mTool=null;
    private boolean mCreateLinkWithEa = true;
    
    /** Creates a new instance of File */
    public DataFile(DataFileType type, String path, boolean createLinkWithEa) {
           this.mType = type;
           this.mPath = path;
           this.mCreateLinkWithEa = createLinkWithEa;
    }
    
    public DataFile(DataFileType type, String path, String tool, boolean createLinkWithEa) {
           this.mType = type;
           this.mPath = path;
           this.mTool = tool;
           this.mCreateLinkWithEa = createLinkWithEa;
    }
    
    public DataFileType getType(){
        return this.mType;
    }
    
    public String getPath(){
        return this.mPath;
    }
    
    public String getTool(){
        return this.mTool;
    }
    
    public boolean isCreateLinkWithEa() {
    	return this.mCreateLinkWithEa;
    }
    
}
