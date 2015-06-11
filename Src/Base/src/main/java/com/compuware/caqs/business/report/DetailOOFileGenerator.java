/**
 * 
 */
package com.compuware.caqs.business.report;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.apache.struts.util.MessageResources;

import com.compuware.caqs.domain.dataschemas.ElementBean;
import com.compuware.toolbox.io.FileTools;
import com.compuware.toolbox.io.insert.XmlFileInsertEntry;

/**
 * @author cwfr-fdubois
 *
 */
public class DetailOOFileGenerator extends SyntheseOOFileGenerator {

    private static final String INSERT_REGEXP_CRITERION_DETAILS = "[ ]*<text:p text:style-name=\"Standard\">@CRITERION-DETAILS[ ]*</text:p>[ ]*";
    private static final String INSERT_REGEXP_ACTIONPLAN_DETAILS = "[ ]*<text:p text:style-name=\"Standard\">@ACTIONPLAN-DETAILS[ ]*</text:p>[ ]*";
    private static final String INSERT_REGEXP_EVOLUTION_DETAILS = "[ ]*<text:p text:style-name=\"Standard\">@EVOLUTION-DETAILS[ ]*</text:p>[ ]*";
    private static final String INSERT_REGEXP_ACTIONPLAN_EVOLUTION_DETAILS = "[ ]*<text:p text:style-name=\"Standard\">@ACTIONPLAN-EVOLUTION-DETAILS[ ]*</text:p>[ ]*";
    private static final String CAQS_OPENOFFICE_TEMP_DETAIL_DIR = "detail";
    private static final String CAQS_OPENOFFICE_DETAIL_ZIP_NAME = "detail";
    private static final String CAQS_OPENOFFICE_ZIP_ANT_TASK = "createDetailOpenOfficeDocument";

    public DetailOOFileGenerator(ElementBean eltBean, String destination, MessageResources resources, Locale locale) {
        super(eltBean, destination, DetailOOFileGenerator.CAQS_OPENOFFICE_TEMP_DETAIL_DIR, resources, locale);
    }

    protected String getTemplatePath(Locale loc) {
        return getTemplatePath(DetailOOFileGenerator.CAQS_OPENOFFICE_DETAIL_ZIP_NAME, loc);
    }

    /* (non-Javadoc)
     * @see com.compuware.caqs.business.report.OpenOfficeFileGenerator#createFileInsertEntryList()
     */
    protected List<XmlFileInsertEntry> createFileInsertEntryList() {
        List<XmlFileInsertEntry> result = new ArrayList<XmlFileInsertEntry>();
        String[] regexpExcludeList = getRegexpExcludeList();
        result.addAll(super.createFileInsertEntryList());
        result.add(createFileInsertEntry(ReportConstants.ACTIONPLAN_DETAILS_XML_FILE_NAME, DetailOOFileGenerator.INSERT_REGEXP_ACTIONPLAN_DETAILS, regexpExcludeList));
        result.add(createFileInsertEntry(ReportConstants.EVOLUTION_DETAILS_XML_FILE_NAME, DetailOOFileGenerator.INSERT_REGEXP_EVOLUTION_DETAILS, regexpExcludeList));
        result.add(createFileInsertEntry(ReportConstants.ACTIONPLAN_EVOLUTION_DETAILS_XML_FILE_NAME, DetailOOFileGenerator.INSERT_REGEXP_ACTIONPLAN_EVOLUTION_DETAILS, regexpExcludeList));
        int nbCriterionDetail = 1;
        if (eltBean.getInfo2() != null && eltBean.getInfo2().length() > 0) {
            nbCriterionDetail = eltBean.getInfo2().split("\\|").length;
        }
        result.add(createFileInsertEntry(ReportConstants.CRITERION_DETAILS_XML_FILE_NAME, nbCriterionDetail, DetailOOFileGenerator.INSERT_REGEXP_CRITERION_DETAILS, regexpExcludeList));
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
        StringBuffer result = new StringBuffer("Detail-");
        result.append(this.eltBean.getProject().getLib());
        result.append('-').append(this.eltBean.getLib()).append(fileExt);
        return result.toString();
    }

    public String getAntTask() {
        return DetailOOFileGenerator.CAQS_OPENOFFICE_ZIP_ANT_TASK;
    }
}
