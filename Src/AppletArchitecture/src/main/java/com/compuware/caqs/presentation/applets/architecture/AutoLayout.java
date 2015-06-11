/*
 * Loader.java
 *
 * Created on 29 juillet 2005, 11:26
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */

package com.compuware.caqs.presentation.applets.architecture;

import javax.swing.JDialog;
import javax.swing.JLabel;

import com.compuware.caqs.domain.architecture.serializeddata.ArchitectureModel;
import com.compuware.caqs.presentation.applets.architecture.modellayout.Relaxer;

/**
 * 
 * @author cwfr-fxalbouy
 */
public class AutoLayout implements Runnable {

	private Thread m_me;
	private JDialog m_dialog;
	private ArchitectureModel m_model;
	private JLabel m_text;
	private boolean m_stop = false;
	private boolean m_matrixFirst = true;
	
	public AutoLayout(ArchitectureModel model, JLabel aText, JDialog dialog) {
		this.m_model = model;
		this.m_text = aText;
		this.m_dialog = dialog;
	}

	public void run() {
		int cycles = 600;
		double cyclesd = cycles;
		double id = 0;
		int pctProgess = 0;
		
		if (this.m_matrixFirst) {
			m_model.putNodesInMatrix();			
		}
		
		for (int i = 0; i < cycles; i++) {
			if (!this.m_stop) {
				Relaxer.springRelax();
				id = i;
				pctProgess = (int) (id / cyclesd * 100);
				this.m_text.setText(I18n.getString("autoLayout") + " Progress : " + pctProgess + "%");
				this.m_dialog.repaint();
			} else {
				break;
			}
		}
		this.m_dialog.dispose();
	}

	public void start(boolean matrixFirst) {
		this.m_matrixFirst = matrixFirst;
		this.m_me = new Thread(this);
		this.m_me.start();
	}

	public void stop() {
		this.m_stop = true;
	}

}
