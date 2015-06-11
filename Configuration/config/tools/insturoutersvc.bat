@echo off
call settings.bat
rem ---------------------------------------
echo Installing service %UROUTER_SERVICE_NAME%...
%UROUTER_PATH%\bin\urouter.exe /ins=%UROUTER_SERVICE_NAME% & net start %UROUTER_SERVICE_NAME%



