package com.compuware.caqs.presentation.applets.graphapplet;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;
import org.netbeans.api.visual.graph.GraphScene;
import org.openide.ErrorManager;

/**
 *
 * @author cwfr-fdubois
 */
public class SceneExport {

    public void exportScene(GraphScene scene) {
        JComponent view;
        Dimension dim;
        BufferedImage bufImage;
        Graphics2D graphics;
        File file;

        view = scene.getView();
        dim = view.getSize();
        bufImage = new BufferedImage (dim.width, dim.height,
                BufferedImage.TYPE_4BYTE_ABGR);
        graphics = bufImage.createGraphics ();
        scene.paint (graphics);
        graphics.dispose ();
        file = getPNGSaveFile(view);
        if (file != null) {
            diagramToPNG(file, bufImage);
        }
    }

    private File getPNGSaveFile (JComponent view) {
        JFileChooser chooser;
        File file;
        chooser = new JFileChooser();
        chooser.setDialogTitle("Export Scene As ...");
        chooser.setDialogType(JFileChooser.SAVE_DIALOG);
        chooser.setMultiSelectionEnabled(false);
        chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        chooser.setFileFilter(new FileFilter() {
            public boolean accept(File file) {
                if (file.isDirectory())
                    return true;
                return file.getName().toLowerCase().endsWith(".png");
            }
            public String getDescription () {
                return "Portable Network Graphics (.png)";
            }
        });
        if (chooser.showSaveDialog(view) != JFileChooser.APPROVE_OPTION) {
            return null;
        }

        file = chooser.getSelectedFile ();
        if (! file.getName ().toLowerCase ().endsWith (".png")) {
            file = new File (file.getParentFile (), file.getName () + ".png");
        }
        if (file.exists ()) {
            int choice = JOptionPane.showConfirmDialog(
                    null,
                    "File (" + file.getAbsolutePath () + ") already exists. Do you want to overwrite it?",
                    "File Exists",
                    JOptionPane.YES_NO_OPTION
            );
            if (choice == JOptionPane.NO_OPTION) {
                file = null;
            }
        }
        return file;
    }

    private void diagramToPNG(File file, BufferedImage bufImage) {

        try {
            ImageIO.write (bufImage, "png", file); // NOI18N
        } catch (IOException e) {
            ErrorManager.getDefault().notify(e);
        }
    }

}
