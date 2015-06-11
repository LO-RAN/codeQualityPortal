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

    <xsl:import href="../content-synthese.xsl"/>
    
    <xsl:variable name="synthese.title">Quality goals synthesis</xsl:variable>
    
    <xsl:variable name="synthese.resultat"> Results</xsl:variable>
    <xsl:variable name="synthese.figure">Figure </xsl:variable>
    <xsl:variable name="synthese.figure.graph"> : Kiviat of audited system</xsl:variable>
    
    <xsl:variable name="synthese.tableau">Table </xsl:variable>
    <xsl:variable name="synthese.tableau.synthese"> : Goal marks synthesis</xsl:variable>
    
    <xsl:variable name="synthese.facteur">Quality goal</xsl:variable>
    <xsl:variable name="synthese.note">Mark</xsl:variable>
    <xsl:variable name="synthese.tendance">Trend</xsl:variable>
    <xsl:variable name="synthese.commentaireLib">Comments</xsl:variable>

    <xsl:variable name="general.nb">Number of</xsl:variable>

    <xsl:variable name="synthese.commentaireVal">COMMENTS</xsl:variable>
    <xsl:variable name="synthese.asterix">* With marks higher or equal to 3 the application is considered accepted. With marks lower than 3, justifications are required. When no good justification is provided, changes to the code are required.</xsl:variable>
    <xsl:variable name="synthese.asterixdouble">** Computed from rules agreed upon by both the auditor and </xsl:variable>
    <xsl:variable name="synthese.nomclient">CLIENTNAME</xsl:variable>
    <xsl:variable name="synthese.asterixtriple">*** Mark adjusted by auditor after thorough analysis.</xsl:variable>
    <xsl:variable name="synthese.remarqueLib">Remarks</xsl:variable>
    <xsl:variable name="synthese.remarqueVal">REMARKS IF NEEDED  example DISCARDED RULES</xsl:variable>
    <xsl:variable name="synthese.volumetrie">Volumetry</xsl:variable>
    <xsl:variable name="synthese.tableau.volumetrie"> : Volumetry</xsl:variable>
    <xsl:variable name="synthese.nbclasse">Number of classes and interfaces</xsl:variable>
    <xsl:variable name="synthese.nbmethod">Number of methods</xsl:variable>
    <xsl:variable name="synthese.nblignecode">Number of code lines</xsl:variable>
    <xsl:variable name="synthese.txcomment">Process commenting rate (comments inside methods)</xsl:variable>
    <xsl:variable name="synthese.txcodecomplexdest">Complex and unstructured code rate</xsl:variable>
    <xsl:variable name="synthese.repartition.title">Methods dispatching according to their complexity and unstructuredness</xsl:variable>
    <xsl:variable name="synthese.repartition.lib"> : Methods dispatching according to their complexity and unstructuredness</xsl:variable>
    <xsl:variable name="synthese.ifpug">Number of function points</xsl:variable>

</xsl:stylesheet>
