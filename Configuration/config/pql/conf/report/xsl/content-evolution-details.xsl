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
	
	<xsl:strip-space elements="BASELINE CRITERE CRIT_DETAIL ELEMENT ELT FACTOR MET METRIQUES REPORT"/>


  <xsl:template name="INSERT_TENDANCE_PICTURE">
    <xsl:param name="tendance" />
        <draw:frame draw:style-name="fr1" draw:name="Cadre7" text:anchor-type="as-char" svg:width="0.36cm" svg:height="0.36cm" draw:z-index="3">
  			   <xsl:choose>
              <xsl:when test="@tendance = 'up'">
                <draw:image xlink:href="Pictures/note_up.gif" xlink:type="simple" xlink:show="embed" xlink:actuate="onLoad"/>
  				    </xsl:when>
              <xsl:when test="@tendance = 'down'">
                <draw:image xlink:href="Pictures/note_down.gif" xlink:type="simple" xlink:show="embed" xlink:actuate="onLoad"/>
  				    </xsl:when>
              <xsl:when test="@tendance = 'unchanged'">
                <draw:image xlink:href="Pictures/note_unchanged.gif" xlink:type="simple" xlink:show="embed" xlink:actuate="onLoad"/>
  				    </xsl:when>
              <xsl:otherwise>
                <draw:image xlink:href="Pictures/note_none.gif" xlink:type="simple" xlink:show="embed" xlink:actuate="onLoad"/>
              </xsl:otherwise>
            </xsl:choose>
        </draw:frame>
  </xsl:template>
  
  <xsl:template name="DEGREE_APPLICATION">
	<xsl:if test="ACTIONPLAN/@exists = 'true'">
        	<text:h text:style-name="Heading_20_2" text:outline-level="2"><xsl:value-of select="$actionplan.evolution.degree.title"/></text:h>
			<text:p text:style-name="Standard"/>
				
              <table:table table:name="Tableau5" table:style-name="Tableau5">
					<table:table-column table:style-name="Tableau5.A"/>
					<table:table-column table:style-name="Tableau5.B"/>
					<table:table-row table:style-name="Tableau5.1">
						<table:table-cell table:style-name="Tableau5.A1" office:value-type="string">
							<text:p text:style-name="P48">
							<xsl:value-of select="$actionplan.evolution.degree.corrected"/>
						</text:p>
					</table:table-cell>
					<table:table-cell table:style-name="Tableau5.B1" office:value-type="string">
						<text:p text:style-name="P42Right">
							<xsl:value-of select="ACTIONPLAN/@corrected"/>
						</text:p>
					</table:table-cell>
				</table:table-row>
				<table:table-row>
					<table:table-cell table:style-name="Tableau5.A1" office:value-type="string">
							<text:p text:style-name="P48">
							<xsl:value-of select="$actionplan.evolution.degree.partially"/>
						</text:p>
					</table:table-cell>
					<table:table-cell table:style-name="Tableau5.B1" office:value-type="string">
						<text:p text:style-name="P42Right">
							<xsl:value-of select="ACTIONPLAN/@partially"/>
						</text:p>
					</table:table-cell>
				</table:table-row>
				<table:table-row>
					<table:table-cell table:style-name="Tableau5.A1" office:value-type="string">
							<text:p text:style-name="P48">
							<xsl:value-of select="$actionplan.evolution.degree.degraded"/>
						</text:p>
					</table:table-cell>
					<table:table-cell table:style-name="Tableau5.B1" office:value-type="string">
						<text:p text:style-name="P42Right">
							<xsl:value-of select="ACTIONPLAN/@degraded"/>
						</text:p>
					</table:table-cell>
				</table:table-row>
				<table:table-row>
					<table:table-cell table:style-name="Tableau5.A1" office:value-type="string">
							<text:p text:style-name="P48">
							<xsl:value-of select="$actionplan.evolution.degree.stable"/>
						</text:p>
					</table:table-cell>
					<table:table-cell table:style-name="Tableau5.B1" office:value-type="string">
						<text:p text:style-name="P42Right">
							<xsl:value-of select="ACTIONPLAN/@stable"/>
						</text:p>
					</table:table-cell>
				</table:table-row>
			</table:table>
	</xsl:if>
  </xsl:template>
  
	<xsl:template match="GOAL_EVOLUTION">
  	 <text:h text:style-name="P47" text:outline-level="2">
          <xsl:value-of select="$evolution.goal.title"/>
        </text:h>
				<text:p text:style-name="Standard"/>
      <table:table>
					<table:table-column table:style-name="TableauGoalEvol.A" />
					<table:table-column table:style-name="TableauGoalEvol.C" />
					<table:table-column table:style-name="TableauGoalEvol.B" />
					<table:table-row>
						<table:table-cell table:style-name="TableauGoalEvol.A1" office:value-type="string">
							<text:p text:style-name="P35">
								<xsl:value-of select="$evolution.goal.goal"/>
							</text:p>
						</table:table-cell>
						<table:table-cell table:style-name="TableauGoalEvol.A1" office:value-type="string">
							<text:p text:style-name="P35">
								<xsl:value-of select="$evolution.goal.note"/>
							</text:p>
						</table:table-cell>
						<table:table-cell table:style-name="TableauGoalEvol.A1" office:value-type="string">
							<text:p text:style-name="P35">
								<xsl:value-of select="$evolution.goal.evolution"/>
							</text:p>
						</table:table-cell>
					</table:table-row>
					<xsl:for-each select="GOAL">
							<table:table-row>
								<table:table-cell table:style-name="TableauGoalEvol.A2" office:value-type="string">
									<text:p text:style-name="P42">
											<xsl:value-of select="@lib"/>
								  </text:p>
								</table:table-cell>
								<table:table-cell table:style-name="TableauGoalEvol.A2" office:value-type="string">
									<text:p text:style-name="P42Right">
										<xsl:value-of select="@note"/>
									</text:p>
								</table:table-cell>
								<table:table-cell table:style-name="TableauGoalEvol.A2" office:value-type="string">
									<text:p text:style-name="P35">
										<xsl:call-template name="INSERT_TENDANCE_PICTURE">
											<xsl:with-param name="tendance"><xsl:value-of select="@tendance"/></xsl:with-param>
										</xsl:call-template>
									</text:p>
								</table:table-cell>
							</table:table-row>
					</xsl:for-each>
				</table:table>
  </xsl:template>
  
  <xsl:template name="TABLE">
        <text:h text:style-name="P47" text:outline-level="2">
          <xsl:value-of select="$evolution.title.volumetrie"/>
        </text:h>
				<text:p text:style-name="Standard"/>
        <table:table table:name="Tableau5" table:style-name="Tableau5">
					<table:table-column table:style-name="Tableau5.A"/>
					<table:table-column table:style-name="Tableau5.B"/>
					<table:table-row table:style-name="Tableau5.1">
						<table:table-cell table:style-name="Tableau5.A1" office:value-type="string">
							<text:p text:style-name="P48">
                <xsl:value-of select="$evolution.newandbad"/>
              </text:p>
						</table:table-cell>
						<table:table-cell table:style-name="Tableau5.B1" office:value-type="string">
							<text:p text:style-name="P42Right">
							 <xsl:value-of select="NEWBAD/@nb"/>
              </text:p>
						</table:table-cell>
					</table:table-row>
					<table:table-row table:style-name="Tableau5.1">
						<table:table-cell table:style-name="Tableau5.A1" office:value-type="string">
							<text:p text:style-name="P48">
                <xsl:value-of select="$evolution.oldworst"/>
              </text:p>
						</table:table-cell>
						<table:table-cell table:style-name="Tableau5.B1" office:value-type="string">
							<text:p text:style-name="P42Right">
							 <xsl:value-of select="OLDWORST/@nb"/>
              </text:p>
						</table:table-cell>
					</table:table-row>
					<table:table-row table:style-name="Tableau5.1">
						<table:table-cell table:style-name="Tableau5.A1" office:value-type="string">
							<text:p text:style-name="P50">
                <xsl:value-of select="$evolution.oldbetter"/>
              </text:p>
						</table:table-cell>
						<table:table-cell table:style-name="Tableau5.B1" office:value-type="string">
							<text:p text:style-name="P42Right">
							 <xsl:value-of select="OLDBETTER/@nb"/>
              </text:p>
						</table:table-cell>
					</table:table-row>
					<table:table-row table:style-name="Tableau5.1">
						<table:table-cell table:style-name="Tableau5.A1" office:value-type="string">
							<text:p text:style-name="P50">
                <xsl:value-of select="$evolution.oldbetterworst"/>
              </text:p>
						</table:table-cell>
						<table:table-cell table:style-name="Tableau5.B1" office:value-type="string">
							<text:p text:style-name="P42Right">
							 <xsl:value-of select="OLDBETTERWORST/@nb"/>
              </text:p>
						</table:table-cell>
					</table:table-row>
					<table:table-row table:style-name="Tableau5.1">
						<table:table-cell table:style-name="Tableau5.A1" office:value-type="string">
							<text:p text:style-name="P50">
                <xsl:value-of select="$evolution.stable"/>
              </text:p>
						</table:table-cell>
						<table:table-cell table:style-name="Tableau5.B1" office:value-type="string">
							<text:p text:style-name="P42Right">
							 <xsl:value-of select="STABLE/@nb"/>
              </text:p>
						</table:table-cell>
					</table:table-row>
					<table:table-row table:style-name="Tableau5.1">
						<table:table-cell table:style-name="Tableau5.A1" office:value-type="string">
							<text:p text:style-name="P50">
                <xsl:value-of select="$evolution.oldbadstable"/>
              </text:p>
						</table:table-cell>
						<table:table-cell table:style-name="Tableau5.B1" office:value-type="string">
							<text:p text:style-name="P42Right">
							 <xsl:value-of select="BADSTABLE/@nb"/>
              </text:p>
						</table:table-cell>
					</table:table-row>
				</table:table>
				<text:p text:style-name="Inter"><xsl:value-of select="$evolution.tableau"/><text:sequence text:ref-name="refTable4" text:name="Table" text:formula="ooow:Table+1" style:num-format="1">5</text:sequence><xsl:value-of select="$evolution.volumetrie"/></text:p>
				
  </xsl:template>
  
  <xsl:template name="TPL_NEWBAD">
      <text:h text:style-name="P61" text:outline-level="3">
          <xsl:value-of select="$evolution.repartition.newbad"/>
        </text:h>
				<text:p text:style-name="P35">
  				<draw:frame draw:style-name="fr1" draw:name="Cadre7" text:anchor-type="as-char" svg:width="15.875cm" svg:height="7.938cm" draw:z-index="3">
  					<draw:image xlink:href="Pictures/repartition-EvolNewBad.png" xlink:type="simple" xlink:show="embed" xlink:actuate="onLoad"/>
  				</draw:frame>
  			</text:p>
  			<text:p text:style-name="Inter"><xsl:value-of select="$evolution.figure"/><text:sequence text:ref-name="refIllustration5" text:name="Illustration" text:formula="ooow:Illustration+1" style:num-format="1">5</text:sequence>
          <xsl:value-of select="$evolution.repartition.crit"/>
        </text:p>
				<text:p text:style-name="Standard"/>
				
				<text:p text:style-name="Standard">
				  <text:span text:style-name="F12Bold">
				    <xsl:value-of select="$evolution.nbElts.newandbad"/></text:span>
            <xsl:value-of select="NEWBAD/@nb"/>
				</text:p>
				<table:table>
						<table:table-column />
						<table:table-column />
						<table:table-row>
							<table:table-cell table:style-name="TableauDetailsActionPlan.B1" office:value-type="string">
								<text:p text:style-name="P35">
									<xsl:value-of select="$evolution.eltstable.eltdesc"/>
								</text:p>
							</table:table-cell>
							<table:table-cell table:style-name="TableauDetailsActionPlan.B1" office:value-type="string">
								<text:p text:style-name="P35">
									<xsl:value-of select="$evolution.eltstable.criteres"/>
								</text:p>
							</table:table-cell>
						</table:table-row>
						<xsl:for-each select="NEWBAD/ELEMENT">
							<table:table-row>
								<table:table-cell table:style-name="TableauDetailsActionPlan.B2" office:value-type="string">
									<text:p text:style-name="Standard">
										<xsl:value-of select="@desc"/>
									</text:p>
								</table:table-cell>
								<table:table-cell table:style-name="TableauDetailsActionPlan.B2" office:value-type="string">
									<xsl:for-each select="CRITERION">
										<text:p text:style-name="Standard">- <xsl:value-of select="@lib" />
									<xsl:call-template name="INSERT_TENDANCE_PICTURE">
                      <xsl:with-param name="tendance"><xsl:value-of select="@tendance"/></xsl:with-param>
                  </xsl:call-template>
                  </text:p>
									</xsl:for-each>
								</table:table-cell>
							</table:table-row>
						</xsl:for-each>
					</table:table>
	</xsl:template>
	
	<xsl:template name="OLDWORST">
      <text:h text:style-name="P18" text:outline-level="3">
          <xsl:value-of select="$evolution.repartition.oldworst"/>
        </text:h>
				<text:p text:style-name="P35">
  				<draw:frame draw:style-name="fr1" draw:name="Cadre7" text:anchor-type="as-char" svg:width="15.875cm" svg:height="7.938cm" draw:z-index="3">
  					<draw:image xlink:href="Pictures/repartition-EvolOldWorst.png" xlink:type="simple" xlink:show="embed" xlink:actuate="onLoad"/>
  				</draw:frame>
  			</text:p>
  			<text:p text:style-name="Inter"><xsl:value-of select="$evolution.figure"/><text:sequence text:ref-name="refIllustration5" text:name="Illustration" text:formula="ooow:Illustration+1" style:num-format="1">5</text:sequence>
          <xsl:value-of select="$evolution.repartition.crit"/>
        </text:p>
				<text:p text:style-name="Standard"/>	
				<text:p text:style-name="Standard">
				  <text:span text:style-name="F12Bold">
				    <xsl:value-of select="$evolution.nbElts.oldworst"/></text:span>
            <xsl:value-of select="OLDWORST/@nb"/>
				</text:p>
				<table:table>
						<table:table-column />
						<table:table-column />
						<table:table-row>
							<table:table-cell table:style-name="TableauDetailsActionPlan.B1" office:value-type="string">
								<text:p text:style-name="P35">
									<xsl:value-of select="$evolution.eltstable.eltdesc"/>
								</text:p>
							</table:table-cell>
							<table:table-cell table:style-name="TableauDetailsActionPlan.B1" office:value-type="string">
								<text:p text:style-name="P35">
									<xsl:value-of select="$evolution.eltstable.criteres"/>
								</text:p>
							</table:table-cell>
						</table:table-row>
						<xsl:for-each select="OLDWORST/ELEMENT">
							<table:table-row>
								<table:table-cell table:style-name="TableauDetailsActionPlan.B2" office:value-type="string">
									<text:p text:style-name="Standard">
										<xsl:value-of select="@desc"/>
									</text:p>
								</table:table-cell>
								<table:table-cell table:style-name="TableauDetailsActionPlan.B2" office:value-type="string">
									<xsl:for-each select="CRITERION">
										<text:p text:style-name="Standard">- <xsl:value-of select="@lib" />
									<xsl:call-template name="INSERT_TENDANCE_PICTURE">
                      <xsl:with-param name="tendance"><xsl:value-of select="@tendance"/></xsl:with-param>
                  </xsl:call-template>
                  </text:p>
									</xsl:for-each>
								</table:table-cell>
							</table:table-row>
						</xsl:for-each>
					</table:table>
	</xsl:template>
	
	<xsl:template name="OLDBETTER">
      <text:h text:style-name="P18" text:outline-level="3">
          <xsl:value-of select="$evolution.repartition.oldbetter"/>
        </text:h>
				<text:p text:style-name="P35">
  				<draw:frame draw:style-name="fr1" draw:name="Cadre7" text:anchor-type="as-char" svg:width="15.875cm" svg:height="7.938cm" draw:z-index="3">
  					<draw:image xlink:href="Pictures/repartition-EvolOldBetter.png" xlink:type="simple" xlink:show="embed" xlink:actuate="onLoad"/>
  				</draw:frame>
  			</text:p>
  			<text:p text:style-name="Inter"><xsl:value-of select="$evolution.figure"/><text:sequence text:ref-name="refIllustration5" text:name="Illustration" text:formula="ooow:Illustration+1" style:num-format="1">5</text:sequence>
          <xsl:value-of select="$evolution.repartition.crit"/>
        </text:p>
				<text:p text:style-name="Standard"/>	
				<text:p text:style-name="Standard">
				  <text:span text:style-name="F12Bold">
				    <xsl:value-of select="$evolution.nbElts.oldbetter"/></text:span>
            <xsl:value-of select="OLDBETTER/@nb"/>
				</text:p>
				<table:table>
						<table:table-column />
						<table:table-column />
						<table:table-row>
							<table:table-cell table:style-name="TableauDetailsActionPlan.B1" office:value-type="string">
								<text:p text:style-name="P35">
									<xsl:value-of select="$evolution.eltstable.eltdesc"/>
								</text:p>
							</table:table-cell>
							<table:table-cell table:style-name="TableauDetailsActionPlan.B1" office:value-type="string">
								<text:p text:style-name="P35">
									<xsl:value-of select="$evolution.eltstable.criteres"/>
								</text:p>
							</table:table-cell>
						</table:table-row>
						<xsl:for-each select="OLDBETTER/ELEMENT">
							<table:table-row>
								<table:table-cell table:style-name="TableauDetailsActionPlan.B2" office:value-type="string">
									<text:p text:style-name="Standard">
										<xsl:value-of select="@desc"/>
									</text:p>
								</table:table-cell>
								<table:table-cell table:style-name="TableauDetailsActionPlan.B2" office:value-type="string">
									<xsl:for-each select="CRITERION">
										<text:p text:style-name="Standard">- <xsl:value-of select="@lib" />
									<xsl:call-template name="INSERT_TENDANCE_PICTURE">
                      <xsl:with-param name="tendance"><xsl:value-of select="@tendance"/></xsl:with-param>
                  </xsl:call-template>
                  </text:p>
									</xsl:for-each>
								</table:table-cell>
							</table:table-row>
						</xsl:for-each>
					</table:table>
	</xsl:template>
	
	<xsl:template name="OLDBETTERWORST">
      <text:h text:style-name="P18" text:outline-level="3">
          <xsl:value-of select="$evolution.repartition.oldbetterworst"/>
        </text:h>
				<text:p text:style-name="P35">
  				<draw:frame draw:style-name="fr1" draw:name="Cadre7" text:anchor-type="as-char" svg:width="15.875cm" svg:height="7.938cm" draw:z-index="3">
  					<draw:image xlink:href="Pictures/repartition-EvolOldBetterWorst.png" xlink:type="simple" xlink:show="embed" xlink:actuate="onLoad"/>
  				</draw:frame>
  			</text:p>
  			<text:p text:style-name="Inter"><xsl:value-of select="$evolution.figure"/><text:sequence text:ref-name="refIllustration5" text:name="Illustration" text:formula="ooow:Illustration+1" style:num-format="1">5</text:sequence>
          <xsl:value-of select="$evolution.repartition.crit"/>
        </text:p>
				<text:p text:style-name="Standard"/>	
				<text:p text:style-name="Standard">
				  <text:span text:style-name="F12Bold">
				    <xsl:value-of select="$evolution.nbElts.oldbetterworst"/></text:span>
            <xsl:value-of select="OLDBETTERWORST/@nb"/>
				</text:p>
				<table:table>
						<table:table-column />
						<table:table-column />
						<table:table-row>
							<table:table-cell table:style-name="TableauDetailsActionPlan.B1" office:value-type="string">
								<text:p text:style-name="P35">
									<xsl:value-of select="$evolution.eltstable.eltdesc"/>
								</text:p>
							</table:table-cell>
							<table:table-cell table:style-name="TableauDetailsActionPlan.B1" office:value-type="string">
								<text:p text:style-name="P35">
									<xsl:value-of select="$evolution.eltstable.criteres"/>
								</text:p>
							</table:table-cell>
						</table:table-row>
						<xsl:for-each select="OLDBETTERWORST/ELEMENT">
							<table:table-row>
								<table:table-cell table:style-name="TableauDetailsActionPlan.B2" office:value-type="string">
									<text:p text:style-name="Standard">
										<xsl:value-of select="@desc"/>
									</text:p>
								</table:table-cell>
								<table:table-cell table:style-name="TableauDetailsActionPlan.B2" office:value-type="string">
									<xsl:for-each select="CRITERION">
										<text:p text:style-name="Standard">- <xsl:value-of select="@lib" />
									<xsl:call-template name="INSERT_TENDANCE_PICTURE">
                      <xsl:with-param name="tendance"><xsl:value-of select="@tendance"/></xsl:with-param>
                  </xsl:call-template>
                  </text:p>
									</xsl:for-each>
								</table:table-cell>
							</table:table-row>
						</xsl:for-each>
					</table:table>
	</xsl:template>
	
	 <xsl:template name="OLDBADSTABLES">
      <text:h text:style-name="P18" text:outline-level="3">
          <xsl:value-of select="$evolution.repartition.oldstables"/>
        </text:h>
				<text:p text:style-name="P35">
  				<draw:frame draw:style-name="fr1" draw:name="Cadre7" text:anchor-type="as-char" svg:width="15.875cm" svg:height="7.938cm" draw:z-index="3">
  					<draw:image xlink:href="Pictures/repartition-EvolOldBadStable.png" xlink:type="simple" xlink:show="embed" xlink:actuate="onLoad"/>
  				</draw:frame>
  			</text:p>
  			<text:p text:style-name="Inter"><xsl:value-of select="$evolution.figure"/><text:sequence text:ref-name="refIllustration5" text:name="Illustration" text:formula="ooow:Illustration+1" style:num-format="1">5</text:sequence>
          <xsl:value-of select="$evolution.repartition.crit"/>
        </text:p>
				<text:p text:style-name="Standard"/>	
				<text:p text:style-name="Standard">
				  <text:span text:style-name="F12Bold">
				    <xsl:value-of select="$evolution.nbElts.oldbadstable"/></text:span>
            <xsl:value-of select="BADSTABLE/@nb"/>
				</text:p>
				<table:table>
						<table:table-column />
						<table:table-column />
						<table:table-row>
							<table:table-cell table:style-name="TableauDetailsActionPlan.B1" office:value-type="string">
								<text:p text:style-name="P35">
									<xsl:value-of select="$evolution.eltstable.eltdesc"/>
								</text:p>
							</table:table-cell>
							<table:table-cell table:style-name="TableauDetailsActionPlan.B1" office:value-type="string">
								<text:p text:style-name="P35">
									<xsl:value-of select="$evolution.eltstable.criteres"/>
								</text:p>
							</table:table-cell>
						</table:table-row>
						<xsl:for-each select="BADSTABLE/ELEMENT">
							<table:table-row>
								<table:table-cell table:style-name="TableauDetailsActionPlan.B2" office:value-type="string">
									<text:p text:style-name="Standard">
										<xsl:value-of select="@desc"/>
									</text:p>
								</table:table-cell>
								<table:table-cell table:style-name="TableauDetailsActionPlan.B2" office:value-type="string">
									<xsl:for-each select="CRITERION">
										<text:p text:style-name="Standard">- <xsl:value-of select="@lib" />
									<xsl:call-template name="INSERT_TENDANCE_PICTURE">
                      <xsl:with-param name="tendance"><xsl:value-of select="@tendance"/></xsl:with-param>
                  </xsl:call-template>
                  </text:p>
									</xsl:for-each>
								</table:table-cell>
							</table:table-row>
						</xsl:for-each>
					</table:table>
	</xsl:template>
	
	
	<xsl:template match="REPORT">
		<content>
		<xsl:if test="count(NEWBAD)>0 or count(OLDWORST)>0 or count(OLDBETTER)>0 or count(OLDBETTERWORST)>0 or count(OLDBADSTABLES)>0">
		  <text:h text:style-name="P60" text:outline-level="1">
			   <xsl:value-of select="$evolution.title"/>
		  </text:h>
        
				<text:p text:style-name="Standard"/>

	      <xsl:apply-templates select="GOAL_EVOLUTION" />
		  <xsl:call-template name="DEGREE_APPLICATION" />
		    <xsl:call-template name="TABLE" />
  	     <xsl:if test="NEWBAD/@nb > 0">
				    <xsl:call-template name="TPL_NEWBAD" />
				 </xsl:if>
			   <xsl:if test="OLDWORST/@nb > 0">
				    <xsl:call-template name="OLDWORST" />
				 </xsl:if>
			   <xsl:if test="OLDBETTER/@nb > 0">
            <xsl:call-template name="OLDBETTER" />
				 </xsl:if>
			   <xsl:if test="OLDBETTERWORST/@nb > 0">
            <xsl:call-template name="OLDBETTERWORST" />
				 </xsl:if>
			   <xsl:if test="OLDBADSTABLES/@nb > 0">
            <xsl:call-template name="OLDBADSTABLES" />
				 </xsl:if>
		</xsl:if>
		</content>
	</xsl:template>

	
</xsl:stylesheet>
