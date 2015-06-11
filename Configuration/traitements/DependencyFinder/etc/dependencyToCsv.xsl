<?xml version="1.0" encoding="ISO-8859-1" ?>
<!--
    Document   : dependency.xsl
    Created on : 22 février 2007
    Author     : cwfr-fdubois
    Description:
-->
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

<xsl:output method="text" indent="no" omit-xml-declaration="yes" />
<xsl:template match="/">
<xsl:apply-templates select="dependencies/package/class/feature" />
</xsl:template>

<xsl:template match="dependencies/package/class/feature">
<xsl:for-each select="outbound">
<xsl:if test="@type='feature' and not(starts-with(.,'java.')) and not(starts-with(.,'javax.')) and not(starts-with(.,'org.'))">
<xsl:value-of select="../name" />;<xsl:value-of select="." />;
</xsl:if>
</xsl:for-each>
<xsl:for-each select="inbound">
<xsl:if test="@type='feature' and not(starts-with(.,'java.')) and not(starts-with(.,'javax.')) and not(starts-with(.,'org.'))">
<xsl:value-of select="." />;<xsl:value-of select="../name" />;
</xsl:if>
</xsl:for-each>
</xsl:template>


</xsl:stylesheet>
