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
	
    <xsl:import href="../content-synthese-bottomup.xsl"/>
    
    <xsl:variable name="bottomup.amelioration.title">Improvements to make</xsl:variable>
    <xsl:variable name="bottomup.amelioration.volumetrie">Volumetry</xsl:variable>
    <xsl:variable name="bottomup.amelioration.tableau">Table </xsl:variable>
    
    <xsl:variable name="bottomup.repartitionelement.title">Elements dispatching analysis</xsl:variable>
    <xsl:variable name="bottomup.amelioration.repartitionparniveau"> : Elements dispatching according to their acceptability state</xsl:variable>
    <xsl:variable name="bottomup.amelioration.nbrejet">Number of refused elements</xsl:variable>
    <xsl:variable name="bottomup.amelioration.nbrejetexplain">– at least one mark between 1 and 2</xsl:variable>
    <xsl:variable name="bottomup.amelioration.nbreserve">Number of elements acceptable with reserve</xsl:variable>
    <xsl:variable name="bottomup.amelioration.nbreserveexplain">– at least one mark between 2 and 3 (but no mark 1)</xsl:variable>
    <xsl:variable name="bottomup.amelioration.nbaccepte">Number of accepted elements</xsl:variable>
    <xsl:variable name="bottomup.amelioration.nbaccepteexplain">– at least one mark between 3 and 4 (but no mark 1 and no mark 2)</xsl:variable>
    <xsl:variable name="bottomup.amelioration.pctamelior">Percentage of elements to improve</xsl:variable>
    
    
    <xsl:variable name="bottomup.repartitiondefaut.title">Defects dispatching analysis</xsl:variable>
    <xsl:variable name="bottomup.amelioration.repartitionparseverite"> : Dispatching by severity</xsl:variable>
    <xsl:variable name="bottomup.amelioration.nbameliornv1">Nuber of level 1 improvements</xsl:variable>
    <xsl:variable name="bottomup.amelioration.nbameliornv2">Number of level 2 improvements</xsl:variable>
    <xsl:variable name="bottomup.amelioration.nbtotalamelior">Total number of improvements to make</xsl:variable>

    
    <xsl:variable name="bottomup.amelioration.ameliorparobjectif">Defects dispatching by goal</xsl:variable>
    <xsl:variable name="bottomup.amelioration.figure">Figure </xsl:variable>
    <xsl:variable name="bottomup.amelioration.repartitionameliorparobj"> : Defects dispatching by goal</xsl:variable>
    <xsl:variable name="bottomup.amelioration.ameliorparcritere">Defects dispatching by criterion</xsl:variable>
    <xsl:variable name="bottomup.amelioration.repartitionameliorparcrit"> : Defects dispatching by criterion</xsl:variable>

</xsl:stylesheet>
