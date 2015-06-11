@echo off
echo import données modèle qualimétrique
if "%4"=="" goto usage

unzip modele_%4.zip *.DMP                                   > %4_unzip.log
unzip -uoCv modele_%4.zip method-%4.xml -d ..\..\..\config >> %4_unzip.log

imp %1/%2@%3 tables=OUTILS          file=%4_OUTILS          COMMIT=Y IGNORE=Y log=%4_OUTILS_import.log
imp %1/%2@%3 tables=METRIQUE        file=%4_METRIQUE        COMMIT=Y IGNORE=Y log=%4_METRIQUE_import.log
imp %1/%2@%3 tables=CRITERE         file=%4_CRITERE         COMMIT=Y IGNORE=Y log=%4_CRITERE_import.log
imp %1/%2@%3 tables=FACTEUR         file=%4_FACTEUR         COMMIT=Y IGNORE=Y log=%4_FACTEUR_import.log

imp %1/%2@%3 tables=MODELE          file=%4_USAGE           COMMIT=Y IGNORE=Y log=%4_USAGE_import.log
imp %1/%2@%3 tables=CRITERE_USAGE   file=%4_CRITERE_USAGE   COMMIT=Y IGNORE=Y log=%4_CRITERE_USAGE_import.log
imp %1/%2@%3 tables=REGLE           file=%4_REGLE           COMMIT=Y IGNORE=Y log=%4_REGLE_import.log
imp %1/%2@%3 tables=FACTEUR_CRITERE file=%4_FACTEUR_CRITERE COMMIT=Y IGNORE=Y log=%4_FACTEUR_CRITERE_import.log

imp %1/%2@%3 tables=I18N            file=%4_I18N_METRIQUE   COMMIT=Y IGNORE=Y log=%4_I18N_METRIQUE_import.log
imp %1/%2@%3 tables=I18N            file=%4_I18N_CRITERE    COMMIT=Y IGNORE=Y log=%4_I18N_CRITEUR_import.log
imp %1/%2@%3 tables=I18N            file=%4_I18N_FACTEUR    COMMIT=Y IGNORE=Y log=%4_I18N_FACTEUR_import.log

del *.DMP

goto end
:usage
echo .
echo . usage : %0  user  password  OracleSid  idUsage
echo .
echo . example : %0  pqlapp  pqlapp  CAQS  STANDARD-JAVA
echo .
pause
:end
