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
		xmlns:office="urn:oasis:names:tc:opendocument:xmlns:office:1.0"
    xmlns:style="urn:oasis:names:tc:opendocument:xmlns:style:1.0"
    xmlns:text="urn:oasis:names:tc:opendocument:xmlns:text:1.0"
    xmlns:table="urn:oasis:names:tc:opendocument:xmlns:table:1.0"
    xmlns:draw="urn:oasis:names:tc:opendocument:xmlns:drawing:1.0"
    xmlns:fo="urn:oasis:names:tc:opendocument:xmlns:xsl-fo-compatible:1.0"
    xmlns:xlink="http://www.w3.org/1999/xlink"
    xmlns:dc="http://purl.org/dc/elements/1.1/"
    xmlns:meta="urn:oasis:names:tc:opendocument:xmlns:meta:1.0"
    xmlns:number="urn:oasis:names:tc:opendocument:xmlns:datastyle:1.0"
    xmlns:svg="urn:oasis:names:tc:opendocument:xmlns:svg-compatible:1.0"
    xmlns:chart="urn:oasis:names:tc:opendocument:xmlns:chart:1.0"
    xmlns:dr3d="urn:oasis:names:tc:opendocument:xmlns:dr3d:1.0"
    xmlns:math="http://www.w3.org/1998/Math/MathML"
    xmlns:form="urn:oasis:names:tc:opendocument:xmlns:form:1.0"
    xmlns:script="urn:oasis:names:tc:opendocument:xmlns:script:1.0"
    xmlns:ooo="http://openoffice.org/2004/office"
    xmlns:ooow="http://openoffice.org/2004/writer"
    xmlns:oooc="http://openoffice.org/2004/calc"
    xmlns:dom="http://www.w3.org/2001/xml-events"
    xmlns:xforms="http://www.w3.org/2002/xforms"
    xmlns:xsd="http://www.w3.org/2001/XMLSchema"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    office:version="1.0">
	
    <xsl:import href="../content-criterion-definitions.xsl"/>
    
    <xsl:variable name="definition.definition">Definition</xsl:variable>
    <xsl:variable name="definition.complement">Complement</xsl:variable>
    <xsl:variable name="definition.aggregation">Aggregation</xsl:variable>
    <xsl:variable name="definition.typeelement">Element type</xsl:variable>
    <xsl:variable name="definition.typeaggregation">Aggregation type</xsl:variable>
    <xsl:variable name="definition.reglecalcul">Computing rule</xsl:variable>
    <xsl:variable name="definition.regle">Formula</xsl:variable>
    <xsl:variable name="definition.valeur">Score</xsl:variable>
    <xsl:variable name="definition.type.classe">Class</xsl:variable>
    <xsl:variable name="definition.type.method">Method</xsl:variable>
    <xsl:variable name="definition.type.ea">Application entity</xsl:variable>
    <xsl:variable name="definition.type.all">All</xsl:variable>
    <xsl:variable name="definition.aggreg.avg">Weighted average</xsl:variable>
    <xsl:variable name="definition.aggreg.exclus">Exclusion</xsl:variable>
    <xsl:variable name="definition.aggreg.avg_all">Weighted average on all elements</xsl:variable>
    <xsl:variable name="definition.aggreg.avg_weight">Average weighted by score</xsl:variable>
    <xsl:variable name="definition.aggreg.exclus_avg">Weighted average with exclusion</xsl:variable>
    <xsl:variable name="definition.aggreg.exclus_avg_seuil">Excluding weighted average with level</xsl:variable>
    <xsl:variable name="definition.aggreg.multi_seuil">Score depending on level</xsl:variable>
    <xsl:variable name="definition.aggreg.autre">Other</xsl:variable>
	<xsl:variable name="definition.formula.true">Else</xsl:variable>
	<xsl:variable name="definition.costformula">Correction cost computing rule</xsl:variable>
	<xsl:variable name="definition.costformula.explanation">This formula will be applied to each element with problem in order to estimate its correction cost: </xsl:variable>
</xsl:stylesheet>
