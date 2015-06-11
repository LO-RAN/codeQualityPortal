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


    <xsl:import href="../content-synthese-actionplan.xsl"/>
	
	<xsl:variable name="actionplan.title.saved">Plan d&apos;actions</xsl:variable>
	<xsl:variable name="actionplan.title.recommended">Plan d&apos;actions préconisé</xsl:variable>

	<xsl:variable name="actionplan.criteriontable.col01.title">Libellé</xsl:variable>
	<xsl:variable name="actionplan.criteriontable.col02.title">Nombre d&apos;éléments impactés</xsl:variable>
	<xsl:variable name="actionplan.criteriontable.col03.title">Coût</xsl:variable>
	<xsl:variable name="actionplan.criteriontable.col04.title">Pourcentage du coût total</xsl:variable>
	<xsl:variable name="actionplan.notsaved.p1">Aucun plan d&apos;action n&apos;ayant été défini, les cinq critères impactant le plus la qualité sont retenus pour constituer le plan d&apos;action.</xsl:variable>
	<xsl:variable name="actionplan.notsaved.p2">Ces critères sont sélectionnés en fonction du nombre d&apos;éléments à corriger ainsi que de leur impact sur la qualité, permettant ainsi de maximiser les gains de qualité en minimisant les efforts de correction.</xsl:variable>
	<xsl:variable name="actionplan.cost.title">Coût du plan d&apos;action</xsl:variable>
	<xsl:variable name="actionplan.cost.shortTerm.desc">Le tableau suivant liste les critères inclus dans le plan d'actions ainsi que leur coût, exprimé en "unité d'œuvre". Ces critères sont déclarés comme devant être corrigés à court terme, de préférence pour la prochaine analyse.</xsl:variable>
	<xsl:variable name="actionplan.cost.mediumTerm.desc">Le tableau suivant liste les critères inclus dans le plan d'actions ainsi que leur coût, exprimé en "unité d'œuvre". Ces critères sont déclarés comme devant être corrigés à moyen terme.</xsl:variable>
	<xsl:variable name="actionplan.cost.longTerm.desc">Le tableau suivant liste les critères inclus dans le plan d'actions ainsi que leur coût, exprimé en "unité d'œuvre". Ces critères sont déclarés comme devant être corrigés à long terme.</xsl:variable>
	<xsl:variable name="actionplan.cost.cost.calcul2">Le coût global du plan d'actions (incluant développement et tests unitaires), exprimé en unités d'œuvre, est égal à </xsl:variable>
	<xsl:variable name="actionplan.cost.shortcost.calcul2">Le coût du plan d'actions, à court terme, est de  </xsl:variable>
	<xsl:variable name="actionplan.cost.mediumcost.calcul2">Le coût du plan d'actions, à moyen terme, est de  </xsl:variable>
	<xsl:variable name="actionplan.cost.longcost.calcul2">Le coût du plan d'actions, à long terme, est de  </xsl:variable>
	<xsl:variable name="actionplan.kiviat.title">Simulation du résultat de l'application du plan d'actions</xsl:variable>
	<xsl:variable name="actionplan.kiviat.desc">Le kiviat suivant permet de connaître les notes que chaque objectif obtiendra une fois le plan d'actions appliqué. Cette simulation considère qu'après application du plan d'actions, les critères concernés n'auront plus aucun élément non accepté. La note simulée attribuée au critère est la note minimale d'acceptabilité que le critère peut obtenir. Un critère ayant des formules attribuant des notes de 3 et de 4 aura une note simulée de 3 tandis qu'un critère n'ayant aucune formule attribuant une note de 3 aura une note simulée de 4.</xsl:variable>
	<xsl:variable name="actionplan.kiviat.img.titlecml"> : Kiviat simulé du plan d'actions sur les court, moyen et long termes</xsl:variable>
	<xsl:variable name="actionplan.kiviat.img.titlecm"> : Kiviat simulé du plan d'actions sur les court et moyen termes</xsl:variable>
	<xsl:variable name="actionplan.kiviat.img.titlec"> : Kiviat simulé du plan d'actions sur le court terme</xsl:variable>
	
	<xsl:variable name="actionplan.no.title">Plan d&apos;action préconisé</xsl:variable>
	<xsl:variable name="actionplan.no.txt01">Explication de la démarche : </xsl:variable>
	<xsl:variable name="actionplan.no.expl01">Le plan d&apos;action est établi en prenant en compte le nombre d&apos;éléments à corriger ainsi que l&apos;impact sur la qualité, permettant ainsi de maximiser les gains de qualité en minimisant les efforts de correction.</xsl:variable>
	<xsl:variable name="actionplan.no.expl02">Nous proposons ici la liste des principaux éléments permettant d’améliorer la qualité globale de l’application. L’ensemble des anomalies ainsi que leurs solutions génériques se trouve dans les parties critères du rapport.</xsl:variable>
	<xsl:variable name="actionplan.no.expl03">La démarche utilisée inclue une étape de justification après audit de code. Si aucune justification au non respect des règles n&apos;est apportée, alors la correction est nécessaire.</xsl:variable>
	<xsl:variable name="actionplan.no.list01">Action 1 : explication, référence au paragraphe</xsl:variable>
	<xsl:variable name="actionplan.no.list02">Action 2 : explication, référence au paragraphe</xsl:variable>
	
	<xsl:variable name="caqs.actionplan.impression.shortTerm">Critères à corriger à court terme</xsl:variable>
	<xsl:variable name="caqs.actionplan.impression.mediumTerm">Critères à corriger à moyen terme</xsl:variable>
	<xsl:variable name="caqs.actionplan.impression.longTerm">Critères à corriger à long terme</xsl:variable>
	<xsl:variable name="illustration">Illustration</xsl:variable>
	<xsl:variable name="actionplan.comment">Commentaire </xsl:variable>
	<xsl:variable name="actionplan.commentList">Autres commentaires</xsl:variable>
	<xsl:variable name="actionplan.globalCost">Coût global</xsl:variable>
	<xsl:variable name="actionplan.uo">Unités d'œuvre</xsl:variable>
	<xsl:variable name="actionplan.uo.explanation">Cette liste permet de convertir les unités d'œuvre définies comme coût de correction par critère en une autre unité. Ainsi, une unité d'œuvre vaut :</xsl:variable>
</xsl:stylesheet>