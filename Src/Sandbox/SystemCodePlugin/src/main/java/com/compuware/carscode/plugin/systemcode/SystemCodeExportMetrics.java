package com.compuware.carscode.plugin.systemcode;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: cwfr-fdubois
 * Date: 23 févr. 2006
 * Time: 18:00:16
 * To change this template use File | Settings | File Templates.
 */
public class SystemCodeExportMetrics {

    public void exportMetrics(String srcFile, String dest) throws IOException {
        if (dest != null) {
            File fsrc = new File(srcFile);
            File fdest = new File(dest);
            exportMetrics(fsrc, fdest);
        }
    }

    public void exportMetrics(File srcFile, File dest) throws IOException {
        SystemCodeDao dao = SystemCodeDao.getInstance();
        HashMap metrics = dao.retrieveMetrics(srcFile);
        if (metrics != null) {
            Collection ruleSet = getMetricList();
            FileWriter writer = new FileWriter(dest);
            writer.write("eltName"+createStrFromList(expMetricList)+"\n");
            Set metricSet = metrics.keySet();
            Iterator i = metricSet.iterator();
            while (i.hasNext()) {
                String elt = (String)i.next();
                SystemCodeBean scBean = (SystemCodeBean)metrics.get(elt);
                writer.write(scBean.toCsvLine(ruleSet));
            }
            writer.flush();
            writer.close();
        }
    }


    private List metricList;

    public void setMetricList(List metList) {
        this.metricList = metList;
    }

    public List getMetricList() {
        return this.metricList;
    }

    private List expMetricList;

    public void setExpMetricList(List metList) {
        this.expMetricList = metList;
    }

    public List getExpMetricList() {
        return this.expMetricList;
    }

    private String createStrFromList(List in) {
        StringBuffer result = new StringBuffer();
        Iterator i = in.iterator();
        while (i.hasNext()) {
            String tmp = (String)i.next();
            result.append(";").append(tmp);
        }
        return result.toString();
    }

    public static void main(String[] args) throws IOException {
        String src = "D:\\CarsCode\\Temp\\TestScExport\\AnalysisResult.csv";
        String dest = "D:\\CarsCode\\Temp\\TestScExport\\AnalysisResultModified.csv";
        SystemCodeExportMetrics exp = new SystemCodeExportMetrics();
        exp.exportMetrics(src, dest);
    }

}
