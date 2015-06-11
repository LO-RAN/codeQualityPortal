/*
 * GraphPanel.java
 *
 * Created on 23 octobre 2002, 14:33
 */

/**
 *
 * @author  fxa
 */
package com.compuware.caqs.presentation.applets.architecture.panels.graphpanels;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.util.Enumeration;
import java.util.EventObject;
import java.util.Vector;

import javax.swing.event.EventListenerList;
import javax.swing.JPanel;

import com.compuware.caqs.domain.architecture.serializeddata.ArchitectureModel;
import com.compuware.caqs.domain.architecture.serializeddata.DrawnObject;
import com.compuware.caqs.domain.architecture.serializeddata.PreferencesColors;
import com.compuware.caqs.domain.architecture.serializeddata.PreferencesGraphical;
import com.compuware.caqs.domain.architecture.serializeddata.Node;
import com.compuware.caqs.presentation.applets.architecture.I18n;
import com.compuware.caqs.presentation.applets.architecture.panels.control.ControlGraphics;
import com.compuware.caqs.presentation.applets.architecture.panels.control.ControlGraphicsListener;
import com.compuware.caqs.presentation.applets.architecture.panels.control.ControlModeListener;
import com.compuware.caqs.presentation.applets.architecture.panels.control.ControlModes;


public abstract class GraphPanel extends JPanel implements ControlModeListener , ControlGraphicsListener{
	protected EventListenerList m_listenerList = new EventListenerList();
	protected int m_actionMode = ControlModes.ACTION_MODE_SELECT;
	
	protected int m_controlgraphicsmode = ControlGraphics.DISPLAY_REALLINKS_AND_ARCHILINKS;

	protected Point m_firstBoxPoint;

	protected Point m_secondBoxPoint;

	protected Point m_draggedBoxPoint;

	protected Point m_referencePoint;

	protected Point m_mousePosition = new Point(0, 0);

	protected DrawnObject m_resizedDrawObject;

	protected Vector m_drawnObjects;

	protected Vector m_multipleSelectedNodes = new Vector();

	protected boolean m_moving = false;

	protected Node m_startNode;

	protected Node m_endNode;

	protected Image m_offscreen;

	protected Graphics2D m_offgraphics;

	// panel parameters
	// private String m_title;
	protected int m_preferedSizeW = 0;

	protected int m_preferedSizeH = 0;

	protected GraphPanel m_controledPanel;
	protected GraphPanel m_maginifierPanel;

	protected boolean m_permanentFit = false;

	protected boolean m_zoomScaleChanged = true;

	protected String m_title;
	protected boolean m_schematicDraw = false;
	
	public GraphPanel(String title, boolean permanentFit) {
		
		this.m_permanentFit = permanentFit;
		this.m_title = title;

		// take care must be other
		addSpecificMouseListeners();
	}

	abstract protected void addSpecificMouseListeners();
	
	public void graphicsControlsChanged(int mode){
    	this.m_controlgraphicsmode = mode;
    }

	public void modeChanged(int mode) {
		this.m_actionMode = mode;
	}

	public void addViewListener(ViewListener l) {
		this.listenerList.add(ViewListener.class, l);
	}

	public void removeMViewListener(ViewListener l) {
		this.listenerList.remove(ViewListener.class, l);
	}

	protected void fireViewChanged() {
		// System.out.println(this.m_title + "fireViewChanged" + this);
		ViewListener[] listeners = (ViewListener[]) listenerList.getListeners(ViewListener.class);
		EventObject e = new EventObject(this);
		for (int i = listeners.length - 1; i >= 0; i--) {
			listeners[i].viewChanged(e);
		}
	}

	public Graphics2D getOffgraphics() {
		return this.m_offgraphics;
	}

	public void setDrawnObjects(Vector<DrawnObject> objects) {
		this.m_drawnObjects = objects;
	}

	public void setControledPanel(GraphPanel controledPanel) {
		this.m_controledPanel = controledPanel;
	}

	public void setMaginifier(GraphPanel controledPanel) {
		this.m_maginifierPanel = controledPanel;
	}

	public void zoomFit() {
		if (this.m_offgraphics != null) {

			this.m_zoomScaleChanged = true;
			ArchitectureModel model = ArchitectureModel.getInstance();
			Point maxP = model.getMaxPoint();

			if (maxP.x != 0 && maxP.y != 0) {

				this.m_offgraphics.getTransform().transform(maxP, maxP);

				if (maxP.x > 0 && maxP.y > 0) {
					double scX = 1.0;
					try {
						scX = (double) (this.getBounds().width - 5) / maxP.x;
					} catch (Exception e) {
						e.printStackTrace();
						scX = 1.0;
					}
					double scY = 1.0;
					try {
						scY = (double) (this.getBounds().height - 5) / maxP.y;
					} catch (Exception e) {
						e.printStackTrace();
						scY = 1.0;
					}

					double sc = 1.0;
					if (scX > scY) {
						sc = scY;
					} else {
						sc = scX;
					}
					if (sc == Double.NaN) {
						sc = 0.1;
					}
					if (sc > 0.0) {

						this.m_offgraphics.scale(sc, sc);

					}
				} else {
					this.m_offgraphics.setTransform(new AffineTransform(0.01, 0.0, 0.0, 0.01, 0.0, 0.0));
				}

			}

		}
	}

	public void translateView(double x, double y) {
		AffineTransform at = m_offgraphics.getTransform();
		// System.out.println(at);
		at.translate(x, y);
		m_offgraphics.setTransform(at);

		this.fireViewChanged();
	}

	/**
	 * draw user actions
	 */
	private void drawActionsACTION_MODE_DRAW_LINK() {
		if ((this.m_mousePosition != null) && (this.m_startNode != null)) {
			this.m_offgraphics.drawLine((int) this.m_startNode.getCenterX(), (int) this.m_startNode.getCenterY(), (int) this.m_mousePosition.x, (int) this.m_mousePosition.y);
		}
	}

	private void drawActionsACTION_MODE_SELECT() {
		// resizing a node
		if (this.m_resizedDrawObject != null && this.m_draggedBoxPoint != null) {
			Node n = (Node) this.m_resizedDrawObject;
			int x = 0;
			int y = 0;
			int x1 = (int) (n.getCenterX() - n.getWidthDiv2());
			int y1 = (int) (n.getCenterY() - n.getHeightDiv2());
			int x2 = (int) this.m_draggedBoxPoint.x;
			int y2 = (int) this.m_draggedBoxPoint.y;
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
			this.m_offgraphics.setColor(Color.black);
			this.m_offgraphics.drawRect((int) x - width / 2, (int) y - height / 2, (int) width, (int) height);
		}
	}

	private void drawActionsACTION_MODE_DRAW_BOX() {
		// draw a node
		if ((this.m_firstBoxPoint != null) && (this.m_secondBoxPoint != null)) {
			int x = 0;
			int y = 0;
			int x1 = (int) this.m_firstBoxPoint.x;
			int y1 = (int) this.m_firstBoxPoint.y;
			int x2 = (int) this.m_secondBoxPoint.x;
			int y2 = (int) this.m_secondBoxPoint.y;
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
			this.m_offgraphics.setColor(Color.black);
			this.m_offgraphics.drawRect((int) x - width / 2, (int) y - height / 2, (int) width, (int) height);
		}
	}

	private void drawActionsACTION_MODE_MULTIPLE_SELECTION() {
		if ((this.m_firstBoxPoint != null) && (this.m_draggedBoxPoint != null)) {
			int x = 0;
			int y = 0;
			int x1 = (int) this.m_firstBoxPoint.x;
			int y1 = (int) this.m_firstBoxPoint.y;
			int x2 = (int) this.m_draggedBoxPoint.x;
			int y2 = (int) this.m_draggedBoxPoint.y;
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
			this.m_offgraphics.setColor(Color.black);
			this.m_offgraphics.drawRect((int) x - width / 2, (int) y - height / 2, (int) width, (int) height);
		}
	}

	protected void drawActions() {
		switch (this.m_actionMode) {
		// drawing a link
		case ControlModes.ACTION_MODE_DRAW_LINK:
			drawActionsACTION_MODE_DRAW_LINK();
			break;
		case ControlModes.ACTION_MODE_SELECT:
			drawActionsACTION_MODE_SELECT();
			break;
		case ControlModes.ACTION_MODE_DRAW_BOX:
			drawActionsACTION_MODE_DRAW_BOX();
			break;
		case ControlModes.ACTION_MODE_DRAW_BOX_USECASE:
			drawActionsACTION_MODE_DRAW_BOX();
			break;
		case ControlModes.ACTION_MODE_DRAW_BOX_DB:
			drawActionsACTION_MODE_DRAW_BOX();
			break;
		case ControlModes.ACTION_MODE_MULTIPLE_SELECTION:
			drawActionsACTION_MODE_MULTIPLE_SELECTION();
			break;
		case ControlModes.ACTION_MODE_IMPACT_ANALYSIS:
			// nothing to draw for impact analysis action
			break;
		case ControlModes.ACTION_MODE_MOVE:
			// nothing to draw for impact analysis action
			break;
		default:
			//System.out.println(this + "Draw Actions : " + I18n.getString("Unknown_Action_Mode"));
		}
	}

	public Rectangle getControlledPanelVisibleRectangle() {
		Rectangle rect = new Rectangle(0, 0, 10, 10);
		Point topLeft = new Point(0, 0);
		this.m_controledPanel.mapPointToScale(topLeft);
		Point bottomRight = new Point(m_controledPanel.getBounds().width, m_controledPanel.getBounds().height);
		this.m_controledPanel.mapPointToScale(bottomRight);
		rect = new Rectangle(topLeft.x, topLeft.y, bottomRight.x - topLeft.x, bottomRight.y - topLeft.y);

		return rect;
	}
	
	public Rectangle getVisibleRectangle(){
		Rectangle rect = new Rectangle(0, 0, 10, 10);
		Point topLeft = new Point(0, 0);
		this.mapPointToScale(topLeft);
		Point bottomRight = new Point(this.getBounds().width, this.getBounds().height);
		this.mapPointToScale(bottomRight);
		rect = new Rectangle(topLeft.x, topLeft.y, bottomRight.x - topLeft.x, bottomRight.y - topLeft.y);

		return rect;
	}

	protected void drawOffscreenImage() {
		try {

			// set width of image : the visible part of the jpane
			int viewWidth = this.getParent().getBounds().width;
			int viewHeight = this.getParent().getBounds().height;

			//			
			this.m_offscreen = createVolatileImage(viewWidth, viewHeight);
			
			if (this.m_offgraphics != null) {
				AffineTransform at = this.m_offgraphics.getTransform();
				this.m_offgraphics.dispose();
				this.m_offgraphics = (Graphics2D) this.m_offscreen.getGraphics();
				this.m_offgraphics.setTransform(at);
			} else {
				this.m_offgraphics = (Graphics2D) this.m_offscreen.getGraphics();
				//default zoom factor
				this.m_offgraphics.scale(0.9, 0.9);
				this.m_offgraphics.scale(0.9, 0.9);
				this.m_offgraphics.scale(0.9, 0.9);
			}

			//
			PreferencesGraphical gp = PreferencesGraphical.getInstance();
			if (gp.getAntialiasing()) {
				this.m_offgraphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			} else {
				this.m_offgraphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
			}

			this.m_zoomScaleChanged = false;
			this.m_offgraphics.setBackground(PreferencesColors.getInstance().getBackGroungColor());
			this.m_offgraphics.setColor(PreferencesColors.getInstance().getBackGroungColor());
	
			Rectangle myVisibleRect = getVisibleRectangle();

			this.m_offgraphics.setColor(PreferencesColors.getInstance().getBackGroungColor());
			this.m_offgraphics.fillRect(myVisibleRect.x, myVisibleRect.y, myVisibleRect.width, myVisibleRect.height);
			
			this.drawObjects();
			this.drawActions();
		} catch (OutOfMemoryError e) {
			System.out.println("image size " + this.getWidth() + " " + this.getHeight());
			System.out.println(this.m_offgraphics.getTransform());
			System.out.println(I18n.getString("Running_out_of_memory"));
			System.gc();
			e.printStackTrace();
		}
	}

	private void drawObjects() {

		if (this.m_permanentFit) {
			this.zoomFit();
		}
		if (this.m_drawnObjects != null) {
			Enumeration drawnObjectsEnum = this.m_drawnObjects.elements();
			while (drawnObjectsEnum.hasMoreElements()) {
				DrawnObject currDO = (DrawnObject) drawnObjectsEnum.nextElement();
				if (!currDO.isInAGroup()) {
					currDO.update(this.m_offgraphics,this.m_schematicDraw);
				}
			}
		}
	}

	public void paintComponent(Graphics g) {
		
		
		super.paintComponent(g);
		this.drawOffscreenImage();
		g.drawImage(this.m_offscreen, 0, 0, Color.pink, null);
		g.dispose();
	}

	public Point mapPointToScale(Point p) {
		try {
			if (this.m_offgraphics != null) {
				if (this.m_offgraphics.getTransform() != null) {
					AffineTransform inverse = this.m_offgraphics.getTransform().createInverse();
					inverse.transform(p, p);
				}
			}
			// your Point p is now mapped to the graph paper of the "real"
			// points
		} catch (NoninvertibleTransformException e2) {
			// this will never happen if you're just scaling/translating. If you
			// concatenate crazy rotation & sheering it could get more dangerous
			e2.printStackTrace();
		}
		return p;
	}

	public void controlToPt(Point mouseCoord) {
		this.m_mousePosition = new Point(mouseCoord.x, mouseCoord.y);
		Point ptMouse = new Point(mouseCoord.x, mouseCoord.y);
		Rectangle bounds = this.getBounds();
		Point pt = new Point(bounds.width, bounds.height);
		// System.out.println("ctrl "+ this.m_title + " to point" + ptMouse + "
		// In size " + pt);

		if (this.m_offgraphics != null) {

			AffineTransform at = this.m_offgraphics.getTransform();
			double scalex = at.getScaleX();
			double scaley = at.getScaleY();

			at.setToTranslation(scalex * (-ptMouse.x) + pt.x / 2, scaley * (-ptMouse.y) + pt.y / 2);
			// at.setToTranslation(at.getTranslateX()+(ptMouse.x),
			// at.getTranslateY()+ (ptMouse.y ));
			at.scale(scalex, scaley);
			// System.out.println(at);
			this.m_offgraphics.setTransform(at);
		}
		// System.out.println("ctrl to point");
		this.fireViewChanged();
	}

	public void alignHorizontal() {
		if (m_multipleSelectedNodes != null && m_firstBoxPoint != null && m_draggedBoxPoint != null) {
			System.out.println("align");
			double minX = this.m_firstBoxPoint.x;
			double minY = this.m_firstBoxPoint.y;
			double maxX = this.m_draggedBoxPoint.x;
			double maxY = this.m_draggedBoxPoint.y;

			double pas = (maxX - minX) / m_multipleSelectedNodes.size();
			double p = 0;
			for (int i = 0; i < m_multipleSelectedNodes.size(); i++) {
				Node currNode = (Node) m_multipleSelectedNodes.elementAt(i);

				currNode.setY(minY + (maxY - minY) / 2);
				currNode.setX(minX + p);
				p += pas;
			}
			this.fireViewChanged();
		}
	}

	public void alignVertical() {
		if (m_multipleSelectedNodes != null && m_firstBoxPoint != null && m_draggedBoxPoint != null) {
			System.out.println("align");
			double minX = this.m_firstBoxPoint.x;
			double minY = this.m_firstBoxPoint.y;
			double maxX = this.m_draggedBoxPoint.x;
			double maxY = this.m_draggedBoxPoint.y;

			double pas = (maxY - minY) / m_multipleSelectedNodes.size();
			double p = 0;
			for (int i = 0; i < m_multipleSelectedNodes.size(); i++) {
				Node currNode = (Node) m_multipleSelectedNodes.elementAt(i);

				currNode.setY(minY + p);
				currNode.setX(minX + (maxX - minX) / 2);
				p += pas;
			}
			this.fireViewChanged();
		}
	}
}
