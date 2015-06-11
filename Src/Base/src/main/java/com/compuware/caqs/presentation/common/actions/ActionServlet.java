/*
 * Created on 10 mars 2005
 *
 */
package com.compuware.caqs.presentation.common.actions;

import com.compuware.caqs.exception.CaqsException;
import com.compuware.caqs.service.TaskSvc;
import java.io.File;
import java.util.Date;
import java.util.Locale;
//import java.util.Properties;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;

//import com.compuware.caqs.constants.Constants;
import com.compuware.caqs.security.license.License;
import com.compuware.caqs.security.license.LicenseManager;
import com.compuware.caqs.security.net.AddressMacInfo;
import com.compuware.caqs.security.stats.ConnectionStatData;
import com.compuware.caqs.security.stats.ConnectionStats;
import com.compuware.caqs.service.InternationalizationSvc;
import com.compuware.caqs.service.SecuritySvc;
import com.compuware.caqs.service.delegationsvc.BusinessFactory;
import com.compuware.caqs.util.CaqsConfigUtil;
import com.compuware.toolbox.io.FileTools;
//import com.compuware.toolbox.io.PropertiesReader;
import com.compuware.toolbox.util.logging.LoggerManager;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * @author cwfr-lizac
 *
 */
public class ActionServlet extends org.apache.struts.action.ActionServlet {

    private static final long serialVersionUID = -1813052625894292102L;
    private static final String LOG4J_FILE_NAME = "log4j.properties";

    /**
     *
     */
    public ActionServlet() {
        super();
    }

    public void init(ServletConfig config) throws ServletException {
        super.init(config);

        System.out.println("---------- Compuware Corp. -----------");
        System.out.println("                                      ");
        System.out.println("   oooo     ooo      ooo     ooooo    ");
        System.out.println("  o    o   o   o    o   o   o     o   ");
        System.out.println(" o        o     o  o     o  o         ");
        System.out.println(" o        o     o  o     o   ooooo    ");
        System.out.println(" o        ooooooo  o  o  o        o   ");
        System.out.println("  o    o  o     o   o  oo   o     o   ");
        System.out.println("   oooo   o     o    ooo o   ooooo    ");
        System.out.println("                                      ");
        System.out.println("Compuware Application Quality Solution");
        System.out.println("--------------------------------------");

        System.out.println("Init Resources...");

        String configDir = CaqsConfigUtil.getCaqsConfigDir();
        File configFile = new File(configDir, LOG4J_FILE_NAME);
        if (configFile.exists()) {
            LoggerManager.configure(FileTools.concatPath(configDir, LOG4J_FILE_NAME));
        }

        LicenseManager lm = LicenseManager.getInstance();

        License license = lm.getLicense();
        if (license.allowServerAccess(AddressMacInfo.getMacAddress(), new Date(), 0)) {
            this.getServletContext().setAttribute("caqsLicense", license);
        } else {
            System.out.println("Invalid license: ");
            System.out.println(license);
        }

        Locale.setDefault(Locale.FRENCH);
        InternationalizationSvc internationalizationSvc = InternationalizationSvc.getInstance();
        internationalizationSvc.initResourceBundles();

        //les t�ches en cours deviennent en �chec.
        System.out.println("Setting in progress tasks as failed");
        TaskSvc.getInstance().setInProgressTaskAsFailed();

        SecuritySvc securitySvc = SecuritySvc.getInstance();
        try {
            ConnectionStatData statData = securitySvc.retrieveLastStatistics();
            if (statData != null) {
                ConnectionStats statisticManager = ConnectionStats.getInstance();
                statisticManager.setStatData(statData);
            }
        } catch (CaqsException e) {
            System.out.println("unable to retrieve statistics: " + e.toString());
        }

        BusinessFactory.init(WebApplicationContextUtils.getRequiredWebApplicationContext(this.getServletContext()));
        System.out.println("Ready for incoming requests.");
    }
}
