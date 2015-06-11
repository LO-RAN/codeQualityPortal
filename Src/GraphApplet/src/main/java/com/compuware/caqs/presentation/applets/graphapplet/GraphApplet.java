package com.compuware.caqs.presentation.applets.graphapplet;

import java.awt.Dimension;

import javax.swing.JApplet;

import java.awt.BorderLayout;
import java.util.ResourceBundle;
import javax.swing.JComponent;
import javax.swing.JScrollPane;

public class GraphApplet extends JApplet {

    /**
	 * 
	 */
	private static final long serialVersionUID = -8324031188677578130L;
	
	GraphAppletScene scene;
	
    Dimension preferredSize = new Dimension(800,600);
    Dimension preferredLayoutSize = new Dimension(800,600);

	private String m_servletUrl;
	private String mIdElt;
	private String mIdBline;
	private int nbIn=1;
	private int nbOut=1;
	
	/** Initializes the applet */
    @Override
    public void init() {
        System.setSecurityManager(null);
        initParameters();
    	initComponents();
    }

    private void initParameters() {
        // get the host name and port of the applet's web server
        String servletStr = this.getParameter("SERVLET");
        this.m_servletUrl = this.getCodeBase() + "/" + servletStr;
        this.mIdElt = this.getParameter("ID_ELT");
        this.mIdBline = this.getParameter("ID_BLINE");
        if (this.getParameter("NB_IN") != null) {
        	this.nbIn = Integer.parseInt(this.getParameter("NB_IN"));
        }
        if (this.getParameter("NB_OUT") != null) {
        	this.nbOut = Integer.parseInt(this.getParameter("NB_OUT"));
        }
    }

    /** This method is called from within the init() method to
     * initialize the form.
     */
    private void initComponents() {
        scene = retrieveData(this.mIdElt, nbIn, nbOut);
        JComponent sceneView = scene.getView();
        if (sceneView == null) {
            sceneView = scene.createView();
        }
        JScrollPane panel = new JScrollPane(sceneView);
        panel.getHorizontalScrollBar().setUnitIncrement(32);
        panel.getHorizontalScrollBar().setBlockIncrement(256);
        panel.getVerticalScrollBar().setUnitIncrement(32);
        panel.getVerticalScrollBar().setBlockIncrement(256);
        this.add(panel, BorderLayout.CENTER);

        this.add(scene.getSatelliteView(), BorderLayout.WEST);
    }

    private GraphAppletScene retrieveData(String idElt, int nbIn, int nbOut) {
        ResourceBundle messages = ResourceBundle.getBundle("resources", this.getLocale());
        GraphAppletScene result = new GraphAppletScene(this.m_servletUrl, idElt, this.mIdBline, messages);
        result.retrieveData(idElt, nbIn, nbOut);
		return result;
    }

    @Override
    public void destroy() {
        //RequestProcessor.getDefault().stop();
        System.exit(0);
    }

}
