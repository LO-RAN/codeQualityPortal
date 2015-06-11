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
	
    <xsl:import href="../content-justifications.xsl"/>
	
	<xsl:variable name="justifications.title">Liste des justifications</xsl:variable>
	<xsl:variable name="justifications.title.cv">Liste des justifications de critères validées</xsl:variable>
	<xsl:variable name="justifications.title.cr">Liste des justifications de critères refusées</xsl:variable>
	<xsl:variable name="justifications.title.cw">Liste des justifications de critères en attente de validation</xsl:variable>
	<xsl:variable name="justifications.title.fv">Liste des justifications d'objectifs validées</xsl:variable>
	<xsl:variable name="justifications.title.fr">Liste des justifications d'objectifs refusées</xsl:variable>
	<xsl:variable name="justifications.title.fw">Liste des justifications d'objectifs en attente de validation</xsl:variable>
	<xsl:variable name="justifications.table.author">Auteur</xsl:variable>
	<xsl:variable name="justifications.table.justVal">Commentaire de la validation</xsl:variable>
	<xsl:variable name="justifications.table.justRef">Commentaire du refus</xsl:variable>
	<xsl:variable name="justifications.table.justDem">Commentaire de la demande</xsl:variable>
	<xsl:variable name="justifications.table.critLib">Critère</xsl:variable>
	<xsl:variable name="justifications.table.factLib">Objectif</xsl:variable>
	<xsl:variable name="justifications.table.eltLib">Elément justifié</xsl:variable>
	<xsl:variable name="justifications.table.telt">Type de l'élément</xsl:variable>
	<xsl:variable name="justifications.table.oldMark">Note</xsl:variable>
	<xsl:variable name="justifications.table.newMark">Note Justifiée</xsl:variable>

</xsl:stylesheet>
