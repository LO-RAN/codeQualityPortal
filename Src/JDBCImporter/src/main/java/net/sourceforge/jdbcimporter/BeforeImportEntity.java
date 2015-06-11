package net.sourceforge.jdbcimporter;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author cwfr-dzysman
 */
public class BeforeImportEntity {

    private String query;
    private List params;

    public void setQuery(String q) {
        this.query = q;
    }

    public String getQuery() {
        return this.query;
    }

    public void setParameters(String p) {
        List l = new ArrayList();
        if (p != null) {
            String[] s = p.split(",");
            if (s != null) {
                for (int i = 0; i < s.length; i++) {
                    Integer integer = Integer.valueOf(s[i]);
                    l.add(integer);
                }
            }
        }
        this.params = l;
    }

    public List getParameters() {
        return this.params;
    }
}
