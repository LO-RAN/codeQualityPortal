package com.compuware.caqs.presentation.applets.scatterplot.draw;

import java.io.IOException;

import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;

public class ScatterplotDisplaySourceDialog extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1169496119541833326L;

	public ScatterplotDisplaySourceDialog(String url) {
		super();
		this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		try {
			JEditorPane htmlPane = new JEditorPane(url);
			htmlPane.setEditable(false);
			this.setSize(1024, 768);
			JScrollPane p = new JScrollPane(htmlPane);
			p = new JScrollPane(htmlPane);
			p.setSize(1000, 760);
			this.add(p);
		} catch(IOException e) {
			JOptionPane.showMessageDialog(this, "Error while opening the source code", "", JOptionPane.ERROR_MESSAGE);
		}
	}

}
