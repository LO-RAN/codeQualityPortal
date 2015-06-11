package com.compuware.carscode.plugin.deventreprise.dataschemas;

import com.compuware.toolbox.util.logging.LoggerManager;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;

public abstract class CobolSource {

    static protected Logger logger = LoggerManager.getLogger("StaticAnalysis");

    protected int id;
    protected String name;
    protected Map<String, Object> violations = null;
    protected String collection;
    protected int loc = 0;
    protected boolean hasTabs = false;
    protected String likeName = null;
    protected List<Variable> variables = null;
    private File parentDirectory;
    private CobolPackage cobolPackage;
    protected List<Copy> includedCopies = null;

    protected CobolSource(String name, String collection, CobolPackage cp) {
        this.violations = new HashMap<String, Object>();
        this.name = name;
        this.collection = collection;
        this.parentDirectory = cp.getCobolPackageDirectory();
        this.cobolPackage = cp;
        this.includedCopies = new ArrayList<Copy>();
    }

    public String getLikeName() {
        return likeName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Map<String, Object> getViolations() {
        return this.violations;
    }

    abstract public String getName();

    public String getElementName() {
        return name;
    }

    public String getElementDesc() {
        return this.cobolPackage.getCobolPackage() + '.' + getName();
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCollection() {
        return collection;
    }

    public void setCollection(String collection) {
        this.collection = collection;
    }

    public int getLoc() {
        return loc;
    }

    public void setLoc(int loc) {
        this.loc = loc;
    }

    public boolean isHasTabs() {
        return hasTabs;
    }

    public void setHasTabs(boolean hasTabs) {
        this.hasTabs = hasTabs;
    }

    public void addViolation(String idViolation, Violation v) {
        Object o = violations.get(idViolation);
        List<Violation> l = null;
        if (o == null) {
            l = new ArrayList<Violation>();
            violations.put(idViolation, l);
        } else {
            l = (List<Violation>) o;
        }
        boolean found = false;
        for (Violation a : l) {
            if (a.getLine().equals(v.getLine())) {
                found = true;
                break;
            }
        }
        if (!found) {
            l.add(v);
        }
    }

    public void setViolation(String idViolation, Violation v) {
        Integer i = null;
        try {
            i = Integer.decode(v.getLine());
            this.violations.put(idViolation, i);
        }
        catch (NumberFormatException e) {
            logger.error(idViolation + ":" + v.getLine());
        }
    }

    public StringBuffer toStringBuffer() {
        StringBuffer bf = new StringBuffer();
        for (Iterator<Map.Entry<String, Object>> it = this.violations.entrySet().iterator(); it.hasNext();) {
            Map.Entry<String, Object> entry = it.next();
            Object value = entry.getValue();
            if (value instanceof Integer) {
                Integer i = (Integer) value;
                if (i.intValue() > 0) {
                    bf.append("    <metric id=\"" + entry.getKey() +
                            "\" value=\"" + i.intValue() + "\" />\n");
                }
            } else if (value instanceof List) {
                List<Violation> l = (List<Violation>) value;
                if (l.size() > 0) {
                    bf.append("    <metric id=\"" + entry.getKey() +
                            "\" value=\"" + l.size() + "\" lines=\"");
                    boolean first = true;
                    for (Violation v : l) {
                        if (!first) {
                            bf.append(",");
                        }
                        if (!"".equals(v.getLine())) {
                            bf.append(v.getLine());
                        }
                        /*try {
                        Integer i = Integer.decode(s);
                        retour.append(i.toString());
                        } catch(NumberFormatException e) {
                        logger.error(entry.getKey()+":"+e.getMessage());
                        }*/
                        first = false;
                    }
                    bf.append("\" />\n");
                }
            }
        }
        bf.append("    <metric id=\"LOC\" value=\"" + this.loc + "\" />\n");
        bf.append("    <metric id=\"TABS-PRESENT\" value=\"" +
                ((this.hasTabs) ? "1" : "0") + "\" />\n");
        return bf;
    }

    public abstract String getElementType();

    public abstract Map<String, Object> getGlobalParams();

    protected Map<String, Object> getCobolSourceGlobalParams() {
        Map<String, Object> retour = new HashMap<String, Object>();
        retour.put("programId", this.id);
        retour.put("programName", this.name);
        return retour;
    }

    public File getParentDirectory() {
        return this.parentDirectory;
    }

    public CobolPackage getCobolPackage() {
        return cobolPackage;
    }

    public void setCobolPackage(CobolPackage cobolPackage) {
        this.cobolPackage = cobolPackage;
    }

    public void setViolation(String violationId, List<Violation> l, List<Copy> copies) {
        if (l != null && !l.isEmpty()) {
            for (Violation v : l) {
                String owner = v.getOwner();
                if (owner.indexOf('.') == -1) {
                    this.addViolation(violationId, v);
                } else {
                    String copyName = this.getCopyName(owner);
                    if (!"".equals(copyName)) {
                        this.addViolationToCopy(copyName, violationId, v, copies);
                    }
                }
            }
//			this.violations.put(violationId, l);
        }
    }

    private String getCopyName(String owner) {
        String retour = "";
        int indexof = owner.indexOf('.');
        if (indexof > -1) {
            retour = owner.substring(indexof + 1);
        }
        return retour;
    }

    private void addViolationToCopy(String copyName, String idViolation, Violation v, List<Copy> copies) {
        boolean added = false;
        for (Copy c : this.includedCopies) {
            if (c.getElementName().equals(copyName)) {
                c.addViolation(idViolation, v);
                added = true;
                break;
            }
        }
        if (!added) {
            for (Copy c : copies) {
                if (c.getElementName().equals(copyName)) {
                    c.addViolation(idViolation, v);
                    this.includedCopies.add(c);
                    break;
                }
            }
        }
    }
}
