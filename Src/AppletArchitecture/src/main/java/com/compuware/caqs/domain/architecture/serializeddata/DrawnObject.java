/*
 * DrawnObject.java
 *
 * Created on 25 octobre 2002, 14:52
 */
/**
 *
 * @author  fxa
 */
package com.compuware.caqs.domain.architecture.serializeddata;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.io.Serializable;
import java.util.Enumeration;
import java.util.Vector;

public abstract class DrawnObject implements Serializable {
    // selections types for DrawnObjects

    public static final double SELECTION_TYPE_NOT_SELECTED = 0;
    public static final double SELECTION_TYPE_BASIC = 3;
    public static final double SELECTION_TYPE_IMPACT_ANALYSIS = 4;
    public static final double SELECTION_TYPE_IMPACT_ANALYSIS_IMPACTED_UNDER = 5;
    public static final double SELECTION_TYPE_IMPACT_ANALYSIS_IMPACTED_ABOVE = 6;
    Vector<DrawnObjectListener> listeners;
    private String id;
    protected boolean deleted = false;
    // selection data
    protected double selectionType = 0;
    transient protected boolean isSelected = false;
    protected int selectionLevel;
    //is in a group
    protected boolean isInAGroup = false;
    protected DrawnObject group;
    protected String parentId = ""; //needed in the restore process to relink with parent after query from db
    protected int level = 0;
    //is a group
    protected boolean expanded = true;
    protected Vector<DrawnObject> containedPackages = new Vector<DrawnObject>();
    protected boolean isVisible = true;


    /** Creates a new instance of DrawnObject */
    public DrawnObject() {
    }

    abstract public void update(Graphics2D g, boolean schematicDraw);

    abstract public void setDeleted(boolean bo);

    abstract protected void setSelected(boolean bo, double selectionType, int level, Vector<DrawnObject> selectedNodes);

    abstract public boolean onDrawnObject(int x, int y);

    abstract public boolean onDrawnObjectResizeHandle(int x, int y);

    abstract public void setResizeHandleSelected(boolean bo);

    abstract public double getCenterX();

    abstract public double getCenterY();

    abstract public BasicStroke getStroke();

    abstract public Color getColor();

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getParentId() {
        return this.parentId;
    }

    public void addChildObject(DrawnObject n) {
        this.containedPackages.add(n);
        n.setIsInAGroup(true);
        n.setGroup(this);
    }

    public Vector<DrawnObject> getChildObjects() {
        return this.containedPackages;
    }

    public int getLevel() {
        return this.level;
    }

    public void setIsExpanded(boolean value) {
        this.expanded = value;
        if (this.expanded) {
            this.setChildAsVisible();
        } else {
            this.setChildAsNotVisible();
        }

    }

    public void setChildAsVisible() {
        Enumeration<DrawnObject> objects = this.containedPackages.elements();
        while (objects.hasMoreElements()) {
            DrawnObject currDO = objects.nextElement();
            currDO.setIsVisible(true);
            currDO.setChildAsVisible();
        }
    }

    public void setChildAsNotVisible() {
        Enumeration<DrawnObject> objects = this.containedPackages.elements();
        while (objects.hasMoreElements()) {
            DrawnObject currDO = objects.nextElement();
            currDO.setIsVisible(false);
            currDO.setChildAsNotVisible();
        }
    }

    public boolean isExpanded() {
        return this.expanded;
    }

    public void setIsInAGroup(boolean b) {
        this.isInAGroup = b;
    }

    public boolean isInAGroup() {
        return this.isInAGroup;
    }

    public void setGroup(DrawnObject group) {
        this.group = group;
        if (group != null) {
            this.level = group.getLevel() + 1;
        } else {
            this.parentId = "";
        }
    }

    public DrawnObject getGroup() {
        return this.group;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isDeleted() {
        return this.deleted;
    }

    public boolean isActionnable() {
        //return (!this.isDeleted() & !(this.isInAGroup() && !this.getGroup().getIsExpanded()));
        return (!this.isDeleted() & this.isVisible);
    }

    public DrawnObject getVisibleGroup() {
        DrawnObject returnObject = this;
        if (isActionnable()) {
            return this;
        } else {
            if (this.isInAGroup()) {
                returnObject = group.getVisibleGroup();
            }
        }
        return returnObject;
    }

    public void setLevel(int i) {
        this.level = i;
    }

    public void ungroup() {
        for (int i = 0; i < this.containedPackages.size(); i++) {
            DrawnObject n = this.containedPackages.elementAt(i);


            n.setIsInAGroup(false);
            n.setGroup(null);
            n.setLevel(0);

        }
        this.containedPackages.removeAllElements();
        this.setIsExpanded(true);
        this.fireDrawnObjectChangedEvent();

    }

    public void addListener(DrawnObjectListener listener) {
        if (this.listeners == null) {
            this.listeners = new Vector<DrawnObjectListener>();
        }
        // avoid multiple registration
        if (!this.listeners.contains(listener)) {
            this.listeners.add(listener);
            listener.DrawnObjectChanged();
        }
    }

    public void fireDrawnObjectChangedEvent() {
        if (this.listeners != null) {
            Enumeration<DrawnObjectListener> listenersEnum = this.listeners.elements();
            while (listenersEnum.hasMoreElements()) {
                DrawnObjectListener aListener = listenersEnum.nextElement();
                aListener.DrawnObjectChanged();
            }
        } else {
            //System.out.println("No one is listening");
        }
    }

    public void setIsVisible(boolean value) {
        this.isVisible = value;
    }

    public boolean isVisible() {
        return this.isVisible;
    }
}
