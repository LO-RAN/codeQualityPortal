@echo off

cd ..\Src
call mvn -DdynaTrace.agentPath=d:\cpwr\dynaTrace_6.2\agent\lib64\dtagent.dll  clean install

IF %ERRORLEVEL% NEQ 0 goto mvnerror



Echo Build succeeded; making things installable...
cd ..\Build
"%JAVA_HOME%\bin\java" -cp ./lib/ant.jar;./lib/ant-launcher.jar;"%JAVA_HOME%\lib\tools.jar" -Dant.home=. org.apache.tools.ant.Main -buildfile scripts/compile/build.xml makeInstall

goto theend

:mvnerror
echo ERROR : MAVEN BUILD FAILED !

:theend
pause
rem end
