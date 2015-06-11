package com.compuware.caqs.presentation.applets.architecture.panels.graphpanels;

import java.awt.Point;
import java.awt.Rectangle;

import java.awt.geom.AffineTransform;


import com.compuware.caqs.domain.architecture.serializeddata.ArchitectureModel;




public class GraphPanelMagnifier extends GraphPanel {
	
	
	public GraphPanelMagnifier(String title, boolean permanentFit) {
		super(title, permanentFit);
		// TODO Auto-generated constructor stub
	}
	protected void addSpecificMouseListeners(){
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

			at.setToTranslation(scalex * (-ptMouse.x) + pt.x / 2, scaley
					* (-ptMouse.y) + pt.y / 2);
			// at.setToTranslation(at.getTranslateX()+(ptMouse.x),
			// at.getTranslateY()+ (ptMouse.y ));
			at.scale(scalex, scaley);
			// System.out.println(at);
			this.m_offgraphics.setTransform(at);
		}
		this.fireViewChanged();		
	}

}
