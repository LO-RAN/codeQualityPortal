@echo off

set ANT_HOME=..
set ANT_CP=%ANT_HOME%\ant-launcher-1.6.5.jar;%ANT_HOME%\ant-1.6.5.jar;
java -Xmx1024m -classpath "%ANT_CP%" "-Dant.home=%ANT_HOME%" -DSRC_DIR=D:\TestPhoenix\TestAnt -Dlib_elt=Toto org.apache.tools.ant.Main %*

