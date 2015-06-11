/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.compuware.caqs.graphcreator;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author cwfr-fdubois
 */
public class GraphCreator {

    public Set<Link> read(File input) throws FileNotFoundException, IOException {
        Set<Link> result = new LinkedHashSet<Link>();
        FileReader fr = new FileReader(input);
        BufferedReader br = new BufferedReader(fr);
        try {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.length() >= 545) {
                    Link link = new Link();
                    link.setSource(line.substring(159, 287).trim());
                    link.setTarget(line.substring(416, 544).trim());
                    result.add(link);
                }
            }
        }
        finally {
            fr.close();
        }
        return result;
    }

    public void generateGraphml(File output, Set<Link> links) throws IOException {
        FileWriter fw = new FileWriter(output);
        PrintWriter out = new PrintWriter(fw);
        out.write("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>\n");
		out.write("<graphml xmlns=\"http://graphml.graphdrawing.org/xmlns/graphml\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:y=\"http://www.yworks.com/xml/graphml\" xsi:schemaLocation=\"http://graphml.graphdrawing.org/xmlns/graphml http://www.yworks.com/xml/schema/graphml/1.0/ygraphml.xsd\">\n");
        out.write("<key for=\"node\" id=\"name\" yfiles.type=\"nodegraphics\"/>\n");
        out.write("<key for=\"edge\" id=\"estyle\" yfiles.type=\"edgegraphics\"/>\n");
        out.write("<graph edgedefault=\"directed\">\n");
        Map<String, String> existingNodes = new HashMap<String, String>();
        for (Link l: links) {
            if (!existingNodes.containsKey(l.getSource())) {
                out.append("<node id='").append(l.getSource()).append("'><data key='name'><y:ShapeNode><y:NodeLabel>").append(l.getSource()).append("</y:NodeLabel></y:ShapeNode></data></node>\n");
                existingNodes.put(l.getSource(), l.getSource());
            }
            if (!existingNodes.containsKey(l.getTarget())) {
                out.append("<node id='").append(l.getTarget()).append("'><data key='name'><y:ShapeNode><y:NodeLabel>").append(l.getTarget()).append("</y:NodeLabel></y:ShapeNode></data></node>\n");
                existingNodes.put(l.getTarget(), l.getTarget());
            }
            out.append("<edge id='").append(l.getSource()).append('-').append(l.getTarget()).append("' source='").append(l.getSource()).append("' target='").append(l.getTarget()).append("'><data key=\"estyle\"><y:PolyLineEdge><y:Arrows source=\"none\" target=\"standard\"/></y:PolyLineEdge></data></edge>\n");
        }
        out.write("</graph>\n");
        out.write("</graphml>\n");
        out.flush();
        out.close();
    }

    public static void main(String[] args) throws FileNotFoundException, IOException {
        GraphCreator creator = new GraphCreator();
        Set<Link> links = creator.read(new File(args[0]));
        creator.generateGraphml(new File(args[1]), links);
    }

}
