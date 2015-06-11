package com.compuware.caqs.presentation.applets.architecture.panels;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.EventObject;

import javax.swing.ImageIcon;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;
import javax.swing.ButtonGroup;

import com.compuware.caqs.presentation.applets.architecture.I18n;
import com.compuware.caqs.presentation.applets.architecture.panels.control.ControlModeListener;
import com.compuware.caqs.presentation.applets.architecture.panels.control.ControlModes;

public class ToolBarControlMode extends JToolBar implements ActionListener {
	// selection toggle button
	protected int m_actionmode = ControlModes.ACTION_MODE_SELECT;

	protected JToggleButton m_selectionBtn = null;

	protected JToggleButton m_linkBtn = null;

	protected JToggleButton m_boxBtn = null;
	protected JToggleButton m_boxUseCaseBtn = null;
	protected JToggleButton m_boxDbBtn = null;

	protected JToggleButton m_moveSelectionBtn = null;

	protected JToggleButton m_multipleSelection = null;

	protected JToggleButton m_impactAnalysis = null;

	private boolean m_testing;
	public ToolBarControlMode(boolean testing) {
		this.m_testing = testing;
		ImageIcon imgSelect = Tools.createAppletImageIcon("select.gif", "");
		ImageIcon imgLinks = Tools.createAppletImageIcon("liens.gif", "");
		ImageIcon imgImpact = Tools.createAppletImageIcon("impact.gif", "");
		ImageIcon imgModules = Tools.createAppletImageIcon("modules.gif", "");
		ImageIcon imgUseCase = Tools.createAppletImageIcon("useCase.gif", "");
		ImageIcon imgDb = Tools.createAppletImageIcon("db.gif", "");
		ImageIcon imgMultiSelect = Tools.createAppletImageIcon("multiSelect.gif", "");
		ImageIcon imgMoveMultiSelect = Tools.createAppletImageIcon("moveMultiSelect.gif", "");

		this.m_selectionBtn = new JToggleButton(imgSelect);
		this.m_selectionBtn.setToolTipText(I18n.getString("select"));

		this.m_multipleSelection = new JToggleButton(imgMultiSelect);
		this.m_multipleSelection.setToolTipText(I18n.getString("MultiSelection"));

		this.m_impactAnalysis = new JToggleButton(imgImpact);
		this.m_impactAnalysis.setToolTipText(I18n.getString("ImpactAnalysis"));

		this.m_moveSelectionBtn = new JToggleButton(imgMoveMultiSelect);
		this.m_moveSelectionBtn.setToolTipText(I18n.getString("Move"));

		this.m_linkBtn = new JToggleButton(imgLinks);
		this.m_linkBtn.setToolTipText(I18n.getString("link"));

		this.m_boxBtn = new JToggleButton(imgModules);
		this.m_boxBtn.setToolTipText(I18n.getString("Module"));

		this.m_boxUseCaseBtn = new JToggleButton(imgUseCase);
		this.m_boxUseCaseBtn.setToolTipText(I18n.getString("UseCase"));

		this.m_boxDbBtn = new JToggleButton(imgDb);
		this.m_boxDbBtn.setToolTipText(I18n.getString("DataBase"));

		ButtonGroup pgroup = new ButtonGroup();
		
		pgroup.add(this.m_selectionBtn);
		this.add(this.m_selectionBtn);
		this.m_selectionBtn.addActionListener(this);

		this.addSeparator();

		pgroup.add(this.m_linkBtn);
		this.add(this.m_linkBtn);
		this.m_linkBtn.addActionListener(this);

		pgroup.add(this.m_boxBtn);
		this.add(this.m_boxBtn);
		this.m_boxBtn.addActionListener(this);

		//if (this.m_testing) {
			pgroup.add(this.m_boxUseCaseBtn);
			this.add(this.m_boxUseCaseBtn);
			this.m_boxUseCaseBtn.addActionListener(this);

			pgroup.add(this.m_boxDbBtn);
			this.add(this.m_boxDbBtn);
			this.m_boxDbBtn.addActionListener(this);
		//}

		this.addSeparator();

		pgroup.add(this.m_multipleSelection);
		this.add(this.m_multipleSelection);
		this.m_multipleSelection.addActionListener(this);

		pgroup.add(this.m_moveSelectionBtn);
		this.add(this.m_moveSelectionBtn);
		this.m_moveSelectionBtn.addActionListener(this);

		this.addSeparator();

		pgroup.add(this.m_impactAnalysis);
		this.add(this.m_impactAnalysis);
		this.m_impactAnalysis.addActionListener(this);
	}

	public void actionPerformed(ActionEvent e) {
		Object src = e.getSource();
		if (src == this.m_boxBtn) {
			this.m_actionmode = ControlModes.ACTION_MODE_DRAW_BOX;

		} else if (src == this.m_boxUseCaseBtn) {
			this.m_actionmode = ControlModes.ACTION_MODE_DRAW_BOX_USECASE;

		} else if (src == this.m_boxDbBtn) {
			this.m_actionmode = ControlModes.ACTION_MODE_DRAW_BOX_DB;

		} else if (src == this.m_linkBtn) {
			this.m_actionmode = ControlModes.ACTION_MODE_DRAW_LINK;
			fireControlModeChanged();
		} else if (src == this.m_selectionBtn) {
			this.m_actionmode = ControlModes.ACTION_MODE_SELECT;

		} else if (src == this.m_multipleSelection) {
			this.m_actionmode = ControlModes.ACTION_MODE_MULTIPLE_SELECTION;

		} else if (src == this.m_impactAnalysis) {
			this.m_actionmode = ControlModes.ACTION_MODE_IMPACT_ANALYSIS;
		} else if (src == this.m_moveSelectionBtn) {
			this.m_actionmode = ControlModes.ACTION_MODE_MOVE;

		}
		fireControlModeChanged();
	}

	public void addControlModeListener(ControlModeListener l) {
		this.listenerList.add(ControlModeListener.class, l);
	}

	public void removeControlModeListener(ControlModeListener l) {
		this.listenerList.remove(ControlModeListener.class, l);
	}

	protected void fireControlModeChanged() {
		ControlModeListener[] listeners = (ControlModeListener[]) listenerList.getListeners(ControlModeListener.class);
		EventObject e = new EventObject(this);
		for (int i = listeners.length - 1; i >= 0; i--) {
			listeners[i].modeChanged(this.m_actionmode);
		}
	}

	public int getActionMode() {
		return this.m_actionmode;
	}
}
