/*
 * CsvFileWriter.java
 *
 * Created on August 10, 2004, 4:22 PM
 */

package com.compuware.caqs.business.load.writer;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import org.apache.log4j.Logger;

import com.compuware.caqs.constants.Constants;
/**
 *
 * @author  cwfr-fxalbouy
 */
public class CsvFileWriter {
    
    //logger
    static protected Logger logger = com.compuware.toolbox.util.logging.LoggerManager.getLogger(Constants.LOG4J_ANALYSIS_LOGGER_KEY);

    /** Creates a new instance of CsvFileWriter */
    public CsvFileWriter(Object[][] data, String delimitor,String filePath) {
        try{
            
            BufferedWriter fluxEcrit= new BufferedWriter(new FileWriter(filePath));
            for (int i = 0 ; i < data.length ; i++){
                Object[] line = data[i];
                for (int j = 0 ; j < line.length ; j++){
                    if(j!=(line.length-1)){
                        fluxEcrit.write(line[j]+delimitor);
                    }
                    else{
                        fluxEcrit.write(""+line[j]);
                    }
                }
                fluxEcrit.write("\n");
            }
            fluxEcrit.close();
        }
        catch(IOException e){
            logger.error("CsvFileWriter", e);
        }
    }
    
}
