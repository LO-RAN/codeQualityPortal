/*
  * DataFileType.java
  *
  * Created on 21 janvier 2005, 11:56
  */

package com.compuware.caqs.business.load.db;

/**
  *
  * @author  cwfr-fdubois
  */
public enum DataFileType {
    
    EA ("EA"),
    CLS ("CLS"),
    MET ("MET"),
    ALL ("ALL"),
    TASK ("TASK"),
    LINKS_INTERFACEMETHODS ("INTERFACEMETHODS"),
    LINKS_CLASSESMETHODS ("CLASSESMETHODS"),
    LINKS_INHERITANCE ("INHERITANCE");
    
    
    private String name = null;
    
    /** Creates a new instance of DataFileType */
    private DataFileType(String name) {
    	this.name = name;
    }
    
    public String getName() {
    	return this.name;
    }
    
    public static DataFileType fromName(String val) {
    	DataFileType result = null;
    	if (val.equalsIgnoreCase(EA.getName())) {
    		result = EA;
    	}
    	else if (val.equalsIgnoreCase(CLS.getName())) {
    		result = CLS;
    	}
    	else if (val.equalsIgnoreCase(MET.getName())) {
    		result = MET;
    	}
    	else if (val.equalsIgnoreCase(TASK.getName())) {
    		result = TASK;
    	}
    	else if (val.equalsIgnoreCase(ALL.getName())) {
    		result = ALL;
    	}
    	else if (val.equalsIgnoreCase(LINKS_INTERFACEMETHODS.getName())) {
    		result = LINKS_INTERFACEMETHODS;
    	}
    	else if (val.equalsIgnoreCase(LINKS_CLASSESMETHODS.getName())) {
    		result = LINKS_CLASSESMETHODS;
    	}
    	else if (val.equalsIgnoreCase(LINKS_INHERITANCE.getName())) {
    		result = LINKS_INHERITANCE;
    	}
    	return result;
    }
    
}
