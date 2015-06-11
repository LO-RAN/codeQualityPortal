/**
 * 
 */
package com.compuware.caqs.presentation.common.tag;

import java.io.IOException;
import java.util.Properties;

import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.BodyContent;
import javax.servlet.jsp.tagext.BodyTagSupport;

import com.compuware.caqs.util.CaqsConfigUtil;

/**
 * @author cwfr-fdubois
 *
 */
public class JreDefinedAppletTag extends BodyTagSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1086924558693583067L;
	
	private String classid;
	private String code;
	private String codebase;
	private String archive;
	private String width;
	private String height;
	private String type;
	private String message;
	
	/**
	 * @return Returns the archive.
	 */
	public String getArchive() {
		return archive;
	}

	/**
	 * @param archive The archive to set.
	 */
	public void setArchive(String archive) {
		this.archive = archive;
	}

	/**
	 * @return Returns the classid.
	 */
	public String getClassid() {
		return classid;
	}

	/**
	 * @param classid The classid to set.
	 */
	public void setClassid(String classid) {
		this.classid = classid;
	}

	/**
	 * @return Returns the code.
	 */
	public String getCode() {
		return code;
	}

	/**
	 * @param code The code to set.
	 */
	public void setCode(String code) {
		this.code = code;
	}

	/**
	 * @return Returns the codebase.
	 */
	public String getCodebase() {
		return codebase;
	}

	/**
	 * @param codebase The codebase to set.
	 */
	public void setCodebase(String codebase) {
		this.codebase = codebase;
	}

	/**
	 * @return Returns the height.
	 */
	public String getHeight() {
		return height;
	}

	/**
	 * @param height The height to set.
	 */
	public void setHeight(String height) {
		this.height = height;
	}

	/**
	 * @return Returns the message.
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * @param message The message to set.
	 */
	public void setMessage(String message) {
		this.message = message;
	}

	/**
	 * @return Returns the type.
	 */
	public String getType() {
		return type;
	}

	/**
	 * @param type The type to set.
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * @return Returns the width.
	 */
	public String getWidth() {
		return width;
	}

	/**
	 * @param width The width to set.
	 */
	public void setWidth(String width) {
		this.width = width;
	}

	public int doStartTag() throws JspTagException {
		return EVAL_BODY_BUFFERED;
	}
	
	public int doAfterBody() throws JspTagException {
		/*
      BodyContent bc = getBodyContent();
      // get the bc as string
      String query = bc.getString();
      // clean up
      bc.clearBody();
      */
      return SKIP_BODY;
   }
	
	public int doEndTag() throws JspTagException {
		BodyContent bc = getBodyContent();
		// get the bc as string
		String content = bc.getString();
        Properties dynProp = CaqsConfigUtil.getCaqsGlobalConfigProperties();
        this.classid = dynProp.getProperty("java.applet.jre.classid");
        String version = dynProp.getProperty("java.applet.jre.version");
        if (this.classid != null && this.classid.length() > 0) {
        	printAppletHeader(content, version);
        	printAppletFooter(content, version);
        }
        else {
        	printStandardAppletHeader(content);
        	printStandardAppletFooter(content);
        }
		return EVAL_PAGE;
	}
	
	private void printStandardAppletHeader(String body)throws JspTagException {
		try {
			JspWriter writer = pageContext.getOut();
			writer.append("<applet ");
			writer.append(" width=\"").append(this.width).append('\"');
			writer.append(" height=\"").append(this.height).append('\"');
			writer.append(" code=\"").append(this.code).append('\"');
			writer.append(" archive=\"").append(this.archive).append('\"');
			writer.append(" codebase=\"").append(this.codebase).append('\"');
			writer.append(" type=\"").append(this.type).append("\">");
			writer.append(body);
		}
		catch (IOException e) {
			throw new JspTagException(e);
		}
	}
	
	private void printAppletHeader(String body, String version)throws JspTagException {
		try {
			JspWriter writer = pageContext.getOut();
			writer.append("<!--[if IE]>");
			writer.append("<object ").append("classid=\"").append(this.classid).append('\"');
			writer.append(" width=\"").append(this.width).append('\"');
			writer.append(" height=\"").append(this.height).append("\">");
			writer.append("<param name=\"code\" value=\"").append(this.code).append("\" />");
			writer.append("<param name=\"archive\" value=\"").append(this.archive).append("\" />");
			writer.append("<param name=\"codebase\" value=\"").append(this.codebase).append("\" />");
			writer.append("<param name=\"type\" value=\"").append(this.type);
			if (version != null && version.length() > 0) {
				writer.append(";version=").append(version).append("\" />");
			}
			writer.append(body);
		}
		catch (IOException e) {
			throw new JspTagException(e);
		}
	}
	
	private void printStandardAppletFooter(String body)throws JspTagException {
		try {
			JspWriter writer = pageContext.getOut();
			writer.append("<strong>").append(this.message).append("</strong>");
			writer.append("</applet>");
		}
		catch (IOException e) {
			throw new JspTagException(e);
		}
	}
	
	private void printAppletFooter(String body, String version)throws JspTagException {
		try {
			JspWriter writer = pageContext.getOut();
			writer.append("<strong>").append(this.message).append("</strong>");
			writer.append("</object>");
			writer.append("<![endif] -->");
			writer.append("<comment>");
			writer.append("<embed");
			writer.append(" width=\"").append(this.width).append('\"');
			writer.append(" height=\"").append(this.height).append('\"');
			writer.append(" code=\"").append(this.code).append('\"');
			writer.append(" archive=\"").append(this.archive).append('\"');
			writer.append(" codebase=\"").append(this.codebase).append('\"');
			writer.append(" type=\"").append(this.type);
			if (version != null && version.length() > 0) {
				writer.append(";version=").append(version).append('\"');
			}
			writer.append(transformParameters(body));
			writer.append('>');
			writer.append("</embed>");
			writer.append("</comment>");
		}
		catch (IOException e) {
			throw new JspTagException(e);
		}
	}
	private String transformParameters(String input) {
		String result = input.replaceAll("<PARAM name=\"", " ");
		result = result.replaceAll("\" value=\"", "=\"");
		result = result.replaceAll("\" value='", "=\"");
		result = result.replaceAll("\" VALUE=\"", "=\"");
		result = result.replaceAll("\" VALUE='", "=\"");
		result = result.replaceAll("\" />", "\"");
		result = result.replaceAll("' />", "\"");
		return result;
	}
}
