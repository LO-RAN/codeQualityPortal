package com.compuware.carscode.plugin.deventreprise.util.launchcollector;

import com.compuware.carscode.plugin.deventreprise.util.ReturnCodes;
import com.compuware.toolbox.io.StreamType;
import java.io.File;
import java.io.IOException;

import org.apache.log4j.Logger;

public class LaunchCollectorCommandExec {

    public static ReturnCodes exec(String command, String[] env, File dir, Logger logger) {
        ReturnCodes retour = ReturnCodes.NO_ERROR;
        java.lang.Process p = null;
        try {
            p = Runtime.getRuntime().exec(command, env, dir);
            StreamGobbler out = new StreamGobbler(p.getInputStream());
            StreamGobbler err = new StreamGobbler(p.getErrorStream());
            out.start();
            err.start();

            try {
                if (p.waitFor() != 0) {
                    logger.info("Fin");
                    logger.info("Process end with exit value : " + p.exitValue());
                }
            } catch (InterruptedException e) {
                retour = ReturnCodes.GENERIC_ERROR;
            }
            int result = p.exitValue();
            if (result != 0) {
                retour = ReturnCodes.GENERIC_ERROR;
            } else {
                ReturnCodes r = out.getDetectedError();
                if (!r.equals(ReturnCodes.NO_ERROR)) {
                    retour = r;
                } else {
                    r = err.getDetectedError();
                    if (!r.equals(ReturnCodes.NO_ERROR)) {
                        retour = r;
                    }
                }
            }
        } catch (IOException e) {
            logger.error("Error executing process: ", e);
            retour = ReturnCodes.GENERIC_ERROR;
        }
        return retour;
    }
}
