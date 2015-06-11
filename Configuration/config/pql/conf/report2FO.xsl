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
                <xsl:apply-templates select="FACTOR" />
                <xsl:apply-templates select="CRIT_DETAIL" />
                
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

<xsl:template match="FACTOR">
      <fo:block space-before.optimum="12.0pt" space-after.optimum="3.0pt" page-break-inside="avoid" font-size="14.0pt" font-family="sans-serif" font-style="italic" foa:group="paragraph" foa:name="h2">
        <fo:inline space-before.optimum="12.0pt" space-after.optimum="3.0pt" page-break-inside="avoid" font-size="14.0pt" font-family="serif" font-style="normal" foa:group="emphasis" foa:name="h2_1">
          <xsl:value-of select="@lib"/>
        </fo:inline>
      </fo:block>
      <fo:block font-size="12.0pt" font-family="Arial" foa:group="paragraph" foa:name="Normal">
       
      </fo:block>
      
      <fo:table border-collapse="collapse" border-style="none"  foa:group="simple-table" foa:name="Table_1">

      <fo:table-column   column-width="10.55cm" column-number="1"/>
      <fo:table-column   column-width="1.52cm" column-number="2"/>
      <fo:table-column  column-width="1.52cm" column-number="3"/>

      <fo:table-body>
          
      <fo:table-row foa:group="simple-table" foa:name="Table_1">
      <fo:table-cell padding-left="3.5pt" background-color="yellow" padding-top="0cm" padding-bottom="0cm"  border-color="windowtext" border-width=".5pt" border-style="solid" padding-right="3.5pt"  foa:group="simple-table" foa:name="Table_1">
      <fo:block font-size="12.0pt" font-family="serif" text-align="center" foa:group="paragraph" foa:name="Normal">
              Critère
      </fo:block>
      </fo:table-cell>
      <fo:table-cell  padding-right="3.5pt" background-color="yellow" border-style="solid" border-width=".5pt" border-left-style="none" padding-top="0cm" border-color="windowtext" padding-bottom="0cm" padding-left="3.5pt"  foa:group="simple-table" foa:name="Table_1">
      <fo:block font-size="12.0pt" font-family="serif" text-align="center" foa:group="paragraph" foa:name="Normal">
              Note
      </fo:block>
      </fo:table-cell>
      <fo:table-cell  padding-right="3.5pt" background-color="yellow" border-style="solid" border-width=".5pt" border-left-style="none" padding-top="0cm" border-color="windowtext" padding-bottom="0cm" padding-left="3.5pt"  foa:group="simple-table" foa:name="Table_1">
      <fo:block font-size="12.0pt" font-family="serif" text-align="center" foa:group="paragraph" foa:name="Normal">
              Poids
      </fo:block>
      </fo:table-cell>
      </fo:table-row>

        <xsl:apply-templates select="CRITERE" />
        
      <fo:table-row foa:group="simple-table" foa:name="Table_1">
      <fo:table-cell padding-left="3.5pt"  padding-top="0cm" padding-bottom="0cm"  border-color="windowtext" border-width=".5pt" border-style="solid" padding-right="3.5pt"  foa:group="simple-table" foa:name="Table_1">
      <fo:block font-size="12.0pt" font-family="serif" foa:group="paragraph" foa:name="Normal">
              Note
      </fo:block>
      </fo:table-cell>
            <fo:table-cell  padding-right="3.5pt"  border-style="solid" border-width=".5pt" border-left-style="none" padding-top="0cm" border-color="windowtext" padding-bottom="0cm" padding-left="3.5pt"  foa:group="simple-table" foa:name="Table_1">
      <fo:block font-size="12.0pt" font-family="serif" text-align="center" foa:group="paragraph" foa:name="Normal">
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

<xsl:template match="CRIT_DETAIL">
      <fo:block font-size="12.0pt" font-family="Arial" foa:group="paragraph" foa:name="Normal">
       
      </fo:block>
      <fo:block font-size="18.0pt" font-family="serif" foa:group="paragraph" foa:name="h2">
        <xsl:value-of select="@lib"/>
      </fo:block>
      <fo:block font-size="12.0pt" font-family="Arial" foa:group="paragraph" foa:name="Normal">
       
      </fo:block>
      <fo:table font-size="18.0pt" font-family="serif" width="0.0pt" table-layout="fixed" foa:group="simple-table" foa:name="h2">
      <fo:table-column column-width="14.55cm" column-number="1"/>
      <fo:table-column column-width="1.50cm" column-number="2"/>
      <fo:table-column column-width="1.50cm" column-number="3"/>
      <fo:table-column column-width="1.50cm" column-number="4"/>
      <fo:table-column column-width="1.50cm" column-number="3"/>
      <fo:table-column column-width="1.50cm" column-number="5"/>


        <fo:table-body>
          
        <fo:table-row font-size="18.0pt" font-family="serif" foa:group="simple-table" foa:name="null">
        <fo:table-cell border-color="windowtext" border-width=".5pt" border-style="solid" height="0.0pt"  padding-left=".75pt" padding-top=".75pt" padding-bottom=".75pt" background-color="yellow" width="0.0pt" padding-right=".75pt"  foa:group="simple-table" foa:name="h2">
        <fo:block font-size="10.0pt" font-family="serif" text-align="center" foa:group="paragraph" foa:name="Normal_1">
        <fo:inline font-weight="bold" foa:group="emphasis" foa:name="bold">
                Nom
        </fo:inline>
        </fo:block>
        </fo:table-cell>
        <xsl:apply-templates select="METRIQUES" />
        <fo:table-cell border-color="windowtext" border-width=".5pt" border-style="solid" height="0.0pt" padding-left=".75pt" padding-top=".75pt" padding-bottom=".75pt" background-color="yellow" width="0.0pt" padding-right=".75pt"  foa:group="simple-table" foa:name="h2">
        <fo:block font-size="10.0pt" font-family="serif" text-align="center" foa:group="paragraph" foa:name="Normal_1">
        <fo:inline font-weight="bold" foa:group="emphasis" foa:name="bold">
                Note
        </fo:inline>
        </fo:block>
        </fo:table-cell>
        
        </fo:table-row>

        <xsl:apply-templates select="ELT" />

       </fo:table-body>
    </fo:table>
</xsl:template>

<xsl:template match="METRIQUES">
    <xsl:for-each select="MET">
    
        <fo:table-cell border-color="windowtext" border-width=".5pt" border-style="solid" height="0.0pt" padding-left=".75pt" padding-top=".75pt" padding-bottom=".75pt" background-color="yellow" width="0.0pt" padding-right=".75pt"  foa:group="simple-table" foa:name="h2">
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

</xsl:stylesheet> 
