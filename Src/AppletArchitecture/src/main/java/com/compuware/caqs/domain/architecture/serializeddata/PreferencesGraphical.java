package com.compuware.caqs.domain.architecture.serializeddata;

/**
 * @author cwfr-fxalbouy
 * Singleton that keeps the graphical setting for the applet
 */
public class PreferencesGraphical {
    static private PreferencesGraphical graphicalPreferences = new PreferencesGraphical();

    //a debugging option, must be set at compilation time;

    protected boolean debugGraphics = false;
    //performance influences basic
    protected boolean openGl = false;
    protected boolean antialiasing = false;
    protected boolean drawResizeHandle = false;
    protected boolean gradientFill = false;
    protected boolean arrowDrawn = true;
    protected boolean showLinkInBirdView = true;

    private PreferencesGraphical() {
    }

    synchronized static public PreferencesGraphical getInstance() {
        return PreferencesGraphical.graphicalPreferences;
    }

    public void setAntialiaising(boolean value) {
        this.antialiasing = value;
    }

    public boolean getAntialiasing() {
        return this.antialiasing;
    }

    public void setOpenGLMode(boolean value) {
        this.openGl = value;
        try {
            if (value) {
                System.setProperty("sun.java2d.opengl", "true");
                System.setProperty("sun.java2d.accthreshold", "0");
                System.setProperty("sun.java2d.d3dtexbpp", "16");
                System.setProperty("sun.java2d.ddforcevram", "true");
            } else {
                System.setProperty("sun.java2d.opengl", "false");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean isOpenGL() {
        return this.openGl;
    }

    public boolean isDebugGraphics() {
        return this.debugGraphics;
    }

    public boolean isDrawResizeHandle() {
        return this.drawResizeHandle;
    }

    public void setDrawResizeHandle(boolean value) {
        this.drawResizeHandle = value;
    }

    public boolean isGradientFill() {
        return this.gradientFill;
    }

    public void setGradiedtFill(boolean value) {
        this.gradientFill = value;
    }

    public boolean isArrowDrawn() {
        return this.arrowDrawn;
    }

    public void setDrawArrow(boolean value) {
        this.arrowDrawn = value;
    }

    public void setShowLinksInBirdView(boolean value) {
        this.showLinkInBirdView = value;
    }

    public boolean getShowLinksInBirdView() {
        return this.showLinkInBirdView;
    }
}
