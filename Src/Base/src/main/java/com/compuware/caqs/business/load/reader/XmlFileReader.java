/**
 * Titre : <p>
 * Description : <p>
 * Copyright : Copyright (c) FXA<p>
 * Soci�t� : <p>
 * @author FXA
 * @version 1.0
 */
package com.compuware.caqs.business.load.reader;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.compuware.caqs.constants.Constants;
import org.apache.xerces.parsers.DOMParser;

public class XmlFileReader{

    // d�claration du logger
    static protected Logger logger = com.compuware.toolbox.util.logging.LoggerManager.getLogger(Constants.LOG4J_ANALYSIS_LOGGER_KEY);
    
    protected String m_file;
    protected Document m_xmlDocument;
    protected String m_path;
    
    protected String m_errorString="";
    
    public XmlFileReader(String file) {        
        this.m_file = file;        
    }
    
    public boolean execute(){
        boolean ok = true;
        
        XmlFileReader.logger.info("Start : reading XML file");
        DOMParser monParser= new DOMParser();
        try{
            monParser.parse( this.m_file );
            this.m_xmlDocument = monParser.getDocument();
            ok = this.treatTree();
        }
        catch (Exception e){
            ok = false;
            XmlFileReader.logger.error(e.toString());
        }
        
        XmlFileReader.logger.info("End : reading XML file");
        return ok;
    }
    
    protected boolean treatTree(){
        boolean ok = true;
        NodeList error = this.m_xmlDocument.getElementsByTagName("Error");
        
        if(error.getLength()>0){
            System.out.println("length:"+error.getLength());
            NodeList message = this.m_xmlDocument.getElementsByTagName("Message");
            for (int i = 0 ; i < message.getLength(); i++){
                Node mes = message.item(i);
                Node texte = mes.getFirstChild();                
                try{
                    this.m_errorString += texte.getNodeValue();
                }
                catch(Exception e){
                    XmlFileReader.logger.error(e.toString());
                }
            }
            ok = false;
            System.out.println("ERROR IN XML FILE");
        }
        else{
            NodeList user = this.m_xmlDocument.getElementsByTagName("User");
            for (int i = 0 ; i < user.getLength() ; i ++){
                Node node = user.item(i);
                XmlFileReader.logger.info("node : " + node.getNodeName());
                if (node.getNodeName().compareTo("User")==0){
                    NamedNodeMap nnm = node.getAttributes();
                    //nnm.getNamedItem("Name").getNodeValue() ;
                    this.m_path = nnm.getNamedItem("Path").getNodeValue() ;
                    XmlFileReader.logger.info("Path is : " + this.m_path);
                }
            }
        }
        return ok;
    }
    
    public String getPath(){
        return this.m_path;
    }
    
    public String getErrorString(){
        return this.m_errorString;
    }
}