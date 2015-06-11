/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.compuware.caqs.presentation.util;

import com.compuware.caqs.constants.Constants;
import com.compuware.caqs.domain.dataschemas.ElementBean;
import com.compuware.caqs.domain.dataschemas.ElementType;
import com.compuware.caqs.service.ElementSvc;
import com.compuware.caqs.util.CaqsConfigUtil;
import com.compuware.toolbox.io.FileTools;
import com.compuware.toolbox.util.logging.LoggerManager;
import java.io.File;
import java.io.IOException;
import java.util.Properties;

/**
 *
 * @author cwfr-dzysman
 */
public class CaqsUtils {

    private static org.apache.log4j.Logger logger = LoggerManager.getLogger(Constants.LOG4J_IHM_LOGGER_KEY);

    public static void cleanTemporaryDirectory() {
        Properties dynProp = CaqsConfigUtil.getCaqsGlobalConfigProperties();
        String unifaceviewImageDirectory = dynProp.getProperty(Constants.UNIFACEVIEW_IMAGE_TMP_DIR_KEY);
        File uvDir = new File(unifaceviewImageDirectory);
        if (uvDir.exists()) {
            try {
                FileTools.rdelete(uvDir);
            } catch (IOException e) {
                logger.error("Error cleaning unifaceview image directory", e);
            }
        }
    }

    /**
     * indique si un element est supprimable ou non, dans le contexte de la corbeille
     * @param elt l'element a tester
     * @return <code>true</code> s'il est supprimable, <code>false</code> sinon.
     */
    public static boolean recycleBinElementIsDeletable(ElementBean elt) {
        boolean deletable = true;
        if(ElementType.EA.equals(elt.getTypeElt())
                || ElementType.SSP.equals(elt.getTypeElt())) {
            //s'il y a au moins une baseline, on ne peut pas supprimer
            deletable = !ElementSvc.getInstance().elementHasBaselines(elt.getId());
        }
        return deletable;
    }
}
