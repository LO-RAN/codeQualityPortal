/*
 * @(#)DefaultTreeCellRenderer.java	1.51 04/01/23
 *
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.compuware.caqs.presentation.applets.architecture.panels.selection;

import com.compuware.caqs.presentation.applets.architecture.panels.Tools;

import java.awt.Component;
import javax.swing.ImageIcon;
import javax.swing.JTree;

/**
 * Displays an entry in a tree.
 * <code>DefaultTreeCellRenderer</code> is not opaque and
 * unless you subclass paint you should not change this.
 * See <a
href="http://java.sun.com/docs/books/tutorial/uiswing/components/tree.html">How to Use Trees</a> 
 * in <em>The Java Tutorial</em>
 * for examples of customizing node display using this class.
 * <p>                                                                        
 * 
 * <strong><a name="override">Implementation Note:</a></strong>
 * This class overrides
 * <code>invalidate</code>,
 * <code>validate</code>,
 * <code>revalidate</code>,
 * <code>repaint</code>,
 * and
 * <code>firePropertyChange</code>
 * solely to improve performance.
 * If not overridden, these frequently called methods would execute code paths
 * that are unnecessary for the default tree cell renderer.
 * If you write your own renderer,
 * take care to weigh the benefits and
 * drawbacks of overriding these methods.
 *
 * <p>
 * <strong>Warning:</strong>
 * Serialized objects of this class will not be compatible with
 * future Swing releases. The current serialization support is
 * appropriate for short term storage or RMI between applications running
 * the same version of Swing.  As of 1.4, support for long term storage
 * of all JavaBeans<sup><font size="-2">TM</font></sup>
 * has been added to the <code>java.beans</code> package.
 * Please see {@link java.beans.XMLEncoder}.
 * 
 * @version 1.51 01/23/04
 * @author Rob Davis
 * @author Ray Ryan
 * @author Scott Violet
 */
public class MyTreeCellRenderer extends javax.swing.tree.DefaultTreeCellRenderer {

    /**
     * Configures the renderer based on the passed in components.
     * The value is set from messaging the tree with
     * <code>convertValueToText</code>, which ultimately invokes
     * <code>toString</code> on <code>value</code>.
     * The foreground color is set based on the selection and the icon
     * is set based on on leaf and expanded.
     */
    public Component getTreeCellRendererComponent(JTree tree, Object value,
            boolean sel,
            boolean expanded,
            boolean leaf, int row,
            boolean hasFocus) {
        String stringValue = tree.convertValueToText(value, sel,
                expanded, leaf, row, hasFocus);

        // this.tree = tree;
        //this.hasFocus = hasFocus;
        setText(stringValue);
        if (sel) {
            setForeground(getTextSelectionColor());
        } else {
            setForeground(getTextNonSelectionColor());
        }
        // There needs to be a way to specify disabled icons.
        if (!tree.isEnabled()) {
            //System.out.println("Object is " + value);
            setEnabled(false);
            if (leaf) {
                setDisabledIcon(getLeafIcon());
            } else if (expanded) {
                setDisabledIcon(getOpenIcon());
            } else {
                setDisabledIcon(getClosedIcon());
            }
        } else {
            setEnabled(true);
            //System.out.println("Object is " + value + " class : "+ value.getClass() );
            if (value instanceof NodeTreeNode) {
                ImageIcon imgModules = Tools.createAppletImageIcon("modules.gif", "");
                setIcon(imgModules);
            } else {
                if (leaf) {
                    setIcon(getLeafIcon());
                } else if (expanded) {
                    setIcon(getOpenIcon());
                } else {
                    setIcon(getClosedIcon());
                }
            }
        }
        setComponentOrientation(tree.getComponentOrientation());

        selected = sel;

        return this;
    }
}
