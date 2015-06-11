/*
 * Architecture.java
 * Created on 28 octobre 2002, 11:10
 * @author  fxa
 * @version
 */

package com.compuware.caqs.presentation.architecture.servlets;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.compuware.caqs.business.architecture.ArchitectureCreator;
import com.compuware.caqs.domain.architecture.serializeddata.ArchitectureModel;
import com.compuware.caqs.exception.CaqsException;
import com.compuware.caqs.security.auth.Users;
import com.compuware.toolbox.util.logging.LoggerManager;

public class ArchitectureServlet extends HttpServlet{

    /**
	 * 
	 */
	private static final long serialVersionUID = -7525357876525792073L;
	
	protected static org.apache.log4j.Logger logger = LoggerManager.getLogger("Architecture");
    
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        ArchitectureServlet.logger.info("Servlet Architecture initialized");
    }
    
    public void doGet(HttpServletRequest request,HttpServletResponse response)throws ServletException, IOException{
        String value = null;
        String test = null;
        String baseLineId = null;
        String projectId = null;
        
        // read in parameter value for dbDriver
        if ( (value = request.getParameter("test")) != null ) {
            test = value;
            ArchitectureServlet.logger.info("test is : " + test);
        }
        if ( (value = request.getParameter("projectid")) != null ) {
            projectId = value;
            ArchitectureServlet.logger.info("ProjectId is : " + projectId);
        }
        if ( (value = request.getParameter("baselineid")) != null ) {
            baseLineId = value;
            ArchitectureServlet.logger.info("baselineid is : " + baseLineId);
        }
        try {
            
            GZIPOutputStream zout = new GZIPOutputStream(response.getOutputStream());
            ObjectOutputStream outputToApplet = new ObjectOutputStream(zout);
             
            ArchitectureServlet.logger.info("Start : getting infop from dataBase");
            ArchitectureCreator ac = new ArchitectureCreator(baseLineId,projectId);
            ac.getFromDB(test);            
            
            ArchitectureServlet.logger.info("End : getting infop from dataBase");     
            
            Users user = (Users) request.getSession().getAttribute("user");
            if(user.access("EDIT_ARCHITECTURE")){
            	ArchitectureModel.getInstance().setIsModifiable( true );
            }
            else {
            	ArchitectureModel.getInstance().setIsModifiable( false );
            }
            ArchitectureServlet.logger.info("Start : Sending Model to applet...");
            outputToApplet.writeObject(ArchitectureModel.getInstance());
            outputToApplet.flush();
            outputToApplet.close();
            ArchitectureServlet.logger.info("End : Sending Model to applet.");
        }
        catch (Exception e) {    
            logger.error(e);
        }
    }
    
    public void doPost(HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException {
        ObjectInputStream inputFromApplet = null;
        String baseLineId = null;
        String projectId = null;
        try {
            // get an input stream from the applet
            GZIPInputStream zin = new GZIPInputStream(request.getInputStream());
            inputFromApplet = new ObjectInputStream(zin);
            ArchitectureServlet.logger.info("Connected");
            
            // read the serialized data from applet
            ArchitectureServlet.logger.info("Reading data...");
            ArchitectureModel.setInstance((ArchitectureModel) inputFromApplet.readObject());
            inputFromApplet.close();
            ArchitectureServlet.logger.info("Finished reading.");
                                    
            baseLineId = ArchitectureModel.getInstance().getBaseLineID();
            projectId = ArchitectureModel.getInstance().getEaId();
            
            ArchitectureServlet.logger.info("Start Architecture Creator");
            ArchitectureCreator ac = new ArchitectureCreator(baseLineId,projectId);
            ArchitectureServlet.logger.info("set");            
            ArchitectureServlet.logger.info("insert");
            ac.insertIntoDB();
        }
        catch (ClassNotFoundException e) {
            ArchitectureServlet.logger.error(e.toString());
        }
        catch (CaqsException e) {
            ArchitectureServlet.logger.error(e.toString());
        }
        
        
        
        try{
            ArchitectureServlet.logger.info("get");
            ArchitectureCreator ac = new ArchitectureCreator(baseLineId, projectId);
            ac.getFromDB(null);
                   
            ArchitectureServlet.logger.info("End Architecture Creator");
            
            ArchitectureServlet.logger.info("Start : Sending back the data");
            ObjectOutputStream outToApplet = new ObjectOutputStream(response.getOutputStream());
            response.setContentType("application/octet-stream");
            outToApplet.writeObject(ArchitectureModel.getInstance());
            outToApplet.flush();
            outToApplet.close();
            ArchitectureServlet.logger.info("End : Sending back the data.");
        }
        catch (IOException e) {
            ArchitectureServlet.logger.error(e.toString());
        }
    }
    
    public String getServletInfo() {
        return "Architecture Servlet";
    }
    
}
