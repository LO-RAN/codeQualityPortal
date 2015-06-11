<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@ page import="com.compuware.caqs.domain.dataschemas.JustificationBean" %>
<%@ page import="com.compuware.caqs.domain.dataschemas.CriterionDefinition" %>
<%@ page import="com.compuware.caqs.presentation.util.RequestUtil" %>
<%@ page import="java.util.*" %>
<%@ page errorPage="/errorPage.jsp" %>

<jsp:include page="/ext/common/extjsFramework.jsp" />
<jsp:include page="/common/javascriptResources.jsp" />
<jsp:include page="/ext/common/includes.jsp" />


<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/auth.tld" prefix="auth" %>

<html>
<auth:checkElement/>
<head>
    <meta http-equiv='Content-Type' content='text/html; charset=UTF-8' />
<meta http-equiv="pragma" content="no-cache">
<title><bean:message key="caqs.justificatif.title" /></title>
<link href="css/carscode.css" rel="stylesheet" type="text/css" />
<jsp:useBean id="user" scope="session" class="com.compuware.caqs.security.auth.Users" />
</head>
<%
    if (session == null || user == null || user.getId() == null || user.getId().equals("")) { %>
		<jsp:forward page="sessiontimedout.jsp"/>
<%  }
    else {
        Locale locale = RequestUtil.getLocale(request);
    String pageSuivante = (String)request.getAttribute("page");
    String id_elt = (String)request.getAttribute("id_elt");
    String id_telt = (String)request.getAttribute("id_telt");
    String sub_elt = (String)request.getAttribute("sub_elt");
    String id_pere = (String)request.getAttribute("id_pere");
    String lib_elt = (String)request.getAttribute("lib_elt");
    String id_bline = (String)request.getAttribute("id_bline");
    String id_pro = (String)request.getAttribute("id_pro");
    String id_crit = (String)request.getAttribute("id_crit");
	CriterionDefinition crit = new CriterionDefinition();
	crit.setId(id_crit);
    String lib_crit = crit.getEscapedLib(true, false, false, locale);
    String id_fac = (String)request.getAttribute("id_fac");
    String id_met = (String)request.getAttribute("id_met");
    String id_just = (String)request.getAttribute("id_just");
    String nc = (String)request.getAttribute("notecalc");
    String only_crit = (String)request.getAttribute("only_crit");
    String crit_list = (String)request.getAttribute("crit_list");
    JustificationBean just = (JustificationBean)request.getAttribute("justificatif");
%>
<BODY style="margin-top:5px;">

<div id="divJustificationValid" > </div>

</body>

<script language="javascript">
	function submitForm(action) {
		panel.getForm().getEl().dom.action= requestContextPath + '/Justification.do?action='+action;
		panel.getForm().getEl().dom.submit();
	}

caqsOnReady = function(){
    // turn on validation errors beside the field globally
    Ext.form.Field.prototype.msgTarget = 'side';

	var validateFunction = function() {
		submitForm('valid');
	};

	var validButton = new Ext.Button({
		text:		'<bean:message key="caqs.valider" />',
		name:		'valid',
		formBind:	true,
		handler: 	validateFunction
	});

	var cancelFunction = function() {
		submitForm('cancel');
	};

	var cancelButton = new Ext.Button({
		text:		'<bean:message key="caqs.annuler" />',
		name:		'cancel',
		handler: 	cancelFunction
	});

	panel = new Ext.form.FormPanel({
		title:				'<bean:message key="caqs.justificatif.justificatif" />',
		width:				600,
		autoHeight:			true,
		monitorValid:		true,
		bodyStyle:			'padding-left: 5px; padding-top: 5px;',
		labelWidth:			100,
		submit: function() {
            this.getForm().getEl().dom.submit();
        },
		items: [
            <% if (id_crit != null) { %>
			{
				name:       'id_crit',
				id:         'id_crit',
				value:      '<%=id_crit%>',
				xtype:      'hidden'
			},{
				name:       'id_fac',
				id:         'id_fac',
				value:      '<%=id_fac%>',
				xtype:      'hidden'
			},
            <% } else {
					if (id_fac != null) { %>
			{
				name:       'id_fac',
				id:         'id_fac',
				value:      '<%=id_fac%>',
				xtype:      'hidden'
			},
			<% } else { %>
			{
				name:       'id_met',
				id:         'id_met',
				value:      '<%=id_met%>',
				xtype:      'hidden'
			},
                     <% }
               } %>
            <% if (id_just != null) { %>
			{
				name:       'update',
				id:         'update',
				value:      'true',
				xtype:      'hidden'
			},
            <% } %>
			{
				name:       'id_pro',
				id:         'id_pro',
				value:      '<%=id_pro%>',
				xtype:      'hidden'
			},{
				name:       'notecalc',
				id:         'notecalc',
				value:      '<%=nc%>',
				xtype:      'hidden'
			},{
				name:       'only_crit',
				id:         'only_crit',
				value:      '<%=only_crit%>',
				xtype:      'hidden'
			},{
				name:       'crit_list',
				id:         'crit_list',
				value:      '<%=crit_list%>',
				xtype:      'hidden'
			},{
				name:       'lib_elt',
				id:         'lib_elt',
				value:      '<%=lib_elt%>',
				xtype:      'hidden'
			},{
				name:       'cuser',
				id:         'cuser',
				value:      '<%=just.getUser() != null ? just.getUser() : user.getId()%>',
				xtype:      'hidden'
			},{
				name:       'id_elt',
				id:         'id_elt',
				value:      '<%=id_elt%>',
				xtype:      'hidden'
			},{
				name:       'id_just',
				id:         'id_just',
				value:      '<%=just.getId()%>',
				xtype:      'hidden'
			},{
				name:       'page',
				id:         'page',
				value:      '<%=pageSuivante%>',
				xtype:      'hidden'
			},{
				name:       'id_pere',
				id:         'id_pere',
				value:      '<%=id_pere%>',
				xtype:      'hidden'
			},{
				name:       'id_bline',
				id:         'id_bline',
				value:      '<%=id_bline%>',
				xtype:      'hidden'
			},{
				name:       'id_telt',
				id:         'id_telt',
				value:      '<%=id_telt%>',
				xtype:      'hidden'
			},{
				name:       'from',
				id:         'from',
				value:      'justificatif',
				xtype:      'hidden'
			},{
				name:       'sub_elt',
				id:         'sub_elt',
				value:      '<%=sub_elt%>',
				xtype:      'hidden'
			},
					{
						xtype:			'statictextfield',
						parentClassName: 	'smallStaticField',
						fieldLabel:		'<bean:message key="caqs.justificatif.element" />',
						value:			'<%=lib_elt%>'
					},
        <% if (id_crit != null) { %>
					{
						xtype:			'statictextfield',
						parentClassName: 	'smallStaticField',
						fieldLabel:		'<bean:message key="caqs.critere" />',
						value:			'<%=lib_crit%>'
					},
        <% } else {
                if (id_fac != null) { %>
					{
						xtype:			'statictextfield',
						parentClassName: 	'smallStaticField',
						fieldLabel:		'<bean:message key="caqs.objectif" />',
						value:			'<%=id_fac%>'
					},
             <% } else { %>
					{
						xtype:			'statictextfield',
						parentClassName: 	'smallStaticField',
						fieldLabel:		'<bean:message key="caqs.metrique" />',
						value:			'<%=id_met%>'
					},
             <% }
           } %>
				{
					xtype:			'statictextfield',
					parentClassName: 	'smallStaticField',
					fieldLabel:		'<bean:message key="caqs.justificatif.note" />',
	    			value:			'<%=nc%>'
				},
				{
					xtype:			'statictextfield',
					parentClassName: 	'smallStaticField',
					fieldLabel:		'<bean:message key="caqs.justificatif.auteur" />',
	    			value:			'<%=just.getUser() != null ? just.getUser() : user.getId()%>'
				},
   <% if (just != null && just.getNote() > 0) { %>
				{
					xtype:			'statictextfield',
					parentClassName: 	'smallStaticField',
					fieldLabel:		'<bean:message key="caqs.justificatif.noteforcee" />',
	    			value:			'<%=just.getNote()%>'
				},
   <% } 
	if(just!=null) {%>
				{
					xtype:			'statictextfield',
					parentClassName: 	'smallStaticField',
					fieldLabel:		'<bean:message key="caqs.justificatif.statut" />',
	<%if("DEMAND".equals(just.getStatut())) {%>
					value:			'<bean:message key="caqs.justificatif.statutEnCours" />'
	<% } %>
	<%if("VALID".equals(just.getStatut())) {%>
					value:			'<bean:message key="caqs.justificatif.statutValide" />'
	<% } %>
	<%if("REJET".equals(just.getStatut())) {%>
					value:			'<bean:message key="caqs.justificatif.statutRejete" />'
	<% } %>
				},
				{
					xtype:			'hidden',
					name:			'statut',
					id:				'statut',
					value:			'<%= just.getStatut()%>'
				},
<% } %>
				{
					xtype:			'textfield',
					fieldLabel:		'<bean:message key="caqs.justificatif.libelle" />',
					allowBlank:		false,
					name:			'just_lib',
					id:				'just_lib',
					maxLengthText:	'<bean:message key="caqs.maxlength.text" /> {0}',
					maxLength:		32,
					value:			'<%=just != null ? just.getEscapedLib(true, false, false) : ""%>'
				},
				{
					xtype:			'textarea',
					fieldLabel:		'<bean:message key="caqs.justificatif.description" />',
					allowBlank:		false,
					name:			'just_desc',
					id:				'just_desc',
					maxLength:		128,
					maxLengthText:	"<bean:message key="caqs.maxlength.text" /> {0}",
					value:			'<%=just != null ? just.getEscapedDesc(true, false, false) : ""%>',
					width:			270,
					height:			70
				}
		],
		buttons: [
            <auth:access function="Justification_Creation">
	            validButton,
            </auth:access>
			cancelButton
		]
	});
	panel.render("divJustificationValid");
};
</script>

<% } %>
</html>
