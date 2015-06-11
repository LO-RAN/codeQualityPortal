<?xml version="1.0" encoding="UTF-8" ?>

<!--
    Document   : report2fo.xsl
    Created on : 2 février 2004
    Author     : cwfr-gcartigny
    Description:
        Purpose of transformation follows.
-->

<xsl:stylesheet version="1.0"
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:text="urn:oasis:names:tc:opendocument:xmlns:text:1.0"
    xmlns:table="urn:oasis:names:tc:opendocument:xmlns:table:1.0"
    xmlns:office="urn:oasis:names:tc:opendocument:xmlns:office:1.0">

    <xsl:strip-space elements="MODEL REPORT"/>
              
    <xsl:template match="REPORT">
      <content xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:text="urn:oasis:names:tc:opendocument:xmlns:text:1.0" xmlns:table="urn:oasis:names:tc:opendocument:xmlns:table:1.0" xmlns:draw="urn:oasis:names:tc:opendocument:xmlns:drawing:1.0" xmlns:svg="urn:oasis:names:tc:opendocument:xmlns:svg-compatible:1.0" xmlns:office="urn:oasis:names:tc:opendocument:xmlns:office:1.0">
		<text:p text:style-name="P3">
		<draw:custom-shape text:anchor-type="char" draw:z-index="1" draw:style-name="gr1" draw:text-style-name="P4" svg:width="12.715cm" svg:height="2.0cm" svg:x="2.293cm" svg:y="1.561cm">
					<text:p text:style-name="P4"><xsl:value-of select="$general.annexe.title"/></text:p><text:p text:style-name="P4"><xsl:value-of select="MODEL/@lib"/></text:p>
					<draw:enhanced-geometry draw:text-path-allowed="true" draw:text-areas="0 0 21600 21600" draw:text-path="true" draw:text-path-mode="shape" draw:text-path-scale="path" draw:text-path-same-letter-heights="false" svg:viewBox="0 0 21600 21600" draw:type="fontwork-plain-text" draw:modifiers="10800" draw:enhanced-path="M ?f3 0 L ?f5 0 N M ?f6 21600 L ?f7 21600 N">
						<draw:equation draw:name="f0" draw:formula="$0 -10800"/>
						<draw:equation draw:name="f1" draw:formula="?f0 *2"/>
						<draw:equation draw:name="f2" draw:formula="abs(?f1 )"/>
						<draw:equation draw:name="f3" draw:formula="if(?f1 ,0,?f2 )"/>
						<draw:equation draw:name="f4" draw:formula="21600-?f2 "/>
						<draw:equation draw:name="f5" draw:formula="if(?f1 ,?f4 ,21600)"/>
						<draw:equation draw:name="f6" draw:formula="if(?f1 ,?f2 ,0)"/>
						<draw:equation draw:name="f7" draw:formula="if(?f1 ,21600,?f4 )"/>
						<draw:handle draw:handle-position="$0 21600" draw:handle-range-x-minimum="6629" draw:handle-range-x-maximum="14971"/>
					</draw:enhanced-geometry>
				</draw:custom-shape>
		</text:p>

      </content>
    </xsl:template>

</xsl:stylesheet> 
