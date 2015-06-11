@echo off
echo export données métrique, critère, facteur
if "%3"=="" goto usage
exp %1/%2@%3 tables=METRIQUE        file=%3_METRIQUE log=%3_METRIQUE.log query=\"\"
exp %1/%2@%3 tables=CRITERE         file=%3_CRITERE  log=%3_CRITERE.log  query=\"\"
exp %1/%2@%3 tables=FACTEUR         file=%3_FACTEUR  log=%3_FACTEUR.log  query=\"\"
exp %1/%2@%3 tables=I18N            file=%3_I18N     log=%3_I18N.log     query=\"\"

zip -m -9 common_%3.zip %3_*

goto end
:usage
echo .
echo . usage : %0  user  password  OracleSid
echo .
echo . example : %0  pqlapp  pqlapp  CAQS
echo .
pause
:end
