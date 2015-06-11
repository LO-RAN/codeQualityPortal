import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.log4j.Logger;

/**
 * 
 */

/**
 * @author cwfr-fdubois
 *
 */
public class UQOSCMD {

	private static Logger logger = Logger.getLogger("TaskServer");

	public String COMMANDOUT(String IN) {
		String result = "";
		try {
            Process p = Runtime.getRuntime().exec(IN);
            BufferedReader bf = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String inputLine;
            while ((inputLine = bf.readLine()) != null) {
            	result += inputLine;
            }
            bf.close();
            
            try {
                if (p.waitFor() != 0) {
                	logger.info("Fin");
                	logger.info("Process end with exit value : " + p.exitValue() );
                }
            }
            catch (InterruptedException e) {
            	result = "Error executing command: " + IN;
    			result += '\n' + e.toString();
            }            
		}
		catch (IOException e) {
			result = "Error executing command: " + IN;
			result += '\n' + e.toString();
		}
		return result;
	}
	
}
