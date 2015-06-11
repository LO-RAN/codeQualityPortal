@echo off
rem this script can be used to test Ant targets individually
rem before they get included in a process flow
rem type : ant -help    for help on using Ant
rem type : ant -p       for the list of available targets
set ANT_HOME=%~dp0\..\..
set CLASSPATH=%ANT_HOME%\lib\ant-launcher-1.6.5.jar;%ANT_HOME%\lib\ant-1.6.5.jar
java -classpath %CLASSPATH% -Dant.home=%ANT_HOME% org.apache.tools.ant.Main -f %~dp0\build_analysis.xml %*
