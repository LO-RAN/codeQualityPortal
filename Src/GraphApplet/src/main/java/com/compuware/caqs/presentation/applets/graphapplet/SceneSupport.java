package com.compuware.caqs.presentation.applets.graphapplet;

import java.awt.BorderLayout;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import org.netbeans.api.visual.widget.Scene;
import org.openide.util.RequestProcessor;

/**
 * @author cwfr-fdubois
 */
public class SceneSupport {

    public static void show (final Scene scene, final JComponent satelliteView) {
        if (SwingUtilities.isEventDispatchThread ())
            showEDT (scene, satelliteView);
        else
            SwingUtilities.invokeLater (new Runnable() {
                public void run () {
                    showEDT (scene, satelliteView);
                }
            });
    }

    private static void showEDT (Scene scene, JComponent satelliteView) {
        JComponent sceneView = scene.getView ();
        if (sceneView == null)
            sceneView = scene.createView ();
        show (sceneView, satelliteView);
    }

    public static void show (final JComponent sceneView, final JComponent satelliteView) {
        if (SwingUtilities.isEventDispatchThread ())
            showEDT (sceneView, satelliteView);
        else
            SwingUtilities.invokeLater (new Runnable() {
                public void run () {
                    showEDT (sceneView, satelliteView);
                }
            });
    }

    private static void showEDT (JComponent sceneView, JComponent satelliteView) {
        JScrollPane panel = new JScrollPane (sceneView);
        panel.getHorizontalScrollBar ().setUnitIncrement (32);
        panel.getHorizontalScrollBar ().setBlockIncrement (256);
        panel.getVerticalScrollBar ().setUnitIncrement (32);
        panel.getVerticalScrollBar ().setBlockIncrement (256);
        showCoreEDT (panel, satelliteView);
    }

    public static void showCore (final JComponent view, final JComponent satelliteView) {
        if (SwingUtilities.isEventDispatchThread ())
            showCoreEDT (view, satelliteView);
        else
            SwingUtilities.invokeLater (new Runnable() {
                public void run () {
                    showCoreEDT (view, satelliteView);
                }
            });
    }

    private static void showCoreEDT (JComponent view, JComponent satelliteView) {
        int width=800,height=600;
        JFrame frame = new JFrame ();//new JDialog (), true);
        frame.add (view, BorderLayout.CENTER);
        frame.add (satelliteView, BorderLayout.WEST);
        frame.setDefaultCloseOperation (JFrame.DISPOSE_ON_CLOSE);
        java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        frame.setBounds((screenSize.width-width)/2, (screenSize.height-height)/2, width, height);
        frame.setVisible (true);
    }

    public static int randInt (int max) {
        return (int) (Math.random () * max);
    }

    public static void invokeLater (final Runnable runnable, int delay) {
        RequestProcessor.getDefault ().post (new Runnable() {
            public void run () {
                SwingUtilities.invokeLater (runnable);
            }
        }, delay);
    }

    public static void sleep (int delay) {
        try {
            Thread.sleep (delay);
        } catch (InterruptedException e) {
            e.printStackTrace ();
        }
    }

}
