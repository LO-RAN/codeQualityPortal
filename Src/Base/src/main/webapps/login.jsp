<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>

<%@ taglib prefix='logic' uri='/WEB-INF/struts-logic.tld' %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>

<html>
<head>
    <meta http-equiv='Content-Type' content='text/html; charset=UTF-8' />
    <TITLE><bean:message key="caqsPortal.login.title" /></TITLE>
    <!-- LIBS -->
<script type="text/javascript" src="<%= request.getContextPath() %>/ext/adapter/ext/ext-base.js"></script>
<script type="text/javascript" src="<%= request.getContextPath() %>/ext/ext-all.js"></script>
<script type="text/javascript" charset="UTF-8" src="<%= request.getContextPath() %>/ext/locale/ext-lang-<%=request.getLocale().getLanguage()%>.js"></script>

<link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/ext/resources/css/ext-all.css" ></link>
<link id="extJSTheme" rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/ext/resources/css/xtheme-slate.css">
</link>
<link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/css/customExt.css" ></link>

<jsp:include page="/common/javascriptResources.jsp" />

<script language="javascript">

Ext.BLANK_IMAGE_URL = '<%= request.getContextPath()%>/ext/resources/images/default/s.gif';

function detectIFrame() {
    if (top.location != self.document.location) {
        top.location = self.document.location;
    }
}

</script>

</head>

<body style="height:100%" onLoad="detectIFrame();">
    <div style="width:100%; height:15%">
        <img src="images/CAQS_logo_without_reverb.gif" />
        <span style="color: red;">
<logic:equal parameter="login_error" value="1">
    <bean:message key="caqsPortal.login.error" /><c:out value="${SPRING_SECURITY_LAST_EXCEPTION.message}"/>
</logic:equal>
        </span>
    </div>
    <div  align="center" style="width:100%;">
        <div id="loginPanel" ></div>
    </div>
    <div style="height:303px; width:100%;background-image:url('images/login_bck_bottom.gif'); background-repeat:repeat-x">
        <div style="float:right; margin-right: 10px; margin-top:200px;" ><a href="http://www.compuware.com" target="compuware"><img src="images/login_bck_bottom_right.png" /></a></div>
    </div>
    <div align="center" class="normalText" style="width:100%;">
        <bean:message key="caqsPortal.login.about"/>
    </div>
<script language="javascript">

var mainform = undefined;

function submitForm(val) {
	mainform.getForm().getEl().dom.action='j_spring_security_check';
	mainform.getForm().getEl().dom.submit();
}

Ext.onReady(function(){
    Ext.QuickTips.init();
    // turn on validation errors beside the field globally
    Ext.form.Field.prototype.msgTarget = 'side';

    var connectButton = new Ext.Button({
        handler:    submitForm,
        icon:       '<%= request.getContextPath()%>/images/connect.gif',
        formBind:   true,
        cls:        'x-btn-text-icon',
        text:       getI18nResource('caqs.login.connect')
    })

    mainform = new Ext.form.FormPanel({
        method:             "POST",
        frame:              true,
        labelWidth:         140,
        bodyStyle:          'padding:5px 5px 0',
        width:              500,
        style:              'margin-bottom: 10px; text-align: left;',
        monitorValid:       true,
        title:              getI18nResource('caqsPortal.login.title'),
        buttonAlign:        'left',
        keys:               new Ext.KeyMap(document, {
                                key:        Ext.EventObject.ENTER,
                                fn:         submitForm,
                                scope:      this
                            }),
        submit:             function() {
            this.getForm().getEl().dom.submit();
        },
        formId:             'loginForm',
        items: [
        	{
                xtype:		'textfield',
                fieldLabel: getI18nResource('caqsPortal.login.id'),
                anchor:     '95%',
                name:       'j_username',
                id:         'j_username',
                validateOnBlur:true,
                allowBlank:	false
            }
        	, {
                xtype:		'textfield',
                fieldLabel: getI18nResource('caqsPortal.login.password'),
                inputType:  'password',
                anchor:     '95%',
                name:       'j_password',
                id:         'j_password',
                validateOnBlur:true,
                allowBlank:	false
            }
		],//fin definition des panels

        buttons: [
        	connectButton
        ]//end button
    });

    mainform.on('render', function() {
        var field = mainform.findById('j_username');
        if(field!=undefined) {
            field.focus(false, 500);
        }
    }, this);
    mainform.render('loginPanel');
});
</script>
</body>
</html>

