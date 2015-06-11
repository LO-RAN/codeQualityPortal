/**
 * Titre : <p>
 * Description : <p>
 * Copyright : Copyright (c) Frederic DUBOIS<p>
 * Societe : Software & Process<p>
 * @author Frederic DUBOIS
 * @version 1.0
 */
package com.compuware.caqs.presentation.process.servlets;

import com.compuware.caqs.service.impexp.ImportExportSvc;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.compuware.caqs.constants.Constants;
import com.compuware.caqs.domain.dataschemas.AnalysisResult;
import com.compuware.caqs.presentation.util.RequestUtil;
import com.compuware.toolbox.util.logging.LoggerManager;

public class ExportServlet extends ProcessServlet {

    /**
     *
     */
    private static final long serialVersionUID = 8359395015826115883L;
    private static org.apache.log4j.Logger logger = LoggerManager.getLogger(Constants.LOG4J_IHM_LOGGER_KEY);

    public void init(ServletConfig config) throws ServletException {
        super.init(config);
    }

    //Traiter la requete HTTP Post
    /**
     * When used from client browser, the following parameters are interpreted :
     * <ul>
     * <li>id_pro   : project Id</li> 
     * <li>id_bline : baseline Id</li>
     * <li>metrics	: one of {true,false} (false when recalculating EA)</li> 
     * </ul>
     * ex: http://myWebSite/CalculServlet?id_pro=20050101010101&id_bline=20050101010102&metrics=false
     * <p/>
     * @param request
     * @param response
     * @throws ServletException
     * @throws java.io.IOException
     */
    protected AnalysisResult doProcessRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, java.io.IOException {
        ResourceBundle resources = RequestUtil.getCaqsResourceBundle(request);

        response.setContentType("text/html");

        String typeExport = request.getParameter("type");

        boolean success = false;
        String message = null;

        String idUser = request.getParameter("idUser");

        if ("modele".equals(typeExport)) {
            String idUsa = request.getParameter("idUsa");
            String language = request.getParameter("language");
            success = ImportExportSvc.getInstance().exportModel(idUsa, idUser, new Locale(language));
            if (success) {
                message = resources.getString("caqs.exportservlet.reussi");
            } else {
                message = message + resources.getString("caqs.exportservlet.echec");
            }
        } else if ("project".equals(typeExport)) {
            String idPro = request.getParameter("idPro");
            String libPro = request.getParameter("libPro");
            success = ImportExportSvc.getInstance().exportProject(idPro, libPro, idUser);
            if (success) {
                message = resources.getString("caqs.exportservlet.reussi");
            } else {
                message = message + resources.getString("caqs.exportservlet.echec");
            }
        }

        //Sends the response to the scheduller
        AnalysisResult result = new AnalysisResult();
        result.setSuccess(success);
        result.setMessage(message);
        return result;

    }
}
