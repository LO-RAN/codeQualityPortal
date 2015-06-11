@echo off
echo export données baseline
if "%5"=="" goto usage
exp %1/%2@%3 tables=PROJET file=PROJET query=\"where ID_PRO='%4'\"
exp %1/%2@%3 tables=BASELINE file=BASELINE_INST query=\"where PRO_BLINRE='%4' and LIB_BLINE='BaseLine d''Instanciation'\"
exp %1/%2@%3 tables=BASELINE file=BASELINE query=\"where ID_BLINE='%5'\"
exp %1/%2@%3 tables=ELEMENT file=ELEMENT_STRUCT query=\"where ID_PRO='%4' AND (ID_TELT='PRJ' OR ID_TELT='SSP' OR ID_TELT='EA')\"
exp %1/%2@%3 tables=PACKAGE file=PACKAGE query=\"where ID_PACK in (Select ID_PACK From Element Where ID_PRO='%4')\"
exp %1/%2@%3 tables=ELEMENT file=ELEMENT query=\"where ID_PRO='%4' AND (ID_TELT<>'PRJ' AND ID_TELT<>'SSP' AND ID_TELT<>'EA')\"
exp %1/%2@%3 tables=ELT_LINKS file=ELT_LINKS query=\"where ELT_FILS in (Select ID_ELT From Element Where ID_PRO='%4')\"
exp %1/%2@%3 tables=QAMETRIQUE file=QAMETRIQUE query=\"where ID_BLINE='%5'\"
exp %1/%2@%3 tables=JUSTIFICATION file=JUSTIFICATION_LINK query=\"where ID_JUST in (Select j.JUST_LINK From Justification j, Critere_bline c Where c.ID_BLINE='%5' AND c.ID_JUST IS NOT NULL AND c.ID_JUST=j.ID_JUST)\"
exp %1/%2@%3 tables=JUSTIFICATION file=JUSTIFICATION query=\"where ID_JUST in (Select distinct j.ID_JUST From Justification j, Critere_bline c Where c.ID_BLINE='%5' AND c.ID_JUST IS NOT NULL AND c.ID_JUST=j.ID_JUST)\"
exp %1/%2@%3 tables=CRITERE_BLINE file=CRITERE_BLINE query=\"where ID_BLINE='%5'\"
exp %1/%2@%3 tables=CRITERENOTEREPARTITION file=CRITERENOTEREPARTITION query=\"where ID_BLINE='%5'\"
exp %1/%2@%3 tables=ELEMENT_BASELINE_INFO file=ELEMENT_BASELINE_INFO query=\"where ID_BLINE='%5'\"
exp %1/%2@%3 tables=LABELLISATION file=LABELLISATION_LINK query=\"where ID_LABEL in (Select distinct l.LABEL_LINK From Labellisation l, Facteur_bline f Where f.ID_BLINE='%5' AND f.ID_LABEL IS NOT NULL AND f.ID_LABEL=l.ID_LABEL)\"
exp %1/%2@%3 tables=LABELLISATION file=LABELLISATION query=\"where ID_LABEL in (Select distinct l.ID_LABEL From Labellisation l, Facteur_bline f Where f.ID_BLINE='%5' AND f.ID_LABEL IS NOT NULL AND f.ID_LABEL=l.ID_LABEL)\"
exp %1/%2@%3 tables=FACTEUR_BLINE file=FACTEUR_BLINE query=\"where ID_BLINE='%5'\"
exp %1/%2@%3 tables=POIDS_FACT_CRIT file=POIDS_FACT_CRIT query=\"where BLINE_POIDS='%5'\"
exp %1/%2@%3 tables=LINK_REAL file=LINK_REAL query=\"where ID_BLINE='%5'\"
exp %1/%2@%3 tables=LINK_ELT_BLINE file=LINK_ELT_BLINE query=\"where ID_BLINE='%5'\"
exp %1/%2@%3 tables=ARCHI_LINK file=ARCHI_LINK query=\"where ID_PROJ in (Select ID_ELT From Element Where ID_PRO='%4')\"
goto end
:usage
echo .
echo . usage   : %0  user  password  OracleSID  projectId  baselineId
echo .
echo . example : %0  pqlapp pqlapp CAQS  20060320150830984334443 20060320150831062397028
echo .
pause
:end
