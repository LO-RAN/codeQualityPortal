package com.compuware.caqs.presentation.applets.architecture.panels;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.EventObject;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JToolBar;

import com.compuware.caqs.presentation.applets.architecture.I18n;
import com.compuware.caqs.presentation.applets.architecture.panels.control.ControlGraphics;
import com.compuware.caqs.presentation.applets.architecture.panels.control.ControlGraphicsListener;
import com.compuware.caqs.presentation.applets.architecture.panels.graphpanels.GraphPanelMainViewAdvanced;

public class ToolBarView extends JToolBar implements ActionListener, ItemListener {
	// zoom
	protected JButton m_zoomIn = null;

	protected JButton m_zoomOut = null;

	protected JButton m_zoomFit = null;

	protected JButton m_zoom1 = null;

	// display relay and archi link
	protected JCheckBox m_displayArchi = new JCheckBox(I18n.getString("architectureLink"));

	protected JCheckBox m_displayReal = new JCheckBox(I18n.getString("realLink"));

	protected int m_graphicsmode = ControlGraphics.DISPLAY_REALLINKS_AND_ARCHILINKS;

	GraphPanelMainViewAdvanced m_mainGraphPanel;

	public ToolBarView(GraphPanelMainViewAdvanced mainPanel) {
		m_mainGraphPanel = mainPanel;

		ImageIcon imgIn = Tools.createAppletImageIcon("zoomIn.gif", "");
		ImageIcon imgOut = Tools.createAppletImageIcon("zoomOut.gif", "");
		ImageIcon imgFit = Tools.createAppletImageIcon("bestFit.gif", "");
		ImageIcon imgZoom1 = Tools.createAppletImageIcon("zoom1.gif", "");

		this.m_zoomIn = new JButton(imgIn);
		this.m_zoomIn.setToolTipText(I18n.getString("zoomIn"));

		this.m_zoomOut = new JButton(imgOut);
		this.m_zoomOut.setToolTipText(I18n.getString("zoomOut"));

		this.m_zoomFit = new JButton(imgFit);
		this.m_zoomFit.setToolTipText(I18n.getString("bestFit"));

		this.m_zoom1 = new JButton(imgZoom1);
		this.m_zoom1.setToolTipText("");

		this.add(this.m_zoomOut);
		this.m_zoomOut.addActionListener(this);
		this.add(this.m_zoomIn);
		this.m_zoomIn.addActionListener(this);
		this.add(this.m_zoomFit);
		this.m_zoomFit.addActionListener(this);
		this.add(this.m_zoom1);
		this.m_zoom1.addActionListener(this);
		this.add(this.m_zoom1);

		this.m_displayReal = (JCheckBox) this.add(new JCheckBox(I18n.getString("realLink")));
		this.m_displayArchi = (JCheckBox) this.add(new JCheckBox(I18n.getString("architectureLink")));

		this.m_displayArchi.setSelected(true);
		this.m_displayReal.setSelected(true);
		this.m_displayArchi.addItemListener(this);
		this.m_displayReal.addItemListener(this);
	}

	public void actionPerformed(ActionEvent e) {
		Object src = e.getSource();
		if (src == this.m_zoomIn) {
			this.m_mainGraphPanel.zoomIn();
			// this.m_drawer.draw();
		} else if (src == this.m_zoomOut) {
			this.m_mainGraphPanel.zoomOut();
			// this.m_drawer.draw();
		} else if (src == this.m_zoomFit) {
			this.m_mainGraphPanel.zoomFit();
			// this.m_drawer.draw();
		} else if (src == this.m_zoom1) {
			this.m_mainGraphPanel.zoom1();
			// this.m_drawer.draw();
		}
	}

	public void itemStateChanged(ItemEvent e) {
		Object src = e.getSource();

		if (src == this.m_displayArchi || src == this.m_displayReal) {
			if (m_displayArchi.isSelected()) {
				this.m_graphicsmode = ControlGraphics.DISPLAY_ARCHILINKS_ONLY;
				if (m_displayReal.isSelected()) {
					this.m_graphicsmode = ControlGraphics.DISPLAY_REALLINKS_AND_ARCHILINKS;
				}
			} else {

				if (m_displayReal.isSelected()) {
					this.m_graphicsmode = ControlGraphics.DISPLAY_REALLINKS_ONLY;
				} else {
					this.m_graphicsmode = ControlGraphics.DISPLAY_NONE;
				}
			}
			this.fireControlGraphicsChanged();
		}
	}

	public void addControlGraphicsListener(ControlGraphicsListener l) {
		this.listenerList.add(ControlGraphicsListener.class, l);
	}

	public void removeControlGraphicsListener(ControlGraphicsListener l) {
		this.listenerList.remove(ControlGraphicsListener.class, l);
	}

	protected void fireControlGraphicsChanged() {
		ControlGraphicsListener[] listeners = (ControlGraphicsListener[]) listenerList.getListeners(ControlGraphicsListener.class);
		EventObject e = new EventObject(this);
		for (int i = listeners.length - 1; i >= 0; i--) {
			listeners[i].graphicsControlsChanged(this.m_graphicsmode);
		}
	}
}
