<?xml version="1.0" encoding="UTF-8" ?>

<!--
    Document   : report2html.xsl
    Created on : 30 janvier 2004, 11:52
    Author     : cwfr-fdubois
    Description:
        Purpose of transformation follows.
-->

<xsl:stylesheet version="1.0"
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:fo="http://www.w3.org/1999/XSL/Format">

    <xsl:strip-space elements="BASELINE CRITERE CRIT_DETAIL ELEMENT ELT FACTOR MET METRIQUES REPORT"/>
              
    <xsl:template match="REPORT">
        <html>
            <body>
                <h1>Rapport d'Analyse <xsl:apply-templates select="ELEMENT"/></h1>
                <xsl:apply-templates select="BASELINE"/>
                <xsl:apply-templates select="FACTOR" />
                <xsl:apply-templates select="CRIT_DETAIL" />
          </body>
        </html>
    </xsl:template>
    
<!-- template rule matching source root element -->
<xsl:template match="ELEMENT">
<xsl:value-of select="@lib"/>
</xsl:template>

<xsl:template match="BASELINE">
Baseline: <xsl:value-of select="@lib"/><BR/>
</xsl:template>

<xsl:template match="FACTOR">
    <h2><xsl:value-of select="@lib"/></h2>
    <TABLE border='1'>
        <TR bgcolor='yellow'><TH>Critère</TH><TH>Note</TH><TH>Poids</TH></TR>
        <xsl:apply-templates select="CRITERE" />
        <TR bgcolor='yellow'><TH>Note</TH><TH colspan='2'><xsl:value-of select="@note"/></TH></TR>
    </TABLE>
</xsl:template>

<xsl:template match="CRITERE">
<TR><TD><xsl:value-of select="@lib"/></TD><TD><xsl:value-of select="@note"/></TD><TD><xsl:value-of select="@weight"/></TD></TR>
</xsl:template>

<xsl:template match="CRIT_DETAIL">
    <h2><xsl:value-of select="@lib"/></h2>
    <TABLE border='1'>
        <TR bgcolor='yellow'>
            <TH>Nom</TH>
            <xsl:apply-templates select="METRIQUES" />
            <TH>Note</TH>
        </TR>
        <xsl:apply-templates select="ELT" />
    </TABLE>
</xsl:template>

<xsl:template match="METRIQUES">
    <xsl:for-each select="MET">
        <TH><xsl:value-of select="@lib"/></TH>
    </xsl:for-each>
</xsl:template>

<xsl:template match="ELT">
    <TR>
        <TD><xsl:value-of select="@desc"/></TD>
        <xsl:for-each select="MET">
            <TD><xsl:value-of select="@value"/></TD>
        </xsl:for-each>
        <TD><xsl:value-of select="@note"/></TD>
    </TR>
</xsl:template>

</xsl:stylesheet> 
