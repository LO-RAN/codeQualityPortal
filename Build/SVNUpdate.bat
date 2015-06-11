@echo off
echo Processing SVN update on all CAQS modules.
echo When finished, the log file should open in Notepad for thorough checking...
"%JAVA_HOME%\bin\java" -cp "./lib/ant.jar;./lib/ant-launcher.jar;%JAVA_HOME%\lib\tools.jar" -Dant.home=. org.apache.tools.ant.Main -buildfile scripts/compile/build.xml svnupdate  > svnUpdate.log
notepad svnUpdate.log
