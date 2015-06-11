/*
 * Node.java
 *
 * Created on 23 octobre 2002, 14:31
 */
/**
 *
 * @author  fxa
 */
package com.compuware.caqs.domain.architecture.serializeddata;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.io.Serializable;
import java.util.Enumeration;
import java.util.Vector;

public abstract class Node extends DrawnObject implements Serializable, Comparable {

    public static final int NODE_ARCHI = 1;
    public static final int NODE_DB = 2;
    public static final int NODE_USECASE = 3;
    protected int type = 0;
    protected Point matrixPlace = new Point();
    protected int deepestPosition = 0;
    protected boolean lockY = false;
    public static final int AR = 0;
    public static final int BR = 1;
    public static final int AL = 2;
    public static final int BL = 3;
    protected String lbl;
    protected int weight = 1;
    protected ElementsList elementList = new ElementsList();
    protected double coordX;
    protected double coordY;
    protected int zOrder;
    protected boolean userDefinedSize = true;
    protected double width = 0;
    protected double widthDiv2 = 0;
    protected double height = 0;
    protected double heightDiv2 = 0;
    // not persistant variables
    transient protected double dx = 0.0;
    transient protected double dy = 0.0;
    transient protected boolean isStartOrEnd = false;
    transient protected boolean resizeHandleSelected = false;
    protected boolean isAutoLayoutProtected = false;
    protected Vector<Node> callers = new Vector<Node>();
    protected Vector<Node> callees = new Vector<Node>();

    /** Creates a new instance of Node */
    public Node() {
    }

    public Node(String label, double x, double y, int z, int width, int height) {

        this.userDefinedSize = true;
        this.lbl = label;
        this.coordX = x;// this.setX(x);
        this.coordY = y;// this.setY(y);
        this.zOrder = z;
        this.width = width;
        this.widthDiv2 = width / 2;
        this.height = height;
        this.heightDiv2 = height / 2;
    }

    public int compareTo(Object o) {
        int value = 0;
        if (o instanceof Node) {
            Node curr = (Node) o;
            value = this.lbl.compareTo(curr.lbl);
        }
        return value;
    }

    public int getType() {
        return this.type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setMatrixPlace(Point p) {
        this.matrixPlace = p;
    }

    public Point getMatrixPlace() {
        return this.matrixPlace;
    }

    public void lockY(boolean test) {
        this.lockY = test;
    }

    public void setDeepestPosition(int d) {
        this.deepestPosition = d;
    }

    public int getDeepestPosition() {
        return this.deepestPosition;
    }

    public void calculateDeepestPosition(int position, Vector<Node> inspectedNodes) {
        position += 1;
        if (this.deepestPosition < position) {
            this.deepestPosition = position;
        }
        inspectedNodes.add(this);
        for (int i = 0; i < this.callees.size(); i++) {
            Node n = this.callees.elementAt(i);
            if (!this.callers.contains(n)) {
                if (!inspectedNodes.contains(n)) {
                    n.calculateDeepestPosition(position, inspectedNodes);
                }
            }
        }
    }

    protected void setSelected(boolean selected, double selectionType, int level, Vector<DrawnObject> selectedNodes) {
        this.selectionLevel = level;
        this.selectionType = selectionType;
        this.isSelected = selected;
        if (selected) {
            selectedNodes.add(this);

            if (selectionType == DrawnObject.SELECTION_TYPE_IMPACT_ANALYSIS) {
                if (this.selectionLevel >= 0) {
                    for (int i = 0; i < this.callees.size(); i++) {
                        Node currNode = this.callees.elementAt(i);
                        if (!selectedNodes.contains(currNode)) {
                            currNode.setSelected(true, DrawnObject.SELECTION_TYPE_IMPACT_ANALYSIS, level
                                    + 1, selectedNodes);
                        }

                    }
                }

                if (this.selectionLevel <= 0) {
                    for (int i = 0; i < this.callers.size(); i++) {
                        Node currNode = this.callers.elementAt(i);
                        if (!selectedNodes.contains(currNode)) {
                            this.callers.elementAt(i).setSelected(true, DrawnObject.SELECTION_TYPE_IMPACT_ANALYSIS, level
                                    - 1, selectedNodes);
                        }
                    }
                }

            }
        }

    }

    public boolean overlap(Node n2) {
        if (this.getCenterX() - n2.getCenterX() > 0) {
            // this is on the right of n2
            if (this.getCenterY() - n2.getCenterY() > 0) {
                // this is on the under n2
                if (n2.onDrawnObject(this.getUpperLeftX(), this.getUpperLeftY())) {
                    return true;
                }
            } else {
                // this is on the above n2
                if (n2.onDrawnObject(this.getBottomLeftX(), this.getBottomLeftY())) {
                    return true;
                }
            }
        } else {
            // this is on the left of n2
            if (this.getCenterY() - n2.getCenterY() > 0) {
                // this is on the under n2
                if (n2.onDrawnObject(this.getUpperRightX(), this.getUpperRightY())) {
                    return true;
                }
            } else {
                // this is on the above n2
                if (n2.onDrawnObject(this.getBottomRightX(), this.getBottomRightY())) {
                    return true;
                }
            }
        }
        return false;
    }

    public int relativePosition(Node n2) {
        if (this.getCenterX() - n2.getCenterX() > 0) {
            // this is on the right of n2
            if (this.getCenterY() - n2.getCenterY() > 0) {
                // this is on the under n2
                return Node.BR;
            } else {
                // this is on the above n2
                return Node.AR;
            }
        } else {
            // this is on the left of n2
            if (this.getCenterY() - n2.getCenterY() > 0) {
                // this is on the under n2
                return Node.BL;
            } else {
                // this is on the upper n2
                return Node.AL;
            }
        }
    }

    public Node(String label) {

        this.lbl = label;
    }

    public Vector<Node> getCallers() {
        return this.callers;
    }

    public Vector<Node> getCallees() {
        return this.callees;
    }

    public void addCaller(Node n) {
        this.callers.add(n);
    }

    public void addCallee(Node n) {
        this.callees.add(n);
    }

    public boolean isAutoLayoutProtected() {
        return this.isAutoLayoutProtected;
    }

    public void setIsAutoLayoutProtected(boolean bool) {
        this.isAutoLayoutProtected = bool;
    }

    public int getWeight() {
        return this.weight;
    }

    public void setWeight(int i) {
        this.weight = i;
    }

    public void setDeleted(boolean bo) {
        this.deleted = bo;
        if (this.deleted) {
            // unassign all elements
            Enumeration elementsEnum = (Enumeration) elementList.elements();

            while (elementsEnum.hasMoreElements()) {
                Element curElt = (Element) elementsEnum.nextElement();
                curElt.setAssignedToNode(null);
                ArchitectureModel.getInstance().getUnassignedElementsList().addElement(curElt);
            }
            this.elementList.removeAllElements();
            // end of unassign all elements
        }
    }

    public void addElement(Element element) {
        this.elementList.addElement(element);
        element.setAssignedToNode(this);
    }

    public void removeElement(Element element) {
        // System.out.println("removing " + el.getLabel() );
        this.elementList.removeElement(element);
        element.setAssignedToNode(null);
    }

    public void setElements(ElementsList elements) {
        this.elementList = elements;
    }

    public ElementsList getElements() {
        return this.elementList;
    }

    public void setLbl(String label) {
        this.lbl = label;
    }

    public String getLbl() {
        return this.lbl;
    }

    public double getCenterX() {
        return this.coordX;
    }

    public double getCenterY() {
        return this.coordY;
    }

    public void setX(double x) {
        double transX = this.coordX - x;

        this.coordX = x;
        if (x < 0) {
            ArchitectureModel.getInstance().translateAllModel(-x, 0);
        }
        for (int i = 0; i < this.containedPackages.size(); i++) {
            Node n = (Node) this.containedPackages.elementAt(i);
            n.setX(n.getCenterX() - transX);
        }

    }

    public void setZOrder(int z) {
        this.zOrder = z;
    }

    public int getZOrder() {
        return this.zOrder;
    }

    public void setY(double y) {
        double transY = this.coordY - y;
        if (!lockY) {
            this.coordY = y;
        }
        if (y < 0) {
            ArchitectureModel.getInstance().translateAllModel(0, -y);
        }
        for (int i = 0; i < this.containedPackages.size(); i++) {
            Node n = (Node) this.containedPackages.elementAt(i);
            n.setY(n.getCenterY() - transY);
        }
    }

    public double getDx() {
        return this.dx;
    }

    public void setDx(double dx) {
        if (dx < 150 && dx > -150) {
            this.dx = dx;
        } else {
            if (dx > 0) {
                this.dx = 100;
            } else {
                this.dx = -100;
            }
        }

    }

    public double getDy() {

        return this.dy;
    }

    public void setDy(double dy) {
        if (dy < 150 && dy > -150) {
            this.dy = dy;
        } else {
            if (dy > 0) {
                this.dy = 100;
            } else {
                this.dy = -100;
            }
        }

    }

    public void setIsStartOrEnd(boolean bo) {
        this.isStartOrEnd = bo;
    }

    public void setWidth(int width) {
        this.width = width;
        this.widthDiv2 = width / 2;
    }

    public double getWidth() {
        return this.width;
    }

    public double getWidthDiv2() {
        return this.widthDiv2;
    }

    public void setHeight(int height) {
        this.height = height;
        this.heightDiv2 = height / 2;
    }

    public double getHeight() {
        return this.height;
    }

    public double getHeightDiv2() {
        return this.heightDiv2;
    }

    abstract public void update(Graphics2D g, boolean schematicDraw);

    protected void drawResizeHandle(Graphics2D g, int x, int y, int h, int w) {
        if (this.resizeHandleSelected) {
            g.setColor(Color.red);
        } else {
            g.setColor(Color.black);
        }
        for (int i = 0; i < 3; i++) {
            int xx = (int) this.getBottomRightX();
            int yy = (int) this.getBottomRightY();

            g.drawLine(xx - (10 + i * 4), yy - 1, xx - 1, yy - (10 + i * 4));
        }

        // g.fillRoundRect(x + w / 2 - 11, y + h / 2 - 11, 11, 11, 3, 3);
    }

    public void setResizeHandleSelected(boolean bo) {
        this.resizeHandleSelected = bo;
    }

    public boolean onDrawnObjectResizeHandle(int x, int y) {
        boolean returnValue = false;
        if ((this.getCenterX() + this.getWidthDiv2() - 10 < x) && (this.getCenterX()
                + this.getWidthDiv2() + 10 > x) && (this.getCenterY()
                + this.getHeightDiv2() - 10 < y) && (this.getCenterY()
                + this.getHeightDiv2() + 10 > y)) {
            returnValue = true;
        }
        return returnValue;
    }

    public boolean onDrawnObject(double x, double y) {
        boolean inSquare = false;
        if (((this.getCenterX() - this.getWidthDiv2()) < x) && ((this.getCenterX()
                + this.getWidthDiv2()) > x) && ((this.getCenterY()
                - this.getHeightDiv2()) < y) && ((this.getCenterY()
                + this.getHeightDiv2()) > y)) {
            inSquare = true;
        }
        return inSquare;
    }

    public boolean onDrawnObject(int x, int y) {
        boolean inSquare = false;
        if (expanded) {
            if (((this.getCenterX() - this.getWidthDiv2()) < x) && ((this.getCenterX()
                    + this.getWidthDiv2()) > x) && ((this.getCenterY()
                    - this.getHeightDiv2()) < y)
                    && ((this.getCenterY() + this.getHeightDiv2()) > y)) {
                inSquare = true;
            }
        } else {
            return false;
        }
        return inSquare;
    }

    public double getUpperLeftX() {
        double upperLeftX = this.getCenterX() - this.getWidthDiv2();
        return upperLeftX;
    }

    public double getUpperLeftY() {
        double upperLeftY = this.getCenterY() - this.getHeightDiv2();
        return upperLeftY;
    }

    public double getUpperRightX() {
        double upperRightX = this.getCenterX() + this.getWidthDiv2();
        return upperRightX;
    }

    public double getUpperRightY() {
        double upperRightY = this.getCenterY() - this.getHeightDiv2();
        return upperRightY;
    }

    public double getBottomLeftX() {
        double bottomLeftX = this.getCenterX() - this.getWidthDiv2();
        return bottomLeftX;
    }

    public double getBottomLeftY() {
        double bottomLeftY = this.getCenterY() + this.getHeightDiv2();
        return bottomLeftY;
    }

    public double getBottomRightX() {
        double bottomRightX = this.getCenterX() + this.getWidthDiv2();
        return bottomRightX;
    }

    public double getBottomRightY() {
        double bottomRightY = this.getCenterY() + this.getHeightDiv2();
        return bottomRightY;
    }

    public Color getColor() {
        Color startColor = PreferencesColors.getInstance().getNodeBackGroungColor();
        // nodes

        if (this.isSelected) {
            if (this.selectionType
                    == DrawnObject.SELECTION_TYPE_IMPACT_ANALYSIS) {
                if (this.selectionLevel == 0) {
                    startColor = new Color(255, 0, 0);
                } else if (this.selectionLevel > 0) {
                    startColor = PreferencesColors.getInstance().getImpactAnalysisCallee();
                } else {
                    startColor = PreferencesColors.getInstance().getImpactAnalysisCaller();
                }
            } else {
                startColor = PreferencesColors.getInstance().getSelectedColor();
            }
        } else {
            if (this.isStartOrEnd) {
                startColor = PreferencesColors.getInstance().getLinkTargetColor();
            } else if (this.isAutoLayoutProtected()) {
                startColor = new Color(0, 0, 255);
            }

        }

        return startColor;
    }

    public BasicStroke getStroke() {
        BasicStroke stroke = new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f);

        if (this.containedPackages.size() > 0) {
            float dash1[] = {10.0f};
            stroke = new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f, dash1, 0.0f);
        }

        return stroke;
    }
}
