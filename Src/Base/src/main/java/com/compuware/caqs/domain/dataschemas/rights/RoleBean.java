package com.compuware.caqs.domain.dataschemas.rights;

import com.compuware.caqs.domain.dataschemas.DefinitionBean;

public class RoleBean extends DefinitionBean implements Comparable {
    private static final long serialVersionUID = 1L;
	
    @Override
    public boolean equals(Object role) {
        boolean result = false;
        if (role != null && role instanceof RoleBean && this.id != null) {
            result = id.equals(((RoleBean) role).getId());
        }
        return result;
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    public int compareTo(Object role) {
          int result = 0;
        if (role != null) {
            if (this.lib != null) {
                result = this.lib.compareTo(((RoleBean) role).getLib());
            } else if (this.id != null) {
                result = this.id.compareTo(((RoleBean) role).getId());
            }
        }
        return result;
  }
}
