<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@ page import="com.compuware.caqs.domain.dataschemas.ElementBean" %>
<%@page import="com.compuware.caqs.presentation.util.RequestUtil"%>
<%@ page import="java.util.*" %>
<%@ page errorPage="errorPage.jsp" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/auth.tld" prefix="auth" %>
<html>
<head>
    <meta http-equiv='Content-Type' content='text/html; charset=UTF-8' />
<meta http-equiv="pragma" content="no-cache">
<title><bean:message key="caqs.labellisationValid.title" /></title>
<script language="javascript">
function validForm() {
	var retour = true;
	var comm = document.getElementById('desc');
	if(comm.value=='' && !cancel) {
		alert('<bean:message key="caqs.labellisationValid.commentMandatory" />');
		retour = false;
	}
	return retour;
}

var cancel = false;
function setCancel(v) {
	cancel = v;
}
</script>
<jsp:include page="/ext/common/extjsFramework.jsp" />
<jsp:include page="/common/javascriptResources.jsp" />
<jsp:include page="/ext/common/includes.jsp" />
</head>
<link href="css/carscode.css" rel="stylesheet" type="text/css" />
<jsp:useBean id="user" scope="session" class="com.compuware.caqs.security.auth.Users" />
<%
    if (session == null || user == null || user.getId() == null || user.getId().equals("")) { %>
		<jsp:forward page="sessiontimedout.jsp"/>
<%  }
    else {
    String req = (String)request.getAttribute("req");
    ElementBean eltBean = (ElementBean)request.getAttribute("labelElement");
    
    com.compuware.caqs.presentation.consult.forms.LabelForm formBean = (com.compuware.caqs.presentation.consult.forms.LabelForm)request.getAttribute("labelForm");
%>
<BODY>

<div id="labellisationPanel"></div>
<div id="topdown"></div>

<script type="text/javascript" src="<%= request.getContextPath() %>/gestionqualite/synthese.js">
</script>

</body>

<script language="javascript">
	function submitForm(action) {
		panel.getForm().getEl().dom.action='<%=request.getContextPath()%>/LabelUpdate.do?action='+action;
		panel.getForm().getEl().dom.name = 'labelForm';
		panel.getForm().getEl().dom.submit();
	}

caqsOnReady = function(){
	var validateFunction = function() {
		setCancel(false);
		if(validForm()) {
			submitForm('valid');
		} 
	};

	validButton = new Ext.Button({
		text:		'<bean:message key="caqs.valider" />',
		name:		'valid',
		formBind:	true,
		style:		'margin-left: 5px;',
		handler: 	validateFunction
	});

	var changeLibFunction = function() {
		setCancel(false);
		if(validForm()) {
			submitForm('changeLib');
		} 
	};

	changeLibButton = new Ext.Button({
		text:		'<bean:message key="caqs.valider" />',
		name:		'changeLib',
		formBind:	true,
		style:		'margin-left: 5px;',
		handler: 	changeLibFunction
	});

	var validateReserveFunction = function() {
		setCancel(false);
		if(validForm()) {
			submitForm('validReserve');
		}
	};

	validReserveButton = new Ext.Button({
		text:		'<bean:message key="caqs.sousreserve" />',
		name:		'valid',
		formBind:	true,
		style:		'margin-left: 5px;',
		handler: 	validateReserveFunction
	});

	var rejectFunction = function() {
		setCancel(false);
		if(validForm()) {
			submitForm('rejet');
		}
	};

	var rejectButton = new Ext.Button({
		text:		'<bean:message key="caqs.refuser" />',
		name:		'rejet',
		style:		'margin-left: 5px;',
		formBind:	true,
		handler: 	rejectFunction
	});

	var cancelFunction = function() {
		panel.getForm().findField("<%= org.apache.struts.taglib.html.Constants.CANCEL_PROPERTY%>").setValue("desc");
		setCancel(true);
		if(validForm()) {
			submitForm('cancel');
		}
	};

	var cancelButton = new Ext.Button({
		text:		'<bean:message key="caqs.annuler" />',
		name:		'cancel_justif',
		style:		'margin-left: 5px;',
		handler: 	cancelFunction
	});

<%
String demandDesc = "";
String demandLib = "";
String demandUser = "";
String demandDinst = "";
if(formBean.getDemand()!=null) {
	demandDesc = formBean.getDemand().getEscapedDesc(true, false, false);
	demandLib = formBean.getDemand().getEscapedLib(true, false, false);
	demandUser = formBean.getDemand().getUser();
	demandDinst = formBean.getDemand().getDinst();
}

String userLib = formBean.getUser();
String lib = formBean.getEscapedLib(true, false, false);
String desc = formBean.getEscapedDesc(true, false, false);
String dinst = formBean.getDinst();

%>	
	panel = new Ext.form.FormPanel({
		title:				'<bean:message key="caqs.labellisationValid.element" /> : &nbsp;<bean:write name="labelElement" property="lib"  filter="true" />',
		width:				900,
		autoHeight:			true,
		monitorValid:		true,
		labelWidth:			60,
		method:				"POST",
		submit: function() {
            this.getForm().getEl().dom.submit();
        },
		items: [
			{
                name:       '<%= org.apache.struts.taglib.html.Constants.CANCEL_PROPERTY%>',
                id:	       '<%= org.apache.struts.taglib.html.Constants.CANCEL_PROPERTY%>',
                value:      '',
                xtype:      'hidden'
            },{
				name:       'id',
				id:         'id',
				value:      '<bean:write name="labelForm" property="id" />',
				xtype:      'hidden'
			},{
				name:       'demand.id',
				id:         'demand.id',
				value:      '<bean:write name="labelForm" property="demand.id" />',
				xtype:      'hidden'
			},{
				name:       'demand.lib',
				id:         'demand.lib',
				value:      '<%= demandLib%>',
				xtype:      'hidden'
			},{
				name:       'status',
				id:         'status',
				value:      '<bean:write name="labelForm" property="status" />',
				xtype:      'hidden'
			},{
				name:       'demand.status',
				id:         'demand.status',
				value:      '<bean:write name="labelForm" property="demand.status" />',
				xtype:      'hidden'
			},{
				name:       'demand.desc',
				id:         'demand.desc',
				value:      '<%= demandDesc%>',
				xtype:      'hidden'
			},{
				name:       'user',
				id:         'user',
				value:      '<%= userLib%>',
				xtype:      'hidden'
			},{
				name:       'demand.user',
				id:         'demand.user',
				value:      '<%= demandUser%>',
				xtype:      'hidden'
			},{
				name:       'req',
				id:         'req',
				value:      '<%=req%>',
				xtype:      'hidden'
			},{
				name:       'from',
				id:         'from',
				value:      'labellisation',
				xtype:      'hidden'
			},
			{
				layout:			'table',
				layoutConfig:	{
					columns:	2
				},
				width:			900,
				border:			false,
				style:			'margin-bottom: 5px;',
				items: [
        			new Ext.form.FieldSet({
						title:			'<bean:message key="caqs.labellisationValid.demande" />',
						height:			190,
						style:			'margin-left: 5px;',
						labelWidth:		130,
						width:			440,
						items:[
							{
								xtype:				'statictextfield',
								parentClassName: 	'smallStaticField',
								smallLine:			true,
								fieldLabel:			"<bean:message key="caqs.labellisationValid.redacteur" />",
								value:				'<%= demandUser%>'							
							}
							,{
								xtype:				'statictextfield',
								parentClassName: 	'smallStaticField',
								smallLine:		true,
								fieldLabel:			'<bean:message key="caqs.labellisationValid.dateCreation" />',
						        value:				'<%= demandDinst%>'
							}
							,{
								xtype:				'statictextfield',
								parentClassName: 	'smallStaticField',
								smallLine:		true,
								fieldLabel:			"<bean:message key="caqs.labellisationValid.oldlibelle" />",
								value:				'<%= demandLib%>'
							}, {
								xtype:				'textarea',
								fieldLabel:			"<bean:message key="caqs.labellisationValid.oldcommentaire" />",
						        value:				'<%= demandDesc%>',
						        id:					"demand.desc",
						        name:				"demand.desc",
						        disabled:			true,
								width:				270,
								height:				70
							}
						]
					})
					, new Ext.form.FieldSet({
						title:			'<bean:message key="caqs.labellisationValid.labellisation" />',
						height:			190,
						style:			'margin-left: 5px;',
						labelWidth:		130,
						width:			440,
						items:[
							{
								xtype:			'statictextfield',
								smallLine:		true,
								parentClassName: 	'smallStaticField',
								fieldLabel:		"<bean:message key="caqs.labellisationValid.qualiticien" />",
								value:			'<%= userLib%>'							
							
							}
							,{
								xtype:			'statictextfield',
								smallLine:		true,
								parentClassName: 	'smallStaticField',
								fieldLabel:		'<bean:message key="caqs.labellisationValid.dateLabellisation" />',
						        value:			'<%= dinst%>'
							} ,
							{
								name:       'lib',
								id:         'lib',
								value:      '<%= lib%>',
								xtype:      'hidden'
							},{
								xtype:			'statictextfield',
								smallLine:		true,
								parentClassName: 	'smallStaticField',
								fieldLabel:		'<bean:message key="caqs.labellisationValid.libelle" />',
						        value:			'<%= lib%>'
							} ,
							{
								xtype:				'textarea',
								maxLength:			128,
								allowBlank:			false,
								maxLengthText:		'<bean:message key="caqs.maxlength.text" /> {0}',
								fieldLabel:			"<bean:message key="caqs.labellisationValid.commentaire" />",
								value:				'<%= desc%>',
								id:					'desc',
								name:				'desc',
								width:				270,
								height:				70
							}
						]
					})
				]
			}
		],
		buttons: [
	        <logic:equal name="labelForm" property="status" value="DEMAND">
				<auth:access function="LABEL_VALIDATION">
					validButton,
					validReserveButton,
					rejectButton,
				</auth:access>
            </logic:equal>
	        <logic:notEqual name="labelForm" property="status" value="DEMAND">
				<auth:access function="LABEL_VALIDATION">
					changeLibButton,
				</auth:access>
	        </logic:notEqual>
			cancelButton
		]
	});
	panel.render("labellisationPanel");
        var topdownpanel = new Ext.ux.CaqsTopDownSynthesisVolumetryPanel({
            renderTo: 'topdown',
            idBline:    '<%=eltBean.getBaseline().getId()%>',
            idPro:    '<%=eltBean.getProject().getId()%>',
            idElt:    '<%=eltBean.getId()%>',
            fromLabellisation: true
        });
        topdownpanel.reload();
};
</script>
<% } %>
</html>
