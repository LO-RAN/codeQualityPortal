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
    
    <xsl:variable name="evolution.title">Evolution de l'application</xsl:variable>
    <xsl:variable name="evolution.title.volumetrie">Répartition de l'évolution</xsl:variable>
    <xsl:variable name="evolution.volumetrie">: Répartition de l'évolution</xsl:variable>
    <xsl:variable name="evolution.tableau">Tableau </xsl:variable>
    <xsl:variable name="evolution.figure">Illustration </xsl:variable>
    <xsl:variable name="actionplan.evolution.degree.title">Degré d'application du plan d'action précédent</xsl:variable>
    <xsl:variable name="actionplan.evolution.degree.corrected">Critères corrigés</xsl:variable>
    <xsl:variable name="actionplan.evolution.degree.partially">Critères partiellement corrigés</xsl:variable>
    <xsl:variable name="actionplan.evolution.degree.degraded">Critères dégradés</xsl:variable>
    <xsl:variable name="actionplan.evolution.degree.stable">Critères stables</xsl:variable>
    
    <xsl:variable name="evolution.newandbad">Nouveaux et problématiques</xsl:variable>
    <xsl:variable name="evolution.oldworst">Existants et dégradés</xsl:variable>
    <xsl:variable name="evolution.oldbetter">Existants et améliorés</xsl:variable>
    <xsl:variable name="evolution.oldbetterworst">Existants, améliorés et dégradés</xsl:variable>
    <xsl:variable name="evolution.stable">Existants et stables</xsl:variable>
    <xsl:variable name="evolution.oldbadstable">Existants, problématiques et stables</xsl:variable>
    
    <xsl:variable name="evolution.repartition.crit">: Répartition par critère</xsl:variable>
    
    <xsl:variable name="evolution.repartition.newbad">Répartition des défauts des nouveaux éléments problématiques</xsl:variable>
    <xsl:variable name="evolution.repartition.oldworst">Répartition des dégradations</xsl:variable>
    <xsl:variable name="evolution.repartition.oldbetter">Répartition des améliorations</xsl:variable>
    <xsl:variable name="evolution.repartition.oldbetterworst">Répartition des dégradations pour les éléments améliorés et dégradés</xsl:variable>
    <xsl:variable name="evolution.repartition.oldstables">Répartition des défauts des éléments stables problématiques</xsl:variable>

    <xsl:variable name="evolution.nbElts.newandbad">Nombre d'éléments nouveaux et problématiques : </xsl:variable>
    <xsl:variable name="evolution.nbElts.oldworst">Nombre d'éléments existants et dégradés : </xsl:variable>
    <xsl:variable name="evolution.nbElts.oldbetter">Nombre d'éléments existants et améliorés : </xsl:variable>
    <xsl:variable name="evolution.nbElts.oldbetterworst">Nombre d'éléments existants, améliorés et dégradés : </xsl:variable>
    <xsl:variable name="evolution.nbElts.oldbadstable">Nombre d'éléments existants, problématiques et stables : </xsl:variable>

    <xsl:variable name="evolution.goal.goal">Objectifs</xsl:variable>
    <xsl:variable name="evolution.goal.note">Note</xsl:variable>
    <xsl:variable name="evolution.goal.evolution">Evolution</xsl:variable>
    <xsl:variable name="evolution.goal.title">Evolution des objectifs</xsl:variable>
    <xsl:variable name="evolution.eltstable.eltdesc">Eléments</xsl:variable>
    <xsl:variable name="evolution.eltstable.criteres">Critères</xsl:variable>
</xsl:stylesheet>
