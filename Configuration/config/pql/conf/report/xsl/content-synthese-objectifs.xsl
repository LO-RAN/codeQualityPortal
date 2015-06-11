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
	
	<xsl:template name="INSERT_TENDANCE_PICTURE">
    <xsl:param name="tendance" />
        <draw:frame draw:style-name="fr1" draw:name="Cadre7" text:anchor-type="as-char" svg:width="0.36cm" svg:height="0.36cm" draw:z-index="3">
  			   <xsl:choose>
              <xsl:when test="@tendance = 'up'">
                <draw:image xlink:href="Pictures/note_up.gif" xlink:type="simple" xlink:show="embed" xlink:actuate="onLoad"/>
  				    </xsl:when>
              <xsl:when test="@tendance = 'down'">
                <draw:image xlink:href="Pictures/note_down.gif" xlink:type="simple" xlink:show="embed" xlink:actuate="onLoad"/>
  				    </xsl:when>
              <xsl:when test="@tendance = 'unchanged'">
                <draw:image xlink:href="Pictures/note_unchanged.gif" xlink:type="simple" xlink:show="embed" xlink:actuate="onLoad"/>
  				    </xsl:when>
              <xsl:otherwise>
                <draw:image xlink:href="Pictures/note_none.gif" xlink:type="simple" xlink:show="embed" xlink:actuate="onLoad"/>
              </xsl:otherwise>
            </xsl:choose>
        </draw:frame>
  </xsl:template>
	
	<xsl:template match="REPORT">
		<content>
		  <xsl:apply-templates select="FACTOR"/>
		</content>
	</xsl:template>
	
	
	<xsl:template match="FACTOR">
		<text:h text:style-name="Heading_20_1" text:outline-level="1">
			<xsl:value-of select="@lib"/>
		</text:h>
		<text:p text:style-name="Text_20_body"><xsl:value-of select="@desc"/></text:p>
		<text:h text:style-name="Heading_20_2" text:outline-level="2"><xsl:value-of select="$synthobj.synthese"/></text:h>
		<text:p text:style-name="Standard"/>
		<table:table table:name="TableauSynthObjectifs" table:style-name="TableauSynthObjectifs">
			<table:table-column table:style-name="TableauSynthObjectifs.A"/>
			<table:table-column table:style-name="TableauSynthObjectifs.B"/>
			<table:table-column table:style-name="TableauSynthObjectifs.B"/>
			<table:table-column table:style-name="TableauSynthObjectifs.C"/>
			<table:table-column table:style-name="TableauSynthObjectifs.C"/>
			<table:table-column table:style-name="TableauSynthObjectifs.C"/>
			<table:table-column table:style-name="TableauSynthObjectifs.C"/>
			<xsl:if test="@hasTendance = 'true'">
			<table:table-column table:style-name="TableauSynthObjectifs.B"/>
			</xsl:if>
			<table:table-row table:style-name="TableauSynthObjectifs.1">
				<table:table-cell table:style-name="TableauSynthObjectifs.A1" office:value-type="string">
					<text:p text:style-name="P48">
            <xsl:value-of select="$synthobj.critere"/>
					</text:p>
				</table:table-cell>
				<table:table-cell table:style-name="TableauSynthObjectifs.B1" office:value-type="string">
					<text:p text:style-name="P48">
            <xsl:value-of select="$synthobj.note"/>
          </text:p>
				</table:table-cell>
				<table:table-cell table:style-name="TableauSynthObjectifs.B1" office:value-type="string">
					<text:p text:style-name="P48">
            <xsl:value-of select="$synthobj.poids"/>
          </text:p>
				</table:table-cell>
				<table:table-cell table:style-name="TableauSynthObjectifs.C1" office:value-type="string">
					<text:p text:style-name="P48">
            <xsl:value-of select="$synthobj.note1"/>
          </text:p>
				</table:table-cell>
				<table:table-cell table:style-name="TableauSynthObjectifs.C1" office:value-type="string">
					<text:p text:style-name="P48">
            <xsl:value-of select="$synthobj.note2"/>
          </text:p>
				</table:table-cell>
				<table:table-cell table:style-name="TableauSynthObjectifs.C1" office:value-type="string">
					<text:p text:style-name="P48">
            <xsl:value-of select="$synthobj.note3"/>
          </text:p>
				</table:table-cell>
				<table:table-cell table:style-name="TableauSynthObjectifs.C1" office:value-type="string">
					<text:p text:style-name="P48">
            <xsl:value-of select="$synthobj.note4"/>
          </text:p>
				</table:table-cell>
				<xsl:if test="@hasTendance = 'true'">
				<table:table-cell table:style-name="TableauSynthObjectifs.B1" office:value-type="string">
					<text:p text:style-name="P48">
						<xsl:value-of select="$synthobj.tendance"/>
					</text:p>
				</table:table-cell>
				</xsl:if>
			</table:table-row>
			<xsl:for-each select="CRITERE">
				<table:table-row table:style-name="TableauSynthObjectifs.1">
					<table:table-cell table:style-name="TableauSynthObjectifs.A2" office:value-type="string">
						<text:p text:style-name="P42">
								<!--<text:a>
									<xsl:attribute name="xlink:type" >simple</xsl:attribute>
									<xsl:attribute name="xlink:href" >#.<xsl:value-of select="@lib"/>|outline</xsl:attribute>
									<xsl:value-of select="@lib"/>
								</text:a>-->
								<xsl:value-of select="@lib"/>
					  </text:p>
					</table:table-cell>
					<table:table-cell table:style-name="TableauSynthObjectifs.B2" office:value-type="string">
						<text:p text:style-name="P42Right">
							<xsl:value-of select="@note"/>
						</text:p>
					</table:table-cell>
					<table:table-cell table:style-name="TableauSynthObjectifs.B2" office:value-type="string">
						<text:p text:style-name="P42Right">
							<xsl:value-of select="@weight"/>
						</text:p>
					</table:table-cell>
					<xsl:apply-templates select="REPARTITION"/>
					<xsl:if test="@hasTendance = 'true'">
				    <table:table-cell table:style-name="TableauSynthObjectifs.B2" office:value-type="string">
						<text:p text:style-name="P35">
							<xsl:call-template name="INSERT_TENDANCE_PICTURE">
								<xsl:with-param name="tendance"><xsl:value-of select="@tendance"/></xsl:with-param>
							</xsl:call-template>
						</text:p>
					</table:table-cell>
					</xsl:if>
				</table:table-row>
			</xsl:for-each>
		</table:table>
		<text:p text:style-name="Inter"><xsl:value-of select="$synthobj.tableau"/><text:sequence text:ref-name="refTable3" text:name="Table" text:formula="ooow:Table+1" style:num-format="1">1</text:sequence><xsl:value-of select="$synthobj.tableausynthobj"/><xsl:value-of select="@lib"/></text:p>
		<text:h text:style-name="Heading_20_2" text:outline-level="3"><xsl:value-of select="$synthobj.commentaires"/></text:h>
		<xsl:if test="@comment != ''">
			<text:p text:style-name="F12">
				<xsl:value-of select="@comment"/>
			</text:p>
			<text:p text:style-name="Standard"/>
		</xsl:if>

		<xsl:for-each select="CRITERE[@comment != '']">
			<text:p text:style-name="F12">
				<text:span text:style-name="F12Bold">
					<xsl:value-of select="@lib"/>
				</text:span>
				<xsl:text> </xsl:text>:<xsl:text> </xsl:text><xsl:value-of select="@comment"/>
			</text:p>
			<text:p text:style-name="Standard"/>
		</xsl:for-each>
		<text:p text:style-name="P53"/>
	</xsl:template>
	
	<xsl:template match="REPARTITION">
      <xsl:choose>
        <xsl:when test="@value='0'">
		     <table:table-cell table:style-name="TableauSynthObjectifs.C2" office:value-type="string">
      			<text:p text:style-name="P42Right">-</text:p>
      		</table:table-cell>
        </xsl:when>
        <xsl:when test="@id='0'">
		     <table:table-cell table:style-name="TableauSynthObjectifs.C2RED" office:value-type="string">
      			<text:p text:style-name="P42Right">
      				<xsl:value-of select="@pct"/>%
      			</text:p>
      			<text:p text:style-name="P42Right">
      				<xsl:value-of select="@value"/>
      			</text:p>
      		</table:table-cell>
		    </xsl:when>
        <xsl:when test="@id='1'">
		     <table:table-cell table:style-name="TableauSynthObjectifs.C2YELLOW" office:value-type="string">
      			<text:p text:style-name="P42Right">
      				<xsl:value-of select="@pct"/>%
      			</text:p>
      			<text:p text:style-name="P42Right">
      				<xsl:value-of select="@value"/>
      			</text:p>
      		</table:table-cell>
		    </xsl:when>
        <xsl:when test="@id='2'">
		     <table:table-cell table:style-name="TableauSynthObjectifs.C2GREEN" office:value-type="string">
      			<text:p text:style-name="P42Right">
      				<xsl:value-of select="@pct"/>%
      			</text:p>
      			<text:p text:style-name="P42Right">
      				<xsl:value-of select="@value"/>
      			</text:p>
      		</table:table-cell>
		    </xsl:when>
        <xsl:when test="@id='3'">
		     <table:table-cell table:style-name="TableauSynthObjectifs.C2LIGHTGREEN" office:value-type="string">
      			<text:p text:style-name="P42Right">
      				<xsl:value-of select="@pct"/>%
      			</text:p>
      			<text:p text:style-name="P42Right">
      				<xsl:value-of select="@value"/>
      			</text:p>
      		</table:table-cell>
		    </xsl:when>
      </xsl:choose>
	</xsl:template>

</xsl:stylesheet>
