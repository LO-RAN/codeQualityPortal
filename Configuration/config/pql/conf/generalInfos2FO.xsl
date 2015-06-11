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
                  <fo:block font-size="16.0pt" font-family="serif" foa:group="paragraph" foa:name="Normal">
                    Rapport d'Analyse <xsl:apply-templates select="ELEMENT"/>
                  </fo:block>
                  <fo:block font-size="12.0pt" font-family="serif" foa:group="paragraph" foa:name="Normal">
                           
                  </fo:block>
                <xsl:apply-templates select="BASELINE"/>
             </fo:flow>
             </fo:page-sequence>
             </fo:root>
        </fo>
    </xsl:template>
    
<!-- template rule matching source root element -->
<xsl:template match="ELEMENT">
<xsl:value-of select="@lib"/>
</xsl:template>
            
<xsl:template match="BASELINE">
       <fo:block font-size="12.0pt" font-family="serif" foa:group="paragraph" foa:name="Normal">
                           
      </fo:block>
      <fo:block font-size="12.0pt" font-family="serif" foa:group="paragraph" foa:name="Normal">
        Baseline : <xsl:value-of select="@lib"/>
      </fo:block>
      <fo:block font-size="12.0pt" font-family="serif" foa:group="paragraph" foa:name="Normal">
                    
      </fo:block>
                       
</xsl:template>

</xsl:stylesheet> 
