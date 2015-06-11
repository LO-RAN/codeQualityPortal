<?xml version="1.0"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
<xsl:output method="text" />
<xsl:strip-space elements="*"/>
<xsl:template match="error"> 
<xsl:value-of select="../@name"/>; <xsl:value-of select="@line"/>; <xsl:value-of select="@column"/>; <xsl:value-of select="@severity"/>; <xsl:if test="contains(@message, ';')"><xsl:value-of select="substring-before(@message, ';')" /></xsl:if><xsl:if test="not(contains(@message, ';'))"><xsl:value-of select="@message"/></xsl:if>; <xsl:value-of select="@source"/>
<xsl:text>
</xsl:text>
</xsl:template>
</xsl:stylesheet>

