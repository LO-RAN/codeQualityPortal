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
    
    <xsl:variable name="bottomup.amelioration.title">Améliorations à réaliser</xsl:variable>
    <xsl:variable name="bottomup.amelioration.volumetrie">Volumétrie</xsl:variable>
    <xsl:variable name="bottomup.amelioration.tableau">Tableau </xsl:variable>
    
    <xsl:variable name="bottomup.repartitionelement.title">Analyse de la répartition des éléments</xsl:variable>
    <xsl:variable name="bottomup.amelioration.repartitionparniveau"> : Répartition des éléments par état d&apos;acceptabilité</xsl:variable>
    <xsl:variable name="bottomup.amelioration.nbrejet">Nombre d'éléments rejetés</xsl:variable>
    <xsl:variable name="bottomup.amelioration.nbrejetexplain">– au moins une note entre 1 et 2</xsl:variable>
    <xsl:variable name="bottomup.amelioration.nbreserve">Nombre d'éléments acceptables avec réserve</xsl:variable>
    <xsl:variable name="bottomup.amelioration.nbreserveexplain">– au moins une note entre 2 et 3 (mais pas de note à 1)</xsl:variable>
    <xsl:variable name="bottomup.amelioration.nbaccepte">Nombre d'éléments acceptés</xsl:variable>
    <xsl:variable name="bottomup.amelioration.nbaccepteexplain">– au moins une note entre 3 et 4 (mais pas de note à 1, ni à 2)</xsl:variable>
    <xsl:variable name="bottomup.amelioration.pctamelior">Pourcentage d'éléments à améliorer</xsl:variable>
    
    
    <xsl:variable name="bottomup.repartitiondefaut.title">Analyse de la répartition des défauts</xsl:variable>
    <xsl:variable name="bottomup.amelioration.repartitionparseverite"> : Répartition des défauts par sévérité</xsl:variable>
    <xsl:variable name="bottomup.amelioration.nbameliornv1">Nombre d'améliorations de niveau 1</xsl:variable>
    <xsl:variable name="bottomup.amelioration.nbameliornv2">Nombre d'améliorations de niveau 2</xsl:variable>
    <xsl:variable name="bottomup.amelioration.nbtotalamelior">Nombre total d'améliorations à réaliser</xsl:variable>

    
    <xsl:variable name="bottomup.amelioration.ameliorparobjectif">Répartition des défauts par objectif</xsl:variable>
    <xsl:variable name="bottomup.amelioration.figure">Illustration </xsl:variable>
    <xsl:variable name="bottomup.amelioration.repartitionameliorparobj"> : Répartition des défauts par objectif</xsl:variable>
    <xsl:variable name="bottomup.amelioration.ameliorparcritere">Répartition des défauts par critère</xsl:variable>
    <xsl:variable name="bottomup.amelioration.repartitionameliorparcrit"> : Répartition des défauts par critère</xsl:variable>

</xsl:stylesheet>
