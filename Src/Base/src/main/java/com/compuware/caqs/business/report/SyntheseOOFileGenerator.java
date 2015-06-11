/**
 * 
 */
package com.compuware.caqs.business.report;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;

import org.apache.struts.util.MessageResources;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;

import com.compuware.caqs.business.chart.config.ChartConfigGenerator;
import com.compuware.caqs.business.chart.factory.KiviatChartFactory;
import com.compuware.caqs.business.chart.factory.PieChartFactory;
import com.compuware.caqs.business.chart.factory.ScatterPlotChartFactory;
import com.compuware.caqs.business.chart.xml.XmlDatasetGenerator;
import com.compuware.caqs.business.consult.ActionPlan;
import com.compuware.caqs.business.consult.Baseline;
import com.compuware.caqs.business.consult.BottomUp;
import com.compuware.caqs.business.consult.Evolution;
import com.compuware.caqs.business.consult.ModelDefinition;
import com.compuware.caqs.business.consult.ScatterPlotSynthese;
import com.compuware.caqs.business.consult.Synthese;
import com.compuware.caqs.constants.Constants;
import com.compuware.caqs.domain.chart.config.ChartConfig;
import com.compuware.caqs.domain.dataschemas.BaselineBean;
import com.compuware.caqs.domain.dataschemas.ElementBean;
import com.compuware.caqs.domain.dataschemas.ElementType;
import com.compuware.caqs.domain.dataschemas.FactorBean;
import com.compuware.caqs.domain.dataschemas.FilterBean;
import com.compuware.caqs.domain.dataschemas.MetriqueDefinitionBean;
import com.compuware.caqs.domain.dataschemas.ScatterDataBean;
import com.compuware.caqs.domain.dataschemas.actionplan.ActionPlanBean;
import com.compuware.caqs.domain.dataschemas.actionplan.ActionPlanCriterionBean;
import com.compuware.caqs.domain.dataschemas.actionplan.ActionPlanPriority;
import com.compuware.caqs.domain.dataschemas.actionplan.list.ActionPlanElementBeanCollection;
import com.compuware.caqs.presentation.consult.actions.actionplan.util.ActionPlanKiviatUtils;
import com.compuware.caqs.util.CaqsConfigUtil;
import com.compuware.toolbox.dbms.JdbcDAOUtils;
import com.compuware.toolbox.io.FileTools;
import com.compuware.toolbox.io.PropertiesReader;
import com.compuware.toolbox.io.insert.XmlFileInsertEntry;
import java.util.Properties;

/**
 * @author cwfr-fdubois
 *
 */
public class SyntheseOOFileGenerator extends OpenOfficeFileGenerator {

    /* Report images constants. */
    private static final String IMAGE_PNG_EXT = ".png";
    private static final String SCATTER_PLOT_FILE_NAME = "scatterPlot";
    private static final int SCATTER_PLOT_IMAGE_HEIGHT = 400;
    private static final int SCATTER_PLOT_IMAGE_WIDTH = 600;
    public static final String KIVIAT_FILE_NAME = "kiviat";
    private static final int KIVIAT_IMAGE_HEIGHT = 350;
    private static final int KIVIAT_IMAGE_WIDTH = 400;
    public static final String ACTIONPLAN_KIVIAT_CML_FILE_NAME = "actionPlanKiviatCML";
    public static final String ACTIONPLAN_KIVIAT_CM_FILE_NAME = "actionPlanKiviatCM";
    public static final String ACTIONPLAN_KIVIAT_C_FILE_NAME = "actionPlanKiviatC";
    private static final int ACTIONPLAN_KIVIAT_IMAGE_HEIGHT = 350;
    private static final int ACTIONPLAN_KIVIAT_IMAGE_WIDTH = 400;
    public static final String EVOLUTION_OLD_KIVIAT_FILE_NAME = "evolutionOldKiviat";
    private static final int EVOLUTION_OLD_KIVIAT_IMAGE_HEIGHT = 350;
    private static final int EVOLUTION_OLD_KIVIAT_IMAGE_WIDTH = 400;
    public static final String EVOLUTION_OLD_ACTIONPLAN_KIVIAT_FILE_NAME = "evolutionOldActionPlanKiviat";
    private static final int EVOLUTION_OLD_ACTIONPLAN_KIVIAT_IMAGE_HEIGHT = 350;
    private static final int EVOLUTION_OLD_ACTIONPLAN_KIVIAT_IMAGE_WIDTH = 400;
    public static final String EVOLUTION_NEW_KIVIAT_FILE_NAME = "evolutionNewKiviat";
    private static final int EVOLUTION_NEW_KIVIAT_IMAGE_HEIGHT = 350;
    private static final int EVOLUTION_NEW_KIVIAT_IMAGE_WIDTH = 400;
    private static final String BOTTOMUP_FACT_FILE_NAME = "bottomup-fact";
    private static final String BOTTOMUP_CRIT_FILE_NAME = "bottomup-crit";
    private static final int BOTTOM_UP_IMAGE_HEIGHT = 300;
    private static final int BOTTOM_UP_IMAGE_WIDTH = 600;
    private static final String EVOLUTION_REPARTITION_NEW_BAD_FILE_NAME = "repartition-EvolNewBad";
    private static final String EVOLUTION_REPARTITION_OLD_WORST_FILE_NAME = "repartition-EvolOldWorst";
    private static final String EVOLUTION_REPARTITION_OLD_BETTER_FILE_NAME = "repartition-EvolOldBetter";
    private static final String EVOLUTION_REPARTITION_OLD_BETTER_WORST_FILE_NAME = "repartition-EvolOldBetterWorst";
    private static final String EVOLUTION_REPARTITION_OLD_BADSTABLE_FILE_NAME = "repartition-EvolOldBadStable";
    private static final int EVOLUTION_REPARTITION_IMAGE_HEIGHT = 300;
    private static final int EVOLUTION_REPARTITION_IMAGE_WIDTH = 600;
    private static final String INSERT_REGEXP_SYNTHESE_OBJECTIFS = "[ ]*<text:p text:style-name=\"Standard\">@SYNTHESE-OBJECTIFS[ ]*</text:p>[ ]*";
    private static final String INSERT_REGEXP_SYNTHESE_BOTTOMUP = "[ ]*<text:p text:style-name=\"Standard\">@SYNTHESE-BOTTOMUP[ ]*</text:p>[ ]*";
    private static final String INSERT_REGEXP_SYNTHESE_EVOLUTION = "[ ]*<text:p text:style-name=\"Standard\">@SYNTHESE-EVOLUTION[ ]*</text:p>[ ]*";
    private static final String INSERT_REGEXP_SYNTHESE_ACTIONPLAN = "[ ]*<text:p text:style-name=\"Standard\">@SYNTHESE-ACTIONPLAN[ ]*</text:p>[ ]*";
    private static final String INSERT_REGEXP_SYNTHESE = "[ ]*<text:p text:style-name=\"Standard\">@SYNTHESE[ ]*</text:p>[ ]*";
    private static final String INSERT_REGEXP_GENERAL_INFOS = "[ ]*<text:p text:style-name=\"Standard\">@GENERAL-INFOS[ ]*</text:p>[ ]*";
    private static final String INSERT_REGEXP_TITLE = "[ ]*<text:p text:style-name=\"P2\">@TITLE</text:p>[ ]*";
    private static final String INSERT_REGEXP_STYLES = "[ ]*<office:automatic-styles>@CONTENT-STYLES</office:automatic-styles>[ ]*";
    private static final String INSERT_REGEXP_SYNTHESE_JUSTIFICATIONS = "[ ]*<text:p text:style-name=\"Standard\">@SYNTHESE-JUSTIFICATIONS</text:p>[ ]*";
    private static final String CAQS_OPENOFFICE_TEMPLATE_ZIP_NAME = "template";
    private static final String CAQS_OPENOFFICE_ZIP_ANT_TASK = "createSyntheseOpenOfficeDocument";

    public SyntheseOOFileGenerator(ElementBean eltBean, String destination, MessageResources resources, Locale locale) {
        super(eltBean, destination, OpenOfficeFileGenerator.CAQS_OPENOFFICE_TEMP_TEMPLATE_DIR, resources, locale);
    }

    public SyntheseOOFileGenerator(ElementBean eltBean, String destination, String templateDir, MessageResources resources, Locale locale) {
        super(eltBean, destination, templateDir, resources, locale);
    }

    public void prepareOpenOfficeTemplate(String destDir, String configPath, Locale loc) throws IOException {
        String tempDir = FileTools.concatPath(destDir, templateDir);
        String templatePath = getTemplatePath(loc);
        String templateDefaultPath = getTemplatePath(Locale.getDefault());
        prepareOpenOfficeTemplate(destDir, configPath, templatePath, templateDefaultPath, tempDir);
        extractKiviatImage(destination + templateDir, eltBean, loc);
        if (ElementType.EA.equalsIgnoreCase(eltBean.getTypeElt())) {
            extractBottomUpImage(destination + templateDir, eltBean, loc);
            // added by laurent.izac@compuware.com on april 11, 2006
            extractScatterPlotImage(destination + templateDir, eltBean, loc);
            extractEvolutionRepartitionImage(destination + templateDir, eltBean, loc);
            this.extractActionPlanKiviatImages(destination + templateDir, eltBean, loc);
            extractActionPlanEvolutionKiviatImage(destination + templateDir, eltBean, loc);
        }
    }

    protected String getTemplatePath(Locale loc) {
        return getTemplatePath(SyntheseOOFileGenerator.CAQS_OPENOFFICE_TEMPLATE_ZIP_NAME, loc);
    }

    /* (non-Javadoc)
     * @see com.compuware.caqs.business.report.OpenOfficeFileGenerator#createFileInsertEntryList()
     */
    protected List<XmlFileInsertEntry> createFileInsertEntryList() {
        List<XmlFileInsertEntry> result = new ArrayList<XmlFileInsertEntry>();
        String[] regexpExcludeList = getRegexpExcludeList();
        result.add(createFileInsertEntry(ReportConstants.STYLES_XML_FILE_NAME, SyntheseOOFileGenerator.INSERT_REGEXP_STYLES, regexpExcludeList));
        result.add(createFileInsertEntry(ReportConstants.GENERAL_XML_FILE_NAME, SyntheseOOFileGenerator.INSERT_REGEXP_TITLE, regexpExcludeList));
        result.add(createFileInsertEntry(ReportConstants.GENERAL_OO_FILE_NAME, SyntheseOOFileGenerator.INSERT_REGEXP_GENERAL_INFOS, regexpExcludeList));
        result.add(createFileInsertEntry(ReportConstants.SYNTHESE_XML_FILE_NAME, SyntheseOOFileGenerator.INSERT_REGEXP_SYNTHESE, regexpExcludeList));
        result.add(createFileInsertEntry(ReportConstants.SYNTHESE_BOTTOMUP_XML_FILE_NAME, SyntheseOOFileGenerator.INSERT_REGEXP_SYNTHESE_BOTTOMUP, regexpExcludeList));
        result.add(createFileInsertEntry(ReportConstants.EVOLUTION_XML_FILE_NAME, SyntheseOOFileGenerator.INSERT_REGEXP_SYNTHESE_EVOLUTION, regexpExcludeList));
        result.add(createFileInsertEntry(ReportConstants.SYNTHESE_ACTIONPLAN_XML_FILE_NAME, SyntheseOOFileGenerator.INSERT_REGEXP_SYNTHESE_ACTIONPLAN, regexpExcludeList));
        result.add(createFileInsertEntry(ReportConstants.SYNTHESE_OBJECTIFS_XML_FILE_NAME, SyntheseOOFileGenerator.INSERT_REGEXP_SYNTHESE_OBJECTIFS, regexpExcludeList));
        result.add(createFileInsertEntry(ReportConstants.SYNTHESE_JUSTIFICATIONS_XML_FILE_NAME, SyntheseOOFileGenerator.INSERT_REGEXP_SYNTHESE_JUSTIFICATIONS, regexpExcludeList));
        return result;
    }
    private static final String KIVIAT_TITLE_MSG = "caqs.synthese.kiviat";

    private void extractKiviatImage(String destDir, ElementBean eltBean, Locale loc) {
        try {
            Synthese synthese = new Synthese();
            Collection<FactorBean> result = synthese.retrieveKiviat(eltBean);
            StringBuffer out = new StringBuffer();
            XmlDatasetGenerator.writeFactorData(out, result, loc, resources);
            InputStream str = new ByteArrayInputStream(out.toString().getBytes(Constants.GLOBAL_CHARSET));
            ChartConfig cfg = ChartConfigGenerator.getKiviatConfig();
            cfg.setTitle(resources.getMessage(loc, SyntheseOOFileGenerator.KIVIAT_TITLE_MSG));
            JFreeChart kiviatChart = KiviatChartFactory.createFromXml(str, cfg);
            saveChartAsImage(destDir, kiviatChart, SyntheseOOFileGenerator.KIVIAT_FILE_NAME, SyntheseOOFileGenerator.KIVIAT_IMAGE_WIDTH, SyntheseOOFileGenerator.KIVIAT_IMAGE_HEIGHT);
        } catch (IOException e) {
            logger.error("Error generating spider chart", e);
        }
    }

    private void extractActionPlanKiviatImages(String destDir, ElementBean eltBean, Locale loc) {
        String fullDestDir = destDir +
                OpenOfficeFileGenerator.CAQS_OPENOFFICE_TEMP_PICTURES_DIR +
                FileTools.FILE_SEPARATOR_SLASH_STR;
        String fileName = SyntheseOOFileGenerator.ACTIONPLAN_KIVIAT_CML_FILE_NAME +
                SyntheseOOFileGenerator.IMAGE_PNG_EXT;
        ActionPlanKiviatUtils.getInstance().getKiviatImageInFileForReport(
                fullDestDir, fileName, eltBean, eltBean.getBaseline().getId(), ActionPlanPriority.LONG_TERM, null, SyntheseOOFileGenerator.ACTIONPLAN_KIVIAT_IMAGE_WIDTH,
                SyntheseOOFileGenerator.ACTIONPLAN_KIVIAT_IMAGE_HEIGHT, false, resources, loc);
        fileName = SyntheseOOFileGenerator.ACTIONPLAN_KIVIAT_CM_FILE_NAME +
                SyntheseOOFileGenerator.IMAGE_PNG_EXT;
        ActionPlanKiviatUtils.getInstance().getKiviatImageInFileForReport(
                fullDestDir, fileName, eltBean, eltBean.getBaseline().getId(), ActionPlanPriority.MEDIUM_TERM, null, SyntheseOOFileGenerator.ACTIONPLAN_KIVIAT_IMAGE_WIDTH,
                SyntheseOOFileGenerator.ACTIONPLAN_KIVIAT_IMAGE_HEIGHT, false, resources, loc);
        fileName = SyntheseOOFileGenerator.ACTIONPLAN_KIVIAT_C_FILE_NAME +
                SyntheseOOFileGenerator.IMAGE_PNG_EXT;
        ActionPlanKiviatUtils.getInstance().getKiviatImageInFileForReport(
                fullDestDir, fileName, eltBean, eltBean.getBaseline().getId(), ActionPlanPriority.SHORT_TERM, null, SyntheseOOFileGenerator.ACTIONPLAN_KIVIAT_IMAGE_WIDTH,
                SyntheseOOFileGenerator.ACTIONPLAN_KIVIAT_IMAGE_HEIGHT, false, resources, loc);
    }

    private void extractActionPlanEvolutionKiviatImage(String destDir, ElementBean eltBean, Locale loc) {
        Baseline baselinesvc = new Baseline();
        ActionPlan actionplansvc = new ActionPlan();

        BaselineBean bline = baselinesvc.getPreviousBaseline(eltBean.getBaseline(), eltBean.getId());

        if (bline != null) {
            boolean hasPreviousActionPlan = false;
            ActionPlanElementBeanCollection<ActionPlanCriterionBean> actionPlanPreviousBaseline = null;
            ActionPlanBean ap = actionplansvc.getCompleteActionPlan(eltBean, bline.getId());
            actionPlanPreviousBaseline = ap.getCorrectedElements();
            hasPreviousActionPlan = !actionPlanPreviousBaseline.isEmpty();

            if (hasPreviousActionPlan) {
                String fullDestDir = destDir +
                        OpenOfficeFileGenerator.CAQS_OPENOFFICE_TEMP_PICTURES_DIR +
                        FileTools.FILE_SEPARATOR_SLASH_STR;
                //String fileName = SyntheseOOFileGenerator.EVOLUTION_OLD_KIVIAT_FILE_NAME + SyntheseOOFileGenerator.IMAGE_PNG_EXT;
                //on recupere le kiviat de l'ancienne baseline
                ElementBean oldEltBean = new ElementBean();
                oldEltBean.setId(eltBean.getId());
                oldEltBean.setProject(eltBean.getProject());
                oldEltBean.setBaseline(bline);
                oldEltBean.setTypeElt(eltBean.getTypeElt());
                try {
                    Synthese synthese = new Synthese();
                    Collection<FactorBean> result = synthese.retrieveKiviat(oldEltBean);
                    StringBuffer out = new StringBuffer();
                    XmlDatasetGenerator.writeFactorData(out, result, loc, resources);
                    InputStream str = new ByteArrayInputStream(out.toString().getBytes(Constants.GLOBAL_CHARSET));
                    ChartConfig cfg = ChartConfigGenerator.getKiviatConfig();
                    cfg.setTitle(resources.getMessage(loc, "caqs.evolution.synthese.kiviat"));
                    JFreeChart kiviatChart = KiviatChartFactory.createFromXml(str, cfg);
                    saveChartAsImage(destDir, kiviatChart, SyntheseOOFileGenerator.EVOLUTION_OLD_KIVIAT_FILE_NAME,
                            SyntheseOOFileGenerator.EVOLUTION_OLD_KIVIAT_IMAGE_WIDTH, SyntheseOOFileGenerator.EVOLUTION_OLD_KIVIAT_IMAGE_HEIGHT);
                } catch (IOException e) {
                    logger.error("Error generating spider chart", e);
                }

                try {
                    Synthese synthese = new Synthese();
                    Collection<FactorBean> result = synthese.retrieveKiviat(eltBean);
                    StringBuffer out = new StringBuffer();
                    XmlDatasetGenerator.writeFactorData(out, result, loc, resources);
                    InputStream str = new ByteArrayInputStream(out.toString().getBytes(Constants.GLOBAL_CHARSET));
                    ChartConfig cfg = ChartConfigGenerator.getKiviatConfig();
                    cfg.setTitle(resources.getMessage(loc, "caqs.evolution.synthese.kiviat"));
                    JFreeChart kiviatChart = KiviatChartFactory.createFromXml(str, cfg);
                    saveChartAsImage(destDir, kiviatChart, SyntheseOOFileGenerator.EVOLUTION_NEW_KIVIAT_FILE_NAME,
                            SyntheseOOFileGenerator.EVOLUTION_NEW_KIVIAT_IMAGE_WIDTH, SyntheseOOFileGenerator.EVOLUTION_NEW_KIVIAT_IMAGE_HEIGHT);
                } catch (IOException e) {
                    logger.error("Error generating spider chart", e);
                }


                String fileName = SyntheseOOFileGenerator.EVOLUTION_OLD_ACTIONPLAN_KIVIAT_FILE_NAME +
                        SyntheseOOFileGenerator.IMAGE_PNG_EXT;
                ActionPlanKiviatUtils.getInstance().getKiviatImageInFileForReport(
                        fullDestDir, fileName, oldEltBean, oldEltBean.getBaseline().getId(), ActionPlanPriority.LONG_TERM, null, SyntheseOOFileGenerator.EVOLUTION_OLD_ACTIONPLAN_KIVIAT_IMAGE_WIDTH,
                        SyntheseOOFileGenerator.EVOLUTION_OLD_ACTIONPLAN_KIVIAT_IMAGE_HEIGHT, false, resources, loc);
            }
        }
    }

    private void extractScatterPlotImage(String destDir, ElementBean eltBean, Locale loc) {
        try {
            ScatterPlotSynthese scatterPlotSynthese = new ScatterPlotSynthese();
            String dynamicPropFileName = CaqsConfigUtil.getLocalizedCaqsFile(Constants.CAQS_DYNAMIC_CONFIG_FILE_PATH);
            Properties dynProp = PropertiesReader.getProperties(dynamicPropFileName, this, false);
            String metH = dynProp.getProperty(Constants.SCATTERPLOT_X + eltBean.getDialecte().getLangage().getId());
            String metV = dynProp.getProperty(Constants.SCATTERPLOT_Y + eltBean.getDialecte().getLangage().getId());
            String centerMetH = dynProp.getProperty(Constants.SCATTERPLOT_CENTER_X + eltBean.getDialecte().getLangage().getId());
            String centerMetV = dynProp.getProperty(Constants.SCATTERPLOT_CENTER_Y + eltBean.getDialecte().getLangage().getId());
            String telt = dynProp.getProperty(Constants.DEFAULT_ELEMENT_TYPE + eltBean.getDialecte().getLangage().getId());
            List<ScatterDataBean> result = scatterPlotSynthese.retrieveScatterPlot(eltBean, metH,
                    metV, telt);
            String str = scatterPlotSynthese.getScatterBytes(result);

            ModelDefinition md = new ModelDefinition();
            MetriqueDefinitionBean metHBean = md.retrieveMetriqueDefinition(metH);
            MetriqueDefinitionBean metVBean = md.retrieveMetriqueDefinition(metV);
            ChartConfig config = ChartConfigGenerator.getScatterPlotConfig();
            config.setCenterX(Integer.parseInt(centerMetH));
            config.setCenterY(Integer.parseInt(centerMetV));
            JFreeChart scatterPlotChart = ScatterPlotChartFactory.createFromString(str,
                    config,
                    metHBean.getLib(loc), metVBean.getLib(loc), loc);
            saveChartAsImage(destDir, scatterPlotChart, SyntheseOOFileGenerator.SCATTER_PLOT_FILE_NAME, SyntheseOOFileGenerator.SCATTER_PLOT_IMAGE_WIDTH, SyntheseOOFileGenerator.SCATTER_PLOT_IMAGE_HEIGHT);
        } catch (IOException e) {
            logger.error("Error generating scatterplot chart", e);
        }
    }

    private void extractBottomUpImage(String destDir, ElementBean eltBean, Locale loc) {
        Collection repartitionCrit = null;
        Collection repartitionFact = null;
        try {
            BottomUp bottomUp = new BottomUp();
            repartitionCrit = bottomUp.retrieveRepartitionByCriterion(eltBean, JdbcDAOUtils.DATABASE_STRING_NOFILTER, JdbcDAOUtils.DATABASE_STRING_NOFILTER);
            extractBottomUpImage(destDir, SyntheseOOFileGenerator.BOTTOMUP_CRIT_FILE_NAME, repartitionCrit, loc);
            repartitionFact = bottomUp.retrieveRepartitionByFactor(eltBean, JdbcDAOUtils.DATABASE_STRING_NOFILTER, JdbcDAOUtils.DATABASE_STRING_NOFILTER);
            extractBottomUpImage(destDir, SyntheseOOFileGenerator.BOTTOMUP_FACT_FILE_NAME, repartitionFact, loc);
        } catch (IOException e) {
            logger.error("Error generating pie chart", e);
        }
    }

    private void extractBottomUpImage(String destDir, String name, Collection repartition, Locale loc) throws IOException {
        StringBuffer xmlout = new StringBuffer();
        XmlDatasetGenerator.writeRepartitionData(xmlout, repartition, loc);
        InputStream str = new ByteArrayInputStream(xmlout.toString().getBytes(Constants.GLOBAL_CHARSET));
        JFreeChart pieChart = PieChartFactory.createFromXml(str, ChartConfigGenerator.getPieConfig("", true));
        saveChartAsImage(destDir, pieChart, name, SyntheseOOFileGenerator.BOTTOM_UP_IMAGE_WIDTH, SyntheseOOFileGenerator.BOTTOM_UP_IMAGE_HEIGHT);
    }

    private void extractEvolutionRepartitionImage(String destDir, ElementBean eltBean, Locale loc) {
        try {
            Evolution evolution = new Evolution();
            FilterBean filter = new FilterBean();
            Collection repartitionNewAndBadElements = evolution.retrieveRepartitionNewAndBadElements(eltBean.getId(), eltBean.getBaseline().getId(), filter);
            Collection repartitionOldAndWorstElements = evolution.retrieveRepartitionOldAndWorstElements(eltBean.getId(), eltBean.getBaseline().getId(), filter);
            Collection repartitionOldAndBetterElements = evolution.retrieveRepartitionOldAndBetterElements(eltBean.getId(), eltBean.getBaseline().getId(), filter);
            Collection repartitionOldAndBetterWorstElements = evolution.retrieveRepartitionOldBetterWorstElements(eltBean.getId(), eltBean.getBaseline().getId(), filter);
            Collection repartitionOldBadStableElements = evolution.retrieveRepartitionBadStableElements(eltBean.getId(), eltBean.getBaseline().getId(), filter);

            extractEvolutionRepartitionImage(destDir, SyntheseOOFileGenerator.EVOLUTION_REPARTITION_NEW_BAD_FILE_NAME, repartitionNewAndBadElements, resources.getMessage(loc, SyntheseOOFileGenerator.EVOLUTION_REPARTITION_NEW_BAD_MSG), loc);
            extractEvolutionRepartitionImage(destDir, SyntheseOOFileGenerator.EVOLUTION_REPARTITION_OLD_WORST_FILE_NAME, repartitionOldAndWorstElements, resources.getMessage(loc, SyntheseOOFileGenerator.EVOLUTION_REPARTITION_OLD_WORST_MSG), loc);
            extractEvolutionRepartitionImage(destDir, SyntheseOOFileGenerator.EVOLUTION_REPARTITION_OLD_BETTER_FILE_NAME, repartitionOldAndBetterElements, resources.getMessage(loc, SyntheseOOFileGenerator.EVOLUTION_REPARTITION_OLD_BETTER_MSG), loc);
            extractEvolutionRepartitionImage(destDir, SyntheseOOFileGenerator.EVOLUTION_REPARTITION_OLD_BETTER_WORST_FILE_NAME, repartitionOldAndBetterWorstElements, resources.getMessage(loc, SyntheseOOFileGenerator.EVOLUTION_REPARTITION_OLD_BETTER_WORST_MSG), loc);
            extractEvolutionRepartitionImage(destDir, SyntheseOOFileGenerator.EVOLUTION_REPARTITION_OLD_BADSTABLE_FILE_NAME, repartitionOldBadStableElements, resources.getMessage(loc, SyntheseOOFileGenerator.EVOLUTION_REPARTITION_OLD_BADSTABLE_MSG), loc);
        } catch (IOException e) {
            logger.error("Error generating pie chart", e);
        }
    }
    private static final String EVOLUTION_REPARTITION_NEW_BAD_MSG = "caqs.evolution.repartition.newBad";
    private static final String EVOLUTION_REPARTITION_OLD_WORST_MSG = "caqs.evolution.repartition.oldWorst";
    private static final String EVOLUTION_REPARTITION_OLD_BETTER_MSG = "caqs.evolution.repartition.oldBetter";
    private static final String EVOLUTION_REPARTITION_OLD_BETTER_WORST_MSG = "caqs.evolution.repartition.oldBetterWorst";
    private static final String EVOLUTION_REPARTITION_OLD_BADSTABLE_MSG = "caqs.evolution.repartition.badStable";

    private void extractEvolutionRepartitionImage(String destDir, String name, Collection repartition, String msg, Locale loc) throws IOException {
        StringBuffer xmlout = new StringBuffer();
        XmlDatasetGenerator.writeRepartitionData(xmlout, repartition, loc);
        InputStream str = new ByteArrayInputStream(xmlout.toString().getBytes(Constants.GLOBAL_CHARSET));
        JFreeChart pieChart = PieChartFactory.createFromXml(str, ChartConfigGenerator.getPieConfig(msg, true));
        saveChartAsImage(destDir, pieChart, name, SyntheseOOFileGenerator.EVOLUTION_REPARTITION_IMAGE_WIDTH, SyntheseOOFileGenerator.EVOLUTION_REPARTITION_IMAGE_HEIGHT);
    }

    private void saveChartAsImage(String destDir, JFreeChart chart, String name, int width, int height) throws IOException {
        File tempFile = new File(destDir +
                OpenOfficeFileGenerator.CAQS_OPENOFFICE_TEMP_PICTURES_DIR +
                FileTools.FILE_SEPARATOR_SLASH_STR + name +
                SyntheseOOFileGenerator.IMAGE_PNG_EXT);
        ChartUtilities.saveChartAsPNG(tempFile, chart, width, height);
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
        StringBuffer result = new StringBuffer("Synthese-");
        result.append(this.eltBean.getProject().getLib());
        result.append('-').append(this.eltBean.getLib()).append(fileExt);
        return result.toString();
    }

    public String getAntTask() {
        return SyntheseOOFileGenerator.CAQS_OPENOFFICE_ZIP_ANT_TASK;
    }
}
