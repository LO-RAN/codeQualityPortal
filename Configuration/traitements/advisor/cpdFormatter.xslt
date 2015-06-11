<?xml version="1.0" encoding="UTF-8"?>

<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
<xsl:template match="pmd-cpd">
  <xsl:copy>
    <xsl:apply-templates/>
  </xsl:copy>
</xsl:template>

<xsl:template match="pmd-cpd">
  <xsl:copy-of select="*"/>
</xsl:template>

<xsl:template match="codefragment">
</xsl:template>

</xsl:stylesheet>
