@echo off
java -cp ./lib/ant.jar;./lib/ant-launcher.jar;C:\j2sdk1.4.2_03\lib\tools.jar -Dant.home=. org.apache.tools.ant.Main -buildfile scripts/save/build.xml -logger org.apache.tools.ant.listener.MailLogger %1
