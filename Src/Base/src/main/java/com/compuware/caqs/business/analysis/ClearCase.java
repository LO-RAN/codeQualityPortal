package com.compuware.caqs.business.analysis;

import java.io.CharArrayWriter;
import java.io.InputStream;
import java.util.Properties;
import java.util.StringTokenizer;

import org.apache.log4j.Logger;

import com.compuware.caqs.constants.Constants;
import com.compuware.caqs.util.CaqsConfigUtil;

public class ClearCase {

    // declaration du logger
	private static final Logger logger = com.compuware.toolbox.util.logging.LoggerManager.getLogger("ClearCase");
    
    protected String stream="";
    protected String lastLine="";
    protected String scriptPath="";
    
    public ClearCase( String stream) {
        this.stream = stream;
        
        Properties dbProps = CaqsConfigUtil.getCaqsConfigProperties(this);

        this.scriptPath = CaqsConfigUtil.getLocalizedCaqsProperty(dbProps, Constants.CLEAR_TOOL_SCRIPT_PATH_KEY);
        if (this.scriptPath == null || this.scriptPath.length() < 1) {
            logger.error("ClearCase: no property found in configuration file for script path: key=cleartoolScriptPath");
        }

        this.listBaseLine();
    }
    
    /**
     * commande Clearcase:
     * cleartool lsbl -stream Integration_14148@\\pvob_cct -short
     */
    private boolean listBaseLine() {
        Process p = null;
        boolean cmdOk = false;
        
        String cmd = "perl "+this.scriptPath+ " "+  this.stream;
        
        ClearCase.logger.info("SYSTEM COMMAND FOR LAST CC BASELINE : " + cmd );
        InputStream i = null;
        try {
            p = (Runtime.getRuntime()).exec(cmd);
            
            i= p.getInputStream();
            
            int b;
            CharArrayWriter caw = new CharArrayWriter();
            while((b= i.read()) != -1) {
                caw.write(b);                
            }
                        
            StringTokenizer st = new StringTokenizer(caw.toString(),"\n");
            String token = "";
            while(st.hasMoreTokens()){
                token = st.nextToken();
            }
            this.lastLine = token;
            ClearCase.logger.info("last line is : " + this.lastLine);
            
            cmdOk = (p.waitFor() == 0) ? true : false;
            
        }
        catch (Exception ex) {
            logger.error(ex);
        }
        finally{
            try{
                i.close();
            }
            catch(Exception e){
            	ClearCase.logger.error("Error listing baselines", e);
            }
        }
        
        return cmdOk;
    }
    
    public String getLastBaseLine(){
        //Bouchon pour simuler l'interogation clearcase
        return this.lastLine;
    }
}