/**
 * 
 */
package com.compuware.carscode.parser.ant;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Authenticator;
import java.net.MalformedURLException;
import java.net.PasswordAuthentication;
import java.net.URL;
import java.net.URLConnection;

import org.apache.tools.ant.BuildException;

/**
 * @author cwfr-fdubois
 *
 */
public class AbapConnector extends AbstractConnector {
	
	/** The Abap analyzer url. */
	private String url;
	/** The Abap server login. */
	private String login;
	/** The Abap server password. */
	private String password;
	/** The destination file. */
	private String destFile;

	/**
	 * @param destFile The destFile to set.
	 */
	public void setDestFile(String destFile) {
		this.destFile = destFile;
	}

	/**
	 * @param login The login to set.
	 */
	public void setLogin(String login) {
		this.login = login;
	}

	/**
	 * @param password The password to set.
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * @param url The url to set.
	 */
	public void setUrl(String url) {
		this.url = url;
	}

	protected void checkParameters() throws BuildException {
        if (url == null || url.length() < 1) {
            throw new BuildException("A url is required.", getLocation());
        }
        if (login == null || login.length() < 1) {
            throw new BuildException("A login is required.", getLocation());
        }
        if (password == null || password.length() < 1) {
            throw new BuildException("A password is required.", getLocation());
        }
        if (destFile == null || destFile.length() < 1) {
            throw new BuildException("A destination file is required.", getLocation());
        }
    }
	
    protected void processAnalysis() {
        callRemoteAnalysis(this.url, this.login, this.password, this.destFile);
    }
    
	public void callRemoteAnalysis(String url, final String login, final String password, String destFile) throws BuildException {

		Authenticator.setDefault(new Authenticator() {
		    protected PasswordAuthentication getPasswordAuthentication() {
		        return new PasswordAuthentication (login, password.toCharArray());
		    }
		});
		
		InputStream input = null;

		URL servletUrl;
		try {
			servletUrl = new URL(url);
			log("Connecting to SAP Web Server using url : "+ url);
	        URLConnection servletConnection = servletUrl.openConnection();
	        input = servletConnection.getInputStream();
	        InputStreamReader reader = new InputStreamReader(input);
	        BufferedReader breader = new BufferedReader(reader);
	        
	        File fileOut = new File(destFile);
	        FileWriter writer = new FileWriter(fileOut);
	        BufferedWriter bwriter = new BufferedWriter(writer);
	        
			log("Writing result to file " + destFile);
	        char[] buf = new char[1024];
	        int i = 0;
	        while((i = breader.read(buf))!=-1) {
	        	bwriter.write(buf, 0, i);
	        }
	        bwriter.close();
	        writer.close();
	        breader.close();
	        reader.close();
		}
		catch (MalformedURLException e) {
            throw new BuildException("Error during analysis.", e, getLocation());
		}
		catch (IOException e) {
            throw new BuildException("Error during analysis.", e, getLocation());
		}
		
	}

}
