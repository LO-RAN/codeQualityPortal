<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">

<xsl:template match="/REPORT">
<xsl:apply-templates select="CRIT_DETAIL"/>
</xsl:template>

<xsl:template match="CRIT_DETAIL">
Nom;<xsl:for-each select="METRIQUES/MET"><xsl:value-of select="@id" />;</xsl:for-each><xsl:value-of select="@id" />;
<xsl:apply-templates select="ELT"/>
</xsl:template>

<xsl:template match="ELT">
<xsl:value-of select="@desc" />;<xsl:for-each select="MET"><xsl:value-of select="@value" />;</xsl:for-each><xsl:value-of select="@note" />;
</xsl:template>

</xsl:stylesheet>
