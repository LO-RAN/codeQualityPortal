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

	<xsl:variable name="actionplan.title.saved">Actions plan</xsl:variable>
	<xsl:variable name="actionplan.title.recommended">Recommended actions plan</xsl:variable>

	<xsl:variable name="actionplan.criteriontable.col01.title">Name</xsl:variable>
	<xsl:variable name="actionplan.criteriontable.col02.title">Number of impacted elements</xsl:variable>
	<xsl:variable name="actionplan.criteriontable.col03.title">Cost</xsl:variable>
	<xsl:variable name="actionplan.criteriontable.col04.title">Percentage in total cost</xsl:variable>
	<xsl:variable name="actionplan.notsaved.p1">Because no actions plan has been defined, the five criterions which impact the most the quality have been chosen to establish the actions plan.</xsl:variable>
	<xsl:variable name="actionplan.notsaved.p2">Those criterions are selected regarding the number of elements to correct and their impact over the quality, allowing a maximization of quality improvements and a minimization of correction efforts.</xsl:variable>
	<xsl:variable name="actionplan.cost.title">Actions plan cost</xsl:variable>
	<xsl:variable name="actionplan.cost.shortTerm.desc">The following table lists the criterions included in the actions plan and their cost, expressed in "work unit". Those criterions are targetted for a short term correction, preferably for the next analysis.</xsl:variable>
	<xsl:variable name="actionplan.cost.mediumTerm.desc">The following table lists the criterions included in the actions plan and their cost, expressed in "work unit". Those criterions are targetted for a medium term correction.</xsl:variable>
	<xsl:variable name="actionplan.cost.longTerm.desc">The following table lists the criterions included in the actions plan and their cost, expressed in "work unit". Those criterions are targetted for a long term correction.</xsl:variable>
	<xsl:variable name="actionplan.cost.cost.calcul2">The global actions plan&apos;s cost (including development and unit testing), expressed as "work unit", is equal to </xsl:variable>
	<xsl:variable name="actionplan.cost.shortcost.calcul2">The actions plan&apos;s cost, at short term, is  </xsl:variable>
	<xsl:variable name="actionplan.cost.mediumcost.calcul2">The actions plan&apos;s cost, at medium term, is  </xsl:variable>
	<xsl:variable name="actionplan.cost.longcost.calcul2">The actions plan&apos;s cost, at long term, is  </xsl:variable>
	<xsl:variable name="actionplan.kiviat.title">Simulation of the result obtained after application of the actions plan</xsl:variable>
	<xsl:variable name="actionplan.kiviat.desc">The following kiviat permits to know the marks which every goal will obtain once the actions plan will be executed. This simulation takes into consideration that, after execution of the actions plan, concerned criterions won't have any non acceptable element. The simulated mark for a criterion is the minimal mark for acceptation it can obtain. A criterion having formulas giving mark of 3 and 4 will have a simulated mark of 3 whereas a criterion having no formula giving mark of 3 will have a simulated mark of 4.</xsl:variable>
	<xsl:variable name="actionplan.kiviat.img.title"> : Simulated Kiviat of the actions plan</xsl:variable>
	<xsl:variable name="actionplan.kiviat.img.titlecml"> : Simulated Kiviat of the actions plan for short, medium and long run</xsl:variable>
	<xsl:variable name="actionplan.kiviat.img.titlecm"> : Simulated Kiviat of the actions plan for short and medium run</xsl:variable>
	<xsl:variable name="actionplan.kiviat.img.titlec"> : Simulated Kiviat of the actions plan for short run</xsl:variable>

	<xsl:variable name="actionplan.no.title">Actions Plan</xsl:variable>
	<xsl:variable name="actionplan.no.txt01"></xsl:variable>
	<xsl:variable name="actionplan.no.expl01"></xsl:variable>
	<xsl:variable name="actionplan.no.expl02"></xsl:variable>
	<xsl:variable name="actionplan.no.expl03"></xsl:variable>
	<xsl:variable name="actionplan.no.list01">Action 1 : explanation, paragraph reference</xsl:variable>
	<xsl:variable name="actionplan.no.list02">Action 2 : explanation, paragraph reference</xsl:variable>
	
	<xsl:variable name="caqs.actionplan.impression.shortTerm">Criterions which need to be corrected at short term</xsl:variable>
	<xsl:variable name="caqs.actionplan.impression.mediumTerm">Criterions which need to be corrected at medium term</xsl:variable>
	<xsl:variable name="caqs.actionplan.impression.longTerm">Criterions which need to be corrected at long term</xsl:variable>
	<xsl:variable name="illustration">Figure</xsl:variable>
	<xsl:variable name="actionplan.comment">Comment</xsl:variable>
	<xsl:variable name="actionplan.commentList">Other comments</xsl:variable>
	<xsl:variable name="actionplan.globalCost">Global cost</xsl:variable>
	<xsl:variable name="actionplan.uo">Work units</xsl:variable>
	<xsl:variable name="actionplan.uo.explanation">This list permits to convert units defined as correction cost per criterion in another unit. Therefore, a unit means :</xsl:variable>
</xsl:stylesheet>
