<%@ page import="com.compuware.caqs.presentation.util.RequestUtil,
                java.util.Enumeration,
                java.util.Locale,
                java.util.ResourceBundle" %>
<SCRIPT language="javascript">
var resourcesJavascriptArray = new Array();
<%
ResourceBundle resourcesCaqs = RequestUtil.getCaqsResourceBundle(request);
if(resourcesCaqs != null) {
    Enumeration<String> key = resourcesCaqs.getKeys();
    if(key != null) {
        for(; key.hasMoreElements(); ) {
            String s = key.nextElement();
            String value = resourcesCaqs.getString(s);
            if(value != null) {
                 if (value.indexOf("\n\r") != -1) {
                    value = value.replaceAll("\\n\\r", "<BR/>");
                } else if (value.indexOf("\r\n") != -1) {
                    value = value.replaceAll("\\r\\n", "<BR/>");
                } else if (value.indexOf("\r") != -1) {
                    value = value.replaceAll("\\r", "<BR/>");
                } else if (value.indexOf("\n") != -1) {
                    value = value.replaceAll("\\n", "<BR/>");
                }
                value = value.replaceAll("\"", "&quot;");
            }
%>resourcesJavascriptArray['<%= s%>'] = "<%= value%>";<%
        }
    }
}
%>

//les arguments ne sont pas encore pris en compte
function getI18nResource(code, arg0, arg1, arg2, arg3, arg4) {
    var retour = "???"+code+"???";
    var tmp = resourcesJavascriptArray[code];
    if(tmp != undefined) {
        retour = tmp;
        if(arg0 != undefined) {
            retour = retour.replace('{0}', arg0);
        }
        if(arg1 != undefined) {
            retour = retour.replace('{1}', arg1);
        }
        if(arg2 != undefined) {
            retour = retour.replace('{2}', arg2);
        }
        if(arg3 != undefined) {
            retour = retour.replace('{3}', arg3);
        }
        if(arg4 != undefined) {
            retour = retour.replace('{4}', arg4);
        }
    }
    return retour;
}

function getI18nResourceOrNull(code, arg0, arg1, arg2, arg3, arg4) {
    var retour = resourcesJavascriptArray[code];
    if(retour != undefined) {
        if(arg0 != undefined) {
            retour = retour.replace('{0}', arg0);
        }
        if(arg1 != undefined) {
            retour = retour.replace('{1}', arg1);
        }
        if(arg2 != undefined) {
            retour = retour.replace('{2}', arg2);
        }
        if(arg3 != undefined) {
            retour = retour.replace('{3}', arg3);
        }
        if(arg4 != undefined) {
            retour = retour.replace('{4}', arg4);
        }
    }
    return retour;
}

</SCRIPT>