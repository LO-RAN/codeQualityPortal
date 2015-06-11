/**
 * 
 */
package com.compuware.caqs.business.report;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.apache.struts.util.MessageResources;

import com.compuware.caqs.domain.dataschemas.ElementBean;
import com.compuware.toolbox.io.FileTools;

/**
 * @author cwfr-fdubois
 *
 */
public class AnnexesOOFileGenerator extends OpenOfficeFileGenerator {

    private static final String INSERT_REGEXP_STYLES = "[ ]*<office:automatic-styles>@CONTENT-STYLES</office:automatic-styles>[ ]*";
    private static final String INSERT_REGEXP_DEFINITIONS_METRIQUES = "[ ]*<text:p text:style-name=\"Standard\">@DEFINITIONS-METRIQUES[ ]*</text:p>[ ]*";
    private static final String INSERT_REGEXP_DEFINITIONS_IFPUG = "[ ]*<text:p text:style-name=\"Standard\">@DEFINITIONS-IFPUG[ ]*</text:p>[ ]*";
    private static final String INSERT_REGEXP_DEFINITIONS_CRITERES = "[ ]*<text:p text:style-name=\"Standard\">@DEFINITIONS-CRITERES[ ]*</text:p>[ ]*";
    private static final String INSERT_REGEXP_TITLE = "[ ]*<text:p text:style-name=\"P3\">@TITLE</text:p>[ ]*";
    protected static final String CAQS_OPENOFFICE_ZIP_ANT_TASK = "createAnnexesOpenOfficeDocument";
    private static final String CAQS_OPENOFFICE_TEMP_ANNEXES_DIR = "annexes";
    public static final String CAQS_OPENOFFICE_ANNEXES_TEMPLATE_ZIP_NAME = "annexes";

    public AnnexesOOFileGenerator(ElementBean eltBean, String destination, MessageResources resources, Locale locale) {
        super(eltBean, destination, AnnexesOOFileGenerator.CAQS_OPENOFFICE_TEMP_ANNEXES_DIR, resources, locale);
    }

    public void prepareOpenOfficeTemplate(String destDir, String configPath, Locale loc) throws IOException {
        String tempDir = FileTools.concatPath(destDir, AnnexesOOFileGenerator.CAQS_OPENOFFICE_TEMP_ANNEXES_DIR);
        String templatePath = getTemplatePath(loc);
        String templateDefaultPath = getTemplatePath(Locale.getDefault());
        prepareOpenOfficeTemplate(destDir, configPath, templatePath, templateDefaultPath, tempDir);
    }

    protected String getTemplatePath(Locale loc) {
        return getTemplatePath(AnnexesOOFileGenerator.CAQS_OPENOFFICE_ANNEXES_TEMPLATE_ZIP_NAME, loc);
    }

    /* (non-Javadoc)
     * @see com.compuware.caqs.business.report.OpenOfficeFileGenerator#createFileInsertEntryList()
     */
    protected List createFileInsertEntryList() {
        List result = new ArrayList();
        String[] regexpExcludeList = getRegexpExcludeList();
        result.add(createFileInsertEntry(ReportConstants.STYLES_XML_FILE_NAME, AnnexesOOFileGenerator.INSERT_REGEXP_STYLES, regexpExcludeList));
        result.add(createFileInsertEntry(ReportConstants.ANNEXE_TITLE_XML_FILE_NAME, AnnexesOOFileGenerator.INSERT_REGEXP_TITLE, regexpExcludeList));
        result.add(createFileInsertEntry(ReportConstants.IFPUG_DEFINITION_XML_FILE_NAME, AnnexesOOFileGenerator.INSERT_REGEXP_DEFINITIONS_IFPUG, regexpExcludeList));
        result.add(createFileInsertEntry(ReportConstants.CRITERION_DEFINITION_XML_FILE_NAME, AnnexesOOFileGenerator.INSERT_REGEXP_DEFINITIONS_CRITERES, regexpExcludeList));
        result.add(createFileInsertEntry(ReportConstants.METRIC_DEFINITION_XML_FILE_NAME, AnnexesOOFileGenerator.INSERT_REGEXP_DEFINITIONS_METRIQUES, regexpExcludeList));
        return result;
    }

    public String getReportFilePath(String fileExt) {
        StringBuffer result = new StringBuffer(this.destination);
        if (!FileTools.endsWithFileSeparator(this.destination)) {
            result.append(FileTools.FILE_SEPARATOR_SLASH);
        }
        result.append(getReportFileName(fileExt));
        return result.toString();
    }

    public String getReportFileName(String fileExt) {
        return "annexes-"+this.eltBean.getUsage().getId() + fileExt;
    }

    public String getAntTask() {
        return AnnexesOOFileGenerator.CAQS_OPENOFFICE_ZIP_ANT_TASK;
    }
}
