package com.compuware.carscode.plugin.deventreprise.util;

import com.compuware.carscode.plugin.deventreprise.dao.DaoFactory;
import com.compuware.carscode.plugin.deventreprise.dao.DevEntrepriseDao;
import com.compuware.carscode.plugin.deventreprise.dataschemas.CobolSource;
import com.compuware.carscode.plugin.deventreprise.dataschemas.Copy;
import com.compuware.carscode.plugin.deventreprise.dataschemas.Violation;
import com.compuware.toolbox.dbms.JdbcDAOUtils;
import com.compuware.toolbox.util.logging.LoggerManager;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

/**
 *
 * @author cwfr-dzysman
 */
public class RetrieveExternalizedMetrics {

    static protected Logger logger = LoggerManager.getLogger("StaticAnalysis");

    private Document doc = null;
    private Element rootElement = null;
    private Connection connection = null;

    public RetrieveExternalizedMetrics(String xmlFilePath, Connection conn) {
        this.connection = conn;
        this.parseFile(xmlFilePath);
    }

    private void parseFile(String xmlFilePath) {
        File fichier = new File(xmlFilePath);
        try {
            if (fichier != null && fichier.exists() && fichier.isFile()) {
                SAXBuilder saxbuilder = new SAXBuilder();
                try {
                    this.doc = saxbuilder.build(fichier.getAbsolutePath());
                    this.rootElement = doc.getRootElement();
                }
                catch (JDOMException e) {
                    logger.error("Error parsing externalized metric file " + fichier.getAbsolutePath(), e);
                }
            }
        }
        catch (IOException e) {
            logger.error("Error parsing externalized metric file " + fichier.getAbsolutePath(), e);
        }
    }

    public void retrieveMetrics(CobolSource cs, List<Copy> copies) {
        List<Element> children = this.rootElement.getChildren("METRIC");
        for (Element metricElement : children) {
            this.calculateMetric(metricElement, cs, copies);
        }
    }

    private void calculateMetric(Element metricElement, CobolSource cs, List<Copy> copies) {
        String metricType = metricElement.getAttributeValue("type");
        String metricId = metricElement.getAttributeValue("id");
        if ("count".equals(metricType)) {
            Object retour = this.calculateMetricCountQuery(metricElement, cs);
            cs.getViolations().put(metricId, retour);
        }
        else if ("violation".equals(metricType)) {
            List<Violation> violations = this.calculateMetricViolationQuery(metricElement, cs);
            cs.setViolation(metricId, violations, copies);
        }
    }

    private List<Violation> calculateMetricViolationQuery(Element metricElement, CobolSource cs) {
        DevEntrepriseDao dao = DaoFactory.getInstance().getDevPartnerDao();
        List<Violation> retour = new ArrayList<Violation>();
        String query = this.retrieveQuery(metricElement);
        if (query != null) {
            PreparedStatement ps = null;
            ResultSet rs = null;
            String ownerPrefixValue = null;
            String ownerColumn = null;
            int ownerType = 0;
            String ownerTypeColumn = null;
            Element ownerPrefixElt = metricElement.getChild("PREFIX_OWNER");
            if (ownerPrefixElt != null) {
                ownerPrefixValue = ownerPrefixElt.getAttributeValue("prefixValue");
                ownerColumn = ownerPrefixElt.getAttributeValue("col");
                ownerTypeColumn = ownerPrefixElt.getAttributeValue("ownerTypeCol");
                String t = ownerPrefixElt.getAttributeValue("type");
                if (t != null) {
                    try {
                        ownerType = Integer.parseInt(t);
                    }
                    catch (NumberFormatException e) {
                        ownerType = -1;
                    }
                }
            }

            String lineDetailFrom = "";
            String lineDetailColumn = "";
            Element lineDetail = metricElement.getChild("RETRIEVE_LINE");
            if(lineDetail != null) {
                lineDetailFrom = lineDetail.getAttributeValue("from");
                lineDetailColumn = lineDetail.getAttributeValue("column");
            }

            try {
                if (this.connection != null) {
                    ps = this.connection.prepareStatement(query);
                    this.putParameters(ps, metricElement, cs);
                    rs = ps.executeQuery();
                    if (rs != null) {
                        while (rs.next()) {
                            String owner = "";
                            if (ownerColumn != null) {
                                owner = rs.getString(ownerColumn);
                            }
                            if (ownerTypeColumn != null &&
                                    !"".equals(ownerTypeColumn)) {
                                int typeId = rs.getInt(ownerTypeColumn);
                                if (typeId == ownerType) {
                                    owner = ownerPrefixValue + owner;
                                }
                            }

                            String corr = rs.getString(lineDetailColumn);
                            String line = null;
                            if("relDetail".equals(lineDetailFrom)) {
                                line = dao.getLineFromRelDetail(corr);
                            }
                            else if("fileLine".equals(lineDetailFrom)) {
                                line = dao.getFileLine(corr);
                            }
                            if (line != null) {
                                Violation v = new Violation(line, owner);
                                retour.add(v);
                            }
                        }
                    }
                }
            }
            catch (SQLException e) {
                logger.error("Error calculating metric violation", e);
            }
            finally {
                JdbcDAOUtils.closeResultSet(rs);
                JdbcDAOUtils.closeStatement(ps);
            }
        }
        return retour;
    }

    private Object calculateMetricCountQuery(Element metricElement, CobolSource cs) {
        Object retour = null;
        String query = this.retrieveQuery(metricElement);
        if (query != null) {
            PreparedStatement ps = null;
            ResultSet rs = null;
            try {
                if (this.connection != null) {
                    ps = this.connection.prepareStatement(query);
                    this.putParameters(ps, metricElement, cs);
                    rs = ps.executeQuery();
                    if (rs != null && rs.next()) {
                        retour = this.retrieveCountResult(metricElement, rs);
                    }
                }
            }
            catch (SQLException e) {
                logger.error("Error calculating metric by count", e);
            }
            finally {
                JdbcDAOUtils.closeResultSet(rs);
                JdbcDAOUtils.closeStatement(ps);
            }
        }
        return retour;
    }

    private Object retrieveCountResult(Element metricElement, ResultSet rs)
            throws SQLException {
        Element retourElt = metricElement.getChild("RETURN");
        String retourCol = retourElt.getAttributeValue("id");
        return rs.getInt(retourCol);
    }

    private String retrieveQuery(Element metricElement) {
        String q = null;
        Element queryElt = metricElement.getChild("QUERY");
        if (queryElt != null) {
            q = queryElt.getText();
        }
        return q;
    }

    private void putParameters(PreparedStatement ps, Element metricElement, CobolSource cs)
            throws SQLException {
        Map<String, Object> globalParams = cs.getGlobalParams();
        Element paramsElt = metricElement.getChild("PARAMS");
        if (paramsElt != null) {
            List<Element> paramList = paramsElt.getChildren("PARAM");
            if (paramList != null) {
                for (Element param : paramList) {
                    String sOrder = param.getAttributeValue("order");
                    int order = Integer.parseInt(sOrder);
                    String type = param.getAttributeValue("type");
                    String paramName = param.getAttributeValue("id");
                    if ("int".equals(type)) {
                        Integer p = (Integer) globalParams.get(paramName);
                        ps.setInt(order, p);
                    }
                    else if ("string".equals(type)) {
                        String p = this.getStringParameterValue(globalParams, param);
                        ps.setString(order, p);
                    }
                }
            }
        }
    }

    private String getStringParameterValue(Map<String, Object> globalParams, Element parameterDef) {
        String paramName = parameterDef.getAttributeValue("id");
        String p = (String) globalParams.get(paramName);
        String comparison = parameterDef.getAttributeValue("comparison");
        if ("startsWith".equals(comparison)) {
            p += "%";
        }
        else if ("endsWith".equals(comparison)) {
            p = "%" + p;
        }
        else if ("contains".equals(comparison)) {
            p = "%" + p + "%";
        }
        return p;
    }
}
