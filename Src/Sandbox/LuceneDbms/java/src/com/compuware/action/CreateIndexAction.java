package com.compuware.action;

import java.io.IOException;
import java.util.*;
import java.util.Date;
import java.sql.*;

import com.compuware.dbms.connection.JdbcDAOUtils;
import com.compuware.searchengine.indexer.IndexDbms;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.MessageResources;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.analysis.standard.StandardAnalyzer;

public final class CreateIndexAction extends Action {


    public ActionForward perform(ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response)
            throws IOException, ServletException {

        // Extract attributes we will need
        Locale locale = getLocale(request);
        MessageResources messages = getResources();

        // ActionErrors needed for error passing
        ActionErrors errors = new ActionErrors();

        HttpSession session = request.getSession();

        ActionForward forward = null;

        Connection conn = JdbcDAOUtils.getConnection(this, "PqlDS");
        Date start = new Date();
        try {
            IndexWriter writer = new IndexWriter("index", new StandardAnalyzer(), true);
            String[] columnNames = new String[3];
            columnNames[0] = "ID_FICHE";
            columnNames[1] = "LIB_FICHE";
            columnNames[2] = "INFOS_FICHE";

            IndexDbms.indexDocs(writer, "FICHE_CONNAISSANCE", columnNames, conn);

            writer.optimize();
            writer.close();

            Date end = new Date();

            System.out.print(end.getTime() - start.getTime());
            System.out.println(" total milliseconds");

        } catch (IOException e) {
            System.out.println(" caught a " + e.getClass() +
                    "\n with message: " + e.getMessage());
        }
        finally {
            JdbcDAOUtils.closeConnection(conn);
        }

        if (forward != null)
            return forward;

        return (mapping.findForward("success"));

    }

}
