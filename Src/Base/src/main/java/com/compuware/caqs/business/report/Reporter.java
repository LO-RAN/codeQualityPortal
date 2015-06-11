/**
 * 
 */
package com.compuware.caqs.business.report;

import com.compuware.toolbox.exception.SystemIOException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Properties;

import org.apache.struts.util.MessageResources;

import com.compuware.caqs.business.messages.MessagesListMgmt;
import com.compuware.caqs.constants.Constants;
import com.compuware.caqs.dao.factory.DaoFactory;
import com.compuware.caqs.dao.interfaces.InternationalizationDao;
import com.compuware.caqs.domain.dataschemas.ElementBean;
import com.compuware.caqs.domain.dataschemas.ElementType;
import com.compuware.caqs.util.CaqsConfigUtil;
import com.compuware.caqs.util.FileUtils;
import com.compuware.toolbox.io.FileTools;
import com.compuware.toolbox.io.PropertiesReader;
import com.compuware.toolbox.util.logging.LoggerManager;

/**
 * @author cwfr-fdubois
 *
 */
public class Reporter {

    private static org.apache.log4j.Logger logger = LoggerManager.getLogger(Constants.LOG4J_IHM_LOGGER_KEY);
    private DaoFactory daoFactory = DaoFactory.getInstance();
    private static final String CONTENT_METRIC_DEFINITIONS_XSL = "content-metric-definitions.xsl";
    private static final String CONTENT_CRITERION_DEFINITIONS_XSL = "content-criterion-definitions.xsl";
    private static final String CONTENT_CRITERE_DETAILS_XSL = "content-critere-details.xsl";
    private static final String CONTENT_SYNTHESE_OBJECTIFS_XSL = "content-synthese-objectifs.xsl";
    private static final String CONTENT_SYNTHESE_BOTTOMUP_XSL = "content-synthese-bottomup.xsl";
    private static final String CONTENT_SYNTHESE_EVOLUTION_XSL = "content-synthese-evolution.xsl";
    private static final String CONTENT_SYNTHESE_XSL = "content-synthese.xsl";
    private static final String CONTENT_GENERAL_XSL = "content-general.xsl";
    private static final String CONTENT_TITLE_XSL = "content-title.xsl";
    private static final String CONTENT_SYNTHESE_ACTIONPLAN_XSL = "content-synthese-actionplan.xsl";
    private static final String CONTENT_ACTIONPLAN_DETAILS_XSL = "content-actionplan-details.xsl";
    private static final String CONTENT_STYLES_XSL = "content-styles.xsl";
    private static final String CONTENT_ANNEXE_IFPUG_XSL = "content-ifpug-annexe.xsl";
    private static final String CONTENT_ANNEXE_TITLE_XSL = "content-title-annexe.xsl";
    private static final String CONTENT_JUSTIFICATIONS_XSL = "content-justifications.xsl";
    private static final String CONTENT_EVOLUTION_DETAILS_XSL = "content-evolution-details.xsl";
    private static final String CONTENT_ACTIONPLAN_EVOLUTION_DETAILS_XSL = "content-actionplan-evolution-details.xsl";

    public File getReportFile(ElementBean eltBean, String idBline, Locale loc) {
        String destination = Reporter.getReportRootDestination();
        destination = getReportPath(eltBean, idBline, loc);
        File zipFile = new File(getReportFileName(destination, eltBean, ReportConstants.ZIP_FILE_EXT));
        return zipFile;
    }

    public boolean reportAlreadyExists(ElementBean eltBean, Locale loc) {
        File f = this.getReportFile(eltBean, eltBean.getBaseline().getId(), loc);
        boolean retour = (f != null) ? f.exists() && f.isFile() : false;
        return retour;
    }

    private static String getReportRootDestination() {
        String configFile = CaqsConfigUtil.getLocalizedCaqsFile(Constants.CAQS_REPORT_PROPERTIES_FILE_PATH);
        // try to find the properties file
        Properties reportProps = PropertiesReader.getProperties(configFile, null);
        return new File(reportProps.getProperty(Constants.CAQS_REPORT_DEST_KEY)).getAbsolutePath();
    }

    public File generateReport(ElementBean eltBean, String configDir, boolean forceRegeneration, Locale loc,
            MessageResources resources, String idMessage) throws IOException, SystemIOException {

        String configFile = CaqsConfigUtil.getLocalizedCaqsFile(Constants.CAQS_REPORT_PROPERTIES_FILE_PATH);

        // try to find the properties file
        Properties reportProps = PropertiesReader.getProperties(configFile, this);

        InternationalizationDao intlDao = daoFactory.getInternationalizationDao();
        Locale workLocale = intlDao.retrieveLocale(loc.getLanguage());

        String destination = getReportPath(eltBean, eltBean.getBaseline().getId(), loc);

        if (forceRegeneration) {
            if (idMessage != null) {
                MessagesListMgmt.getInstance().setInProgressMessageToStep(idMessage, "caqs.messages.report.deletingOldReport");
            }
            FileUtils.deleteDirectory(destination);
            String annexeDir = getAnnexesDestination(eltBean, loc);
            FileUtils.deleteDirectory(annexeDir);
            if (idMessage != null) {
                MessagesListMgmt.getInstance().setMessagePercentage(idMessage, 5);
            }
        }

        String snote = reportProps.getProperty(Constants.CAQS_REPORT_NOTE_SEUIL_KEY);
        double noteSeuil = Double.parseDouble(snote);

        File zipFile = new File(getReportFileName(destination, eltBean, ReportConstants.ZIP_FILE_EXT));

        if (!reportAlreadyExists(eltBean, loc)) {
            if (idMessage != null) {
                MessagesListMgmt.getInstance().setInProgressMessageToStep(idMessage, "caqs.messages.report.generatingDatas");
            }
            generateXmlData(eltBean, noteSeuil, destination, configDir, workLocale);
            if (idMessage != null) {
                MessagesListMgmt.getInstance().setMessagePercentage(idMessage, 20);
            }
            createXmlReports(destination, eltBean, configDir, workLocale);
            List wordFiles = createWordDocument(eltBean, destination, configDir, workLocale, resources, idMessage);
            FileTools.zipFile(wordFiles, zipFile, false);
        }

        return zipFile;
    }

    public static String getReportPath(ElementBean eltBean, String idBline) {
        String rootPath = getReportRootDestination();
        StringBuffer result = new StringBuffer(rootPath);
        if (!FileTools.endsWithFileSeparator(rootPath)) {
            result.append(FileTools.FILE_SEPARATOR_SLASH);
        }
        result.append(eltBean.getProject().getLib()).append('-').append(eltBean.getProject().getId());
        result.append(FileTools.FILE_SEPARATOR_SLASH).append(idBline);
        result.append(FileTools.FILE_SEPARATOR_SLASH).append(eltBean.getLib()).append("-").append(eltBean.getId());
        result.append(FileTools.FILE_SEPARATOR_SLASH);
        return result.toString();
    }

    private static String getReportPath(ElementBean eltBean, String idBline, Locale locale) {
        StringBuffer result = new StringBuffer(getReportPath(eltBean, idBline));
        result.append(locale.getLanguage());
        result.append(FileTools.FILE_SEPARATOR_SLASH);
        return result.toString();
    }

    public static String getReportFileName(String rootPath, ElementBean eltBean, String fileExt) {
        StringBuffer result = new StringBuffer(rootPath);
        if (!FileTools.endsWithFileSeparator(rootPath)) {
            result.append(FileTools.FILE_SEPARATOR_SLASH);
        }
        result.append(eltBean.getProject().getLib()).append('-').append(eltBean.getLib()).append(fileExt);
        return result.toString();
    }

    private void generateXmlData(ElementBean eltBean, double noteSeuil,
            String destination, String configDir, Locale loc) throws IOException {
        logger.debug("Xml dir:" + destination + ReportConstants.CAQS_XML_DIR +
                FileTools.FILE_SEPARATOR_SLASH);
        File xmlDest = new File(destination + ReportConstants.CAQS_XML_DIR +
                FileTools.FILE_SEPARATOR_SLASH);
        if (!xmlDest.exists()) {
            xmlDest.mkdirs();
        }

        String[][] filterArray = getModuleArray(eltBean.getInfo2());

        generateXmlData(new GeneralInformationXmlGenerator(eltBean, noteSeuil, loc), xmlDest, configDir, loc);
        generateXmlData(new SyntheseXmlGenerator(eltBean, noteSeuil, loc), xmlDest, configDir, loc);
        if (ElementType.EA.equalsIgnoreCase(eltBean.getTypeElt())) {
            generateXmlData(new StylesXmlGenerator(eltBean, noteSeuil, loc), xmlDest, configDir, loc);
            generateXmlData(new SyntheseObjectifsXmlGenerator(eltBean, noteSeuil, loc), xmlDest, configDir, loc);
            generateXmlData(new DetailCriterionXmlGenerator(eltBean, noteSeuil, filterArray, loc), xmlDest, configDir, loc);
            generateXmlData(new BottomUpSyntheseXmlGenerator(eltBean, noteSeuil, loc), xmlDest, configDir, loc);
            generateXmlData(new BottomUpListeXmlGenerator(eltBean, noteSeuil, loc), xmlDest, configDir, loc);
            generateXmlData(new EvolutionXmlGenerator(eltBean, noteSeuil, loc), xmlDest, configDir, loc);
            generateXmlData(new EvolutionDetailsXmlGenerator(eltBean, noteSeuil, loc), xmlDest, configDir, loc);
            generateXmlData(new SyntheseActionPlanXmlGenerator(eltBean, noteSeuil, loc), xmlDest, configDir, loc);
            generateXmlData(new DetailActionPlanXmlGenerator(eltBean, noteSeuil, loc), xmlDest, configDir, loc);
            generateXmlData(new JustificationXmlGenerator(eltBean, noteSeuil, loc), xmlDest, configDir, loc);
            generateXmlData(new DetailEvolutionActionPlanXmlGenerator(eltBean, noteSeuil, loc), xmlDest, configDir, loc);

            String annexeDestination = getAnnexesDestination(eltBean, loc);
            File annexeFile = new File(annexeDestination + "annexes.doc");
            if (!annexeFile.exists()) {
                File xmlAnnexesDest = new File(annexeDestination +
                        ReportConstants.CAQS_XML_DIR +
                        FileTools.FILE_SEPARATOR_SLASH);
                if (!xmlAnnexesDest.exists()) {
                    xmlAnnexesDest.mkdirs();
                }
                generateXmlData(new StylesXmlGenerator(eltBean, noteSeuil, loc), xmlAnnexesDest, configDir, loc);
                generateXmlData(new AnnexeTitleXmlGenerator(eltBean, noteSeuil, loc), xmlAnnexesDest, configDir, loc);
                generateXmlData(new IFPUGXmlGenerator(eltBean, noteSeuil, loc), xmlAnnexesDest, configDir, loc);
                generateXmlData(new DefinitionXmlGenerator(eltBean, noteSeuil, loc), xmlAnnexesDest, configDir, loc);
            }
        }
    }

    private String[][] getModuleArray(String moduleArrayString) {
        String[][] result = {{"%", ""}};
        if (moduleArrayString != null && moduleArrayString.length() > 0 &&
                moduleArrayString.contains("|")) {
            String[] tmp = moduleArrayString.split("\\|");
            if (tmp != null && tmp.length > 1) {
                String[][] tmp2 = new String[tmp.length][2];
                for (int i = 0; i < tmp.length; i++) {
                    tmp2[i] = tmp[i].split(";");
                }
                result = tmp2;
            }
        }
        return result;
    }

    private void generateXmlData(XmlGenerator generator, File xmlDest, String configDir, Locale loc) throws IOException {
        generator.setDtdLocation(configDir);
        generator.setLocale(loc);
        generator.retrieveData();
        generator.generate(xmlDest);
    }

    private File createWordDocument(OpenOfficeFileGenerator generator, String destDir, String configPath, Locale loc) throws IOException, SystemIOException {
        generator.prepareOpenOfficeTemplate(destDir, configPath, loc);
        generator.generateFile();
        return generator.createWordDocument();
    }

    private List createWordDocument(ElementBean eltBean, String dest, String configPath, Locale loc, MessageResources resources, String idMess) throws IOException, SystemIOException {
        List result = new ArrayList();
        if (idMess != null) {
            MessagesListMgmt.getInstance().setInProgressMessageToStep(idMess, "caqs.messages.report.generatingOdtSynthesis");
        }
        OpenOfficeFileGenerator generator = new SyntheseOOFileGenerator(eltBean, dest, resources, loc);

        if (idMess != null) {
            MessagesListMgmt.getInstance().setMessagePercentage(idMess, 50);
            MessagesListMgmt.getInstance().setInProgressMessageToStep(idMess, "caqs.messages.report.generatingWordSynthesis");
        }
        result.add(createWordDocument(generator, dest, configPath, loc));

        if (idMess != null) {
            MessagesListMgmt.getInstance().setMessagePercentage(idMess, 60);
            MessagesListMgmt.getInstance().setInProgressMessageToStep(idMess, "caqs.messages.report.generatingOdtDetail");
        }
        generator = new DetailOOFileGenerator(eltBean, dest, resources, loc);

        if (idMess != null) {
            MessagesListMgmt.getInstance().setMessagePercentage(idMess, 70);
            MessagesListMgmt.getInstance().setInProgressMessageToStep(idMess, "caqs.messages.report.generatingWordDetail");
        }
        result.add(createWordDocument(generator, dest, configPath, loc));

        String annexeDestination = getAnnexesDestination(eltBean, loc);
        File annexeFile = new File(annexeDestination + "annexes-"+eltBean.getUsage().getId()+".doc");
        if (annexeFile.exists()) {
            result.add(annexeFile);
        } else {
            if (idMess != null) {
                MessagesListMgmt.getInstance().setMessagePercentage(idMess, 80);
                MessagesListMgmt.getInstance().setInProgressMessageToStep(idMess, "caqs.messages.report.generatingOdtAnnexe");
            }
            generator = new AnnexesOOFileGenerator(eltBean, annexeDestination, resources, loc);
            if (idMess != null) {
                MessagesListMgmt.getInstance().setMessagePercentage(idMess, 90);
                MessagesListMgmt.getInstance().setInProgressMessageToStep(idMess, "caqs.messages.report.generatingWordAnnexe");
            }
            result.add(createWordDocument(generator, annexeDestination, configPath, loc));
        }
        return result;
    }

    private String getAnnexesDestination(ElementBean eltBean, Locale loc) {
        // try to find the properties file
        String annexeDestination = FileTools.concatPath(Reporter.getReportRootDestination(), eltBean.getUsage().getId() +
                '/' + loc.getLanguage() + '/');
        return annexeDestination;
    }

    private void createXmlReports(String destDir, ElementBean eltBean, String configPath, Locale loc) {
        String xslPath = FileTools.concatPath(configPath, ReportConstants.CAQS_XSL_PATH);
        createOpenOfficeXmlReport(ReportConstants.STYLES_XML_FILE_NAME, destDir, xslPath, Reporter.CONTENT_STYLES_XSL, loc);
        createOpenOfficeXmlReport(ReportConstants.GENERAL_XML_FILE_NAME, destDir, xslPath, Reporter.CONTENT_TITLE_XSL, loc);
        createOpenOfficeXmlReport(ReportConstants.GENERAL_XML_FILE_NAME, ReportConstants.GENERAL_OO_FILE_NAME, destDir, xslPath, Reporter.CONTENT_GENERAL_XSL, loc);
        createOpenOfficeXmlReport(ReportConstants.SYNTHESE_XML_FILE_NAME, destDir, xslPath, Reporter.CONTENT_SYNTHESE_XSL, loc);
        if (ElementType.EA.equalsIgnoreCase(eltBean.getTypeElt())) {
            createOpenOfficeXmlReport(ReportConstants.SYNTHESE_BOTTOMUP_XML_FILE_NAME, destDir, xslPath, Reporter.CONTENT_SYNTHESE_BOTTOMUP_XSL, loc);
            createOpenOfficeXmlReport(ReportConstants.SYNTHESE_OBJECTIFS_XML_FILE_NAME, destDir, xslPath, Reporter.CONTENT_SYNTHESE_OBJECTIFS_XSL, loc);
            createOpenOfficeXmlReport(ReportConstants.SYNTHESE_ACTIONPLAN_XML_FILE_NAME, destDir, xslPath, Reporter.CONTENT_SYNTHESE_ACTIONPLAN_XSL, loc);
            File evolutionXmlFile = new File(destDir +
                    ReportConstants.CAQS_XML_DIR +
                    FileTools.FILE_SEPARATOR_SLASH +
                    ReportConstants.EVOLUTION_XML_FILE_NAME +
                    ReportConstants.XML_FILE_EXT);
            if (evolutionXmlFile.exists()) {
                createOpenOfficeXmlReport(ReportConstants.EVOLUTION_XML_FILE_NAME, destDir, xslPath, Reporter.CONTENT_SYNTHESE_EVOLUTION_XSL, loc);
            }
            createOpenOfficeXmlReport(ReportConstants.SYNTHESE_JUSTIFICATIONS_XML_FILE_NAME, destDir, xslPath, Reporter.CONTENT_JUSTIFICATIONS_XSL, loc);

            int nbCriterionDetail = 1;
            if (eltBean.getInfo2() != null && eltBean.getInfo2().length() > 0) {
                nbCriterionDetail = eltBean.getInfo2().split("\\|").length;
            }
            for (int i = 0; i < nbCriterionDetail; i++) {
                createOpenOfficeXmlReport(ReportConstants.CRITERION_DETAILS_XML_FILE_NAME +
                        i, destDir, xslPath, Reporter.CONTENT_CRITERE_DETAILS_XSL, loc);
            }

            createOpenOfficeXmlReport(ReportConstants.ACTIONPLAN_DETAILS_XML_FILE_NAME, destDir, xslPath, Reporter.CONTENT_ACTIONPLAN_DETAILS_XSL, loc);
            createOpenOfficeXmlReport(ReportConstants.EVOLUTION_DETAILS_XML_FILE_NAME, destDir, xslPath, Reporter.CONTENT_EVOLUTION_DETAILS_XSL, loc);
            createOpenOfficeXmlReport(ReportConstants.ACTIONPLAN_EVOLUTION_DETAILS_XML_FILE_NAME, destDir, xslPath, Reporter.CONTENT_ACTIONPLAN_EVOLUTION_DETAILS_XSL, loc);

            String annexeDestination = getAnnexesDestination(eltBean, loc);
            File annexeFile = new File(annexeDestination + "annexes.doc");
            if (!annexeFile.exists()) {
                createOpenOfficeXmlReport(ReportConstants.STYLES_XML_FILE_NAME, annexeDestination, xslPath, Reporter.CONTENT_STYLES_XSL, loc);
                createOpenOfficeXmlReport(ReportConstants.ANNEXE_TITLE_XML_FILE_NAME, annexeDestination, xslPath, Reporter.CONTENT_ANNEXE_TITLE_XSL, loc);
                createOpenOfficeXmlReport(ReportConstants.IFPUG_DEFINITION_XML_FILE_NAME, annexeDestination, xslPath, Reporter.CONTENT_ANNEXE_IFPUG_XSL, loc);
                createOpenOfficeXmlReport(ReportConstants.CRITERION_DEFINITION_XML_FILE_NAME, annexeDestination, xslPath, Reporter.CONTENT_CRITERION_DEFINITIONS_XSL, loc);
                createOpenOfficeXmlReport(ReportConstants.METRIC_DEFINITION_XML_FILE_NAME, annexeDestination, xslPath, Reporter.CONTENT_METRIC_DEFINITIONS_XSL, loc);
            }
        }
    }

    private void createOpenOfficeXmlReport(String xmlFileName, String destDir, String configPath, String xslFileName, Locale loc) {
        createOpenOfficeXmlReport(xmlFileName, xmlFileName, destDir, configPath, xslFileName, loc);
    }

    private void createOpenOfficeXmlReport(String xmlFileName, String ooFileName, String destDir, String configPath, String xslFileName, Locale loc) {
        try {

            //Setup directories
            File xmlDir = new File(destDir + ReportConstants.CAQS_XML_DIR +
                    FileTools.FILE_SEPARATOR_SLASH);
            File ooDir = new File(destDir +
                    ReportConstants.CAQS_OPENOFFICE_XML_DIR + File.separator);
            if (!ooDir.exists()) {
                ooDir.mkdirs();
            }

            //Setup input and output files
            File xmlFile = new File(xmlDir, xmlFileName +
                    ReportConstants.XML_FILE_EXT);
            File xsltFile = getValidLocalizedFile(configPath, xslFileName, loc);
            File ooFile = new File(ooDir, ooFileName +
                    ReportConstants.XML_FILE_EXT);

            logger.debug("OO File: " + ooFile.getAbsolutePath());
            logger.debug("XML File: " + xmlFile.getAbsolutePath());
            logger.debug("XSLT File: " + xsltFile.getAbsolutePath());

            try {

                javax.xml.transform.Source xmlSource = new javax.xml.transform.stream.StreamSource(xmlFile.getAbsolutePath());
                javax.xml.transform.Source xsltSource = new javax.xml.transform.stream.StreamSource(xsltFile.getAbsolutePath());
                javax.xml.transform.Result result = new javax.xml.transform.stream.StreamResult(ooFile.getAbsolutePath());

                // create an instance of TransformerFactory
                javax.xml.transform.TransformerFactory transFact = javax.xml.transform.TransformerFactory.newInstance();

                javax.xml.transform.Transformer trans = transFact.newTransformer(xsltSource);

                trans.transform(xmlSource, result);

            } catch (RuntimeException e) {
                logger.error("Error generating report", e);
            }

        } catch (Exception e) {
            logger.error("Error generating report", e);
        }
    }

    private File getValidLocalizedFile(String path, String fileName, Locale loc) {
        String localizedPath = FileTools.concatPath(path, loc.getLanguage().toLowerCase());
        File rootDir = new File(localizedPath);
        File tmpFile = new File(rootDir, fileName);
        if (!tmpFile.exists()) {
            localizedPath = FileTools.concatPath(path, Locale.getDefault().getLanguage().toLowerCase());
            rootDir = new File(localizedPath);
            tmpFile = new File(rootDir, fileName);
        }
        return tmpFile;
    }
}
