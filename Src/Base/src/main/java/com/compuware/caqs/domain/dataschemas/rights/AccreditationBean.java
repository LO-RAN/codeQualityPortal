/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.compuware.caqs.domain.dataschemas.rights;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author cwfr-dzysman
 */
public class AccreditationBean {
    private String rightId;
    private String rightLib;
    private Map<String, Boolean> rights;

    public AccreditationBean() {
        this.rights = new HashMap<String, Boolean>();
    }

    public String getRightId() {
        return rightId;
    }

    public void setRightId(String rightId) {
        this.rightId = rightId;
    }

    public String getRightLib() {
        return rightLib;
    }

    public void setRightLib(String rightLib) {
        this.rightLib = rightLib;
    }

    public Map<String, Boolean> getRights() {
        return rights;
    }

    public void setRights(Map<String, Boolean> rights) {
        this.rights = rights;
    }
}
