/*
 * Edge.java
 *
 * Created on 23 octobre 2002, 14:32
 *
 * @author  fxa
 */
package com.compuware.caqs.domain.architecture.serializeddata;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class Edge extends DrawnObject implements Serializable {

    public static final int ARCHI_LINK = 100;
    public static final int ARCHI_LINK_USECASE_TO_BOX = 101;
    public static final int ARCHI_LINK_ANY_TO_DB = 102;
    public static final int REAL_LINK_OK = 1;
    public static final int REAL_LINK_ANTI = 2;
    public static final int REAL_LINK_NOTEXPECTED = 3;
    protected Node from;
    protected Node to;
    protected int type;
    protected boolean isReal = false;
    protected List<ElementsCouple> elementsCouplesList = new ArrayList<ElementsCouple>();
    transient protected int arrowPosition = 100;
    transient protected boolean bestArrowPositionFound = false;
    transient protected int arrowTailPosition = 100;
    transient protected boolean bestArrowTailPositionFound = false;
    protected final int MAX = 2 + 1;
    protected Point node[] = new Point[MAX]; // node
    protected int weight[][] = new int[MAX][MAX]; // weight of arrow
    protected Point arrow[][] = new Point[MAX][MAX]; // current position of
    // arrowhead
    protected Point startp[][] = new Point[MAX][MAX]; // start and
    protected Point endp[][] = new Point[MAX][MAX]; // endpoint of arrow
    protected float dirX[][] = new float[MAX][MAX]; // direction of arrow
    protected float dirY[][] = new float[MAX][MAX]; // direction of arrow
    protected Point nodeTail[] = new Point[MAX]; // node
    protected int weightTail[][] = new int[MAX][MAX]; // weight of arrow
    protected Point arrowTail[][] = new Point[MAX][MAX]; // current position
    // of arrowhead
    protected Point startpTail[][] = new Point[MAX][MAX]; // start and
    protected Point endpTail[][] = new Point[MAX][MAX]; // endpoint of arrow
    protected float dirXTail[][] = new float[MAX][MAX]; // direction of
    // arrow
    protected float dirYTail[][] = new float[MAX][MAX]; // direction of
    // arrow
    protected Vector<Point> intermediatePoints = new Vector<Point>();
    transient protected int drawnLineEndx;
    transient protected int drawnLineEndy;
    transient protected int drawnLineStartx;
    transient protected int drawnLineStarty;

    /** Creates a new instance of Edge */
    public Edge() {
        //m_intermediatePoints.add(new Point(100,100));
        //m_intermediatePoints.add(new Point(200,200));
        //m_intermediatePoints.add(new Point(300,300));
    }

    public Edge(Node from, Node to, int linkType, List<ElementsCouple> couples) {
        this.from = from;
        this.to = to;
        this.from.addCallee(this.to);
        this.to.addCaller(this.from);
        this.elementsCouplesList = couples;
        this.setLinkType(linkType);
    }

    public double getLen() {
        if (this.type == Edge.ARCHI_LINK) {
            return 10000;
        } else {
            return this.elementsCouplesList.size();
        }
    }

    public void setDeleted(boolean bo) {
        this.deleted = bo;
        if (this.deleted) {
            this.elementsCouplesList.clear();
            this.from.getCallees().remove(this.to);
            this.to.getCallers().remove(this.from);
        }

    }

    public void setArrowPosition(int value) {
        this.arrowPosition = value;
    }

    public void setArrowTailPosition(int value) {
        this.arrowTailPosition = value;
    }

    public void setBestArrowPositionFound(boolean value) {
        this.bestArrowPositionFound = value;
    }

    public void setBestArrowTailPositionFound(boolean value) {
        this.bestArrowTailPositionFound = value;
    }

    public Node getFrom() {
        return this.from;
    }

    public Node getTo() {
        return this.to;
    }

    public void addCouple(ElementsCouple couple) {
        this.elementsCouplesList.add(couple);
    }

    public List<ElementsCouple> getCouples() {
        return this.elementsCouplesList;
    }

    public final void setLinkType(int linkType) {
        this.type = linkType;
        if (linkType == Edge.ARCHI_LINK | linkType
                == Edge.ARCHI_LINK_USECASE_TO_BOX | linkType
                == Edge.ARCHI_LINK_ANY_TO_DB) {
            this.setReal(false);
        } else {
            this.setReal(true);
        }
    }

    public int getLinkType() {
        return this.type;
    }

    public boolean isReal() {
        return this.isReal;
    }

    private void setReal(boolean bo) {
        this.isReal = bo;
    }

    public String getFromId() {
        return this.from.getId();
    }

    public String getToId() {
        return this.to.getId();
    }

    public void setSelected(boolean bo) {
        this.isSelected = bo;
    }

    protected void setSelected(boolean bo, double selectionType, int level, Vector<DrawnObject> selectedNodes) {
        this.selectionType = selectionType;
        this.isSelected = bo;
        this.selectionLevel = level;
    }

    public double getCenterX() {
        return (this.from.getCenterX() + this.to.getCenterX()) / 2;
    }

    public double getCenterY() {
        return (this.from.getCenterY() + this.to.getCenterY()) / 2;
    }

    public void update(Graphics2D g, boolean schematicDraw) {
        int x1 = (int) this.from.getCenterX();
        int y1 = (int) this.from.getCenterY();
        int x2 = (int) this.to.getCenterX();
        int y2 = (int) this.to.getCenterY();

        // group impact when collapsed
        x1 = (int) this.from.getVisibleGroup().getCenterX();
        y1 = (int) this.from.getVisibleGroup().getCenterY();

        x2 = (int) this.to.getVisibleGroup().getCenterX();
        y2 = (int) this.to.getVisibleGroup().getCenterY();


        this.arrowPosition = 100;
        this.bestArrowPositionFound = false;
        this.arrowTailPosition = 100;
        this.bestArrowTailPositionFound = false;

        g.setColor(this.getColor());
        g.setStroke(this.getStroke());

        // draw arrow
        if (intermediatePoints.size() == 0) {
            this.node[0] = new Point((int) x1, (int) y1);
            this.node[1] = new Point((int) x2, (int) y2);
            this.nodeTail[1] = new Point((int) x1, (int) y1);
            this.nodeTail[0] = new Point((int) x2, (int) y2);
        } else {
            this.node[1] = new Point((int) x1, (int) y1);
            //this.m_node[1] = new Point((int) x2, (int) y2);
            //this.m_nodeTail[1] = new Point((int) x1, (int) y1);
            this.node[0] = new Point(intermediatePoints.elementAt(0).x, intermediatePoints.elementAt(0).y);
            this.nodeTail[0] = new Point(intermediatePoints.elementAt(intermediatePoints.size()
                    - 1).x, intermediatePoints.elementAt(intermediatePoints.size()
                    - 1).y);
            this.nodeTail[1] = new Point((int) x2, (int) y2);
        }

        if (!schematicDraw && PreferencesGraphical.getInstance().isArrowDrawn()) {
            int cptIteration = 0;
            while ((cptIteration < 100) && ((!this.bestArrowPositionFound)
                    || (!bestArrowTailPositionFound))) {
                this.arrowupdate(0, 1, this.arrowPosition);
                this.arrowTailUpdate(0, 1, this.arrowTailPosition);
                cptIteration++;
            }
            this.drawArrow(g, 0, 1);
            if (intermediatePoints.size() != 0) {
                int fromX = this.drawnLineStartx;
                int fromY = this.drawnLineStarty;
                int toX = this.drawnLineEndx;
                int toY = this.drawnLineEndy;
                for (int i = 0; i < intermediatePoints.size(); i++) {
                    if (i == 0) {
                        toX = intermediatePoints.elementAt(i).x;
                        toY = intermediatePoints.elementAt(i).y;
                        g.drawLine(fromX, fromY, toX, toY);
                    } else {
                        fromX = intermediatePoints.elementAt(i - 1).x;
                        fromY = intermediatePoints.elementAt(i - 1).y;
                        toX = intermediatePoints.elementAt(i).x;
                        toY = intermediatePoints.elementAt(i).y;
                        g.drawLine(fromX, fromY, toX, toY);
                    }

                    if (i == (intermediatePoints.size() - 1)) {
                        fromX = intermediatePoints.elementAt(i).x;
                        fromY = intermediatePoints.elementAt(i).y;
                        toX = this.drawnLineEndx;
                        toY = this.drawnLineEndy;
                        g.drawLine(fromX, fromY, toX, toY);
                    }
                }
            } else {
                g.drawLine(this.drawnLineStartx, this.drawnLineStarty, this.drawnLineEndx, this.drawnLineEndy);
            }
        } else {
            g.drawLine(x1, y1, x2, y2);
        }
        // Draw direct line for arrows

        // g.drawLine(x1,y1,x2,y2);

        if (this.isReal) {
            if ((x1 - x2) == 0) {
                // vertical alignment
                if ((y1 - y2) > 0) {
                    g.drawString("" + this.elementsCouplesList.size(), x1 + (x2
                            - x1) / (2) + 5, y1 + (y2 - y1) / (2));
                } else {
                    g.drawString("" + this.elementsCouplesList.size(), x1 + (x2
                            - x1) / (2) - 10, y1 + (y2 - y1) / (2));
                }
            } else if ((y1 - y2) == 0) {
                // horizontzl alignment
                if ((x1 - x2) > 0) {
                    g.drawString("" + this.elementsCouplesList.size(), x1 + (x2
                            - x1) / (2), y1 + (y2 - y1) / (2) - 5);
                } else {
                    g.drawString("" + this.elementsCouplesList.size(), x1 + (x2
                            - x1) / (2), y1 + (y2 - y1) / (2) + 10);
                }
            } else if ((x1 - x2) < 0) {
                if ((y1 - y2) > 0) {
                    g.drawString("" + this.elementsCouplesList.size(), x1 + (x2
                            - x1) / (2) + 5, y1 + (y2 - y1) / (2) + 10);
                } else {
                    g.drawString("" + this.elementsCouplesList.size(), x1 + (x2
                            - x1) / (2) - 5, y1 + (y2 - y1) / (2) - 10);
                }
            } else {
                if ((y1 - y2) > 0) {
                    g.drawString("" + this.elementsCouplesList.size(), x1 + (x2
                            - x1) / (2) + 5, y1 + (y2 - y1) / (2) + 10);
                } else {
                    g.drawString("" + this.elementsCouplesList.size(), x1 + (x2
                            - x1) / (2) - 5, y1 + (y2 - y1) / (2) - 10);
                }
            }
        }
        // end : draw arrow
    }

    private void arrowTailUpdate(int p1, int p2, int w) {
        // make a new arrow from node p1 to p2 with weight w, or change
        // the weight of the existing arrow to w, calculate the resulting
        // position of the arrowhead
        int dx, dy;
        float l;

        // direction line between p1 and p2
        dx = this.nodeTail[p2].x - this.nodeTail[p1].x;
        dy = this.nodeTail[p2].y - this.nodeTail[p1].y;

        // distance between p1 and p2
        l = (float) (Math.sqrt((float) (dx * dx + dy * dy)));
        this.dirXTail[p1][p2] = dx / l;
        this.dirYTail[p1][p2] = dy / l;

        // calculate the start and endpoints of the arrow,
        // adjust startpoints if there also is an arrow from p2 to p1
        if (this.weightTail[p2][p1] > 0) {
            this.startpTail[p1][p2] = new Point((int) (this.nodeTail[p1].x - 5
                    * this.dirYTail[p1][p2]), (int) (this.nodeTail[p1].y + 5
                    * this.dirXTail[p1][p2]));
            this.endpTail[p1][p2] = new Point((int) (this.nodeTail[p2].x - 5
                    * this.dirYTail[p1][p2]), (int) (this.nodeTail[p2].y + 5
                    * this.dirXTail[p1][p2]));
        } else {
            this.startpTail[p1][p2] = new Point(this.nodeTail[p1].x, this.nodeTail[p1].y);
            this.endpTail[p1][p2] = new Point(this.nodeTail[p2].x, this.nodeTail[p2].y);
        }

        // range for arrowhead is not all the way to the start/endpoints
        int diff_x = (int) (Math.abs(20 * this.dirXTail[p1][p2]));
        int diff_y = (int) (Math.abs(20 * this.dirYTail[p1][p2]));

        // calculate new x-position arrowhead
        if (this.startpTail[p1][p2].x > this.endpTail[p1][p2].x) {
            this.arrowTail[p1][p2] = new Point(this.endpTail[p1][p2].x
                    + diff_x + (Math.abs(this.endpTail[p1][p2].x
                    - this.startpTail[p1][p2].x) - 2 * diff_x) * (100 - w)
                    / 100, 0);
        } else {
            this.arrowTail[p1][p2] = new Point(this.startpTail[p1][p2].x
                    + diff_x + (Math.abs(this.endpTail[p1][p2].x
                    - this.startpTail[p1][p2].x) - 2 * diff_x) * w / 100, 0);
        }

        // calculate new y-position arrowhead
        if (this.startpTail[p1][p2].y > this.endpTail[p1][p2].y) {
            this.arrowTail[p1][p2].y = this.endpTail[p1][p2].y + diff_y + (Math.abs(this.endpTail[p1][p2].y
                    - this.startpTail[p1][p2].y) - 2 * diff_y) * (100 - w)
                    / 100;
        } else {
            this.arrowTail[p1][p2].y = this.startpTail[p1][p2].y + diff_y + (Math.abs(this.endpTail[p1][p2].y
                    - this.startpTail[p1][p2].y) - 2 * diff_y) * w / 100;
        }
        int i = 0;
        int j = 1;
        int x3 = (int) (this.arrowTail[i][j].x + 6 * this.dirXTail[i][j]);
        int y3 = (int) (this.arrowTail[i][j].y + 6 * this.dirYTail[i][j]);

        if (this.from.onDrawnObject(x3, y3)) {
            if (!this.bestArrowTailPositionFound) {
                this.arrowTailPosition = this.arrowTailPosition - 1;
                if (this.arrowTailPosition < 0) {
                    this.arrowTailPosition = 1;
                }
            }
        } else {
            this.bestArrowTailPositionFound = true;
            this.arrowTailPosition = this.arrowTailPosition + 1;
            if (this.arrowTailPosition > 100) {
                this.arrowTailPosition = 100;
            }
        }

        this.drawnLineStartx = x3;
        this.drawnLineStarty = y3;
    }

    private void arrowupdate(int p1, int p2, int w) {
        // make a new arrow from node p1 to p2 with weight w, or change
        // the weight of the existing arrow to w, calculate the resulting
        // position of the arrowhead
        int dx, dy;
        float l;

        // direction line between p1 and p2
        dx = this.node[p2].x - this.node[p1].x;
        dy = this.node[p2].y - this.node[p1].y;

        // distance between p1 and p2
        l = (float) (Math.sqrt((float) (dx * dx + dy * dy)));
        this.dirX[p1][p2] = dx / l;
        this.dirY[p1][p2] = dy / l;

        // calculate the start and endpoints of the arrow,
        // adjust startpoints if there also is an arrow from p2 to p1
        if (this.weight[p2][p1] > 0) {
            this.startp[p1][p2] = new Point((int) (this.node[p1].x - 5
                    * this.dirY[p1][p2]), (int) (this.node[p1].y + 5
                    * this.dirX[p1][p2]));
            this.endp[p1][p2] = new Point((int) (this.node[p2].x - 5
                    * this.dirY[p1][p2]), (int) (this.node[p2].y + 5
                    * this.dirX[p1][p2]));
        } else {
            this.startp[p1][p2] = new Point(this.node[p1].x, this.node[p1].y);
            this.endp[p1][p2] = new Point(this.node[p2].x, this.node[p2].y);
        }

        // range for arrowhead is not all the way to the start/endpoints
        int diff_x = (int) (Math.abs(20 * this.dirX[p1][p2]));
        int diff_y = (int) (Math.abs(20 * this.dirY[p1][p2]));

        // calculate new x-position arrowhead
        if (this.startp[p1][p2].x > this.endp[p1][p2].x) {
            this.arrow[p1][p2] = new Point(this.endp[p1][p2].x + diff_x + (Math.abs(this.endp[p1][p2].x
                    - this.startp[p1][p2].x) - 2 * diff_x) * (100 - w) / 100, 0);
        } else {
            this.arrow[p1][p2] = new Point(this.startp[p1][p2].x + diff_x + (Math.abs(this.endp[p1][p2].x
                    - this.startp[p1][p2].x) - 2 * diff_x) * w / 100, 0);
        }

        // calculate new y-position arrowhead
        if (this.startp[p1][p2].y > this.endp[p1][p2].y) {
            this.arrow[p1][p2].y = this.endp[p1][p2].y + diff_y + (Math.abs(this.endp[p1][p2].y
                    - this.startp[p1][p2].y) - 2 * diff_y) * (100 - w) / 100;
        } else {
            this.arrow[p1][p2].y = this.startp[p1][p2].y + diff_y + (Math.abs(this.endp[p1][p2].y
                    - this.startp[p1][p2].y) - 2 * diff_y) * w / 100;
        }
        int i = 0;
        int j = 1;
        int x3 = (int) (this.arrow[i][j].x + 6 * this.dirX[i][j]);
        int y3 = (int) (this.arrow[i][j].y + 6 * this.dirY[i][j]);

        if (this.to.onDrawnObject(x3, y3)) {
            if (!this.bestArrowPositionFound) {
                this.arrowPosition = this.arrowPosition - 1;
                if (this.arrowPosition < 0) {
                    this.arrowPosition = 1;
                }
            }
        } else {
            this.bestArrowPositionFound = true;
            this.arrowPosition = this.arrowPosition + 1;
            if (this.arrowPosition > 100) {
                this.arrowPosition = 100;
            }
        }

        this.drawnLineEndx = x3;
        this.drawnLineEndy = y3;
    }

    public boolean inSquare(int x, int y) {
        boolean inSquare = false;
        int x1;
        int x2;
        int y1;
        int y2;
        if (this.drawnLineStartx < this.drawnLineEndx) {
            x1 = this.drawnLineStartx;
            x2 = this.drawnLineEndx;
        } else {
            x2 = this.drawnLineStartx;
            x1 = this.drawnLineEndx;
        }
        if (this.drawnLineStarty < this.drawnLineEndy) {
            y1 = this.drawnLineStarty;
            y2 = this.drawnLineEndy;
        } else {
            y2 = this.drawnLineStarty;
            y1 = this.drawnLineEndy;
        }
        x1 -= 10;
        x2 += 10;
        y1 -= 10;
        y2 += 10;
        if ((x1 < x) && (x2 > x) && (y1 < y) && (y2 > y)) {
            inSquare = true;
        }
        return inSquare;
    }

    public void setResizeHandleSelected(boolean bo) {
    }

    public boolean onDrawnObjectResizeHandle(int x, int y) {
        return false;
    }

    public boolean onDrawnObject(int x, int y) {
        boolean returnValue = false;
        // y = ax+ b
        returnValue = this.inSquare(x, y);
        return returnValue;
    }

    private void drawArrow(Graphics2D g, int i, int j) {
        // draw arrow between node i and node j
        int x1, x2, x3, y1, y2, y3;

        // calculate arrowhead
        x1 = (int) (this.arrow[i][j].x - 3 * this.dirX[i][j] + 6
                * this.dirY[i][j]);
        x2 = (int) (this.arrow[i][j].x - 3 * this.dirX[i][j] - 6
                * this.dirY[i][j]);
        x3 = (int) (this.arrow[i][j].x + 6 * this.dirX[i][j]);

        y1 = (int) (this.arrow[i][j].y - 3 * this.dirY[i][j] - 6
                * this.dirX[i][j]);
        y2 = (int) (this.arrow[i][j].y - 3 * this.dirY[i][j] + 6
                * this.dirX[i][j]);
        y3 = (int) (this.arrow[i][j].y + 6 * this.dirY[i][j]);

        int arrowhead_x[] = {x1, x2, x3, x1};
        int arrowhead_y[] = {y1, y2, y3, y1};

        // draw arrow
        // g.drawLine(this.m_startp[i][j].x, this.m_startp[i][j].y,
        // this.m_endp[i][j].x, this.m_endp[i][j].y);
        g.fillPolygon(arrowhead_x, arrowhead_y, 4);
    }

    public Color getColor() {
        Color startColor = Color.pink;
        if (this.isSelected) {
            startColor = PreferencesColors.getInstance().getSelectedColor();
        } else {

            switch (this.type) {
                case Edge.ARCHI_LINK:
                    startColor = PreferencesColors.getInstance().getArchiLinkColor();
                    break;
                case Edge.ARCHI_LINK_USECASE_TO_BOX:
                    startColor = PreferencesColors.getInstance().getUseCaseLinkColor();// Color.blue;
                    break;
                case Edge.ARCHI_LINK_ANY_TO_DB:
                    startColor = PreferencesColors.getInstance().getDBLinkColor();// Color.MAGENTA;
                    break;
                case Edge.REAL_LINK_OK:
                    startColor = Color.green;
                    break;
                case Edge.REAL_LINK_ANTI:
                    startColor = Color.red;
                    break;
                case Edge.REAL_LINK_NOTEXPECTED:
                    startColor = Color.gray;
                    break;
                default: {
                    startColor = Color.pink;
                    // System.out.println("Unknow link type");
                }
            }
            if (this.deleted) {
                startColor = Color.pink;
            }
        }
        return startColor;
    }

    public BasicStroke getStroke() {
        BasicStroke stroke = new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f);

        if (this.from.isVisible() && this.to.isVisible()) {
            switch (this.type) {
                case Edge.ARCHI_LINK:

                    break;
                case Edge.ARCHI_LINK_USECASE_TO_BOX:
                    float dash1[] = {10.0f};
                    stroke = new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f, dash1, 0.0f);

                    break;
                case Edge.ARCHI_LINK_ANY_TO_DB:
                    float dash2[] = {10.0f};
                    stroke = new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f, dash2, 0.0f);

                    break;
                case Edge.REAL_LINK_OK:

                    break;
                case Edge.REAL_LINK_ANTI:

                    break;
                case Edge.REAL_LINK_NOTEXPECTED:

                    break;
                default: {
                }

            }
        } else {
            //One of the node is not visible (it is in a group that is collapsed(not expanded)
            float dash2[] = {2.5f};
            stroke = new BasicStroke(2.5f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f, dash2, 0.0f);


        }
        return stroke;
    }
}
