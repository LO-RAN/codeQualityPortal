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
	
    <xsl:import href="../content-synthese-evolution.xsl"/>
    
    <xsl:variable name="evolution.title">Application evolution</xsl:variable>
    <xsl:variable name="evolution.title.volumetrie">Evolution's dispatching</xsl:variable>
    <xsl:variable name="evolution.volumetrie">: Evolution's dispatching</xsl:variable>
    <xsl:variable name="evolution.tableau">Table </xsl:variable>
    <xsl:variable name="evolution.figure">Figure </xsl:variable>
    <xsl:variable name="actionplan.evolution.degree.title">Degree of application of the previous action plan</xsl:variable>
    <xsl:variable name="actionplan.evolution.degree.corrected">Corrected criterions</xsl:variable>
    <xsl:variable name="actionplan.evolution.degree.partially">Partially corrected criterions</xsl:variable>
    <xsl:variable name="actionplan.evolution.degree.degraded">Degraded criterions</xsl:variable>
    <xsl:variable name="actionplan.evolution.degree.stable">Stable criterions</xsl:variable>

    <xsl:variable name="evolution.newandbad">New and problematic</xsl:variable>
    <xsl:variable name="evolution.oldworst">Existing and degraded</xsl:variable>
    <xsl:variable name="evolution.oldbetter">Existing and improved</xsl:variable>
    <xsl:variable name="evolution.oldbetterworst">Existing, both improved and degraded</xsl:variable>
    <xsl:variable name="evolution.stable">Existing and stable</xsl:variable>
    <xsl:variable name="evolution.oldbadstable">Existing, problematic and stable</xsl:variable>
    
    <xsl:variable name="evolution.repartition.crit">: Dispatching by criterion</xsl:variable>
    
    <xsl:variable name="evolution.repartition.newbad">Defects dispatching for new problematic elements</xsl:variable>
    <xsl:variable name="evolution.repartition.oldworst">Degradations dispatching</xsl:variable>
    <xsl:variable name="evolution.repartition.oldbetter">Improvements dispatching</xsl:variable>
    <xsl:variable name="evolution.repartition.oldbetterworst">Degradations dispatching for existing elements, both improved and degraded</xsl:variable>
    <xsl:variable name="evolution.repartition.oldstables">Defects dispatching for stable elements</xsl:variable>

    <xsl:variable name="evolution.nbElts.newandbad">Number of new problematic elements: </xsl:variable>
    <xsl:variable name="evolution.nbElts.oldworst">Number of existing degraded elements: </xsl:variable>
    <xsl:variable name="evolution.nbElts.oldbetter">Number of existing improved elements: </xsl:variable>
    <xsl:variable name="evolution.nbElts.oldbetterworst">Number of existing, improved or degraded, elements: </xsl:variable>
    <xsl:variable name="evolution.nbElts.oldbadstable">Number of problematic and stable elements: </xsl:variable>

    <xsl:variable name="evolution.goal.goal">Goals</xsl:variable>
    <xsl:variable name="evolution.goal.note">Mark</xsl:variable>
    <xsl:variable name="evolution.goal.evolution">Evolution</xsl:variable>
    <xsl:variable name="evolution.goal.title">Goal's evolution</xsl:variable>
    <xsl:variable name="evolution.eltstable.eltdesc">Elements</xsl:variable>
    <xsl:variable name="evolution.eltstable.criteres">Criterions</xsl:variable>
</xsl:stylesheet>
