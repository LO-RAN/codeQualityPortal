/*
 * ProcessComparator.java
 *
 * Created on 1 avril 2003, 15:19
 */

package com.compuware.optimal.flow.webconsole;

import com.compuware.optimal.flow.Process;
import com.compuware.optimal.flow.WorkflowException;
import java.util.*;

/**
 *
 * @author  cwfr-fdubois
 */
public class ProcessComparator implements Comparator {
    
    /** Creates a new instance of ProcessComparator */
    public ProcessComparator() {
    }
    
    public int compare(Object o1, Object o2) {
        int result = 0;
        try {
            Date ct1 = ((Process)o1).getCreationTime();
            Date ct2 = ((Process)o2).getCreationTime();
            result = ct1.compareTo(ct2);
        }
        catch (WorkflowException e) {
            e.printStackTrace();
        }
        return -result;
    }
    
}
