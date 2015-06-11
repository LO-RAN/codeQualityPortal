//il est important que cela soit fait avant le onReady car on peut placer des tooltips dans le code du body
//et si on initialise les quicktips après, ça plante.
Ext.QuickTips.init();
/*Ext.apply(Ext.QuickTips.getQuickTip(), {
    trackMouse: true
});*/

var tooltipsTitles = new Array();
var tooltipsText = new Array();
var tooltipsIds = new Array();
var tooltipsDismissDelay = new Array();
var tooltipWidth = new Array();
var tooltipMaxWidth = new Array();

function addTooltip(id, title, text, dismissDelay, width, maxWidth) {
    var l = tooltipsIds.length;
    tooltipsIds[l] = id;
    tooltipsText[l] = text;
    tooltipsTitles[l] = title;
    tooltipsDismissDelay[l] = (dismissDelay!=undefined)?dismissDelay:5000;
    tooltipWidth[l] = (width!=undefined)?width:'auto';
    tooltipMaxWidth[l] = (maxWidth!=undefined)?maxWidth:300;
}

function resetTooltipsArrays() {
    tooltipsTitles = new Array();
    tooltipsText = new Array();
    tooltipsIds = new Array();
    tooltipsDismissDelay = new Array();
    tooltipWidth = new Array();
    tooltipMaxWidth = new Array();
}

var putTooltips = function() {
    for(var i=0; i<tooltipsIds.length; i++) {
        if(tooltipWidth[i]!=='auto') {
            Ext.QuickTips.register({
                target: 	tooltipsIds[i],
                title:		tooltipsTitles[i],
                dismissDelay:	tooltipsDismissDelay[i],
                width:		tooltipWidth[i],
                maxForcedWidth:	tooltipMaxWidth[i],
                text:   	tooltipsText[i]
            });
        } else {
            Ext.QuickTips.register({
                target: 	tooltipsIds[i],
                title:		tooltipsTitles[i],
                dismissDelay:	tooltipsDismissDelay[i],
                autoWidth:	true,
                maxForcedWidth:	tooltipMaxWidth[i],
                text:   	tooltipsText[i]
            });
        }
    }
    resetTooltipsArrays();
};
