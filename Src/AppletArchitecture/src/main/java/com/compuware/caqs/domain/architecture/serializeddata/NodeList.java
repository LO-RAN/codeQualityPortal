/*
 * NodeList.java
 *
 * Created on 24 octobre 2002, 11:19
 */
/**
 *
 * @author  fxa
 */
package com.compuware.caqs.domain.architecture.serializeddata;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Vector;

public class NodeList extends Vector<Node> implements Serializable {

    /** Creates a new instance of NodeList */
    public NodeList() {
    }

    public Vector<Node> sortByLevel() {
        Vector<Node> sortedNodes = new Vector<Node>();
        int maxLevel = 0;
        for (int i = 0; i < this.size(); i++) {
            Node currNode = this.elementAt(i);
            if (maxLevel < currNode.getLevel()) {
                maxLevel = currNode.getLevel();
            }
        }
        for (int level = 0; level < maxLevel + 1; level++) {
            for (int i = 0; i < this.size(); i++) {
                Node currNode = this.elementAt(i);
                if (currNode.getLevel() == level) {
                    sortedNodes.add(currNode);
                }
            }
        }
        return sortedNodes;
    }

    public Node[] getNodes() {
        Node[] nodes = new Node[this.size()];
        for (int i = 0; i < this.size(); i++) {
            nodes[i] = (Node) this.elementAt(i);
        }
        return nodes;
    }

    public Vector<Node> getNodesVector() {
        return this;
    }

    public ArrayList<Node> getAffectableNodes() {
        /*int notDeletedCount = 0;
        for (int i = 0 ; i < this.size() ; i++){            
        Node currentNode = (Node) this.elementAt(i);
        if(!currentNode.isDeleted() && currentNode instanceof NodeArchitectureModule){
        notDeletedCount++;
        }
        }*/
        //Node[] nodes = new Node[notDeletedCount];
        ArrayList<Node> nodes = new ArrayList<Node>();
        int j = 0;
        for (int i = 0; i < this.size(); i++) {
            Node currentNode = (Node) this.elementAt(i);
            if (!currentNode.isDeleted()
                    && currentNode instanceof NodeAffectable) {
                nodes.add((Node) currentNode);
                j++;
            }
        }

        Collections.sort(nodes);

        return nodes;
    }

    public Node[] getNotDeletedNodes() {
        int notDeletedCount = 0;
        for (int i = 0; i < this.size(); i++) {
            Node currentNode = (Node) this.elementAt(i);
            if (!currentNode.isDeleted()) {
                notDeletedCount++;
            }
        }
        Node[] nodes = new Node[notDeletedCount];
        int j = 0;
        for (int i = 0; i < this.size(); i++) {
            Node currentNode = (Node) this.elementAt(i);
            if (!currentNode.isDeleted()) {
                nodes[j] = currentNode;
                j++;
            }
        }

        // Tri bulle simple sur le vecteur parsedHtmlFiles
        // pour une sortie des fichiers par ordre alphabétique
        boolean sorted;
        do {
            sorted = true;
            for (int i = 0; i < nodes.length - 1; i++) {
                Node node1 = (Node) nodes[i];
                Node node2 = (Node) nodes[i + 1];
                if (node1.getLbl().compareTo(node2.getLbl()) > 0) {
                    nodes[i] = node2;
                    nodes[i + 1] = node1;
                    sorted = false;
                }
            }
        } while (!sorted);
        return nodes;
    }

    public int getNumberOfNodes() {
        return this.size();
    }

    public void addNode(Node n) {
        this.addElement(n);
    }

    public Node createNode(String lbl, int x, int y, int width, int height, int type) {
        if (width < 10) {
            width = 10;
        }
        if (height < 10) {
            height = 10;
        }
        Node n = null;
        switch (type) {
            case 0:
                n = new NodeArchitectureModule(lbl, x, y, 0, width, height);
                break;
            case 1:
                n = new NodeUseCase(lbl + "_UseCase", x, y, 0, width, height);
                break;
            case 2:
                n = new NodeDB(lbl + "_DB", x, y, 0, width, height);
                break;
            default://System.out.println(this + "Don't know this node type");
        }
        this.addElement(n);
        return n;
    }

    public Node createNode(int x, int y, int width, int height, int type) {
        return createNode("unnamed", x, y, width, height, type);
    }

    public void removeNode(Node n) {
        this.removeElement(n);
    }

    public Node findNodeById(String id) {
        for (int i = 0; i < this.size(); i++) {
            if (((Node) this.elementAt(i)).getId().equals(id)) {
                return (Node) this.elementAt(i);
            }
        }
        return null;
    }
}
