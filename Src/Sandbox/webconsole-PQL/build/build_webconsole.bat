@echo off
set JAVA_HOME=C:\j2sdk1.4.2_03
set PATH=%JAVA_HOME%\bin;%PATH%
%JAVA_HOME%\bin\java -cp ../lib/ant.jar;../lib/jaxp.jar;../lib/parser.jar;%JAVA_HOME%\lib\tools.jar -Dant.home=. org.apache.tools.ant.Main -buildfile build.xml %1
pause
rem end
