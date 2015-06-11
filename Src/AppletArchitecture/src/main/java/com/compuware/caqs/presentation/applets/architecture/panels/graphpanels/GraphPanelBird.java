package com.compuware.caqs.presentation.applets.architecture.panels.graphpanels;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import com.compuware.caqs.domain.architecture.serializeddata.ArchitectureModel;


public class GraphPanelBird extends GraphPanel {
	public GraphPanelBird( String title, boolean permanentFit) {
		
		super(title, permanentFit);
		m_schematicDraw =true;
	}

	protected void addSpecificMouseListeners() {
		addMouseListener(new BirdViewMouseListener());
		addMouseMotionListener(new BirdViewMouseListener());
	}

	protected void drawActions() {

		if (this.m_mousePosition != null) {
			// draw square around mouse only for the bird view
			this.m_offgraphics.setColor(Color.black);
			BasicStroke stroke = new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f);
			this.m_offgraphics.setStroke(stroke);
			Rectangle rect = this.getControlledPanelVisibleRectangle();
			this.m_offgraphics.drawRect(rect.x, rect.y, rect.width, rect.height);
		}
	}

	class BirdViewMouseListener implements MouseListener, MouseMotionListener {
		public void mouseClicked(MouseEvent ee) {
			m_mousePosition = mapPointToScale(ee.getPoint());
			m_controledPanel.controlToPt(m_mousePosition);
			//fireViewChanged();
			// System.out.println("click in the bird view");
			ee.consume();
		}

		public void mousePressed(MouseEvent ee) {
			m_mousePosition = mapPointToScale(ee.getPoint());
			m_controledPanel.controlToPt(m_mousePosition);
			//fireViewChanged();
			ee.consume();
		}

		public void mouseReleased(MouseEvent ee) {
		}

		public void mouseEntered(MouseEvent e) {
		}

		public void mouseExited(MouseEvent e) {
		}

		public void mouseDragged(MouseEvent ee) {
			 m_mousePosition = mapPointToScale(ee.getPoint());
			 m_controledPanel.controlToPt(m_mousePosition);
			// fireViewChanged();
		}

		public void mouseMoved(MouseEvent e) {
		}
	}
}
