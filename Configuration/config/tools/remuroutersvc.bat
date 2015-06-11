@echo off
call settings.bat
rem ---------------------------------------
echo Installing service %SERVICE_NAME%...
net stop %UROUTER_SERVICE_NAME% & %UROUTER_PATH%\bin\urouter.exe /rem=%UROUTER_SERVICE_NAME%

