Avant utilisation:
 - V�rifier que le PATH contient bien l'acc�s � la commande java.

/*************************************************************
* Utilisation de la commande extract:
* Extraction des m�triques NOCL, VG et CLOC pour les proc�dures
* et les programmes contenus dans un r�pertoire donn�.
**************************************************************/
search BASEDIR PROPERTIESFILE PROGRAMOUTPUTFILE PROCOUTPUTFILE

BASEDIR           : le r�pertoire racine contenant les fichiers sources.
PROPERTIESFILE    : le fichier properties de configuration de l'analyse.
PROGRAMOUTPUTFILE : le fichier CSV de sortie pour les programmes.
PROCOUTPUTFILE    : le fichier CSV de sortie pour les proc�dures.

Exemple d'utilisation:
extract C:/Analyses/PL1/src resources/extractor.properties resultProg.csv resultProc.csv

/**************************************************************
* Configuration via le fichier properties.
***************************************************************/
metrics.regexp.filefilter
    - filtre sur les fichiers � analyser.
    
metrics.regexp.mainproc.separator
    - le s�parateur utilis� pour les expressions r�guli�res d'identification d'un main.
    
metrics.regexp.mainproc
    - les expressions r�guli�res d'identification d'un main s�par�es par le s�parateur sp�cifi�.

metrics.regexp.proc
    - l'expression r�guli�re d'identification d'une proc�dure.
    
metrics.regexp.end
    - les expressions r�guli�res d'identification de fin de bloc s�par�es par des ",".

metrics.regexp.comments
    - les expressions r�guli�res d'identification de commentaire s�par�es par des ",".

metrics.regexp.empty
    - l' expression r�guli�re d'identification de ligne vide.

metrics.regexp.complexity
    - les expressions r�guli�res d'identification de mot cl� pour le calcul du VG s�par�es par des ",".

metrics.regexp.needEnd
    - les expressions r�guli�res d'identification de mot cl� n�cessitant une fin de bloc s�par�es par des ",".
    
