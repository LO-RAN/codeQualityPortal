package com.compuware.caqs.presentation.applets.architecture.panels;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JToolBar;

import com.compuware.caqs.presentation.applets.architecture.I18n;
import com.compuware.caqs.presentation.applets.architecture.panels.graphpanels.GraphPanelMainViewAdvanced;

public class ToolBarDrawingHelp extends JToolBar implements ActionListener {
	// align
	protected JButton m_alignHorizontalBtn = null;
	protected JButton m_alignVerticalBtn = null;
	GraphPanelMainViewAdvanced m_mainGraphPanel;
	
	public ToolBarDrawingHelp(GraphPanelMainViewAdvanced mainPanel) {
		m_mainGraphPanel = mainPanel;
		
		ImageIcon imgAlignHorizontalIcon = Tools.createAppletImageIcon("alignHoriz.gif", "");
		ImageIcon imgAlignVerticalIcon = Tools.createAppletImageIcon("alignVertical.gif", "");

		this.m_alignHorizontalBtn = new JButton(imgAlignHorizontalIcon);
		this.m_alignHorizontalBtn.setToolTipText(I18n.getString("AlignHoriz"));

		this.m_alignVerticalBtn = new JButton(imgAlignVerticalIcon);
		this.m_alignVerticalBtn.setToolTipText(I18n.getString("AlignVertical"));

		this.add(this.m_alignHorizontalBtn);
		this.add(this.m_alignHorizontalBtn);
		this.m_alignHorizontalBtn.addActionListener(this);

		this.add(this.m_alignVerticalBtn);
		this.add(this.m_alignVerticalBtn);
		this.m_alignVerticalBtn.addActionListener(this);

	}

	public void actionPerformed(ActionEvent e) {
		Object src = e.getSource();
		if (src == this.m_alignHorizontalBtn) {
			m_mainGraphPanel.alignHorizontal();
		} else if (src == this.m_alignVerticalBtn) {
			m_mainGraphPanel.alignVertical();
		}
	}
}
