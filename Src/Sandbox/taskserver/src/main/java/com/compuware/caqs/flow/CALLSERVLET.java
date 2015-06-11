package com.compuware.caqs.flow;
/*
 * LaunchTask.java
 *
 * Created on 28 novembre 2002, 17:49
 */

import java.net.URLConnection;
import java.net.URL;
import java.io.*;

/**
 *
 * @author  cwfr-fdubois
 */
public class CALLSERVLET {

    /** Creates a new instance of LaunchTask */
    public CALLSERVLET() {
    }
    
    public String CALLURL(String url) {
        StringBuffer result = new StringBuffer();
        BufferedReader inputFromServlet = null;
        System.out.println("URL: " + url);
        try {
            URL servleturl = new URL( url );

            URLConnection servletConnection = servleturl.openConnection();

            // Read the input from the servlet.
            System.out.println("Getting input stream");
            inputFromServlet = new BufferedReader(new InputStreamReader(servletConnection.getInputStream()));
            String oes = "";
            while (oes != null) {
                try {
                    oes = inputFromServlet.readLine();
                    if (oes != null)
                        result.append(oes);
                }
                catch (IOException ex) {
                    result = new StringBuffer();
                    result.append("1 " + ex.getMessage());
                    oes = null;
                }
            }
        }
        catch (Exception e) {
            result = new StringBuffer();
            result.append("1 " + e.getMessage());
        }
        finally {
            try {
                if (inputFromServlet != null)
                    inputFromServlet.close();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result.toString();
    }
    
    /**
    public static void main(String[] args) {
        String url;
        url = "http://"+args[0]+":"+args[1]+"/SncfQualite/servlet/CalculServlet?id_pro="+args[2]+"&id_bline="+args[3];
        CALLSERVLET task = new CALLSERVLET();
        String res = task.CALLURL(url);
        System.out.println(res);
    }
    */
    
}
