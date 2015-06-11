package com.compuware.caqs.presentation.applets.architecture.panels.control;

import java.awt.BorderLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JPanel;

import com.compuware.caqs.domain.architecture.serializeddata.PreferencesGraphical;


public class GraphicalPreferencesWindow extends JFrame implements ItemListener {

	JCheckBox m_boxIsGradientPaint = new JCheckBox();
	JCheckBox m_boxOpenGl = new JCheckBox();
	JCheckBox m_antialiasing = new JCheckBox();
	JCheckBox m_boxDrawResizeHandle = new JCheckBox();
	JCheckBox m_boxArrowDrawn = new JCheckBox();
	JCheckBox m_showLinksInBirdView = new JCheckBox();

	public GraphicalPreferencesWindow() {
		this.setDefaultCloseOperation(javax.swing.JFrame.DISPOSE_ON_CLOSE);
		this.setSize(200, 400);
		this.getContentPane().setLayout(new BorderLayout());
		this.createPanel();
		this.validate();
		this.setVisible(true);
		this.setAlwaysOnTop(false);
	}

	private void createPanel() {
		JPanel internalPanel = new JPanel();
		internalPanel.setLayout(new BoxLayout(internalPanel, BoxLayout.PAGE_AXIS));
		// gradient
		m_boxIsGradientPaint.setText("Gradient");
		m_boxIsGradientPaint.setSelected(PreferencesGraphical.getInstance().isGradientFill());
		m_boxIsGradientPaint.addItemListener(this);
		internalPanel.add(m_boxIsGradientPaint);

		// openGl
		m_boxOpenGl.setText("OpenGl");
		m_boxOpenGl.setSelected(PreferencesGraphical.getInstance().isOpenGL());
		m_boxOpenGl.addItemListener(this);
		internalPanel.add(m_boxOpenGl);

		// antialiasing
		m_antialiasing.setText("Anti-aliasing");
		m_antialiasing.setSelected(PreferencesGraphical.getInstance().getAntialiasing());
		m_antialiasing.addItemListener(this);
		internalPanel.add(m_antialiasing);
		
		// ResizeHandle
		m_boxDrawResizeHandle.setText("Resize Handle");
		m_boxDrawResizeHandle.setSelected(PreferencesGraphical.getInstance().isDrawResizeHandle());
		m_boxDrawResizeHandle.addItemListener(this);
		internalPanel.add(m_boxDrawResizeHandle);

		// arrow
		m_boxArrowDrawn.setText("Arrow");
		m_boxArrowDrawn.setSelected(PreferencesGraphical.getInstance().isArrowDrawn());
		m_boxArrowDrawn.addItemListener(this);
		internalPanel.add(m_boxArrowDrawn);

		m_showLinksInBirdView.setText("Show links in bird view");
		m_showLinksInBirdView.setSelected(PreferencesGraphical.getInstance().getShowLinksInBirdView());
		m_showLinksInBirdView.addItemListener(this);
		internalPanel.add(m_showLinksInBirdView);
		
		this.getContentPane().add("Center", internalPanel);
		this.setResizable(false);
		this.setVisible(true);
		internalPanel.repaint();
	}

	public void itemStateChanged(ItemEvent e) {
		Object src = e.getSource();

		if (src == m_boxIsGradientPaint) {
			PreferencesGraphical.getInstance().setGradiedtFill(m_boxIsGradientPaint.isSelected());
		} else if (src == m_boxOpenGl) {
			PreferencesGraphical.getInstance().setOpenGLMode(m_boxOpenGl.isSelected());
		} else if(src == m_antialiasing) {
			PreferencesGraphical.getInstance().setAntialiaising(m_antialiasing.isSelected());
		} 
		else if (src == m_boxDrawResizeHandle) {
			PreferencesGraphical.getInstance().setDrawResizeHandle(m_boxDrawResizeHandle.isSelected());
		} else if (src == m_boxArrowDrawn) {
			PreferencesGraphical.getInstance().setDrawArrow(m_boxArrowDrawn.isSelected());
		}
		else if (src==m_showLinksInBirdView){
			PreferencesGraphical.getInstance().setShowLinksInBirdView(m_showLinksInBirdView.isSelected());
		}
	}
}
