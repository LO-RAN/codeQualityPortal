@echo off
echo import données métrique, critère, facteur
if "%3"=="" goto usage

unzip common_%3.zip *.DMP

imp %1/%2@%3 tables=METRIQUE        file=%3_METRIQUE        COMMIT=Y IGNORE=Y log=METRIQUE_import.log
imp %1/%2@%3 tables=CRITERE         file=%3_CRITERE         COMMIT=Y IGNORE=Y log=CRITERE_import.log
imp %1/%2@%3 tables=FACTEUR         file=%3_FACTEUR         COMMIT=Y IGNORE=Y log=FACTEUR_import.log

imp %1/%2@%3 tables=I18N            file=%3_I18N            COMMIT=Y IGNORE=Y log=I18N_import.log

del *.DMP

goto end
:usage
echo .
echo . usage : %0  user  password  OracleSid
echo .
echo . example : %0  pqlapp  pqlapp  CAQS
echo .
pause
:end
