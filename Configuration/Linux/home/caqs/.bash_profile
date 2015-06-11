# .bash_profile

# Get the aliases and functions
if [ -f ~/.bashrc ]; then
    . ~/.bashrc
fi

# User specific environement and startup programs

PATH=$PATH:$HOME/bin

export PATH
unset USERNAME

# ---- BEGIN : Compuware CAQS specific settings --------------------------------

# Java
export JAVA_HOME=/opt/Compuware/jdk1.5.0_12
export LD_LIBRARY_PATH=$LD_LIBRARY_PATH:$JAVA_HOME/jre/lib/i386/server:$JAVA_HOME/jre/lib/i386

export CPWR_HOME=/opt/Compuware
export LD_LIBRARY_PATH=$LD_LIBRARY_PATH:$CPWR_HOME/UnifaceView/application/3gl:$CPWR_HOME/lmgrd/cpwr_4.0/Linux/32:$CPWR_HOME/VantageAnalyzer/agent/linux

export PATH=$JAVA_HOME/bin:$PATH

export ADVISOR_HOME=$CPWR_HOME/OptimalAdvisor
export MF_ADVISOR_HOME=/opt/microfocus/DPJ

# use Xvfb for display (concerns batch invocation of OptimalAdvisor with JDK 1.4 and OpenOffice)
# cf. /etc/rc.d/init.d/xvfb script
export DISPLAY=localhost:5.0

. $CPWR_HOME/UnifaceFlow/adm/insunis

# Oracle
export ORAENV_ASK=NO
export ORACLE_SID=CAQS
export ORACLE_HOME=/logiciel/oracle/client_10203
export ORAHOME=$ORACLE_HOME
export PATH=$ORACLE_HOME/bin:$PATH
export LD_LIBRARY_PATH=$LD_LIBRARY_PATH:$ORACLE_HOME/lib

# splint
export LARCH_PATH=/usr/local/share/splint/lib
export LLCLIMPORTDIR=/usr/local/share/splint/include

# ---- END : Compuware CAQS specific settings ----------------------------------
