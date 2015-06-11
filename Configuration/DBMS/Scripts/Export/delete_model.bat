@echo off
echo suppression modèle qualimétrique
if "%4"=="" goto usage

sqlplus %1/%2@%3 @delete_model.sql %4 > delete_model_%4.log

goto end
:usage
echo .
echo . usage : %0  user  password  OracleSid  idUsage
echo .
echo . example : %0  pqlapp  pqlapp  CAQS  STANDARD-JAVA
echo .
pause
:end
