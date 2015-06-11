package com.compuware.caqs.presentation.consult.actions;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
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
import com.compuware.caqs.domain.dataschemas.LineOfMetricsBean;
import com.compuware.caqs.domain.dataschemas.MetriqueBean;
import com.compuware.caqs.domain.dataschemas.MetriqueLineBean;
import com.compuware.caqs.domain.dataschemas.evolutions.ElementsCategory;
import com.compuware.caqs.i18n.LangageUtil;
import com.compuware.caqs.presentation.util.RequestUtil;
import com.compuware.caqs.service.MetricSvc;
import com.compuware.caqs.util.CaqsConfigUtil;
import com.compuware.toolbox.io.FileTools;
import com.compuware.toolbox.util.logging.LoggerManager;
import java.io.PrintWriter;

public class RetrievePrintableSourceFileAction extends ElementSelectedActionAbstract {

    private static org.apache.log4j.Logger logger = LoggerManager.getLogger(Constants.LOG4J_IHM_LOGGER_KEY);

    // --------------------------------------------------------- Public Methods
    public ActionForward doExecute(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response)
            throws IOException, ServletException {

        HttpSession session = request.getSession();

        String idElt = request.getParameter("id_elt");
        String idCrit = request.getParameter("id_crit");

        LoggerManager.pushContexte("RetrieveSourceFileAction");
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

        String output = "";
        if (eltBean != null) {
            output = doRetrieveFile(mapping, eltBean, eaEltBean, idCrit, request, response);
        }
        LoggerManager.popContexte();
        try {
            PrintWriter out = response.getWriter();
            out.write(output);
            out.flush();
        } catch (IOException e) {
            logger.error("RetrievePrintableSourceFileAction : " + e.getMessage());
        }

        return null;

    }

    /**
     * Retrieve Retrieve the source file content of the given element and redirect it to the HTTP response.
     * @param mapping the action mapping.
     * @param eltBean the given element.
     * @param request the current HTTP request.
     * @param response the current HTTP response.
     * @return an action forward if an error occurs or null.
     */
    protected String doRetrieveFile(ActionMapping mapping,
            ElementBean eltBean,
            ElementBean eaEltBean,
            String idCrit,
            HttpServletRequest request,
            HttpServletResponse response) {
        logger.info("Starting to retrieve file field " + eltBean.getFilePath() +
                "...");

        Collection<MetriqueBean> metriqueColl = MetricSvc.getInstance().retrieveQametriqueLines(
                eltBean.getId(), eaEltBean.getBaseline().getId(),
                idCrit, eaEltBean.getUsage().getId(),
                ElementsCategory.fromCode(RequestUtil.getIntParam(request, "tendance", -1)));
        List lines = getLines(metriqueColl);

        return getFileContent(eltBean, eaEltBean, lines, getLocale(request), request);
    }

    private List getLines(Collection<MetriqueBean> metriqueColl) {
        List result = new ArrayList();
        MetriqueBean bean;
        Iterator<MetriqueBean> i = metriqueColl.iterator();
        while (i.hasNext()) {
            bean = i.next();
            result.addAll(bean.getLines());
        }
        Collections.sort(result);
        return result;
    }

    /**
     * Retrieve the source file content of the given element.
     * @param eltBean the element to display.
     * @return the source file content of the given element.
     */
    private String getFileContent(ElementBean eltBean, ElementBean eaEltBean, List lines, Locale loc, HttpServletRequest request) {
        String result = null;
        if (eltBean != null) {
            Properties dynProp = CaqsConfigUtil.getCaqsGlobalConfigProperties();
            List filePath = getFilePath(eltBean, eaEltBean);
            if ((filePath != null) && (filePath.size() > 0)) {
                if (dynProp != null &&
                        dynProp.getProperty(Constants.WEB_DATA_DIRECTORY_KEY) !=
                        null) {
                    File srcFile = getValidFile(dynProp.getProperty(Constants.WEB_DATA_DIRECTORY_KEY), filePath, eaEltBean);
                    if (srcFile != null) {
                        try {
                            String line;
                            int lineNumber = -5;
                            List<LineOfMetricsBean> linesOfMetrics = groupLines(lines);
                            Iterator<LineOfMetricsBean> linesIt = linesOfMetrics.iterator();
                            LineOfMetricsBean nextErrorLine = getNextLine(linesIt);
                            StringBuffer buffer = new StringBuffer();
                            FileInputStream is = new FileInputStream(srcFile);
                            InputStreamReader ir = new InputStreamReader(is, Constants.GLOBAL_CHARSET);
                            BufferedReader reader = new BufferedReader(ir);

                            boolean stopNumbering = false;
                            
                            while ((line = reader.readLine()) != null) {

                                if (lineNumber == 0) {
                                    skipNoCodeLines(eaEltBean.getDialecte(), srcFile, reader);
                                }
                                if (newErrorLineDetected(lineNumber, nextErrorLine)) {
                                    // BEGIN:ASCENDING COMPATIBILITY SPECIFIC CODE
                                    // only if old style used (generated with old PERL script)
                                    // because new java html generator uses an external css file
                                    
                                    if (!line.startsWith("<span class='gutter'>") &&
                                            !line.startsWith("</body>") &&
                                            !line.startsWith("</html>")) {
                                        buffer.append("<span  >");
                                    } else {
                                        // END:ASCENDING COMPATIBILITY SPECIFIC CODE
                                        buffer.append("<span  >");
                                        // BEGIN:ASCENDING COMPATIBILITY SPECIFIC CODE
                                    }
                                   
                                    // END:ASCENDING COMPATIBILITY SPECIFIC CODE
                                }



                                // BEGIN:ASCENDING COMPATIBILITY SPECIFIC CODE 	                        	
                                // only if old style used (generated with old PERL script)
                                // because new java html generator has already generated a gutter with line numbers
                                if(line.startsWith("</pre>")) {
                                    stopNumbering = true;
                                }

                                if (lineNumber > 0 && !stopNumbering &&
                                        !line.startsWith("</body>") &&
                                        !line.startsWith("</html>")) {
                                    if (!line.startsWith("<span class='gutter'>")) {
                                        buffer.append("<a>").append(lineNumber).append("</a>&nbsp;");
                                    }
                                }
                                // END:ASCENDING COMPATIBILITY SPECIFIC CODE 	                        	






                                if (line.startsWith("</head>")) {
                                    /*	                        		if(sizePx!=null) {
                                    buffer.append("<style>body {font-size: ")
                                    .append(sizePx)
                                    .append("px;} </style>");
                                    }*/
                                    buffer.append("<link href=\"" +
                                            request.getContextPath() +
                                            "/css/carscode.css\" rel=\"stylesheet\" type=\"text/css\"'></link>");
                                }
                                buffer.append(line);
                                if (newErrorLineDetected(lineNumber, nextErrorLine)) {
                                    buffer.append("</span>");
                                    String errors = this.getErrors(nextErrorLine, loc);
                                    buffer.append(errors);
                                    nextErrorLine = getNextLine(linesIt);
                                }
                                buffer.append('\n');
                                lineNumber++;
                            }
                            result = buffer.toString();
                            reader.close();
                        } catch (IOException e) {
                            logger.error("Error reading file: " +
                                    srcFile.getAbsolutePath(), e);
                            result = null;
                        }
                    }
                }
            }
        }
        return result;
    }

    private List<LineOfMetricsBean> groupLines(List<MetriqueLineBean> lines) {
        List<LineOfMetricsBean> result = new ArrayList<LineOfMetricsBean>();
        Iterator<MetriqueLineBean> metIter = lines.iterator();
        MetriqueLineBean currentMetric = null;
        LineOfMetricsBean currentLine = null;
        while (metIter.hasNext()) {
            currentMetric = metIter.next();
            if (currentLine == null || currentLine.getLine() !=
                    currentMetric.getLine()) {
                currentLine = new LineOfMetricsBean(currentMetric.getLine());
                result.add(currentLine);
            }
            currentLine.addMetricLine(currentMetric);
        }
        return result;
    }

    private void skipNoCodeLines(DialecteBean dialecte, File srcFile, BufferedReader reader) throws IOException {
        if (dialecte != null && dialecte.getLangage() != null &&
                dialecte.getLangage().getId() != null &&
                "vb".equalsIgnoreCase(dialecte.getLangage().getId())) {
            boolean firstAttributeFound = false;
            String line = null;
            if (!(srcFile.getAbsolutePath().endsWith(".vb.html")) &&
                    !(srcFile.getAbsolutePath().endsWith(".cs.html")) &&
                    !(srcFile.getAbsolutePath().endsWith(".aspx.html"))) {
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

    /**
     * Verify if a new error line has to be inserted.
     * @param lineNumber the current line number.
     * @param nextErrorLine the next error line to insert.
     * @return true if the current line is the next error line.
     */
    private boolean newErrorLineDetected(int lineNumber, LineOfMetricsBean nextErrorLine) {
        return nextErrorLine != null && (lineNumber == nextErrorLine.getLine());
    }

    private LineOfMetricsBean getNextLine(Iterator<LineOfMetricsBean> it) {
        LineOfMetricsBean result = null;
        if (it != null && it.hasNext()) {
            result = it.next();
        }
        return result;
    }

    private String getErrors(LineOfMetricsBean nextErrorLine, Locale loc) {
        StringBuffer result = new StringBuffer();
        result.append("<P style='font-weight:bold; font-style:italic; font-family: Helvetica' />");
        Collection<MetriqueLineBean> metricColl = nextErrorLine.getMetricColl();
        Iterator<MetriqueLineBean> metricIter = metricColl.iterator();
        MetriqueLineBean currentMetric = null;
        MetriqueBean tmp = new MetriqueBean();
        while (metricIter.hasNext()) {
            currentMetric = metricIter.next();
            tmp.setId(currentMetric.getIdMet());
            result.append("<img src='images/exclamation.gif' />&nbsp;&nbsp;").append(tmp.getLib(loc)).append("<BR/>");
        }
        result.append("</P>");
        return result.toString();
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
                if (srcFile.exists()) {
                    result = srcFile;
                    logger.info("Found file: " + result.getAbsolutePath());
                }
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
