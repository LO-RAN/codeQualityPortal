package com.compuware.searchengine.indexer;

import com.compuware.dbms.connection.JdbcDAOUtils;
import com.compuware.searchengine.document.TextDocument;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.IndexWriter;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: cwfr-fdubois
 * Date: 21 nov. 2005
 * Time: 15:00:29
 * To change this template use File | Settings | File Templates.
 */
public class IndexDbms {

    public static void indexDocs(IndexWriter writer, String tableName, String[] columnNames, Connection conn)
            throws IOException {
        Statement stmt = null;
        ResultSet rs = null;
        // do not try to index files that cannot be read
        System.out.println("adding " + tableName);
        String columnNamesStr = getQueryColumn(columnNames);
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT DISTINCT " + columnNamesStr + " FROM " + tableName);
            while (rs.next()) {
                String[] values = new String[columnNames.length];
                for (int i = 0; i < values.length; i++) {
                    String tmp = rs.getString(i+1);
                    if (tmp == null)
                        tmp = "";
                    values[i] = tmp;
                }
                if (values != null) {
                    writer.addDocument(TextDocument.Document(columnNames, values));
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            JdbcDAOUtils.closeResultSet(rs);
            JdbcDAOUtils.closeStatement(stmt);
        }
    }

    private static String getQueryColumn(String[] columnNames) {
        StringBuffer result = new StringBuffer();
        if (columnNames != null) {
            for (int i = 0; i < columnNames.length; i++) {
                if (i != 0)
                    result.append(", ");
                result.append(columnNames[i]);
            }
        }
        return result.toString();
    }

    public static void main(String[] args) {
        String usage = "java " + IndexDbms.class + " <root_directory>";
        if (args.length == 0) {
            System.err.println("Usage: " + usage);
            System.exit(1);
        }

        Date start = new Date();
        try {
            IndexWriter writer = new IndexWriter("index", new StandardAnalyzer(), true);
            String[] columnNames = new String[2];
            columnNames[0] = "ID_FICHE";
            columnNames[1] = "LIB_FICHE";
            columnNames[2] = "INFOS_FICHE";
            indexDocs(writer, "FICHE_CONNAISSANCE", columnNames, JdbcDAOUtils.getConnection(new String(), "PqlDS"));

            writer.optimize();
            writer.close();

            Date end = new Date();

            System.out.print(end.getTime() - start.getTime());
            System.out.println(" total milliseconds");

        } catch (IOException e) {
            System.out.println(" caught a " + e.getClass() +
                    "\n with message: " + e.getMessage());
        }
    }

}
