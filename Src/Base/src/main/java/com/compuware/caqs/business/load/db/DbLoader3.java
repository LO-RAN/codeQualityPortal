/*
 * DbLoader3.java
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

public class DbLoader3 {//extends DbConnect{

    // declaration du logger
    static protected Logger logger = com.compuware.toolbox.util.logging.LoggerManager.getLogger(Constants.LOG4J_ANALYSIS_LOGGER_KEY);
    
    protected Timestamp instantiationBaseline = null;
    protected Collection<DataFile> filesVector = null;
    
    /*metrics stored in Database*/
    protected Collection<String> dbKnownMetrics = new ArrayList<String>();
    
    /*csv file column index*/
    protected List<Integer> retainedFileColumnIndex = new ArrayList<Integer>();
    
    /* currently inserted file*/
    protected DataFile curFile = null;
    protected DataFileType curElementTypeId = null;
    protected Object[][] data = null;
    protected int line=0;
    protected Map sameLevelElementHash;
    protected Map classElementHash;
    protected File target;
    protected String srcPath;
    
    protected Connection conn;
    protected String webDataAbsolutePath;
    
    protected LoaderConfig loaderConfig = null;

    /** Creates a new instance of DbLoader3 */
    public DbLoader3(LoaderConfig config, Collection<DataFile> files) {
        //initializing
    	this.loaderConfig = config;
        this.filesVector = files;
        // end initializing
        
        //getting configuration
        Properties dynProp = CaqsConfigUtil.getCaqsGlobalConfigProperties();

        try {
            this.webDataAbsolutePath = dynProp.getProperty(Constants.WEB_DATA_DIRECTORY_KEY);
        }
        catch (Exception e) {
            DbLoader3.logger.error("Can't read the properties file. Make sure configPortailQualite.conf is in the CLASSPATH", e);
        }

        //end of getting configuration                                      
	}
    
    
    public void execute() throws LoaderException {
    	EA ea = this.loaderConfig.getEa();
         //Creates Data/idea/idbline directory for graphListing
        this.target = new File(this.webDataAbsolutePath + File.separator 
                + ea.getLibraries() + "-" + ea.getId()
                + File.separator  + loaderConfig.getBaseLineId());
        if (!this.target.exists()) {
        	this.target.mkdirs();
        }
        DbLoader3.logger.debug("creating dir " + this.target.getPath());
        
        DaoFactory daoFactory = DaoFactory.getInstance();
        ArchitectureDao architectureDao = daoFactory.getArchitectureDao();
        
        //opens a db connection
        this.conn = JdbcDAOUtils.getConnection(this, Constants.CAQS_DATASOURCE_KEY);

        try {

            //gets All baseline Details
            this.getBaselineDetails();

            //gets All db known metrics
            this.getDbKnownMetrics();

            Iterator i = this.filesVector.iterator();
            //Start : for each file
            while (i.hasNext()) {
                this.curFile = (DataFile)i.next();
                DbLoader3.logger.info(" reading file : " + this.curFile.getPath());
                boolean ok = this.readDataFromFile( this.curFile.getPath() );

                if(ok){
                    this.checkFileAvailableKnownMetrics(this.curFile.getPath());
                    this.curElementTypeId = this.curFile.getType();

                    if (this.curElementTypeId.equals(DataFileType.EA)){
                        this.loadDataForEaElement(ea.getId());
                    }
                    else{
                        //get Children element of ea where type is similar to current type

                        //needed for peremtion
                        this.sameLevelElementHash = architectureDao.retrieveArchitectureElement(ea.getId(), this.curElementTypeId);

                        if (this.curElementTypeId.compareTo(DataFileType.MET)==0){
                            this.classElementHash = architectureDao.retrieveArchitectureElement(ea.getId(), DataFileType.CLS);
                        }

                        this.createElements();

                        if (this.loaderConfig.isPeremptElementsIfNotUsed()){
                            this.peremptElement();
                        }
                    }

                }

            }
            //End : for each file
            //just for debug and controle purpose logging the projet hierarchie
            this.showClassMethodsList();
        
            //commit db connection
            DbLoader3.logger.info("No error during insertions -> Commit");
            JdbcDAOUtils.commit(conn);
        } catch (SQLException e) {
            DbLoader.logger.error("Error loading data", e);
            JdbcDAOUtils.rollbackConnection(conn);
            throw new LoaderException("Error loading data", e);
        }
        catch (DataAccessException e) {
            DbLoader.logger.error("Error loading data", e);
            JdbcDAOUtils.rollbackConnection(conn);
            throw new LoaderException("Error loading data", e);
        }
        finally {
            JdbcDAOUtils.closeConnection(this.conn);
        }
    }
    
    private void showClassMethodsList() throws DataAccessException {
        DbLoader3.logger.debug("---Starting Class/Methods Listing---");
        String query = "SELECT ID_ELT, DESC_ELT FROM ELEMENT"
            + " WHERE ELEMENT.ID_MAIN_ELT = ?"
            + " And DPEREMPTION IS NULL";

    	EA ea = this.loaderConfig.getEa();
        DaoFactory daoFactory = DaoFactory.getInstance();
        ArchitectureDao architectureDao = daoFactory.getArchitectureDao();
        PreparedStatement st = null;
        ResultSet rs = null;
        try {
            Map classes = architectureDao.retrieveArchitectureElement(ea.getId(), DataFileType.CLS);
            Iterator enumClassesDesc = classes.keySet().iterator();
            st = this.conn.prepareStatement(query);
            while (enumClassesDesc.hasNext()){
                String classDesc = (String) enumClassesDesc.next();
                ElementOfArchitecture eltClass = (ElementOfArchitecture) classes.get(classDesc);
                String classId = eltClass.getId();
                DbLoader3.logger.debug("Class : "+ classDesc);
                st.setString(1, classId);
                rs = st.executeQuery();
                while(rs.next()){
                    DbLoader3.logger.debug("\tmethod : "+ rs.getString(2));
                }
                JdbcDAOUtils.closeResultSet(rs);
            }
        }
        catch(SQLException e){
            DbLoader3.logger.error(e.toString());
            throw new DataAccessException(e);
        }
        finally {
            JdbcDAOUtils.closeResultSet(rs);
            JdbcDAOUtils.closePrepareStatement(st);
        }
        DbLoader3.logger.debug("---End Class/Methods Listing---");
    }
    
    private void peremptElement(){
    	DbLoader3.logger.debug("---Starting Element Peremtion---");
        if (this.sameLevelElementHash!=null){
            Collection peremptedElements = this.sameLevelElementHash.values();
            if (peremptedElements!=null){
                String query = "UPDATE ELEMENT SET DPEREMPTION = {fn now()} WHERE ID_ELT = ?" ;
                PreparedStatement statement0 = null;
                try{
                    statement0 = this.conn.prepareStatement(query);
                    Iterator it = peremptedElements.iterator();
                    while (it.hasNext()){
                        ElementOfArchitecture elt = (ElementOfArchitecture) it.next();
                        String elementID = elt.getId();
                        DbLoader3.logger.debug("Element : " +elementID+ " has been perempted");
                        statement0.setString(1, elementID);
                        statement0.executeUpdate();
                    }
                }
                catch(Exception e){
                	DbLoader3.logger.error("Error during element Dperemtion update", e);
                    JdbcDAOUtils.rollbackConnection(this.conn);
                }
                finally {
                    JdbcDAOUtils.closePrepareStatement(statement0);
                }
            }
        }
        DbLoader3.logger.debug("---End Element Peremtion---");
    }
    
    private void getBaselineDetails(){
        Statement sta = null;
        ResultSet rs = null;
        String query= "SELECT DMAJ_BLINE FROM BASELINE WHERE ID_BLINE ='" + this.loaderConfig.getBaseLineId()+"'";
        try{
            sta = this.conn.createStatement();
            rs = sta.executeQuery(query);
            while(rs.next()){
                this.instantiationBaseline = rs.getTimestamp(1);
                DbLoader3.logger.debug("Baseline was instantiated on : " + this.instantiationBaseline);
            }
        }
        catch(SQLException e){
        	DbLoader3.logger.error("Error during DMAJ_BLINE Select", e);
        }
        finally{
            JdbcDAOUtils.closeResultSet(rs);
            JdbcDAOUtils.closeStatement(sta);
        }
    }
    
    private void createElements(){
        String updateDmajEltQuery = "UPDATE ELEMENT SET DMAJ_ELT = {fn now()} WHERE ID_ELT = ?";
        PreparedStatement updateDmajEltStmt = null;
        String elementLinkQuery = "INSERT INTO ELT_LINKS (ELT_PERE,ELT_FILS,DINST_LINKS,DMAJ_LINKS) VALUES(?, ?, {fn now()}, {fn now()})";
        PreparedStatement elementLinkStmt = null;
        String updatePackIDQuery = "UPDATE ELEMENT SET ID_PACK = ? WHERE ID_ELT = ?" ;                        
        PreparedStatement updatePackIDStmt = null;
        try{
            updateDmajEltStmt = conn.prepareStatement(updateDmajEltQuery);
            elementLinkStmt = conn.prepareStatement(elementLinkQuery);
            updatePackIDStmt = conn.prepareStatement(updatePackIDQuery);
            for (this.line = 1 ; this.line < this.data.length ; this.line++){
                String elementName = (String) this.data[this.line][0];
                ElementOfArchitecture elt = (ElementOfArchitecture) this.sameLevelElementHash.get(elementName);
                String elementID =null;
                String modArchiId = null;
                if (elt!=null){
                    elementID = elt.getId();
                }

                if ( elementID ==null ){
                    DbLoader3.logger.debug(elementName + " doesn't exist." + this.loaderConfig.isCreateIfDoesNotExists());
                    if(this.loaderConfig.isCreateIfDoesNotExists()){
                        //if element doesn't exist should i insert it?
                        elementID = this.insertElement();

                        if(this.curElementTypeId.compareTo(DataFileType.MET)==0){
                        	String className=null;
                        	try {
                        		className = (String) this.data[this.line][this.data[this.line].length-1];
                        	}
                            catch (ClassCastException cce) {
                            	// can happen if last column of csv file is not the name of parent class
                            	// but maybe a numeric value
                            	DbLoader3.logger.error(elementName + " has no parent class name in the csv File.");
                            }                            
                            
                            ElementOfArchitecture eltClass = null;
                            if (className==null){
                            	DbLoader3.logger.error(elementName + " has no parent class name in the csv File.");
                            }
                            else{
                                eltClass = (ElementOfArchitecture) this.classElementHash.get(className);
                            }

                            String parentClassId = null;
                            if (eltClass!=null){
                                parentClassId = eltClass.getId();
                                modArchiId = eltClass.getIdArchiMod();
                            }
                            if (parentClassId==null){
                            	DbLoader3.logger.error(elementName + " has no parent class.");
                            }
                            else{
                                elementLinkStmt.setString(1, parentClassId);
                                elementLinkStmt.setString(2, elementID);
                                elementLinkStmt.executeUpdate();
                                DbLoader3.logger.debug(elementName + " parent class is "+className+" of id : "+parentClassId);
                            }
                        }
                        this.insertDataForElement(elementID);
                    }
                }
                else {
                	DbLoader3.logger.debug(elementName + " already exist. Id is " + elementID);
                    //remove from list all present element at the end will be perempted.
                    this.sameLevelElementHash.remove(elementName);
                    
                    updateDmajEltStmt.setString(1, elementID);
                    updateDmajEltStmt.executeUpdate();
                    this.insertDataForElement(elementID);
                }
                if(this.curElementTypeId.compareTo(DataFileType.MET)==0){
                    if (modArchiId!=null){
                        updatePackIDStmt.setString(1, modArchiId);
                        updatePackIDStmt.setString(2, elementID);
                        DbLoader3.logger.debug("updating architecture module " + modArchiId + " - " + elementID);
                        updatePackIDStmt.executeUpdate();
                    }
                    else{
                    	DbLoader3.logger.debug("Architecture module is null parent is either unaffected or there is an error");
                    }
                }

            }
        }
        catch(Exception e){
        	DbLoader3.logger.error("Error creating element", e);
            JdbcDAOUtils.rollbackConnection(this.conn);
        }
        finally {
            JdbcDAOUtils.closePrepareStatement(elementLinkStmt);
            JdbcDAOUtils.closePrepareStatement(updateDmajEltStmt);
            JdbcDAOUtils.closePrepareStatement(updatePackIDStmt);
        }
    }

    public static String getRelativePath(String fullPath, String basePath) {
        String tmpFull = fullPath.replaceAll("\\\\", "/");
        String tmpBase = basePath.replaceAll("\\\\", "/");
        if (!tmpBase.endsWith("/")) {
             tmpBase = tmpBase + "/";
        }
        tmpBase = tmpBase + "src";
        return tmpFull.substring(tmpBase.length());
    }

    private String insertElement() throws Exception {
        //start : insert the element
        
        String idElt = IDCreator.getID();
        String desc = (String) this.data[this.line][0];
        int firstBracket = desc.lastIndexOf("(");
        
        String lib = desc;
        if(firstBracket > 0){
            String cuttedDesc = desc.substring(0,firstBracket);
            lib = cuttedDesc;
            int lastDot = cuttedDesc.lastIndexOf(".");
            if(lastDot > 0){
                lib = cuttedDesc.substring(lastDot+1);
            }
        }
        else{
            //place here treatment for class names
            int lastDot = desc.lastIndexOf(".");
            if ((lastDot > -1) && (lastDot + 1 < lib.length()) ){
                lib = desc.substring(lastDot + 1,lib.length());
            }
        }
        if (lib.length()>32){
            lib = lib.substring(0,31);
        }
        String elementQuery;
        if (!this.loaderConfig.isFilePathAndLineActive()) {
            elementQuery = "INSERT INTO ELEMENT (ID_ELT,LIB_ELT,DESC_ELT,ID_PRO,ID_TELT,ID_MAIN_ELT,DINST_ELT,DMAJ_ELT) VALUES('"+ idElt +"','"+lib+"','"+this.data[this.line][0]+"','"+this.loaderConfig.getProjectId()+"','"+this.curElementTypeId+"','" + this.loaderConfig.getEa().getId() + "',{fn now()},{fn now()})";
        }
        else {
            String filePath = (String) this.data[this.line][this.loaderConfig.getFilePathColumn()];
            elementQuery = "INSERT INTO ELEMENT (ID_ELT,LIB_ELT,DESC_ELT,ID_PRO,ID_TELT,ID_MAIN_ELT,DINST_ELT,DMAJ_ELT,FILEPATH) VALUES('"+ idElt +"','"+lib+"','"+this.data[this.line][0]+"','"+this.loaderConfig.getProjectId()+"','"+this.curElementTypeId+"','" + this.loaderConfig.getEa().getId() + "',{fn now()},{fn now()},'"+filePath+"')";
        }
        String elementLinkQuery = "INSERT INTO ELT_LINKS (ELT_PERE,ELT_FILS,DINST_LINKS,DMAJ_LINKS) VALUES('"+ this.loaderConfig.getEa().getId() +"','"+ idElt +"',{fn now()},{fn now()})";
        Statement stmt = null;
        try{
            stmt = conn.createStatement();
            stmt.execute(elementQuery);
            if (this.curFile.isCreateLinkWithEa()) {
                stmt.execute(elementLinkQuery);
            }
        }
        catch(Exception e){
        	DbLoader3.logger.error(e.toString());
            throw e;
        }
        finally {
            JdbcDAOUtils.closeStatement(stmt);
        }
        //end : element inserted
        return idElt.toUpperCase() ;
    }
    
    private void loadDataForEaElement(String eaId){
        if (this.retainedFileColumnIndex.size() > 0){
            //for each element in the file
            for (this.line = 1 ; this.line < this.data.length ; this.line++){
                this.insertDataForElement( eaId.toUpperCase() );
            }
        }
        else{
        	DbLoader3.logger.debug("No Known Metric Found.");
        }
    }
    
    private void copyFiles(String elementId){
        File dataFile = new File(curFile.getPath());
        if(this.curElementTypeId.compareTo(DataFileType.MET)==0){
            List<String> extensions = new ArrayList<String>();
            extensions.add("_source.txt");
            extensions.add("_flow.pdf");
            for (int i = 0 ; i < extensions.size() ; i ++){
                String ext = (String) extensions.get(i);
                File fi = new File(dataFile.getParent()+File.separator +"m"+ this.line+ext);
                DbLoader3.logger.debug("looking for file : " + fi);
                if(fi.exists()){
                    File fiCible = new File(this.target.getPath()+File.separator +"m"+ elementId +ext);
                    DbLoader3.logger.debug("Copy file : " + fiCible);
                    try {
                        com.compuware.toolbox.io.FileTools.copy(fi, fiCible);
                    }
                    catch(java.io.IOException ex) {
                    	DbLoader3.logger.error("Error copying source files", ex);
                    }
                    //fi.renameTo(fiCible);
                }
            }
        }
    }
    
    private void insertDataForElement(String elementId){
        //Copy graphListing files to web
        this.copyFiles(elementId);
        //end : Copy graphListing files to web
        
        String selectForUpdateQuery = "SELECT id_elt FROM Qametrique WHERE ID_ELT=? AND ID_BLINE=? AND ID_MET=?";
        String insertMetricQuery = "INSERT INTO QAMETRIQUE (ID_ELT,ID_BLINE,ID_MET,VALBRUTE_QAMET,DINST_QAMET,DMAJ_QAMET) VALUES(?,?,?,?,{fn now()},{fn now()})";
        String updateMetricQuery = "UPDATE QAMETRIQUE SET VALBRUTE_QAMET=?, DMAJ_QAMET={fn now()} WHERE ID_ELT=? and ID_BLINE=? and ID_MET=?";
        
        PreparedStatement selectStmt = null;
        PreparedStatement insertStmt = null;
        PreparedStatement updateStmt = null;
        ResultSet rs = null;
        try{
            selectStmt = conn.prepareStatement(selectForUpdateQuery);
            insertStmt = conn.prepareStatement(insertMetricQuery);
            updateStmt = conn.prepareStatement(updateMetricQuery);

            Object valueObj;
            //for each know metric of the element do insertion
            for(int column = 0 ; column < this.retainedFileColumnIndex.size() ; column++){
                int retainedColumnInt = ((Integer)this.retainedFileColumnIndex.get(column)).intValue();
                valueObj = this.data[this.line][retainedColumnInt];
                if (valueObj == null) {
                    valueObj = new Double(0);
                }
                String valueToInsert = null;
                if (valueObj instanceof Double){
                    valueToInsert = ((Double) valueObj).toString();
                }
                else{
                    try{
                        new Double( valueObj.toString() );
                        DbLoader3.logger.debug("\t\tCast of" + valueObj.toString() + " into Double Successfull.");
                        valueToInsert = valueObj.toString();
                    }
                    catch(Exception e){
                    	DbLoader3.logger.debug("\t\tCoudn't cast " + valueObj.toString() + " into Double.");
                        if (valueObj.toString().compareToIgnoreCase("FALSE")==0){
                            valueToInsert = "0";
                            DbLoader3.logger.debug("\t\t" + valueObj.toString() + " is a Boolean value.");
                        }
                        else if (valueObj.toString().compareToIgnoreCase("TRUE")==0){
                            valueToInsert = "1";
                            DbLoader3.logger.debug("\t\t" + valueObj.toString() + " is a Boolean value.");
                        }
                        else{
                        	DbLoader3.logger.debug("\t\tCoudn't cast " + valueObj.toString() + " into Double nor Boolean.");
                        	DbLoader3.logger.debug("\t\tAssuming the Value is n/a or err change to NULL.");
                            valueToInsert = null;
                        }
                    }
                }

                conn.setAutoCommit(false);
                selectStmt.setString(1, elementId.toUpperCase());
                selectStmt.setString(2, this.loaderConfig.getBaseLineId().toUpperCase());
                
            	DbLoader3.logger.debug("BASELINE : "+this.loaderConfig.getBaseLineId().toUpperCase() );

                
                selectStmt.setString(3, this.data[0][retainedColumnInt].toString());
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
                    updateStmt.setString(4, this.data[0][retainedColumnInt].toString());
                    updateStmt.executeUpdate();
                }
                else {
                    insertStmt.setString(1, elementId.toUpperCase());
                    insertStmt.setString(2, this.loaderConfig.getBaseLineId().toUpperCase());
                    insertStmt.setString(3, this.data[0][retainedColumnInt].toString());
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
        }
        catch(Exception e){
        	DbLoader3.logger.error("Error insert data", e);
            JdbcDAOUtils.rollbackConnection(this.conn);
        }
        finally {
            JdbcDAOUtils.closeResultSet(rs);
            JdbcDAOUtils.closePrepareStatement(selectStmt);
            JdbcDAOUtils.closePrepareStatement(insertStmt);
            JdbcDAOUtils.closePrepareStatement(updateStmt);
        }
        
        try {
            conn.setAutoCommit(true);
        }
        catch (SQLException e) {
        	DbLoader3.logger.error(e.toString());
        }
        DbLoader3.logger.debug("END of insertion.");
    }
    
    private boolean readDataFromFile(String inFile){
        /*
         * reads a csv file using the SpreadSheetReportReader
         * convert to UPPERCASE all headers.
         * CSV file separator should be a semi column
         */
        boolean ok = true;
        
        SpreadSheetReportReader reader = new SpreadSheetReportReader();
        reader.loadFileData(inFile,";");
        this.data = reader.getData();
        
        if ((this.data==null)||this.data.length==0){
            ok = false;
        }
        else{
            //add the tool.lib
            for(int column =0; column < this.data[0].length; column++){
                this.data[0][column] = ((String)this.data[0][column]).toUpperCase();
                DbLoader3.logger.debug("Renaiming metric : "+this.data[0][column]);
            }
        }
        return ok;
    }
    
    private void checkFileAvailableKnownMetrics(String filename){
        /*
         * look for the intersection of db known metrics ensemble
         * and csv headers ensemble.
         */
        this.retainedFileColumnIndex.clear();
        for(int column =0; column < this.data[0].length; column++){
            
            if( this.dbKnownMetrics.contains(this.data[0][column])){
            	DbLoader3.logger.debug("Known metric found at file column : " + column);
                this.retainedFileColumnIndex.add(new Integer(column));
            }
            else{
            	if ("filePath".equalsIgnoreCase((String)this.data[0][column])) {
            		this.loaderConfig.setFilePathAndLineActive(true);
            		this.loaderConfig.setFilePathColumn(column);
            	}
            	DbLoader3.logger.debug("Metric : "+ this.data[0][column] + " from file : " + filename + " won't be loaded");
            }
        }
        DbLoader3.logger.info("for each element " + this.retainedFileColumnIndex.size() + " metrics will be inserted.");
    }
    
    private void getDbKnownMetrics(){
        String query = "SELECT ID_MET FROM METRIQUE";
        DbLoader3.logger.debug(query);
        Statement sta = null;
        ResultSet rs = null;
        try{
            sta = this.conn.createStatement();
            rs = sta.executeQuery(query);
            while(rs.next()){
                String name = rs.getString(1);
                DbLoader3.logger.debug("Adding " + name);
                this.dbKnownMetrics.add(name);
            }
        }
        catch(SQLException e){
        	DbLoader3.logger.error(e.toString());
            JdbcDAOUtils.rollbackConnection(this.conn);
        }
        finally {
            JdbcDAOUtils.closeResultSet(rs);
            JdbcDAOUtils.closeStatement(sta);
        }
    }

    public static void main(String args[]) {
        System.out.println(DbLoader3.getRelativePath("D:\\CarsCode\\Temp\\", "D:\\CarsCode\\"));
    }

}
