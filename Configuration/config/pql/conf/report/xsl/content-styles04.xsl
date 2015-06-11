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
		<xsl:template match="STYLES04">
		<style:style style:name="P36" style:family="paragraph" style:parent-style-name="Standard" style:list-style-name="WW8Num4">
			<style:paragraph-properties>
				<style:tab-stops>
					<style:tab-stop style:position="1.27cm"/>
				</style:tab-stops>
			</style:paragraph-properties>
			<style:text-properties fo:language="fr" fo:country="FR"/>
		</style:style>
		<style:style style:name="P37" style:family="paragraph" style:parent-style-name="Standard" style:list-style-name="WW8Num5">
			<style:paragraph-properties>
				<style:tab-stops>
					<style:tab-stop style:position="1.27cm"/>
				</style:tab-stops>
			</style:paragraph-properties>
			<style:text-properties fo:language="fr" fo:country="FR"/>
		</style:style>
		<style:style style:name="P38" style:family="paragraph" style:parent-style-name="Standard">
			<style:paragraph-properties fo:margin-left="1.27cm" fo:margin-right="0cm" fo:text-indent="0cm" style:auto-text-indent="false"/>
			<style:text-properties fo:language="fr" fo:country="FR"/>
		</style:style>
		<style:style style:name="P39" style:family="paragraph" style:parent-style-name="Standard" style:list-style-name="WW8Num6">
			<style:paragraph-properties>
				<style:tab-stops>
					<style:tab-stop style:position="1.27cm"/>
				</style:tab-stops>
			</style:paragraph-properties>
			<style:text-properties fo:language="fr" fo:country="FR"/>
		</style:style>
		<style:style style:name="P40" style:family="paragraph" style:parent-style-name="Heading_20_1">
			<style:paragraph-properties>
				<style:tab-stops>
					<style:tab-stop style:position="0.762cm"/>
				</style:tab-stops>
			</style:paragraph-properties>
			<style:text-properties fo:language="fr" fo:country="FR"/>
		</style:style>
		<style:style style:name="P41" style:family="paragraph" style:parent-style-name="Table_20_Contents">
			<style:paragraph-properties style:snap-to-layout-grid="false"/>
			<style:text-properties fo:font-weight="bold" style:font-weight-asian="bold" style:font-weight-complex="bold"/>
		</style:style>
		<style:style style:name="P42" style:family="paragraph" style:parent-style-name="Table_20_Contents">
			<style:paragraph-properties style:snap-to-layout-grid="false"/>
		</style:style>
		<style:style style:name="P42Right" style:family="paragraph" style:parent-style-name="Table_20_Contents">
			<style:paragraph-properties fo:text-align="end" style:snap-to-layout-grid="false"/>
		</style:style>
		<style:style style:name="P43" style:family="paragraph" style:parent-style-name="Table_20_Contents">
			<style:paragraph-properties style:snap-to-layout-grid="false"/>
			<style:text-properties fo:language="fr" fo:country="FR" fo:font-weight="bold" style:font-weight-asian="bold" style:font-weight-complex="bold"/>
		</style:style>
		<style:style style:name="P44" style:family="paragraph" style:parent-style-name="Table_20_Contents">
			<style:paragraph-properties style:snap-to-layout-grid="false"/>
			<style:text-properties fo:language="fr" fo:country="FR"/>
		</style:style>
		<style:style style:name="P45" style:family="paragraph" style:parent-style-name="Heading_20_3">
			<style:paragraph-properties fo:margin-left="1.27cm" fo:margin-right="0cm" fo:text-indent="0cm" style:auto-text-indent="false"/>
		</style:style>
		<style:style style:name="P46" style:family="paragraph" style:parent-style-name="Table_20_Heading">
			<style:paragraph-properties style:snap-to-layout-grid="false"/>
		</style:style>
		<style:style style:name="P46Left" style:family="paragraph" style:parent-style-name="Table_20_Heading">
			<style:paragraph-properties fo:text-align="start" style:snap-to-layout-grid="false"/>
		</style:style>
		<style:style style:name="P47" style:family="paragraph" style:parent-style-name="Heading_20_2">
			<style:paragraph-properties>
				<style:tab-stops>
					<style:tab-stop style:position="1.016cm"/>
				</style:tab-stops>
			</style:paragraph-properties>
			<style:text-properties fo:language="fr" fo:country="FR"/>
		</style:style>
		<style:style style:name="P48" style:family="paragraph" style:parent-style-name="Tableau_20_M_2f_B">
			<style:paragraph-properties style:snap-to-layout-grid="false"/>
			<style:text-properties fo:color="#000000" fo:font-weight="normal" style:font-weight-asian="normal"/>
		</style:style>
		<style:style style:name="P49" style:family="paragraph" style:parent-style-name="Tableau_20_N_2f_B">
			<style:paragraph-properties fo:text-align="center" style:justify-single-word="false" style:snap-to-layout-grid="false"/>
		</style:style>
		<style:style style:name="P50" style:family="paragraph" style:parent-style-name="Tableau_20_M_2f_B">
			<style:paragraph-properties style:snap-to-layout-grid="false"/>
			<style:text-properties fo:color="#000000" fo:language="fr" fo:country="FR" fo:font-weight="normal" style:font-weight-asian="normal"/>
		</style:style>
		<style:style style:name="P51" style:family="paragraph" style:parent-style-name="Tableau_20_N_2f_B">
			<style:paragraph-properties fo:text-align="center" style:justify-single-word="false" style:snap-to-layout-grid="false"/>
			<style:text-properties fo:language="fr" fo:country="FR"/>
		</style:style>
		<style:style style:name="P52" style:family="paragraph" style:parent-style-name="Heading_20_2">
			<style:paragraph-properties fo:margin-left="1.016cm" fo:margin-right="0cm" fo:text-indent="0cm" style:auto-text-indent="false"/>
			<style:text-properties fo:language="fr" fo:country="FR"/>
		</style:style>
		<style:style style:name="P53" style:family="paragraph" style:parent-style-name="Tableau_20_M_2f_B">
			<style:paragraph-properties fo:text-align="start" style:justify-single-word="false" style:snap-to-layout-grid="false"/>
			<style:text-properties fo:color="#000000" fo:font-size="10pt" fo:language="fr" fo:country="FR" fo:font-weight="normal" style:font-size-asian="10pt" style:font-weight-asian="normal" style:font-size-complex="10pt"/>
		</style:style>
		<style:style style:name="P54" style:family="paragraph" style:parent-style-name="Tableau_20_M_2f_B">
			<style:paragraph-properties fo:text-align="start" style:justify-single-word="false" style:snap-to-layout-grid="false"/>
			<style:text-properties fo:color="#000000" fo:font-size="10pt" fo:font-weight="normal" style:font-size-asian="10pt" style:font-weight-asian="normal" style:font-size-complex="10pt"/>
		</style:style>
		<style:style style:name="P55" style:family="paragraph" style:parent-style-name="Text_20_body">
			<style:text-properties fo:font-weight="bold" style:font-weight-asian="bold"/>
		</style:style>
		<style:style style:name="P56" style:family="paragraph" style:parent-style-name="Text_20_body">
			<style:text-properties fo:language="fr" fo:country="FR"/>
		</style:style>
		<style:style style:name="P57" style:family="paragraph" style:parent-style-name="Text_20_body" style:list-style-name="WW8Num7">
			<style:paragraph-properties>
				<style:tab-stops>
					<style:tab-stop style:position="1.27cm"/>
				</style:tab-stops>
			</style:paragraph-properties>
		</style:style>
		<style:style style:name="P58" style:family="paragraph" style:parent-style-name="Standard">
			<style:paragraph-properties fo:text-align="center" style:justify-single-word="false"/>
			<style:text-properties fo:language="fr" fo:country="FR"/>
		</style:style>
		
		<style:style style:name="P59" style:family="paragraph" style:parent-style-name="Heading_20_1" style:master-page-name="Conversion_20_1"/>
		<style:style style:name="P60" style:family="paragraph" style:parent-style-name="Heading_20_1" style:master-page-name="Standard"/>
		<style:style style:name="P61" style:family="paragraph" style:parent-style-name="Heading_20_3" style:master-page-name="Conversion_20_1">
			<style:paragraph-properties>
				<style:tab-stops>
					<style:tab-stop style:position="1.27cm"/>
				</style:tab-stops>
			</style:paragraph-properties>
		</style:style>
		
		<style:style style:name="T1" style:family="text">
			<style:text-properties fo:background-color="#ffff00"/>
		</style:style>
		<style:style style:name="T2" style:family="text">
		</style:style>
		<style:style style:name="fr1" style:family="graphic" style:parent-style-name="Graphics">
			<style:graphic-properties fo:margin-left="0cm" fo:margin-right="0cm" style:vertical-pos="top" style:vertical-rel="baseline" fo:background-color="#ffffff" style:background-transparency="0%" fo:padding-left="0.28cm" fo:padding-right="0.28cm" fo:padding-top="0.153cm" fo:padding-bottom="0.153cm" fo:border="none" style:mirror="none" fo:clip="rect(0cm 0cm 0cm 0cm)" draw:luminance="0%" draw:contrast="0%" draw:red="0%" draw:green="0%" draw:blue="0%" draw:gamma="100%" draw:color-inversion="false" draw:image-opacity="100%" draw:color-mode="standard">
				<style:background-image/>
			</style:graphic-properties>
		</style:style>
		<style:style style:name="fr2" style:family="graphic" style:parent-style-name="Frame">
			<style:graphic-properties style:wrap="run-through" style:number-wrapped-paragraphs="no-limit" style:vertical-pos="from-top" style:horizontal-pos="from-left" style:horizontal-rel="char" fo:background-color="#ffffff" style:background-transparency="0%" fo:padding="0.026cm" fo:border="none">
				<style:background-image/>
			</style:graphic-properties>
		</style:style>
		<style:style style:name="fr3" style:family="graphic" style:parent-style-name="Frame">
			<style:graphic-properties style:wrap="none" style:vertical-pos="from-top" style:vertical-rel="paragraph" style:horizontal-pos="center" style:horizontal-rel="paragraph" fo:background-color="#ffffff" style:background-transparency="0%" fo:padding="0.026cm" fo:border="none">
				<style:background-image/>
			</style:graphic-properties>
		</style:style>
		<style:style style:name="fr4" style:family="graphic" style:parent-style-name="Frame">
			<style:graphic-properties style:wrap="run-through" style:number-wrapped-paragraphs="no-limit" style:vertical-pos="from-top" style:horizontal-pos="from-left" style:horizontal-rel="char" fo:background-color="#ffffff" style:background-transparency="0%" fo:padding-left="0.263cm" fo:padding-right="0.263cm" fo:padding-top="0.136cm" fo:padding-bottom="0.136cm" fo:border="0.018cm solid #0000ff">
				<style:background-image/>
			</style:graphic-properties>
		</style:style>
		<style:style style:name="Sect1" style:family="section">
			<style:section-properties style:writing-mode="lr-tb" style:editable="false">
				<style:columns fo:column-count="0" fo:column-gap="0cm"/>
				<text:notes-configuration text:note-class="footnote"/>
			</style:section-properties>
		</style:style>
		<style:style style:name="Sect2" style:family="section">
			<style:section-properties style:editable="false">
				<style:columns fo:column-count="0" fo:column-gap="0cm"/>
			</style:section-properties>
		</style:style>
		<style:style style:name="Sect3" style:family="section">
			<style:section-properties text:dont-balance-text-columns="true" style:writing-mode="lr-tb" style:editable="false">
				<style:columns fo:column-count="0" fo:column-gap="0cm"/>
				<text:notes-configuration text:note-class="footnote"/>
			</style:section-properties>
		</style:style>
		<style:style style:name="gr1" style:family="graphic">
			<style:graphic-properties draw:stroke="solid" svg:stroke-width="0.026cm" svg:stroke-color="#000000" draw:stroke-linejoin="miter" draw:fill="solid" draw:fill-color="#ffffff" draw:textarea-horizontal-align="center" draw:textarea-vertical-align="middle" draw:auto-grow-height="false" draw:fit-to-size="false" fo:padding-top="0.229cm" fo:padding-bottom="0.229cm" fo:padding-left="0.441cm" fo:padding-right="0.441cm" fo:wrap-option="no-wrap" draw:shadow="hidden" draw:shadow-color="#868686" fo:margin-left="0.319cm" fo:margin-right="0.319cm" style:run-through="foreground" style:wrap="run-through" style:number-wrapped-paragraphs="no-limit" style:vertical-pos="from-top" style:vertical-rel="paragraph" style:horizontal-pos="from-left" style:horizontal-rel="paragraph"/>
		</style:style>
		<style:style style:name="DefinitionPart" style:family="paragraph" style:parent-style-name="Standard">
			<style:paragraph-properties fo:margin-left="2cm" fo:margin-right="0cm" fo:text-align="start" style:justify-single-word="false" fo:text-indent="0cm" style:auto-text-indent="false"/>
			<style:text-properties fo:font-size="12pt" fo:font-weight="bold"/>
		</style:style>

		<style:style style:name="Inter" style:family="paragraph" style:parent-style-name="Standard">
			<style:paragraph-properties fo:text-align="center" style:justify-single-word="false" style:snap-to-layout-grid="false"/>
			<style:text-properties fo:font-size="10pt" fo:font-weight="bold" style:font-size-asian="8pt" style:font-weight-asian="bold" />
		</style:style>

		<style:style style:name="P65" style:family="paragraph" style:parent-style-name="Standard" style:master-page-name="Standard">
			<style:paragraph-properties fo:text-align="start" style:justify-single-word="false" style:page-number="auto"/>
		</style:style>

		<style:style style:name="F16Bold" style:family="text">
			<style:text-properties fo:font-size="16pt" fo:font-weight="bold" style:font-size-asian="16pt" style:font-weight-asian="bold" style:font-size-complex="16pt" />
		</style:style>

		<style:style style:name="F12Bold" style:family="text">
			<style:text-properties fo:font-size="12pt" fo:font-weight="bold" style:font-size-asian="12pt" style:font-weight-asian="bold" style:font-size-complex="12pt" />
		</style:style>

		<style:style style:name="F12Italic" style:family="text">
			<style:text-properties fo:font-size="12pt" fo:font-style="italic" fo:font-style-asian="italic" style:font-size-asian="12pt" style:font-size-complex="12pt" />
		</style:style>

		<style:style style:name="F10Bold" style:family="text">
			<style:text-properties fo:font-size="10pt" fo:font-weight="bold" style:font-size-asian="10pt" style:font-weight-asian="bold" style:font-size-complex="10pt" />
		</style:style>

		<style:style style:name="F10" style:family="text">
			<style:text-properties fo:font-size="10pt" style:font-size-asian="10pt" style:font-size-complex="10pt"/>
		</style:style> 

		<style:style style:name="F10Italic" style:family="text">
			<style:text-properties fo:font-size="10pt" fo:font-style="italic" fo:font-style-asian="italic" style:font-size-asian="10pt" style:font-size-complex="10pt" />
		</style:style>

		<style:style style:name="F10Underline" style:family="text">
			<style:text-properties fo:font-size="10pt" style:font-size-asian="10pt" style:font-size-complex="10pt" style:text-underline-style="solid" style:text-underline-width="auto" style:text-underline-color="font-color"/>
		</style:style>

		<style:style style:name="F12Underline" style:family="text">
			<style:text-properties fo:font-size="12pt" style:font-size-asian="12pt" style:font-size-complex="12pt" style:text-underline-style="solid" style:text-underline-width="auto" style:text-underline-color="font-color"/>
		</style:style>

	</xsl:template>
</xsl:stylesheet>
