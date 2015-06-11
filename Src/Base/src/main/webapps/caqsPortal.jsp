<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@ page errorPage="/errorPage.jsp" %>
<%@ page import="java.util.*,
		 java.io.File,
         com.compuware.caqs.domain.dataschemas.ElementType,
         com.compuware.caqs.security.auth.Users,
         com.compuware.caqs.presentation.util.RequestUtil"
         %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/auth.tld" prefix="auth" %>

<html>
    <head>

        <meta http-equiv='Content-Type' content='text/html; charset=UTF-8' />

        <title><bean:message key="caqs.caqsFullName" /></title>
        <jsp:include page="/ext/common/extjsFramework.jsp" />

<link href="<%= request.getContextPath()%>/css/carscode.css" rel="stylesheet" type="text/css" />
<link href="<%= request.getContextPath()%>/css/synthese.css" rel="stylesheet" type="text/css" />
<link href="<%= request.getContextPath()%>/css/main.css" rel="stylesheet" type="text/css" />

    </head>
    <body>


        <jsp:include page="/common/javascriptResources.jsp" />
    
        <div id="caqsloadingmask" class="ext-el-mask"></div>
        <div style="left: 100px; top: 100px;" id="caqsloadingmask2" class="ext-el-mask-msg x-mask-loading">
            <div><bean:message key="caqs.loading" /></div>
        </div>


        <jsp:include page="/ext/common/includes.jsp" />

        <%
            Users connectedUser = RequestUtil.getConnectedUser(request);
        %>

        <script language="javascript">
            // Contains the already opened graph applet window.
            openedGraphWindow = undefined;

            messagesZone = '<%= request.getAttribute("messageLocation")%>';
            messagesDisplayLimit = '<%= request.getAttribute("messagesDisplayLimit")%>';
            startingPage = '<%= request.getAttribute("startingPage")%>';
            dashboardDefaultDomain = '<%= request.getAttribute("dashboardDefaultDomain")%>',

                dashboardSeeConnectionTimeline = <%= request.getAttribute("seeConnectionTimeline")%>;

                dashboardSeeGlobalBaselineTimeline = <%= request.getAttribute("seeGlobalTimeline")%>;
                //recuperation du nom d'utilisateur
                lastname = "<%= connectedUser.getlastName()%>";
                firstname = "<%= connectedUser.getFirstName()%>";
                userName = getI18nResource("caqs.header.userNameFormating");
                userName = userName.replace("%firstname%", firstname);
                userName = userName.replace("%lastname%", lastname);
                documentations = new Array();
            <%
            File[] documentations = (File[]) request.getAttribute("documentations");
            if (documentations != null) {
                for (int i = 0; i < documentations.length; i++) {
            %>
                documentations[documentations.length] = {
                    name :      '<%= documentations[i].getName()%>'
                };
            <%
                }
            }
            %>

        </script>


        <SCRIPT type="text/javascript" src="<%= request.getContextPath()%>/gestionqualite/common/bottomUpDetail.js">
        </SCRIPT>
        <script type="text/javascript" src="<%= request.getContextPath()%>/js/functions.js">
        </script>
        <script type="text/javascript" src="<%= request.getContextPath()%>/js/number-functions.js">
        </script>
        <script type="text/javascript" src="<%= request.getContextPath()%>/ext/plugins/borderLayoutExtend.js">
        </script>
        <script type="text/javascript" src="<%= request.getContextPath()%>/common/messages.js">
        </script>
        <script type="text/javascript" src="<%= request.getContextPath()%>/admin/trashGrid.js">
        </script>
        <script type="text/javascript" src="<%= request.getContextPath()%>/admin/administration.js">
        </script>
        <script type="text/javascript" src="<%= request.getContextPath()%>/admin/administrationPanel.js">
        </script>
        <script type="text/javascript" src="<%= request.getContextPath()%>/common/projectTree.js">
        </script>
        <script type="text/javascript" src="<%= request.getContextPath()%>/common/commonLayout.js">
        </script>
        <script type="text/javascript" src="<%= request.getContextPath()%>/architecture/architecture.js">
        </script>
        <script type="text/javascript" src="<%= request.getContextPath()%>/gestionqualite/gestionQualite.js">
        </script>
        <script type="text/javascript" src="<%= request.getContextPath()%>/ext/plugins/miframe/miframe-min.js">
        </script>
        <script type="text/javascript" src="<%= request.getContextPath()%>/userPreferences.js">
        </script>
        <script type="text/javascript" src="<%= request.getContextPath()%>/admin/userRights.js">
        </script>
        <script type="text/javascript" src="<%= request.getContextPath() %>/admin/uploadSrcWnd.js">
        </script>
        <script type="text/javascript" src="<%= request.getContextPath() %>/admin/analysisSummary.js">
        </script>
        <script type="text/javascript" src="<%= request.getContextPath() %>/admin/commonInfoPanel.js">
        </script>
        <script type="text/javascript" src="<%= request.getContextPath() %>/admin/infosDomain.js">
        </script>
        <script type="text/javascript" src="<%= request.getContextPath() %>/admin/infosEA.js">
        </script>

<script type="text/javascript" src="<%= request.getContextPath()%>/modeleditor/searches/CaqsModelEditorSearchPanel.js">
</script>
<script type="text/javascript" src="<%= request.getContextPath()%>/modeleditor/searches/CaqsModelEditorGridPanel.js">
</script>
<script type="text/javascript" src="<%= request.getContextPath()%>/modeleditor/elementPanel.js">
</script>
<script type="text/javascript" src="<%= request.getContextPath()%>/modeleditor/editors/ElementEditor.js">
</script>
<script type="text/javascript" src="<%= request.getContextPath()%>/admin/users/UserEditorPanel.js">
</script>
<script type="text/javascript" src="<%= request.getContextPath()%>/admin/users/UserPanel.js">
</script>

        <script type="text/javascript" src="<%= request.getContextPath() %>/ext/plugins/columntree/ColumnNodeUI.js"></script>
        <link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/ext/plugins/columntree/column-tree.css" />
        <script type="text/javascript" src="<%= request.getContextPath() %>/ext/plugins/analyseprj/analyseprj.js"></script>

        <script language="javascript">
        var nameRegexp = new RegExp("[a-z]","gi");
        cancelProperty = '<%= org.apache.struts.taglib.html.Constants.CANCEL_PROPERTY%>';

        baselinesCbLib = '<bean:message key="caqs.baselines" />';
        DELETE_BASELINE_ACTION = <%= com.compuware.caqs.presentation.admin.actions.BaselineAction.ACTION_PURGE%>;
        RENAME_BASELINE_ACTION = <%= com.compuware.caqs.presentation.admin.actions.BaselineAction.ACTION_RENAME%>;

        </script>
        <script type="text/javascript" src="<%= request.getContextPath() %>/admin/purgeBaseline.js">
        </script>
        <script type="text/javascript" src="<%= request.getContextPath() %>/admin/infosSSP.js">
        </script>
        <script type="text/javascript" src="<%= request.getContextPath() %>/admin/commonInfoPanel.js">
        </script>
        <script type="text/javascript" src="<%= request.getContextPath() %>/admin/infosPRJ.js">
        </script>
        <script type="text/javascript" src="<%= request.getContextPath() %>/admin/launchAnalysisWnd.js">
        </script>
        <script type="text/javascript" src="<%= request.getContextPath() %>/admin/projectTree.js">
        </script>
        <script type="text/javascript" src="<%= request.getContextPath()%>/admin/projectsAdmin.js">
        </script>
        <script type="text/javascript" src="<%= request.getContextPath()%>/about.js">
        </script>
        <script src="<%= request.getContextPath()%>/js/syntaxe.js"></script>
        <script src="<%= request.getContextPath()%>/js/divers.js"></script>
        <link rel='stylesheet' type='text/css' href='<%= request.getContextPath() %>/ext/plugins/multiselect/Multiselect.css'/>
        <script type="text/javascript" src="<%= request.getContextPath() %>/ext/plugins/multiselect/Multiselect.js"></script>
        <script type="text/javascript" src="<%= request.getContextPath() %>/ext/plugins/multiselect/DDView.js"></script>
        <script type="text/javascript" src="<%= request.getContextPath() %>/ext/plugins/multiselect/SearchSelectorField.js"></script>
        <script type='text/javascript' src='<%= request.getContextPath() %>/ext/plugins/radiogroup/radiogroup.js'></script>

        <jsp:include page="/admin/userRights.jsp" />

        <jsp:include page="/modeleditor/modeleditor.jsp" />

        <jsp:include page="/dashboard/caqsDashboard.jsp" />


<script language="javascript" type="text/javascript" src="<%= request.getContextPath() %>/ext/plugins/portal/portal.js">
</script>
<script language="javascript" type="text/javascript" src="<%= request.getContextPath() %>/ext/plugins/portal/portalcolumn.js">
</script>
<script language="javascript" type="text/javascript" src="<%= request.getContextPath() %>/ext/plugins/portal/portlet.js">
</script>
<link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/ext/plugins/portal/portal.css" />

<script language="javascript" type="text/javascript" src="<%= request.getContextPath()%>/gestionqualite/common/justificationDemand.js">
</script>
<script type="text/javascript" src="<%= request.getContextPath() %>/gestionqualite/common/commentWindow.js">
</script>
<script type="text/javascript" src="<%= request.getContextPath() %>/gestionqualite/critereGrid.js">
</script>
<script type="text/javascript" src="<%= request.getContextPath() %>/gestionqualite/critere.js">
</script>
<script type="text/javascript" src="<%= request.getContextPath() %>/gestionqualite/qametriquechange.js">
</script>
<script type="text/javascript" src="<%= request.getContextPath() %>/gestionqualite/facteursynthesegrid.js">
</script>
<script type="text/javascript" src="<%= request.getContextPath() %>/gestionqualite/facteursynthese.js">
</script>
<script language="javascript" type="text/javascript" src="<%= request.getContextPath()%>/gestionqualite/evolutions/actionsPlanEvolutionsWnd.js">
</script>
<script language="javascript" type="text/javascript" src="<%= request.getContextPath()%>/gestionqualite/evolutions/actionsPlanEvolutions.js">
</script>
<script language="javascript" type="text/javascript" src="<%= request.getContextPath()%>/gestionqualite/evolutions/actionsPlanEvolutionsGraph.js">
</script>
<script language="javascript" type="text/javascript" src="<%= request.getContextPath()%>/gestionqualite/evolutions/volumetryEvolutions.js">
</script>
<script language="javascript" type="text/javascript" src="<%= request.getContextPath()%>/gestionqualite/evolutions/volumetryEvolutionsGraph.js">
</script>
<script language="javascript" type="text/javascript" src="<%= request.getContextPath()%>/gestionqualite/evolutions/elementsEvolutionsWnd.js">
</script>
<script language="javascript" type="text/javascript" src="<%= request.getContextPath()%>/gestionqualite/evolutions/elementsEvolutionsGraph.js">
</script>
<script language="javascript" type="text/javascript" src="<%= request.getContextPath()%>/gestionqualite/evolutions/elementsEvolutions.js">
</script>
<script language="javascript" type="text/javascript" src="<%= request.getContextPath()%>/gestionqualite/evolutions/evolutionsGraph.js">
</script>
<script language="javascript" type="text/javascript" src="<%= request.getContextPath()%>/gestionqualite/evolutions/goalsEvolutions.js">
</script>
<script language="javascript" type="text/javascript" src="<%= request.getContextPath()%>/gestionqualite/evolutions/evolutions.js">
</script>


<script language="javascript" type="text/javascript" src="<%= request.getContextPath() %>/js/treemaps/jit.js">
</script>
<link href="<%= request.getContextPath()%>/css/treemap.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="<%= request.getContextPath() %>/js/number-functions.js">
</script>
<script type="text/javascript" src="<%= request.getContextPath() %>/gestionqualite/domainSynthesis/domainSynthesisTreeMap.js">
</script>

<script type="text/javascript" src="<%= request.getContextPath() %>/gestionqualite/domainSynthesis/globalDomainSynthesis.js">
</script>

<script type="text/javascript" src="<%= request.getContextPath() %>/gestionqualite/domainSynthesis/repartitionCharts.js">
</script>

<script type="text/javascript" src="<%= request.getContextPath() %>/js/number-functions.js">
</script>

<script type="text/javascript" src="<%= request.getContextPath() %>/ext/plugins/Ext.ux.grid.ProgressColumn/Ext.ux.grid.ProgressColumn.js">
</script>
<link href="<%= request.getContextPath() %>/ext/plugins/Ext.ux.grid.ProgressColumn/Ext.ux.grid.ProgressColumn.css" rel="stylesheet" type="text/css" />

<script type="text/javascript" src="<%= request.getContextPath() %>/gestionqualite/domainSynthesis/volumetry.js">
</script>
<script type="text/javascript" src="<%= request.getContextPath() %>/gestionqualite/domainSynthesis/domainsynthesis.js">
</script>


<script type="text/javascript" src="<%= request.getContextPath() %>/ext/plugins/clickabletabpanel.js">
</script>
<script type="text/javascript" src="<%= request.getContextPath() %>/ext/plugins/miframe/miframe.js">
</script>
<script type="text/javascript" src="<%= request.getContextPath() %>/gestionqualite/topdown.js">
</script>
<script type="text/javascript" src="<%= request.getContextPath() %>/gestionqualite/mainFrame.js">
</script>

<SCRIPT type="text/javascript" src="<%= request.getContextPath()%>/gestionqualite/bottomUpSynthese.js">
</SCRIPT>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/number-functions.js">
</script>

<script type="text/javascript" src="<%= request.getContextPath() %>/gestionqualite/actionplan/actionPlanGridCheckBox.js">
</script>
<script type="text/javascript" src="<%= request.getContextPath() %>/gestionqualite/common/commentWindow.js">
</script>
<script type="text/javascript" src="<%= request.getContextPath() %>/gestionqualite/actionplan/actionplan.js">
</script>


<script type="text/javascript" src="<%= request.getContextPath() %>/gestionqualite/projectSynthesis/projectSynthesisTreeMap.js">
</script>
<script type="text/javascript" src="<%= request.getContextPath() %>/gestionqualite/projectSynthesis/globalProjectSynthesis.js">
</script>
<script type="text/javascript" src="<%= request.getContextPath() %>/gestionqualite/projectSynthesis/repartitionCharts.js">
</script>
<script type="text/javascript" src="<%= request.getContextPath() %>/gestionqualite/projectSynthesis/volumetry.js">
</script>
<script type="text/javascript" src="<%= request.getContextPath() %>/gestionqualite/actionplan/projectActionPlan.js">
</script>
<SCRIPT type="text/javascript" src="<%= request.getContextPath()%>/gestionqualite/projectSynthesis/projectSynthesis.js">
</SCRIPT>

<jsp:include page="/gestionqualite/synthese/justificationsPopup.jsp" />
<jsp:include page="/label.jsp" />
<script type="text/javascript" src="<%= request.getContextPath() %>/gestionqualite/synthese.js">
</script>

        <script type="text/javascript" src="<%= request.getContextPath()%>/caqsPortal.js">
        </script>
        <script language="javascript">

            caqsPortal = undefined;

            customHeaderRight = '<%= request.getAttribute("customHeaderRight")%>';
            if(customHeaderRight=='null') {
                customHeaderRight = null;
            }
            customHeaderLeft = '<%= request.getAttribute("customHeaderLeft")%>';
            if(customHeaderLeft=='null') {
                customHeaderLeft = null;
            }

            goToSelectedFavorite = function(pIdElt, pIdPrj) {
                if(caqsPortal != null) {
                    caqsPortal.goToSelectedFavorite(pIdElt, pIdPrj);
                }
            };

            caqsOnReady = function(){
                caqsPortal = new Ext.ux.CaqsPortal({
                    messagesZone:       messagesZone,
                    startingPage:       startingPage,
                    dashboardSeeConnectionTimeline: dashboardSeeConnectionTimeline,
                    dashboardSeeGlobalBaselineTimeline: dashboardSeeGlobalBaselineTimeline,
                    documentations:     documentations,
                    dashboardDefaultDomain: dashboardDefaultDomain
                });
                caqsPortal.start();
                var el = Ext.get("caqsloadingmask");
                el.remove();
                el = Ext.get("caqsloadingmask2");
                el.remove();
            };
        </script>
    </body>
</html>
