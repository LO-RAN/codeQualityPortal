package com.compuware.caqs.webservices.impl;

import com.compuware.caqs.constants.Constants;
import com.compuware.caqs.domain.dataschemas.BaselineBean;
import com.compuware.caqs.domain.dataschemas.DialecteBean;
import com.compuware.caqs.domain.dataschemas.ElementBean;
import com.compuware.caqs.i18n.LangageUtil;
import com.compuware.caqs.service.BaselineSvc;
import com.compuware.caqs.service.ElementSvc;
import com.compuware.caqs.util.CaqsConfigUtil;
import com.compuware.toolbox.io.FileTools;
import com.compuware.toolbox.util.logging.LoggerManager;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Properties;
import javax.jws.WebService;

/**
 *
 * @author cwfr-dzysman
 */
@WebService(//endpointInterface = "com.compuware.caqs.webservices.ProjectInfosWS",
serviceName = "ProjectInfosWS")
public class ProjectInfosWSImpl /*implements ProjectInfosWS */ {
    private static org.apache.log4j.Logger logger = LoggerManager.getLogger(Constants.LOG4J_WEBSERVICES_LOGGER_KEY);

    public byte[] retrieveAnalyzedSource(String eaId, String eltDesc) {
        byte[] retour = null;
        File srcFile = null;
        ElementBean ea = ElementSvc.getInstance().retrieveElementById(eaId);
        ElementBean elt = ElementSvc.getInstance().retrieveElementByDesc(eltDesc, eaId);
        if (ea != null && elt != null) {
            Properties dynProp = CaqsConfigUtil.getCaqsGlobalConfigProperties();
            String basePath = dynProp.getProperty(Constants.WEB_DATA_DIRECTORY_KEY);
            DialecteBean dialecte = ea.getDialecte();
            List<String> paths = LangageUtil.getFileNameFromDescription(dialecte.getLangage().getId(), elt.getTypeElt(), eltDesc, dynProp);
            BaselineBean bb = BaselineSvc.getInstance().getLastBaseline(ea);
            for (String path : paths) {
                String pathWithBaseline = ElementBean.getHtmlSrcDir(bb.getId(), ea, ea.getProject(), basePath);
                String absolutePath = FileTools.concatPath(pathWithBaseline, path);
                srcFile = new File(absolutePath);
                if(srcFile.exists()) {
                    break;
                }
                srcFile = null;
            }
        }
        if(srcFile != null) {
            try {
            retour = FileTools.getBytesFromFile(srcFile);
            } catch(IOException exc) {
                logger.error("ProjectInfosWSImpl : "+exc.getMessage());
            }
        }
        return retour;
    }
}
