
var isDateEqual = function(d1, d2) {
    var retour = false;
    if(d1.getDayOfYear()==d2.getDayOfYear() && d1.getMonth()==d2.getMonth()
       && d1.getYear()== d2.getYear()) {
       //aujourd'hui
       retour = true;
    }
    return retour;
};

var isDateInSameWeek = function(d1, d2) {
    var retour = false;
    var d1NumberInWeek = d1.getDay();
    var d1WeekBeginning = d1.add(Date.DAY, -1*d1NumberInWeek);
    var d1WeekEnding = d1.add(Date.DAY, (7-d1NumberInWeek));
    if(d2.between(d1WeekBeginning, d1WeekEnding)) {
       //aujourd'hui
       retour = true;
    }
    return retour;
};

// Array.insert( index, value ) - Insert value at index, without overwriting existing keys
Array.prototype.insert = function( i, v ) {
 if( i>=0 ) {
  var a = this.slice(), b = a.splice( i );
  a[i] = v;
  return a.concat( b );
 }
};

/**********************************************************/
/**    Manipulation des fen�tres (window,popup...)       **/
/**********************************************************/
function maximiseWindow(objWin){
	var bord = 2;	//retire les bord du navigateur
	objWin.moveTo(-bord, -bord);
	objWin.resizeTo(objWin.screen.availWidth+2*bord, objWin.screen.availHeight+2*bord)
}
function openPopupFull(url){
	var w = window.screen.availWidth;
	var h = window.screen.availHeight;
	var win = open (url, 'faqts','resizable=0,status=0,menubar=0,toolbar=0,location=0,scrollbars=1');
	maximiseWindow(win);
}
function openPopup(page,nom,largeur,hauteur,option){
        var optionPopup=" ,resizable=no,menubar=no,status=yes,scrollbars=yes";
        var bigOption=option+optionPopup;
        PopupCentrer(page,nom,largeur,hauteur,bigOption);
}

function openPopupModalDialog(page,arg,largeur,hauteur,option){
        var optionPopup = "dialogWidth:'+largeur+'px;dialogHeight:'+hauteur+'px;center:yes;";
        var bigOption = optionPopup + option;
        return window.showModalDialog(page, arg, 'dialogWidth:'+largeur+'px;dialogHeight:'+hauteur+'px;'); 
        //return window.showModelessDialog(page, window, 'dialogWidth:'+largeur+'px;dialogHeight:'+hauteur+'px;');
}

//fction a virer apres remplacement par dans les pages par 'openPopup(monURL, "popup",450,80)'
function ouvreFenetre(monURL) {
	window.open(monURL, "popup", "width=450px;height=80px;");
}
/*********************************************************/



/*********************************************************/
/**          Manipulation DOM             **/
/*********************************************************/
function createElement(element) {
  if (typeof document.createElementNS != 'undefined') {
    return document.createElementNS('http://www.w3.org/1999/xhtml', element);
  }
  if (typeof document.createElement != 'undefined') {
    return document.createElement(element);
  }
  return false;	
}

// modifie la classe d'un element.
function setClassToElement(obj,className){
	/* pour insertion dans l'arbre DOM
	if(document.all)
		obj.setAttribute('className',className);	// IE
	else
		obj.setAttribute('class',className);*/
	obj.className = className;
}

// ajoute un eveenement sans ecraser l'existant.
// param obj => element sur lequel appliquer l'evenement
// param eventType => 'click', 'dblclick', 'mouseover', 'mouseout', 'focus' ......
// param fn => script ? executer
function addEventToElement(obj,eventType,fn){  
	if(obj.addEventListener)
		obj.addEventListener(eventType,fn,false); // NS6+ 
	else if(obj.attachEvent)
		obj.attachEvent("on"+eventType,fn); // IE 5+ 
	else
		return false;
        return true;
} 
/*********************************************************/



/*********************************************************/
/**    Classe d'aide pour le tri des tableaux           **/
/*********************************************************/
function triClass(champCol,sensTri,obj) {
    this.champ = champCol;
    this.sens = sensTri;    /*sens=false : ascendant; sens=true : descendant*/
    this.objImg = obj;
    function changeSens(){
        if(this.sens) {
            this.resetSens(false);
        }
        else {
            this.sens = true;
            this.objImg.src = 'images/desc.png';
        }
    }
    this.changeSens = changeSens;
    function resetSens(testInactif){
        this.sens = false;
        var suffix = (testInactif)?'Gris':'';
        this.objImg.src = 'images/asc' + suffix + '.png';
    }
    this.resetSens = resetSens;
    function changeTri(champCol, objImgNew) {
        if(this.champ == champCol) {
            this.objImg = objImgNew;
            this.changeSens();
        }
        else {
            this.resetSens(true);
            this.champ = champCol;
            this.objImg = objImgNew;
            this.resetSens(false);
        }
    }
    this.changeTri = changeTri;
}
/*********************************************************/



/*********************************************************/
/**              Autres fonctions utiles                **/
/*********************************************************/
function addVarQueryString(url, nomVar, valueVar) {
    url += (url.indexOf('?') == -1) ? '?' : '&';
    url += nomVar + '=' + valueVar;
    return url;
}

String.prototype.trim = function() { return this.replace(/^\s+|\s+$/, ''); }; 

function getRadioValueChecked(elem){
    returnResult = '';
    for(i=0;i<elem.length;i++){
        if(elem[i].checked)
            returnResult = elem[i].value;
    }
    return returnResult;
}

function returnIfNotNull(elem,replaceBy){
    if(!replaceBy)replaceBy='';
    return (elem!=null) ? elem : replaceBy;
}

function getValueOfDropDown(obj) {
    if(obj.selectedIndex!=-1) {
      return obj.options[obj.selectedIndex].value;
    }
    return '';
}

/************* FONCTIONS GENERIQUES **********/
    function changeSelectedIndexWith(elem, val) {
        for(i=0; i < elem.options.length; i++){
            if( elem.options[i].value == val ){
                elem.selectedIndex = i;
                break;
            }
        }
    }
    function setValueForRadio(elem, val){
        for(i=0; i < elem.length; i++){
            if( elem[i].value == val )
                elem[i].checked = true;
            else
                elem[i].checked = false;
        }
    }

/************* FIN FONCTIONS GENERIQUES **********/

//*****************************************************************************************************************************
//****************************************** Contr�le des touches de fonctions et du click droit ******************************
//*****************************************************************************************************************************
//document.onkeydown = gererOnKeyDown;

function clickIE4(){
    if (event.button==2){
            return false;
    }
}
function clickNS4(e){
    if (document.layers||document.getElementById&&!document.all){
        if (e.which==2||e.which==3){
            return false;
        }
    }
}

if (document.layers){
    document.captureEvents(Event.MOUSEDOWN);
    document.onmousedown=clickNS4;
}
else if (document.all&&!document.getElementById){
    document.onmousedown=clickIE4;
}

keys = new Array();
keys["f112"] = 'f1';
keys["f113"] = 'f2';
keys["f114"] = 'f3';
keys["f115"] = 'f4';
keys["f116"] = 'f5';
keys["f117"] = 'f6';
keys["f118"] = 'f7';
keys["f119"] = 'f8';
keys["f120"] = 'f9';
keys["f121"] = 'f10';
keys["f122"] = 'f11';
keys["f123"] = 'f12';
//keys["f8"] 	 = 'retour';

saveCode="";

function gererOnKeyDown(){
    // Capture and remap F-key
    if(window.event && keys["f"+window.event.keyCode])  
    {
        saveCode=window.event.keyCode;
        window.event.keyCode = 505;		   
    }

    //Touche Ctrl enfonc�e

    if(event.ctrlKey){ 

        //touche n ou N enfonc�e

        if((event.keyCode == 78) || (event.keyCode == 104))
        {
            saveCode=window.event.keyCode;
            window.event.keyCode = 505;	
        }
    }

    //Touche ALT enfonc�e

    if(event.altKey) 
    {	
        //touche fleche gauche ou fleche droite enfonc�e (page suivante/precedente)

        if((event.keyCode == 37) || (event.keyCode == 39)){ 
            saveCode=window.event.keyCode;
            window.event.keyCode = 505;
        }
    }

    //Touche Del (retour page pr�c�dente)

    if(event.keyCode == 8) 
    {
        if( !(event.srcElement.type=="textarea" || event.srcElement.type=="text") ){
            saveCode=window.event.keyCode;
            window.event.keyCode = 505;
        }
    } 

    //Touche Entr�e

    /*if(event.keyCode == 13) 
    {
        if(!event.srcElement.type=="textarea"){
            saveCode=window.event.keyCode;
            window.event.keyCode = 505;
        }
    }*/

    //Annulation ou traitement particulier

    if(window.event && window.event.keyCode == 505) {
        //New action for keycode

        myFunc(saveCode);

 // Must return false or the browser will execute old code

        return false;
    }
}
function myFunc(code) {
    alert('Cette manip est interdite...!');
}
//*****************************************************************************************************************************
//****************************************** FIN Contr�le des touches de fonctions et du click droit **************************
//*****************************************************************************************************************************

String.prototype.startsWith = function(str)
{return (this.match("^"+str)==str)}
String.prototype.endsWith = function(str)
{return (this.match(str+"$")==str)}
String.prototype.trim = function(){return
(this.replace(/^[\s\xA0]+/, "").replace(/[\s\xA0]+$/, ""))}