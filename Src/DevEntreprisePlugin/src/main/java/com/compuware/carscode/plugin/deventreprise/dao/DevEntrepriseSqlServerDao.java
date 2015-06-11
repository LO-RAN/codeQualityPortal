package com.compuware.carscode.plugin.deventreprise.dao;

import com.compuware.carscode.plugin.deventreprise.dataschemas.CobolSource;
import com.compuware.carscode.plugin.deventreprise.dataschemas.Copy;
import com.compuware.carscode.plugin.deventreprise.util.RetrieveExternalizedMetrics;
import com.compuware.carscode.plugin.deventreprise.util.ReturnCodes;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.compuware.carscode.plugin.deventreprise.dataschemas.Variable;
import com.compuware.carscode.plugin.deventreprise.dataschemas.Move;
import com.compuware.carscode.plugin.deventreprise.dataschemas.Violation;
import com.compuware.carscode.plugin.deventreprise.util.Constants;
import com.compuware.toolbox.dbms.JdbcDAOUtils;
import com.compuware.toolbox.util.logging.LoggerManager;

public class DevEntrepriseSqlServerDao implements DevEntrepriseDao {

    static protected Logger logger = LoggerManager.getLogger("StaticAnalysis");
    private static Connection connection = null;
    private static DevEntrepriseSqlServerDao instance = null;

    public ReturnCodes setConnection(Properties dynProp) {
        ReturnCodes retour = ReturnCodes.ERROR_CONNECTING_TO_DATABASE;
        String serverName = dynProp.getProperty("sqlserver.servername");
        String userName = dynProp.getProperty("sqlserver.username");
        String password = dynProp.getProperty("sqlserver.password");
        String databaseName = dynProp.getProperty("sqlserver.databaseName");
        String url = "jdbc:sqlserver://" + serverName + ";databaseName=" +
                databaseName + ";userName=" + userName + ";password=" + password +
                ";selectMethod=cursor";
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver").newInstance();
            connection = DriverManager.getConnection(url);
            if (connection != null) {
                retour = ReturnCodes.NO_ERROR;
            }
        } catch (ClassNotFoundException e) {
            logger.error("ClassNotFoundException " + e.getMessage());
        } catch (IllegalAccessException e) {
            logger.error("IllegalAccessException " + e.getMessage());
        } catch (InstantiationException e) {
            logger.error("InstantiationException " + e.getMessage());
        } catch (SQLException e) {
            logger.error("SQLException " + e.getMessage());
        }
        return retour;
    }

    public void closeConnection() {
        JdbcDAOUtils.closeConnection(connection);
    }

    public static DevEntrepriseSqlServerDao getInstance() {
        if (DevEntrepriseSqlServerDao.instance == null) {
            DevEntrepriseSqlServerDao.instance = new DevEntrepriseSqlServerDao();
        }
        return DevEntrepriseSqlServerDao.instance;
    }

    private DevEntrepriseSqlServerDao() {
    }

    private Connection getConnection() {
        return connection;
    }
    private static final String RETRIEVE_LEARNING_STATUS_QUERY =
            "SELECT attr_data_value " +
            "FROM cw_objects, cw_attributes " +
            "WHERE cw_objects.id = ? " +
            "AND cw_objects.id = cw_attributes.id " +
            "AND attr_type_id = " + Constants.LEARNING_STATUS;

    public String getLearningStatus(int programId) {
        String retour = "";
        Connection conn = this.getConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            if (conn != null) {
                ps = conn.prepareStatement(RETRIEVE_LEARNING_STATUS_QUERY);
                ps.setInt(1, programId);
                rs = ps.executeQuery();
                if (rs != null && rs.next()) {
                    retour = rs.getString("attr_data_value");
                }
            }
        } catch (SQLException exc) {
            logger.error("SQLException " + exc.getMessage());
        } finally {
            JdbcDAOUtils.closeResultSet(rs);
            JdbcDAOUtils.closeStatement(ps);
        }
        return retour;
    }
    private static final String RETRIEVE_LAST_COMPLETED_PROGRAMS_QUERY =
            "SELECT obj2.id, obj2.name, d.attr_data_value" +
            " FROM cw_objects obj1, cw_objects obj2, cw_relationships rel, cw_attributes d, cw_attributes s" +
            " WHERE obj2.type_id = " + Constants.PROGRAM_TYPE_ID +
            " AND obj1.name = ?" + " AND obj1.type_id = " +
            Constants.COLLECTION_TYPE_ID + " AND rel.obj_id_1 = obj1.id" +
            " AND rel.obj_id_2 = obj2.id" + " AND rel.type_id = " +
            Constants.COLLECTED_RELATIONSHIP + " AND obj2.id = s.id" +
            " AND s.attr_type_id = " + Constants.LEARNING_STATUS +
            " AND s.attr_data_value = 'Complete'" + " AND obj2.id = d.id" +
            " AND d.attr_type_id = " + Constants.LEARNED_TIMESTAMP +
            " AND d.attr_data_value >= ?";

    public List<String> retrieveLastCompletedPrograms(String collectionName, String timestampAsString) {
        List<String> result = new ArrayList<String>();
        PreparedStatement ps = null;
        ResultSet rs = null;
        Connection conn = this.getConnection();

        try {
            if (conn != null) {
                ps = conn.prepareStatement(RETRIEVE_LAST_COMPLETED_PROGRAMS_QUERY);
                ps.setString(1, collectionName);
                ps.setString(2, timestampAsString);
                rs = ps.executeQuery();
                while (rs.next()) {
                    result.add("" + rs.getString("name"));
                }
            }
        } catch (SQLException exc) {
            logger.error("SQLException " + exc.getMessage());
        } finally {
            JdbcDAOUtils.closeResultSet(rs);
            JdbcDAOUtils.closeStatement(ps);
        }
        return result;
    }
    private static final String PENDING_LEARNING_EXIST_QUERY =
            "SELECT count(obj2.id)" +
            " FROM cw_objects obj1, cw_objects obj2, cw_relationships rel, cw_attributes" +
            " WHERE obj2.type_id = " + Constants.PROGRAM_TYPE_ID +
            " AND obj1.name = ?" + " AND obj1.type_id = " +
            Constants.COLLECTION_TYPE_ID + " AND rel.obj_id_1 = obj1.id" +
            " AND rel.obj_id_2 = obj2.id" + " AND rel.type_id = " +
            Constants.COLLECTED_RELATIONSHIP + " AND obj2.id = cw_attributes.id" +
            " AND attr_type_id = " + Constants.LEARNING_STATUS +
            " AND attr_data_value = ?";

    public int countLearningProgramsWithStatus(String collectionName, String status) {
        int result = 0;
        Connection conn = this.getConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            if (conn != null) {
                ps = conn.prepareStatement(PENDING_LEARNING_EXIST_QUERY);
                ps.setString(1, collectionName);
                ps.setString(2, status);
                rs = ps.executeQuery();
                if (rs != null && rs.next()) {
                    result = rs.getInt(1);
                }
            }
        } catch (SQLException exc) {
            logger.error("SQLException " + exc.getMessage());
        } finally {
            JdbcDAOUtils.closeResultSet(rs);
            JdbcDAOUtils.closeStatement(ps);
        }
        return result;
    }
    private static final String FILE_LINE_QUERY =
            "SELECT attr_data_value" +
            " FROM cw_attributes" +
            " WHERE id = ?" +
            " AND attr_type_id = " + Constants.FILE_LINE;

    public String getFileLine(String objId) {
        Connection conn = this.getConnection();
        String retour = "";
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            if (conn != null) {
                ps = conn.prepareStatement(FILE_LINE_QUERY);
                ps.setString(1, objId);
                rs = ps.executeQuery();
                if (rs != null && rs.next()) {
                    retour = rs.getString("attr_data_value");
                }
            }
        } catch (SQLException exc) {
            logger.error("SQLException " + exc.getMessage());
        } finally {
            JdbcDAOUtils.closeResultSet(rs);
            JdbcDAOUtils.closeStatement(ps);
        }
        return retour;
    }
    private static String GET_PROGRAM_ID_QUERY =
            "SELECT obj2.id " +
            " FROM cw_objects obj1, cw_objects obj2, cw_relationships rel" +
            " WHERE obj2.name = ? " +
            " AND obj2.type_id IN (" + Constants.PROGRAM_TYPE_ID + ',' +
            Constants.INCLUDE + ')' +
            " AND obj1.name = ?" +
            " AND obj1.type_id = " + Constants.COLLECTION_TYPE_ID +
            " AND rel.obj_id_1 = obj1.id" +
            " AND rel.obj_id_2 = obj2.id" +
            " AND rel.type_id = " + Constants.COLLECTED_RELATIONSHIP;

    public int getIdFromDatabase(String name, String collection) {
        int retour = 0;
        PreparedStatement ps = null;
        ResultSet rs = null;
        Connection conn = this.getConnection();

        try {
            if (conn != null) {
                ps = conn.prepareStatement(GET_PROGRAM_ID_QUERY);
                ps.setString(1, name);
                ps.setString(2, collection);
                rs = ps.executeQuery();
                if (rs != null) {
                    if (rs.next()) {
                        retour = rs.getInt("id");
                    }
                }
            }
        } catch (SQLException exc) {
            logger.error("SQLException " + exc.getMessage());
        } finally {
            JdbcDAOUtils.closeResultSet(rs);
            JdbcDAOUtils.closeStatement(ps);
        }
        return retour;
    }
    private static final String GET_COMPUTE_OPERANDS_QUERY =
            "SELECT rel.obj_id_1 as obj_id_1, obj1.name as obj1_name, rel.obj_id_2 as obj_id_2, rel.rel_detail as corr, obj2.name as ownerName, obj2.type_id" +
            " FROM cw_relationships rel, cw_objects obj1, cw_relationships rel2, cw_objects obj2" +
            " WHERE " +
            " ( " +
            "       rel.rel_detail like 'COMPUTE;%'" +
            "    or rel.rel_detail like 'ADD;%'" +
            "    or rel.rel_detail like 'SUBTRACT;%'" +
            "    or rel.rel_detail like 'MULTIPLY;%'" +
            "    or rel.rel_detail like 'DIVIDE;%'" +
            " ) " +
            " and obj1.id = rel.obj_id_1 " +
            " and rel2.obj_id_1 = ? and rel2.obj_id_2 = obj1.id and rel2.type_id =  " +
            Constants.CONTAINS_RELATIONSHIP +
            " and obj1.owner like ?" +
            " and obj2.id = rel.obj_id_related";

    public List<Violation> getNbArithmeticOverflow(int id, String name) {
        List<Violation> retour = new ArrayList<Violation>();
        Connection conn = this.getConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;
        Map<String, String> recepteurs = new HashMap<String, String>();
        Map<String, List<String>> operandes = new HashMap<String, List<String>>();
        Map<String, String> operandsNames = new HashMap<String, String>();
        Map<String, String> owners = new HashMap<String, String>();
        try {
            if (conn != null) {
                ps = conn.prepareStatement(GET_COMPUTE_OPERANDS_QUERY);
                ps.setInt(1, id);
                ps.setString(2, name + "%");
                rs = ps.executeQuery();
            }
            if (rs != null) {
                while (rs.next()) {
                    String corr = rs.getString("corr");

                    int typeId = rs.getInt("type_id");
                    String owner = rs.getString("ownerName");
                    if (typeId == Constants.INCLUDE) {
                        owner = "INCLUDE." + owner;
                    }
                    owners.put(corr, owner);

                    String objId1 = rs.getString("obj_id_1");
                    String objId2 = rs.getString("obj_id_2");
                    recepteurs.put(corr, objId2);
                    operandsNames.put(objId1, rs.getString("obj1_name"));
                    List<String> ops = operandes.get(corr);
                    if (ops == null) {
                        ops = new ArrayList<String>();
                        operandes.put(corr, ops);
                    }
                    ops.add(objId1);
                }
                for (Iterator<String> it = recepteurs.keySet().iterator(); it.hasNext();) {
                    String corr = it.next();
                    String recepteurId = recepteurs.get(corr);
                    List<String> operandsIds = operandes.get(corr);
                    int recepteurLength = this.getLengthForDataItem(recepteurId);
                    int operandsLengthes = 0;
                    for (Iterator<String> itOperandsIds = operandsIds.iterator(); itOperandsIds.hasNext();) {
                        String opId = itOperandsIds.next();
                        int l = this.getLengthForDataItem(opId);
                        if (l > 0) {
                            operandsLengthes += l;
                        } else {
                            String opName = operandsNames.get(opId);
                            if (opName != null) {
                                operandsLengthes += opName.length();
                            }
                        }
                    }
                    if ((recepteurLength < operandsLengthes) &&
                            (recepteurLength > 0)) {
                        String line = this.getLineFromRelDetail(corr);
                        if (line != null) {
                            Violation v = new Violation(line, owners.get(corr));
                            retour.add(v);
                        }
                    }
                }
            }
        } catch (SQLException exc) {
            logger.error("SQLException " + exc.getMessage());
        } finally {
            JdbcDAOUtils.closeResultSet(rs);
            JdbcDAOUtils.closeStatement(ps);
        }
        return retour;
    }
    private static final String LST_COMPLEX_COMPUTE =
            "SELECT rel.rel_detail as corr, obj2.name as ownerName, obj2.type_id" +
            " FROM cw_relationships rel, cw_objects obj1, cw_relationships rel2, cw_objects obj2" +
            " WHERE " +
            " ( " +
            "       rel.rel_detail like 'COMPUTE;%'" +
            " ) " +
            " and obj1.id = rel.obj_id_1 " +
            " and rel2.obj_id_1 = ? and rel2.obj_id_2 = obj1.id and rel2.type_id =  " +
            Constants.CONTAINS_RELATIONSHIP +
            " and obj1.owner like ?" +
            " and obj2.id = rel.obj_id_related";

    public List<Violation> getLstComplexCompute(int id, String name) {
        List<Violation> retour = new ArrayList<Violation>();
        Connection conn = this.getConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;
        Map<String, Integer> correspondances = new HashMap<String, Integer>();
        Map<String, String> owners = new HashMap<String, String>();
        try {
            if (conn != null) {
                ps = conn.prepareStatement(LST_COMPLEX_COMPUTE);
                ps.setInt(1, id);
                ps.setString(2, name + "%");
                rs = ps.executeQuery();
            }
            if (rs != null) {
                while (rs.next()) {
                    String corr = rs.getString("corr");
                    int typeId = rs.getInt("type_id");
                    String owner = rs.getString("ownerName");
                    if (typeId == Constants.INCLUDE) {
                        owner = "INCLUDE." + owner;
                    }
                    owners.put(corr, owner);
                    Integer i = correspondances.get(corr);
                    int compte = 0;
                    if (i != null) {
                        compte = i.intValue();
                    }
                    compte++;
                    correspondances.put(corr, new Integer(compte));
                }
                Collection<Integer> coll = correspondances.values();
                if (coll != null && !coll.isEmpty()) {
                    for (Iterator<Map.Entry<String, Integer>> itEntries = correspondances.entrySet().iterator(); itEntries.hasNext();) {
                        Map.Entry<String, Integer> entry = itEntries.next();
                        Integer compte = entry.getValue();
                        if (compte.intValue() > 4) {
                            String relDetail = entry.getKey();
                            String line = this.getLineFromRelDetail(relDetail);
                            if (line != null) {
                                Violation v = new Violation(line, owners.get(relDetail));
                                retour.add(v);
                            }
                        }
                    }
                }
            }
        } catch (SQLException exc) {
            logger.error("SQLException " + exc.getMessage());
        } finally {
            JdbcDAOUtils.closeResultSet(rs);
            JdbcDAOUtils.closeStatement(ps);
        }
        return retour;
    }
    private static final String GET_DATA_ITEMS_LENGTH_QUERY =
            "SELECT attr.attr_data_value as length" +
            " FROM cw_objects as item, cw_relationships as rel, cw_attributes as attr " +
            " WHERE " +
            " item.type_id = " + Constants.DATA_ITEM +
            " AND attr.attr_type_id = " + Constants.LENGTH +
            " AND attr.id = item.id " +
            " AND item.id = rel.obj_id_2 " +
            " AND rel.type_id = " + Constants.CONTAINS_RELATIONSHIP +
            " AND rel.obj_id_1 = ?" +
            " AND item.owner like ?";

    public int getMaxDataItemLength(int id, String name) {
        int retour = 0;
        PreparedStatement ps = null;
        ResultSet rs = null;
        Connection conn = this.getConnection();

        try {
            if (conn != null) {
                ps = conn.prepareStatement(GET_DATA_ITEMS_LENGTH_QUERY);
                ps.setInt(1, id);
                ps.setString(2, name + "%");
                rs = ps.executeQuery();
            }
            if (rs != null) {
                while (rs.next()) {
                    int length = rs.getInt("length");
                    if (length > retour) {
                        retour = length;
                    }
                }
            }
        } catch (SQLException exc) {
            logger.error("SQLException " + exc.getMessage());
        } finally {
            JdbcDAOUtils.closeResultSet(rs);
            JdbcDAOUtils.closeStatement(ps);
        }
        return retour;
    }

    private boolean isNumericPicture(String pict) {
        return (pict.startsWith("9") || this.isSignedNumericPicture(pict));
    }

    private boolean isSignedNumericPicture(String pict) {
        return (pict.startsWith("S") || pict.startsWith("+") ||
                pict.startsWith("-") ||
                pict.startsWith("CR") || pict.startsWith("DB"));
    }
    private static final String GET_PICTURE_ATTRIBUTE_VALUE_QUERY = "SELECT attr01.attr_data_value as val01, " +
            "      attr02.attr_data_value as val02" +
            " FROM cw_attributes attr01, cw_attributes attr02 " +
            " WHERE  " +
            " attr01.id = ? AND attr01.attr_type_id = " + Constants.PICTURE +
            " AND " +
            " attr02.id = ? AND attr02.attr_type_id = " + Constants.PICTURE;

    public Map<String, List<Violation>> getNbMoveProblems(int id, String name) {
        Connection conn = null;
        Map<String, List<Violation>> retour = new HashMap<String, List<Violation>>();
        List<Move> moves = this.getMoves(id, name);
        List<Violation> l = null;
        if (!moves.isEmpty()) {
            conn = this.getConnection();
            for (Move m : moves) {
                PreparedStatement ps = null;
                ResultSet rs = null;

                try {
                    if (conn != null) {
                        ps = conn.prepareStatement(GET_PICTURE_ATTRIBUTE_VALUE_QUERY);
                        ps.setString(1, m.getFrom());
                        ps.setString(2, m.getTo());
                        rs = ps.executeQuery();
                    }
                    if (rs != null) {
                        while (rs.next()) {
                            String type01 = rs.getString("val01");
                            String type02 = rs.getString("val02");
                            if (type01.startsWith("X") || type01.startsWith("A")) {
                                if (this.isNumericPicture(type02)) {
                                    l = retour.get(DevEntrepriseDao.ALPHANUMERIC_TO_NUMERIC);
                                    if (l == null) {
                                        l = new ArrayList<Violation>();
                                        retour.put(DevEntrepriseDao.ALPHANUMERIC_TO_NUMERIC, l);
                                    }
                                    l.add(new Violation(m.getLine(), m.getOwner()));
                                }
                            }
                            if (type01.startsWith("X")) {
                                if (type02.startsWith("A")) {
                                    l = retour.get(DevEntrepriseDao.ALPHANUMERIC_TO_ALPHABETIC);
                                    if (l == null) {
                                        l = new ArrayList<Violation>();
                                        retour.put(DevEntrepriseDao.ALPHANUMERIC_TO_ALPHABETIC, l);
                                    }
                                    l.add(new Violation(m.getLine(), m.getOwner()));
                                }
                            }
                            if (type01.startsWith("A")) {
                                if (this.isNumericPicture(type02)) {
                                    l = retour.get(DevEntrepriseDao.ALPHABETIC_TO_NUMERIC);
                                    if (l == null) {
                                        l = new ArrayList<Violation>();
                                        retour.put(DevEntrepriseDao.ALPHABETIC_TO_NUMERIC, l);
                                    }
                                    l.add(new Violation(m.getLine(), m.getOwner()));
                                }
                            }
                            if (this.isNumericPicture(type01)) {
                                if (type02.startsWith("A")) {
                                    l = retour.get(DevEntrepriseDao.NUMERIC_TO_ALPHABETIC);
                                    if (l == null) {
                                        l = new ArrayList<Violation>();
                                        retour.put(DevEntrepriseDao.NUMERIC_TO_ALPHABETIC, l);
                                    }
                                    l.add(new Violation(m.getLine(), m.getOwner()));
                                }
                            }
                            if (this.isNumericPicture(type01)) {
                                if (type02.startsWith("A")) {
                                    l = retour.get(DevEntrepriseDao.NUMERIC_TO_ALPHABETIC);
                                    if (l == null) {
                                        l = new ArrayList<Violation>();
                                        retour.put(DevEntrepriseDao.NUMERIC_TO_ALPHABETIC, l);
                                    }
                                    l.add(new Violation(m.getLine(), m.getOwner()));
                                }
                            }
                            if (this.isSignedNumericPicture(type01) &&
                                    !this.isSignedNumericPicture(type02)) {
                                l = retour.get(DevEntrepriseDao.SIGNED_TO_UNSIGNED);
                                if (l == null) {
                                    l = new ArrayList<Violation>();
                                    retour.put(DevEntrepriseDao.SIGNED_TO_UNSIGNED, l);
                                }
                                l.add(new Violation(m.getLine(), m.getOwner()));
                            }
                        }
                    }
                } catch (SQLException exc) {
                    logger.error("SQLException " + exc.getMessage());
                } finally {
                    JdbcDAOUtils.closeResultSet(rs);
                    JdbcDAOUtils.closeStatement(ps);
                }
            }
        }
        return retour;
    }

    public List<Violation> getNbLowerPrecisionMoves(int id, String name) {
        List<Violation> retour = new ArrayList<Violation>();
        List<Move> moves = this.getMoves(id, name);
        Map<String, Integer> moveItemLengthMap = getAllMoveItemLength(id);
        if (!moves.isEmpty()) {
            for (Move m : moves) {
                Integer fromLength = moveItemLengthMap.get(m.getFrom());
                Integer toLength = moveItemLengthMap.get(m.getTo());
                if (fromLength != null && toLength != null) {
                    if ((fromLength > toLength) && (toLength > 0)) {
                        retour.add(new Violation(m.getLine(), m.getOwner()));
                    }
                }
            }
        }
        return retour;
    }
    private static final String GET_LOGICAL_LENGTH_QUERY =
            "SELECT attr_data_value FROM cw_attributes" +
            " WHERE id = ?" +
            " AND ATTR_TYPE_ID = " + Constants.LOGICAL_LENGTH;
    private static final String GET_LENGTH_QUERY =
            "SELECT attr_data_value FROM cw_attributes" +
            " WHERE id = ?" +
            " AND ATTR_TYPE_ID = " + Constants.LENGTH;

    private int getLengthForDataItem(String dataItemId) {
        int retour = 0;
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = this.getConnection();
            if (conn != null) {
                ps = conn.prepareStatement(GET_LOGICAL_LENGTH_QUERY);
                ps.setString(1, dataItemId);
                rs = ps.executeQuery();
                if (rs != null && rs.next()) {
                    retour = rs.getInt("attr_data_value");
                }
                if (retour == 0) {
                    ps = conn.prepareStatement(GET_LENGTH_QUERY);
                    ps.setString(1, dataItemId);
                    rs = ps.executeQuery();
                    if (rs != null && rs.next()) {
                        retour = rs.getInt("attr_data_value");
                    }
                }
            }
        } catch (SQLException exc) {
            logger.error("SQLException " + exc.getMessage());
        } finally {
            JdbcDAOUtils.closeResultSet(rs);
            JdbcDAOUtils.closeStatement(ps);
        }
        return retour;
    }

    public String getLineFromRelDetail(String relDetail) {
        String retour = "";
        String[] split = relDetail.split(";");
        if (split != null && split.length > 1) {
            retour = split[1];
            try {
                Integer.parseInt(retour);
            } catch (NumberFormatException exc) {
                retour = split[0];
                try {
                    Integer.parseInt(retour);
                } catch (NumberFormatException exc2) {
                    retour = null;
                }
            }
        }
        return retour;
    }
    private static final String GET_MOVE_QUERY = "SELECT OBJ_ID_1, OBJ_ID_2, REL_DETAIL " +
            " FROM CW_RELATIONSHIPS " +
            " WHERE " +
            "   (OBJ_ID_RELATED = ?) AND (TYPE_ID = " + Constants.ACTION_TYPE +
            ") AND (REL_DETAIL LIKE 'MOVE%') " +
            " ORDER BY REL_DETAIL";

    private List<Move> getMoves(int id, String name) {
        List<Move> retour = new ArrayList<Move>();

        PreparedStatement ps = null;
        ResultSet rs = null;
        Connection conn = null;

        try {
            conn = this.getConnection();
            if (conn != null) {
                ps = conn.prepareStatement(GET_MOVE_QUERY);
                ps.setInt(1, id);
                rs = ps.executeQuery();
                if (rs != null) {
                    while (rs.next()) {
                        String id1 = rs.getString("OBJ_ID_1");
                        String id2 = rs.getString("OBJ_ID_2");
                        String relDetail = rs.getString("REL_DETAIL");
                        Move m = new Move(id1, id2);
                        String line = this.getLineFromRelDetail(relDetail);
                        if (line != null) {
                            m.setLine(line);
                            m.setOwner(name);
                            retour.add(m);
                        }
                    }
                }
            }
        } catch (SQLException exc) {
            logger.error("SQLException " + exc.getMessage());
        } finally {
            JdbcDAOUtils.closeResultSet(rs);
            JdbcDAOUtils.closeStatement(ps);
        }

        return retour;
    }
    private static final String GET_ALL_MOVE_LENGTH_QUERY =
            "SELECT distinct a.ID, a2.ATTR_DATA_VALUE, a.ATTR_DATA_VALUE" +
            " FROM CW_RELATIONSHIPS r, CW_ATTRIBUTES a" +
            " LEFT JOIN CW_ATTRIBUTES as a2 on (a2.ID = a.ID and a2.ATTR_TYPE_ID = " +
            Constants.LOGICAL_LENGTH + ")" +
            " WHERE a.ATTR_TYPE_ID = " + Constants.LENGTH +
            " AND (r.OBJ_ID_RELATED = ?) AND (r.TYPE_ID = " +
            Constants.ACTION_TYPE + ") AND (r.REL_DETAIL LIKE 'MOVE%')" +
            " AND (r.OBJ_ID_1 = a.ID OR r.OBJ_ID_2 = a.ID)";

    private Map<String, Integer> getAllMoveItemLength(int id) {
        Map<String, Integer> result = new HashMap<String, Integer>();

        PreparedStatement ps = null;
        ResultSet rs = null;
        Connection conn = null;

        try {
            conn = this.getConnection();
            if (conn != null) {
                ps = conn.prepareStatement(GET_ALL_MOVE_LENGTH_QUERY);
                ps.setInt(1, id);
                rs = ps.executeQuery();
                if (rs != null) {
                    while (rs.next()) {
                        String itemId = rs.getString(1);
                        int length = rs.getInt(2);
                        if (rs.wasNull()) {
                            length = rs.getInt(3);
                        }
                        result.put(itemId, length);
                    }
                }
            }
        } catch (SQLException exc) {
            logger.error("SQLException " + exc.getMessage());
        } finally {
            JdbcDAOUtils.closeResultSet(rs);
            JdbcDAOUtils.closeStatement(ps);
        }

        return result;
    }
    private static final String GET_LINE_NUMBER_QUERY =
            " SELECT cw_attributes.attr_data_value as lineNumber " +
            " FROM cw_attributes " +
            " WHERE " +
            "  cw_attributes.attr_type_id = " + Constants.SOURCE_LINE +
            " AND cw_attributes.id = ?";

    public int getLineNumbers(int id) {
        PreparedStatement ps = null;
        Connection conn = null;
        ResultSet rs = null;
        int retour = 0;

        try {
            if (conn != null) {
                ps = conn.prepareStatement(GET_LINE_NUMBER_QUERY);
                ps.setInt(1, id);
                rs = ps.executeQuery();
                if (rs != null) {
                    if (rs.next()) {
                        retour = rs.getInt("lineNumber");
                    }
                }
            }
        } catch (SQLException exc) {
            logger.error("SQLException " + exc.getMessage());
        } finally {
            JdbcDAOUtils.closeResultSet(rs);
            JdbcDAOUtils.closeStatement(ps);
        }
        return retour;
    }
    private static final String NUMERIC_VARIABLE_MORE_18_DIGIT_QUERY =
            "select attr_data_value, cw_objects.name as object_name, cw_objects.id as object_id, cw_objects.owner as owner" +
            " from cw_attributes, cw_objects where " +
            " attr_type_id = " + Constants.LOGICAL_LENGTH +
            " and cw_objects.id = cw_attributes.id " +
            " and cw_attributes.id in ( SELECT " +
            "  obj1.id as id " +
            " from  " +
            "  cw_attributes " +
            "  inner join cw_objects as obj1 on cw_attributes.id = obj1.id " +
            "  inner join cw_relationships as rel on rel.obj_id_2 = obj1.id " +
            "  inner join cw_objects as obj2 on rel.obj_id_1 = obj2.id " +
            " where  " +
            "  (cw_attributes.attr_data_value like '9%' or cw_attributes.attr_data_value like 'S%' or cw_attributes.attr_data_value like '+%' or cw_attributes.attr_data_value like '-%' or cw_attributes.attr_data_value like 'CR%' or cw_attributes.attr_data_value like 'DB%')" +
            "  and cw_attributes.attr_type_id = " + Constants.PICTURE +
            "  and rel.obj_id_1 = ? " +
            "  and rel.type_id = " + Constants.CONTAINS_RELATIONSHIP +
            "  and obj1.owner like ? )";
    private static final String NUMERIC_VARIABLE_MORE_18_DIGIT_QUERY_WITHOUT_ID =
            "select attr_data_value, cw_objects.name as object_name, cw_objects.id as object_id, cw_objects.owner as owner" +
            " from cw_attributes, cw_objects where " +
            " attr_type_id = " + Constants.LOGICAL_LENGTH +
            " and cw_objects.id = cw_attributes.id " +
            " and cw_attributes.id in ( SELECT " +
            "  obj1.id as id " +
            " from  " +
            "  cw_attributes " +
            "  inner join cw_objects as obj1 on cw_attributes.id = obj1.id " +
            "  inner join cw_relationships as rel on rel.obj_id_2 = obj1.id " +
            "  inner join cw_objects as obj2 on rel.obj_id_1 = obj2.id " +
            " where  " +
            "  (cw_attributes.attr_data_value like '9%' or cw_attributes.attr_data_value like 'S%' or cw_attributes.attr_data_value like '+%' or cw_attributes.attr_data_value like '-%' or cw_attributes.attr_data_value like 'CR%' or cw_attributes.attr_data_value like 'DB%')" +
            "  and cw_attributes.attr_type_id = " + Constants.PICTURE +
            "  and rel.type_id = " + Constants.CONTAINS_RELATIONSHIP +
            "  and obj1.owner like ? )";

    public List<Variable> getNumericVariablesMore18Digits(int id, String name) {
        List<Variable> retour = new ArrayList<Variable>();
        PreparedStatement ps = null;
        ResultSet rs = null;
        Connection conn = null;

        try {
            conn = this.getConnection();
            if (conn != null) {
                if (id != 0) {
                    ps = conn.prepareStatement(NUMERIC_VARIABLE_MORE_18_DIGIT_QUERY);
                    ps.setInt(1, id);
                    ps.setString(2, name);
                } else {
                    ps = conn.prepareStatement(NUMERIC_VARIABLE_MORE_18_DIGIT_QUERY_WITHOUT_ID);
                    ps.setString(1, name);
                }
                rs = ps.executeQuery();
            }
            if (rs != null) {
                while (rs.next()) {
                    int length = rs.getInt("attr_data_value");
                    if (length > 18) {
                        Variable v = new Variable();
                        v.setId(rs.getInt("object_id"));
                        v.setName(rs.getString("object_name"));
                        v.setOwner(rs.getString("owner"));
                        v.setMoreThan18Digits(true);
                        String objId = rs.getString("object_id");
                        String line = this.getFileLine(objId);
                        boolean found = false;
                        for (Variable a : retour) {
                            if (a.getLine().equals(line)) {
                                found = true;
                                break;
                            }
                        }
                        if (!found) {
                            v.setLine(line);
                            retour.add(v);
                        }
                    }
                }
            }
        } catch (SQLException exc) {
            logger.error("SQLException getNumericVariablesMore18Digits " +
                    exc.getMessage());
        } finally {
            JdbcDAOUtils.closeResultSet(rs);
            JdbcDAOUtils.closeStatement(ps);
        }
        return retour;
    }
    private static final String CONDITION_NOT_USED_QUERY =
            " SELECT obj.id as objId, obj.owner " +
            " FROM " +
            " cw_objects as obj, cw_relationships as rel " +
            " WHERE obj.id in (select cw_attributes.id from cw_attributes where cw_attributes.attr_type_id = " +
            Constants.LEVEL +
            " AND cw_attributes.attr_data_value = 88) " +
            " AND rel.obj_id_2 = obj.id " +
            " AND rel.type_id = " + Constants.CONTAINS_RELATIONSHIP +
            " AND rel.obj_id_1 = ? " +//program id
            " AND obj.type_id =  " + Constants.DATA_ITEM +
            " AND obj.id not in (select obj_id_1 from cw_relationships)" +
            " AND obj.owner like ?";
    private static final String CONDITION_NOT_USED_QUERY_WITHOUT_ID =
            " SELECT obj.id as objId, obj.owner " +
            " FROM " +
            " cw_objects as obj, cw_relationships as rel " +
            " WHERE obj.id in (select cw_attributes.id from cw_attributes where cw_attributes.attr_type_id = " +
            Constants.LEVEL +
            " AND cw_attributes.attr_data_value = 88) " +
            " AND rel.obj_id_2 = obj.id " +
            " AND rel.type_id = " + Constants.CONTAINS_RELATIONSHIP +
            " AND obj.type_id =  " + Constants.DATA_ITEM +
            " AND obj.id not in (select obj_id_1 from cw_relationships)" +
            " AND obj.owner like ?";

    public List<Violation> getNbConditionsNotUsed(int id, String name) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<Violation> retour = new ArrayList<Violation>();

        try {
            conn = this.getConnection();
            if (conn != null) {
                if (id != 0) {
                    ps = conn.prepareStatement(CONDITION_NOT_USED_QUERY);
                    ps.setInt(1, id);
                    ps.setString(2, name);
                } else {
                    ps = conn.prepareStatement(CONDITION_NOT_USED_QUERY_WITHOUT_ID);
                    ps.setString(1, name);
                }
                rs = ps.executeQuery();
                if (rs != null) {
                    while (rs.next()) {
                        String idObj = rs.getString("objId");
                        String line = this.getFileLine(idObj);
                        boolean found = false;
                        for (Violation v : retour) {
                            if (v.getLine().equals(line)) {
                                found = true;
                                break;
                            }
                        }
                        if (!found) {
                            retour.add(new Violation(line, rs.getString("owner")));
                        }
                    }
                }
            }
        } catch (SQLException exc) {
            logger.error("SQLException " + exc.getMessage());
        } finally {
            JdbcDAOUtils.closeResultSet(rs);
            JdbcDAOUtils.closeStatement(ps);
        }
        return retour;
    }
    private static final String IDENTIFIER_TOO_LONG_QUERY =
            "SELECT obj.id as objId, obj.name, obj.owner " +
            " FROM cw_objects obj, cw_relationships rel " +
            " WHERE obj.type_id = " + Constants.DATA_ITEM +
            " AND obj.id NOT IN " +
            "	(SELECT id " +
            "      FROM cw_attributes " +
            "      WHERE attr_type_id = " + Constants.LITERAL +
            " AND attr_data_value = 'yes')" +
            " AND rel.obj_id_1 = ? and rel.obj_id_2 = obj.id and rel.type_id =  " +
            Constants.CONTAINS_RELATIONSHIP +
            " AND obj.owner like ? " +
            " AND len(obj.name) > 30";

    public List<Violation> getIdentiferTooLong(int id, String name) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<Violation> retour = new ArrayList<Violation>();

        try {
            conn = this.getConnection();
            if (conn != null) {
                ps = conn.prepareStatement(IDENTIFIER_TOO_LONG_QUERY);
                ps.setInt(1, id);
                ps.setString(2, name + "%");
                rs = ps.executeQuery();
                if (rs != null) {
                    while (rs.next()) {
                        String objId = rs.getString("objId");
                        String line = this.getFileLine(objId);
                        retour.add(new Violation(line, rs.getString("owner")));
                    }
                }
            }
        } catch (SQLException e) {
            logger.error("Error getting too long identifiers", e);
        } finally {
            JdbcDAOUtils.closeResultSet(rs);
            JdbcDAOUtils.closeStatement(ps);
        }
        return retour;
    }
    private final String RETRIEVE_ALL_INCLUDES_FOR_COLLECTION =
            "SELECT DISTINCT attr.attr_data_value as cobolInclude" +
            " FROM cw_attributes attr, cw_objects obj, cw_relationships rel " +
            " WHERE attr.attr_type_id = 110 " +
            " AND obj.id = attr.id AND attr_data_value != '' " +
            " AND rel.obj_id_2 = obj.id and rel.obj_id_1 = " +
            " (SELECT id from cw_objects " +
            "   WHERE type_id = 6 and name = ?)";

    public List<String> retrieveAllIncludesForCollection(String collectionName) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<String> includes = new ArrayList<String>();

        try {
            conn = this.getConnection();
            if (conn != null) {
                ps = conn.prepareStatement(RETRIEVE_ALL_INCLUDES_FOR_COLLECTION);
                ps.setString(1, collectionName);
                rs = ps.executeQuery();
                if (rs != null) {
                    while (rs.next()) {
                        includes.add(rs.getString("cobolInclude"));
                    }
                }
            }
        } catch (SQLException e) {
            logger.error("Error retrieving all includes for collection " +
                    collectionName, e);
        } finally {
            JdbcDAOUtils.closeResultSet(rs);
            JdbcDAOUtils.closeStatement(ps);
        }
        return includes;
    }
    private final String RETRIEVE_ALL_SOURCES_FOR_COLLECTION =
            "SELECT DISTINCT attr.attr_data_value as cobolInclude" +
            " FROM cw_attributes attr, cw_objects obj, cw_relationships rel " +
            " WHERE attr.attr_type_id = 107 " +
            " AND obj.id = attr.id AND attr_data_value != '' " +
            " AND rel.obj_id_2 = obj.id and rel.obj_id_1 = " +
            " (SELECT id from cw_objects " +
            "   WHERE type_id = 6 and name = ?)";

    public List<String> retrieveAllSourcesForCollection(String collectionName) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<String> sources = new ArrayList<String>();

        try {
            conn = this.getConnection();
            if (conn != null) {
                ps = conn.prepareStatement(RETRIEVE_ALL_SOURCES_FOR_COLLECTION);
                ps.setString(1, collectionName);
                rs = ps.executeQuery();
                if (rs != null) {
                    while (rs.next()) {
                        sources.add(rs.getString("cobolInclude"));
                    }
                }
            }
        } catch (SQLException e) {
            logger.error("Error retrieving all sources for collection " +
                    collectionName, e);
        } finally {
            JdbcDAOUtils.closeResultSet(rs);
            JdbcDAOUtils.closeStatement(ps);
        }
        return sources;
    }
    private final String RETRIEVE_ALL_PROGRAM_MEMBER_NAMES_FOR_COLLECTION =
            "SELECT attr.attr_data_value as memberName" +
            " FROM cw_attributes attr, cw_objects obj" +
            " WHERE attr.attr_type_id = 128" +
            " AND obj.id = attr.id" +
            " AND attr_data_value != ''" +
            " AND obj.type_id = 6" +
            " AND obj.name = ?";

    public List<String> retrieveAllProgramMemberNames(String collectionName) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<String> filters = new ArrayList<String>();

        try {
            conn = this.getConnection();
            if (conn != null) {
                ps = conn.prepareStatement(RETRIEVE_ALL_PROGRAM_MEMBER_NAMES_FOR_COLLECTION);
                ps.setString(1, collectionName);
                rs = ps.executeQuery();
                if (rs != null) {
                    while (rs.next()) {
                        String memberName = rs.getString("memberName");
                        if (memberName != null && memberName.length() > 0) {
                            filters.add(memberName);
                        }
                    }
                }
            }
        } catch (SQLException e) {
            logger.error("Error retrieving all program member names", e);
        } finally {
            JdbcDAOUtils.closeResultSet(rs);
            JdbcDAOUtils.closeStatement(ps);
        }
        return filters;
    }
    private static final String PROGRAM_DEPENDENCIES_QUERY =
            "SELECT o.id, o.name" + " FROM cw_relationships r, cw_objects o" +
            " where r.type_id IN (" + Constants.USES_RELATIONSHIP + ',' +
            Constants.CONTAINS_RELATIONSHIP + ')' +
            " and ((r.obj_id_1 = ? and r.obj_id_2 = o.id)" +
            " or (r.obj_id_2 = ? and r.obj_id_1 = o.id))" +
            " and o.type_id in (" + Constants.PROGRAM_TYPE_ID + ',' +
            Constants.INCLUDE + ")";

    public List<String> getProgramDependencies(int id) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<String> result = new ArrayList<String>();

        try {
            conn = this.getConnection();
            if (conn != null) {
                ps = conn.prepareStatement(PROGRAM_DEPENDENCIES_QUERY);
                ps.setInt(1, id);
                ps.setInt(2, id);
                rs = ps.executeQuery();
                if (rs != null) {
                    while (rs.next()) {
                        String objId = rs.getString("id");
                        String objName = rs.getString("name");
                        result.add(objId);
                    }
                }
            }
        } catch (SQLException e) {
            logger.error("Error getting program dependencies", e);
        } finally {
            JdbcDAOUtils.closeResultSet(rs);
            JdbcDAOUtils.closeStatement(ps);
        }
        return result;
    }

    public void getMetricsFromXML(String xmlPath, CobolSource cs, List<Copy> copies) {
        Connection conn = this.getConnection();
        RetrieveExternalizedMetrics rem = new RetrieveExternalizedMetrics(xmlPath, conn);
        rem.retrieveMetrics(cs, copies);
    }
}
