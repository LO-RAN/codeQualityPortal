copy %~dp0compuware.properties "C:\CAST Implementation Framework\Customization\Customization\CAQS\compuware.properties"
if errorlevel 1 echo **** ERROR ****

C:
if errorlevel 1 echo **** ERROR ****

cd "C:\CAST Implementation Framework\Customization\Customization\CAQS"
if errorlevel 1 echo **** ERROR ****

call launch.cmd > %~dp0Logs\Pivots.log"
if errorlevel 1 echo **** ERROR ****
