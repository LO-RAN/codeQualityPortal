package com.compuware.caqs.constants;

/**
 * Created by IntelliJ IDEA.
 * User: cwfr-fdubois
 * Date: 1 dec. 2005
 * Time: 16:51:45
 * To change this template use File | Settings | File Templates.
 */
public class Constants {

    public static final String OS_NAME = System.getProperty("os.name");
    public static final String OS_SPECIFIC_BATCH_EXTENSION = OS_NAME.compareToIgnoreCase("WINDOWS") >=
            0 ? ".bat" : ".sh";
    public static final String OS_SPECIFIC_DOUBLE_QUOTE = OS_NAME.compareToIgnoreCase("WINDOWS") >=
            0 ? "\"" : "";
    /* ------------------------------------------- */
    /* Constantes d'acces a la configuration CAQS. */
    /* ------------------------------------------- */
    public static final String CONFIG_FILE_PATH = "/configPortailQualite.conf";

    /* ---------------------------- */
    /* Constantes pour l'acces JNDI */
    /* ---------------------------- */
    public static final String CAQS_HOME_KEY = "caqs.home";
    public static final String CAQS_HOME_DEFAULT = "/CAQS";
    public static final String CAQS_CONFIG_DIR_VALUE = "/config";

    /* Acces aux fichiers de configuration des rapports. */
    public static final String CAQS_REPORT_PROPERTIES_FILE_PATH = "/config/report.properties";
    public static final String CAQS_REPORT_DEST_KEY = "report.destination";
    public static final String CAQS_REPORT_NOTE_SEUIL_KEY = "report.note_seuil";
    //nom de la propriete definissant l'adresse du serveur de generation du rapport
    public static final String CAQS_REPORT_GENERATOR_ADDRESS = "caqs.report.serverAddress";
    //nom de la propriete definissant le repertoire partage avec les donnees caqs
    public static final String CAQS_ALLDATAS_DIR = "caqs.allDatasDir";
    //nom de la propriete definissant l'adresse du serveur de calcul
    public static final String CAQS_COMPUTE_ADDRESS = "caqs.compute.serverAddress";
    //nom de la propriete definissant l'adresse du serveur d'export
    public static final String CAQS_EXPORT_ADDRESS = "caqs.export.serverAddress";
    //nom de la propriete definissant l'adresse du serveur d'export
    public static final String CAQS_IMPORT_ADDRESS = "caqs.import.serverAddress";

    /**
     * Personnalisations d'entete du portail caqs
     */
    public static final String CAQS_CUSTOM_HEADER_LEFT= "caqs.customHeader.left";
    public static final String CAQS_CUSTOM_HEADER_RIGHT = "caqs.customHeader.right";

    /* ------------------------------------------------- */
    /* Constantes d'acces au fichier de config du cache. */
    /* ------------------------------------------------- */
    public static final String CAQS_CACHE_CONFIG_FILE_NAME = "cache.ccf";

    /* Constantes de l'application CAQS. */
    public static final String CAQS_TRAITEMENT_PATH = "Traitements";
    public static final String CAQS_DYNAMIC_CONFIG_FILE = "caqsDynamicScriptConfigurationFilePath";
    public static final String CAQS_DYNAMIC_CONFIG_FILE_PATH = CAQS_TRAITEMENT_PATH +
            "/Configuration/conf.txt";
    public static final String TARGET_DIRECTORY_KEY = "targetDirectory";
    public static final String XML_TEMP_DIRECTORY_KEY = "xmlTempDirectory";
    public static final String WEB_DATA_DIRECTORY_KEY = "webDataAbsolutePath";
    public static final String UPLOAD_DIRECTORY_KEY = "upload";
    public static final String UPLOAD_KNOWLEDGE_DIRECTORY_KEY = "knowledge";
    public static final String EXTRACTOR_SCRIPT_PATH_KEY = "extractorScriptPath";
    public static final String EXTRACTOR_CONFIG_FILE_PATH_KEY = "extractorConfigurationFilePath";
    public static final String CLEAR_TOOL_SCRIPT_PATH_KEY = "cleartoolScriptPath";
    public static final String DEFAULT_ELEMENT_TYPE = "defaultElementType.";
    public static final String SCATTERPLOT_CENTER_X = "scatterplotX.center.";
    public static final String SCATTERPLOT_CENTER_Y = "scatterplotY.center.";
    public static final String SCATTERPLOT_X = "scatterplotX.";
    public static final String SCATTERPLOT_Y = "scatterplotY.";
    public static final String DEVPARTNER_PLUGIN_BATCH_CMD = CAQS_TRAITEMENT_PATH +
            "/devpartnerstudio/plugin/startanalyse.bat";
    public static final String DEVENTREPRISE_PLUGIN_BATCH_CMD = CAQS_TRAITEMENT_PATH +
            "/deventreprise/plugin/startanalyse.bat";
    public static final String DEVPARTNER_SCRIPT_MODIF_CSV_KEY = "devPartnerScriptModifCsv";
    public static final String DEVENTREPRISE_COBOL_SCRIPT_MODIF_CSV_KEY = "devEnterpriseCobolScriptModifCsv";
    public static final String DEVENTREPRISE_MSSQLSERVER_PROPERTIES = CAQS_TRAITEMENT_PATH +
            "/deventreprise/plugin/config.properties";
    public static final String DEVPARTNER_CODE_REVIEW_BATCH_KEY = "devPartnerCrBatch";
    public static final String DEVPARTNER_SCRIPT_PATH_KEY = "devPartnerScriptPath";
    public static final String DEVPARTNER_JAVA_SCRIPT_PATH_KEY = "devPartnerJavaScriptPath";
    public static final String DEVPARTNER_NMJRULES_CMD_KEY = "nmjrulesCmd";
    public static final String DEVPARTNER_JAVA_RULE_BY_RULE_KEY = "devPartnerJavaRuleByRule";
    public static final String DEVPARTNER_JAVA_RULE_LIST_RULE_KEY = "devPartnerJavaRuleByRuleListRule";
    public static final String MCCABE_SCRIPT_PATH_KEY = "mccabeScriptPath";
    public static final String MCCABE_SCRIPT_CONFIG_PATH_KEY = "mccabeScriptConfigurationFilePath";
    public static final String CAST_SCRIPT_CONFIG_PATH_KEY = "castScriptConfigurationFilePath";
    public static final String CALLS_TO_SCRIPT_PATH_KEY = "callsToScript";
    public static final String CHECKSTYLE_CHECK_CONFIG_FILE_KEY = "checkStyleChecksConfigurationFile";
    public static final String CHECKSTYLE_XSL_CONFIG_FILE_KEY = "checkStyleXSLConfigurationFile";
    public static final String CHECKSTYLE_CMD_KEY = "checkStyleCmd";
    public static final String CHECKSTYLE_CMD_ARGS_KEY = "checkStyleCmdArgs";
    public static final String CHECKSTYLE_XSL_TRANSFORM_CMD_KEY = "xslTransformCmd";
    public static final String CHECKSTYLE_XSL_TRANSFORM_CMD_ARGS_KEY = "xslTransformCmdArgs";
    public static final String CHECKSTYLE_CSV_PERL_PROCESS_KEY = "checkStyleCsvPerlProcessing";
    public static final String FLAW_FINDER_PYTHON_CMD_KEY = "flawFinderPythonCmd";
    public static final String FLAW_FINDER_CMD_KEY = "flawFinderCmd";
    public static final String FLAWFINDER_BASE_MODULE_METRICS_FILE = "flawFinderBaseModuleMetricsFile";

    /* Constantes pour l'analyse OptimalAdvisor. */
    public static final String OPTIMAL_ADVISOR_HOME_KEY = CAQS_TRAITEMENT_PATH +
            "/advisor/plugin";
    public static final String OPTIMAL_ADVISOR_RULESET_OPTION_KEY = "ruleset";
    public static final String OPTIMAL_ADVISOR_RULES_ID_KEY = "advisor.rulesid";
    public static final String OPTIMAL_ADVISOR_RULES_KEY = "advisor.rules";
    public static final String OPTIMAL_ADVISOR_PMD_RULES_KEY = "advisor.pmd.rules";
    public static final String OPTIMAL_ADVISOR_INPUTPATH_OPTION_KEY = "inputPath";
    public static final String OPTIMAL_ADVISOR_SOURCEPATH_OPTION_KEY = "sourcepath";
    public static final String OPTIMAL_ADVISOR_REPORT_DIR_KEY = "optimaladvisorreports";
    public static final String OPTIMAL_ADVISOR_REPORT_XML_FILE_NAME_KEY = "tmpAdvisorAnalyse.xml";
    public static final String PQC_DATA_FILE_KEY = "pqcDataFile";

    /* Constantes d'acc�s � la base de donn�es. */
    public static final String CAQS_DATASOURCE_KEY = "PqlDS";
    public static final String OVIEW_DATASOURCE_KEY = "OviewDS";
    public static final String ENTRYPOINT_ELEMENT_VALUE = "ENTRYPOINT";

    /* Constantes d'identification des loggers. */
    public static final String LOG4J_IHM_LOGGER_KEY = "Ihm";
    public static final String LOG4J_WEBSERVICES_LOGGER_KEY = "WebServices";
    public static final String LOG4J_DBMS_LOGGER_KEY = "Dbms";
    public static final String LOG4J_ANALYSIS_LOGGER_KEY = "StaticAnalysis";
    public static final String LOG4J_CALCUL_LOGGER_KEY = "Calcul";
    public static final String LOG4J_CACHE_LOGGER_KEY = "Cache";
    public static final String LOG4J_LOGGER_DIR_KEY = "log.dir";
    public static final String ANT_DEFAULT_BUILD_DIR = CAQS_TRAITEMENT_PATH +
            "/ant/";
    public static final String SC_METRICS = "caqs.scmetrics.bestpractices";
    public static final String SC_EXP_METRICS = "caqs.scmetrics.bestpractices.export";
    /* Constantes usuelles */
    public static final String CAQS_CURRENT_ELEMENT_SESSION_KEY = "currentElement";
    public static final String CAQS_SUB_ELEMENT_TYPES_SESSION_KEY = "subElementTypes";
    public static final String CS_METRIC_GENERATION_CMD_KEY = CAQS_TRAITEMENT_PATH +
            "/metricGeneration/launch_metricGeneration_CAQS" +
            OS_SPECIFIC_BATCH_EXTENSION;
    public static final String CS_METRIC_GENERATION_REPORTS = "metricgenerationreports";
    public static final String CS_METRIC_GENERATION_REPORTS_PKG_FILE = "packageMetrics.xml";
    public static final String CS_METRIC_GENERATION_REPORTS_CLS_FILE = "classMetrics.xml";
    public static final String CS_METRIC_GENERATION_REPORTS_MET_FILE = "methodMetrics.xml";
    public static final String[] ALL_CODE_KEYS = new String[]{"ALL_CODE", "NOCL", "LOC", "TOTAL-LOC", "DP-COUNTOFCODELINES", "NB_LIGNE_CODE", "PMCCABE_LOC", "CS_CAQS_SNIFFS_METRICS_CODELINECOUNTSNIFF","FLEX_NCSS","NAT_LINES_OF_CODE"};
    public static final String[] COMMENT_KEYS = new String[]{"COMMENTS", "CLOC", "TOTAL-COMMENTS-NB", "DP-COUNTOFCOMMENTLINES", "NB_LIGNE_COMMENTAIRE", "PMCCABE_CSL", "CS_CAQS_SNIFFS_METRICS_COMMENTLINECOUNTSNIFF","NAT_NET_COMMENT_LINES"};
    public static final String ANT_ERROR_KEY = "anterrorkeywords";
    public static final String ANT_BUILD_NOT_FOUND_ERROR_KEY = "antbuldnotfounderror";
    public static final char CSV_SEPARATOR = ';';
    public static final char CR = '\n';
    public static final String BOTTOMUP_ADDITIONNAL_METRIC_LIST_KEY = "caqs.bottomup.additionnal.metriclist";
    public static final String UNIFACEVIEW_LOGOUT_URL = "caqs.unifaceview.url";
    //public static final String GLOBAL_CHARSET = "iso-8859-1";
    public static final String GLOBAL_CHARSET = "utf-8";
    public static final String UNIFACEVIEW_IMAGE_TMP_DIR_KEY = "unifaceview.imagelib.dir";
    public static final String SRC_BASE_PATH = "caqs.src.base.path";
    public static final String CAQS_IMAGE_TMP_DIR_KEY = "caqs.imagelib.dir";
    //constants for action plan
    public static final String ACTION_PLAN_READ_ONLY = "ActionPlanReadOnly";
    public static final String ACTION_PLAN_IMPACTED_ELTS_LIST = "actionplanimpactedeltslist";
    public static final String ACTION_PLAN_CORRECTED = "actionPlanCorrected";
    public static final String ACTION_PLAN_PARTIALLY_CORRECTED = "actionPlanPartiallyCorrected";
    public static final String ACTION_PLAN_DETERIORATED = "actionPlanDeteriorated";
    public static final String ACTION_PLAN_STABLES = "actionPlanStable";
    //constants for messages
    
    /**
     * maximum number of messages that will be sent to the client per user sesion.
     */
    public static final String MESSAGES_DISPLAY_LIMIT_KEY = "messages.display.limit";
    /**
     * apply on all baselines
     */
    public static final String TASK_ON_ALL_BASELINES = "ALL_BASELINES";
    /**
     * apply on all elements
     */
    public static final String TASK_ON_ALL_ELEMENTS = "ALL_ELTS";
    /**
     * permet de placer le libelle du projet dans le message.
     */
    public static final String MESSAGES_LIBPRJ_INFO1 = "LIBPRJ=";
    /**
     *permet de placer des arguments qui seront utilises dans le message 
     * ({0}, etc). Format : ARGS=arg1;arg2;arg3.
     * attention, la virgule est utilisee comme separateur 
     */
    public static final String MESSAGES_ARGS_INFO1 = "ARGS=";
    /**
     * permet de placer l'identifiant de la justification dans le message.
     */
    public static final String MESSAGES_ID_JUST_INFO1 = "ID_JUST=";
    /**
     * permet de placer le step dans le message.
     */
    public static final String MESSAGES_STEP_INFO1 = "STEP=";
    public static final int MAX_LINES_SIZE = 2000;
    public static final String ALL_FACTORS = "ALL_FACTORS";
    public static final String SYMBOLIC_LINK_TYPE = "S";
    public static final String INSTANCIATION_BASELINE_SQL_NAME = "BaseLine d''Instanciation";
    public static final String INSTANCIATION_BASELINE_NAME = "BaseLine d'Instanciation";
    public static final String GENERIC_PARSER_HOME_KEY = CAQS_TRAITEMENT_PATH +
            "/genericParser";
    public static final String GENERIC_PARSER_LANGAGE_PATH_KEY = GENERIC_PARSER_HOME_KEY +
            "/languages";
    //parametres de lancement de batch
    public static final String BATCH_LAUNCH_PARAMETERS = "batchLaunchParameters";

    //all domains for dashboard
    public static final String DASHBOARD_ALL_DOMAINS = "DASHBOARD_ALL_DOMAINS";

    public static final String ELEMENT_PATH_SEPARATOR = "/";

    public static final String CODE2HTML_MODES_HOME_KEY = CAQS_TRAITEMENT_PATH +
            "/modes";


}
