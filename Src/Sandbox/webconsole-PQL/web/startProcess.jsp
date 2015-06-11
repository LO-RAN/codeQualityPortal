<HTML>
    <HEAD>
        <TITLE>Manual Task</TITLE>
    </HEAD>

    <BODY>

<%
    String projectId = request.getParameter("projectId");
    String packageId = request.getParameter("packageId");
    String taskDefId = request.getParameter("taskId");
%>

<FORM method="POST" action="http://cwfr-d070:8181/oview/wrd/run/UWWMTE30">
  <FONT FACE="Arial, Helvetica, sans-serif">
    <HR>
    <B>Lancement Manuel</B>
  </FONT>
  <HR>

<INPUT TYPE=HIDDEN NAME="UTASKID.UHIDDENDUMMY.UWFM" VALUE="<%=taskDefId%> <%=packageId%>" size=1>
<B>
Paramètres:&nbsp;
</B>
<TABLE WIDTH="510" BORDER="0">
    <TR>
      <TD>
        Projet:&nbsp;<%=projectId%>
        <INPUT TYPE=HIDDEN NAME="UPARAMNAME.UOUTPARAMDUMMY.UWFM.1." VALUE="vp_projectId" >
        <INPUT TYPE=HIDDEN NAME="UVALUE.UOUTPARAMDUMMY.UWFM.1." VALUE="<%=projectId%>" size=26>
      </TD>
   </TR>

</TABLE>
<P>
<HR>
    <INPUT TYPE=SUBMIT NAME="UOK.UFOOTERDUMMY.UWFM" VALUE="Submit" >
    <INPUT TYPE=SUBMIT NAME="UCANCEL.UFOOTERDUMMY.UWFM" VALUE="Cancel" >
</PRE>

</FORM>

<!-- A href="http://cwfr-d070:8181/oview/wrd/run/UWWMTE30?UOK.UFOOTERDUMMY.UWFM=Submit&UPARAMNAME.UOUTPARAMDUMMY.UWFM.1.=vp_projectId&UVALUE.UOUTPARAMDUMMY.UWFM.1.=tto&UTASKID.UHIDDENDUMMY.UWFM=40726781640726788525501 40709226440709315950156">Lien Test</A> -->


</BODY>
</HTML>
