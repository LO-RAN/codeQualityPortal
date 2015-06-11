<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@ page errorPage="/errorPage.jsp" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>

<%String projectId = request.getParameter("projectId");
  String baselineId = request.getParameter("baselineId");
  String eaList = request.getParameter("eaList");
  String eaOptionList = request.getParameter("eaOptionList");
  %>
<html><head>
        <meta http-equiv='Content-Type' content='text/html; charset=UTF-8' />

<script src="js/xp_progress.js"></script>
<script>
function barStop() {
if (bar) {
bar.pause();
bar.hide();
}
}

function barStart() {
if (bar) {
bar.show();
bar.resume();
}
}

function createBaseline() {
showPendingAction("Cr&eacute;ation");
top.asResult.document.location.href="baseline?projectId=<%=projectId%>&action=0&forcedId=<%=baselineId%>";
}

function updateBaseline() {
showPendingAction("Mise &agrave; jour");
top.asResult.document.location.href="baseline?projectId=<%=projectId%>&baselineName=<%=baselineId%>&action=1&baselineId=<%=baselineId%>";
}

function buildAnalyse(step) {

  var theList = document.getElementById("outilSelect");

  var theSelectedItem = theList.selectedIndex;

  var theTool = theList.options[theSelectedItem].value;

  var theTool = theList.options[theSelectedItem].value;

  var master = document.getElementById("masterSelect").checked;

  analyseStatique(theTool, step, master);
}

function analyseStatique(tool, step, master) {
barStart();
showPendingAction("Analyse ("+tool+","+step+","+master+")");
top.asResult.document.location.href="analysestatique?projectId=<%=projectId%>&tool="+tool+"&step="+step+"&baselineId=<%=baselineId%>&master="+master+"&eaList=<%=eaList%>&eaList=<%=eaList%>&eaOptionList=<%=eaOptionList%>";
}

function calcul() {
barStart();
showPendingAction("Calcul");
top.asResult.document.location.href="calcul?projectId=<%=projectId%>&baselineId=<%=baselineId%>&eaList=<%=eaList%>&eaOptionList=<%=eaOptionList%>";
}

function mavenTest() {
barStart();
showPendingAction("Compilation et test des sources");
top.asResult.document.location.href="sourcemanager?projectId=<%=projectId%>&baselineId=<%=baselineId%>&goal=compile-and-test-sources&eaList=<%=eaList%>&eaOptionList=<%=eaOptionList%>";
}

function copySrcFiles() {
barStart();
showPendingAction("Copie des sources");
top.asResult.document.location.href="sourcemanager?projectId=<%=projectId%>&baselineId=<%=baselineId%>&goal=transformAndCopy&eaList=<%=eaList%>&eaOptionList=<%=eaOptionList%>";
}

function showPendingAction(theLabel) {
top.asResult.document.write("<HTML><HEAD></HEAD><BODY>"+theLabel+" en cours...</BODY></HTML>");
}
</script>
</head>
<body>
<Center><H3><bean:message key="caqs.as.attention" /></H3></center>
<bean:message key="caqs.as.message01" arg0='<%=projectId%>' arg1='<%=baselineId%>' />
<br/>
<table width="100%">
<tr>
<th width="50%" align="center"><bean:message key="caqs.as.etapes" /></th><th width="50%" align="center"><bean:message key="caqs.as.resultats" /></th>
</tr>
<tr><td>
<HR/>
1. <a href="javascript:createBaseline()"><bean:message key="caqs.as.createbaseline" /></a><BR />
&nbsp;&nbsp;&nbsp;1.1 <a href="javascript:analyseStatique('cobolprocextractor','analyse')"><bean:message key="caqs.as.cobol" /></a><BR/>
&nbsp;&nbsp;&nbsp;1.2 <a href="javascript:mavenTest()"><bean:message key="caqs.as.maven-test" /></a><BR/>
<HR/>
  <br/>
  2.&nbsp;<bean:message key="caqs.as.selection" /> :
  <select ID="outilSelect" size="1">
    <option                     value="uml"                 ><bean:message key="caqs.outils.uml" /></option>
    <option                     value="cast"                ><bean:message key="caqs.outils.CAST" /></option>
    <option                     value="checkstyle"          ><bean:message key="caqs.outils.checkstyle" /></option>
    <option                     value="pmd"                 ><bean:message key="caqs.outils.pmd" /></option>
    <option                     value="optimaladvisor"      ><bean:message key="caqs.outils.optimaladvisor" /></option>
    <option                     value="jdt"                 ><bean:message key="caqs.outils.jdt" /></option>
    <option                     value="jspanalyzer"         ><bean:message key="caqs.outils.jspanalyzer" /></option>
    <option                     value="devpartner"          ><bean:message key="caqs.outils.devpartner" /></option>
    <option                     value="junit"               ><bean:message key="caqs.outils.junit" /></option>
    <option                     value="mccabe"              ><bean:message key="caqs.outils.mccabe" /></option>
    <option                     value="systemcode"          ><bean:message key="caqs.outils.systemcode" /></option>
    <option                     value="pqc"                 ><bean:message key="caqs.outils.pqc" /></option>
    <option                     value="flawfinder"          ><bean:message key="caqs.outils.flawfinder" /></option>
    <option                     value="pmccabe"             ><bean:message key="caqs.outils.pmccabe" /></option>
    <option                     value="splint"              ><bean:message key="caqs.outils.splint" /></option>
    <option                     value="cppcheck"            ><bean:message key="caqs.outils.cppcheck" /></option>
    <option                     value="codeanalyzerpro"     ><bean:message key="caqs.outils.codeanalyzerpro" /></option>
    <option                     value="phpcs"               ><bean:message key="caqs.outils.phpcs" /></option>
    <option selected="selected" value="csmetricgeneration"  ><bean:message key="caqs.outils.csmetricgeneration" /></option>
    <option                     value="phoenix"             ><bean:message key="caqs.outils.phoenix" /></option>
    <option                     value="deventreprise"       ><bean:message key="caqs.outils.deventreprise" /></option>
    <option                     value="genericparser"       ><bean:message key="caqs.outils.genericparser" /></option>
    <option                     value="flexpmd"             ><bean:message key="caqs.outils.flexpmd" /></option>
    <option                     value="codeinspector"       ><bean:message key="caqs.outils.codeinspector" /></option>
    <option                     value="restnat"             ><bean:message key="caqs.outils.restnat" /></option>
    <option                     value="agility"             ><bean:message key="caqs.outils.agility" /></option>
  </select>
  <br/>
  <bean:message key="caqs.as.masterselect" />:&nbsp;<input type="checkbox" ID="masterSelect" checked="true" />
  <br/>
  3.&nbsp;<a href="javascript:buildAnalyse('analyseandload')"><bean:message key="caqs.as.analyseload" /></a><BR />
&nbsp;&nbsp;3.1&nbsp;<a href="javascript:buildAnalyse('analyse')"><bean:message key="caqs.as.analyse" /></a><BR />
&nbsp;&nbsp;3.2&nbsp;<a href="javascript:buildAnalyse('load')"><bean:message key="caqs.as.load" /></a><BR />
<hr/>
4. <a href="javascript:copySrcFiles()"><bean:message key="caqs.as.copysrcfiles" /></a><BR/>
5. <a href="javascript:calcul()"><bean:message key="caqs.as.calcul" /></a><BR/>
6. <a href="javascript:updateBaseline()"><bean:message key="caqs.as.update" /></a><BR/>
</td>
<td>
<div align="center">
<script type="text/javascript">
var bar=createBar(320,15,'white',1,'black','brown',100,7,3,"");
barStop();
</script>
</div>
<iframe name="asResult" height="100%" width="100%"></iframe></td>
</tr>
</table>
</body>
</html>

