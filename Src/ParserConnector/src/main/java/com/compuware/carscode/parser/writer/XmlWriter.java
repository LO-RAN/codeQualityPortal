package com.compuware.carscode.parser.writer;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.compuware.carscode.parser.bean.ElementBeanComparator;
import com.compuware.carscode.parser.bean.IElementBean;
import com.compuware.carscode.parser.bean.IMetricBean;

/** 
 * @author cwfr-fdubois
 *
 */
public class XmlWriter implements ElementMetricWriter {

    public void print(Map data, File out) throws IOException {
        if (data != null && !data.isEmpty() && out != null) {
            print(data.values(), out);
        }
    }

    public void print(Collection data, File out) throws IOException {
        if (data != null && out != null) {
            List orderedData = new ArrayList(data);
            Collections.sort(orderedData, new ElementBeanComparator());
            FileWriter fw = new FileWriter(out);
            BufferedWriter bw = new BufferedWriter(fw);
            Iterator eltIter = orderedData.iterator();
            IElementBean currentElt = null;
            bw.append("<Result>\n");
            while (eltIter.hasNext()) {
                currentElt = (IElementBean) eltIter.next();
                print(currentElt, bw);
            }
            bw.append("</Result>");
            bw.flush();
            bw.close();
        }
    }

    protected void print(IElementBean elt, BufferedWriter bw) throws IOException {
        if (elt != null) {
            Map metricMap = elt.getMetricMap();
            bw.append("<elt name='").append(elt.getDescElt()).append("' type='").append(elt.getTypeElt());
            if (elt.getFilePath() != null) {
                bw.append("' filepath='").append(elt.getFilePath());
            }
            bw.append("'>\n");
            if (elt.getDescParent() != null) {
                bw.append("    <parent name='").append(elt.getDescParent()).append("' />\n");
            }
            if (metricMap != null && metricMap.size() > 0) {
                Collection metricColl = metricMap.values();
                Iterator metricIter = metricColl.iterator();
                IMetricBean metricBean = null;
                while (metricIter.hasNext()) {
                    metricBean = (IMetricBean) metricIter.next();
                    printMetric(metricBean, bw);
                }
            }
            bw.append("</elt>\n");
        }
    }

    protected void printMetric(IMetricBean metricBean, BufferedWriter bw) throws IOException {
        bw.append("    <metric id=\"");
        bw.append(metricBean.getId());
        bw.append("\"");
        bw.append(" value=\"");
        bw.append("" + metricBean.getValue());
        bw.append("\"");
        bw.append(" lines=\"");
        bw.append(metricBean.getLinesAsString());
        bw.append("\"");
        bw.append(" />\n");
    }
}
