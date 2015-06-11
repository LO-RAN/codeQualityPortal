package com.compuware.caqs.domain.architecture.serializeddata;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.util.Vector;

public class NodeUseCase extends Node {

    protected Vector<Element> elementsTouched;

    public NodeUseCase() {
        super();
        type = Node.NODE_USECASE;
        // TODO Auto-generated constructor stub
    }

    public NodeUseCase(String label) {
        super(label);
        type = Node.NODE_USECASE;
        // TODO Auto-generated constructor stub
    }

    public NodeUseCase(String label, int x, int y, int z, int width, int height) {
        super(label, x, y, z, width, height);
        type = Node.NODE_USECASE;
        // TODO Auto-generated constructor stub
    }

    public void addTouchedElement(Element elt) {
        if (this.elementsTouched == null) {
            this.elementsTouched = new Vector<Element>();
        }
        if (!this.elementsTouched.contains(elt)) {
            this.elementsTouched.add(elt);
        }
    }

    public void update(Graphics2D g, boolean schematicDraw) {
        int x = (int) this.coordX;
        int y = (int) this.coordY;

        int w = 0;
        int h = 0;

        String line1 = this.lbl + " [" + this.weight + "]";
        String line2 = "caller : " + this.callers.size() + " callee : "
                + this.callees.size();
        String line3 = "number of elements : " + this.elementList.size();
        FontMetrics fm = g.getFontMetrics();
        int widthLine1 = line1.length() * fm.getHeight() / 2;
        int widthLine2 = line2.length() * fm.getHeight() / 2;
        int widthLine3 = line3.length() * fm.getHeight() / 2;
        int max1 = Math.max(widthLine1, widthLine2);
        int max = Math.max(max1, widthLine3);

        int withToContainLabel = max;
        int heighToContainLabel = 3 * fm.getHeight() + 4;
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

        // define colors
        // select color depending on the selection state
        Color startColor = this.getColor();
        Color endColor = new Color(255, 255, 255);
        g.setStroke(this.getStroke());
        if (PreferencesGraphical.getInstance().isGradientFill()) {
            g.setPaint(new GradientPaint(topLeft, startColor, bottomRight, endColor));
        } else {
            // Possible Performance improvements replace gradient paint which
            // consumes 92.8% of time by plain color using the line below
            g.setColor(startColor);
        }

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

            g.setColor(startColor);

            if (this.isExpanded()) {
                w = (int) maxX - minX + 10;
                h = (int) maxY - minY + 2 * heighToContainLabel + 10;
                // to keep proportion we use the max
                if (2 * h > w) {
                    this.height = h;
                    this.width = h * 2;
                    w = h * 2;
                } else {

                    this.width = w;
                    this.height = w / 2;
                    h = w / 2;
                }

                x = (maxX + minX) / 2;
                y = (maxY + minY) / 2 - heighToContainLabel;
                this.coordX = x;
                this.coordY = y;
            } else {
                h = 100;
                w = 200;
            }

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

            // basic box
            g.fillOval(x - w / 2, y - h / 2, w - 1, h - 1);
            g.setColor(PreferencesColors.getInstance().getTextColor());
            g.drawOval(x - w / 2, y - h / 2, w - 1, h - 1);
            if (this.expanded) {
                for (int i = 0; i < this.containedPackages.size(); i++) {
                    DrawnObject currDo = this.containedPackages.elementAt(i);
                    if (!currDo.isDeleted()) {
                        currDo.update(g, schematicDraw);
                    }
                }
            }
        } else {
            // basic box
            g.fillOval(x - w / 2, y - h / 2, w - 1, h - 1);
            g.setColor(PreferencesColors.getInstance().getTextColor());
            g.drawOval(x - w / 2, y - h / 2, w - 1, h - 1);

            // text
            if (!schematicDraw) {
                g.drawString(line1, x - widthLine1 / 2 + 10, (y - (h - 4) / 2)
                        + fm.getAscent());
                g.drawString(line2, x - widthLine2 / 2 + 20, (y - (h - 4) / 2) + 2
                        * fm.getAscent() + 3);
                g.drawString(line3, x - widthLine3 / 2 + 20, (y - (h - 4) / 2) + 3
                        * fm.getAscent() + 3);
            }
        }
        // g.drawString("" + this.m_deepestPosition, x - (w - 10) / 2, (y - (h -
        // 4) / 2) + 4 * fm.getAscent() + 3);
        // draw resize Handle
        if (this.userDefinedSize) {
            this.drawResizeHandle(g, x, y, h, w);
        }

        // draw force vector
        g.setColor(Color.RED);
        int ax = (int) this.getCenterX();
        int ay = (int) this.getCenterY();
        int bx = (int) (this.getCenterX() + dx);
        int by = (int) (this.getCenterY() + dy);
        g.drawLine(ax, ay, bx, by);
    }
}
