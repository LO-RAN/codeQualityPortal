/*
 * CtrlPanel.java
 *
 * @author  fxa
 *
 * Created on 24 octobre 2002, 11:38
 */
package com.compuware.caqs.presentation.applets.architecture.panels;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Vector;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;
import javax.swing.JToolBar;


import com.compuware.caqs.domain.architecture.serializeddata.ArchitectureModel;
import com.compuware.caqs.domain.architecture.serializeddata.ArchitectureModelListener;
import com.compuware.caqs.presentation.applets.architecture.ArchitectureModelCreators;
import com.compuware.caqs.presentation.applets.architecture.AutoLayout;
import com.compuware.caqs.presentation.applets.architecture.I18n;
import com.compuware.caqs.presentation.applets.architecture.Loader;
import com.compuware.caqs.presentation.applets.architecture.modellayout.Layouter;
import com.compuware.caqs.presentation.applets.architecture.modellayout.RelaxerGOOD;

import com.compuware.caqs.presentation.applets.architecture.panels.control.GraphicalPreferencesWindow;
import com.compuware.caqs.presentation.applets.architecture.panels.legend.LegendWindow;
import com.compuware.caqs.presentation.applets.architecture.panels.selection.SelectionWindow;

public class ControlPanel extends JToolBar implements ActionListener, ItemListener {

    GraphicalPreferencesWindow m_preferenceWindow;
    private boolean m_testing = false;
    public static final int CTRL_SCHEME_ALL_ON = 1;
    public static final int CTRL_SCHEME_ALL_OFF = 0;
    protected String webServerStr;
    protected String idElement = null;
    protected String idBaseline = null;
    protected LegendWindow legendWindow;
    protected SelectionWindow unassignedWindow = null;
    // menubar
    protected JMenuBar menubar = new JMenuBar();
    // menu Model
    protected JMenu menuModels = new JMenu(I18n.getString("Model"));
    protected JMenuItem doBasicLayout = new JMenuItem("Do Basic Layout");
    protected JMenuItem assignUncalled = new JMenuItem("Assign Uncalled to Uncalled Package");
    protected JMenuItem assignUncaller = new JMenuItem("Assign Uncaller to Uncaller Package");
    protected JMenuItem assignNeverUsed = new JMenuItem("Assign Never used to NeverUsed Package");
    protected JMenuItem circularLayout = new JMenuItem("circular Layout");
    protected JMenuItem oneClickLayout = new JMenuItem("oneClick Layout");
    protected JMenuItem oneClickLayout_noMatrix = new JMenuItem("oneClick Layout No Matrix");
    protected JMenuItem oneToOneClasses = new JMenuItem(I18n.getString("1/1_Classes"));
    protected JMenuItem oneToOneMethods = new JMenuItem(I18n.getString("1/1_Methods"));
    protected JCheckBoxMenuItem autoLayout = new JCheckBoxMenuItem(I18n.getString("Autolayout"));
    protected JMenuItem deleteAllBtn = new JMenuItem(I18n.getString("Delete_All"));
    protected JMenuItem restoreBtn = new JMenuItem(I18n.getString("restore"));
    protected JMenuItem saveBtn = new JMenuItem(I18n.getString("save"));
    protected JMenuItem saveOnDiskBtn = new JMenuItem(I18n.getString("saveondisk"));
    protected JMenuItem getFromDiskBtn = new JMenuItem(I18n.getString("getfromdisk"));
    protected JMenuItem modelInfoBtn = new JMenuItem(I18n.getString("Model_Informations"));
    // menu View
    protected JMenu menuView = new JMenu(I18n.getString("View"));
    protected JMenuItem refreshBtn = new JMenuItem(I18n.getString("refresh"));
    protected JMenuItem preferencesBtn = new JMenuItem(I18n.getString("preferences"));
    // view unAssignedWindow
    protected JButton unAssignedBtn = null;
    protected JButton legend = null;
    protected Layouter layouter;
    private AutoLayout lay;

    /**
     * Creates a new instance of CtrlPanel
     */
    public ControlPanel(String webServerStr, String idElement, String idBaseline, boolean testing) {

        ImageIcon imgNotAssign = Tools.createAppletImageIcon("notAssigned.gif", "");
        ImageIcon imgLegend = Tools.createAppletImageIcon("legend.gif", "");

        this.unAssignedBtn = new JButton(imgNotAssign);
        this.unAssignedBtn.setToolTipText(I18n.getString("unAssigned"));

        this.legend = new JButton(imgLegend);
        this.legend.setToolTipText(I18n.getString("legendToolTip"));

        this.oneToOneClasses.setToolTipText(I18n.getString("1/1_ClassesToolTip"));
        this.oneToOneMethods.setToolTipText(I18n.getString("1/1_MethodsToolTip"));

        this.m_testing = testing;
        this.idElement = idElement;
        this.idBaseline = idBaseline;
        this.webServerStr = webServerStr;
        this.createPanel();
    }

    public boolean isAutoLayout() {
        return this.autoLayout.isSelected();
    }

    public void setCtrlScheme(int ctrlScheme) {
        int componentsCount = this.getComponentCount();
        switch (ctrlScheme) {
            case ControlPanel.CTRL_SCHEME_ALL_OFF:
                for (int i = 0; i < componentsCount; i++) {
                    this.getComponent(i).setEnabled(false);
                }
                break;
            case ControlPanel.CTRL_SCHEME_ALL_ON:

                for (int i = 0; i < componentsCount; i++) {
                    this.getComponent(i).setEnabled(true);
                }

                if (!ArchitectureModel.getInstance().isModifiable()) {
                    this.deleteAllBtn.setVisible(false);
                    this.menuModels.setVisible(false);
                } else {
                    this.deleteAllBtn.setVisible(true);
                    this.menuModels.setVisible(true);
                    this.saveBtn.setVisible(true);
                }

                break;
        }
    }

    public JMenuBar getJMenuBar() {
        return this.menubar;
    }

    private void createMenues() {
        // this buttons are for test mode.
        this.assignUncalled.addActionListener(this);
        this.assignUncaller.addActionListener(this);
        this.assignNeverUsed.addActionListener(this);
        this.doBasicLayout.addActionListener(this);

        // this.m_oneToOnePackages.addActionListener(this);
        this.oneToOneClasses.addActionListener(this);
        this.oneToOneMethods.addActionListener(this);
        this.deleteAllBtn.addActionListener(this);

        this.menuModels.add(this.assignUncalled);
        this.menuModels.add(this.assignUncaller);
        this.menuModels.add(this.assignNeverUsed);
        this.menuModels.add(this.doBasicLayout);
        this.menuModels.addSeparator();

        // this.m_menuModels.add(this.m_oneToOnePackages);
        this.menuModels.add(this.oneToOneClasses);
        this.menuModels.add(this.oneToOneMethods);
        this.menuModels.addSeparator();

        this.menuModels.add(this.circularLayout);
        this.circularLayout.addActionListener(this);

        this.menuModels.add(this.oneClickLayout);
        this.oneClickLayout.addActionListener(this);

        this.menuModels.add(this.oneClickLayout_noMatrix);
        this.oneClickLayout_noMatrix.addActionListener(this);

        this.menuModels.add(this.autoLayout);
        this.autoLayout.setSelected(false);
        this.autoLayout.addItemListener(this);
        this.menuModels.addSeparator();
        this.menuModels.add(this.deleteAllBtn);
        this.menuModels.addSeparator();
        this.menuModels.add(this.restoreBtn);
        this.restoreBtn.addActionListener(this);
        this.menuModels.add(this.saveBtn);
        this.saveBtn.addActionListener(this);
        this.menuModels.addSeparator();
        this.menuModels.add(modelInfoBtn);
        this.modelInfoBtn.addActionListener(this);
        if (this.m_testing) {
            this.menuModels.addSeparator();
            this.menuModels.add(this.saveOnDiskBtn);
            this.saveOnDiskBtn.addActionListener(this);
            this.menuModels.add(getFromDiskBtn);
            this.getFromDiskBtn.addActionListener(this);

        }
        this.refreshBtn.addActionListener(this);
        this.menuView.add(refreshBtn);
        preferencesBtn.addActionListener(this);
        this.menuView.add(preferencesBtn);
        this.menubar.add(this.menuView);
    }

    private void createBouttonBar() {
        JToolBar basicToolBar = new JToolBar();

        basicToolBar.add(this.unAssignedBtn);
        basicToolBar.addSeparator();
        this.unAssignedBtn.addActionListener(this);

        basicToolBar.add(this.legend);
        this.legend.addActionListener(this);
        this.add(basicToolBar);

    }

    private void createPanel() {
        this.removeAll();

        this.createMenues();
        this.createBouttonBar();

        this.menubar.add(this.menuModels);

        this.setCtrlScheme(ControlPanel.CTRL_SCHEME_ALL_OFF);
    }

    public void itemStateChanged(ItemEvent e) {
        Object src = e.getSource();
        if (src == this.autoLayout) {
            if (this.autoLayout.isSelected()) {
                this.startAutoLayout();
            } else {
                this.stopAutoLayout();
            }

        }
    }

    public void actionPerformed(ActionEvent e) {
        Object src = e.getSource();

        if (src == this.deleteAllBtn) {
            // le premier paramètre est la fenêtre mère. Il sert à centrer la
            // boite de dialogue
            int result = JOptionPane.showConfirmDialog(null, I18n.getString("confirme_delete_all"));
            switch (result) {
                case JOptionPane.YES_OPTION:
                    ArchitectureModel.getInstance().deleteAll();
            }
        } else if (src == this.refreshBtn) {
            // this.m_drawer.draw();
        } else if (src == this.preferencesBtn) {

            if (this.m_preferenceWindow == null) {
                this.m_preferenceWindow = new GraphicalPreferencesWindow();
            } else {
                this.m_preferenceWindow.setVisible(true);
            }
        } else if (src == this.modelInfoBtn) {
            JOptionPane.showMessageDialog(this, new String(I18n.getString("Model_Informations") +
                    "\n" + ArchitectureModel.getInstance().toString()), I18n.getString("architecture"), JOptionPane.INFORMATION_MESSAGE);
        } else if (src == this.oneToOneClasses) {
            ArchitectureModelCreators.oneToOneClasses(ArchitectureModel.getInstance());
        } else if (src == this.oneToOneMethods) {
            ArchitectureModelCreators.oneToOneMethods(ArchitectureModel.getInstance());
        } else if (src == this.doBasicLayout) {
            ArchitectureModelCreators.matrixLayout(ArchitectureModel.getInstance());
        } else if (src == this.oneClickLayout) {
            startAutolayout(true);
        } else if (src == this.oneClickLayout_noMatrix) {
            startAutolayout(false);
        } else if (src == this.assignUncalled) {
            ArchitectureModelCreators.assignUncalledToUncalledPackage(ArchitectureModel.getInstance());
        } else if (src == this.assignUncaller) {
            ArchitectureModelCreators.assignUncallerToUncallerPackage(ArchitectureModel.getInstance());
        } else if (src == this.assignNeverUsed) {
            ArchitectureModelCreators.assignNeverUsedToNeverUsedPackage(ArchitectureModel.getInstance());
        } else if (src == this.circularLayout) {
            RelaxerGOOD.circleRelax(ArchitectureModel.getInstance());
            ArchitectureModel.getInstance().fireModelChangedEvent();
        } else if (src == this.legend) {
            if (this.legendWindow == null) {
                this.legendWindow = new LegendWindow();
            } else {
                this.legendWindow.setVisible(true);
            }

        } else if (src == this.restoreBtn) {
            int result = JOptionPane.showConfirmDialog(null, I18n.getString("restoreMessage"));
            switch (result) {
                case JOptionPane.YES_OPTION:
                    this.getFromServlet();
            }
        } else if (src == this.saveBtn) {
            int result = JOptionPane.showConfirmDialog(null, I18n.getString("saveMessage"));
            switch (result) {
                case JOptionPane.YES_OPTION:
                    this.saveToServlet();
            }
        } else if (src == saveOnDiskBtn) {
            int result = JOptionPane.showConfirmDialog(null, I18n.getString("saveOnDiskMessage"));
            switch (result) {
                case JOptionPane.YES_OPTION:
                    this.saveToDisk();
            }
        } else if (src == this.getFromDiskBtn) {
            this.getFromDisk();
        } else if (src == this.unAssignedBtn) {
            if (this.unassignedWindow != null) {
                this.unassignedWindow.dispose();
            }
            this.unassignedWindow = new SelectionWindow();
        }
    }

    private void startAutolayout(boolean matrixFirst) {
        // Start : creation of Progress Dialog
        JDialog dialog = new JDialog(JOptionPane.getFrameForComponent(this), true);
        JProgressBar progressB = new JProgressBar();
        progressB.setIndeterminate(true);
        dialog.getContentPane().setLayout(new BoxLayout(dialog.getContentPane(), BoxLayout.PAGE_AXIS));
        JLabel aText = new JLabel();
        JButton button = new JButton("STOP");
        lay = new AutoLayout(ArchitectureModel.getInstance(), aText, dialog);
        button.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                lay.stop();
            }
        });
        dialog.getContentPane().add(aText);
        dialog.getContentPane().add(progressB);
        dialog.getContentPane().add(button);

        dialog.setSize(400, 100);
        Dimension screenDim = Toolkit.getDefaultToolkit().getScreenSize();
        Rectangle winDim = dialog.getBounds();
        dialog.setLocation((screenDim.width - winDim.width) / 2, (screenDim.height -
                winDim.height) / 2);
        dialog.setTitle(I18n.getString("autoLayoutTitle"));
        aText.setText(I18n.getString("autoLayout"));
        // End : creation of Progress Dialog

        lay.start(matrixFirst);
        dialog.setVisible(true);

        dialog.dispose();
    }

    public void startAutoLayout() {
        if (this.layouter == null) {
            this.layouter = new Layouter(40);
            this.layouter.start();
        } else {
            this.layouter.start();
        }
    }

    public void stopAutoLayout() {
        this.layouter.stop();
        this.layouter = null;
    }

    public void getFromServlet() {
        // Start : creation of Progress Dialog
        JDialog dialog = new JDialog(JOptionPane.getFrameForComponent(this), true);
        JProgressBar progressB = new JProgressBar();
        progressB.setIndeterminate(true);
        dialog.getContentPane().setLayout(new BorderLayout());
        JLabel aText = new JLabel();
        dialog.getContentPane().add(aText, BorderLayout.CENTER);
        dialog.getContentPane().add(progressB, BorderLayout.SOUTH);
        dialog.setSize(400, 100);
        Dimension screenDim = Toolkit.getDefaultToolkit().getScreenSize();
        Rectangle winDim = dialog.getBounds();
        dialog.setLocation((screenDim.width - winDim.width) / 2, (screenDim.height -
                winDim.height) / 2);
        dialog.setTitle(I18n.getString("LoadingTitle"));
        dialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        aText.setText(I18n.getString("Loading"));
        // End : creation of Progress Dialog
        // this.m_model = new ArchitectureModel();
        this.setCtrlScheme(ControlPanel.CTRL_SCHEME_ALL_OFF);
        Vector<ArchitectureModelListener> listeners = ArchitectureModel.getInstance().getListeners();
        Loader l = new Loader(dialog, this.m_testing, this.webServerStr, this.idElement, this.idBaseline);
        l.start(Loader.LOADER_LOAD_ACTION);
        dialog.setVisible(true);
        this.setCtrlScheme(ControlPanel.CTRL_SCHEME_ALL_ON);
        ArchitectureModel.getInstance().resetListeners(listeners);
    }

    protected void saveToServlet() {
        // Start : creation of Progress Dialog
        JDialog dialog = new JDialog(JOptionPane.getFrameForComponent(this), true);
        JProgressBar progressB = new JProgressBar();
        progressB.setIndeterminate(true);
        dialog.getContentPane().setLayout(new BorderLayout());
        JLabel aText = new JLabel();
        dialog.getContentPane().add(aText, BorderLayout.CENTER);
        dialog.getContentPane().add(progressB, BorderLayout.SOUTH);
        dialog.setSize(400, 100);
        Dimension screenDim = Toolkit.getDefaultToolkit().getScreenSize();
        Rectangle winDim = dialog.getBounds();
        dialog.setLocation((screenDim.width - winDim.width) / 2, (screenDim.height -
                winDim.height) / 2);
        dialog.setTitle(I18n.getString("SavingTitle"));
        aText.setText(I18n.getString("Saving"));
        Vector<ArchitectureModelListener> listeners = ArchitectureModel.getInstance().getListeners();
        // End : creation of Progress Dialog
        Loader l = new Loader(dialog, this.m_testing, this.webServerStr, this.idElement, this.idBaseline);
        this.setCtrlScheme(ControlPanel.CTRL_SCHEME_ALL_OFF);
        l.start(Loader.LOADER_SAVE_ACTION);
        dialog.setVisible(true);
        this.setCtrlScheme(ControlPanel.CTRL_SCHEME_ALL_ON);
        ArchitectureModel.getInstance().resetListeners(listeners);

    }

    protected void saveToDisk() {
        try {
            String filename = File.separator + "arc";
            JFileChooser fc = new JFileChooser(new File(filename));

            // Show open dialog; this method does not return until the dialog is
            // closed
            fc.showSaveDialog(this);
            String fileAbsPath = fc.getSelectedFile().getAbsolutePath();

            FileOutputStream fout = new FileOutputStream(fileAbsPath);
            ObjectOutputStream oos = new ObjectOutputStream(fout);
            oos.writeObject(ArchitectureModel.getInstance());
            oos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    protected void getFromDisk() {
        try {
            String filename = File.separator + "arc";
            JFileChooser fc = new JFileChooser(new File(filename));

            // Show open dialog; this method does not return until the dialog is
            // closed
            fc.showOpenDialog(this);
            String fileAbsPath = fc.getSelectedFile().getAbsolutePath();

            FileInputStream fin = new FileInputStream(fileAbsPath);
            ObjectInputStream ois = new ObjectInputStream(fin);
            ArchitectureModel.setInstance((ArchitectureModel) ois.readObject());
            ArchitectureModel.getInstance().computeRealLinks();
            ois.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
