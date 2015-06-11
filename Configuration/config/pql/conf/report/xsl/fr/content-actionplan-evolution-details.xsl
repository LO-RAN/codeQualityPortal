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
    
    <xsl:variable name="actionplan.evolution.title">Evolution de l'application selon le plan d'actions précédemment établi</xsl:variable>
    <xsl:variable name="actionplan.evolution.kiviats.title">Kiviats</xsl:variable>
    <xsl:variable name="actionplan.evolution.kiviats.previousKiviat">Kiviat de l'analyse précédente</xsl:variable>
    <xsl:variable name="actionplan.evolution.kiviats.previousActionPlanKiviat">Kiviat du plan d'actions établi pour l'analyse précédente</xsl:variable>
    <xsl:variable name="actionplan.evolution.kiviats.newKiviat">Kiviat obtenu pour cette analyse</xsl:variable>
    
    <xsl:variable name="actionplan.evolution.criterions.title">Degré d'application du plan d'actions précédent</xsl:variable>
    <xsl:variable name="actionplan.evolution.criterions.corrected.title">Critères corrigés</xsl:variable>
    <xsl:variable name="actionplan.evolution.criterions.stable.title">Critères stables</xsl:variable>
    <xsl:variable name="actionplan.evolution.criterions.partiallycorrected.title">Critères partiellement corrigés</xsl:variable>
    <xsl:variable name="actionplan.evolution.criterions.degraded.title">Critères dégradés</xsl:variable>

    <xsl:variable name="actionplan.evolution.criterions.corrected.expl">Cette section liste les critères inclus dans le plan d'actions défini pour l'analyse précédente et n'ayant plus d'éléments problématiques. Pour chaque critère sont listés les éléments corrigés depuis l'analyse précédente.</xsl:variable>
    <xsl:variable name="actionplan.evolution.criterions.stable.expl">Cette section liste les critères inclus dans le plan d'actions défini pour l'analyse précédente et ayant le même nombre d'éléments problématiques que pour l'analyse précédente. Pour chaque critère sont listés les éléments stables en anomalie depuis l'analyse précédente.</xsl:variable>
    <xsl:variable name="actionplan.evolution.criterions.partiallycorrected.expl">Cette section liste les critères inclus dans le plan d'actions défini pour l'analyse précédente et dont le nombre d'éléments problématiques a diminué depuis l'analyse précédente. Pour chaque critère sont listés les éléments améliorés mais toujours problématiques, les éléments corrigés et les éléments détériorés.</xsl:variable>
    <xsl:variable name="actionplan.evolution.criterions.degraded.expl">Cette section liste les critères inclus dans le plan d'actions défini pour l'analyse précédente et dont le nombre d'éléments problématiques a augmenté depuis l'analyse précédente. Pour chaque critère sont listés les éléments dégradés (problématiques ou non) depuis l'analyse précédente.</xsl:variable>

    <xsl:variable name="actionplan.evolution.criterions.elements">Eléments</xsl:variable>
</xsl:stylesheet>
