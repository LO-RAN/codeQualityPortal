<html>
<!-- Date de création: 05/10/2002 -->
<head>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<meta http-equiv="pragma" content="no-cache">
<title>Add Project</title>
 <script src="js/syntaxe.js"></script>
<script language="javascript">
<!--
function isDigit (c)
{   return ((c >= "0") && (c <= "9"))
}

function checkModification()
{
	if (document.ADDPRJ.NAME.value != document.ADDPRJ.OLDNAME.value)
	{
		document.ADDPRJ.MAJ.disabled=false;
	}
	if (document.ADDPRJ.DESC.value != document.ADDPRJ.OLDDESC.value)
	{
		document.ADDPRJ.MAJ.disabled=false;
	}
	if (document.ADDPRJ.STREAM.value != document.ADDPRJ.OLDSTREAM.value)
	{
		document.ADDPRJ.MAJ.disabled=false;
	}
}


function champsok()
{
if (document.ADDPRJ.ACTION.value != 'CANCELPRJ')
	{
		var prjName = document.ADDPRJ.NAME.value;
		prjName = prjName.trim();
		if (prjName.length == 0)
		{
			alert("Veuillez donner un nom à l'élément.");
			return false;
		}
		if(document.ADDPRJ.USAGE.value == "Aucun")
		{
			alert("Veuillez choisir un usage.");
			return false;
		}
		if(!(isDigit(document.ADDPRJ.POIDS.value)))
		{
			alert("Veuillez donner saisir un nombre pour le poids.");
			return false;
		}
		
	if (!verifSyntaxe(document.ADDPRJ.NAME)==true){
			return false;
		}
	}
	document.ADDPRJ.NAME.value = document.ADDPRJ.NAME.value.toUpperCase();
	return true;
}

function valued(newValue)
{
	document.ADDPRJ.ACTION.value = newValue;
	return true;
}
//-->
</script>
<style>
   BODY {background-color: transparent; font-size: 10pt; font-family: verdana,helvetica}
   TABLE {font-size: 10pt; font-family: verdana,helvetica}
</style>
</head>
<jsp:useBean id="element" scope="session" class="com.compuware.caqs.quality.Elements" />

<%@ page language="java" import="java.util.Vector" %>
<%@ page import="java.util.Enumeration" %>
<%@ page import="java.util.Date" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%
SimpleDateFormat formatter = new SimpleDateFormat ("dd-MM-yyyy");
Date dateInstd = new Date();
Date dateMajd = new Date();
String dateInst = formatter.format(dateInstd);
String dateMaj = formatter.format(dateMajd);
%>

<body>
<H2> Création d'un Projet </H2>
<form name="ADDPRJ" action="<%=request.getContextPath()%>/servlet/UpdateElt" method="post" OnSubmit="return champsok()">
<h6><font COLOR="FF0000">Les * mentionnent les champs obligatoires.</font></h6>
<input type="hidden" name="FROM" value="ROOT">
<input type="hidden" name="IDFATHER" value="ENTRYPOINT">
<input type="hidden" name="ACTION" value="">
<input type="hidden" name="TYPE" value="PRJ">
<table summary="" border="0">
  <tr>
    <td>Nom : *</td>
    <td>
    <input type="text" name="NAME" size="40" maxlength="128" value="" onblur="checkModification();"><input type="hidden" name="OLDNAME" size="40" maxlength="128" value="">
    </td>
  </tr>
  <tr>
    <td>Description : </td>
    <td>
    <input type="text" name="DESC" size="40" maxlength="40" value="" onblur="checkModification();"><input type="hidden" name="OLDDESC" size="40" maxlength="40" value="">
    </td>
  </tr>
  <tr>
    <td>Date de création : </td>
    <td><%=dateInst%> <input type="hidden" name="DATEINST" value="<%=dateInst%>"></td>
  </tr>
  <tr>
    <td>Date de modification : </td>
    <td><%=dateMaj%> <input type="hidden" name="DATEMAJ" value="<%=dateMaj%>"></td>
  </tr>
  <tr>
    <td>Type d'usage : *</td>
    <td>
        <select name="USAGE" >
            <option value="STANDARD-OBJECT">STANDARD-OBJECT</option>
            <option value="LAPOSTE-OBJECT">LAPOSTE-OBJECT</option>
            <option value="COBOL-ANY">COBOL-ANY</option>
            <option value="UML">UML</option>
        </select>
    </td>
  </tr>
  <tr>
    <td>Stream Clear Case : </td>
    <td>
    <input type="text" name="STREAM" size="40" maxlength="40" value="" onblur="checkModification();"><input type="hidden" name="OLDSTREAM" size="40" maxlength="40" value="">
    </td>
  </tr>
   <tr>

    <td>PVobName : </td>

    <td><input type="text" name="PVOBNAME" value="" size="40" maxlength="128" onblur="checkModification();"><input type="hidden" name="OLDPVOBNAME" size="40" maxlength="40" value=""></td>

  </tr>

    <tr>

    <td>VobMountPoint : </td>

    <td><input type="text" name="VOBMOUNTPOINT" value="" size="40" maxlength="128" onblur="checkModification();"><input type="hidden" name="OLDVOBMOUNTPOINT" size="40" maxlength="40" value=""></td>

  </tr>

   <!-- <tr>
        <td>Répertoire Makefile : </td>
        <td><input type="text" name="MAKEFILE_DIR" value="" size="40" maxlength="128" onblur="checkModification();"><input type="hidden" name="OLDMAKEFILE_DIR" size="40" maxlength="40" value=""></td>
    </tr>
    <tr>
        <td>Répertoire Source : </td>
        <td><input type="text" name="SOURCE_DIR" value="" size="40" maxlength="128" onblur="checkModification();"><input type="hidden" name="OLDSOURCE_DIR" size="40" maxlength="40" value=""></td>
    </tr>
    <tr>
        <td>Répertoire Bin : </td>
        <td><input type="text" name="BIN_DIR" value="" size="40" maxlength="128" onblur="checkModification();"><input type="hidden" name="OLDBIN_DIR" size="40" maxlength="40" value=""></td>
    </tr>
    -->
    <tr>
        <td>Périodicité : </td>
        <td>
        <select name="PERIODIC_DIR">
        <option value="true">Oui</option><option value="false"> Non</option></select>
        <input type="hidden" name="OLDPERIODIC_DIR" size="40" maxlength="40" value="">
        </td>
        
    </tr>
  <tr>
    <td>Poids : *</td>
    <td>
    <input type="text" name="POIDS" size="4" maxlength="4" value="" onblur="checkModification();"><input type="hidden" name="OLDPOIDS" size="4" maxlength="4" value="">
    </td>
  </tr>
  
</table>
<br><br>

<input type="submit" value="Annuler" Onclick="valued('CANCELPRJ')">
<input name="MAJ" type="submit" value="Valider" Onclick="valued('OKPRJ');" disabled="true">
</form>
</body>
</html>
