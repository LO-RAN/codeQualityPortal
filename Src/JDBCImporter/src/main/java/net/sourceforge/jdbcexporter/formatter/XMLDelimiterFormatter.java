package net.sourceforge.jdbcexporter.formatter;

/*
 * JDBC Importer - database import utility/framework.
 * Copyright (C) 2002 Chris Nagy
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 * 
 * Chris Nagy
 * Email:  cnagyxyz@hotmail.com
 */

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import net.sourceforge.jdbcexporter.DelimiterFormatter;
import net.sourceforge.jdbcimporter.ColumnValue;

/**
 * The XMLDelimiterFormatter class implements the DelimiterFormatter interface
 * to format column values into an XML document and transform the XML document
 * with the given XSL.
 * 
 * @version 0.6
 * @author Chris Nagy
 */
public class XMLDelimiterFormatter implements DelimiterFormatter {
	/**
	 * The output writer.
	 */
	protected Writer writer;

	/**
	 * The XSL used to transform the XML.
	 */
	protected String xslString;

	/**
	 * The temporary writer.
	 */
	protected StringWriter tmpWriter;

	/**
	 * The root element name.
	 */
	protected String rowsElementName = "e";

	/**
	 * The name of the element that encloses the row data.
	 */
	protected String rowElementName = "r";

	/**
	 * The list of names for the column elements.
	 */
	protected String[] columnElementNames = new String[] { "c" };

	/**
	 * Sets the root element name.
	 * 
	 * @param name
	 *            the element name
	 * @since 0.7
	 */
	public void setRowsElement(String name) {
		this.rowsElementName = name;
	}

	/**
	 * Sets the name of the element that encloses the row data.
	 * 
	 * @param name
	 *            the element name
	 * @since 0.7
	 */
	public void setRowElement(String name) {
		this.rowElementName = name;
	}

	/**
	 * Sets the list of names for the column elements.
	 * 
	 * @param names
	 *            a comma-separated list of element names. if the list of
	 *            columns is longer then the last value in the list is used for
	 *            the rest of columns.
	 * @since 0.7
	 */
	public void setColumnElements(String names) {
		List nameList = new ArrayList();
		StringTokenizer tokenizer = new StringTokenizer(names, ",");
		while (tokenizer.hasMoreTokens()) {
			nameList.add(tokenizer.nextToken());
		}
		this.columnElementNames = new String[nameList.size()];
		this.columnElementNames = (String[]) nameList
				.toArray(this.columnElementNames);
	}

	/**
	 * @see net.sourceforge.jdbcexporter.DelimiterFormatter#setWriter(java.io.Writer)
	 */
	public void setWriter(Writer output) {
		writer = output;
	}

	/**
	 * @see net.sourceforge.jdbcexporter.DelimiterFormatter#formatValues(net.sourceforge.jdbcimporter.ColumnValue[])
	 */
	public String formatValues(ColumnValue[] values) {
		StringBuffer buf = new StringBuffer("<" + rowElementName + ">");
		for (int i = 0; i < values.length; i++) {
			String columnElementName = null;
			if (columnElementNames.length <= i) {
				columnElementName = columnElementNames[columnElementNames.length - 1];
			} else {
				columnElementName = columnElementNames[i];
			}
			if (values[i].getString() == null
					|| "".equals(values[i].getString())) {
				buf.append("<" + columnElementName + "/>");
			} else {
				buf.append("<" + columnElementName + ">");
				buf.append("<![CDATA[" + values[i].getString() + "]]>");
				buf.append("</" + columnElementName + ">");
			}

		}
		buf.append("</" + rowElementName + ">");
		return buf.toString();
	}

	/**
	 * @see net.sourceforge.jdbcexporter.DelimiterFormatter#writeNextRow(java.lang.String)
	 */
	public void writeNextRow(String row) throws IOException {
		if (tmpWriter == null) {
			tmpWriter = new StringWriter();

			// if no xsl required, no need for temporary writer
			if (xslString == null) {
				writer.write("<?xml version=\"1.0\"  encoding=\"UTF-8\"?>");
				writer.write("<" + rowsElementName + ">");
			} else {
				tmpWriter.write("<?xml version=\"1.0\"  encoding=\"UTF-8\"?>");
				tmpWriter.write("<" + rowsElementName + ">");
			}
		}
		// if no xsl required, no need for temporary writer
		if (xslString == null) {
			writer.write(row);
		} else {
			tmpWriter.write(row);
		}
	}

	/**
	 * @see net.sourceforge.jdbcexporter.DelimiterFormatter#finish()
	 */
	public void finish() throws IOException {
		// if the xml string is not empty
		if (tmpWriter != null) {
	         // if XSL transformation required
			if (xslString != null) {
				tmpWriter.write("</" + rowsElementName + ">");

				StringReader tmpReader = new StringReader(tmpWriter.toString());
				try {
					Transformer transformer = TransformerFactory.newInstance()
							.newTransformer(
									new StreamSource(
											new StringReader(xslString)));
					transformer.transform(new StreamSource(tmpReader),
							new StreamResult(writer));
				} catch (TransformerConfigurationException e) {
					throw new IOException(e.getMessage());
				} catch (TransformerException e) {
					throw new IOException(e.getMessage());
				}
			} else {
				writer.write("</" + rowsElementName + ">");
			}
		}

	}

	/**
	 * Set the xsl filename that will be used to transform the XML before
	 * writing to the file.
	 * 
	 * @param filename
	 *            the filename
	 */
	public void setXSLFile(String filename) {
		StringBuffer xslBuf = new StringBuffer("");
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(filename));
			String line = null;
			while ((line = reader.readLine()) != null) {
				xslBuf.append(line);
				xslBuf.append("\n");
			}
		} catch (IOException e) {
			throw new IllegalArgumentException(e.getMessage());
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {

				}
			}
		}
		setXSL(xslBuf.toString());
	}

	/**
	 * Set the XSL that will be used to transform the XML.
	 * 
	 * @param str
	 *            the xsl
	 */
	public void setXSL(String str) {
		xslString = str;
	}
}
