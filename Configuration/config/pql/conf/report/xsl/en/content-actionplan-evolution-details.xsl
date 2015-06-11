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
	
    <xsl:import href="../content-actionplan-evolution-details.xsl"/>
    
    <xsl:variable name="actionplan.evolution.title">Application's evolution regarding the previous actions plan</xsl:variable>
    <xsl:variable name="actionplan.evolution.kiviats.title">Kiviats</xsl:variable>
    <xsl:variable name="actionplan.evolution.kiviats.previousKiviat">Previous analysis' kiviat</xsl:variable>
    <xsl:variable name="actionplan.evolution.kiviats.previousActionPlanKiviat">Kiviat resulting of the previous actions plan execution</xsl:variable>
    <xsl:variable name="actionplan.evolution.kiviats.newKiviat">Current analysis' kiviat</xsl:variable>
    
    <xsl:variable name="actionplan.evolution.criterions.title">Degree of application of the previous actions plan</xsl:variable>
    <xsl:variable name="actionplan.evolution.criterions.corrected.title">Corrected criterions</xsl:variable>
    <xsl:variable name="actionplan.evolution.criterions.stable.title">Stables criterions</xsl:variable>
    <xsl:variable name="actionplan.evolution.criterions.partiallycorrected.title">Partially corrected criterions</xsl:variable>
    <xsl:variable name="actionplan.evolution.criterions.degraded.title">Degraded criterions</xsl:variable>

    <xsl:variable name="actionplan.evolution.criterions.corrected.expl">This section lists all criterions included in the previous actions plan which no more have problematic elements. For each criterion, only elements corrected since the previous analysis are listed.</xsl:variable>
    <xsl:variable name="actionplan.evolution.criterions.stable.expl">This section lists all criterions included in the previous actions plan and for which the number of problematic elements has not changed since the previous analysis. For each criterion, only elements stable and problematic since the previous analysis are listed.</xsl:variable>
    <xsl:variable name="actionplan.evolution.criterions.partiallycorrected.expl">This section lists all criterions included in the previous actions plan and for which the number of problematic elements has lowered since the previous analysis. For each criterion, only elements improved but still problematic since the previous analysis are listed.</xsl:variable>
    <xsl:variable name="actionplan.evolution.criterions.degraded.expl">This section lists all criterions included in the previous actions plan and for which the number of problematic elements has raised since the previous analysis. For each criterion, only elements (problematic or not) degraded since the previous analysis are listed.</xsl:variable>

    <xsl:variable name="actionplan.evolution.criterions.elements">Elements</xsl:variable>
</xsl:stylesheet>
