package com.compuware.caqs.presentation.applets.architecture.panels.graphpanels;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.AffineTransform;
import java.util.Enumeration;
import java.util.EventObject;
import java.util.Vector;

import com.compuware.caqs.domain.architecture.serializeddata.ArchitectureModel;
import com.compuware.caqs.domain.architecture.serializeddata.DrawnObject;
import com.compuware.caqs.domain.architecture.serializeddata.Edge;
import com.compuware.caqs.domain.architecture.serializeddata.Node;
import com.compuware.caqs.domain.architecture.serializeddata.NodeDB;
import com.compuware.caqs.domain.architecture.serializeddata.NodeList;
import com.compuware.caqs.domain.architecture.serializeddata.NodeUseCase;
import com.compuware.caqs.presentation.applets.architecture.I18n;
import com.compuware.caqs.presentation.applets.architecture.panels.control.ControlGraphics;
import com.compuware.caqs.presentation.applets.architecture.panels.control.ControlModes;

public class GraphPanelMainViewAdvanced extends GraphPanel {

	protected MainViewMouseMotionListener m_motionListener;
	protected DrawnObject m_picked;
	protected DrawnObject m_previousPicked;
	Node m_groupToDrawIn;

	// DrawnObjectPanel m_drawnObjectEditor; //TODO remove

	public GraphPanelMainViewAdvanced(String title, boolean permanentFit) {
		super(title, permanentFit);
	}

	public void requestTheFocus() {
		this.requestFocus();
	}

	protected void addSpecificMouseListeners() {
		addMouseListener(new MainViewMouseListener());
		addMouseMotionListener(new MainViewMouseMotionListener());
		this.addKeyListener(new MainViewKeyListener());
	}

	public void zoomIn() {
		this.m_zoomScaleChanged = true;
		// center on screen
		double width = 0;
		double height = 0;
		{
			Point topLeft = new Point(0, 0);
			this.mapPointToScale(topLeft);
			Point bottomRight = new Point(this.getBounds().width, this.getBounds().height);
			this.mapPointToScale(bottomRight);
			Rectangle visibleRect = new Rectangle(topLeft.x, topLeft.y, bottomRight.x - topLeft.x, bottomRight.y - topLeft.y);
			width = visibleRect.getWidth();
			height = visibleRect.getHeight();
		}
		this.m_offgraphics.scale(1.1, 1.1);
		double widthNew = 0;
		double heightNew = 0;
		{
			Point topLeft = new Point(0, 0);
			this.mapPointToScale(topLeft);
			Point bottomRight = new Point(this.getBounds().width, this.getBounds().height);
			this.mapPointToScale(bottomRight);
			Rectangle visibleRect = new Rectangle(topLeft.x, topLeft.y, bottomRight.x - topLeft.x, bottomRight.y - topLeft.y);
			widthNew = visibleRect.getWidth();
			heightNew = visibleRect.getHeight();
		}
		// end center on screen
		double translateX = (width - widthNew) / 2;
		double translateY = (height - heightNew) / 2;
		this.translateView(-translateX, -translateY);

		this.fireViewChanged();
	}

	public void zoom1() {
		this.m_zoomScaleChanged = true;
		this.m_offgraphics.setTransform(new AffineTransform());
		// default zoom factor
		this.m_offgraphics.scale(0.9, 0.9);
		this.m_offgraphics.scale(0.9, 0.9);
		this.m_offgraphics.scale(0.9, 0.9);
		this.fireViewChanged();
	}

	public void zoomOut() {
		this.m_zoomScaleChanged = true;
		// center on screen
		double width = 0;
		double height = 0;
		{
			Point topLeft = new Point(0, 0);
			this.mapPointToScale(topLeft);
			Point bottomRight = new Point(this.getBounds().width, this.getBounds().height);
			this.mapPointToScale(bottomRight);
			Rectangle visibleRect = new Rectangle(topLeft.x, topLeft.y, bottomRight.x - topLeft.x, bottomRight.y - topLeft.y);
			width = visibleRect.getWidth();
			height = visibleRect.getHeight();
		}

		this.m_offgraphics.scale(0.9, 0.9);
		double widthNew = 0;
		double heightNew = 0;
		{
			Point topLeft = new Point(0, 0);
			this.mapPointToScale(topLeft);
			Point bottomRight = new Point(this.getBounds().width, this.getBounds().height);
			this.mapPointToScale(bottomRight);
			Rectangle visibleRect = new Rectangle(topLeft.x, topLeft.y, bottomRight.x - topLeft.x, bottomRight.y - topLeft.y);
			widthNew = visibleRect.getWidth();
			heightNew = visibleRect.getHeight();
		}
		// end center on screen
		double translateX = (width - widthNew) / 2;
		double translateY = (height - heightNew) / 2;
		this.translateView(-translateX, -translateY);

		this.fireViewChanged();
	}

	class MainViewKeyListener implements KeyListener {
		public void keyPressed(KeyEvent evt) {
			AffineTransform at = m_offgraphics.getTransform();
			double scalex = at.getScaleX();
			double scaley = at.getScaleY();

			int code = evt.getKeyCode();
			switch (code) {
			case 16:// shift key pressed
				m_actionMode = ControlModes.ACTION_MODE_MULTIPLE_PICK_SELECTION;
				break;
			case 37:
				translateView(10 / scalex, 0);
				break;
			case 38:
				translateView(0, 10 / scaley);
				break;
			case 39:
				translateView(-10 / scalex, 0);
				break;
			case 40:
				translateView(0, -10 / scaley);
				break;
			}
			fireViewChanged();
		}

		public void keyTyped(KeyEvent evt) {
		}

		public void keyReleased(KeyEvent evt) {
			int code = evt.getKeyCode();
			switch (code) {
			case 16:// shift key pressed
				m_actionMode = ControlModes.ACTION_MODE_SELECT;
				ArchitectureModel.getInstance().fireEndMultipleSelection(m_multipleSelectedNodes);
				break;
			default:
			}

		}

	}

	class MainViewMouseMotionListener implements MouseMotionListener {

		private void mouseDraggedACTION_MODE_SELECT(MouseEvent ee) {
			// Start of transform of mouse coordinates for zoom purpose
			m_mousePosition = mapPointToScale(ee.getPoint());
			// end of transform of mouse coordinates for zoom purpose
			// m_controledPanel.controlToPt(m_mousePosition);

			int xMouse = m_mousePosition.x;
			int yMouse = m_mousePosition.y;

			if (m_resizedDrawObject != null) {
				m_draggedBoxPoint = new Point(xMouse, yMouse);
			}
			if ((m_picked != null) && (m_picked instanceof Node)) {
				Node pickedNode = (Node) m_picked;
				pickedNode.setX(xMouse);
				pickedNode.setY(yMouse);
			}
		}

		private void mouseDraggedACTION_MODE_DRAW_LINK(MouseEvent ee) {
			// Start of transform of mouse coordinates for zoom purpose
			m_mousePosition = mapPointToScale(ee.getPoint());
			// end of transform of mouse coordinates for zoom purpose
			// m_controledPanel.controlToPt(m_mousePosition);

			int xMouse = m_mousePosition.x;
			int yMouse = m_mousePosition.y;

			if ((m_picked != null)) {
				ArchitectureModel.getInstance().selectedDrawnObject(m_picked, false, DrawnObject.SELECTION_TYPE_BASIC, 0, m_multipleSelectedNodes);
				m_picked = null;
			}

			int x = xMouse;
			int y = yMouse;
			m_mousePosition = new Point(x, y);
			ArchitectureModel model = ArchitectureModel.getInstance();
			Rectangle rec = getVisibleRectangle();
			Point topLeft = new Point((int) rec.getMinX(), (int) rec.getMinY());
			Point bottomRight = new Point((int) rec.getMaxX(), (int) rec.getMaxY());
			
			boolean showRealLinks = false;
			boolean showArchiLinks = false;
			if ( m_controlgraphicsmode == ControlGraphics.DISPLAY_REALLINKS_AND_ARCHILINKS) {
				 showRealLinks = true;
				 showArchiLinks = true;
			} else if ( m_controlgraphicsmode == ControlGraphics.DISPLAY_REALLINKS_ONLY) {
				 showRealLinks = true;
				 showArchiLinks = false;
			} else if ( m_controlgraphicsmode == ControlGraphics.DISPLAY_ARCHILINKS_ONLY) {
				 showRealLinks = false;
				 showArchiLinks = true;
			}
		

			Vector<DrawnObject> nodes = model.sortDrawableObjectsBeforeDrawings(topLeft, bottomRight, false, showRealLinks, showArchiLinks);
			// Node[] nodes = model.getNodeList().getNodes();
			if (m_endNode != null) {
				m_endNode.setIsStartOrEnd(false);
				m_endNode = null;
			}
			m_endNode = (Node) ArchitectureModel.isClickOnADrawnObject(nodes, x, y);
			if (m_endNode != null) {
				m_endNode.setIsStartOrEnd(true);
			}		
		}

		private void mouseDraggedACTION_MODE_MOVE(MouseEvent ee) {
			// Start of transform of mouse coordinates for zoom purpose
			m_mousePosition = mapPointToScale(ee.getPoint());
			// end of transform of mouse coordinates for zoom purpose
			// m_controledPanel.controlToPt(m_mousePosition);

			int xMouse = m_mousePosition.x;
			int yMouse = m_mousePosition.y;

			for (int i = 0; i < m_multipleSelectedNodes.size(); i++) {
				Node currNode = (Node) m_multipleSelectedNodes.elementAt(i);
				currNode.setX(currNode.getCenterX() + xMouse - m_referencePoint.x);
				currNode.setY(currNode.getCenterY() + yMouse - m_referencePoint.y);
			}
			m_referencePoint.x = xMouse;
			m_referencePoint.y = yMouse;
		}

		private void mouseDraggedACTION_MODE_DRAW_BOX(MouseEvent ee) {
			// Start of transform of mouse coordinates for zoom purpose
			m_mousePosition = mapPointToScale(ee.getPoint());
			// end of transform of mouse coordinates for zoom purpose
			// m_controledPanel.controlToPt(m_mousePosition);

			int xMouse = m_mousePosition.x;
			int yMouse = m_mousePosition.y;
			if ((m_picked != null)) {
				ArchitectureModel.getInstance().selectedDrawnObject(m_picked, false, DrawnObject.SELECTION_TYPE_BASIC, 0, m_multipleSelectedNodes);
				m_picked = null;
			}

			// if (m_controlPanel.getActionMode() ==
			// CtrlPanel.ACTION_MODE_DRAW_BOX
			// || m_controlPanel.getActionMode() ==
			// CtrlPanel.ACTION_MODE_DRAW_BOX_USECASE) {
			m_secondBoxPoint = new Point(xMouse, yMouse);
			// }
		}

		public void mouseDragged(MouseEvent ee) {
			// Start of transform of mouse coordinates for zoom purpose
			m_mousePosition = mapPointToScale(ee.getPoint());
			// end of transform of mouse coordinates for zoom purpose

			// m_controledPanel.controlToPt(m_mousePosition);

			int xMouse = m_mousePosition.x;
			int yMouse = m_mousePosition.y;

			switch (m_actionMode) {
			case ControlModes.ACTION_MODE_SELECT:
				mouseDraggedACTION_MODE_SELECT(ee);

				break;
			case ControlModes.ACTION_MODE_MULTIPLE_SELECTION:
				m_draggedBoxPoint = new Point(xMouse, yMouse);
				break;
			case ControlModes.ACTION_MODE_MOVE:
				mouseDraggedACTION_MODE_MOVE(ee);
				break;
			case ControlModes.ACTION_MODE_DRAW_BOX:
				mouseDraggedACTION_MODE_DRAW_BOX(ee);
				break;
			case ControlModes.ACTION_MODE_DRAW_BOX_USECASE:
				mouseDraggedACTION_MODE_DRAW_BOX(ee);
				break;
			case ControlModes.ACTION_MODE_DRAW_BOX_DB:
				mouseDraggedACTION_MODE_DRAW_BOX(ee);
				break;
			case ControlModes.ACTION_MODE_DRAW_LINK:
				mouseDraggedACTION_MODE_DRAW_LINK(ee);
				break;
			case ControlModes.ACTION_MODE_IMPACT_ANALYSIS:
				// do nothing for impact analysis
				break;
			default:
				//System.out.println(this + " Mouse dragged : " + I18n.getString("Unknown_Action_Mode"));
				break;
			}
			if (null != m_maginifierPanel) {
				m_maginifierPanel.controlToPt(mapPointToScale(ee.getPoint()));
			}
			ee.consume();
			fireViewChanged();
		}

		public void mouseMoved(MouseEvent e) {
			// setFocusable(true);
			// requestFocus();
			if (null != m_maginifierPanel) {
				m_maginifierPanel.controlToPt(mapPointToScale(e.getPoint()));
			}
		}

	}

	class MainViewMouseListener implements MouseListener {

		public void mouseClicked(MouseEvent e) {
			e.consume();
		}

		private void mousePressedACTION_MODE_DRAW_LINK(MouseEvent ee) {
			Point p = mapPointToScale(ee.getPoint());
			int x = p.x;
			int y = p.y;
			m_mousePosition = p;
			double bestdist = Double.MAX_VALUE;
			ArchitectureModel model = ArchitectureModel.getInstance();
			Enumeration nodesEnum = model.getNodeList().elements();
			while (nodesEnum.hasMoreElements()) {
				Node n = (Node) nodesEnum.nextElement();
				double dist = (n.getCenterX() - x) * (n.getCenterX() - x) + (n.getCenterY() - y) * (n.getCenterY() - y);
				if (!n.isDeleted() && (dist < bestdist) && n.onDrawnObject(x, y)) {
					if (m_startNode != null) {
						m_startNode.setIsStartOrEnd(false);
						m_startNode = null;
					}
					m_startNode = n;
					bestdist = dist;
					m_startNode.setIsStartOrEnd(true);
				}
			}
		}

		private void mousePressedACTION_MODE_IMPACT_ANALYSIS(MouseEvent ee) {
			if (m_multipleSelectedNodes != null) {

				for (int i = 0; i < m_multipleSelectedNodes.size(); i++) {
					ArchitectureModel.getInstance().selectedDrawnObject(((DrawnObject) m_multipleSelectedNodes.elementAt(i)), false, DrawnObject.SELECTION_TYPE_BASIC, 0, m_multipleSelectedNodes);

				}
				m_multipleSelectedNodes.removeAllElements();
			}
			mousePressedForSelection(ee, ControlModes.ACTION_MODE_IMPACT_ANALYSIS);
		}

		public void mousePressed(MouseEvent ee) {
			m_motionListener = new MainViewMouseMotionListener();
			addMouseMotionListener(m_motionListener);
			Point p = mapPointToScale(ee.getPoint());
			int x = p.x;
			int y = p.y;
			// end of transform of mouse coordinates for zoom purpose

			switch (m_actionMode) {
			case ControlModes.ACTION_MODE_DRAW_BOX:
				m_firstBoxPoint = new Point(x, y);
				// returns null if the first point is not made in an existing
				// box
				m_groupToDrawIn = (Node) ArchitectureModel.isClickOnADrawnObject(m_drawnObjects, x, y);
				if (m_groupToDrawIn != null) {
					m_groupToDrawIn.setIsStartOrEnd(true);
				}
				break;
			case ControlModes.ACTION_MODE_DRAW_BOX_USECASE:
				ArchitectureModel.getInstance().resetMultipleSelection();
				m_multipleSelectedNodes.removeAllElements();
				m_firstBoxPoint = new Point(x, y);
				m_groupToDrawIn = (Node) ArchitectureModel.isClickOnADrawnObject(m_drawnObjects, x, y);
				if (m_groupToDrawIn != null) {
					m_groupToDrawIn.setIsStartOrEnd(true);
				}
				break;
			case ControlModes.ACTION_MODE_DRAW_BOX_DB:
				ArchitectureModel.getInstance().resetMultipleSelection();
				m_multipleSelectedNodes.removeAllElements();
				m_firstBoxPoint = new Point(x, y);
				m_groupToDrawIn = (Node) ArchitectureModel.isClickOnADrawnObject(m_drawnObjects, x, y);
				if (m_groupToDrawIn != null) {
					m_groupToDrawIn.setIsStartOrEnd(true);
				}
				break;
			case ControlModes.ACTION_MODE_SELECT:
				ArchitectureModel.getInstance().resetMultipleSelection();
				m_multipleSelectedNodes.removeAllElements();
				mousePressedForSelection(ee, ControlModes.ACTION_MODE_SELECT);
				break;
			case ControlModes.ACTION_MODE_MOVE:
				m_referencePoint = new Point(x, y);
				break;
			case ControlModes.ACTION_MODE_MULTIPLE_SELECTION:
				ArchitectureModel.getInstance().resetMultipleSelection();
				m_multipleSelectedNodes.removeAllElements();
				m_firstBoxPoint = new Point(x, y);
				break;
			case ControlModes.ACTION_MODE_MULTIPLE_PICK_SELECTION:
				m_firstBoxPoint = new Point(x, y);

				ArchitectureModel.getInstance().selectedMultipleNodes(((Node) ArchitectureModel.isClickOnADrawnObject(m_drawnObjects, x, y)), true, 0, 0, m_multipleSelectedNodes);
				break;
			case ControlModes.ACTION_MODE_DRAW_LINK:
				ArchitectureModel.getInstance().resetMultipleSelection();
				m_multipleSelectedNodes.removeAllElements();
				mousePressedACTION_MODE_DRAW_LINK(ee);
				break;
			case ControlModes.ACTION_MODE_IMPACT_ANALYSIS:
				ArchitectureModel.getInstance().resetMultipleSelection();
				m_multipleSelectedNodes.removeAllElements();
				mousePressedACTION_MODE_IMPACT_ANALYSIS(ee);
				break;
			default:
				//System.out.println(this + "Mouse pressed " + I18n.getString("Unknown_Action_Mode"));
			}

			ee.consume();
			fireViewChanged();
		}

		private void mousePressedForSelection(MouseEvent ee, int selectionType) {

			// Start of transform of mouse coordinates for zoom purpose
			Point p = mapPointToScale(ee.getPoint());

			int x = p.x;
			int y = p.y;
			// end of transform of mouse coordinates for zoom purpose

			double bestdist = Double.MAX_VALUE;
			// Delete
			/*Enumeration drawnObjectsEnum2 = m_drawnObjects.elements();
			while (drawnObjectsEnum2.hasMoreElements()) {
				DrawnObject currDO = (DrawnObject) drawnObjectsEnum2.nextElement();				
				ArchitectureModel.getInstance().selectedDrawnObject(currDO,false, DrawnObject.SELECTION_TYPE_BASIC, 0, m_multipleSelectedNodes);				
			}*/
			if (m_drawnObjects != null) {
				Enumeration drawnObjectsEnum = m_drawnObjects.elements();
				while (drawnObjectsEnum.hasMoreElements()) {
					DrawnObject currDO = (DrawnObject) drawnObjectsEnum.nextElement();
					// currDO.setSelected(false,0);
					if (currDO.isActionnable()) {
						if (currDO.onDrawnObjectResizeHandle(x, y)) {
							// resizing the object
							currDO.setResizeHandleSelected(true);
							m_resizedDrawObject = currDO;
							if (m_picked != null) {
								// ArchitectureModel.getInstance().selectedDrawnObject(m_picked,
								// false, DrawnObject.SELECTION_TYPE_BASIC, 0,
								// m_multipleSelectedNodes);
								m_picked = null;
							}
						} else {
							// selecting the object
							double dist = (currDO.getCenterX() - x) * (currDO.getCenterX() - x) + (currDO.getCenterY() - y) * (currDO.getCenterY() - y);
							if (currDO != m_previousPicked) {
								if (((dist < bestdist) || (dist == bestdist))) {
									if (currDO.onDrawnObject(x, y)) {
										bestdist = dist;

										if (m_picked != null) {
											// ArchitectureModel.getInstance().selectedDrawnObject(m_picked,
											// false,
											// DrawnObject.SELECTION_TYPE_BASIC,
											// 0,
											// m_multipleSelectedNodes);
											m_picked = null;
										}
										m_picked = currDO;
										// ArchitectureModel.getInstance().selectedDrawnObject(m_picked,
										// true, selectionType, 0,
										// m_multipleSelectedNodes);

									}
								}
							} else {
								// System.out.println("Selecting the same");
								if (null != m_picked) {
									// ArchitectureModel.getInstance().selectedDrawnObject(m_picked,
									// true, selectionType, 0,
									// m_multipleSelectedNodes);
								}
							}
						}
					}
				}

				if (m_previousPicked != null) {
					ArchitectureModel.getInstance().selectedDrawnObject(m_previousPicked, false, 0, 0, m_multipleSelectedNodes);
				}
				if (m_picked != null) {
					ArchitectureModel.getInstance().selectedDrawnObject(m_picked, true, selectionType, 0, m_multipleSelectedNodes);
				}
				m_previousPicked = m_picked;
			}
		}

		private void mouseReleasedACTION_MODE_SELECT(MouseEvent ee) {
			// draw an object while resing it
			if (m_resizedDrawObject != null) {
				if (m_draggedBoxPoint != null) {
					Node n = (Node) m_resizedDrawObject;
					int x = 0;
					int y = 0;
					int x1 = (int) (n.getCenterX() - n.getWidthDiv2());
					int y1 = (int) (n.getCenterY() - n.getHeightDiv2());
					int x2 = (int) m_draggedBoxPoint.x;
					int y2 = (int) m_draggedBoxPoint.y;
					int width = Math.abs(x1 - x2);
					int height = Math.abs(y1 - y2);
					if (x1 < x2) {
						x = (int) x1 + width / 2;
					} else {
						x = (int) x2 + width / 2;
					}
					if (y1 < y2) {
						y = (int) y1 + height / 2;
					} else {
						y = (int) y2 + height / 2;
					}
					n.setX((int) x);
					n.setY((int) y);
					n.setWidth((int) width);
					n.setHeight((int) height);
				}
				m_resizedDrawObject.setResizeHandleSelected(false);
				m_resizedDrawObject = null;
				m_draggedBoxPoint = null;
			}
		}

		private void mouseReleasedACTION_MODE_MULTIPLE_SELECTION(MouseEvent ee) {
			ArchitectureModel model = ArchitectureModel.getInstance();
			model.resetMultipleSelection();
			m_multipleSelectedNodes.removeAllElements();
			if (m_firstBoxPoint != null) {
				if (m_draggedBoxPoint != null) {
					int x1 = (int) m_firstBoxPoint.x;
					int y1 = (int) m_firstBoxPoint.y;
					int x2 = (int) m_draggedBoxPoint.x;
					int y2 = (int) m_draggedBoxPoint.y;

					NodeList nodes = model.getNodeList();

					if (m_multipleSelectedNodes != null) {
						for (int i = 0; i < m_multipleSelectedNodes.size(); i++) {
							ArchitectureModel.getInstance().selectedMultipleNodes(((Node) m_multipleSelectedNodes.elementAt(i)), false, 0, 0, m_multipleSelectedNodes);
							m_moving = false;
							// m_draggedBoxPoint = null;
							// m_firstBoxPoint = null;
						}
					}
					m_multipleSelectedNodes = new Vector();

					for (int i = 0; i < nodes.size(); i++) {
						Node currNode = (Node) nodes.elementAt(i);
						if (!currNode.isDeleted()) {
							if (currNode.getCenterX() > x1 && currNode.getCenterY() > y1 && currNode.getCenterX() < x2 && currNode.getCenterY() < y2) {
								if (!currNode.isInAGroup()) {
									ArchitectureModel.getInstance().selectedMultipleNodes(currNode, true, DrawnObject.SELECTION_TYPE_BASIC, 0, m_multipleSelectedNodes);
								} else {
									// to be determined
								}
							}
						}
					}
				}
			}
			if (m_multipleSelectedNodes != null) {
				ArchitectureModel.getInstance().fireEndMultipleSelection(m_multipleSelectedNodes);
				// m_drawnObjectEditor = new
				// DrawnObjectPanelMultipleSelection(m_multipleSelectedNodes,
				// m_applet);
				// m_applet.addSouthPanel(m_drawnObjectEditor);
			}

			m_firstBoxPoint = null;

			m_draggedBoxPoint = null;

		}

		private void mouseReleasedACTION_MODE_DRAW_BOX(MouseEvent ee, int type) {
			// un select the selected object
			if ((m_picked != null)) {
				ArchitectureModel.getInstance().selectedDrawnObject(m_picked, false, DrawnObject.SELECTION_TYPE_BASIC, 0, m_multipleSelectedNodes);
				m_picked = null;
			}
			if ((m_firstBoxPoint != null) && (m_secondBoxPoint != null)) {
				int x = 0;
				int y = 0;
				int x1 = (int) m_firstBoxPoint.x;
				int y1 = (int) m_firstBoxPoint.y;
				int x2 = (int) m_secondBoxPoint.x;
				int y2 = (int) m_secondBoxPoint.y;
				int width = Math.abs(x1 - x2);
				int height = Math.abs(y1 - y2);
				if (x1 < x2) {
					x = (int) x1 + width / 2;
				} else {
					x = (int) x2 + width / 2;
				}
				if (y1 < y2) {
					y = (int) y1 + height / 2;
				} else {
					y = (int) y2 + height / 2;
				}
				ArchitectureModel model = ArchitectureModel.getInstance();
				if (null != m_groupToDrawIn) {
					Node n = model.getNodeList().createNode((int) x, (int) y, (int) width, (int) height, type);
					m_groupToDrawIn.addChildObject(n);
				} else {
					model.getNodeList().createNode((int) x, (int) y, (int) width, (int) height, type);
				}
			}
			if (m_firstBoxPoint != null) {
				m_firstBoxPoint = null;
			}
			if (m_secondBoxPoint != null) {
				m_secondBoxPoint = null;
				if (m_groupToDrawIn != null) {

					m_groupToDrawIn.setIsStartOrEnd(false);

					m_groupToDrawIn = null;
				}
			}
		}

		private void mouseReleasedACTION_MODE_DRAW_LINK(MouseEvent ee) {
			ArchitectureModel model = ArchitectureModel.getInstance();
			if ((m_picked != null)) {
				ArchitectureModel.getInstance().selectedDrawnObject(m_picked, false, DrawnObject.SELECTION_TYPE_BASIC, 0, m_multipleSelectedNodes);
				m_picked = null;
			}
			if ((m_startNode != null) && (m_endNode != null)) {
				if (m_startNode instanceof NodeDB | m_endNode instanceof NodeDB) {
					model.addEdge(m_startNode, m_endNode, Edge.ARCHI_LINK_ANY_TO_DB, new Vector());
				} else if (m_startNode instanceof NodeUseCase | m_endNode instanceof NodeUseCase) {
					model.addEdge(m_startNode, m_endNode, Edge.ARCHI_LINK_USECASE_TO_BOX, new Vector());
				} else {
					model.addEdge(m_startNode, m_endNode, Edge.ARCHI_LINK, new Vector());
				}

				model.computeRealLinks();
			}

			if (m_startNode != null) {
				m_startNode.setIsStartOrEnd(false);
				m_startNode = null;
			}
			if (m_endNode != null) {
				m_endNode.setIsStartOrEnd(false);
				m_endNode = null;
			}
		}

		private void mouseReleasedACTION_MODE_IMPACT_ANALYSIS(MouseEvent ee) {
			ArchitectureModel model = ArchitectureModel.getInstance();
			model.fireEndMultipleSelection(m_multipleSelectedNodes);
			// m_drawnObjectEditor = new
			// DrawnObjectPanelMultipleSelection(m_multipleSelectedNodes,
			// m_applet);
			// m_applet.addSouthPanel(m_drawnObjectEditor);
		}

		public void mouseReleased(MouseEvent ee) {
			removeMouseMotionListener(m_motionListener);

			switch (m_actionMode) {
			case ControlModes.ACTION_MODE_SELECT:
				mouseReleasedACTION_MODE_SELECT(ee);
				break;
			case ControlModes.ACTION_MODE_MULTIPLE_SELECTION:
				mouseReleasedACTION_MODE_MULTIPLE_SELECTION(ee);
				break;
			case ControlModes.ACTION_MODE_DRAW_BOX:
				mouseReleasedACTION_MODE_DRAW_BOX(ee, 0);
				break;
			case ControlModes.ACTION_MODE_DRAW_BOX_USECASE:
				mouseReleasedACTION_MODE_DRAW_BOX(ee, 1);
				break;
			case ControlModes.ACTION_MODE_DRAW_BOX_DB:
				mouseReleasedACTION_MODE_DRAW_BOX(ee, 2);
				break;
			case ControlModes.ACTION_MODE_DRAW_LINK:
				mouseReleasedACTION_MODE_DRAW_LINK(ee);
				break;
			case ControlModes.ACTION_MODE_IMPACT_ANALYSIS:
				mouseReleasedACTION_MODE_IMPACT_ANALYSIS(ee);
				break;
			case ControlModes.ACTION_MODE_MOVE:
				// do nothing
				break;
			default:
				//System.out.println(this + " Mouse released " + "Unknown mode");

			}
			/*if (m_mousePosition != null) {
				m_mousePosition = null;
			}*/
			ee.consume();
			fireViewChanged();
		}

		public void mouseEntered(MouseEvent e) {
			requestTheFocus();
		}

		public void mouseExited(MouseEvent e) {
		}

	}

	public void modeChanged(EventObject e) {
		// TODO Auto-generated method stub

	}

}
