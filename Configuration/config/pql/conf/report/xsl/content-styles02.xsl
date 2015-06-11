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
		xmlns:office="urn:oasis:names:tc:opendocument:xmlns:office:1.0"
    xmlns:style="urn:oasis:names:tc:opendocument:xmlns:style:1.0"
    xmlns:text="urn:oasis:names:tc:opendocument:xmlns:text:1.0"
    xmlns:table="urn:oasis:names:tc:opendocument:xmlns:table:1.0"
    xmlns:draw="urn:oasis:names:tc:opendocument:xmlns:drawing:1.0"
    xmlns:fo="urn:oasis:names:tc:opendocument:xmlns:xsl-fo-compatible:1.0"
    xmlns:xlink="http://www.w3.org/1999/xlink"
    xmlns:dc="http://purl.org/dc/elements/1.1/"
    xmlns:meta="urn:oasis:names:tc:opendocument:xmlns:meta:1.0"
    xmlns:number="urn:oasis:names:tc:opendocument:xmlns:datastyle:1.0"
    xmlns:svg="urn:oasis:names:tc:opendocument:xmlns:svg-compatible:1.0"
    xmlns:chart="urn:oasis:names:tc:opendocument:xmlns:chart:1.0"
    xmlns:dr3d="urn:oasis:names:tc:opendocument:xmlns:dr3d:1.0"
    xmlns:math="http://www.w3.org/1998/Math/MathML"
    xmlns:form="urn:oasis:names:tc:opendocument:xmlns:form:1.0"
    xmlns:script="urn:oasis:names:tc:opendocument:xmlns:script:1.0"
    xmlns:ooo="http://openoffice.org/2004/office"
    xmlns:ooow="http://openoffice.org/2004/writer"
    xmlns:oooc="http://openoffice.org/2004/calc"
    xmlns:dom="http://www.w3.org/2001/xml-events"
    xmlns:xforms="http://www.w3.org/2002/xforms"
    xmlns:xsd="http://www.w3.org/2001/XMLSchema"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    office:version="1.0">
	<xsl:template match="STYLES02">
		<style:style style:name="TableauRegleCritere" style:family="table">
			<style:table-properties style:width="17cm" fo:margin-left="0cm" table:align="center" style:writing-mode="lr-tb"/>
		</style:style>
		<style:style style:name="TableauRegleCritere.A" style:family="table-column">
			<style:table-column-properties style:column-width="14cm"/>
		</style:style>
		<style:style style:name="TableauRegleCritere.B" style:family="table-column">
			<style:table-column-properties style:column-width="3cm"/>
		</style:style>
		<style:style style:name="TableauRegleCritere.1" style:family="table-row">
			<style:table-row-properties style:keep-together="true"/>
		</style:style>
		<style:style style:name="TableauRegleCritere.A1" style:family="table-cell">
			<style:table-cell-properties style:vertical-align="middle" fo:background-color="#c0c0c0" fo:padding-left="0.123cm" fo:padding-right="0.123cm" fo:padding-top="0cm" fo:padding-bottom="0cm" fo:border-left="0.018cm solid #000000" fo:border-right="0.018cm solid #000000" fo:border-top="0.018cm solid #000000" fo:border-bottom="0.018cm solid #000000" style:writing-mode="lr-tb">
			</style:table-cell-properties>
		</style:style>
		<style:style style:name="TableauRegleCritere.B1" style:family="table-cell">
			<style:table-cell-properties style:vertical-align="middle" fo:background-color="#c0c0c0" fo:padding-left="0.123cm" fo:padding-right="0.123cm" fo:padding-top="0cm" fo:padding-bottom="0cm" fo:border-left="0.018cm solid #000000" fo:border-right="0.018cm solid #000000" fo:border-top="0.018cm solid #000000" fo:border-bottom="0.018cm solid #000000" style:writing-mode="lr-tb">
			</style:table-cell-properties>
		</style:style>
		<style:style style:name="TableauRegleCritere.A2" style:family="table-cell">
			<style:table-cell-properties style:vertical-align="middle" fo:background-color="#ccccff" fo:padding-left="0.123cm" fo:padding-right="0.123cm" fo:padding-top="0cm" fo:padding-bottom="0cm" fo:border-left="0.018cm solid #000000" fo:border-right="0.018cm solid #000000" fo:border-top="0.018cm solid #000000" fo:border-bottom="0.018cm solid #000000" style:writing-mode="lr-tb">
			</style:table-cell-properties>
		</style:style>
		<style:style style:name="TableauRegleCritere.B2" style:family="table-cell">
			<style:table-cell-properties style:vertical-align="middle" fo:padding-left="0.123cm" fo:padding-right="0.123cm" fo:padding-top="0cm" fo:padding-bottom="0cm" fo:border-left="0.018cm solid #000000" fo:border-right="0.018cm solid #000000" fo:border-top="0.018cm solid #000000" fo:border-bottom="0.018cm solid #000000" style:writing-mode="lr-tb"/>
		</style:style>
		
		<style:style style:name="TableauDetailCritere" style:family="table">
			<style:table-properties style:width="25cm" fo:margin-left="0cm" table:align="center" style:writing-mode="lr-tb"/>
		</style:style>
		<style:style style:name="TableauDetailCritere.A" style:family="table-column">
			<style:table-column-properties style:column-width="18cm"/>
		</style:style>
		<style:style style:name="TableauDetailCritere.B" style:family="table-column">
			<style:table-column-properties style:column-width="3cm"/>
		</style:style>
		<style:style style:name="TableauDetailCritere.1" style:family="table-row">
			<style:table-row-properties style:keep-together="true"/>
		</style:style>
		<style:style style:name="TableauDetailCritere.A1" style:family="table-cell">
			<style:table-cell-properties style:vertical-align="middle" fo:background-color="#c0c0c0" fo:padding-left="0.123cm" fo:padding-right="0.123cm" fo:padding-top="0cm" fo:padding-bottom="0cm" fo:border-left="0.018cm solid #000000" fo:border-right="0.018cm solid #000000" fo:border-top="0.018cm solid #000000" fo:border-bottom="0.018cm solid #000000" style:writing-mode="lr-tb">
			</style:table-cell-properties>
		</style:style>
		<style:style style:name="TableauDetailCritere.B1" style:family="table-cell">
			<style:table-cell-properties style:vertical-align="middle" fo:background-color="#c0c0c0" fo:padding-left="0.123cm" fo:padding-right="0.123cm" fo:padding-top="0cm" fo:padding-bottom="0cm" fo:border-left="0.018cm solid #000000" fo:border-right="0.018cm solid #000000" fo:border-top="0.018cm solid #000000" fo:border-bottom="0.018cm solid #000000" style:writing-mode="lr-tb">
			</style:table-cell-properties>
		</style:style>
		<style:style style:name="TableauDetailCritere.B1M" style:family="table-cell">
			<style:table-cell-properties style:vertical-align="middle" fo:background-color="#c0c0c0" fo:padding-left="0.123cm" fo:padding-right="0.123cm" fo:padding-top="0cm" fo:padding-bottom="0cm" fo:border-left="0.018cm solid #000000" fo:border-right="0.018cm solid #000000" fo:border-top="0.018cm solid #000000" fo:border-bottom="0.018cm solid #000000">
			</style:table-cell-properties>
		</style:style>
		<style:style style:name="TableauDetailCritere.A2" style:family="table-cell">
			<style:table-cell-properties style:vertical-align="middle" fo:background-color="#ccccff" fo:padding-left="0.123cm" fo:padding-right="0.123cm" fo:padding-top="0cm" fo:padding-bottom="0cm" fo:border-left="0.018cm solid #000000" fo:border-right="0.018cm solid #000000" fo:border-top="0.018cm solid #000000" fo:border-bottom="0.018cm solid #000000" style:writing-mode="lr-tb">
			</style:table-cell-properties>
		</style:style>
		<style:style style:name="TableauDetailCritere.B2" style:family="table-cell">
			<style:table-cell-properties style:vertical-align="middle" fo:padding-left="0.123cm" fo:padding-right="0.123cm" fo:padding-top="0cm" fo:padding-bottom="0cm" fo:border-left="0.018cm solid #000000" fo:border-right="0.018cm solid #000000" fo:border-top="0.018cm solid #000000" fo:border-bottom="0.018cm solid #000000" style:writing-mode="lr-tb"/>
		</style:style>		
		
		<style:style style:name="TableauSynthVol" style:family="table">
			<style:table-properties style:width="9.843cm" fo:margin-left="2.094cm" table:align="center" style:writing-mode="lr-tb"/>
		</style:style>
		<style:style style:name="TableauSynthVol.A" style:family="table-column">
			<style:table-column-properties style:column-width="8.255cm"/>
		</style:style>
		<style:style style:name="TableauSynthVol.B" style:family="table-column">
			<style:table-column-properties style:column-width="2.588cm"/>
		</style:style>
		<style:style style:name="TableauSynthVol.1" style:family="table-row">
			<style:table-row-properties style:keep-together="false"/>
		</style:style>
		<style:style style:name="TableauSynthVol.A1" style:family="table-cell">
			<style:table-cell-properties style:vertical-align="middle" fo:background-color="#cccccc" fo:padding-left="0.123cm" fo:padding-right="0.123cm" fo:padding-top="0cm" fo:padding-bottom="0cm" fo:border-left="0.018cm solid #000000" fo:border-right="0.018cm solid #000000" fo:border-top="0.018cm solid #000000" fo:border-bottom="0.018cm solid #000000" style:writing-mode="lr-tb">
				<style:background-image/>
			</style:table-cell-properties>
		</style:style>
		<style:style style:name="TableauSynthVol.B1" style:family="table-cell">
			<style:table-cell-properties style:vertical-align="middle" fo:padding-left="0.123cm" fo:padding-right="0.123cm" fo:padding-top="0cm" fo:padding-bottom="0cm" fo:border-left="0.018cm solid #000000" fo:border-right="0.018cm solid #000000" fo:border-top="0.018cm solid #000000" fo:border-bottom="0.018cm solid #000000" style:writing-mode="lr-tb"/>
		</style:style>
		<style:style style:name="TableauSynthVol.A2" style:family="table-cell">
			<style:table-cell-properties style:vertical-align="middle" fo:background-color="#cccccc" fo:padding-left="0.123cm" fo:padding-right="0.123cm" fo:padding-top="0cm" fo:padding-bottom="0cm" fo:border-left="0.018cm solid #000000" fo:border-right="0.018cm solid #000000" fo:border-top="0.018cm solid #000000" fo:border-bottom="0.018cm solid #000000" style:writing-mode="lr-tb">
				<style:background-image/>
			</style:table-cell-properties>
		</style:style>
		<style:style style:name="TableauSynthVol.B2" style:family="table-cell">
			<style:table-cell-properties style:vertical-align="middle" fo:padding-left="0.123cm" fo:padding-right="0.123cm" fo:padding-top="0cm" fo:padding-bottom="0cm" fo:border-left="0.018cm solid #000000" fo:border-right="0.018cm solid #000000" fo:border-top="0.018cm solid #000000" fo:border-bottom="0.018cm solid #000000" style:writing-mode="lr-tb"/>
		</style:style>		
		
		
		<style:style style:name="Tableau6" style:family="table">
			<style:table-properties style:width="11.892cm" fo:margin-left="2.54cm" table:align="center" style:writing-mode="lr-tb"/>
		</style:style>
		<style:style style:name="Tableau6.A" style:family="table-column">
			<style:table-column-properties style:column-width="8.719cm"/>
		</style:style>
		<style:style style:name="Tableau6.B" style:family="table-column">
			<style:table-column-properties style:column-width="3.156cm"/>
		</style:style>
		<style:style style:name="Tableau6.1" style:family="table-row">
			<style:table-row-properties style:keep-together="true"/>
		</style:style>
		<style:style style:name="Tableau6.A1" style:family="table-cell">
			<style:table-cell-properties style:vertical-align="middle" fo:background-color="#cccccc" fo:padding-left="0.123cm" fo:padding-right="0.123cm" fo:padding-top="0cm" fo:padding-bottom="0cm" fo:border-left="0.018cm solid #000000" fo:border-right="0.018cm solid #000000" fo:border-top="0.018cm solid #000000" fo:border-bottom="0.018cm solid #000000" style:writing-mode="lr-tb">
				<style:background-image/>
			</style:table-cell-properties>
		</style:style>
		<style:style style:name="Tableau6.B1" style:family="table-cell">
			<style:table-cell-properties style:vertical-align="middle" fo:padding-left="0.123cm" fo:padding-right="0.123cm" fo:padding-top="0cm" fo:padding-bottom="0cm" fo:border-left="0.018cm solid #000000" fo:border-right="0.018cm solid #000000" fo:border-top="0.018cm solid #000000" fo:border-bottom="0.018cm solid #000000" style:writing-mode="lr-tb"/>
		</style:style>
		<style:style style:name="Tableau6.B3" style:family="table-cell">
			<style:table-cell-properties style:vertical-align="middle" fo:padding-left="0.123cm" fo:padding-right="0.123cm" fo:padding-top="0cm" fo:padding-bottom="0cm" fo:border-left="0.018cm solid #000000" fo:border-right="0.018cm solid #000000" fo:border-top="0.018cm solid #000000" fo:border-bottom="0.018cm solid #000000" style:writing-mode="lr-tb"/>
		</style:style>

		<style:style style:name="TableauJust" style:family="table">
			<style:table-properties style:width="11.892cm" fo:margin-left="2.54cm" table:align="center" style:writing-mode="lr-tb"/>
		</style:style>
		<style:style style:name="TableauJust.A" style:family="table-column">
			<style:table-column-properties style:column-width="8.719cm" style:align="center" />
		</style:style>
		<style:style style:name="TableauJust.1" style:family="table-row">
			<style:table-row-properties style:keep-together="true"/>
		</style:style>
		<style:style style:name="TableauJust.A1" style:family="table-cell">
			<style:table-cell-properties style:align="left" style:vertical-align="middle" fo:background-color="#cccccc" fo:padding-left="0.123cm" fo:padding-right="0.123cm" fo:padding-top="0cm" fo:padding-bottom="0cm" fo:border-left="0.018cm solid #000000" fo:border-right="0.018cm solid #000000" fo:border-top="0.018cm solid #000000" fo:border-bottom="0.018cm solid #000000" style:writing-mode="lr-tb">
				<style:background-image/>
			</style:table-cell-properties>
		</style:style>
		<style:style style:name="TableauJust.A2" style:family="table-cell">
			<style:table-cell-properties style:vertical-align="middle" fo:padding-left="0.123cm" fo:padding-right="0.123cm" fo:padding-top="0cm" fo:padding-bottom="0cm" fo:border-left="0.018cm solid #000000" fo:border-right="0.018cm solid #000000" fo:border-top="0.018cm solid #000000" fo:border-bottom="0.018cm solid #000000" style:writing-mode="lr-tb">
				<style:background-image/>
			</style:table-cell-properties>
		</style:style>

		<style:style style:name="TableauGoalEvol" style:family="table">
			<style:table-properties style:width="11.892cm" fo:margin-left="2.54cm" table:align="center" style:writing-mode="lr-tb"/>
		</style:style>
		<style:style style:name="TableauGoalEvol.A" style:family="table-column">
			<style:table-column-properties style:column-width="8.719cm" style:align="center" />
		</style:style>
		<style:style style:name="TableauGoalEvol.1" style:family="table-row">
			<style:table-row-properties style:keep-together="true"/>
		</style:style>
		<style:style style:name="TableauGoalEvol.A1" style:family="table-cell">
			<style:table-cell-properties style:vertical-align="middle" fo:background-color="#cccccc" fo:padding-left="0.123cm" fo:padding-right="0.123cm" fo:padding-top="0cm" fo:padding-bottom="0cm" fo:border-left="0.018cm solid #000000" fo:border-right="0.018cm solid #000000" fo:border-top="0.018cm solid #000000" fo:border-bottom="0.018cm solid #000000" style:writing-mode="lr-tb">
				<style:background-image/>
			</style:table-cell-properties>
		</style:style>
		<style:style style:name="TableauGoalEvol.A2" style:family="table-cell">
			<style:table-cell-properties style:vertical-align="middle" fo:padding-left="0.123cm" fo:padding-right="0.123cm" fo:padding-top="0cm" fo:padding-bottom="0cm" fo:border-left="0.018cm solid #000000" fo:border-right="0.018cm solid #000000" fo:border-top="0.018cm solid #000000" fo:border-bottom="0.018cm solid #000000" style:writing-mode="lr-tb">
				<style:background-image/>
			</style:table-cell-properties>
		</style:style>

	</xsl:template>
</xsl:stylesheet>
