@echo off
rem this script can be used to test Ant targets individually
rem before they get included in a process flow
rem type : ant -help    for help on using Ant
rem type : ant -p       for the list of available targets
set ANT_HOME=%~dp0
set CLASSPATH=%ANT_HOME%\lib\ant-launcher-1.6.5.jar;%ANT_HOME%\lib\ant-1.6.5.jar;%ANT_HOME%\lib\anttask-1.3.jar;%ANT_HOME%\lib\ant-commons-net-1.6.5.jar;%ANT_HOME%\lib\commons-net-1.4.1.jar;%ANT_HOME%\lib\jakarta-oro-2.0.8.jar;%ANT_HOME%\export\importExport-1.3.jar;%ANT_HOME%\export\commons-logging.jar;%ANT_HOME%\lib\jsch-0.1.30.jar;%ANT_HOME%\lib\ant-nodeps.jar
set CLASSPATH=%CLASSPATH%;..\..\lib\oracle-jdbc-1.4.jar
set CLASSPATH=%CLASSPATH%;..\..\lib\h2-1.2.139.jar
java -classpath %CLASSPATH% -Dant.home=%ANT_HOME% org.apache.tools.ant.Main %*
