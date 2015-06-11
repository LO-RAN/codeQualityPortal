/*
 * ClearCaseServlet.java
 *
 * Created on 15 novembre 2002, 16:21
 */

package com.compuware.caqs.presentation.process.servlets;

import java.util.ResourceBundle;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.compuware.caqs.business.analysis.ClearCase;
import com.compuware.caqs.presentation.util.RequestUtil;

/**
 * @author cwfr-fxalbouy
 */
public class ClearCaseServlet extends HttpServlet {

    /**
	 * 
	 */
	private static final long serialVersionUID = -6954140150919546964L;

	/**
     * Initializes the servlet.
     */
    public void init(ServletConfig config) throws ServletException {
        super.init(config);

    }

    /**
     * Destroys the servlet.
     */
    public void destroy() {

    }

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     *
     * @param request  servlet request
     * @param response servlet response
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, java.io.IOException {
        response.setContentType("text/html");
        java.io.PrintWriter out = response.getWriter();
        String stream = request.getParameter("stream");
        ClearCase cc = new ClearCase(stream);
        String last = cc.getLastBaseLine();

        ResourceBundle resources = RequestUtil.getCaqsResourceBundle(request);

        //output your page here
        out.println("<html>");
        out.println("<head>");
        out.println("<title>Servlet</title>");
        out.println("</head>");
        out.println("<body>");
        out.println(resources.getString("caqs.clearcaseservlet.stream") + stream);
        out.println(resources.getString("caqs.clearcaseservlet.lastbline") + last);
        out.println("</body>");
        out.println("</html>");
        out.close();
    }

    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request  servlet request
     * @param response servlet response
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, java.io.IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request  servlet request
     * @param response servlet response
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, java.io.IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     */
    public String getServletInfo() {
        return "ClearCase Servlet";
    }

}
