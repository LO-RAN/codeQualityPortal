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
    
    <xsl:variable name="definition.definition">Définition</xsl:variable>
    <xsl:variable name="definition.complement">Complément</xsl:variable>
    <xsl:variable name="definition.aggregation">Agrégation</xsl:variable>
    <xsl:variable name="definition.typeelement">Type d'élément</xsl:variable>
    <xsl:variable name="definition.typeaggregation">Type d'agrégation</xsl:variable>
    <xsl:variable name="definition.reglecalcul">Règle de calcul</xsl:variable>
    <xsl:variable name="definition.regle">Formule</xsl:variable>
    <xsl:variable name="definition.valeur">Note de</xsl:variable>
    <xsl:variable name="definition.type.classe">Classe</xsl:variable>
    <xsl:variable name="definition.type.method">Méthode</xsl:variable>
    <xsl:variable name="definition.type.ea">Entité applicative</xsl:variable>
    <xsl:variable name="definition.type.all">Tous</xsl:variable>
    <xsl:variable name="definition.aggreg.avg">Moyenne pondérée</xsl:variable>
    <xsl:variable name="definition.aggreg.exclus">Exclusion</xsl:variable>
    <xsl:variable name="definition.aggreg.avg_all">Moyenne pondérée sur tous les éléments</xsl:variable>
    <xsl:variable name="definition.aggreg.avg_weight">Moyenne pondérée par la note</xsl:variable>
    <xsl:variable name="definition.aggreg.exclus_avg">Moyenne pondérée avec exclusion</xsl:variable>
    <xsl:variable name="definition.aggreg.exclus_avg_seuil">Moyenne pondérée exclusive avec seuil</xsl:variable>
    <xsl:variable name="definition.aggreg.multi_seuil">Note selon seuil d'exclusion</xsl:variable>
    <xsl:variable name="definition.aggreg.autre">Autre</xsl:variable>
	<xsl:variable name="definition.formula.true">Sinon</xsl:variable>
	<xsl:variable name="definition.costformula">Formule de calcul du coût de correction</xsl:variable>
	<xsl:variable name="definition.costformula.explanation">Cette formule de calcul sera appliquée sur chaque élément problématique pour en estimer le coût de correction : </xsl:variable>
</xsl:stylesheet>
