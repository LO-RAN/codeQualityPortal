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
		
				<text:p text:style-name="Standard"/>
				<text:h text:style-name="P47" text:outline-level="2">
          <xsl:value-of select="$bottomup.repartitiondefaut.title"/>
        </text:h>
				<table:table table:name="Tableau5" table:style-name="Tableau5">
					<table:table-column table:style-name="Tableau5.A"/>
					<table:table-column table:style-name="Tableau5.B"/>
					<table:table-row table:style-name="Tableau5.1">
						<table:table-cell table:style-name="Tableau5.A1" office:value-type="string">
							<text:p text:style-name="P48">
                <xsl:value-of select="$bottomup.amelioration.nbameliornv1"/>
              </text:p>
						</table:table-cell>
						<table:table-cell table:style-name="Tableau5.B1" office:value-type="string">
							<text:p text:style-name="P42Right">
							 <xsl:value-of select="VOLUMETRIE/NBAMELIOR/@nblevel1"/>
              </text:p>
						</table:table-cell>
					</table:table-row>
					<table:table-row table:style-name="Tableau5.1">
						<table:table-cell table:style-name="Tableau5.A1" office:value-type="string">
							<text:p text:style-name="P48">
                <xsl:value-of select="$bottomup.amelioration.nbameliornv2"/>
              </text:p>
						</table:table-cell>
						<table:table-cell table:style-name="Tableau5.B1" office:value-type="string">
							<text:p text:style-name="P42Right">
							 <xsl:value-of select="VOLUMETRIE/NBAMELIOR/@nblevel2"/>
              </text:p>
						</table:table-cell>
					</table:table-row>
					<table:table-row table:style-name="Tableau5.1">
						<table:table-cell table:style-name="Tableau5.A1" office:value-type="string">
							<text:p text:style-name="P50">
                <xsl:value-of select="$bottomup.amelioration.nbtotalamelior"/>
              </text:p>
						</table:table-cell>
						<table:table-cell table:style-name="Tableau5.B1" office:value-type="string">
							<text:p text:style-name="P42Right">
							 <xsl:value-of select="VOLUMETRIE/NBAMELIOR/@nbamelior"/>
              </text:p>
						</table:table-cell>
					</table:table-row>
				</table:table>
				<text:p text:style-name="Inter"><xsl:value-of select="$bottomup.amelioration.tableau"/><text:sequence text:ref-name="refTable4" text:name="Table" text:formula="ooow:Table+1" style:num-format="1">5</text:sequence><xsl:value-of select="$bottomup.amelioration.repartitionparseverite"/></text:p>
				<text:h text:style-name="P52" text:outline-level="2" text:is-list-header="true"/>
				<text:h text:style-name="P47" text:outline-level="2">
          <xsl:value-of select="$bottomup.repartitionelement.title"/>
        </text:h>
				<text:p text:style-name="P22"/>
				<table:table table:name="Tableau6" table:style-name="Tableau6">
					<table:table-column table:style-name="Tableau6.A"/>
					<table:table-column table:style-name="Tableau6.B"/>
					<table:table-row table:style-name="Tableau6.1">
						<table:table-cell table:style-name="Tableau6.A1" office:value-type="string">
							<text:p text:style-name="P53"><xsl:value-of select="$bottomup.amelioration.nbrejet"/></text:p>
							<text:p text:style-name="P53"><xsl:value-of select="$bottomup.amelioration.nbrejetexplain"/></text:p>
						</table:table-cell>
						<table:table-cell table:style-name="Tableau6.B1" office:value-type="string">
							<text:p text:style-name="P42Right"><xsl:value-of select="VOLUMETRIE/NBELEMENT/@rejet"/></text:p>
						</table:table-cell>
					</table:table-row>
					<table:table-row table:style-name="Tableau6.1">
						<table:table-cell table:style-name="Tableau6.A1" office:value-type="string">
							<text:p text:style-name="P53"><xsl:value-of select="$bottomup.amelioration.nbreserve"/></text:p>
							<text:p text:style-name="P53"><xsl:value-of select="$bottomup.amelioration.nbreserveexplain"/></text:p>
						</table:table-cell>
						<table:table-cell table:style-name="Tableau6.B1" office:value-type="string">
							<text:p text:style-name="P42Right"><xsl:value-of select="VOLUMETRIE/NBELEMENT/@reserve"/></text:p>
						</table:table-cell>
					</table:table-row>
					<table:table-row table:style-name="Tableau6.1">
						<table:table-cell table:style-name="Tableau6.A1" office:value-type="string">
							<text:p text:style-name="P53"><xsl:value-of select="$bottomup.amelioration.nbaccepte"/></text:p>
							<text:p text:style-name="P53"><xsl:value-of select="$bottomup.amelioration.nbaccepteexplain"/></text:p>
						</table:table-cell>
						<table:table-cell table:style-name="Tableau6.B3" office:value-type="string">
							<text:p text:style-name="P42Right"><xsl:value-of select="VOLUMETRIE/NBELEMENT/@accept"/></text:p>
						</table:table-cell>
					</table:table-row>
					<table:table-row table:style-name="Tableau6.1">
						<table:table-cell table:style-name="Tableau6.A1" office:value-type="string">
							<text:p text:style-name="P54"><xsl:value-of select="$bottomup.amelioration.pctamelior"/></text:p>
						</table:table-cell>
						<table:table-cell table:style-name="Tableau6.B3" office:value-type="string">
							<text:p text:style-name="P42Right"><xsl:value-of select="VOLUMETRIE/NBELEMENT/@pctamelior"/></text:p>
						</table:table-cell>
					</table:table-row>
				</table:table>		
				<text:p text:style-name="Inter"><xsl:value-of select="$bottomup.amelioration.tableau"/><text:sequence text:ref-name="refTable5" text:name="Table" text:formula="ooow:Table+1" style:num-format="1">6</text:sequence><xsl:value-of select="$bottomup.amelioration.repartitionparniveau"/></text:p>

				<text:h text:style-name="P47" text:outline-level="2">
          <xsl:value-of select="$bottomup.amelioration.ameliorparobjectif"/>
        </text:h>
  			<text:p text:style-name="Standard">
  				<text:s/>
  			</text:p>
				<text:p text:style-name="P35">
  				<draw:frame draw:style-name="fr1" draw:name="Cadre7" text:anchor-type="as-char" svg:width="15.875cm" svg:height="7.938cm" draw:z-index="3">
  					<draw:image xlink:href="Pictures/bottomup-fact.png" xlink:type="simple" xlink:show="embed" xlink:actuate="onLoad"/>
  				</draw:frame>
  			</text:p>
  			<text:p text:style-name="Inter"><xsl:value-of select="$bottomup.amelioration.figure"/><text:sequence text:ref-name="refIllustration3" text:name="Illustration" text:formula="ooow:Illustration+1" style:num-format="1">3</text:sequence>
          <xsl:value-of select="$bottomup.amelioration.repartitionameliorparobj"/>
        </text:p>

				<text:p text:style-name="Standard"/>

				<text:h text:style-name="P47" text:outline-level="2">
          <xsl:value-of select="$bottomup.amelioration.ameliorparcritere"/>
        </text:h>
  			<text:p text:style-name="Standard">
  				<text:s/>
  			</text:p>
				<text:p text:style-name="P35">
  				<draw:frame draw:style-name="fr1" draw:name="BottomUpCrit" text:anchor-type="as-char" svg:width="15.875cm" svg:height="7.938cm" draw:z-index="3">
  					<draw:image xlink:href="Pictures/bottomup-crit.png" xlink:type="simple" xlink:show="embed" xlink:actuate="onLoad"/>
  				</draw:frame>
  			</text:p>
  			<text:p text:style-name="Inter"><xsl:value-of select="$bottomup.amelioration.figure"/><text:sequence text:ref-name="refIllustration4" text:name="Illustration" text:formula="ooow:Illustration+1" style:num-format="1">4</text:sequence>
          <xsl:value-of select="$bottomup.amelioration.repartitionameliorparcrit"/>
        </text:p>
		
		</content>
	</xsl:template>
</xsl:stylesheet>
