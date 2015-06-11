@echo off
REM #!/bin/sh

set CLASSPATH=.\installlib\xercesImpl.jar;.\installlib\xml-apis.jar;.\installlib\ant-installer.jar;.\installlib\tools.jar
set CLASSPATH=%CLASSPATH%;.\installlib\ant.jar;.\installlib\ant-launcher.jar
set CLASSPATH=%CLASSPATH%;.\installclasspath
set CLASSPATH=%CLASSPATH%;.\installlib\jgoodies-edited-1_2_2.jar
set CLASSPATH=%CLASSPATH%;.\installlib\sysout.jar
set CLASSPATH=%CLASSPATH%;.\installlib\h2-1.2.139.jar
set CLASSPATH=%CLASSPATH%;.\installlib\jtds-1.2.2.jar
set CLASSPATH=%CLASSPATH%;.\installlib\oracle-jdbc-1.4.jar
set CLASSPATH=%CLASSPATH%;.\installlib\postgresql-9.1-901.jdbc3.jar


if "%1" == "text" goto settext
if "%1" == "swing" goto setswing
goto setswing

:settext
set COMMAND=java
set INTERFACE=text
goto install

:setswing
set COMMAND=javaw
set INTERFACE=swing
goto install

:install

start %COMMAND% -Xms768m -Xmx768m -classpath %CLASSPATH%  org.tp23.antinstaller.runtime.ExecInstall %INTERFACE% .


:end
