package com.compuware.caqs.presentation.applets.graphapplet;

import java.awt.Color;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import org.netbeans.api.visual.action.ActionFactory;
import org.netbeans.api.visual.action.PopupMenuProvider;
import org.netbeans.api.visual.action.SelectProvider;
import org.netbeans.api.visual.action.TwoStateHoverProvider;
import org.netbeans.api.visual.action.WidgetAction;
import org.netbeans.api.visual.anchor.AnchorFactory;
import org.netbeans.api.visual.graph.GraphScene;
import org.netbeans.api.visual.widget.ConnectionWidget;
import org.netbeans.api.visual.widget.LayerWidget;
import org.netbeans.api.visual.widget.Widget;

import org.netbeans.api.visual.anchor.AnchorShape;
import org.netbeans.api.visual.router.RouterFactory;

/**
 * @author cwfr-fdubois
 */
public abstract class UMLGraphScene extends GraphScene {

    private LayerWidget mainLayer;
    private LayerWidget connectionLayer;

    private WidgetAction moveAction = ActionFactory.createMoveAction();
    private WidgetAction mouseHoverAction = ActionFactory.createHoverAction(new NodeHoverProvider());
    private WidgetAction mouseHoverEdgeAction = ActionFactory.createHoverAction(new EdgeHoverProvider());
    private WidgetAction popupMenuAction = ActionFactory.createPopupMenuAction(new UMLGraphPopupMenuProvider());

    private WidgetAction highlightAction = ActionFactory.createSelectAction(new WidgetHighlightProvider());

    private static final Color MAIN_NODE_BG_COLOR = Color.YELLOW;
    private static final Color MAIN_NODE_HOVER_BG_COLOR = Color.LIGHT_GRAY;

    private JComponent satelliteView;
    private ResourceBundle messages;

    public UMLGraphScene(ResourceBundle messages) {
        mainLayer = new LayerWidget(this);
        connectionLayer = new LayerWidget(this);
        this.messages = messages;
        addChild(mainLayer);
        addChild(connectionLayer);
        getActions().addAction(highlightAction);
        getActions().addAction(moveAction);
        getActions().addAction(mouseHoverAction);
        getActions().addAction(mouseHoverEdgeAction);
        getActions().addAction(popupMenuAction);
    }

    protected Widget attachNodeWidget(Object node) {
        UMLClassWidget widget = new UMLClassWidget (this, ((NodeData)node).isMainNode());
        widget.setId (((NodeData)node).getId());
        widget.setClassName (((NodeData)node).getLib());
        widget.setToolTipText(((NodeData)node).getDesc());
        if (widget.isMainNode()) {
            widget.setBackground(MAIN_NODE_BG_COLOR);
        }
        mainLayer.addChild (widget);

        widget.getActions().addAction(mouseHoverAction);
        widget.getActions().addAction(popupMenuAction);
        widget.getActions().addAction(highlightAction);
        widget.getActions().addAction(moveAction);

        return widget;
    }

    protected Widget attachEdgeWidget(Object edge) {
        ConnectionWidget connectionWidget = new ConnectionWidget(this);
        connectionLayer.addChild(connectionWidget);
        connectionWidget.setTargetAnchorShape(AnchorShape.TRIANGLE_FILLED);
        connectionWidget.setRouter(RouterFactory.createOrthogonalSearchRouter(mainLayer));
        connectionWidget.getActions().addAction(mouseHoverEdgeAction);
        return connectionWidget;
    }

    protected void attachEdgeSourceAnchor(Object edge, Object oldSourceNode, Object sourceNode) {
        ((ConnectionWidget) findWidget(edge)).setSourceAnchor (AnchorFactory.createRectangularAnchor (findWidget (sourceNode)));
    }

    protected void attachEdgeTargetAnchor(Object edge, Object oldTargetNode, Object targetNode) {
        ((ConnectionWidget) findWidget (edge)).setTargetAnchor (AnchorFactory.createRectangularAnchor (findWidget (targetNode)));
    }

    public LayerWidget getMainLayer() {
        return mainLayer;
    }

    public LayerWidget getConnectionLayer() {
        return connectionLayer;
    }

    private Color getBackgroundColor(NodeData nodeData) {
        Color result = Color.WHITE;
        if (nodeData != null && nodeData.isMainNode()) {
            result = Color.LIGHT_GRAY;
        }
        return result;
    }

    private Color getUnsetHoveringColor(UMLClassWidget widget) {
        Color result = Color.WHITE;
        if (widget.isMainNode()) {
            result = MAIN_NODE_BG_COLOR;
        }
        return result;
    }

    public synchronized JComponent getSatelliteView() {
        if (this.satelliteView == null) {
            this.satelliteView = createSatelliteView();
        }
        return this.satelliteView;
    }

    private class NodeHoverProvider implements TwoStateHoverProvider {

        private Color getSetHoveringColor(UMLClassWidget widget) {
            Color result = Color.LIGHT_GRAY;
            if (widget.isMainNode()) {
                result = MAIN_NODE_HOVER_BG_COLOR;
            }
            return result;
        }

        public void unsetHovering(Widget widget) {
            if (!highlightedNodes.containsKey(widget)) {
                widget.setBackground(getUnsetHoveringColor((UMLClassWidget)widget));
            }
        }

        public void setHovering(Widget widget) {
            if (!highlightedNodes.containsKey(widget)) {
                widget.setBackground(getSetHoveringColor((UMLClassWidget)widget));
            }
        }

    }

    private static final Color EDGE_HOVER_COLOR = new Color(119, 176, 255);

    private class EdgeHoverProvider implements TwoStateHoverProvider {

        public void unsetHovering(Widget widget) {
            if (!highlightedEdges.containsKey(widget)) {
                ConnectionWidget edge = (ConnectionWidget)widget;
                Object edgeObj = findObject(edge);
                Object sourceNode = getEdgeSource(edgeObj);
                Object targetNode = getEdgeTarget(edgeObj);
                Widget sourceNodeWidget = findWidget(sourceNode);
                Widget targetNodeWidget = findWidget(targetNode);
                if (!highlightedNodes.containsKey(sourceNodeWidget) && !highlightedNodes.containsKey(targetNodeWidget)) {
                    edge.setLineColor(Color.BLACK);
                    getScene().getSceneAnimator().animateBackgroundColor(sourceNodeWidget, getUnsetHoveringColor((UMLClassWidget)sourceNodeWidget));
                    getScene().getSceneAnimator().animateBackgroundColor(targetNodeWidget, getUnsetHoveringColor((UMLClassWidget)targetNodeWidget));
                }
            }
        }

        public void setHovering(Widget widget) {
            if (!highlightedEdges.containsKey(widget)) {
                ConnectionWidget edge = (ConnectionWidget)widget;
                Object edgeObj = findObject(edge);
                Object sourceNode = getEdgeSource(edgeObj);
                Object targetNode = getEdgeTarget(edgeObj);
                Widget sourceNodeWidget = findWidget(sourceNode);
                Widget targetNodeWidget = findWidget(targetNode);
                if (!highlightedNodes.containsKey(sourceNodeWidget) && !highlightedNodes.containsKey(targetNodeWidget)) {
                    edge.setLineColor(EDGE_HOVER_COLOR);
                    getScene().getSceneAnimator().animateBackgroundColor(sourceNodeWidget, EDGE_HOVER_COLOR);
                    getScene().getSceneAnimator().animateBackgroundColor(targetNodeWidget, EDGE_HOVER_COLOR);
                }
            }
        }

    }

    private static final String EXPORT_ACTION = "caqs.graphapplet.export";
    private static final String SATELLITE_ACTION = "caqs.graphapplet.satellite";

    private static final String OPEN_NODE_MSG_KEY = "caqs.graphapplet.opennode";

    private class UMLGraphPopupMenuProvider implements PopupMenuProvider {
        public JPopupMenu getPopupMenu(Widget widget, Point localLocation) {
            JPopupMenu popupMenu = new JPopupMenu();
            if (widget instanceof UMLClassWidget) {
                popupMenu.add(new JMenuItem(new UpdateDataAction((UMLClassWidget)widget)));
            }
            else {
                popupMenu.add(new JMenuItem(new SceneContextAction(EXPORT_ACTION, (UMLGraphScene)widget)));
                popupMenu.add(new JMenuItem(new SceneContextAction(SATELLITE_ACTION, (UMLGraphScene)widget)));
            }
            return popupMenu;
        }
    }

    class UpdateDataAction extends AbstractAction {

        String idElt;

        UpdateDataAction(UMLClassWidget widget) {
            super(messages.getString(OPEN_NODE_MSG_KEY) + ' ' + widget.getClassName());
            this.idElt = widget.getId();
        }

        public void actionPerformed(ActionEvent actionEvent) {
            updateData(this.idElt, 1, 1);
        }
    }

    class SceneContextAction extends AbstractAction {

        private UMLGraphScene scene;
        private String actionKey;

        SceneContextAction(String actionKey, UMLGraphScene widget) {
            super(messages.getString(actionKey));
            scene = widget;
            this.actionKey = actionKey;
        }

        public void actionPerformed(ActionEvent actionEvent) {
            if (EXPORT_ACTION.equals(this.actionKey)) {
                SceneExport export = new SceneExport();
                export.exportScene(scene);
            }
            else if (SATELLITE_ACTION.equals(this.actionKey)) {
                JComponent viewer = scene.getSatelliteView();
                if (viewer.isVisible()) {
                    viewer.setVisible(false);
                } else {
                    viewer.setVisible(true);
                }
            }
        }
    }

    private static final Color HIGHLIGHT_OUT_COLOR = Color.MAGENTA;
    private static final Color HIGHLIGHT_IN_COLOR = Color.ORANGE;

    private class WidgetHighlightProvider implements SelectProvider {

        public boolean isAimingAllowed (Widget widget, Point localLocation, boolean invertSelection) {
            return false; // HINT - this has to be true for correct cooperation with another locking action (now rectangular select action)
        }

        public boolean isSelectionAllowed (Widget widget, Point localLocation, boolean invertSelection) {
            return true;
        }

        public void select (Widget widget, Point localLocation, boolean invertSelection) {
            if (widget instanceof UMLClassWidget) {
                if (!widget.equals(selectedNode)) {
                    clearHighlightedNodes();
                }
                selectedNode = widget;
                getScene().getSceneAnimator().animateBackgroundColor(widget, EDGE_HOVER_COLOR);
                highlightOut(widget);
                highlightedNodes.remove(widget);
                highlightIn(widget);
                getScene().getSceneAnimator().animateBackgroundColor(widget, EDGE_HOVER_COLOR);
            }
            else {
                clearHighlightedNodes();
            }
        }

        private void highlightOut(Widget widget) {
            if (!highlightedNodes.containsKey(widget) && widget instanceof UMLClassWidget) {
                Object node = findObject(widget);
                highlightedNodes.put(widget, widget);
                Collection outputEdges = findNodeEdges(node, true, false);
                for(Object edge: outputEdges) {
                    ConnectionWidget edgeWidget = (ConnectionWidget)findWidget(edge);
                    edgeWidget.setLineColor(HIGHLIGHT_OUT_COLOR);
                    highlightedEdges.put(edgeWidget, edgeWidget);
                    Object targetNode = getEdgeTarget(edge);
                    Widget targetNodeWidget = findWidget(targetNode);
                    getScene().getSceneAnimator().animateBackgroundColor(targetNodeWidget, HIGHLIGHT_OUT_COLOR);
                    highlightOut(targetNodeWidget);
                }
            }
        }

        private void highlightIn(Widget widget) {
            if (!highlightedNodes.containsKey(widget) && widget instanceof UMLClassWidget) {
                Object node = findObject(widget);
                highlightedNodes.put(widget, widget);
                Collection inputEdges = findNodeEdges(node, false, true);
                for(Object edge: inputEdges) {
                    ConnectionWidget edgeWidget = (ConnectionWidget)findWidget(edge);
                    edgeWidget.setLineColor(HIGHLIGHT_IN_COLOR);
                    highlightedEdges.put(edgeWidget, edgeWidget);
                    Object sourceNode = getEdgeSource(edge);
                    Widget sourceNodeWidget = findWidget(sourceNode);
                    getScene().getSceneAnimator().animateBackgroundColor(sourceNodeWidget, HIGHLIGHT_IN_COLOR);
                    highlightIn(sourceNodeWidget);
                }
            }
        }

        private void clearHighlightedNodes() {
            if (highlightedEdges != null && highlightedEdges.size() > 0) {
                for (Widget widget: highlightedEdges.values()) {
                    ((ConnectionWidget)widget).setLineColor(Color.BLACK);
                }
                highlightedEdges.clear();
            }
            if (highlightedNodes != null && highlightedNodes.size() > 0) {
                for (Widget widget: highlightedNodes.values()) {
                    getScene().getSceneAnimator().animateBackgroundColor(widget, getUnsetHoveringColor((UMLClassWidget)widget));
                }
                highlightedNodes.clear();
            }
            if (selectedNode != null) {
                getScene().getSceneAnimator().animateBackgroundColor(selectedNode, getUnsetHoveringColor((UMLClassWidget)selectedNode));
                selectedNode = null;
            }
        }

    }

    Widget selectedNode = null;
    Map<Widget, Widget> highlightedNodes = new HashMap<Widget, Widget>();
    Map<Widget, Widget> highlightedEdges = new HashMap<Widget, Widget>();


    protected abstract void updateData(String idElt, int nbIn, int nbOut);

}
