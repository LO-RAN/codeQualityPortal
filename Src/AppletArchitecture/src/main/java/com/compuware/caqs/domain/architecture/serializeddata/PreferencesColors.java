package com.compuware.caqs.domain.architecture.serializeddata;

import java.awt.Color;

public class PreferencesColors {
    static private PreferencesColors preferencesColors = new PreferencesColors();

    private static final int BLUEPRINT_THEME = 1;
    private int theme = 0;

    private PreferencesColors() {
    }

    synchronized static public PreferencesColors getInstance() {
        return preferencesColors;
    }

    public Color getNodeBackGroungColor() {
        Color aColor = new Color(200, 200, 200);
        switch (theme) {
            case BLUEPRINT_THEME:
                aColor = new Color(0, 0, 255);
        }
        return aColor;
    }

    ;

    public Color getTextColor() {
        Color aColor = new Color(0, 0, 0);
        switch (theme) {
            case BLUEPRINT_THEME:
                aColor = new Color(255, 255, 255);
        }
        return aColor;
    }

    ;

    public Color getBackGroungColor() {
        Color aColor = new Color(255, 255, 255);
        switch (theme) {
            case BLUEPRINT_THEME:
                aColor = new Color(0, 0, 200);
        }
        return aColor;
    }

    public Color getSelectedColor() {
        Color aColor = new Color(248, 130, 7);
        switch (theme) {
            case BLUEPRINT_THEME:
                aColor = new Color(200, 200, 200);
        }
        return aColor;
    }

    public Color getLinkTargetColor() {
        Color aColor = new Color(126, 186, 0);
        switch (theme) {
            case BLUEPRINT_THEME:
                aColor = new Color(255, 255, 255);
        }
        return aColor;
    }

    public Color getImpactAnalysisCaller() {
        Color aColor = new Color(255, 107, 232);
        switch (theme) {
            case BLUEPRINT_THEME:
                aColor = new Color(255, 255, 255);
        }
        return aColor;
    }

    public Color getImpactAnalysisCallee() {
        Color aColor = new Color(247, 209, 24);
        switch (theme) {
            case BLUEPRINT_THEME:
                aColor = new Color(255, 255, 255);
        }
        return aColor;
    }

    public Color getArchiLinkColor() {
        Color aColor = new Color(0, 0, 0);
        switch (theme) {
            case BLUEPRINT_THEME:
                aColor = new Color(255, 255, 255);
        }
        return aColor;
    }

    public Color getUseCaseLinkColor() {
        Color aColor = Color.blue;
        switch (theme) {
            case BLUEPRINT_THEME:
                aColor = new Color(255, 255, 255);
        }
        return aColor;
    }

    public Color getDBLinkColor() {
        Color aColor = Color.magenta;
        switch (theme) {
            case BLUEPRINT_THEME:
                aColor = new Color(255, 255, 255);
        }
        return aColor;
    }
}
