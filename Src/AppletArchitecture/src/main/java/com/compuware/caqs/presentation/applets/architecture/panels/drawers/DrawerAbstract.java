/*
 * DrawerInterface.java
 *
 * Created on 26 juillet 2005, 10:15
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */

package com.compuware.caqs.presentation.applets.architecture.panels.drawers;

import java.util.Enumeration;
import java.util.EventObject;
import java.util.Vector;

import com.compuware.caqs.domain.architecture.serializeddata.ArchitectureModel;
import com.compuware.caqs.domain.architecture.serializeddata.ArchitectureModelListener;
import com.compuware.caqs.domain.architecture.serializeddata.DrawnObject;
import com.compuware.caqs.presentation.applets.architecture.panels.control.ControlGraphics;
import com.compuware.caqs.presentation.applets.architecture.panels.control.ControlGraphicsListener;
import com.compuware.caqs.presentation.applets.architecture.panels.graphpanels.GraphPanel;
import com.compuware.caqs.presentation.applets.architecture.panels.graphpanels.ViewListener;
/**
 *
 * @author cwfr-fxalbouy
 */
abstract public class DrawerAbstract  implements ArchitectureModelListener, ViewListener, ControlGraphicsListener  {
    protected Vector<GraphPanel> m_graphPanels = new Vector<GraphPanel>();
    protected int m_controlgraphicsmode = ControlGraphics.DISPLAY_REALLINKS_AND_ARCHILINKS;
    
    abstract public void draw();
    
    public void setArchitectureModel(){
        //this.m_model = model;
    	ArchitectureModel.getInstance().addListener(this);
    }
    
    public void graphicsControlsChanged(int mode){
    	this.m_controlgraphicsmode = mode;
    	//System.out.println("Mode changed to " + mode);
    	draw();
    }
    
    public void viewChanged(EventObject e) {
		draw();		
	}
    
    public void architectureModelChanged(){
       draw();
    }
    
    public void newSelectedElement(DrawnObject obj){
    	
    }
    public void multipleSelectionDone(Vector multipleSelectedNodes){
    	
    }
    
    public void selectedItemDeleted(){
    	
    }
    
    public void addGraphPanel(GraphPanel panel){       
        this.m_graphPanels.add(panel);
    }
    
    public void start(){};
    
}
