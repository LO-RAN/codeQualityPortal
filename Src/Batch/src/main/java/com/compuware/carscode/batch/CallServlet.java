package com.compuware.carscode.batch;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class CallServlet {
	
    public String callUrl(String url) {
    	boolean error = false;
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
                    if (oes != null) {
                        result.append(oes);
                    }
                    if (result.indexOf("ok=0") >= 0) {
                    	result.append(("**** ERROR **** during task execution"));
                    	error = true;
                    }
                    if (result.indexOf("0;") >= 0) {
                    	result.append(("**** ERROR **** during task execution"));
                    	error = true;
                    }
                }
                catch (IOException ex) {
                    result = new StringBuffer();
                    result.append("**** ERROR **** " + ex.getMessage());
                    oes = null;
                	error = true;
                }
            }
        }
        catch (Exception e) {
            result.append("**** ERROR **** " + e.getMessage());
        	error = true;
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
        
        if (error) {
        	//System.exit(1);
        }
        
        return result.toString();
    }
    
    public static void main(String[] args) {
    	/*
        StringBuffer url = new StringBuffer(args[0]);
        for (int i = 1; i < args.length; i++) {
        	if((i%2) == 0) {
        		url.append('=');
        	}
        	else if (i > 1) {
        		url.append('&');
        	}
        	url.append(args[i]);
        }
        */
        CallServlet task = new CallServlet();
        String res = task.callUrl(args[0].replaceAll(" ", "%20"));
        System.out.println(res);
    }

}
