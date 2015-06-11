package com.compuware.caqs.presentation.applets.architecture.panels;

import java.awt.BorderLayout;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.util.EventObject;

import javax.swing.JPanel;
import javax.swing.JScrollBar;

import com.compuware.caqs.domain.architecture.serializeddata.ArchitectureModel;
import com.compuware.caqs.presentation.applets.architecture.panels.graphpanels.GraphPanelBird;
import com.compuware.caqs.presentation.applets.architecture.panels.graphpanels.GraphPanelMainViewAdvanced;
import com.compuware.caqs.presentation.applets.architecture.panels.graphpanels.ViewListener;

public class MainPanelScrolls extends JPanel implements ViewListener {
	private MyJScrollBar m_hbar = new MyJScrollBar(JScrollBar.HORIZONTAL, 0, 100, 0, 100, MyJScrollBar.HORIZONTALFX);
	private MyJScrollBar m_vbar = new MyJScrollBar(JScrollBar.VERTICAL, 0, 100, 0, 100, MyJScrollBar.VERTICALFX);
	private GraphPanelMainViewAdvanced m_mainGraphPanel;
	private GraphPanelBird m_birdPanel;
	
	public MainPanelScrolls(){
		this.setLayout(new BorderLayout());
		this.add(m_hbar, BorderLayout.SOUTH);
		this.add(m_vbar, BorderLayout.EAST);

		// needed to create the illusion of scrolling thru a full image
		m_hbar.addAdjustmentListener(new MyBarListener(0, m_hbar));
		m_hbar.setUnitIncrement(2);
		m_hbar.setBlockIncrement(1);

		m_vbar.addAdjustmentListener(new MyBarListener(0, m_vbar));
		m_vbar.setUnitIncrement(2);
		m_vbar.setBlockIncrement(1);
	}
	
	public void updatescrollbars() {
		this.m_vbar.updateValues();
		this.m_hbar.updateValues();
	}

	class MyJScrollBar extends JScrollBar {
		int m_extent;
		int m_type;
		public final static int VERTICALFX = 1;
		public final static int HORIZONTALFX = 2;

		public MyJScrollBar(int a, int b, int c, int d, int e, int type) {
			super(a, b, c, d, e);
			m_type = type;
		}

		public void updateValues() {
			AdjustmentListener[] listeners = this.getAdjustmentListeners();
			if (listeners.length > 0) {
				for (int i = 0; i < listeners.length; i++) {
					this.removeAdjustmentListener(listeners[i]);
				}
				Rectangle visibleRect = m_birdPanel.getControlledPanelVisibleRectangle();
				Point maxPoint = ArchitectureModel.getInstance().getMaxPoint();

				int transX = (int) (m_mainGraphPanel.getOffgraphics().getTransform().getTranslateX() * m_mainGraphPanel.getOffgraphics().getTransform().getScaleX());
				int transY = (int) (m_mainGraphPanel.getOffgraphics().getTransform().getTranslateY() * m_mainGraphPanel.getOffgraphics().getTransform().getScaleY());
				//System.out.println("Min " + transX + " " + transY);
				switch (m_type) {
				case MyJScrollBar.HORIZONTALFX:
					m_extent = visibleRect.width;
					m_hbar.setValues(visibleRect.x, visibleRect.width, 0 + transX, maxPoint.x + transX);
					m_hbar.setUnitIncrement(2);
					m_hbar.setBlockIncrement(1);
					break;
				case MyJScrollBar.VERTICALFX:
					m_extent = visibleRect.height;
					m_vbar.setValues(visibleRect.y, visibleRect.height, 0 + transY, maxPoint.y + transY);
					m_vbar.setUnitIncrement(2);
					m_vbar.setBlockIncrement(1);
					break;

				}

				for (int i = 0; i < listeners.length; i++) {
					this.addAdjustmentListener(listeners[i]);
				}
			}
		}

		public int getExtentValue() {
			return this.m_extent;
		}

		public int getTypeValue() {
			return this.m_type;
		}
	}

	class MyBarListener implements AdjustmentListener {
		int previousValue;
		MyJScrollBar m_myBar;

		public MyBarListener(int value, MyJScrollBar myBar) {
			this.previousValue = value;
			this.m_myBar = myBar;
		}

		public void adjustmentValueChanged(AdjustmentEvent ae) {
			int newValue = ae.getValue();
			this.m_myBar.removeAdjustmentListener(this);

			if (null != m_mainGraphPanel.getOffgraphics()) {
				// this.m_myBar.updateValues();
				//System.out.println("previousValue " + previousValue);
				//System.out.println("newValue " + newValue);
				switch (this.m_myBar.getTypeValue()) {
				case MyJScrollBar.HORIZONTALFX:
					m_mainGraphPanel.translateView(-(newValue - previousValue), 0);
					break;
				case MyJScrollBar.VERTICALFX:
					m_mainGraphPanel.translateView(0, -(newValue - previousValue));
					break;
				}
				this.m_myBar.updateValues();
				this.previousValue = newValue;

			}
			this.m_myBar.addAdjustmentListener(this);
		}
	}

	public void setMainGraphPanel(GraphPanelMainViewAdvanced graphPanel) {
		this.m_mainGraphPanel = graphPanel;
		this.add(m_mainGraphPanel, BorderLayout.CENTER);		
	}

	public void setBirdView(GraphPanelBird panel) {
		this.m_birdPanel = panel;		
	}

	public void viewChanged(EventObject e) {
		this.updatescrollbars();		
	}
}
