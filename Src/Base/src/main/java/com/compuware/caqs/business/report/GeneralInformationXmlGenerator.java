package com.compuware.caqs.business.report;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.Iterator;
import java.util.ResourceBundle;

import com.compuware.caqs.dao.factory.DaoFactory;
import com.compuware.caqs.dao.interfaces.OutilDao;
import com.compuware.caqs.dao.interfaces.UsageDao;
import com.compuware.caqs.domain.dataschemas.ElementBean;
import com.compuware.caqs.domain.dataschemas.ElementType;
import com.compuware.caqs.domain.dataschemas.OutilBean;
import com.compuware.caqs.domain.dataschemas.UsageBean;
import java.util.Locale;

public class GeneralInformationXmlGenerator extends XmlGenerator {

    Collection<OutilBean> outilCollection = null;
    UsageBean usageBean = null;

    public GeneralInformationXmlGenerator(ElementBean eltBean, double noteSeuil, Locale loc) {
        super(eltBean, noteSeuil, loc);
    }

    public void retrieveData() {
        DaoFactory daoFactory = DaoFactory.getInstance();
        OutilDao outilFacade = daoFactory.getOutilDao();
        outilCollection = outilFacade.retrieveOutilByBaseline(this.eltBean.getBaseline().getId());
        UsageDao usageDao = daoFactory.getUsageDao();
        usageBean = usageDao.retrieveUsageByElementId(this.eltBean.getId());
    }

    public void generate(File destination) throws IOException {
        PrintWriter out = createWriter(destination, "general.xml");
        printReportHeader(out);
        generateElement(out);
        generateBaseline(out);
        generateModeleInfos(out);
        generateOutils(out);
        generateCaqsVersionNumber(out);
        printReportFooter(out);
        out.flush();
        out.close();
    }

    private void generateCaqsVersionNumber(PrintWriter out) {
        ResourceBundle resources = ResourceBundle.getBundle("com.compuware.caqs.Resources.resources");
        if (resources != null) {
            String versionNumber = resources.getString("caqs.about.versionNumber");
            String caqsName = resources.getString("caqs.about.caqsname");
            out.print("    <CAQSVERSION");
            out.print(" value=\"" + caqsName + " " + versionNumber + "\"");
            out.println(" />");
        }

    }

    private void generateElement(PrintWriter out) {
        out.print("    <ELEMENT");
        out.print(" id=\"" + this.eltBean.getId() + "\"");
        out.print(" lib=\"" + getXmlCompliantString(this.eltBean.getLib())
                + "\"");
        out.print(" projectName=\""
                + getXmlCompliantString(this.eltBean.getProject().getLib())
                + "\"");
        out.println(" desc=\"" + getXmlCompliantString(this.eltBean.getDesc())
                + "\" />");
    }

    private void generateBaseline(PrintWriter out) {
        out.print("    <BASELINE");
        out.print(" id=\"" + this.eltBean.getBaseline().getId() + "\"");
        out.print(" lib=\""
                + getXmlCompliantString(this.eltBean.getBaseline().getLib())
                + "\"");
        out.print(" desc=\""
                + getXmlCompliantString(this.eltBean.getBaseline().getDesc())
                + "\"");
        out.println(" />");
    }

    private void generateModeleInfos(PrintWriter out) {
        if (ElementType.EA.equals(this.eltBean.getTypeElt())) {
            out.print("    <MODELE");
            out.print(" dialecte=\"" + this.eltBean.getDialecte().getLib()
                    + "\"");
            out.print(" usage=\""
                    + getXmlCompliantString(this.usageBean.getLib(this.locale)) + "\"");
            out.println(" />");
        }
    }

    private void generateOutils(PrintWriter out) {
        out.println("    <OUTILS>");
        if (outilCollection != null) {
            Iterator<OutilBean> i = outilCollection.iterator();
            OutilBean bean = null;
            while (i.hasNext()) {
                bean = (OutilBean) i.next();
                if (bean.getId() != null) {
                    out.print("        <OUTIL");
                    out.print(" id=\"" + bean.getId() + "\"");
                    out.print(" lib=\""
                            + getXmlCompliantString(bean.getLib(locale)) + "\"");
                    out.print(" desc=\""
                            + getXmlCompliantString(bean.getDesc(locale)) + "\"");
                    out.println(" />");
                }
            }
        }
        out.println("    </OUTILS>");
    }
}
