@echo off
echo Processing SVN checkout on all CAQS modules.
"%JAVA_HOME%\bin\java" -cp "./lib/ant.jar;./lib/ant-launcher.jar;%JAVA_HOME%\lib\tools.jar" -Dant.home=. org.apache.tools.ant.Main -buildfile scripts/compile/build.xml svncheckout

