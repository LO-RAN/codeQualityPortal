@echo off
"%JAVA_HOME%\bin\java" -cp "./lib/ant.jar;./lib/ant-launcher.jar;%JAVA_HOME%\lib\tools.jar" -Dant.home=. org.apache.tools.ant.Main -buildfile scripts/compile/build.xml compile-last-version
pause
rem end