package com.compuware.caqs.domain.architecture.serializeddata;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;

public class NodeArchitectureModule extends NodeAffectable {

    String line1;
    String line11;
    String line2;
    String line3;
    transient Graphics2D graphics = null;

    public NodeArchitectureModule() {
        super();
        type = Node.NODE_ARCHI;
        // TODO Auto-generated constructor stub
    }

    public NodeArchitectureModule(String label) {
        super(label);
        type = Node.NODE_ARCHI;
        // TODO Auto-generated constructor stub
    }

    public NodeArchitectureModule(String label, double x, double y, int z, int width, int height) {
        super(label, x, y, z, width, height);
        type = Node.NODE_ARCHI;
        // TODO Auto-generated constructor stub
    }

    public void update(Graphics2D g, boolean schematicDraw) {
        this.graphics = g;
        int x = (int) this.coordX;
        int y = (int) this.coordY;

        int w = 0;
        int h = 0;

        // define colors
        // select color depending on the selection state
        Color startColor = this.getColor();
        Color endColor = new Color(255, 255, 255);
        g.setStroke(this.getStroke());

        // 1st step Find the right size to display node contain
        if (this.containedPackages.size() > 0) {
            Node n = (Node) this.containedPackages.elementAt(0);
            // initialize with first node coordinnates
            double upperLeftX = n.getUpperLeftX();
            double upperLeftY = n.getUpperLeftY();
            double upperRigthX = n.getUpperRightX();
            double upperRigthY = n.getUpperRightY();
            double bottomLeftX = n.getBottomLeftX();
            double bottomLeftY = n.getBottomLeftY();
            double bottomRightX = n.getBottomRightX();
            double bottomRightY = n.getBottomRightY();

            int minX = (int) Math.min(upperLeftX, bottomLeftX);
            int maxX = (int) Math.max(upperRigthX, bottomRightX);
            int minY = (int) Math.min(upperLeftY, upperRigthY);
            int maxY = (int) Math.max(bottomLeftY, bottomRightY);

            for (int i = 0; i < this.containedPackages.size(); i++) {
                n = (Node) this.containedPackages.elementAt(i);
                if (!n.isDeleted()) {
                    if (upperLeftX > n.getUpperLeftX()) {
                        upperLeftX = n.getUpperLeftX();
                    }
                    if (bottomLeftX > n.getBottomLeftX()) {
                        bottomLeftX = n.getBottomLeftX();
                    }
                    if (upperLeftY > n.getUpperLeftX()) {
                        upperLeftY = n.getUpperLeftY();
                    }
                    if (upperRigthX < n.getUpperRightX()) {
                        upperRigthX = n.getUpperRightX();
                    }
                    if (upperRigthY > n.getUpperRightY()) {
                        upperRigthY = n.getUpperRightY();
                    }
                    if (bottomLeftY < n.getBottomLeftY()) {
                        bottomLeftY = n.getBottomLeftY();
                    }
                    if (bottomRightX < n.getBottomRightX()) {
                        bottomRightX = n.getBottomRightX();
                    }
                    if (bottomRightY < n.getBottomRightY()) {
                        bottomRightY = n.getBottomRightY();
                    }
                }
            }

            minX = (int) Math.min(upperLeftX, bottomLeftX);
            maxX = (int) Math.max(upperRigthX, bottomRightX);
            minY = (int) Math.min(upperLeftY, upperRigthY);
            maxY = (int) Math.max(bottomLeftY, bottomRightY);

            int widthToContainLabel = this.lineWidth(g);
            FontMetrics fm = g.getFontMetrics();
            int heighToContainLabel = this.lineHeight(fm);

            g.setColor(startColor);

            if (this.isExpanded()) {
                w = (int) maxX - minX + 10;
                h = (int) maxY - minY + heighToContainLabel + 10;
                super.setHeight(h);
                super.setWidth(w);
                x = (maxX + minX) / 2;
                y = (maxY + minY - heighToContainLabel + 10) / 2;
                this.coordX = x;
                this.coordY = y;
            } else {
                h = heighToContainLabel;
                w = widthToContainLabel;
            }

            g.fillRoundRect(x - w / 2, y - h / 2, w - 1, h - 1, 8, 8);
            g.setColor(PreferencesColors.getInstance().getTextColor());
            g.drawRoundRect(x - w / 2, y - h / 2, w - 1, h - 1, 8, 8);

            this.drawText(g, x, y, h, w);

            if (this.expanded) {
                for (int i = 0; i < this.containedPackages.size(); i++) {
                    DrawnObject currDo = this.containedPackages.elementAt(i);
                    if (!currDo.isDeleted()) {
                        currDo.update(g, schematicDraw);
                    }
                }
            }
        } else {
            int max = this.lineWidth(g);
            int withToContainLabel = max;
            FontMetrics fm = g.getFontMetrics();
            int heighToContainLabel = this.lineHeight(fm);

            if (!this.userDefinedSize) {
                w = withToContainLabel;
                h = heighToContainLabel;
            } else {
                if (withToContainLabel > this.width) {
                    w = withToContainLabel;
                } else {
                    w = (int) this.width;
                }
                if (heighToContainLabel > this.height) {
                    h = heighToContainLabel;
                } else {
                    h = (int) this.height;
                }
            }

            super.setHeight(h);
            super.setWidth(w);

            Point2D.Double topLeft = new Point2D.Double(x, y - h / 2);
            Point2D.Double bottomRight = new Point2D.Double(x, y + h / 2);

            // basic box
            // set the paint type
            if (PreferencesGraphical.getInstance().isGradientFill()) {
                g.setPaint(new GradientPaint(topLeft, startColor, bottomRight, endColor));
            } else {
                // Possible Performance improvements replace gradient paint
                // which
                // consumes 92.8% of time by plain color using the line below
                g.setColor(startColor);
            }

            g.fillRoundRect(x - w / 2, y - h / 2, w - 1, h - 1, 8, 8);
            if (!schematicDraw) {
                g.setColor(PreferencesColors.getInstance().getTextColor());
                g.drawRoundRect(x - w / 2, y - h / 2, w - 1, h - 1, 8, 8);

                // title : this line will draw a box around the node name, it
                // will
                // look
                // a little more like a UML class diagram
                // g.drawRoundRect(x - w / 2, y - h / 2, w - 1,
                // fm.getAscent()+5, 8,
                // 8);

                this.drawText(g, x, y, h, w);
                // draw resize Handle
                if (PreferencesGraphical.getInstance().isDrawResizeHandle()) {
                    if (this.userDefinedSize) {
                        this.drawResizeHandle(g, x, y, h, w);
                    }
                }

                if (PreferencesGraphical.getInstance().isDebugGraphics()) {
                    // draw force vector
                    g.setColor(Color.RED);
                    int ax = (int) this.getCenterX();
                    int ay = (int) this.getCenterY();
                    int bx = (int) (this.getCenterX() + dx);
                    int by = (int) (this.getCenterY() + dy);
                    g.drawLine(ax, ay, bx, by);
                }
            }
        }

    }

    protected int lineWidth(Graphics2D g) {
        line1 = this.lbl + " [" + this.weight + "]";
        line11 = "";
        line2 = "caller : " + this.callers.size() + " callee : "
                + this.callees.size();
        line3 = "number of elements : " + this.elementList.size();
        FontMetrics fm = g.getFontMetrics();
        int widthLine1 = line1.length() * fm.getHeight() / 2;
        int widthLine11 = line11.length() * fm.getHeight() / 2;
        int widthLine2 = line2.length() * fm.getHeight() / 2;
        int widthLine3 = line3.length() * fm.getHeight() / 2;
        int max1 = Math.max(widthLine1, widthLine2);
        int max2 = Math.max(max1, widthLine3);
        int max = Math.max(max2, widthLine11);
        return max;
    }

    protected void drawText(Graphics2D g, int x, int y, int h, int w) {
        line1 = this.lbl + " [" + this.weight + "]" + " Level " + this.level;
        line11 = "";
        line2 = "caller : " + this.callers.size() + " callee : "
                + this.callees.size();
        line3 = "number of elements : " + this.elementList.size();
        FontMetrics fm = g.getFontMetrics();
        g.drawString(line1, x - (w - 10) / 2, (y - (h - 4) / 2) + fm.getAscent());
        g.drawString(line11, x - (w - 10) / 2, (y - (h - 4) / 2) + 2
                * fm.getAscent() + 3);
        g.drawString(line2, x - (w - 10) / 2, (y - (h - 4) / 2) + 3
                * fm.getAscent() + 3);
        g.drawString(line3, x - (w - 10) / 2, (y - (h - 4) / 2) + 4
                * fm.getAscent() + 3);

    }

    protected int lineHeight(FontMetrics fm) {
        return (4 * fm.getHeight() + 4);
    }

    @Override
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
            if (this.graphics != null) {
                FontMetrics fm = this.graphics.getFontMetrics();
                double h = this.lineHeight(fm);
                double w = this.lineWidth(this.graphics);
                if (((this.getCenterX() - w / 2) < x) && ((this.getCenterX() + w
                        / 2) > x) && ((this.getCenterY() - h / 2) < y)
                        && ((this.getCenterY() + h / 2) > y)) {
                    inSquare = true;
                }
            } else {
                return false;
            }
        }
        return inSquare;
    }
}
