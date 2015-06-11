package com.compuware.caqs.presentation.consult.actions;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Locale;
import java.util.Properties;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.compuware.caqs.constants.Constants;
import com.compuware.caqs.dao.factory.DaoFactory;
import com.compuware.caqs.dao.interfaces.ElementDao;
import com.compuware.caqs.domain.dataschemas.ElementBean;
import com.compuware.caqs.util.CaqsConfigUtil;
import com.compuware.toolbox.io.FileTools;
import com.compuware.toolbox.util.logging.LoggerManager;

/**
 * Created by IntelliJ IDEA.
 * User: cwfr-fdubois
 * Date: 30 nov. 2005
 * Time: 10:31:03
 * To change this template use File | Settings | File Templates.
 */
public class RetrieveADPFileAction extends ElementSelectedActionAbstract {

    private static org.apache.log4j.Logger logger = LoggerManager.getLogger(Constants.LOG4J_IHM_LOGGER_KEY);

    // --------------------------------------------------------- Public Methods


    public ActionForward doExecute(ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response)
            throws IOException, ServletException {

        ActionForward forward = null;
        HttpSession session = request.getSession();

        String idElt = request.getParameter("id_elt");
        String idCrit = request.getParameter("id_crit");

        LoggerManager.pushContexte("RetrieveADPFileAction");
        ElementBean eltBean = null;
        ElementBean eaEltBean = null;
        
        eaEltBean = (ElementBean) session.getAttribute(Constants.CAQS_CURRENT_ELEMENT_SESSION_KEY);
    	DaoFactory daoFactory = DaoFactory.getInstance(); 
        ElementDao elementFacade = daoFactory.getElementDao(); 
        eltBean = elementFacade.retrieveAllElementDataById(idElt);
        eltBean.setBaseline(eaEltBean.getBaseline());

        if (eltBean != null) {
            forward = doRetrieveFile(mapping, eltBean, eaEltBean, idCrit, request, response);
        }
        LoggerManager.popContexte();

        if (forward == null) {
			forward = mapping.findForward("success");
		}

        return forward;
    }

    /**
     * Retrieve Retrieve the source file content of the given element and redirect it to the HTTP response.
     * @param mapping the action mapping.
     * @param eltBean the given element.
     * @param request the current HTTP request.
     * @param response the current HTTP response.
     * @return an action forward if an error occurs or null.
     */
    protected ActionForward doRetrieveFile(ActionMapping mapping,
                                       ElementBean eltBean,
                                       ElementBean eaEltBean,
                                       String idCrit,
                                       HttpServletRequest request,
                                       HttpServletResponse response) {
        logger.info("Starting to retrieve file field " + eltBean.getFilePath() + "...");

        byte[] fileContent = null;
        OutputStream out = null;
        try {
            fileContent = getFileContent(eltBean, eaEltBean, getLocale(request));

            if (fileContent == null) {
                request.setAttribute("filePath", eltBean.getFilePath());
                return mapping.findForward("failure");
            }

            out = response.getOutputStream();
            // Set your response headers here
            response.setContentType("image/png");

            response.setContentLength(fileContent.length);

            out.write(fileContent);
            
        } catch (IOException ioe) {
            return mapping.findForward("failure");
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException ioe) {
                    //ignore subtly
                	logger.error("Error during closing stream", ioe);
                }
            }
        }

        return null;
    }
    
    private static final String ADP_FILE_NAME = "ModelDiagram.png";
    
    /**
     * Retrieve the source file content of the given element.
     * @param eltBean the element to display.
     * @return the source file content of the given element.
     */
    private byte[] getFileContent(ElementBean eltBean, ElementBean eaEltBean, Locale loc) {
        byte[] result = null;
        if (eltBean != null) {
            Properties dynProp = CaqsConfigUtil.getCaqsGlobalConfigProperties();
            if (dynProp != null && dynProp.getProperty(Constants.WEB_DATA_DIRECTORY_KEY) != null) {
                String adpFileName = getAdpFileName(dynProp.getProperty(Constants.WEB_DATA_DIRECTORY_KEY), ADP_FILE_NAME, eaEltBean, eltBean);
                File adpFile = new File(adpFileName);
                if (adpFile.exists()) {
                    try {
                        result = new byte[(int)adpFile.length()];
                        BufferedInputStream input = new BufferedInputStream(new FileInputStream(adpFile));
                        input.read(result);
                        input.close();
                    } catch (IOException e) {
                        logger.error("Error reading file: "+adpFile.getName(), e);
                        result = null;
                    }
                }
            }
        }
        return result;
    }
    
    /**
     * Get a well formed file path from a base directory path and a relative file path.
     * @param path a base directory path.
     * @param filePath a relative file path.
     * @param eaEltBean the EA father element.
     * @return the path and filepath concat.
     */
    private String getAdpFileName(String path, String filePath, ElementBean eaEltBean, ElementBean eltBean) {
        String result = "";
        if (path != null && filePath != null) {
            result = ElementBean.getHtmlSrcDir(eaEltBean.getBaseline().getId(), eaEltBean, eaEltBean.getProject(), path);
            result = FileTools.concatPath(result, eltBean.getDesc().replaceAll("\\.", "/"));
            result = FileTools.concatPath(result, filePath);
        }
        return result;
    }

}
