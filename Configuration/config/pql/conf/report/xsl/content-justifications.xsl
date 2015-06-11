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
	
	<xsl:strip-space elements="FACTOR_WAITING FACTOR_REFUSED FACTOR_VALIDATED CRITERION_REFUSED CRITERION_WAITING CRITERION_VALIDATED REPORT JUSTFICATIONS"/>
	
	<xsl:template match="JUSTIFICATIONS_LIST">
			<text:h text:style-name="P17" text:outline-level="2">			
				<xsl:choose>
			        <xsl:when test="@type='cv'">
			          <xsl:value-of select="$justifications.title.cv"/>
			        </xsl:when>
			        <xsl:when test="@type='cr'">
			          <xsl:value-of select="$justifications.title.cr"/>
			        </xsl:when>
			        <xsl:when test="@type='cw'">
			          <xsl:value-of select="$justifications.title.cw"/>
			        </xsl:when>
			        <xsl:when test="@type='fr'">
			          <xsl:value-of select="$justifications.title.fr"/>
			        </xsl:when>
			        <xsl:when test="@type='fw'">
			          <xsl:value-of select="$justifications.title.fw"/>
			        </xsl:when>
			        <xsl:when test="@type='fv'">
			          <xsl:value-of select="$justifications.title.fv"/>
			        </xsl:when>
				</xsl:choose>
			</text:h>					
			<text:p text:style-name="Standard"/>
			<table:table>
				<table:table-column />
				<table:table-column />
				<table:table-column />
				<table:table-column />
				<table:table-column />
				<table:table-column />
				<table:table-column />
				<table:table-row>
					<table:table-cell table:style-name="TableauJust.A1" office:value-type="string">
						<text:p text:style-name="P35"><xsl:value-of select="$justifications.table.author"/></text:p>
					</table:table-cell>
					<table:table-cell table:style-name="TableauJust.A1" office:value-type="string">
						<text:p text:style-name="P35">
							<xsl:choose>
						        <xsl:when test="(@type='cv') or (@type='fv')">
						          <xsl:value-of select="$justifications.table.justVal"/>
						        </xsl:when>
						        <xsl:when test="(@type='cr') or (@type='fr')">
						          <xsl:value-of select="$justifications.table.justRef"/>
						        </xsl:when>
						        <xsl:when test="(@type='cw') or (@type='fw')">
						          <xsl:value-of select="$justifications.table.justDem"/>
						        </xsl:when>
							</xsl:choose>
						</text:p>
					</table:table-cell>
					<table:table-cell table:style-name="TableauJust.A1" office:value-type="string">
						<text:p text:style-name="P35">
							<xsl:choose>
						        <xsl:when test="(@type='cv') or (@type='cr') or (@type='cw')">
						          <xsl:value-of select="$justifications.table.critLib"/>
						        </xsl:when>
						        <xsl:when test="(@type='fr') or (@type='fw') or (@type='fv')">
						          <xsl:value-of select="$justifications.table.factLib"/>
						        </xsl:when>
							</xsl:choose>
						</text:p>
					</table:table-cell>
					<table:table-cell table:style-name="TableauJust.A1" office:value-type="string">
						<text:p text:style-name="P35"><xsl:value-of select="$justifications.table.eltLib"/></text:p>
					</table:table-cell>
					<table:table-cell table:style-name="TableauJust.A1" office:value-type="string">
						<text:p text:style-name="P35"><xsl:value-of select="$justifications.table.telt"/></text:p>
					</table:table-cell>
					<table:table-cell table:style-name="TableauJust.A1" office:value-type="string">
						<text:p text:style-name="P35"><xsl:value-of select="$justifications.table.oldMark"/></text:p>
					</table:table-cell>
					<table:table-cell table:style-name="TableauJust.A1" office:value-type="string">
						<text:p text:style-name="P35"><xsl:value-of select="$justifications.table.newMark"/></text:p>
					</table:table-cell>
				</table:table-row>
				<xsl:for-each select="JUST">
					<table:table-row>
						<table:table-cell table:style-name="TableauJust.A2" office:value-type="string">
							<text:p text:style-name="P35"><xsl:value-of select="@author"/></text:p>
						</table:table-cell>
						<table:table-cell table:style-name="TableauJust.A2" office:value-type="string">
							<text:p text:style-name="P35"><xsl:value-of select="@justDesc"/></text:p>
						</table:table-cell>
						<table:table-cell table:style-name="TableauJust.A2" office:value-type="string">
							<text:p text:style-name="P35"><xsl:value-of select="@critFacLib"/></text:p>
						</table:table-cell>
						<table:table-cell table:style-name="TableauJust.A2" office:value-type="string">
							<text:p text:style-name="P35"><xsl:value-of select="@eltLib"/></text:p>
						</table:table-cell>
						<table:table-cell table:style-name="TableauJust.A2" office:value-type="string">
							<text:p text:style-name="P35"><xsl:value-of select="@eltType"/></text:p>
						</table:table-cell>
						<table:table-cell table:style-name="TableauJust.A2" office:value-type="string">
							<text:p text:style-name="P35"><xsl:value-of select="@oldMark"/></text:p>
						</table:table-cell>
						<table:table-cell table:style-name="TableauJust.A2" office:value-type="string">
							<text:p text:style-name="P35"><xsl:value-of select="@mark"/></text:p>
						</table:table-cell>
					</table:table-row>
				</xsl:for-each>
			</table:table>
	</xsl:template>
	
	
	<xsl:template match="REPORT">
		<content>
			<xsl:if test="JUSTIFICATIONS/@nb > 0">
				<text:h text:style-name="P59" text:outline-level="1">
					<xsl:value-of select="$justifications.title"/>
				</text:h>
				<text:p text:style-name="Standard"/>

				<xsl:apply-templates />
				
			</xsl:if>
		</content>
	</xsl:template>

</xsl:stylesheet>