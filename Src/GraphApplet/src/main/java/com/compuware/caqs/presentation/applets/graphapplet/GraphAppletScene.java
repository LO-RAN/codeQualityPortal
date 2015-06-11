package com.compuware.caqs.presentation.applets.graphapplet;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.netbeans.api.visual.action.ActionFactory;
import org.netbeans.api.visual.graph.layout.GraphLayoutFactory;
import org.netbeans.api.visual.graph.layout.GridGraphLayout;
import org.netbeans.api.visual.layout.LayoutFactory;
import org.netbeans.api.visual.layout.SceneLayout;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * @author cwfr-fdubois
 */
public class GraphAppletScene extends UMLGraphScene {

    private int edgeID = 1;
    Map<String, NodeData> existingNodes = new HashMap<String, NodeData>();
    Map<String, String> existingEdges = new HashMap<String, String>();

    String servletBaseUrl;
    String idMainElt;
    String idBline;

    SceneLayout sceneGraphLayout;

    public GraphAppletScene(String idMainElt, ResourceBundle messages) {
        super(messages);
        this.idMainElt = idMainElt;
    }

    public GraphAppletScene(String servletBaseUrl, String idMainElt, String idBline, ResourceBundle messages) {
        super(messages);
        this.servletBaseUrl = servletBaseUrl;
        this.idMainElt = idMainElt;
        this.idBline = idBline;
    }

    public void init(InputStream in) {
        initializeGraph(in);

        this.getActions().addAction(ActionFactory.createWheelPanAction());
        this.getActions().addAction(ActionFactory.createMouseCenteredZoomAction(1.5));

        this.setPreferredSize(new Dimension(1024,768));
        this.setPreferredBounds(new Rectangle(0, 0, 1024,768));
        GraphLayoutFactory f = new GraphLayoutFactory();
        GridGraphLayout<String, String> graphLayout = new GridGraphLayout<String, String>();
        //GraphLayout<String, String> graphLayout = f.createOrthogonalGraphLayout(this, true);
        //graphLayout.addGraphLayoutListener(new MyListener());

        sceneGraphLayout = LayoutFactory.createSceneGraphLayout(this, graphLayout);
        sceneGraphLayout.invokeLayout();
    }

    private void addEdge(NodeData sourceNode, NodeData targetNode) {
        String id = "edge" + (edgeID ++);
        addEdge(id);
        setEdgeSource(id, sourceNode);
        setEdgeTarget(id, targetNode);
    }

    public void retrieveData(String idElt, int nbIn, int nbOut) {
        java.net.URL servletUrl;
        try {
            servletUrl = new java.net.URL(this.servletBaseUrl + "?id_elt="+idElt+"&id_bline="+this.idBline+"&nbIn="+nbIn+"&nbOut="+nbOut);

            java.net.URLConnection servletConnection = servletUrl.openConnection();
            java.io.InputStream stream = servletConnection.getInputStream();

            // Nécessaire: le flux provenant de la servlet est mal interprété à la création du camenbert.
            // La lecture et le stockage du flux dans un String règle lengthproblème.
            java.io.InputStreamReader reader = new java.io.InputStreamReader(stream);
            java.io.BufferedReader br = new java.io.BufferedReader(reader);

            init(stream);

	        stream.close();
	    }
	    catch(Exception e) {
            e.printStackTrace();
	    }
    }

    public void updateData(String idElt, int nbIn, int nbOut) {
        java.net.URL servletUrl;
        try {
            servletUrl = new java.net.URL(this.servletBaseUrl + "?id_elt="+idElt+"&id_bline="+this.idBline+"&nbIn="+nbIn+"&nbOut="+nbOut);

            java.net.URLConnection servletConnection = servletUrl.openConnection();
            java.io.InputStream stream = servletConnection.getInputStream();

            // Nécessaire: le flux provenant de la servlet est mal interprété à la création du camenbert.
            // La lecture et le stockage du flux dans un String règle lengthproblème.
            java.io.InputStreamReader reader = new java.io.InputStreamReader(stream);
            java.io.BufferedReader br = new java.io.BufferedReader(reader);

            init(stream);

	        stream.close();
	    }
	    catch(Exception e) {
            e.printStackTrace();
	    }
        sceneGraphLayout.invokeLayout();
        this.validate();
    }

    private void initializeGraph(InputStream in) {

    	SAXParserFactory fabrique = SAXParserFactory.newInstance();

    	try {
			SAXParser parseur = fabrique.newSAXParser();

	    	XmlFileHandler gestionnaire = new XmlFileHandler();
	    	parseur.parse(in, gestionnaire);

		}
    	catch(ParserConfigurationException e) {
		}
    	catch(SAXException e) {
		}
    	catch(IOException e) {
		}
    }

    public static void main(String[] args) throws FileNotFoundException {
        GraphAppletScene graph = new GraphAppletScene("20080227122401328984617", ResourceBundle.getBundle("resources", Locale.FRENCH));
        File fichier = new File("D:/Referentiel-Dev/Last/Src/Jung/src/datasets/test2.xml");
        graph.init(new FileInputStream(fichier));
        SceneSupport.show(graph, graph.getSatelliteView());
    }

    private class XmlFileHandler extends DefaultHandler {

        private static final String NODE_TAG = "node";
        private static final String EDGE_TAG = "edge";

        @Override
		public void startDocument() throws SAXException {
		}

        @Override
		public void endDocument() throws SAXException {
		}

        @Override
		public void startElement(String uri,
				String localName,
				String qName,
				Attributes attributes) throws SAXException{
			if(NODE_TAG.equals(qName)) {
                if(!existingNodes.containsKey(attributes.getValue("id"))) {
                    NodeData n = new NodeData();
                    n.setId(attributes.getValue("id"));
                    n.setDesc(attributes.getValue("desc"));
                    n.setLib(attributes.getValue("name"));
                    if(idMainElt.equals(n.getId())) {
                        n.setMainNode(true);
                    }
                    addNode(n);
                    existingNodes.put(n.getId(), n);
                }
			}
			else if(EDGE_TAG.equals(qName)) {
                if(existingNodes.get(attributes.getValue("source")) != null
                    && existingNodes.get(attributes.getValue("target")) != null
                    && existingEdges.get(attributes.getValue("source") + '-' + attributes.getValue("target")) == null)
                {
                    addEdge(existingNodes.get(attributes.getValue("source")), existingNodes.get(attributes.getValue("target")));
                    existingEdges.put(attributes.getValue("source") + '-' + attributes.getValue("target"), "Done");
                }
			}
		}

        @Override
		public void endElement(String uri,
				String localName,
				String qName) throws SAXException{
		}

        @Override
		public void characters(char[] text, int start, int length)
	     throws SAXException {
	    }

	};

}
