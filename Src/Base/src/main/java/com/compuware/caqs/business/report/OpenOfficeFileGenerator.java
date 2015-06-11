package com.compuware.caqs.business.report;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Properties;

import com.compuware.caqs.business.util.AntExecutor;
import com.compuware.caqs.constants.Constants;
import com.compuware.caqs.domain.dataschemas.ElementBean;
import com.compuware.caqs.util.CaqsConfigUtil;
import com.compuware.caqs.util.SystemIO;
import com.compuware.toolbox.exception.SystemIOException;
import com.compuware.toolbox.io.FileTools;
import com.compuware.toolbox.io.PropertiesReader;
import com.compuware.toolbox.io.insert.XmlFileInsertEntry;
import com.compuware.toolbox.util.logging.LoggerManager;

import org.apache.struts.util.MessageResources;

public abstract class OpenOfficeFileGenerator {

	protected static org.apache.log4j.Logger logger = LoggerManager.getLogger(Constants.LOG4J_IHM_LOGGER_KEY);

	private static final String EXCLUDE_REGEXP_2 = "</content>";
	private static final String EXCLUDE_REGEXP_1 = "<content[^>]*>";
	private static final String EXCLUDE_REGEXP_0 = "<\\?xml version=\"1\\.0\" encoding=\"UTF-8\"\\?>";
	
	private static final String TEMP_CONTENT_XML_FILE = "/content.xml";
	
	protected static final String CAQS_OPENOFFICE_TEMP_PICTURES_DIR = "/Pictures";
	protected static final String CAQS_OPENOFFICE_TEMP_TEMPLATE_DIR = "template";
	protected static final String CAQS_OPENOFFICE_TEMPLATE_ZIP_PATH = "report/template/";
	protected static final String CAQS_OPENOFFICE_CONVERT_CMD_POST_KEY = "report.openoffice.cmd.post";
	protected static final String CAQS_OPENOFFICE_CONVERT_CMD_PRE_KEY = "report.openoffice.cmd.pre";
	
	ElementBean eltBean;
	String destination;
	String templateDir;
	MessageResources resources;
	Locale locale;

	public OpenOfficeFileGenerator(ElementBean eltBean, String destination, String templateDir, MessageResources resources, Locale locale) {
		this.eltBean = eltBean;
		this.destination = destination;
		this.templateDir = templateDir;
		this.resources = resources;
		this.locale = locale;
	}
	
	/**
	 * Create a file list to include into the destination report.
	 * @return the created list.
	 */
	protected abstract List<XmlFileInsertEntry> createFileInsertEntryList();
	
	/**
	 * Get the path to the template according to the given locale.
	 * @param loc the locale.
	 * @return the path to the template.
	 */
    protected abstract String getTemplatePath(Locale loc);

    protected String getTemplatePath(String templateName, Locale loc) {
    	StringBuffer result = new StringBuffer(OpenOfficeFileGenerator.CAQS_OPENOFFICE_TEMPLATE_ZIP_PATH);
    	result.append(templateName);
    	result.append('_').append(loc.getLanguage().toLowerCase());
    	result.append(ReportConstants.ZIP_FILE_EXT);
    	return result.toString();
    }

	protected String[] getRegexpExcludeList() {
    	String[] regexpExcludeList = new String[3];
    	regexpExcludeList[0] = OpenOfficeFileGenerator.EXCLUDE_REGEXP_0;
    	regexpExcludeList[1] = OpenOfficeFileGenerator.EXCLUDE_REGEXP_1;
    	regexpExcludeList[2] = OpenOfficeFileGenerator.EXCLUDE_REGEXP_2;
    	return regexpExcludeList;
	}
	
	public void generateFile() throws IOException {
    	List<XmlFileInsertEntry> l = createFileInsertEntryList();
    	FileTools.insertFiles(new File(destination + this.templateDir + OpenOfficeFileGenerator.TEMP_CONTENT_XML_FILE), l);
	}
	
	protected XmlFileInsertEntry createFileInsertEntry(String filePathFromDest, String regexp, String[] regexpExcludeList) {
    	XmlFileInsertEntry entry = new XmlFileInsertEntry();
    	entry.addFileToInsert(new File(destination + FileTools.FILE_SEPARATOR_SLASH + ReportConstants.CAQS_OPENOFFICE_XML_DIR + FileTools.FILE_SEPARATOR_SLASH + filePathFromDest + ReportConstants.XML_FILE_EXT));
    	entry.setRegexp(regexp);
    	entry.setExcludeRegexp(regexpExcludeList);
    	return entry;
	}
	
	protected XmlFileInsertEntry createFileInsertEntry(String filePathFromDest, int nbFiles, String regexp, String[] regexpExcludeList) {
    	XmlFileInsertEntry entry = new XmlFileInsertEntry();
    	for (int i = 0; i < nbFiles; i++) {
        	entry.addFileToInsert(new File(destination + FileTools.FILE_SEPARATOR_SLASH + ReportConstants.CAQS_OPENOFFICE_XML_DIR + FileTools.FILE_SEPARATOR_SLASH + filePathFromDest + i + ReportConstants.XML_FILE_EXT));
    	}
    	entry.setRegexp(regexp);
    	entry.setExcludeRegexp(regexpExcludeList);
    	return entry;
	}
	
	/**
	 * Prepare the OpenOffice report:
	 *   <BR/>- extract the template to a temporary directory.
	 *   <BR/>- insert images into the folder structure.
	 * @param destDir the destination directory.
	 * @param configPath the configuration path.
	 * @param loc the locale.
	 * @throws IOException
	 */
    public abstract void prepareOpenOfficeTemplate(String destDir, String configPath, Locale loc) throws IOException;
	
    protected void prepareOpenOfficeTemplate(String destDir, String configPath, String templatePath, String templateDefaultPath, String tempDir) throws IOException {
        try {
        	File tempZipFile = new File(FileTools.concatPath(configPath, templatePath));
        	if (!tempZipFile.exists()) {
            	tempZipFile = new File(FileTools.concatPath(configPath, templateDefaultPath));
        	}
        	FileTools.rdelete(new File(tempDir));
            FileTools.unzipToDir(tempDir, tempZipFile);
        } 
        catch(IOException e) { 
            //handle exception
        	logger.error("Error during report zip", e);
        	throw e;
        }
    }

    public File createWordDocument() throws SystemIOException, IOException {
    	AntExecutor executor = new AntExecutor(logger);
    	executor.processAntScript(getAntTask(), getProperties());
    	return generateWordDocument();
    }

    protected abstract String getAntTask();

    protected Properties getProperties() {
        Properties prop = new Properties();
        prop.put("baselineId", this.eltBean.getBaseline().getId());
        prop.put("id_elt", this.eltBean.getId()); 
        prop.put("lib_elt", this.eltBean.getLib()); 
        prop.put("id_pro", this.eltBean.getProject().getId()); 
        prop.put("lib_pro", this.eltBean.getProject().getLib()); 
        prop.put("id_usa", this.eltBean.getUsage().getId()); 
        prop.put("templateDir", templateDir);
        prop.put("fileName", getReportFileName(ReportConstants.OPENOFFICE_DOCUMENT_EXT));
        prop.put("langage", this.locale.getLanguage());
        Properties caqsProp = CaqsConfigUtil.getCaqsGlobalConfigProperties();
        prop.put("commonDir", new File(caqsProp.getProperty(Constants.CAQS_ALLDATAS_DIR)).getAbsolutePath());
        return prop;
    }

    /**
     * Get the report file path for the given file extension.
     * @param fileExt the file extension.
     * @returnthe report file path.
     */
    protected abstract String getReportFilePath(String fileExt);

    /**
     * Get the report file name for the given file extension.
     * @param fileExt the file extension.
     * @returnthe report file name.
     */
    protected abstract String getReportFileName(String fileExt);

    public File generateWordDocument() throws SystemIOException {
    	String configFile = CaqsConfigUtil.getLocalizedCaqsFile(Constants.CAQS_REPORT_PROPERTIES_FILE_PATH);
    	Properties reportProps = PropertiesReader.getProperties(configFile, this);
        StringBuffer command = new StringBuffer(reportProps.getProperty(OpenOfficeFileGenerator.CAQS_OPENOFFICE_CONVERT_CMD_PRE_KEY));
        command.append(getReportFilePath(ReportConstants.OPENOFFICE_DOCUMENT_EXT));
        command.append(reportProps.getProperty(OpenOfficeFileGenerator.CAQS_OPENOFFICE_CONVERT_CMD_POST_KEY));
        logger.debug("Generating word document");
        logger.debug(command.toString());
        SystemIO.exec(command.toString(), logger, null);
        return new File(getReportFilePath(ReportConstants.WORD_DOCUMENT_EXT));
    }

}
