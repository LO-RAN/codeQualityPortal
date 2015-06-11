/*
 * LinkInfos.java
 *
 * Created on 13 janvier 2003, 16:00
 */

package com.compuware.caqs.domain.dataschemas;

/**
 *
 * @author  cwfr-fdubois
 */
public class LinkInfos {
    
    String mIdEltFrom;
    String mLibEltFrom;
    String mDescEltFrom;
    String mLibPackFrom;
    String mIdEltTo;
    String mLibEltTo;
    String mDescEltTo;
    String mLibPackTo;
    
    /** Creates a new instance of LinkInfos */
    public LinkInfos(String idEltFrom, String libEltFrom,
            String descEltFrom, String libPackFrom, String idEltTo,
            String libEltTo, String descEltTo, String libPackTo) {
        mIdEltFrom = idEltFrom;
        mLibEltFrom = libEltFrom;
        mDescEltFrom = descEltFrom;
        mLibPackFrom = libPackFrom;
        mIdEltTo = idEltTo;
        mLibEltTo = libEltTo;
        mDescEltTo = descEltTo;
        mLibPackTo = libPackTo;
    }
    
    public String getIdEltFrom() {
        return mIdEltFrom;
    }
    
    public String getLibEltFrom() {
        return mLibEltFrom;
    }
    
    public String getDescEltFrom() {
        return mDescEltFrom;
    }
    
    public String getLibPackFrom() {
        return mLibPackFrom;
    }
    
    public String getIdEltTo() {
        return mIdEltTo;
    }
    
    public String getLibEltTo() {
        return mLibEltTo;
    }
    
    public String getDescEltTo() {
        return mDescEltTo;
    }
    
    public String getLibPackTo() {
        return mLibPackTo;
    }
    
}
