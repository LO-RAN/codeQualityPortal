package com.compuware.caqs.service;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import au.com.bytecode.opencsv.CSVReader;

import com.compuware.caqs.constants.Constants;
import com.compuware.caqs.constants.MessagesCodes;
import com.compuware.caqs.dao.factory.DaoFactory;
import com.compuware.caqs.dao.interfaces.CriterionDao;
import com.compuware.caqs.dao.interfaces.FactorDao;
import com.compuware.caqs.dao.interfaces.InternationalizationDao;
import com.compuware.caqs.dao.interfaces.MetriqueDao;
import com.compuware.caqs.domain.dataschemas.CriterionDefinition;
import com.compuware.caqs.domain.dataschemas.FactorBean;
import com.compuware.caqs.domain.dataschemas.FactorDefinitionBean;
import com.compuware.caqs.domain.dataschemas.InternationalizationBean;
import com.compuware.caqs.domain.dataschemas.MetriqueDefinitionBean;
import com.compuware.caqs.domain.dataschemas.i18n.I18nUtil;
import com.compuware.toolbox.util.logging.LoggerManager;
import com.compuware.toolbox.util.resources.DbmsResourceBundle;
import com.compuware.toolbox.util.resources.Internationalizable;
import java.sql.SQLException;

public class InternationalizationSvc {
    protected static org.apache.log4j.Logger logger = LoggerManager.getLogger(Constants.LOG4J_IHM_LOGGER_KEY);

    private static final InternationalizationSvc instance = new InternationalizationSvc();

    private InternationalizationSvc() {
    }

    public static InternationalizationSvc getInstance() {
        return instance;
    }
    private DaoFactory daoFactory = DaoFactory.getInstance();

    public void initResourceBundles() {
        InternationalizationDao dao = daoFactory.getInternationalizationDao();
        Collection locales = dao.retrieveLocales();
        Iterator i = locales.iterator();
        while (i.hasNext()) {
            Locale loc = (Locale) i.next();
            dao.initResources(loc);
        }
    }

    /**
     * retrieves all locales defined in caqs
     * @return all locales defined in caqs
     */
    public Collection<Locale> retrieveLocales() {
        InternationalizationDao dao = daoFactory.getInternationalizationDao();
        return dao.retrieveLocales();
    }

    public Collection retrieveMetrics() {
        MetriqueDao metriqueFacade = daoFactory.getMetriqueDao();
        return metriqueFacade.retrieveAllMetriques();
    }

    public void extractMetricsAsCsv(PrintWriter out) {
        Collection locales = retrieveLocales();
        extractMetricsAsCsv(locales, out);
    }

    public void extractMetricsAsCsv(Collection locales, PrintWriter out) {
        printHeader(locales, out);
        Collection metricColl = retrieveMetrics();
        Iterator i = metricColl.iterator();
        while (i.hasNext()) {
            MetriqueDefinitionBean met = (MetriqueDefinitionBean) i.next();
            printMetric(met, locales, out);
        }
    }

    private void printMetric(MetriqueDefinitionBean met, Collection locales, PrintWriter out) {
        String key = met.getTextKey(Internationalizable.LIB_PROPERTY_KEY);
        printInfo(key, locales, I18nUtil.METRIQUE_BUNDLE_NAME, out);
        key = met.getTextKey(Internationalizable.DESC_PROPERTY_KEY);
        printInfo(key, locales, I18nUtil.METRIQUE_BUNDLE_NAME, out);
        key = met.getTextKey(Internationalizable.COMPL_PROPERTY_KEY);
        printInfo(key, locales, I18nUtil.METRIQUE_BUNDLE_NAME, out);
    }

    public Collection retrieveCriterions() {
        CriterionDao criterionFacade = daoFactory.getCriterionDao();
        return criterionFacade.retrieveAllCriterions();
    }

    public void extractCriterionsAsCsv(PrintWriter out) {
        Collection locales = retrieveLocales();
        extractCriterionsAsCsv(locales, out);
    }

    public void extractCriterionsAsCsv(Collection locales, PrintWriter out) {
        printHeader(locales, out);
        Collection criterionColl = retrieveCriterions();
        Iterator i = criterionColl.iterator();
        while (i.hasNext()) {
            CriterionDefinition crit = (CriterionDefinition) i.next();
            printCriterion(crit, locales, out);
        }
    }

    private void printCriterion(CriterionDefinition crit, Collection locales, PrintWriter out) {
        String key = crit.getTextKey(Internationalizable.LIB_PROPERTY_KEY);
        printInfo(key, locales, I18nUtil.CRITERE_BUNDLE_NAME, out);
        key = crit.getTextKey(Internationalizable.DESC_PROPERTY_KEY);
        printInfo(key, locales, I18nUtil.CRITERE_BUNDLE_NAME, out);
        key = crit.getTextKey(Internationalizable.COMPL_PROPERTY_KEY);
        printInfo(key, locales, I18nUtil.CRITERE_BUNDLE_NAME, out);
    }

    public Collection<FactorBean> retrieveFactors() {
        FactorDao factorFacade = daoFactory.getFactorDao();
        return factorFacade.retrieveAllFactorDefinitions();
    }

    public void extractFactorsAsCsv(PrintWriter out) {
        Collection locales = retrieveLocales();
        extractFactorsAsCsv(locales, out);
    }

    public void extractFactorsAsCsv(Collection locales, PrintWriter out) {
        printHeader(locales, out);
        Collection<FactorBean> factorColl = retrieveFactors();
        Iterator<FactorBean> i = factorColl.iterator();
        while (i.hasNext()) {
            FactorDefinitionBean fact = (FactorDefinitionBean) i.next();
            printFactor(fact, locales, out);
        }
    }

    private void printFactor(FactorDefinitionBean fact, Collection locales, PrintWriter out) {
        String key = fact.getTextKey(Internationalizable.LIB_PROPERTY_KEY);
        printInfo(key, locales, I18nUtil.FACTEUR_BUNDLE_NAME, out);
        key = fact.getTextKey(Internationalizable.DESC_PROPERTY_KEY);
        printInfo(key, locales, I18nUtil.FACTEUR_BUNDLE_NAME, out);
        key = fact.getTextKey(Internationalizable.COMPL_PROPERTY_KEY);
        printInfo(key, locales, I18nUtil.FACTEUR_BUNDLE_NAME, out);
    }

    private void printInfo(String key, Collection locales, String bundleName, PrintWriter out) {
        StringBuffer tempout = new StringBuffer();
        tempout.append(key).append(";");
        Iterator i = locales.iterator();
        while (i.hasNext()) {
            Locale loc = (Locale) i.next();
            DbmsResourceBundle messages = (DbmsResourceBundle) DbmsResourceBundle.getResourceBundle(bundleName, loc);
            tempout.append("\"").append(convertAsCsv(messages.getStringNoDefault(key))).append("\";");
        }
        out.println(tempout.toString());
    }

    private String convertAsCsv(String in) {
        String result;
        if (in != null) {
            result = in.replaceAll("\"", "\"\"");
        } else {
            result = "";
        }
        return result;
    }

    private void printHeader(Collection locales, PrintWriter out) {
        out.print("Key;");
        Iterator i = locales.iterator();
        while (i.hasNext()) {
            Locale loc = (Locale) i.next();
            out.print(loc.getLanguage());
            out.print(";");
        }
        out.println();
    }

    /**
     * updates the database using bean informations and the resourcebundle
     * @param bean the bean to update
     */
    public MessagesCodes updateData(InternationalizationBean bean, Internationalizable internationalizable) {
        MessagesCodes retour = MessagesCodes.NO_ERROR;
        try {
            DaoFactory.getInstance().getInternationalizationDao().updateData(bean);
            DbmsResourceBundle.setString(internationalizable, bean.getColumnName(),
                    new Locale(bean.getLanguageId()), bean.getText());
        } catch (SQLException exc) {
            logger.error("error updating i18n", exc);
            retour = MessagesCodes.DATABASE_ERROR;
        }
        return retour;
    }

    public void saveInternationalizationDataFromCsv(InputStream in) throws IOException {
        CSVReader reader = new CSVReader(new InputStreamReader(in), ';');
        Collection updateDataCollection = new ArrayList();
        String[] nextLine;
        List languages = readLanguagesFromCsv(reader.readNext());
        while ((nextLine = reader.readNext()) != null) {
            // nextLine[] is an array of values from the line
            updateDataCollection.addAll(readInternationalizationDataFromCsv(nextLine, languages));
        }
        reader.close();
        in.close();
        InternationalizationDao dao = daoFactory.getInternationalizationDao();
        dao.updateData(updateDataCollection);
        initResourceBundles();
    }

    private List readLanguagesFromCsv(String[] headerLine) {
        List result = Arrays.asList(headerLine);
        if (result != null) {
            result = result.subList(1, result.size());
        }
        return result;
    }

    private Collection readInternationalizationDataFromCsv(String[] line, List languages) {
        Collection result = new ArrayList();
        if (line.length > 0) {
            InternationalizationBean bean;
            String language;
            int currentColumn = 1;
            Iterator i = languages.iterator();
            while (i.hasNext()) {
                language = (String) i.next();
                bean = new InternationalizationBean();
                String key = line[0];
                String[] splittedKey = key.split("\\.");
                if (splittedKey.length > 2) {
                    bean.setTableName(splittedKey[0]);
                    bean.setColumnName(splittedKey[1]);
                    String tableId = "";
                    boolean first = true;
                    for (int cpt = 2; cpt < splittedKey.length; cpt++) {
                        if (!first) {
                            tableId += ".";
                        }
                        tableId += splittedKey[cpt];
                        first = false;
                    }
                    bean.setTableId(tableId);
                    bean.setLanguageId(language);
                    if (line.length > currentColumn) {
                        bean.setText(line[currentColumn]);
                    } else {
                        bean.setText(null);
                    }
                    result.add(bean);
                }
                currentColumn++;
            }
        }
        return result;
    }
}
