package com.compuware.caqs.presentation.util;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;

public class StringFormatUtil {

    private static NumberFormat decimalNf = NumberFormat.getInstance();
    private static final StringFormatUtil singleton = new StringFormatUtil();

    private StringFormatUtil() {
        decimalNf.setMaximumFractionDigits(2);
        decimalNf.setMinimumFractionDigits(2);
    }

    public static StringFormatUtil getInstance() {
        return singleton;
    }

    public static String decimalFormat(double value) {
        return decimalNf.format(value);
    }

    public static Double parseDecimal(String sVal) {
        Double result = null;
        try {
            result = new Double(decimalNf.parse(sVal).doubleValue());
        } catch (ParseException e) {
            result = null;
        }
        return result;
    }

    public static String escapeString(String s, boolean quote, boolean bracket, boolean doubleQuote) {
        String retour = s;
        if (retour != null) {
            retour = retour.replaceAll("\\\\", "\\\\\\\\");
            if (quote) {
                retour = retour.replaceAll("'", "\\\\'");
            }
            if (bracket) {
                retour = retour.replaceAll("\\[", "\\[\\[");
                retour = retour.replaceAll("\\]", "\\]\\]");
            }
            if (doubleQuote) {
                retour = retour.replaceAll("\"", "&quot;");
            }
        }
        return retour;
    }

    /**
     * replace all carriage returns by their HTML equivalent
     * @param s the string for which the carraige returns have to be
     * replaced
     * @return the replaced string
     */
    public static String replaceCarriageReturnByHTML(String s) {
        String retour = s;
        String htmlNewLine = "<BR/>";
        if (s != null) {
            if (s.indexOf("\n\r") != -1) {
                retour = s.replaceAll("\\n\\r", htmlNewLine);
            } else if (s.indexOf("\r\n") != -1) {
                retour = s.replaceAll("\\r\\n", htmlNewLine);
            } else if (s.indexOf("\r") != -1) {
                retour = s.replaceAll("\\r", htmlNewLine);
            } else if (s.indexOf("\n") != -1) {
                retour = s.replaceAll("\\n", htmlNewLine);
            }
        }
        return retour;
    }

    public static String getTendanceLabel(double tendance, double note) {
        String tendanceLabel = "unchanged";
        if (tendance == 0) {
            tendanceLabel = "new";
        } else if (tendance < note && tendance > 0) {
            tendanceLabel = "up";
        } else if (tendance > note) {
            tendanceLabel = "down";
        }
        return tendanceLabel;
    }

    public static NumberFormat getMarkFormatter(Locale locale) {
        return com.compuware.caqs.business.util.StringFormatUtil.getMarkFormatter(locale);
    }

    public static NumberFormat getIntegerFormatter(Locale locale) {
        return com.compuware.caqs.business.util.StringFormatUtil.getIntegerFormatter(locale);
    }

    public static NumberFormat getWeightFormatter(Locale locale) {
        return com.compuware.caqs.business.util.StringFormatUtil.getWeightFormatter(locale);
    }

    public static NumberFormat getFormatter(Locale locale, int minimumFractionDigit,
            int maximumFractionDigit) {
        return com.compuware.caqs.business.util.StringFormatUtil.getFormatter(locale,
                minimumFractionDigit, maximumFractionDigit);
    }
}
