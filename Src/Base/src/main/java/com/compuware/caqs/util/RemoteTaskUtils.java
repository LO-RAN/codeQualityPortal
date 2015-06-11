/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.compuware.caqs.util;

import com.compuware.caqs.constants.Constants;
import com.compuware.toolbox.util.logging.LoggerManager;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 *
 * @author cwfr-dzysman
 */
public class RemoteTaskUtils {

    private static org.apache.log4j.Logger logger = LoggerManager.getLogger(Constants.LOG4J_IHM_LOGGER_KEY);

    public static final boolean callRemoteTask(String sUrl) {
        boolean retour = false;
        HttpURLConnection connection = null;
        try {
            URL url = new URL(sUrl);
            connection = (HttpURLConnection) url.openConnection();
            connection.addRequestProperty("User-Agent", "Mozilla/4.76");
            InputStreamReader isr = new InputStreamReader(connection.getInputStream());
            BufferedReader d = new BufferedReader(isr);
            String response = "";
            String inputLine;
            while ((inputLine = d.readLine()) != null) {
                response += inputLine;
            }
            d.close();
            isr.close();
            retour = true;
        } catch (MalformedURLException exc) {
            logger.error("erreur lors de la creation de l'url : ", exc);
            logger.error("url = " + sUrl);
        } catch (IOException exc) {
            logger.error("erreur lors de la connexion a la servlet : ", exc);
            logger.error("url = " + sUrl);
        } catch (Exception e) {
            logger.error("erreur lors de la connexion a la servlet : ", e);
            logger.error("url = " + sUrl);
        } catch (Throwable th) {
            logger.error("erreur lors de la connexion a la servlet : ", th);
            logger.error("url = " + sUrl);
        }
        return retour;
    }
}
