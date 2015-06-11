@echo off
"%JAVA_HOME%\bin\java" -cp "./lib/ant.jar;./lib/ant-launcher.jar;%ADVISOR_HOME%\bin\OptimalAdvisor.jar;%ADVISOR_HOME%\lib\JLmgr.jar;%JAVA_HOME\jre/lib/ext/jdom.jar%;%JAVA_HOME%\lib\tools.jar" -Dant.home=. org.apache.tools.ant.Main -buildfile scripts/compile/build.xml %1
pause
rem end
