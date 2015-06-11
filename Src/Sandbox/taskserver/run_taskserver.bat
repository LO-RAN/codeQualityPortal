@echo off
java -cp .\extjars\ant.jar;.\extjars\jaxp.jar;.\extjars\parser.jar;%JAVA_HOME%\lib\tools.jar -Dant.home=. org.apache.tools.ant.Main -buildfile build/build.xml deploy
pause
rem end
