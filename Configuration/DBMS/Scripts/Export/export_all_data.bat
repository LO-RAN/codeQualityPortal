@echo off
if "%3"=="" goto usage
exp %1/%2@%3 tables=PROJET file=PROJET
exp %1/%2@%3 tables=BASELINE file=BASELINE_INST
exp %1/%2@%3 tables=BASELINE file=BASELINE
exp %1/%2@%3 tables=ELEMENT file=ELEMENT_STRUCT
exp %1/%2@%3 tables=PACKAGE file=PACKAGE
exp %1/%2@%3 tables=ELEMENT file=ELEMENT
exp %1/%2@%3 tables=ELT_LINKS file=ELT_LINKS
exp %1/%2@%3 tables=QAMETRIQUE file=QAMETRIQUE
exp %1/%2@%3 tables=JUSTIFICATION file=JUSTIFICATION_LINK
exp %1/%2@%3 tables=JUSTIFICATION file=JUSTIFICATION
exp %1/%2@%3 tables=CRITERE_BLINE file=CRITERE_BLINE
exp %1/%2@%3 tables=CRITERENOTEREPARTITION file=CRITERENOTEREPARTITION
exp %1/%2@%3 tables=LABELLISATION file=LABELLISATION_LINK
exp %1/%2@%3 tables=LABELLISATION file=LABELLISATION
exp %1/%2@%3 tables=FACTEUR_BLINE file=FACTEUR_BLINE
exp %1/%2@%3 tables=POIDS_FACT_CRIT file=POIDS_FACT_CRIT
exp %1/%2@%3 tables=LINK_REAL file=LINK_REAL
exp %1/%2@%3 tables=LINK_ELT_BLINE file=LINK_ELT_BLINE
exp %1/%2@%3 tables=ARCHI_LINK file=ARCHI_LINK
goto end
:usage
echo .
echo . usage   : %0  user  password   OracleSID
echo .
echo . example : %0  pqlapp pqlapp CAQS 
echo .
pause
:end
