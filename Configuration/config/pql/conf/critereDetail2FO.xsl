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

    <xsl:strip-space elements="CRIT_DETAIL ELT MET METRIQUES REPORT"/>

    <xsl:template match="REPORT">
        <fo>
            <fo:root xmlns:fo="http://www.w3.org/1999/XSL/Format" xmlns:foa="http://fabio" xmlns:fox="http://xml.apache.org/fop/extensions">
            <fo:layout-master-set>
            <fo:simple-page-master margin-right="59.041666666666664pt" margin-left="59.041666666666664pt" margin-bottom="53.14999999999999pt" margin-top="63.2pt" master-name="Section1-rest" page-height="21cm" page-width="29.7cm">
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
                <xsl:apply-templates select="CRIT_DETAIL" />
                <xsl:apply-templates select="NONE" />
             </fo:flow>
             </fo:page-sequence>
             </fo:root>
        </fo>
    </xsl:template>

<xsl:template match="CRIT_DETAIL">
      <fo:table font-size="18.0pt" font-family="serif" table-layout="auto" foa:group="simple-table" foa:name="h2">
      <fo:table-column column-width="20cm" column-number="1"/>
      <fo:table-column column-width="1.50cm" column-number="2"/>
      <fo:table-column column-width="1.50cm" column-number="3"/>
      <fo:table-column column-width="1.50cm" column-number="4"/>
      <fo:table-column column-width="1.50cm" column-number="3"/>
      <fo:table-column column-width="1.50cm" column-number="5"/>


        <fo:table-body>
		<fo:table-header>
        <fo:table-row font-size="18.0pt" font-family="serif" foa:group="simple-table" foa:name="null">
        <fo:table-cell border-color="windowtext" border-width=".5pt" border-style="solid" height="0.0pt"  padding-left=".75pt" padding-top=".75pt" padding-bottom=".75pt" background-color="#E5E5E5" width="0.0pt" padding-right=".75pt"  foa:group="simple-table" foa:name="h2">
        <fo:block font-size="10.0pt" font-family="serif" text-align="center" foa:group="paragraph" foa:name="Normal_1">
        <fo:inline font-weight="bold" foa:group="emphasis" foa:name="bold">
                Nom
        </fo:inline>
        </fo:block>
        </fo:table-cell>
        <xsl:apply-templates select="METRIQUES" />
        <fo:table-cell border-color="windowtext" border-width=".5pt" border-style="solid" height="0.0pt" padding-left=".75pt" padding-top=".75pt" padding-bottom=".75pt" background-color="#E5E5E5" width="0.0pt" padding-right=".75pt"  foa:group="simple-table" foa:name="h2">
        <fo:block font-size="10.0pt" font-family="serif" text-align="center" foa:group="paragraph" foa:name="Normal_1">
        <fo:inline font-weight="bold" foa:group="emphasis" foa:name="bold">
                Note
        </fo:inline>
        </fo:block>
        </fo:table-cell>

        </fo:table-row>
		</fo:table-header>

        <xsl:apply-templates select="ELT" />

       </fo:table-body>
    </fo:table>
</xsl:template>

<xsl:template match="METRIQUES">
    <xsl:for-each select="MET">

        <fo:table-cell border-color="windowtext" border-width=".5pt" border-style="solid" height="0.0pt" padding-left=".75pt" padding-top=".75pt" padding-bottom=".75pt" background-color="#E5E5E5" width="0.0pt" padding-right=".75pt"  foa:group="simple-table" foa:name="h2">
        <fo:block font-size="10.0pt" font-family="serif" text-align="center" foa:group="paragraph" foa:name="Normal_1">
        <fo:inline font-weight="bold" foa:group="emphasis" foa:name="bold">
                <xsl:value-of select="@lib"/>
        </fo:inline>
        </fo:block>
        </fo:table-cell>

    </xsl:for-each>
</xsl:template>

<xsl:template match="ELT">

        <fo:table-row font-size="18.0pt" font-family="serif" foa:group="simple-table" foa:name="null">
        <fo:table-cell border-color="windowtext" border-width=".5pt" border-style="solid" height="0.0pt" padding-left=".75pt" padding-top=".75pt" padding-bottom=".75pt"  width="0.0pt" padding-right=".75pt"  foa:group="simple-table" foa:name="h2">
        <fo:block font-size="10.0pt" font-family="Arial" foa:group="paragraph" foa:name="Normal_1">
        <fo:inline foa:group="emphasis" >
                <xsl:value-of select="@desc"/>
        </fo:inline>
        </fo:block>
        </fo:table-cell>

        <xsl:for-each select="MET">

            <fo:table-cell border-color="windowtext" border-width=".5pt" border-style="solid" height="0.0pt" padding-left=".75pt" padding-top=".75pt" padding-bottom=".75pt"  width="0.0pt" padding-right=".75pt"  foa:group="simple-table" foa:name="h2">
            <fo:block font-size="10.0pt" font-family="Arial" foa:group="paragraph" foa:name="Normal_1">
            <fo:inline  foa:group="emphasis" >
               <xsl:value-of select="@value"/>
            </fo:inline>
            </fo:block>
            </fo:table-cell>
        </xsl:for-each>
            <fo:table-cell border-color="windowtext" border-width=".5pt" border-style="solid" height="0.0pt" padding-left=".75pt" padding-top=".75pt" padding-bottom=".75pt"  width="0.0pt" padding-right=".75pt"  foa:group="simple-table" foa:name="h2">
            <fo:block font-size="10.0pt" font-family="Arial" foa:group="paragraph" foa:name="Normal_1">
            <fo:inline  foa:group="emphasis" >
               <xsl:value-of select="@note"/>
            </fo:inline>
            </fo:block>
            </fo:table-cell>
        </fo:table-row>

</xsl:template>

<xsl:template match="NONE">
	<fo:block font-size="10.0pt" font-family="serif" foa:group="paragraph" foa:name="Normal_1">
		Aucune.
	</fo:block>
</xsl:template>

</xsl:stylesheet>
