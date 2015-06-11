@echo off
rem passer l'URL à appeler en paramètre

rem %~dp0 means the directory in which this batch is located and executed from.
rem It allows the tool to be fully relocatable.

java -cp "%~dp0batch-1.0.jar" com.compuware.carscode.batch.CallServlet %* > "%~dp0\callServlet.log" 
if errorlevel 1 goto error
echo 1
goto end
:error
echo 0
:end
