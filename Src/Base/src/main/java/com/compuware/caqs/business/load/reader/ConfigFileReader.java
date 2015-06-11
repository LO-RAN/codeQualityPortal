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
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

import org.apache.log4j.Logger;

import com.compuware.caqs.constants.Constants;

public class ConfigFileReader {

    // déclaration du logger
    static protected Logger logger = com.compuware.toolbox.util.logging.LoggerManager.getLogger(Constants.LOG4J_ANALYSIS_LOGGER_KEY);
    
   protected Map m_fileLines = new HashMap();
   protected String m_fileName;
        
    public ConfigFileReader(String fileName) {
        this.m_fileName = fileName;
        this.readConfigFile();        
    }
    
    public String getItem(String key){
        return (String) this.m_fileLines.get(key);
    }
    
    private void readConfigFile(){        
        ConfigFileReader.logger.info("Config file" + this.m_fileName);
        File fichier= new File(this.m_fileName);
        if (!fichier.exists()){
            ConfigFileReader.logger.error("Fichier de Configuration non trouvé.");
        }
        else{
            try {
                //flux en lecture sur un tampon
                BufferedReader fluxLu= new BufferedReader(new FileReader(fichier));
                
                try {
                    String oneFileLine;
                    
                    while ( (oneFileLine = fluxLu.readLine()) != null){
                        try{
                            StringTokenizer st = new StringTokenizer(oneFileLine,"=");
                            String tok1 = st.nextToken();
                            String tok2 = st.nextToken();
                            ConfigFileReader.logger.info("Configuration : "+tok1 + " " + tok2);
                            this.m_fileLines.put(tok1,tok2);
                        }
                        catch(Exception e){
                            ConfigFileReader.logger.error(e.toString());
                        }
                    }
                    
                }
                catch (IOException e){
                    ConfigFileReader.logger.error(e.toString());
                }
                finally {fluxLu.close();}
            }
            catch (IOException e){
                ConfigFileReader.logger.error(e.toString());
            }            
        }
    }
}