/*
 * ArchitectureModelListener.java
 *
 * Created on 27 juillet 2005, 11:14
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */

package com.compuware.caqs.domain.architecture.serializeddata;

import java.util.Vector;

/**
 *
 * @author cwfr-fxalbouy
 */
public interface ArchitectureModelListener {
    public void architectureModelChanged();
    public void newSelectedElement(DrawnObject object);
    public void multipleSelectionDone(Vector<Node> multipleSelectedNodes);
    public void selectedItemDeleted();
}
