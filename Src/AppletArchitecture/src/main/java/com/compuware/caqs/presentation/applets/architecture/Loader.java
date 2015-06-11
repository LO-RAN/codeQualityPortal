/*
 * Loader.java
 *
 * Created on 29 juillet 2005, 11:26
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */
package com.compuware.caqs.presentation.applets.architecture;

import java.awt.Dimension;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import javax.swing.JDialog;

import com.compuware.caqs.domain.architecture.serializeddata.ArchitectureModel;

/**
 * 
 * @author cwfr-fxalbouy
 */
public class Loader implements Runnable {

    public static final int LOADER_SAVE_ACTION = 0;
    public static final int LOADER_LOAD_ACTION = 1;
    protected String webServerStr;
    protected String idElement = null;
    protected String idBaseline = null;
    //protected ArchitectureModel m_model;
    private Thread me;
    private int selectedAction;
    private boolean testing;
    private JDialog dialog;

    /** Creates a new instance of Loader */
    public Loader(JDialog dialog, boolean testing, String webServerStr, String idElement, String idBaseline) {
        this.dialog = dialog;
        this.testing = testing;
        this.webServerStr = webServerStr;
        this.idElement = idElement;
        this.idBaseline = idBaseline;
    }

    public void run() {
        switch (this.selectedAction) {
            case Loader.LOADER_LOAD_ACTION:
                this.get();
                break;
            case Loader.LOADER_SAVE_ACTION:
                this.save();
                break;
            default:
            //System.out.println("Unknown Action");
        }
        this.dialog.dispose();
    }

    public void start(int action) {
        this.selectedAction = action;
        this.me = new Thread(this);
        this.me.start();
    }

    public void stop() {
        this.me = null;
    }

    private void save() {
        if (this.testing) {
            try {
                Thread.sleep(5000);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            try {
                URL testServlet = new URL(this.webServerStr);
                URLConnection servletConnection = testServlet.openConnection();
                servletConnection.setDoInput(true);
                servletConnection.setDoOutput(true);
                servletConnection.setUseCaches(false);
                // Specify the content type that we will send binary data
                servletConnection.setRequestProperty("Content-Type", "application/octet-stream");
                GZIPOutputStream zout = new GZIPOutputStream(servletConnection.getOutputStream());
                ObjectOutputStream outputToServlet = new ObjectOutputStream(zout);

                ArchitectureModel.getInstance().setEaId(this.idElement);
                ArchitectureModel.getInstance().setBaseLineId(this.idBaseline);
                // serialize the object
                outputToServlet.writeObject(ArchitectureModel.getInstance());
                outputToServlet.flush();
                outputToServlet.close();

                try {
                    // now, let's read the response from the servlet.
                    // this is simply a confirmation string
                    ObjectInputStream inputFromServlet = new ObjectInputStream(servletConnection.getInputStream());

                    ArchitectureModel.setInstance((ArchitectureModel) inputFromServlet.readObject());
                    ArchitectureModel.getInstance().computeRealLinks();

                    inputFromServlet.close();
                } catch (IOException e) {
                    System.out.println(e.toString());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    private void get() {
        if (this.testing) {
            try {
                Thread.sleep(5000);
            } catch (Exception e) {
                e.printStackTrace();
            }
            ArchitectureModelCreators.createFakeModel(new Dimension(800, 600));
        } else {
            String servletGET = this.webServerStr + "?";
            servletGET += "projectid=" + this.idElement;
            servletGET += "&baselineid=" + this.idBaseline;

            try {
                // connect to the servlet
                System.out.println("servlet url : "+servletGET);
                URL studentDBservlet = new URL(servletGET);

                URLConnection servletConnection = studentDBservlet.openConnection();
                GZIPInputStream zin = new GZIPInputStream(servletConnection.getInputStream());
                // Read the input from the servlet.
                ObjectInputStream inputFromServlet = new ObjectInputStream(zin);

                ArchitectureModel.setInstance((ArchitectureModel) inputFromServlet.readObject());
                ArchitectureModel.getInstance().computeRealLinks();

                inputFromServlet.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
