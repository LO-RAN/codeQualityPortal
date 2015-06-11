<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@ page errorPage="errorPage.jsp" %>
<%@ taglib uri="/struts-html.tld" prefix="html" %>
<%@ taglib uri="/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/struts-bean.tld" prefix="bean" %>
<%@ page language="java" import="com.compuware.caqs.domain.dataschemas.ElementType,
                                 com.compuware.caqs.presentation.admin.actions.StereotypeMgmtElementRetrieveAction,
                                 com.compuware.caqs.presentation.admin.actions.StereotypeMgmtElementUpdateAction" %>

<link href="css/carscode.css" rel="stylesheet" type="text/css" />

<jsp:useBean id="currentAdminEa" scope="session" class="com.compuware.caqs.domain.dataschemas.ElementBean" />

<html>

<head>
    <meta http-equiv='Content-Type' content='text/html; charset=UTF-8' />
<meta http-equiv="pragma" content="no-cache">

<title><bean:message key="caqs.stereotypemgmt.title"/></title>

</head>

<body>
    <H2><bean:message key="caqs.stereotypemgmt.ea" arg0="<%= currentAdminEa.getLib() %>" /></H2>

    <html:form action="/StereotypeMgmtElementRetrieve.do" method="post">
        <H3>
            <bean:message key="caqs.stereotypemgmt.search" />
        </H3>
        <TABLE>
            <TR>
                <TD><bean:message key="caqs.stereotypemgmt.filterDesc" /></TD>
                <TD class="filterSearch"><html:text name="elementRetrieveFilterForm" property="descElt" /></TD>
                <TD class="filterSearch">
                    <bean:message key="caqs.stereotypemgmt.filterStereotype" />
                </TD>
                <TD class="filterSearch">
                    <html:select name="elementRetrieveFilterForm" property="idStereotype">
                        <option value=''></option>
                        <html:options collection="<%=StereotypeMgmtElementRetrieveAction.STEREOTYPE_RETRIEVED_COLLECTION%>" property="id" labelProperty="lib" />
                    </html:select>
                </TD>
            </TR>
            <TR>
                <TD>
                    <bean:message key="caqs.stereotypemgmt.filterTypeElt" />
                </TD>
                <TD class="filterSearch">
                    <html:select name="elementRetrieveFilterForm" property="typeElt">
                        <html:option value="<%= ElementType.ALL %>"><bean:message key="caqs.select.all" /></html:option>
                        <html:option value="<%= ElementType.CLS %>"><bean:message key='caqs.obj.class' /></html:option>
                        <html:option value="<%= ElementType.MET %>"><bean:message key='caqs.obj.method' /></html:option>
                    </html:select>
                </TD>
                <TD class="filterSearch">
                  <bean:message key="caqs.stereotypemgmt.filterEmptyOnly" />
                </TD>
                <TD class="filterSearch">
                  <html:checkbox name="elementRetrieveFilterForm" property="emptyOnly" />
                </TD>
            </TR>
            <TR>
                <TD>&nbsp;</TD>
                <TD class="filterSearch">&nbsp;</TD>
                <TD class="filterSearch">&nbsp;</TD>
                <TD class="filterSearch">
                    <html:submit property="action"><bean:message key="caqs.search.rechercher" /></html:submit>
                </TD>
            </TR>
        </TABLE>
    </html:form>

<logic:present name="<%=StereotypeMgmtElementRetrieveAction.ELEMENT_RETRIEVED_COLLECTION%>">
    <form action="StereotypeMgmtElementUpdate.do" method="post">
        <H3>
            <bean:message key="caqs.stereotypemgmt.liste" />
        </H3>
        <TABLE>
            <TR>
                <TD>
                    <select class="multiselect"  name="<%=StereotypeMgmtElementUpdateAction.SELECTED_ELEMENTS_KEY%>" size="10" multiple>
                        <logic:iterate id="elt" name="<%=StereotypeMgmtElementRetrieveAction.ELEMENT_RETRIEVED_COLLECTION%>" type="com.compuware.caqs.domain.dataschemas.ElementBean">
                            <option value='<bean:write name="elt" property="id" filter="true"/>'><bean:write name="elt" property="desc" filter="true"/></option>
                        </logic:iterate>
                    </select>
                </TD>
                <TD>
                    <select name="<%=StereotypeMgmtElementUpdateAction.SELECTED_STEREOTYPE_KEY%>">
                        <option value=''></option>
                        <logic:iterate id="stereotype" name="<%=StereotypeMgmtElementRetrieveAction.STEREOTYPE_RETRIEVED_COLLECTION%>" type="com.compuware.caqs.domain.dataschemas.StereotypeBean">
                            <option value='<bean:write name="stereotype" property="id" filter="true"/>'><bean:write name="stereotype" property="lib" filter="true"/></option>
                        </logic:iterate>
                    </select>
                    <A href="StereotypeNew.do"><IMG src='images/addico.gif' title="<bean:message key='caqs.stereotype.new' />" border='0'></A>
                    <BR/>
                    <html:submit property="actionMaj"><bean:message key="caqs.update" /></html:submit>
                </TD>
            </TR>
        </TABLE>
    </form>
</logic:present>
</body>

</html>
