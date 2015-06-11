package com.compuware.caqs.presentation.consult.actions.copypaste;

import code2html.Code2HTML;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
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
import com.compuware.caqs.domain.dataschemas.DialecteBean;
import com.compuware.caqs.domain.dataschemas.ElementBean;
import com.compuware.caqs.domain.dataschemas.ElementType;
import com.compuware.caqs.domain.dataschemas.copypaste.CopyPasteBean;
import com.compuware.caqs.exception.CaqsException;
import com.compuware.caqs.i18n.LangageUtil;
import com.compuware.caqs.presentation.consult.actions.ElementSelectedActionAbstract;
import com.compuware.caqs.service.copypaste.CopyPasteSvc;
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
public class CopyPasteRetrieveSourceAction extends ElementSelectedActionAbstract {

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
        String idCopy = request.getParameter("id");
        String line = request.getParameter("line");

        ElementBean eltBean = null;
        ElementBean eaEltBean = null;

        ElementBean currentEltBean = (ElementBean) session.getAttribute(Constants.CAQS_CURRENT_ELEMENT_SESSION_KEY);

        DaoFactory daoFactory = DaoFactory.getInstance();
        ElementDao elementFacade = daoFactory.getElementDao();
        if (currentEltBean.getTypeElt().equalsIgnoreCase(ElementType.EA)) {
            eaEltBean = currentEltBean;
        } else {
            eaEltBean = elementFacade.retrieveMainElement(idElt);
            eaEltBean.setBaseline(currentEltBean.getBaseline());
        }

        eltBean = elementFacade.retrieveAllElementDataById(idElt);
        eltBean.setBaseline(eaEltBean.getBaseline());

        CopyPasteSvc copyPasteSvc = CopyPasteSvc.getInstance();
        CopyPasteBean copyPaste = null;
        try {
            copyPaste = copyPasteSvc.retrieveCopyPaste(idCopy, idElt, eaEltBean.getBaseline().getId(), Integer.parseInt(line));
        } catch (CaqsException e) {
            logger.error("Error during copy/paste retrieve", e);
            forward = mapping.findForward("failure");
        }

        if (forward == null && eltBean != null) {
            forward = doRetrieveFile(mapping, eltBean, eaEltBean, copyPaste, request, response);
        }
        LoggerManager.popContexte();

        if (forward != null) {
            return forward;
        }

        return mapping.findForward("success");

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
            CopyPasteBean copyPaste,
            HttpServletRequest request,
            HttpServletResponse response) {
        logger.info("Starting to retrieve file field " + eltBean.getFilePath() + "...");

        byte[] fileContent = null;
        OutputStream out = null;
        try {
            fileContent = getFileContent(eltBean, eaEltBean, copyPaste, getLocale(request), request);

            if (fileContent == null) {
                request.setAttribute("filePath", eltBean.getFilePath());
                return mapping.findForward("failure");
            }

            out = response.getOutputStream();
            // Set your response headers here
            response.setContentType("text/html");
            response.setCharacterEncoding(Constants.GLOBAL_CHARSET);
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

    /**
     * Retrieve the source file content of the given element.
     * @param eltBean the element to display.
     * @return the source file content of the given element.
     */
    private byte[] getFileContent(ElementBean eltBean, ElementBean eaEltBean, CopyPasteBean copyPaste, Locale loc, HttpServletRequest request) {
        byte[] result = null;
        if (eltBean != null) {
            Properties dynProp = CaqsConfigUtil.getCaqsGlobalConfigProperties();
            List filePath = getFilePath(eltBean, eaEltBean);
            if ((filePath != null) && (!filePath.isEmpty())) {
                if (dynProp != null && dynProp.getProperty(Constants.WEB_DATA_DIRECTORY_KEY) != null) {
                    File srcFile = getValidFile(dynProp.getProperty(Constants.WEB_DATA_DIRECTORY_KEY), filePath, eaEltBean);
                    if (srcFile != null) {
                        try {
                            String line;
                            int lineNumber = -5;
                            int lineStart = copyPaste.getElements().get(0).getLine();
                            StringBuffer buffer = new StringBuffer();
                            BufferedReader reader = new BufferedReader(new FileReader(srcFile));
                            while ((line = reader.readLine()) != null) {
                                if (lineNumber == 0) {
                                    skipNoCodeLines(eaEltBean.getDialecte(), srcFile, reader);
                                    //skipLines(reader, copyPaste.getElements().get(0).getLine());
                                }
                                if (lineNumber > lineStart && lineNumber < lineStart + copyPaste.getLines()) {
                                    buffer.append("<span style=\"background-color: yellow\">");
                                }
                                if (lineNumber > 0) {
                                    buffer.append("<a name='line").append(lineNumber).append("'>").append(lineNumber).append("</a>");
                                }
                                if (line.startsWith("</head>")) {
                                    buffer.append("<link href=\"css/carscode.css\" rel=\"stylesheet\" type=\"text/css\" />");
                                    buffer.append("<script type=\"text/javascript\" src=\"js/boxOver.js\"></script>");
                                }
                                buffer.append(line);
                                if (lineNumber > lineStart && lineNumber < lineStart + copyPaste.getLines()) {
                                    buffer.append("</span>");
                                }
                                buffer.append('\n');
                                lineNumber++;
                            }
                            result = buffer.toString().getBytes(Constants.GLOBAL_CHARSET);
                            reader.close();
                        } catch (IOException e) {
                            logger.error("Error reading file: " + srcFile.getAbsolutePath(), e);
                            result = null;
                        }
                    }
                }
            }
        }
        return result;
    }

    private void skipLines(BufferedReader reader, int nbLines) throws IOException {
        if (reader != null) {
            for (int i = 0; i < nbLines; i++) {
                reader.readLine();
            }
        }
    }

    private void skipNoCodeLines(DialecteBean dialecte, File srcFile, BufferedReader reader) throws IOException {
        if (dialecte != null
                && dialecte.getLangage() != null
                && "vb".equalsIgnoreCase(dialecte.getLangage().getId())) {
            boolean firstAttributeFound = false;
            String line = null;
            if (!(srcFile.getAbsolutePath().endsWith(".vb.html"))
                    && !(srcFile.getAbsolutePath().endsWith(".cs.html"))
                    && !(srcFile.getAbsolutePath().endsWith(".aspx.html"))) {
                while (!firstAttributeFound) {
                    line = reader.readLine();
                    if (line.contains("Attribute ")) {
                        firstAttributeFound = true;
                    }
                }
                boolean noMoreAttributeFound = false;
                while (!noMoreAttributeFound) {
                    reader.mark(300);
                    line = reader.readLine();
                    if (!line.contains("Attribute ")) {
                        noMoreAttributeFound = true;
                        reader.reset();
                    }
                }
            }
        }
    }

    private File getValidFile(String dataPath, List filePathList, ElementBean eaEltBean) {
        File result = null;
        String filePath = null;
        String htmlFileName = null;
        File srcFile = null;
        if (filePathList != null) {
            Iterator i = filePathList.iterator();
            while (i.hasNext() && (result == null)) {
                filePath = (String) i.next();
                htmlFileName = getHtmlFileName(dataPath, filePath, eaEltBean);
                srcFile = new File(htmlFileName);
                // if the html file does not exist yet
                if (!srcFile.exists()) {
                    // try to generate it from source
                    String srcFileName = htmlFileName.replace(".html", "");
                    Code2HTML.processFile(CaqsConfigUtil.getLocalizedCaqsFile(Constants.CODE2HTML_MODES_HOME_KEY), srcFileName, htmlFileName);
                    // try to open newly created html file
                    srcFile = new File(htmlFileName);
                    if (!srcFile.exists()) {
                        logger.warn("File not found: " + result.getAbsolutePath());
                    }
                }
                result = srcFile;
                logger.info("Found file: " + result.getAbsolutePath());
            }
        }
        return result;
    }

    /**
     *
     * @param eltBean
     * @param eaEltBean
     * @return
     */
    private List getFilePath(ElementBean eltBean, ElementBean eaEltBean) {
        List result = new ArrayList();
        String path = eltBean.getFilePath();
        if (path == null || path.length() < 1) {
            Properties dynProp = CaqsConfigUtil.getCaqsGlobalConfigProperties();
            DialecteBean dialecte = eaEltBean.getDialecte();
            result.addAll(LangageUtil.getFileNameFromDescription(dialecte.getLangage().getId(), eltBean.getTypeElt(), eltBean.getDesc(), dynProp));
        } else {
            result.add(path);
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
    private String getHtmlFileName(String path, String filePath, ElementBean eaEltBean) {
        StringBuffer result = new StringBuffer();
        if (path != null && filePath != null) {
            String pathWithBaseline = ElementBean.getHtmlSrcDir(eaEltBean.getBaseline().getId(), eaEltBean, eaEltBean.getProject(), path);
            result.append(FileTools.concatPath(pathWithBaseline, filePath));
        }
        result.append(".html");
        return result.toString();
    }
}
