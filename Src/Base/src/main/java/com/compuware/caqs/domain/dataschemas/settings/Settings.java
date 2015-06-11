package com.compuware.caqs.domain.dataschemas.settings;

public enum Settings {

    UNKNOWN,
    MESSAGE_LOCATION,
    STARTING_PAGE,
    COLOR_THEME,
    LAST_ANALYSIS_DATE_WARNING,
    DISPLAY_GLOBAL_BASELINES_TIMEPLOT,
    DISPLAY_CONNECTIONS_TIMEPLOT,
    DASHBOARD_DEFAULT_DOMAIN;

    public static Settings fromString(String s) {
        Settings retour = Settings.UNKNOWN;
        if (MESSAGE_LOCATION.toString().equals(s)) {
            retour = Settings.MESSAGE_LOCATION;
        } else if (COLOR_THEME.toString().equals(s)) {
            retour = Settings.COLOR_THEME;
        } else if (STARTING_PAGE.toString().equals(s)) {
            retour = Settings.STARTING_PAGE;
        } else if (LAST_ANALYSIS_DATE_WARNING.toString().equals(s)) {
            retour = Settings.LAST_ANALYSIS_DATE_WARNING;
        } else if (DISPLAY_GLOBAL_BASELINES_TIMEPLOT.toString().equals(s)) {
            retour = Settings.DISPLAY_GLOBAL_BASELINES_TIMEPLOT;
        } else if (DISPLAY_CONNECTIONS_TIMEPLOT.toString().equals(s)) {
            retour = Settings.DISPLAY_CONNECTIONS_TIMEPLOT;
        } else if (DASHBOARD_DEFAULT_DOMAIN.toString().equals(s)) {
            retour = Settings.DASHBOARD_DEFAULT_DOMAIN;
        }
        return retour;
    }
}
