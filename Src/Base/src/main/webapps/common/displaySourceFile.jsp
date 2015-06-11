<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<html>
<head>
    <meta http-equiv='Content-Type' content='text/html; charset=UTF-8' />
    <title><%= request.getAttribute("descElt")%></title>
<%
boolean fromScatterplot = false;
Boolean sFromScatterplot = (Boolean)request.getAttribute("fromScatterplot");
if(sFromScatterplot!=null) {
    fromScatterplot = sFromScatterplot.booleanValue();
}

if(!fromScatterplot) {
%>
<jsp:include page="/ext/common/extjsFramework.jsp" />
<jsp:include page="/common/javascriptResources.jsp" />
<jsp:include page="/ext/common/includes.jsp" />
<%
}
%>

<%= (String) request.getAttribute("responseText")%>
<%
if(!fromScatterplot) {
    String params = "?id_elt="+(String)request.getAttribute("idElt");
    String endRequest = (String)request.getAttribute("idCrit");
    if(endRequest!=null) {
        params += "&id_crit="+endRequest;
    }

%>
<script language="javascript">
var versionImprimableFct = function() {
    PopupCentrer(requestContextPath+"/RetrievePrintableSourceFile.do<%= params%>",640,480,"menubar=yes,statusbar=no,scrollbars=yes,resizable=yes");
};

caqsOnReady = function(){
    new Ext.Button({
        applyTo:    'printableVersionButton',
        text:       getI18nResource("caqs.critere.versionimprimable"),
        icon:       requestContextPath+'/images/printer.gif',
        cls:        "x-btn-text-icon",
        style:      'padding-left: 10px;',
        handler:    versionImprimableFct
    });
};

</script>
<%
}
%>
</html>