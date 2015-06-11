/*
 * ArchitectureModelCerators.java
 *
 * Created on 28 juin 2005, 11:34
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */
package com.compuware.caqs.presentation.applets.architecture;

/**
 * 
 * @author cwfr-fxalbouy
 */
import java.awt.Dimension;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import com.compuware.caqs.domain.architecture.serializeddata.ArchitectureModel;
import com.compuware.caqs.domain.architecture.serializeddata.Element;
import com.compuware.caqs.domain.architecture.serializeddata.ElementsCouple;
import com.compuware.caqs.domain.architecture.serializeddata.ElementsList;
import com.compuware.caqs.domain.architecture.serializeddata.Node;
import com.compuware.caqs.domain.architecture.serializeddata.NodeList;

public class ArchitectureModelCreators {

    /** Creates a new instance of ArchitectureModelCerators */
    private ArchitectureModelCreators() {
    }

    public static ArchitectureModel createFakeModel0(Dimension dim) {
        ArchitectureModel fakeArchitectureModel = ArchitectureModel.getCleanInstance();
        ElementsList unAssignedElements = new ElementsList();

        Vector<Element> goals = new Vector<Element>();
        for (int i = 0; i < 100; i++) {
            Element eltGoal1 = new Element("Goal " + i, "GOAL" + i);
            unAssignedElements.add(eltGoal1);
            goals.add(eltGoal1);
        }

        for (int i = 1; i < goals.size() - 2; i++) {

            fakeArchitectureModel.addElementsCouple(new ElementsCouple("fakeID", goals.elementAt(i), goals.elementAt(i
                    + 1)));
            fakeArchitectureModel.addElementsCouple(new ElementsCouple("fakeID", goals.elementAt(i
                    + 1), goals.elementAt(i)));
            fakeArchitectureModel.addElementsCouple(new ElementsCouple(
                    "fakeID", goals.elementAt(i + 2), goals.elementAt(i)));

            fakeArchitectureModel.addElementsCouple(new ElementsCouple("fakeID", goals.elementAt(i), goals.elementAt(i
                    + 2)));
        }

        fakeArchitectureModel.setUnassignedElementsList(unAssignedElements);

        fakeArchitectureModel.computeRealLinks();
        // ArchitectureModelCreators.oneToOnePackage(fakeArchitectureModel,dim);
        return fakeArchitectureModel;
    }

    public static ArchitectureModel createFakeModel2(Dimension dim) {
        ArchitectureModel fakeArchitectureModel = ArchitectureModel.getCleanInstance();
        ElementsList unAssignedElements = new ElementsList();

        Vector<Element> goals = new Vector<Element>();
        for (int i = 0; i < 7; i++) {
            Element eltGoal1 = new Element("Goal " + i, "GOAL" + i);
            unAssignedElements.add(eltGoal1);
            goals.add(eltGoal1);
        }

        Vector<Element> questions = new Vector<Element>();
        for (int i = 0; i < 40; i++) {
            Element eltGoal1 = new Element("Question " + i, "question" + i);
            unAssignedElements.add(eltGoal1);
            questions.add(eltGoal1);
        }

        Vector<Element> metrics = new Vector<Element>();
        for (int i = 0; i < 40; i++) {
            Element eltGoal1 = new Element("Metric " + i, "metric" + i);
            unAssignedElements.add(eltGoal1);
            metrics.add(eltGoal1);
        }

        for (int i = 0; i < questions.size(); i++) {
            int a = (int) (Math.random() * goals.size());
            int b = (int) (Math.random() * questions.size());

            fakeArchitectureModel.addElementsCouple(new ElementsCouple("fakeID", goals.elementAt(a), questions.elementAt(b)));
        }

        for (int i = 0; i < metrics.size(); i++) {
            int a = (int) (Math.random() * questions.size());
            int b = (int) (Math.random() * metrics.size());

            fakeArchitectureModel.addElementsCouple(new ElementsCouple("fakeID", questions.elementAt(a), metrics.elementAt(b)));

        }

        fakeArchitectureModel.setUnassignedElementsList(unAssignedElements);

        fakeArchitectureModel.computeRealLinks();
        // ArchitectureModelCreators.oneToOnePackage(fakeArchitectureModel,dim);
        return fakeArchitectureModel;
    }

    public static ArchitectureModel createFakeModel(Dimension dim) {
        ArchitectureModel fakeArchitectureModel = ArchitectureModel.getCleanInstance();
        // creates unassigned elements list
        ElementsList unAssignedElements = new ElementsList();
        // Create elements
        for (int h = 0; h < 1000; h++) {
            Element eltPkg = new Element("package" + h, "el" + h);
            unAssignedElements.add(eltPkg);
            for (int i = 0; i < 1; i++) {
                int maxW = (int) (1);
                Element eltCls = new Element("package" + h + ".Element" + i, "el"
                        + h + "-" + i);
                unAssignedElements.add(eltCls);
                eltPkg.addChild(eltCls);
                eltCls.addParent(eltPkg);
                for (int w = 0; w < maxW; w++) {
                    Element elt = new Element(eltCls.getLabel() + ".method" + w
                            + "()", "el" + h + "-" + i + "-" + w);
                    elt.addParent(eltCls);
                    eltCls.addChild(elt);
                    unAssignedElements.add(elt);
                }

            }
        }
        int maxCouples = (int) 3000;
        for (int i = 0; i < maxCouples; i++) {
            int n1index = (int) (Math.random() * unAssignedElements.size());
            int n2index = (int) (Math.random() * unAssignedElements.size());
            //System.out.println(n1index+"/"+n2index);
            fakeArchitectureModel.addElementsCouple(new ElementsCouple("fakeID", (Element) unAssignedElements.elementAt(n1index), (Element) unAssignedElements.elementAt(n2index)));
        }
        fakeArchitectureModel.setUnassignedElementsList(unAssignedElements);

        fakeArchitectureModel.computeRealLinks();

        return fakeArchitectureModel;
    }

    public static void matrixLayout(ArchitectureModel architectureModel) {
        // calculate number of layers
        NodeList nodes = architectureModel.getNodeList();

        for (int i = 0; i < nodes.size(); i++) {

            Node n = (Node) nodes.elementAt(i);
            if (!n.isDeleted()) {
                n.lockY(false);
                n.setDeepestPosition(0);
            }
        }
        Vector<Node> inspectedNodes = new Vector<Node>();
        for (int i = 0; i < nodes.size(); i++) {
            Node n = (Node) nodes.elementAt(i);
            // if (n.getCallers().size() == 0) {
            // numberOfUncalledNodes++;
            if (n.getCallees().size() > 0) {
                n.calculateDeepestPosition(-1, inspectedNodes);
            }
            // }

        }
        // getDeepst level
        int deepest = 0;
        for (int i = 0; i < nodes.size(); i++) {
            Node n = (Node) nodes.elementAt(i);
            if (!n.isDeleted()) {
                if (n.getDeepestPosition() > deepest) {
                    deepest = n.getDeepestPosition();
                }
            }
        }

        // number of nodes by level
        Vector<Vector<Node>> levels = new Vector<Vector<Node>>();

        for (int i = 0; i < (deepest + 1); i++) {
            levels.add(i, new Vector<Node>());
        }

        for (int i = 0; i < nodes.size(); i++) {
            Node n = (Node) nodes.elementAt(i);
            if (!n.isDeleted()) {
                levels.elementAt(n.getDeepestPosition()).add(n);
            }
        }

        // do layout
        for (int i = 0; i < levels.size(); i++) {
            Vector<Node> levelI = levels.elementAt(i);
            for (int j = 0; j < levelI.size(); j++) {
                Node n = levels.elementAt(i).elementAt(j);
                if (!n.isDeleted()) {
                    n.setY(200 + 200 * i);
                    n.setX(200 + 200 * j);
                    n.lockY(true);
                }
            }
        }
        architectureModel.computeRealLinks();
    }

    public static void assignUncalledToUncalledPackage(ArchitectureModel architectureModel) {
        ElementsList unassigned = architectureModel.getUnassignedElementsList();
        Node curNode = architectureModel.getNodeList().createNode("NEVER CALLED PACKAGE", 300, 100, 10, 10, 0);
        curNode.setIsAutoLayoutProtected(true);
        Vector<Element> elementsAssigned = new Vector<Element>();
        for (int i = 0; i < unassigned.size(); i++) {
            Element e = (Element) unassigned.elementAt(i);
            if (!isCalled(architectureModel, e)) {
                // System.out.println("Assigning " + e);
                e.setAssignedToNode(curNode);
                curNode.addElement(e);
                elementsAssigned.add(e);
            }
        }
        for (int j = 0; j < elementsAssigned.size(); j++) {
            unassigned.remove(elementsAssigned.elementAt(j));
        }
        architectureModel.computeRealLinks();
    }

    public static void assignUncallerToUncallerPackage(ArchitectureModel architectureModel) {
        ElementsList unassigned = architectureModel.getUnassignedElementsList();
        Node curNode = architectureModel.getNodeList().createNode("NEVER CALLER PACKAGE", 300, 400, 10, 10, 0);
        curNode.setIsAutoLayoutProtected(true);
        Vector<Element> elementsAssigned = new Vector<Element>();
        for (int i = 0; i < unassigned.size(); i++) {
            Element e = (Element) unassigned.elementAt(i);
            if (!isCaller(architectureModel, e)) {
                // System.out.println("Assigning " + e);
                e.setAssignedToNode(curNode);
                curNode.addElement(e);
                elementsAssigned.add(e);
            }
        }
        for (int j = 0; j < elementsAssigned.size(); j++) {
            unassigned.remove(elementsAssigned.elementAt(j));
        }
        architectureModel.computeRealLinks();
    }

    public static void assignNeverUsedToNeverUsedPackage(ArchitectureModel architectureModel) {
        ElementsList unassigned = architectureModel.getUnassignedElementsList();
        Node curNode = architectureModel.getNodeList().createNode("NEVER USED PACKAGE", 100, 100, 10, 10, 0);
        curNode.setIsAutoLayoutProtected(true);
        Vector<Element> elementsAssigned = new Vector<Element>();
        for (int i = 0; i < unassigned.size(); i++) {
            Element e = (Element) unassigned.elementAt(i);
            if (!isUsed(architectureModel, e)) {
                // System.out.println("Assigning " + e);
                e.setAssignedToNode(curNode);
                curNode.addElement(e);
                elementsAssigned.add(e);
            }
        }
        for (int j = 0; j < elementsAssigned.size(); j++) {
            unassigned.remove(elementsAssigned.elementAt(j));
        }
        architectureModel.computeRealLinks();
    }

    private static boolean isCaller(ArchitectureModel architectureModel, Element e) {
        Vector couples = architectureModel.getElementsCouples();
        for (int j = 0; j < couples.size(); j++) {
            ElementsCouple c = (ElementsCouple) couples.elementAt(j);
            if (c.getFrom() == e) {
                // e is called
                return true;
            }
        }
        // System.out.println("is never called" + e);
        return false;
    }

    private static boolean isUsed(ArchitectureModel architectureModel, Element e) {
        Vector couples = architectureModel.getElementsCouples();
        for (int j = 0; j < couples.size(); j++) {
            ElementsCouple c = (ElementsCouple) couples.elementAt(j);
            if (c.getFrom() == e | c.getTo() == e) {
                // e is called
                return true;
            }
        }
        // System.out.println("is never used" + e);
        return false;
    }

    private static boolean isCalled(ArchitectureModel architectureModel, Element e) {
        Vector couples = architectureModel.getElementsCouples();
        for (int j = 0; j < couples.size(); j++) {
            ElementsCouple c = (ElementsCouple) couples.elementAt(j);
            if (c.getTo() == e) {
                // e is called
                return true;
            }
        }
        // System.out.println("is never called" + e);
        return false;
    }

    public static void oneToOneClasses(ArchitectureModel architectureModel) {
        Hashtable<String, Vector<Element>> masterElements = new Hashtable<String, Vector<Element>>();
        ElementsList unassigned = architectureModel.getUnassignedElementsList();
        NodeList nodes = architectureModel.getNodeList();

        for (int i = 0; i < unassigned.size(); i++) {
            Element curElt = (Element) unassigned.elementAt(i);
            if (curElt.getChildren() == null || curElt.getChildren().isEmpty()) {
                String masterElement = null;
                if (curElt.getParent() != null) {
                    masterElement = curElt.getParent().getLabel();

                    if (masterElements.get(masterElement) == null) {
                        Vector<Element> subElts = new Vector<Element>();
                        subElts.addElement(curElt);
                        masterElements.put(masterElement, subElts);
                    } else {
                        Vector<Element> subElts = (Vector<Element>) masterElements.get(masterElement);
                        subElts.addElement(curElt);
                        masterElements.put(masterElement, subElts);
                    }
                }
            }
        }

        Enumeration keys = masterElements.keys();
        Vector<String> keysVector = new Vector<String>();

        while (keys.hasMoreElements()) {
            keysVector.addElement((String) keys.nextElement());
        }
        // Tri bulle simple sur le vecteur parsedHtmlFiles
        // pour une sortie des fichiers par ordre alphabétique
        boolean sorted;
        do {
            sorted = true;
            for (int i = 0; i < keysVector.size() - 1; i++) {
                String str1 = (String) keysVector.elementAt(i);
                String str2 = (String) keysVector.elementAt(i + 1);
                if (str1.compareTo(str2) > 0) {
                    keysVector.setElementAt(str2, i);
                    keysVector.setElementAt(str1, i + 1);
                    sorted = false;
                }
            }
        } while (!sorted);

        for (int i = 0; i < keysVector.size(); i++) {
            String key = (String) keysVector.elementAt(i);
            int x = (int) (10 + 200 * Math.random());
            int y = (int) (10 + 200 * Math.random());
            Node curNode = nodes.createNode(key, x, y, 10, 10, 0);

            Vector subElts = (Vector) masterElements.get(key);
            for (int z = 0; z < subElts.size(); z++) {
                Element curElt = (Element) subElts.elementAt(z);
                curNode.addElement(curElt);
            }
        }
        unassigned.removeAllElements();
        architectureModel.computeRealLinks();
        architectureModel.putNodesInMatrix();

    }

    public static void oneToOneMethods(ArchitectureModel architectureModel) {

        ElementsList unassigned = architectureModel.getUnassignedElementsList();
        NodeList nodes = architectureModel.getNodeList();
        for (int i = 0; i < unassigned.size(); i++) {
            Element curEl = (Element) unassigned.elementAt(i);
            int x = (int) (10 + 200 * Math.random());
            int y = (int) (10 + 200 * Math.random());
            Node curNode = nodes.createNode(curEl.getLabel(), x, y, 10, 10, 0);
            curNode.addElement(curEl);
        }
        unassigned.removeAllElements();
        architectureModel.computeRealLinks();
        architectureModel.putNodesInMatrix();
    }

    public static void oneToOnePackage(ArchitectureModel architectureModel, Dimension graphPanelDimension) {
        /*
         * Hashtable masterElements = new Hashtable(); ElementsList unassigned =
         * architectureModel.getUnassignedElementsList(); NodeList nodes =
         * architectureModel.getNodeList();
         *
         * for (int i = 0; i < unassigned.size(); i++) { Element curElt =
         * (Element) unassigned.elementAt(i); String masterElement =
         * curElt.getMasterElementNameOfMasterElementName().getLabel();
         *
         * if (masterElements.get(masterElement) == null) { Vector subElts = new
         * Vector(); subElts.addElement(curElt);
         * masterElements.put(masterElement, subElts); } else { Vector subElts =
         * (Vector) masterElements.get(masterElement);
         * subElts.addElement(curElt); masterElements.put(masterElement,
         * subElts); } }
         *
         * Enumeration keys = masterElements.keys(); Vector keysVector = new
         * Vector();
         *
         * while (keys.hasMoreElements()) {
         * keysVector.addElement(keys.nextElement()); } // Tri bulle simple
         * boolean sorted; do { sorted = true; for (int i = 0; i <
         * keysVector.size() - 1; i++) { String str1 = (String)
         * keysVector.elementAt(i); String str2 = (String)
         * keysVector.elementAt(i + 1); if (str1.compareTo(str2) > 0) {
         * keysVector.setElementAt(str2, i); keysVector.setElementAt(str1, i +
         * 1); sorted = false; } } } while (!sorted);
         *
         * for (int i = 0; i < keysVector.size(); i++) { String key = (String)
         * keysVector.elementAt(i); Dimension d = graphPanelDimension; int x =
         * (int) (10 + (d.width - 20) * Math.random()); int y = (int) (10 +
         * (d.height - 20) * Math.random()); Node curNode =
         * nodes.createNode(key, x, y, 10, 10,0);
         *
         * Vector subElts = (Vector) masterElements.get(key); for (int z = 0; z <
         * subElts.size(); z++) { Element curElt = (Element)
         * subElts.elementAt(z); curNode.addElement(curElt); } }
         * unassigned.removeAllElements(); architectureModel.computeRealLinks();
         */
    }
}
