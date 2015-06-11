package com.compuware.caqs.presentation.consult.forms.comparators;

import com.compuware.caqs.constants.Constants;
import java.util.Comparator;

import com.compuware.caqs.presentation.consult.forms.ElementCriterionForm;
import com.compuware.caqs.presentation.util.StringFormatUtil;
import com.compuware.toolbox.util.logging.LoggerManager;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;

public class ElementCriterionMetricComparator implements Comparator<ElementCriterionForm> {
    protected static org.apache.log4j.Logger mLog = LoggerManager.getLogger(Constants.LOG4J_IHM_LOGGER_KEY);

    private int index = 0;
    private boolean sens = false;
    private String part = "met";
    private Locale locale = null;

    public ElementCriterionMetricComparator(String part, int index, boolean sens, Locale loc) {
        this.index = index;
        this.sens = sens;
        this.part = part;
        this.locale = loc;
    }

    public int compare(ElementCriterionForm arg0, ElementCriterionForm arg1) {
        int result = 0;
        if ("met".equals(this.part)) {
            String[] metrics0 = arg0.getMetrics();
            String[] metrics1 = arg1.getMetrics();

            Double metric0 = getMetric(metrics0);
            Double metric1 = getMetric(metrics1);
            result = compareMetrics(metric0, metric1);
        } else if ("eltBLib".equals(this.part)) {
            result = arg0.getElement().getLib().compareToIgnoreCase(arg1.getElement().getLib());
        } else if ("eltBDesc".equals(this.part)) {
            result = arg0.getElement().getDesc().compareToIgnoreCase(arg1.getElement().getDesc());
        } else if ("note".equals(this.part)) {
            Double d1 = null;
            Double d2 = null;
            try {
                Number n = NumberFormat.getInstance(this.locale).parse(arg0.getNote());
                d1 = n.doubleValue();
            } catch (ParseException ex) {
                mLog.debug("impossible to parse double ", ex);
            }
            try {
                Number n = NumberFormat.getInstance(this.locale).parse(arg1.getNote());
                d2 = (Double) n.doubleValue();
            } catch (ParseException ex) {
                mLog.debug("impossible to parse double ", ex);
            }
            if(d1!=null && d2!=null) {
                result = d1.compareTo(d2);
            } else {
                if(d1==null) {
                    result = (d2!=null) ? 1 : 0;
                } else if(d2==null) {
                    result = (d1!=null) ? -1 : 0;
                }
            }
        }
        if (!this.sens) {
            result *= -1;
        }

        return result;
    }

    public int compareMetrics(Double metric0, Double metric1) {
        int result = 0;

        if (metric0 != null || metric1 != null) {
            if (metric1 == null) {
                result = 1;
            } else if (metric0 == null) {
                result = -1;
            } else {
                result = metric0.compareTo(metric1);
            }
        }

        return result;
    }

    private Double getMetric(String[] metrics) {
        Double result = null;
        if (metrics.length > this.index && metrics[this.index] != null &&
                metrics[this.index].length() > 0) {
            result = StringFormatUtil.parseDecimal(metrics[this.index]);
        }
        return result;
    }
}
