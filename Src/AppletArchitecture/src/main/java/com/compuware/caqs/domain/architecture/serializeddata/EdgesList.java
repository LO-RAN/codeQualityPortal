/*
 * EdgesList.java
 *
 * Created on 24 octobre 2002, 11:28
 */
/**
 *
 * @author  fxa
 */
package com.compuware.caqs.domain.architecture.serializeddata;

import java.io.Serializable;
import java.util.Enumeration;
import java.util.Vector;

public class EdgesList extends Vector<Edge> implements Serializable {

    /** Creates a new instance of EdgesList */
    public EdgesList() {
    }

    public void deleteEdge(Edge e) {
        e.setDeleted(true);
    }

    public void deleteRealEdges() {
        Enumeration<Edge> edgesEnum = this.elements();
        while (edgesEnum.hasMoreElements()) {
            Edge curEd = (Edge) edgesEnum.nextElement();
            //System.out.println("edge " +curEd);
            if (curEd.isReal()) {
                curEd.setDeleted(true);
            }
        }
    }

    public void deleteNode(Node n) {
        //set node as deleted
        n.setDeleted(true);
        //set edges as deleted
        for (int i = 0; i < this.size(); i++) {
            Edge e = (Edge) this.elementAt(i);
            if ((n == e.from) || (n == e.to)) {
                e.setDeleted(true);
            }
        }
    }

    public Vector<Edge> getEdgesVector() {
        return this;
    }

    public Edge[] getEdges() {
        return this.toArray(new Edge[this.size()]);
    }

    public boolean nodeCallExists(Node to) {
        for (int i = 0; i < this.size(); i++) {
            Edge curr = (Edge) this.elementAt(i);

            if (!curr.isDeleted()) {
                if (curr.to == to) {
                    return true;
                }
            }
        }
        return false;
    }

    protected boolean edgeExistsNoType(Node from, Node to) {
        for (int i = 0; i < this.size(); i++) {
            Edge curr = (Edge) this.elementAt(i);

            if (!curr.isDeleted()) {
                if (curr.from == from && curr.to == to) {
                    return true;
                }
            }
        }
        return false;
    }

    protected boolean edgeExists(Node from, Node to, int type) {
        for (int i = 0; i < this.size(); i++) {
            Edge curr = (Edge) this.elementAt(i);

            if (!curr.isDeleted() && curr.getLinkType() >= type) {
                if (curr.from == from && curr.to == to) {
                    return true;
                }
            }
        }
        return false;
    }

    protected boolean edgeExistsDontCareDirection(Node from, Node to) {
        for (int i = 0; i < this.size(); i++) {
            Edge curr = (Edge) this.elementAt(i);

            if (!curr.isDeleted()) {
                if ((curr.from == from && curr.to == to) || (curr.from == to && curr.to
                        == from)) {
                    return true;
                }
            }
        }
        return false;
    }

    protected boolean edgeExists(Node from, Node to) {
        for (int i = 0; i < this.size(); i++) {
            Edge curr = (Edge) this.elementAt(i);

            if (!curr.isDeleted()) {
                if (curr.from == from && curr.to == to) {
                    return true;
                }
            }
        }
        return false;
    }

    protected Edge getRealEdge(Node from, Node to) {
        for (int i = 0; i < this.size(); i++) {
            Edge curr = (Edge) this.elementAt(i);
            if (!curr.isDeleted() && curr.isReal()) {
                if (curr.from == from && curr.to == to) {
                    return curr;
                }
            }
        }
        return null;
    }
}
