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
	
	<xsl:strip-space elements="BASELINE CRITERE CRIT_DETAIL ELEMENT ELT FACTOR MET METRIQUES REPORT"/>
	
	<xsl:template match="REPORT">
		<content>
  		<text:h text:style-name="P59" text:outline-level="1">
  			<xsl:value-of select="$criteredetail.title"/> <xsl:value-of select="MODULE/@name"/>
  		</text:h>
		  <xsl:apply-templates select="CRIT_DETAIL"/>
		</content>
	</xsl:template>
	
	<xsl:template match="CRIT_DETAIL">
		<text:h text:style-name="Heading_20_2" text:outline-level="2">
			<xsl:value-of select="@lib"/>
		</text:h>
		<text:h text:style-name="Heading_20_3" text:outline-level="3">
      <xsl:value-of select="$criteredetail.definition"/>
    </text:h>
		<text:p text:style-name="Text_20_body"><xsl:value-of select="@desc"/></text:p>

    <xsl:if test="@compl != ''">
    			<text:h text:style-name="Heading_20_3" text:outline-level="3">
          <xsl:value-of select="$criteredetail.solution"/>
        </text:h>
    	<text:p text:style-name="Text_20_body"><xsl:value-of select="@compl"/></text:p>
    </xsl:if>
	
		<text:h text:style-name="Heading_20_3" text:outline-level="3">
      <xsl:value-of select="$criteredetail.violations"/>
	
  </text:h>
    
		<text:p text:style-name="Standard"/>
		<table:table table:name="TableauDetailCritere" table:style-name="TableauDetailCritere">
			<table:table-column table:style-name="TableauDetailCritere.A"/>
			<xsl:for-each select="METRIQUES/MET">
  			<table:table-column table:style-name="TableauDetailCritere.B"/>
			</xsl:for-each>
			<table:table-column table:style-name="TableauDetailCritere.B"/>
			<table:table-row table:style-name="TableauDetailCritere.1">
				<table:table-cell table:style-name="TableauDetailCritere.A1" office:value-type="string">
					<text:p text:style-name="P48">
					 <xsl:value-of select="$criteredetail.element"/>
					</text:p>
				</table:table-cell>
  			<xsl:for-each select="METRIQUES/MET">
  				<table:table-cell table:style-name="TableauDetailCritere.B1M" office:value-type="string">
  					<text:p text:style-name="P42"><xsl:value-of select="@lib"/></text:p>
  				</table:table-cell>
  			</xsl:for-each>
				<table:table-cell table:style-name="TableauDetailCritere.B1" office:value-type="string">
					<text:p text:style-name="P48">
            <xsl:value-of select="$criteredetail.note"/>
          </text:p>
				</table:table-cell>
			</table:table-row>
			<xsl:for-each select="ELT">
				<table:table-row table:style-name="TableauDetailCritere.1">
					<table:table-cell table:style-name="TableauDetailCritere.A2" office:value-type="string">
						<text:p text:style-name="P42">
								<xsl:value-of select="@desc"/>
					  </text:p>
					</table:table-cell>
    			<xsl:for-each select="MET">
  					<table:table-cell table:style-name="TableauDetailCritere.B2" office:value-type="string">
  						<text:p text:style-name="P42Right">
  							<xsl:value-of select="@value"/>
  						</text:p>
  						<xsl:if test="@lines!=''">
    						<text:p text:style-name="P42Right">
    							<xsl:value-of select="@lines"/>
    						</text:p>
    				  </xsl:if>
  					</table:table-cell>
    			</xsl:for-each>
					<table:table-cell table:style-name="TableauDetailCritere.B2" office:value-type="string">
						<text:p text:style-name="P42Right">
							<xsl:value-of select="@note"/>
						</text:p>
					</table:table-cell>
				</table:table-row>
			</xsl:for-each>
		</table:table>
		<text:p text:style-name="Inter"><xsl:value-of select="$criteredetail.tableau"/><text:sequence text:ref-name="refTable3" text:name="Table" text:formula="ooow:Table+1" style:num-format="1">1</text:sequence><xsl:value-of select="$criteredetail.tableau.violations"/><xsl:value-of select="@lib"/>
    </text:p>
	</xsl:template>
</xsl:stylesheet>
