@echo off
echo export données projet
if "%4"=="" goto usage
exp %1/%2@%3 tables=PROJET                 file=%4_PROJET                 log=%4_PROJET.log                 query=\"where ID_PRO='%4'\"
exp %1/%2@%3 tables=BASELINE               file=%4_BASELINE               log=%4_BASELINE.log               query=\"where PRO_BLINRE='%4'\"
exp %1/%2@%3 tables=ELEMENT                file=%4_ELEMENT_STRUCT         log=%4_ELEMENT_STRUCT.log         query=\"where ID_PRO='%4' AND (ID_TELT='PRJ' OR ID_TELT='SSP' OR ID_TELT='EA')\"
exp %1/%2@%3 tables=PACKAGE                file=%4_PACKAGE                log=%4_PACKAGE.log                query=\"where ID_PACK in (Select ID_PACK From Element Where ID_PRO='%4')\"
exp %1/%2@%3 tables=ELEMENT                file=%4_ELEMENT                log=%4_ELEMENT.log                query=\"where ID_PRO='%4' AND (ID_TELT<>'PRJ' AND ID_TELT<>'SSP' AND ID_TELT<>'EA')\"
exp %1/%2@%3 tables=ELT_LINKS              file=%4_ELT_LINKS              log=%4_ELT_LINKS.log              query=\"where ELT_FILS in (Select ID_ELT From Element Where ID_PRO='%4')\"
exp %1/%2@%3 tables=QAMETRIQUE             file=%4_QAMETRIQUE             log=%4_QAMETRIQUE.log             query=\"where ID_BLINE in (Select id_bline From Baseline where PRO_BLINRE='%4')\"
exp %1/%2@%3 tables=JUSTIFICATION          file=%4_JUSTIFICATION_LINK     log=%4_JUSTIFICATION_LINK.log     query=\"where ID_JUST in (Select j.JUST_LINK From Justification j, Critere_bline c Where c.ID_BLINE in (Select id_bline From Baseline where PRO_BLINRE='%4') AND c.ID_JUST IS NOT NULL AND c.ID_JUST=j.ID_JUST)\"
exp %1/%2@%3 tables=JUSTIFICATION          file=%4_JUSTIFICATION          log=%4_JUSTIFICATION.log          query=\"where ID_JUST in (Select distinct j.ID_JUST From Justification j, Critere_bline c Where c.ID_BLINE in (Select id_bline From Baseline where PRO_BLINRE='%4') AND c.ID_JUST IS NOT NULL AND c.ID_JUST=j.ID_JUST)\"
exp %1/%2@%3 tables=CRITERE_BLINE          file=%4_CRITERE_BLINE          log=%4_CRITERE_BLINE.log          query=\"where ID_BLINE in (Select id_bline From Baseline where PRO_BLINRE='%4')\"
exp %1/%2@%3 tables=CRITERENOTEREPARTITION file=%4_CRITERENOTEREPARTITION log=%4_CRITERENOTEREPARTITION.log query=\"where ID_BLINE in (Select id_bline From Baseline where PRO_BLINRE='%4')\"
exp %1/%2@%3 tables=ELEMENT_BASELINE_INFO  file=%4_ELEMENT_BASELINE_INFO  log=%4_ELEMENT_BASELINE_INFO.log  query=\"where ID_BLINE in (Select id_bline From Baseline where PRO_BLINRE='%4')\"
exp %1/%2@%3 tables=LABELLISATION          file=%4_LABELLISATION_LINK     log=%4_LABELLISATION_LINK.log     query=\"where ID_LABEL in (Select distinct l.LABEL_LINK From Labellisation l, Facteur_bline f Where f.ID_BLINE in (Select id_bline From Baseline where PRO_BLINRE='%4') AND f.ID_LABEL IS NOT NULL AND f.ID_LABEL=l.ID_LABEL)\"
exp %1/%2@%3 tables=LABELLISATION          file=%4_LABELLISATION          log=%4_LABELLISATION.log          query=\"where ID_LABEL in (Select distinct l.ID_LABEL From Labellisation l, Facteur_bline f Where f.ID_BLINE in (Select id_bline From Baseline where PRO_BLINRE='%4') AND f.ID_LABEL IS NOT NULL AND f.ID_LABEL=l.ID_LABEL)\"
exp %1/%2@%3 tables=FACTEUR_BLINE          file=%4_FACTEUR_BLINE          log=%4_FACTEUR_BLINE.log          query=\"where ID_BLINE in (Select id_bline From Baseline where PRO_BLINRE='%4')\"
exp %1/%2@%3 tables=POIDS_FACT_CRIT        file=%4_POIDS_FACT_CRIT        log=%4_POIDS_FACT_CRIT.log        query=\"where BLINE_POIDS in (Select id_bline From Baseline where PRO_BLINRE='%4')\"
exp %1/%2@%3 tables=LINK_REAL              file=%4_LINK_REAL              log=%4_LINK_REAL.log              query=\"where ID_BLINE in (Select id_bline From Baseline where PRO_BLINRE='%4')\"
exp %1/%2@%3 tables=LINK_ELT_BLINE         file=%4_LINK_ELT_BLINE         log=%4_LINK_ELT_BLINE.log         query=\"where ID_BLINE in (Select id_bline From Baseline where PRO_BLINRE='%4')\"
exp %1/%2@%3 tables=ARCHI_LINK             file=%4_ARCHI_LINK             log=%4_ARCHI_LINK.log             query=\"where ID_PROJ in (SELECT id_elt From Element Where ID_PRO='%4')\"

zip -m -9 projet_%4.zip %4_*

goto end
:usage
echo .
echo . usage : %0  user  password  OracleSID  projectId
echo .
echo . example : %0  pqlapp pqlapp CAQS  20060320150830984334443
echo .
pause
:end
