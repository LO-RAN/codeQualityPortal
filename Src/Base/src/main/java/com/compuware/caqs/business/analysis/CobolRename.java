/*
 * CobolRename.java
 *
 * Created on 28 septembre 2004, 11:44
 */

package com.compuware.caqs.business.analysis;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.compuware.caqs.constants.Constants;
import com.compuware.toolbox.io.FileTools;
import com.compuware.toolbox.io.PropertiesReader;
import com.compuware.toolbox.io.filter.RegexpTools;

/**
 *
 * @author  cwfr-lizac
 */
public class CobolRename {
    
    //logger
	private static final Logger logger = com.compuware.toolbox.util.logging.LoggerManager.getLogger(Constants.LOG4J_ANALYSIS_LOGGER_KEY);

    private Properties props = null;
    
    /** Creates a new instance of CobolRename
     *
     * This is used to rename COBOL source files so that file names correspond to PROGRAM-ID.
     * (seems to be required by COBOL McCabe parser)
     *
     * NB: uses properties in cobolproc.properties
     *
     * @param sourceDir
     * @param destDir
     */
    public CobolRename(File sourceDir, File destDir) {
        this.props = PropertiesReader.getProperties("cobolproc.properties", this);
        
        File[] progList = sourceDir.listFiles();
        for(int i=0;i<progList.length;i++){
            
            String programId=null;
            try {
                programId=RegexpTools.getFirstFromFile(progList[i], props.getProperty("copy.program.id.regexp"), 1);
            }
            catch (Exception e) {
                logger.error("Error reading Regexp "+props.getProperty("copy.program.id.regexp")+" from file "+progList[i].getAbsolutePath(), e);
            }
            
            File fileOut = new File(destDir, File.separator+programId+"."+props.getProperty("file.ext"));
            
            if (fileOut.exists()) {
                logger.warn("file "+fileOut.getAbsolutePath()+" already exists."); 
            }
            
            logger.info("Copying file "+progList[i].getAbsolutePath()+" to "+fileOut.getAbsolutePath());
            
            try {
                FileTools.copy(progList[i], fileOut);
            }
            catch (IOException e) {
                logger.error("Error copying file "+progList[i].getAbsolutePath()+" to "+fileOut.getAbsolutePath(), e);
            }
            
        }
    }
    
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        if(args.length<2){
            System.out.println("Usage :");
            System.out.println("  com.compuware.business.analysis.CobolRename  <sourceDir> <destDir>");
            System.out.println("\nExamples :");
            System.out.println("  com.compuware.business.analysis.CobolRename  /tmp/src /tmp/dest");
            System.out.println("  com.compuware.business.analysis.CobolRename  c:\\temp\\src c:\\temp\\dest");

            
            System.exit(0);
        }

        File sourceDir = new File(args[0]);
        File destDir = new File(args[1]);

        new CobolRename(sourceDir, destDir);
        
    }
}
