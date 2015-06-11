/*
 * DbLoader.java
 * Created on 8 septembre 2002, 10:54
 */

package com.compuware.caqs.business.load.db;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.compuware.caqs.business.load.ElementOfArchitecture;
import com.compuware.caqs.business.load.reader.SpreadSheetReportReader;
import com.compuware.caqs.constants.Constants;
import com.compuware.caqs.dao.factory.DaoFactory;
import com.compuware.caqs.dao.interfaces.ArchitectureDao;
import com.compuware.caqs.domain.dataschemas.analysis.EA;
import com.compuware.caqs.exception.DataAccessException;
import com.compuware.caqs.util.CaqsConfigUtil;
import com.compuware.caqs.util.IDCreator;
import com.compuware.toolbox.dbms.JdbcDAOUtils;
import com.compuware.toolbox.util.StringUtils;

public class DbLoader {

    // d�claration du logger
    static final protected Logger logger = com.compuware.toolbox.util.logging.LoggerManager.getLogger(Constants.LOG4J_ANALYSIS_LOGGER_KEY);

    protected Timestamp m_instantiationBaseline = null;

    /*csv file column index*/
    protected List<Integer> m_retainedFileColumnIndex = new ArrayList<Integer>();

    /* currently inserted file*/
    protected DataFile m_curFile = null;
    protected DataFileType m_curElementTypeId = null;
    protected Object[][] m_data = null;
    protected Map<String, String> mEltAndLinks;
    protected File m_target;

    protected Connection m_conn;
    protected String m_webDataAbsolutePath;

    protected Collection<String> m_dbKnownMetrics = null;

    protected LoaderConfig loaderConfig = null;

    /**
     * Creates a new instance of DbLoader
     */
    public DbLoader(LoaderConfig config) {
        Properties dynProp = CaqsConfigUtil.getCaqsGlobalConfigProperties();
        try {
            this.m_webDataAbsolutePath = dynProp.getProperty(Constants.WEB_DATA_DIRECTORY_KEY);
            if (m_webDataAbsolutePath == null) {
                DbLoader.logger.error("Can't read the properties file. Make sure configPortailQualite.conf is in the CLASSPATH");
            }
        } catch (Exception e) {
            DbLoader.logger.error("Can't read the properties file. Make sure configPortailQualite.conf is in the CLASSPATH");
        }
        this.loaderConfig = config;

    }

    public void execute(Collection<DataFile> fileList) throws LoaderException {
        //Creates Data/idea/idbline directory for graphListing
    	EA ea = this.loaderConfig.getEa();
        this.m_target = new File(this.m_webDataAbsolutePath + File.separator + ea.getLibraries() + "-" + ea.getId() + File.separator + this.loaderConfig.getBaseLineId());
        if (!this.m_target.exists()) {
        	this.m_target.mkdirs();
        }
        DbLoader.logger.debug("creating dir " + this.m_target.getPath());
        
        DaoFactory daoFactory = DaoFactory.getInstance();
        ArchitectureDao architectureDao = daoFactory.getArchitectureDao();
        
        //opens a db connection
        this.m_conn = JdbcDAOUtils.getConnection(this, Constants.CAQS_DATASOURCE_KEY);

        try {

            //gets All baseline Details
            this.getBaselineDetails();

            //gets All db known metrics
            this.getDbKnownMetrics();

            this.mEltAndLinks = new HashMap<String, String>();

            //Start : for each file
            for (java.util.Iterator it = fileList.iterator(); it.hasNext();) {
                this.m_curFile = (DataFile) it.next();
                DbLoader.logger.info(" reading file : " + this.m_curFile.getPath());
                boolean ok = this.readDataFromFile(this.m_curFile.getPath());
                if (ok) {
                    DbLoader.logger.debug("ok for reading");
                    this.checkFileAvailableKnownMetrics(this.m_curFile.getPath());
                    this.m_curElementTypeId = this.m_curFile.getType();

                    //if it is an EA
                    if (this.m_curElementTypeId.compareTo(DataFileType.EA) == 0) {
                        DbLoader.logger.debug("it is an ea");
                        this.loadDataForEaElement(ea.getId());
                    } else {
                        //else get Children element of ea where type is similar to current type
                        DbLoader.logger.debug("it is not an ea");

                        try {
                            if (this.m_curElementTypeId.compareTo(DataFileType.LINKS_INTERFACEMETHODS) == 0
                                    || this.m_curElementTypeId.compareTo(DataFileType.LINKS_CLASSESMETHODS) == 0) {
                                DbLoader.logger.debug("inserting links classmethods");
                                this.insertLinksBetweenTwoElements(architectureDao.retrieveArchitectureElement(ea.getId(), DataFileType.CLS), architectureDao.retrieveArchitectureElement(ea.getId(), DataFileType.MET));
                                continue;
                            }
                            if (this.m_curElementTypeId.compareTo(DataFileType.LINKS_INHERITANCE) == 0) {
                                DbLoader.logger.debug("inserting links inheritance");
                                Map hash = architectureDao.retrieveArchitectureElement(ea.getId(), DataFileType.CLS);
                                this.insertLinksBetweenTwoElements(hash, hash);
                                continue;
                            }
                        } catch (Exception e) {
                            DbLoader.logger.error("Error during the insertion of the links between the interfaces and their methods");
                            continue;
                        }

                        Map sameLevelElementHash = architectureDao.retrieveArchitectureElement(ea.getId(), this.m_curElementTypeId);
                        Map peremptionHashtable = architectureDao.retrieveArchitectureElement(ea.getId(), this.m_curElementTypeId);
                        //we first load the elements.
                        DbLoader.logger.debug("creating elements");
                        this.createElements(sameLevelElementHash, peremptionHashtable);
                        //then we load the links.
                        //this.createLinks();
                        if (this.loaderConfig.isPeremptElementsIfNotUsed()) {
                            DbLoader.logger.debug("perempting elements");
                            this.peremptElement(peremptionHashtable);
                        }
                    }
                }

            }
            //End : for each file

            this.showClassMethodsList();

            DbLoader.logger.info("No error during insertions -> Commit");
            JdbcDAOUtils.commit(m_conn);
        } catch (SQLException e) {
            DbLoader.logger.error("Error loading data", e);
            JdbcDAOUtils.rollbackConnection(m_conn);
            throw new LoaderException("Error loading data", e);
        }
        catch (DataAccessException e) {
            DbLoader.logger.error("Error loading data", e);
            JdbcDAOUtils.rollbackConnection(m_conn);
            throw new LoaderException("Error loading data", e);
        }
        finally {
            JdbcDAOUtils.closeConnection(this.m_conn);
        }
    }

    private void insertLinksBetweenTwoElements(Map hash1, Map hash2) throws Exception {

        for (int i = 1; i < this.m_data.length; i++) {
            String name1 = (String) this.m_data[i][0];
            String name2 = (String) this.m_data[i][1];
            boolean insert = true;
            if (hash1.get(name1) == null) {
                DbLoader.logger.info(name1 + " doesn't exist (1).");
                insert = false;
            }
            if (hash2.get(name2) == null) {
                DbLoader.logger.info(name2 + " doesn't exist (2).");
                insert = false;
            }
            if (insert) {
                String modArchiId = null;

                ElementOfArchitecture eltClass = null;

                String updateDmajEltQuery = "UPDATE ELEMENT SET DMAJ_ELT = {fn now()} WHERE ID_ELT = ?";
                PreparedStatement updateDmajEltStmt = null;
                String updatePackIDQuery = "UPDATE ELEMENT SET ID_PACK = ? WHERE ID_ELT = ?";
                PreparedStatement updatePackIDStmt = null;

                String name1ID = (String) this.mEltAndLinks.get(name1);
                String name2ID = (String) this.mEltAndLinks.get(name2);

                String elementLinkQuery = "INSERT INTO ELT_LINKS (ELT_PERE,ELT_FILS,DINST_LINKS,DMAJ_LINKS) VALUES('" + name1ID + "','" + name2ID + "',{fn now()},{fn now()})";
                Statement stmt = null;
                boolean goOn = true;
                if (name1ID == null) {
                    DbLoader.logger.error(name1 + " has no id found.");
                    goOn = false;
                }
                if (name2ID == null) {
                    DbLoader.logger.error(name2 + " has no id found.");
                    goOn = false;
                }
                if (!goOn) {
                	continue;
                }

                try {
                    String searchLink = "SELECT ELT_PERE FROM ELT_LINKS WHERE ELT_PERE='" + name1ID + "' AND ELT_FILS='" + name2ID + "'";

                    stmt = m_conn.createStatement();

                    ResultSet rs = stmt.executeQuery(searchLink);
                    if (!rs.next()) {
                        stmt.execute(elementLinkQuery);
                    } else {
                        DbLoader.logger.error("Link between " + name1 + " and " + name2 + " already exists.");
                    }

                    updateDmajEltStmt = m_conn.prepareStatement(updateDmajEltQuery);
                    updatePackIDStmt = m_conn.prepareStatement(updatePackIDQuery);

                    if (name1 == null) {
                        DbLoader.logger.error(name2 + " has no parent class name in the csv File.");
                    } else {
                        eltClass = (ElementOfArchitecture) hash1.get(name1);
                    }

                    if (eltClass != null) {
                        modArchiId = eltClass.getIdArchiMod();
                    }
                    String elementID = (String) this.mEltAndLinks.get(name2);
                    if (elementID == null) {
                        DbLoader.logger.debug("ElementID not found for the element " + name2);
                    }
                    if (modArchiId != null) {
                        updatePackIDStmt.setString(1, modArchiId);
                        updatePackIDStmt.setString(2, elementID);
                        DbLoader.logger.debug("updating architecture module " + modArchiId + " - " + elementID);
                        updatePackIDStmt.executeUpdate();
                    } else {
                        DbLoader.logger.debug("Architecture module is null parent is either unaffected or there is an error");
                    }
                } catch (Exception e) {
                    DbLoader.logger.error(e.toString());
                    JdbcDAOUtils.rollbackConnection(this.m_conn);
                } finally {
                    JdbcDAOUtils.closeStatement(stmt);
                    JdbcDAOUtils.closePrepareStatement(updateDmajEltStmt);
                    JdbcDAOUtils.closePrepareStatement(updatePackIDStmt);
                }

            }
        }

    }


    private void showClassMethodsList() throws DataAccessException {
        DbLoader.logger.debug("---Starting Class/Methods Listing---");
        String query = "SELECT ID_ELT, DESC_ELT FROM ELEMENT"
                + " WHERE ELEMENT.ID_MAIN_ELT = ?"
                + " And DPEREMPTION IS NULL";

        EA ea = loaderConfig.ea;
        DaoFactory daoFactory = DaoFactory.getInstance();
        ArchitectureDao architectureDao = daoFactory.getArchitectureDao();
        PreparedStatement st = null;
        ResultSet rs = null;
        try {
            Map classes = architectureDao.retrieveArchitectureElement(ea.getId(), DataFileType.CLS);
            Iterator enumClassesDesc = classes.keySet().iterator();
            st = this.m_conn.prepareStatement(query);
            while (enumClassesDesc.hasNext()) {
                String classDesc = (String) enumClassesDesc.next();
                ElementOfArchitecture eltClass = (ElementOfArchitecture) classes.get(classDesc);
                String classId = eltClass.getId();
                DbLoader.logger.debug("Class : " + classDesc);
                st.setString(1, classId);
                rs = st.executeQuery();
                while (rs.next()) {
                    DbLoader.logger.debug("\tmethod : " + rs.getString(2));
                }
                JdbcDAOUtils.closeResultSet(rs);
            }
        }
        catch (SQLException e) {
            DbLoader.logger.error(e.toString());
            throw new DataAccessException(e);
        }
        finally {
            JdbcDAOUtils.closeResultSet(rs);
            JdbcDAOUtils.closePrepareStatement(st);
        }
        DbLoader.logger.debug("---End Class/Methods Listing---");
    }

    private void peremptElement(Map sameLevelElementHash) {
        DbLoader.logger.debug("---Starting Element Peremtion---");
        if (sameLevelElementHash != null) {
            Iterator peremptedElements = sameLevelElementHash.values().iterator();
            if (peremptedElements != null) {
                String query = "UPDATE ELEMENT SET DPEREMPTION = {fn now()} WHERE ID_ELT = ?";
                PreparedStatement statement0 = null;
                try {
                    statement0 = m_conn.prepareStatement(query);
                    while (peremptedElements.hasNext()) {
                        ElementOfArchitecture elt = (ElementOfArchitecture) peremptedElements.next();
                        String elementID = elt.getId();
                        DbLoader.logger.debug("Element : " + elementID + " has been perempted");
                        statement0.setString(1, elementID);
                        statement0.executeUpdate();
                    }
                } catch (Exception e) {
                    DbLoader.logger.error("Error during element Dperemtion update", e);
                    JdbcDAOUtils.rollbackConnection(this.m_conn);
                } finally {
                    JdbcDAOUtils.closePrepareStatement(statement0);
                }
            }
        }
        DbLoader.logger.debug("---End Element Peremtion---");
    }

    private void getBaselineDetails() {
        Statement sta = null;
        ResultSet rs = null;
        String query = "SELECT DMAJ_BLINE FROM BASELINE WHERE ID_BLINE ='" + this.loaderConfig.getBaseLineId() + "'";
        try {
            sta = this.m_conn.createStatement();
            rs = sta.executeQuery(query);
            while (rs.next()) {
                this.m_instantiationBaseline = rs.getTimestamp(1);
                DbLoader.logger.debug("Baseline was instantiated on : " + this.m_instantiationBaseline);
            }
        } catch (SQLException e) {
            DbLoader.logger.error("Error during DMAJ_BLINE Select", e);
        } finally {
            JdbcDAOUtils.closeResultSet(rs);
            JdbcDAOUtils.closeStatement(sta);
        }
    }

    private void createElements(Map sameLevelElementHash, Map peremptionHashtable) {
        String updateDmajEltQuery = "UPDATE ELEMENT SET DMAJ_ELT = {fn now()} WHERE ID_ELT = ?";
        PreparedStatement updateDmajEltStmt = null;
        try {
            updateDmajEltStmt = m_conn.prepareStatement(updateDmajEltQuery);
            for (int m_line = 1; m_line < this.m_data.length; m_line++) {
                String elementName = (String) this.m_data[m_line][0];
                ElementOfArchitecture elt = (ElementOfArchitecture) sameLevelElementHash.get(elementName);
                String elementID = null;

                if (elt != null) {
                    elementID = elt.getId();
                }
                if (elementID == null) {
                    DbLoader.logger.debug(elementName + " doesn't exist.");
                    if (this.loaderConfig.isCreateIfDoesNotExists()) {
                        //if element doesn't exist should i insert it?
                        elementID = this.insertElement((String) this.m_data[m_line][0], m_line);
                    }
                } else {
                    DbLoader.logger.debug(elementName + " already exist. Id is " + elementID);
                    this.mEltAndLinks.put(elementName, elementID);
                    //remove from list all present element at the end will be perempted.
                    peremptionHashtable.remove(elementName);
                    //not necessary because element should be linked on 1st creation
                    updateDmajEltStmt.setString(1, elementID);
                    updateDmajEltStmt.executeUpdate();
                }
                this.insertDataForElement(elementID, m_line);
            }
        } catch (Exception e) {
            DbLoader.logger.error(e.toString());
            JdbcDAOUtils.rollbackConnection(this.m_conn);
        }
    }


    private String insertElement(String desc, int line) throws Exception {
        //start : insert the element

        String idElt = IDCreator.getID();
        
        this.mEltAndLinks.put(desc, idElt);

        String lib = StringUtils.getStringBetween(desc, '.', '(', 64);
        
        String elementQuery;
        if (!this.loaderConfig.isFilePathAndLineActive()) {
        	elementQuery = "INSERT INTO ELEMENT (ID_ELT,LIB_ELT,DESC_ELT,ID_PRO,ID_TELT,ID_MAIN_ELT,DINST_ELT,DMAJ_ELT) VALUES('" + idElt + "','" + lib + "','" + desc + "','" + this.loaderConfig.getProjectId() + "','" + this.m_curElementTypeId + "','" + this.loaderConfig.getEa().getId() + "',{fn now()},{fn now()})";
        }
        else {
            String filePath = (String) this.m_data[line][this.loaderConfig.getFilePathColumn()];
            elementQuery = "INSERT INTO ELEMENT (ID_ELT,LIB_ELT,DESC_ELT,ID_PRO,ID_TELT,ID_MAIN_ELT,DINST_ELT,DMAJ_ELT,FILEPATH) VALUES('"+ idElt +"','"+lib+"','"+desc+"','"+this.loaderConfig.getProjectId()+"','"+this.m_curElementTypeId+"','" + this.loaderConfig.getEa().getId() + "',{fn now()},{fn now()},'"+filePath+"')";
        }
        String elementLinkQuery = "INSERT INTO ELT_LINKS (ELT_PERE,ELT_FILS,DINST_LINKS,DMAJ_LINKS) VALUES('" + this.loaderConfig.getEa().getId() + "','" + idElt + "',{fn now()},{fn now()})";

        Statement stmt = null;
        try {
            stmt = m_conn.createStatement();
            stmt.execute(elementQuery);
            if (this.m_curFile.isCreateLinkWithEa()) {
                stmt.execute(elementLinkQuery);
            }
        } catch (Exception e) {
            DbLoader.logger.error(e.toString());
            throw e;
        } finally {
            JdbcDAOUtils.closeStatement(stmt);
        }
        //end : element inserted
        return idElt.toUpperCase();
    }

    private void loadDataForEaElement(String eaId) {
        if (this.m_retainedFileColumnIndex.size() > 0) {
            //for each element in the file
            for (int m_line = 1; m_line < this.m_data.length; m_line++) {
                this.insertDataForElement(eaId.toUpperCase(), m_line);
            }
        } else {
            DbLoader.logger.debug("No Known Metric Found.");
        }
    }
    
    private void insertDataForElement(String elementId, int m_line) {
        String selectForUpdateQuery = "SELECT id_elt FROM Qametrique WHERE ID_ELT=? AND ID_BLINE=? AND ID_MET=?";
        String insertMetricQuery = "INSERT INTO QAMETRIQUE (ID_ELT,ID_BLINE,ID_MET,VALBRUTE_QAMET,DINST_QAMET,DMAJ_QAMET) VALUES(?,?,?,?,{fn now()},{fn now()})";
        String updateMetricQuery = "UPDATE QAMETRIQUE SET VALBRUTE_QAMET=?, DMAJ_QAMET={fn now()} WHERE ID_ELT=? and ID_BLINE=? and ID_MET=?";

        PreparedStatement selectStmt = null;
        PreparedStatement insertStmt = null;
        PreparedStatement updateStmt = null;
        ResultSet rs = null;
        try {
            selectStmt = m_conn.prepareStatement(selectForUpdateQuery);
            insertStmt = m_conn.prepareStatement(insertMetricQuery);
            updateStmt = m_conn.prepareStatement(updateMetricQuery);
            
            //for each know metric of the element do insertion
            for (int column = 0; column < this.m_retainedFileColumnIndex.size(); column++) {
                int retainedColumnInt = ((Integer) this.m_retainedFileColumnIndex.get(column)).intValue();
                Object valueObj = this.m_data[m_line][retainedColumnInt];
                String valueToInsert = null;

                if (valueObj instanceof Double) {
                    valueToInsert = ((Double) valueObj).toString();
                } else {
                    try {
                        DbLoader.logger.debug("\t\tCast of" + valueObj.toString() + " into Double Successfull.");
                        valueToInsert = valueObj.toString();
                    } catch (Exception e) {
                        DbLoader.logger.debug("\t\tCoudn't cast " + valueObj.toString() + " into Double.");
                        if (valueObj.toString().compareToIgnoreCase("FALSE") == 0) {
                            valueToInsert = "0";
                            DbLoader.logger.debug("\t\t" + valueObj.toString() + " is a Boolean value.");
                        } else if (valueObj.toString().compareToIgnoreCase("TRUE") == 0) {
                            valueToInsert = "1";
                            DbLoader.logger.debug("\t\t" + valueObj.toString() + " is a Boolean value.");
                        } else {
                            DbLoader.logger.debug("\t\tCoudn't cast " + valueObj.toString() + " into Double nor Boolean.");
                            DbLoader.logger.debug("\t\tAssuming the Value is n/a or err change to NULL.");
                            valueToInsert = null;
                        }
                    }
                }

                m_conn.setAutoCommit(false);
                selectStmt.setString(1, elementId.toUpperCase());
                selectStmt.setString(2, this.loaderConfig.getBaseLineId().toUpperCase());
                selectStmt.setString(3, this.m_data[0][retainedColumnInt].toString());
                rs = selectStmt.executeQuery();
                if (rs.next()) {
                    if (valueToInsert != null) {
                        updateStmt.setDouble(1, Double.parseDouble(valueToInsert));
                    }
                    else {
                        updateStmt.setNull(1, java.sql.Types.DOUBLE);
                    }
                    updateStmt.setString(2, elementId.toUpperCase());
                    updateStmt.setString(3, this.loaderConfig.getBaseLineId().toUpperCase());
                    updateStmt.setString(4, this.m_data[0][retainedColumnInt].toString());
                    updateStmt.executeUpdate();
                } else {
                    insertStmt.setString(1, elementId.toUpperCase());
                    insertStmt.setString(2, this.loaderConfig.getBaseLineId().toUpperCase());
                    insertStmt.setString(3, this.m_data[0][retainedColumnInt].toString());
                    if (valueToInsert != null) {
                        insertStmt.setDouble(4, Double.parseDouble(valueToInsert));
                    }
                    else {
                        insertStmt.setNull(4, java.sql.Types.DOUBLE);
                    }
                    insertStmt.executeUpdate();
                }
                JdbcDAOUtils.closeResultSet(rs);
            }
        } catch (Exception e) {
            DbLoader.logger.error(e.toString());
            JdbcDAOUtils.rollbackConnection(this.m_conn);
        } finally {
            JdbcDAOUtils.closeResultSet(rs);
            JdbcDAOUtils.closePrepareStatement(selectStmt);
            JdbcDAOUtils.closePrepareStatement(insertStmt);
            JdbcDAOUtils.closePrepareStatement(updateStmt);
        }

        try {
            m_conn.setAutoCommit(true);
        } catch (SQLException e) {
            DbLoader.logger.error(e.toString());
        }
        DbLoader.logger.debug("END of insertion.");
    }

    private boolean readDataFromFile(String inFile) {
        /*
         * reads a csv file using the SpreadSheetReportReader
         * convert to UPPERCASE all headers.
         * CSV file separator should be a semi column
         */
        boolean ok = true;

        SpreadSheetReportReader reader = new SpreadSheetReportReader();
        reader.loadFileData(inFile, ";");
        this.m_data = reader.getData();

        if ((this.m_data == null) || this.m_data.length == 0) {
            ok = false;
        } else {
            //add the tool.lib
            for (int column = 0; column < this.m_data[0].length; column++) {
                if (this.m_data[0][column] == null) {
                    DbLoader.logger.debug("A column is invalid.");
                    continue;
                }
                this.m_data[0][column] = ((String) this.m_data[0][column]).toUpperCase();
                DbLoader.logger.debug("Renaiming metric : " + this.m_data[0][column]);
            }
        }
        return ok;
    }

    private void checkFileAvailableKnownMetrics(String filename) {
        /*
         * look for the intersection of db known metrics ensemble
         * and csv headers ensemble.
         */
        this.m_retainedFileColumnIndex.clear();
        Collection<String> dbKnownMetrics = this.getDbKnownMetrics();
        for (int column = 0; column < this.m_data[0].length; column++) {

            if (dbKnownMetrics.contains(this.m_data[0][column])) {
                DbLoader.logger.debug("Known metric found at file column : " + column);
                this.m_retainedFileColumnIndex.add(new Integer(column));
            } else {
            	if ("filePath".equalsIgnoreCase((String)this.m_data[0][column])) {
            		this.loaderConfig.setFilePathAndLineActive(true);
            		this.loaderConfig.setFilePathColumn(column);
            	}
                DbLoader.logger.debug("Metric : " + this.m_data[0][column] + " from file : " + filename + " won't be loaded");
            }
        }
        DbLoader.logger.info("for each element " + this.m_retainedFileColumnIndex.size() + " metrics will be inserted.");
    }

    private Collection<String> getDbKnownMetrics() {
        if (this.m_dbKnownMetrics == null) {
            String query = "SELECT ID_MET FROM METRIQUE";
            this.m_dbKnownMetrics = new ArrayList<String>();
            Statement sta = null;
            ResultSet rs = null;
            try {
                sta = this.m_conn.createStatement();
                rs = sta.executeQuery(query);
                while (rs.next()) {
                    String name = rs.getString(1);
                    DbLoader.logger.info("Adding " + name);
                    this.m_dbKnownMetrics.add(name);
                }
            } catch (SQLException e) {
                DbLoader.logger.error(e.toString());
                JdbcDAOUtils.rollbackConnection(this.m_conn);
            } finally {
                JdbcDAOUtils.closeResultSet(rs);
                JdbcDAOUtils.closeStatement(sta);
            }
        }
        return this.m_dbKnownMetrics;
    }
    
}
