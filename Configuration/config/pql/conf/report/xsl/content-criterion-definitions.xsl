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
		  <xsl:apply-templates select="CRITERION"/>
		</content>
	</xsl:template>
	<xsl:template match="CRITERION">
		<text:h text:style-name="Heading_20_2" text:outline-level="2">
			<xsl:value-of select="@lib"/>
		</text:h>
		<text:h text:style-name="Heading_20_3" text:outline-level="3">
      <xsl:value-of select="$definition.definition"/>
    </text:h>
    	<text:p text:style-name="Text_20_body"><xsl:value-of select="@desc"/></text:p>
	<xsl:if test="@compl != ''">
		<text:h text:style-name="Heading_20_3" text:outline-level="3">
        <xsl:value-of select="$definition.complement"/>
        </text:h>
	  <text:p text:style-name="Text_20_body"><xsl:value-of select="@compl"/></text:p>
	</xsl:if>
		<text:h text:style-name="Heading_20_3" text:outline-level="3">
      <xsl:value-of select="$definition.aggregation"/>
    </text:h>
		<text:p text:style-name="Text_20_body"></text:p>
		<table:table table:name="TableauRegleCritere" table:style-name="TableauRegleCritere">
			<table:table-column table:style-name="TableauRegleCritere.A"/>
			<table:table-column table:style-name="TableauRegleCritere.B"/>
			<table:table-row table:style-name="TableauRegleCritere.1">
				<table:table-cell table:style-name="TableauRegleCritere.A1" office:value-type="string">
					<text:p text:style-name="P48">
					 <xsl:value-of select="$definition.typeelement"/>
					</text:p>
				</table:table-cell>
				<table:table-cell table:style-name="TableauRegleCritere.B1" office:value-type="string">
					<text:p text:style-name="P48">
            <xsl:value-of select="$definition.typeaggregation"/>
          </text:p>
				</table:table-cell>
			</table:table-row>
		<xsl:apply-templates select="AGGREGATION"/>
		</table:table>
		<text:h text:style-name="Heading_20_3" text:outline-level="3">
      <xsl:value-of select="$definition.reglecalcul"/>
    </text:h>
		<text:p text:style-name="Text_20_body"></text:p>
		<table:table table:name="TableauRegleCritere" table:style-name="TableauRegleCritere">
			<table:table-column table:style-name="TableauRegleCritere.A"/>
			<table:table-column table:style-name="TableauRegleCritere.B"/>
			<table:table-row table:style-name="TableauRegleCritere.1">
				<table:table-cell table:style-name="TableauRegleCritere.A1" office:value-type="string">
					<text:p text:style-name="P48">
					 <xsl:value-of select="$definition.regle"/>
					</text:p>
				</table:table-cell>
				<table:table-cell table:style-name="TableauRegleCritere.B1" office:value-type="string">
					<text:p text:style-name="P48">
            <xsl:value-of select="$definition.valeur"/>
          </text:p>
				</table:table-cell>
			</table:table-row>
  	  <xsl:apply-templates select="FORMULA"/>
		</table:table>
		<xsl:if test="count(//COSTFORMULA) &gt; 0" >
			<text:h text:style-name="Heading_20_3" text:outline-level="3">
				<xsl:value-of select="$definition.costformula"/>
			</text:h>
			<text:p text:style-name="Text_20_body"><xsl:value-of select="$definition.costformula.explanation"/></text:p><text:p text:style-name="Text_20_body"><xsl:value-of select="COSTFORMULA/RULE"/></text:p>
		</xsl:if>
	</xsl:template>
	
	<xsl:template match="FORMULA">
			<table:table-row table:style-name="TableauRegleCritere.1">
				<table:table-cell table:style-name="TableauRegleCritere.A2" office:value-type="string">
				  <text:p text:style-name="P42">
				    <xsl:choose>
					  <xsl:when test="RULE='true'">
					    <xsl:value-of select="$definition.formula.true"/>
					  </xsl:when>
					  <xsl:otherwise>
					    <xsl:value-of select="RULE"/>
  					  </xsl:otherwise>
				    </xsl:choose>
				  </text:p>
				</table:table-cell>
				<table:table-cell table:style-name="TableauRegleCritere.B2" office:value-type="string">
					<text:p text:style-name="P42">
						<xsl:value-of select="@value"/>
					</text:p>
				</table:table-cell>
			</table:table-row>
  </xsl:template>	
  
	<xsl:template match="AGGREGATION">
			<table:table-row table:style-name="TableauRegleCritere.1">
				<table:table-cell table:style-name="TableauRegleCritere.A2" office:value-type="string">
					<text:p text:style-name="P42">
              <xsl:choose>
                <xsl:when test="@typeElt='CLS'">
                  <xsl:value-of select="$definition.type.classe"/>
                </xsl:when>
                <xsl:when test="@typeElt='MET'">
                  <xsl:value-of select="$definition.type.method"/>
                </xsl:when>
                <xsl:when test="@typeElt='EA'">
                  <xsl:value-of select="$definition.type.ea"/>
                </xsl:when>
                <xsl:otherwise>
                  <xsl:value-of select="$definition.type.all"/>
                </xsl:otherwise>
              </xsl:choose>
				  </text:p>
				</table:table-cell>
				<table:table-cell table:style-name="TableauRegleCritere.B2" office:value-type="string">
					<text:p text:style-name="P42">
              <xsl:choose>
                <xsl:when test="@id = 'AVG'">
                  <xsl:value-of select="$definition.aggreg.avg"/>
                </xsl:when>
                <xsl:when test="@id = 'EXCLUS'">
                  <xsl:value-of select="$definition.aggreg.exclus"/>
                </xsl:when>
                <xsl:when test="@id = 'AVG_ALL'">
                  <xsl:value-of select="$definition.aggreg.avg_all"/>
                </xsl:when>
                <xsl:when test="@id = 'EXCLUS_AVG'">
                  <xsl:value-of select="$definition.aggreg.exclus_avg"/>
                </xsl:when>
                <xsl:when test="@id = 'EXCLUS_AVG_SEUIL'">
                  <xsl:value-of select="$definition.aggreg.exclus_avg_seuil"/>
                </xsl:when>
                <xsl:when test="@id = 'AVG_WEIGHT'">
                  <xsl:value-of select="$definition.aggreg.avg_weight"/>
                </xsl:when>
                <xsl:when test="@id = 'MULTI_SEUIL'">
                  <xsl:value-of select="$definition.aggreg.multi_seuil"/>
                </xsl:when>
                <xsl:otherwise>
                  <xsl:value-of select="$definition.aggreg.autre"/>
                </xsl:otherwise>
              </xsl:choose>
					</text:p>
				</table:table-cell>
			</table:table-row>
  </xsl:template>	
</xsl:stylesheet>
