@echo off

set ANT_HOME=..\..
set ANT_CP=%ANT_HOME%\lib\ant-launcher.jar;%ANT_HOME%\lib\ant.jar;..\lib\jdbcimporter.jar;..\lib\jdbcimporter-samples.jar;..\lib\jdbcimporter-anttask.jar;..\lib\commons-logging.jar;D:\oracle\product\10.2.0\client_1\jdbc\lib\ojdbc14.jar
java -Xmx1024m -classpath "%ANT_CP%" "-Dant.home=%ANT_HOME%" org.apache.tools.ant.Main %*

