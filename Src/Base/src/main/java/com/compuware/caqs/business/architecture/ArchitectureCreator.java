/*
 * ArchitectureCreator.java
 *
 * Created on 6 d�cembre 2002, 11:50
 */
package com.compuware.caqs.business.architecture;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.apache.log4j.Logger;

import com.compuware.caqs.constants.Constants;
import com.compuware.caqs.dao.factory.DaoFactory;
import com.compuware.caqs.dao.interfaces.MetriqueDao;
import com.compuware.caqs.exception.CaqsException;
import com.compuware.caqs.exception.DataAccessException;
import com.compuware.caqs.domain.architecture.serializeddata.ArchitectureModel;
import com.compuware.caqs.domain.architecture.serializeddata.Edge;
import com.compuware.caqs.domain.architecture.serializeddata.EdgesList;
import com.compuware.caqs.domain.architecture.serializeddata.Element;
import com.compuware.caqs.domain.architecture.serializeddata.ElementsCouple;
import com.compuware.caqs.domain.architecture.serializeddata.ElementsList;
import com.compuware.caqs.domain.architecture.serializeddata.Node;
import com.compuware.caqs.domain.architecture.serializeddata.NodeArchitectureModule;
import com.compuware.caqs.domain.architecture.serializeddata.NodeDB;
import com.compuware.caqs.domain.architecture.serializeddata.NodeList;
import com.compuware.caqs.domain.architecture.serializeddata.NodeUseCase;
import com.compuware.caqs.util.IDCreator;
import com.compuware.toolbox.dbms.JdbcDAOUtils;

public class ArchitectureCreator {
	// declaration du logger
	static protected Logger logger = com.compuware.toolbox.util.logging.LoggerManager.getLogger("Architecture");

	protected String mBaselineId;
	protected String mProjectId;

	protected Timestamp mMajBaseline;
	protected Timestamp mInsBaseline;

	protected Map<String, Element> mElementsHash = new HashMap<String, Element>();

    /** Creates a new instance of ArchitectureCreator
     * @param baselineId
     * @param projectId
     */
	public ArchitectureCreator(String baselineId, String projectId) {
		this.mBaselineId = baselineId;
		this.mProjectId = projectId;
		this.getBaselineDetails();
	}

	private Map elementLinks = null;

	public void getFromDB(String test) {
		elementLinks = new HashMap();
		if (test != null) {
			ArchitectureCreator.logger.info("Start loading FAKE model : \n");
			this.createFake();
			ArchitectureCreator.logger.info("end loading FAKE model : \n" + ArchitectureModel.getInstance());
		} else {
			ArchitectureCreator.logger.info("Start loading model : \n");
			// recalc the real links for this base line
			// NOTE : should be done for all base line at the end of
			// insertIntoDb
			// Recalc of marks TOO...
			ArchitectureModel architectureModel = ArchitectureModel.getCleanInstance();
			ElementsList unAssignedElements = this.getUnassignedElements();
			ArchitectureModel.getInstance().setUnassignedElementsList(unAssignedElements);
			this.getArchitecturePackages();
			// create a hash of element id,elt
			this.createElementHash();
			this.createElementTree();
			this.getArchitectureLinks();
			this.getRealLinks();
			this.getCalls();
			// this.updateMetrics();
			ArchitectureCreator.logger.info("end loading model : \n" + ArchitectureModel.getInstance());
		}
	}

	public void insertIntoDB() throws CaqsException {
		// ArchitectureModel architectureModel =
		// ArchitectureModel.getInstance();
		ArchitectureCreator.logger.info("start saving model : \n" + ArchitectureModel.getInstance());
		List<String> deleteNodesQueries = this.insertNodes();
		this.nullifyRealLinks(ArchitectureModel.getInstance().getElementsCouples());
		this.insertEdges();
		this.deleteNodes(deleteNodesQueries);
		this.updateMetrics();
		ArchitectureCreator.logger.info("end saving model : \n" + ArchitectureModel.getInstance());
	}

	private void createFake() {
		// creates unassigned elements
		ElementsList unAssignedElements = new ElementsList();
		unAssignedElements.add(new Element("unassignedElement.method()", "el1"));

		// creates nodes
		NodeList nodes = new NodeList();

		// node 1
		NodeArchitectureModule n1 = new NodeArchitectureModule("Node", 100, 100, 0, 100, 100);
		ElementsList assignedElements = new ElementsList();
		assignedElements.add(new Element("assignedElement1.method()", "el2"));
		n1.setElements(assignedElements);
		n1.setId("node1");
		nodes.addNode(n1);

		// node 2
		NodeArchitectureModule n2 = new NodeArchitectureModule("Node2", 600, 100, 0, 100, 100);
		ElementsList assignedElements2 = new ElementsList();
		assignedElements2.add(new Element("assignedElement2.method()", "el3"));
		n2.setElements(assignedElements2);
		n2.setId("node2");
		nodes.addNode(n2);

		// creates the edges
		EdgesList edges = new EdgesList();

		// creates the architecture Model
		ArchitectureModel.getInstance().setEdgesList(edges);
		ArchitectureModel.getInstance().setNodeList(nodes);
		ArchitectureModel.getInstance().setUnassignedElementsList(unAssignedElements);
		List<ElementsCouple> couples = new ArrayList<ElementsCouple>();
		couples.add(new ElementsCouple("FAKEID", new Element("fromDesc", "el1"), new Element("toDesc", "el2")));
		// this.m_architectureModel.addEdge("id_link","node1","node2",Color.pink,true,couples);
		ArchitectureModel.getInstance().addEdge(n2, n1, Edge.ARCHI_LINK, new ArrayList<ElementsCouple>());
	}

	private void createElementHash() {
		Enumeration eltEnum = ArchitectureModel.getInstance().getAssignedElementsList().elements();
		while (eltEnum.hasMoreElements()) {
			Element curElt = (Element) eltEnum.nextElement();
			this.mElementsHash.put(curElt.getId(), curElt);
		}
		Enumeration eltEnum2 = ArchitectureModel.getInstance().getUnassignedElementsList().elements();
		while (eltEnum2.hasMoreElements()) {
			Element curElt = (Element) eltEnum2.nextElement();
			this.mElementsHash.put(curElt.getId(), curElt);
		}
	}

	private void createElementTree() {
		Enumeration eltEnum = ArchitectureModel.getInstance().getAssignedElementsList().elements();
		while (eltEnum.hasMoreElements()) {
			Element curElt = (Element) eltEnum.nextElement();
			String idParent = (String) this.elementLinks.get(curElt.getId());
			if (idParent != null && idParent.length() > 0) {
				Element parent = this.mElementsHash.get(idParent);
				if (parent != null) {
					curElt.addParent(parent);
					parent.addChild(curElt);
				}
			}
		}
		Enumeration eltEnum2 = ArchitectureModel.getInstance().getUnassignedElementsList().elements();
		while (eltEnum2.hasMoreElements()) {
			Element curElt = (Element) eltEnum2.nextElement();
			String idParent = (String) this.elementLinks.get(curElt.getId());
			if (idParent != null && idParent.length() > 0) {
				Element parent = this.mElementsHash.get(idParent);
				if (parent != null) {
					curElt.addParent(parent);
					parent.addChild(curElt);
				}
			}
		}
	}

	/**
	 * Obtaints all
	 */
	private void getCalls() {

		if (this.mBaselineId != null) {
			// if the baseline id isn't null get the real links
			Connection conn = JdbcDAOUtils.getConnection(this, Constants.CAQS_DATASOURCE_KEY);
			PreparedStatement sta = null;
			ResultSet rs = null;
			try {
				// Select the real links from database
				sta = conn.prepareStatement("SELECT " + " leb.LINK_ID, leb.ELT_FROM_ID, elt1.desc_elt, leb.ELT_TO_ID, elt2.desc_elt" + " FROM LINK_ELT_BLINE leb, Element elt1, Element elt2" + " WHERE elt1.id_elt=leb.elt_from_id"
						+ " AND elt2.id_elt=leb.elt_to_id" + " AND leb.ID_BLINE=?");

				// set request parameter
				sta.setString(1, this.mBaselineId);
				ArchitectureCreator.logger.debug(sta);
				// execute the query
				rs = sta.executeQuery();

				while (rs.next()) {
					// for each result
					ArchitectureCreator.logger.debug("adding an element couple.");
					String coupleId = rs.getString(1);
					String eltFromID = rs.getString(2);
					String eltToID = rs.getString(4);
					Element eltFrom = this.mElementsHash.get(eltFromID);
					Element eltTo = this.mElementsHash.get(eltToID);
					ArchitectureModel.getInstance().addElementsCouple(
                            new ElementsCouple(coupleId, eltFrom, eltTo));
				}
			} catch (Exception e) {
				ArchitectureCreator.logger.error("Error getting calls", e);
			} finally {
				JdbcDAOUtils.closeResultSet(rs);
				JdbcDAOUtils.closePrepareStatement(sta);
				JdbcDAOUtils.closeConnection(conn);
			}

		}
	}

	private void getRealLinks() {

		if (this.mBaselineId != null) {
			// if the baseline id isn't null get the real links
			Connection conn = JdbcDAOUtils.getConnection(this, Constants.CAQS_DATASOURCE_KEY);
			PreparedStatement sta = null;
			ResultSet rs = null;
			try {
				// Select the real links from database
				sta = conn.prepareStatement("SELECT " + " leb.LINK_ID, " + "leb.ELT_FROM_ID, elt1.desc_elt, leb.ELT_TO_ID, elt2.desc_elt, lr.ID_LINK, lr.ID_FROM, lr.ID_TO, lr.STATE"
						+ " FROM LINK_ELT_BLINE leb, LINK_REAL lr, Element elt1, Element elt2" + " WHERE leb.REAL_LINK_ID=lr.ID_LINK" + " AND elt1.id_elt=leb.elt_from_id" + " AND elt2.id_elt=leb.elt_to_id" + " AND lr.ID_PROJ=?"
						+ " AND lr.ID_BLINE=?" + " AND lr.STATE < 20" + " AND lr.ID_FROM<>lr.ID_TO" + " ORDER BY lr.ID_FROM, lr.ID_TO");

				// set request parameter
				sta.setString(1, this.mProjectId);
				sta.setString(2, this.mBaselineId);

				// execute the query
				rs = sta.executeQuery();

				// initialise variables
				String previousFromtoMacroStrutureID = null; // the request
				// get the real
				// link ordered
				// by ID_FROM,
				// ID_TO so
				// links from
				// one
				// marcostructure
				// to another
				// are in an
				// ordered list.
				List<ElementsCouple> couples = null; // get the couple
				// caller-callee

				while (rs.next()) {
					// for each result
					ArchitectureCreator.logger.debug("adding a real link");
					String coupleId = rs.getString(1);
					String eltFromID = rs.getString(2);
					// String eltFrom_desc = rs.getString(3);
					String eltToID = rs.getString(4);
					// String eltTo_desc = rs.getString(5);
					String realLinkId = rs.getString(6);
					String realLinkFromMacroStrutureId = rs.getString(7);
					String realLinkToMacroStrutureId = rs.getString(8);
					int realLinkState = rs.getInt(9);

					// if realLink_fromMacroStrutureId AND
					// realLink_toMacroStrutureId are the same : add elements
					// couple.
					if ((previousFromtoMacroStrutureID == null)
                            || (previousFromtoMacroStrutureID.compareTo(realLinkFromMacroStrutureId + realLinkToMacroStrutureId) != 0)) {
						previousFromtoMacroStrutureID = realLinkFromMacroStrutureId + realLinkToMacroStrutureId;
						couples = new ArrayList<ElementsCouple>();
						ArchitectureCreator.logger.debug("adding real edge from" + realLinkFromMacroStrutureId + " to " + realLinkToMacroStrutureId);
						ArchitectureModel.getInstance().addEdge(realLinkId, realLinkFromMacroStrutureId, realLinkToMacroStrutureId, realLinkState, couples);
					}
					// getCallsCouples
					Element eltFrom = (Element) this.mElementsHash.get(eltFromID);
					Element eltTo = (Element) this.mElementsHash.get(eltToID);

					couples.add(new ElementsCouple(coupleId, eltFrom, eltTo));

				}
			} catch (Exception e) {
				ArchitectureCreator.logger.error("Error getting real links", e);
			} finally {
				JdbcDAOUtils.closeResultSet(rs);
				JdbcDAOUtils.closePrepareStatement(sta);
				JdbcDAOUtils.closeConnection(conn);
			}

		}
	}

	private void getArchitectureLinks() {

		// get architecture links
		Connection conn = JdbcDAOUtils.getConnection(this, Constants.CAQS_DATASOURCE_KEY);

		PreparedStatement sta = null;
		ResultSet rs = null;

		try {
			sta = conn.prepareStatement("SELECT ID_LINK,ID_FROM,ID_TO,TYPE FROM ARCHI_LINK WHERE ID_PROJ=?");
			// set request parameter
			sta.setString(1, this.mProjectId);
			rs = sta.executeQuery();
			while (rs.next()) {
				String idLink = rs.getString(1);
				String idFrom = rs.getString(2);
				String idTo = rs.getString(3);
				int type = rs.getInt(4);
				ArchitectureCreator.logger.debug("adding a real edge from" + idFrom + " to " + idTo + " type " + type);
				ArchitectureModel.getInstance().addEdge(idLink, idFrom, idTo, type, new ArrayList());
			}
		} catch (Exception e) {
			ArchitectureCreator.logger.error("Error getting architecture links", e);
		} finally {
			JdbcDAOUtils.closeResultSet(rs);
			JdbcDAOUtils.closePrepareStatement(sta);
			JdbcDAOUtils.closeConnection(conn);
		}

	}

	private void getArchitecturePackages() {
		Connection conn = JdbcDAOUtils.getConnection(this, Constants.CAQS_DATASOURCE_KEY);

		PreparedStatement sta = null;
		ResultSet rs = null;

		NodeList nodes = new NodeList();
		Map<String, Node> nodeMap = new HashMap<String, Node>();
		try {
			// Start getting packages
			sta = conn.prepareStatement("SELECT ID_PACK,LIB_PACK,X,Y,ZORDER,WIDTH,HEIGHT,COEFF_PACK,TYPE,PARENT_PACKAGE FROM PACKAGE WHERE ID_PROJ=? order by ZORDER");
			// set request parameter
			sta.setString(1, this.mProjectId);
			rs = sta.executeQuery();
			while (rs.next()) {
				String id = rs.getString(1);
				String lbl = rs.getString(2);
				int xPack = rs.getInt(3);
				int yPack = rs.getInt(4);
				int zOrder = rs.getInt(5);
				int widthPack = rs.getInt(6);
				int heightPack = rs.getInt(7);
				int coeffPack = rs.getInt(8);
				int type = rs.getInt(9);
				String parentPackageId = rs.getString(10);
				Node n = null;
				switch (type) {
				case Node.NODE_ARCHI:
					n = new NodeArchitectureModule(lbl, xPack, yPack, zOrder, widthPack, heightPack);
					break;
				case Node.NODE_DB:
					n = new NodeDB(lbl, xPack, yPack, zOrder, widthPack, heightPack);
					break;
				case Node.NODE_USECASE:
					n = new NodeUseCase(lbl, xPack, yPack, zOrder, widthPack, heightPack);
					break;
				default:
					n = new NodeArchitectureModule(lbl, xPack, yPack, zOrder, widthPack, heightPack);
					break;
				}

				n.setWeight(coeffPack);
				n.setId(id);
				n.setElements(new ElementsList());
				nodes.addNode(n);
				nodeMap.put(id, n);
				n.setParentId(parentPackageId);
			}
			setContainedElements(nodeMap, conn);
		} catch (SQLException e) {
			ArchitectureCreator.logger.error("Error getting architecture packages", e);
		} finally {
			JdbcDAOUtils.closeResultSet(rs);
			JdbcDAOUtils.closePrepareStatement(sta);
			JdbcDAOUtils.closeConnection(conn);
		}

		// Now link the nodes with their parent package;
		for (int i = 0; i < nodes.size(); i++) {
			Node currNode = nodes.elementAt(i);
			if (currNode.getParentId() != null && currNode.getParentId().compareTo("") != 0) {
				// the node has a parent node
				Node parentNode = nodeMap.get(currNode.getParentId());
				if (null != parentNode) {
					parentNode.addChildObject(currNode);
				}
			}
		}
		//
		ArchitectureModel.getInstance().setNodeList(nodes);
		// end getting packages

	}

	private void setContainedElements(Map<String, Node> nodeMap, Connection conn) {

		PreparedStatement sta = null;
		ResultSet rs = null;
		String selectAssignedElemlents = "Select id_elt, desc_elt, elt_pere, id_pack" + " From Element, Elt_links where id_pack Is NOT NULL" + " And id_main_elt = '" + this.mProjectId + "'" + " And (dperemption is null or dperemption>?)"
				+ " And (dinst_elt<?)" + " And id_elt = elt_fils";
		try {
			sta = conn.prepareStatement(selectAssignedElemlents);
			sta.setTimestamp(1, this.mMajBaseline);
			sta.setTimestamp(2, this.mMajBaseline);

			rs = sta.executeQuery();
			ElementsList assignedElements = null;
			Node n = null;
			while (rs.next()) {
				String idPack = rs.getString("id_pack");
				n = nodeMap.get(idPack);
				assignedElements = n.getElements();
				String idElt = rs.getString(1);
				String lblElt = rs.getString(2);
				Element elt = new Element(lblElt, idElt);
				elementLinks.put(idElt, rs.getString(3));
				assignedElements.add(elt);
				elt.setAssignedToNode(n);
				ArchitectureModel.getInstance().getAssignedElementsList().add(elt);
			}
		} catch (SQLException e) {
			ArchitectureCreator.logger.error("Error getting architecture packages", e);
		} finally {
			JdbcDAOUtils.closeResultSet(rs);
			JdbcDAOUtils.closePrepareStatement(sta);
		}
	}

	private void getBaselineDetails() {
		Statement sta = null;
		ResultSet rs = null;
		String query = "SELECT DINST_BLINE, DMAJ_BLINE FROM BASELINE WHERE ID_BLINE ='" + this.mBaselineId + "'";
		Connection conn = JdbcDAOUtils.getConnection(this, Constants.CAQS_DATASOURCE_KEY);
		try {
			sta = conn.createStatement();
			rs = sta.executeQuery(query);
			while (rs.next()) {
				this.mInsBaseline = rs.getTimestamp(1);
				this.mMajBaseline = rs.getTimestamp(2);
				ArchitectureCreator.logger.debug("Baseline was updated on : " + this.mMajBaseline);
			}
		} catch (SQLException e) {
			ArchitectureCreator.logger.error("Error getting baseline details", e);
		} finally {
			JdbcDAOUtils.closeResultSet(rs);
			JdbcDAOUtils.closeStatement(sta);
			JdbcDAOUtils.closeConnection(conn);
		}
	}

	private ElementsList getUnassignedElements() {
		Connection conn = JdbcDAOUtils.getConnection(this, Constants.CAQS_DATASOURCE_KEY);
		// Start getting unassigned Elements
		ElementsList unAssignedElements = new ElementsList();
		String selectUnAssignedElementsQuery = "Select id_elt, desc_elt, elt_pere From Element, Elt_links" + " Where ID_PACK IS NULL" + " And id_main_elt = '" + this.mProjectId + "'" + " And (dperemption is null or dperemption>?)"
				+ " And (dinst_elt<?)" + " And id_elt=elt_fils";

		PreparedStatement sta = null;
		ResultSet rs = null;

		try {
			sta = conn.prepareStatement(selectUnAssignedElementsQuery);
			sta.setTimestamp(1, this.mMajBaseline);
			sta.setTimestamp(2, this.mMajBaseline);
			ArchitectureCreator.logger.debug("INST BaseLINE : " + this.mInsBaseline);
			ArchitectureCreator.logger.debug("MAJ BaseLine : " + this.mMajBaseline);
			ArchitectureCreator.logger.info("Selecting unassigned elements with query : " + selectUnAssignedElementsQuery);
			rs = sta.executeQuery();
			while (rs.next()) {
				String id = rs.getString(1);
				String lbl = rs.getString(2);
				unAssignedElements.add(new Element(lbl, id));
				elementLinks.put(id, rs.getString(3));
			}

		} catch (Exception e) {
			ArchitectureCreator.logger.error("Error getting unassigned elements", e);
		} finally {
			JdbcDAOUtils.closeResultSet(rs);
			JdbcDAOUtils.closePrepareStatement(sta);
			JdbcDAOUtils.closeConnection(conn);
		}
		// End getting unassigned Elements
		return unAssignedElements;
	}

	private void deleteNodes(List<String> deleteNodesQueries) {
		Connection conn = JdbcDAOUtils.getConnection(this, Constants.CAQS_DATASOURCE_KEY);
		// delete the nodes after deleting the edges for db integrity.
		Statement sta = null;
		try {
			sta = conn.createStatement();
			int size = deleteNodesQueries.size();
			for (int i = size - 1; i > -1; i--) {
				String query = (String) deleteNodesQueries.get(i);
				sta.addBatch(query);
			}
			sta.executeBatch();
		} catch (Exception e) {
			ArchitectureCreator.logger.error("Error deleting nodes", e);
		} finally {
			JdbcDAOUtils.closeStatement(sta);
			JdbcDAOUtils.closeConnection(conn);
		}
	}

	private List<String> insertNodes() {

		// Returns a list of SQL Queries (String)
		Connection conn = JdbcDAOUtils.getConnection(this, Constants.CAQS_DATASOURCE_KEY);
		NodeList nodes = ArchitectureModel.getInstance().getNodeList();
		List<String> deleteNodesQueries = new ArrayList<String>();

		this.doElementIdPackUpdate(null, ArchitectureModel.getInstance().getUnassignedElementsList(), conn);
		Vector<Node> sortedNodeVector = nodes.sortByLevel();
		for (int i = 0; i < sortedNodeVector.size(); i++) {
			Node currNode = (Node) sortedNodeVector.elementAt(i);
			String query = null;

			if (!currNode.isDeleted()) {
				if (currNode.getId() == null) {
					// it's a new node not deleted, not stored in db
					String idPack = IDCreator.getID();
					String libPack = currNode.getLbl();
					currNode.setId(idPack);
					int xPack = (int) currNode.getCenterX();
					int yPack = (int) currNode.getCenterY();
					int zOrder = (int) currNode.getZOrder();
					int widthPack = (int) currNode.getWidth();
					int heightPack = (int) currNode.getHeight();
					int type = (int) currNode.getType();
					String parentId = "";
					if (currNode.isInAGroup()) {
						parentId = currNode.getGroup().getId();
						query = "INSERT INTO PACKAGE (ID_PACK,LIB_PACK,ID_PROJ,X,Y,ZORDER,WIDTH,HEIGHT,COEFF_PACK,TYPE,PARENT_PACKAGE) VALUES('" + idPack + "','" + libPack + "','" + this.mProjectId + "'," + xPack + "," + yPack + ","
								+ zOrder + "," + widthPack + "," + heightPack + "," + currNode.getWeight() + "," + type + "," + parentId + ")";
					} else {
						query = "INSERT INTO PACKAGE (ID_PACK,LIB_PACK,ID_PROJ,X,Y,ZORDER,WIDTH,HEIGHT,COEFF_PACK,TYPE) VALUES('" + idPack + "','" + libPack + "','" + this.mProjectId + "'," + xPack + "," + yPack + "," + zOrder + ","
								+ widthPack + "," + heightPack + "," + currNode.getWeight() + "," + type + ")";
					}

				} else {
					// the node already exists in database
					String libPack = currNode.getLbl();
					int xPack = (int) currNode.getCenterX();
					int yPack = (int) currNode.getCenterY();
					int zOrder = currNode.getZOrder();
					int widthPack = (int) currNode.getWidth();
					int heightPack = (int) currNode.getHeight();

					String parentId = "";
					if (currNode.isInAGroup()) {
						parentId = currNode.getGroup().getId();
					}
					query = "UPDATE PACKAGE SET LIB_PACK='" + libPack + "',X=" + xPack + ",Y=" + yPack + ",ZORDER=" + zOrder + ",WIDTH=" + widthPack + ",HEIGHT=" + heightPack + ", COEFF_PACK=" + currNode.getWeight() + ",PARENT_PACKAGE='"
							+ parentId + "' WHERE ID_PACK='" + currNode.getId() + "'";

				}
				Statement sta = null;
				try {
					sta = conn.createStatement();
					sta.execute(query);
					// set elements assigned package
					this.doElementIdPackUpdate(currNode.getId(), currNode.getElements(), conn);

				} catch (Exception e) {
					ArchitectureCreator.logger.error("Error inserting package with query " + query, e);
				} finally {
					JdbcDAOUtils.closeStatement(sta);
				}
			} else {
				// the node is deleted...
				if (currNode.getId() != null) {
					// ...and stored.
					query = "DELETE FROM PACKAGE WHERE ID_PACK='" + currNode.getId() + "'";
					deleteNodesQueries.add(query);

					// remove elements assigned package
					// change :
					// this.doElementIdPackUpdate("NULL",currNode.getElements(),conn);
					deleteNodesQueries.add("UPDATE ELEMENT Set ID_PACK=NULL WHERE ID_PACK='" + currNode.getId() + "'");
				} else {
					// ...and not stored yet. So don't do anything.
					ArchitectureCreator.logger.info("not stored yet");
				}
			}
		}
		JdbcDAOUtils.closeConnection(conn);

		return deleteNodesQueries;
	}

	private void updateRealLinks(Edge realEdge, PreparedStatement updatePstmt) throws SQLException {
		if (realEdge != null && realEdge.getCouples() != null) {
			Iterator<ElementsCouple> enumCouples = realEdge.getCouples().iterator();
			ElementsCouple currCouple = null;
			while (enumCouples.hasNext()) {
				currCouple = enumCouples.next();
				updatePstmt.setString(1, realEdge.getId());
				updatePstmt.setString(2, currCouple.getId());
				updatePstmt.addBatch();
			}
		}
	}

	private void nullifyRealLinks(List<ElementsCouple> listCouples) {
		if (listCouples != null && listCouples.size() > 0) {
			Iterator<ElementsCouple> enumCouples = listCouples.iterator();
			ElementsCouple currCouple = null;
			Connection conn = JdbcDAOUtils.getConnection(this, Constants.CAQS_DATASOURCE_KEY);
			PreparedStatement updatePstmt = null;
			try {
				updatePstmt = conn.prepareStatement(LINK_REAL_UPDATE_QUERY);
				while (enumCouples.hasNext()) {
					currCouple = enumCouples.next();
					updatePstmt.setString(1, null);
					updatePstmt.setString(2, currCouple.getId());
					updatePstmt.addBatch();
				}
				updatePstmt.executeBatch();
				JdbcDAOUtils.commit(conn);
			} catch (SQLException e) {
				JdbcDAOUtils.rollbackConnection(conn);
				ArchitectureCreator.logger.error("Error during edge insert", e);
			} finally {
				JdbcDAOUtils.closeConnection(conn);
			}
		}
	}

	private static final String LINK_REAL_INSERT_QUERY = "INSERT INTO LINK_REAL (ID_LINK,ID_FROM,ID_TO,ID_PROJ,ID_BLINE,STATE) values(?,?,?,?,?,?)";
	private static final String LINK_REAL_DELETE_QUERY = "DELETE FROM LINK_REAL WHERE ID_LINK=?";

	private static final String LINK_REAL_UPDATE_QUERY = "UPDATE LINK_ELT_BLINE SET REAL_LINK_ID=? where LINK_ID=?";

	private void insertRealEdges(EdgesList edges, Connection connection) throws SQLException {
		Iterator<Edge> edgeIter = edges.iterator();
		Edge currentEdge = null;
		PreparedStatement insertPstmt = null;
		PreparedStatement deletePstmt = null;
		PreparedStatement updatePstmt = null;
		try {
			insertPstmt = connection.prepareStatement(LINK_REAL_INSERT_QUERY);
			deletePstmt = connection.prepareStatement(LINK_REAL_DELETE_QUERY);
			updatePstmt = connection.prepareStatement(LINK_REAL_UPDATE_QUERY);
			while (edgeIter.hasNext()) {
				currentEdge = edgeIter.next();
				if (currentEdge.isReal() && (currentEdge.getId() != null) || !currentEdge.isDeleted()) {
					ArchitectureCreator.logger.info("Real Edge deleted : " + currentEdge.isDeleted());
					ArchitectureCreator.logger.info("Real Edge id : " + currentEdge.getId());
					// do something in the database
					if (!currentEdge.isDeleted() && currentEdge.getId() == null) {
						String realEdgeId = IDCreator.getID();
						currentEdge.setId(realEdgeId);
						insertPstmt.setString(1, realEdgeId);
						insertPstmt.setString(2, currentEdge.getFrom().getId());
						insertPstmt.setString(3, currentEdge.getTo().getId());
						insertPstmt.setString(4, this.mProjectId);
						insertPstmt.setString(5, this.mBaselineId);
						insertPstmt.setInt(6, currentEdge.getLinkType());
						insertPstmt.addBatch();
					} else if (currentEdge.getId() != null && currentEdge.isDeleted()) {
						deletePstmt.setString(1, currentEdge.getId());
						deletePstmt.addBatch();
					}
					updateRealLinks(currentEdge, updatePstmt);
				}
			}
			insertPstmt.executeBatch();
			deletePstmt.executeBatch();
			updatePstmt.executeBatch();
		} catch (SQLException e) {
			ArchitectureCreator.logger.error("Error during real edge insert", e);
		} finally {
			JdbcDAOUtils.closeStatement(insertPstmt);
			JdbcDAOUtils.closeStatement(deletePstmt);
			JdbcDAOUtils.closeStatement(updatePstmt);
		}
	}

	private static final String ARCHI_LINK_INSERT_QUERY = "INSERT INTO ARCHI_LINK (ID_LINK,ID_FROM,ID_TO,ID_PROJ,TYPE) VALUES(?,?,?,?,?)";
	private static final String ARCHI_LINK_UPDATE_QUERY = "UPDATE ARCHI_LINK SET ID_FROM=?,ID_TO=?,ID_PROJ=?, TYPE=? WHERE ID_LINK=?";
	private static final String ARCHI_LINK_DELETE_QUERY = "DELETE FROM ARCHI_LINK WHERE ID_LINK=?";

	private void insertArchitectureEdges(EdgesList edges, Connection connection) throws SQLException {
		Iterator<Edge> edgeIter = edges.iterator();
		Edge currentEdge = null;
		PreparedStatement insertPstmt = null;
		PreparedStatement updatePstmt = null;
		PreparedStatement deletePstmt = null;
		try {
			insertPstmt = connection.prepareStatement(ARCHI_LINK_INSERT_QUERY);
			updatePstmt = connection.prepareStatement(ARCHI_LINK_UPDATE_QUERY);
			deletePstmt = connection.prepareStatement(ARCHI_LINK_DELETE_QUERY);
			while (edgeIter.hasNext()) {
				currentEdge = edgeIter.next();
				if (!currentEdge.isReal() && ((currentEdge.getId() != null) || !currentEdge.isDeleted())) {
					// do something in the database
					if (!currentEdge.isDeleted() && currentEdge.getId() == null) {

						// it's a new edge
						currentEdge.setId(IDCreator.getID());
						insertPstmt.setString(1, currentEdge.getId());
						insertPstmt.setString(2, currentEdge.getFromId());
						insertPstmt.setString(3, currentEdge.getToId());
						insertPstmt.setString(4, this.mProjectId);
						insertPstmt.setInt(5, currentEdge.getLinkType());
						insertPstmt.addBatch();
					} else if (currentEdge.getId() != null) {
						if (!currentEdge.isDeleted()) {
							updatePstmt.setString(1, currentEdge.getFromId());
							updatePstmt.setString(2, currentEdge.getToId());
							updatePstmt.setString(3, this.mProjectId);
							updatePstmt.setString(4, currentEdge.getId());
							updatePstmt.setInt(5, currentEdge.getLinkType());
							updatePstmt.addBatch();
						} else {
							deletePstmt.setString(1, currentEdge.getId());
							deletePstmt.addBatch();
						}
					}
				}

			}
			insertPstmt.executeBatch();
			updatePstmt.executeBatch();
			deletePstmt.executeBatch();
		} catch (SQLException e) {
			ArchitectureCreator.logger.error("Error during archi edge insert", e);
		} finally {
			JdbcDAOUtils.closePrepareStatement(insertPstmt);
			JdbcDAOUtils.closePrepareStatement(updatePstmt);
			JdbcDAOUtils.closePrepareStatement(deletePstmt);
		}
	}

	private void insertEdges() {
		Connection conn = JdbcDAOUtils.getConnection(this, Constants.CAQS_DATASOURCE_KEY);

		EdgesList edges = ArchitectureModel.getInstance().getEdgesList();
		ArchitectureCreator.logger.info("There are " + edges.size() + " Edges");
		try {
			conn.setAutoCommit(false);
			insertArchitectureEdges(edges, conn);
			insertRealEdges(edges, conn);
			JdbcDAOUtils.commit(conn);
		} catch (SQLException e) {
			JdbcDAOUtils.rollbackConnection(conn);
			ArchitectureCreator.logger.error("Error during edge insert", e);
		} finally {
			JdbcDAOUtils.closeConnection(conn);
		}

	}

	private void doElementIdPackUpdate(String idPack, ElementsList elements, Connection conn) {

		for (int assCpt = 0; assCpt < elements.size(); assCpt++) {
			Element currElement = (Element) elements.elementAt(assCpt);
			PreparedStatement sta = null;
			try {
				sta = conn.prepareStatement("UPDATE ELEMENT Set ID_PACK=? WHERE ID_ELT=?");
				ArchitectureCreator.logger.debug("UPDATE ELEMENT Set ID_PACK=" + idPack + " WHERE ID_ELT=" + currElement.getId());
				sta.setString(1, idPack);
				sta.setString(2, currElement.getId());
				sta.execute();
			} catch (Exception e) {
				ArchitectureCreator.logger.error("Error updating element package", e);
			} finally {
				JdbcDAOUtils.closePrepareStatement(sta);
			}

			/*
			//get parent CLass
			PreparedStatement st = null;
			try{
			    st = conn.prepareStatement("Update ELEMENT Set ID_PACK=? where id_elt in (select ELT_LINKS.ELT_PERE"
			            + " FROM ELEMENT ,"
			            + " ELT_LINKS WHERE ELEMENT.ID_ELT = ELT_LINKS.ELT_PERE"
			            //+ " AND ELEMENT.ID_TELT = '"+ElementType.CLS+"'"
			            + " AND ELT_LINKS.ELT_FILS =?)");
			    //+ " And DPEREMPTION IS NULL"); should be done any way
			    st.setString(1, id_pack);
			    st.setString(2, currElement.getId());
			    st.execute();
			} catch(Exception e){
			    ArchitectureCreator.m_log.error("Error updating element package", e);
			} finally {
			    JdbcDAOUtils.closePrepareStatement(st);
			}
			*/
		}

	}

	protected void updateMetrics() throws CaqsException {

		Connection conn = JdbcDAOUtils.getConnection(this, Constants.CAQS_DATASOURCE_KEY);
		ArchitectureCreator.logger.debug("setting metric : LIENS_CS" + ArchitectureModel.getInstance().getNumberOfEdgesByType(Edge.REAL_LINK_ANTI) + "for project ID " + this.mProjectId + " baselineID" + this.mBaselineId);
		ArchitectureCreator.logger.debug("setting metric : LIENS_NP" + ArchitectureModel.getInstance().getNumberOfEdgesByType(Edge.REAL_LINK_NOTEXPECTED) + "for project ID " + this.mProjectId + " baselineID" + this.mBaselineId);
		try {
			DaoFactory daoFactory = DaoFactory.getInstance();
			MetriqueDao metriqueDao = daoFactory.getMetriqueDao();
			metriqueDao.setMetrique(this.mProjectId, this.mBaselineId, "LIENS_CS", ArchitectureModel.getInstance().getNumberOfEdgesByType(Edge.REAL_LINK_ANTI), conn, true);
			metriqueDao.setMetrique(this.mProjectId, this.mBaselineId, "LIENS_NP", ArchitectureModel.getInstance().getNumberOfEdgesByType(Edge.REAL_LINK_NOTEXPECTED), conn, true);
		} catch (DataAccessException e) {
			throw new CaqsException("Error updating architecture metics", e);
		} finally {
			JdbcDAOUtils.closeConnection(conn);
		}
	}
}
