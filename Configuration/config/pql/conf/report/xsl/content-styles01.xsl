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
	
	<xsl:template match="STYLES01">
		<style:style style:name="Tableau1" style:family="table">
			<style:table-properties style:width="14.028cm" fo:margin-left="1.058cm" table:align="center" style:writing-mode="lr-tb"/>
		</style:style>
		<style:style style:name="Tableau1.A" style:family="table-column">
			<style:table-column-properties style:column-width="0.847cm"/>
		</style:style>
		<style:style style:name="Tableau1.B" style:family="table-column">
			<style:table-column-properties style:column-width="3.052cm"/>
		</style:style>
		<style:style style:name="Tableau1.C" style:family="table-column">
			<style:table-column-properties style:column-width="10.13cm"/>
		</style:style>
		<style:style style:name="Tableau1.1" style:family="table-row">
			<style:table-row-properties style:keep-together="true"/>
		</style:style>
		<style:style style:name="Tableau1.A1" style:family="table-cell">
			<style:table-cell-properties style:vertical-align="middle" fo:background-color="#ff0000" fo:padding-left="0.191cm" fo:padding-right="0.191cm" fo:padding-top="0cm" fo:padding-bottom="0cm" fo:border-left="0.018cm solid #000000" fo:border-right="0.018cm solid #000000" fo:border-top="0.018cm solid #000000" fo:border-bottom="0.018cm solid #000000" style:writing-mode="lr-tb">
				<style:background-image/>
			</style:table-cell-properties>
		</style:style>
		<style:style style:name="Tableau1.C1" style:family="table-cell">
			<style:table-cell-properties style:vertical-align="middle" fo:padding-left="0.191cm" fo:padding-right="0.191cm" fo:padding-top="0cm" fo:padding-bottom="0cm" fo:border-left="0.018cm solid #000000" fo:border-right="0.018cm solid #000000" fo:border-top="0.018cm solid #000000" fo:border-bottom="0.018cm solid #000000" style:writing-mode="lr-tb"/>
		</style:style>
		<style:style style:name="Tableau1.2" style:family="table-row">
			<style:table-row-properties style:min-row-height="1.519cm" style:keep-together="true"/>
		</style:style>
		<style:style style:name="Tableau1.A2" style:family="table-cell">
			<style:table-cell-properties style:vertical-align="middle" fo:background-color="#ff9966" fo:padding-left="0.191cm" fo:padding-right="0.191cm" fo:padding-top="0cm" fo:padding-bottom="0cm" fo:border-left="0.018cm solid #000000" fo:border-right="0.018cm solid #000000" fo:border-top="0.018cm solid #000000" fo:border-bottom="0.018cm solid #000000" style:writing-mode="lr-tb">
				<style:background-image/>
			</style:table-cell-properties>
		</style:style>
		<style:style style:name="Tableau1.C2" style:family="table-cell">
			<style:table-cell-properties style:vertical-align="middle" fo:padding-left="0.191cm" fo:padding-right="0.191cm" fo:padding-top="0cm" fo:padding-bottom="0cm" fo:border-left="0.018cm solid #000000" fo:border-right="0.018cm solid #000000" fo:border-top="0.018cm solid #000000" fo:border-bottom="0.018cm solid #000000" style:writing-mode="lr-tb"/>
		</style:style>
		<style:style style:name="Tableau1.A3" style:family="table-cell">
			<style:table-cell-properties style:vertical-align="middle" fo:background-color="#008000" fo:padding-left="0.191cm" fo:padding-right="0.191cm" fo:padding-top="0cm" fo:padding-bottom="0cm" fo:border-left="0.018cm solid #000000" fo:border-right="0.018cm solid #000000" fo:border-top="0.018cm solid #000000" fo:border-bottom="0.018cm solid #000000" style:writing-mode="lr-tb">
				<style:background-image/>
			</style:table-cell-properties>
		</style:style>
		<style:style style:name="Tableau1.A4" style:family="table-cell">
			<style:table-cell-properties style:vertical-align="middle" fo:background-color="#00ff00" fo:padding-left="0.191cm" fo:padding-right="0.191cm" fo:padding-top="0cm" fo:padding-bottom="0cm" fo:border-left="0.018cm solid #000000" fo:border-right="0.018cm solid #000000" fo:border-top="0.018cm solid #000000" fo:border-bottom="0.018cm solid #000000" style:writing-mode="lr-tb">
				<style:background-image/>
			</style:table-cell-properties>
		</style:style>
		
		
		<style:style style:name="Tableau2" style:family="table">
			<style:table-properties style:width="10.876cm" fo:margin-left="2.094cm" table:align="center" style:writing-mode="lr-tb"/>
		</style:style>
		<style:style style:name="Tableau2.A" style:family="table-column">
			<style:table-column-properties style:column-width="5.858cm"/>
		</style:style>
		<style:style style:name="Tableau2.B" style:family="table-column">
			<style:table-column-properties style:column-width="5.018cm"/>
		</style:style>
		<style:style style:name="Tableau2.1" style:family="table-row">
			<style:table-row-properties style:keep-together="true"/>
		</style:style>
		<style:style style:name="Tableau2.A1" style:family="table-cell">
			<style:table-cell-properties style:vertical-align="middle" fo:background-color="#cccccc" fo:padding="0.097cm" fo:border-left="0.002cm solid #000000" fo:border-right="none" fo:border-top="0.002cm solid #000000" fo:border-bottom="0.002cm solid #000000" style:writing-mode="lr-tb">
				<style:background-image/>
			</style:table-cell-properties>
		</style:style>
		<style:style style:name="Tableau2.B1" style:family="table-cell">
			<style:table-cell-properties style:vertical-align="middle" fo:padding="0.097cm" fo:border="0.002cm solid #000000" style:writing-mode="lr-tb"/>
		</style:style>
		<style:style style:name="Tableau2.A2" style:family="table-cell">
			<style:table-cell-properties style:vertical-align="middle" fo:background-color="#cccccc" fo:padding="0.097cm" fo:border-left="0.002cm solid #000000" fo:border-right="none" fo:border-top="none" fo:border-bottom="0.002cm solid #000000" style:writing-mode="lr-tb">
				<style:background-image/>
			</style:table-cell-properties>
		</style:style>
		<style:style style:name="Tableau2.B2" style:family="table-cell">
			<style:table-cell-properties style:vertical-align="middle" fo:padding="0.097cm" fo:border-left="0.002cm solid #000000" fo:border-right="0.002cm solid #000000" fo:border-top="none" fo:border-bottom="0.002cm solid #000000" style:writing-mode="lr-tb"/>
		</style:style>
		
		
		<style:style style:name="Tableau3" style:family="table">
			<style:table-properties style:width="10.897cm" fo:margin-left="2.088cm" table:align="center" style:writing-mode="lr-tb"/>
		</style:style>
		<style:style style:name="Tableau3.A" style:family="table-column">
			<style:table-column-properties style:column-width="5.872cm"/>
		</style:style>
		<style:style style:name="Tableau3.B" style:family="table-column">
			<style:table-column-properties style:column-width="5.025cm"/>
		</style:style>
		<style:style style:name="Tableau3.1" style:family="table-row">
			<style:table-row-properties style:keep-together="true"/>
		</style:style>
		<style:style style:name="Tableau3.A1" style:family="table-cell">
			<style:table-cell-properties style:vertical-align="middle" fo:background-color="#cccccc" fo:padding="0.097cm" fo:border-left="0.002cm solid #000000" fo:border-right="none" fo:border-top="0.002cm solid #000000" fo:border-bottom="0.002cm solid #000000" style:writing-mode="lr-tb">
				<style:background-image/>
			</style:table-cell-properties>
		</style:style>
		<style:style style:name="Tableau3.B1" style:family="table-cell">
			<style:table-cell-properties style:vertical-align="middle" fo:padding="0.097cm" fo:border="0.002cm solid #000000" style:writing-mode="lr-tb"/>
		</style:style>
		<style:style style:name="Tableau3.A2" style:family="table-cell">
			<style:table-cell-properties style:vertical-align="middle" fo:background-color="#cccccc" fo:padding="0.097cm" fo:border-left="0.002cm solid #000000" fo:border-right="none" fo:border-top="none" fo:border-bottom="0.002cm solid #000000" style:writing-mode="lr-tb">
				<style:background-image/>
			</style:table-cell-properties>
		</style:style>
		<style:style style:name="Tableau3.B2" style:family="table-cell">
			<style:table-cell-properties style:vertical-align="middle" fo:padding="0.097cm" fo:border-left="0.002cm solid #000000" fo:border-right="0.002cm solid #000000" fo:border-top="none" fo:border-bottom="0.002cm solid #000000" style:writing-mode="lr-tb"/>
		</style:style>
		
		
		<style:style style:name="Tableau4" style:family="table">
			<style:table-properties style:width="15.873cm" table:align="center" style:writing-mode="lr-tb"/>
		</style:style>
		<style:style style:name="Tableau4.A" style:family="table-column">
			<style:table-column-properties style:column-width="3.759cm"/>
		</style:style>
		<style:style style:name="Tableau4.B" style:family="table-column">
			<style:table-column-properties style:column-width="1.812cm"/>
		</style:style>
		<style:style style:name="Tableau4.D" style:family="table-column">
			<style:table-column-properties style:column-width="3.012cm"/>
		</style:style>
		<style:style style:name="Tableau4.C" style:family="table-column">
			<style:table-column-properties style:column-width="10.303cm"/>
		</style:style>
		<style:style style:name="Tableau4.1" style:family="table-row">
			<style:table-row-properties style:keep-together="true"/>
		</style:style>
		<style:style style:name="Tableau4.A1" style:family="table-cell">
			<style:table-cell-properties style:vertical-align="middle" fo:background-color="#cccccc" fo:padding="0.097cm" fo:border-left="0.002cm solid #000000" fo:border-right="none" fo:border-top="0.002cm solid #000000" fo:border-bottom="0.002cm solid #000000" style:writing-mode="lr-tb">
				<style:background-image/>
			</style:table-cell-properties>
		</style:style>
		<style:style style:name="Tableau4.B1" style:family="table-cell">
			<style:table-cell-properties style:vertical-align="middle" fo:background-color="#cccccc" fo:padding="0.097cm" fo:border-left="0.002cm solid #000000" fo:border-right="none" fo:border-top="0.002cm solid #000000" fo:border-bottom="0.002cm solid #000000" style:writing-mode="lr-tb">
				<style:background-image/>
			</style:table-cell-properties>
		</style:style>
		<style:style style:name="Tableau4.C1" style:family="table-cell">
			<style:table-cell-properties style:vertical-align="middle" fo:background-color="#cccccc" fo:padding="0.097cm" fo:border="0.002cm solid #000000" style:writing-mode="lr-tb">
				<style:background-image/>
			</style:table-cell-properties>
		</style:style>
		<style:style style:name="Tableau4.A2" style:family="table-cell">
			<style:table-cell-properties style:vertical-align="middle" fo:padding="0.097cm" fo:border-left="0.002cm solid #000000" fo:border-right="none" fo:border-top="none" fo:border-bottom="0.002cm solid #000000" style:writing-mode="lr-tb"/>
		</style:style>
		<style:style style:name="Tableau4.C2" style:family="table-cell">
			<style:table-cell-properties style:vertical-align="middle" fo:padding="0.097cm" fo:border-left="0.002cm solid #000000" fo:border-right="0.002cm solid #000000" fo:border-top="none" fo:border-bottom="0.002cm solid #000000" style:writing-mode="lr-tb"/>
		</style:style>
		
		<style:style style:name="Tableau5" style:family="table">
			<style:table-properties style:width="9.663cm" fo:margin-left="2.54cm" table:align="center" style:writing-mode="lr-tb"/>
		</style:style>
		<style:style style:name="Tableau5.A" style:family="table-column">
			<style:table-column-properties style:column-width="7.662cm"/>
		</style:style>
		<style:style style:name="Tableau5.B" style:family="table-column">
			<style:table-column-properties style:column-width="2cm"/>
		</style:style>
		<style:style style:name="Tableau5.1" style:family="table-row">
			<style:table-row-properties style:keep-together="false"/>
		</style:style>
		<style:style style:name="Tableau5.A1" style:family="table-cell">
			<style:table-cell-properties style:vertical-align="middle" fo:background-color="#cccccc" fo:padding-left="0.123cm" fo:padding-right="0.123cm" fo:padding-top="0cm" fo:padding-bottom="0cm" fo:border-left="0.018cm solid #000000" fo:border-right="0.018cm solid #000000" fo:border-top="0.018cm solid #000000" fo:border-bottom="0.018cm solid #000000" style:writing-mode="lr-tb">
				<style:background-image/>
			</style:table-cell-properties>
		</style:style>
		<style:style style:name="Tableau5.B1" style:family="table-cell">
			<style:table-cell-properties style:vertical-align="middle" fo:padding-left="0.123cm" fo:padding-right="0.123cm" fo:padding-top="0cm" fo:padding-bottom="0cm" fo:border-left="0.018cm solid #000000" fo:border-right="0.018cm solid #000000" fo:border-top="0.018cm solid #000000" fo:border-bottom="0.018cm solid #000000" style:writing-mode="lr-tb"/>
		</style:style>
		
		
		<style:style style:name="TableauSynthActionPlan" style:family="table">
			<style:table-properties style:width="18cm" fo:margin-left="0cm" table:align="center" style:writing-mode="lr-tb"/>
		</style:style>
		<style:style style:name="TableauSynthActionPlan.A" style:family="table-column">
			<style:table-column-properties style:column-width="12cm"/>
		</style:style>
		<style:style style:name="TableauSynthActionPlan.B" style:family="table-column">
			<style:table-column-properties style:column-width="2cm"/>
		</style:style>
		<style:style style:name="TableauSynthActionPlan.C" style:family="table-column">
			<style:table-column-properties style:column-width="4cm"/>
		</style:style>
		<style:style style:name="TableauSynthActionPlan.A1" style:family="table-cell">
			<style:table-cell-properties style:vertical-align="middle" fo:background-color="#c0c0c0" fo:padding-left="0.123cm" fo:padding-right="0.123cm" fo:padding-top="0cm" fo:padding-bottom="0cm" fo:border-left="0.018cm solid #000000" fo:border-right="0.018cm solid #000000" fo:border-top="0.018cm solid #000000" fo:border-bottom="0.018cm solid #000000" style:writing-mode="lr-tb">
			</style:table-cell-properties>
		</style:style>
		<style:style style:name="TableauSynthActionPlan.A2" style:family="table-cell">
			<style:table-cell-properties style:vertical-align="middle" fo:background-color="#ccccff" fo:padding-left="0.123cm" fo:padding-right="0.123cm" fo:padding-top="0cm" fo:padding-bottom="0cm" fo:border-left="0.018cm solid #000000" fo:border-right="0.018cm solid #000000" fo:border-top="0.018cm solid #000000" fo:border-bottom="0.018cm solid #000000" style:writing-mode="lr-tb">
			</style:table-cell-properties>
		</style:style>
		<style:style style:name="TableauSynthActionPlan.B2" style:family="table-cell">
			<style:table-cell-properties style:vertical-align="middle" fo:padding-left="0.123cm" fo:padding-right="0.123cm" fo:padding-top="0cm" fo:padding-bottom="0cm" fo:border-left="0.018cm solid #000000" fo:border-right="0.018cm solid #000000" fo:border-top="0.018cm solid #000000" fo:border-bottom="0.018cm solid #000000" style:writing-mode="lr-tb"/>
		</style:style>

		
		<style:style style:name="TableauDetailsActionPlan" style:family="table">
			<style:table-properties style:width="18cm" fo:margin-left="0cm" table:align="center" style:writing-mode="lr-tb"/>
		</style:style>
		<style:style style:name="TableauDetailsActionPlan.B" style:family="table-column">
			<style:table-column-properties style:column-width="2cm"/>
		</style:style>
		<style:style style:name="TableauDetailsActionPlan.B1" style:family="table-cell">
			<style:table-cell-properties style:vertical-align="middle" fo:background-color="#c0c0c0" fo:padding-left="0.123cm" fo:padding-right="0.123cm" fo:padding-top="0cm" fo:padding-bottom="0cm" fo:border-left="0.018cm solid #000000" fo:border-right="0.018cm solid #000000" fo:border-top="0.018cm solid #000000" fo:border-bottom="0.018cm solid #000000" style:writing-mode="lr-tb">
			</style:table-cell-properties>
		</style:style>
		<style:style style:name="TableauDetailsActionPlan.B2" style:family="table-cell">
			<style:table-cell-properties style:vertical-align="middle" fo:padding-left="0.123cm" fo:padding-right="0.123cm" fo:padding-top="0cm" fo:padding-bottom="0cm" fo:border-left="0.018cm solid #000000" fo:border-right="0.018cm solid #000000" fo:border-top="0.018cm solid #000000" fo:border-bottom="0.018cm solid #000000" style:writing-mode="lr-tb"/>
		</style:style>		

		
		<style:style style:name="TableauSynthObjectifs" style:family="table">
			<style:table-properties style:width="18cm" fo:margin-left="0cm" table:align="center" style:writing-mode="lr-tb"/>
		</style:style>
		<style:style style:name="TableauSynthObjectifs.A" style:family="table-column">
			<style:table-column-properties style:column-width="12cm"/>
		</style:style>
		<style:style style:name="TableauSynthObjectifs.B" style:family="table-column">
			<style:table-column-properties style:column-width="2cm"/>
		</style:style>
		<style:style style:name="TableauSynthObjectifs.C" style:family="table-column">
			<style:table-column-properties style:column-width="2cm"/>
		</style:style>
		<style:style style:name="TableauSynthObjectifs.1" style:family="table-row">
			<style:table-row-properties style:keep-together="true"/>
		</style:style>
		<style:style style:name="TableauSynthObjectifs.A1" style:family="table-cell">
			<style:table-cell-properties style:vertical-align="middle" fo:background-color="#c0c0c0" fo:padding-left="0.123cm" fo:padding-right="0.123cm" fo:padding-top="0cm" fo:padding-bottom="0cm" fo:border-left="0.018cm solid #000000" fo:border-right="0.018cm solid #000000" fo:border-top="0.018cm solid #000000" fo:border-bottom="0.018cm solid #000000" style:writing-mode="lr-tb">
			</style:table-cell-properties>
		</style:style>
		<style:style style:name="TableauSynthObjectifs.B1" style:family="table-cell">
			<style:table-cell-properties style:vertical-align="middle" fo:background-color="#c0c0c0" fo:padding-left="0.123cm" fo:padding-right="0.123cm" fo:padding-top="0cm" fo:padding-bottom="0cm" fo:border-left="0.018cm solid #000000" fo:border-right="0.018cm solid #000000" fo:border-top="0.018cm solid #000000" fo:border-bottom="0.018cm solid #000000" style:writing-mode="lr-tb">
			</style:table-cell-properties>
		</style:style>
		<style:style style:name="TableauSynthObjectifs.C1" style:family="table-cell">
			<style:table-cell-properties style:vertical-align="middle" fo:background-color="#c0c0c0" fo:padding-left="0.123cm" fo:padding-right="0.123cm" fo:padding-top="0cm" fo:padding-bottom="0cm" fo:border-left="0.018cm solid #000000" fo:border-right="0.018cm solid #000000" fo:border-top="0.018cm solid #000000" fo:border-bottom="0.018cm solid #000000" style:writing-mode="lr-tb">
			</style:table-cell-properties>
		</style:style>
		<style:style style:name="TableauSynthObjectifs.A2" style:family="table-cell">
			<style:table-cell-properties style:vertical-align="middle" fo:background-color="#ccccff" fo:padding-left="0.123cm" fo:padding-right="0.123cm" fo:padding-top="0cm" fo:padding-bottom="0cm" fo:border-left="0.018cm solid #000000" fo:border-right="0.018cm solid #000000" fo:border-top="0.018cm solid #000000" fo:border-bottom="0.018cm solid #000000" style:writing-mode="lr-tb">
			</style:table-cell-properties>
		</style:style>
		<style:style style:name="TableauSynthObjectifs.B2" style:family="table-cell">
			<style:table-cell-properties style:vertical-align="middle" fo:padding-left="0.123cm" fo:padding-right="0.123cm" fo:padding-top="0cm" fo:padding-bottom="0cm" fo:border-left="0.018cm solid #000000" fo:border-right="0.018cm solid #000000" fo:border-top="0.018cm solid #000000" fo:border-bottom="0.018cm solid #000000" style:writing-mode="lr-tb"/>
		</style:style>		
		<style:style style:name="TableauSynthObjectifs.C2" style:family="table-cell">
			<style:table-cell-properties style:vertical-align="middle" fo:padding-left="0.123cm" fo:padding-right="0.123cm" fo:padding-top="0cm" fo:padding-bottom="0cm" fo:border-left="0.018cm solid #000000" fo:border-right="0.018cm solid #000000" fo:border-top="0.018cm solid #000000" fo:border-bottom="0.018cm solid #000000" style:writing-mode="lr-tb"/>
		</style:style>		
		<style:style style:name="TableauSynthObjectifs.C2LIGHTGREEN" style:family="table-cell">
			<style:table-cell-properties style:vertical-align="middle" fo:background-color="#00FF00" fo:padding-left="0.123cm" fo:padding-right="0.123cm" fo:padding-top="0cm" fo:padding-bottom="0cm" fo:border-left="0.018cm solid #000000" fo:border-right="0.018cm solid #000000" fo:border-top="0.018cm solid #000000" fo:border-bottom="0.018cm solid #000000" style:writing-mode="lr-tb"/>
		</style:style>		
		<style:style style:name="TableauSynthObjectifs.C2GREEN" style:family="table-cell">
			<style:table-cell-properties style:vertical-align="middle" fo:background-color="#00F000" fo:padding-left="0.123cm" fo:padding-right="0.123cm" fo:padding-top="0cm" fo:padding-bottom="0cm" fo:border-left="0.018cm solid #000000" fo:border-right="0.018cm solid #000000" fo:border-top="0.018cm solid #000000" fo:border-bottom="0.018cm solid #000000" style:writing-mode="lr-tb"/>
		</style:style>		
		<style:style style:name="TableauSynthObjectifs.C2YELLOW" style:family="table-cell">
			<style:table-cell-properties style:vertical-align="middle" fo:background-color="#FFFF00" fo:padding-left="0.123cm" fo:padding-right="0.123cm" fo:padding-top="0cm" fo:padding-bottom="0cm" fo:border-left="0.018cm solid #000000" fo:border-right="0.018cm solid #000000" fo:border-top="0.018cm solid #000000" fo:border-bottom="0.018cm solid #000000" style:writing-mode="lr-tb"/>
		</style:style>		
		<style:style style:name="TableauSynthObjectifs.C2RED" style:family="table-cell">
			<style:table-cell-properties style:vertical-align="middle" fo:background-color="#FF0000" fo:padding-left="0.123cm" fo:padding-right="0.123cm" fo:padding-top="0cm" fo:padding-bottom="0cm" fo:border-left="0.018cm solid #000000" fo:border-right="0.018cm solid #000000" fo:border-top="0.018cm solid #000000" fo:border-bottom="0.018cm solid #000000" style:writing-mode="lr-tb"/>
		</style:style>
	</xsl:template>
</xsl:stylesheet>
