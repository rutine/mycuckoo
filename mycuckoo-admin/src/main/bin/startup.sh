#!/bin/bash

CURRENT_PATH=`pwd`
case "`uname`" in
  Linux)
    BIN_PATH=$(readlink -f $(dirname $0))
    ;;
  *)
    BIN_PATH=`cd $(dirname $0); pwd`
    ;;
esac
BASE_PATH=${BIN_PATH}/..
CONF_PATH=$BASE_PATH/config
export LANG=en_US.UTF-8
export BASE=$BASE_PATH

if [ -f $BASE_PATH/bin/cuckoo.pid ] ; then
  echo "found cuckoo.pid, Please run stop.sh first, then startup.sh" 2>&2
  exit 1
fi

if [ ! -d $BASE_PATH/logs ] ; then
  mkdir -p $BASE_PATH/logs
fi

## set java path
if [ -z "$JAVA" ] ; then
  JAVA=$(which java)
fi

ALIBABA_JAVA="/usr/alibaba/java/bin/java"
TAOBAO_JAVA="/opt/taobao/java/bin/java"
if [ -z "$JAVA" ] ; then
  if [ -f $ALIBABA_JAVA ] ; then
    JAVA=$ALIBABA_JAVA
  elif [ -f $TAOBAO_JAVA ] ; then
    JAVA=$TAOBAO_JAVA
  else
    echo "Cannot find a Java JDK. Please set either set JAVA or put java (>=1.6) in your PATH." 2>&2
    exit 1
  fi
fi

case "$#" in
  0 )
    ;;
  1 )
    var=$*
    if [ -f $var ] ; then
      CONF_PATH=$var
    else
      echo "THE PARAMETER IS NOT CORRECT.PLEASE CHECK AGAIN."
      exit
    fi;;
  2 )
    var=$1
    if [ -f $var ] ; then
      CONF_PATH=$var
    else
      if [ "$1" = "debug" ] ; then
        DEBUG_PORT=$2
        DEBUG_SUSPEND="n"
        JAVA_DEBUG_OPT="-Xdebug -Xnoagent -Djava.compiler=NONE -Xrunjdwp:transport=dt_socket,address=$DEBUG_PORT,server=y,suspend=$DEBUG_SUSPEND"
      fi
    fi;;
  * )
    echo "THE PARAMETERS MUST BE TWO OR LESS.PLEASE CHECK AGAIN."
    exit;;
esac

str=`file -L $JAVA | grep 64-bit`
if [ -n "$str" ]; then
  JAVA_OPTS="-server -Xms256m -Xmx512m -Xmn256m -XX:SurvivorRatio=2 -XX:MetaspaceSize=96m -XX:MaxMetaspaceSize=256m -Xss256k -XX:-UseAdaptiveSizePolicy -XX:MaxTenuringThreshold=15 -XX:+DisableExplicitGC -XX:+UseConcMarkSweepGC -XX:+CMSParallelRemarkEnabled -XX:+UseCMSCompactAtFullCollection -XX:+UseFastAccessorMethods -XX:+UseCMSInitiatingOccupancyOnly -XX:+HeapDumpOnOutOfMemoryError"
else
  JAVA_OPTS="-server -Xms1024m -Xmx1024m -XX:NewSize=256m -XX:MaxNewSize=256m -XX:MaxMetaspaceSize=128m "
fi

JAVA_OPTS=" $JAVA_OPTS -Djava.awt.headless=true -Djava.net.preferIPv4Stack=true -Dfile.encoding=UTF-8"
MYCUCKOO_OPTS="-Dmycuckoo.config.location=$CONF_PATH -Dspring.config.location=$CONF_PATH\application.yml"

if [ -e $CONF_PATH ] ; then
  CLASSPATH="$CONF_PATH:$CLASSPATH";

  echo "cd to $BIN_PATH for workaround relative path"
  cd $BIN_PATH

  echo mycuckoo config : $CONF_PATH
  echo CLASSPATH : $CLASSPATH
  $JAVA $JAVA_OPTS $JAVA_DEBUG_OPT $MYCUCKOO_OPTS -classpath .:$CLASSPATH -jar mycuckoo-web.jar 1>>$BASE_PATH/logs/std.out 2>&1 &
  echo $! > $BASE_PATH/bin/cuckoo.pid

  echo "cd to $CURRENT_PATH for continue"
  cd $CURRENT_PATH
else 
  echo "mycuckoo config("$CONF_PATH") is not exist, please create then first!"
fi
