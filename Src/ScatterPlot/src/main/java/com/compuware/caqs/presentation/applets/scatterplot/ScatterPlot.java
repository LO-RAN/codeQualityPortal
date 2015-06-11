package com.compuware.caqs.presentation.applets.scatterplot;

/**
 * Titre : <p>
 * Description : <p>
 * Copyright : Copyright (c) <p>
 * Société : <p>
 * @author
 * @version 1.0
 */
import java.awt.Color;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JApplet;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import com.compuware.caqs.presentation.applets.scatterplot.draw.ScatterplotAppletContainer;

public class ScatterPlot extends JApplet {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private ScatterplotDataRetriever scatterplotDataRetriever = null;

    public void init() {
        // Get the native look and feel class name
        String nativeLF = UIManager.getSystemLookAndFeelClassName();

        // Install the look and feel
        try {
            UIManager.setLookAndFeel(nativeLF);
        } catch (InstantiationException e) {
            System.out.println("impossible to set look and feel");
        } catch (ClassNotFoundException e) {
            System.out.println("impossible to set look and feel");
        } catch (UnsupportedLookAndFeelException e) {
            System.out.println("impossible to set look and feel");
        } catch (IllegalAccessException e) {
            System.out.println("impossible to set look and feel");
        }

        URL codeBase = null;
        try {
            codeBase = new URL(getParameter("serverAdress"));
        } catch (MalformedURLException ex) {
            Logger.getLogger(ScatterPlot.class.getName()).log(Level.SEVERE, null, ex);
        }
        if ("true".equals(this.getParameter("debug"))) {
            try {
                codeBase = new URL("http://cwfr-d213:8080/caqs/");
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }
        String locale = this.getParameter("LOCALE");
        Locale loc = new Locale(locale);
        scatterplotDataRetriever = new ScatterplotDataRetriever(
                codeBase,
                //getParameter("variables"),
                getParameter("id_bline"),
                getParameter("id_elt"),
                getParameter("metH"),
                getParameter("metV"),
                getParameter("centerH"),
                getParameter("centerV"),
                getParameter("defaultEltType"),
                loc);
        setBackground(Color.white);
        this.setlayout(loc);
    }

    public void setlayout(Locale loc) {
        this.add(new ScatterplotAppletContainer(this.scatterplotDataRetriever, getParameter("serverAdress"), loc));
    }

    public ScatterPlot() {
    }
}
