<?xml version="1.0" encoding="UTF-8" ?>

<!--
    Document   : report2fo.xsl
    Created on : 2 février 2004
    Author     : cwfr-gcartigny
    Description:
        Purpose of transformation follows.
-->

<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:office="urn:oasis:names:tc:opendocument:xmlns:office:1.0" xmlns:style="urn:oasis:names:tc:opendocument:xmlns:style:1.0" xmlns:text="urn:oasis:names:tc:opendocument:xmlns:text:1.0" xmlns:table="urn:oasis:names:tc:opendocument:xmlns:table:1.0" xmlns:draw="urn:oasis:names:tc:opendocument:xmlns:drawing:1.0" xmlns:fo="urn:oasis:names:tc:opendocument:xmlns:xsl-fo-compatible:1.0" xmlns:xlink="http://www.w3.org/1999/xlink" xmlns:dc="http://purl.org/dc/elements/1.1/" xmlns:meta="urn:oasis:names:tc:opendocument:xmlns:meta:1.0" xmlns:number="urn:oasis:names:tc:opendocument:xmlns:datastyle:1.0" xmlns:svg="urn:oasis:names:tc:opendocument:xmlns:svg-compatible:1.0" xmlns:chart="urn:oasis:names:tc:opendocument:xmlns:chart:1.0" xmlns:dr3d="urn:oasis:names:tc:opendocument:xmlns:dr3d:1.0" xmlns:math="http://www.w3.org/1998/Math/MathML" xmlns:form="urn:oasis:names:tc:opendocument:xmlns:form:1.0" xmlns:script="urn:oasis:names:tc:opendocument:xmlns:script:1.0" xmlns:ooo="http://openoffice.org/2004/office" xmlns:ooow="http://openoffice.org/2004/writer" xmlns:oooc="http://openoffice.org/2004/calc" xmlns:dom="http://www.w3.org/2001/xml-events" xmlns:xforms="http://www.w3.org/2002/xforms" xmlns:xsd="http://www.w3.org/2001/XMLSchema" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" office:version="1.0">

	

	<xsl:template match="REPORT">
		<content>
			<xsl:call-template name="ACTIONPLAN01" />
			<xsl:call-template name="ACTIONPLAN02" />
		</content>
	</xsl:template>




	<xsl:template name="ACTIONPLAN02">
		<xsl:if test="ACTIONPLAN/@saved = 'true'">
			<xsl:if test="ACTIONPLAN/@nbLong > 0">
				<text:h text:style-name="P18" text:outline-level="3">
					<xsl:value-of select="$caqs.actionplan.impression.longTerm" />
				</text:h>
				<text:p text:style-name="P56">
					<xsl:value-of select="$actionplan.cost.longTerm.desc" />
				</text:p>
					
				<table:table>
					<table:table-column table:style-name="TableauSynthActionPlan.A" />
					<table:table-column table:style-name="TableauSynthActionPlan.C" />
					<table:table-column table:style-name="TableauSynthActionPlan.B" />
					<table:table-column table:style-name="TableauSynthActionPlan.C" />
					<table:table-row>
						<table:table-cell table:style-name="TableauSynthActionPlan.A1" office:value-type="string">
							<text:p text:style-name="P35">
								<xsl:value-of select="$actionplan.criteriontable.col01.title"/>
							</text:p>
						</table:table-cell>
						<table:table-cell table:style-name="TableauSynthActionPlan.A1" office:value-type="string">
							<text:p text:style-name="P35">
								<xsl:value-of select="$actionplan.criteriontable.col02.title"/>
							</text:p>
						</table:table-cell>
						<table:table-cell table:style-name="TableauSynthActionPlan.A1" office:value-type="string">
							<text:p text:style-name="P35">
								<xsl:value-of select="$actionplan.criteriontable.col03.title"/>
							</text:p>
						</table:table-cell>
						<table:table-cell table:style-name="TableauSynthActionPlan.A1" office:value-type="string">
							<text:p text:style-name="P35">
								<xsl:value-of select="$actionplan.criteriontable.col04.title"/>
							</text:p>
						</table:table-cell>
					</table:table-row>
					<xsl:for-each select="ACTIONPLAN/CRITERE">
						<xsl:if test="@corrected = 'true'">
							<xsl:if test="@priority = 'LONG_TERM'">
								<table:table-row>
									<table:table-cell table:style-name="TableauSynthActionPlan.A2" office:value-type="string">
										<text:p text:style-name="P42">
											<xsl:value-of select="@lib"/>
										</text:p>
									</table:table-cell>
									<table:table-cell table:style-name="TableauSynthActionPlan.B2" office:value-type="string">
										<text:p text:style-name="P42Right">
											<xsl:value-of select="@nbImpacted"/>
										</text:p>
									</table:table-cell>
									<table:table-cell table:style-name="TableauSynthActionPlan.B2" office:value-type="string">
										<text:p text:style-name="P42Right">
											<xsl:value-of select="@cost"/>
										</text:p>
									</table:table-cell>
									<table:table-cell table:style-name="TableauSynthActionPlan.B2" office:value-type="string">
										<text:p text:style-name="P42Right">
											<xsl:value-of select="@percent"/>
										</text:p>
									</table:table-cell>
								</table:table-row>
							</xsl:if>
						</xsl:if>
					</xsl:for-each>
				</table:table>
				<text:p text:style-name="P56" />
				<text:p text:style-name="P56">
					<xsl:value-of select="$actionplan.cost.longcost.calcul2" />
					<xsl:value-of select="ACTIONPLAN/@longCost"/>.
				</text:p>
				
				<text:p text:style-name="Standard"/>
				<xsl:for-each select="ACTIONPLAN/CRITERE">
					<xsl:if test="@corrected = 'true'">
						<xsl:if test="@priority = 'LONG_TERM'">
							<xsl:if test="@comment != ''">
								<text:p text:style-name="Standard">
									<text:span text:style-name="F12Underline">
										<xsl:value-of select="@lib"/> :</text:span>
								</text:p>
								<text:p text:style-name="F12">
									<xsl:value-of select="@comment"/>
								</text:p>
								<text:p text:style-name="Standard"/>
							</xsl:if>
						</xsl:if>
					</xsl:if>
				</xsl:for-each>
			
			</xsl:if>
			
			<xsl:if test="count(ACTIONPLAN/CRITERE[@corrected='false' and @comment!='']) > 0">
				<text:h text:style-name="P18" text:outline-level="3">
					<xsl:value-of select="$actionplan.commentList" />
				</text:h>
				<xsl:for-each select="ACTIONPLAN/CRITERE">
					<xsl:if test="@corrected = 'false'">
						<xsl:if test="@comment != ''">
							<text:p text:style-name="Standard">
								<text:span text:style-name="F12Underline">
									<xsl:value-of select="@lib"/> :</text:span>
							</text:p>
							<text:p text:style-name="F12">
								<xsl:value-of select="@comment"/>
							</text:p>
							<text:p text:style-name="Standard"/>
						</xsl:if>
					</xsl:if>
				</xsl:for-each>
			</xsl:if>
			
			<text:p text:style-name="P18" text:outline-level="2">
				<xsl:value-of select="$actionplan.kiviat.title"/>
			</text:p>
			<text:p text:style-name="P30Just">
				<xsl:value-of select="$actionplan.kiviat.desc"/>
			</text:p>
			<xsl:if test="ACTIONPLAN/@nbShort > 0">
				<text:p text:style-name="P35">
					<draw:frame draw:style-name="fr1" draw:name="ActionPlanKiviat" text:anchor-type="as-char" svg:width="9.269cm" svg:height="7.946cm" draw:z-index="42">
						<draw:image xlink:href="Pictures/actionPlanKiviatC.png" xlink:type="simple" xlink:show="embed" xlink:actuate="onLoad"/>
					</draw:frame>
				</text:p>
				<text:p text:style-name="Inter">
					<text:span text:style-name="T2">
						<xsl:value-of select="$illustration" />
						<xsl:text></xsl:text>
					</text:span>
					<text:sequence text:ref-name="refIllustration1" text:name="Illustration" text:formula="ooow:Illustration+1" style:num-format="1">2</text:sequence>
					<text:span text:style-name="T2">
						<xsl:value-of select="$actionplan.kiviat.img.titlec" />
					</text:span>
				</text:p>
			</xsl:if>
			<xsl:if test="ACTIONPLAN/@nbMedium > 0">
				<text:p text:style-name="P35">
					<draw:frame draw:style-name="fr1" draw:name="ActionPlanKiviat" text:anchor-type="as-char" svg:width="9.269cm" svg:height="7.946cm" draw:z-index="42">
						<draw:image xlink:href="Pictures/actionPlanKiviatCM.png" xlink:type="simple" xlink:show="embed" xlink:actuate="onLoad"/>
					</draw:frame>
				</text:p>
				<text:p text:style-name="Inter">
					<text:span text:style-name="T2">
						<xsl:value-of select="$illustration" />
						<xsl:text></xsl:text>
					</text:span>
					<text:sequence text:ref-name="refIllustration1" text:name="Illustration" text:formula="ooow:Illustration+1" style:num-format="1">2</text:sequence>
					<text:span text:style-name="T2">
						<xsl:value-of select="$actionplan.kiviat.img.titlecm" />
					</text:span>
				</text:p>
			</xsl:if>
			<xsl:if test="ACTIONPLAN/@nbLong > 0">
				<text:p text:style-name="P35">
					<draw:frame draw:style-name="fr1" draw:name="ActionPlanKiviat" text:anchor-type="as-char" svg:width="9.269cm" svg:height="7.946cm" draw:z-index="42">
						<draw:image xlink:href="Pictures/actionPlanKiviatCML.png" xlink:type="simple" xlink:show="embed" xlink:actuate="onLoad"/>
					</draw:frame>
				</text:p>
				<text:p text:style-name="Inter">
					<text:span text:style-name="T2">
						<xsl:value-of select="$illustration" />
						<xsl:text></xsl:text>
					</text:span>
					<text:sequence text:ref-name="refIllustration1" text:name="Illustration" text:formula="ooow:Illustration+1" style:num-format="1">2</text:sequence>
					<text:span text:style-name="T2">
						<xsl:value-of select="$actionplan.kiviat.img.titlecml" />
					</text:span>
				</text:p>
			</xsl:if>
		</xsl:if>
		<xsl:if test="ACTIONPLAN/@saved = 'false'">
			<text:p text:style-name="Table"/>
			<text:h text:style-name="P17" text:outline-level="2">
				<xsl:value-of select="$actionplan.no.title" />
			</text:h>
			<text:p text:style-name="Standard"/>
			<text:p text:style-name="P55">
				<xsl:value-of select="$actionplan.no.txt01" />
			</text:p>
			<text:p text:style-name="P56">
				<xsl:value-of select="$actionplan.no.expl01" />
			</text:p>
			<text:p text:style-name="P56">
				<xsl:value-of select="$actionplan.no.expl02" />
			</text:p>
			<text:p text:style-name="P56">
				<xsl:value-of select="$actionplan.no.expl03" />
			</text:p>
			<text:p text:style-name="P56"/>
			<text:list text:style-name="WW8Num7">
				<text:list-item>
					<text:p text:style-name="P57">
						<xsl:value-of select="$actionplan.no.list01" />
					</text:p>
				</text:list-item>
				<text:list-item>
					<text:p text:style-name="P57">
						<xsl:value-of select="$actionplan.no.list02" />
					</text:p>
				</text:list-item>
				<text:list-item>
					<text:p text:style-name="P57">...</text:p>
				</text:list-item>
				<text:list-item>
					<text:p text:style-name="P57">...</text:p>
				</text:list-item>
			</text:list>
			<text:p text:style-name="Text_20_body"/>
		
		</xsl:if>
	</xsl:template>






	
	
	<xsl:template name="ACTIONPLAN01">
		<xsl:if test="ACTIONPLAN/@saved = 'true'">
			<text:p text:style-name="P17">
				<xsl:value-of select="$actionplan.title.saved"/>
			</text:p>
			<text:p text:style-name="Standard"/>
			<text:p text:style-name="Standard">
				<text:span text:style-name="F12Bold">
					<xsl:value-of select="$actionplan.comment" />
					<xsl:text> </xsl:text>:
				</text:span>
			</text:p>
			<text:p text:style-name="F12">
				<xsl:text></xsl:text>
				<xsl:value-of select="ACTIONPLAN/@comment"/>
			</text:p>
			<text:p text:style-name="Standard"/>
			<text:p text:style-name="Standard">
				<text:span text:style-name="F12Bold">
					<xsl:value-of select="$actionplan.uo" /> :
				</text:span>
			</text:p>
			<text:p text:style-name="F12">
				<xsl:value-of select="$actionplan.uo.explanation" />
			</text:p>
			<xsl:for-each select="ACTIONPLAN/WORKUNIT">
				<text:p text:style-name="F12">
					<xsl:text>- </xsl:text><xsl:value-of select="@unit"/><xsl:text> </xsl:text><xsl:value-of select="@type"/>
				</text:p>
			</xsl:for-each>
			<text:p text:style-name="Standard"/>
			<text:p text:style-name="Standard">
				<text:span text:style-name="F12Bold">
					<xsl:value-of select="$actionplan.globalCost" /> :
				</text:span>
			</text:p>
			<text:p text:style-name="F12">
				<xsl:value-of select="$actionplan.cost.cost.calcul2" />
				<xsl:value-of select="ACTIONPLAN/@totalCost"/>.
			</text:p>
			
				
			<xsl:if test="ACTIONPLAN/@nbShort > 0">
				<text:h text:style-name="P18" text:outline-level="3">
					<xsl:value-of select="$caqs.actionplan.impression.shortTerm" />
				</text:h>
				<text:p text:style-name="P56">
					<xsl:value-of select="$actionplan.cost.shortTerm.desc" />
				</text:p>
					
				<table:table>
					<table:table-column table:style-name="TableauSynthActionPlan.A" />
					<table:table-column table:style-name="TableauSynthActionPlan.C" />
					<table:table-column table:style-name="TableauSynthActionPlan.B" />
					<table:table-column table:style-name="TableauSynthActionPlan.C" />
					<table:table-row>
						<table:table-cell table:style-name="TableauSynthActionPlan.A1" office:value-type="string">
							<text:p text:style-name="P35">
								<xsl:value-of select="$actionplan.criteriontable.col01.title"/>
							</text:p>
						</table:table-cell>
						<table:table-cell table:style-name="TableauSynthActionPlan.A1" office:value-type="string">
							<text:p text:style-name="P35">
								<xsl:value-of select="$actionplan.criteriontable.col02.title"/>
							</text:p>
						</table:table-cell>
						<table:table-cell table:style-name="TableauSynthActionPlan.A1" office:value-type="string">
							<text:p text:style-name="P35">
								<xsl:value-of select="$actionplan.criteriontable.col03.title"/>
							</text:p>
						</table:table-cell>
						<table:table-cell table:style-name="TableauSynthActionPlan.A1" office:value-type="string">
							<text:p text:style-name="P35">
								<xsl:value-of select="$actionplan.criteriontable.col04.title"/>
							</text:p>
						</table:table-cell>
					</table:table-row>
					<xsl:for-each select="ACTIONPLAN/CRITERE">
						<xsl:if test="@corrected = 'true'">
							<xsl:if test="@priority = 'SHORT_TERM'">
								<table:table-row>
									<table:table-cell table:style-name="TableauSynthActionPlan.A2" office:value-type="string">
										<text:p text:style-name="P42">
											<xsl:value-of select="@lib"/>
										</text:p>
									</table:table-cell>
									<table:table-cell table:style-name="TableauSynthActionPlan.B2" office:value-type="string">
										<text:p text:style-name="P42Right">
											<xsl:value-of select="@nbImpacted"/>
										</text:p>
									</table:table-cell>
									<table:table-cell table:style-name="TableauSynthActionPlan.B2" office:value-type="string">
										<text:p text:style-name="P42Right">
											<xsl:value-of select="@cost"/>
										</text:p>
									</table:table-cell>
									<table:table-cell table:style-name="TableauSynthActionPlan.B2" office:value-type="string">
										<text:p text:style-name="P42Right">
											<xsl:value-of select="@percent"/>
										</text:p>
									</table:table-cell>
								</table:table-row>
							</xsl:if>
						</xsl:if>
					</xsl:for-each>
				</table:table>
				<text:p text:style-name="P56" />
				<text:p text:style-name="P56">
					<xsl:value-of select="$actionplan.cost.shortcost.calcul2" />
					<xsl:value-of select="ACTIONPLAN/@shortCost"/>.
				</text:p>
				
				<text:p text:style-name="Standard"/>
				<xsl:for-each select="ACTIONPLAN/CRITERE">
					<xsl:if test="@corrected = 'true'">
						<xsl:if test="@priority = 'SHORT_TERM'">
							<xsl:if test="@comment != ''">
								<text:p text:style-name="Standard">
									<text:span text:style-name="F12Underline">
										<xsl:value-of select="@lib"/> :</text:span>
								</text:p>
								<text:p text:style-name="F12">
									<xsl:value-of select="@comment"/>
								</text:p>
								<text:p text:style-name="Standard"/>
							</xsl:if>
						</xsl:if>
					</xsl:if>
				</xsl:for-each>
			
			</xsl:if>
			
				
			<xsl:if test="ACTIONPLAN/@nbMedium > 0">
				<text:h text:style-name="P18" text:outline-level="3">
					<xsl:value-of select="$caqs.actionplan.impression.mediumTerm" />
				</text:h>
				<text:p text:style-name="P56">
					<xsl:value-of select="$actionplan.cost.mediumTerm.desc" />
				</text:p>
					
				<table:table>
					<table:table-column table:style-name="TableauSynthActionPlan.A" />
					<table:table-column table:style-name="TableauSynthActionPlan.C" />
					<table:table-column table:style-name="TableauSynthActionPlan.B" />
					<table:table-column table:style-name="TableauSynthActionPlan.C" />
					<table:table-row>
						<table:table-cell table:style-name="TableauSynthActionPlan.A1" office:value-type="string">
							<text:p text:style-name="P35">
								<xsl:value-of select="$actionplan.criteriontable.col01.title"/>
							</text:p>
						</table:table-cell>
						<table:table-cell table:style-name="TableauSynthActionPlan.A1" office:value-type="string">
							<text:p text:style-name="P35">
								<xsl:value-of select="$actionplan.criteriontable.col02.title"/>
							</text:p>
						</table:table-cell>
						<table:table-cell table:style-name="TableauSynthActionPlan.A1" office:value-type="string">
							<text:p text:style-name="P35">
								<xsl:value-of select="$actionplan.criteriontable.col03.title"/>
							</text:p>
						</table:table-cell>
						<table:table-cell table:style-name="TableauSynthActionPlan.A1" office:value-type="string">
							<text:p text:style-name="P35">
								<xsl:value-of select="$actionplan.criteriontable.col04.title"/>
							</text:p>
						</table:table-cell>
					</table:table-row>
					<xsl:for-each select="ACTIONPLAN/CRITERE">
						<xsl:if test="@corrected = 'true'">
							<xsl:if test="@priority = 'MEDIUM_TERM'">
								<table:table-row>
									<table:table-cell table:style-name="TableauSynthActionPlan.A2" office:value-type="string">
										<text:p text:style-name="P42">
											<xsl:value-of select="@lib"/>
										</text:p>
									</table:table-cell>
									<table:table-cell table:style-name="TableauSynthActionPlan.B2" office:value-type="string">
										<text:p text:style-name="P42Right">
											<xsl:value-of select="@nbImpacted"/>
										</text:p>
									</table:table-cell>
									<table:table-cell table:style-name="TableauSynthActionPlan.B2" office:value-type="string">
										<text:p text:style-name="P42Right">
											<xsl:value-of select="@cost"/>
										</text:p>
									</table:table-cell>
									<table:table-cell table:style-name="TableauSynthActionPlan.B2" office:value-type="string">
										<text:p text:style-name="P42Right">
											<xsl:value-of select="@percent"/>
										</text:p>
									</table:table-cell>
								</table:table-row>
							</xsl:if>
						</xsl:if>
					</xsl:for-each>
				</table:table>
				<text:p text:style-name="P56" />
				<text:p text:style-name="P56">
					<xsl:value-of select="$actionplan.cost.mediumcost.calcul2" />
					<xsl:value-of select="ACTIONPLAN/@mediumCost"/>.
				</text:p>
				
				<text:p text:style-name="Standard"/>
				<xsl:for-each select="ACTIONPLAN/CRITERE">
					<xsl:if test="@corrected = 'true'">
						<xsl:if test="@priority = 'MEDIUM_TERM'">
							<xsl:if test="@comment != ''">
								<text:p text:style-name="Standard">
									<text:span text:style-name="F12Underline">
										<xsl:value-of select="@lib"/> :</text:span>
								</text:p>
								<text:p text:style-name="F12">
									<xsl:value-of select="@comment"/>
								</text:p>
								<text:p text:style-name="Standard"/>
							</xsl:if>
						</xsl:if>
					</xsl:if>
				</xsl:for-each>
			
			</xsl:if>
		</xsl:if>
	</xsl:template>

</xsl:stylesheet>
