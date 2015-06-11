/*
 * LazyDrawer.java
 *
 * Created on 26 juillet 2005, 10:13
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */

package com.compuware.caqs.presentation.applets.architecture.panels.drawers;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.Enumeration;
import java.util.EventObject;
import java.util.Vector;


import com.compuware.caqs.domain.architecture.serializeddata.ArchitectureModel;
import com.compuware.caqs.domain.architecture.serializeddata.DrawnObject;
import com.compuware.caqs.domain.architecture.serializeddata.PreferencesGraphical;
import com.compuware.caqs.presentation.applets.architecture.modellayout.Layouter;
import com.compuware.caqs.presentation.applets.architecture.panels.control.ControlGraphics;
import com.compuware.caqs.presentation.applets.architecture.panels.graphpanels.GraphPanel;
import com.compuware.caqs.presentation.applets.architecture.panels.graphpanels.GraphPanelBird;
/**
 *
 * @author cwfr-fxalbouy
 */
public class DrawerLazy extends DrawerAbstract{
    private Layouter m_layouter;

    
    /** Creates a new instance of Drawer */
    public DrawerLazy() {    	
    }
    
    
    public void draw() {
    	
        //then draw for all graphPanels
        Enumeration graphPanelsEnum = this.m_graphPanels.elements();
        
        while(graphPanelsEnum.hasMoreElements()){
        	
        	GraphPanel currGraphPanel = (GraphPanel)graphPanelsEnum.nextElement();
        	Rectangle rec = currGraphPanel.getVisibleRectangle();
        	if(rec.width>10){
	        	Point topLeft = new Point((int) rec.getMinX(),(int)rec.getMinY());
	        	Point bottomRight = new Point((int) rec.getMaxX(),(int)rec.getMaxY());
	        	
	        	boolean showRealLinks = false;
				boolean showArchiLinks = false;
				if ( m_controlgraphicsmode == ControlGraphics.DISPLAY_REALLINKS_AND_ARCHILINKS) {
					 showRealLinks = true;
					 showArchiLinks = true;
				} else if ( m_controlgraphicsmode == ControlGraphics.DISPLAY_REALLINKS_ONLY) {
					 showRealLinks = true;
					 showArchiLinks = false;
				} else if ( m_controlgraphicsmode == ControlGraphics.DISPLAY_ARCHILINKS_ONLY) {
					 showRealLinks = false;
					 showArchiLinks = true;
				}
							
				
	        	if(currGraphPanel instanceof GraphPanelBird){
		        	Vector<DrawnObject> info  = ArchitectureModel.getInstance().sortDrawableObjectsBeforeDrawings(topLeft,bottomRight,PreferencesGraphical.getInstance().getShowLinksInBirdView(),showRealLinks,showArchiLinks);
		        	currGraphPanel.setDrawnObjects(info);
	        	}
	        	else{
	        		Vector<DrawnObject> info  = ArchitectureModel.getInstance().sortDrawableObjectsBeforeDrawings(topLeft,bottomRight,true,showRealLinks,showArchiLinks);
		        	currGraphPanel.setDrawnObjects(info);
	        	}
	        	currGraphPanel.repaint();
	            
        	}
        	else{
        		//System.out.println("Wont paint this graph pane");
        	}
        }
    }
    
    public void start(){draw();};
    public void stop(){};
    
    
}
