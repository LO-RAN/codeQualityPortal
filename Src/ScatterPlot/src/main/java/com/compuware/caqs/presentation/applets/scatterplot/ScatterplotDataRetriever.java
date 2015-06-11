package com.compuware.caqs.presentation.applets.scatterplot;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.Locale;
import java.util.Vector;
import java.util.zip.GZIPInputStream;

import com.compuware.caqs.presentation.applets.scatterplot.data.ElementType;
import com.compuware.caqs.presentation.applets.scatterplot.data.Metric;
import com.compuware.caqs.presentation.applets.scatterplot.data.ScatterPlotDataSet;

public class ScatterplotDataRetriever {

    private String variableX = null;
    private Metric metricX = null;
    private String variableY = null;
    private Metric metricY = null;
    private String idBline = null;
    private String idElt = null;
    private String centerH = null;
    private String centerV = null;
    private ScatterPlotDataSet spds = null;
    private Vector<Metric> metrics = null;
    private ElementType elementType = null;
    private Vector<ElementType> elementTypes = null;
    private URL codeBase = null;
    private final Locale locale;

    public ScatterplotDataRetriever(URL codeBase, String idBline,
            String idElt, String metH, String metV, String centerH, String centerV, String defaultEt, Locale loc) {
        this.codeBase = codeBase;
        this.idBline = idBline;
        this.locale = loc;
        this.idElt = idElt;
        this.centerV = centerV;
        this.centerH = centerH;
        this.variableX = metH;
        this.variableY = metV;
        this.retrieveElementTypes(defaultEt);
        //this.getData(v);
        this.retrieveMetrics();
        this.retrievePlotDatas();
    }

    public void refreshDatas() {
        this.retrievePlotDatas();
    }

    private void retrieveElementTypes(String defaultEt) {
        this.elementTypes = new Vector<ElementType>();
        String connectionStr = this.codeBase +
                "ScatterPlotElementType.do";
        if (this.idElt != null) {
            connectionStr += "?idEa=" + idElt;
        }
        try {
            URL urlGet = new URL(connectionStr);
            URLConnection urlConnection = urlGet.openConnection();
            GZIPInputStream zin = new GZIPInputStream(new BufferedInputStream(urlConnection.getInputStream()));
            BufferedReader bin = new BufferedReader(new InputStreamReader(zin, "UTF-8"));

            String line = "";
            while ((line = bin.readLine()) != null) {
                String[] m = line.split(";");
                if (m != null && m.length == 2) {
                    ElementType et = new ElementType();
                    if (m[0].equals(defaultEt)) {
                        this.elementType = et;
                    }
                    et.setId(m[0]);
                    et.setLib(m[1]);
                    this.elementTypes.add(et);
                }
            }
            if (this.elementType == null && !this.elementTypes.isEmpty()) {
                this.elementType = this.elementTypes.get(0);
            }
            bin.close();
            zin.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void retrieveMetrics() {
        this.metrics = new Vector<Metric>();
        String connectionStr = this.codeBase +
                "ScatterPlotMetrics.do?idTelt=" + elementType.getId();
        if (idElt != null && idBline != null) {
            connectionStr += "&id_elt=" + idElt + "&id_bline=" + idBline;
        }
        try {
            URL urlGet = new URL(connectionStr);
            URLConnection urlConnection = urlGet.openConnection();
            GZIPInputStream zin = new GZIPInputStream(new BufferedInputStream(urlConnection.getInputStream()));
            BufferedReader bin = new BufferedReader(new InputStreamReader(zin, "UTF-8"));

            String line = "";
            while ((line = bin.readLine()) != null) {
                String[] m = line.split(";");
                if (m != null && m.length == 2) {
                    Metric met = new Metric(m[0], m[1]);
                    if (m[0].equals(this.variableX)) {
                        this.setMetricX(met);
                    } else if (m[0].equals(this.variableY)) {
                        this.setMetricY(met);
                    }
                    this.metrics.add(met);
                }
            }
            if (this.metricX == null) {
                this.metricX = this.getMetrics().get(0);
                this.variableX = this.metricX.getId();
            }
            if (this.metricY == null) {
                this.metricY = this.getMetrics().get(0);
                this.variableY = this.metricY.getId();
            }
            bin.close();
            zin.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void retrievePlotDatas() {
        String connectionStr = this.codeBase +
                "ScatterPlot.do?metH=" + variableX + "&metV=" + variableY +
                "&centerH=" + centerH + "&centerV=" + centerV + "&idTelt=" +
                elementType.getId();
        if (idElt != null && idBline != null) {
            connectionStr += "&id_elt=" + idElt + "&id_bline=" + idBline;
        }
        try {
            URL urlGet = new URL(connectionStr);
            URLConnection urlConnection = urlGet.openConnection();
            GZIPInputStream zin = new GZIPInputStream(new BufferedInputStream(urlConnection.getInputStream()));
            BufferedReader bin = new BufferedReader(new InputStreamReader(zin));

            String line = "";
            StringBuffer buff = new StringBuffer();
            while ((line = bin.readLine()) != null) {
                buff.append(line).append('\n');
            }
            String jfreeChartDatas = buff.toString();
            this.spds = new ScatterPlotDataSet(jfreeChartDatas, this.locale);
            bin.close();
            zin.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }

    private void getData(String variables) {
        //variable
        java.util.List<String> variablesVector = new java.util.ArrayList<String>();
        java.util.StringTokenizer stringtokenizer = new java.util.StringTokenizer(variables);
        while (stringtokenizer.hasMoreTokens()) {
            variablesVector.add(stringtokenizer.nextToken());
        }

        String[] v = variablesVector.toArray(new String[0]);
        if (v != null && v.length == 2) {
            this.variableX = v[0];
            this.variableY = v[1];
        }
        retrievePlotDatas();
    }

    public ScatterPlotDataSet getPlotDatas() {
        return this.spds;
    }

    public String getIdBline() {
        return idBline;
    }

    public void setIdBline(String idBline) {
        this.idBline = idBline;
    }

    public String getIdElt() {
        return idElt;
    }

    public void setIdElt(String idElt) {
        this.idElt = idElt;
    }

    public String getCenterH() {
        return centerH;
    }

    public void setCenterH(String centerH) {
        this.centerH = centerH;
    }

    public String getCenterV() {
        return centerV;
    }

    public void setCenterV(String centerV) {
        this.centerV = centerV;
    }

    public Vector<Metric> getMetrics() {
        return metrics;
    }

    public String getVariableX() {
        return variableX;
    }

    public void setVariableX(String variableX) {
        this.variableX = variableX;
    }

    public String getVariableY() {
        return variableY;
    }

    public void setVariableY(String variableY) {
        this.variableY = variableY;
    }

    public Metric getMetricX() {
        return metricX;
    }

    public void setMetricX(Metric metricX) {
        this.metricX = metricX;
    }

    public Metric getMetricY() {
        return metricY;
    }

    public void setMetricY(Metric metricY) {
        this.metricY = metricY;
    }

    public ElementType getElementType() {
        return elementType;
    }

    public void setElementType(ElementType elementType) {
        this.elementType = elementType;
    }

    public Vector<ElementType> getElementTypes() {
        return elementTypes;
    }
}
