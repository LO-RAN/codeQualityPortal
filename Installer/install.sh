#!/bin/sh
# -- Compuware CAQS for unix installation program
# -- created by laurent.izac@compuware.com on december 2, 2005

# -- set classpath -------------------
CLASSPATH=./installlib/xercesImpl.jar:./installlib/xml-apis.jar:./installlib/ant-installer.jar:./installlib/tools.jar
CLASSPATH=$CLASSPATH:./installlib/ant.jar:./installlib/ant-launcher.jar
CLASSPATH=$CLASSPATH:./installclasspath
CLASSPATH=$CLASSPATH:./installlib/jgoodies-edited-1_2_2.jar
CLASSPATH=$CLASSPATH:./installlib/sysout.jar
CLASSPATH=$CLASSPATH:./installlib/h2-1.2.139.jar
CLASSPATH=$CLASSPATH:./installlib/jtds-1.2.2.jar
CLASSPATH=$CLASSPATH:./installlib/oracle-jdbc-1.4.jar
#-------------------------------------

# -- check whether install should start in character or graphical mode ---------------
# -- we check the existence of a DISPLAY environment variable
# -- (maybe should we use a more robust condition)
if [ -n "$DISPLAY" ]
then
	echo installing in graphical mode
	INTERFACE=swing
else
	echo installing in text mode
	INTERFACE=text
fi

# ------------------------------------------------------------------------------------

java -classpath $CLASSPATH org.tp23.antinstaller.runtime.ExecInstall $INTERFACE .

