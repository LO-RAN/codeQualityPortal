package com.compuware.caqs.presentation.applets.scatterplot.draw;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collections;
import java.util.Locale;

import java.util.Vector;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

import com.compuware.caqs.business.chart.resources.Messages;
import com.compuware.caqs.presentation.applets.scatterplot.data.ElementType;
import com.compuware.caqs.presentation.applets.scatterplot.data.Metric;


public class ScatterplotNorthPanel extends JPanel {

	/**
	 * 
	 */
	private static final long 					serialVersionUID 	= -6267577348717014484L;
	private final ScatterplotAppletContainer		parent;
	private final JComboBox						xComboBox;
	private final JComboBox						yComboBox;
	private final JButton							refreshButton;
	private final JTextField						xCenter;
	private final JTextField						yCenter;
	private final JComboBox						elementTypeComboBox;
	private final JButton							refreshMetricsButton;


	public ScatterplotNorthPanel(ScatterplotAppletContainer p, Locale loc) {
		this.parent = p;

		Box b = Box.createHorizontalBox();

		b.add(Box.createHorizontalGlue());

		JPanel elementTypePanel = new JPanel();
		elementTypePanel.setMinimumSize(new Dimension(400, 90));
		TitledBorder elementypeborder = BorderFactory.createTitledBorder(Messages.getString("caqs.scatterplot.elementType", loc));
		elementTypePanel.setBorder(elementypeborder);
		Box elementTypeBox = Box.createVerticalBox();
		this.elementTypeComboBox = new JComboBox(p.getDatasRetriever().getElementTypes());
		this.elementTypeComboBox.setSelectedItem(p.getDatasRetriever().getElementType());
		this.elementTypeComboBox.setPreferredSize(new Dimension(340, 20));
		elementTypeBox.add(this.elementTypeComboBox);
		elementTypePanel.add(elementTypeBox);
		this.refreshMetricsButton = new JButton();
		this.refreshMetricsButton.setToolTipText(Messages.getString("caqs.scatterplot.refreshMetrics", loc));
		this.refreshMetricsButton.addActionListener(new RefreshMetricsButtonActionListener(this));
		try {
			URL icon = new URL(p.getServerAdress()+"images/arrow_refresh.gif");
			this.refreshMetricsButton.setIcon(new ImageIcon(icon));
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		elementTypeBox.add(refreshMetricsButton);
		
		b.add(elementTypePanel);
		b.add(Box.createHorizontalGlue());

		JPanel panelX = new JPanel();
		panelX.setMinimumSize(new Dimension(400, 90));
		TitledBorder xborder = BorderFactory.createTitledBorder(Messages.getString("caqs.scatterplot.abscisse", loc));
		panelX.setBorder(xborder);
		Box xBox = Box.createVerticalBox();
		this.xComboBox = new JComboBox();
		this.xComboBox.setModel(new DefaultComboBoxModel());
		this.xComboBox.setPreferredSize(new Dimension(340, 20));
		xBox.add(this.xComboBox);
		xBox.add(Box.createRigidArea(new Dimension(0, 5)));
		JPanel xCenterPanel = new JPanel();
		xCenterPanel.add(new JLabel(Messages.getString("scatterplot.centerLabel", loc)+" :"));		
		this.xCenter = new JTextField(this.parent.getDatasRetriever().getCenterH());
		this.xCenter.setPreferredSize(new Dimension(300, 20));
		xCenterPanel.add(this.xCenter);
		xBox.add(xCenterPanel);
		panelX.add(xBox);
		b.add(panelX);

		b.add(Box.createHorizontalGlue());

		JPanel panelY = new JPanel();
		panelY.setMinimumSize(new Dimension(400, 90));
		TitledBorder yborder = BorderFactory.createTitledBorder(Messages.getString("caqs.scatterplot.ordonnee", loc));
		panelY.setBorder(yborder);
		Box yBox = Box.createVerticalBox();
		this.yComboBox = new JComboBox();
		this.yComboBox.setModel(new DefaultComboBoxModel());
		this.yComboBox.setPreferredSize(new Dimension(340, 20));
		yBox.add(this.yComboBox);
		yBox.add(Box.createRigidArea(new Dimension(0, 5)));
		JPanel yCenterPanel = new JPanel();
		yCenterPanel.add(new JLabel(Messages.getString("scatterplot.centerLabel", loc)+" :"));		
		this.yCenter = new JTextField(this.parent.getDatasRetriever().getCenterV());
		this.yCenter.setPreferredSize(new Dimension(300, 20));
		yCenterPanel.add(this.yCenter);
		yBox.add(yCenterPanel);
		panelY.add(yBox);
		b.add(panelY);
		
		this.fillMetricsCombos();
		Metric x = p.getDatasRetriever().getMetricX();
		this.xComboBox.setSelectedItem(x);
		Metric y = p.getDatasRetriever().getMetricY();
		this.yComboBox.setSelectedItem(y);


		b.add(Box.createHorizontalGlue());

		JPanel rightPanel = new JPanel();
		this.refreshButton = new JButton();
		this.refreshButton.setToolTipText(Messages.getString("caqs.scatterplot.submit", loc));
		this.refreshButton.addActionListener(new RefreshButtonActionListener(this));
		try {
			URL icon = new URL(p.getServerAdress()+"images/arrow_refresh.gif");
			this.refreshButton.setIcon(new ImageIcon(icon));
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		rightPanel.add(this.refreshButton);
		
		b.add(rightPanel);

		b.add(Box.createHorizontalGlue());

		this.add(b);
	}

	public void refreshPlot() {
		Metric x = (Metric)xComboBox.getSelectedItem();
		Metric y = (Metric)yComboBox.getSelectedItem();
		if(x!=null && y!=null) {
			parent.getDatasRetriever().setMetricX(x);
			parent.getDatasRetriever().setMetricY(y);
			parent.getDatasRetriever().setVariableX(x.getId());
			parent.getDatasRetriever().setVariableY(y.getId());
			parent.getDatasRetriever().setCenterH(this.xCenter.getText());
			parent.getDatasRetriever().setCenterV(this.yCenter.getText());
			parent.getDatasRetriever().refreshDatas();
			parent.getScatterplotPanel().refreshDatas();
			parent.getEastPanel().refresh();
		}
	}
	
	private void fillMetricsCombos() {
        Vector<Metric> metrics = parent.getDatasRetriever().getMetrics();
        Collections.sort(metrics);
		for(Metric m : metrics) {
			((DefaultComboBoxModel)xComboBox.getModel()).addElement(m);
			((DefaultComboBoxModel)yComboBox.getModel()).addElement(m);
		}
	}

	public void refreshMetrics() {
		ElementType et = (ElementType)elementTypeComboBox.getSelectedItem();
		if(et!=null) {
			parent.getDatasRetriever().setElementType(et);
			parent.getDatasRetriever().retrieveMetrics();
			((DefaultComboBoxModel)xComboBox.getModel()).removeAllElements();
			((DefaultComboBoxModel)yComboBox.getModel()).removeAllElements();
			fillMetricsCombos();
			xComboBox.setSelectedIndex(0);
			yComboBox.setSelectedIndex(0);
			this.refreshPlot();
		}
	}
}

class RefreshButtonActionListener implements ActionListener {
	private final ScatterplotNorthPanel parent;

	public RefreshButtonActionListener(ScatterplotNorthPanel p) {
		this.parent = p;
	}

	public void actionPerformed(ActionEvent e) {
		parent.refreshPlot();
	}

}

class RefreshMetricsButtonActionListener implements ActionListener {
	private final ScatterplotNorthPanel parent;

	public RefreshMetricsButtonActionListener(ScatterplotNorthPanel p) {
		this.parent = p;
	}

	public void actionPerformed(ActionEvent e) {
		parent.refreshMetrics();
	}

}
