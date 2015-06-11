<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@ page import="com.compuware.caqs.domain.dataschemas.JustificationBean" %>
<%@ page import="com.compuware.caqs.domain.dataschemas.CriterionDefinition" %>
<%@ page import="com.compuware.caqs.domain.dataschemas.FactorDefinitionBean" %>
<%@ page import="com.compuware.toolbox.util.resources.Internationalizable" %>
<%@ page import="com.compuware.caqs.security.auth.Users" %>
<%@ page import="com.compuware.caqs.presentation.util.RequestUtil" %>
<%@ page import="java.util.*" %>
<%@ page import="java.text.NumberFormat" %>
<%@ page errorPage="errorPage.jsp" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/auth.tld" prefix="auth" %>
<html>
<head>
    <meta http-equiv='Content-Type' content='text/html; charset=UTF-8' />
<meta http-equiv="pragma" content="no-cache">
<title><bean:message key="caqs.justifValid.title" /></title>
<jsp:include page="/ext/common/extjsFramework.jsp" />
<jsp:include page="/common/javascriptResources.jsp" />
<jsp:include page="/ext/common/includes.jsp" />


<SCRIPT language='Javascript'>
    function validNote(doNotShowAlert) {
    	var input = document.getElementById('note_just');
        if (input!=undefined && ((input.value < 1) || (input.value > 4))) {
            validButton.disable();
            return false;
        }
        validButton.enable();
        return true;
    }

    function validFields() {
        if(!validNote(true)) {
        	return false;
        }
        if (document.forms[0].just_desc_valid.value.length > 128){
            alert("<bean:message key="caqs.justifValid.erreur02" />");
            return false;
        }
        return true;
    }

    function validCancel() {
    	return confirm('<bean:message key="caqs.justifValid.confirmCancel" />');
    }
</SCRIPT>
</head>

<jsp:useBean id="user" scope="session" class="com.compuware.caqs.security.auth.Users" />
<%
    if (session == null || user == null || user.getId() == null || user.getId().equals("")) { %>
		<jsp:forward page="sessiontimedout.jsp"/>
<%  }
    else {
    Locale locale = RequestUtil.getLocale(request);
    String req = request.getParameter("req");
    String id_elt = request.getParameter("id_elt");
    String dbtable = request.getParameter("dbtable");
    String lib_elt = request.getParameter("lib_elt");
    String id_just = request.getParameter("id_just");
    String id_critfac = request.getParameter("id_critfac");
    String libPro = (String) request.getAttribute("libPro");
	Internationalizable critFact = null;
	if (dbtable.equals("FACT")) {
		critFact = new FactorDefinitionBean();
		((FactorDefinitionBean)critFact).setId(id_critfac);
	}
	else {
		critFact = new CriterionDefinition();
		((CriterionDefinition)critFact).setId(id_critfac);
	}
    String lib_critfac = critFact.getLib(locale);
    String id_bline = request.getParameter("id_bline");
    double[] note = (double[])request.getAttribute("justifiedNoteArray");
    JustificationBean just = (JustificationBean)request.getAttribute("justificatif");
    JustificationBean just_old = just.getLinkedJustificatif();
    NumberFormat nf = NumberFormat.getInstance(locale);
    nf.setMaximumFractionDigits(2);
    nf.setMinimumFractionDigits(0);
    NumberFormat nfEng = NumberFormat.getInstance(Locale.ENGLISH);
    nfEng.setMaximumFractionDigits(2);
    nfEng.setMinimumFractionDigits(0);
    ResourceBundle resources =  ResourceBundle.getBundle("com.compuware.caqs.Resources.resources", locale);

    String libEA = (String)request.getAttribute("libEA");
%>
<BODY>
</body>
<script language="javascript">
	function submitForm(action) {
		panel.getForm().getEl().dom.action='<%=request.getContextPath()%>/Justification.do?action='+action;
		panel.getForm().getEl().dom.submit();
	}

caqsOnReady = function(){
	var cancelFunction = function() {
		submitForm('cancel');
	};

	var cancelButton = new Ext.Button({
<% if (just.getStatut().equals("DEMAND")) { %>
		text:		'<bean:message key="caqs.annuler" />',
<% } else { %>
		text:		'<bean:message key="caqs.finir" />',
<% } %>
		name:		'cancel',
		style:		'margin-left: 5px;',
		handler: 	cancelFunction
	});


	var noteJust = new Ext.form.NumberField({
		name:			'note_just',
		id:				'note_just',
		formBind:		true,
		minValue:		1,
		maxValue:		4,
<% if (!"DEMAND".equals(just.getStatut())) { %>
		disabled:		true,
<% } %>
<auth:notAccess function="JUSTIFICATION_VALIDATION">
		disabled:		true,
</auth:notAccess>
		value:			"<%= nfEng.format(just.getNote()) %>"
		});
<% if (just.getStatut().equals("DEMAND")) { %>
<auth:access function="JUSTIFICATION_VALIDATION">
	noteJust.on('blur', function() {validNote('note_just');});
</auth:access>
<% } %>
	
	
	var validateFunction = function() {
		if(validFields()) {
			submitForm('valid');
		}
	};

	validButton = new Ext.Button({
		text:		'<bean:message key="caqs.valider" />',
		name:		'valid',
		style:		'margin-left: 5px;',
		handler: 	validateFunction
	});

	var rejectFunction = function() {
		if(validFields()) {
			submitForm('rejet');
		}
	};

	var rejectButton = new Ext.Button({
		text:		'<bean:message key="caqs.refuser" />',
		name:		'rejet',
		style:		'margin-left: 5px;',
		handler: 	rejectFunction
	});

	var cancelJustifFunction = function() {
		panel.getForm().findField("<%= org.apache.struts.taglib.html.Constants.CANCEL_PROPERTY%>").setValue("lib");
		if(validCancel()) {
			submitForm('cancel_justif');
		}
	};

	var cancelJustifButton = new Ext.Button({
		text:		'<bean:message key="caqs.justifValid.cancelJustif" />',
		name:		'cancel_justif',
		style:		'margin-left: 5px;',
		handler: 	cancelJustifFunction
	});

	panel = new Ext.form.FormPanel({
		title:				'<bean:message key="caqs.justifValid.justificatif" />',
		//width:				900,
		autoHeight:			true,
		monitorValid:		true,
		labelWidth:			150,
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
				name:       'cuser',
				id:         'cuser',
				value:      '<%=user.getId()%>',
				xtype:      'hidden'
			},{
				name:       'id_elt',
				id:         'id_elt',
				value:      '<%=id_elt%>',
				xtype:      'hidden'
			},{
				name:       'id_just',
				id:         'id_just',
				value:      '<%=id_just%>',
				xtype:      'hidden'
			},{
				name:       'just_lib',
				id:         'just_lib',
<%
String justLib = just.getEscapedLib(true, false, false);
%>
				value:      '<%=justLib%>',
				xtype:      'hidden'
			},{
				name:       'id_critfac',
				id:         'id_critfac',
				value:      '<%=id_critfac%>',
				xtype:      'hidden'
			},{
				name:       'id_bline',
				id:         'id_bline',
				value:      '<%=id_bline%>',
				xtype:      'hidden'
			},{
				name:       'dbtable',
				id:         'dbtable',
				value:      '<%=dbtable%>',
				xtype:      'hidden'
			},{
				name:       'from',
				id:         'from',
				value:      'justification',
				xtype:      'hidden'
			},{
				name:       'req',
				id:         'req',
				value:      '<%=req%>',
				xtype:      'hidden'
			},
			{
				style:		'margin-left: 5px; margin-top: 5px;',
				border:		false,
				layout:		'form',
				items: [
					{
						xtype:			'statictextfield',
						parentClassName: 	'smallStaticField',
						fieldLabel:		getI18nResource('caqs.projet'),
						value:			'<%= libPro%>'
					},
<%
if(libEA != null) {
%>
                    {
						xtype:			'statictextfield',
						parentClassName: 	'smallStaticField',
						fieldLabel:		getI18nResource('caqs.EA'),
						value:			'<%= libEA%>'
					},
<%
}
%>
                    {
						xtype:			'statictextfield',
						parentClassName: 	'smallStaticField',
						fieldLabel:		'<bean:message key="caqs.justifValid.portee" />',
<%
if(lib_elt!=null) {			
	lib_elt = lib_elt.replaceAll("'","\\\\'");
}
%>
						value:			'<%=lib_elt%>'
					},
					{
						xtype:			'statictextfield',
						parentClassName: 	'smallStaticField',
						fieldLabel:		'<%= dbtable.equals("FACT") ? resources.getString("caqs.objectif") : resources.getString("caqs.critere") %>',
<%
if(lib_critfac!=null) {			
	lib_critfac = lib_critfac.replaceAll("'","\\\\'");
}
%>		    			value:			'<%=lib_critfac%>'
					}
				]
			}, {
				layout:			'table',
				layoutConfig:	{
					columns:	2
				},
				//width:			900,
				border:			false,
				style:			'margin-bottom: 5px;',
				items: [
        			new Ext.form.FieldSet({
						title:			'<bean:message key="caqs.justifValid.demande" />',
						height:			220,
						style:			'margin-left: 5px;',
						labelWidth:		130,
						width:			440,
						items:[
							{
								xtype:				'statictextfield',
								parentClassName: 	'smallStaticField',
								smallLine:		true,
								fieldLabel:			"<bean:message key="caqs.justifValid.redacteur" />",
<%
String justUser = (just_old!=null)?just_old.getEscapedUser(true, false, false):just.getEscapedUser(true, false, false);
%>								value:				'<%=justUser%>'							
							}
							,{
								xtype:				'statictextfield',
								parentClassName: 	'smallStaticField',
								smallLine:		true,
								fieldLabel:			'<bean:message key="caqs.justifValid.dateCreation" />',
						        <% if (just_old != null) { %>
						        value:				'<%=java.text.DateFormat.getDateInstance(java.text.DateFormat.DEFAULT, locale).format(just_old.getDinst())%>'
						        <% } else { %>
						        value:				'<%=java.text.DateFormat.getDateInstance(java.text.DateFormat.DEFAULT, locale).format(just.getDinst())%>'
						        <% } %>
							}
							,{
								xtype:				'statictextfield',
								parentClassName: 	'smallStaticField',
								smallLine:		true,
								fieldLabel:			"<bean:message key="caqs.justifValid.libelle" />",
<%
String justOldLib = (just_old!=null)?just_old.getEscapedLib(true, false, false):just.getEscapedLib(true, false, false);
%>								value:				'<%=justOldLib%>'
							}, {
								xtype:				'textarea',
								fieldLabel:			"<bean:message key="caqs.justifValid.commentaire" />",
<%
String justOldDesc = (just_old!=null)?just_old.getEscapedDesc(true, false, false):(just != null)?just.getEscapedDesc(true, false, false):"";
%>
								value:				'<%=justOldDesc%>',
								disabled:			true,
								width:				270,
								height:				70
							}
						]
					})
					, new Ext.form.FieldSet({
						title:			'<bean:message key="caqs.justifValid.justification" />',
						height:			220,
						style:			'margin-left: 5px;',
						labelWidth:		130,
						width:			440,
						items:[
							{
								xtype:			'statictextfield',
								smallLine:		true,
								parentClassName: 	'smallStaticField',
								fieldLabel:		"<bean:message key="caqs.justifValid.qualiticien" />",
<%
String justUserId = (just_old!=null)?just.getEscapedUser(true, false, false):user.getId();
%>								value:			'<%=justUserId%>'							
							
							}
							,{
								xtype:			'statictextfield',
								smallLine:		true,
								parentClassName: 	'smallStaticField',
								fieldLabel:		'<bean:message key="caqs.justifValid.dateModif" />',
						        <% if (just_old != null) { %>
						        value:			'<%=java.text.DateFormat.getDateInstance(java.text.DateFormat.DEFAULT, locale).format(just.getDinst())%>'
						        <% } else { %>
						        value:			'<%=java.text.DateFormat.getDateInstance(java.text.DateFormat.DEFAULT, locale).format(new Date())%>'
						        <% } %>
							}
							,{
								xtype:			'statictextfield',
								smallLine:		true,
								parentClassName: 	'smallStaticField',
								fieldLabel:		"<bean:message key="caqs.justifValid.libelle" />",
								value:			'<%= justLib%>'
							}, {
								xtype:				'textarea',
								id:					'just_desc_valid',
								name:				'just_desc_valid',
								maxLength:			128,
								maxLengthText:		'<bean:message key="caqs.maxlength.text" /> {0}',
								fieldLabel:			"<bean:message key="caqs.justifValid.commentaire" />",
<%
String justDesc = (just!=null)?just.getEscapedDesc(true, false, false):"";
%>								value:				'<%=justDesc%>',
						        <% if (!"DEMAND".equals(just.getStatut())) { %>
								disabled:			true,
						        <% } %>
								width:				270,
								height:				70
							},{
								xtype:			'statictextfield',
								smallLine:		true,
								parentClassName: 	'smallStaticField',
								fieldLabel:		"<bean:message key="caqs.justifValid.notecalculee" />",
								value:			"<%= nf.format(note[0]) %>"
							},{
								layout:			'table',
								border:			false,
								autoHeight:		true,
								layoutConfig:	{
<%
 int columns = 2;
 if(just.getStatut().equals("DEMAND")) {
	 if(user.access("JUSTIFICATION_VALIDATION")) {
		 columns += 2;
	 }
 } else {
	 if(user.access("CANCEL_JUSTIF")) {
		 columns += 1;
	 }
 }
%>
									columns:	<%= columns%>
								},
								items: [
									{
										border:		false,
										autoHeight:	true,
										baseCls:	'x-form-item',
										html: 		'<label class="x-form-item-label" ><bean:message key="caqs.justifValid.noteattribuee" />: </label>'
									}
									,noteJust
            <% if (just.getStatut().equals("DEMAND")) { %>
<auth:access function="JUSTIFICATION_VALIDATION">
									,validButton
									,rejectButton
</auth:access>
            <% } else { %>
<auth:access function="CANCEL_JUSTIF">
									,cancelJustifButton
</auth:access>
            <% } %>
								]			
							}
						]
					})
				]
			
			}
		],
		buttons: [
			cancelButton
		]
	});
	//panel.render("divJustificationValid");

        new Ext.Viewport({
        autoScroll: true,
        layout: 'fit',
        items: panel
        })
};
</script>
<% } %>
</html>
