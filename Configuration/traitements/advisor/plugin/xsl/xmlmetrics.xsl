<?xml version="1.0" encoding="UTF-8" ?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

    <xsl:template match="/">
        <xsl:text disable-output-escaping="yes">&lt;Result></xsl:text>
        <xsl:apply-templates select="metrics/details/package"/>
        <xsl:text disable-output-escaping="yes">&lt;/Result></xsl:text>
    </xsl:template>

    <xsl:template match="package">
        <xsl:text disable-output-escaping="yes">&lt;elt name="</xsl:text><xsl:value-of select='@fully-qualified-name'/>" lib="<xsl:value-of select='@fully-qualified-name'/><xsl:text disable-output-escaping="yes">" type='PKG'></xsl:text>
        <xsl:for-each select="metric">
            <xsl:text disable-output-escaping="yes">&lt;metric id='</xsl:text><xsl:value-of select="@name"/>' value='<xsl:value-of select="@value"/><xsl:text disable-output-escaping="yes">' /></xsl:text>
        </xsl:for-each>
        <xsl:text disable-output-escaping="yes">&lt;/elt></xsl:text>
    </xsl:template>
</xsl:stylesheet>
