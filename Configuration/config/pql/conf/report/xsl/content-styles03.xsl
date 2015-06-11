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
	<xsl:template match="STYLES03">
		<style:style style:name="P1" style:family="paragraph" style:parent-style-name="Standard" style:master-page-name="Standard">
			<style:paragraph-properties fo:text-align="center" style:justify-single-word="false"/>
			<style:text-properties fo:font-size="22pt" fo:font-style="italic" fo:font-weight="bold" style:font-size-asian="22pt" style:font-style-asian="italic" style:font-weight-asian="bold"/>
		</style:style>
		<style:style style:name="P2" style:family="paragraph" style:parent-style-name="Standard">
			<style:paragraph-properties fo:text-align="center" style:justify-single-word="false"/>
			<style:text-properties fo:font-size="22pt" fo:font-style="italic" fo:font-weight="bold" style:font-size-asian="22pt" style:font-style-asian="italic" style:font-weight-asian="bold"/>
		</style:style>
		<style:style style:name="P3" style:family="paragraph" style:parent-style-name="Standard">
		</style:style>
		<style:style style:name="P4" style:family="paragraph">
			<style:paragraph-properties text:enable-numbering="false" style:writing-mode="lr-tb"/>
			<style:text-properties style:font-name="Arial Black1" fo:letter-spacing="0.001cm" fo:font-style="normal" fo:font-weight="normal" style:text-scale="120%"/>
		</style:style>
		<style:style style:name="P5" style:family="paragraph" style:parent-style-name="Standard">
			<style:paragraph-properties fo:break-before="page"/>
			<style:text-properties fo:language="fr" fo:country="FR"/>
		</style:style>
		<style:style style:name="P6" style:family="paragraph" style:parent-style-name="Standard">
			<style:paragraph-properties>
				<style:tab-stops>
					<style:tab-stop style:position="0.751cm"/>
					<style:tab-stop style:position="1cm"/>
					<style:tab-stop style:position="1.752cm"/>
				</style:tab-stops>
			</style:paragraph-properties>
			<style:text-properties fo:language="fr" fo:country="FR"/>
		</style:style>
		<style:style style:name="P7" style:family="paragraph" style:parent-style-name="Contents_20_Heading">
			<style:text-properties fo:language="fr" fo:country="FR"/>
		</style:style>
		<style:style style:name="P8" style:family="paragraph" style:parent-style-name="Contents_20_1">
			<style:paragraph-properties>
				<style:tab-stops>
					<style:tab-stop style:position="15.87cm" style:type="right" style:leader-style="dotted" style:leader-text="."/>
				</style:tab-stops>
			</style:paragraph-properties>
		</style:style>
		<style:style style:name="P9" style:family="paragraph" style:parent-style-name="Contents_20_2">
			<style:paragraph-properties>
				<style:tab-stops>
					<style:tab-stop style:position="15.371cm" style:type="right" style:leader-style="dotted" style:leader-text="."/>
					<style:tab-stop style:position="17.09cm" style:type="right" style:leader-style="dotted" style:leader-text="."/>
				</style:tab-stops>
			</style:paragraph-properties>
		</style:style>
		<style:style style:name="P10" style:family="paragraph" style:parent-style-name="Contents_20_3">
			<style:paragraph-properties>
				<style:tab-stops>
					<style:tab-stop style:position="14.871cm" style:type="right" style:leader-style="dotted" style:leader-text="."/>
					<style:tab-stop style:position="16.591cm" style:type="right" style:leader-style="dotted" style:leader-text="."/>
				</style:tab-stops>
			</style:paragraph-properties>
		</style:style>
		<style:style style:name="P11" style:family="paragraph" style:parent-style-name="Contents_20_4">
			<style:paragraph-properties>
				<style:tab-stops>
					<style:tab-stop style:position="14.372cm" style:type="right" style:leader-style="dotted" style:leader-text="."/>
					<style:tab-stop style:position="16.092cm" style:type="right" style:leader-style="dotted" style:leader-text="."/>
				</style:tab-stops>
			</style:paragraph-properties>
		</style:style>
		<style:style style:name="P12" style:family="paragraph" style:parent-style-name="Standard">
			<style:paragraph-properties fo:text-align="center" style:justify-single-word="false">
				<style:tab-stops>
					<style:tab-stop style:position="15.87cm" style:type="right" style:leader-style="dotted" style:leader-text="."/>
				</style:tab-stops>
			</style:paragraph-properties>
			<style:text-properties fo:font-size="14pt" fo:language="fr" fo:country="FR" fo:font-weight="bold" style:font-size-asian="14pt" style:font-weight-asian="bold" style:font-size-complex="14pt"/>
		</style:style>
		<style:style style:name="P13" style:family="paragraph" style:parent-style-name="Standard">
			<style:paragraph-properties fo:text-align="center" style:justify-single-word="false"/>
			<style:text-properties fo:font-size="14pt" fo:language="fr" fo:country="FR" fo:font-weight="bold" style:font-size-asian="14pt" style:font-weight-asian="bold" style:font-size-complex="14pt"/>
		</style:style>
		<style:style style:name="P14" style:family="paragraph" style:parent-style-name="Illustration_20_Index_20_Heading">
			<style:text-properties fo:language="fr" fo:country="FR"/>
		</style:style>
		<style:style style:name="P15" style:family="paragraph" style:parent-style-name="User_20_Index_20_Heading">
			<style:text-properties fo:language="fr" fo:country="FR"/>
		</style:style>
		
		<style:style style:name="P16" style:family="paragraph" style:parent-style-name="Heading_20_1">
			<style:paragraph-properties>
				<style:tab-stops>
					<style:tab-stop style:position="0.762cm"/>
				</style:tab-stops>
			</style:paragraph-properties>
			<style:text-properties style:font-size="14pt" fo:font-weight="bold" style:font-size-asian="14pt" style:font-weight-asian="bold"/>
		</style:style>
		
		<style:style style:name="P17" style:family="paragraph" style:parent-style-name="Heading_20_2">
			<style:paragraph-properties>
				<style:tab-stops>
					<style:tab-stop style:position="1.016cm"/>
				</style:tab-stops>
			</style:paragraph-properties>
			<style:text-properties style:font-size="12pt" fo:font-style="italic" fo:font-weight="bold" style:font-size-asian="12pt" style:font-style-asian="italic" style:font-weight-asian="bold"/>
		</style:style>

		<style:style style:name="P17_BREAK" style:family="paragraph" style:parent-style-name="Heading_20_2">
			<style:paragraph-properties fo:break-before="page">
				<style:tab-stops>
					<style:tab-stop style:position="1.016cm"/>
				</style:tab-stops>
			</style:paragraph-properties>
		</style:style>

		<style:style style:name="DETAILCRIT_HEAD" style:family="paragraph" style:parent-style-name="Heading_20_2">
			<style:paragraph-properties fo:break-before="page">
			</style:paragraph-properties>
		</style:style>

		<style:style style:name="P18" style:family="paragraph" style:parent-style-name="Heading_20_3">
			<style:paragraph-properties>
				<style:tab-stops>
					<style:tab-stop style:position="1.27cm"/>
				</style:tab-stops>
			</style:paragraph-properties>
		</style:style>
		<style:style style:name="P19" style:family="paragraph" style:parent-style-name="Standard">
			<style:text-properties fo:language="fr" fo:country="FR" fo:background-color="#ffff00"/>
		</style:style>
		<style:style style:name="P20" style:family="paragraph" style:parent-style-name="Heading_20_3">
			<style:paragraph-properties>
				<style:tab-stops>
					<style:tab-stop style:position="1.27cm"/>
				</style:tab-stops>
			</style:paragraph-properties>
			<style:text-properties fo:language="fr" fo:country="FR"/>
		</style:style>
		<style:style style:name="P21" style:family="paragraph" style:parent-style-name="Heading_20_4">
			<style:paragraph-properties>
				<style:tab-stops>
					<style:tab-stop style:position="1.524cm"/>
				</style:tab-stops>
			</style:paragraph-properties>
			<style:text-properties fo:language="fr" fo:country="FR"/>
		</style:style>
		<style:style style:name="P22" style:family="paragraph" style:parent-style-name="Standard">
			<style:text-properties fo:language="fr" fo:country="FR"/>
		</style:style>
		<style:style style:name="P23" style:family="paragraph" style:parent-style-name="Standard">
			<style:text-properties fo:background-color="#ffff00"/>
		</style:style>
		<style:style style:name="P24" style:family="paragraph" style:parent-style-name="Standard" style:list-style-name="WW8Num2">
			<style:paragraph-properties fo:margin-left="0.404cm" fo:margin-right="0cm" fo:text-indent="0cm" style:auto-text-indent="false">
				<style:tab-stops>
					<style:tab-stop style:position="0.635cm"/>
				</style:tab-stops>
			</style:paragraph-properties>
		</style:style>
		<style:style style:name="P25" style:family="paragraph" style:parent-style-name="Table">
			<style:paragraph-properties fo:keep-with-next="always"/>
		</style:style>
		<style:style style:name="P26" style:family="paragraph" style:parent-style-name="Standard">
			<style:paragraph-properties fo:margin-top="0.026cm" fo:margin-bottom="0cm" fo:text-align="center" style:justify-single-word="false" fo:background-color="#ff0000" style:snap-to-layout-grid="false">
				<style:background-image/>
			</style:paragraph-properties>
			<style:text-properties fo:color="#000000" style:font-name="Arial Black" fo:font-weight="bold" style:font-weight-asian="bold" style:font-weight-complex="bold"/>
		</style:style>
		<style:style style:name="P27" style:family="paragraph" style:parent-style-name="Standard">
			<style:paragraph-properties fo:text-align="start" style:justify-single-word="false" fo:background-color="#ff0000" style:snap-to-layout-grid="false">
				<style:background-image/>
			</style:paragraph-properties>
		</style:style>
		<style:style style:name="P28" style:family="paragraph" style:parent-style-name="Text_20_body">
			<style:paragraph-properties style:snap-to-layout-grid="false"/>
		</style:style>
		<style:style style:name="P29" style:family="paragraph" style:parent-style-name="Standard">
			<style:paragraph-properties fo:margin-top="0.026cm" fo:margin-bottom="0cm" fo:text-align="center" style:justify-single-word="false" style:snap-to-layout-grid="false"/>
			<style:text-properties fo:color="#000000" style:font-name="Arial Black" fo:font-weight="bold" style:font-weight-asian="bold" style:font-weight-complex="bold"/>
		</style:style>
		<style:style style:name="P30" style:family="paragraph" style:parent-style-name="Standard">
			<style:paragraph-properties fo:text-align="start" style:justify-single-word="false" style:snap-to-layout-grid="false"/>
			<style:text-properties fo:font-style="italic" style:font-style-asian="italic" style:font-style-complex="italic"/>
		</style:style>
		<style:style style:name="P31" style:family="paragraph" style:parent-style-name="Text_20_body">
			<style:paragraph-properties style:snap-to-layout-grid="false"/>
			<style:text-properties fo:language="fr" fo:country="FR"/>
		</style:style>
		<style:style style:name="P32" style:family="paragraph" style:parent-style-name="Standard">
			<style:paragraph-properties fo:text-align="start" style:justify-single-word="false" style:snap-to-layout-grid="false"/>
		</style:style>
		<style:style style:name="P33" style:family="paragraph" style:parent-style-name="Heading_20_4">
			<style:paragraph-properties>
				<style:tab-stops>
					<style:tab-stop style:position="1.524cm"/>
				</style:tab-stops>
			</style:paragraph-properties>
		</style:style>
		<style:style style:name="P34" style:family="paragraph" style:parent-style-name="Standard" style:list-style-name="WW8Num3">
			<style:paragraph-properties>
				<style:tab-stops>
					<style:tab-stop style:position="1.27cm"/>
				</style:tab-stops>
			</style:paragraph-properties>
			<style:text-properties fo:language="fr" fo:country="FR"/>
		</style:style>
		<style:style style:name="P35" style:family="paragraph" style:parent-style-name="Standard">
			<style:paragraph-properties fo:text-align="center" style:justify-single-word="false"/>
		</style:style>

		<style:style style:name="TableauSyntheseEvolActionPlan" style:family="table">
			<style:table-properties style:width="10.876cm" fo:margin-left="2.094cm" table:align="center" style:writing-mode="lr-tb"/>
		</style:style>
		<style:style style:name="TableauSyntheseEvolActionPlan.A" style:family="table-column">
			<style:table-column-properties style:column-width="5.858cm"/>
		</style:style>
		<style:style style:name="TableauSyntheseEvolActionPlan.B" style:family="table-column">
			<style:table-column-properties style:column-width="5.018cm"/>
		</style:style>
		<style:style style:name="TableauSyntheseEvolActionPlan.1" style:family="table-row">
			<style:table-row-properties style:keep-together="true"/>
		</style:style>
		<style:style style:name="TableauSyntheseEvolActionPlan.A1" style:family="table-cell">
			<style:table-cell-properties style:vertical-align="middle" fo:background-color="#cccccc" fo:padding="0.097cm" fo:border-left="0.002cm solid #000000" fo:border-right="none" fo:border-top="0.002cm solid #000000" fo:border-bottom="0.002cm solid #000000" style:writing-mode="lr-tb">
				<style:background-image/>
			</style:table-cell-properties>
		</style:style>
		<style:style style:name="TableauSyntheseEvolActionPlan.B1" style:family="table-cell">
			<style:table-cell-properties style:vertical-align="middle" fo:padding="0.097cm" fo:border="0.002cm solid #000000" style:writing-mode="lr-tb"/>
		</style:style>

	</xsl:template>
</xsl:stylesheet>
