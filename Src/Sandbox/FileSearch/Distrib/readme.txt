Avant utilisation:
 - Vérifier que le PATH contient bien l'accès à la commande java.

/************************************
* Utilisation de la commande search:
*************************************/
search BASEDIR XMLCONFIGFILE OUTPUTFILE

BASEDIR       : le répertoire racine pour la recherche.
XMLCONFIGFILE : le fichier de configuration des recherches.
OUTPUTFILE    : (optionnel) le fichier de sortie pour la recherche.

Exemple d'utilisation:
search C:\Analyses\CarsCode\src regexp/exemple_config.xml exemple_result.xml


/*************************************************
* Utilisation de la commande extract:
* Extraction des anomalies détectées à checkstyle:
**************************************************/
extractcheckstyle FICHIERXMLCHECKSTYLE PATHTOSRC CHECKSTYLECLASSNAME ANOMALIELIB OUTPUTFILE

FICHIERXMLCHECKSTYLE : Le fichier de résultat xml checkstyle.
PATHTOSRC            : Le chemin d'accès aux sources comme défini dans le fichier de résultat checkstyle.
CHECKSTYLECLASSNAME  : Le nom de la classe checkstyle à extraire (respecter la casse).
ANOMALIELIB          : Le libéllé de l'anomalie Renault.
OUTPUTFILE           : Le fichier csv de résultat.

Exemple d'utilisation:
extractcheckstyle ../checkstyleOut.xml D:\ReferentielRenault\AnalyseProd\sources\1_Audit_en_cours\GDI(Java)\src\ AvoidUsingLiteralInformation V4 extractv4.csv

/******************************************************
* Utilisation de la commande extract:
* Extraction des anomalies détectées à OptimalAdvisor:
*******************************************************/
extractadvisor FICHIERXMLADVISOR PATHTOSRC ADVISORCLASSNAME ANOMALIELIB OUTPUTFILE

FICHIERXMLADVISOR    : Le fichier de résultat xml advisor.
PATHTOSRC            : Le chemin d'accès aux sources comme défini dans le fichier de résultat advisor.
ADVISORCLASSNAME     : Le nom de la classe advisor à extraire (respecter la casse).
ANOMALIELIB          : Le libéllé de l'anomalie Renault.
OUTPUTFILE           : Le fichier csv de résultat.

Exemple d'utilisation:
extractadvisor ../tmpAdvisorAnalyse.xml D:\ReferentielRenault\AnalyseProd\sources\1_Audit_en_cours\GDI(Java)\src\ EmptyCatchBlock R4 extract_R4.csv


/**********************************************************************
* Utilisation de la commande analyse:
* Extraction des anomalies détectées par Checkstyle et OptimalAdvisor
* pour un certain nombre d'anomalies V4, S2, JV1, R4, Code mort.
***********************************************************************/
analyse FICHIERXMLCHECKSTYLE PATHTOSRC FICHIERXMLADVISOR 

FICHIERXMLCHECKSTYLE : Le fichier de résultat xml checkstyle.
PATHTOSRC            : Le chemin d'accès aux sources comme défini dans le fichier de résultat checkstyle (ou advisor).
FICHIERXMLADVISOR    : Le fichier de résultat xml advisor.

Extraction globale:
analyse ../checkstyleOut.xml D:\ReferentielRenault\AnalyseProd\sources\1_Audit_en_cours\GDI(Java)\src\ ../tmpAdvisorAnalyse.xml