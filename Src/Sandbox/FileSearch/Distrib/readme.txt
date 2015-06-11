Avant utilisation:
 - V�rifier que le PATH contient bien l'acc�s � la commande java.

/************************************
* Utilisation de la commande search:
*************************************/
search BASEDIR XMLCONFIGFILE OUTPUTFILE

BASEDIR       : le r�pertoire racine pour la recherche.
XMLCONFIGFILE : le fichier de configuration des recherches.
OUTPUTFILE    : (optionnel) le fichier de sortie pour la recherche.

Exemple d'utilisation:
search C:\Analyses\CarsCode\src regexp/exemple_config.xml exemple_result.xml


/*************************************************
* Utilisation de la commande extract:
* Extraction des anomalies d�tect�es � checkstyle:
**************************************************/
extractcheckstyle FICHIERXMLCHECKSTYLE PATHTOSRC CHECKSTYLECLASSNAME ANOMALIELIB OUTPUTFILE

FICHIERXMLCHECKSTYLE : Le fichier de r�sultat xml checkstyle.
PATHTOSRC            : Le chemin d'acc�s aux sources comme d�fini dans le fichier de r�sultat checkstyle.
CHECKSTYLECLASSNAME  : Le nom de la classe checkstyle � extraire (respecter la casse).
ANOMALIELIB          : Le lib�ll� de l'anomalie Renault.
OUTPUTFILE           : Le fichier csv de r�sultat.

Exemple d'utilisation:
extractcheckstyle ../checkstyleOut.xml D:\ReferentielRenault\AnalyseProd\sources\1_Audit_en_cours\GDI(Java)\src\ AvoidUsingLiteralInformation V4 extractv4.csv

/******************************************************
* Utilisation de la commande extract:
* Extraction des anomalies d�tect�es � OptimalAdvisor:
*******************************************************/
extractadvisor FICHIERXMLADVISOR PATHTOSRC ADVISORCLASSNAME ANOMALIELIB OUTPUTFILE

FICHIERXMLADVISOR    : Le fichier de r�sultat xml advisor.
PATHTOSRC            : Le chemin d'acc�s aux sources comme d�fini dans le fichier de r�sultat advisor.
ADVISORCLASSNAME     : Le nom de la classe advisor � extraire (respecter la casse).
ANOMALIELIB          : Le lib�ll� de l'anomalie Renault.
OUTPUTFILE           : Le fichier csv de r�sultat.

Exemple d'utilisation:
extractadvisor ../tmpAdvisorAnalyse.xml D:\ReferentielRenault\AnalyseProd\sources\1_Audit_en_cours\GDI(Java)\src\ EmptyCatchBlock R4 extract_R4.csv


/**********************************************************************
* Utilisation de la commande analyse:
* Extraction des anomalies d�tect�es par Checkstyle et OptimalAdvisor
* pour un certain nombre d'anomalies V4, S2, JV1, R4, Code mort.
***********************************************************************/
analyse FICHIERXMLCHECKSTYLE PATHTOSRC FICHIERXMLADVISOR 

FICHIERXMLCHECKSTYLE : Le fichier de r�sultat xml checkstyle.
PATHTOSRC            : Le chemin d'acc�s aux sources comme d�fini dans le fichier de r�sultat checkstyle (ou advisor).
FICHIERXMLADVISOR    : Le fichier de r�sultat xml advisor.

Extraction globale:
analyse ../checkstyleOut.xml D:\ReferentielRenault\AnalyseProd\sources\1_Audit_en_cours\GDI(Java)\src\ ../tmpAdvisorAnalyse.xml