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
		
		
				<text:h text:style-name="P17_BREAK" text:outline-level="2">
          <xsl:value-of select="$synthese.title"/>
        </text:h>
				<text:p text:style-name="P35">
						<draw:frame draw:style-name="fr1" draw:name="Image6" text:anchor-type="as-char" svg:width="9.269cm" svg:height="7.946cm" draw:z-index="28">
							<draw:image xlink:href="Pictures/kiviat.png" xlink:type="simple" xlink:show="embed" xlink:actuate="onLoad"/>
						</draw:frame>
				</text:p>
				<text:p text:style-name="Inter"><xsl:value-of select="$synthese.figure"/> <text:sequence text:ref-name="refIllustration4" text:name="Illustration" text:formula="ooow:Illustration+1" style:num-format="1">1</text:sequence>
            <xsl:value-of select="$synthese.figure.graph"/>
        </text:p>

				<text:p text:style-name="Standard"/>
				<table:table table:name="Tableau4" table:style-name="Tableau4">
					<table:table-column table:style-name="Tableau4.A"/>
					<table:table-column table:style-name="Tableau4.B"/>
					<xsl:if test="FACTORSYNTHESE/@hasTendance = 'true'">
						<table:table-column table:style-name="Tableau4.D"/>
					</xsl:if>
					<table:table-column table:style-name="Tableau4.C"/>
					<table:table-header-rows>
						<table:table-row table:style-name="Tableau4.1">
							<table:table-cell table:style-name="Tableau4.A1" office:value-type="string">
								<text:p text:style-name="P46">
                  <xsl:value-of select="$synthese.facteur"/>
                </text:p>
							</table:table-cell>
							<table:table-cell table:style-name="Tableau4.A1" office:value-type="string">
								<text:p text:style-name="P46">
                  <xsl:value-of select="$synthese.note"/>
                </text:p>
							</table:table-cell>
							<xsl:if test="FACTORSYNTHESE/@hasTendance = 'true'">
								<table:table-cell table:style-name="Tableau4.B1" office:value-type="string">
									<text:p text:style-name="P46">
										<xsl:value-of select="$synthese.tendance"/>
									</text:p>
								</table:table-cell>
							</xsl:if>
							<table:table-cell table:style-name="Tableau4.C1" office:value-type="string">
								<text:p text:style-name="P46">
                  <xsl:value-of select="$synthese.commentaireLib"/>
                </text:p>
							</table:table-cell>
						</table:table-row>
					</table:table-header-rows>
  				<xsl:for-each select="FACTORSYNTHESE/FACTOR">
					<table:table-row table:style-name="Tableau4.1">
						<table:table-cell table:style-name="Tableau4.A2" office:value-type="string">
							<text:p text:style-name="P42">
							 <xsl:value-of select="@lib"/>
              </text:p>
						</table:table-cell>
						<table:table-cell table:style-name="Tableau4.A2" office:value-type="string">
							<text:p text:style-name="P42Right">
							 <xsl:value-of select="@note"/>
              </text:p>
						</table:table-cell>
						<xsl:if test="@hasTendance = 'true'">
							<table:table-cell table:style-name="Tableau4.A2" office:value-type="string">
								<text:p text:style-name="P58">
									<xsl:call-template name="INSERT_TENDANCE_PICTURE">
										<xsl:with-param name="tendance"><xsl:value-of select="@tendance"/></xsl:with-param>
									</xsl:call-template>
								</text:p>
							</table:table-cell>
						</xsl:if>
						<table:table-cell table:style-name="Tableau4.C2" office:value-type="string">
							<text:p text:style-name="P42">
								 <xsl:value-of select="@comment"/>
              </text:p>
						</table:table-cell>
					</table:table-row>
				</xsl:for-each>
					
				</table:table>		
				<text:p text:style-name="Inter"><xsl:value-of select="$synthese.tableau"/><text:sequence text:ref-name="refTable3" text:name="Table" text:formula="ooow:Table+1" style:num-format="1">4</text:sequence><xsl:value-of select="$synthese.tableau.synthese"/></text:p>
		

			<text:h text:style-name="P17" text:outline-level="2">
        <xsl:value-of select="$synthese.volumetrie"/>
      </text:h>
			<table:table table:name="TableauSynthVol" table:style-name="TableauSynthVol">
				<table:table-column table:style-name="TableauSynthVol.A"/>
				<table:table-column table:style-name="TableauSynthVol.B"/>
				
				<xsl:for-each select="VOLUMETRY">
  				<table:table-row table:style-name="TableauSynthVol.1">
  					<table:table-cell table:style-name="TableauSynthVol.A2" office:value-type="string">
  						<text:p text:style-name="P46Left">
                  <xsl:value-of select="$general.nb"/><xsl:text> </xsl:text><xsl:value-of select="@type"/>s
              </text:p>
  					</table:table-cell>
  					<table:table-cell table:style-name="TableauSynthVol.B2" office:value-type="string">
  						<text:p text:style-name="P42Right"><xsl:value-of select="@total"/></text:p>
  					</table:table-cell>
  				</table:table-row>
				</xsl:for-each>
				
				<table:table-row table:style-name="TableauSynthVol.1">
					<table:table-cell table:style-name="TableauSynthVol.A2" office:value-type="string">
						<text:p text:style-name="P46Left">
              <xsl:value-of select="$synthese.nblignecode"/>
            </text:p>
					</table:table-cell>
					<table:table-cell table:style-name="TableauSynthVol.B2" office:value-type="string">
						<text:p text:style-name="P42Right"><xsl:value-of select="METRICS/ALL_CODE/@note"/></text:p>
					</table:table-cell>
				</table:table-row>
				<table:table-row table:style-name="TableauSynthVol.1">
					<table:table-cell table:style-name="TableauSynthVol.A2" office:value-type="string">
						<text:p text:style-name="P46Left">
              <xsl:value-of select="$synthese.txcomment"/>
            </text:p>
					</table:table-cell>
					<table:table-cell table:style-name="TableauSynthVol.B2" office:value-type="string">
						<text:p text:style-name="P42Right">
							<xsl:value-of select="METRICS/PCT_COMMENTS/@note"/>%
            </text:p>
					</table:table-cell>
				</table:table-row>
				<table:table-row table:style-name="TableauSynthVol.1">
					<table:table-cell table:style-name="TableauSynthVol.A2" office:value-type="string">
						<text:p text:style-name="P46Left">
              <xsl:value-of select="$synthese.txcodecomplexdest"/>
            </text:p>
					</table:table-cell>
					<table:table-cell table:style-name="TableauSynthVol.B2" office:value-type="string">
						<text:p text:style-name="P42Right">
							<xsl:value-of select="METRICS/PCT_COMPLEX_DEST/@note"/>%
            </text:p>
					</table:table-cell>
				</table:table-row>
				<table:table-row table:style-name="TableauSynthVol.1">
					<table:table-cell table:style-name="TableauSynthVol.A2" office:value-type="string">
						<text:p text:style-name="P46Left">
              <xsl:value-of select="$synthese.ifpug"/>
            </text:p>
					</table:table-cell>
					<table:table-cell table:style-name="TableauSynthVol.B2" office:value-type="string">
						<text:p text:style-name="P42Right"><xsl:value-of select="METRICS/IFPUG/@note"/></text:p>
					</table:table-cell>
				</table:table-row>
			</table:table>
			<text:p text:style-name="Inter"><xsl:value-of select="$synthese.tableau"/><text:sequence text:ref-name="refTable1" text:name="Table" text:formula="ooow:Table+1" style:num-format="1">2</text:sequence><xsl:value-of select="$synthese.tableau.volumetrie"/></text:p>
      
			<text:h text:style-name="P17" text:outline-level="2">
        <xsl:value-of select="$synthese.repartition.title"/>
      </text:h>
			<text:p text:style-name="P35">
			</text:p>
			<text:p text:style-name="P35">
				<draw:frame draw:style-name="fr3" draw:name="ScatterPlot" text:anchor-type="as-char" svg:width="15.875cm" svg:height="10.583cm" draw:z-index="152">
					<draw:image xlink:href="Pictures/scatterPlot.png" xlink:type="simple" xlink:show="embed" xlink:actuate="onLoad"/>
				</draw:frame>
			</text:p>
			<text:p text:style-name="Inter"><xsl:value-of select="$synthese.figure"/><text:sequence text:ref-name="refIllustration1" text:name="Illustration" text:formula="ooow:Illustration+1" style:num-format="1">2</text:sequence>
        <xsl:value-of select="$synthese.repartition.lib"/>
      </text:p>
      			
		</content>
	</xsl:template>
</xsl:stylesheet>
