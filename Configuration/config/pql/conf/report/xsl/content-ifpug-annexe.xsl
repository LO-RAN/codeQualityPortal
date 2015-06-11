<?xml version="1.0" encoding="UTF-8" ?>

<!--
    Document   : report2fo.xsl
    Created on : 2 février 2004
    Author     : cwfr-gcartigny
    Description:
        Purpose of transformation follows.
-->

<xsl:stylesheet version="1.0"
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:text="urn:oasis:names:tc:opendocument:xmlns:text:1.0"
    xmlns:table="urn:oasis:names:tc:opendocument:xmlns:table:1.0"
    xmlns:office="urn:oasis:names:tc:opendocument:xmlns:office:1.0">

    <xsl:strip-space elements="MODEL REPORT"/>
              
    <xsl:template match="REPORT">
      <text:p text:style-name="Text_20_body">
		<xsl:value-of select="$definition.ifpug.explanation"/></text:p>
		<text:p text:style-name="Text_20_body">
			<xsl:value-of select="IFPUG"/>
		</text:p>
    </xsl:template>

</xsl:stylesheet> 
