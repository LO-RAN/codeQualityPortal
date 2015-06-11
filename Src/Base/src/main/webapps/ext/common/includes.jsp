<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@page import="com.compuware.caqs.domain.dataschemas.ElementType" %>
<%@ page import="com.compuware.caqs.presentation.util.RequestUtil" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>

<script type="text/javascript" src="<%= request.getContextPath() %>/ext/plugins/staticfield/staticfield.js"></script>
<link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/ext/plugins/staticfield/staticfield.css"/>

<link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/css/customExt.css" />
<link rel="stylesheet" type="text/css" href="<%= request.getContextPath()%>/ext/plugins/fileupload/FileUpload.css" />

<script type="text/javascript" src="<%= request.getContextPath() %>/ext/plugins/notification/Ext.ux.Notification.js"></script>
<link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/ext/plugins/notification/notification.css"/>

<script type="text/javascript" src="<%= request.getContextPath() %>/ext/plugins/rowexpander/RowExpander.js">
</script>
<script src="<%= request.getContextPath() %>/ext/plugins/rowactions/Ext.ux.grid.RowActions.js" type="text/javascript">
</script>
<link href="<%= request.getContextPath() %>/ext/plugins/rowactions/Ext.ux.grid.RowActions.css" rel="stylesheet" type="text/css" />

<script src="<%= request.getContextPath() %>/ext/plugins/rowfitlayout/rowfitlayout.js" type="text/javascript">
</script>
<script src="<%= request.getContextPath() %>/ext/plugins/link/link.js" type="text/javascript">
</script>

<script src="<%= request.getContextPath() %>/ext/plugins/radiogroup/radiogroup.js" type="text/javascript">
</script>

<script src="<%= request.getContextPath() %>/ext/plugins/gridheaderfilter/gridheaderfilter.js" type="text/javascript">
</script>
<script src="<%= request.getContextPath() %>/ext/plugins/fileupload/FileUpload.js" type="text/javascript">
</script>

<script src="<%= request.getContextPath() %>/ext/plugins/maximizeTools.js" type="text/javascript">
</script>
<%--
<script src="<%= request.getContextPath() %>/ext/plugins/locationbar/Ext.ux.LocationBar.js" type="text/javascript">
</script>
<link href="<%= request.getContextPath() %>/ext/plugins/locationbar/LocationBar.css" rel="stylesheet" type="text/css" />
--%>
<script src="<%= request.getContextPath() %>/caqs.js" type="text/javascript">
</script>
<%-- le flot --%>
<%--
Attention : des surcharges en dur sont faites dans ce fichier. Les reporter si un changement de version est nécessaire
function drawSeriesBars(series) {
            function plotBars(datapoints, barLeft, barRight, offset, fillStyleCallback, axisx, axisy) {
                var points = datapoints.points, incr = datapoints.incr;

                for (var i = 0; i < points.length; i += incr) {
                    if (points[i] == null)
                        continue;
                    //daz : ajout
                    if(series.bars && series.bars.fillColorFunctionUsingDatas) {
                        series.color = series.bars.fillColorFunctionUsingDatas(points[i], points[i+1], ctx);
                    }
                    //daz : fin ajout
                    drawBar(points[i], points[i + 1], points[i + 2], barLeft, barRight, offset, fillStyleCallback, axisx, axisy, ctx, series.bars.horizontal);
                }
            }

            ctx.save();
            ctx.translate(plotOffset.left, plotOffset.top);

            // FIXME: figure out a way to add shadows (for instance along the right edge)
            ctx.lineWidth = series.bars.lineWidth;
            ctx.strokeStyle = series.color;
            var barLeft = series.bars.align == "left" ? 0 : -series.bars.barWidth/2;
            var fillStyleCallback = series.bars.fill ? function (bottom, top) { return getFillStyle(series.bars, series.color, bottom, top); } : null;
            plotBars(series.datapoints, barLeft, barLeft + series.bars.barWidth, 0, fillStyleCallback, series.xaxis, series.yaxis);
            ctx.restore();
        }

--%>
<!--[if IE]><script language="javascript" type="text/javascript" src="<%= request.getContextPath() %>/ext/plugins/extflot/excanvas.js"></script><![endif]-->
<script language="javascript" type="text/javascript" src="<%= request.getContextPath() %>/ext/plugins/extflot/jquery.flot.js"></script>
<link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/ext/plugins/extflot/Flot.css" />
<script language="javascript" type="text/javascript" src="<%= request.getContextPath() %>/ext/plugins/extflot/jquery.flot.stack.js"></script>
<script language="javascript" type="text/javascript" src="<%= request.getContextPath() %>/ext/plugins/extflot/jquery.flot.navigate.js"></script>
<script language="javascript" type="text/javascript" src="<%= request.getContextPath() %>/ext/plugins/extflot/jquery.flot.selection.js"></script>
<%--<script language="javascript" type="text/javascript" src="<%= request.getContextPath() %>/ext/plugins/extflot/jquery.flot.fillbetween.js"></script>--%>
<script language="javascript" type="text/javascript" src="<%= request.getContextPath() %>/ext/plugins/extflot/GetText.js">
</script>
<script language="javascript" type="text/javascript" src="<%= request.getContextPath() %>/ext/plugins/extflot/Flot.js">
</script>
<script language="javascript" type="text/javascript" src="<%= request.getContextPath() %>/ext/plugins/extflot/caqsPropertyGrid.js">
</script>

<%-- fin du flot --%>

<jsp:include page="/common/rightManagement.jsp" />

<style>
.x-grid3-header-offset {
    padding-left:1px;
    width: auto;
}

.x-date-middle {
    padding-top:2px;padding-bottom:2px;
    width:130px; /* FF3 */
}
</style>

<script language="javascript" type="text/javascript" src="<%= request.getContextPath() %>/ext/plugins/image/componentImage.js">
</script>

 <script type="text/javascript" src="<%= request.getContextPath() %>/ext/common/overrides.js">
</script>

<SCRIPT type="text/javascript" src="<%= request.getContextPath()%>/common/ajaxSearchByNamePanel.js">
</SCRIPT>


<script language="javascript">

logoutFunction = function() {
    location.href = 'j_spring_security_logout';
};

requestContextPath = '<%= request.getContextPath()%>';
// reference local blank image
Ext.BLANK_IMAGE_URL = requestContextPath+'/ext/resources/images/default/s.gif';

globalMaskOnBody = undefined;


var caqsOnReady = undefined;
var otherOnReadyFunctions = new Array();

function addOtherOnReadyFunctions(f) {
	otherOnReadyFunctions[otherOnReadyFunctions.length] = f;
}



Ext.onReady(function(){
    Ext.state.Manager.setProvider(new Ext.state.CookieProvider());

    globalMaskOnBody = new Ext.LoadMask(Ext.getBody());
    
    if(caqsOnReady != undefined) {
            caqsOnReady();
    }
    for(var i=0; i<otherOnReadyFunctions.length; i++) {
            otherOnReadyFunctions[i]();
    }
    putTooltips();
});

TYPE_PRJ = '<%= ElementType.PRJ%>';
TYPE_PRJ_LIB = "<%= new ElementType("PRJ").getLib(RequestUtil.getLocale(request))%>";
TYPE_DOMAIN = '<%= ElementType.DOMAIN%>';
TYPE_DOMAIN_LIB = '<%= new ElementType("DOMAIN").getLib(RequestUtil.getLocale(request))%>';
TYPE_EA = '<%= ElementType.EA%>';
TYPE_EA_LIB = '<%= new ElementType("EA").getLib(RequestUtil.getLocale(request))%>';
TYPE_SSP = '<%= ElementType.SSP%>';
TYPE_SSP_LIB = '<%= new ElementType("SSP").getLib(RequestUtil.getLocale(request))%>';
TYPE_ENTRYPOINT = 'ENTRYPOINT';

languageCode = '<%= RequestUtil.getLocale(request).getLanguage()%>';

javascriptDateFormatWithHour = "<bean:message key="caqs.dateFormat.withHour.js" />";
javascriptDateFormat = "<bean:message key="caqs.dateFormat.js" />";
javascriptDateFormatMonthYear = "<bean:message key="caqs.dateFormat.monthYear.js" />";

Ext.util.Format.dateAsLong = function(value, format) {
    var date = new Date(value);
    return Ext.util.Format.date(date, format);
};

//pour arrondir à 2 décimales, format = 100; pour 3 décimales, format = 1000
Ext.util.Format.roundToDecimals = function(value, format) {
    return Math.round(value*format)/format;
};

String.prototype.endsWith = function(str) {return (this.match(str+"$")==str)};
String.prototype.trim = function(){return (this.replace(/^[\s\xA0]+/, "").replace(/[\s\xA0]+$/, ""))};
String.prototype.endsWith = function(str) {return (this.match(str+"$")==str)};

//methode de tri pour la synthese du bottomup
Ext.data.SortTypes.bottomUpEltSort = function(val, cell, record) {
	var retour = String(record.data.eltLib.toLowerCase());
	return retour;
};

function PopupCentrer(page,largeur,hauteur,options) {
  var top=(screen.height-hauteur)/2;
  var left=(screen.width-largeur)/2;
  window.open(page,"","top="+top+",left="+left+",width="+largeur+",height="+hauteur+","+options);
}
</script>

<script type="text/javascript" src="<%= request.getContextPath() %>/ext/common/tooltipsMgmt.js" >
</script>
