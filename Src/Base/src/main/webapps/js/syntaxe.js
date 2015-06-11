/*
* Verification de la presence de caracteres speciaux dans un champ
*
*/
function verifSyntaxe(champ) 
{ 
   	for (var i = 0;  i < champ.value.length;  i++) {
   			var ch = champ.value.charAt(i);
   			//alert(ch);
   			if (ch=='"' || ch=="'"){
	   			if(champ.type == 'text'){
			 		champ.focus();
			 		champ.select();
				}
   				return false;
   			}
		}
	  	return true;
 }
 
 /*
* Verification de la presence de caracteres speciaux dans un champ
*
*/
function verifSyntaxeValue(champ) 
{ 
   	for (var i = 0;  i < champ.length;  i++) {
   			var ch = champ.charAt(i);
   			if (ch=='"' || ch=="'"){
   				return false;
   			}
		}
	  	return true;
 }
 
 /*
* Verification des noms des éléments
*
*/
function verifSyntaxeName(champ) 
{ 
/*   	for (var i = 0;  i < champ.length;  i++) {
		var charCode = champ.charAt(i);
		if(charCode>='a' && charCode<='z') {//a-z
			continue;
		}	
		if(charCode>='A' && charCode<='Z') {//A-Z
			continue;
		}	
		if(charCode>='0' && charCode<='9') {//0-9
			continue;
		}
		if(charCode=='-' || charCode=='_' || charCode==' ') {//- _
			continue;
		}
		return false;
	}
	return true;*/
	var reg = new RegExp("^(\\w)*(-)*(\\s)*$","gi");
	return reg.test(champ);
 }
 /*
 * verification de la presence de caracteres speciaux dans  tous les champs d'un formulaire
 *
 */
 
 function  verifForm(form){

for(var i = 0; i < document.forms.length; i++)
  {
    // La deuxième boucle parcourt les champs de formulaire
    for(var j = 0; j < document.forms[i].elements.length; j++)
    {
    	 verifSyntaxe(document.forms[i].elements[j]);
    }
   }
}


/*
* Fonction Trim à la fin et au debut de la chaine
*
*/
String.prototype.trim = function()    
{
	var ar;
 return( (ar=/^\s*([\s\S]*\S+)\s*$/.exec(this)) ? ar[1] : "" ); 
};
