set ANT_HOME=%~dp0..
set CLASSPATH="%ANT_HOME%/batch\lib\unifaceflowlauncher-1.0.jar;%ANT_HOME%/batch\lib\uniface_flow-1.0.jar;%ANT_HOME%\batch\lib\unet-1.0.jar;%ANT_HOME%\batch\lib\log4j-1.2.14.jar;%ANT_HOME%\lib\ant-launcher-1.6.5.jar;%ANT_HOME%\lib\ant-1.6.5.jar"
java -cp %CLASSPATH%  -Xmx1024m org.apache.tools.ant.Main -buildfile %ANT_HOME%/batch/buildAnalyse.xml

