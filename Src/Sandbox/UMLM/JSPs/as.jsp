<html>
<body>
<%String projectId = request.getParameter("projectId");%>
<%String baselineId = request.getParameter("baselineId");%>

<Center><H3>Attention à utiliser Etape par Etape.</H3><H4>Chaque étape est finie et valide lorsqu'une fenêtre s'ouvre avec ecrit ok=1; suivi ou non d'autres caractères.<BR>Un autre résultat doit être interprété comme une erreur (attention possibilité de problème d'affichage du aux cartes graphiques si page blanche regarder la source du document ne pas faire de mise à jour).</H4></Center>
<BR>
Vous travaillez sur le projet ayant l'id : <%=projectId%><BR>
Pour la Baseline ayant l'id : <%=baselineId%><BR>
Etapes :<BR>
<BR>
<BR>
<HR>
<a href="/carscode/servlet/com.compuware.carscode.servlet.DoAllServlet?projectId=<%=projectId%>&baselineId=<%=baselineId%>" target="NewWindow">Do All [JAVA ONLY]</a><BR>
<HR>
<HR>
<a href="/carscode/servlet/com.compuware.carscode.servlet.AnalyseStatiqueServlet?projectId=<%=projectId%>&tool=uml&step=analyseandload&baselineId=<%=baselineId%>" target="NewWindow">Analyse Statique Uml ET chargement</a><BR>
&nbsp;&nbsp;&nbsp;<a href="/carscode/servlet/com.compuware.carscode.servlet.AnalyseStatiqueServlet?projectId=<%=projectId%>&tool=uml&step=analyse&baselineId=<%=baselineId%>" target="NewWindow">Analyse Statique uml seule</a><BR>
&nbsp;&nbsp;&nbsp;<a href="/carscode/servlet/com.compuware.carscode.servlet.AnalyseStatiqueServlet?projectId=<%=projectId%>&tool=uml&step=load&baselineId=<%=baselineId%>" target="NewWindow">Chargement des données uml</a><BR>
<HR>
1. <a href="/carscode/servlet/com.compuware.carscode.servlet.BaselineServlet?projectId=<%=projectId%>&action=0&forcedId=<%=baselineId%>" target="NewWindow">Create Baseline</a><BR>
&nbsp;&nbsp;&nbsp;1.1 <a href="/carscode/servlet/com.compuware.carscode.servlet.AnalyseStatiqueServlet?projectId=<%=projectId%>&tool=cobolprocextractor&step=analyse&baselineId=<%=baselineId%>" target="NewWindow">Extraction des procédures Cobol</a><BR>
2. <a href="/carscode/servlet/com.compuware.carscode.servlet.AnalyseStatiqueServlet?projectId=<%=projectId%>&tool=mccabe&step=analyseandload&baselineId=<%=baselineId%>" target="NewWindow">Analyse Statique McCabe ET chargement</a><BR>
&nbsp;&nbsp;&nbsp;2.1 <a href="/carscode/servlet/com.compuware.carscode.servlet.AnalyseStatiqueServlet?projectId=<%=projectId%>&tool=mccabe&step=analyse&baselineId=<%=baselineId%>" target="NewWindow">Analyse Statique McCabe seule</a><BR>
&nbsp;&nbsp;&nbsp;2.2 <a href="/carscode/servlet/com.compuware.carscode.servlet.AnalyseStatiqueServlet?projectId=<%=projectId%>&tool=mccabe&step=load&baselineId=<%=baselineId%>" target="NewWindow">Chargement des données McCabe</a><BR>
3. <a href="/carscode/servlet/com.compuware.carscode.servlet.AnalyseStatiqueServlet?projectId=<%=projectId%>&tool=devpartner&step=analyseandload&baselineId=<%=baselineId%>" target="NewWindow">Analyse Statique Devpartner ET Chargement</a><BR>
&nbsp;&nbsp;&nbsp;3.1<a href="/carscode/servlet/com.compuware.carscode.servlet.AnalyseStatiqueServlet?projectId=<%=projectId%>&tool=devpartner&step=analyse&baselineId=<%=baselineId%>" target="NewWindow">Analyse Statique Devpartner seule</a><BR>
&nbsp;&nbsp;&nbsp;3.2<a href="/carscode/servlet/com.compuware.carscode.servlet.AnalyseStatiqueServlet?projectId=<%=projectId%>&tool=devpartner&step=load&baselineId=<%=baselineId%>" target="NewWindow">Chargement des données Devpartner</a><BR>
3bis. <a href="/carscode/servlet/com.compuware.carscode.servlet.AnalyseStatiqueServlet?projectId=<%=projectId%>&tool=devpartnerrulebyrule&step=analyseandload&baselineId=<%=baselineId%>" target="NewWindow">Analyse Statique Devpartner rule by rule ET Chargement</a><BR>
&nbsp;&nbsp;&nbsp;3.1<a href="/carscode/servlet/com.compuware.carscode.servlet.AnalyseStatiqueServlet?projectId=<%=projectId%>&tool=devpartnerrulebyrule&step=analyse&baselineId=<%=baselineId%>" target="NewWindow">Analyse Statique Devpartner seule</a><BR>
&nbsp;&nbsp;&nbsp;3.2<a href="/carscode/servlet/com.compuware.carscode.servlet.AnalyseStatiqueServlet?projectId=<%=projectId%>&tool=devpartnerrulebyrule&step=load&baselineId=<%=baselineId%>" target="NewWindow">Chargement des données Devpartner</a><BR>
4.<a href="/carscode/servlet/com.compuware.carscode.servlet.AnalyseStatiqueServlet?projectId=<%=projectId%>&tool=checkstyle&step=analyseandload&baselineId=<%=baselineId%>" target="NewWindow">Analyse Statique CheckStyle ET Chargement</a><BR>
&nbsp;&nbsp;&nbsp;3.1<a href="/carscode/servlet/com.compuware.carscode.servlet.AnalyseStatiqueServlet?projectId=<%=projectId%>&tool=checkstyle&step=analyse&baselineId=<%=baselineId%>" target="NewWindow">Analyse Statique CheckStyle seule</a><BR>
&nbsp;&nbsp;&nbsp;3.2<a href="/carscode/servlet/com.compuware.carscode.servlet.AnalyseStatiqueServlet?projectId=<%=projectId%>&tool=checkstyle&step=load&baselineId=<%=baselineId%>" target="NewWindow">Chargement des données CheckStyle</a><BR>
5. <a href="/carscode/servlet/com.compuware.carscode.ihm.servlets.CalculServlet?id_pro=<%=projectId%>&id_bline=<%=baselineId%>" target="NewWindow">Calculs</a><BR>
6. <a href="/carscode/servlet/com.compuware.carscode.servlet.BaselineServlet?projectId=<%=projectId%>&baselineName=test<%=baselineId%>&action=1&baselineId=<%=baselineId%>" target="NewWindow">Update Baseline</a><BR>

</body>
</html>

