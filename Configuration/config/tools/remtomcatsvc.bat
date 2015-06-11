rem @echo off
call settings.bat
rem ---------------------------------------
echo Removing service %SERVICE_NAME%...
net stop %TOMCAT_SERVICE_NAME% & "%TOMCAT_PATH%\bin\tomcat5.exe" //DS// %TOMCAT_SERVICE_NAME%
