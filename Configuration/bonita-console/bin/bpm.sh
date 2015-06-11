#!/bin/sh

PRGDIR=`dirname $0` 

# Computes the absolute path of the current installation
cd ${PRGDIR}

# Set the CATALINA_HOME and CATALINA_BASE to the current tomcat 
CATALINA_HOME=${PRGDIR}/..
CATALINA_BASE=${PRGDIR}/..

# Set the bonita environment file
BONITA_OPTS="-DBONITA_HOME=${CATALINA_HOME}/bonita"
LOG_OPTS="-Djava.util.logging.config.file=${CATALINA_HOME}/conf/logging.properties"

# Sets some variables
SECURITY_OPTS="-Djava.security.auth.login.config=${CATALINA_HOME}/conf/jaas.conf"

# CAQS specific parameters
CAQS_OPTS="-Xshare:auto -XX:MaxPermSize=256m -Xms512m -Xmx1024m -XX:+HeapDumpOnOutOfMemoryError -Duser.language=en -Dfile.encoding=UTF-8 -Djava.awt.headless=true -Dh2.bindAddress=127.0.0.1"

# dynatrace agent settings
# DT_OPTS=-agentpath:/opt/dynaTrace/dynaTrace_4.0.0/agent/lib64/dtagent.so=name=caqs,server=localhost

JAVA_OPTS="$DT_OPTS $CAQS_OPTS $LOG_OPTS $SECURITY_OPTS $BONITA_OPTS"
export JAVA_OPTS

export JPDA_TRANSPORT=dt_socket
export JPDA_ADDRESS=8000

# Launches the server
exec ${PRGDIR}/catalina.sh "$@"
