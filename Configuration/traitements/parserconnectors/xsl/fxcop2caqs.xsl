<?xml version="1.0" encoding="UTF-8"?>

<!--
    Document   : fxcop2caqs.xsl
    Created on : Wed Dec 22 17:03:08 CET 2010
    Author     : cwfr-lizac
    Description:
        Purpose of transformation follows.
-->

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:str="http://xsltsl.org/string" version="1.0">
    <xsl:import href="string.xsl"/>
    <xsl:output method="xml" indent="yes"/>

    <xsl:template name="commonMetrics">
        <metric id="FLEX_ccn" value="{ccn/text()}"/>
        <metric id="FLEX_ncss" value="{ncss/text()}"/>
        <metric id="FLEX_javadocs" value="{javadocs/text()}"/>
        <metric id="FLEX_javadoc_lines" value="{javadoc_lines/text()}"/>
        <metric id="FLEX_multi_comment_lines" value="{multi_comment_lines/text()}"/>
        <metric id="FLEX_single_comment_lines" value="{single_comment_lines/text()}"/>
        <xsl:if test="functions">
            <metric id="FLEX_functions" value="{functions/text()}"/>
        </xsl:if>
        <xsl:if test="classes">
            <metric id="FLEX_classes" value="{classes/text()}"/>
        </xsl:if>
    </xsl:template>

    <xsl:template match="/">
        <JavaStyle>
            <xsl:for-each select="javancss/packages/package">
                <elt type="PKG" name="{name/text()}">
                    <xsl:if test="contains(name/text(),'.') and string-length(name/text())>1">
                        <xsl:variable name='theName'>
                            <xsl:call-template name='str:substring-before-last'>
                                <xsl:with-param name='text' select='name/text()'/>
                                <xsl:with-param name='chars' select='"."'/>
                            </xsl:call-template>
                        </xsl:variable>
                        <parent name="{$theName}"/>
                    </xsl:if>
                    <xsl:call-template name='commonMetrics'/>
                </elt>
            </xsl:for-each>

            <xsl:for-each select="javancss/objects/object">
                <xsl:variable name="objName">
                    <xsl:choose>
                        <xsl:when test="contains(name/text(),'.mxml')">
                            <xsl:value-of select="substring-before(name/text(),'.mxml')"/>
                        </xsl:when>
                        <xsl:otherwise>
                            <xsl:value-of select="name/text()"/>
                        </xsl:otherwise>
                    </xsl:choose>
                </xsl:variable>
                <xsl:variable name="fileName">
                    <xsl:choose>
                        <xsl:when test="contains(name/text(),'.mxml')">
                            <xsl:value-of select="concat(translate($objName,'.','/'),'.mxml')"/>
                        </xsl:when>
                        <xsl:otherwise>
                            <xsl:value-of select="concat(translate($objName,'.','/'),'.as')"/>
                        </xsl:otherwise>
                    </xsl:choose>
                </xsl:variable>
                <elt type="CLS" name="{$objName}" filepath="{$fileName}">

                    <xsl:variable name='theName'>
                        <xsl:choose>
                            <xsl:when test="contains($objName,'.')">
                                <xsl:call-template name='str:substring-before-last'>
                                    <xsl:with-param name='text' select='$objName'/>
                                    <xsl:with-param name='chars' select='"."'/>
                                </xsl:call-template>
                            </xsl:when>
                            <xsl:otherwise>.</xsl:otherwise>
                        </xsl:choose>
                    </xsl:variable>
                    <parent name="{$theName}"/>
                    <xsl:call-template name='commonMetrics'/>
                </elt>
            </xsl:for-each>

            <xsl:for-each select="javancss/functions/function">
                <elt type="MET" name="{name/text()}">
                    <xsl:if test="contains(name/text(),'::')">
                        <parent name="{substring-before(name/text(),'::')}"/>
                        <xsl:call-template name='commonMetrics'/>
                    </xsl:if>
                </elt>
            </xsl:for-each>

        </JavaStyle>
    </xsl:template>
</xsl:stylesheet>

