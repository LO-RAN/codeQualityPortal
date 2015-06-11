/*
 * ArchitectureLoader.java
 *
 * Created on 6 novembre 2002, 10:37
 */
package com.compuware.caqs.business.load.db;

import java.util.Collection;
import java.util.Map;
import java.util.TreeSet;

import org.apache.log4j.Logger;

import com.compuware.caqs.business.load.ElementOfArchitecture;
import com.compuware.caqs.business.load.reader.SpreadSheetReportReader;
import com.compuware.caqs.dao.factory.DaoFactory;
import com.compuware.caqs.dao.interfaces.ArchitectureDao;
import com.compuware.caqs.dao.interfaces.ElementDao;
import com.compuware.caqs.domain.dataschemas.ElementBean;
import com.compuware.caqs.domain.dataschemas.analysis.EA;
import com.compuware.caqs.domain.dataschemas.callsto.CallBean;
import com.compuware.caqs.exception.DataAccessException;

public class ArchitectureLoader {

    // d�claration du logger
    static protected Logger logger = com.compuware.toolbox.util.logging.LoggerManager.getLogger("Architecture");
    protected Object[][] data;
    protected EA ea;
    protected Map<String, ElementOfArchitecture> childrenMap;
    protected String baseLineId;

    /** Creates a new instance of ArchitectureLoader */
    public ArchitectureLoader(EA ea, String baseLineId, String filePath) throws LoaderException {
        this.ea = ea;
        this.baseLineId = baseLineId;

        DaoFactory daoFactory = DaoFactory.getInstance();
        ArchitectureDao architectureDao = daoFactory.getArchitectureDao();

        try {
            ArchitectureLoader.logger.debug("Loading Architecture");
            if (this.readDataFromFile(filePath)) {
                this.childrenMap = architectureDao.retrieveArchitectureElement(ea.getId(), null);

                Collection<CallBean> callColl = new TreeSet<CallBean>();
                CallBean currentCall = null;

                for (int ligne = 0; ligne < this.data.length; ligne++) {
                    String fromElementName = (String) this.data[ligne][0];

                    if (fromElementName.lastIndexOf("{") > -1) {
                        ArchitectureLoader.logger.debug("Found an interface method named " + fromElementName);
                    }
                    ElementOfArchitecture eltFrom = (ElementOfArchitecture) this.childrenMap.get(fromElementName);
                    String fromElementID = null;
                    if (eltFrom != null) {
                        fromElementID = eltFrom.getId();
                    }
                    String toElementName = (String) this.data[ligne][1];

                    ElementOfArchitecture eltTo = (ElementOfArchitecture) this.childrenMap.get(toElementName);
                    String toElementID = null;
                    if (eltTo != null) {
                        toElementID = eltTo.getId();
                    }
                    if ((fromElementID == null) || (toElementID == null)) {
                        ArchitectureLoader.logger.trace("Invalid relation : \t" + fromElementName + "->" + toElementName);
                    } else {
                        ArchitectureLoader.logger.debug("Valid relation : \t" + fromElementName + "->" + toElementName);
                        currentCall = new CallBean();
                        currentCall.setIdFrom(fromElementID);
                        currentCall.setIdTo(toElementID);
                        callColl.add(currentCall);
                    }
                }
                architectureDao.insertCalls(callColl, this.baseLineId);
            } else {
                ArchitectureLoader.logger.error("Error When reading file : " + filePath);
            }
        } catch (DataAccessException e) {
            throw new LoaderException(e);
        }
        ArchitectureLoader.logger.debug("End Loading Architecture");
    }
    private static final String DATABASE_NAME = "sql.DB";
    private static final String DATABASE_TYPE = "DB";

    private String checkForDatabaseName(String name) {
        String result = null;
        if (name != null && name.equalsIgnoreCase(DATABASE_NAME)) {
            DaoFactory daoFactory = DaoFactory.getInstance();
            ElementDao elementDao = daoFactory.getElementDao();
            ElementBean eltBean = new ElementBean();
            eltBean.setDesc(DATABASE_NAME);
            eltBean.setTypeElt(DATABASE_TYPE);
            eltBean.setProject(this.ea.getProject());
            ElementBean tmp = elementDao.retrieveUnknownElement(eltBean, this.ea.getId(), true);
            eltBean.setId(tmp.getId());
            elementDao.setLeafElementLink(eltBean, this.ea.getId());
            ElementOfArchitecture newElement = new ElementOfArchitecture(eltBean.getId(), eltBean.getDesc(), null);
            this.childrenMap.put(eltBean.getDesc(), newElement);
            result = eltBean.getId();
        }
        return result;
    }

    private boolean readDataFromFile(String inFile) {
        boolean ok = true;

        SpreadSheetReportReader reader = new SpreadSheetReportReader();
        reader.loadFileData(inFile, ";");
        this.data = reader.getData();

        if ((this.data == null) || this.data.length == 0) {
            ok = false;
        }
        return ok;
    }
}
