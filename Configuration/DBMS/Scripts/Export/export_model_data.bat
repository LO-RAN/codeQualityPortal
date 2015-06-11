@echo off
echo export données modèle qualimétrique
if "%4"=="" goto usage
exp %1/%2@%3 tables=OUTILS          file=%4_OUTILS          query=\"where ID_OUTILS in (Select distinct OUTIL_MET From Metrique m, Regle r Where m.ID_MET = r.ID_MET AND r.ID_USA='%4')\" log=%4_OUTILS_export.log
exp %1/%2@%3 tables=METRIQUE        file=%4_METRIQUE        query=\"where ID_MET in (Select distinct ID_MET From Regle Where ID_USA='%4')\" log=%4_METRIQUE_export.log
exp %1/%2@%3 tables=CRITERE         file=%4_CRITERE         query=\"where ID_CRIT in (Select distinct ID_CRIT From CRITERE_USAGE Where ID_USA='%4')\" log=%4_CRITERE_export.log
exp %1/%2@%3 tables=FACTEUR         file=%4_FACTEUR         query=\"where ID_FACT in (Select distinct ID_FACT From FACTEUR_CRITERE Where ID_USA='%4')\" log=%4_FACTEUR_export.log
exp %1/%2@%3 tables=MODELE          file=%4_USAGE           query=\"where ID_USA='%4'\" log=%4_USAGE_export.log
exp %1/%2@%3 tables=CRITERE_USAGE   file=%4_CRITERE_USAGE   query=\"where ID_USA='%4'\" log=%4_CRITERE_USAGE_export.log
exp %1/%2@%3 tables=REGLE           file=%4_REGLE           query=\"where ID_USA='%4'\" log=%4_REGLE_export.log
exp %1/%2@%3 tables=FACTEUR_CRITERE file=%4_FACTEUR_CRITERE query=\"where ID_USA='%4'\" log=%4_FACTEUR_CRITERE_export.log

exp %1/%2@%3 tables=I18N         file=%4_I18N_METRIQUE      query=\"where table_name='metrique' and id_table in (Select distinct ID_MET From Regle Where ID_USA='%4')\" log=%4_I18N_METRIQUE_export.log
exp %1/%2@%3 tables=I18N         file=%4_I18N_CRITERE       query=\"where table_name='critere' and id_table in (Select distinct ID_CRIT From CRITERE_USAGE Where ID_USA='%4')\" log=%4_I18N_CRITERE_export.log
exp %1/%2@%3 tables=I18N         file=%4_I18N_FACTEUR       query=\"where table_name='facteur' and id_table in (Select distinct ID_FACT From FACTEUR_CRITERE Where ID_USA='%4')\" log=%4_I18N_FACTEUR_export.log

copy ..\..\..\config\method-%4.xml .
zip -m -9 modele_%4.zip %4_* method-%4.xml
 
goto end
:usage
echo .
echo . usage : %0  user  password  OracleSid  idUsage
echo .
echo . example : %0  pqlapp  pqlapp  CAQS  STANDARD-JAVA
echo .
pause
:end
