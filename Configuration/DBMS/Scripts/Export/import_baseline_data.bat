@echo off
echo import donn�es projet
if "%3"=="" goto usage
imp %1/%2@%3 tables=PROJET file=PROJET COMMIT=Y IGNORE=Y
imp %1/%2@%3 tables=BASELINE file=BASELINE_INST COMMIT=Y IGNORE=Y
imp %1/%2@%3 tables=BASELINE file=BASELINE COMMIT=Y IGNORE=Y
imp %1/%2@%3 tables=ELEMENT file=ELEMENT_STRUCT COMMIT=Y IGNORE=Y
imp %1/%2@%3 tables=PACKAGE file=PACKAGE COMMIT=Y IGNORE=Y
imp %1/%2@%3 tables=ELEMENT file=ELEMENT COMMIT=Y IGNORE=Y
imp %1/%2@%3 tables=ELT_LINKS file=ELT_LINKS COMMIT=Y IGNORE=Y
imp %1/%2@%3 tables=QAMETRIQUE file=QAMETRIQUE COMMIT=Y IGNORE=Y
imp %1/%2@%3 tables=JUSTIFICATION file=JUSTIFICATION_LINK COMMIT=Y IGNORE=Y
imp %1/%2@%3 tables=JUSTIFICATION file=JUSTIFICATION COMMIT=Y IGNORE=Y
imp %1/%2@%3 tables=CRITERE_BLINE file=CRITERE_BLINE COMMIT=Y IGNORE=Y
imp %1/%2@%3 tables=LABELLISATION file=LABELLISATION_LINK COMMIT=Y IGNORE=Y
imp %1/%2@%3 tables=LABELLISATION file=LABELLISATION COMMIT=Y IGNORE=Y
imp %1/%2@%3 tables=FACTEUR_BLINE file=FACTEUR_BLINE COMMIT=Y IGNORE=Y
imp %1/%2@%3 tables=POIDS_FACT_CRIT file=POIDS_FACT_CRIT COMMIT=Y IGNORE=Y
imp %1/%2@%3 tables=LINK_REAL file=LINK_REAL COMMIT=Y IGNORE=Y
imp %1/%2@%3 tables=LINK_ELT_BLINE file=LINK_ELT_BLINE COMMIT=Y IGNORE=Y
imp %1/%2@%3 tables=ARCHI_LINK file=ARCHI_LINK COMMIT=Y IGNORE=Y
imp %1/%2@%3 tables=CRITERENOTEREPARTITION file=CRITERENOTEREPARTITION COMMIT=Y IGNORE=Y
imp %1/%2@%3 tables=ELEMENT_BASELINE_INFO file=ELEMENT_BASELINE_INFO COMMIT=Y IGNORE=Y
goto end
:usage
echo .
echo . usage : %0  user  password  OracleSID
echo .
echo . example : %0  pqlapp pqlapp CAQS
echo .
pause
:end