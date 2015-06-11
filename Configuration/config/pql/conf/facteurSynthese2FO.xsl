<?xml version="1.0" encoding="ISO-8859-1" ?>

<!--
    Document   : report2fo.xsl
    Created on : 2 février 2004
    Author     : cwfr-gcartigny
    Description:
        Purpose of transformation follows.
-->

<xsl:stylesheet version="1.0"
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:fo="http://www.w3.org/1999/XSL/Format"
    xmlns:foa="http://fabio"
    xmlns:fox="http://xml.apache.org/fop/extensions">

    <xsl:strip-space elements="BASELINE CRITERE CRIT_DETAIL ELEMENT ELT FACTOR MET METRIQUES REPORT"/>

    <xsl:template match="REPORT">
        <fo>
            <fo:root xmlns:fo="http://www.w3.org/1999/XSL/Format" xmlns:foa="http://fabio" xmlns:fox="http://xml.apache.org/fop/extensions">
            <fo:layout-master-set>
            <fo:simple-page-master margin-right="59.041666666666664pt" margin-left="59.041666666666664pt" margin-bottom="53.14999999999999pt" margin-top="63.2pt" master-name="Section1-rest" page-height="841.9pt" page-width="595.3pt">
            <fo:region-before extent="63.2pt"/>
            <fo:region-after extent="53.14999999999999pt"/>
            <fo:region-body margin-bottom="53.14999999999999pt" margin-top="63.2pt"/>
            </fo:simple-page-master><fo:page-sequence-master master-name="Section1-ps">
            <fo:repeatable-page-master-reference master-reference="Section1-rest"/>
            </fo:page-sequence-master>
            </fo:layout-master-set><fo:page-sequence master-reference="Section1-ps">
            <fo:static-content flow-name="xsl-region-before"/>
            <fo:static-content flow-name="xsl-region-after"/>
            <fo:flow flow-name="xsl-region-body">
                <xsl:apply-templates select="FACTOR" />
             </fo:flow>
             </fo:page-sequence>
             </fo:root>
        </fo>
    </xsl:template>

<xsl:template match="FACTOR">
      <fo:table border-collapse="collapse" border-style="none"  foa:group="simple-table" foa:name="Table_1">

      <fo:table-column   column-width="10.55cm" column-number="1"/>
      <fo:table-column   column-width="1.52cm" column-number="2"/>
      <fo:table-column  column-width="1.52cm" column-number="3"/>

      <fo:table-body>

      <fo:table-row foa:group="simple-table" foa:name="Table_1">
      <fo:table-cell padding-left="3.5pt" background-color="#E5E5E5" padding-top="0cm" padding-bottom="0cm"  border-color="windowtext" border-width=".5pt" border-style="solid" padding-right="3.5pt"  foa:group="simple-table" foa:name="Table_1">
      <fo:block font-size="12.0pt" font-family="serif" font-weight="bold" text-align="center" foa:group="paragraph" foa:name="Normal">
              Critère
      </fo:block>
      </fo:table-cell>
      <fo:table-cell  padding-right="3.5pt" background-color="#E5E5E5" border-style="solid" border-width=".5pt" border-left-style="none" padding-top="0cm" border-color="windowtext" padding-bottom="0cm" padding-left="3.5pt"  foa:group="simple-table" foa:name="Table_1">
      <fo:block font-size="12.0pt" font-family="serif" font-weight="bold" text-align="center" foa:group="paragraph" foa:name="Normal">
              Note
      </fo:block>
      </fo:table-cell>
      <fo:table-cell  padding-right="3.5pt" background-color="#E5E5E5" border-style="solid" border-width=".5pt" border-left-style="none" padding-top="0cm" border-color="windowtext" padding-bottom="0cm" padding-left="3.5pt"  foa:group="simple-table" foa:name="Table_1">
      <fo:block font-size="12.0pt" font-family="serif" font-weight="bold" text-align="center" foa:group="paragraph" foa:name="Normal">
              Poids
      </fo:block>
      </fo:table-cell>
      </fo:table-row>

        <xsl:apply-templates select="CRITERE" />

      <fo:table-row foa:group="simple-table" foa:name="Table_1">
	<fo:table-cell number-columns-spanned="3" border-style="none" foa:group="simple-table" foa:name="Table_1">
        </fo:table-cell>
      </fo:table-row>
      <fo:table-row foa:group="simple-table" foa:name="Table_1">
      <fo:table-cell padding-left="3.5pt" background-color="#E5E5E5"  padding-top="0cm" padding-bottom="0cm"  border-color="windowtext" border-width=".5pt" border-style="solid" padding-right="3.5pt"  foa:group="simple-table" foa:name="Table_1">
      <fo:block font-size="12.0pt" font-family="serif" font-weight="bold" text-align="center" foa:group="paragraph" foa:name="Normal">
              Note
      </fo:block>
      </fo:table-cell>
      <fo:table-cell number-columns-spanned="2" background-color="#E5E5E5" padding-right="3.5pt"  border-style="solid" border-width=".5pt" border-left-style="none" padding-top="0cm" border-color="windowtext" padding-bottom="0cm" padding-left="3.5pt"  foa:group="simple-table" foa:name="Table_1">
      	<fo:block font-size="12.0pt" font-family="serif" font-weight="bold" text-align="center" foa:group="paragraph" foa:name="Normal">
          <xsl:value-of select="@note"/>
      	</fo:block>
      </fo:table-cell>
      </fo:table-row>
    </fo:table-body>
    </fo:table>

</xsl:template>

<xsl:template match="CRITERE">

      <fo:table-row foa:group="simple-table" foa:name="Table_1">
      <fo:table-cell padding-left="3.5pt" padding-top="0cm" padding-bottom="0cm"  border-color="windowtext" border-width=".5pt" border-style="solid" padding-right="3.5pt"  foa:group="simple-table" foa:name="Table_1">
      <fo:block font-size="12.0pt" font-family="serif"  foa:group="paragraph" foa:name="Normal">
              <xsl:value-of select="@lib"/>
      </fo:block>
      </fo:table-cell>
      <fo:table-cell  padding-right="3.5pt" border-style="solid" border-width=".5pt" border-left-style="none" padding-top="0cm" border-color="windowtext" padding-bottom="0cm" padding-left="3.5pt" foa:group="simple-table" foa:name="Table_1">
      <fo:block font-size="12.0pt" font-family="serif" text-align="center" foa:group="paragraph" foa:name="Normal">
              <xsl:value-of select="@note"/>
      </fo:block>
      </fo:table-cell>
      <fo:table-cell  padding-right="3.5pt" border-style="solid" border-width=".5pt" border-left-style="none" padding-top="0cm" border-color="windowtext" padding-bottom="0cm" padding-left="3.5pt"  foa:group="simple-table" foa:name="Table_1">
      <fo:block font-size="12.0pt" font-family="serif" text-align="center" foa:group="paragraph" foa:name="Normal">
              <xsl:value-of select="@weight"/>
      </fo:block>
      </fo:table-cell>
      </fo:table-row>

</xsl:template>

</xsl:stylesheet>
