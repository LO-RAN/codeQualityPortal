/*
 * ArchitectureModel.java
 *
 * Created on 10 avril 2003, 10:22
 * @author  cwfr-fxalbouy
 */
package com.compuware.caqs.domain.architecture.serializeddata;

import java.awt.Point;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Vector;

public final class ArchitectureModel implements Serializable, DrawnObjectListener {

    static private ArchitectureModel instance = new ArchitectureModel();
    private boolean modelChanged = true;
    private String eaId = "";
    private String baselineId = "";
    private boolean isModifiable = true;
    private EdgesList edgesList = new EdgesList();
    private NodeList nodeList = new NodeList();
    private ElementsList assignedElements = new ElementsList();
    private ElementsList unAssignedElements = new ElementsList();
    private Vector<ElementsCouple> elementsCouples = new Vector<ElementsCouple>();
    transient private Vector<ArchitectureModelListener> modelListener;

    /** Creates a new instance of ArchitectureModel */
    private ArchitectureModel() {
        this.modelListener = new Vector<ArchitectureModelListener>();
    }

    public void DrawnObjectChanged() {
        this.fireModelChangedEvent();
    }

    static public DrawnObject deepestDrawnObject(Vector<DrawnObject> doVector) {
        DrawnObject obj = null;
        int maxLevel = -1;
        for (int i = 0; i < doVector.size(); i++) {
            DrawnObject currDO = doVector.elementAt(i);
            if (currDO.getLevel() > maxLevel) {
                obj = currDO;
                maxLevel = currDO.getLevel();
            }
        }
        return obj;
    }

    public void resetMultipleSelection() {
        Enumeration<Node> nodes = this.nodeList.elements();
        while (nodes.hasMoreElements()) {
            Node node = nodes.nextElement();
            node.setSelected(false, 0, 0, null);
        }
    }

    static public DrawnObject isClickOnADrawnObject(Vector<DrawnObject> drawnObjects, int x, int y) {
        DrawnObject retour = null;
        Vector<DrawnObject> doFound = new Vector<DrawnObject>();
        if (drawnObjects != null) {
            Enumeration drawnObjectsEnum = drawnObjects.elements();
            while (drawnObjectsEnum.hasMoreElements()) {
                DrawnObject currDO = (DrawnObject) drawnObjectsEnum.nextElement();
                if (currDO.onDrawnObject(x, y) && currDO.isActionnable()) {
                    doFound.add(currDO);
                }
            }
        }
        //System.out.println("Possible nodes are " + doFound.size());
        if (!doFound.isEmpty()) {
            if (doFound.size() > 1) {
                // return the deepest element.
                retour = ArchitectureModel.deepestDrawnObject(doFound);
            } else {
                retour = doFound.elementAt(0);
            }
        }
        return retour;
    }

    public static ArchitectureModel getInstance() {
        return instance;
    }

    public synchronized static ArchitectureModel getCleanInstance() {
        instance = new ArchitectureModel();
        return instance;
    }

    public void selectedDrawnObject(DrawnObject object, boolean bo, double selectionType, int level, Vector<DrawnObject> selectedNodes) {
        object.setSelected(bo, selectionType, level, selectedNodes);
        if (bo) {
            fireNewSelectElementEvent(object);
        }
    }

    public void selectedMultipleNodes(Node object, boolean bo, double selectionType, int level, Vector<DrawnObject> selectedNodes) {
        object.setSelected(bo, selectionType, level, selectedNodes);
    }

    public void putNodesInMatrix() {
        // put nodes in matrix

        NodeList nodeList = this.getNodeList();
        Node[] nodeArray = nodeList.getNodes();
        int numberOfNodes = nodeArray.length;

        double sqrRoot = Math.sqrt(numberOfNodes);
        int nodeVerticalGap = 100;
        int nodeHorizontalGap = 200;
        int lIndex = 0;
        int cIndex = 0;
        double previousNodeWidth = 0;

        for (int i = 0; i < numberOfNodes; i++) {
            if (cIndex > sqrRoot) {
                cIndex = 0;
                previousNodeWidth = 0;
                lIndex++;
            }
            Node n = nodeArray[i];
            // System.out.println("previous : "+previousNodeWidth);
            n.setX(nodeHorizontalGap * cIndex + previousNodeWidth);
            n.setY(nodeVerticalGap * lIndex);
            previousNodeWidth = n.getWidth();
            cIndex++;
        }

        // END : put nodes in matrix
    }

    /*
     * ADD A CALL TO THIS METHOD TO ALL METHODES THAT CHANGES MODEL
     */
    public void addListener(ArchitectureModelListener listener) {
        // avoid multiple registration
        if (!this.modelListener.contains(listener)) {
            this.modelListener.add(listener);
            listener.architectureModelChanged();
        }
    }

    public void fireModelChangedEvent() {
        if(this.modelListener==null) {
            this.modelListener = new Vector<ArchitectureModelListener>();
        }
        Enumeration<ArchitectureModelListener> listenersEnum = this.modelListener.elements();
        while (listenersEnum.hasMoreElements()) {
            ArchitectureModelListener aListener = (ArchitectureModelListener) listenersEnum.nextElement();
            aListener.architectureModelChanged();
        }
    }

    public void fireNewSelectElementEvent(DrawnObject object) {
        Enumeration<ArchitectureModelListener> listenersEnum = this.modelListener.elements();
        while (listenersEnum.hasMoreElements()) {
            ArchitectureModelListener aListener = (ArchitectureModelListener) listenersEnum.nextElement();
            aListener.newSelectedElement(object);
        }
    }

    public void fireEndMultipleSelection(Vector<Node> multipleSelectedNodes) {
        Enumeration<ArchitectureModelListener> listenersEnum = this.modelListener.elements();
        while (listenersEnum.hasMoreElements()) {
            ArchitectureModelListener aListener = (ArchitectureModelListener) listenersEnum.nextElement();
            aListener.multipleSelectionDone(multipleSelectedNodes);
        }
    }

    public void fireSelectedItemDeleted() {
        Enumeration<ArchitectureModelListener> listenersEnum = this.modelListener.elements();
        while (listenersEnum.hasMoreElements()) {
            ArchitectureModelListener aListener = (ArchitectureModelListener) listenersEnum.nextElement();
            aListener.selectedItemDeleted();
        }
    }

    public Point getMinPoint() {

        double minX = 0;
        double minY = 0;
        Enumeration<Node> nodesEnum = this.nodeList.elements();
        while (nodesEnum.hasMoreElements()) {
            Node currNode = (Node) nodesEnum.nextElement();
            if (!currNode.isDeleted()) {
                double nodX = currNode.getCenterX() + currNode.getWidth();
                if (minX > nodX) {
                    minX = nodX;
                }
                double nodY = currNode.getCenterY() + currNode.getHeight();
                if (minY > nodY) {
                    minY = nodY;
                }
            }
        }
        Point minPoint = new Point((int) minX, (int) minY);

        return minPoint;
    }

    public Point getMaxPoint() {

        double maxX = 0;
        double maxY = 0;
        Enumeration<Node> nodesEnum = this.nodeList.elements();
        while (nodesEnum.hasMoreElements()) {
            Node currNode = (Node) nodesEnum.nextElement();
            if (!currNode.isDeleted()) {
                double nodX = currNode.getCenterX() + currNode.getWidth();
                if (maxX < nodX) {
                    maxX = nodX;
                }
                double nodY = currNode.getCenterY() + currNode.getHeight();
                if (maxY < nodY) {
                    maxY = nodY;
                }
            }
        }
        Point maxPoint = new Point((int) maxX, (int) maxY);

        return maxPoint;
    }

    public Vector<ElementsCouple> getElementsCouples() {
        return this.elementsCouples;
    }

    public String toString() {
        StringBuffer out = new StringBuffer();
        out.append("Architecture Model\n");
        out.append("for ea : " + this.eaId + "\n");
        out.append("Baseline : " + this.baselineId + "\n");
        out.append("number of edges : " + this.edgesList.size() + "\n");
        out.append("number of architecture edges : "
                + this.getNumberOfEdgesByType(Edge.ARCHI_LINK) + "\n");
        out.append("number of unexpected edges : "
                + this.getNumberOfEdgesByType(Edge.REAL_LINK_NOTEXPECTED) + "\n");
        out.append("number of antilink edges : "
                + this.getNumberOfEdgesByType(Edge.REAL_LINK_ANTI) + "\n");
        out.append("number of Ok edges : "
                + this.getNumberOfEdgesByType(Edge.REAL_LINK_OK) + "\n");
        out.append("number of deleted edges : " + this.getNumberOfDeletedEdges()
                + "\n");
        out.append("number of nodes : " + this.nodeList.size() + "\n");
        out.append("number of assigned elements : "
                + this.assignedElements.size() + "\n");
        out.append("number of unassigned elements : "
                + this.unAssignedElements.size() + "\n");
        out.append("number of elements couples : " + this.elementsCouples.size()
                + "\n");
        return out.toString();
    }

    public void setIsModifiable(boolean value) {
        this.isModifiable = value;
    }

    public boolean isModifiable() {
        return this.isModifiable;
    }

    public void setUnassignedElementsList(ElementsList el) {
        this.unAssignedElements = el;
    }

    public ElementsList getAssignedElementsList() {
        return this.assignedElements;
    }

    public ElementsList getUnassignedElementsList() {
        return this.unAssignedElements;
    }

    public void setEdgesList(EdgesList edgesList) {
        this.edgesList = edgesList;
    }

    public EdgesList getEdgesList() {
        return this.edgesList;
    }

    public void setNodeList(NodeList nodesList) {
        this.nodeList = nodesList;
    }

    public NodeList getNodeList() {
        return this.nodeList;
    }

    public void setEaId(String id) {
        this.eaId = id;
    }

    public String getEaId() {
        return this.eaId;
    }

    public void setBaseLineId(String id) {
        this.baselineId = id;
    }

    public String getBaseLineID() {
        return this.baselineId;
    }

    public void addElementsCouple(ElementsCouple ec) {
        this.elementsCouples.add(ec);
    }

    public int getNumberOfDeletedEdges() {
        int returnValue = 0;
        Enumeration<Edge> enumEdges = this.edgesList.elements();
        while (enumEdges.hasMoreElements()) {
            Edge currEdge = (Edge) enumEdges.nextElement();
            if (currEdge.isDeleted()) {
                returnValue++;
            }
        }
        return returnValue;
    }

    public int getNumberOfDeletedNodes() {
        int returnValue = 0;
        Enumeration<Node> enumNodes = this.nodeList.elements();
        while (enumNodes.hasMoreElements()) {
            Node currNode = (Node) enumNodes.nextElement();
            if (currNode.isDeleted()) {
                returnValue++;
            }
        }
        return returnValue;
    }

    public int getNumberOfEdgesByType(int type) {
        int returnValue = 0;
        Enumeration<Edge> enumEdges = this.edgesList.elements();
        while (enumEdges.hasMoreElements()) {
            Edge currEdge = (Edge) enumEdges.nextElement();
            if (!currEdge.isDeleted()) {
                if (currEdge.getLinkType() == type) {
                    returnValue++;
                }
            }
        }
        return returnValue;
    }

    public void addNode(Node n) {
        n.addListener(this);
        this.nodeList.addNode(n);
    }

    public void addEdge(String id, String idFrom, String idTo, int linkType, List<ElementsCouple> couples) {
        Edge e = new Edge();
        e.setId(id);
        e.from = this.nodeList.findNodeById(idFrom);
        e.to = this.nodeList.findNodeById(idTo);
        e.from.addCallee(e.to);
        e.to.addCaller(e.from);

        e.setLinkType(linkType);
        if (couples != null) {
            e.elementsCouplesList = couples;
        } else {
            e.elementsCouplesList = new ArrayList<ElementsCouple>();
        }
        e.addListener(this);
        this.edgesList.addElement(e);
    }

    public void addEdge(Node from, Node to, int linkType, List<ElementsCouple> couples) {
        //System.out.println("adding edge " + linkType);
        Edge e = null;
        switch (linkType) {
            case Edge.ARCHI_LINK:
            case Edge.ARCHI_LINK_USECASE_TO_BOX:
            case Edge.ARCHI_LINK_ANY_TO_DB:
                if (!this.edgesList.edgeExists(from, to, linkType)) {
                    e = new Edge(from, to, linkType, couples);
                }
                break;
            default:
                e = new Edge(from, to, linkType, couples);
                e.setDeleted(false);
        }
        if (e != null) {
            this.edgesList.addElement(e);
            e.addListener(this);
        }
    }

    public void deleteAll() {
        Enumeration nodesEnum = this.getNodeList().getNodesVector().elements();
        while (nodesEnum.hasMoreElements()) {
            this.getEdgesList().deleteNode((Node) nodesEnum.nextElement());
        }
        this.edgesList.deleteRealEdges();
        this.computeRealLinks();
        fireModelChangedEvent();
    }

    public void computeRealLinks() {
        // System.out.println("Model before Computing real links\n" + this);
        this.edgesList.deleteRealEdges();

        Enumeration eltCouplesEnum = this.elementsCouples.elements();

        while (eltCouplesEnum.hasMoreElements()) {
            ElementsCouple curEltCouple = (ElementsCouple) eltCouplesEnum.nextElement();
            Element from = curEltCouple.getFrom();
            Element to = curEltCouple.getTo();

            Node nFrom = null;
            if (from != null) {
                nFrom = from.getAssignedToNode();
            }
            Node nTo = null;
            if (to != null) {
                nTo = to.getAssignedToNode();
            }

            if (nFrom != null && nTo != null && nFrom != nTo) {
                // both elements are assigned to a node
                // both elements are ssigned to different nodes
                Edge ed = this.getEdgesList().getRealEdge(nFrom, nTo);
                // System.out.println("edge is "+ed);
                if (ed != null) {
                    // System.out.println("edge already exists");
                    ed.getCouples().add(curEltCouple);
                } else {
                    List<ElementsCouple> elementsCouples = new ArrayList<ElementsCouple>();
                    elementsCouples.add(curEltCouple);
                    if (this.getEdgesList().edgeExists(nFrom, nTo, 100)) {
                        this.addEdge(nFrom, nTo, Edge.REAL_LINK_OK, elementsCouples);
                    } else if (this.getEdgesList().edgeExists(nTo, nFrom, 100)) {
                        this.addEdge(nFrom, nTo, Edge.REAL_LINK_ANTI, elementsCouples);
                    } else {
                        this.addEdge(nFrom, nTo, Edge.REAL_LINK_NOTEXPECTED, elementsCouples);
                    }
                }
            }
        }
        // System.out.println("Model after Computing real links\n" + this);
        fireModelChangedEvent();
    }

    public Vector<DrawnObject> sortDrawableObjectsBeforeDrawings(Point topLeft, Point bottomRight, boolean drawEdges, boolean drawRealLinks, boolean drawArchiLinks) {
        Vector<DrawnObject> drawnObjects = new Vector<DrawnObject>();

        if (this != null) {
            if (this.modelChanged) {
                // first add the nodes so they will be drawn before edges
                double maxX = 0;
                double maxY = 0;
                Enumeration<Node> nodesEnum = this.getNodeList().elements();
                while (nodesEnum.hasMoreElements()) {
                    Node currNode = (Node) nodesEnum.nextElement();
                    if (!currNode.isDeleted()) {
                        if (currNode.getBottomRightX() > topLeft.x && currNode.getUpperLeftX()
                                < bottomRight.x && currNode.getBottomRightY()
                                > topLeft.y && currNode.getUpperLeftY()
                                < bottomRight.y) {
                            drawnObjects.addElement((DrawnObject) currNode);
                        }
                    }
                    // look for the max Point
                    if (!currNode.isDeleted()) {
                        double nodX = currNode.getCenterX()
                                + currNode.getWidth();
                        if (maxX < nodX) {
                            maxX = nodX;
                        }
                        double nodY = currNode.getCenterY()
                                + currNode.getHeight();
                        if (maxY < nodY) {
                            maxY = nodY;
                        }
                    }
                }

                if (drawEdges) {
                    Enumeration edgesEnum = this.getEdgesList().elements();
                    while (edgesEnum.hasMoreElements()) {
                        // edges doesn't need to be sorted has the real edge are
                        // always
                        // computed after the architecture edges
                        Edge e = (Edge) edgesEnum.nextElement();
                        if (!e.isDeleted() && (drawnObjects.contains(e.getFrom())
                                || drawnObjects.contains(e.getTo()))) {
                            if (e.isReal() && drawRealLinks) {
                                drawnObjects.addElement(e);
                            }
                            if (!e.isReal() && drawArchiLinks) {
                                drawnObjects.addElement(e);
                            }
                        }
                    }
                }
            }
        }
        return drawnObjects;
    }

    public void translateAllModel(double dx, double dy) {
        Enumeration nodesEnum = this.getNodeList().elements();
        while (nodesEnum.hasMoreElements()) {
            Node n = (Node) nodesEnum.nextElement();
            if (!n.isAutoLayoutProtected()) {
                n.setX(n.getCenterX() + dx);
                n.setY(n.getCenterY() + dy);
            }
        }
    }

    public static void setInstance(ArchitectureModel model) {
        instance = model;
    }

    public Vector<ArchitectureModelListener> getListeners() {
        return this.modelListener;
    }

    public void resetListeners(Vector<ArchitectureModelListener> listeners) {
        this.modelListener = listeners;
    }
}
