Avant utilisation:
 - Vérifier que le PATH contient bien l'accès à la commande java.

/*************************************************************
* Utilisation de la commande extract:
* Extraction des métriques NOCL, VG et CLOC pour les procédures
* et les programmes contenus dans un répertoire donné.
**************************************************************/
search BASEDIR PROPERTIESFILE PROGRAMOUTPUTFILE PROCOUTPUTFILE

BASEDIR           : le répertoire racine contenant les fichiers sources.
PROPERTIESFILE    : le fichier properties de configuration de l'analyse.
PROGRAMOUTPUTFILE : le fichier CSV de sortie pour les programmes.
PROCOUTPUTFILE    : le fichier CSV de sortie pour les procédures.

Exemple d'utilisation:
extract C:/Analyses/PL1/src resources/extractor.properties resultProg.csv resultProc.csv

/**************************************************************
* Configuration via le fichier properties.
***************************************************************/
metrics.regexp.filefilter
    - filtre sur les fichiers à analyser.
    
metrics.regexp.mainproc.separator
    - le séparateur utilisé pour les expressions régulières d'identification d'un main.
    
metrics.regexp.mainproc
    - les expressions régulières d'identification d'un main séparées par le séparateur spécifié.

metrics.regexp.proc
    - l'expression régulière d'identification d'une procédure.
    
metrics.regexp.end
    - les expressions régulières d'identification de fin de bloc séparées par des ",".

metrics.regexp.comments
    - les expressions régulières d'identification de commentaire séparées par des ",".

metrics.regexp.empty
    - l' expression régulière d'identification de ligne vide.

metrics.regexp.complexity
    - les expressions régulières d'identification de mot clé pour le calcul du VG séparées par des ",".

metrics.regexp.needEnd
    - les expressions régulières d'identification de mot clé nécessitant une fin de bloc séparées par des ",".
    
