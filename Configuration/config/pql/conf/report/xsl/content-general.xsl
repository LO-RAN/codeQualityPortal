<?xml version="1.0" encoding="UTF-8" ?>

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

      <content xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:text="urn:oasis:names:tc:opendocument:xmlns:text:1.0" xmlns:table="urn:oasis:names:tc:opendocument:xmlns:table:1.0" xmlns:number="urn:oasis:names:tc:opendocument:xmlns:datastyle:1.0" xmlns:office="urn:oasis:names:tc:opendocument:xmlns:office:1.0">

      <text:h text:style-name="P18" text:outline-level="3">
        <xsl:value-of select="$general.systemeaudit"/>
      </text:h>
				<text:p text:style-name="Standard"/>
				<table:table table:name="Tableau2" table:style-name="Tableau2">
					<table:table-column table:style-name="Tableau2.A"/>
					<table:table-column table:style-name="Tableau2.B"/>
					<table:table-row table:style-name="Tableau2.1">
						<table:table-cell table:style-name="Tableau2.A1" office:value-type="string">
							<text:p text:style-name="P41">
								<xsl:value-of select="$general.project"/>
							</text:p>
						</table:table-cell>
						<table:table-cell table:style-name="Tableau2.B1" office:value-type="string">
							<text:p text:style-name="P42">
							  <xsl:value-of select="ELEMENT/@projectName"/>
							</text:p>
						</table:table-cell>
					</table:table-row>
					<table:table-row table:style-name="Tableau2.1">
						<table:table-cell table:style-name="Tableau2.A1" office:value-type="string">
							<text:p text:style-name="P41">
								<xsl:value-of select="$general.nom"/>
							</text:p>
						</table:table-cell>
						<table:table-cell table:style-name="Tableau2.B1" office:value-type="string">
							<text:p text:style-name="P42">
							  <xsl:value-of select="ELEMENT/@lib"/>
							</text:p>
						</table:table-cell>
					</table:table-row>
					<table:table-row table:style-name="Tableau2.1">
						<table:table-cell table:style-name="Tableau2.A1" office:value-type="string">
							<text:p text:style-name="P41">
								<xsl:value-of select="$general.language"/>
						</text:p>
						</table:table-cell>
						<table:table-cell table:style-name="Tableau2.B1" office:value-type="string">
								<text:p text:style-name="P42">
								  <xsl:value-of select="MODELE/@dialecte"/>
								</text:p>
						</table:table-cell>
					</table:table-row>
				</table:table>

				<text:p text:style-name="Inter"><xsl:value-of select="$general.tableau"/><text:sequence text:ref-name="refTable1" text:name="Table" text:formula="ooow:Table+1" style:num-format="1">2</text:sequence> : <xsl:value-of select="$general.systemeaudit"/></text:p>

				<text:p text:style-name="P22"/>
				<text:h text:style-name="P20" text:outline-level="3">
					<xsl:value-of select="$general.suivioutillage"/>
				</text:h>
				<text:p text:style-name="P22"/>
				<text:p text:style-name="P22">
					<xsl:value-of select="$general.outilsintro"/>
				</text:p>
				<text:p text:style-name="P22"/>
				<table:table table:name="Tableau3" table:style-name="Tableau3">
					<table:table-column table:style-name="Tableau3.A"/>
					<table:table-column table:style-name="Tableau3.B"/>
					<table:table-header-rows>
						<table:table-row table:style-name="Tableau3.1">
							<table:table-cell table:style-name="Tableau3.A1" office:value-type="string">
								<text:p text:style-name="P41">
									<xsl:value-of select="$general.caqsversion"/>
								</text:p>
							</table:table-cell>
							<table:table-cell table:style-name="Tableau3.B1" office:value-type="string">
								<text:p text:style-name="P42"><xsl:value-of select="CAQSVERSION/@value"/></text:p>
							</table:table-cell>
						</table:table-row>
					</table:table-header-rows>
					<table:table-row table:style-name="Tableau3.1">
						<table:table-cell table:style-name="Tableau3.A2" office:value-type="string">
							<text:p text:style-name="P41">
								<xsl:value-of select="$general.analyseur"/>
							</text:p>
						</table:table-cell>
						<table:table-cell table:style-name="Tableau3.B2" office:value-type="string">
      				<xsl:apply-templates select="OUTILS/OUTIL"/>
						</table:table-cell>
					</table:table-row>
					<table:table-row table:style-name="Tableau3.1">
						<table:table-cell table:style-name="Tableau3.A2" office:value-type="string">
							<text:p text:style-name="P41">
								<xsl:value-of select="$general.modelequalimetrique"/>
							</text:p>
						</table:table-cell>
						<table:table-cell table:style-name="Tableau3.B2" office:value-type="string">
								  <text:p text:style-name="P42"><xsl:value-of select="MODELE/@usage"/></text:p>
						</table:table-cell>
					</table:table-row>
				</table:table>
				<text:p text:style-name="Inter"><xsl:value-of select="$general.tableau"/><text:sequence text:ref-name="refTable2" text:name="Table" text:formula="ooow:Table+1" style:num-format="1">3</text:sequence> : <xsl:value-of select="$general.suivioutillage"/></text:p>

      </content>
    </xsl:template>
    
    <xsl:template match="OUTILS/OUTIL">
				<text:p text:style-name="P42">
					 <xsl:value-of select="@lib" />
        </text:p>
    </xsl:template>

</xsl:stylesheet> 
