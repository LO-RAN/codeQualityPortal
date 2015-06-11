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
	
	<xsl:strip-space elements="CRITERE ACTIONPLAN REPORT ELEMENT"/>
	
	<xsl:template match="REPORT">
		<content>
			<xsl:if test="ACTIONPLAN/@saved = 'true'">
				<text:h text:style-name="Heading_20_1" text:outline-level="1">
					<xsl:value-of select="$actionplan.details.elementslist.title"/>
				</text:h>
				<text:p text:style-name="Standard"/>
				<xsl:for-each select="ACTIONPLAN/ELEMENTTYPE">
					<text:h text:style-name="Heading_20_2" text:outline-level="2">
						<xsl:value-of select="@lib"/>
					</text:h>
					<table:table>
						<table:table-column />
						<table:table-column />
						<table:table-row>
							<table:table-cell table:style-name="TableauDetailsActionPlan.B1" office:value-type="string">
								<text:p text:style-name="P35">
									<xsl:value-of select="$actionplan.details.elementslist.elements.title"/>
								</text:p>
							</table:table-cell>
							<table:table-cell table:style-name="TableauDetailsActionPlan.B1" office:value-type="string">
								<text:p text:style-name="P35">
									<xsl:value-of select="$actionplan.details.elementslist.criterions.title"/>
								</text:p>
							</table:table-cell>
						</table:table-row>
						<xsl:for-each select="ELEMENT">
							<table:table-row>
								<table:table-cell table:style-name="TableauDetailsActionPlan.B2" office:value-type="string">
									<text:p text:style-name="Standard">
										<xsl:value-of select="@desc"/>
									</text:p>
								</table:table-cell>
								<table:table-cell table:style-name="TableauDetailsActionPlan.B2" office:value-type="string">
									<xsl:for-each select="CRITERE">
										<text:p text:style-name="Standard">- <xsl:value-of select="@lib" /></text:p>
									</xsl:for-each>
								</table:table-cell>
							</table:table-row>
						</xsl:for-each>
					</table:table>
				</xsl:for-each>
			</xsl:if>
		</content>
	</xsl:template>

</xsl:stylesheet>
