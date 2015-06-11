package com.compuware.caqs.business.chart.xml;

import com.compuware.caqs.business.util.StringFormatUtil;
import java.text.NumberFormat;
import java.util.Collection;
import java.util.Iterator;
import java.util.Locale;

import org.apache.struts.util.MessageResources;
import org.jfree.data.xml.DatasetTags;

import com.compuware.caqs.constants.Constants;
import com.compuware.caqs.domain.dataschemas.BaselineBean;
import com.compuware.caqs.domain.dataschemas.EvolutionBean;
import com.compuware.caqs.domain.dataschemas.FactorBean;
import com.compuware.caqs.domain.dataschemas.FactorEvolutionBean;
import com.compuware.caqs.domain.dataschemas.RepartitionBean;
import com.compuware.caqs.domain.dataschemas.list.FactorBeanCollection;
import java.util.List;

public class XmlDatasetGenerator {

    private static final String ACCEPTED_MSG_KEY = "caqs.synthese.kiviat.accepted";
    private static final String RESERVE_MSG_KEY = "caqs.synthese.kiviat.reserve";
    private static final String REJECTED_MSG_KEY = "caqs.synthese.kiviat.rejected";
    private static final String REAL_MSG_KEY = "caqs.synthese.kiviat.real";
    private static final String SIMULATED_MSG_KEY = "caqs.synthese.kiviat.simulated";

    public static void writeFactorDataKiviatActionPlan(StringBuffer out,
            FactorBeanCollection reals,
            FactorBeanCollection simulatedValues,
            Locale loc, MessageResources resources) {
        out.append("<?xml version=\"1.0\" encoding=\"" +
                Constants.GLOBAL_CHARSET + "\"?>\n");
        out.append("<").append(DatasetTags.CATEGORYDATASET_TAG).append(">\n");
        writeFactorData(resources.getMessage(loc, ACCEPTED_MSG_KEY), "4D", out, reals, loc);
        writeFactorData(resources.getMessage(loc, RESERVE_MSG_KEY), "3D", out, reals, loc);
        writeFactorData(resources.getMessage(loc, REJECTED_MSG_KEY), "2D", out, reals, loc);

        NumberFormat nf = StringFormatUtil.getMarkFormatter(loc);

        if (simulatedValues != null) {
            Iterator<FactorBean> fiter = simulatedValues.iterator();
            out.append("<").append(DatasetTags.SERIES_TAG).append(" name=\"").append(resources.getMessage(loc, SIMULATED_MSG_KEY)).append("\">\n");
            while (fiter.hasNext()) {
                FactorBean fbean = fiter.next();
                if(!reals.containsByObject(fbean)) {
                    continue;
                }
                String key = fbean.getLib(loc);
                out.append("<").append(DatasetTags.ITEM_TAG).append(">\n");
                out.append("<").append(DatasetTags.KEY_TAG).append(">");
                out.append(key);
                out.append("</").append(DatasetTags.KEY_TAG).append(">\n");
                out.append("<").append(DatasetTags.VALUE_TAG).append(">");
                out.append(nf.format(fbean.getNote() - 0.005).replaceAll(",", "."));
                out.append("</").append(DatasetTags.VALUE_TAG).append(">\n");
                out.append("</").append(DatasetTags.ITEM_TAG).append(">\n");
            }
            out.append("</").append(DatasetTags.SERIES_TAG).append(">\n");
        }
        if (reals != null) {
            Iterator<FactorBean> fiter = reals.iterator();
            out.append("<").append(DatasetTags.SERIES_TAG).append(" name=\"").append(resources.getMessage(loc, REAL_MSG_KEY)).append("\">\n");
            while (fiter.hasNext()) {
                FactorBean fbean = fiter.next();
                String key = fbean.getLib(loc);
                out.append("<").append(DatasetTags.ITEM_TAG).append(">\n");
                out.append("<").append(DatasetTags.KEY_TAG).append(">");
                out.append(key);
                out.append("</").append(DatasetTags.KEY_TAG).append(">\n");
                out.append("<").append(DatasetTags.VALUE_TAG).append(">");
                out.append(nf.format(fbean.getNote() - 0.005).replaceAll(",", "."));
                out.append("</").append(DatasetTags.VALUE_TAG).append(">\n");
                out.append("</").append(DatasetTags.ITEM_TAG).append(">\n");
            }
            out.append("</").append(DatasetTags.SERIES_TAG).append(">\n");
        }
        out.append("</").append(DatasetTags.CATEGORYDATASET_TAG).append(">\n");
    }

    public static void writeFactorData(StringBuffer out, Collection<FactorBean> factors, Locale loc, MessageResources resources) {
        out.append("<?xml version=\"1.0\" encoding=\"" +
                Constants.GLOBAL_CHARSET + "\"?>\n");
        out.append("<").append(DatasetTags.CATEGORYDATASET_TAG).append(">\n");
        writeFactorData(resources.getMessage(loc, ACCEPTED_MSG_KEY), "4D", out, factors, loc);
        writeFactorData(resources.getMessage(loc, RESERVE_MSG_KEY), "3D", out, factors, loc);
        writeFactorData(resources.getMessage(loc, REJECTED_MSG_KEY), "2D", out, factors, loc);

        NumberFormat nf = StringFormatUtil.getMarkFormatter(loc);

        Iterator fiter = factors.iterator();
        out.append("<").append(DatasetTags.SERIES_TAG).append(" name=\"").append(resources.getMessage(loc, REAL_MSG_KEY)).append("\">\n");
        while (fiter.hasNext()) {
            FactorBean fbean = (FactorBean) fiter.next();
            String key = fbean.getLib(loc);
            out.append("<").append(DatasetTags.ITEM_TAG).append(">\n");
            out.append("<").append(DatasetTags.KEY_TAG).append(">");
            out.append(key);
            out.append("</").append(DatasetTags.KEY_TAG).append(">\n");
            out.append("<").append(DatasetTags.VALUE_TAG).append(">");
            out.append(nf.format(fbean.getNote() - 0.005).replaceAll(",", "."));
            out.append("</").append(DatasetTags.VALUE_TAG).append(">\n");
            out.append("</").append(DatasetTags.ITEM_TAG).append(">\n");
        }
        out.append("</").append(DatasetTags.SERIES_TAG).append(">\n");
        out.append("</").append(DatasetTags.CATEGORYDATASET_TAG).append(">\n");
    }

    private static void writeFactorData(String seriesName, String value, StringBuffer out, Collection<FactorBean> factors, Locale loc) {
        out.append("<Series name=\"").append(seriesName).append("\">\n");
        Iterator<FactorBean> fiter = factors.iterator();
        while (fiter.hasNext()) {
            FactorBean fbean = (FactorBean) fiter.next();
            String key = fbean.getLib(loc);
            out.append("<Item>\n");
            out.append("<Key>" + key + "</Key>\n");
            out.append("<Value>" + value + "</Value>\n");
            out.append("</Item>\n");
        }
        out.append("</Series>\n");
    }

    public static void writeFactorEvolutionData(StringBuffer out, List<BaselineBean> baselines, List<FactorEvolutionBean> factors, Locale loc, MessageResources resources) {
        out.append("<?xml version=\"1.0\" encoding=\"" +
                Constants.GLOBAL_CHARSET + "\"?>");
        out.append('<').append(DatasetTags.CATEGORYDATASET_TAG).append('>');
        Iterator fiter = factors.iterator();
        while (fiter.hasNext()) {
            EvolutionBean fbean = (EvolutionBean) fiter.next();
            out.append('<').append(DatasetTags.SERIES_TAG).append(" name=\"").append(fbean.getLib(loc)).append("\">");
            Iterator<BaselineBean> biter = baselines.iterator();
            while (biter.hasNext()) {
                BaselineBean bb = biter.next();
                String key = bb.getLib();
                out.append('<').append(DatasetTags.ITEM_TAG).append('>');
                out.append('<').append(DatasetTags.KEY_TAG).append('>');
                out.append(key);
                out.append("</").append(DatasetTags.KEY_TAG).append(">");
                out.append('<').append(DatasetTags.VALUE_TAG).append(">");
                double d = fbean.getDoubleEntry(bb.getId());
                out.append(d - 0.005);
                out.append("</").append(DatasetTags.VALUE_TAG).append(">");
                out.append("</").append(DatasetTags.ITEM_TAG).append(">");
            }
            out.append("</").append(DatasetTags.SERIES_TAG).append(">");
        }
        out.append("</").append(DatasetTags.CATEGORYDATASET_TAG).append(">");
    }

    public static void writeRepartitionData(StringBuffer out, Collection repartition, Locale loc) {
        out.append("<?xml version=\"1.0\" encoding=\"" +
                Constants.GLOBAL_CHARSET + "\"?>\n");
        out.append("<").append(DatasetTags.PIEDATASET_TAG).append(">\n");

        if (repartition != null) {
            Iterator it = repartition.iterator();
            while (it.hasNext()) {
                RepartitionBean bean = (RepartitionBean) it.next();
                out.append("<").append(DatasetTags.ITEM_TAG).append(">\n");
                out.append("<").append(DatasetTags.KEY_TAG).append(">").append(bean.getLib(loc)).append("</").append(DatasetTags.KEY_TAG).append(">\n");
                out.append("<").append(DatasetTags.VALUE_TAG).append(">").append(bean.getNb()).append("</").append(DatasetTags.VALUE_TAG).append(">\n");
                out.append("</").append(DatasetTags.ITEM_TAG).append(">\n");
            }
        }
        out.append("</").append(DatasetTags.PIEDATASET_TAG).append(">");
    }
}
