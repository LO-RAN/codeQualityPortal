@echo off
echo import données projet
if "%4"=="" goto usage
unzip projet_%4.zip *.DMP

imp %1/%2@%3 tables=PROJET file=%4_PROJET COMMIT=Y IGNORE=Y log=%4_PROJET_import.log
imp %1/%2@%3 tables=BASELINE file=%4_BASELINE COMMIT=Y IGNORE=Y log=%4_BASELINE_import.log
imp %1/%2@%3 tables=ELEMENT file=%4_ELEMENT_STRUCT COMMIT=Y IGNORE=Y log=%4_ELEMENT_STRUCT_import.log
imp %1/%2@%3 tables=PACKAGE file=%4_PACKAGE COMMIT=Y IGNORE=Y log=%4_PACKAGE_import.log
imp %1/%2@%3 tables=ELEMENT file=%4_ELEMENT COMMIT=Y IGNORE=Y log=%4_ELEMENT_import.log
imp %1/%2@%3 tables=ELT_LINKS file=%4_ELT_LINKS COMMIT=Y IGNORE=Y log=%4_ELT_LINKS_import.log
imp %1/%2@%3 tables=QAMETRIQUE file=%4_QAMETRIQUE COMMIT=Y IGNORE=Y log=%4_QAMETRIQUE_import.log
imp %1/%2@%3 tables=JUSTIFICATION file=%4_JUSTIFICATION_LINK COMMIT=Y IGNORE=Y log=%4_JUSTIFICATION_LINK_import.log
imp %1/%2@%3 tables=JUSTIFICATION file=%4_JUSTIFICATION COMMIT=Y IGNORE=Y log=%4_JUSTIFICATION_import.log
imp %1/%2@%3 tables=CRITERE_BLINE file=%4_CRITERE_BLINE COMMIT=Y IGNORE=Y log=%4_CRITERE_BLINE_import.log
imp %1/%2@%3 tables=LABELLISATION file=%4_LABELLISATION_LINK COMMIT=Y IGNORE=Y log=%4_LABELLISATION_LINK_import.log
imp %1/%2@%3 tables=LABELLISATION file=%4_LABELLISATION COMMIT=Y IGNORE=Y log=%4_LABELLISATION_import.log
imp %1/%2@%3 tables=FACTEUR_BLINE file=%4_FACTEUR_BLINE COMMIT=Y IGNORE=Y log=%4_FACTEUR_BLINE_import.log
imp %1/%2@%3 tables=POIDS_FACT_CRIT file=%4_POIDS_FACT_CRIT COMMIT=Y IGNORE=Y log=%4_POIDS_FACT_CRIT_import.log
imp %1/%2@%3 tables=LINK_REAL file=%4_LINK_REAL COMMIT=Y IGNORE=Y log=%4_LINK_REAL_import.log
imp %1/%2@%3 tables=LINK_ELT_BLINE file=%4_LINK_ELT_BLINE COMMIT=Y IGNORE=Y log=%4_LINK_ELT_BLINE_import.log
imp %1/%2@%3 tables=ARCHI_LINK file=%4_ARCHI_LINK COMMIT=Y IGNORE=Y log=%4_ARCHI_LINK_import.log
imp %1/%2@%3 tables=CRITERENOTEREPARTITION file=%4_CRITERENOTEREPARTITION COMMIT=Y IGNORE=Y log=%4_CRITERENOTEREPARTITION_import.log
imp %1/%2@%3 tables=ELEMENT_BASELINE_INFO file=%4_ELEMENT_BASELINE_INFO COMMIT=Y IGNORE=Y log=%4_ELEMENT_BASELINE_INFO_import.log

del *.DMP

goto end
:usage
echo .
echo . usage : %0  user  password  OracleSID projectId
echo .
echo . example : %0  pqlapp pqlapp CAQS  20060721152401781962450
echo .
pause
:end
