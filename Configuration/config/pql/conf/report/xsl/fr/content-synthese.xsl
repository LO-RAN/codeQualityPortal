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
    
    <xsl:variable name="synthese.title">Synthèse des objectifs qualité</xsl:variable>
    
    <xsl:variable name="synthese.resultat"> Résultats</xsl:variable>
    <xsl:variable name="synthese.figure">Illustration </xsl:variable>
    <xsl:variable name="synthese.figure.graph"> : Kiviat du système audité</xsl:variable>
    
    <xsl:variable name="synthese.tableau">Tableau </xsl:variable>
    <xsl:variable name="synthese.tableau.synthese"> : Synthèse des notes des objectifs</xsl:variable>
    
    <xsl:variable name="synthese.facteur">Objectif qualité</xsl:variable>
    <xsl:variable name="synthese.note">Note</xsl:variable>
    <xsl:variable name="synthese.tendance">Tendance</xsl:variable>
    <xsl:variable name="synthese.commentaireLib">Commentaires</xsl:variable>

    <xsl:variable name="general.nb">Nombre de</xsl:variable>

    <xsl:variable name="synthese.commentaireVal">COMMENTAIRES</xsl:variable>
    <xsl:variable name="synthese.asterix">* Pour des notes supérieures ou égales à 3 l’application est considérée acceptable. Pour des notes inférieures à 3 des justifications sont nécessaires ou, si aucune justification valable n’est trouvée, les modifications sont indispensables.</xsl:variable>
    <xsl:variable name="synthese.asterixdouble">** Calculée à partir des règles établies d’un commun accord entre l’auditeur et </xsl:variable>
    <xsl:variable name="synthese.nomclient">NOMDUCLIENT</xsl:variable>
    <xsl:variable name="synthese.asterixtriple">*** Note corrigée suite à l’analyse de l’auditeur.</xsl:variable>
    <xsl:variable name="synthese.remarqueLib">Remarques</xsl:variable>
    <xsl:variable name="synthese.remarqueVal">REMARQUES SI BESOINS  exemple DESACTIVATION DE REGLES</xsl:variable>
    <xsl:variable name="synthese.volumetrie">Volumétrie</xsl:variable>
    <xsl:variable name="synthese.tableau.volumetrie"> : Volumétrie</xsl:variable>
    <xsl:variable name="synthese.nbclasse">Nombre de classes et interfaces</xsl:variable>
    <xsl:variable name="synthese.nbmethod">Nombre de méthodes</xsl:variable>
    <xsl:variable name="synthese.nblignecode">Nombre de lignes de code</xsl:variable>
    <xsl:variable name="synthese.txcomment">Taux de commentaire de traitements (commentaire à l’intérieur des méthodes)</xsl:variable>
    <xsl:variable name="synthese.txcodecomplexdest">Pourcentage de code Complexe et Déstructuré</xsl:variable>
    <xsl:variable name="synthese.repartition.title">Répartition des méthodes en fonction de leur complexité et de leur déstructuration</xsl:variable>
    <xsl:variable name="synthese.repartition.lib"> : Répartition des méthodes en fonction de leur complexité et de leur déstructuration</xsl:variable>
    <xsl:variable name="synthese.ifpug">Nombre de points de fonctions</xsl:variable>

</xsl:stylesheet>
