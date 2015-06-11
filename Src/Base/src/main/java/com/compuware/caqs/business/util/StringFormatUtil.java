package com.compuware.caqs.business.util;

import java.text.NumberFormat;
import java.util.Locale;

public class StringFormatUtil {

    public static NumberFormat getMarkFormatter(Locale locale) {
        NumberFormat retour = NumberFormat.getInstance(locale);
        retour.setMaximumFractionDigits(2);
        retour.setMinimumFractionDigits(2);
        return retour;
    }

    public static NumberFormat getIntegerFormatter(Locale locale) {
        NumberFormat retour = NumberFormat.getInstance(locale);
        retour.setMaximumFractionDigits(0);
        retour.setMinimumFractionDigits(0);
        return retour;
    }

    public static NumberFormat getWeightFormatter(Locale locale) {
        NumberFormat retour = NumberFormat.getInstance(locale);
        retour.setMaximumFractionDigits(0);
        retour.setMinimumFractionDigits(0);
        return retour;
    }

    public static NumberFormat getFormatter(Locale locale, int minimumFractionDigit,
            int maximumFractionDigit) {
        NumberFormat retour = NumberFormat.getInstance(locale);
        retour.setMaximumFractionDigits(maximumFractionDigit);
        retour.setMinimumFractionDigits(minimumFractionDigit);
        return retour;
    }

}
