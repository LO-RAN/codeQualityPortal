/*
 */
package com.compuware.caqs.presentation.applets.architecture;

import java.awt.BorderLayout;
import java.util.Vector;

import javax.swing.JApplet;
import javax.swing.JPanel;
import javax.swing.JSplitPane;

import com.compuware.caqs.domain.architecture.serializeddata.ArchitectureModel;
import com.compuware.caqs.domain.architecture.serializeddata.ArchitectureModelListener;
import com.compuware.caqs.domain.architecture.serializeddata.DrawnObject;
import com.compuware.caqs.domain.architecture.serializeddata.Edge;
import com.compuware.caqs.domain.architecture.serializeddata.Node;
import com.compuware.caqs.domain.architecture.serializeddata.NodeArchitectureModule;
import com.compuware.caqs.domain.architecture.serializeddata.NodeDB;
import com.compuware.caqs.domain.architecture.serializeddata.NodeUseCase;
import com.compuware.caqs.presentation.applets.architecture.panels.ControlPanel;
import com.compuware.caqs.presentation.applets.architecture.panels.MainPanelScrolls;
import com.compuware.caqs.presentation.applets.architecture.panels.ToolBarControlMode;
import com.compuware.caqs.presentation.applets.architecture.panels.ToolBarDrawingHelp;
import com.compuware.caqs.presentation.applets.architecture.panels.ToolBarView;
import com.compuware.caqs.presentation.applets.architecture.panels.drawers.DrawerAbstract;
import com.compuware.caqs.presentation.applets.architecture.panels.drawers.DrawerLazy;
import com.compuware.caqs.presentation.applets.architecture.panels.drawnobjectpanels.DrawnObjectPanel;
import com.compuware.caqs.presentation.applets.architecture.panels.drawnobjectpanels.DrawnObjectPanelEdge;
import com.compuware.caqs.presentation.applets.architecture.panels.drawnobjectpanels.DrawnObjectPanelMultipleSelection;
import com.compuware.caqs.presentation.applets.architecture.panels.drawnobjectpanels.DrawnObjectPanelNode;
import com.compuware.caqs.presentation.applets.architecture.panels.drawnobjectpanels.DrawnObjectPanelNodeUseCase;
import com.compuware.caqs.presentation.applets.architecture.panels.graphpanels.GraphPanel;
import com.compuware.caqs.presentation.applets.architecture.panels.graphpanels.GraphPanelBird;
import com.compuware.caqs.presentation.applets.architecture.panels.graphpanels.GraphPanelMagnifier;
import com.compuware.caqs.presentation.applets.architecture.panels.graphpanels.GraphPanelMainViewAdvanced;

/**
 * to test this applet you must set the following parameters testing = true
 * useConnectionForTest = true|false testingServletUrl = [http address of the
 * servlet] works only if useConnectionForTest = true language = fr | en
 */
public class AppletArchitecture extends JApplet implements ArchitectureModelListener {

    private static final long serialVersionUID = 1L;
    private DrawerAbstract drawer;
    private ControlPanel controlPanel;
    // Main panel
    private MainPanelScrolls mainPanel;
    private GraphPanelMainViewAdvanced mainGraphPanel;
    private GraphPanelBird birdPanel;
    private GraphPanel maginifierPanel;
    private JPanel southPanel;
    // Split panels
    private JSplitPane mainvsbirdZoompanelsplitPanel;
    private JSplitPane verticalSplitPanel;
    private JSplitPane birdZoomSplitPanel;
    // connection informations
    private String webServerStr;
    private String idBaseline;
    private String idElement;
    protected boolean testingFlag = false;
    protected boolean useConnectionForTest = false;

    @Override
    public void init() {
        // project parameters
        this.idElement = getParameter("idElement");
        this.idBaseline = getParameter("idBaseline");

        String language = getParameter("language");
        I18n.init(language);

        // parameter for testing will automatically create a fake
        // ArchitectureModel.
        String testing = getParameter("testing");

        if (testing != null) {
            if ("true".equals(testing)) {
                this.testingFlag = true;
                String useConnection = getParameter("useConnectionForTest");
                if ("true".equals(useConnection)) {
                    this.useConnectionForTest = true;
                }
            }
        }

        this.initLayoutAndComponents();
    }

    /**
     * initial layout of the components
     */
    private void initLayoutAndComponents() {
        /*
         * +-----------------------------+
         * |          |                  |
         * |          |                 v|
         * | Bird     | Main            b|
         * +----------|                 a|
         * |Magnifier |       hbar      r|
         * |----------+------------------+
         * |South                        |
         * +-----------------------------+
         */

        this.getContentPane().setLayout(new BorderLayout());
        this.setWebServerUrl();

        // initialize the controlPanel

        this.controlPanel = new ControlPanel(this.webServerStr, this.idElement, this.idBaseline, this.testingFlag);
        this.getContentPane().add(this.controlPanel, BorderLayout.NORTH);
        this.setJMenuBar(this.controlPanel.getJMenuBar());

        this.mainvsbirdZoompanelsplitPanel = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        this.mainvsbirdZoompanelsplitPanel.setContinuousLayout(true);
        this.mainvsbirdZoompanelsplitPanel.setOneTouchExpandable(true);

        // set the drawer either lazy -redraw when asked- or Cyclic -redraw
        // every x ms-
        this.drawer = new DrawerLazy();

        this.mainPanel = new MainPanelScrolls();

        // Create the 3 graph panels
        this.birdPanel = new GraphPanelBird("bird", true);
        this.maginifierPanel = new GraphPanelMagnifier("magnifier", false);
        this.mainGraphPanel = new GraphPanelMainViewAdvanced("main", false);
        // end create the 3 graph panels

        // link the panels so they can control each other
        this.mainGraphPanel.setControledPanel(birdPanel);
        this.mainGraphPanel.setMaginifier(maginifierPanel);
        this.birdPanel.setControledPanel(mainGraphPanel);
        this.maginifierPanel.setControledPanel(null);
        // end link the panels

        this.mainPanel.setMainGraphPanel(mainGraphPanel);
        this.mainPanel.setBirdView(birdPanel);

        this.mainGraphPanel.addViewListener(this.mainPanel);
        this.mainGraphPanel.addViewListener(this.drawer);

        this.birdPanel.addViewListener(this.drawer);
        this.maginifierPanel.addViewListener(this.drawer);

        // tool bars
        ToolBarView viewToolBar = new ToolBarView(this.mainGraphPanel);
        this.controlPanel.add(viewToolBar);
        viewToolBar.addControlGraphicsListener(this.drawer);

        ToolBarControlMode controlModeToolBar = new ToolBarControlMode(this.testingFlag);
        controlModeToolBar.addControlModeListener(this.mainGraphPanel);
        this.controlPanel.add(controlModeToolBar);

        ToolBarDrawingHelp drawingHelpToolBar = new ToolBarDrawingHelp(this.mainGraphPanel);
        this.controlPanel.add(drawingHelpToolBar);
        // Create the content pane
        this.birdZoomSplitPanel = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        this.birdZoomSplitPanel.setOneTouchExpandable(true);
        this.birdZoomSplitPanel.setContinuousLayout(true);
        this.birdZoomSplitPanel.add(this.birdPanel);
        this.birdZoomSplitPanel.add(this.maginifierPanel);

        this.mainvsbirdZoompanelsplitPanel.add(this.birdZoomSplitPanel);
        this.mainvsbirdZoompanelsplitPanel.add(this.mainPanel);
        this.mainvsbirdZoompanelsplitPanel.setDividerLocation(.2);

        verticalSplitPanel = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        verticalSplitPanel.add(this.mainvsbirdZoompanelsplitPanel);

        this.getContentPane().add(verticalSplitPanel, BorderLayout.CENTER);
        // end create the content pane

        // initialize the drawer.

        this.drawer.addGraphPanel(this.mainGraphPanel);
        this.drawer.addGraphPanel(this.birdPanel);
        this.drawer.addGraphPanel(this.maginifierPanel);
    }

    /**
     * creates the url request to access the servlet
     */
    private void setWebServerUrl() {
        if (!this.testingFlag) {
            this.webServerStr = this.getCodeBase() + "../architecture";
        } else {
            this.webServerStr = "TestingWithOutConnection";
            if (this.testingFlag && this.useConnectionForTest) {
                this.webServerStr = getParameter("testingServletUrl");
                this.testingFlag = false;
            }
        }
    }

    public void addSouthPanel(JPanel p) {
        if (null == this.southPanel) {
            verticalSplitPanel.setOneTouchExpandable(true);
            verticalSplitPanel.setDividerLocation((double) .8);
        } else if (this.southPanel != null) {
            this.verticalSplitPanel.remove(this.southPanel);
        }
        int dividerLocation = verticalSplitPanel.getDividerLocation();
        this.southPanel = p;
        this.verticalSplitPanel.add(this.southPanel);
        verticalSplitPanel.setDividerLocation(dividerLocation);

        this.validate();
        this.repaint();
    }

    @Override
    public void destroy() {
        removeAll();
    }

    @Override
    public void start() {
        System.out.println("v3.4");
        this.controlPanel.getFromServlet();
        ArchitectureModel.getInstance().addListener(this.drawer);
        ArchitectureModel.getInstance().addListener(this);
        this.mainvsbirdZoompanelsplitPanel.setDividerLocation(.2);
        this.birdZoomSplitPanel.setDividerLocation(.5);
        this.drawer.start();
    }

    @Override
    public void stop() {
    }

    @Override
    public String getAppletInfo() {
        return "Title: Architecture Viewer\nAuthor: Compuware France";
    }

    @Override
    public String[][] getParameterInfo() {
        String[][] info = {};
        return info;
    }

    public void architectureModelChanged() {
    }

    public void newSelectedElement(DrawnObject object) {
        DrawnObjectPanel drawnObjectEditor = null;
        if (object instanceof NodeArchitectureModule) {
            drawnObjectEditor = new DrawnObjectPanelNode((Node) object);
        } else if (object instanceof NodeUseCase) {
            drawnObjectEditor = new DrawnObjectPanelNodeUseCase((Node) object);
        } else if (object instanceof NodeDB) {
            drawnObjectEditor = new DrawnObjectPanelNode((Node) object);
        } else if (object instanceof Edge) {
            drawnObjectEditor = new DrawnObjectPanelEdge((Edge) object);
        }
        this.addSouthPanel(drawnObjectEditor);
    }

    public void multipleSelectionDone(Vector<Node> multipleSelectedNodes) {
        DrawnObjectPanel drawnObjectEditor = new DrawnObjectPanelMultipleSelection(multipleSelectedNodes);
        this.addSouthPanel(drawnObjectEditor);
    }

    public void selectedItemDeleted() {
        addSouthPanel(new JPanel());
    }
}
