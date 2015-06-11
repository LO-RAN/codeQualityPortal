/*
 * Class.java
 *
 * Created on 23 janvier 2004, 12:22
 */
package com.compuware.caqs.dao.dbms;

// Imports for DBMS transactions.
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.compuware.caqs.constants.Constants;
import com.compuware.caqs.dao.interfaces.CallDao;
import com.compuware.caqs.dao.interfaces.ElementDao;
import com.compuware.caqs.domain.dataschemas.ElementBean;
import com.compuware.caqs.domain.dataschemas.callsto.CallBean;
import com.compuware.caqs.domain.dataschemas.callsto.CallGraphNode;
import com.compuware.caqs.exception.DataAccessException;
import com.compuware.caqs.util.IDCreator;
import com.compuware.toolbox.dbms.JdbcDAOUtils;
import com.compuware.toolbox.util.logging.LoggerManager;

/**
 *
 * @author  cwfr-fdubois
 */
public class CallDbmsDao implements CallDao {

    private static org.apache.log4j.Logger mLog = LoggerManager.getLogger(Constants.LOG4J_DBMS_LOGGER_KEY);
    private static CallDao singleton = new CallDbmsDao();

    public static CallDao getInstance() {
        return CallDbmsDao.singleton;
    }

    /** Creates a new instance of Class */
    private CallDbmsDao() {
    }
    private static final String RETRIEVE_ALL_CALLS_QUERY =
            "Select distinct e1.ID_ELT, e1.LIB_ELT, e1.DESC_ELT, e1.ID_TELT, e2.ID_ELT, e2.LIB_ELT, e2.DESC_ELT, e2.ID_TELT"
            + " From LINK_ELT_BLINE, ELEMENT e1, ELEMENT e2"
            + " Where ELT_FROM_ID = e1.ID_ELT"
            + " And ELT_TO_ID = e2.ID_ELT"
            + " And ID_BLINE = ? And TYPE = 'CALLSTO'";

    public Collection<CallGraphNode> retrieveAllCalls(String idBline) throws DataAccessException {
        Map<String, CallGraphNode> mainGraph = new HashMap<String, CallGraphNode>();
        Connection connection = JdbcDAOUtils.getConnection(this, Constants.CAQS_DATASOURCE_KEY);
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = connection.prepareStatement(RETRIEVE_ALL_CALLS_QUERY);
            CallGraphNode currentNodeFrom = null;
            CallGraphNode currentNodeTo = null;
            ElementBean currentElementFrom = null;
            ElementBean currentElementTo = null;
            pstmt.setString(1, idBline);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                currentElementFrom = new ElementBean();
                currentElementFrom.setId(rs.getString(1));
                currentElementFrom.setLib(rs.getString(2));
                currentElementFrom.setDesc(rs.getString(3));
                currentElementFrom.setTypeElt(rs.getString(4));

                currentNodeFrom = mainGraph.get(currentElementFrom.getId());
                if (currentNodeFrom == null) {
                    currentNodeFrom = new CallGraphNode();
                    currentNodeFrom.setElement(currentElementFrom);
                    mainGraph.put(currentElementFrom.getId(), currentNodeFrom);
                }

                currentElementTo = new ElementBean();
                currentElementTo.setId(rs.getString(5));
                currentElementTo.setLib(rs.getString(6));
                currentElementTo.setDesc(rs.getString(7));
                currentElementTo.setTypeElt(rs.getString(8));

                currentNodeTo = mainGraph.get(currentElementTo.getId());
                if (currentNodeTo == null) {
                    currentNodeTo = new CallGraphNode();
                    currentNodeTo.setElement(currentElementTo);
                    mainGraph.put(currentElementTo.getId(), currentNodeTo);
                }

                currentNodeTo.addCallIn(currentNodeFrom);
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error retrieving calls IN for baseline="
                    + idBline, e);
        } finally {
            JdbcDAOUtils.closeResultSet(rs);
            JdbcDAOUtils.closePrepareStatement(pstmt);
            JdbcDAOUtils.closeConnection(connection);
        }
        return mainGraph.values();
    }
    private static final String RETRIEVE_CALLS_IN_QUERY = "Select distinct ID_ELT, LIB_ELT, DESC_ELT, ID_TELT From LINK_ELT_BLINE, ELEMENT Where ELT_TO_ID = ? And ELT_FROM_ID = ID_ELT And ID_BLINE = ? And TYPE = 'CALLSTO'";
    private static final String RETRIEVE_CALLS_OUT_QUERY = "Select distinct ID_ELT, LIB_ELT, DESC_ELT, ID_TELT From LINK_ELT_BLINE, ELEMENT Where ELT_FROM_ID = ? And ELT_TO_ID = ID_ELT And ID_BLINE = ? And TYPE = 'CALLSTO'";
    /* Appels d'elements d'autres classes vers les fils de l'element selectionne. */
    private static final String RETRIEVE_CALLS_IN_CLS_QUERY =
            "Select distinct id_elt, lib_elt, desc_elt, id_telt, count(*)"
            + " From link_elt_bline, elt_links l1, elt_links l2, element"
            + " Where elt_to_id = l1.elt_fils"
            + " And l1.elt_pere = ?"
            + " And elt_from_id = l2.elt_fils"
            + " And id_elt=l2.elt_pere"
            + " And id_telt='CLS'"
            + " And id_elt <> l1.elt_pere"
            + " And id_bline = ?"
            + " Group By id_elt, lib_elt, desc_elt, id_telt";
    /* Appels des fils de l'element selectionne vers des elements d'autres classes. */
    private static final String RETRIEVE_CALLS_OUT_CLS_QUERY =
            "Select distinct id_elt, lib_elt, desc_elt, id_telt, count(*)"
            + " From link_elt_bline, elt_links l1, elt_links l2, element"
            + " Where elt_from_id = l1.elt_fils"
            + " And l1.elt_pere = ?"
            + " And elt_to_id = l2.elt_fils"
            + " And id_elt=l2.elt_pere"
            + " And id_telt='CLS'"
            + " And id_elt <> l1.elt_pere"
            + " And id_bline = ?"
            + " Group By id_elt, lib_elt, desc_elt, id_telt";

    /* (non-Javadoc)
     * @see com.compuware.caqs.dao.dbms.CallDao#retrieveCalls(java.lang.String, int, int)
     */
    public CallGraphNode retrieveCalls(String idElt, String idBline, int nbIn, int nbOut) throws DataAccessException {
        ElementDao elementDao = ElementDbmsDao.getInstance();
        ElementBean elt = elementDao.retrieveElementById(idElt);
        return retrieveCalls(elt, idBline, nbIn, nbOut);
    }

    /* (non-Javadoc)
     * @see com.compuware.caqs.dao.dbms.CallDao#retrieveCalls(ElementBean, int, int)
     */
    public CallGraphNode retrieveCalls(ElementBean elt, String idBline, int nbIn, int nbOut) throws DataAccessException {
        CallGraphNode result = new CallGraphNode();
        result.setElement(elt);
        retrieveCallsIn(result, idBline, nbIn, getQueryIn(elt.getTypeElt()), new HashMap());
        retrieveCallsOut(result, idBline, nbOut, getQueryOut(elt.getTypeElt()), new HashMap());
        return result;
    }

    private String getQueryIn(String typeElt) {
        String result = RETRIEVE_CALLS_IN_QUERY;
        /*
        if (typeElt != null && typeElt.equals(ElementType.CLS)) {
        result = RETRIEVE_CALLS_IN_CLS_QUERY;
        }
         */
        return result;
    }

    private String getQueryOut(String typeElt) {
        String result = RETRIEVE_CALLS_OUT_QUERY;
        /*
        if (typeElt != null && typeElt.equals(ElementType.CLS)) {
        result = RETRIEVE_CALLS_OUT_CLS_QUERY;
        }
         */
        return result;
    }

    /* (non-Javadoc)
     * @see com.compuware.caqs.dao.dbms.CallDao#retrieveCallsIn(CallGraph, ElementBean, int)
     */
    public CallGraphNode retrieveCallsIn(CallGraphNode mainGraph, String idBline, int nbIn, String query, Map<String, String> alreadyDone) throws DataAccessException {
        CallGraphNode result = mainGraph;
        if (nbIn > 0) {
            Connection connection = JdbcDAOUtils.getConnection(this, Constants.CAQS_DATASOURCE_KEY);
            PreparedStatement pstmt = null;
            ResultSet rs = null;
            try {
                pstmt = connection.prepareStatement(query);
                CallGraphNode currentGraph = null;
                ElementBean currentElement = null;
                pstmt.setString(1, mainGraph.getElement().getId());
                pstmt.setString(2, idBline);
                rs = pstmt.executeQuery();
                while (rs.next()) {
                    currentGraph = new CallGraphNode();
                    currentElement = new ElementBean();
                    currentElement.setId(rs.getString("ID_ELT"));
                    currentElement.setLib(rs.getString("LIB_ELT"));
                    currentElement.setDesc(rs.getString("DESC_ELT"));
                    currentElement.setTypeElt(rs.getString("ID_TELT"));
                    currentGraph.setElement(currentElement);
                    currentGraph.addCallOut(result);
                    result.addCallIn(currentGraph);
                }
            } catch (SQLException e) {
                throw new DataAccessException("Error retrieving calls IN for baseline="
                        + idBline + ", element="
                        + mainGraph.getElement().getId(), e);
            } finally {
                JdbcDAOUtils.closeResultSet(rs);
                JdbcDAOUtils.closePrepareStatement(pstmt);
                JdbcDAOUtils.closeConnection(connection);
                alreadyDone.put(mainGraph.getElement().getId(), mainGraph.getElement().getId());
            }
            retrieveCallsIn(result.getCallIn(), idBline, nbIn - 1, query, alreadyDone);
        }
        return result;
    }

    /* (non-Javadoc)
     * @see com.compuware.caqs.dao.dbms.CallDao#retrieveCallsIn(List, ElementBean, int)
     */
    public void retrieveCallsIn(List graphList, String idBline, int nbIn, String query, Map<String, String> alreadyDone) throws DataAccessException {
        if (graphList != null && !graphList.isEmpty() && nbIn > 0) {
            CallGraphNode current = null;
            Iterator i = graphList.iterator();
            while (i.hasNext()) {
                current = (CallGraphNode) i.next();
                if (!alreadyDone.containsKey(current.getElement().getId())) {
                    retrieveCallsIn(current, idBline, nbIn, query, alreadyDone);
                }
            }
        }
    }

    /* (non-Javadoc)
     * @see com.compuware.caqs.dao.dbms.CallDao#retrieveCallsOut(CallGraph, ElementBean, int)
     */
    public CallGraphNode retrieveCallsOut(CallGraphNode mainGraph, String idBline, int nbOut, String query, Map<String, String> alreadyDone) throws DataAccessException {
        CallGraphNode result = mainGraph;
        if (nbOut > 0) {
            Connection connection = JdbcDAOUtils.getConnection(this, Constants.CAQS_DATASOURCE_KEY);
            PreparedStatement pstmt = null;
            ResultSet rs = null;
            try {
                pstmt = connection.prepareStatement(query);
                CallGraphNode currentGraph = null;
                ElementBean currentElement = null;
                pstmt.setString(1, mainGraph.getElement().getId());
                pstmt.setString(2, idBline);
                rs = pstmt.executeQuery();
                while (rs.next()) {
                    currentGraph = new CallGraphNode();
                    currentElement = new ElementBean();
                    currentElement.setId(rs.getString("ID_ELT"));
                    currentElement.setLib(rs.getString("LIB_ELT"));
                    currentElement.setDesc(rs.getString("DESC_ELT"));
                    currentElement.setTypeElt(rs.getString("ID_TELT"));
                    currentGraph.setElement(currentElement);
                    currentGraph.addCallIn(result);
                    result.addCallOut(currentGraph);
                }
            } catch (SQLException e) {
                throw new DataAccessException("Error retrieving calls OUT for baseline="
                        + idBline + ", element="
                        + mainGraph.getElement().getId(), e);
            } finally {
                JdbcDAOUtils.closeResultSet(rs);
                JdbcDAOUtils.closePrepareStatement(pstmt);
                JdbcDAOUtils.closeConnection(connection);
                alreadyDone.put(mainGraph.getElement().getId(), mainGraph.getElement().getId());
            }
            retrieveCallsOut(result.getCallOut(), idBline, nbOut - 1, query, alreadyDone);
        }
        return result;
    }

    /* (non-Javadoc)
     * @see com.compuware.caqs.dao.dbms.CallDao#retrieveCallsIn(List, ElementBean, int)
     */
    public void retrieveCallsOut(List graphList, String idBline, int nbOut, String query, Map<String, String> alreadyDone) throws DataAccessException {
        if (graphList != null && !graphList.isEmpty() && nbOut > 0) {
            CallGraphNode current = null;
            Iterator i = graphList.iterator();
            while (i.hasNext()) {
                current = (CallGraphNode) i.next();
                if (!alreadyDone.containsKey(current.getElement().getId())) {
                    retrieveCallsOut(current, idBline, nbOut, query, alreadyDone);
                }
            }
        }
    }
    /* Select les liens sortants de l'element donne a partir des liens des fils. */
    private static final String RETRIEVE_PARENT_CALLS_OUT =
            " Select distinct l1.elt_pere, id_elt"
            + " From link_elt_bline, elt_links l1, elt_links l2, element"
            + " Where elt_from_id = l1.elt_fils"
            + " And l1.elt_pere = ?"
            + " And elt_to_id = l2.elt_fils"
            + " And id_elt=l2.elt_pere"
            + " And id_elt <> l1.elt_pere"
            + " And id_bline = ?";

    private List retrieveParentCallsOut(String idElt, String idBline) {
        List result = new ArrayList();
        Connection connection = JdbcDAOUtils.getConnection(this, Constants.CAQS_DATASOURCE_KEY);
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = connection.prepareStatement(RETRIEVE_PARENT_CALLS_OUT);
            CallBean currentCall = null;
            pstmt.setString(1, idElt);
            pstmt.setString(2, idBline);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                currentCall = new CallBean();
                currentCall.setIdFrom(rs.getString(1));
                currentCall.setIdTo(rs.getString(2));
                result.add(currentCall);
            }
        } catch (SQLException e) {
            mLog.error(e);
        } finally {
            JdbcDAOUtils.closeResultSet(rs);
            JdbcDAOUtils.closePrepareStatement(pstmt);
            JdbcDAOUtils.closeConnection(connection);
        }
        return result;
    }
    /* Select les liens sortants de l'element donne a partir des liens des fils. */
    private static final String INSERT_PARENT_CALLS_OUT =
            "Insert into link_elt_bline (elt_from_id, elt_to_id, type, id_bline, link_id)"
            + " VALUES (?, ?, 'CALLSTO', ?, ?)";
    private static final String SELECT_PARENT_CALLS_OUT =
            "SELECT count(link_id) FROM link_elt_bline "
            + " WHERE ELT_FROM_ID = ? AND ELT_TO_ID = ? AND ID_BLINE = ? AND type = 'CALLSTO'";

    public void createParentLinks(String idElt, String idBline) {
        List calls = retrieveParentCallsOut(idElt, idBline);
        if (calls != null && calls.size() > 0) {
            Connection connection = JdbcDAOUtils.getConnection(this, Constants.CAQS_DATASOURCE_KEY);
            PreparedStatement selectstmt = null;
            PreparedStatement insertstmt = null;
            ResultSet rs = null;
            try {
                selectstmt = connection.prepareStatement(SELECT_PARENT_CALLS_OUT);
                insertstmt = connection.prepareStatement(INSERT_PARENT_CALLS_OUT);
                Iterator i = calls.iterator();
                CallBean currentCall = null;
                while (i.hasNext()) {
                    currentCall = (CallBean) i.next();
                    selectstmt.setString(1, currentCall.getIdFrom());
                    selectstmt.setString(2, currentCall.getIdTo());
                    selectstmt.setString(3, idBline);
                    rs = selectstmt.executeQuery();
                    if (rs.next()) {
                        int nb = rs.getInt(1);
                        if (nb == 0) {
                            // Insertion des donnees dans la base.
                            // Initialisation des donnees.
                            String linkId = IDCreator.getID();
                            insertstmt.setString(1, currentCall.getIdFrom());
                            insertstmt.setString(2, currentCall.getIdTo());
                            insertstmt.setString(3, idBline);
                            insertstmt.setString(4, linkId);
                            // Execution de la requete.
                            insertstmt.addBatch();
                        }
                    }
                    JdbcDAOUtils.closeResultSet(rs);
                }
                insertstmt.executeBatch();
            } catch (SQLException e) {
                mLog.error("Error creating parent links :", e);
            } finally {
                JdbcDAOUtils.closePrepareStatement(insertstmt);
                JdbcDAOUtils.closeConnection(connection);
            }
        }
    }
}
