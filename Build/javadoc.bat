@echo off
set ADVISOR_HOME=.\lib
set LM_LICENSE_FILE=%ADVISOR_HOME%\advisorLicense.dat
set PATH=%ADVISOR_HOME%;%PATH%

%JAVA_HOME%\bin\java -cp %ADVISOR_HOME%/JLmgr.jar;%ADVISOR_HOME%/OptimalAdvisor.jar;./lib/ant.jar;./lib/ant-launcher.jar;%JAVA_HOME%\lib\tools.jar -Dant.home=. org.apache.tools.ant.Main -buildfile scripts/compile/build.xml -Djdk118.home=%JAVA_HOME% -DADVISOR_HOME="%ADVISOR_HOME%" -Djava.library.path="%ADVISOR_HOME%" doc
pause
rem end

