/**
 * Titre : <p>
 * Description : <p>
 * Copyright : Copyright (c) François-Xavier ALBOUY<p>
 * Société : Software & Process<p>
 * @author François-Xavier ALBOUY
 * @version 1.0
 */
package com.compuware.caqs.business.load.reader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

import org.apache.log4j.Logger;

import com.compuware.caqs.constants.Constants;

public class SpreadSheetReportReader {

    // déclaration du logger
    static protected Logger logger = com.compuware.toolbox.util.logging.LoggerManager.getLogger(Constants.LOG4J_ANALYSIS_LOGGER_KEY);
    
    protected List m_reportData = new ArrayList();
    
    public SpreadSheetReportReader() {
    }
    
    protected List<Object> getDataLine(StringTokenizer st){
        List<Object> dataLine = new ArrayList<Object>();
        int count = st.countTokens();
        for (int index = 0 ; index < count ; index++){
            String token = st.nextToken();
            if (token.compareTo("")!=0){
                if (token.compareTo("err")==0){
                    dataLine.add(token);
                }
                else{
                    try{
                        Double d = new Double(Double.parseDouble(token.trim()));
                        dataLine.add(d);
                    }
                    catch (NumberFormatException e){
                        dataLine.add(token.trim());
                    }
                }
            }
        }
        return dataLine;
    }
    
    public void loadFileData(String fileName,String delimitorString){
        File fichier= new File(fileName);
        if (!fichier.exists()){
            SpreadSheetReportReader.logger.error("File: "+fileName+ "does not exist");
            SpreadSheetReportReader.logger.error("File absolute path: "+fichier.getAbsolutePath());
        }
        else {
            try {
                //flux en lecture sur un tampon
                BufferedReader fluxLu= new BufferedReader(new FileReader(fichier));
                
                try {
                    String oneFileLine;
                    
                    while ( (oneFileLine = fluxLu.readLine()) != null) {
                        StringTokenizer st = new StringTokenizer(oneFileLine,delimitorString);
                        int number=st.countTokens();
                        if (number!=0){
                            this.m_reportData.add(getDataLine(st));
                        }
                    }
                }
                catch (IOException e){                    
                    SpreadSheetReportReader.logger.error("Error reading file: "+fileName, e);
                }
                finally {fluxLu.close();}
            }
            catch (IOException e){
                SpreadSheetReportReader.logger.error("Error reading file: "+fileName, e);
            }
        }
    }
    
    protected Object[][] vectorToObjectArray(List vector){
        //go through the vector to find the max number of columns
        int columnMax=0;
        Iterator it = vector.iterator();
        while (it.hasNext()) {
            List line = new ArrayList();
            line = (List) it.next();
            columnMax = Math.max(line.size(), columnMax);
        }
        
        int lineMax = vector.size();
        
        //create the data Array
        Object[][] data= new Object[lineMax][columnMax];
        
        //fill the data array.
        for (int i =0 ; i < lineMax; i++){
            List line = (List) vector.get(i);
            int j = 0;
            for (j=0 ; j <  (line.size()) ; j++){
                try{
                    data[i][j] = line.get(j);
                }
                catch(Exception e){
                    data[i][j] = "n/a";
                }
            }// end for
        }//data
        return data;
    }
    
    public Object[][] getData(){
        return this.vectorToObjectArray(this.m_reportData);
    }    
    
}
