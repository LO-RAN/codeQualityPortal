package com.compuware.action;

import com.compuware.form.ResultForm;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.queryParser.MultiFieldQueryParser;
import org.apache.lucene.search.Hits;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.Searcher;
import org.apache.struts.action.*;
import org.apache.struts.util.MessageResources;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Locale;

public final class SearchAction extends Action {


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

        String filter = request.getParameter("filter");

        try {
            Searcher searcher = new IndexSearcher("index");
            Analyzer analyzer = new StandardAnalyzer();

            String[] queryFields = new String[3];
            queryFields[0] = "ID_FICHE";
            queryFields[1] = "LIB_FICHE";
            queryFields[2] = "INFOS_FICHE";
            Query query = MultiFieldQueryParser.parse(filter, queryFields, analyzer);
            System.out.println("Searching for: " + query.toString("contents"));

            Hits hits = searcher.search(query);
            System.out.println(hits.length() + " total matching documents");

            final int HITS_PER_PAGE = 10;
            Collection result = new ArrayList();
            for (int start = 0; start < hits.length(); start += HITS_PER_PAGE) {
                int end = Math.min(hits.length(), start + HITS_PER_PAGE);
                for (int i = start; i < end; i++) {
                    Document doc = hits.doc(i);
                    String key = doc.get("ID_FICHE");
                    ResultForm resultForm = new ResultForm();
                    resultForm.setId(key);
                    resultForm.setContent(doc.get("LIB_FICHE"));
                    if (key != null) {
                        result.add(resultForm);
                    }
                }

                request.setAttribute("result", result);
            }
            searcher.close();

        } catch (Exception e) {
            System.out.println(" caught a " + e.getClass() +
                    "\n with message: " + e.getMessage());
        }

        return (mapping.findForward("success"));

    }

}
