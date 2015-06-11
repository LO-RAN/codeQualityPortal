/*
 * TxtValidityErrorCollection.java
 *
 * Created on 3 juin 2003, 10:23
 */

package com.compuware.caqs.business.calculation.xmlimpl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Properties;

import com.compuware.caqs.domain.calculation.rules.Operand;
import com.compuware.caqs.domain.calculation.rules.ValidityError;
import com.compuware.caqs.domain.calculation.rules.ValidityErrorCollection;

/**
 *
 * @author  cwfr-fdubois
 */
public class TxtValidityErrorCollection extends ArrayList<ValidityError> implements ValidityErrorCollection {
    
    /**
	 * SerialVersionUID. 
	 */
	private static final long serialVersionUID = -658873889954435494L;
	
	Properties mMessages = null;
    
    /** Creates a new instance of TxtValidityErrorCollection */
    public TxtValidityErrorCollection(Properties prop) {
        this.mMessages = prop;
    }
    
    public void addError(Operand location, String errorKey) {
        ValidityError err = new ValidityError(errorKey, location);
        this.add(err);
    }
    
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        if (this.isEmpty()) {
            buffer.append(mMessages.getProperty(TxtValidityErrorCollection.NO_ERROR));
        }
        else {
            Iterator<ValidityError> i = this.iterator();
            while (i.hasNext()) {
                ValidityError err = i.next();
                buffer.append(err.toString(this.mMessages)+"\n");
            }
        }
        return buffer.toString();
    }
    
}
